package com.ute.admin.product;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ute.admin.category.ICategoryService;
import com.ute.common.entity.Category;
import com.ute.common.entity.Product;
import com.ute.common.request.ProductRequest;
import com.ute.common.response.ResponseMessage;

@RestController
@RequestMapping("/api")
public class ProductRestController {

	@Autowired
	IProductService productService;

	@Autowired
	ICategoryService categoryService;

	@PostMapping("/product/create")
	public ResponseEntity<?> createProduct(@RequestBody ProductRequest request) {

		if (productService.existsByName(request.getName().trim())) {
			return new ResponseEntity<>(new ResponseMessage("Name of product is existed"), HttpStatus.BAD_REQUEST);
		}

		Product product = new Product(request.getName().trim());
		Category category = categoryService.findById(Integer.parseInt(request.getCategoryId())).get();
		product.setCreatedTime(new Date());
		product.setEnabled(true);
		product.setPrice(Float.parseFloat(request.getPrice()));
		product.setCost(Float.parseFloat(request.getCost()));
		product.setDiscountPercent(Float.parseFloat(request.getDiscount()));
		product.setMainImage(request.getImage());
		product.setCategory(category);
		productService.save(product);

		return new ResponseEntity<>(new ResponseMessage("Create new product successfully"), HttpStatus.CREATED);
	}

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

	@PutMapping("/product/{id}")
	public ResponseEntity<?> changeNameProductById(@PathVariable Integer id, @RequestBody ProductRequest request) {
		Optional<Product> product = productService.findById(id);
		if (!product.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		product.get().setName(request.getName());
		//multi update
		productService.save(product.get());

		return new ResponseEntity<>(new ResponseMessage("Update category successfully"), HttpStatus.OK);
	}

	@PutMapping("/product/disaled/{id}")
	public ResponseEntity<?> disabledProduct(@PathVariable Integer id) {
		Optional<Product> product = productService.findById(id);
		if (!product.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		product.get().setEnabled(false);
		productService.save(product.get());

		return new ResponseEntity<>(new ResponseMessage("Disabled product successfully"), HttpStatus.OK);
	}
}
