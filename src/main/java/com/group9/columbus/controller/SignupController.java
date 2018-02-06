package com.group9.columbus.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.group9.columbus.entity.User;
import com.group9.columbus.service.UserManagementService;

@RestController
@RequestMapping(value = "/signup")
public class SignupController {

	final static Logger logger = Logger.getLogger(SignupController.class);
	
	@Autowired
	UserManagementService userMgmtSvc;

	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> login(@Validated @RequestBody User user) {

		userMgmtSvc.saveNewUser(user);
		
		return null;
	}
}
