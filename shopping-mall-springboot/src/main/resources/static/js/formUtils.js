/**
 * formUtils.js
 * 상품 등록/수정, 구매 등록/수정 폼에서 공통으로 사용하는 유틸리티 함수
 */

// ========================================
// 전역 네임스페이스
// ========================================
const FormUtils = {

    // ========================================
    // 유효성 검사 관련
    // ========================================

    /**
     * 유효성 검사 성공 표시
     * @param {jQuery} $element - 대상 요소
     * @param {string} message - 성공 메시지 (선택)
     */
    showValidationSuccess: function($element, message) {
        $element.removeClass('invalid').addClass('valid');
        
        // 기존 메시지 제거
        $element.siblings('.validation-msg').remove();
        
        if (message) {
            $element.after(
                '<small class="validation-msg success">' + message + '</small>'
            );
        }
    },

    /**
     * 유효성 검사 실패 표시
     * @param {jQuery} $element - 대상 요소
     * @param {string} message - 에러 메시지
     */
    showValidationError: function($element, message) {
        $element.removeClass('valid').addClass('invalid');
        
        // 기존 메시지 제거
        $element.siblings('.validation-msg').remove();
        
        $element.after(
            '<small class="validation-msg error">' + message + '</small>'
        );
    },

    /**
     * 유효성 표시 초기화
     * @param {jQuery} $element - 대상 요소
     */
    clearValidation: function($element) {
        $element.removeClass('valid invalid');
        $element.siblings('.validation-msg').remove();
    },

    // ========================================
    // 전화번호 관련
    // ========================================

    /**
     * 전화번호 유효성 검사
     * @param {string} phone - 전화번호
     * @returns {boolean} 유효 여부
     */
    isValidPhoneNumber: function(phone) {
        // 하이픈 제거
        var phoneDigits = phone.replace(/[^0-9]/g, '');
        
        // 숫자만 있고 10~11자리인지 확인
        if (!/^\d{10,11}$/.test(phoneDigits)) {
            return false;
        }
        
        // 010, 011, 016, 017, 018, 019로 시작하는지 확인
        if (!/^01[0-9]/.test(phoneDigits)) {
            return false;
        }
        
        return true;
    },

    /**
     * 전화번호 자동 포맷팅 (010-1234-5678 형식)
     * @param {string} phone - 전화번호
     * @returns {string} 포맷팅된 전화번호
     */
    formatPhoneNumber: function(phone) {
        var phoneDigits = phone.replace(/[^0-9]/g, '');
        
        if (phoneDigits.length === 10) {
            // 010-123-4567 형식
            return phoneDigits.replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3');
        } else if (phoneDigits.length === 11) {
            // 010-1234-5678 형식
            return phoneDigits.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');
        }
        
        return phone; // 형식이 맞지 않으면 원본 반환
    },

    /**
     * 전화번호 입력 필드에 실시간 포맷팅 적용
     * @param {jQuery} $input - 전화번호 입력 필드
     */
    applyPhoneFormatting: function($input) {
        $input.on('input', function() {
            var value = $(this).val();
            var formatted = FormUtils.formatPhoneNumber(value);
            if (value !== formatted) {
                $(this).val(formatted);
            }
        });
    },

    // ========================================
    // 날짜 관련
    // ========================================

    /**
     * 날짜가 오늘 이후인지 확인
     * @param {string} dateString - 날짜 문자열
     * @returns {boolean} 오늘 이후 여부
     */
    isDateAfterToday: function(dateString) {
        if (!dateString) return true; // 빈 값은 허용
        
        var selectedDate = new Date(dateString);
        var today = new Date();
        today.setHours(0, 0, 0, 0);
        
        return selectedDate >= today;
    },

    /**
     * 날짜 형식 검증 (YYYYMMDD)
     * @param {string} dateString - 날짜 문자열
     * @returns {boolean} 유효 여부
     */
    isValidDateFormat: function(dateString) {
        // YYYYMMDD 형식 확인
        if (!/^\d{8}$/.test(dateString)) {
            return false;
        }
        
        var year = parseInt(dateString.substring(0, 4));
        var month = parseInt(dateString.substring(4, 6));
        var day = parseInt(dateString.substring(6, 8));
        
        if (month < 1 || month > 12) return false;
        if (day < 1 || day > 31) return false;
        
        // 날짜 객체로 변환하여 유효성 확인
        var date = new Date(year, month - 1, day);
        return date.getFullYear() === year && 
               date.getMonth() === month - 1 && 
               date.getDate() === day;
    },

    // ========================================
    // 가격 관련
    // ========================================

    /**
     * 숫자에 천 단위 콤마 추가
     * @param {number|string} number - 숫자
     * @returns {string} 포맷팅된 숫자
     */
    formatPrice: function(number) {
        if (!number && number !== 0) return '';
        return parseInt(number).toLocaleString('ko-KR');
    },

    /**
     * 콤마 제거하고 숫자만 추출
     * @param {string} priceString - 가격 문자열
     * @returns {number} 숫자
     */
    parsePrice: function(priceString) {
        return parseInt(priceString.replace(/[^0-9]/g, '')) || 0;
    },

    /**
     * 가격 입력 필드에 실시간 포맷팅 적용
     * @param {jQuery} $input - 가격 입력 필드
     */
    applyPriceFormatting: function($input) {
        $input.on('input', function() {
            var value = $(this).val().replace(/[^0-9]/g, '');
            if (value) {
                $(this).val(FormUtils.formatPrice(value));
            } else {
                $(this).val('');
            }
        });
    },

    // ========================================
    // 파일 업로드 관련
    // ========================================

    /**
     * 파일 타입에 따른 아이콘 반환
     * @param {string} fileType - MIME 타입
     * @returns {string} 아이콘 이모지
     */
    getFileIcon: function(fileType) {
        if (fileType.startsWith('image/')) {
            return '🖼️';
        } else if (fileType.includes('pdf')) {
            return '📄';
        } else if (fileType.includes('word') || fileType.includes('document')) {
            return '📝';
        } else if (fileType.includes('excel') || fileType.includes('spreadsheet')) {
            return '📊';
        } else if (fileType.includes('zip') || fileType.includes('rar')) {
            return '📦';
        } else if (fileType.startsWith('video/')) {
            return '🎥';
        } else if (fileType.startsWith('audio/')) {
            return '🎵';
        } else {
            return '📎';
        }
    },

    /**
     * 파일 크기를 읽기 쉬운 형식으로 변환
     * @param {number} bytes - 바이트 크기
     * @returns {string} 포맷팅된 크기
     */
    formatFileSize: function(bytes) {
        if (bytes === 0) return '0 Bytes';
        
        var k = 1024;
        var sizes = ['Bytes', 'KB', 'MB', 'GB'];
        var i = Math.floor(Math.log(bytes) / Math.log(k));
        
        return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    },

    /**
     * 파일 목록 미리보기 생성
     * @param {FileList} files - 파일 목록
     * @param {jQuery} $container - 미리보기를 표시할 컨테이너
     * @param {Object} options - 옵션 (showDeleteBtn, onDelete)
     */
    displayFilePreview: function(files, $container, options) {
        options = options || {};
        
        $container.empty();
        
        if (!files || files.length === 0) {
            $container.html('<p style="color:#999; text-align:center; padding:20px;">선택된 파일이 없습니다.</p>');
            return;
        }
        
        var totalSize = 0;
        var $list = $('<ul class="file-list"></ul>');
        
        for (var i = 0; i < files.length; i++) {
            var file = files[i];
            totalSize += file.size;
            
            var $item = $('<li class="file-item"></li>');
            var icon = FormUtils.getFileIcon(file.type);
            var size = FormUtils.formatFileSize(file.size);
            
            var html = 
                '<span class="file-icon">' + icon + '</span>' +
                '<div class="file-info">' +
                    '<div class="file-name">' + file.name + '</div>' +
                    '<div class="file-size">' + size + '</div>' +
                '</div>';
            
            if (options.showDeleteBtn) {
                html += '<span class="file-delete" data-index="' + i + '">✕</span>';
            }
            
            $item.html(html);
            $list.append($item);
        }
        
        $container.append($list);
        
        // 총 용량 표시
        var totalSizeFormatted = FormUtils.formatFileSize(totalSize);
        $container.append(
            '<div style="margin-top:12px; padding-top:12px; border-top:1px solid #e9ecef; color:#6c757d; font-size:13px;">' +
                '총 ' + files.length + '개 파일, ' + totalSizeFormatted +
            '</div>'
        );
        
        // 삭제 버튼 이벤트
        if (options.showDeleteBtn && options.onDelete) {
            $container.find('.file-delete').on('click', function() {
                var index = $(this).data('index');
                options.onDelete(index);
            });
        }
    },

    /**
     * 드래그 앤 드롭 파일 업로드 초기화
     * @param {jQuery} $dropZone - 드롭 영역
     * @param {jQuery} $fileInput - 파일 입력 요소
     * @param {function} onFilesSelected - 파일 선택 시 콜백
     */
    initDragAndDrop: function($dropZone, $fileInput, onFilesSelected) {
        // 드롭존 클릭 시 파일 선택
        $dropZone.on('click', function() {
            $fileInput.click();
        });
        
        // 파일 선택 시
        $fileInput.on('change', function() {
            if (this.files.length > 0) {
                onFilesSelected(this.files);
            }
        });
        
        // 드래그 오버
        $dropZone.on('dragover', function(e) {
            e.preventDefault();
            e.stopPropagation();
            $(this).addClass('dragging');
        });
        
        // 드래그 떠남
        $dropZone.on('dragleave', function(e) {
            e.preventDefault();
            e.stopPropagation();
            $(this).removeClass('dragging');
        });
        
        // 드롭
        $dropZone.on('drop', function(e) {
            e.preventDefault();
            e.stopPropagation();
            $(this).removeClass('dragging');
            
            var files = e.originalEvent.dataTransfer.files;
            if (files.length > 0) {
                // 파일 input에 설정 (최신 브라우저만 지원)
                $fileInput[0].files = files;
                onFilesSelected(files);
            }
        });
    },

    // ========================================
    // 버튼 상태 관리
    // ========================================

    /**
     * 버튼 로딩 상태 설정
     * @param {jQuery} $button - 버튼 요소
     * @param {boolean} loading - 로딩 여부
     */
    setButtonLoading: function($button, loading) {
        if (loading) {
            $button.data('original-text', $button.text());
            $button.addClass('loading')
                   .prop('disabled', true)
                   .css('pointer-events', 'none');
        } else {
            $button.removeClass('loading')
                   .prop('disabled', false)
                   .css('pointer-events', 'auto');
            
            var originalText = $button.data('original-text');
            if (originalText) {
                $button.text(originalText);
            }
        }
    },

    // ========================================
    // Ajax 요청 헬퍼
    // ========================================

    /**
     * FormData를 사용한 Ajax 요청
     * @param {Object} config - 설정 객체
     */
    ajaxSubmitWithFiles: function(config) {
        var defaultConfig = {
            type: 'POST',
            processData: false,
            contentType: false,
            beforeSend: function() {
                if (config.$submitButton) {
                    FormUtils.setButtonLoading(config.$submitButton, true);
                }
            },
            complete: function() {
                if (config.$submitButton) {
                    FormUtils.setButtonLoading(config.$submitButton, false);
                }
            }
        };
        
        $.ajax($.extend({}, defaultConfig, config));
    },

    /**
     * JSON을 사용한 Ajax 요청
     * @param {Object} config - 설정 객체
     */
    ajaxSubmitWithJson: function(config) {
        var defaultConfig = {
            type: 'POST',
            contentType: 'application/json',
            beforeSend: function() {
                if (config.$submitButton) {
                    FormUtils.setButtonLoading(config.$submitButton, true);
                }
            },
            complete: function() {
                if (config.$submitButton) {
                    FormUtils.setButtonLoading(config.$submitButton, false);
                }
            }
        };
        
        // data를 JSON 문자열로 변환
        if (config.data && typeof config.data === 'object') {
            config.data = JSON.stringify(config.data);
        }
        
        $.ajax($.extend({}, defaultConfig, config));
    },

    // ========================================
    // 기타 유틸리티
    // ========================================

    /**
     * 바이트 길이 계산
     * @param {string} str - 문자열
     * @returns {number} 바이트 길이
     */
    getByteLength: function(str) {
        return new Blob([str]).size;
    },

    /**
     * 이메일 유효성 검사
     * @param {string} email - 이메일 주소
     * @returns {boolean} 유효 여부
     */
    isValidEmail: function(email) {
        var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
        return emailPattern.test(email);
    },

    /**
     * 입력 필드 값 트림 및 검증
     * @param {jQuery} $input - 입력 필드
     * @param {number} minLength - 최소 길이
     * @param {string} fieldName - 필드 이름
     * @returns {Object} {valid: boolean, value: string, message: string}
     */
    validateInput: function($input, minLength, fieldName) {
        var value = $input.val().trim();
        
        if (value.length < minLength) {
            return {
                valid: false,
                value: value,
                message: fieldName + '은(는) ' + minLength + '자 이상 입력해주세요.'
            };
        }
        
        return {
            valid: true,
            value: value,
            message: ''
        };
    },

    /**
     * 폼 데이터를 객체로 변환
     * @param {jQuery} $form - 폼 요소
     * @returns {Object} 폼 데이터 객체
     */
    serializeFormToObject: function($form) {
        var formData = {};
        var serializedArray = $form.serializeArray();
        
        $.each(serializedArray, function() {
            if (formData[this.name]) {
                if (!formData[this.name].push) {
                    formData[this.name] = [formData[this.name]];
                }
                formData[this.name].push(this.value || '');
            } else {
                formData[this.name] = this.value || '';
            }
        });
        
        return formData;
    },

    /**
     * 알림 메시지 표시
     * @param {string} message - 메시지
     * @param {string} type - 타입 (success, error, info, warning)
     */
    showAlert: function(message, type) {
        type = type || 'info';
        
        // 기존 알림 제거
        $('.form-alert').remove();
        
        var alertClass = 'form-alert-' + type;
        var iconMap = {
            success: '✓',
            error: '✕',
            info: 'ℹ',
            warning: '⚠'
        };
        
        var $alert = $('<div class="form-alert ' + alertClass + '">' +
            '<span class="form-alert-icon">' + iconMap[type] + '</span>' +
            '<span class="form-alert-message">' + message + '</span>' +
            '</div>');
        
        $('body').append($alert);
        
        // 3초 후 자동 제거
        setTimeout(function() {
            $alert.fadeOut(300, function() {
                $(this).remove();
            });
        }, 3000);
    },

    /**
     * 디바운스 함수 (연속 호출 방지)
     * @param {function} func - 실행할 함수
     * @param {number} wait - 대기 시간 (ms)
     * @returns {function} 디바운스된 함수
     */
    debounce: function(func, wait) {
        var timeout;
        return function() {
            var context = this;
            var args = arguments;
            clearTimeout(timeout);
            timeout = setTimeout(function() {
                func.apply(context, args);
            }, wait);
        };
    },

    /**
     * 스로틀 함수 (일정 시간마다 한 번만 실행)
     * @param {function} func - 실행할 함수
     * @param {number} limit - 제한 시간 (ms)
     * @returns {function} 스로틀된 함수
     */
    throttle: function(func, limit) {
        var inThrottle;
        return function() {
            var args = arguments;
            var context = this;
            if (!inThrottle) {
                func.apply(context, args);
                inThrottle = true;
                setTimeout(function() {
                    inThrottle = false;
                }, limit);
            }
        };
    }
};

// ========================================
// jQuery 플러그인으로 확장
// ========================================

// 전화번호 포맷팅 플러그인
$.fn.phoneFormatter = function() {
    return this.each(function() {
        FormUtils.applyPhoneFormatting($(this));
    });
};

// 가격 포맷팅 플러그인
$.fn.priceFormatter = function() {
    return this.each(function() {
        FormUtils.applyPriceFormatting($(this));
    });
};

// 유효성 검사 플러그인
$.fn.validateField = function(options) {
    var defaults = {
        minLength: 1,
        maxLength: null,
        maxBytes: null,
        required: true,
        pattern: null,
        customValidator: null,
        errorMessage: '올바른 값을 입력해주세요.'
    };
    
    var settings = $.extend({}, defaults, options);
    
    return this.each(function() {
        var $input = $(this);
        
        $input.on('blur', function() {
            var value = $input.val().trim();
            
            // 필수 체크
            if (settings.required && value.length === 0) {
                FormUtils.showValidationError($input, settings.errorMessage);
                return;
            }
            
            // 최소 길이 체크
            if (value.length > 0 && value.length < settings.minLength) {
                FormUtils.showValidationError($input, 
                    settings.minLength + '자 이상 입력해주세요.');
                return;
            }
            
            // 최대 길이 체크
            if (settings.maxLength && value.length > settings.maxLength) {
                FormUtils.showValidationError($input, 
                    settings.maxLength + '자 이하로 입력해주세요.');
                return;
            }
            
            // 최대 바이트 체크
            if (settings.maxBytes) {
                var bytes = FormUtils.getByteLength(value);
                if (bytes > settings.maxBytes) {
                    FormUtils.showValidationError($input, 
                        '최대 ' + settings.maxBytes + ' 바이트까지 입력 가능합니다.');
                    return;
                }
            }
            
            // 패턴 체크
            if (settings.pattern && !settings.pattern.test(value)) {
                FormUtils.showValidationError($input, settings.errorMessage);
                return;
            }
            
            // 커스텀 검증
            if (settings.customValidator) {
                var result = settings.customValidator(value);
                if (result !== true) {
                    FormUtils.showValidationError($input, result || settings.errorMessage);
                    return;
                }
            }
            
            // 모든 검증 통과
            if (value.length > 0) {
                FormUtils.showValidationSuccess($input);
            } else {
                FormUtils.clearValidation($input);
            }
        });
    });
};

// ========================================
// 전역에서 접근 가능하도록 설정
// ========================================
window.FormUtils = FormUtils;