package com.ute.admin.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ute.common.entity.Role;

public interface IRoleRepository extends JpaRepository<Role, Integer>{

}
