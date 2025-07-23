package com.QuickBites.app.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.QuickBites.app.AdminOrderMetrics;
import com.QuickBites.app.entities.Order;
import com.QuickBites.app.enums.DeliveryStatus;
import com.QuickBites.app.enums.PaymentMethod;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserUserName(String username); // for order history

    Optional<Order> findByIdAndUserUserName(Long orderId, String username);
    
    @Query("SELECT o from Order o "
    		+ "WHERE o.kitchenStatus IN ('TO_BE_PREPARED','PREPARING','READY')"
    		+ "AND o.deliveryStatus IN ('TO_BE_DELIVERED','CANCELLED')"
    		+ "ORDER BY o.createdAt DESC")
    List<Order> findOrderForKitchenDashboard();
    
    @Query(value = "SELECT COUNT(*) AS total_orders, " +
    	       "COUNT(*) FILTER (WHERE DATE(created_at) = CURRENT_DATE) AS orders_today, " +
    	       "COUNT(*) FILTER (WHERE DATE(created_at) = CURRENT_DATE - INTERVAL '1 day') AS orders_yesterday, " +
    	       "COUNT(*) FILTER (WHERE created_at >= CURRENT_DATE - EXTRACT(DOW FROM CURRENT_DATE)::int * INTERVAL '1 day' AND created_at < CURRENT_DATE + INTERVAL '1 day') AS orders_this_week, " +
    	       "COUNT(*) FILTER (WHERE created_at >= date_trunc('month', CURRENT_DATE) AND created_at < date_trunc('month', CURRENT_DATE) + INTERVAL '1 month') AS orders_this_month, " +
    	       "COALESCE(SUM(total_amount) FILTER (WHERE DATE(created_at) = CURRENT_DATE), 0) AS revenue_today, " +
    	       "COALESCE(SUM(total_amount) FILTER (WHERE DATE(created_at) = CURRENT_DATE - INTERVAL '1 day'), 0) AS revenue_yesterday, " +
    	       "COALESCE(SUM(total_amount) FILTER (WHERE created_at >= CURRENT_DATE - EXTRACT(DOW FROM CURRENT_DATE)::int * INTERVAL '1 day' AND created_at < CURRENT_DATE + INTERVAL '1 day'), 0) AS revenue_this_week, " +
    	       "COALESCE(SUM(total_amount) FILTER (WHERE created_at >= date_trunc('month', CURRENT_DATE) AND created_at < date_trunc('month', CURRENT_DATE) + INTERVAL '1 month'), 0) AS revenue_this_month, " +
    	       "COUNT(*) FILTER (WHERE payment_method = 'STRIPE' AND DATE(created_at) = CURRENT_DATE) AS stripe_payments_today, " +
    	       "COUNT(*) FILTER (WHERE payment_method = 'COD' AND DATE(created_at) = CURRENT_DATE) AS cod_payments_today, " +
    	       "COUNT(*) FILTER (WHERE payment_method = 'STRIPE') AS stripe_payments_total, " +
    	       "COUNT(*) FILTER (WHERE payment_method = 'COD') AS cod_payments_total " +
    	       "FROM orders", nativeQuery = true)
    	AdminOrderMetrics fetchAllOrderMetrics();
    
    long countByAssignedAgentIdAndDeliveryStatusNotIn(
            @Param("agentId") Long agentId,
            @Param("statuses") List<DeliveryStatus> statuses
    );
    
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0.0) FROM Order o " +
            "WHERE o.assignedAgent.id = :agentId " +
            "AND o.deliveryStatus = :deliveredStatus " +
            "AND o.paymentMethod = :codPaymentMethod " +
    		"AND FUNCTION('DATE', o.createdAt) = CURRENT_DATE")
     BigDecimal sumTotalEarningsTodayForAgent(
             @Param("agentId") Long agentId,
             @Param("deliveredStatus") DeliveryStatus deliveredStatus,
             @Param("codPaymentMethod") PaymentMethod codPaymentMethod
     );
    
    @Query("SELECT COUNT(o) FROM Order o " +
            "WHERE o.assignedAgent.id = :agentId " +
            "AND o.deliveryStatus = :deliveredStatus " +
    		 "AND FUNCTION('DATE', o.createdAt) = CURRENT_DATE")
     long countCompletedDeliveriesTodayForAgent( 
             @Param("agentId") Long agentId,
             @Param("deliveredStatus") DeliveryStatus deliveredStatus
     );
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.assignedAgent.id = :agentId AND FUNCTION('DATE', o.createdAt) = CURRENT_DATE")
    long countAssignedDeliveriesTodayForAgent(@Param("agentId") Long agentId);
    
    List<Order> findByAssignedAgentIdAndDeliveryStatusOrderByCreatedAtDesc(
            @Param("agentId") Long agentId,
            @Param("deliveredStatus") DeliveryStatus deliveredStatus
    );

    Optional<Order> findByIdAndAssignedAgentId(
            @Param("orderId") Long orderId,
            @Param("agentId") Long agentId
    );
}