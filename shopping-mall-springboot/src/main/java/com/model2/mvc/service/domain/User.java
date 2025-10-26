package com.model2.mvc.service.domain;

import java.sql.Date;

/**
 * 회원정보를 모델링(추상화/캡슐화)한 Bean
 * DB 테이블: new_users (새 구조)
 */
public class User {
    
    ///Field
    private int userNo;         // ✅ 추가: user_no (PK)
    private String userId;      // user_id (UNIQUE, 일반 로그인용, 소셜 로그인은 NULL 가능)
    private String userName;    // user_name
    private String password;    // password (소셜 로그인은 NULL 가능)
    private String role;        // role (user, admin)
    private String status;      // ✅ 추가: status (ACTIVE, WITHDRAWN, DORMANT)
    private String email;       // email
    private Date regDate;       // reg_date
    
    // ❌ 제거: ssn, phone, addr (새 구조에서는 user_addresses 테이블로 분리)
    
    /////////////// EL 적용 위해 추가된 Field ///////////
    // JSON ==> Domain Object Binding을 위해 추가된 부분
    private String regDateString;
    
    ///Constructor
    public User() {
    }
    
    ///Method
    public int getUserNo() {
        return userNo;
    }
    
    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Date getRegDate() {
        return regDate;
    }
    
    public void setRegDate(Date regDate) {
        this.regDate = regDate;
        
        if (regDate != null) {
            // JSON ==> Domain Object Binding을 위해 추가된 부분
            this.setRegDateString(regDate.toString().split("-")[0]
                    + "-" + regDate.toString().split("-")[1]
                    + "-" + regDate.toString().split("-")[2]);
        }
    }
    
    public String getRegDateString() {
        return regDateString;
    }
    
    public void setRegDateString(String regDateString) {
        this.regDateString = regDateString;
    }
    
    @Override
    public String toString() {
        return "User [userNo=" + userNo + 
               ", userId=" + userId + 
               ", userName=" + userName + 
               ", password=" + password + 
               ", role=" + role + 
               ", status=" + status + 
               ", email=" + email + 
               ", regDate=" + regDate + "]";
    }
}