/**
 * getUser.js
 * 회원 상세 조회 페이지 전용 JavaScript
 */
$(function() {

    // '수정' 버튼 클릭 이벤트
    $('#updateBtn').on('click', function() {
        // URL에서 userId 파라미터를 가져옴
        const urlParams = new URLSearchParams(window.location.search);
        const userId = urlParams.get('userId');
        
        if (userId) {
            self.location = "/user/updateUser?userId=" + userId;
        } else {
            alert("사용자 ID를 찾을 수 없어 수정 페이지로 이동할 수 없습니다.");
        }
    });

    // '목록으로' 버튼 클릭 이벤트
    $('#backBtn').on('click', function() {
        // 이전 페이지로 이동
        history.go(-1);
    });

});