package com.QuickBites.app.DTO;

public class AgentRejectionRequest {
	
    private String reason;
    
    private boolean allowReapply = false;

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
	public boolean isAllowReapply() {
		return allowReapply;
	}
	public void setAllowReapply(boolean allowReapply) {
		this.allowReapply = allowReapply;
	}
    
}