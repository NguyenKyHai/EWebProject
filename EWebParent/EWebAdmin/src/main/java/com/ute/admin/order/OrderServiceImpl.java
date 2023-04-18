package com.ute.admin.order;

import com.ute.common.entity.Order;
import com.ute.common.entity.OrderDetail;
import com.ute.common.response.ReportItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements IOrderService {

    @Autowired
    IOrderRepository orderRepository;

    @Autowired
    IOrderDetailRepository orderDetailRepository;

    @Override
    public List<Order> getListOrder() {
        return orderRepository.findAll();
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
    public Optional<Order> orderDetail(Integer id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<ReportItemResponse> findByOrderTimeBetween(Date startTime, Date endTime) {
        return orderDetailRepository.findByOrderTimeBetween(startTime, endTime);
    }

    @Override
    public List<OrderDetail>bestSellingProduct(Date startDate,Date endDate,Pageable page) {

        return orderDetailRepository.bestSellingProduct(startDate,endDate,page);
    }

}
