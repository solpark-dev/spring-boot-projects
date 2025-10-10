package spring.common.web;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import spring.domain.User;

public class LogonCheckInterceptor implements HandlerInterceptor {

    /// Constructor
    public LogonCheckInterceptor() {
        System.out.println("==> LogonCheckInterceptor default Constructor call......");
    }
    
    /**
     * Controller 실행 전, return 값이 true 이면 Controller 로 진행
     * return 값이 false 이면 Controller 로 진행하지 않음
     */
    @Override
    public boolean preHandle(HttpServletRequest request, 
                             HttpServletResponse response, 
                             Object handler) throws Exception {
        
        System.out.println("\n[ LogonCheckInterceptor start........]");
        
        // 1. session 에서 sessionUser 정보를 가져온다.
        HttpSession session = request.getSession(true);
        User sessionUser = (User) session.getAttribute("sessionUser");
        
        // 2. 로그인 유무를 확인한다.
        boolean isLoggedIn = (sessionUser != null && sessionUser.isActive());
        
        // 3. RequestURI 를 통해 요청 경로를 확인한다.
        String requestURI = request.getRequestURI();
        System.out.println("Current Request URI: " + requestURI);

        // 4. 요구사항에 따라 분기 처리
        if (requestURI.endsWith("/logon")) {
            if (isLoggedIn) {
                // 로그인한 유저는 home.jsp 로 포워드
                System.out.println("[ 로그인 상태, home.jsp로 이동 ]");
                request.getRequestDispatcher("/user/home.jsp").forward(request, response);
                return false;
            } else {
                // 미 로그인한 유저는 컨트롤러로 진행
                System.out.println("[ 미로그인 상태, Controller로 진행 ]");
                return true;
            }
        }
        
        else if (requestURI.endsWith("/home")) {
            if (isLoggedIn) {
                // 로그인한 유저는 컨트롤러로 진행
                System.out.println("[ 로그인 상태, Controller로 진행 ]");
                return true;
            } else {
                // 미 로그인한 유저는 logon.jsp 로 포워드
                System.out.println("[ 미로그인 상태, logon.jsp로 이동 ]");
                request.getRequestDispatcher("/user/logon.jsp").forward(request, response);
                return false;
            }
        }
        
        else if (requestURI.endsWith("/logonAction")) {
            if (isLoggedIn) {
                // 로그인한 유저는 home.jsp 로 포워드
                System.out.println("[ 로그인 상태, home.jsp로 이동 ]");
                request.getRequestDispatcher("/user/home.jsp").forward(request, response);
                return false;
            } else {
                // 미 로그인한 유저는 컨트롤러로 진행
                System.out.println("[ 미로그인 상태, Controller로 진행 ]");
                return true;
            }
        }
        
        else if (requestURI.endsWith("/logout")) {
            if (isLoggedIn) {
                // 로그인한 유저는 컨트롤러로 진행
                System.out.println("[ 로그인 상태, Controller로 진행 ]");
                return true;
            } else {
                // 미 로그인한 유저는 logon.jsp 로 포워드
                System.out.println("[ 미로그인 상태, logon.jsp로 이동 ]");
                request.getRequestDispatcher("/user/logon.jsp").forward(request, response);
                return false;
            }
        }
        
        // 위 조건에 해당하지 않는 다른 모든 요청은 통과시킵니다.
        return true;
    }
}