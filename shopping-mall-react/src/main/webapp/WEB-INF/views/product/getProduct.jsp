<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${product.prodName} - MVC Shop</title>
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/getProduct.css"> <%-- CSS도 외부 파일로 분리 권장 --%>
    <script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
    <script src="/javascript/common.js"></script>
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <div class="product-detail-page">
        <div class="product-detail-container">
            <div class="product-detail-card">
                <div class="product-info-grid">
                    
                    <div class="product-gallery">
                        <div class="main-image-container">
                             <c:choose>
                                 <c:when test="${not empty product.productFiles && fn:length(product.productFiles) > 0}">
                                     <img src="/uploads/${product.productFiles[0].savedName}" alt="${product.prodName}" class="main-image" id="mainImage" onerror="this.parentElement.innerHTML='<div class=\'image-placeholder\'>📦</div>'">
                                 </c:when>
                                 <c:otherwise>
                                     <div class="image-placeholder">📦</div>
                                 </c:otherwise>
                             </c:choose>
                        </div>
                        <c:if test="${not empty product.productFiles && fn:length(product.productFiles) > 1}">
                            <div class="thumbnail-list">
                                <c:forEach var="file" items="${product.productFiles}" varStatus="status">
                                    <div class="thumbnail-item ${status.index == 0 ? 'active' : ''}">
                                        <img src="/uploads/${file.savedName}" alt="${product.prodName} ${status.index + 1}" data-full="/uploads/${file.savedName}" onerror="this.style.display='none'">
                                    </div>
                                </c:forEach>
                            </div>
                        </c:if>
                    </div>

                    <!-- 오른쪽: 상품 정보 -->
                    <div class="product-info">
                        <div class="product-header">
                            <!-- 판매 상태 -->
                            <c:choose>
                                <c:when test="${product.saleStatus == 'SOLD'}">
                                    <span class="product-status status-sold">🔒 판매완료</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="product-status status-available">✓ 판매중</span>
                                </c:otherwise>
                            </c:choose>

                            <!-- 상품명 -->
                            <h1 class="product-title">${product.prodName}</h1>
                            
                            <!-- 상품 번호 -->
                            <div class="product-number">상품번호: ${product.prodNo}</div>
                        </div>

                        <!-- 가격 -->
                        <div class="product-price">${product.price}</div>

                        <!-- 상품 스펙 -->
                        <div class="product-specs">
                            <div class="spec-row">
                                <div class="spec-label">제조일자</div>
                                <div class="spec-value">
                                    ${fn:substring(product.manuDate, 0, 4)}.${fn:substring(product.manuDate, 4, 6)}.${fn:substring(product.manuDate, 6, 8)}
                                </div>
                            </div>
                            <div class="spec-row">
                                <div class="spec-label">등록일</div>
                                <div class="spec-value">${product.regDate}</div>
                            </div>
                            <div class="spec-row">
                                <div class="spec-label">상세정보</div>
                                <div class="spec-value">${product.prodDetail}</div>
                            </div>
                        </div>

                        <!-- 구매 정보 (판매완료시) -->
                        <c:if test="${product.saleStatus == 'SOLD' && not empty purchaseInfo}">
                        <div class="purchase-info-card">
                            <div class="purchase-info-title">📦 구매 정보</div>
                            <div class="purchase-detail">
                                <span class="purchase-label">구매자</span>
                                <span class="purchase-value">${purchaseInfo.buyer.userId}</span>
                            </div>
                            <div class="purchase-detail">
                                <span class="purchase-label">구매일</span>
                                <span class="purchase-value">${purchaseInfo.orderDate}</span>
                            </div>
                        </div>
                        </c:if>

                        <div class="product-actions">
                            <c:choose>
                                <c:when test="${viewMode == 'admin'}">
                                    <button type="button" class="btn-action btn-primary" onclick="ProductDetailPage.goToUpdateProduct()">✏️ 상품 수정</button>
                                </c:when>
                                <c:when test="${viewMode == 'user' && product.saleStatus == 'AVAILABLE'}">
                                    <button type="button" class="btn-action btn-cart">🛒 장바구니</button>
                                        <button type="button" id="purchaseBtn" class="btn-action btn-primary">💳 구매하기</button>
                                </c:when>
                                <c:when test="${product.saleStatus == 'SOLD'}">
                                    <button class="btn-action btn-disabled">🔒 판매완료</button>
                                </c:when>
                                <c:otherwise>
                                    <button type="button" class="btn-action btn-primary" onclick="location.href='/user/login'">🔑 로그인 후 구매</button>
                                </c:otherwise>
                            </c:choose>
                            <a href="/product/listProduct?menu=${viewMode == 'admin' ? 'manage' : 'search'}" class="btn-action btn-secondary">📋 목록으로</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="image-modal" id="imageModal">
        <span class="modal-close" id="modalClose">×</span>
        <img src="" alt="확대 이미지" class="modal-image" id="modalImage">
    </div>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <script src="/javascript/getProduct.js"></script>
    <script>
        $(function() {
            // 서버 데이터를 JavaScript 모듈로 전달하여 초기화
            ProductDetailPage.init({
                prodNo: ${product.prodNo},
                isLoggedIn: ${not empty user}
            });
        });
    </script>
</body>
</html>

