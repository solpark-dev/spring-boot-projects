<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    /* --- CSS 변수 및 기본 스타일 --- */
    :root {
        --color-text: #212529;
        --color-background: #ffffff;
        --color-primary: #667eea;
        --color-secondary: #f8f9fa;
        --font-main: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
        --container-width: 1200px;
    }

    * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
    }

    body {
        font-family: var(--font-main);
        color: var(--color-text);
        background-color: var(--color-background);
        line-height: 1.6;
        overflow-x: hidden;
    }

    a {
        text-decoration: none;
        color: inherit;
    }

    img {
        max-width: 100%;
        display: block;
    }

    .container {
        max-width: var(--container-width);
        margin: 0 auto;
        padding: 0 24px;
    }

    /* --- 헤더 --- */
    .header {
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        height: 70px;
        display: flex;
        align-items: center;
        z-index: 1000;
        transition: background-color 0.3s ease, box-shadow 0.3s ease;
        background-color: rgba(255, 255, 255, 0.95);
        backdrop-filter: blur(10px);
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
    }

    .header.scrolled {
        background-color: rgba(255, 255, 255, 0.98);
        box-shadow: 0 2px 15px rgba(0, 0, 0, 0.1);
    }

    .header-content {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
    }

    .logo {
        display: flex;
        align-items: center;
        gap: 0px;
        font-size: 24px;
        font-weight: 600;
        cursor: pointer;
        color: var(--color-primary);
    }

    .logo-image {
        width: 75px;
        height: 75px;
        object-fit: contain;
    }

    .logo-text {
        font-size: 26px;
        font-weight: 600;
    }

    .nav {
        display: flex;
        gap: 32px;
    }

    .nav-item {
        font-size: 16px;
        font-weight: 500;
        color: #495057;
        transition: color 0.2s ease;
        cursor: pointer;
    }

    .nav-item:hover {
        color: var(--color-primary);
    }

    .header-actions {
        display: flex;
        align-items: center;
        gap: 20px;
        font-size: 20px;
    }

    .header-actions span {
        cursor: pointer;
        transition: transform 0.2s ease;
    }

    .header-actions span:hover {
        transform: scale(1.1);
    }

    .mobile-menu-btn {
        display: none;
    }

    /* --- 반응형 디자인 --- */
    @media (max-width: 768px) {
        .nav {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100vh;
            background-color: white;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            gap: 20px;
            transform: translateX(-100%);
            transition: transform 0.3s ease-in-out;
        }

        .nav.active {
            transform: translateX(0);
        }

        .nav-item {
            font-size: 24px;
        }

        .mobile-menu-btn {
            display: flex;
            flex-direction: column;
            gap: 5px;
            background: none;
            border: none;
            cursor: pointer;
            z-index: 1001;
        }

        .mobile-menu-btn span {
            width: 25px;
            height: 2px;
            background-color: var(--color-text);
            border-radius: 2px;
            transition: transform 0.3s ease, opacity 0.3s ease;
        }

        .mobile-menu-btn.active span:nth-child(1) {
            transform: rotate(45deg) translate(5px, 5px);
        }

        .mobile-menu-btn.active span:nth-child(2) {
            opacity: 0;
        }

        .mobile-menu-btn.active span:nth-child(3) {
            transform: rotate(-45deg) translate(5px, -5px);
        }
        
         /* 장바구니 아이콘 래퍼 */
    .cart-icon-wrapper {
        position: relative;
        display: inline-block;
    }

    /* 장바구니 배지 */
    .cart-badge {
        position: absolute;
        top: -8px;
        right: -8px;
        background: #dc3545;
        color: white;
        font-size: 11px;
        font-weight: 600;
        padding: 2px 6px;
        border-radius: 10px;
        min-width: 18px;
        text-align: center;
        line-height: 1.2;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
        animation: badge-pop 0.3s ease;
    }

    @keyframes badge-pop {
        0% {
            transform: scale(0);
        }
        50% {
            transform: scale(1.2);
        }
        100% {
            transform: scale(1);
        }
    }

    /* 스크롤된 헤더에서도 잘 보이도록 */
    .header.scrolled .cart-badge {
        top: -6px;
        right: -6px;
    }
    }
</style>

<header class="header" id="header">
    <div class="container header-content">
        <a href="/" class="logo">
            <img src="/images/logo.png" alt="MVC Shop Logo" class="logo-image">
            <span class="logo-text">MVC Shop</span>
        </a>
        <nav class="nav" id="nav">
            <a href="#" class="nav-item" data-action="product-search">상품검색</a>
            
            <c:if test="${!empty user && user.role == 'admin'}">
                <a href="#" class="nav-item" data-action="product-manage">상품관리</a>
                <a href="#" class="nav-item" data-action="product-add">상품등록</a>
                <a href="#" class="nav-item" data-action="user-manage">회원관리</a>
            </c:if>
        </nav>
        <!-- header.jsp의 header-actions 부분만 -->

<div class="header-actions">
    <span title="검색" data-action="search">🔍</span>
    
    <!-- ✅ 장바구니 아이콘 (배지 추가) -->
    <span title="장바구니" class="cart-icon-wrapper" data-action="cart">
        🛒
        <span class="cart-badge" id="cartBadge" style="display: none;">0</span>
    </span>
    
    <!-- 로그인 전/후 아이콘 -->
    <c:if test="${empty user}">
        <span title="로그인" data-action="login">👤</span>
    </c:if>
    <c:if test="${!empty user}">
        <span title="마이페이지" data-action="mypage">👤</span>
    </c:if>
    
    <button class="mobile-menu-btn" id="mobileMenuBtn">
        <span></span>
        <span></span>
        <span></span>
    </button>
</div>
    </div>
</header>

<script type="text/javascript">
// ⭐ header.jsp에서는 이벤트 바인딩만 하고, common.js와 중복 제거
$(function() {
    // 스크롤 이벤트
    $(window).on("scroll", function() {
        if ($(window).scrollTop() > 50) {
            $(".header").addClass("scrolled");
        } else {
            $(".header").removeClass("scrolled");
        }
    });
    
    // 로고 클릭
    $(".logo").on("click", function(e) {
        e.preventDefault();
        location.href = "/";
    });
    
    // 상품검색 메뉴 클릭
    $(".nav-item[data-action='product-search']").on("click", function(e) {
        e.preventDefault();
        location.href = "/product/listProduct?menu=search";
    });
    
    // 회원관리 메뉴 클릭
    $(".nav-item[data-action='user-manage']").on("click", function(e) {
        e.preventDefault();
        location.href = "/user/listUser";
    });
    
    // 상품관리 메뉴 클릭
    $(".nav-item[data-action='product-manage']").on("click", function(e) {
        e.preventDefault();
        location.href = "/product/listProduct?menu=manage";
    });
    
    // 상품등록 메뉴 클릭
    $(".nav-item[data-action='product-add']").on("click", function(e) {
        e.preventDefault();
        location.href = "/product/addProductView";
    });
    
    // 검색 아이콘 클릭
    $("span[data-action='search']").on("click", function() {
        location.href = "/product/listProduct?menu=search";
    });
    
    // 로그인 아이콘 클릭
    $("span[data-action='login']").on("click", function() {
        location.href = "/user/login";
    });
    
    // ⭐ 변경된 부분: 로그아웃 대신 마이페이지 이동
    $("span[data-action='mypage']").on("click", function() {
        location.href = "/mypage";
    });
    
    // 모바일 메뉴 버튼
    $("#mobileMenuBtn").on("click", function() {
        $(this).toggleClass("active");
        $(".nav").toggleClass("active");
    });
    
    // 네비게이션 아이템 클릭 시 모바일 메뉴 닫기
    $(".nav-item").on("click", function() {
        if ($(window).width() <= 768) {
            $("#mobileMenuBtn").removeClass("active");
            $(".nav").removeClass("active");
        }
    });
    

    $(function() {
        // 장바구니 아이콘 클릭
        $(".cart-icon-wrapper[data-action='cart']").on("click", function() {
            // 로그인 여부 체크
            <c:if test="${empty user}">
                alert('로그인이 필요합니다.');
                location.href = "/user/login";
                return;
            </c:if>
            
            // 마이페이지 장바구니 탭으로 이동
            location.href = "/mypage#cart";
        });
    });

});
</script>