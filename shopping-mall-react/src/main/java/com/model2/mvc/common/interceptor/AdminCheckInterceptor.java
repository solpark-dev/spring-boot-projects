// AdminCheckInterceptor.java (수정 후)
package com.model2.mvc.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.model2.mvc.service.domain.User;

public class AdminCheckInterceptor extends HandlerInterceptorAdapter {

    public AdminCheckInterceptor() {
        System.out.println("\nCommon :: " + this.getClass() + "\n");
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        System.out.println("\n[ AdminCheckInterceptor start........]");
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // 세션에서 사용자 정보를 확인하여 'admin' 역할이 아니거나 로그아웃 상태이면 접근 차단
        if (user == null || !"admin".equals(user.getRole())) {
            System.out.println("[ 관리자 권한 없음... ]");
            // 권한이 없을 경우 메인 페이지로 리다이렉트
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            System.out.println("[ AdminCheckInterceptor end........]\n");
            return false; // 컨트롤러 실행 차단
        }

        // 관리자 권한이 확인되면 요청 계속 진행
        System.out.println("[ 관리자 권한 확인됨. ]");
        System.out.println("[ AdminCheckInterceptor end........]\n");
        return true; // 컨트롤러 실행 허용
    }
}