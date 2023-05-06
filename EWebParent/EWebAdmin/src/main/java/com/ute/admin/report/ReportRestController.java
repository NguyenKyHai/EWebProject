package com.ute.admin.report;

import com.ute.admin.order.IOrderService;
import com.ute.common.entity.Category;
import com.ute.common.response.OrderReport;
import com.ute.common.response.OrderReportByTime;
import com.ute.common.response.ProductReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ReportRestController {
    @Autowired
    IOrderService orderService;

    @GetMapping("/sales-report-by-time")
    public ResponseEntity<?> productsReportByTime(@RequestParam(defaultValue = "1") int day,
                                                  @RequestParam(defaultValue = "-1") List<String> paymentMethod) {
        checkPaymentMethod(paymentMethod);
        List<ProductReport> sales = orderService.getProductReportByDay(day, paymentMethod);
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @GetMapping("/orders-report-by-time")
    public ResponseEntity<?> ordersReportByTime(@RequestParam(defaultValue = "1") int day,
                                                @RequestParam(defaultValue = "-1") List<String> paymentMethod) {
        checkPaymentMethod(paymentMethod);
        List<OrderReport> sales = orderService.getOrderReportByDay(day,paymentMethod);
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @GetMapping("/orders-report-by-type")
    public ResponseEntity<?> ordersReportByType(@RequestParam(defaultValue = "WEEK") String type,
                                                @RequestParam(defaultValue = "-1") List<String> paymentMethod) {
        checkPaymentMethod(paymentMethod);
        List<OrderReportByTime> sales = orderService.getOrderReportByType(type, paymentMethod);
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    private void checkPaymentMethod(List<String> paymentMethod){
        if(Objects.equals(paymentMethod.get(0), "-1")){
            paymentMethod.add("COD");
            paymentMethod.add("VNPAY");
        }
    }

}
