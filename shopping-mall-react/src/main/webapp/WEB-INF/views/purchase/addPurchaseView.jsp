<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>상품 구매 - MVC Shop</title>
    
    <!-- CSS -->
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/form.css">
    
    <!-- jQuery -->
    <script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
    
    <!-- JavaScript -->
    <script src="/javascript/common.js"></script>
    <script src="/javascript/formUtils.js"></script>
</head>

<body>
    <!-- 헤더 -->
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <!-- 메인 컨텐츠 -->
    <div class="form-page">
        <div class="form-container">
            <!-- 폼 헤더 -->
            <div class="form-header">
                <h1>🛒 상품 구매</h1>
                <p>배송 정보를 입력하고 구매를 완료하세요</p>
            </div>

            <!-- 구매 상품 정보 -->
            <div class="info-box">
                <div class="info-box-title">
                    📦 구매 상품 정보
                </div>
                <div class="info-box-content">
                    <strong style="font-size:16px; color:#212529;">${product.prodName}</strong><br>
                    <span style="font-size:20px; color:#e74c3c; font-weight:600;">₩${product.price}</span>
                </div>
            </div>

            <!-- 구매 정보 폼 -->
            <form name="purchaseForm">
                <!-- Hidden Fields -->
                <input type="hidden" name="prodNo" value="${product.prodNo}"/>

                <!-- 구매자 ID (읽기 전용) -->
                <div class="form-group">
                    <label class="form-label">구매자 ID</label>
                    <input type="text" 
                           class="form-input" 
                           value="${user.userId}"
                           readonly>
                    <small class="form-help">💡 로그인한 사용자 정보가 자동으로 입력됩니다</small>
                </div>

                <!-- 결제 방법 -->
                <div class="form-group required">
                    <label class="form-label">결제 방법</label>
                    <select name="paymentOption" class="form-select">
                        <option value="1" selected>현금 구매</option>
                        <option value="2">신용 구매</option>
                    </select>
                </div>

                <!-- 받는 사람 -->
                <div class="form-group required">
                    <label class="form-label">받는 사람</label>
                    <input type="text" 
                           name="receiverName" 
                           class="form-input" 
                           placeholder="예: 홍길동"
                           maxlength="20">
                    <small class="validation-msg"></small>
                    <small class="form-help">💡 한글 최대 6글자 (20바이트)</small>
                </div>

                <!-- 받는 사람 연락처 -->
                <div class="form-group required">
                    <label class="form-label">받는 사람 연락처</label>
                    <input type="text" 
                           name="receiverPhone" 
                           class="form-input" 
                           placeholder="예: 010-1234-5678"
                           maxlength="14">
                    <small class="validation-msg"></small>
                    <small class="form-help">💡 하이픈(-)을 포함하여 입력하세요</small>
                </div>

                <!-- 배송지 주소 -->
                <div class="form-group required">
                    <label class="form-label">배송지 주소</label>
                    <input type="text" 
                           name="dlvyAddr" 
                           class="form-input" 
                           placeholder="예: 서울시 강남구 테헤란로 123"
                           maxlength="200">
                    <small class="validation-msg"></small>
                    <small class="form-help">💡 상세 주소까지 정확하게 입력해주세요</small>
                </div>

                <!-- 배송 요청사항 -->
                <div class="form-group">
                    <label class="form-label">배송 요청사항</label>
                    <textarea name="dlvyRequest" 
                              class="form-textarea" 
                              placeholder="배송 시 요청사항을 입력해주세요 (선택사항)"
                              maxlength="200"
                              style="min-height:100px;"></textarea>
                    <small class="form-help">💬 예: 문 앞에 놔주세요, 경비실에 맡겨주세요</small>
                </div>

                <!-- 배송 희망일 -->
                <div class="form-group">
                    <label class="form-label">배송 희망일</label>
                    <input type="date" 
                           name="dlvyDate" 
                           class="form-input">
                    <small class="form-help">📅 오늘 이후 날짜를 선택하세요 (선택사항)</small>
                </div>

                <!-- 버튼 그룹 -->
                <div class="form-actions">
                    <button type="button" class="btn-submit">
                        ✓ 구매하기
                    </button>
                    <button type="button" class="btn-cancel">
                        ✕ 취소
                    </button>
                </div>

            </form>
        </div>
    </div>

    <!-- 푸터 -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <!-- JavaScript -->
    <script type="text/javascript">
    // ========================================
    // 페이지 초기화
    // ========================================
    $(function() {
        console.log('구매 페이지 로드 완료');

        // 전화번호 포맷팅 적용
        $("input[name='receiverPhone']").phoneFormatter();

        // 실시간 유효성 검사
        initValidation();

        // 버튼 이벤트
        $(".btn-submit").on("click", handleSubmit);
        $(".btn-cancel").on("click", handleCancel);

        // 첫 번째 입력 필드에 포커스
        $("input[name='receiverName']").focus();
    });

    // ========================================
    // 유효성 검사 초기화
    // ========================================
    function initValidation() {
        // 받는 사람
        $("input[name='receiverName']").on("blur", function() {
            var value = $(this).val().trim();
            var bytes = FormUtils.getByteLength(value);

            if (value.length < 1) {
                FormUtils.showValidationError($(this), "받는 사람 이름을 입력해주세요.");
            } else if (bytes > 20) {
                FormUtils.showValidationError($(this), "받는 사람 이름이 너무 깁니다. (한글 최대 6글자)");
            } else {
                FormUtils.showValidationSuccess($(this), "✓");
            }
        });

        // 받는 사람 연락처
        $("input[name='receiverPhone']").on("blur", function() {
            var value = $(this).val().trim();

            if (value.length < 1) {
                FormUtils.showValidationError($(this), "받는 사람 연락처를 입력해주세요.");
            } else if (!FormUtils.isValidPhoneNumber(value)) {
                FormUtils.showValidationError($(this), "올바른 전화번호 형식이 아닙니다. (예: 010-1234-5678)");
            } else {
                FormUtils.showValidationSuccess($(this), "✓");
            }
        });

        // 배송지 주소
        $("input[name='dlvyAddr']").on("blur", function() {
            var value = $(this).val().trim();

            if (value.length < 5) {
                FormUtils.showValidationError($(this), "배송지 주소를 상세히 입력해주세요. (5자 이상)");
            } else {
                FormUtils.showValidationSuccess($(this), "✓");
            }
        });

        // 배송 희망일
        $("input[name='dlvyDate']").on("change", function() {
            var value = $(this).val();

            if (value && !FormUtils.isDateAfterToday(value)) {
                FormUtils.showValidationError($(this), "배송 희망일은 오늘 이후로 선택해주세요.");
                $(this).val('');
            } else if (value) {
                FormUtils.showValidationSuccess($(this), "✓");
            }
        });
    }

    // ========================================
    // 폼 제출 처리
    // ========================================
    function handleSubmit() {
        // 필수 입력값 검증
        var receiverName = $("input[name='receiverName']").val().trim();
        var receiverPhone = $("input[name='receiverPhone']").val().trim();
        var dlvyAddr = $("input[name='dlvyAddr']").val().trim();

        // 받는 사람 검증
        var nameBytes = FormUtils.getByteLength(receiverName);
        if (receiverName.length < 1) {
            alert("받는 사람 이름을 입력하세요.");
            $("input[name='receiverName']").focus();
            return;
        }
        if (nameBytes > 20) {
            alert("받는 사람 이름이 너무 깁니다. (한글 최대 6글자)");
            $("input[name='receiverName']").focus().select();
            return;
        }

        // 연락처 검증
        if (receiverPhone.length < 1) {
            alert("받는 사람 연락처를 입력하세요.");
            $("input[name='receiverPhone']").focus();
            return;
        }
        if (!FormUtils.isValidPhoneNumber(receiverPhone)) {
            alert("올바른 전화번호 형식으로 입력하세요. (예: 010-1234-5678)");
            $("input[name='receiverPhone']").focus().select();
            return;
        }

        // 주소 검증
        if (dlvyAddr.length < 5) {
            alert("배송지 주소를 상세히 입력하세요. (5자 이상)");
            $("input[name='dlvyAddr']").focus();
            return;
        }

        // 배송 희망일 검증
        var dlvyDate = $("input[name='dlvyDate']").val();
        if (dlvyDate && !FormUtils.isDateAfterToday(dlvyDate)) {
            alert("배송 희망일은 오늘 이후로 선택해주세요.");
            $("input[name='dlvyDate']").focus();
            return;
        }

        // Ajax 요청 데이터 준비
        var purchaseData = {
            purchaseProd: {
                prodNo: parseInt($("input[name='prodNo']").val())
            },
            receiverName: receiverName,
            receiverPhone: receiverPhone,
            dlvyAddr: dlvyAddr,
            dlvyRequest: $("textarea[name='dlvyRequest']").val().trim(),
            paymentOption: $("select[name='paymentOption']").val(),
            dlvyDate: dlvyDate || null
        };

        console.log("구매 요청 데이터:", purchaseData);

        // Ajax 요청
        FormUtils.ajaxSubmitWithJson({
            url: "/purchase/json/addPurchase",
            data: purchaseData,
            $submitButton: $(".btn-submit"),
            success: function(response) {
                console.log("구매 성공:", response);

                if (response === true || response.success === true) {
                    alert("✓ 구매가 완료되었습니다!");
                    location.href = "/purchase/listPurchase";
                } else {
                    alert("✕ " + (response.message || "구매 처리에 실패했습니다."));
                }
            },
            error: function(xhr, status, error) {
                console.error("Ajax 오류:", error);
                console.error("상태 코드:", xhr.status);
                console.error("응답 내용:", xhr.responseText);

                var errorMsg = "구매 처리 중 오류가 발생했습니다.";

                try {
                    var errorResponse = JSON.parse(xhr.responseText);
                    if (errorResponse.message) {
                        errorMsg = errorResponse.message;
                    }
                } catch (e) {
                    // JSON 파싱 실패 시 기본 메시지 사용
                }

                alert("✕ " + errorMsg);
            }
        });
    }

    // ========================================
    // 취소 처리
    // ========================================
    function handleCancel() {
        if (confirm("구매를 취소하시겠습니까?")) {
            location.href = "/product/getProduct?prodNo=${product.prodNo}";
        }
    }
    </script>
</body>

</html>