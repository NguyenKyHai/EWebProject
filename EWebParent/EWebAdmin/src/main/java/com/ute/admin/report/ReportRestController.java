package com.ute.admin.report;

import com.ute.admin.order.IOrderDetailRepository;
import com.ute.admin.order.IOrderService;
import com.ute.common.response.OrderReport;
import com.ute.common.response.ProductReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ReportRestController {
    @Autowired
    IOrderService orderService;

    @GetMapping("/products-report-by-time")
    public ResponseEntity<?> productsReportByTime(@RequestParam(defaultValue = "1") int day) {
        List<ProductReport> sales = orderService.getProductReportByDay(day);
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @GetMapping("/orders-report-by-time")
    public ResponseEntity<?> ordersReportByTime(@RequestParam(defaultValue = "1") int day) {
        List<OrderReport> sales = orderService.getOrderReportByDay(day);
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

}
