package com.ute.shopping.category;

import com.ute.common.entity.Category;
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
public class CategoryRestController {

	@Autowired
	ICategoryService categoryService;

	@GetMapping("/categories")
	public ResponseEntity<?> getListCategories() {
		List<Category> listCategories = categoryService.categories();
		if (listCategories.isEmpty()) {
			return new ResponseEntity<>(new ResponseMessage("List of Categories is empty!"), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(listCategories, HttpStatus.OK);
	}

	@GetMapping("/category/{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable Integer id) {
		Optional<Category> category = categoryService.findById(id);
		if (!category.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(category.get(), HttpStatus.OK);
	}

}
