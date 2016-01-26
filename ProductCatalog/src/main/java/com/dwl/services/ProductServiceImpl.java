package com.dwl.services;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.view.ViewQuery;
import com.couchbase.client.java.view.ViewResult;
import com.couchbase.client.java.view.ViewRow;
import com.dwl.GlobalMartConfiguration;

public class ProductServiceImpl implements ProductService{
	
//	@Inject
	GlobalMartConfiguration config;
	CouchbaseCluster cluster;
	Bucket bucket;
	
	public ProductServiceImpl() {
		cluster = CouchbaseCluster.create("127.0.0.1");
		bucket = cluster.openBucket("products");
	}
	
	public JsonArray getProducts(String productType){
		JsonArray keys = JsonArray.create();
		ViewResult result = bucket.query(ViewQuery.from("dev_by_type", "by_type").key(productType));
		for (ViewRow row : result) {
		    JsonDocument doc = row.document();

		    keys.add(doc.content());
		}
		return keys;
	}
	
	public JsonDocument getProduct(String productId){
		JsonDocument found = bucket.get(productId);
		return found;
	}
	
	public JsonDocument addProduct(String productId, JsonObject product){
		return bucket.upsert(JsonDocument.create(productId, product));
	}
	
	public JsonDocument deleteProduct(String productId){
		return bucket.remove(productId);
	}
	
	public JsonDocument updateProduct(JsonDocument product){
		return bucket.replace(product);
	}

}
