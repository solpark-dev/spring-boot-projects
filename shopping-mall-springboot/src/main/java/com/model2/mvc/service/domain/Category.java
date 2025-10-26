package com.model2.mvc.service.domain;

import java.sql.Date;

/**
 * 카테고리 정보를 담는 Domain 객체
 * DB 테이블: new_categories
 */
public class Category {
    
    // ========================================
    // Fields
    // ========================================
    private int categoryId;         // category_id (PK)
    private String categoryName;    // category_name (카테고리명)
    private Date regDate;           // reg_date (등록일) - 필요시 추가
    
    // ========================================
    // Constructors
    // ========================================
    public Category() {
    }
    
    public Category(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
    
    // ========================================
    // Getters & Setters
    // ========================================
    public int getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
        return "Category [categoryId=" + categoryId + 
               ", categoryName=" + categoryName + 
               ", regDate=" + regDate + "]";
    }
}