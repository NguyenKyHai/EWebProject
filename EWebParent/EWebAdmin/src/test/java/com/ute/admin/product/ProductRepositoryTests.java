package com.ute.admin.product;

import com.ute.admin.order.IOrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.ute.common.entity.Product;

import java.util.List;
import java.util.Map;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {

    @Autowired
    IProductRepository repo;

    @Test
    public void testFindByName() {
        Product p = repo.findByName("Máy tính DELL");
        if (p != null)
            System.out.println(p.getId() + " " + p.getName());
    }

}
