package com.group9.columbus.controller;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.group9.columbus.entity.User;

@RestController
@RequestMapping(value = "")
public class UserManagementController {

	final static Logger logger = Logger.getLogger(UserManagementController.class);
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> login(@RequestBody User user ) {
		
		
		return null;
	}
}
