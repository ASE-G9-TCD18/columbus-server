package com.group9.columbus.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group9.columbus.dto.UserDto;

@Document(collection = "user")
public class ApplicationUser {

	@Id
	private String id;

	@Pattern(regexp = "[A-Za-z0-9]{4,}", message = "LoginId can contain only alphanumeric characters."
			+ " Minimum length 4 and maximum 10.")
	@NotNull(message = "Login Id cannot be left null.")
	private String loginId;

	@Pattern(regexp = "(?=.[a-z]{8,14})", message = "Password should be of minimum length 8 and max 14.")
	// @NotNull(message="Password cannot be left null.")
	private String password;

	@Pattern(regexp = "[a-zA-z]+", message = "Please enter a valid name.")
	@NotNull(message = "First Name cannot be left null.")
	private String firstName;

	@Pattern(regexp = "[a-zA-z]*", message = "Please enter a valid name.")
	private String lastName;

	@Pattern(regexp = "[0-9]{1,2}", message = "Age can contain only numbers.")
	@NotNull(message = "Age cannot be left null.")
	private Integer age;

	@Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")
	@NotNull(message = "Email Id cannot be left null.")
	private String emailId;

	@Pattern(regexp = "[0-9]{10}", message = "Contact number should of lenght 10.")
	@NotNull(message = "Contact number cannot be left null.")
	private String contactNumber;

	@NotNull
	private Double userRating;

	private boolean isActive;

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

	@JsonIgnore
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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Double getUserRating() {
		return userRating;
	}

	public void setUserRating(Double userRating) {
		this.userRating = userRating;
	}

	public ApplicationUser() {

	}

	public ApplicationUser(String loginId, String password, String firstName, String lastName, Integer age,
			String emailId, String contactNumber) {
		this.loginId = loginId;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.emailId = emailId;
		this.contactNumber = contactNumber;
	}

	/**
	 * Helper method to set details from the UserDto to the ApplicationUser.
	 * Required because the client might not send the password and other security details. If used directly,
	 * it might override the user details in the database.
	 * @param user
	 */
	@JsonIgnore
	public void setApplicationUserDetails(UserDto user) {
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.age = user.getAge();
		this.emailId = user.getEmailId();
		this.contactNumber = user.getContactNumber();
		this.userRating = user.getUserRating();
	}
	
	@JsonIgnore
	public void setApplicationUserDetails(ApplicationUser user) {
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.age = user.getAge();
		this.emailId = user.getEmailId();
		this.contactNumber = user.getContactNumber();
		this.userRating = user.getUserRating();
	}

	@Override
	public String toString() {
		return String.format(
				"User[id=%s, loginId= '%s', isActive='%s'," + " firstName='%s', lastName='%s', " + "emailid='%s']", id,
				loginId, isActive, firstName, lastName, emailId);

	}

}
