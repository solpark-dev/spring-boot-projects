/**
 * getPurchase.js
 * 구매 상세 조회 페이지 전용 JavaScript
 */
$(function() {
    // data 속성에서 tranNo 가져오기
    const tranNo = $('.form-container').data('tran-no');

    if (!tranNo) {
        console.error("주문 번호를 찾을 수 없습니다.");
        return;
    }

    // '구매정보수정' 버튼 이벤트
    $('#updatePurchaseBtn').on('click', function() {
        location.href = "/purchase/updatePurchaseView?tranNo=" + tranNo;
    });

    // '배송시작' 버튼 이벤트
    $('#startDeliveryBtn').on('click', function() {
        if (confirm("배송을 시작하시겠습니까?")) {
            location.href = "/purchase/updateTranCodeByAdmin?tranNo=" + tranNo + "&tranCode=2";
        }
    });

    // '수령확인' 버튼 이벤트
    $('#confirmReceiptBtn').on('click', function() {
        if (confirm("상품을 받으셨습니까? 수령 확인 후에는 되돌릴 수 없습니다.")) {
            location.href = "/purchase/updateTranCodeByUser?tranNo=" + tranNo;
        }
    });
    
    // '목록으로' 버튼 이벤트
    $('#toListBtn').on('click', function() {
        location.href = "/purchase/listPurchase";
    });
});