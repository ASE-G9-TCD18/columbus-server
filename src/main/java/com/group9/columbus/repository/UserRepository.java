package com.group9.columbus.repository;

import com.group9.columbus.entity.ApplicationUser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<ApplicationUser, String> {

	public ApplicationUser findByLoginId(String loginId);
	
	public List<ApplicationUser> findByLoginIdIn(List<String> loginIds);
	
}
