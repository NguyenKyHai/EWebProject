package com.ute.shopping.order;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ute.common.entity.Order;
import com.ute.common.entity.OrderDetail;
import com.ute.common.request.LineItem;
import com.ute.shopping.product.IProductRepository;

@Service
public class OrderService implements IOrderService {

	@Autowired
	IOrderDetailRepository orderDetailRepository;
	@Autowired
	IOrderRepository orderRepository;
	@Autowired
	IProductRepository productRepository;

	@Override
	public void createOrder(List<LineItem> lineItem, Order order) {
	
		order.setOrderTime(new Date());
		
		orderRepository.save(order);

		for (LineItem item : lineItem) {
			OrderDetail detail = new OrderDetail();
			detail.setOrder(order);
			detail.setProduct(productRepository.getById(Integer.parseInt(item.getProductId())));
			detail.setQuantity(Integer.parseInt(item.getQuantity()));
			detail.setProductPrice(Float.parseFloat(item.getProductPrice()));

			orderDetailRepository.save(detail);
		}

	}
}
