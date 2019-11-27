package com.codesimple.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codesimple.security.objects.User;
import com.codesimple.security.service.UserService;


@Service
public class JwtUserDetailsService implements UserDetailsService{

	@Autowired
	private UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userService.findByUserName(userName);
		  
	        if (user == null) {
	            throw new UsernameNotFoundException(String.format("USER_NOT_FOUND '%s'.", userName));
	        } else {
	        	return new JwtUserDetails(user.getUserName(), user.getPassword(), user.getUserRoles());
	        }
	}
}
