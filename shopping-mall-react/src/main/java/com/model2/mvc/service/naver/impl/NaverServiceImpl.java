// src/main/java/com/model2/mvc/service/naver/impl/NaverServiceImpl.java
package com.model2.mvc.service.naver.impl;

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

import com.model2.mvc.service.domain.NaverUserInfo;
import com.model2.mvc.service.domain.SocialAccount;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.naver.NaverService;
import com.model2.mvc.service.social.SocialAccountDao;
import com.model2.mvc.service.user.UserService;

@Service("naverServiceImpl")
public class NaverServiceImpl implements NaverService {
    
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;
    
    @Autowired
    @Qualifier("socialAccountDaoImpl")
    private SocialAccountDao socialAccountDao;
    
    // âœ… ìˆ˜ì •: SpEL ë°©ì‹ìœ¼ë¡œ ë³€ê²½
    @Value("${social.naver.client-id}")
    private String naverClientId;
    
    @Value("${social.naver.client-secret}")
    private String naverClientSecret;
    
    @Value("${social.naver.redirect-uri}")
    private String naverRedirectUri;
    
    @Value("${social.naver.auth-url}")
    private String naverAuthUrl;
    
    @Value("${social.naver.api-url}")
    private String naverApiUrl;
    
    @Override
    public String getAccessToken(String code, String state) throws Exception {
        System.out.println("==> NaverService.getAccessToken() ì‹œìž‘");
        
        // âœ… ë””ë²„ê¹…: ì£¼ìž…ëœ ê°’ í™•ì¸
        System.out.println("    [DEBUG] naverClientId: " + naverClientId);
        System.out.println("    [DEBUG] naverClientSecret: " + naverClientSecret);
        System.out.println("    [DEBUG] naverRedirectUri: " + naverRedirectUri);
        
        RestTemplate restTemplate = new RestTemplate();
        
        // ìš”ì²­ URL
        String tokenUrl = naverAuthUrl + "/oauth2.0/token";
        
        // ìš”ì²­ íŒŒë¼ë¯¸í„°
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", naverClientId);
        params.add("client_secret", naverClientSecret);
        params.add("code", code);
        params.add("state", state);
        
        // í—¤ë” ì„¤ì •
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        
        // API í˜¸ì¶œ
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        
        Map<String, Object> responseBody = response.getBody();
        String accessToken = (String) responseBody.get("access_token");
        
        System.out.println("==> ë„¤ì´ë²„ ì•¡ì„¸ìŠ¤ í† í° ë°œê¸‰ ì„±ê³µ");
        return accessToken;
    }
    
    @Override
    public NaverUserInfo getUserInfo(String accessToken) throws Exception {
        System.out.println("==> NaverService.getUserInfo() ì‹œìž‘");
        
        RestTemplate restTemplate = new RestTemplate();
        
        // ìš”ì²­ URL
        String userInfoUrl = naverApiUrl + "/v1/nid/me";
        
        // í—¤ë”ì— ì•¡ì„¸ìŠ¤ í† í° í¬í•¨
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        
        HttpEntity<String> request = new HttpEntity<>(headers);
        
        // API í˜¸ì¶œ
        ResponseEntity<Map> response = restTemplate.exchange(
            userInfoUrl, 
            HttpMethod.GET, 
            request, 
            Map.class
        );
        
        Map<String, Object> responseBody = response.getBody();
        Map<String, Object> userInfo = (Map<String, Object>) responseBody.get("response");
        
        // NaverUserInfo ê°ì²´ë¡œ ë³€í™˜
        NaverUserInfo naverUser = new NaverUserInfo();
        naverUser.setId((String) userInfo.get("id"));
        naverUser.setEmail((String) userInfo.get("email"));
        naverUser.setName((String) userInfo.get("name"));
        naverUser.setNickname((String) userInfo.get("nickname"));
        naverUser.setProfileImage((String) userInfo.get("profile_image"));
        
        System.out.println("==> ë„¤ì´ë²„ ì‚¬ìš©ìž ì •ë³´ ì¡°íšŒ ì„±ê³µ: " + naverUser);
        return naverUser;
    }
    
    @Override
    public User saveOrUpdateUser(NaverUserInfo naverUser) throws Exception {
        System.out.println("==> NaverService.saveOrUpdateUser() ì‹œìž‘");
        
        // 1. ì†Œì…œ ê³„ì • í…Œì´ë¸”ì—ì„œ ê¸°ì¡´ ì‚¬ìš©ìž ì°¾ê¸°
        SocialAccount socialAccount = socialAccountDao.findByProviderAndProviderId(
            "NAVER", 
            naverUser.getId()
        );
        
        User user;
        
        if (socialAccount != null) {
            // ê¸°ì¡´ ì‚¬ìš©ìž - ì •ë³´ ì—…ë°ì´íŠ¸
            System.out.println("==> ê¸°ì¡´ ë„¤ì´ë²„ ì‚¬ìš©ìž ë¡œê·¸ì¸");
            user = userService.getUserByUserNo(socialAccount.getUserNo());
            
            // í•„ìš”ì‹œ ì •ë³´ ì—…ë°ì´íŠ¸
            if (naverUser.getEmail() != null && !naverUser.getEmail().equals(user.getEmail())) {
                user.setEmail(naverUser.getEmail());
                userService.updateUser(user);
            }
            
        } else {
            // ì‹ ê·œ ì‚¬ìš©ìž - íšŒì›ê°€ìž…
            System.out.println("==> ì‹ ê·œ ë„¤ì´ë²„ ì‚¬ìš©ìž íšŒì›ê°€ìž…");
            
            user = new User();
            user.setUserId(null); // ì†Œì…œ ë¡œê·¸ì¸ì€ userId null
            user.setUserName(naverUser.getName() != null ? naverUser.getName() : naverUser.getNickname());
            user.setPassword(null); // ì†Œì…œ ë¡œê·¸ì¸ì€ ë¹„ë°€ë²ˆí˜¸ null
            user.setRole("user");
            user.setEmail(naverUser.getEmail());
            
            // íšŒì› ë“±ë¡
            userService.addSocialUser(user);
            
            // ì†Œì…œ ê³„ì • ì—°ë™ ì •ë³´ ì €ìž¥
            SocialAccount newSocialAccount = new SocialAccount();
            newSocialAccount.setUserNo(user.getUserNo());
            newSocialAccount.setProvider("NAVER");
            newSocialAccount.setProviderUserId(naverUser.getId());
            
            socialAccountDao.addSocialAccount(newSocialAccount);
        }
        
        System.out.println("==> ë„¤ì´ë²„ ì‚¬ìš©ìž DB ì €ìž¥/ì—…ë°ì´íŠ¸ ì™„ë£Œ");
        return user;
    }
    
    @Override
    public void logout(String accessToken) throws Exception {
        // ë„¤ì´ë²„ëŠ” ë³„ë„ ë¡œê·¸ì•„ì›ƒ API ì—†ìŒ
        // ì„¸ì…˜ë§Œ ì œê±°í•˜ë©´ ë¨
        System.out.println("==> NaverService.logout() - ì„¸ì…˜ ì œê±°ë§Œ ìˆ˜í–‰");
    }
    
    /**
     * âœ… [ì¶”ê°€] ê¸°ì¡´ ê³„ì •ì— ë„¤ì´ë²„ ê³„ì • ì—°ê²°
     */
    @Override
    public void linkSocialAccount(NaverUserInfo naverUser, int userNo) throws Exception {
        System.out.println("==> NaverService.linkSocialAccount() ì‹œìž‘");
        
        SocialAccount existingAccount = socialAccountDao.findByProviderAndProviderId(
            "NAVER", 
            naverUser.getId()
        );
        
        if (existingAccount != null) {
            throw new IllegalStateException("ì´ë¯¸ ë‹¤ë¥¸ ê³„ì •ì— ì—°ê²°ëœ ë„¤ì´ë²„ ê³„ì •ìž…ë‹ˆë‹¤.");
        }

        SocialAccount newSocialAccount = new SocialAccount();
        newSocialAccount.setUserNo(userNo);
        newSocialAccount.setProvider("NAVER");
        newSocialAccount.setProviderUserId(naverUser.getId());

        socialAccountDao.addSocialAccount(newSocialAccount);
        System.out.println("==> SocialAccount (ë„¤ì´ë²„) ì—°ê²° ì™„ë£Œ: " + newSocialAccount);
    }
}