package com.ute.admin.order;

import com.ute.common.entity.Order;
import com.ute.common.response.OrderReport;
import com.ute.common.response.OrderReportByTime;
import com.ute.common.response.ProductReport;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IOrderService {
    List<Order> getListOrder();

    Optional<Order> findById(Integer id);

    void updateStatus(Integer id, String status);

    Optional<Order> orderDetail(Integer id);

    List<ProductReport> getProductReportByDay(int day);

    List<OrderReport> getOrderReportByDay(int day) ;

    List<OrderReportByTime> getOrderReportByType(String typeReport) ;

    Page<Order> filterOrders(Date startSate, Date endDate, String paymentMethod,
                                 int page, int size, List<String> sortBy, String order);
}
