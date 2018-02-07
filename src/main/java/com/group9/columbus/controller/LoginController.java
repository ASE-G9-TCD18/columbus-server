package com.group9.columbus.controller;


import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.group9.columbus.dto.UserDto;
import com.group9.columbus.entity.UserPrincipal;
import com.group9.columbus.utils.JsonUtils;


@RestController
@RequestMapping(value = "/login")
public class LoginController {
	final static Logger logger = Logger.getLogger(LoginController.class);
	
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> login() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		try {
			UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
			
			return JsonUtils.getJsonForResponse(new UserDto(userPrincipal.getUser()));
		} catch (Exception e) {
			logger.error("",e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please check your payload format.");
		}
	}
}
