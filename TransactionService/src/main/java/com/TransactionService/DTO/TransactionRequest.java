package com.TransactionService.DTO;

public class TransactionRequest {
	 private Long userId;
	    private Long dealId;
	    private String couponCode;
	    
		public Long getUserId() {
			return userId;
		}
		public void setUserId(Long userId) {
			this.userId = userId;
		}
		public Long getDealId() {
			return dealId;
		}
		public void setDealId(Long dealId) {
			this.dealId = dealId;
		}
		public String getCouponCode() {
			return couponCode;
		}
		public void setCouponCode(String couponCode) {
			this.couponCode = couponCode;
		}
		public TransactionRequest() {
			super();
			// TODO Auto-generated constructor stub
		}
		public TransactionRequest(Long userId, Long dealId, String couponCode) {
			super();
			this.userId = userId;
			this.dealId = dealId;
			this.couponCode = couponCode;
		}

}

