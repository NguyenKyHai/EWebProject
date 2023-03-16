package com.ute.admin.order;

import com.ute.common.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface IOrderRepository extends JpaRepository<Order,Integer> {
    @Query("UPDATE Order o SET o.status = ?2 WHERE o.id = ?1")
    @Modifying
    void updateOrderStatus(Integer id, String status);
}
