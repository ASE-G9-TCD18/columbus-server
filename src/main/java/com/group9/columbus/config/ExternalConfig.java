package com.group9.columbus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.google.maps.GeoApiContext;

@Component
public class ExternalConfig {
	
	@Value("${config.fcm.key}")
	private String fcmKey;
	
	@Value("${config.fcm.url}")
	private String fcmUrl;

	@Value("${config.geoapi.key}")
	private String geoApiKey;

	public String getFcmKey() {
		return fcmKey;
	}

	public void setFcmKey(String fcmKey) {
		this.fcmKey = fcmKey;
	}

	public String getFcmUrl() {
		return fcmUrl;
	}

	public void setFcmUrl(String fcmUrl) {
		this.fcmUrl = fcmUrl;
	}

	@Bean(name="geoApiContext")
	public GeoApiContext getGeoApiContext() {
		GeoApiContext context =  new GeoApiContext.Builder().apiKey(geoApiKey).build();
		return context;
	}
}
