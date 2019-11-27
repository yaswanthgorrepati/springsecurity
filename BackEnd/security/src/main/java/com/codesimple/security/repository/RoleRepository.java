package com.codesimple.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codesimple.security.objects.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
