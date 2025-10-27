<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Model2 MVC Shop - Simple & Premium</title>
    
    <!-- CSS -->
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/index.css">
    
    <!-- jQuery CDN -->
    <script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
    
    <!-- JavaScript -->
    <script src="/javascript/common.js"></script>
    <script src="/javascript/index.js"></script>
</head>

<body data-user-logged-in="${!empty user}" data-user-id="${user.userId}">
    <!-- 헤더 -->
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main>
        <!-- 히어로 섹션 -->
        <section class="hero">
            <div class="hero-background"></div>
            <div class="hero-content">
                <h1>Simple shopping.<br>Better life.</h1>
                <p>당신의 일상을 더욱 가치있게 만드는 아이템을 만나보세요.</p>
                <a href="#" class="cta-button">Shop Now</a>
            </div>
        </section>

        <!-- Featured Categories -->
        <section class="section featured-categories">
            <div class="container">
                <h2 class="section-title">Featured Categories</h2>
                <div class="category-grid">
                    <!-- 관리자 전용 메뉴 -->
                    <c:if test="${user.role == 'admin'}">
                        <a href="#" class="category-card category-user-management">
                            <img src="https://images.unsplash.com/photo-1552581234-26160f608093?auto=format&fit=crop&w=600&q=60" alt="회원관리">
                            <h3>회원 관리</h3>
                        </a>
                        <a href="#" class="category-card category-product-register">
                            <img src="https://images.unsplash.com/photo-1556228453-efd6c1ff04f6?auto=format&fit=crop&w=600&q=60" alt="상품등록">
                            <h3>상품 등록</h3>
                        </a>
                        <a href="#" class="category-card category-product-management">
                            <img src="https://images.unsplash.com/photo-1542838132-92c53300491e?auto=format&fit=crop&w=600&q=60" alt="상품관리">
                            <h3>상품 관리</h3>
                        </a>
                    </c:if>
                    
                    <!-- 모든 사용자 메뉴 -->
                    <a href="#" class="category-card category-product-search">
                        <img src="https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?auto=format&fit=crop&w=600&q=60" alt="상품검색">
                        <h3>상품 검색</h3>
                    </a>
                    
                    <!-- 일반 사용자 전용 메뉴 -->
                    <c:if test="${!empty user && user.role == 'user'}">
                        <a href="#" class="category-card category-purchase-management">
                            <img src="https://images.unsplash.com/photo-1576566588028-4147f3842f27?auto=format&fit=crop&w=600&q=60" alt="구매관리">
                            <h3>구매 관리</h3>
                        </a>
                    </c:if>
                </div>
            </div>
        </section>

        <!-- Best Sellers -->
        <section class="section">
            <div class="container">
                <h2 class="section-title">Best Sellers</h2>
                <!-- AJAX로 동적 로드 -->
                <div class="product-grid">
                    <p style="text-align:center; grid-column: 1/-1; padding: 40px;">로딩 중...</p>
                </div>
            </div>
        </section>

        <!-- Why Choose Us -->
        <section class="section why-us">
            <div class="container">
                <h2 class="section-title">Why Choose Us?</h2>
                <div class="features">
                    <div class="feature-item">
                        <div class="feature-icon">🚚</div>
                        <h3>무료 배송</h3>
                        <p>모든 주문에 대해 빠르고 안전한 무료 배송 서비스를 제공합니다.</p>
                    </div>
                    <div class="feature-item">
                        <div class="feature-icon">🔒</div>
                        <h3>안전한 결제</h3>
                        <p>최신 보안 기술로 안전하게 보호되는 결제 시스템입니다.</p>
                    </div>
                    <div class="feature-item">
                        <div class="feature-icon">💎</div>
                        <h3>프리미엄 품질</h3>
                        <p>엄선된 고품질 상품만을 제공합니다.</p>
                    </div>
                    <div class="feature-item">
                        <div class="feature-icon">🎁</div>
                        <h3>특별 혜택</h3>
                        <p>회원님을 위한 독점 할인과 특별 이벤트를 즐기세요.</p>
                    </div>
                </div>
            </div>
        </section>

        <!-- Newsletter -->
        <section class="section newsletter">
            <div class="container">
                <h2 class="section-title">Stay Updated</h2>
                <p class="newsletter-description">
                    최신 상품과 특별 혜택 소식을 가장 먼저 받아보세요
                </p>
                <form class="subscribe-form">
                    <input type="email" placeholder="이메일 주소를 입력하세요" />
                    <button type="submit">구독하기</button>
                </form>
            </div>
        </section>
    </main>

    <!-- 푸터 -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>