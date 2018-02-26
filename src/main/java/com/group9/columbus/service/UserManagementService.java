package com.group9.columbus.service;

import com.group9.columbus.entity.ApplicationUser;
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

/**
 * Service class required by Spring Security for authentication.
 * 
 * @author amit
 */
@Service
public class UserManagementService implements UserDetailsService {

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
		
		if(!user.getPassword().equals(dbUser.getPassword())) {
			throw new PasswordChangedException("DataMismatch: Not allowed to change password from here!");
		}

		if (!dbUser.getLoginId().equals(user.getLoginId()) && !dbUser.getId().equals(user.getId())) {
			throw new LoginIdChangedException("LoginId change is not allowed!");
		}

		dbUser.setApplicationUserDetails(user);

		dbUser = userRepository.save(dbUser);

		return dbUser;

	}
}
