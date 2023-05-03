package com.ute.common.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderReportByTime {
    private Date time;
    private List<OrderReport> orderReports = new ArrayList<>();

    public OrderReportByTime() {
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public List<OrderReport> getOrderReports() {
        return orderReports;
    }

    public void setOrderReports(List<OrderReport> orderReports) {
        this.orderReports = orderReports;
    }
}
