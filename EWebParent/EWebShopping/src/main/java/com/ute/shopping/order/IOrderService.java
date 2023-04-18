package com.ute.shopping.order;

import java.util.List;
import java.util.Optional;

import com.ute.common.entity.Order;
import com.ute.common.request.LineItem;

public interface IOrderService {
	void createOrder(List<LineItem> lineItem, Order order);
	Optional<Order> findById(Integer id);
	void updateStatus(Integer id, String status);

	List<Order> getOrderDetail(Integer id);
}
