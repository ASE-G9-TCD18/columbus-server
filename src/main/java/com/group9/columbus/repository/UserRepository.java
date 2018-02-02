package com.group9.columbus.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.group9.columbus.entity.User;

public interface UserRepository extends MongoRepository<User, String> {

	public User findByLoginId(String loginId);
	
}
