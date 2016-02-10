package com.dwl.resources;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.couchbase.client.java.document.json.JsonArray;
import com.dwl.GlobalMartConfiguration;
import com.dwl.services.ProductService;
import com.dwl.services.ProductServiceImpl;

@RunWith(PowerMockRunner.class)
public class ProductResourceTest{
	
	@Mock
	ProductService productService;
	
	@InjectMocks
	ProductResource productResource;
	
	String[] data={"server","local.yml"};
	
	@Before
	public void init(){
		ProductServiceImpl productService = Mockito.mock(ProductServiceImpl.class);
		try {
			PowerMockito.whenNew(ProductServiceImpl.class).withArguments(Mockito.isNull()).thenReturn(productService);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void getProduct(){
		
		Response rs =productResource.getProducts("test");
		Assert.assertEquals(rs.getStatus(), 404);
	}
	
	/*@Test
	public void getProduct1(){
		JsonArray documentArray=JsonArray.create();
		documentArray.add(true);
		Mockito.when(productService.getProducts("test")).thenReturn(documentArray);
		Response rs =productResource.getProducts("test");
		Assert.assertEquals(rs.getStatus(), 200);
	}*/
	
}
