// src/main/java/com/model2/mvc/service/google/impl/GoogleServiceImpl.java
package com.model2.mvc.service.google.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.model2.mvc.service.domain.GoogleUserInfo;
import com.model2.mvc.service.domain.SocialAccount;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.google.GoogleService;
import com.model2.mvc.service.social.SocialAccountDao;
import com.model2.mvc.service.user.UserService;

@Service("googleServiceImpl")
public class GoogleServiceImpl implements GoogleService {
    
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;
    
    @Autowired
    @Qualifier("socialAccountDaoImpl")
    private SocialAccountDao socialAccountDao;
    
    // Property injection
    @Value("${social.google.client-id}")
    private String googleClientId;
    
    @Value("${social.google.client-secret}")
    private String googleClientSecret;
    
    @Value("${social.google.redirect-uri}")
    private String googleRedirectUri;
    
    @Value("${social.google.auth-url}")
    private String googleAuthUrl;
    
    @Value("${social.google.api-url}")
    private String googleApiUrl;
    
    @Override
    public String getAccessToken(String code) throws Exception {
        System.out.println("==> GoogleService.getAccessToken() start");
        
        // Property injection
        System.out.println("    [DEBUG] googleClientId: " + googleClientId);
        System.out.println("    [DEBUG] googleClientSecret: " + googleClientSecret);
        System.out.println("    [DEBUG] googleRedirectUri: " + googleRedirectUri);
        
        RestTemplate restTemplate = new RestTemplate();
        
        // Property injection
        String tokenUrl = googleAuthUrl + "/o/oauth2/token";
        
        // Property injection
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", googleRedirectUri);
        params.add("code", code);
        
        // Property injection
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        
        // Property injection
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        
        Map<String, Object> responseBody = response.getBody();
        String accessToken = (String) responseBody.get("access_token");
        
        System.out.println("==> êµ¬ê¸€ start");
        return accessToken;
    }
    
    @Override
    public GoogleUserInfo getUserInfo(String accessToken) throws Exception {
        System.out.println("==> GoogleService.getUserInfo() start");
        
        RestTemplate restTemplate = new RestTemplate();
        
        // Property injection
        String userInfoUrl = googleApiUrl + "/oauth2/v2/userinfo";
        
        // Property injection
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        
        HttpEntity<String> request = new HttpEntity<>(headers);
        
        // Property injection
        ResponseEntity<Map> response = restTemplate.exchange(
            userInfoUrl, 
            HttpMethod.GET, 
            request, 
            Map.class
        );
        
        Map<String, Object> userInfo = response.getBody();
        
        // Property injection
        GoogleUserInfo googleUser = new GoogleUserInfo();
        googleUser.setId((String) userInfo.get("id"));
        googleUser.setEmail((String) userInfo.get("email"));
        googleUser.setName((String) userInfo.get("name"));
        googleUser.setPicture((String) userInfo.get("picture"));
        googleUser.setEmailVerified((Boolean) userInfo.get("verified_email"));
        
        System.out.println("==> êµ¬ê¸€ start");
        return googleUser;
    }
    
    @Override
    public User saveOrUpdateUser(GoogleUserInfo googleUser) throws Exception {
        System.out.println("==> GoogleService.saveOrUpdateUser() start");
        
        // Property injection
        SocialAccount socialAccount = socialAccountDao.findByProviderAndProviderId(
            "GOOGLE", 
            googleUser.getId()
        );
        
        User user;
        
        if (socialAccount != null) {
            // Property injection
            System.out.println("==> ê¸°start");
            user = userService.getUserByUserNo(socialAccount.getUserNo());
            
            // Property injection
            if (googleUser.getEmail() != null && !googleUser.getEmail().equals(user.getEmail())) {
                user.setEmail(googleUser.getEmail());
                userService.updateUser(user);
            }
            
        } else {
            // Property injection
            System.out.println("==> start");
            
            user = new User();
            // Property injection
            user.setUserName(googleUser.getName());
            // Property injection
            user.setRole("user");
            user.setEmail(googleUser.getEmail());
            
            // Property injection
            userService.addSocialUser(user);
            
            // Property injection
            SocialAccount newSocialAccount = new SocialAccount();
            newSocialAccount.setUserNo(user.getUserNo());
            newSocialAccount.setProvider("GOOGLE");
            newSocialAccount.setProviderUserId(googleUser.getId());
            
            socialAccountDao.addSocialAccount(newSocialAccount);
        }
        
        System.out.println("==> êµ¬ê¸€ start");
        return user;
    }
    
    @Override
    public void logout(String accessToken) throws Exception {
        // Property injection
        // Property injection
        System.out.println("==> GoogleService.logout() - start");
    }
    
    /**
     * âœ… [ì¶”ê°€] ê¸°ì¡´ ê³„ì •ì— êµ¬ê¸€ ê³„ì • ì—°ê²°
     */
    @Override
    public void linkSocialAccount(GoogleUserInfo googleUser, int userNo) throws Exception {
        System.out.println("==> GoogleService.linkSocialAccount() start");
        
        SocialAccount existingAccount = socialAccountDao.findByProviderAndProviderId(
            "GOOGLE", 
            googleUser.getId()
        );
        
        if (existingAccount != null) {
            throw new IllegalStateException("ì´ë¯¸ ë‹¤ë¥¸ ê³„ì •ì— ì—°ê²°ëœ êµ¬ê¸€ ê³„ì •ìž…ë‹ˆë‹¤.");
        }

        SocialAccount newSocialAccount = new SocialAccount();
        newSocialAccount.setUserNo(userNo);
        newSocialAccount.setProvider("GOOGLE");
        newSocialAccount.setProviderUserId(googleUser.getId());

        socialAccountDao.addSocialAccount(newSocialAccount);
        System.out.println("==> SocialAccount (êµ¬ê¸€) start");
    }
}