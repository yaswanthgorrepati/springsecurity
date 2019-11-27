package com.codesimple.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codesimple.security.objects.User;
import com.codesimple.security.service.UserService;
import com.codesimple.security.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Override
	public User findByUserName(String userName) {
		User user = userRepository.findByUserName(userName);
		return user;
	}
	@Override
	public void save(User user) {
		userRepository.save(user);		
	}

}
