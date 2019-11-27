package com.codesimple.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codesimple.security.objects.Role;
import com.codesimple.security.repository.RoleRepository;
import com.codesimple.security.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleRepository roleRepository;
	@Override
	public void save(Role role) {
		roleRepository.save(role);   
	}

}
