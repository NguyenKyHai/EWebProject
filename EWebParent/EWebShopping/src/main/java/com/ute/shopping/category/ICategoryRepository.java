package com.ute.shopping.category;

import com.ute.common.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ICategoryRepository extends JpaRepository<Category, Integer>{
	
	Boolean existsByName(String name);

	public Long countById(Integer id);

	public Optional<Category> findByName(String name);

	@Query("UPDATE Category c SET c.enabled = ?2 WHERE c.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
}
