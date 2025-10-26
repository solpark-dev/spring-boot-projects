<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원 정보 조회 - MVC Shop</title>
    
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/form.css">
    
    <script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
    <script src="/javascript/common.js"></script>
    <script src="/javascript/getUser.js"></script>
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <div class="form-page">
        <div class="form-container">
            <div class="form-header">
                <h1>👤 회원 정보 조회</h1>
                <p>회원님의 상세 정보입니다.</p>
            </div>

            <form name="getUserForm">
                <div class="form-group">
                    <label class="form-label">회원번호</label>
                    <div class="form-value">${user.userNo}</div>
                </div>

                <div class="form-group">
                    <label class="form-label">아이디</label>
                    <div class="form-value">
                        <c:choose>
                            <c:when test="${not empty user.userId}">${user.userId}</c:when>
                            <c:otherwise><span class="text-muted">소셜 로그인 사용자</span></c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">이름</label>
                    <div class="form-value">${user.userName}</div>
                </div>

                <div class="form-group">
                    <label class="form-label">이메일</label>
                    <div class="form-value">${user.email}</div>
                </div>

                <div class="form-group">
                    <label class="form-label">권한</label>
                    <div class="form-value">
                        <span class="badge ${user.role == 'admin' ? 'badge-admin' : 'badge-user'}">
                            ${user.role == 'admin' ? '관리자' : '일반회원'}
                        </span>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">계정 상태</label>
                    <div class="form-value">
                        <c:choose>
                            <c:when test="${user.status == 'ACTIVE'}">활성</c:when>
                            <c:when test="${user.status == 'DORMANT'}">휴면</c:when>
                            <c:otherwise>탈퇴</c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">가입일</label>
                    <div class="form-value">${user.regDate}</div>
                </div>
                
                <div class="form-actions">
                    <button type="button" id="updateBtn" class="btn-submit">✏️ 수정</button>
                    <button type="button" id="backBtn" class="btn-cancel">📋 목록으로</button>
                </div>
            </form>
        </div>
    </div>
    
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    
    <%-- form.css에 .form-value 스타일이 없으므로, 여기에 간단히 추가 --%>
    <style>
        .form-value {
            padding: 14px 16px;
            background-color: #f8f9fa;
            border-radius: 10px;
            font-size: 15px;
            color: #495057;
            border: 2px solid #e9ecef;
        }
        .text-muted {
            color: #6c757d;
            font-style: italic;
        }
        .badge {
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 13px;
            font-weight: 600;
            color: white;
        }
        .badge-admin { background: #dc3545; }
        .badge-user { background: #28a745; }
    </style>
</body>
</html>