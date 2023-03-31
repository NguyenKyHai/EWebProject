package com.ute.shopping.address;

import java.util.Optional;

import com.ute.common.entity.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAddressRepository extends JpaRepository<ShippingAddress, Integer>{
	Optional<ShippingAddress> findByName(String name);
}
