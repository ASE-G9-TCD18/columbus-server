package com.group9.columbus.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.group9.columbus.notifications.FcmMessageFactory;
import com.group9.columbus.notifications.FcmThread;

@Service
public class NotificationService {

	Logger logger = Logger.getLogger(NotificationService.class);

	@Autowired
	ApplicationContext applicationContext;

	/**
	 * Method that sends a new notification message to the Admin of trip.
	 * @param fcmToken
	 * @param requestFrom
	 * @return
	 */
	public boolean sendNewJoinRequestNotification(String fcmToken, String requestFrom) {

		String fcmMessage = null;
		String title = "Request to join Trip by " + requestFrom;
		String body = null;
		// Some logic to send notification
		try {
			fcmMessage = FcmMessageFactory.buildFcmMessage(fcmToken, title, body, null);

			if (fcmMessage == null) {
				logger.warn("Unable to send notification. Device Id Token not present.");
				return false;
			}
			FcmThread fcmThread = applicationContext.getBean(FcmThread.class, fcmMessage);
			// fcmThread.setFcmMessage(fcmMessage);
			fcmThread.start();
			logger.info("Notification: Request to join group from (" + requestFrom + ") sent successfully.");
		} catch (Exception e) {
			logger.error("Error sending notification requested by (" + requestFrom + ").", e);
			return false;
		}

		// If success then return true;
		return true;
	}

	/**
	 * Generic method to send a notification given a message and a deviceid.
	 * @param fcmToken
	 * @param message
	 * @return
	 */
	public boolean sendNotification(String fcmToken, String message) {

		String fcmMessage = null;
		String title = message;
		String body = null;
		// Some logic to send notification
		try {
			fcmMessage = FcmMessageFactory.buildFcmMessage(fcmToken, title, body, null);

			if (fcmMessage == null) {
				logger.warn("Unable to send notification. Device Id Token not present.");
				return false;
			}
			FcmThread fcmThread = applicationContext.getBean(FcmThread.class, fcmMessage);
			fcmThread.start();
			logger.info("Notification: with message (" + title + ") sent successfully.");
		} catch (Exception e) {
			logger.info("Error sending Notification: with message (" + title + ").");
			return false;
		}

		// If success then return true;
		return true;
	}
}
