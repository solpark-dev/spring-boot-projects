<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>구매 상세 조회 - MVC Shop</title>
    
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/form.css">
    
    <script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
    <script src="/javascript/common.js"></script>
    <script src="/javascript/getPurchase.js"></script>
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <div class="form-page">
        <div class="form-container" data-tran-no="${purchase.tranNo}">
            <div class="form-header">
                <h1>📦 구매 상세 정보</h1>
                <p>주문 번호 <strong>${purchase.tranNo}</strong>에 대한 상세 내역입니다.</p>
            </div>

            <form name="getPurchaseForm">
                <div class="form-group"><label class="form-label">주문번호</label><div class="form-value">${purchase.tranNo}</div></div>
                <div class="form-group"><label class="form-label">구매상품</label><div class="form-value"><a href="/product/getProduct?prodNo=${purchase.purchaseProd.prodNo}">${purchase.purchaseProd.prodName}</a> (${purchase.purchaseProd.price}원)</div></div>
                <div class="form-group"><label class="form-label">구매자 ID</label><div class="form-value">${purchase.buyer.userId}</div></div>
                <div class="form-group"><label class="form-label">받는 사람</label><div class="form-value">${purchase.receiverName}</div></div>
                <div class="form-group"><label class="form-label">받는 사람 연락처</label><div class="form-value">${purchase.receiverPhone}</div></div>
                <div class="form-group"><label class="form-label">배송지 주소</label><div class="form-value">${purchase.dlvyAddr}</div></div>
                <div class="form-group"><label class="form-label">배송 요청사항</label><div class="form-value">${not empty purchase.dlvyRequest ? purchase.dlvyRequest : '-'}</div></div>
                <div class="form-group"><label class="form-label">결제방법</label><div class="form-value">${purchase.paymentOption == '1' ? '현금결제' : '신용카드'}</div></div>
                <div class="form-group"><label class="form-label">주문일시</label><div class="form-value">${purchase.orderDate}</div></div>
                
                <div class="form-group">
                    <label class="form-label">주문상태</label>
                    <div class="form-value">
                        <c:choose>
                            <c:when test="${purchase.tranCode == '1'}"><span class="badge badge-info">구매완료</span></c:when>
                            <c:when test="${purchase.tranCode == '2'}"><span class="badge badge-warning">배송중</span></c:when>
                            <c:when test="${purchase.tranCode == '3'}"><span class="badge badge-success">배송완료</span></c:when>
                            <c:otherwise><span class="badge">상태미확인</span></c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="form-actions">
                    <c:if test="${user.userId == purchase.buyer.userId && purchase.tranCode == '1'}">
                        <button type="button" id="updatePurchaseBtn" class="btn-submit">✏️ 구매정보수정</button>
                    </c:if>

                    <c:if test="${user.role == 'admin' && purchase.tranCode == '1'}">
                        <button type="button" id="startDeliveryBtn" class="btn-submit">🚚 배송시작</button>
                    </c:if>

                    <c:if test="${user.userId == purchase.buyer.userId && purchase.tranCode == '2'}">
                        <button type="button" id="confirmReceiptBtn" class="btn-submit">✓ 수령확인</button>
                    </c:if>
                    
                    <button type="button" id="toListBtn" class="btn-cancel">📋 목록으로</button>
                </div>
            </form>
        </div>
    </div>
    
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    
    <%-- CSS 추가 --%>
    <style>
        .form-value { padding: 14px 16px; background-color: #f8f9fa; border-radius: 10px; font-size: 15px; color: #495057; border: 2px solid #e9ecef; }
        .form-value a { color: var(--color-primary); text-decoration: underline; }
        .badge { display: inline-block; padding: .35em .65em; font-size: .75em; font-weight: 700; line-height: 1; color: #fff; text-align: center; white-space: nowrap; vertical-align: baseline; border-radius: .25rem; }
        .badge-info { background-color: #17a2b8; }
        .badge-warning { background-color: #ffc107; color: #212529; }
        .badge-success { background-color: #28a745; }
    </style>
</body>
</html>