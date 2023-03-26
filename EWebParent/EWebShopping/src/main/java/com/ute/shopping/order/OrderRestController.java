package com.ute.shopping.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ute.common.constants.Constants;
import com.ute.common.entity.Customer;
import com.ute.common.entity.Order;
import com.ute.common.request.ShoppingCart;
import com.ute.common.response.ResponseMessage;
import com.ute.shopping.security.CustomUserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class OrderRestController {

    @Autowired
    OrderService orderService;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @PostMapping("/order/create")
    public ResponseEntity<?> createProduct(@RequestBody ShoppingCart cart) {

        Customer customer = customUserDetailsService.getCurrentCustomer();
        if (customer.getId() == null) {
            return new ResponseEntity<>(new ResponseMessage("Customer not found"), HttpStatus.BAD_REQUEST);
        }
        if (customer.getSessionString() == null) {
            return new ResponseEntity<>(new ResponseMessage("Please login to continue"), HttpStatus.UNAUTHORIZED);
        }
        String paymentMethod = cart.getPaymentMethod();
        Order order = new Order();
        order.setDistrict(cart.getShippingAddress().getDistrict());
        order.setStreet(cart.getShippingAddress().getStreet());
        order.setPhoneNumber(cart.getShippingAddress().getPhoneNumber());
        order.setNote(cart.getNote());
        order.setTotal(cart.getTotalPrice());
        order.setPaymentMethod(paymentMethod);
        order.setCustomer(customer);
        if (paymentMethod != null && paymentMethod.equals(Constants.VNPay)) {
            order.setStatus(Constants.ORDER_STATUS_PAID);
        } else {
            order.setStatus(Constants.ORDER_STATUS_NEW);
        }

        orderService.createOrder(cart.getLineItem(), order);

        return new ResponseEntity<>(new ResponseMessage("Create new order successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/order-detail")
    public ResponseEntity<?> getListOrder(HttpServletRequest request) {
        Customer customer = customUserDetailsService.getCurrentCustomer();
        if (customer.getId() == null) {
            return new ResponseEntity<>(new ResponseMessage("Customer not found"), HttpStatus.BAD_REQUEST);
        }
        if (customer.getSessionString() == null) {
            return new ResponseEntity<>(new ResponseMessage("Please login to continue"), HttpStatus.UNAUTHORIZED);
        }

        List<Order> orders = orderService.orderDetail(customer.getId());

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

}
