package com.ute.shopping.address;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ute.common.entity.Address;

public interface IAddressRepository extends JpaRepository<Address, Integer>{
	Optional<Address> findByName(String name);
}
