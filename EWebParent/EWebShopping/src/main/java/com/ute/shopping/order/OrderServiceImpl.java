package com.ute.shopping.order;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.ute.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ute.common.entity.Order;
import com.ute.common.entity.OrderDetail;
import com.ute.common.request.LineItem;
import com.ute.shopping.product.IProductRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements IOrderService {

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
    public Optional<Order> findById(Integer id) {
        return orderRepository.findById(id);
    }

    @Override
    public void updateStatus(Integer id, String status) {
        orderRepository.updateOrderStatus(id, status);
    }

    @Override
    public List<Order> getOrderDetail(Integer id) {
        return orderRepository.orderDetailByCustomer(id);
    }

    public void updateQuantityAfterReturn(Order order){
        Set<OrderDetail> orderDetails = order.getOrderDetails();

        for (OrderDetail detail : orderDetails) {
            Product product = detail.getProduct();
            product.setQuantity(product.getQuantity() + detail.getQuantity());
            product.setSold(product.getSold() - detail.getQuantity());
            productRepository.save(product);
        }
    }
}
