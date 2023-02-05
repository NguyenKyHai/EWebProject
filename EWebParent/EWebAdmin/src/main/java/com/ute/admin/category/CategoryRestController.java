package com.ute.admin.category;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ute.admin.response.ResponseMessage;
import com.ute.common.entity.Category;

@RestController
@RequestMapping("/api")
public class CategoryRestController {

	@Autowired
	ICategoryService categoryService;

	@GetMapping("/categories")
	public ResponseEntity<?> listCategories() {
		List<Category> listUsers = categoryService.getListCategories();
		if (listUsers.isEmpty()) {
			return new ResponseEntity<>(new ResponseMessage("List of users is empty!"), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(listUsers, HttpStatus.OK);

	}

	@GetMapping("/category/{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable Integer id) {
		try {
			Category category = categoryService.findCategoryById(id);
			return new ResponseEntity<Category>(category, HttpStatus.OK);

		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus.NO_CONTENT);
		}
	}

}
