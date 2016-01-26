package com.dwl.utils;

import java.util.UUID;

import com.couchbase.client.java.document.json.JsonObject;

public class Utils {

	public static String getUUID(){
		return UUID.randomUUID().toString();
	}
	
	public static JsonObject getProductFromJson(String strJson){
		return JsonObject.fromJson(strJson);
	}
}
