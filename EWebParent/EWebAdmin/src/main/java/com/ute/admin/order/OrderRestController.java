package com.ute.admin.order;

import com.ute.common.entity.Category;
import com.ute.common.entity.Order;
import com.ute.common.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class OrderRestController {

    @Autowired
    IOrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<?> getListOrder() {
        List<Order> orders = orderService.getListOrder();

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/order-detail/{id}")
    public ResponseEntity<?> getListOrder(@PathVariable Integer id) {
        Optional<Order> order = orderService.orderDetail(id);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/order/update-status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Integer id, @RequestBody Map<String, String> param) {
        Optional<Order> order = orderService.findById(id);

        if (!order.isPresent()) {
            return new ResponseEntity<>(new ResponseMessage("Order not found"), HttpStatus.NOT_FOUND);
        }

        String status = param.get("status");
        orderService.updateStatus(id, status);

        return new ResponseEntity<>(new ResponseMessage("Update status successfully"), HttpStatus.OK);
    }

    @GetMapping("/orders/filter")
    public Page<Order> filterAdnSortedOrder(@RequestParam(defaultValue = "2000-01-01") String startDate,
                                            @RequestParam(defaultValue = "2099-01-01") String endDate,
                                            @RequestParam(defaultValue = "-1") List<String> paymentMethod,
                                            @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "20") int size,
                                            @RequestParam(defaultValue = "id") List<String> sortBy,
                                            @RequestParam(defaultValue = "DESC") Sort.Direction order)
            throws ParseException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start = dateFormat.parse(startDate);
        Date end = dateFormat.parse(endDate);
        if(Objects.equals(paymentMethod.get(0), String.valueOf(-1))){
            paymentMethod.add("COD");
            paymentMethod.add("VNPAY");
        }
        return orderService
                .filterOrders(start, end, paymentMethod, page, size, sortBy, order.toString());
    }
}
