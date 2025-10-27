package com.model2.mvc.service.google;

import com.model2.mvc.service.domain.GoogleUserInfo;
import com.model2.mvc.service.domain.User;

public interface GoogleService {
    String getAccessToken(String code) throws Exception;
    GoogleUserInfo getUserInfo(String accessToken) throws Exception;
    User saveOrUpdateUser(GoogleUserInfo googleUser) throws Exception;
    void logout(String accessToken) throws Exception;
    void linkSocialAccount(GoogleUserInfo googleUser, int userNo) throws Exception;
}
