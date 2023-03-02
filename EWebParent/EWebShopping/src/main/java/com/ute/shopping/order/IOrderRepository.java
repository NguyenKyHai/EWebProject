package com.ute.shopping.order;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ute.common.entity.Order;

public interface IOrderRepository extends JpaRepository<Order, Integer>{

}
