package com.ute.shopping.product;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ute.common.entity.Product;

public interface IProductRepository extends JpaRepository<Product, Integer> {

	Optional<Product> findByName(String name);

	Boolean existsByName(String name);

	@Query(value = "select p.* from products p join categories c on p.category_id=c.id"
			+ " where upper(p.name) like CONCAT('%',UPPER(?1),'%')"
			+ " and upper(c.name) like CONCAT('%',UPPER(?2),'%') and (p.price*(100-p.discount_percent)/100 between ?3 and ?4) "
			+ " order by p.id"
			, nativeQuery = true)
	public List<Product> filterProduct(String productName, String categoryName, double min_price, double max_price);
}
