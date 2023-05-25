package com.ute.admin.order;

import com.ute.common.entity.OrderDetail;
import com.ute.common.response.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Date;
import java.util.List;

public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Integer>{
    @Query("SELECT sum(d.order.total) as totalPrice, "
            + " sum(d.product.cost * d.quantity) as totalCost, "
            + "sum(d.productPrice - d.product.cost) * d.quantity as profit, "
            + "sum (d.quantity) as quantity, "
            + "d.product.name as productName,  "
            + "d.product.mainImage as productImage,"
            + "d.product.category.name as categoryName"
            + " FROM OrderDetail d "
            + " WHERE d.order.orderTime BETWEEN ?1 AND ?2 AND d.order.paymentMethod in ?3"
            + " Group by d.product.id"
            + " Order by d.id ")
    List<ProductReport> productsReportTimeBetween(Date startTime, Date endTime, List<String> paymentMethod);

    @Query("SELECT sum(d.order.total) as grossSale, "
            + "sum((d.productPrice - d.product.cost) * d.quantity) as netSale, "
            + "count (d.id) as orderDetailQuantity, "
            + "count (distinct d.order.id) as orderQuantity "
            + " FROM OrderDetail d "
            + " WHERE d.order.orderTime BETWEEN ?1 AND ?2 AND d.order.paymentMethod in ?3"
            + " Order by d.id ")
    List<OrderReport> orderReportByTimeBetween(Date startTime, Date endTime, List<String> paymentMethod);

    @Query("SELECT d.product.category.id as categoryId, "
            + " d.product.category.name as categoryName, "
            + " sum(d.product.cost * d.quantity + d.shippingFee) as grossSale, "
            + " sum(d.productPrice - d.product.cost) * d.quantity as netSale, "
            + "count (d.id) as productQuantity, "
            + "count (distinct d.order.id) as orderQuantity "
            + " FROM OrderDetail d "
            + " WHERE d.order.orderTime BETWEEN ?1 AND ?2 AND d.order.paymentMethod in ?3"
            + " GROUP BY d.product.category.id"
            + " Order by d.id ")
    List<CategoryReport> categoryReportByOrder(Date startTime, Date endTime, List<String> paymentMethod);

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

    @Query("SELECT d.product.id as id, "
            + "d.product.name as productName, "
            + "d.product.category.name as categoryName, "
            + "d.product.price as productPrice, "
            + "d.product.discountPercent as discountPercent, "
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


    @Query("SELECT d.product.id as id, "
            + "d.product.name as productName, "
            + "d.product.category.name as categoryName, "
            + "d.product.price as productPrice, "
            + "d.product.discountPercent as discountPercent, "
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

    @Query("SELECT p.id as id, "
            + "p.name as productName, "
            + "p.category.name as categoryName, "
            + "p.discountPercent as discountPercent, "
            + "p.price as productPrice, "
            + "p.sold as totalSold, "
            + "p.mainImage as productImage "
            + " FROM Product p left join OrderDetail d on p.id = d.product.id"
            + " WHERE  d.quantity is null"
            + " Order by p.id ")
    List<ProductItem> productUnSold();
}
