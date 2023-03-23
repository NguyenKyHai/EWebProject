package com.ute.admin.supplier;

import com.ute.common.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ISupplierRepository extends JpaRepository<Supplier, Integer>{
	
	Boolean existsByName(String name);

	Optional<Supplier> findByName(String name);

}
