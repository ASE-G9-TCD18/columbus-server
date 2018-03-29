package com.group9.columbus.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group9.columbus.dto.TripJoinRequestDto;
import com.group9.columbus.dto.UserDto;

@Document(collection = "user")
public class ApplicationUser {

	@Id
	private String id;

	@NotNull(message = "Login Id cannot be left null.")
	@Indexed(unique = true)
	private String loginId;

	@NotNull(message = "Password cannot be left null.")
	private String password;

	@NotNull(message = "First Name cannot be left null.")
	private String firstName;

	private String lastName;

	@NotNull(message = "Age cannot be left null.")
	private String age;

	@NotNull(message = "Email Id cannot be left null.")
	private String emailId;

	@NotNull(message = "Contact number cannot be left null.")
	private String contactNumber;

	@NotNull(message = "User rating cannot be left null.")
	private Double userRating;

	private String deviceId;

	private boolean active;

	@DBRef(lazy = true)
	private List<Trip> trips;

	@DBRef(lazy = true)
	private List<Trip> tripsRequestsMade;

	private List<TripJoinRequestDto> tripsRequestsAwaitingConfirmation;

	private Double tripsTillNow;

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

	// @JsonIgnore
	public String getPassword() {
		return password;
	}

	// @JsonProperty
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

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
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
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Double getUserRating() {
		return userRating;
	}

	public void setUserRating(Double userRating) {
		this.userRating = userRating;
	}

	public List<Trip> getTrips() {
		return trips;
	}

	public void setTrips(List<Trip> trips) {
		this.trips = trips;
	}

	public List<Trip> getTripsRequestsMade() {
		return tripsRequestsMade;
	}

	public void setTripsRequestsMade(List<Trip> tripsRequestsMade) {
		this.tripsRequestsMade = tripsRequestsMade;
	}

	public List<TripJoinRequestDto> getTripsRequestsAwaitingConfirmation() {
		return tripsRequestsAwaitingConfirmation;
	}

	public void setTripsRequestsAwaitingConfirmation(List<TripJoinRequestDto> tripsRequestsAwaitingConfirmation) {
		this.tripsRequestsAwaitingConfirmation = tripsRequestsAwaitingConfirmation;
	}

	public Double getTripsTillNow() {
		return tripsTillNow;
	}

	public void setTripsTillNow(Double tripsTillNow) {
		this.tripsTillNow = tripsTillNow;
	}

	public ApplicationUser() {

	}

	public ApplicationUser(String loginId, String password, String firstName, String lastName, String age,
			String emailId, String contactNumber, Double userRating, boolean active, String deviceId) {
		super();
		this.loginId = loginId;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.emailId = emailId;
		this.contactNumber = contactNumber;
		this.userRating = userRating;
		this.active = active;
		this.deviceId = deviceId;
	}

	/**
	 * Helper method to set details from the UserDto to the ApplicationUser.
	 * Required because the client might not send the password and other security
	 * details. If used directly, it might override the user details in the
	 * database.
	 * 
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

	/**
	 * Helper method to enable deep copy of non-sensitive {@link ApplicationUser}
	 * details.
	 * 
	 * @param user
	 */
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
				loginId, active, firstName, lastName, emailId);

	}

	/**
	 * Returns a deep copy of {@link ApplicationUser}.
	 * 
	 * @return
	 */
	@JsonIgnore
	public ApplicationUser getCopy() {
		ApplicationUser user = new ApplicationUser();
		user.id = user.id;
		user.loginId = loginId;
		user.password = password;
		user.firstName = firstName;
		user.lastName = lastName;
		user.age = age;
		user.emailId = emailId;
		user.contactNumber = contactNumber;
		user.userRating = userRating;
		user.active = active;

		return user;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}
