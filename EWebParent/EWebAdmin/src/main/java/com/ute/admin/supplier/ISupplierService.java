package com.ute.admin.supplier;

import com.ute.common.entity.Supplier;
import java.util.List;
import java.util.Optional;

public interface ISupplierService {
	List<Supplier> categories();
	Boolean existsByName(String name);
	Supplier save(Supplier supplier);
	Optional<Supplier> findById(Integer id);
	Optional<Supplier> findByName(String name);
}