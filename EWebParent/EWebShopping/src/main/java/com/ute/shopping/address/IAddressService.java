package com.ute.shopping.address;

import java.util.List;
import java.util.Optional;

import com.ute.common.entity.ShippingAddress;

public interface IAddressService {
	List<ShippingAddress>getAllAddresses();
	ShippingAddress save(ShippingAddress shippingAddress);
	Optional<ShippingAddress>findById(Integer id);
	Optional<ShippingAddress> findByName(String name);
	void updateDefaultAddress(Integer id, boolean isDefault);
	void deleteAddress(Integer id, boolean deleteFlag);
}
