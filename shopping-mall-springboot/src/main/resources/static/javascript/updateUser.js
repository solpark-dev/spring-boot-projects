/**
 * updateUser.js
 * 회원 정보 수정 페이지 전용 JavaScript
 */
$(function() {
    
    // 유효성 검사 초기화
    initValidation();

    // 수정 완료 버튼 클릭 이벤트
    $('#submitBtn').on('click', handleSubmit);

    // 취소 버튼 클릭 이벤트
    $('#cancelBtn').on('click', handleCancel);

    function initValidation() {
        // 이름 유효성 검사
        $('#userName').on('blur', function() {
            const value = $(this).val().trim();
            if (value.length > 0 && value.length < 2) {
                FormUtils.showValidationError($(this), '이름은 2자 이상이어야 합니다.');
            } else if (value.length >= 2) {
                FormUtils.showValidationSuccess($(this), '✓');
            }
        });

        // 이메일 유효성 검사
        $('#email').on('blur', function() {
            const value = $(this).val().trim();
            if (value.length > 0 && !FormUtils.isValidEmail(value)) {
                FormUtils.showValidationError($(this), '올바른 이메일 형식이 아닙니다.');
            } else if (value.length > 0) {
                FormUtils.showValidationSuccess($(this), '✓');
            }
        });
    }

    function handleSubmit() {
        // 유효성 검사 강제 실행
        $('input[required]').trigger('blur');

        if ($('.form-input.invalid').length > 0) {
            alert('입력 정보를 다시 확인해주세요.');
            $('.form-input.invalid').first().focus();
            return;
        }

        const formData = {
            userId: $('input[name="userId"]').val(),
            userName: $('#userName').val().trim(),
            email: $('#email').val().trim()
        };

        // formUtils.js의 AJAX 헬퍼 사용
        FormUtils.ajaxSubmitWithJson({
            url: '/user/json/updateUser', // JSON API로 변경 필요
            data: formData,
            $submitButton: $('#submitBtn'),
            success: function(response) {
                if (response.success) {
                    alert('✓ 회원 정보가 수정되었습니다.');
                    location.href = '/user/getUser?userId=' + formData.userId;
                } else {
                    alert('✕ ' + (response.message || '정보 수정에 실패했습니다.'));
                }
            }
        });
    }

    function handleCancel() {
        if (confirm('수정을 취소하시겠습니까?')) {
            history.back();
        }
    }
});