// src/main/java/com/model2/mvc/service/domain/GoogleUserInfo.java
package com.model2.mvc.service.domain;

/**
 * 구글 사용자 정보
 */
public class GoogleUserInfo {
    
    private String id;              // 구글 고유 ID (sub)
    private String email;           // 이메일
    private String name;            // 이름
    private String picture;         // 프로필 이미지 URL
    private Boolean emailVerified;  // 이메일 인증 여부
    
    // 기본 생성자
    public GoogleUserInfo() {}
    
    // getter/setter
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPicture() {
        return picture;
    }
    
    public void setPicture(String picture) {
        this.picture = picture;
    }
    
    public Boolean getEmailVerified() {
        return emailVerified;
    }
    
    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    @Override
    public String toString() {
        return "GoogleUserInfo [id=" + id + ", email=" + email + ", name=" + name + 
               ", picture=" + picture + ", emailVerified=" + emailVerified + "]";
    }
}