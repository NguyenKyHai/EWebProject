package com.ute.shopping.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ute.common.entity.OrderDetail;

public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Integer>{

}
