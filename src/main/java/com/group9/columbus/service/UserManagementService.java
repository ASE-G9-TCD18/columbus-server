package com.group9.columbus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.group9.columbus.entity.User;
import com.group9.columbus.entity.UserPrincipal;
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

	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		User user = userRepository.findByLoginId(loginId);
        if (user == null) {
            throw new UsernameNotFoundException(loginId);
        }
        return new UserPrincipal(user);
	}
	
	public void saveNewUser(User user) {
		// Check if user already present
		userRepository.save(user);
	}
}
