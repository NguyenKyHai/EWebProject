package com.ute.admin.order;

import com.ute.common.entity.Order;
import com.ute.common.response.OrderReport;
import com.ute.common.response.ProductReport;
import com.ute.common.util.SortedUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
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
    public List<ProductReport> getProductReportByDay(int day) {
        Calendar cal = Calendar.getInstance();
        Date endTime = cal.getTime();
        cal.add(Calendar.DATE, -day);
        Date startTime = cal.getTime();
        return orderDetailRepository.productsReportTimeBetween(startTime, endTime);
    }

    @Override
    public List<OrderReport> getOrderReportByDay(int day) {
        Calendar cal = Calendar.getInstance();
        Date endTime = cal.getTime();
        cal.add(Calendar.DATE, -day);
        Date startTime = cal.getTime();
        return orderDetailRepository.orderReportByTimeBetween(startTime, endTime);
    }

    @Override
    public Page<Order> filterOrders(Date startSate, Date endDate, String paymentMethod, int page, int size, List<String> sortBy, String order) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(SortedUtil.createListSortOrder(sortBy, order)));

        return orderRepository.filterOrder(startSate, endDate, paymentMethod, pageable);
    }
}
