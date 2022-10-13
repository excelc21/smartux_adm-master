package com.dmi.smartux.admin.commonMng.vo;

public class CategoryAlbumListVo {

	private String categoryId;
	private String type;
	private String searchType;
	private String searchVal;
	
	
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getSearchVal() {
		return searchVal;
	}
	public void setSearchVal(String searchVal) {
		this.searchVal = searchVal;
	}
	@Override
	public String toString() {
		return "CategoryAlbumListVo [categoryId=" + categoryId + ", type=" + type + ", searchType=" + searchType
				+ ", searchVal=" + searchVal + "]";
	}

}
