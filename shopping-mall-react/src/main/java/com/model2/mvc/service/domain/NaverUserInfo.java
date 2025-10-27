// src/main/java/com/model2/mvc/service/domain/NaverUserInfo.java
package com.model2.mvc.service.domain;

/**
 * 네이버 사용자 정보
 * 카카오와 동일한 패턴
 */
public class NaverUserInfo {
    
    private String id;              // 네이버 고유 ID (필수)
    private String email;           // 이메일
    private String name;            // 이름
    private String nickname;        // 닉네임
    private String profileImage;    // 프로필 이미지 URL
    
    // 기본 생성자
    public NaverUserInfo() {}
    
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
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getProfileImage() {
        return profileImage;
    }
    
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "NaverUserInfo [id=" + id + ", email=" + email + ", name=" + name + 
               ", nickname=" + nickname + ", profileImage=" + profileImage + "]";
    }
}