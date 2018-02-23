package com.group9.columbus.controller;

import com.group9.columbus.entity.ApplicationUser;
import com.group9.columbus.exception.UserManagementException;
import com.group9.columbus.service.UserManagementService;
import com.group9.columbus.utils.CommonUtils;
import com.group9.columbus.utils.JsonUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user/{loginId}")
public class UserManagementController {

	final static Logger logger = Logger.getLogger(UserManagementController.class);
	@Autowired
	UserManagementService userMgmtSvc;

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getResponse(@PathVariable("loginId") String loginId) {
		ApplicationUser user = userMgmtSvc.findUserByUsername(loginId);

		return JsonUtils.getJsonForResponse(user);
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> editUser(@PathVariable("loginId") String loginId,
			@Validated @RequestBody ApplicationUser user) {

		try {
			user = userMgmtSvc.editUser(loginId, user);
		} catch (UserManagementException e) {
			logger.error(e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(CommonUtils.createErrorResponseMessage(e.getMessage()));
		}
		return JsonUtils.getJsonForResponse(user);
	}
}