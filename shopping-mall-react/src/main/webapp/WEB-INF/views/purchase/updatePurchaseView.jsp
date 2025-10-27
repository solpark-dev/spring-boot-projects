<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>구매 정보 수정 - MVC Shop</title>
    
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
                <h1>✏️ 구매 정보 수정</h1>
                <p>주문 번호 <strong>${purchase.tranNo}</strong>의 배송 정보를 수정합니다</p>
            </div>

            <!-- 안내 박스 -->
            <div class="info-box">
                <div class="info-box-title">
                    📌 수정 가능 항목
                </div>
                <div class="info-box-content">
                    • 배송 전 상태에서만 정보를 수정할 수 있습니다<br>
                    • 받는 사람, 연락처, 배송지 주소를 변경할 수 있습니다<br>
                    • 상품 정보와 구매자는 변경할 수 없습니다
                </div>
            </div>

            <!-- 구매 정보 수정 폼 -->
            <form name="purchaseForm">
                <!-- Hidden Fields -->
                <input type="hidden" name="tranNo" value="${purchase.tranNo}"/>

                <!-- 주문 번호 (읽기 전용) -->
                <div class="form-group">
                    <label class="form-label">주문 번호</label>
                    <input type="text" 
                           class="form-input" 
                           value="${purchase.tranNo}"
                           readonly>
                </div>

                <!-- 구매 상품 (읽기 전용) -->
                <div class="form-group">
                    <label class="form-label">구매 상품</label>
                    <input type="text" 
                           class="form-input" 
                           value="${purchase.purchaseProd.prodName}"
                           readonly>
                </div>

                <!-- 구매자 ID (읽기 전용) -->
                <div class="form-group">
                    <label class="form-label">구매자 ID</label>
                    <input type="text" 
                           class="form-input" 
                           value="${purchase.buyer.userId}"
                           readonly>
                </div>

                <!-- 받는 사람 -->
                <div class="form-group required">
                    <label class="form-label">받는 사람</label>
                    <input type="text" 
                           name="receiverName" 
                           class="form-input" 
                           value="${purchase.receiverName}"
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
                           value="${purchase.receiverPhone}"
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
                           value="${purchase.dlvyAddr}"
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
                              style="min-height:100px;">${purchase.dlvyRequest}</textarea>
                    <small class="form-help">💬 예: 문 앞에 놔주세요, 경비실에 맡겨주세요</small>
                </div>

                <!-- 배송 희망일 -->
                <div class="form-group">
                    <label class="form-label">배송 희망일</label>
                    <input type="date" 
                           name="dlvyDate" 
                           class="form-input"
                           value="${purchase.dlvyDate}">
                    <small class="form-help">📅 오늘 이후 날짜를 선택하세요 (선택사항)</small>
                </div>

                <!-- 버튼 그룹 -->
                <div class="form-actions">
                    <button type="button" class="btn-submit">
                        ✓ 수정 완료
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
        console.log('구매 정보 수정 페이지 로드 완료');

        // 전화번호 포맷팅 적용
        $("input[name='receiverPhone']").phoneFormatter();

        // 실시간 유효성 검사
        initValidation();

        // 버튼 이벤트
        $(".btn-submit").on("click", handleSubmit);
        $(".btn-cancel").on("click", handleCancel);
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
            tranNo: parseInt($("input[name='tranNo']").val()),
            receiverName: receiverName,
            receiverPhone: receiverPhone,
            dlvyAddr: dlvyAddr,
            dlvyRequest: $("textarea[name='dlvyRequest']").val().trim(),
            dlvyDate: dlvyDate || null
        };

        console.log("구매 정보 수정 데이터:", purchaseData);

        // Ajax 요청
        FormUtils.ajaxSubmitWithJson({
            url: "/purchase/json/updatePurchase",
            data: purchaseData,
            $submitButton: $(".btn-submit"),
            success: function(response) {
                console.log("수정 성공:", response);

                if (response === true || response.success === true) {
                    alert("✓ 구매 정보가 수정되었습니다!");
                    location.href = "/purchase/getPurchase?tranNo=${purchase.tranNo}";
                } else {
                    alert("✕ " + (response.message || "수정에 실패했습니다."));
                }
            },
            error: function(xhr, status, error) {
                console.error("Ajax 오류:", error);
                alert("✕ 수정 중 오류가 발생했습니다.");
            }
        });
    }

    // ========================================
    // 취소 처리
    // ========================================
    function handleCancel() {
        if (confirm("수정한 내용이 모두 취소됩니다. 계속하시겠습니까?")) {
            location.href = "/purchase/getPurchase?tranNo=${purchase.tranNo}";
        }
    }
    </script>
</body>

</html>