/**
 * listProduct.js
 * 상품 목록 페이지 전용 JavaScript
 */

const ListProduct = {
    // ========================================
    // 상태 관리
    // ========================================
    currentPage: 1,
    isLoading: false,
    hasMore: true,
    productCounter: 0,
    viewMode: 'grid', // 'grid' or 'list'
    searchTimeout: null,
    
    // 필터 상태
    filters: {
        searchCondition: '1', // 1: 상품명
        searchKeyword: '',
        priceRange: '',
        status: '',
        sortBy: 'latest'
    },

    /**
     * 초기화
     */
    init: function() {
        console.log('ListProduct 초기화 시작');
        
        // 초기 데이터 로드
        this.loadInitialData();
        
        // 이벤트 바인딩
        this.bindSearchEvents();
        this.bindFilterEvents();
        this.bindViewToggle();
        this.bindInfiniteScroll();
        this.bindLoadMoreButton();
        
        console.log('ListProduct 초기화 완료');
    },

    /**
     * 초기 데이터 로드
     */
    loadInitialData: function() {
        const initialList = window.initialProductList || [];
        const totalCount = window.initialTotalCount || 0;
        
        if (initialList && initialList.length > 0) {
            this.renderProducts(initialList);
            this.updateResultInfo(totalCount);
            
            if (this.productCounter >= totalCount) {
                this.hasMore = false;
            }
            
            // 초기 UI 상태 설정
            this.updateLoadMoreUI();
        } else {
            this.showEmptyState();
            this.hasMore = false;
            this.updateLoadMoreUI();
        }
    },

    /**
     * 검색 이벤트 바인딩
     */
    bindSearchEvents: function() {
        const self = this;
        
        // 검색 버튼 클릭
        $('.search-btn').on('click', function() {
            self.performSearch();
        });
        
        // 자동완성
        $('.search-input').on('keyup', function(e) {
            // 방향키와 Enter는 무시
            if ([38, 40, 13, 27].indexOf(e.keyCode) !== -1) return;
            
            const keyword = $(this).val().trim();
            const condition = $('.search-select').eq(0).val();
            
            if (self.searchTimeout) {
                clearTimeout(self.searchTimeout);
            }
            
            if (keyword.length < 2) {
                self.hideAutocomplete();
                return;
            }
            
            self.searchTimeout = setTimeout(function() {
                self.fetchAutocompleteSuggestions(condition, keyword);
            }, 300);
        });
        
        // 키보드 네비게이션 (Enter, 방향키, ESC)
        $('.search-input').on('keydown', function(e) {
            const $dropdown = $('.autocomplete-dropdown');
            const $items = $('.autocomplete-item');
            const $active = $items.filter('.active');
            let $next = null;
            
            // Enter 키 (13)
            if (e.keyCode === 13) {
                if ($dropdown.is(':visible') && $active.length > 0) {
                    e.preventDefault();
                    const selectedValue = $active.data('value');
                    $(this).val(selectedValue);
                    self.hideAutocomplete();
                    self.performSearch();
                } else {
                    e.preventDefault();
                    self.hideAutocomplete();
                    self.performSearch();
                }
                return;
            }
            
            if (!$dropdown.is(':visible') || $items.length === 0) return;
            
            // 아래 화살표 (40)
            if (e.keyCode === 40) {
                e.preventDefault();
                
                if ($active.length === 0) {
                    $next = $items.first();
                } else {
                    $next = $active.next('.autocomplete-item');
                    if ($next.length === 0) {
                        $next = $items.first();
                    }
                }
                
                $items.removeClass('active');
                $next.addClass('active');
            }
            // 위 화살표 (38)
            else if (e.keyCode === 38) {
                e.preventDefault();
                
                if ($active.length === 0) {
                    $next = $items.last();
                } else {
                    $next = $active.prev('.autocomplete-item');
                    if ($next.length === 0) {
                        $next = $items.last();
                    }
                }
                
                $items.removeClass('active');
                $next.addClass('active');
            }
            // ESC (27)
            else if (e.keyCode === 27) {
                self.hideAutocomplete();
            }
        });
        
        // 자동완성 항목 클릭
        $(document).on('click', '.autocomplete-item', function() {
            const value = $(this).data('value');
            $('.search-input').val(value);
            self.hideAutocomplete();
            self.performSearch();
        });
        
        // 외부 클릭 시 자동완성 닫기
        $(document).on('click', function(e) {
            if (!$(e.target).closest('.search-input, .autocomplete-dropdown').length) {
                self.hideAutocomplete();
            }
        });
    },

    /**
     * 필터 이벤트 바인딩
     */
    bindFilterEvents: function() {
        const self = this;
        
        // 필터 변경 시 자동 검색
        $('.filter-select').on('change', function() {
            const name = $(this).attr('name');
            const value = $(this).val();
            self.filters[name] = value;
            self.performSearch();
        });
    },

    /**
     * 보기 방식 토글
     */
    bindViewToggle: function() {
        const self = this;
        
        $('.view-btn').on('click', function() {
            const mode = $(this).data('view');
            self.viewMode = mode;
            
            $('.view-btn').removeClass('active');
            $(this).addClass('active');
            
            self.updateViewMode();
        });
    },

    /**
     * 무한 스크롤
     */
    bindInfiniteScroll: function() {
        const self = this;
        
        $(window).on('scroll', function() {
            if ($(window).scrollTop() + $(window).height() >= $(document).height() - 300) {
                self.loadNextPage();
            }
        });
    },

    /**
     * 더보기 버튼 이벤트
     */
    bindLoadMoreButton: function() {
        const self = this;
        
        $('#load-more-btn').on('click', function() {
            console.log('더보기 버튼 클릭');
            self.loadNextPage();
        });
    },

    /**
     * UI 상태 업데이트 (버튼/로딩/완료 메시지)
     */
    updateLoadMoreUI: function() {
        const $loadMoreBtn = $('#load-more-btn');
        const $loadingSpinner = $('#loading-spinner');
        const $endMessage = $('#end-message');
        
        // 모든 UI 숨기기
        $loadMoreBtn.hide();
        $loadingSpinner.hide();
        $endMessage.hide();
        
        // 상태에 따라 표시
        if (this.isLoading) {
            // 로딩 중 → 스피너 표시
            $loadingSpinner.show();
            console.log('UI: 로딩 스피너 표시');
        } else if (!this.hasMore) {
            // 더 이상 데이터 없음 → 완료 메시지
            $endMessage.show();
            console.log('UI: 완료 메시지 표시');
        } else {
            // 더 불러올 데이터 있음 → 더보기 버튼
            $loadMoreBtn.show();
            console.log('UI: 더보기 버튼 표시');
        }
    },

    /**
     * 검색 실행
     */
    performSearch: function() {
        console.log('검색 실행');
        
        // 상태 초기화
        this.currentPage = 1;
        this.productCounter = 0;
        this.hasMore = true;
        
        // 검색 조건 업데이트
        this.filters.searchCondition = $('.search-select').eq(0).val();
        this.filters.searchKeyword = $('.search-input').val().trim();
        
        // 기존 상품 제거
        this.clearProducts();
        
        // 첫 페이지 로드
        this.loadProducts(1);
    },

    /**
     * 상품 로드
     */
    loadProducts: function(page) {
        if (this.isLoading) return;
        
        this.isLoading = true;
        this.updateLoadMoreUI();  // ⭐ 로딩 스피너 표시
        
        const self = this;
        
        $.ajax({
            url: '/product/json/getFilteredProducts',
            type: 'GET',
            data: {
                currentPage: page,
                pageSize: 12,
                searchCondition: this.filters.searchCondition,
                searchKeyword: this.filters.searchKeyword,
                priceRange: this.filters.priceRange,
                status: this.filters.status,
                sortBy: this.filters.sortBy
            },
            success: function(response) {
                console.log('상품 로드 성공:', response);
                
                if (response.success && response.data) {
                    const products = response.data.list || [];
                    const totalCount = response.data.totalCount || 0;
                    
                    // 빈 배열이 오면 더 이상 데이터가 없다는 의미
                    if (products.length === 0) {
                        self.hasMore = false;
                        // updateLoadMoreUI()가 complete에서 호출되므로 여기서는 생략
                        return;
                    }
                    
                    self.renderProducts(products);
                    self.updateResultInfo(totalCount);
                    
                    // 현재까지 로드한 상품 수가 전체 개수 이상이면 종료
                    if (self.productCounter >= totalCount) {
                        self.hasMore = false;
                        // updateLoadMoreUI()가 complete에서 호출되므로 여기서는 생략
                    }
                    
                    self.currentPage = page;
                } else {
                    console.error('응답 형식 오류');
                    self.hasMore = false;
                }
            },
            error: function(xhr, status, error) {
                console.error('상품 로드 실패:', error);
                alert('상품 목록을 불러오는데 실패했습니다.');
                self.hasMore = false;
            },
            complete: function() {
                self.isLoading = false;
                self.updateLoadMoreUI();  // ⭐ UI 상태 업데이트
            }
        });
    },

    /**
     * 다음 페이지 로드
     */
    loadNextPage: function() {
        if (!this.hasMore || this.isLoading) return;
        
        console.log('다음 페이지 로드:', this.currentPage + 1);
        this.loadProducts(this.currentPage + 1);
    },

    /**
     * 상품 렌더링
     */
    renderProducts: function(products) {
        if (!products || products.length === 0) {
            if (this.productCounter === 0) {
                this.showEmptyState();
            }
            return;
        }
        
        const container = this.viewMode === 'grid' ? $('.product-grid') : $('.product-list');
        
        products.forEach(product => {
            this.productCounter++;
            const html = this.viewMode === 'grid' 
                ? this.createCardHTML(product) 
                : this.createListHTML(product);
            container.append(html);
        });
        
        // 클릭 이벤트 바인딩
        this.bindProductClick();
    },

    /**
     * 카드형 HTML 생성
     */
    createCardHTML: function(product) {
        const formattedPrice = CommonUtils.formatPrice(product.price);
        const statusClass = product.saleStatus === 'SOLD' ? 'badge-sold' : 'badge-available';
        const statusText = product.saleStatus === 'SOLD' ? '판매완료' : '판매중';
        const imageUrl = this.getProductImage(product);
        
        return `
            <div class="product-card" data-prod-no="${product.prodNo}">
                <div class="product-image-wrapper">
                    <img src="${imageUrl}" class="product-image" 
                         onerror="this.src='/images/default-image.png'">
                    <span class="product-badge ${statusClass}">${statusText}</span>
                </div>
                <div class="product-info">
                    <div class="product-name">${product.prodName}</div>
                    <div class="product-price">₩${formattedPrice}</div>
                    <div class="product-date">${product.regDate || ''}</div>
                </div>
            </div>
        `;
    },

    /**
     * 리스트형 HTML 생성
     */
    createListHTML: function(product) {
        const formattedPrice = CommonUtils.formatPrice(product.price);
        const statusClass = product.saleStatus === 'SOLD' ? 'badge-sold' : 'badge-available';
        const statusText = product.saleStatus === 'SOLD' ? '판매완료' : '판매중';
        const imageUrl = this.getProductImage(product);
        
        return `
            <div class="product-list-item" data-prod-no="${product.prodNo}">
                <div class="list-image-wrapper">
                    <img src="${imageUrl}" class="list-image"
                         onerror="this.src='/images/default-image.png'">
                </div>
                <div class="list-content">
                    <div class="list-header">
                        <div class="list-title">${product.prodName}</div>
                        <div class="list-price-wrapper">
                            <div class="list-price">₩${formattedPrice}</div>
                            <span class="product-badge ${statusClass}">${statusText}</span>
                        </div>
                    </div>
                    <div class="list-meta">
                        <span>등록일: ${product.regDate || ''}</span>
                    </div>
                </div>
            </div>
        `;
    },

    /**
     * 상품 이미지 가져오기
     */
    getProductImage: function(product) {
        if (product.productFiles && product.productFiles.length > 0) {
            for (let file of product.productFiles) {
                if (file.fileType && file.fileType.startsWith('image/')) {
                    return '/uploads/' + file.savedName;
                }
            }
        }
        return '/images/default-image.png';
    },

    /**
     * 상품 클릭 이벤트
     */
    bindProductClick: function() {
        $('.product-card, .product-list-item').off('click').on('click', function() {
            const prodNo = $(this).data('prod-no');
            const menu = new URLSearchParams(window.location.search).get('menu') || 'search';
            location.href = `/product/getProduct?prodNo=${prodNo}&menu=${menu}`;
        });
    },

    /**
     * 자동완성 가져오기
     */
    fetchAutocompleteSuggestions: function(condition, keyword) {
        const self = this;
        
        $.ajax({
            url: '/product/json/searchSuggestions',
            type: 'GET',
            data: {
                searchCondition: condition,
                searchKeyword: keyword
            },
            success: function(suggestions) {
                self.displayAutocomplete(suggestions, keyword);
            },
            error: function() {
                self.hideAutocomplete();
            }
        });
    },

    /**
     * 자동완성 표시
     */
    displayAutocomplete: function(suggestions, keyword) {
        if (!suggestions || suggestions.length === 0) {
            this.hideAutocomplete();
            return;
        }
        
        let html = '';
        suggestions.forEach(item => {
            const highlighted = item.label.replace(
                new RegExp(keyword, 'gi'), 
                match => `<strong>${match}</strong>`
            );
            html += `<div class="autocomplete-item" data-value="${item.displayText}">${highlighted}</div>`;
        });
        
        const $dropdown = $('.autocomplete-dropdown');
        if ($dropdown.length === 0) {
            $('.search-input').parent().css('position', 'relative').append(
                '<div class="autocomplete-dropdown"></div>'
            );
        }
        
        $('.autocomplete-dropdown').html(html).show();
    },

    /**
     * 자동완성 숨기기
     */
    hideAutocomplete: function() {
        $('.autocomplete-dropdown').hide();
    },

    /**
     * 보기 방식 업데이트
     */
    updateViewMode: function() {
        // 먼저 CSS display 속성을 변경해서 즉각적으로 UI가 바뀌는 것처럼 보이게 함
        if (this.viewMode === 'grid') {
            $('.product-list').hide();
            $('.product-grid').show();
        } else {
            $('.product-grid').hide();
            $('.product-list').show();
        }
        
        // 새로운 뷰 모드로 다시 검색
        this.performSearch();
    },

    /**
     * 상품 제거
     */
    clearProducts: function() {
        $('.product-grid, .product-list').empty();
        this.productCounter = 0;
        
        // ⭐ 더보기 섹션 UI 초기화
        $('#load-more-btn').hide();
        $('#loading-spinner').hide();
        $('#end-message').hide();
    },

    /**
     * 결과 정보 업데이트
     */
    updateResultInfo: function(totalCount) {
        $('.result-info').html(
            `검색 결과 <span class="result-count">${totalCount}</span>건`
        );
    },

    /**
     * 빈 상태 표시
     */
    showEmptyState: function() {
        const html = `
            <div class="empty-state">
                <div class="empty-icon">📦</div>
                <div class="empty-title">검색 결과가 없습니다</div>
                <div class="empty-description">다른 검색어로 시도해보세요</div>
            </div>
        `;
        $('.product-grid, .product-list').html(html);
    },
	
	/**
	 * listProduct.js
	 * 상품 목록 페이지 - 장바구니 버튼 추가된 버전
	 */

	// ========================================
	// ListProduct 객체에 추가할 메서드들
	// ========================================

	/**
	 * 카드형 HTML 생성 (장바구니 버튼 추가)
	 */
	createCardHTML: function(product) {
	    const formattedPrice = CommonUtils.formatPrice(product.price);
	    const statusClass = product.saleStatus === 'SOLD' ? 'badge-sold' : 'badge-available';
	    const statusText = product.saleStatus === 'SOLD' ? '판매완료' : '판매중';
	    const imageUrl = this.getProductImage(product);
	    
	    // 로그인 여부 체크 (body에 data attribute로 전달받음)
	    const isLoggedIn = $('body').data('user-logged-in');
	    const userRole = $('body').data('user-role');
	    
	    // 장바구니 버튼 HTML (조건부 렌더링)
	    let cartButtonHTML = '';
	    if (isLoggedIn && userRole !== 'admin' && product.saleStatus === 'AVAILABLE') {
	        cartButtonHTML = `
	            <button class="cart-btn-small" 
	                    data-prod-no="${product.prodNo}"
	                    onclick="event.stopPropagation(); ListProduct.addToCart(${product.prodNo});"
	                    title="장바구니에 추가">
	                🛒
	            </button>
	        `;
	    }
	    
	    return `
	        <div class="product-card" data-prod-no="${product.prodNo}">
	            <div class="product-image-wrapper">
	                <img src="${imageUrl}" class="product-image" 
	                     onerror="this.src='/images/default-image.png'">
	                <span class="product-badge ${statusClass}">${statusText}</span>
	                ${cartButtonHTML}
	            </div>
	            <div class="product-info">
	                <div class="product-name">${product.prodName}</div>
	                <div class="product-price">₩${formattedPrice}</div>
	                <div class="product-date">${product.regDate || ''}</div>
	            </div>
	        </div>
	    `;
	},

	/**
	 * 리스트형 HTML 생성 (장바구니 버튼 추가)
	 */
	createListHTML: function(product) {
	    const formattedPrice = CommonUtils.formatPrice(product.price);
	    const statusClass = product.saleStatus === 'SOLD' ? 'badge-sold' : 'badge-available';
	    const statusText = product.saleStatus === 'SOLD' ? '판매완료' : '판매중';
	    const imageUrl = this.getProductImage(product);
	    
	    // 로그인 여부 체크
	    const isLoggedIn = $('body').data('user-logged-in');
	    const userRole = $('body').data('user-role');
	    
	    // 장바구니 버튼 HTML
	    let cartButtonHTML = '';
	    if (isLoggedIn && userRole !== 'admin' && product.saleStatus === 'AVAILABLE') {
	        cartButtonHTML = `
	            <button class="cart-btn-list" 
	                    data-prod-no="${product.prodNo}"
	                    onclick="event.stopPropagation(); ListProduct.addToCart(${product.prodNo});"
	                    title="장바구니에 추가">
	                🛒 담기
	            </button>
	        `;
	    }
	    
	    return `
	        <div class="product-list-item" data-prod-no="${product.prodNo}">
	            <div class="list-image-wrapper">
	                <img src="${imageUrl}" class="list-image"
	                     onerror="this.src='/images/default-image.png'">
	            </div>
	            <div class="list-content">
	                <div class="list-header">
	                    <div class="list-title">${product.prodName}</div>
	                    <div class="list-price-wrapper">
	                        <div class="list-price">₩${formattedPrice}</div>
	                        <span class="product-badge ${statusClass}">${statusText}</span>
	                        ${cartButtonHTML}
	                    </div>
	                </div>
	                <div class="list-meta">
	                    <span>등록일: ${product.regDate || ''}</span>
	                </div>
	            </div>
	        </div>
	    `;
	},

	/**
	 * ✅ 장바구니에 상품 추가
	 */
	addToCart: function(prodNo) {
	    console.log('장바구니 추가:', prodNo);
	    
	    // 로그인 체크
	    const isLoggedIn = $('body').data('user-logged-in');
	    if (!isLoggedIn) {
	        alert('로그인이 필요합니다.');
	        location.href = '/user/login';
	        return;
	    }
	    
	    // 버튼 찾기 및 비활성화
	    const $btn = $(`.cart-btn-small[data-prod-no="${prodNo}"], .cart-btn-list[data-prod-no="${prodNo}"]`);
	    const originalText = $btn.text();
	    $btn.prop('disabled', true).text('⏳');
	    
	    // AJAX 요청
	    $.ajax({
	        url: '/mypage/json/addToCart',
	        type: 'POST',
	        contentType: 'application/json',
	        data: JSON.stringify({
	            prodNo: prodNo,
	            quantity: 1
	        }),
	        success: function(response) {
	            if (response.success) {
	                // 성공 메시지
	                $btn.text('✓');
	                
	                // ✅ 헤더 장바구니 개수 업데이트
	                if (typeof CartBadge !== 'undefined') {
	                    CartBadge.updateCount();
	                }
	                
	                // 확인 메시지
	                setTimeout(function() {
	                    if (confirm(response.message + '\n장바구니로 이동하시겠습니까?')) {
	                        location.href = '/mypage#cart';
	                    } else {
	                        $btn.prop('disabled', false).text(originalText);
	                    }
	                }, 300);
	            } else {
	                alert(response.message);
	                $btn.prop('disabled', false).text(originalText);
	            }
	        },
	        error: function(xhr, status, error) {
	            console.error('장바구니 추가 오류:', error);
	            alert('장바구니 추가에 실패했습니다.');
	            $btn.prop('disabled', false).text(originalText);
	        }
	    });
	},

	/**
	 * 상품 이미지 가져오기
	 */
	getProductImage: function(product) {
	    if (product.productFiles && product.productFiles.length > 0) {
	        for (let file of product.productFiles) {
	            if (file.fileType && file.fileType.startsWith('image/')) {
	                return '/uploads/' + file.savedName;
	            }
	        }
	    }
	    return '/images/default-image.png';
	},
	
	/**
	 * listProduct.js에 추가할 검색 자동완성 메서드들
	 */

	// ========================================
	// 검색 이벤트 바인딩 (수정)
	// ========================================
	bindSearchEvents: function() {
	    const self = this;
	    
	    // 검색 버튼 클릭
	    $('.search-btn').on('click', function() {
	        self.performSearch();
	    });
	    
	    // ✅ 검색창 키 입력 (자동완성)
	    $('.search-input').on('input', function() {
	        const keyword = $(this).val().trim();
	        
	        // 디바운싱
	        clearTimeout(self.searchTimeout);
	        
	        if (keyword.length >= 2) {
	            self.searchTimeout = setTimeout(function() {
	                self.fetchSuggestions(keyword);
	            }, 300); // 300ms 대기
	        } else {
	            self.hideSuggestions();
	        }
	    });
	    
	    // Enter 키로 검색
	    $('.search-input').on('keydown', function(e) {
	        if (e.key === 'Enter') {
	            self.performSearch();
	            self.hideSuggestions();
	        } else if (e.key === 'Escape') {
	            self.hideSuggestions();
	        } else if (e.key === 'ArrowDown') {
	            e.preventDefault();
	            self.navigateSuggestions('down');
	        } else if (e.key === 'ArrowUp') {
	            e.preventDefault();
	            self.navigateSuggestions('up');
	        }
	    });
	    
	    // 검색창 포커스 아웃 시 드롭다운 숨기기 (약간의 지연)
	    $('.search-input').on('blur', function() {
	        setTimeout(function() {
	            self.hideSuggestions();
	        }, 200);
	    });
	},

	/**
	 * ✅ 자동완성 제안 가져오기
	 */
	fetchSuggestions: function(keyword) {
	    console.log('자동완성 검색:', keyword);
	    
	    const self = this;
	    const searchCondition = $('.search-select').val();
	    
	    $.ajax({
	        url: '/product/json/searchSuggestions',
	        type: 'GET',
	        data: {
	            searchCondition: searchCondition,
	            searchKeyword: keyword
	        },
	        success: function(suggestions) {
	            console.log('자동완성 결과:', suggestions);
	            self.displaySuggestions(suggestions);
	        },
	        error: function(xhr, status, error) {
	            console.error('자동완성 오류:', error);
	            self.hideSuggestions();
	        }
	    });
	},

	/**
	 * ✅ 자동완성 드롭다운 표시
	 */
	displaySuggestions: function(suggestions) {
	    const $dropdown = $('.autocomplete-dropdown');
	    
	    // 드롭다운이 없으면 생성
	    if ($dropdown.length === 0) {
	        $('.search-box').append('<div class="autocomplete-dropdown"></div>');
	    }
	    
	    const $newDropdown = $('.autocomplete-dropdown');
	    $newDropdown.empty();
	    
	    if (!suggestions || suggestions.length === 0) {
	        $newDropdown.hide();
	        return;
	    }
	    
	    // 제안 목록 생성
	    suggestions.forEach(function(item, index) {
	        const $item = $('<div class="autocomplete-item"></div>')
	            .attr('data-index', index)
	            .html(self.highlightKeyword(item.prodName, $('.search-input').val()))
	            .on('click', function() {
	                $('.search-input').val(item.prodName);
	                self.performSearch();
	                self.hideSuggestions();
	            });
	        
	        $newDropdown.append($item);
	    });
	    
	    $newDropdown.fadeIn(200);
	},

	/**
	 * ✅ 키워드 하이라이트
	 */
	highlightKeyword: function(text, keyword) {
	    if (!keyword) return text;
	    
	    const regex = new RegExp('(' + keyword + ')', 'gi');
	    return text.replace(regex, '<strong>$1</strong>');
	},

	/**
	 * ✅ 자동완성 숨기기
	 */
	hideSuggestions: function() {
	    $('.autocomplete-dropdown').fadeOut(200);
	},

	/**
	 * ✅ 키보드로 제안 목록 탐색
	 */
	navigateSuggestions: function(direction) {
	    const $items = $('.autocomplete-item');
	    const $current = $('.autocomplete-item.active');
	    
	    if ($items.length === 0) return;
	    
	    let index = $current.index();
	    
	    if (direction === 'down') {
	        index = (index + 1) % $items.length;
	    } else if (direction === 'up') {
	        index = (index - 1 + $items.length) % $items.length;
	    }
	    
	    $items.removeClass('active');
	    const $next = $items.eq(index);
	    $next.addClass('active');
	    
	    // 선택된 항목의 텍스트를 검색창에 설정
	    $('.search-input').val($next.text());
	}
};

// ========================================
// 페이지 로드 시 자동 실행
// ========================================
$(function() {
    ListProduct.init();
});