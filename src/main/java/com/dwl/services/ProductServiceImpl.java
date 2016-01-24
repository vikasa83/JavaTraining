package com.dwl.services;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;

public class ProductServiceImpl {
	
	CouchbaseCluster cluster;
	Bucket bucket;
	
	public ProductServiceImpl() {
		cluster = CouchbaseCluster.create("127.0.0.1");
		bucket = cluster.openBucket("products");
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
