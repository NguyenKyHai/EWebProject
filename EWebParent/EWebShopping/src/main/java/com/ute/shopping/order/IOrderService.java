package com.ute.shopping.order;

import java.util.List;
import java.util.Optional;

import com.ute.common.entity.Order;
import com.ute.common.request.LineItem;

public interface IOrderService {
	void createOrder(List<LineItem> lineItem, Order order);
	List<Order> orderDetail(Integer id);
}
