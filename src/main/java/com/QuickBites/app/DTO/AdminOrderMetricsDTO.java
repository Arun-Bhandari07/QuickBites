package com.QuickBites.app.DTO;

import java.math.BigDecimal;

public class AdminOrderMetricsDTO {

	private long totalOrders;
    private long ordersToday;
    private long ordersYesterday;
    private long ordersThisWeek;
    private long ordersThisMonth;
	private Long cancelledOrders;
	private Long deliveredOrders;
	
    private BigDecimal revenueToday = BigDecimal.ZERO;
    private BigDecimal revenueYesterday = BigDecimal.ZERO;
    private BigDecimal revenueThisWeek = BigDecimal.ZERO;
    private BigDecimal revenueThisMonth = BigDecimal.ZERO;
    
    private long stripePaymentsToday;
    private long codPaymentsToday;
    private  long stripePaymentsTotal;
    private long codPaymentsTotal;
	
	public Long getTotalOrders() {
		return totalOrders;
	}
	public void setTotalOrders(Long totalOrders) {
		this.totalOrders = totalOrders;
	}
	public Long getCancelledOrders() {
		return cancelledOrders;
	}
	public void setCancelledOrders(Long cancelledOrders) {
		this.cancelledOrders = cancelledOrders;
	}
	public Long getDeliveredOrders() {
		return deliveredOrders;
	}
	public void setDeliveredOrders(Long deliveredOrders) {
		this.deliveredOrders = deliveredOrders;
	}
	public long getOrdersToday() {
		return ordersToday;
	}
	public void setOrdersToday(long ordersToday) {
		this.ordersToday = ordersToday;
	}
	public long getOrdersYesterday() {
		return ordersYesterday;
	}
	public void setOrdersYesterday(long ordersYesterday) {
		this.ordersYesterday = ordersYesterday;
	}
	public long getOrdersThisWeek() {
		return ordersThisWeek;
	}
	public void setOrdersThisWeek(long ordersThisWeek) {
		this.ordersThisWeek = ordersThisWeek;
	}
	public long getOrdersThisMonth() {
		return ordersThisMonth;
	}
	public void setOrdersThisMonth(long ordersThisMonth) {
		this.ordersThisMonth = ordersThisMonth;
	}
	public BigDecimal getRevenueToday() {
		return revenueToday;
	}
	public void setRevenueToday(BigDecimal revenueToday) {
		this.revenueToday = revenueToday;
	}
	public BigDecimal getRevenueYesterday() {
		return revenueYesterday;
	}
	public void setRevenueYesterday(BigDecimal revenueYesterday) {
		this.revenueYesterday = revenueYesterday;
	}
	public BigDecimal getRevenueThisWeek() {
		return revenueThisWeek;
	}
	public void setRevenueThisWeek(BigDecimal revenueThisWeek) {
		this.revenueThisWeek = revenueThisWeek;
	}
	public BigDecimal getRevenueThisMonth() {
		return revenueThisMonth;
	}
	public void setRevenueThisMonth(BigDecimal revenueThisMonth) {
		this.revenueThisMonth = revenueThisMonth;
	}
	public long getStripePaymentsToday() {
		return stripePaymentsToday;
	}
	public void setStripePaymentsToday(long stripePaymentsToday) {
		this.stripePaymentsToday = stripePaymentsToday;
	}
	public long getCodPaymentsToday() {
		return codPaymentsToday;
	}
	public void setCodPaymentsToday(long codPaymentsToday) {
		this.codPaymentsToday = codPaymentsToday;
	}
	public void setTotalOrders(long totalOrders) {
		this.totalOrders = totalOrders;
	}
	public long getStripePaymentsTotal() {
		return stripePaymentsTotal;
	}
	public void setStripePaymentsTotal(long stripePaymentsTotal) {
		this.stripePaymentsTotal = stripePaymentsTotal;
	}
	public long getCodPaymentsTotal() {
		return codPaymentsTotal;
	}
	public void setCodPaymentsTotal(long codPaymentsTotal) {
		this.codPaymentsTotal = codPaymentsTotal;
	}
	
	
}
