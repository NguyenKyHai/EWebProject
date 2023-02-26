package com.ute.shopping.address;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ute.common.entity.Address;

@Service
public class AddressService implements IAddressService{

	@Autowired
	IAddressRepository addressRepository;
	
	@Override
	public List<Address>getAllAddresses(){
		return addressRepository.findAll();
	}
	
	@Override
	public Address save(Address address) {
		return addressRepository.save(address);
	}

	@Override
	public Optional<Address> findById(Integer id) {
		return addressRepository.findById(id);
	}

	@Override
	public Optional<Address> findByName(String name) {
		return addressRepository.findByName(name);
	}

}
