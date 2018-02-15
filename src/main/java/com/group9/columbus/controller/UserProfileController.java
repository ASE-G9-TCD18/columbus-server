package com.group9.columbus.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.group9.columbus.dto.UserDto;
import com.group9.columbus.entity.User;
import com.group9.columbus.exception.LoginIdChangedException;
import com.group9.columbus.service.UserManagementService;
import com.group9.columbus.utils.CommonUtils;
import com.group9.columbus.utils.JsonUtils;

@RestController
@RequestMapping(value = "/user/profile/")
public class UserProfileController {

	final static Logger logger = Logger.getLogger(UserProfileController.class);
	
	@Autowired
	UserManagementService userMgmtSvc;
	
	@RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> editUser(@Validated @RequestBody User user) {
		
		try {
			user = userMgmtSvc.editUser(user);
		} catch (LoginIdChangedException e) {
			logger.error(e);
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(CommonUtils.createErrorResponseMessage(e.getMessage()));
		}
		
		return JsonUtils.getJsonForResponse(new UserDto(user));
	}
	
	
}
