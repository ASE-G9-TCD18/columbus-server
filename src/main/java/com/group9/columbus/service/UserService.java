package com.group9.columbus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group9.columbus.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
}
