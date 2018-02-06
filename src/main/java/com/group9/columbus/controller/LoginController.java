package com.group9.columbus.controller;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/v1/login")
public class LoginController {
	final static Logger logger = Logger.getLogger(LoginController.class);
	
//	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
//	public ResponseEntity<String> login(@RequestBody Map<String, String> rawpayload) {
//		String loginId = null;
//		String password = null;
//
//		try {
//			loginId = rawpayload.get("loginId");
//			password = rawpayload.get("password");
//		} catch (Exception e) {
//			logger.error("",e);
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please check your payload format.");
//		}
//	}
}
