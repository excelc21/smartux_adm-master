package com.dmi.smartux.admin.news.vo;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * 새소식 Value Object
 *
 * @author dongho
 */
public class NewsVO extends TargetVO {
	private int mRegNumber;				// 등록 번호
	private String mTitle;				// 제목
	private String mContent;			// 내용
	private String mNotiType;			// 타입
	private String mNotiDetail;			// 타입 선택 데이터
	private String mNotiDetailName;		// 타입 선택 데이터명
	private String mSendingStatus;		// 전송여부
	private Date mSendDate;				// 전송일시
	private Date mRegDate;				// 등록일시
	private String mNetType;			// 노출망(A:ALL, L : LTE, W:WIFI)
	private String mImageURL;			// 타이틀 이미지 URL
	private String mPushType;			// 푸쉬타입 (Anouncemence:A, 타겟팅:T, 직접입력:I, 엑셀:E)
	private String mShowType;			// SHOW 타입 (A:모두, I : 인디케이터, P : 팝업)
	private String mScreenType;			// 스크린타입 (H: HDTV, T : 세컨드TV)
	private String mNotiGB;				// 노티구분(NEW,N1)
	private String mResultCode;			// 연동결과 코드
	private List mSaIDList;				// 타겟팅 리스트
	private MultipartFile mFile;		// 타겟팅(CSV) 파일
	private MultipartFile mImageFile;	// 제목 이미지 파일
	private String mImageName;			// 이미지 파일명

	private List mList;					// 리스트

	private int mPageSize;				// 페이징시 게시물의 노출 개수
	private int mBlockSize;				// 한 화면에 노출할 페이지 번호 개수
	private int mPageNumber;			// 현재 페이지 번호
	private int mPageCount;				// 페이지 번호 전체 사이즈

	private String mFindName;           // 검색 구분
	private String mFindValue;          // 검색어

	private int mStartNumber;			// DB(오라클) 페이징 시작 번호
	private int mEndNumber;				// DB(오라클) 페이징 끝 번호

	private String mActID;				// 사용자 접근 아이디
	private String mActIP;				// 사용자 접근 아이피
	private String mActGbn;				// 사용자 접근 내용(삽입/삭제/수정)

	public int getRegNumber() {
		return mRegNumber;
	}

	public void setRegNumber(int regNumber) {
		mRegNumber = regNumber;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getContent() {
		return mContent;
	}

	public void setContent(String content) {
		mContent = content;
	}

	public String getNotiType() {
		return mNotiType;
	}

	public void setNotiType(String notiType) {
		mNotiType = notiType;
	}

	public String getNotiDetail() {
		return mNotiDetail;
	}

	public void setNotiDetail(String notiDetail) {
		mNotiDetail = notiDetail;
	}

	public String getNotiDetailName() {
		return mNotiDetailName;
	}

	public void setNotiDetailName(String notiDetailName) {
		mNotiDetailName = notiDetailName;
	}

	public String getSendingStatus() {
		return mSendingStatus;
	}

	public void setSendingStatus(String sendingStatus) {
		mSendingStatus = sendingStatus;
	}

	public Date getSendDate() {
		return mSendDate;
	}

	public void setSendDate(Date sendDate) {
		mSendDate = sendDate;
	}

	public Date getRegDate() {
		return mRegDate;
	}

	public void setRegDate(Date regDate) {
		mRegDate = regDate;
	}

	public String getNetType() {
		return mNetType;
	}

	public void setNetType(String netType) {
		mNetType = netType;
	}

	public String getImageURL() {
		return mImageURL;
	}

	public void setImageURL(String imageURL) {
		mImageURL = imageURL;
	}

	public String getPushType() {
		return mPushType;
	}

	public void setPushType(String pushType) {
		mPushType = pushType;
	}

	public String getShowType() {
		return mShowType;
	}

	public void setShowType(String showType) {
		mShowType = showType;
	}

	public String getScreenType() {
		return mScreenType;
	}

	public void setScreenType(String screenType) {
		mScreenType = screenType;
	}

	public String getNotiGB() {
		return mNotiGB;
	}

	public void setNotiGB(String notiGB) {
		mNotiGB = notiGB;
	}

	public List getSaIDList() {
		return mSaIDList;
	}

	public void setSaIDList(List saIDList) {
		mSaIDList = saIDList;
	}

	public MultipartFile getFile() {
		return mFile;
	}

	public void setFile(MultipartFile file) {
		mFile = file;
	}

	public MultipartFile getImageFile() {
		return mImageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		mImageFile = imageFile;
	}

	public String getImageName() {
		return mImageName;
	}

	public void setImageName(String imageName) {
		mImageName = imageName;
	}

	public List getList() {
		return mList;
	}

	public void setList(List list) {
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

	public int getPageNumber() {
		return mPageNumber;
	}

	public void setPageNumber(int pageNumber) {
		mPageNumber = pageNumber;
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

	public String getResultCode() {
		return mResultCode;
	}

	public void setResultCode(String resultCode) {
		this.mResultCode = resultCode;
	}
}
