package com.ute.shopping.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ute.common.constants.Constants;
import com.ute.common.entity.Customer;
import com.ute.common.entity.Order;
import com.ute.common.request.ShoppingCartRequest;
import com.ute.common.response.ResponseMessage;
import com.ute.shopping.security.CustomUserDetailsService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class OrderRestController {

    @Autowired
    OrderServiceImpl orderServiceImpl;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @PostMapping("/order/create")
    public ResponseEntity<?> createProduct(@RequestBody ShoppingCartRequest cart) {

        Customer customer = customUserDetailsService.getCurrentCustomer();
        if (customer.getId() == null) {
            return new ResponseEntity<>(new ResponseMessage("Customer not found"), HttpStatus.BAD_REQUEST);
        }
        if (Constants.STATUS_BLOCKED.equals(customer.getStatus())) {
            return new ResponseEntity<>(new ResponseMessage("Your account is blocked."),
                                    HttpStatus.UNAUTHORIZED);
        }
        String paymentMethod = cart.getPaymentMethod();
        Order order = new Order();
        order.setDistrictId(cart.getShippingAddress().getDistrictId());
        order.setDistrict(cart.getShippingAddress().getDistrict());
        order.setWardCode(cart.getShippingAddress().getWardCode());
        order.setWard(cart.getShippingAddress().getWard());
        order.setStreet(cart.getShippingAddress().getStreet());
        order.setPhoneNumber(cart.getShippingAddress().getPhoneNumber());
        order.setReceiver(cart.getShippingAddress().getReceiver());
        order.setNote(cart.getNote());
        order.setTotal(cart.getTotalPrice());
        order.setPaymentMethod(paymentMethod);
        order.setCustomer(customer);
        if (paymentMethod != null && paymentMethod.equals(Constants.VNPay)) {
            order.setStatus(Constants.ORDER_STATUS_PAID);
        } else {
            order.setStatus(Constants.ORDER_STATUS_NEW);
        }

        orderServiceImpl.createOrder(cart.getLineItem(), order);

        return new ResponseEntity<>(new ResponseMessage("Create new order successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/order-detail")
    public ResponseEntity<?> getListOrder() {
        Customer customer = customUserDetailsService.getCurrentCustomer();
        if (customer.getId() == null) {
            return new ResponseEntity<>(new ResponseMessage("Customer not found"), HttpStatus.BAD_REQUEST);
        }
        if (Constants.STATUS_BLOCKED.equals(customer.getStatus())) {
            return new ResponseEntity<>(new ResponseMessage("Your account is blocked."),
                    HttpStatus.UNAUTHORIZED);
        }

        List<Order> orders = orderServiceImpl.getOrderDetail(customer.getId());

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping("/order/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable Integer id){
        Optional<Order> order = orderServiceImpl.findById(id);
        if(!order.isPresent()){
            return new ResponseEntity<>(new ResponseMessage("Order not found!"), HttpStatus.OK);
        }
        if(Constants.ORDER_STATUS_NEW.equals(order.get().getStatus())) {
            orderServiceImpl.updateStatus(id, Constants.ORDER_STATUS_RETURNED);
            orderServiceImpl.updateQuantityAfterReturn(order.get());
            return new ResponseEntity<>(new ResponseMessage("The order is canceled!"), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ResponseMessage("You do not cancel the order"), HttpStatus.OK);
        }
    }
}