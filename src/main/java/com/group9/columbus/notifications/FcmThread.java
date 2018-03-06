package com.group9.columbus.notifications;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class FcmThread extends Thread {
	private Logger logger = Logger.getLogger(FcmThread.class);
	private String fcmMessage;

	public FcmThread() {
		
	}
	public FcmThread(String fcmMessage) {
		this.fcmMessage = fcmMessage;
	}

	public String getFcmMessage() {
		return fcmMessage;
	}
	
	public void setFcmMessage(String fcmMessage) {
		this.fcmMessage = fcmMessage;
	}

	@Override
	public void run() {
		String fcmServerKey = "";
		CloseableHttpResponse response = null;
		Integer errorCode = null;

		try(CloseableHttpClient httpclient = HttpClients.createDefault()) {
			URI uri = new URI("FCM URL");
			HttpPost httpPost = new HttpPost(uri);

			httpPost.setEntity(new StringEntity(fcmMessage, ContentType.create("application/json")));
			httpPost.setHeader("Authorization", "key=" + fcmServerKey);

			response = httpclient.execute(httpPost);
			errorCode = response.getStatusLine().getStatusCode();
		} catch (URISyntaxException | IOException e) {
			logger.error("",e);
		}  finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					logger.error("",e);
				}
			}
		}

		if (errorCode != 200) {
			logger.error("Failed to send FCM Message");
			logger.error("Failed to send FCM Message Reason :: " + response.getStatusLine().getReasonPhrase());
		} else {
			logger.info("FCM message sent");
		}
	}

}

