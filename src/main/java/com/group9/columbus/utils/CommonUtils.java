package com.group9.columbus.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class CommonUtils {

	public static String createResponseMessage(String message) {
		JsonObject obj = new JsonObject();
		obj.addProperty("message", message);
		return new Gson().toJson(obj);
	}

	public static String createErrorResponseMessage(String message) {
		JsonObject obj = new JsonObject();
		obj.addProperty("error", message);
		return new Gson().toJson(obj);
	}
}
