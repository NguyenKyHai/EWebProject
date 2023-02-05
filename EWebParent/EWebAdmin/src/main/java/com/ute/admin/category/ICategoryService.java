package com.ute.admin.category;

import java.util.List;

import com.ute.common.entity.Category;

public interface ICategoryService {

	List<Category> getListCategories();
	Category save(Category category);
	Category findCategoryById(Integer id);
	void deleteByCategoryId(Integer id);
	void updateCategoryStatus(Integer id, boolean enable);
}

