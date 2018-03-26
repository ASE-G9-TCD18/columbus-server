package com.group9.columbus.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.group9.columbus.entity.Conversation;

public interface ConversationRepository extends MongoRepository<Conversation, String> {

}
