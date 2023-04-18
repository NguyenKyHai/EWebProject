package com.ute.admin.order;

import com.ute.common.entity.Order;
import com.ute.common.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface IOrderRepository extends JpaRepository<Order,Integer> {
    @Query("UPDATE Order o SET o.status = ?2 WHERE o.id = ?1")
    @Modifying
    void updateOrderStatus(Integer id, String status);

    @Query("SELECT o FROM Order o WHERE"
            + " o.orderTime BETWEEN ?1 AND ?2 ORDER BY o.orderTime ASC")
    List<Order> findByOrderTimeBetween(Date startTime, Date endTime);
}
