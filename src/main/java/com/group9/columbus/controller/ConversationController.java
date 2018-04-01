package com.group9.columbus.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.group9.columbus.entity.Conversation;
import com.group9.columbus.entity.Message;

/**
 * Container class for {@link Conversation} APIs.
 * @author amit
 */
@RestController
@RequestMapping(value = "/converstation/{conversationId}")
public class ConversationController {
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> sendMessage(@PathVariable("conversationId") String conversationId, @Validated @RequestBody Message message) {
		
		return null;
	}

}
