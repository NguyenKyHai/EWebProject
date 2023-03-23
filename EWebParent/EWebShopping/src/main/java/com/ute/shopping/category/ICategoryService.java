package com.ute.shopping.category;

import com.ute.common.entity.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
	List<Category> categories();
	Boolean existsByName(String name);
	Category save(Category category);
	Optional<Category> findById(Integer id);
	Optional<Category> findByName(String name);
	void updateCategoryEnabledStatus(Integer id, boolean enabled);
}