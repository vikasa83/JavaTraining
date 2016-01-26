package com.dwl.resources;

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

import org.eclipse.jetty.http.HttpStatus;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.dwl.services.ProductService;
import com.dwl.services.ProductServiceImpl;
import com.dwl.utils.Utils;
import com.google.inject.Inject;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {
	
//	@Inject
	ProductService productService;
	
	public ProductResource() {
		productService = new ProductServiceImpl();
	}
	
	@GET
	public Response getProducts(@QueryParam("productType")String productType) {
		return Response.ok(productService.getProducts(productType).toString()).build();
	}

	@GET
	@Path("/{productId}")
	public Response getProductById(@PathParam("productId") String productId) {
		System.out.println("productService : "+productService);
		return Response.ok(productService.getProduct(productId).content().toString()).build();
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
		JsonObject productObject = Utils.getProductFromJson(productJsonString);
		String productId = Utils.getUUID();
		productService.addProduct(productId, productObject);
		return Response.status(HttpStatus.CREATED_201).build();
	}
	
}
