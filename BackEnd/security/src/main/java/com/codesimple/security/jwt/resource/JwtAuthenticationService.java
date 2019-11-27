package com.codesimple.security.jwt.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codesimple.security.jwt.SecurityUtility;
import com.codesimple.security.mail.Mail;
import com.codesimple.security.objects.Role;
import com.codesimple.security.objects.RoleEnum;
import com.codesimple.security.objects.User;
import com.codesimple.security.objects.UserRole;
import com.codesimple.security.service.UserService;

@Service
public class JwtAuthenticationService {

	@Autowired
	private SecurityUtility securityUtility;

	@Autowired
	private UserService userService;

	@Autowired
	private Mail mail;

	public String forgotPassword(User user) throws Exception {

		String newPassword = securityUtility.randomPassword();
		String encryptedNewPassword = securityUtility.passwordEncoderBean().encode(newPassword);
		user.setPassword(encryptedNewPassword);
		userService.save(user);
		String message = mail.sendMail(user.getUserName(), "New Password", "Here is your new Password : " + newPassword);
		return message;
	}
	
	public String passwordChange(User user) throws Exception {
		String encryptedNewPassword = securityUtility.passwordEncoderBean().encode(user.getPassword());
		user.setPassword(encryptedNewPassword);
		userService.save(user);
		return "Password is changed successfully";
	}

	public String newUserRegistration(JwtTokenRequest authenticationRequest) throws Exception {

        User user = createBasicUser(authenticationRequest);
		userService.save(user);
		String message = mail.sendMail(user.getUserName(), "New User Registration", "Thanks for choosing us " + user.getUserName());
		return message;
	}

	private User createBasicUser(JwtTokenRequest authenticationRequest) {
		List<UserRole> userRoles = new ArrayList<UserRole>();
		Role role = new Role(RoleEnum.ADMIN.getRoleId(), RoleEnum.ADMIN.name());
		UserRole userRole = new UserRole();
		userRole.setRole(role);
		userRoles.add(userRole);
		
		String encryptedPassword = securityUtility.passwordEncoderBean().encode(authenticationRequest.getPassword());
		
		User user = new User(authenticationRequest.getUsername(),encryptedPassword, userRoles);
		
		return user;
	}
}
