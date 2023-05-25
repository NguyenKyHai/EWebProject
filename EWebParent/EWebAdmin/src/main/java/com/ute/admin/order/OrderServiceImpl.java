package com.ute.admin.order;

import com.ute.common.constants.Constants;
import com.ute.common.entity.Order;
import com.ute.common.response.*;
import com.ute.common.util.SortedUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class OrderServiceImpl implements IOrderService {

    @Autowired
    IOrderRepository orderRepository;

    @Autowired
    IOrderDetailRepository orderDetailRepository;

    @Override
    public List<Order> getListOrder() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> findById(Integer id) {
        return orderRepository.findById(id);
    }

    @Override
    public void updateStatus(Integer id, String status) {
        orderRepository.updateOrderStatus(id, status);
    }

    @Override
    public Optional<Order> orderDetail(Integer id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<ProductReport> getProductReportByDay(int day, List<String> paymentMethod) {
        Calendar cal = Calendar.getInstance();
        Date endTime = cal.getTime();
        cal.add(Calendar.DATE, -day);
        Date startTime = cal.getTime();
        return orderDetailRepository.productsReportTimeBetween(startTime, endTime, paymentMethod);
    }

    @Override
    public List<OrderReport> getOrderReportByDay(int day, List<String> paymentMethod) {
        Calendar cal = Calendar.getInstance();
        Date endTime = cal.getTime();
        cal.add(Calendar.DATE, -day);
        Date startTime = cal.getTime();
        return orderDetailRepository.orderReportByTimeBetween(startTime, endTime, paymentMethod);
    }

    @Override
    public List<CategoryReport> getCategoryReportByDay(int day, List<String> paymentMethod) {
        Calendar cal = Calendar.getInstance();
        Date endTime = cal.getTime();
        cal.add(Calendar.DATE, -day);
        Date startTime = cal.getTime();
        return orderDetailRepository.categoryReportByOrder(startTime, endTime, paymentMethod);
    }

    @Override
    public List<OrderReportByTime> getOrderReportByType(String typeReport, List<String> paymentMethod) {

        if (typeReport == null) {
            throw new RuntimeException("Type report not found");
        }
        List<OrderReportByTime> orderReportByTimeList = new ArrayList<>();
        List<OrderReport> orderReports = new ArrayList<>();

        Calendar cal = checkTimeByType(typeReport);


        if (Constants.TYPE_REPORT_WEEK.equals(typeReport)) {

            for (int i = 1; i <= 7; i++) {
                Date startTime = cal.getTime();
                cal.add(Calendar.DATE, 1);
                Date endTime = cal.getTime();

                orderReports = orderDetailRepository.orderReportByTimeBetween(startTime, endTime, paymentMethod);
                OrderReportByTime orderReportByTimes = new OrderReportByTime();
                orderReportByTimes.setTime(endTime);
                orderReportByTimes.setOrderReports(orderReports);
                orderReportByTimeList.add(orderReportByTimes);
            }
        }else if (Constants.TYPE_REPORT_MONTH.equals(typeReport)) {

            for (int i = 1; i <= 4; i++) {
                Date startTime = cal.getTime();
                cal.add(Calendar.DATE, 7);
                Date endTime = cal.getTime();

                orderReports = orderDetailRepository.orderReportByTimeBetween(startTime, endTime, paymentMethod);
                OrderReportByTime orderReportByTimes = new OrderReportByTime();
                orderReportByTimes.setTime(endTime);
                orderReportByTimes.setOrderReports(orderReports);
                orderReportByTimeList.add(orderReportByTimes);
            }
        } else if (Constants.TYPE_REPORT_YEAR.equals(typeReport)) {

            for (int i = 1; i <= 12; i++) {
                Date startTime = cal.getTime();
                cal.add(Calendar.DATE, 30);
                Date endTime = cal.getTime();

                orderReports = orderDetailRepository.orderReportByTimeBetween(startTime, endTime, paymentMethod);
                OrderReportByTime orderReportByTimes = new OrderReportByTime();
                orderReportByTimes.setTime(endTime);
                orderReportByTimes.setOrderReports(orderReports);
                orderReportByTimeList.add(orderReportByTimes);
            }
        }  else {
            OrderReportByTime orderReportByTimes = new OrderReportByTime();
            orderReportByTimes.setTime(cal.getTime());
            orderReportByTimes.setOrderReports(orderReports);
            orderReportByTimeList.add(orderReportByTimes);
        }

        return orderReportByTimeList;
    }

    @Override
    public List<OrderReportByTime> getOrderReportByTypePlus(String typeReport, List<String> paymentMethod) {
        if (typeReport == null) {
            throw new RuntimeException("Type report not found");
        }
        List<OrderReportByTime> orderReportByTimeList = new ArrayList<>();
        List<OrderReport> orderReports = new ArrayList<>();

        Calendar cal = checkTimeByType(typeReport);
        Date startTime = cal.getTime();

        if (Constants.TYPE_REPORT_WEEK.equals(typeReport)) {

            for (int i = 1; i <= 7; i++) {
                cal.add(Calendar.DATE, 1);
                Date endTime = cal.getTime();

                orderReports = orderDetailRepository.orderReportByTimeBetween(startTime, endTime, paymentMethod);
                OrderReportByTime orderReportByTimes = new OrderReportByTime();
                orderReportByTimes.setTime(endTime);
                orderReportByTimes.setOrderReports(orderReports);
                orderReportByTimeList.add(orderReportByTimes);
            }
        }else if (Constants.TYPE_REPORT_MONTH.equals(typeReport)) {

            for (int i = 1; i <= 4; i++) {
                cal.add(Calendar.DATE, 7);
                Date endTime = cal.getTime();

                orderReports = orderDetailRepository.orderReportByTimeBetween(startTime, endTime, paymentMethod);
                OrderReportByTime orderReportByTimes = new OrderReportByTime();
                orderReportByTimes.setTime(endTime);
                orderReportByTimes.setOrderReports(orderReports);
                orderReportByTimeList.add(orderReportByTimes);
            }
        } else if (Constants.TYPE_REPORT_YEAR.equals(typeReport)) {

            for (int i = 1; i <= 12; i++) {
                cal.add(Calendar.DATE, 30);
                Date endTime = cal.getTime();

                orderReports = orderDetailRepository.orderReportByTimeBetween(startTime, endTime, paymentMethod);
                OrderReportByTime orderReportByTimes = new OrderReportByTime();
                orderReportByTimes.setTime(endTime);
                orderReportByTimes.setOrderReports(orderReports);
                orderReportByTimeList.add(orderReportByTimes);
            }
        }  else {
            OrderReportByTime orderReportByTimes = new OrderReportByTime();
            orderReportByTimes.setTime(cal.getTime());
            orderReportByTimes.setOrderReports(orderReports);
            orderReportByTimeList.add(orderReportByTimes);
        }

        return orderReportByTimeList;
    }


    @Override
    public List<CountItem> countAll() {
        return orderDetailRepository.countAll();
    }

    @Override
    public List<ProductItem> bestSellingProduct(long sold, Date startTime, Date endTime, List<String> paymentMethod) {
        return orderDetailRepository.bestSellingProduct(sold, startTime, endTime, paymentMethod);
    }

    @Override
    public List<ProductItem> productInStock(long sold, Date startTime, Date endTime, List<String> paymentMethod) {
        List<ProductItem> productItemList = orderDetailRepository.productUnSold();

        if (sold > 0) {
            List<ProductItem> itemList = orderDetailRepository.productInStock(sold, startTime, endTime, paymentMethod);
            productItemList.addAll(itemList);
        }
        return productItemList;
    }


    @Override
    public Page<Order> filterOrders(Date startSate, Date endDate, List<String> paymentMethod, int page, int size, List<String> sortBy, String order) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(SortedUtil.createListSortOrder(sortBy, order)));

        return orderRepository.filterOrder(startSate, endDate, paymentMethod, pageable);
    }

    private Calendar checkTimeByType(String type){
        Calendar cal = Calendar.getInstance();
        if(Constants.TYPE_REPORT_WEEK.equals(type)){
            cal.add(Calendar.DATE, -6);
        } else if(Constants.TYPE_REPORT_MONTH.equals(type)){
            cal.add(Calendar.DATE, -27);
        }
        else if(Constants.TYPE_REPORT_YEAR.equals(type)){
            cal.add(Calendar.DATE, -359);
        }

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal;
    }
}
