package com.ute.admin.order;

import com.ute.common.entity.Order;
import com.ute.common.entity.OrderDetail;
import com.ute.common.entity.Product;
import com.ute.common.response.ReportItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IOrderService {
    List<Order> getListOrder();

    Optional<Order> findById(Integer id);

    void updateStatus(Integer id, String status);

    Optional<Order> orderDetail(Integer id);

    List<ReportItemResponse> findByOrderTimeBetween(Date startTime, Date endTime);

    Page<Order> filterOrders(Date startSate, Date endDate, String paymentMethod,
                                 int page, int size, List<String> sortBy, String order);
}
