package com.ute.admin.payment;

import com.ute.common.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PaymentRestController {

    @Autowired
    IPaymentService paymentService;

    @GetMapping("payments")
    public ResponseEntity<?> paymentInformation() {
        List<Payment> payments = paymentService.payments();
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @GetMapping("payment/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable String id) {
        Optional<Payment> payment = paymentService.findById(id);

        return new ResponseEntity<>(payment, HttpStatus.OK);
    }
}