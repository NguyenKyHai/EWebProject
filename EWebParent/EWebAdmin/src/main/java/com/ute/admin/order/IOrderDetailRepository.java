package com.ute.admin.order;

import com.ute.common.entity.OrderDetail;
import com.ute.common.response.OrderReport;
import com.ute.common.response.ProductReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Date;
import java.util.List;

public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Integer>{
    @Query("SELECT d.product.price * d.quantity as totalPrice, "
            + "d.product.cost * d.quantity as totalCost, "
            + "(d.product.price - d.product.cost) * d.quantity as profit, "
            + "sum (d.quantity) as quantity, "
            + "d.product.name as productName,  "
            + "d.product.mainImage as productImage,"
            + "d.product.category.name as categoryName"
            + " FROM OrderDetail d "
            + " WHERE d.order.orderTime BETWEEN ?1 AND ?2 "
            + "Group by d.product.id ")
    List<ProductReport> productsReportTimeBetween(Date startTime, Date endTime);

    @Query("SELECT (d.product.price * d.quantity + d.shippingFee) as grossSale, "
            + "(d.product.price - d.product.cost) * d.quantity as netSale, "
            + "count (d.id) as orderDetailQuantity, "
            + "count (distinct d.order.id) as orderQuantity "
            + " FROM OrderDetail d "
            + " WHERE d.order.orderTime BETWEEN ?1 AND ?2 ")
    List<OrderReport> orderReportByTimeBetween(Date startTime, Date endTime);

}
