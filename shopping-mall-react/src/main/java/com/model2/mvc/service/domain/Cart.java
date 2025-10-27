package com.model2.mvc.service.domain;

import java.sql.Date;

/**
 * Cart 도메인 객체
 * new_cart 테이블과 매핑되는 장바구니 정보 클래스
 */
public class Cart {
    
    // ========================================
    // new_cart 테이블 필드
    // ========================================
    private int cartId;         // cart_id (장바구니 ID)
    private int userNo;         // user_no (사용자 번호)
    private int prodNo;         // prod_no (상품 번호)
    private int quantity;       // quantity (수량)
    private Date regDate;       // reg_date (등록일)
    
    // ========================================
    // JOIN으로 가져올 연관 객체
    // ========================================
    private User user;          // 사용자 정보
    private Product product;    // 상품 정보 (상품명, 가격, 이미지 등)
    
    
    // ========================================
    // 생성자
    // ========================================
    public Cart() {
    }
    
    
    // ========================================
    // Getters & Setters
    // ========================================
    
    public int getCartId() {
        return cartId;
    }
    
    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
    
    public int getUserNo() {
        return userNo;
    }
    
    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }
    
    public int getProdNo() {
        return prodNo;
    }
    
    public void setProdNo(int prodNo) {
        this.prodNo = prodNo;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public Date getRegDate() {
        return regDate;
    }
    
    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    
    // ========================================
    // toString() 오버라이드
    // ========================================
    @Override
    public String toString() {
        return "Cart [cartId=" + cartId 
                + ", userNo=" + userNo 
                + ", prodNo=" + prodNo 
                + ", quantity=" + quantity 
                + ", regDate=" + regDate 
                + ", user=" + (user != null ? user.getUserName() : "null")
                + ", product=" + (product != null ? product.getProdName() : "null")
                + "]";
    }
}