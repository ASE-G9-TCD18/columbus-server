package com.group9.columbus.dto;

import com.group9.columbus.entity.ApplicationUser;

/**
 * Helper class for returning User Objects to client.
 * Avoids serializing password.
 * @author amit
 */
public class UserDto {
	
	private String id;

	private String loginId;
	
	private String firstName;
	
	private String lastName;
	
	private Integer age;
	
	private String emailId;
	
	private String contactNumber;
	
	private Double userRating;

	public UserDto(ApplicationUser user) {
		this.id = user.getId();
		this.loginId = user.getLoginId();;
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.age = user.getAge();
		this.emailId = user.getEmailId();
		this.contactNumber = user.getEmailId();
		this.userRating = user.getUserRating();
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public Double getUserRating() {
		return userRating;
	}
	public void setUserRating(Double userRating) {
		this.userRating = userRating;
	}
	
}
