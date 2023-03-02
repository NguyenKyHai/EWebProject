package com.ute.shopping.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ute.common.entity.Order;

@Service
public class OrderService {

	@Autowired
	IOrderDetailRepository orderDetailRepository;
	@Autowired
	IOrderRepository orderRepository;

	public void createOrder() {
		Order order = new Order();

		orderRepository.save(order);
	}
}
