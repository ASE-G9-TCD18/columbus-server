package com.group9.columbus.utils;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

	final static Logger logger = Logger.getLogger(JsonUtils.class);
	
	public static ResponseEntity<String> getJsonForResponse(Object object) {
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);

		String json = null;
		try {
			json = om.writeValueAsString(object);
			return ResponseEntity.status(HttpStatus.OK).body(json);
		} catch (JsonProcessingException e) {
			logger.error("", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(CommonUtils.createErrorResponseMessage(e.getMessage()));
		}
	}
}
