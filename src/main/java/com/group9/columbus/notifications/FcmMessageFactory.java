package com.group9.columbus.notifications;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.group9.columbus.notifications.FcmMessage.Notification;

public class FcmMessageFactory {
	private static Gson gson = new Gson();

	public static String buildFcmMessage(String fcmToken, String title, String body, JsonObject data) throws Exception {
		FcmMessage fcmMessage = new FcmMessage();

		if (fcmToken == null) {
			return null;
		}

		fcmMessage.setTo(fcmToken);
		Notification notification = fcmMessage.new Notification();
		notification.setBody(body);
		notification.setTitle(title);
		fcmMessage.setNotification(notification);
		fcmMessage.setData(data);
		
		return gson.toJson(fcmMessage);
	}
}

