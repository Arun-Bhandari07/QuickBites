package com.QuickBites.app;

import java.math.BigDecimal;

public interface AdminOrderMetrics {

	long getTotalOrders();
    long getOrdersToday();
    long getOrdersYesterday();
    long getOrdersThisWeek();
    long getOrdersThisMonth();

    BigDecimal getRevenueToday();
    BigDecimal getRevenueYesterday();
    BigDecimal getRevenueThisWeek();
    BigDecimal getRevenueThisMonth();

    long getStripePaymentsToday();
    long getCodPaymentsToday();
    long getStripePaymentsTotal();
    long getCodPaymentsTotal();
}
