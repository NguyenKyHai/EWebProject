package com.ute.shopping.payment;

import com.ute.common.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService implements IPaymentService{

    @Autowired
    IPaymentRepository paymentRepository;

    @Override
    public void save(Payment payment) {
        paymentRepository.save(payment);
    }

    @Override
    public Optional<Payment> findById(String id) {
        return paymentRepository.findById(id);
    }
}
