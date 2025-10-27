<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>상품 수정 - MVC Shop</title>
    
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
                <h1>✏️ 상품 정보 수정</h1>
                <p>상품 번호 <strong>${product.prodNo}</strong>의 정보를 수정합니다</p>
            </div>

            <!-- 안내 박스 -->
            <div class="info-box">
                <div class="info-box-title">
                    📌 수정 안내
                </div>
                <div class="info-box-content">
                    • 변경하고 싶은 항목만 수정하세요<br>
                    • 기존 파일을 삭제하려면 체크박스를 선택하세요<br>
                    • 새 이미지를 추가할 수 있습니다 (기존 파일 유지)
                </div>
            </div>

            <!-- 상품 수정 폼 -->
            <form name="detailForm">
                <!-- Hidden Fields -->
                <input type="hidden" name="prodNo" value="${product.prodNo}"/>
                
                <!-- 상품명 -->
                <div class="form-group required">
                    <label class="form-label">상품명</label>
                    <input type="text" 
                           name="prodName" 
                           class="form-input" 
                           value="${product.prodName}"
                           placeholder="상품명을 입력하세요"
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
                              maxlength="500">${product.prodDetail}</textarea>
                    <small class="form-help">💡 상품의 특징, 사양, 상태 등을 상세히 작성해주세요</small>
                </div>

                <!-- 가격 -->
                <div class="form-group required">
                    <label class="form-label">가격</label>
                    <div class="input-group">
                        <input type="text" 
                               name="price" 
                               class="form-input" 
                               value="${product.price}"
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

                <!-- 기존 파일 목록 -->
                <c:if test="${not empty product.productFiles}">
                <div class="existing-files">
                    <div class="existing-files-header">
                        <span class="existing-files-title">📎 기존 파일 (${fn:length(product.productFiles)}개)</span>
                        <label class="toggle-all-checkbox">
                            <input type="checkbox" id="toggleAllFiles">
                            전체 선택/해제
                        </label>
                    </div>
                    
                    <c:forEach var="file" items="${product.productFiles}" varStatus="status">
                    <div class="existing-file-item">
                        <input type="checkbox" 
                               name="deleteFile" 
                               value="${file.fileId}" 
                               class="existing-file-checkbox">
                        <span class="file-icon">
                            <c:choose>
                                <c:when test="${fn:startsWith(file.fileType, 'image/')}">🖼️</c:when>
                                <c:when test="${fn:contains(file.fileType, 'pdf')}">📄</c:when>
                                <c:otherwise>📎</c:otherwise>
                            </c:choose>
                        </span>
                        <div class="file-info">
                            <div class="file-name">${file.originalName}</div>
                            <div class="file-size">
                                <c:choose>
                                    <c:when test="${file.fileSize > 1024 * 1024}">
                                        ${fn:substringBefore(file.fileSize / (1024 * 1024), '.')}MB
                                    </c:when>
                                    <c:otherwise>
                                        ${fn:substringBefore(file.fileSize / 1024, '.')}KB
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                    </c:forEach>
                    
                    <small class="form-help" style="display:block; margin-top:12px;">
                        ⚠️ 체크한 파일은 수정 완료 시 삭제됩니다
                    </small>
                </div>
                </c:if>

                <!-- 새 파일 추가 -->
                <div class="file-upload-area">
                    <label class="form-label">새 이미지 추가</label>
                    <div class="file-upload-box" id="fileUploadBox">
                        <div class="file-upload-icon">📷</div>
                        <div class="file-upload-text">
                            클릭하거나 이미지를 드래그하여 업로드
                        </div>
                        <div class="file-upload-hint">
                            기존 파일은 유지되고 새 파일만 추가됩니다
                        </div>
                    </div>
                    <input type="file" 
                           id="uploadFiles" 
                           name="uploadFiles" 
                           class="file-input-hidden"
                           multiple 
                           accept="image/*">
                    
                    <!-- 새 파일 미리보기 -->
                    <div class="file-preview-area" id="newFilePreviewArea" style="display:none;">
                        <div class="file-preview-title">📎 새로 추가될 파일</div>
                        <div id="newFileList"></div>
                    </div>
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
        console.log('상품 수정 페이지 로드 완료');

        // Datepicker 초기화
        $("#manuDate").datepicker({
            dateFormat: "yymmdd",
            changeMonth: true,
            changeYear: true,
            showAnim: "slideDown",
            maxDate: 0
        });

        // ✅ 기존 날짜값 설정 (YYYYMMDD 형식을 Date 객체로 변환)
        var manuDateStr = "${product.manuDate}";
        if (manuDateStr && manuDateStr.length === 8) {
            var year = manuDateStr.substring(0, 4);
            var month = manuDateStr.substring(4, 6);
            var day = manuDateStr.substring(6, 8);
            var dateObj = new Date(year, parseInt(month) - 1, day);
            $("#manuDate").datepicker("setDate", dateObj);
        }

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

        // 전체 선택/해제
        $("#toggleAllFiles").on("change", function() {
            $('input[name="deleteFile"]').prop('checked', $(this).prop('checked'));
        });
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

        // 미리보기 표시
        if (files.length > 0) {
            $("#newFilePreviewArea").show();
            FormUtils.displayFilePreview(files, $("#newFileList"), {
                showDeleteBtn: false
            });
        } else {
            $("#newFilePreviewArea").hide();
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

        var deleteIds = [];
        $('input[name="deleteFile"]:checked').each(function() {
            deleteIds.push($(this).val());
        });
        var deleteFileIds = deleteIds.join(',');

        var files = $("#uploadFiles")[0].files;

        var formData = new FormData();
        formData.append("prodNo", $("input[name='prodNo']").val());
        formData.append("prodName", $("input[name='prodName']").val().trim());
        formData.append("prodDetail", $("textarea[name='prodDetail']").val().trim());
        formData.append("manuDate", dateValue);
        formData.append("price", $priceField.val());
        if (deleteFileIds) {
            formData.append("deleteFileIds", deleteFileIds);
        }
        for (var i = 0; i < files.length; i++) {
            formData.append("uploadFiles", files[i]);
        }

        // Ajax 요청 (formUtils.js 사용)
        FormUtils.ajaxSubmitWithFiles({
            url: "/product/json/updateProduct",
            data: formData,
            $submitButton: $(".btn-submit"),
            success: function(response) {
                if (response.success) {
                    alert("✓ " + response.message);
                    location.href = "/product/getProduct?prodNo=" + response.prodNo;
                } else {
                    alert("✕ " + (response.message || "상품 수정에 실패했습니다."));
                    $priceField.val(originalPrice);
                }
            },
            error: function() {
                alert("✕ 상품 수정 중 오류가 발생했습니다.");
                $priceField.val(originalPrice);
            }
        });
    }

    // ========================================
    // 취소 처리
    // ========================================
    function handleCancel() {
        if (confirm("수정한 내용이 모두 취소됩니다. 계속하시겠습니까?")) {
            location.href = "/product/getProduct?prodNo=${product.prodNo}";
        }
    }
    </script>
</body>

</html>