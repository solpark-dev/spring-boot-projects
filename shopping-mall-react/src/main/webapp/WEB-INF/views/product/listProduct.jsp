<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} - MVC Shop</title>
    
    <!-- CSS -->
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/list.css">
    
    <!-- jQuery CDN -->
    <script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
    
    <!-- JavaScript -->
    <script src="/javascript/common.js"></script>
    <script src="/javascript/listProduct.js"></script>
    
    <script>
        // 초기 데이터를 JavaScript 전역 변수로 전달
        window.initialProductList = [
            <c:forEach var="product" items="${list}" varStatus="status">
            {
                "prodNo": ${product.prodNo},
                "prodName": "${product.prodName}",
                "price": ${product.price},
                "regDate": "${product.regDate}",
                "saleStatus": "${product.saleStatus}",
                "productFiles": [
                    <c:forEach var="file" items="${product.productFiles}" varStatus="fStatus">
                    {
                        "fileId": ${file.fileId},
                        "savedName": "${file.savedName}",
                        "fileType": "${file.fileType}"
                    }<c:if test="${!fStatus.last}">,</c:if>
                    </c:forEach>
                ]
            }<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        ];
        
        window.initialTotalCount = ${resultPage.totalCount};
    </script>
</head>

<body>
    <!-- 헤더 -->
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <div class="list-page">
        <div class="list-container">
            <!-- 페이지 헤더 -->
            <div class="page-header">
                <h1 class="page-title">
                    <c:choose>
                        <c:when test="${viewMode == 'admin'}">
                            상품 관리
                        </c:when>
                        <c:otherwise>
                            상품 검색
                        </c:otherwise>
                    </c:choose>
                </h1>
                <p class="page-subtitle">원하는 상품을 검색하고 필터링하세요</p>
            </div>

            <!-- 검색 및 필터 바 -->
            <div class="search-filter-bar">
                <!-- 검색 영역 -->
                <div class="search-row">
                    <select class="search-select" name="searchCondition">
                        <option value="0" ${search.searchCondition == 0 ? 'selected' : ''}>상품번호</option>
                        <option value="1" ${search.searchCondition == 1 ? 'selected' : ''}>상품명</option>
                        <option value="2" ${search.searchCondition == 2 ? 'selected' : ''}>가격</option>
                    </select>
                    <input type="text" 
                           class="search-input" 
                           name="searchKeyword" 
                           value="${search.searchKeyword}" 
                           placeholder="검색어를 입력하세요 (2글자 이상)"
                           autocomplete="off">
                    <button class="search-btn">🔍 검색</button>
                </div>

                <!-- 필터 영역 -->
                <div class="filter-row">
                    <span class="filter-label">필터:</span>
                    
                    <select class="filter-select" name="priceRange">
                        <option value="">가격대 전체</option>
                        <option value="0-50000">~5만원</option>
                        <option value="50000-100000">5만~10만원</option>
                        <option value="100000-200000">10만~20만원</option>
                        <option value="200000-">20만원 이상</option>
                    </select>
                    
                    <select class="filter-select" name="status">
                        <option value="">상태 전체</option>
                        <option value="AVAILABLE">판매중</option>
                        <option value="SOLD">판매완료</option>
                    </select>
                    
                    <select class="filter-select" name="sortBy">
    <option value="latest">최신순</option>
    <option value="oldest">오래된순</option>
    <option value="lowPrice">가격 낮은순</option>
    <option value="highPrice">가격 높은순</option>
</select>
                </div>
            </div>

            <!-- 툴바 (결과 정보, 보기 방식 전환) -->
            <div class="list-toolbar">
                <div class="result-info">
                    검색 결과 <span class="result-count">${resultPage.totalCount}</span>건
                </div>
                
                <div class="toolbar-actions">
                    <div class="view-toggle">
                        <button class="view-btn active" data-view="grid" title="카드형">
                            ▦
                        </button>
                        <button class="view-btn" data-view="list" title="리스트형">
                            ☰
                        </button>
                    </div>
                    
                    <!-- 관리자인 경우 상품 등록 버튼 -->
                    <c:if test="${viewMode == 'admin'}">
                        <button class="search-btn" onclick="location.href='/product/addProductView'">
                            ➕ 상품 등록
                        </button>
                    </c:if>
                </div>
            </div>

            <!-- 상품 목록 (카드형) -->
            <div class="product-grid" style="display: grid;">
                <!-- JavaScript로 동적 로드 -->
            </div>

            <!-- 상품 목록 (리스트형) -->
            <div class="product-list" style="display: none;">
                <!-- JavaScript로 동적 로드 -->
            </div>
            
                <!-- 상품 목록 끝나는 곳 아래에 -->
<div id="load-more-section" style="text-align: center; padding: 40px 0;">
    <!-- 더보기 버튼 -->
    <button id="load-more-btn" class="btn-primary" style="display: none;">
        더 보기 ⬇️
    </button>
    
    <!-- 로딩 중 -->
    <div id="loading-spinner" class="loading-spinner" style="display: none;">
        <div class="spinner"></div>
        <p>로딩 중...</p>
    </div>
    
    <!-- 완료 메시지 -->
    <div id="end-message" class="end-message" style="display: none;">
        <p>모든 상품을 확인했습니다 ✨</p>
    </div>
</div>
            
        </div>
    </div>
    


    <!-- 푸터 -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>