package com.ute.shopping.product;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ute.common.entity.Product;
import com.ute.common.response.ResponseMessage;

@RestController
@RequestMapping("/api")
public class ProductRestController {

	@Autowired
	IProductService productService;

	@Autowired

	@GetMapping("/products")
	public ResponseEntity<?> listProducts() {
		List<Product> products = productService.listAll();
		if (products.isEmpty()) {
			return new ResponseEntity<>(new ResponseMessage("List of users is empty!"), HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(products, HttpStatus.OK);
	}
	
	@GetMapping("/product/{id}")
	public ResponseEntity<?> getProductById(@PathVariable Integer id) {
		Optional<Product> product = productService.findById(id);
		if (!product.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(product.get(), HttpStatus.OK);
	}
	
}
