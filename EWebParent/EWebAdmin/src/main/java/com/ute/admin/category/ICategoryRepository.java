package com.ute.admin.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ute.common.entity.Category;

public interface ICategoryRepository extends JpaRepository<Category, Integer>{
	@Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
	public List<Category> findRootCategories();

}
