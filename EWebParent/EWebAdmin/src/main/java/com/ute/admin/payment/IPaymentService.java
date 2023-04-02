package com.ute.admin.payment;

import com.ute.common.entity.Payment;

import java.util.List;
import java.util.Optional;

public interface IPaymentService {
    Optional<Payment> findById(String id);
    List<Payment> payments();
}
