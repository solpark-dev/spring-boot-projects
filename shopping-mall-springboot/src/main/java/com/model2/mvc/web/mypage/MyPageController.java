package com.model2.mvc.web.mypage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.user.UserService;

/**
 * 마이페이지 컨트롤러 (수정됨)
 * 사용자의 프로필, 소셜 계정, 즐겨찾기, 장바구니 등을 관리
 */
@Controller
@RequestMapping("/mypage")
public class MyPageController {
    
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;
    
    // ✅ 소셜 로그인 설정 추가
    @Value("${social.kakao.rest-api-key}")
    private String kakaoApiKey;
    @Value("${social.kakao.redirect-uri}")
    private String kakaoRedirectUri;
    
    @Value("${social.naver.client-id}")
    private String naverClientId;
    @Value("${social.naver.redirect-uri}")
    private String naverRedirectUri;
    
    @Value("${social.google.client-id}")
    private String googleClientId;
    @Value("${social.google.redirect-uri}")
    private String googleRedirectUri;
    
    /**
     * 마이페이지 메인
     * GET /mypage
     */
    @GetMapping("")
    public String mypage(HttpSession session, Model model) throws Exception {
        
        System.out.println("==> /mypage : GET");
        
        // 세션에서 사용자 정보 가져오기
        User sessionUser = (User) session.getAttribute("user");
        
        // 최신 사용자 정보 조회
        User user = userService.getUserByUserNo(sessionUser.getUserNo());
        
        // Model에 사용자 정보 추가
        model.addAttribute("user", user);
        
        // ✅ 수정: 소셜 계정 연결용 URL 생성 (간소화된 버전)
        
        // 카카오 연결 URL
        String kakaoLinkUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code"
                + "&client_id=" + kakaoApiKey
                + "&redirect_uri=" + kakaoRedirectUri
                + "&state=link";  // ✅ 간단한 state 값
        
        // 네이버 연결 URL
        String naverLinkUrl = "https://nid.naver.com/oauth2.0/authorize?response_type=code"
                + "&client_id=" + naverClientId
                + "&redirect_uri=" + naverRedirectUri
                + "&state=link";  // ✅ 간단한 state 값 (CSRF 토큰 불필요)
        
        // 구글 연결 URL
        String googleLinkUrl = "https://accounts.google.com/o/oauth2/v2/auth?response_type=code"
                + "&client_id=" + googleClientId
                + "&redirect_uri=" + googleRedirectUri
                + "&scope=email%20profile"
                + "&state=link";  // ✅ 간단한 state 값
        
        model.addAttribute("kakaoLinkUrl", kakaoLinkUrl);
        model.addAttribute("naverLinkUrl", naverLinkUrl);
        model.addAttribute("googleLinkUrl", googleLinkUrl);
        
        System.out.println("==> 마이페이지 접속: " + user.getUserName());
        System.out.println("    카카오 연결 URL: " + kakaoLinkUrl);
        System.out.println("    네이버 연결 URL: " + naverLinkUrl);
        System.out.println("    구글 연결 URL: " + googleLinkUrl);
        
        return "mypage";
    }
    
    // [ History ]
    @RequestMapping(value = "history", method = RequestMethod.GET)
    public String getHistory(HttpServletRequest request, Model model) {
        System.out.println("/mypage/history : GET (최근 본 상품)");

        Cookie[] cookies = request.getCookies();
        List<String> historyList = new ArrayList<>();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("history".equals(cookie.getName())) {
                    String historyValue = cookie.getValue();
                    if (historyValue != null && !historyValue.isEmpty()) {
                        String[] prodNos = historyValue.split("-");
                        historyList = Arrays.stream(prodNos)
                                            .filter(p -> p != null && !p.trim().isEmpty() && !"null".equals(p))
                                            .collect(Collectors.toList());
                        Collections.reverse(historyList);
                    }
                    break;
                }
            }
        }

        model.addAttribute("historyList", historyList);
        return "mypage/history"; 
    }
}