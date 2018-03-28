package com.group9.columbus.service;

import com.group9.columbus.dto.TripJoinRequestDto;
import com.group9.columbus.entity.ApplicationUser;
import com.group9.columbus.entity.Trip;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.group9.columbus.exception.LoginIdChangedException;
import com.group9.columbus.exception.PasswordChangedException;
import com.group9.columbus.exception.UserExistsException;
import com.group9.columbus.exception.UserManagementException;
import com.group9.columbus.repository.UserRepository;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Service class required by Spring Security for authentication.
 * 
 * @author amit
 */
@Service
public class UserManagementService implements UserDetailsService {

	Logger logger = Logger.getLogger(UserManagementService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder encoder;

	public UserManagementService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#
	 * loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		ApplicationUser user = userRepository.findByLoginId(loginId);
		if (user == null) {
			throw new UsernameNotFoundException(loginId);
		}
		return new org.springframework.security.core.userdetails.User(user.getLoginId(), user.getPassword(),
				emptyList());

	}

	public ApplicationUser findUserByUsername(String loginId) throws UsernameNotFoundException {
		ApplicationUser user = userRepository.findByLoginId(loginId);
		if (user == null) {
			throw new UsernameNotFoundException(loginId);
		}
		return user;
	}

	// public List<ApplicationUser> findUsersByUsernames(String[] loginIds) throws
	// UsernameNotFoundException {
	// List<ApplicationUser> users = userRepository.findByLoginIdIn(loginIds);
	// if (users == null) {
	// throw new UsernameNotFoundException(loginIds.toString());
	// }
	// return users;
	// }

	public List<ApplicationUser> findUsersByUsernames(List<String> loginIds) throws UsernameNotFoundException {
		List<ApplicationUser> users = userRepository.findByLoginIdIn(loginIds);
		if (users == null) {
			throw new UsernameNotFoundException(loginIds.toString());
		}
		return users;
	}

	public ApplicationUser saveNewUser(ApplicationUser user) throws UserExistsException {
		// Check if user already present
		ApplicationUser temp = userRepository.findByLoginId(user.getLoginId());
		if (temp == null) {

			// activating the user
			user.setActive(true);
			user.setPassword(encoder.encode(user.getPassword()));

			// UserDto userDto = new UserDto(userRepository.save(user));
			// return userDto;
			return userRepository.save(user);
		} else {
			throw new UserExistsException("User with loginId: " + user.getLoginId() + " already present!");
		}

	}

	public ApplicationUser editUser(String loginId, ApplicationUser user) throws UserManagementException {

		if (!loginId.equals(user.getLoginId())) {
			throw new UserManagementException("Cannot change another user account. LoginId mismatch!");
		}

		ApplicationUser dbUser = userRepository.findByLoginId(loginId);

		if (dbUser == null) {
			throw new UsernameNotFoundException("User with loginId '" + user.getLoginId() + "' does not exist.");
		}

		if (!user.getPassword().equals(dbUser.getPassword())) {
			throw new PasswordChangedException("DataMismatch: Not allowed to change password from here!");
		}

		if (!dbUser.getLoginId().equals(user.getLoginId()) && !dbUser.getId().equals(user.getId())) {
			throw new LoginIdChangedException("LoginId change is not allowed!");
		}

		dbUser.setApplicationUserDetails(user);

		dbUser = userRepository.save(dbUser);

		return dbUser;

	}

	/**
	 * Saves user to DB efficiently in one db call
	 * 
	 * @param users
	 * @return
	 */
	public List<ApplicationUser> saveUser(ApplicationUser... users) {

		List<ApplicationUser> appUsersList = new ArrayList<>(users.length);

		for (ApplicationUser user : users) {
			appUsersList.add(user);
		}

		appUsersList = userRepository.save(appUsersList);
		logger.debug("User saved successfully to database.");
		return appUsersList;
	}

	/**
	 * Update the rating of all users based on the tripRating
	 * 
	 * @param rating
	 * @param users
	 */
	public void updateUserRating(Double rating, List<String> usersIds) {
		List<ApplicationUser> users = findUsersByUsernames(usersIds);
		for (ApplicationUser user : users) {
			// Running mean
			double tripsTillNow = 0;
			double userRating = 0;

			if (user.getTripsTillNow() != null)
				tripsTillNow = user.getTripsTillNow();

			if (user.getUserRating() != null)
				userRating = user.getUserRating();

			double runningMean = ((tripsTillNow * userRating) + rating) / tripsTillNow + 1;
			user.setUserRating(runningMean);
		}

		userRepository.save(users);
	}

	/**
	 * Service method that finds all the user that have requested to join a
	 * particular trip and then removes the trip request from that user.
	 * 
	 * @param trip
	 */
	public void deleteTripRequestsInUsers(Trip trip) {
		List<ApplicationUser> appusers = userRepository.findByTripsRequestsMade(trip);

		for (ApplicationUser appuser : appusers) {
			List<Trip> trips = appuser.getTripsRequestsMade();
			if (trips != null && trips.size() != 0) {
				for (Iterator<Trip> iter = trips.listIterator(); iter.hasNext();) {
					Trip t = iter.next();
					if (t.getTripId().equals(trip.getTripId())) {
						iter.remove();
					}
				}
			}
		}

		userRepository.save(appusers);
	}

	/**
	 * Service method that deletes the trip acceptance requests from the Admin.
	 * 
	 * @param trip
	 */
	public void deleteTripAccRequestsByAdmin(Trip trip) {
		ApplicationUser admin = findUserByUsername(trip.getAdmin());
		List<TripJoinRequestDto> tripReqs = admin.getTripsRequestsAwaitingConfirmation();

		if (tripReqs != null && tripReqs.size() != 0) {
			for (Iterator<TripJoinRequestDto> iter = tripReqs.listIterator(); iter.hasNext();) {
				TripJoinRequestDto tripReq = iter.next();

				if (tripReq.getTrip().getTripId().equals(trip.getTripId())) {
					iter.remove();
				}
			}
			userRepository.save(admin);
		}

	}
	
}
