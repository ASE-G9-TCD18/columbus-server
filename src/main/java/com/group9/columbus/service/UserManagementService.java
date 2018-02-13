package com.group9.columbus.service;

import com.group9.columbus.entity.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.group9.columbus.dto.UserDto;
import com.group9.columbus.exception.UserExistsException;
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
	private Md5PasswordEncoder encoder;

	public UserManagementService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		ApplicationUser user = userRepository.findByLoginId(loginId);
        if (user == null) {
            throw new UsernameNotFoundException(loginId);
        }
		return new org.springframework.security.core.userdetails.User(user.getLoginId(), user.getPassword(), emptyList());
	}

	public UserDto findUserByUsername(String loginId) throws UsernameNotFoundException {
		ApplicationUser user = userRepository.findByLoginId(loginId);
		if (user == null) {
			throw new UsernameNotFoundException(loginId);
		}
		return new UserDto(user);
	}

	public UserDto saveNewUser(ApplicationUser user) throws UserExistsException {
		// Check if user already present
		if(userRepository.findByLoginId(user.getLoginId()) == null ) {
			
			// activating the user
			user.setActive(true);
			user.setPassword(encoder.encodePassword(user.getPassword(), null));
			
			UserDto userDto = new UserDto(userRepository.save(user));
			return userDto;
		} else {
			throw new UserExistsException("User with loginId: "+user.getLoginId()+" already present!");
		}
		
	}
}
