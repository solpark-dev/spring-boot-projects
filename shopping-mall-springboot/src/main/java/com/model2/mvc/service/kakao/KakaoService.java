package com.model2.mvc.service.kakao;

import com.model2.mvc.service.domain.KakaoUserInfo;
import com.model2.mvc.service.domain.User;

public interface KakaoService {
    String getAccessToken(String code) throws Exception;
    KakaoUserInfo getUserInfo(String accessToken) throws Exception;
    User saveOrUpdateUser(KakaoUserInfo kakaoUser) throws Exception;
    void logout(String accessToken) throws Exception;
    void linkSocialAccount(KakaoUserInfo kakaoUser, int userNo) throws Exception;
}
