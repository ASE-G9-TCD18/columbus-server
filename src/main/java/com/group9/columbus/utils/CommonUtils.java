package com.group9.columbus.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
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
	
	public String getLoggedInUserLoginId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginId = (String)auth.getPrincipal();
		return loginId;
	}
	
}
