/**
 * login.js
 * loginView.jsp 페이지 전용 JavaScript
 */

// ========================================
// Login 페이지 객체
// ========================================
const LoginPage = {
    /**
     * 초기화
     */
    init: function() {
        console.log('LoginPage 초기화 시작');
        this.setFocus();
        this.bindEvents();
    },

    /**
     * 아이디 입력창에 포커스
     */
    setFocus: function() {
        $("#userId").focus();
    },

    /**
     * 이벤트 바인딩
     */
    bindEvents: function() {
        // 로그인 버튼 클릭
        $("#loginBtn").on("click", function(e) {
            e.preventDefault();
            LoginPage.handleLogin();
        });

        // Enter 키로 로그인
        $("#userId, #password").on("keypress", function(e) {
            if(e.which === 13) {
                e.preventDefault();
                LoginPage.handleLogin();
            }
        });

        // 회원가입 버튼 클릭
        $("#registerBtn").on("click", function(e) {
            e.preventDefault();
            location.href = "/user/addUser";
        });

        // 실시간 유효성 검사 (선택사항)
        $("#userId").on("blur", function() {
            LoginPage.validateUserId();
        });

        $("#password").on("blur", function() {
            LoginPage.validatePassword();
        });

        console.log('로그인 이벤트 바인딩 완료');
    },

    /**
     * 로그인 처리
     */
    handleLogin: function() {
        console.log('로그인 시도');

        // 유효성 검사
        if (!this.validateForm()) {
            return;
        }

        // 로딩 표시 (선택사항)
        this.setLoading(true);

        // 폼 제출
        $("form[name='loginForm']")
            .attr("method", "POST")
            .attr("action", "/user/login")
            .attr("target", "_parent")
            .submit();
    },

    /**
     * 폼 유효성 검사
     */
    validateForm: function() {
        var isValid = true;

        // 아이디 검사
        if (!this.validateUserId()) {
            isValid = false;
        }

        // 비밀번호 검사
        if (!this.validatePassword()) {
            isValid = false;
        }

        return isValid;
    },

    /**
     * 아이디 유효성 검사
     */
    validateUserId: function() {
        var $userId = $("#userId");
        var userId = $userId.val().trim();
        var $formGroup = $userId.closest(".form-group");

        // 에러 메시지 제거
        $formGroup.removeClass("error");
        $formGroup.find(".error-message").remove();

        if (!userId || userId.length < 1) {
            this.showError($formGroup, "아이디를 입력해주세요.");
            $userId.focus();
            return false;
        }

        if (userId.length < 3) {
            this.showError($formGroup, "아이디는 3자 이상이어야 합니다.");
            $userId.focus();
            return false;
        }

        return true;
    },

    /**
     * 비밀번호 유효성 검사
     */
    validatePassword: function() {
        var $password = $("#password");
        var password = $password.val().trim();
        var $formGroup = $password.closest(".form-group");

        // 에러 메시지 제거
        $formGroup.removeClass("error");
        $formGroup.find(".error-message").remove();

        if (!password || password.length < 1) {
            this.showError($formGroup, "비밀번호를 입력해주세요.");
            $password.focus();
            return false;
        }

        if (password.length < 4) {
            this.showError($formGroup, "비밀번호는 4자 이상이어야 합니다.");
            $password.focus();
            return false;
        }

        return true;
    },

    /**
     * 에러 메시지 표시
     */
    showError: function($formGroup, message) {
        $formGroup.addClass("error");
        
        if ($formGroup.find(".error-message").length === 0) {
            $formGroup.append(
                '<div class="error-message">' + message + '</div>'
            );
        } else {
            $formGroup.find(".error-message").text(message);
        }
    },

    /**
     * 로딩 상태 설정
     */
    setLoading: function(isLoading) {
        var $loginBtn = $("#loginBtn");
        var $registerBtn = $("#registerBtn");

        if (isLoading) {
            $loginBtn.prop("disabled", true).text("로그인 중...");
            $registerBtn.prop("disabled", true);
            CommonUtils.showLoading("로그인 중입니다...");
        } else {
            $loginBtn.prop("disabled", false).text("로그인");
            $registerBtn.prop("disabled", false);
            CommonUtils.hideLoading();
        }
    }
};

// ========================================
// 페이지 로드 시 자동 실행
// ========================================
$(function() {
    LoginPage.init();
});