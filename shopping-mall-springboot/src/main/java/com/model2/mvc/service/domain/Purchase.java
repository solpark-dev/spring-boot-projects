package com.model2.mvc.service.domain;

import java.sql.Date;
import java.util.List;

/**
 * Purchase 도메인 객체 (NEW_ORDERS 기반)
 * 주문 정보를 담는 클래스
 */
public class Purchase {
	
	// ========================================
	// NEW_ORDERS 테이블 필드
	// ========================================
	private int tranNo;              // order_no (주문번호) - 기존 이름 유지
	private User buyer;              // buyer_user_no (구매자)
	private int addressId;           // address_id (배송지 ID)
	private int totalPrice;          // total_price (총 금액)
	private String tranCode;         // order_status_code (주문상태) - 기존 이름 유지
	private Date orderDate;          // order_date (주문일)
	
	// ========================================
	// 하위 호환성을 위한 추가 필드 (JOIN으로 조회)
	// ========================================
	private Product purchaseProd;    // 대표 상품 (단일 상품 구매 시)
	private List<Product> orderProducts; // 주문 상품 목록 (다중 상품 구매 시)
	
	// user_addresses 테이블에서 JOIN으로 가져올 배송지 정보
	private String receiverName;     // 수령인 이름
	private String receiverPhone;    // 수령인 전화번호
	private String dlvyAddr;         // 배송 주소
	
	// new_payments 테이블에서 JOIN으로 가져올 결제 정보
	private String paymentOption;    // payment_method
	
	// ========================================
	// 기존 필드 (사용 안함 - 호환성 유지)
	// ========================================
	private String dlvyRequest;      // 배송 요청사항 (현재 DB에 없음)
	private String dlvyDate;         // 배송일 (현재 DB에 없음)
	
	
	public Purchase(){
	}
	
	// ========================================
	// Getters & Setters
	// ========================================
	
	public int getTranNo() {
		return tranNo;
	}
	
	public void setTranNo(int tranNo) {
		this.tranNo = tranNo;
	}
	
	public User getBuyer() {
		return buyer;
	}
	
	public void setBuyer(User buyer) {
		this.buyer = buyer;
	}
	
	public int getAddressId() {
		return addressId;
	}
	
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}
	
	public int getTotalPrice() {
		return totalPrice;
	}
	
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public String getTranCode() {
		return tranCode;
	}
	
	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}
	
	public Date getOrderDate() {
		return orderDate;
	}
	
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	
	public Product getPurchaseProd() {
		return purchaseProd;
	}
	
	public void setPurchaseProd(Product purchaseProd) {
		this.purchaseProd = purchaseProd;
	}
	
	public List<Product> getOrderProducts() {
		return orderProducts;
	}
	
	public void setOrderProducts(List<Product> orderProducts) {
		this.orderProducts = orderProducts;
	}
	
	public String getReceiverName() {
		return receiverName;
	}
	
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	
	public String getReceiverPhone() {
		return receiverPhone;
	}
	
	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}
	
	public String getDlvyAddr() {
		return dlvyAddr;
	}
	
	public void setDlvyAddr(String dlvyAddr) {
		this.dlvyAddr = dlvyAddr;
	}
	
	public String getPaymentOption() {
		return paymentOption;
	}
	
	public void setPaymentOption(String paymentOption) {
		this.paymentOption = paymentOption;
	}
	
	public String getDlvyRequest() {
		return dlvyRequest;
	}
	
	public void setDlvyRequest(String dlvyRequest) {
		this.dlvyRequest = dlvyRequest;
	}
	
	public String getDlvyDate() {
		return dlvyDate;
	}
	
	public void setDlvyDate(String dlvyDate) {
		this.dlvyDate = dlvyDate;
	}

	@Override
	public String toString() {
		return "Purchase [tranNo=" + tranNo + ", buyer=" + buyer + ", addressId=" + addressId + ", totalPrice="
				+ totalPrice + ", tranCode=" + tranCode + ", orderDate=" + orderDate + ", purchaseProd=" + purchaseProd
				+ ", receiverName=" + receiverName + ", receiverPhone=" + receiverPhone + ", dlvyAddr=" + dlvyAddr
				+ ", paymentOption=" + paymentOption + "]";
	}
}