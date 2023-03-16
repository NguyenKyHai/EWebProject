package com.ute.shopping.payment;

import com.ute.common.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPaymentRepository extends JpaRepository<Payment,String> {
}
