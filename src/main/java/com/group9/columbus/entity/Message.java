package com.group9.columbus.entity;

import org.springframework.data.annotation.Id;

public class Message {

	@Id
	private Long id;
	
	private Boolean dispatched;
	
	private String textMessage;
	
	private ApplicationUser sender;
}
