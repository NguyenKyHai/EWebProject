package com.ute.admin.order;

import com.ute.common.constants.Constants;
import com.ute.common.entity.Order;
import com.ute.common.response.OrderReport;
import com.ute.common.response.OrderReportByTime;
import com.ute.common.response.ProductReport;
import com.ute.common.util.SortedUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

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
    public List<OrderReportByTime> getOrderReportByType(String typeReport) {
        if (typeReport == null) {
            throw new RuntimeException("Type report not found");
        }
        List<OrderReportByTime> orderReportByTimeList = new ArrayList<>();
        List<OrderReport> orderReports = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        if (Constants.TYPE_REPORT_WEEK.equals(typeReport)) {
            for (int i = 1; i <= 7; i++) {
                Date endTime = cal.getTime();
                cal.add(Calendar.DATE, -1);
                Date startTime = cal.getTime();
                orderReports = orderDetailRepository.orderReportByTimeBetween(startTime, endTime);
                OrderReportByTime orderReportByTimes = new OrderReportByTime();
                orderReportByTimes.setTime(startTime);
                orderReportByTimes.setOrderReports(orderReports);
                orderReportByTimeList.add(orderReportByTimes);
            }
        } else if (Constants.TYPE_REPORT_MONTH.equals(typeReport)) {
            for (int i = 1; i <= 4; i++) {
                Date endTime = cal.getTime();
                cal.add(Calendar.DATE, -(i * 7));
                Date startTime = cal.getTime();

                orderReports = orderDetailRepository.orderReportByTimeBetween(startTime, endTime);
                OrderReportByTime orderReportByTimes = new OrderReportByTime();
                orderReportByTimes.setTime(startTime);
                orderReportByTimes.setOrderReports(orderReports);
                orderReportByTimeList.add(orderReportByTimes);
            }
        }  else if (Constants.TYPE_REPORT_YEAR.equals(typeReport)) {
            for (int i = 1; i <= 12; i++) {
                Date endTime = cal.getTime();
                cal.add(Calendar.DATE, -(i * 30));
                Date startTime = cal.getTime();

                orderReports = orderDetailRepository.orderReportByTimeBetween(startTime, endTime);
                OrderReportByTime orderReportByTimes = new OrderReportByTime();
                orderReportByTimes.setTime(startTime);
                orderReportByTimes.setOrderReports(orderReports);
                orderReportByTimeList.add(orderReportByTimes);
            }
        }
        else {

            OrderReportByTime orderReportByTimes = new OrderReportByTime();
            orderReportByTimes.setTime(cal.getTime());
            orderReportByTimes.setOrderReports(orderReports);
            orderReportByTimeList.add(orderReportByTimes);
        }

        return orderReportByTimeList;
    }

    @Override
    public Page<Order> filterOrders(Date startSate, Date endDate, String paymentMethod, int page, int size, List<String> sortBy, String order) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(SortedUtil.createListSortOrder(sortBy, order)));

        return orderRepository.filterOrder(startSate, endDate, paymentMethod, pageable);
    }
}
