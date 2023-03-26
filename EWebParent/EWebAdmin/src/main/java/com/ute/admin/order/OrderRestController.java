package com.ute.admin.order;

import com.ute.common.entity.Order;
import com.ute.common.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class OrderRestController {

    @Autowired
    IOrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<?> getListOrder() {
        List<Order> orders = orderService.getListOrder();
        if (orders.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("The list of order is empty"), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/order-detail/{id}")
    public ResponseEntity<?> getListOrder(@PathVariable Integer id) {
        Optional<Order> order = orderService.orderDetail(id);
        if (!order.isPresent()) {
            return new ResponseEntity<>(new ResponseMessage("The list of order details is empty"), HttpStatus.NO_CONTENT);
        }

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

}
