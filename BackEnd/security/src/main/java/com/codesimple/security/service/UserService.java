package com.codesimple.security.service;

import com.codesimple.security.objects.User;

public interface UserService {

	public User findByUserName(String userName);
	
	public void save(User user);
}
