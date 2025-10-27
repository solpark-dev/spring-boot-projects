<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>구매 내역 - MVC Shop</title>
    
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/mypage.css">
    
    <script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
    <script src="/javascript/common.js"></script>
    <script src="/javascript/listPurchase.js"></script>
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <div class="mypage-container">
        <div class="section-header" style="text-align:center; margin-bottom: 40px;">
            <h1>📦 구매 내역</h1>
            <p>회원님의 주문 내역을 확인하고 관리하세요.</p>
        </div>

        <div class="orders-list" id="purchase-list">
            </div>

        <div id="load-more-section" class="load-more-section" style="text-align: center; padding: 40px 0;">
            <button id="load-more-btn" class="btn btn-primary" style="display: none;">더 보기 ⬇️</button>
            <div id="loading-spinner" class="loading-spinner" style="display: none;">
                <div class="spinner"></div>
                <p>로딩 중...</p>
            </div>
            <div id="end-message" class="end-message" style="display: none;">
                <p>✨ 모든 주문 내역을 확인했습니다.</p>
            </div>
        </div>
    </div>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>