package com.dwl.services;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

public interface ProductService {
	public JsonArray getProducts(String productType);

	public JsonDocument getProduct(String productId);

	public JsonDocument addProduct(String productId, JsonObject product);

	public JsonDocument deleteProduct(String productId);

	public JsonDocument updateProduct(JsonDocument product);
}
