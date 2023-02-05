package com.ute.admin.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ute.common.entity.Category;

@Service
public class CategoryService implements ICategoryService{

	@Autowired
	ICategoryRepository categoryRepository;
	public List<Category> getListCategories() {
		return categoryRepository.findAll();
	}

	@Override
	public Category save(Category category) {
		return categoryRepository.save(category);
	}

	@Override
	public Category findCategoryById(Integer id) {
		return categoryRepository.findById(id).get();
	}

	@Override
	public void deleteByCategoryId(Integer id) {
		categoryRepository.deleteById(id);
	}

	@Override
	public void updateCategoryStatus(Integer id, boolean enable) {
		categoryRepository.updateEnabledStatus(id, enable);
	}
	public List<Category> categories() {
		return categoryRepository.findAll();
	}


}
