package com.dwl.services;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.view.AsyncViewResult;

import rx.Observable;

public interface ProductService {
	public JsonArray getProducts(String productType);

	public JsonDocument getProduct(String productId);

	public JsonDocument addProduct(String productId, JsonObject product);

	public JsonDocument deleteProduct(String productId);

	public JsonDocument updateProduct(JsonDocument product);
	
	public Observable<JsonDocument> addProductAsync(String productId, JsonObject product);
	
	public Observable<AsyncViewResult> getProducts(int offset, int limit);
}
