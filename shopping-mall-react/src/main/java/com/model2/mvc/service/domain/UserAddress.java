package com.model2.mvc.service.domain;

import java.sql.Date;

/**
 * 사용자 배송지 정보를 담는 Domain 객체
 * DB 테이블: user_addresses
 */
public class UserAddress {
    
    // ========================================
    // Fields
    // ========================================
    private int addressId;          // address_id (PK)
    private int userNo;             // user_no (FK)
    private String addressAlias;    // address_alias (주소 별칭: 집, 회사 등)
    private String receiverName;    // receiver_name (수령인 이름)
    private String address;         // address (주소)
    private String phoneNumber;     // phone_number (전화번호)
    private String isDefault;       // is_default (기본 주소 여부: Y/N)
    private Date regDate;           // reg_date (등록일)
    
    // ========================================
    // Constructors
    // ========================================
    public UserAddress() {
    }
    
    public UserAddress(int addressId, int userNo, String addressAlias, 
                      String receiverName, String address, String phoneNumber, 
                      String isDefault) {
        this.addressId = addressId;
        this.userNo = userNo;
        this.addressAlias = addressAlias;
        this.receiverName = receiverName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.isDefault = isDefault;
    }
    
    // ========================================
    // Getters & Setters
    // ========================================
    public int getAddressId() {
        return addressId;
    }
    
    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
    
    public int getUserNo() {
        return userNo;
    }
    
    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }
    
    public String getAddressAlias() {
        return addressAlias;
    }
    
    public void setAddressAlias(String addressAlias) {
        this.addressAlias = addressAlias;
    }
    
    public String getReceiverName() {
        return receiverName;
    }
    
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getIsDefault() {
        return isDefault;
    }
    
    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
    
    public Date getRegDate() {
        return regDate;
    }
    
    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }
    
    // ========================================
    // toString
    // ========================================
    @Override
    public String toString() {
        return "UserAddress [addressId=" + addressId + 
               ", userNo=" + userNo + 
               ", addressAlias=" + addressAlias + 
               ", receiverName=" + receiverName + 
               ", address=" + address + 
               ", phoneNumber=" + phoneNumber + 
               ", isDefault=" + isDefault + 
               ", regDate=" + regDate + "]";
    }
}