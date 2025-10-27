/**
 * listUser.js
 * 사용자 목록 페이지 전용 JavaScript
 */

const ListUser = {
    // ========================================
    // 상태 관리
    // ========================================
    currentPage: 1,
    isLoading: false,
    hasMore: true,
    userCounter: 0,
    searchTimeout: null,
    
    // 검색 조건
    filters: {
        searchCondition: '0',  // 0: 회원ID, 1: 회원명, 2: 이메일
        searchKeyword: ''
    },

    /**
     * 초기화
     */
    init: function() {
        console.log('ListUser 초기화 시작');
        
        // 초기 데이터 로드
        this.loadInitialData();
        
        // 이벤트 바인딩
        this.bindSearchEvents();
        this.bindInfiniteScroll();
        this.bindLoadMoreButton();
        this.bindUserClickEvents();
        
        console.log('ListUser 초기화 완료');
    },

    /**
     * 초기 데이터 로드
     */
    loadInitialData: function() {
        let initialData;
        
        try {
            initialData = JSON.parse($("#initial-data").text().trim());
        } catch (e) {
            console.error('초기 데이터 파싱 실패:', e);
            this.hasMore = false;
            this.updateLoadMoreUI();
            return;
        }

        const users = initialData.list || [];
        const totalCount = initialData.totalCount || 0;

        if (users.length > 0) {
            this.renderUsers(users);
            
            if (this.userCounter >= totalCount) {
                this.hasMore = false;
            }
        } else {
            this.hasMore = false;
        }
        
        // 초기 UI 상태 설정
        this.updateLoadMoreUI();
        
        console.log('초기 상태:', {
            currentPage: this.currentPage,
            hasMore: this.hasMore,
            totalCount: totalCount,
            userCounter: this.userCounter
        });
    },

    /**
     * 검색 이벤트 바인딩
     */
    bindSearchEvents: function() {
        const self = this;
        
        // 검색 버튼 클릭
        $(".search-btn").on('click', function() {
            self.performSearch();
        });
        
        // 검색 조건 변경
        $("select[name='searchCondition']").on('change', function() {
            self.hideAutocomplete();
            self.updatePlaceholder();
        });
        
        // 검색어 입력 (자동완성)
        $("input[name='searchKeyword']").on('keyup', function(e) {
            // 방향키와 Enter는 무시
            if ([38, 40, 13, 27].indexOf(e.keyCode) !== -1) return;
            
            const keyword = $(this).val().trim();
            const condition = $("select[name='searchCondition']").val();
            
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
        
        // 키보드 네비게이션
        $("input[name='searchKeyword']").on('keydown', function(e) {
            const $dropdown = $('#searchSuggestions');
            const $items = $dropdown.find('.suggestion-item');
            const $active = $items.filter('.active');
            let $next = null;
            
            // Enter 키
            if (e.keyCode === 13) {
                e.preventDefault();
                
                if ($dropdown.is(':visible') && $active.length > 0) {
                    const selectedValue = $active.data('value');
                    $(this).val(selectedValue);
                    self.hideAutocomplete();
                }
                
                self.performSearch();
                return;
            }
            
            if (!$dropdown.is(':visible') || $items.length === 0) return;
            
            // 아래 화살표
            if (e.keyCode === 40) {
                e.preventDefault();
                $next = $active.length === 0 ? $items.first() : $active.next('.suggestion-item');
                if ($next.length === 0) $next = $items.first();
                $items.removeClass('active');
                $next.addClass('active');
            }
            // 위 화살표
            else if (e.keyCode === 38) {
                e.preventDefault();
                $next = $active.length === 0 ? $items.last() : $active.prev('.suggestion-item');
                if ($next.length === 0) $next = $items.last();
                $items.removeClass('active');
                $next.addClass('active');
            }
            // ESC
            else if (e.keyCode === 27) {
                self.hideAutocomplete();
            }
        });
        
        // 자동완성 항목 클릭
        $(document).on('click', '.suggestion-item', function() {
            const value = $(this).data('value');
            $("input[name='searchKeyword']").val(value);
            self.hideAutocomplete();
            self.performSearch();
        });
        
        // 외부 클릭 시 자동완성 닫기
        $(document).on('click', function(e) {
            if (!$(e.target).closest("input[name='searchKeyword'], #searchSuggestions").length) {
                self.hideAutocomplete();
            }
        });
        
        // 초기 placeholder 설정
        this.updatePlaceholder();
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
     * 사용자 클릭 이벤트
     */
	bindUserClickEvents: function() {
	    const self = this;
	    
	    $(document).on('click', '.user-id-cell', function() {
	        const userId = $(this).data('user-id');
	        const userNo = $(this).data('user-no');
	        
	        console.log('사용자 클릭:', { userId, userNo });
	        
	        // ✅ userId가 있으면 userId로, 없으면 userNo로 조회
	        if (userId && userId !== 'null' && userId !== '') {
	            location.href = '/user/getUser?userId=' + userId;
	        } else if (userNo) {
	            location.href = '/user/getUser?userNo=' + userNo;
	        } else {
	            console.error('userId와 userNo가 모두 없습니다.');
	            alert('사용자 정보를 불러올 수 없습니다.');
	        }
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
            $loadingSpinner.show();
            console.log('UI: 로딩 스피너 표시');
        } else if (!this.hasMore) {
            $endMessage.show();
            console.log('UI: 완료 메시지 표시');
        } else {
            $loadMoreBtn.show();
            console.log('UI: 더보기 버튼 표시');
        }
    },

    /**
     * 검색 실행
     */
    performSearch: function() {
        document.detailForm.action = '/user/listUser';
        document.detailForm.submit();
    },

    /**
     * 다음 페이지 로드
     */
    loadNextPage: function() {
        if (this.isLoading || !this.hasMore) {
            console.log('loadNextPage 중단:', {
                isLoading: this.isLoading,
                hasMore: this.hasMore
            });
            return;
        }

        this.isLoading = true;
        this.updateLoadMoreUI();
        
        const self = this;
        const nextPage = this.currentPage + 1;
        
        console.log(`페이지 ${nextPage} 데이터 요청 시작...`);

        $.ajax({
            url: '/user/json/getUserList',
            method: 'GET',
            data: {
                currentPage: nextPage,
                pageSize: 10,
                searchCondition: $("select[name='searchCondition']").val(),
                searchKeyword: $("input[name='searchKeyword']").val().trim()
            },
            success: function(response) {
                console.log('AJAX 요청 성공:', response);

                if (response.list && response.list.length > 0) {
                    self.renderUsers(response.list);
                    self.currentPage++;
                    
                    if (self.userCounter >= response.totalCount) {
                        console.log('모든 데이터를 로드했습니다.');
                        self.hasMore = false;
                    }
                } else {
                    console.log('서버로부터 더 이상 받을 데이터가 없습니다.');
                    self.hasMore = false;
                }
            },
            error: function(xhr, status, error) {
                console.error('AJAX 요청 실패:', {status, error});
                alert('데이터 로딩에 실패했습니다.');
                self.hasMore = false;
            },
            complete: function() {
                self.isLoading = false;
                self.updateLoadMoreUI();
            }
        });
    },

    /**
     * 사용자 목록 렌더링
     */
	renderUsers: function(users) {
	    console.log('renderUsers 호출. 추가할 사용자 수:', users.length);
	    
	    const $insertPoint = $("#user-list-body");
	    
	    users.forEach(user => {
	        this.userCounter++;
	        
	        // ✅ userId가 null이면 '소셜 로그인 사용자' 표시
	        const displayUserId = (user.userId && user.userId !== 'null') 
	            ? user.userId 
	            : '소셜 로그인 사용자';
	        
	        const html = `
	            <tr>
	                <td>${this.userCounter}</td>
	                <td class="user-id-cell" 
	                    data-user-id="${user.userId || ''}" 
	                    data-user-no="${user.userNo}">
	                    ${displayUserId}
	                </td>
	                <td>${user.userName || '-'}</td>
	                <td>${user.email || '-'}</td>
	            </tr>
	        `;
	        
	        $insertPoint.append(html);
	    });
	    
	    console.log('renderUsers 완료. 현재까지 총 사용자 수:', this.userCounter);
	},

    /**
     * 자동완성 가져오기
     */
    fetchAutocompleteSuggestions: function(condition, keyword) {
        const self = this;
        
        console.log('자동완성 요청:', condition, keyword);
        
        $.ajax({
            url: '/user/json/searchSuggestions',
            method: 'GET',
            data: {
                searchCondition: condition,
                searchKeyword: keyword
            },
            success: function(suggestions) {
                console.log('자동완성 결과:', suggestions.length + '개');
                self.displayAutocomplete(suggestions);
            },
            error: function(xhr, status, error) {
                console.error('자동완성 요청 실패:', error);
                self.hideAutocomplete();
            }
        });
    },

    /**
     * 자동완성 표시
     */
    displayAutocomplete: function(suggestions) {
        if (!suggestions || suggestions.length === 0) {
            this.hideAutocomplete();
            return;
        }
        
        let $suggestionsBox = $('#searchSuggestions');
        
        // 드롭다운이 없으면 생성
        if ($suggestionsBox.length === 0) {
            $suggestionsBox = $('<div id="searchSuggestions"></div>').css({
                'position': 'absolute',
                'max-height': '300px',
                'overflow-y': 'auto',
                'background': '#fff',
                'border': '1px solid #ccc',
                'border-top': 'none',
                'box-shadow': '0 4px 6px rgba(0,0,0,0.1)',
                'z-index': '1000',
                'display': 'none'
            });
            
            $('body').append($suggestionsBox);
        }
        
        // 위치 업데이트
        const $searchInput = $("input[name='searchKeyword']");
        const inputOffset = $searchInput.offset();
        
        $suggestionsBox.css({
            'top': (inputOffset.top + $searchInput.outerHeight()) + 'px',
            'left': inputOffset.left + 'px',
            'width': $searchInput.outerWidth() + 'px'
        });
        
        // 항목 렌더링
        $suggestionsBox.empty();
        
        suggestions.forEach(item => {
            const $item = $('<div class="suggestion-item"></div>')
                .text(item.displayText || item)
                .data('value', item.displayText || item)
                .css({
                    'padding': '10px',
                    'cursor': 'pointer',
                    'border-bottom': '1px solid #eee'
                })
                .on('mouseenter', function() {
                    $(this).css('background-color', '#f0f8ff');
                    $('.suggestion-item').removeClass('active');
                    $(this).addClass('active');
                })
                .on('mouseleave', function() {
                    $(this).css('background-color', '');
                });
            
            $suggestionsBox.append($item);
        });
        
        $suggestionsBox.show();
        
        console.log('자동완성 드롭다운 초기화 완료');
    },

    /**
     * 자동완성 숨기기
     */
    hideAutocomplete: function() {
        $('#searchSuggestions').hide();
    },

    /**
     * Placeholder 업데이트
     */
    updatePlaceholder: function() {
        const condition = $("select[name='searchCondition']").val();
        let placeholder = "";
        
        switch(condition) {
            case "0": 
                placeholder = "회원ID를 입력하세요 (2글자 이상)"; 
                break;
            case "1": 
                placeholder = "회원명을 입력하세요 (2글자 이상)"; 
                break;
            case "2": 
                placeholder = "이메일을 입력하세요 (2글자 이상)"; 
                break;
        }
        
        $("input[name='searchKeyword']").attr("placeholder", placeholder);
    }
};

// ========================================
// 페이지 로드 시 자동 실행
// ========================================
$(function() {
    ListUser.init();
});