package com.model2.mvc.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.model2.mvc.service.domain.User;

/**
 * LogonCheckInterceptor
 * 로그인 여부 확인 인터셉터
 * 
 * ⭐ 소셜 로그인 콜백 URL은 예외 처리 필요!
 */
public class LogonCheckInterceptor extends HandlerInterceptorAdapter {

    public LogonCheckInterceptor() {
        System.out.println("\nCommon :: " + this.getClass() + "\n");
    }
    
    public boolean preHandle(HttpServletRequest request,
                            HttpServletResponse response, 
                            Object handler) throws Exception {
        
        System.out.println("\n[ LogonCheckInterceptor start........]");
        
        // 로그인 유무 확인
        HttpSession session = request.getSession(true);
        User user = (User)session.getAttribute("user");

        // 로그인한 회원이라면
        if (user != null) {
            String uri = request.getRequestURI();
            
            // 로그인 상태에서 접근 불가 URI
            if (uri.indexOf("addUser") != -1 || 
                uri.indexOf("login") != -1 || 
                uri.indexOf("checkDuplication") != -1) {
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                System.out.println("[ 로그인 상태.. 로그인 후 불필요 한 요구.... ]");
                System.out.println("[ LogonCheckInterceptor end........]\n");
                return false;
            }
            
            System.out.println("[ 로그인 상태 ... ]");
            System.out.println("[ LogonCheckInterceptor end........]\n");
            return true;
            
        } else {
            // 미로그인 회원이라면
            String uri = request.getRequestURI();
            
            // ⭐ 로그인 시도 중 OR 소셜 로그인 콜백 중
            if (uri.indexOf("addUser") != -1 || 
                uri.indexOf("login") != -1 || 
                uri.indexOf("checkDuplication") != -1 ||
                uri.indexOf("/kakao/callback") != -1 ||    // 카카오 콜백
                uri.indexOf("/naver/callback") != -1 ||    // 네이버 콜백
                uri.indexOf("/google/callback") != -1) {   // 구글 콜백
                System.out.println("[ 로그 시도 상태 또는 소셜 로그인 콜백 중 .... ]");
                System.out.println("[ LogonCheckInterceptor end........]\n");
                return true;
            }
            
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            System.out.println("[ 로그인 이전 ... ]");
            System.out.println("[ LogonCheckInterceptor end........]\n");
            return false;
        }
    }
}