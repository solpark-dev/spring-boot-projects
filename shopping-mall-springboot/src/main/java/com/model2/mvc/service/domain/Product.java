package com.model2.mvc.service.domain;

import java.sql.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * Product 도메인 객체
 * 상품 정보를 담는 클래스
 */
public class Product {
	
	private String fileName;
	private String manuDate;
	private int price;
	private String prodDetail;
	private String prodName;
	private int prodNo;
	private Date regDate;
	private String saleStatus;  // 판매 상태 ('AVAILABLE', 'SOLD')
	
	// ✅ 추가: 재고 필드
	private int stock;  // 재고 수량
	
	// 업로드된 파일을 임시로 담을 필드
	private MultipartFile uploadFile;
	
	// ✅ 상품과 연관된 파일 목록 추가
	private List<ProductFile> productFiles;
	
	// ✅ 추가: 카테고리 정보
	private int categoryId;  // 카테고리 ID (FK)
	private Category category;  // 카테고리 객체 (조인용)
	
	public Product(){
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getManuDate() {
		return manuDate;
	}
	
	public void setManuDate(String manuDate) {
		this.manuDate = manuDate;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	public String getProdDetail() {
		return prodDetail;
	}
	
	public void setProdDetail(String prodDetail) {
		this.prodDetail = prodDetail;
	}
	
	public String getProdName() {
		return prodName;
	}
	
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	
	public int getProdNo() {
		return prodNo;
	}
	
	public void setProdNo(int prodNo) {
		this.prodNo = prodNo;
	}
	
	public Date getRegDate() {
		return regDate;
	}
	
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	
	public String getSaleStatus() {
	    return saleStatus;
	}

	public void setSaleStatus(String saleStatus) {
	    this.saleStatus = saleStatus;
	}
	
	// ✅ 추가: stock getter/setter
	public int getStock() {
		return stock;
	}
	
	public void setStock(int stock) {
		this.stock = stock;
	}
	
	public MultipartFile getUploadFile() {
        return uploadFile;
    }
    
    public void setUploadFile(MultipartFile uploadFile) {
        this.uploadFile = uploadFile;
    }
    
    // ✅ 파일 목록 getter/setter
    public List<ProductFile> getProductFiles() {
        return productFiles;
    }
    
    public void setProductFiles(List<ProductFile> productFiles) {
        this.productFiles = productFiles;
    }
    
    // ✅ 추가: 카테고리 getter/setter
    public int getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}

	// ========================================
	// 재고 관련 비즈니스 메소드
	// ========================================
	
	/**
	 * 재고 상태 확인
	 * @return true: 품절, false: 판매 가능
	 */
	public boolean isSoldOut() {
	    return this.stock <= 0;
	}

	/**
	 * 재고 부족 확인
	 * @param requestQuantity 요청 수량
	 * @return true: 재고 부족, false: 주문 가능
	 */
	public boolean isStockInsufficient(int requestQuantity) {
	    return this.stock < requestQuantity;
	}

	/**
	 * 재고 충분 여부 확인
	 * @param requestQuantity 요청 수량
	 * @return true: 주문 가능, false: 재고 부족
	 */
	public boolean hasEnoughStock(int requestQuantity) {
	    return this.stock >= requestQuantity;
	}

	@Override
	public String toString() {
		return "Product [fileName=" + fileName + ", manuDate=" + manuDate + ", price=" + price + ", prodDetail="
				+ prodDetail + ", prodName=" + prodName + ", prodNo=" + prodNo + ", regDate=" + regDate
				+ ", saleStatus=" + saleStatus + ", stock=" + stock + ", categoryId=" + categoryId 
				+ ", uploadFile=" + uploadFile + ", productFiles=" + 
				(productFiles != null ? productFiles.size() + " files" : "no files") + "]";
	}	
}