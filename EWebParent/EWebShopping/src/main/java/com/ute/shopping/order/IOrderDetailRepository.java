package com.ute.shopping.order;

import com.ute.common.response.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ute.common.entity.OrderDetail;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Integer>{

    @Query("SELECT COUNT(d) FROM OrderDetail d "
            + " WHERE d.product.id = ?1 AND d.order.customer.id = ?2 AND"
            + " d.order.status = ?3")
    Long countByProductAndCustomerAndOrderStatus(Integer productId, Integer customerId, String status);

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

}
