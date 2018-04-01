package com.group9.columbus.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utility class with helper methods.
 * @author amit
 */
@Component
public class CommonUtils {

	/**
	 * Helper method to create a response object given a string message.
	 * @param message
	 * @return
	 */
	public static String createResponseMessage(String message) {
		JsonObject obj = new JsonObject();
		obj.addProperty("message", message);
		return new Gson().toJson(obj);
	}

	/**
	 * Helper method to create a error response object given a string message.
	 * @param message
	 * @return
	 */
	public static String createErrorResponseMessage(String message) {
		JsonObject obj = new JsonObject();
		obj.addProperty("error", message);
		return new Gson().toJson(obj);
	}
	
	/**
	 * Helper method that returns the loginId of the current logged in user.
	 * @return
	 */
	public String getLoggedInUserLoginId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginId = (String)auth.getPrincipal();
		return loginId;
	}
	
}
