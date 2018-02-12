package com.group9.columbus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.group9.columbus.dto.UserDto;
import com.group9.columbus.entity.User;
import com.group9.columbus.entity.UserPrincipal;
import com.group9.columbus.exception.UserExistsException;
import com.group9.columbus.repository.UserRepository;

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
	PasswordEncoder encoder;

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		User user = userRepository.findByLoginId(loginId);
        if (user == null) {
            throw new UsernameNotFoundException(loginId);
        }
        return new UserPrincipal(user);
	}
	
	public UserDto saveNewUser(User user) throws UserExistsException {
		// Check if user already present
		if(userRepository.findByLoginId(user.getLoginId()) == null ) {
			
			// activating the user
			user.setActive(true);
			user.setPassword(encoder.encode(user.getPassword()));
			
			UserDto userDto = new UserDto(userRepository.save(user));
			return userDto;
		} else {
			throw new UserExistsException("User with loginId: "+user.getLoginId()+" already present!");
		}
		
	}
}