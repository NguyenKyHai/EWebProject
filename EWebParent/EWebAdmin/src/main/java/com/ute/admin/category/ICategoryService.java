package com.ute.admin.category;

import java.util.List;
import java.util.Optional;

import com.ute.common.entity.Category;

public interface ICategoryService {
	List<Category> categories();
	Boolean existsByName(String name);
	Category save(Category category);
	Optional<Category> findById(Integer id);
	void updateCategoryEnabledStatus(Integer id, boolean enabled);
}