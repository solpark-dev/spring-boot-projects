<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인 - MVC Shop</title>
    
    <!-- CSS -->
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/login.css">
    
    <!-- jQuery CDN -->
    <script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
    
    <!-- JavaScript -->
    <script src="/javascript/common.js"></script>
    <script src="/javascript/login.js"></script>
</head>

<body>
    <!-- 헤더 -->
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <!-- 로그인 컨테이너 -->
    <div class="login-container">
        <div class="login-box">
            <!-- 왼쪽: Welcome 메시지 -->
            <div class="login-left">
                <h1>Welcome Back!</h1>
                <p>더 나은 쇼핑 경험을 위해 로그인하세요. 맞춤형 추천과 특별 혜택이 기다리고 있습니다.</p>
            </div>
            
            <!-- 오른쪽: 로그인 폼 -->
            <div class="login-right">
                <div class="login-header">
                    <h2>로그인</h2>
                    <p>계정에 로그인하여 시작하세요</p>
                </div>

                <form name="loginForm" method="post" action="/user/login" target="_parent">
                    <div class="form-group">
                        <label class="form-label" for="userId">아이디</label>
                        <input 
                            type="text" 
                            id="userId" 
                            name="userId" 
                            class="form-input" 
                            placeholder="아이디를 입력하세요"
                            maxlength="50"
                            autocomplete="username"
                        />
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="password">비밀번호</label>
                        <input 
                            type="password" 
                            id="password" 
                            name="password" 
                            class="form-input" 
                            placeholder="비밀번호를 입력하세요"
                            maxlength="50"
                            autocomplete="current-password"
                        />
                    </div>

                    <button type="button" class="btn-login" id="loginBtn">로그인</button>
                </form>

<!-- 소셜 로그인 섹션 -->
               <div class="social-login-section">
    <div class="divider-social">
        <span>간편 로그인</span>
    </div>
    
    <a href="${kakaoAuthUrl}" class="btn-social btn-kakao">
        <span class="social-icon">💬</span>
        <span>카카오로 시작하기</span>
    </a>
    
    <a href="${naverAuthUrl}" class="btn-social btn-naver">
        <span class="social-icon">N</span>
        <span>네이버로 시작하기</span>
    </a>
    
    <a href="${googleAuthUrl}" class="btn-social btn-google">
        <span class="social-icon">G</span>
        <span>구글로 시작하기</span>
    </a>
</div>

                <div class="divider">
                    <span>또는</span>
                </div>

                <button type="button" class="btn-register" id="registerBtn">회원가입</button>

                <div class="login-footer">
                    <a href="#">비밀번호를 잊으셨나요?</a>
                </div>
            </div>
        </div>
    </div>

    <!-- 푸터 -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>