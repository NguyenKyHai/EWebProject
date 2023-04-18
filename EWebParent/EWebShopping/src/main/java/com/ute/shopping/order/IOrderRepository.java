package com.ute.shopping.order;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ute.common.entity.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IOrderRepository extends JpaRepository<Order, Integer>{

    @Query("UPDATE Order o SET o.status = ?2 WHERE o.id = ?1")
    @Modifying
    void updateOrderStatus(Integer id, String status);

    @Query("Select o as orderDetail" +
            " From Order o  Where o.customer.id = ?1")
    List<Order> orderDetailByCustomer(Integer customerId);

}
