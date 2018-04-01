package com.group9.columbus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group9.columbus.entity.Conversation;
import com.group9.columbus.repository.ConversationRepository;

/**
 * Service class for {@link Conversation}'s.
 * @author amit
 */
@Service
public class ConversationService {

	@Autowired
	ConversationRepository convRepo;
	
	/**
	 * Instantiates a new conversation in the Database.
	 * @return
	 */
	public Conversation createConversation() {
		Conversation conv = convRepo.save(new Conversation());
		return conv;
	}
}
