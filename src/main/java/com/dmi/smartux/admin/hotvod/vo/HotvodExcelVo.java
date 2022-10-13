/**
 * 
 */
package com.dmi.smartux.admin.hotvod.vo;

/**
 *	
 * @Author : yhkim
 * @Date   : 2017. 8. 31.
 */
public class HotvodExcelVo {
	
	String hvCategoryId;
	String categoryOrder;
	String contentsOrder;
	int sort;
	String contentsId;
	String contentsType;
	String fullCategoryName;
	String contentsName;
	String albumId;
	String viewTime;
	String contentsUrl;
	String delYn;
	
	//2019.11.22 브릿지홈 개편 : 카테고리 ID추가
	String vodCategoryId;

	public String getVodCategoryId() {
		return vodCategoryId;
	}
	public void setVodCategoryId(String vodCategoryId) {
		this.vodCategoryId = vodCategoryId;
	}
	public String getHvCategoryId() {
		return hvCategoryId;
	}
	public void setHvCategoryId(String hvCategoryId) {
		this.hvCategoryId = hvCategoryId;
	}
	public String getCategoryOrder() {
		return categoryOrder;
	}
	public void setCategoryOrder(String categoryOrder) {
		this.categoryOrder = categoryOrder;
	}
	public String getContentsOrder() {
		return contentsOrder;
	}
	public void setContentsOrder(String contentsOrder) {
		this.contentsOrder = contentsOrder;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getContentsId() {
		return contentsId;
	}
	public void setContentsId(String contentsId) {
		this.contentsId = contentsId;
	}
	public String getContentsType() {
		return contentsType;
	}
	public void setContentsType(String contentsType) {
		this.contentsType = contentsType;
	}
	public String getFullCategoryName() {
		return fullCategoryName;
	}
	public void setFullCategoryName(String fullCategoryName) {
		this.fullCategoryName = fullCategoryName;
	}
	public String getContentsName() {
		return contentsName;
	}
	public void setContentsName(String contentsName) {
		this.contentsName = contentsName;
	}
	public String getAlbumId() {
		return albumId;
	}
	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}
	public String getViewTime() {
		return viewTime;
	}
	public void setViewTime(String viewTime) {
		this.viewTime = viewTime;
	}
	public String getContentsUrl() {
		return contentsUrl;
	}
	public void setContentsUrl(String contentsUrl) {
		this.contentsUrl = contentsUrl;
	}
	public String getDelYn() {
		return delYn;
	}
	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}
	
	
	@Override
	public String toString() {
		return "HotvodExcelVo [hvCategoryId=" + hvCategoryId + ", categoryOrder=" + categoryOrder + ", contentsOrder="
				+ contentsOrder + ", sort=" + sort + ", contentsId=" + contentsId + ", contentsType=" + contentsType
				+ ", fullCategoryName=" + fullCategoryName + ", contentsName=" + contentsName + ", albumId=" + albumId + ", viewTime=" + viewTime
				+ ", contentsUrl=" + contentsUrl + ", delYn=" + delYn +", vodCategoryId=" + vodCategoryId + "]";
	}
	
}
