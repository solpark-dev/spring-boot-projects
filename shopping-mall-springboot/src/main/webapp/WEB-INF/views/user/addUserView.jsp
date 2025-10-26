<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원 가입 - MVC Shop</title>
    
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/form.css">
    <link rel="stylesheet" href="/css/login.css">
    
    <script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
    
    <script src="/javascript/common.js"></script>
    <script src="/javascript/formUtils.js"></script>
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <div class="login-container" style="padding-top: 100px; padding-bottom: 30px;">
        <div class="login-box" style="max-width: 600px; flex: none;">
            <div class="login-right" style="padding: 48px;">
                <div class="login-header">
                    <h2>✨ 회원 가입</h2>
                    <p>MVC Shop에 오신 것을 환영합니다</p>
                </div>

                <form name="addUserForm">
                    <div class="form-group">
                        <label class="form-label" for="userId">아이디</label>
                        <div class="input-group" style="gap: 8px;">
                            <input type="text" id="userId" name="userId" class="form-input" 
                                   placeholder="아이디 (4자 이상)" minlength="4" maxlength="50" required>
                            <button type="button" class="btn-check-duplicate" id="checkIdBtn">중복 확인</button>
                        </div>
                        <small class="validation-msg" id="idValidationMsg"></small>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="password">비밀번호</label>
                        <input type="password" id="password" name="password" class="form-input" 
                               placeholder="비밀번호 (4자 이상)" minlength="4" maxlength="50" required>
                        <small class="validation-msg"></small>
                    </div>
                    
                    <div class="form-group">
                        <label class="form-label" for="password2">비밀번호 확인</label>
                        <input type="password" id="password2" name="password2" class="form-input" 
                               placeholder="비밀번호를 다시 입력하세요" required>
                        <small class="validation-msg"></small>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="userName">이름</label>
                        <input type="text" id="userName" name="userName" class="form-input" 
                               placeholder="이름 (2자 이상)" minlength="2" maxlength="50" required>
                        <small class="validation-msg"></small>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="email">이메일</label>
                        <input type="email" id="email" name="email" class="form-input" 
                               placeholder="예: mvcshop@example.com" required>
                        <small class="validation-msg"></small>
                    </div>

                    <div class="form-actions" style="border:none; padding-top:10px; margin-top: 20px;">
                        <button type="button" id="submitBtn" class="btn-submit">✓ 가입 완료</button>
                        <button type="button" id="cancelBtn" class="btn-cancel">✕ 취소</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <script>
    $(function() {
        // 아이디 중복 확인 상태
        let isIdChecked = false;
        let isIdAvailable = false;
        
        // 실시간 유효성 검사 초기화
        initValidation();
        
        // 아이디 입력 시 중복 확인 상태 초기화
        $('#userId').on('input', function() {
            isIdChecked = false;
            isIdAvailable = false;
            FormUtils.clearValidation($(this));
            $('#idValidationMsg').text('');
        });

        // 아이디 중복 확인 버튼 클릭
        $('#checkIdBtn').on('click', handleCheckId);
        
        // 가입 완료 버튼 클릭
        $('#submitBtn').on('click', handleSubmit);

        // 취소 버튼 클릭
        $('#cancelBtn').on('click', handleCancel);

        // ========================================
        // 유효성 검사 초기화
        // ========================================
        function initValidation() {
            $('#userId').on('blur', function() {
                const value = $(this).val().trim();
                if (value.length > 0 && value.length < 4) {
                    FormUtils.showValidationError($(this), '아이디는 4자 이상이어야 합니다.');
                } else if (value.length >= 4) {
                    FormUtils.showValidationSuccess($(this), '');
                }
            });

            $('#password').on('blur', function() {
                const value = $(this).val().trim();
                if (value.length > 0 && value.length < 4) {
                    FormUtils.showValidationError($(this), '비밀번호는 4자 이상이어야 합니다.');
                } else if (value.length >= 4) {
                    FormUtils.showValidationSuccess($(this), '');
                }
            });
            
            $('#password2').on('blur', function() {
                const pw1 = $('#password').val().trim();
                const pw2 = $(this).val().trim();
                if (pw2.length > 0 && pw1 !== pw2) {
                    FormUtils.showValidationError($(this), '비밀번호가 일치하지 않습니다.');
                } else if (pw2.length > 0) {
                    FormUtils.showValidationSuccess($(this), '✓');
                }
            });
            
            $('#userName').on('blur', function() {
                const value = $(this).val().trim();
                if (value.length > 0 && value.length < 2) {
                    FormUtils.showValidationError($(this), '이름은 2자 이상이어야 합니다.');
                } else if (value.length >= 2) {
                    FormUtils.showValidationSuccess($(this), '✓');
                }
            });

            $('#email').on('blur', function() {
                const value = $(this).val().trim();
                if (value.length > 0 && !FormUtils.isValidEmail(value)) {
                    FormUtils.showValidationError($(this), '올바른 이메일 형식이 아닙니다.');
                } else if (value.length > 0) {
                    FormUtils.showValidationSuccess($(this), '✓');
                }
            });
        }
        
        // ========================================
        // 아이디 중복 확인
        // ========================================
        function handleCheckId() {
            const $userId = $('#userId');
            const userId = $userId.val().trim();

            if (userId.length < 4) {
                alert('아이디는 4자 이상 입력해주세요.');
                $userId.focus();
                return;
            }

            $.ajax({
                url: '/user/json/checkDuplication',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ userId: userId }),
                success: function(response) {
                    isIdChecked = true;
                    if (response.isDuplicate) {
                        isIdAvailable = false;
                        FormUtils.showValidationError($userId, '이미 사용 중인 아이디입니다.');
                    } else {
                        isIdAvailable = true;
                        FormUtils.showValidationSuccess($userId, '✓ 사용 가능한 아이디입니다.');
                    }
                },
                error: function() {
                    alert('중복 확인 중 오류가 발생했습니다.');
                    isIdChecked = false;
                    isIdAvailable = false;
                }
            });
        }

        // ========================================
        // 폼 제출
        // ========================================
        function handleSubmit() {
            // 모든 필드에 대해 blur 이벤트를 강제로 발생시켜 유효성 검사 실행
            $('input[required]').trigger('blur');
            
            // 유효성 검사 실패한 필드가 있는지 확인
            if ($('.form-input.invalid').length > 0) {
                alert('입력 정보를 다시 확인해주세요.');
                $('.form-input.invalid').first().focus();
                return;
            }

            // 아이디 중복 확인 여부
            if (!isIdChecked || !isIdAvailable) {
                alert('아이디 중복 확인을 해주세요.');
                $('#userId').focus();
                return;
            }

            // FormData 생성
            const formData = {
                userId: $('#userId').val().trim(),
                password: $('#password').val().trim(),
                userName: $('#userName').val().trim(),
                email: $('#email').val().trim()
            };
            
            // Ajax 요청
            FormUtils.ajaxSubmitWithJson({
                url: '/user/json/addUser',
                data: formData,
                $submitButton: $('#submitBtn'),
                success: function(response) {
                    if (response.success) {
                        alert('✓ 회원가입이 완료되었습니다. 로그인 페이지로 이동합니다.');
                        location.href = '/user/login';
                    } else {
                        alert('✕ ' + (response.message || '회원가입에 실패했습니다.'));
                    }
                },
                error: function() {
                    alert('✕ 회원가입 중 오류가 발생했습니다.');
                }
            });
        }

        // ========================================
        // 취소
        // ========================================
        function handleCancel() {
            if (confirm('작성한 모든 내용이 사라집니다. 취소하시겠습니까?')) {
                history.back();
            }
        }
    });
    </script>
</body>
</html>