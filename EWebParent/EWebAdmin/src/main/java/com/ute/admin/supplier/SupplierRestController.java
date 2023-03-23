package com.ute.admin.supplier;

import com.ute.common.entity.Supplier;
import com.ute.common.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SupplierRestController {
	@Autowired
	ISupplierService supplierService;

	@GetMapping("/suppliers")
	public ResponseEntity<?> getListSuppliers() {
		List<Supplier> listSuppliers = supplierService.categories();
		if (listSuppliers.isEmpty()) {
			return new ResponseEntity<>(new ResponseMessage("List of suppliers is empty!"), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(listSuppliers, HttpStatus.OK);
	}

	@PostMapping("/supplier/create")
	public ResponseEntity<?> saveSupplier(@RequestBody Map<String, String> param) {
		String name = param.get("name");
		String phoneNumber = param.get("phone");
		String address = param.get("address");
		if (supplierService.existsByName(name))
			return new ResponseEntity<>(new ResponseMessage("Name of supplier is existed"), HttpStatus.BAD_REQUEST);
		Supplier supplier = new Supplier(name);
		supplier.setPhoneNumber(phoneNumber);
		supplier.setAddress(address);
		supplierService.save(supplier);

		return new ResponseEntity<>(new ResponseMessage("Create a new supplier successfully"), HttpStatus.CREATED);
	}

	@GetMapping("/supplier/{id}")
	public ResponseEntity<?> getSuppliersById(@PathVariable Integer id) {
		Optional<Supplier> category = supplierService.findById(id);
		if (!category.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(category.get(), HttpStatus.OK);
	}

	@PutMapping("/supplier/{id}")
	public ResponseEntity<?> changeNameSuppliersyById(@PathVariable Integer id, @RequestBody Map<String, String> param) {
		Optional<Supplier> supplier = supplierService.findById(id);
		if (!supplier.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		String name = param.get("name");
		supplier.get().setName(name);
		supplierService.save(supplier.get());

		return new ResponseEntity<>(new ResponseMessage("Update supplier successfully"), HttpStatus.OK);
	}

}
