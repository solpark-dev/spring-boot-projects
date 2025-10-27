/**
 * getProduct.js
 * 상품 상세 조회 페이지 전용 JavaScript 모듈
 */
const ProductDetailPage = {
    // 프로퍼티 초기화
    prodNo: 0,
    isLoggedIn: false,

    /**
     * 페이지 초기화 메서드
     * @param {object} options - 서버에서 전달받은 데이터 (prodNo, isLoggedIn 등)
     */
    init: function(options) {
        this.prodNo = options.prodNo;
        this.isLoggedIn = options.isLoggedIn;
        
        console.log('ProductDetailPage 초기화:', this.prodNo);

        // 이벤트 바인딩
        this.bindEvents();
    },

    /**
     * 모든 이벤트 리스너를 등록하는 메서드
     */
	bindEvents: function() {
	    // ▼▼▼ [수정된 부분] id를 사용하여 이벤트 바인딩 ▼▼▼
	    $('#updateProductBtn').on('click', this.goToUpdateProduct.bind(this));
	    $('.btn-cart').on('click', this.addToCart.bind(this));
	    $('#purchaseBtn').on('click', this.goToPurchase.bind(this));
	    $('#loginBtn').on('click', () => location.href = '/user/login');
	    
	    $('.thumbnail-item').on('click', this.handleThumbnailClick);
	    $('#mainImage').on('click', this.openImageModal);
	    $('#modalClose, #imageModal').on('click', this.closeImageModal);
	    $(document).on('keydown', (e) => {
	        if (e.key === 'Escape') {
	            this.closeImageModal.call($('#imageModal')[0], e);
	        }
	    });
	},

    /**
     * 장바구니 추가
     */
    addToCart: function() {
        if (!this.isLoggedIn) {
            alert('로그인이 필요합니다.');
            location.href = '/user/login';
            return;
        }

        const $cartBtn = $('.btn-cart');
        const originalText = $cartBtn.html(); // .text() 대신 .html() 사용 (아이콘 포함)
        $cartBtn.prop('disabled', true).html('추가 중...');
        
        $.ajax({
            url: '/mypage/json/addToCart',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ prodNo: this.prodNo, quantity: 1 }),
            success: function(response) {
                if (response.success) {
                    if (confirm(response.message + '\n장바구니로 이동하시겠습니까?')) {
                        location.href = '/mypage#cart';
                    }
                } else {
                    alert(response.message || '장바구니 추가에 실패했습니다.');
                }
            },
            error: function() {
                alert('장바구니 추가 중 오류가 발생했습니다.');
            },
            complete: function() {
                $cartBtn.prop('disabled', false).html(originalText);
            }
        });
    },

    /**
     * 상품 수정 페이지로 이동
     */
    goToUpdateProduct: function() {
        const form = $('<form>', { method: 'POST', action: '/product/updateProductView' });
        $('<input>').attr({ type: 'hidden', name: 'prodNo', value: this.prodNo }).appendTo(form);
        form.appendTo('body').submit();
    },

    /**
     * 구매 페이지로 이동
     */
    goToPurchase: function() {
        location.href = '/purchase/addPurchaseView?prodNo=' + this.prodNo;
    },

    /**
     * 썸네일 클릭 시 메인 이미지 변경
     */
    handleThumbnailClick: function() {
        const fullImageSrc = $(this).find('img').data('full');
        $('#mainImage').attr('src', fullImageSrc);
        $('.thumbnail-item').removeClass('active');
        $(this).addClass('active');
    },

    /**
     * 이미지 모달 열기
     */
    openImageModal: function() {
        const imageSrc = $(this).attr('src');
        if (imageSrc) {
            $('#modalImage').attr('src', imageSrc);
            $('#imageModal').addClass('active');
        }
    },

    /**
     * 이미지 모달 닫기
     */
    closeImageModal: function(e) {
        // 이벤트 타겟이 모달 자신 또는 닫기 버튼일 때만 닫힘
        if (e.target === this) {
            $('#imageModal').removeClass('active');
        }
    }
};