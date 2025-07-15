package com.QuickBites.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.QuickBites.app.AdminOrderMetrics;
import com.QuickBites.app.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserUserName(String username); // for order history

    Optional<Order> findByIdAndUserUserName(Long orderId, String username);
    
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
}