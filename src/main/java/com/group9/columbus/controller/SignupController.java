package com.group9.columbus.controller;

import com.group9.columbus.entity.ApplicationUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.group9.columbus.exception.UserExistsException;
import com.group9.columbus.service.UserManagementService;
import com.group9.columbus.utils.CommonUtils;
import com.group9.columbus.utils.JsonUtils;

/**
 * Container for Signup APIs.
 * @author amit
 */
@RestController
@RequestMapping(value = "/signup")
public class SignupController {

	final static Logger logger = Logger.getLogger(SignupController.class);
	
	@Autowired
	UserManagementService userMgmtSvc;

	/**
	 * Signup API
	 * @param ApplicationUser user
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> signUp(@Validated @RequestBody ApplicationUser user) {

		//UserDto userDto = null;
		ApplicationUser userDto = null;
		try {
			 userDto = userMgmtSvc.saveNewUser(user);
			 return JsonUtils.getJsonForResponse(userDto);
		} catch (UserExistsException e) {
			logger.error("User already exists.", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(CommonUtils.createErrorResponseMessage(e.getMessage()));
		}
		
	}
}