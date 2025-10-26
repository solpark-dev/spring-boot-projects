package com.model2.mvc.common;

//==>리스트화면을 모델링(추상화/캡슐화)한 Bean 
public class Search {
	
	///Field
	private int currentPage = 0;
	private String searchCondition = "";
	private String searchKeyword = "";
	private int pageSize = 0;
	
	// UserServiceImpl에서 페이징 계산 결과를 담기 위한 필드
	private int endRowNum = 0;
	private int startRowNum = 0;
	
	
	// 기존 필드들 아래에 추가
	private String priceRange;    // 가격대 필터
	private String status;         // 판매 상태 필터
	private String sortBy;         // 정렬 방식
	
	///Constructor
	public Search() {
	}
	
	///Method
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int paseSize) {
		this.pageSize = paseSize;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getSearchCondition() {
		return searchCondition;
	}
	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}
	
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	
	// Getter: 필드 값을 반환하도록 수정
	public int getEndRowNum() {
		return endRowNum;
	}
	
	// Setter: 외부에서 값을 설정할 수 있도록 추가
	public void setEndRowNum(int endRowNum) {
		this.endRowNum = endRowNum;
	}
	
	// Getter: 필드 값을 반환하도록 수정
	public int getStartRowNum() {
		return startRowNum;
	}

	// Setter: 외부에서 값을 설정할 수 있도록 추가
	public void setStartRowNum(int startRowNum) {
		this.startRowNum = startRowNum;
	}

	// getter/setter 추가 (기존 메서드들 아래)
	public String getPriceRange() {
	    return priceRange;
	}
	public void setPriceRange(String priceRange) {
	    this.priceRange = priceRange;
	}

	public String getStatus() {
	    return status;
	}
	public void setStatus(String status) {
	    this.status = status;
	}

	public String getSortBy() {
	    return sortBy;
	}
	public void setSortBy(String sortBy) {
	    this.sortBy = sortBy;
	}
	
	@Override
	public String toString() {
	    return "Search [currentPage=" + currentPage + ", searchCondition="
	            + searchCondition + ", searchKeyword=" + searchKeyword
	            + ", pageSize=" + pageSize + ", endRowNum=" + endRowNum
	            + ", startRowNum=" + startRowNum + ", priceRange=" + priceRange
	            + ", status=" + status + ", sortBy=" + sortBy + "]";
	}
}