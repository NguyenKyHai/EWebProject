package com.ute.admin.report;

import com.ute.admin.order.IOrderService;
import com.ute.common.entity.Product;
import com.ute.common.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
        if(Objects.equals(paymentMethod.get(0), String.valueOf(-1))){
            paymentMethod.add("COD");
            paymentMethod.add("VNPAY");
        }
    }


    @GetMapping("/best-selling-product")
    public ResponseEntity<?> findBestSellingProduct(@RequestParam(defaultValue = "10") long quantity,
                                                    @RequestParam(defaultValue = "-1") String startTime,
                                                    @RequestParam(defaultValue = "-1") String endTime,
                                                    @RequestParam(defaultValue = "-1") List<String> paymentMethod)
            throws ParseException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start;
        Date end;

        if(startTime.equals("-1") && endTime.equals("-1")){
            Calendar cal = Calendar.getInstance();
            end = cal.getTime();
            cal.add(Calendar.DATE, -30);
            start = cal.getTime();
        } else {
           start = dateFormat.parse(startTime);
           end = dateFormat.parse(endTime);
        }
        checkPaymentMethod(paymentMethod);
        List<ProductItem> products = orderService.bestSellingProduct(quantity, start, end, paymentMethod);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products-in-stock")
    public ResponseEntity<?> findProductUnSold(@RequestParam(defaultValue = "10") long quantity,
                                               @RequestParam(defaultValue = "-1") String startTime,
                                               @RequestParam(defaultValue = "-1") String endTime,
                                               @RequestParam(defaultValue = "-1") List<String> paymentMethod)
            throws ParseException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start;
        Date end;

        if(startTime.equals("-1") && endTime.equals("-1")){
            Calendar cal = Calendar.getInstance();
            end = cal.getTime();
            cal.add(Calendar.DATE, -30);
            start = cal.getTime();
        } else {
            start = dateFormat.parse(startTime);
            end = dateFormat.parse(endTime);
        }
        checkPaymentMethod(paymentMethod);
        List<ProductItem> products = orderService.productInStock(quantity, start, end, paymentMethod);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @GetMapping("/count-all")
    public ResponseEntity<?> countAll() {
        List<CountItem> countAll = orderService.countAll();
        return new ResponseEntity<>(countAll, HttpStatus.OK);
    }


}
