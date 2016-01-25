package com.springboot.resources;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.DocumentAlreadyExistsException;
import com.springboot.CouchbaseService;

@RestController
@RequestMapping("/productprices")
public class ProductPrice {

	private final CouchbaseService couchbaseService;

    @Autowired
    public ProductPrice(CouchbaseService couchbaseService) {
    	this.couchbaseService = couchbaseService;
	}
	
    @RequestMapping(method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addPrice(@RequestBody Map<String, String> priceData){
		 String id = "";
	        try {
	            JsonObject productPriceData = parsePrice(priceData);
	            id = "productPrice"+ productPriceData.getString("id");
	            JsonDocument doc = CouchbaseService.createDocument(id, productPriceData);
	            couchbaseService.create(doc);
	            return new ResponseEntity<String>(id, HttpStatus.CREATED);
	        } catch (IllegalArgumentException e) {
	            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	        } catch (DocumentAlreadyExistsException e) {
	            return new ResponseEntity<String>("Id " + id + " already exist", HttpStatus.CONFLICT);
	        } catch (Exception e) {
	            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	}
	
	private JsonObject parsePrice(Map<String, String> priceData) {
        String id = (String) priceData.get("id");
        String price = (String) priceData.get("price");
        if (id == null || price == null || id.isEmpty() || price.isEmpty()) {
           throw new IllegalArgumentException();
        } else {
            JsonObject priceObj = JsonObject.create();
            for (Map.Entry<String, String> entry : priceData.entrySet()) {
            	priceObj.put(entry.getKey(), entry.getValue());
            }
            return priceObj;
        }
    }
	
	@RequestMapping(method=RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getPrice(@PathVariable String id){
		id = "productPrice"+ id;
		   JsonDocument doc = couchbaseService.read(id);
	        if (doc != null) {
	            return new ResponseEntity<String>(doc.content().toString(), HttpStatus.OK);
	        } else {
	            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
	        }
	}
}
