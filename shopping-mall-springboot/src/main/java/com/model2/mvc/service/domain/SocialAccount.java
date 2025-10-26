// src/main/java/com/model2/mvc/service/domain/SocialAccount.java
package com.model2.mvc.service.domain;

import java.sql.Date;

/**
 * 소셜 계정 연동 정보를 담는 Domain 객체
 * DB 테이블: user_social_accounts
 */
public class SocialAccount {
    
    private int socialAccountId;      // social_account_id (PK)
    private int userNo;               // user_no (FK)
    private String provider;          // provider (KAKAO, GOOGLE, NAVER 등)
    private String providerUserId;    // provider_user_id (소셜 서비스의 사용자 ID)
    private Date regDate;             // 등록일 (필요시)
    
    // 기본 생성자
    public SocialAccount() {
    }
    
    // 전체 생성자
    public SocialAccount(int socialAccountId, int userNo, String provider, String providerUserId) {
        this.socialAccountId = socialAccountId;
        this.userNo = userNo;
        this.provider = provider;
        this.providerUserId = providerUserId;
    }
    
    // Getter & Setter
    public int getSocialAccountId() {
        return socialAccountId;
    }
    
    public void setSocialAccountId(int socialAccountId) {
        this.socialAccountId = socialAccountId;
    }
    
    public int getUserNo() {
        return userNo;
    }
    
    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }
    
    public String getProvider() {
        return provider;
    }
    
    public void setProvider(String provider) {
        this.provider = provider;
    }
    
    public String getProviderUserId() {
        return providerUserId;
    }
    
    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }
    
    public Date getRegDate() {
        return regDate;
    }
    
    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }
    
    @Override
    public String toString() {
        return "SocialAccount [socialAccountId=" + socialAccountId + 
               ", userNo=" + userNo + 
               ", provider=" + provider + 
               ", providerUserId=" + providerUserId + 
               ", regDate=" + regDate + "]";
    }
}