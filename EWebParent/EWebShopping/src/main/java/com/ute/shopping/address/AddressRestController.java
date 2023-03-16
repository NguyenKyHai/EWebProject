package com.ute.shopping.address;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ute.common.request.AddressRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ute.common.entity.Address;
import com.ute.common.response.ResponseMessage;

@RestController
public class AddressRestController {
	@Autowired
	IAddressService addressService;

	@GetMapping("/addresses")
	public ResponseEntity<?> getListUsers() {
		List<Address> listCategories = addressService.getAllAddresses();
		if (listCategories.isEmpty()) {
			return new ResponseEntity<>(new ResponseMessage("List of Addresses is empty!"), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(listCategories, HttpStatus.OK);
	}

	@PostMapping("/address/create")
	public ResponseEntity<?> saveAddress(@RequestBody AddressRequest request) {
		Address address = new Address();
		address.setName(request.getName());
		address.setStreet(request.getStreet());
		address.setDistrict(request.getDistrict());
		addressService.save(address);

		return new ResponseEntity<>(new ResponseMessage("Create address successfully"), HttpStatus.CREATED);
	}

	@GetMapping("/address/{id}")
	public ResponseEntity<?> getAddressById(@PathVariable Integer id) {
		Optional<Address> address = addressService.findById(id);
		if (!address.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(address.get(), HttpStatus.OK);
	}

	@PutMapping("/address/{id}")
	public ResponseEntity<?> changeNameAddressById(@PathVariable Integer id, @RequestBody AddressRequest request) {
		Optional<Address> address = addressService.findById(id);
		if (!address.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		address.get().setName(request.getName());
		address.get().setStreet(request.getStreet());
		address.get().setDistrict(request.getDistrict());
		addressService.save(address.get());

		return new ResponseEntity<>(new ResponseMessage("Update address successfully"), HttpStatus.OK);
	}

	
	
}
