package com.ute.admin.supplier;

import com.ute.common.entity.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SupplierService implements ISupplierService {

	@Autowired
	ISupplierRepository supplierRepository;

	@Override
	public List<Supplier> categories() {
		return supplierRepository.findAll();
	}

	@Override
	public Supplier save(Supplier category) {
		
		return supplierRepository.save(category);
	}

	@Override
	public Optional<Supplier> findById(Integer id) {
		
		return supplierRepository.findById(id);
	}

	@Override
	public Boolean existsByName(String name) {
		
		return supplierRepository.existsByName(name);
	}

	@Override
	public Optional<Supplier> findByName(String name) {
	
		return supplierRepository.findByName(name);
	}
}
