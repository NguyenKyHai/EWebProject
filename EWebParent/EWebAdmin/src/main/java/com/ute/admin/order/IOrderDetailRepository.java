package com.ute.admin.order;


import com.ute.common.entity.OrderDetail;
import com.ute.common.response.ReportItemResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Date;
import java.util.List;

public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Integer>{

    @Query("SELECT COUNT(d) FROM OrderDetail d "
            + " WHERE d.product.id = ?1 AND d.order.customer.id = ?2 AND"
            + " d.order.status = ?3")
    Long countByProductAndCustomerAndOrderStatus(Integer productId, Integer customerId, String status);

    @Query("SELECT d.order.id as orderId, d.product.name as name, d.product.price as price, "
            + "d.product.cost as cost, d.product.quantity as nofQuantity, d.product.category.name as categoryName, "
            + "d.product.sold as sold, d.quantity as quantity, d.productPrice as productPrice,  "
            + "d.product.discountPercent as discountPercent"
            + " FROM OrderDetail d"
            + " WHERE d.order.orderTime BETWEEN ?1 AND ?2 ORDER BY d.order.orderTime ASC")
    List<ReportItemResponse> findByOrderTimeBetween(Date startTime, Date endTime);

    @Query("Select d from OrderDetail d where d.order.orderTime BETWEEN ?1 AND ?2 ORDER BY d.quantity DESC")
    List<OrderDetail>bestSellingProduct(Date startTime, Date endTime, Pageable pageable);
}
