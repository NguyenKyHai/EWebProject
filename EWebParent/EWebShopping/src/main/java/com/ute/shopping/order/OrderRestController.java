package com.ute.shopping.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ute.common.request.CartItem;
import com.ute.common.response.ResponseMessage;

@RestController
@RequestMapping("/api")
public class OrderRestController {

	@Autowired
	OrderService orderService;
	
	@PostMapping("/order/create")
	public ResponseEntity<?> createProduct(@RequestBody CartItem request) {
		
		
		return new ResponseEntity<>(new ResponseMessage("Create new order successfully"), HttpStatus.CREATED);
	}

	
}
