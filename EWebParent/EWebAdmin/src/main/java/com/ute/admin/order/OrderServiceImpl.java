package com.ute.admin.order;

import com.ute.common.entity.Order;
import com.ute.common.entity.OrderDetail;
import com.ute.common.response.ReportItemResponse;
import com.ute.common.util.SortedUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<Order> filterOrders(Date startSate, Date endDate, String paymentMethod, int page, int size, List<String> sortBy, String order) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(SortedUtil.createListSortOrder(sortBy, order)));

        return orderRepository.filterOrder(startSate, endDate, paymentMethod, pageable);
    }
}
