package com.group9.columbus.entity;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {
	
	@Id
	private String id;

	@NotNull(message="Login Id cannot be left null.")
	private String loginId;
	
	@NotNull(message="Password cannot be left null.")
	private String password;
	
	//@NotNull(message="First Name cannot be left null.")
	private String firstName;
	
	private String lastName;
	
	//@NotNull(message="Age cannot be left null.")
	private Integer age;
	
	//@NotNull(message="Email Id cannot be left null.")
	private String emailId;
	
	//@NotNull(message="Contact number cannot be left null.")
	private String contactNumber;
	
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	@Override
	public String toString() {
		return String.format("User[id=%s, loginId= '%s'firstName='%s', lastName='%s', "
				+ "emailid='%s']", id, loginId, firstName, lastName, emailId);
		
	}
}
