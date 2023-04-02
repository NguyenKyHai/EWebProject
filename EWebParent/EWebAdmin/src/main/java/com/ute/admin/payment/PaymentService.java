package com.ute.admin.payment;

import com.ute.common.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService implements IPaymentService{

    @Autowired
    IPaymentRepository paymentRepository;


    @Override
    public Optional<Payment> findById(String id) {
        return paymentRepository.findById(id);
    }

    @Override
    public List<Payment> payments() {
        return paymentRepository.findAll();
    }
}
