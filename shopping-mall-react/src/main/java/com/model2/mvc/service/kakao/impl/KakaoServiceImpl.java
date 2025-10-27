// src/main/java/com/model2/mvc/service/kakao/impl/KakaoServiceImpl.java
package com.model2.mvc.service.kakao.impl;

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

import com.model2.mvc.service.domain.KakaoUserInfo;
import com.model2.mvc.service.domain.SocialAccount;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.kakao.KakaoService;
import com.model2.mvc.service.social.SocialAccountDao;
import com.model2.mvc.service.user.UserService;

@Service("kakaoServiceImpl")
public class KakaoServiceImpl implements KakaoService {
    
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;
    
    @Autowired
    @Qualifier("socialAccountDaoImpl")
    private SocialAccountDao socialAccountDao;
    
    // Property injection
    @Value("${social.kakao.rest-api-key}")
    private String kakaoRestApiKey;
    
    @Value("${social.kakao.redirect-uri}")
    private String kakaoRedirectUri;
    
    @Value("${social.kakao.auth-url}")
    private String kakaoAuthUrl;
    
    @Value("${social.kakao.api-url}")
    private String kakaoApiUrl;
    
    public KakaoServiceImpl() {
        System.out.println("==> KakaoServiceImpl start");
    }
    
    /**
     * 1ë‹¨ê³„: ì¸ê°€ ì½”ë“œë¡œ ì•¡ì„¸ìŠ¤ í† í° ë°œê¸‰
     */
    @Override
    public String getAccessToken(String code) throws Exception {
        System.out.println("==> KakaoService.getAccessToken() start");
        System.out.println("==> start");
        
        // Property injection
        System.out.println("    [DEBUG] kakaoRestApiKey: " + kakaoRestApiKey);
        System.out.println("    [DEBUG] kakaoRedirectUri: " + kakaoRedirectUri);
        System.out.println("    [DEBUG] kakaoAuthUrl: " + kakaoAuthUrl);
        
        // Property injection
        String tokenUrl = kakaoAuthUrl + "/oauth/token";
        
        // Property injection
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoRestApiKey);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);
        
        // Property injection
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        // Property injection
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        
        // Property injection
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        
        // Property injection
        Map<String, Object> responseBody = response.getBody();
        String accessToken = (String) responseBody.get("access_token");
        
        System.out.println("==> start");
        
        return accessToken;
    }
    
    /**
     * 2ë‹¨ê³„: ì•¡ì„¸ìŠ¤ í† í°ìœ¼ë¡œ ì‚¬ìš©ìž ì •ë³´ ì¡°íšŒ
     */
    @Override
    public KakaoUserInfo getUserInfo(String accessToken) throws Exception {
        System.out.println("==> KakaoService.getUserInfo() start");
        
        // Property injection
        String userInfoUrl = kakaoApiUrl + "/v2/user/me";
        
        // Property injection
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        // Property injection
        HttpEntity<String> request = new HttpEntity<>(headers);
        
        // Property injection
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(
            userInfoUrl, 
            HttpMethod.GET, 
            request, 
            Map.class
        );
        
        // Property injection
        Map<String, Object> responseBody = response.getBody();
        Long kakaoId = ((Number) responseBody.get("id")).longValue();
        
        // Property injection
        Map<String, Object> kakaoAccount = (Map<String, Object>) responseBody.get("kakao_account");
        String email = kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
        
        // Property injection
        Map<String, Object> profile = kakaoAccount != null ? 
            (Map<String, Object>) kakaoAccount.get("profile") : null;
        
        String nickname = profile != null ? (String) profile.get("nickname") : "ì¹´ì¹´ì˜¤ìœ ì €";
        String profileImage = profile != null ? (String) profile.get("profile_image_url") : null;
        
        // Property injection
        KakaoUserInfo kakaoUser = new KakaoUserInfo(kakaoId, nickname, email, profileImage);
        
        System.out.println("==> start");
        
        return kakaoUser;
    }
    
    /**
     * 3ë‹¨ê³„: ì¹´ì¹´ì˜¤ ì‚¬ìš©ìž ì •ë³´ë¥¼ ìš°ë¦¬ DBì— ì €ìž¥ ë˜ëŠ” ì—…ë°ì´íŠ¸
     * âœ… ì™„ì „ ìž¬ìž‘ì„± - user_social_accounts í…Œì´ë¸” ì‚¬ìš©
     */
    @Override
    public User saveOrUpdateUser(KakaoUserInfo kakaoUser) throws Exception {
        System.out.println("==> KakaoService.saveOrUpdateUser() start");
        System.out.println("==> start");
        
        // Property injection
        SocialAccount socialAccount = socialAccountDao.findByProviderAndProviderId(
            "KAKAO", 
            kakaoUser.getKakaoId().toString()
        );
        
        // Property injection
        if (socialAccount != null) {
            // Property injection
            System.out.println("==> ê¸°start");
            User existingUser = userService.getUserByUserNo(socialAccount.getUserNo());
            
            if (existingUser != null) {
                // Property injection
                existingUser.setUserName(kakaoUser.getNickname());
                if (kakaoUser.getEmail() != null && !kakaoUser.getEmail().isEmpty()) {
                    existingUser.setEmail(kakaoUser.getEmail());
                }
                userService.updateUser(existingUser);
                System.out.println("==> ê¸°start");
                
                return existingUser;
            }
        }
        
        // Property injection
        System.out.println("==> start");
        
        // Property injection
        User newUser = new User();
        // Property injection
        newUser.setUserName(kakaoUser.getNickname());
        newUser.setEmail(kakaoUser.getEmail() != null ? kakaoUser.getEmail() : "");
        // Property injection
        newUser.setRole("user");
        newUser.setStatus("ACTIVE");
        
        // Property injection
        userService.addSocialUser(newUser);
        System.out.println("==> start");
        
        // Property injection
        SocialAccount newSocialAccount = new SocialAccount();
        newSocialAccount.setUserNo(newUser.getUserNo());
        newSocialAccount.setProvider("KAKAO");
        newSocialAccount.setProviderUserId(kakaoUser.getKakaoId().toString());
        
        // Property injection
        socialAccountDao.addSocialAccount(newSocialAccount);
        System.out.println("==> SocialAccount ë“±ë¡ start");
        
        System.out.println("==> start");
        
        return newUser;
    }
    
    /**
     * 4ë‹¨ê³„: ì¹´ì¹´ì˜¤ ë¡œê·¸ì•„ì›ƒ
     */
    @Override
    public void logout(String accessToken) throws Exception {
        System.out.println("==> KakaoService.logout() start");
        
        String logoutUrl = kakaoApiUrl + "/v1/user/logout";
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        
        HttpEntity<String> request = new HttpEntity<>(headers);
        
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(
            logoutUrl,
            HttpMethod.POST,
            request,
            Map.class
        );
        
        System.out.println("==> start");
    }
    
    /**
     * âœ… [ì¶”ê°€] 3-2ë‹¨ê³„: ê¸°ì¡´ ê³„ì •ì— ì¹´ì¹´ì˜¤ ê³„ì • ì—°ê²°
     * ë§ˆì´íŽ˜ì´ì§€ì—ì„œ 'ì—°ê²°í•˜ê¸°'ë¥¼ ëˆŒë €ì„ ë•Œ ì‹¤í–‰
     */
    @Override
    public void linkSocialAccount(KakaoUserInfo kakaoUser, int userNo) throws Exception {
        System.out.println("==> KakaoService.linkSocialAccount() start");
        
        // Property injection
        SocialAccount existingAccount = socialAccountDao.findByProviderAndProviderId(
            "KAKAO", 
            kakaoUser.getKakaoId().toString()
        );
        
        if (existingAccount != null) {
            throw new IllegalStateException("ì´ë¯¸ ë‹¤ë¥¸ ê³„ì •ì— ì—°ê²°ëœ ì¹´ì¹´ì˜¤ ê³„ì •ìž…ë‹ˆë‹¤.");
        }

        // Property injection
        SocialAccount newSocialAccount = new SocialAccount();
        newSocialAccount.setUserNo(userNo);
        newSocialAccount.setProvider("KAKAO");
        newSocialAccount.setProviderUserId(kakaoUser.getKakaoId().toString());

        // Property injection
        socialAccountDao.addSocialAccount(newSocialAccount);
        System.out.println("==> SocialAccount start");
    }
}