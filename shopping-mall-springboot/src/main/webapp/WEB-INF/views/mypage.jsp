<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>마이페이지 - Model2 MVC Shop</title>
    
    <!-- CSS -->
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/mypage.css">
    
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>

<!-- 헤더 -->
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="mypage-container">
    
    <!-- 사이드바 -->
    <aside class="mypage-sidebar">
        <div class="profile-summary">
            <div class="profile-avatar">
                <i class="fas fa-user-circle"></i>
            </div>
            <h3>${user.userName}</h3>
            <p class="user-email">${user.email}</p>
            <p class="user-role">${user.role == 'admin' ? '관리자' : '일반회원'}</p>
        </div>
        
        <nav class="mypage-nav">
            <button class="tab-btn active" data-tab="profile">
                <i class="fas fa-user"></i> 프로필
            </button>
            <button class="tab-btn" data-tab="social">
                <i class="fas fa-share-alt"></i> 소셜 계정
            </button>
            <button class="tab-btn" data-tab="wishlist">
                <i class="fas fa-heart"></i> 찜 목록
            </button>
            <button class="tab-btn" data-tab="cart">
                <i class="fas fa-shopping-cart"></i> 장바구니
            </button>
            <button class="tab-btn" data-tab="orders">
                <i class="fas fa-box"></i> 주문 내역
            </button>
            <button class="tab-btn" data-tab="history">
                <i class="fas fa-history"></i> 최근 본 상품
            </button>
        </nav>
        
        <div class="sidebar-footer">
            <button id="logoutBtn" class="btn btn-secondary btn-block">
                <i class="fas fa-sign-out-alt"></i> 로그아웃
            </button>
        </div>
    </aside>
    
    <!-- 메인 콘텐츠 -->
    <main class="mypage-content">
        
        <!-- 프로필 탭 -->
        <section id="profile-section" class="tab-content active">
            <div class="content-header">
                <h2>프로필 정보</h2>
                <p class="subtitle">회원 정보를 관리할 수 있습니다.</p>
            </div>
            
            <div class="profile-card">
                <div class="profile-info">
                    <div class="info-group">
                        <label>회원번호</label>
                        <p>${user.userNo}</p>
                    </div>
                    <div class="info-group">
                        <label>아이디</label>
                        <p>${user.userId != null ? user.userId : '소셜 로그인 계정'}</p>
                    </div>
                    <div class="info-group">
                        <label>이름</label>
                        <input type="text" id="userNameInput" class="form-input" value="${user.userName}">
                    </div>
                    <div class="info-group">
                        <label>이메일</label>
                        <input type="email" id="emailInput" class="form-input" value="${user.email}">
                    </div>
                    <div class="info-group">
                        <label>회원등급</label>
                        <p>
                            <span class="badge ${user.role == 'admin' ? 'badge-admin' : 'badge-user'}">
                                ${user.role == 'admin' ? '관리자' : '일반회원'}
                            </span>
                        </p>
                    </div>
                    <div class="info-group">
                        <label>회원상태</label>
                        <p>
                            <span class="badge 
                                ${user.status == 'ACTIVE' ? 'badge-success' : ''}
                                ${user.status == 'WITHDRAWN' ? 'badge-danger' : ''}
                                ${user.status == 'DORMANT' ? 'badge-warning' : ''}">
                                ${user.status == 'ACTIVE' ? '활동중' : user.status == 'WITHDRAWN' ? '탈퇴' : '휴면'}
                            </span>
                        </p>
                    </div>
                    <div class="info-group">
                        <label>가입일</label>
                        <p>${user.regDate}</p>
                    </div>
                </div>
                
                <div class="profile-actions">
                    <button id="updateProfileBtn" class="btn btn-primary">
                        <i class="fas fa-save"></i> 정보 수정
                    </button>
                    <c:if test="${user.userId != null}">
                        <button id="changePasswordBtn" class="btn btn-secondary">
                            <i class="fas fa-key"></i> 비밀번호 변경
                        </button>
                    </c:if>
                </div>
            </div>
        </section>
        
        <!-- 소셜 계정 탭 -->
        <section id="social-section" class="tab-content">
            <div class="content-header">
                <h2>소셜 계정 관리</h2>
                <p class="subtitle">카카오, 네이버, 구글 계정을 연결하여 간편하게 로그인하세요.</p>
            </div>
            
            <div class="social-info-box">
                <i class="fas fa-info-circle"></i>
                <div>
                    <h4>소셜 계정 연결이란?</h4>
                    <p>소셜 계정을 연결하면 비밀번호 없이 간편하게 로그인할 수 있습니다.</p>
                    <p>여러 개의 소셜 계정을 동시에 연결할 수 있습니다.</p>
                </div>
            </div>
            
            <!-- ✅ 수정된 부분: JavaScript에서 동적으로 렌더링 -->
            <div class="social-accounts" 
                 data-kakao-link-url="${kakaoLinkUrl}"
                 data-naver-link-url="${naverLinkUrl}"
                 data-google-link-url="${googleLinkUrl}">
                <!-- JavaScript가 여기에 소셜 계정 카드들을 렌더링합니다 -->
                <div class="loading-spinner">
                    <div class="spinner"></div>
                    <p>소셜 계정 정보를 불러오는 중...</p>
                </div>
            </div>
        </section>
        
        <!-- 찜 목록 탭 -->
        <section id="wishlist-section" class="tab-content">
            <div class="content-header">
                <h2>찜 목록</h2>
                <p class="subtitle">관심있는 상품들을 모아보세요.</p>
            </div>
            
            <div class="wishlist-container">
                <div class="empty-message">
                    <i class="fas fa-heart"></i>
                    <p>아직 찜한 상품이 없습니다.</p>
                    <a href="/product/listProduct" class="btn btn-primary">상품 둘러보기</a>
                </div>
            </div>
        </section>
        
        <!-- 장바구니 탭 -->
        <section id="cart-section" class="tab-content">
            <div class="content-header">
                <h2>장바구니</h2>
                <p class="subtitle">선택한 상품을 확인하고 주문하세요.</p>
            </div>
            
            <div class="cart-container">
                <div class="loading-spinner">
                    <div class="spinner"></div>
                    <p>장바구니를 불러오는 중...</p>
                </div>
            </div>
        </section>
        
        <!-- 주문 내역 탭 -->
        <section id="orders-section" class="tab-content">
            <div class="content-header">
                <h2>주문 내역</h2>
                <p class="subtitle">구매한 상품의 배송 상태를 확인하세요.</p>
            </div>
            
            <div class="orders-filter">
                <button class="filter-btn active" data-status="ALL">전체</button>
                <button class="filter-btn" data-status="PAYMENT_COMPLETE">결제완료</button>
                <button class="filter-btn" data-status="SHIPPED">배송중</button>
                <button class="filter-btn" data-status="DELIVERED">배송완료</button>
            </div>
            
            <div class="orders-container">
                <div class="loading-spinner">
                    <div class="spinner"></div>
                    <p>주문 내역을 불러오는 중...</p>
                </div>
            </div>
        </section>
        
        <!-- 최근 본 상품 탭 -->
        <section id="history-section" class="tab-content">
            <div class="content-header">
                <h2>최근 본 상품</h2>
                <p class="subtitle">최근에 조회한 상품 목록입니다.</p>
            </div>
            
            <div id="history-section-content">
                <div class="loading-spinner">
                    <div class="spinner"></div>
                    <p>최근 본 상품을 불러오는 중...</p>
                </div>
            </div>
        </section>
        
    </main>
    
</div>

<!-- 푸터 -->
<jsp:include page="/WEB-INF/views/common/footer.jsp" />

<!-- JavaScript -->
<script src="/javascript/mypage.js"></script>

</body>
</html>
