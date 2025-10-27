<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- 
    이 파일은 AJAX로 로드되는 HTML 조각(Fragment)입니다.
    따라서 전체 HTML 구조(<html>, <head>, <body>)가 필요 없습니다.
--%>
<div class="section-header" style="text-align:center; margin-bottom: 40px;">
    <h1>👀 최근 본 상품</h1>
    <p>최근에 둘러본 상품 목록입니다.</p>
</div>

<div class="orders-list">
    <c:choose>
        <c:when test="${not empty historyList}">
            <c:forEach var="prodNo" items="${historyList}">
                <div class="order-item" style="cursor: pointer;" onclick="location.href='/product/getProduct?prodNo=${prodNo}&menu=search'">
                    <div class="order-body">
                        <div class="order-product">
                            <h4>상품 번호: ${prodNo}</h4>
                            <p>클릭하여 상품 상세 페이지로 이동</p>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="empty-state">
                <div class="empty-icon">👀</div>
                <div class="empty-title">최근 본 상품이 없습니다.</div>
                <p>다양한 상품을 둘러보세요!</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>