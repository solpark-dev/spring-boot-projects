/**
 * index.js
 * index.jsp 페이지 전용 JavaScript
 */

// ========================================
// Index 페이지 객체
// ========================================
const IndexPage = {
    /**
     * 초기화
     */
    init: function() {
        console.log('IndexPage 초기화 시작');
        this.initCategories();
        this.loadTopProducts();
        this.initNewsletter();
        this.initCTAButton();
    },

    /**
     * 카테고리 카드 클릭 이벤트
     */
    initCategories: function() {
        // 회원관리 카드 클릭
        $(".category-user-management").on("click", function(e) {
            e.preventDefault();
            location.href = "/user/listUser";
        });
        
        // 상품등록 카드 클릭
        $(".category-product-register").on("click", function(e) {
            e.preventDefault();
            location.href = "/product/addProductView";
        });
        
        // 상품관리 카드 클릭
        $(".category-product-management").on("click", function(e) {
            e.preventDefault();
            location.href = "/product/listProduct?menu=manage";
        });
        
        // 상품검색 카드 클릭
        $(".category-product-search").on("click", function(e) {
            e.preventDefault();
            location.href = "/product/listProduct?menu=search";
        });
        
        // 구매관리 카드 클릭
        $(".category-purchase-management").on("click", function(e) {
            e.preventDefault();
            location.href = "/purchase/listPurchase";
        });

        console.log('카테고리 이벤트 바인딩 완료');
    },

    /**
     * AJAX로 최신 상품 로드
     */
    loadTopProducts: function() {
        console.log('최신 상품 로드 시작');

        $.ajax({
            url: "/product/json/getTopProducts",
            type: "GET",
            dataType: "json",
            success: function(data) {
                console.log("최신 상품 데이터:", data);
                IndexPage.displayProducts(data);
            },
            error: function(xhr, status, error) {
                console.error("상품 로드 실패:", error);
                console.error("상태:", status);
                console.error("응답:", xhr.responseText);
                $(".product-grid").html(
                    '<p style="text-align:center; grid-column: 1/-1; padding: 40px;">' +
                    '상품을 불러오는데 실패했습니다.</p>'
                );
            }
        });
    },

    /**
     * 상품 카드 표시
     */
    displayProducts: function(products) {
        var productGrid = $(".product-grid");
        productGrid.empty();
        
        if (!products || products.length === 0) {
            productGrid.append(
                '<p style="text-align:center; grid-column: 1/-1; padding: 40px;">' +
                '등록된 상품이 없습니다.</p>'
            );
            return;
        }
        
        // 상품 카드 생성
        $.each(products, function(index, product) {
            var formattedPrice = CommonUtils.formatPrice(product.price);
            var productCard = 
                '<div class="product-card" data-prod-no="' + product.prodNo + '">' +
                    '<img src="' + product.imageUrl + '" ' +
                         'alt="' + product.prodName + '" ' +
                         'class="product-image" ' +
                         'onerror="this.src=\'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=600&q=60\'">' +
                    '<div class="product-info">' +
                        '<h3>' + product.prodName + '</h3>' +
                        '<p>₩' + formattedPrice + '</p>' +
                    '</div>' +
                '</div>';
            
            productGrid.append(productCard);
        });
        
        // 상품 카드 클릭 이벤트
        $(".product-card").on("click", function() {
            var prodNo = $(this).data("prod-no");
            location.href = "/product/getProduct?prodNo=" + prodNo + "&menu=search";
        });

        console.log('상품 카드 표시 완료:', products.length + '개');
    },

    /**
     * 뉴스레터 구독 폼
     */
    initNewsletter: function() {
        $(".subscribe-form").on("submit", function(e) {
            e.preventDefault();
            
            var $emailInput = $(this).find("input[type='email']");
            var email = $emailInput.val().trim();
            
            if (!email) {
                alert("이메일 주소를 입력해주세요.");
                $emailInput.focus();
                return;
            }
            
            if (!CommonUtils.validateEmail(email)) {
                alert("올바른 이메일 주소를 입력해주세요.");
                $emailInput.focus();
                return;
            }
            
            // AJAX로 구독 처리 (실제 구현 시)
            // $.ajax({ ... });
            
            alert("구독해 주셔서 감사합니다! 🎉");
            $emailInput.val("");
        });

        console.log('뉴스레터 이벤트 바인딩 완료');
    },

    /**
     * CTA 버튼 클릭
     */
    initCTAButton: function() {
        $(".cta-button").on("click", function(e) {
            e.preventDefault();
            location.href = "/product/listProduct?menu=search";
        });

        console.log('CTA 버튼 이벤트 바인딩 완료');
    }
};

// ========================================
// 페이지 로드 시 자동 실행
// ========================================
$(function() {
    IndexPage.init();
});