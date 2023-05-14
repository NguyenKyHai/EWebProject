package com.ute.shopping.order;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ute.common.entity.Order;
import com.ute.common.request.LineItem;
import com.ute.common.response.ProductItem;

public interface IOrderService {
	void createOrder(List<LineItem> lineItem, Order order);
	Optional<Order> findById(Integer id);
	void updateStatus(Integer id, String status);
	List<ProductItem> bestSellingProduct(long sold, Date startTime, Date endTime, List<String> paymentMethod);
	List<Order> getOrderDetail(Integer id);
}
