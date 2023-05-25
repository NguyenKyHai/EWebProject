package com.ute.shopping.order;

import com.ute.common.entity.Product;
import com.ute.common.request.LineItem;
import com.ute.common.util.MailUtil;
import com.ute.shopping.product.IProductService;
import com.ute.shopping.util.MailTemplate;
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

import javax.mail.MessagingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class OrderRestController {

    @Autowired
    OrderServiceImpl orderServiceImpl;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    IProductService productService;

    @PostMapping("/order/create")
    public ResponseEntity<?> createProduct(@RequestBody ShoppingCartRequest cart) throws MessagingException {

        Customer customer = customUserDetailsService.getCurrentCustomer();
        if (customer.getId() == null) {
            return new ResponseEntity<>(new ResponseMessage("Customer not found"), HttpStatus.BAD_REQUEST);
        }
        if (customer.isBlockAccount()) {
            return new ResponseEntity<>(new ResponseMessage("Your account is blocked."),
                    HttpStatus.UNAUTHORIZED);
        }
        String paymentMethod = cart.getPaymentMethod();
        Order order = new Order();

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

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
        order.setOrderTime(cal.getTime());
        if (paymentMethod != null && paymentMethod.equals(Constants.VNPAY)) {
            order.setStatus(Constants.ORDER_STATUS_PAID);
        } else {
            order.setStatus(Constants.ORDER_STATUS_NEW);
        }
        orderServiceImpl.createOrder(cart.getLineItem(), order);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(cal.getTime());

        String head = MailTemplate.head;
        String table = MailTemplate.table(strDate);
        String address = cart.getShippingAddress().getStreet() + ", "
                + cart.getShippingAddress().getWard() + ", "
                + cart.getShippingAddress().getDistrict();
        String customerInfo = MailTemplate.customerInfo(cart.getShippingAddress().getReceiver(),
                cart.getShippingAddress().getPhoneNumber(), address);
        StringBuilder orderInfo = new StringBuilder();
        String totalPrice = MailTemplate.totalPrice(cart.getTotalPrice());
        for (LineItem item : cart.getLineItem()
        ) {
            Product product = productService.findById(item.getProductId()).get();
            orderInfo.append(MailTemplate.orderInfo(product.getName(),
                    item.getQuantity(),
                    item.getProductPrice(),
                    item.getShippingFee()));
        }

        MailUtil.sendMail(customer.getEmail(), "Invoice " + strDate,
                head + table + customerInfo + orderInfo + totalPrice);

        return new ResponseEntity<>(new ResponseMessage("Create new order successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/order-detail")
    public ResponseEntity<?> getListOrder() {
        Customer customer = customUserDetailsService.getCurrentCustomer();
        if (customer.getId() == null) {
            return new ResponseEntity<>(new ResponseMessage("Customer not found"), HttpStatus.BAD_REQUEST);
        }
        if (customer.isBlockAccount()) {
            return new ResponseEntity<>(new ResponseMessage("Your account is blocked."),
                    HttpStatus.UNAUTHORIZED);
        }

        List<Order> orders = orderServiceImpl.getOrderDetail(customer.getId());

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping("/order/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable Integer id) {
        Optional<Order> order = orderServiceImpl.findById(id);
        if (!order.isPresent()) {
            return new ResponseEntity<>(new ResponseMessage("Order not found!"), HttpStatus.OK);
        }
        if (Constants.ORDER_STATUS_NEW.equals(order.get().getStatus())) {
            orderServiceImpl.updateStatus(id, Constants.ORDER_STATUS_RETURNED);
            orderServiceImpl.updateQuantityAfterReturn(order.get());
            return new ResponseEntity<>(new ResponseMessage("The order is canceled!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseMessage("You do not cancel the order"), HttpStatus.OK);
        }
    }
}