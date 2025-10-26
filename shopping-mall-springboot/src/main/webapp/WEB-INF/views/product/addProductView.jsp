<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>상품 등록 - MVC Shop</title>
    
    <!-- CSS -->
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/form.css">
    
    <!-- jQuery & UI -->
    <script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    
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
                <h1>✨ 새 상품 등록</h1>
                <p>고객에게 판매할 상품 정보를 입력해주세요</p>
            </div>

            <!-- 안내 박스 -->
            <div class="info-box">
                <div class="info-box-title">
                    📌 등록 안내
                </div>
                <div class="info-box-content">
                    • 상품명과 가격은 필수 입력 항목입니다<br>
                    • 이미지는 최대 10개까지 업로드 가능합니다 (총 50MB 이하)<br>
                    • 등록된 상품은 판매 상품 관리에서 확인할 수 있습니다
                </div>
            </div>

            <!-- 상품 등록 폼 -->
            <form name="detailForm">
                
                <!-- 상품명 -->
                <div class="form-group required">
                    <label class="form-label">상품명</label>
                    <input type="text" 
                           name="prodName" 
                           class="form-input" 
                           placeholder="예: Apple 맥북 프로 14인치"
                           required 
                           fieldTitle="상품명" 
                           minLength="2"
                           maxlength="100">
                    <small class="validation-msg"></small>
                </div>

                <!-- 상품 상세 정보 -->
                <div class="form-group required">
                    <label class="form-label">상품 상세 정보</label>
                    <textarea name="prodDetail" 
                              class="form-textarea" 
                              placeholder="상품에 대한 자세한 설명을 입력해주세요"
                              required 
                              fieldTitle="상품상세정보" 
                              minLength="3"
                              maxlength="500"></textarea>
                    <small class="form-help">💡 상품의 특징, 사양, 상태 등을 상세히 작성하면 구매율이 높아집니다</small>
                </div>

                <!-- 가격 -->
                <div class="form-group required">
                    <label class="form-label">가격</label>
                    <div class="input-group">
                        <input type="text" 
                               name="price" 
                               class="form-input" 
                               placeholder="0"
                               required 
                               fieldTitle="가격" 
                               pattern="number">
                        <span class="input-suffix">원</span>
                    </div>
                    <small class="validation-msg"></small>
                </div>

                <!-- 제조일자 -->
                <div class="form-group required">
                    <label class="form-label">제조일자</label>
                    <input type="text" 
                           id="manuDate"
                           name="manuDate" 
                           class="form-input" 
                           placeholder="날짜를 선택하세요"
                           required 
                           fieldTitle="제조일자"
                           readonly>
                    <small class="form-help">📅 달력 아이콘을 클릭하여 날짜를 선택하세요</small>
                </div>

                <!-- 파일 업로드 -->
                <div class="file-upload-area">
                    <label class="form-label">상품 이미지</label>
                    <div class="file-upload-box" id="fileUploadBox">
                        <div class="file-upload-icon">📷</div>
                        <div class="file-upload-text">
                            클릭하거나 이미지를 드래그하여 업로드
                        </div>
                        <div class="file-upload-hint">
                            JPG, PNG, GIF 형식 | 최대 10개 | 총 50MB 이하
                        </div>
                    </div>
                    <input type="file" 
                           id="uploadFiles" 
                           name="uploadFiles" 
                           class="file-input-hidden"
                           multiple 
                           accept="image/*">
                    
                    <!-- 파일 미리보기 -->
                    <div class="file-preview-area" id="filePreviewArea" style="display:none;">
                        <div class="file-preview-title">📎 선택된 파일</div>
                        <div id="fileList"></div>
                    </div>
                </div>

                <!-- 버튼 그룹 -->
                <div class="form-actions">
                    <button type="button" class="btn-submit">
                        ✓ 상품 등록
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
        console.log('상품 등록 페이지 로드 완료');

        // Datepicker 초기화
        $("#manuDate").datepicker({
            dateFormat: "yymmdd",
            changeMonth: true,
            changeYear: true,
            showAnim: "slideDown",
            maxDate: 0  // 오늘까지만 선택 가능
        });

        // 가격 입력 필드에 포맷팅 적용
        $("input[name='price']").priceFormatter();

        // 파일 업로드 드래그 앤 드롭 초기화
        FormUtils.initDragAndDrop(
            $("#fileUploadBox"),
            $("#uploadFiles"),
            handleFileSelection
        );

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
        // 상품명
        $("input[name='prodName']").on("blur", function() {
            var value = $(this).val().trim();
            if (value.length < 2) {
                FormUtils.showValidationError($(this), "상품명은 2자 이상 입력해주세요.");
            } else if (value.length > 100) {
                FormUtils.showValidationError($(this), "상품명은 100자 이하로 입력해주세요.");
            } else {
                FormUtils.showValidationSuccess($(this), "✓");
            }
        });

        // 상품 상세 정보
        $("textarea[name='prodDetail']").on("blur", function() {
            var value = $(this).val().trim();
            if (value.length < 3) {
                FormUtils.showValidationError($(this), "상세 정보는 3자 이상 입력해주세요.");
            } else if (value.length > 500) {
                FormUtils.showValidationError($(this), "상세 정보는 500자 이하로 입력해주세요.");
            } else {
                FormUtils.showValidationSuccess($(this), "✓");
            }
        });

        // 가격
        $("input[name='price']").on("blur", function() {
            var value = FormUtils.parsePrice($(this).val());
            if (value <= 0) {
                FormUtils.showValidationError($(this), "가격은 0보다 커야 합니다.");
            } else {
                FormUtils.showValidationSuccess($(this), "✓");
            }
        });

        // 제조일자
        $("#manuDate").on("change", function() {
            var value = $(this).val();
            if (value && FormUtils.isValidDateFormat(value)) {
                FormUtils.showValidationSuccess($(this), "✓");
            } else if (value) {
                FormUtils.showValidationError($(this), "올바른 날짜 형식이 아닙니다.");
            }
        });
    }

    // ========================================
    // 파일 선택 처리
    // ========================================
    function handleFileSelection(files) {
        console.log('선택된 파일 개수:', files.length);

        // 파일 개수 검증
        if (files.length > 10) {
            alert("파일은 최대 10개까지 업로드할 수 있습니다.");
            $("#uploadFiles").val('');
            return;
        }

        // 전체 용량 검증
        var totalSize = 0;
        for (var i = 0; i < files.length; i++) {
            totalSize += files[i].size;
        }

        if (totalSize > 50 * 1024 * 1024) {
            alert("전체 파일 용량은 50MB를 초과할 수 없습니다.");
            $("#uploadFiles").val('');
            return;
        }

        // 미리보기 표시
        if (files.length > 0) {
            $("#filePreviewArea").show();
            FormUtils.displayFilePreview(files, $("#fileList"), {
                showDeleteBtn: false
            });
        } else {
            $("#filePreviewArea").hide();
        }
    }

 // ========================================
    // 폼 제출 처리 (수정됨)
    // ========================================
    function handleSubmit() {
        // ▼▼▼ [수정] 모든 필수 입력 필드의 blur 이벤트를 강제로 발생시켜 유효성 검사 실행 ▼▼▼
        $('form[name="detailForm"] [required]').trigger('blur');

        // ▼▼▼ [수정] formUtils.js가 추가한 'invalid' 클래스가 있는지 확인 ▼▼▼
        if ($('.form-input.invalid, .form-textarea.invalid').length > 0) {
            alert('입력 정보를 다시 확인해주세요.');
            // 첫 번째 오류 필드로 포커스 이동
            $('.invalid').first().focus();
            return;
        }

        // ▼▼▼ [제거] CommonScript.js의 FormValidation(form) 호출 삭제 ▼▼▼
        // if (!FormValidation(form)) { ... }

        var $priceField = $("input[name='price']");
        var originalPrice = $priceField.val();
        $priceField.val(originalPrice.replace(/[^0-9]/g, ''));

        var $dateField = $("#manuDate");
        var dateValue = $dateField.val().replace(/-/g, '');

        var files = $("#uploadFiles")[0].files;
        if (files.length > 10) {
            alert("파일은 최대 10개까지 업로드할 수 있습니다.");
            $priceField.val(originalPrice);
            return;
        }

        var formData = new FormData();
        formData.append("prodName", $("input[name='prodName']").val().trim());
        formData.append("prodDetail", $("textarea[name='prodDetail']").val().trim());
        formData.append("manuDate", dateValue);
        formData.append("price", $priceField.val());

        for (var i = 0; i < files.length; i++) {
            formData.append("uploadFiles", files[i]);
        }

        // Ajax 요청 (formUtils.js 사용)
        FormUtils.ajaxSubmitWithFiles({
            url: "/product/json/addProduct",
            data: formData,
            $submitButton: $(".btn-submit"),
            success: function(response) {
                if (response.success) {
                    alert("✓ " + response.message);
                    location.href = "/product/getProduct?prodNo=" + response.prodNo + "&menu=manage";
                } else {
                    alert("✕ " + (response.message || "상품 등록에 실패했습니다."));
                    $priceField.val(originalPrice);
                }
            },
            error: function() {
                alert("✕ 상품 등록 중 오류가 발생했습니다.");
                $priceField.val(originalPrice);
            }
        });
    }

    // ========================================
    // 취소 처리
    // ========================================
    function handleCancel() {
        if (confirm("입력한 모든 내용이 삭제됩니다. 계속하시겠습니까?")) {
            location.href = "/product/listProduct?menu=manage";
        }
    }
    </script>
</body>

</html>