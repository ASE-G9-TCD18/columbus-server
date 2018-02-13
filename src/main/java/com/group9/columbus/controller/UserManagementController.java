package com.group9.columbus.controller;

import com.group9.columbus.dto.UserDto;
import com.group9.columbus.service.UserManagementService;
import com.group9.columbus.utils.JsonUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/user")
public class UserManagementController {

	final static Logger logger = Logger.getLogger(UserManagementController.class);
	@Autowired
	UserManagementService userMgmtSvc;

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getResponse() {
		String success = "ok, 200.";
		return  JsonUtils.getJsonForResponse(success);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getResponse(@PathVariable("id") String userId) {
        UserDto userDetails = userMgmtSvc.findUserByUsername(userId);

		return  JsonUtils.getJsonForResponse(userDetails);
	}
}