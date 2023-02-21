package com.ute.shopping.orderdetail;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ute.common.entity.OrderDetail;

public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Integer>{

}
