<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원 정보 수정 - MVC Shop</title>
    
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/form.css">
    
    <script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
    <script src="/javascript/common.js"></script>
    <script src="/javascript/formUtils.js"></script>
    <script src="/javascript/updateUser.js"></script>
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <div class="form-page">
        <div class="form-container">
            <div class="form-header">
                <h1>✏️ 회원 정보 수정</h1>
                <p>회원님의 정보를 최신으로 유지하세요.</p>
            </div>

            <form name="updateUserForm">
                <input type="hidden" name="userId" value="${user.userId}">

                <div class="form-group">
                    <label class="form-label">아이디</label>
                    <input type="text" class="form-input" value="${user.userId}" readonly>
                </div>
                
                <div class="form-group required">
                    <label class="form-label">이름</label>
                    <input type="text" id="userName" name="userName" class="form-input" value="${user.userName}" placeholder="이름 (2자 이상)" required>
                    <small class="validation-msg"></small>
                </div>

                <div class="form-group">
                    <label class="form-label">이메일</label>
                    <input type="email" id="email" name="email" class="form-input" value="${user.email}" placeholder="예: mvcshop@example.com" required>
                    <small class="validation-msg"></small>
                </div>

                <div class="form-actions">
                    <button type="button" id="submitBtn" class="btn-submit">✓ 수정 완료</button>
                    <button type="button" id="cancelBtn" class="btn-cancel">✕ 취소</button>
                </div>
            </form>
        </div>
    </div>
    
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>