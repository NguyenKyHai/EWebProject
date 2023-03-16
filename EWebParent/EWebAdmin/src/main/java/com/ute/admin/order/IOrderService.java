package com.ute.admin.order;

import com.ute.common.entity.Order;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    List<Order> getListOrder();

    Optional<Order> findById(Integer id);
    void updateStatus(Integer id, String status);
}
