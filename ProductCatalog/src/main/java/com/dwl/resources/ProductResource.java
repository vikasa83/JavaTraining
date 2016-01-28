package com.dwl.resources;

import java.net.URI;
import java.util.concurrent.CountDownLatch;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.eclipse.jetty.http.HttpStatus;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.view.AsyncViewResult;
import com.dwl.GlobalMartConfiguration;
import com.dwl.services.ProductService;
import com.dwl.services.ProductServiceImpl;
import com.dwl.utils.Utils;

import rx.Observable;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {
	
	ProductService productService;
	
	public ProductResource(GlobalMartConfiguration config) {
		productService = new ProductServiceImpl(config);
	}
	
	@GET
	public Response getProducts(@QueryParam("productType")String productType) {
		
		JsonArray documentArray = productService.getProducts(productType);
		if(documentArray != null && !documentArray.isEmpty()){
			return Response.ok(documentArray.toString()).build();
		} else {
			return Response.status(HttpStatus.NOT_FOUND_404).build();
		}
		
	}

	@GET
	@Path("/{productId}")
	public Response getProductById(@PathParam("productId") String productId) {
		JsonDocument document = productService.getProduct(productId);
		if (document != null && !document.content().isEmpty()) {
			return Response.ok(document.content().toString()).build();
		} else {
			return Response.status(HttpStatus.NOT_FOUND_404).build();
		}

	}
	
	@DELETE
	@Path("/{productId}")
	public Response deleteProduct(@PathParam("productId") String productId){
		JsonDocument document = productService.deleteProduct(productId);
		
		if(document != null){
			return Response.status(HttpStatus.NO_CONTENT_204).build();
		}
		else{
			return Response.status(HttpStatus.NOT_FOUND_404).build();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createProduct(String productJsonString){
		try {

			JsonObject productObject = Utils.getProductFromJson(productJsonString);
			String productId = Utils.getUUID();
			productObject.put("productId", productId);
			// productService.addProduct(productId, productObject);
			// Observable<JsonDocument> observable =
			// productService.addProductAsync(productId, productObject);
			// observable.forEach(jsonDocument-> System.out.println("---------------Data persisted in couchbase  async manner"+jsonDocument.toString()));
			productService.addProductAsync(productId, productObject).toBlocking().single();
			ResponseBuilder created = Response.created(URI.create(productId));	
			return created.build();
		} catch (IllegalArgumentException e) {
			return Response.status(HttpStatus.BAD_REQUEST_400).build();
		} catch (Exception e) {
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).build();
		}
		
	}
	
	@GET
	@Path("/batch")
	public Response getProductsInBatch(@QueryParam("offset") int offset) {
		JsonArray array = JsonArray.create();
		final CountDownLatch latch = new CountDownLatch(10);
		Observable<AsyncViewResult> viewResult = productService.getProducts(offset, 10);
		viewResult.toBlocking().subscribe(onNext -> {
			onNext.rows().forEach(row -> {
				row.document().subscribe(jsonDocument -> {
					latch.countDown();
					array.add(jsonDocument.content());
				});
			});
			;
		});
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).build();
		}
		return Response.ok(array.toString()).build();
	}
	
}
