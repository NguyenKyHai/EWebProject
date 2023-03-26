package com.ute.shopping.order;

import com.ute.common.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ute.common.entity.Order;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IOrderRepository extends JpaRepository<Order, Integer>{

    @Query("Select o From Order o Where o.customer.id = ?1")
    List<Order> orderDetailByCustomer(Integer customerId);
}
