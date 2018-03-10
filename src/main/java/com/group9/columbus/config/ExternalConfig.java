package com.group9.columbus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExternalConfig {
	
	@Value("${config.fcm.key}")
	private String fcmKey;
	
	@Value("${config.fcm.url}")
	private String fcmUrl;

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
}
