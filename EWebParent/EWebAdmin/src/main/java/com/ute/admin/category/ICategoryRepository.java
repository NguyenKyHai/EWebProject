package com.ute.admin.category;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ute.common.entity.Category;

public interface ICategoryRepository extends JpaRepository<Category, Integer>{


}
