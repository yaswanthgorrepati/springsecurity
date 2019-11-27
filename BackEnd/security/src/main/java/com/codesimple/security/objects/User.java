package com.codesimple.security.objects;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "USER")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8983824698069765521L;

	// email is username
	@Id
	@Column(name = "USER_NAME")
	private String userName;

	@Column(name = "password")
	private String password;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<UserRole> userRoles;

	public User() {
	}

	public User(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	public User(String userName, String password, List<UserRole> userRoles) {
		this.userName = userName;
		this.password = password;
		this.userRoles = userRoles;
		for (UserRole userRole : userRoles) {
			userRole.setUser(this);
		}
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
		for (UserRole userRole : userRoles) {
			userRole.setUser(this);
		}
	}

	@Override
	public String toString() {
		return "User [username=" + userName + ", password=" + password + ", userRoles=" + userRoles + "]";
	}

}
