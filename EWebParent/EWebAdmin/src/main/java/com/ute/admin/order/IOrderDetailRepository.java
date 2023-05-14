package com.ute.admin.order;

import com.ute.common.entity.OrderDetail;
import com.ute.common.response.CountItem;
import com.ute.common.response.OrderReport;
import com.ute.common.response.ProductItem;
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
            + " WHERE d.order.orderTime BETWEEN ?1 AND ?2 AND d.order.paymentMethod in ?3"
            + " Group by d.product.id"
            + " Order by d.id ")
    List<ProductReport> productsReportTimeBetween(Date startTime, Date endTime, List<String> paymentMethod);

    @Query("SELECT (d.product.price * d.quantity + d.shippingFee) as grossSale, "
            + "(d.product.price - d.product.cost) * d.quantity as netSale, "
            + "count (d.id) as orderDetailQuantity, "
            + "count (distinct d.order.id) as orderQuantity "
            + " FROM OrderDetail d "
            + " WHERE d.order.orderTime BETWEEN ?1 AND ?2 AND d.order.paymentMethod in ?3"
            + " Order by d.id ")
    List<OrderReport> orderReportByTimeBetween(Date startTime, Date endTime, List<String> paymentMethod);
    @Query(value = "SELECT " +
            "(select count(*) from users) as totalUsers, " +
            "(select count(*)  from products) as totalProducts, " +
            "(select count(*)  from customers)  as totalCustomers, " +
            "(select count(*) from suppliers)  as totalSuppliers, " +
            "(select count(*) from categories) as totalCategories, " +
            "(select count(*) from orders)  as totalOrders, " +
            "(select count(*) from  order_details) as totalOrderDetails " +
            " FROM dual",nativeQuery = true)
    List<CountItem>countAll();

    @Query("SELECT d.product.name as productName, "
            + "d.product.category.name as categoryName, "
            + "sum(d.quantity) as quantity, "
            + "d.product.sold as totalSold, "
            + "d.product.mainImage as productImage "
            + " FROM OrderDetail d "
            + " WHERE d.order.orderTime BETWEEN ?2 AND ?3 "
            + " AND d.order.paymentMethod in ?4"
            + " GROUP BY d.product.id "
            + " HAVING sum(d.quantity) >= ?1 "
            + " Order by d.product.id ")
    List<ProductItem> bestSellingProduct(long sold, Date startTime, Date endTime, List<String> paymentMethod);


    @Query("SELECT d.product.name as productName, "
            + "d.product.category.name as categoryName, "
            + "sum(d.quantity) as quantity, "
            + "d.product.sold as totalSold, "
            + "d.product.mainImage as productImage "
            + " FROM OrderDetail d "
            + " WHERE d.order.orderTime BETWEEN ?2 AND ?3 "
            + " AND d.order.paymentMethod in ?4"
            + " GROUP BY d.product.id "
            + " HAVING sum(d.quantity) <= ?1 "
            + " Order by d.product.id ")
    List<ProductItem> productInStock(long sold, Date startTime, Date endTime, List<String> paymentMethod);

    @Query("SELECT p.name as productName, "
            + "p.category.name as categoryName, "
            + "p.sold as totalSold, "
            + "p.mainImage as productImage "
            + " FROM Product p left join OrderDetail d on p.id = d.product.id"
            + " WHERE  d.quantity is null"
            + " Order by p.id ")
    List<ProductItem> productUnSold();
}
