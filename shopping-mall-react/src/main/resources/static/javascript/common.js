/**
 * common.js
 * 모든 페이지에서 공통으로 사용하는 JavaScript
 */

// ========================================
// 공통 유틸리티
// ========================================
const CommonUtils = {
    /**
     * 숫자를 천 단위 구분 기호로 포맷팅
     */
    formatPrice: function(price) {
        return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },

    /**
     * 로딩 표시
     */
    showLoading: function(message) {
        message = message || '처리 중...';
        
        if ($('.loading-overlay').length === 0) {
            const html = `
                <div class="loading-overlay">
                    <div class="loading-spinner">
                        <div class="spinner"></div>
                        <p>${message}</p>
                    </div>
                </div>
            `;
            $('body').append(html);
        } else {
            $('.loading-overlay p').text(message);
            $('.loading-overlay').show();
        }
    },

    /**
     * 로딩 숨기기
     */
    hideLoading: function() {
        $('.loading-overlay').fadeOut(200, function() {
            $(this).remove();
        });
    },

    /**
     * 날짜 포맷팅
     */
    formatDate: function(dateString) {
        if (!dateString) return '';
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    },

    /**
     * 확인 다이얼로그
     */
    confirm: function(message, callback) {
        if (confirm(message)) {
            if (typeof callback === 'function') {
                callback();
            }
        }
    }
};

// ========================================
// 헤더 관련 기능
// ========================================
const HeaderModule = {
    init: function() {
        // header.jsp에서 이벤트를 처리하므로 여기서는 제거
        // 중복 방지를 위해 common.js에서는 헤더 이벤트를 바인딩하지 않음
        console.log('HeaderModule: 이벤트는 header.jsp에서 처리됨');
    }
};

// ========================================
// 푸터 관련 기능
// ========================================
const FooterModule = {
    init: function() {
        this.bindEvents();
    },

    bindEvents: function() {
        // 푸터 링크 클릭
        $(".footer-nav a").on("click", function(e) {
            e.preventDefault();
            const action = $(this).data("action");
            FooterModule.handleLinkClick(action);
        });

        // 소셜 미디어 클릭
        $(".footer-socials a").on("click", function(e) {
            e.preventDefault();
            const social = $(this).data("social");
            FooterModule.handleSocialClick(social);
        });
    },

    handleLinkClick: function(action) {
        switch(action) {
            case 'about':
                alert('About 페이지로 이동합니다.');
                // location.href = "/about";
                break;
            case 'contact':
                alert('Contact 페이지로 이동합니다.');
                // location.href = "/contact";
                break;
            case 'privacy':
                alert('Privacy Policy 페이지로 이동합니다.');
                // location.href = "/privacy";
                break;
            case 'terms':
                alert('Terms of Service 페이지로 이동합니다.');
                // location.href = "/terms";
                break;
            default:
                console.log('Unknown action:', action);
        }
    },

    handleSocialClick: function(social) {
        // 실제 소셜 미디어 URL로 변경 가능
        const socialUrls = {
            facebook: 'https://facebook.com/mvcshop',
            instagram: 'https://instagram.com/mvcshop',
            twitter: 'https://twitter.com/mvcshop',
            youtube: 'https://youtube.com/mvcshop'
        };

        console.log('Social link clicked:', social);
        // window.open(socialUrls[social], '_blank');
        alert(social + ' 페이지로 이동합니다.');
    }
	
	
};

/**
 * common.js에 추가할 CartBadge 객체
 */

// ========================================
// CartBadge 객체 - 장바구니 배지 관리
// ========================================
const CartBadge = {
    
    /**
     * 초기화
     */
    init: function() {
        console.log('CartBadge 초기화');
        
        // 로그인 상태이면 개수 조회
        const isLoggedIn = $('body').data('user-logged-in');
        if (isLoggedIn) {
            this.updateCount();
        }
    },
    
    /**
     * 장바구니 개수 조회 및 업데이트
     */
    updateCount: function() {
        console.log('장바구니 개수 조회');
        
        $.ajax({
            url: '/mypage/json/getCart',
            type: 'GET',
            success: function(response) {
                if (response.success) {
                    const count = response.totalCount || 0;
                    CartBadge.setCount(count);
                    console.log('장바구니 개수:', count);
                } else {
                    console.warn('장바구니 조회 실패:', response.message);
                }
            },
            error: function(xhr, status, error) {
                // 에러 시 조용히 실패 (사용자 경험 방해 안함)
                console.error('장바구니 개수 조회 오류:', error);
            }
        });
    },
    
    /**
     * 배지 개수 설정
     */
    setCount: function(count) {
        const $badge = $('#cartBadge');
        
        if (count > 0) {
            // 99+ 표시
            const displayCount = count > 99 ? '99+' : count;
            $badge.text(displayCount).fadeIn(200);
        } else {
            $badge.fadeOut(200);
        }
    },
    
    /**
     * 개수 증가
     */
    increment: function() {
        const $badge = $('#cartBadge');
        const currentCount = parseInt($badge.text()) || 0;
        this.setCount(currentCount + 1);
    },
    
    /**
     * 개수 감소
     */
    decrement: function() {
        const $badge = $('#cartBadge');
        const currentCount = parseInt($badge.text()) || 0;
        if (currentCount > 0) {
            this.setCount(currentCount - 1);
        }
    }
};

// ========================================
// 페이지 로드 시 자동 초기화
// ========================================
$(function() {
    console.log('common.js 로드 완료');
    
    // 헤더 초기화 (이벤트는 header.jsp에서 처리)
    HeaderModule.init();
    
    // 푸터 초기화
    FooterModule.init();
});

// ========================================
// 페이지 로드 시 자동 실행
// ========================================
$(function() {
    CartBadge.init();
});