package com.dmi.smartux.admin.secondtv_push.vo;

/**
 * 최신회 컨텐츠 VO
 *
 * @author dongho
 */
public class LatestContentVO {
	private String mContentID;
	private String mContentName;
	private String mCategoryID;
	private String mSeriesNumber;
	private String mCategorySeriesNumber;
	private String mStartDate;
	private String mEndDate;
	private int mStartNumber;
	private int mEndNumber;
	private String category_gb;

	public String getContentID() {
		return mContentID;
	}

	public void setContentID(String contentID) {
		mContentID = contentID;
	}

	public String getContentName() {
		return mContentName;
	}

	public void setContentName(String contentName) {
		mContentName = contentName;
	}

	public String getCategoryID() {
		return mCategoryID;
	}

	public void setCategoryID(String categoryID) {
		mCategoryID = categoryID;
	}

	public String getSeriesNumber() {
		return mSeriesNumber;
	}

	public void setSeriesNumber(String seriesNumber) {
		mSeriesNumber = seriesNumber;
	}

	public String getCategorySeriesNumber() {
		return mCategorySeriesNumber;
	}

	public void setCategorySeriesNumber(String categorySeriesNumber) {
		mCategorySeriesNumber = categorySeriesNumber;
	}

	public String getStartDate() {
		return mStartDate;
	}

	public void setStartDate(String startDate) {
		mStartDate = startDate;
	}

	public String getEndDate() {
		return mEndDate;
	}

	public void setEndDate(String endDate) {
		mEndDate = endDate;
	}

	public int getStartNumber() {
		return mStartNumber;
	}

	public void setStartNumber(int startNumber) {
		mStartNumber = startNumber;
	}

	public int getEndNumber() {
		return mEndNumber;
	}

	public void setEndNumber(int endNumber) {
		mEndNumber = endNumber;
	}

	public String getCategory_gb() {
		return category_gb;
	}

	public void setCategory_gb(String category_gb) {
		this.category_gb = category_gb;
	}
}
