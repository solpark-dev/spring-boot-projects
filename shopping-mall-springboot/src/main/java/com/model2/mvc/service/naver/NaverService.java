package com.model2.mvc.service.naver;

import com.model2.mvc.service.domain.NaverUserInfo;
import com.model2.mvc.service.domain.User;

public interface NaverService {
    String getAccessToken(String code, String state) throws Exception;
    NaverUserInfo getUserInfo(String accessToken) throws Exception;
    User saveOrUpdateUser(NaverUserInfo naverUser) throws Exception;
    void logout(String accessToken) throws Exception;
    void linkSocialAccount(NaverUserInfo naverUser, int userNo) throws Exception;
}
