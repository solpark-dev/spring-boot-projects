<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원 관리 - MVC Shop</title>
    
    <!-- CSS -->
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/user.css">
    
    <!-- jQuery CDN -->
    <script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
    
    <!-- JavaScript 모듈 -->
    <script src="/javascript/common.js"></script>
    <script src="/javascript/listUser.js"></script>
    
    <!-- 초기 데이터를 JavaScript로 전달 -->
    <script type="application/json" id="initial-data">
    {
        "list": [
            <c:forEach var="user" items="${list}" varStatus="status">
            {
    		"userNo": ${user.userNo},
    		"userId": "${user.userId}",
    		"userName": "<c:out value='${user.userName}' default='' />",
    		"email": "<c:out value='${user.email}' default='' />"
			}<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        ],
        "totalCount": <c:out value="${totalCount}" default="0" />
    }
    </script>
</head>

<body>
    <!-- 헤더 -->
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <!-- 메인 컨텐츠 -->
    <div class="user-list-page">
        <div class="user-list-container">
            
            <!-- 페이지 헤더 -->
            <div class="page-header">
                <h1 class="page-title">회원 관리</h1>
                <p class="page-subtitle">등록된 회원 목록을 확인하고 관리하세요</p>
            </div>

            <!-- 검색 폼 -->
            <form name="detailForm" method="post">
                
                <!-- 검색 필터 바 -->
                <div class="search-filter-bar">
                    <div class="search-row">
                        <select name="searchCondition" class="search-select">
                            <option value="0" ${!empty search.searchCondition && search.searchCondition == 0 ? 'selected' : ''}>
                                회원 ID
                            </option>
                            <option value="1" ${!empty search.searchCondition && search.searchCondition == 1 ? 'selected' : ''}>
                                회원명
                            </option>
                            <option value="2" ${!empty search.searchCondition && search.searchCondition == 2 ? 'selected' : ''}>
                                이메일
                            </option>
                        </select>
                        
                        <input type="text" 
                               name="searchKeyword" 
                               value="${!empty search.searchKeyword ? search.searchKeyword : ''}"
                               class="search-input" 
                               placeholder="검색어를 입력하세요 (2글자 이상)"/>
                        
                        <button type="button" class="search-btn">
                            🔍 검색
                        </button>
                    </div>
                </div>

                <!-- 결과 정보 -->
                <div class="result-info-bar">
                    <div class="result-info">
                        총 <span class="result-count"><c:out value="${totalCount}" default="0"/></span>명의 회원
                    </div>
                </div>

                <!-- 사용자 테이블 -->
                <div class="user-table-wrapper">
                    <table class="user-table">
                        <thead>
                            <tr>
                                <th>No</th>
                                <th>회원 ID</th>
                                <th>회원명</th>
                                <th>이메일</th>
                            </tr>
                        </thead>
                        <tbody id="user-list-body">
                            <!-- JavaScript가 동적으로 추가 -->
                        </tbody>
                    </table>
                </div>

            </form>

            <!-- 더보기 섹션 -->
            <div id="load-more-section" class="load-more-section">
                <!-- 더보기 버튼 -->
                <button id="load-more-btn" type="button" class="load-more-btn" style="display: none;">
                    더 보기 ⬇️
                </button>
                
                <!-- 로딩 스피너 -->
                <div id="loading-spinner" class="loading-spinner" style="display: none;">
                    <div class="spinner"></div>
                    <p>로딩 중...</p>
                </div>
                
                <!-- 완료 메시지 -->
                <div id="end-message" class="end-message" style="display: none;">
                    <p>✨ 모든 회원을 확인했습니다</p>
                </div>
            </div>

        </div>
    </div>

    <!-- 푸터 -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>