package com.codesimple.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.codesimple.security.objects.Role;
import com.codesimple.security.objects.User;
import com.codesimple.security.objects.UserRole;
import com.codesimple.security.service.RoleService;
import com.codesimple.security.service.UserService;

@SpringBootApplication
public class SecurityApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
		
	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	
		Role role1 = new Role();
		role1.setRoleId(1);
		role1.setRoleName("ADMIN");
		roleService.save(role1);
		
		Role role2 = new Role();
		role2.setRoleId(2);
		role2.setRoleName("INTERNAL");
		roleService.save(role2);
		
		Role role3 = new Role();
		role3.setRoleId(3);
		role3.setRoleName("EXTERNAL");	
		roleService.save(role3);
		
		List<UserRole> userRoles1 = new ArrayList<UserRole>();
		UserRole userRole1 = new UserRole();
		userRole1.setRole(role1);
		userRoles1.add(userRole1);
		
		UserRole userRole2 = new UserRole();
		userRole2.setRole(role2);
		userRoles1.add(userRole2);
		
//		 password: 123456
		User user = new User("example@gmail.com", "$2a$10$3z16hAjpyoFg0j0dHq2cv.q7g7/FAlskXVDwDMSwULlyMZ6CnObKu", userRoles1);
		userService.save(user);
		
		
		List<UserRole> userRoles2 = new ArrayList<UserRole>();
		UserRole userRole11 = new UserRole();
		userRole11.setRole(role1);
		userRoles2.add(userRole11);
		
		UserRole userRole12 = new UserRole();
		userRole12.setRole(role2);
		userRoles2.add(userRole12);
		
		UserRole userRole13 = new UserRole();
		userRole13.setRole(role3);
		userRoles2.add(userRole13);
		
	}

}
