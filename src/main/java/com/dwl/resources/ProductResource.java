package com.dwl.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

	@GET
	@Path("/{id}")
	public Response getProductById(@PathParam("id") int productId){
		// retrieve information about the reward with the provided id
				return Response.ok(
						"{\"points_to_redeem\": 20, \"image_url\": \"http://image.com\", \"id\": "
								+ productId
								+ ",\"redeemable\": true, \"name\": \"Dinesh S\" }")
						.build();
	}
}
