package com.ute.shopping.address;

import java.util.Optional;

import com.ute.common.entity.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface IAddressRepository extends JpaRepository<ShippingAddress, Integer>{
	Optional<ShippingAddress> findByName(String name);
	@Modifying
	@Query("Update ShippingAddress set defaultAddress =?2 where id = ?1")
	void updateDefault(Integer id, boolean isDefault);

	@Modifying
	@Query("Update ShippingAddress set deleteFlag =?2 where id = ?1")
	void deleteAddress(Integer id, boolean deleteFlag);
}
