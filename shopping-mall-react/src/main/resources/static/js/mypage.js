/**
 * mypage.js
 * 마이페이지 전용 JavaScript - 장바구니 기능 완전 구현
 */

// ========================================
// MyPage 객체
// ========================================
const MyPage = {
    
    currentTab: 'profile',
    cartData: {
        items: [],
        totalCount: 0,
        totalPrice: 0
    },
    
    // 초기화
    init: function() {
        console.log('MyPage 초기화 시작');
        this.bindTabEvents();
        this.loadTabFromHash();
        this.bindProfileEvents();
        console.log('MyPage 초기화 완료');
    },
    
    // 탭 전환 이벤트 바인딩
    bindTabEvents: function() {
        $('.tab-btn').on('click', (e) => {
            const tab = $(e.currentTarget).data('tab');
            this.switchTab(tab);
        });
    },

    // URL 해시(#)를 기반으로 초기 탭 설정
    loadTabFromHash: function() {
        const hash = window.location.hash.substring(1);
        const validTabs = ['profile', 'social', 'wishlist', 'cart', 'orders', 'history'];
        if (hash && validTabs.includes(hash)) {
            this.switchTab(hash);
        } else {
            this.switchTab('profile');
        }
    },

    // 탭 전환 로직
    switchTab: function(tab) {
        console.log('탭 전환:', tab);
        this.currentTab = tab;
        
        $('.tab-btn').removeClass('active');
        $(`.tab-btn[data-tab="${tab}"]`).addClass('active');
        
        $('.tab-content').removeClass('active');
        $(`#${tab}-section`).addClass('active');
        
        window.location.hash = tab;
        
        this.loadTabData(tab);
    },

    // 탭에 맞는 데이터 로드
    loadTabData: function(tab) {
        switch(tab) {
            case 'profile': break;
            case 'social': this.loadSocialAccounts(); break;
            case 'wishlist': this.loadWishlist(); break;
            case 'cart': this.loadCart(); break;
            case 'orders': this.loadOrders(); break;
            case 'history': this.loadHistory(); break;
        }
    },

    // ========================================
    // 프로필 관련 함수
    // ========================================

    bindProfileEvents: function() {
        $('#updateProfileBtn').on('click', () => this.updateProfile());
        $('#changePasswordBtn').on('click', () => this.changePassword());
    },

    updateProfile: function() {
        console.log('프로필 수정 시작');
        
        const userName = $('#userNameInput').val().trim();
        const email = $('#emailInput').val().trim();
        
        // 유효성 검사
        if (!userName) {
            alert('이름을 입력해주세요.');
            $('#userNameInput').focus();
            return;
        }
        
        if (!email) {
            alert('이메일을 입력해주세요.');
            $('#emailInput').focus();
            return;
        }
        
        // 이메일 형식 검사
        const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
        if (!emailPattern.test(email)) {
            alert('올바른 이메일 형식이 아닙니다.');
            $('#emailInput').focus();
            return;
        }
        
        // Ajax 요청
        $.ajax({
            url: '/mypage/json/updateProfile',
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({
                userName: userName,
                email: email
            }),
            success: (response) => {
                if (response.success) {
                    alert(response.message);
                    // 세션 정보 갱신 (페이지 새로고침)
                    location.reload();
                } else {
                    alert(response.message);
                }
            },
            error: (xhr, status, error) => {
                console.error('프로필 수정 오류:', error);
                alert('프로필 수정에 실패했습니다.');
            }
        });
    },

    changePassword: function() {
        alert('비밀번호 변경 기능은 준비 중입니다.');
        // TODO: 비밀번호 변경 모달 구현
    },

    // ========================================
    // 소셜 계정 관련 함수
    // ========================================
    
    loadSocialAccounts: function() {
        console.log('소셜 계정 목록 로드');
        const self = this;
        const $container = $('.social-accounts');
        
        $container.html('<div class="loading-spinner"><div class="spinner"></div><p>소셜 계정을 불러오는 중...</p></div>');
        
        $.ajax({
            url: '/mypage/json/getSocialAccounts',
            type: 'GET',
            success: (response) => {
                if (response.success) {
                    self.renderSocialAccounts(response.connectionStatus, response.socialAccounts);
                } else {
                    alert(response.message);
                }
            },
            error: (xhr, status, error) => {
                console.error('소셜 계정 로드 오류:', error);
                $container.html('<div class="empty-message"><p>소셜 계정 정보를 불러오는 데 실패했습니다.</p></div>');
            }
        });
    },

    renderSocialAccounts: function(connectionStatus, socialAccounts) {
        console.log('소셜 계정 렌더링:', connectionStatus);
        
        const $container = $('.social-accounts');
        const kakaoLinkUrl = $container.data('kakao-link-url');
        const naverLinkUrl = $container.data('naver-link-url');
        const googleLinkUrl = $container.data('google-link-url');
        let html = '';
        
        const providers = ['KAKAO', 'NAVER', 'GOOGLE'];
        const providerInfo = {
            KAKAO: { name: '카카오', icon: '💬' },
            NAVER: { name: '네이버', icon: 'N' },
            GOOGLE: { name: '구글', icon: 'G' }
        };
        const linkUrls = {
            KAKAO: kakaoLinkUrl,
            NAVER: naverLinkUrl,
            GOOGLE: googleLinkUrl
        };
        
        providers.forEach(provider => {
            const account = socialAccounts.find(acc => acc.provider === provider);
            const isConnected = connectionStatus[provider];
            
            html += `
                <div class="social-card ${isConnected ? 'connected' : ''}">
                    <div class="social-info-wrapper">
                        <div class="social-icon ${provider.toLowerCase()}">${providerInfo[provider].icon}</div>
                        <div class="social-info">
                            <h4>${providerInfo[provider].name}</h4>
                            <p class="social-status">${isConnected ? '연결됨' : '연결 안 됨'}</p>
                        </div>
                    </div>
                    <div class="social-actions">
                        ${isConnected ? 
                            `<button class="btn btn-secondary btn-small btn-disconnect" data-provider="${provider}" data-id="${account.socialAccountId}">연결 해제</button>` :
                            `<a href="${linkUrls[provider]}" class="btn btn-primary btn-small btn-connect">연결하기</a>`
                        }
                    </div>
                </div>`;
        });
        
        $container.html(html);
        this.bindSocialEvents();
    },

    bindSocialEvents: function() {
        const self = this;
        $('.social-accounts').off('click', '.btn-disconnect').on('click', '.btn-disconnect', function() {
            const provider = $(this).data('provider');
            const socialAccountId = $(this).data('id');
            
            if (confirm(provider + ' 계정 연결을 해제하시겠습니까?')) {
                self.disconnectSocialAccount(socialAccountId);
            }
        });
    },
    
    disconnectSocialAccount: function(socialAccountId) {
        console.log('소셜 계정 연결 해제:', socialAccountId);
        
        const self = this;
        
        $.ajax({
            url: '/mypage/json/disconnectSocialAccount?socialAccountId=' + socialAccountId,
            type: 'DELETE',
            success: function(response) {
                if (response.success) {
                    alert(response.message);
                    self.loadSocialAccounts();
                } else {
                    alert(response.message);
                }
            },
            error: function(xhr, status, error) {
                console.error('연결 해제 오류:', error);
                alert('연결 해제에 실패했습니다.');
            }
        });
    },

    // ========================================
    // 위시리스트 관련 함수
    // ========================================

    loadWishlist: function() {
        console.log('위시리스트 로드');
        const $container = $('.wishlist-container');
        
        // TODO: 위시리스트 API 구현 후 연동
        $container.html(`
            <div class="empty-message">
                <i class="fas fa-heart"></i>
                <p>아직 찜한 상품이 없습니다.</p>
                <a href="/product/listProduct" class="btn btn-primary">상품 둘러보기</a>
            </div>
        `);
    },

    // ========================================
    // 장바구니 관련 함수 (완전 구현)
    // ========================================

    /**
     * 장바구니 목록 조회
     */
    loadCart: function() {
        console.log('장바구니 로드 시작');
        const self = this;
        const $container = $('.cart-container');
        
        // 로딩 표시
        $container.html('<div class="loading-spinner"><div class="spinner"></div><p>장바구니를 불러오는 중...</p></div>');
        
        $.ajax({
            url: '/mypage/json/getCart',
            type: 'GET',
            success: function(response) {
                console.log('장바구니 조회 성공:', response);
                
                if (response.success) {
                    self.cartData.items = response.cartList || [];
                    self.cartData.totalCount = response.totalCount || 0;
                    self.cartData.totalPrice = response.totalPrice || 0;
                    
                    self.renderCart();
                } else {
                    alert(response.message);
                    $container.html('<div class="empty-message"><p>' + response.message + '</p></div>');
                }
            },
            error: function(xhr, status, error) {
                console.error('장바구니 조회 오류:', error);
                $container.html('<div class="empty-message"><p>장바구니를 불러오는 데 실패했습니다.</p></div>');
            }
        });
    },

    /**
     * 장바구니 UI 렌더링
     */
	renderCart: function() {
	    console.log('장바구니 렌더링');
	    const $container = $('.cart-container');
	    
	    if (!this.cartData.items || this.cartData.items.length === 0) {
	        $container.html(`
	            <div class="empty-message">
	                <i class="fas fa-shopping-cart"></i>
	                <p>장바구니가 비어있습니다.</p>
	                <a href="/product/listProduct" class="btn btn-primary">쇼핑 계속하기</a>
	            </div>
	        `);
	        return;
	    }
	    
	    let html = `
	        <div class="cart-header">
	            <div>
	                <input type="checkbox" id="selectAll" class="cart-checkbox"> 
	                <label for="selectAll" style="margin-left:8px; cursor:pointer;">전체 선택</label>
	            </div>
	            <div>
	                <button id="deleteSelectedBtn" class="btn btn-secondary btn-small" style="margin-right:8px;">선택 삭제</button>
	                <button id="clearCartBtn" class="btn btn-danger btn-small">전체 비우기</button>
	            </div>
	        </div>
	        <div class="cart-items">
	    `;
	    
	    this.cartData.items.forEach((cart, index) => {
	        const product = cart.product;
	        const price = product.price || 0;
	        const quantity = cart.quantity || 1;
	        const totalPrice = price * quantity;
	        
	        // ✅ 이미지 처리 로직 개선
	        let imageHtml = '';
	        let hasImage = false;
	        
	        try {
	            if (product.productFiles && Array.isArray(product.productFiles) && product.productFiles.length > 0) {
	                const firstFile = product.productFiles[0];
	                if (firstFile && firstFile.savedName) {
	                    const imageUrl = '/uploads/' + firstFile.savedName;
	                    hasImage = true;
	                    
	                    // 이미지가 있으면 img 태그 사용
	                    imageHtml = `
	                        <img src="${imageUrl}" 
	                             alt="${product.prodName}" 
	                             class="cart-item-image"
	                             loading="lazy"
	                             onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
	                        <div class="cart-item-image-placeholder" style="display:none;">
	                            <i class="fas fa-image"></i>
	                        </div>
	                    `;
	                }
	            }
	        } catch (e) {
	            console.error('이미지 처리 오류:', e);
	        }
	        
	        // 이미지가 없으면 placeholder 사용
	        if (!hasImage) {
	            imageHtml = `
	                <div class="cart-item-image-placeholder">
	                    <i class="fas fa-shopping-bag"></i>
	                </div>
	            `;
	        }
	        
	        const isSold = product.saleStatus === 'SOLD';
	        
	        html += `
	            <div class="cart-item" data-cart-id="${cart.cartId}" data-prod-no="${product.prodNo}">
	                <input type="checkbox" class="cart-checkbox item-checkbox" 
	                       data-cart-id="${cart.cartId}" ${isSold ? 'disabled' : ''}>
	                
	                ${imageHtml}
	                
	                <div class="cart-item-info">
	                    <h4>
	                        ${product.prodName}
	                        ${isSold ? '<span class="sold-badge">판매완료</span>' : ''}
	                    </h4>
	                    <p>${product.prodDetail || '상세설명 없음'}</p>
	                </div>
	                
	                <div class="cart-item-quantity">
	                    <button class="qty-btn qty-decrease" data-cart-id="${cart.cartId}" ${isSold ? 'disabled' : ''}>-</button>
	                    <input type="number" class="qty-input" value="${quantity}" 
	                           data-cart-id="${cart.cartId}" min="1" max="99" ${isSold ? 'disabled' : ''}>
	                    <button class="qty-btn qty-increase" data-cart-id="${cart.cartId}" ${isSold ? 'disabled' : ''}>+</button>
	                </div>
	                
	                <div class="cart-item-price">
	                    <p class="price">${this.formatPrice(totalPrice)}원</p>
	                </div>
	                
	                <button class="btn-icon btn-delete" data-cart-id="${cart.cartId}" title="삭제">
	                    <i class="fas fa-trash"></i>
	                </button>
	            </div>
	        `;
	    });
	    
	    html += `
	        </div>
	        <div class="cart-summary">
	            <div class="summary-row">
	                <span>상품금액</span>
	                <span class="summary-value" id="subtotal">${this.formatPrice(this.cartData.totalPrice)}원</span>
	            </div>
	            <div class="summary-row">
	                <span>배송비</span>
	                <span class="summary-value">0원</span>
	            </div>
	            <div class="summary-row total">
	                <span>총 결제금액</span>
	                <span class="total-price" id="totalPrice">${this.formatPrice(this.cartData.totalPrice)}원</span>
	            </div>
	            <button id="checkoutBtn" class="btn btn-primary btn-large" style="margin-top:20px;">
	                선택 상품 구매하기
	            </button>
	        </div>
	    `;
	    
	    $container.html(html);
	    this.bindCartEvents();
	},

    /**
     * 장바구니 이벤트 바인딩
     */
    bindCartEvents: function() {
        const self = this;
        
        // 전체 선택
        $('#selectAll').on('change', function() {
            const checked = $(this).prop('checked');
            $('.item-checkbox:not(:disabled)').prop('checked', checked);
        });
        
        // 개별 체크박스
        $('.item-checkbox').on('change', function() {
            const allChecked = $('.item-checkbox:not(:disabled)').length === 
                              $('.item-checkbox:not(:disabled):checked').length;
            $('#selectAll').prop('checked', allChecked);
        });
        
        // 수량 감소
        $('.qty-decrease').on('click', function() {
            const cartId = $(this).data('cart-id');
            const $input = $(`.qty-input[data-cart-id="${cartId}"]`);
            const currentQty = parseInt($input.val());
            
            if (currentQty > 1) {
                self.updateCartQuantity(cartId, currentQty - 1);
            }
        });
        
        // 수량 증가
        $('.qty-increase').on('click', function() {
            const cartId = $(this).data('cart-id');
            const $input = $(`.qty-input[data-cart-id="${cartId}"]`);
            const currentQty = parseInt($input.val());
            
            if (currentQty < 99) {
                self.updateCartQuantity(cartId, currentQty + 1);
            }
        });
        
        // 수량 직접 입력
        $('.qty-input').on('change', function() {
            const cartId = $(this).data('cart-id');
            let quantity = parseInt($(this).val());
            
            if (isNaN(quantity) || quantity < 1) {
                quantity = 1;
            } else if (quantity > 99) {
                quantity = 99;
            }
            
            $(this).val(quantity);
            self.updateCartQuantity(cartId, quantity);
        });
        
        // 개별 삭제
        $('.btn-delete').on('click', function() {
            const cartId = $(this).data('cart-id');
            if (confirm('장바구니에서 삭제하시겠습니까?')) {
                self.removeFromCart(cartId);
            }
        });
        
        // 선택 삭제
        $('#deleteSelectedBtn').on('click', function() {
            self.removeSelectedItems();
        });
        
        // 전체 비우기
        $('#clearCartBtn').on('click', function() {
            if (confirm('장바구니를 모두 비우시겠습니까?')) {
                self.clearCart();
            }
        });
        
        // 구매하기
        $('#checkoutBtn').on('click', function() {
            self.proceedToCheckout();
        });
    },

    /**
     * 장바구니 수량 변경
     */
    updateCartQuantity: function(cartId, quantity) {
        console.log('수량 변경:', cartId, quantity);
        const self = this;
        
        $.ajax({
            url: '/mypage/json/updateCartQuantity',
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({
                cartId: cartId,
                quantity: quantity
            }),
            success: function(response) {
                if (response.success) {
                    console.log('수량 변경 성공');
                    self.loadCart(); // 장바구니 새로고침
                    
                    // 헤더 장바구니 배지 업데이트 (common.js의 CartBadge 사용)
                    if (window.CartBadge) {
                        CartBadge.updateCount();
                    }
                } else {
                    alert(response.message);
                }
            },
            error: function(xhr, status, error) {
                console.error('수량 변경 오류:', error);
                alert('수량 변경에 실패했습니다.');
            }
        });
    },

    /**
     * 장바구니 항목 삭제 (단일)
     */
    removeFromCart: function(cartId) {
        console.log('장바구니 항목 삭제:', cartId);
        const self = this;
        
        $.ajax({
            url: '/mypage/json/removeFromCart?cartId=' + cartId,
            type: 'DELETE',
            success: function(response) {
                if (response.success) {
                    console.log('삭제 성공');
                    self.loadCart(); // 장바구니 새로고침
                    
                    // 헤더 장바구니 배지 업데이트
                    if (window.CartBadge) {
                        CartBadge.updateCount();
                    }
                } else {
                    alert(response.message);
                }
            },
            error: function(xhr, status, error) {
                console.error('삭제 오류:', error);
                alert('삭제에 실패했습니다.');
            }
        });
    },

    /**
     * 선택 항목 삭제
     */
    removeSelectedItems: function() {
        console.log('선택 항목 삭제');
        
        const selectedCartIds = [];
        $('.item-checkbox:checked').each(function() {
            selectedCartIds.push($(this).data('cart-id'));
        });
        
        if (selectedCartIds.length === 0) {
            alert('삭제할 항목을 선택해주세요.');
            return;
        }
        
        if (!confirm(`선택한 ${selectedCartIds.length}개 항목을 삭제하시겠습니까?`)) {
            return;
        }
        
        const self = this;
        
        $.ajax({
            url: '/mypage/json/removeCartItems',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                cartIdList: selectedCartIds
            }),
            success: function(response) {
                if (response.success) {
                    console.log('선택 항목 삭제 성공');
                    self.loadCart(); // 장바구니 새로고침
                    
                    // 헤더 장바구니 배지 업데이트
                    if (window.CartBadge) {
                        CartBadge.updateCount();
                    }
                } else {
                    alert(response.message);
                }
            },
            error: function(xhr, status, error) {
                console.error('선택 삭제 오류:', error);
                alert('삭제에 실패했습니다.');
            }
        });
    },

    /**
     * 장바구니 전체 비우기
     */
    clearCart: function() {
        console.log('장바구니 전체 비우기');
        const self = this;
        
        $.ajax({
            url: '/mypage/json/clearCart',
            type: 'DELETE',
            success: function(response) {
                if (response.success) {
                    console.log('전체 비우기 성공');
                    self.loadCart(); // 장바구니 새로고침
                    
                    // 헤더 장바구니 배지 업데이트
                    if (window.CartBadge) {
                        CartBadge.updateCount();
                    }
                } else {
                    alert(response.message);
                }
            },
            error: function(xhr, status, error) {
                console.error('전체 비우기 오류:', error);
                alert('장바구니 비우기에 실패했습니다.');
            }
        });
    },

    /**
     * 구매하기 (선택 상품)
     */
    proceedToCheckout: function() {
        console.log('구매하기');
        
        const selectedProdNos = [];
        $('.item-checkbox:checked').each(function() {
            const prodNo = $(this).closest('.cart-item').data('prod-no');
            selectedProdNos.push(prodNo);
        });
        
        if (selectedProdNos.length === 0) {
            alert('구매할 상품을 선택해주세요.');
            return;
        }
        
        // TODO: 구매 페이지로 이동
        // 예: location.href = '/purchase/addPurchase?prodNo=' + selectedProdNos[0];
        alert(`${selectedProdNos.length}개 상품 구매 기능은 준비 중입니다.`);
    },

    // ========================================
    // 주문 내역 관련 함수
    // ========================================

    loadOrders: function(statusFilter = 'ALL') {
        console.log('주문 내역 로드:', statusFilter);
        const self = this;
        const $container = $('.orders-container');
        
        $container.html('<div class="loading-spinner"><div class="spinner"></div><p>주문 내역을 불러오는 중...</p></div>');
        
        $.ajax({
            url: '/mypage/json/getOrders',
            type: 'GET',
            data: {
                currentPage: 1,
                pageSize: 10,
                statusFilter: statusFilter
            },
            success: function(response) {
                console.log('주문 내역 조회 성공:', response);
                
                if (response.success) {
                    self.renderOrders(response.orderList);
                    self.bindOrderFilterEvents();
                } else {
                    alert(response.message);
                    $container.html('<div class="empty-message"><p>' + response.message + '</p></div>');
                }
            },
            error: function(xhr, status, error) {
                console.error('주문 내역 조회 오류:', error);
                $container.html('<div class="empty-message"><p>주문 내역을 불러오는 데 실패했습니다.</p></div>');
            }
        });
    },

    renderOrders: function(orderList) {
        const $container = $('.orders-container');
        
        if (!orderList || orderList.length === 0) {
            $container.html(`
                <div class="empty-message">
                    <i class="fas fa-box"></i>
                    <p>주문 내역이 없습니다.</p>
                    <a href="/product/listProduct" class="btn btn-primary">쇼핑하러 가기</a>
                </div>
            `);
            return;
        }
        
        let html = '';
        
        orderList.forEach(order => {
            const statusClass = 'status-' + order.tranCode;
            const statusText = this.getOrderStatusText(order.tranCode);
            const productName = order.purchaseProd ? order.purchaseProd.prodName : '상품명 없음';
            
            html += `
                <div class="order-item" data-order-no="${order.tranNo}">
                    <div class="order-header">
                        <div>
                            <span class="order-number">주문번호 ${order.tranNo}</span>
                            <span class="order-date" style="margin-left:16px;">${this.formatDate(order.orderDate)}</span>
                        </div>
                        <span class="order-status ${statusClass}">${statusText}</span>
                    </div>
                    <div class="order-body">
                        <div class="order-product">
                            <h4>${productName}</h4>
                            <p>${this.formatPrice(order.totalPrice)}원</p>
                        </div>
                    </div>
                </div>
            `;
        });
        
        $container.html(html);
    },

    bindOrderFilterEvents: function() {
        const self = this;
        
        $('.filter-btn').on('click', function() {
            $('.filter-btn').removeClass('active');
            $(this).addClass('active');
            
            const status = $(this).data('status');
            self.loadOrders(status);
        });
    },

    getOrderStatusText: function(tranCode) {
        const statusMap = {
            'PAYMENT_PENDING': '결제대기',
            'PAYMENT_COMPLETE': '결제완료',
            'PREPARING_SHIPMENT': '배송준비중',
            'SHIPPED': '배송중',
            'DELIVERED': '배송완료',
            'CANCELLED': '취소됨'
        };
        
        return statusMap[tranCode] || tranCode;
    },

    // ========================================
    // 최근 본 상품 (히스토리)
    // ========================================

    loadHistory: function() {
        console.log('최근 본 상품 로드');
        const $historyContainer = $('#history-section-content');
        
        $historyContainer.html('<div class="loading-spinner"><div class="spinner"></div><p>최근 본 상품을 불러오는 중...</p></div>');
        
        $historyContainer.load('/mypage/history', (response, status, xhr) => {
            if (status === "error") {
                $historyContainer.html('<div class="empty-message"><p>최근 본 상품을 불러오는 데 실패했습니다.</p></div>');
            }
        });
    },

    // ========================================
    // 유틸리티 함수
    // ========================================

    /**
     * 가격 포맷팅
     */
    formatPrice: function(price) {
        if (!price && price !== 0) return '0';
        return parseInt(price).toLocaleString('ko-KR');
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
    }
};

// ========================================
// 페이지 로드 시 자동 실행
// ========================================
$(function() {
    MyPage.init();

    // 로그아웃 버튼 이벤트
    $("#logoutBtn").on("click", function() {
        if(confirm("로그아웃 하시겠습니까?")) {
            location.href = "/user/logout";
        }
    });
});