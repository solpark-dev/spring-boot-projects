/**
 * listPurchase.js
 * 구매 목록 페이지 전용 JavaScript
 */
$(function() {
    let currentPage = 1;
    let isLoading = false;
    let hasMore = true;

    // 초기 데이터 로드
    loadPurchases(currentPage);

    // 더보기 버튼 클릭 이벤트
    $('#load-more-btn').on('click', function() {
        if (!isLoading && hasMore) {
            loadPurchases(++currentPage);
        }
    });

    function loadPurchases(page) {
        isLoading = true;
        updateUI('loading');

        $.ajax({
            url: '/purchase/json/listPurchase', // JSON 데이터를 반환하는 API로 변경 필요
            type: 'GET',
            data: { currentPage: page },
            success: function(response) {
                if (response.success && response.list.length > 0) {
                    renderPurchases(response.list);
                    hasMore = response.list.length === response.pageSize; // 더 불러올 데이터가 있는지 확인
                } else {
                    hasMore = false;
                    if(page === 1) { // 첫 페이지인데 데이터가 없을 경우
                        $('#purchase-list').html('<div class="empty-state"><div class="empty-icon">📦</div><div class="empty-title">주문 내역이 없습니다.</div></div>');
                    }
                }
            },
            error: function() {
                alert('구매 내역을 불러오는 데 실패했습니다.');
                hasMore = false;
            },
            complete: function() {
                isLoading = false;
                updateUI(hasMore ? 'hasMore' : 'end');
            }
        });
    }

    function renderPurchases(list) {
        let html = '';
        list.forEach(purchase => {
            const tranCode = purchase.tranCode.trim();
            let statusText = '상태미확인';
            let statusClass = 'status-UNKNOWN';

            if (tranCode === '1') {
                statusText = '결제 완료';
                statusClass = 'status-PAYMENT_COMPLETE';
            } else if (tranCode === '2') {
                statusText = '배송중';
                statusClass = 'status-SHIPPED';
            } else if (tranCode === '3') {
                statusText = '배송 완료';
                statusClass = 'status-DELIVERED';
            }

            html += `
                <div class="order-item" onclick="location.href='/purchase/getPurchase?tranNo=${purchase.tranNo}'">
                    <div class="order-header">
                        <span class="order-number">주문번호: ${purchase.tranNo}</span>
                        <span class="order-date">${new Date(purchase.orderDate).toLocaleDateString()}</span>
                    </div>
                    <div class="order-body">
                         <div class="order-product">
                            <h4>${purchase.purchaseProd.prodName}</h4>
                            <p>${CommonUtils.formatPrice(purchase.purchaseProd.price)}원</p>
                        </div>
                        <div class="order-status ${statusClass}">${statusText}</div>
                    </div>
                </div>`;
        });
        $('#purchase-list').append(html);
    }
    
    function updateUI(status) {
        $('#loading-spinner').hide();
        $('#load-more-btn').hide();
        $('#end-message').hide();

        if (status === 'loading') {
            $('#loading-spinner').show();
        } else if (status === 'hasMore') {
            $('#load-more-btn').show();
        } else if (status === 'end') {
            if ($('.order-item').length > 0) {
                 $('#end-message').show();
            }
        }
    }
});