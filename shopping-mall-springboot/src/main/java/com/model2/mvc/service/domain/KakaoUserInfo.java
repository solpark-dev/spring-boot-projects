// src/main/java/com/model2/mvc/service/domain/KakaoUserInfo.java
package com.model2.mvc.service.domain;

/**
 * 카카오에서 받아온 사용자 정보를 담는 DTO
 */
public class KakaoUserInfo {
    
    private Long kakaoId;           // 카카오 고유 ID
    private String nickname;        // 닉네임
    private String email;           // 이메일
    private String profileImage;    // 프로필 이미지 URL
    
    // 기본 생성자
    public KakaoUserInfo() {
    }
    
    // 전체 생성자
    public KakaoUserInfo(Long kakaoId, String nickname, String email, String profileImage) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
    }
    
    // Getter & Setter
    public Long getKakaoId() {
        return kakaoId;
    }
    
    public void setKakaoId(Long kakaoId) {
        this.kakaoId = kakaoId;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getProfileImage() {
        return profileImage;
    }
    
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    
    @Override
    public String toString() {
        return "KakaoUserInfo [kakaoId=" + kakaoId + ", nickname=" + nickname + 
               ", email=" + email + ", profileImage=" + profileImage + "]";
    }
}