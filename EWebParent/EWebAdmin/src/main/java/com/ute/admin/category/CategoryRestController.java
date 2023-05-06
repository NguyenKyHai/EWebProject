package com.ute.admin.category;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ute.common.request.CategoryRequest;
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
import com.ute.common.entity.Category;
import com.ute.common.response.ResponseMessage;

@RestController
@RequestMapping("/api")
public class CategoryRestController {

	@Autowired
	ICategoryService categoryService;

	@GetMapping("/categories")
	public ResponseEntity<?> getListCategories() {
		List<Category> listCategories = categoryService.categories();
		return new ResponseEntity<>(listCategories, HttpStatus.OK);
	}

	@PostMapping("/category/create")
	public ResponseEntity<?> saveCategory(@RequestBody Map<String, String> param) {
		String name = param.get("name");
		if (categoryService.existsByName(name))
			return new ResponseEntity<>(new ResponseMessage("Name of category is existed"), HttpStatus.BAD_REQUEST);
		Category category = new Category(name);
		category.setEnabled(true);
		categoryService.save(category);

		return new ResponseEntity<>(new ResponseMessage("Create a new category successfully"), HttpStatus.CREATED);
	}

	@GetMapping("/category/{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable Integer id) {
		Optional<Category> category = categoryService.findById(id);
		if (!category.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(category.get(), HttpStatus.OK);
	}

	@PutMapping("/category/{id}")
	public ResponseEntity<?> changeNameCategoryById(@PathVariable Integer id, @RequestBody CategoryRequest request) {
		Optional<Category> category = categoryService.findById(id);
		if (!category.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		String name = request.getName();
		boolean enabled  = request.isEnabled();
		category.get().setName(name);
		category.get().setEnabled(enabled);
		categoryService.save(category.get());

		return new ResponseEntity<>(new ResponseMessage("Update category successfully"), HttpStatus.OK);
	}

	@PutMapping("/category/disabled/{id}")
	public ResponseEntity<?> disabledCategory(@PathVariable Integer id) {
		Optional<Category> category = categoryService.findById(id);
		if (!category.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		category.get().setEnabled(false);
		categoryService.save(category.get());

		return new ResponseEntity<>(new ResponseMessage("Disabled category successfully"), HttpStatus.OK);
	}

}
