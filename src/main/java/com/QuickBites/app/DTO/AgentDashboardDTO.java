package com.QuickBites.app.DTO;

import java.math.BigDecimal;

public class AgentDashboardDTO {
	private long activeDeliveriesCount;
	private BigDecimal totalEarningsToday;
	private long completedDeliveriesToday;
	private long assignedDeliveriesToday;

	public AgentDashboardDTO() {
	}

	public AgentDashboardDTO(long activeDeliveriesCount, BigDecimal totalEarningsToday, long completedDeliveriesToday,
			long assignedDeliveriesToday) {
		this.activeDeliveriesCount = activeDeliveriesCount;
		this.totalEarningsToday = totalEarningsToday;
		this.completedDeliveriesToday = completedDeliveriesToday;
		this.assignedDeliveriesToday = assignedDeliveriesToday;
	}

	public long getActiveDeliveriesCount() {
		return activeDeliveriesCount;
	}

	public void setActiveDeliveriesCount(long activeDeliveriesCount) {
		this.activeDeliveriesCount = activeDeliveriesCount;
	}

	public BigDecimal getTotalEarningsToday() {
		return totalEarningsToday;
	}

	public void setTotalEarningsToday(BigDecimal totalEarningsToday) {
		this.totalEarningsToday = totalEarningsToday;
	}

	public long getCompletedDeliveriesToday() {
		return completedDeliveriesToday;
	}

	public void setCompletedDeliveriesToday(long completedDeliveriesToday) {
		this.completedDeliveriesToday = completedDeliveriesToday;
	}

	public long getAssignedDeliveriesToday() {
		return assignedDeliveriesToday;
	}

	public void setAssignedDeliveriesToday(long assignedDeliveriesToday) {
		this.assignedDeliveriesToday = assignedDeliveriesToday;
	}

}
