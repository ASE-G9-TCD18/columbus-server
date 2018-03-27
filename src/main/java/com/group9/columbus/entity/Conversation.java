package com.group9.columbus.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Conversation {
	@Id
	private String id;
	
	List<Message> messages;

}
