package com.ute.admin.order;

import com.ute.common.constants.Constants;
import com.ute.common.entity.Order;
import com.ute.common.entity.OrderDetail;
import com.ute.common.response.ReportItemResponse;
import com.ute.common.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @GetMapping("/order-by-time")
    public ResponseEntity<?> findOrderByTime(@RequestParam(defaultValue = "2020-01-11") String startTime,
                                             @RequestParam(defaultValue = "2020-01-11") String endTime)
            throws ParseException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start = dateFormat.parse(startTime);
        Date end = dateFormat.parse(endTime);

        List<ReportItemResponse> orders = orderService.findByOrderTimeBetween(start, end);

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/best-selling-product")
    public ResponseEntity<?> findBestSellingProduct() throws ParseException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse("2023-01-12");
        Date endDate = dateFormat.parse("2023-04-12");
        Pageable page = PageRequest.of(0, 5);
        List<OrderDetail> orders = orderService.bestSellingProduct(startDate, endDate, page);

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
