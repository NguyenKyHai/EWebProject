package com.ute.shopping.address;

import java.util.List;
import java.util.Optional;

import com.ute.common.entity.ShippingAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService implements IAddressService{

	@Autowired
	IAddressRepository addressRepository;
	
	@Override
	public List<ShippingAddress>getAllAddresses(){
		return addressRepository.findAll();
	}
	
	@Override
	public ShippingAddress save(ShippingAddress shippingAddress) {
		return addressRepository.save(shippingAddress);
	}

	@Override
	public Optional<ShippingAddress> findById(Integer id) {
		return addressRepository.findById(id);
	}

	@Override
	public Optional<ShippingAddress> findByName(String name) {
		return addressRepository.findByName(name);
	}

}
