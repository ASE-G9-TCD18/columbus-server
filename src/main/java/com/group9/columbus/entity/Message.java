package com.group9.columbus.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class Message {

	@Id
	private Long id;
	
	private Boolean dispatched;
	
	@DBRef
	private ApplicationUser sender;

	private String textMessage;

	public Message(Boolean dispatched, ApplicationUser sender, String textMessage) {
		super();
		this.dispatched = dispatched;
		this.sender = sender;
		this.textMessage = textMessage;
	}

	public Long getId() {
		return id;
	}

	public Boolean getDispatched() {
		return dispatched;
	}

	public ApplicationUser getSender() {
		return sender;
	}

	public String getTextMessage() {
		return textMessage;
	}
	
}
