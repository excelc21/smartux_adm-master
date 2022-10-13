package com.dmi.smartux.admin.ads.vo;

import java.util.Date;
import java.util.List;

public class AdsVO {
	private int mNumber;
	private String mTitle;
	private int mRolTime;
	private int mType;
	private String mMasterID;
	private String mFileName;
	private String mSaveFileName;
	private String mFileName2;
	private String mSaveFileName2;
	private String mLinkUrl;
	private Date mStartTime;
	private Date mExpiredTime;
	private String mGrade;
	private String mRegID;
	private Date mRegTime;
	private String mLiveType;
	private String mEventID;
	private String mEventName;
	
	private List<AdsVO> mList;          // 리스트 객체
	private int mPageSize;              // 페이징시 게시물의 노출 개수
	private int mBlockSize;             // 한 화면에 노출할 페이지 번호 개수
	private int mPageNum;               // 현재 페이지 번호
	private int mPageCount;             // 페이지 번호 전체 사이즈

	private String mFindName;           // 검색 구분
	private String mFindValue;          // 검색어
 
	private String mScr_tp; // 스크린 타입, 관리자 화면에서 분리 하기 위함, 기존 하드 코딩에서 VO 추가  
	private String mAdsListMode; // 리스트 모드
	
	private int mStartNum;              // DB(오라클) 페이징 시작 번호
	private int mEndNum;                // DB(오라클) 페이징 끝 번호

	private String mValidate;           // 요청/응답 결과 메세지


	private String mBgColor;
	private String mVerticalBgFileName;
	private String mVerticalBgSaveFileName;
	private String mHorizontalBgFileName;
	private String mHorizontalBgSaveFileName;
	private String mOrder;
	private String mDateType;

	private String mActID;
	private String mActIP;
	private String mActGbn;
	
	private String mUpdateYn;
	
	/*2020-06-04 수정*/
	private String textEtc;
	private String textEtc2;
	
	
	public String getAdsListMode() {
		return mAdsListMode;
	}

	public void setAdsListMode(String mAdsListMode) {
		this.mAdsListMode = mAdsListMode;
	}

	

	public String getEventName() {
		return mEventName;
	}

	public void setEventName(String mEventName) {
		this.mEventName = mEventName;
	}

	public String getScr_tp() {
		return mScr_tp;
	}

	public void setScr_tp(String scr_tp) {
		this.mScr_tp = scr_tp;
	}

	public String getMasterID() {
		return mMasterID;
	}

	public void setMasterID(String masterID) {
		mMasterID = masterID;
	}

	public String getFileName() {
		return mFileName;
	}

	public void setFileName(String fileName) {
		mFileName = fileName;
	}

	public String getSaveFileName() {
		return mSaveFileName;
	}

	public void setSaveFileName(String saveFileName) {
		mSaveFileName = saveFileName;
	}

	public String getFileName2() {
		return mFileName2;
	}

	public void setFileName2(String fileName2) {
		mFileName2 = fileName2;
	}

	public String getSaveFileName2() {
		return mSaveFileName2;
	}

	public void setSaveFileName2(String saveFileName2) {
		mSaveFileName2 = saveFileName2;
	}

	public int getNumber() {
		return mNumber;
	}

	public void setNumber(int number) {
		mNumber = number;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public int getRolTime() {
		return mRolTime;
	}

	public void setRolTime(int rolTime) {
		mRolTime = rolTime;
	}

	public int getType() {
		return mType;
	}

	public void setType(int type) {
		mType = type;
	}

	public String getLinkUrl() {
		return mLinkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		mLinkUrl = linkUrl;
	}

	public Date getStartTime() {
		return mStartTime;
	}

	public void setStartTime(Date startTime) {
		mStartTime = startTime;
	}

	public Date getExpiredTime() {
		return mExpiredTime;
	}

	public void setExpiredTime(Date expiredTime) {
		mExpiredTime = expiredTime;
	}

	public Date getRegTime() {
		return mRegTime;
	}

	public void setRegTime(Date regTime) {
		mRegTime = regTime;
	}

	public String getRegID() {
		return mRegID;
	}

	public void setRegID(String regID) {
		mRegID = regID;
	}

	public String getLiveType() {
		return mLiveType;
	}

	public void setLiveType(String liveType) {
		mLiveType = liveType;
	}

	public List<AdsVO> getList() {
		return mList;
	}

	public void setList(List<AdsVO> list) {
		mList = list;
	}

	public int getPageSize() {
		return mPageSize;
	}

	public void setPageSize(int pageSize) {
		mPageSize = pageSize;
	}

	public int getBlockSize() {
		return mBlockSize;
	}

	public void setBlockSize(int blockSize) {
		mBlockSize = blockSize;
	}

	public int getPageNum() {
		return mPageNum;
	}

	public void setPageNum(int pageNum) {
		mPageNum = pageNum;
	}

	public int getPageCount() {
		return mPageCount;
	}

	public void setPageCount(int pageCount) {
		mPageCount = pageCount;
	}

	public String getFindName() {
		return mFindName;
	}

	public void setFindName(String findName) {
		mFindName = findName;
	}

	public String getFindValue() {
		return mFindValue;
	}

	public void setFindValue(String findValue) {
		mFindValue = findValue;
	}

	public int getStartNum() {
		return mStartNum;
	}

	public void setStartNum(int startNum) {
		mStartNum = startNum;
	}

	public int getEndNum() {
		return mEndNum;
	}

	public void setEndNum(int endNum) {
		mEndNum = endNum;
	}

	public String getValidate() {
		return mValidate;
	}

	public void setValidate(String validate) {
		mValidate = validate;
	}

	public String getActID() {
		return mActID;
	}

	public void setActID(String actID) {
		mActID = actID;
	}

	public String getActIP() {
		return mActIP;
	}

	public void setActIP(String actIP) {
		mActIP = actIP;
	}

	public String getActGbn() {
		return mActGbn;
	}

	public void setActGbn(String actGbn) {
		mActGbn = actGbn;
	}

	public String getGrade() {
		return mGrade;
	}

	public void setGrade(String grade) {
		mGrade = grade;
	}

	public String getEventID() {
		return mEventID;
	}

	public void setEventID(String eventID) {
		mEventID = eventID;
	}

	public String getBgColor() {
		return mBgColor;
	}

	public void setBgColor(String bgColor) {
		mBgColor = bgColor;
	}

	public String getVerticalBgFileName() {
		return mVerticalBgFileName;
	}

	public void setVerticalBgFileName(String verticalBgFileName) {
		mVerticalBgFileName = verticalBgFileName;
	}

	public String getVerticalBgSaveFileName() {
		return mVerticalBgSaveFileName;
	}

	public void setVerticalBgSaveFileName(String verticalBgSaveFileName) {
		mVerticalBgSaveFileName = verticalBgSaveFileName;
	}

	public String getHorizontalBgFileName() {
		return mHorizontalBgFileName;
	}

	public void setHorizontalBgFileName(String horizontalBgFileName) {
		mHorizontalBgFileName = horizontalBgFileName;
	}

	public String getHorizontalBgSaveFileName() {
		return mHorizontalBgSaveFileName;
	}

	public void setHorizontalBgSaveFileName(String horizontalBgSaveFileName) {
		mHorizontalBgSaveFileName = horizontalBgSaveFileName;
	}

	public String getOrder() {
		return mOrder;
	}

	public void setOrder(String order) {
		mOrder = order;
	}

	public String getDateType() {
		return mDateType;
	}

	public void setDateType(String dateType) {
		mDateType = dateType;
	}

	public String getUpdateYn() {
		return mUpdateYn;
	}

	public void setUpdateYn(String updateYn) {
		this.mUpdateYn = updateYn;
	}
	
	public String getTextEtc() {
		return textEtc;
	}

	public void setTextEtc(String textEtc) {
		this.textEtc = textEtc;
	}

	public String getTextEtc2() {
		return textEtc2;
	}

	public void setTextEtc2(String textEtc2) {
		this.textEtc2 = textEtc2;
	}

	
}
