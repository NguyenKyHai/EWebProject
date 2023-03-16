package com.ute.shopping.payment;

import com.ute.common.entity.Payment;

import java.util.Optional;

public interface IPaymentService {
    void save(Payment payment);
    Optional<Payment> findById(String id);
}
