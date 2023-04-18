package com.ute.admin.role;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ute.common.entity.Role;

@Service
public class RoleServiceImpl implements IRoleService{

	@Autowired
	IRoleRepository roleRepository;

	@Override
	public Optional<Role> findByName(String name) {
		
		return roleRepository.findByName(name);
	}
	
	

}
