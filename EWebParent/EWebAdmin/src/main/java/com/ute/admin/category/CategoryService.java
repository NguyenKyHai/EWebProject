package com.ute.admin.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ute.common.entity.Category;

@Service
public class CategoryService implements ICategoryService{

	@Autowired
	ICategoryRepository categoryRepository;
	
	@Override
	public List<Category> getAllListCategory() {
		return categoryRepository.findRootCategories();
		
	}

	@Override
	public Category findCategoryById(Integer id) {
		return categoryRepository.findById(id).get();
	}

}
