package com.ute.shopping.order;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ute.common.entity.Product;
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
            Product product = productRepository.getById(item.getProductId());
            product.setQuantity(product.getQuantity() - item.getQuantity());
            product.setSold(product.getSold() + item.getQuantity());
            productRepository.save(product);
            detail.setProduct(product);
            detail.setQuantity(item.getQuantity());
            detail.setProductPrice(item.getProductPrice());

            orderDetailRepository.save(detail);
        }

    }

    @Override
    public List<Order> orderDetail(Integer id) {
        return orderRepository.orderDetailByCustomer(id);
    }
}
