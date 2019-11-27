package com.codesimple.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codesimple.security.objects.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
	
	User findByUserName(String userName);

}
