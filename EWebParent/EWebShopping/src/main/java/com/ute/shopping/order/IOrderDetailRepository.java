package com.ute.shopping.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ute.common.entity.OrderDetail;
import org.springframework.data.jpa.repository.Query;

public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Integer>{

    @Query("SELECT COUNT(d) FROM OrderDetail d "
            + " WHERE d.product.id = ?1 AND d.order.customer.id = ?2 AND"
            + " d.order.status = ?3")
    public Long countByProductAndCustomerAndOrderStatus(
            Integer productId, Integer customerId, String status);
}
