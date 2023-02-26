package com.ute.shopping.address;

import java.util.List;
import java.util.Optional;

import com.ute.common.entity.Address;

public interface IAddressService {
	List<Address>getAllAddresses();
	Address save(Address address);
	Optional<Address>findById(Integer id);
	Optional<Address> findByName(String name);
}
