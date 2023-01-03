package com.ute.admin.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ute.common.entity.User;

public interface IUserRepository extends JpaRepository<User, Integer> {

}
