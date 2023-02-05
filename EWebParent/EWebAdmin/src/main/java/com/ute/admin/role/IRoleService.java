package com.ute.admin.role;

import java.util.Optional;

import com.ute.common.entity.Role;

public interface IRoleService {
	Optional<Role> findByName(String name);
}
