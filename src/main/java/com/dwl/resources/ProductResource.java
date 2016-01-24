package com.dwl.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;

import com.couchbase.client.java.document.JsonDocument;
import com.dwl.services.ProductServiceImpl;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {
	
	ProductServiceImpl productServiceImpl;
	
	public ProductResource() {
		productServiceImpl = new ProductServiceImpl();
	}
	
	@GET
	public Response getProducts() {
		return Response.ok(productServiceImpl.getProducts().toString()).build();
	}

	@GET
	@Path("/{productId}")
	public Response getProductById(@PathParam("productId") String productId) {
		return Response.ok(productServiceImpl.getProduct(productId).content().toString()).build();
	}
	
	@DELETE
	@Path("/{productId}")
	public Response deleteProduct(@PathParam("productId") String productId){
		JsonDocument document = productServiceImpl.deleteProduct(productId);
		
		if(document != null){
			return Response.status(HttpStatus.NO_CONTENT_204).build();
		}
		else{
			return Response.status(HttpStatus.NOT_FOUND_404).build();
		}
	}
	
	@POST
	public Response createProduct(){
		return Response.status(HttpStatus.CREATED_201).build();
	}
	
}
