package com.model2.mvc.service.domain;

import java.sql.Date;

public class ProductFile {
    private int fileId;           // file_id
    private int prodNo;           // prod_no  
    private String originalName;  // original_name
    private String savedName;     // saved_name
    private Long fileSize;        // file_size
    private String fileType;      // file_type
    private Date uploadDate;      // upload_date
	public int getFileId() {
		return fileId;
	}
	public void setFileId(int fileId) {
		this.fileId = fileId;
	}
	public int getProdNo() {
		return prodNo;
	}
	public void setProdNo(int prodNo) {
		this.prodNo = prodNo;
	}
	public String getOriginalName() {
		return originalName;
	}
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	public String getSavedName() {
		return savedName;
	}
	public void setSavedName(String savedName) {
		this.savedName = savedName;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	@Override
	public String toString() {
		return "ProductFile [fileId=" + fileId + ", prodNo=" + prodNo + ", originalName=" + originalName
				+ ", savedName=" + savedName + ", fileSize=" + fileSize + ", fileType=" + fileType + ", uploadDate="
				+ uploadDate + "]";
	}
    
    
}