package com.ute.admin.order;

import com.ute.common.entity.Order;
import com.ute.common.response.*;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IOrderService {
    List<Order> getListOrder();

    Optional<Order> findById(Integer id);

    void updateStatus(Integer id, String status);

    Optional<Order> orderDetail(Integer id);

    List<ProductReport> getProductReportByDay(int day, List<String> paymentMethod);

    List<OrderReport> getOrderReportByDay(int day, List<String> paymentMethod);

    List<CategoryReport> getCategoryReportByDay(int day, List<String> paymentMethod);
    List<OrderReportByTime> getOrderReportByType(String typeReport, List<String> paymentMethod);

    List<OrderReportByTime> getOrderReportByTypePlus(String typeReport, List<String> paymentMethod);

    List<CountItem> countAll();

    List<ProductItem> bestSellingProduct(long sold, Date startTime, Date endTime, List<String> paymentMethod);

    List<ProductItem> productInStock(long sold, Date startTime, Date endTime, List<String> paymentMethod);

    Page<Order> filterOrders(Date startSate, Date endDate, List<String> paymentMethod,
                             int page, int size, List<String> sortBy, String order);
}
