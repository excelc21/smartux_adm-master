package com.dmi.smartux.admin.ads.vo;

import java.util.Date;

/**
 * 광고 마스터 VO
 */
public class AdsMasterVO {
	private String mMasterID;	// 마스터 아이디
	private String mMasterName;	// 마스터명
	private int mLiveCount;		// 라이브 제한 숫자(기본 10개)
	private Date mRegDate;		// 등록 일시
	private Date mModDate;		// 수정 일시
	private String mRegID;		// 등록자 아이디
	private String mActID;		// 사용자 접근 아이디
	private String mActIP;		// 사용자 접근 아이피
	private String mActGB;		// 사용자 접근 내용(삽입/삭제/수정)
	private Date mActDate;		// 사용자 접근 날짜

	public String getMasterID() {
		return mMasterID;
	}

	public void setMasterID(String masterID) {
		mMasterID = masterID;
	}

	public String getMasterName() {
		return mMasterName;
	}

	public void setMasterName(String masterName) {
		mMasterName = masterName;
	}

	public int getLiveCount() {
		return mLiveCount;
	}

	public void setLiveCount(int liveCount) {
		mLiveCount = liveCount;
	}

	public Date getRegDate() {
		return mRegDate;
	}

	public void setRegDate(Date regDate) {
		mRegDate = regDate;
	}

	public Date getModDate() {
		return mModDate;
	}

	public void setModDate(Date modDate) {
		mModDate = modDate;
	}

	public String getRegID() {
		return mRegID;
	}

	public void setRegID(String regID) {
		mRegID = regID;
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

	public String getActGB() {
		return mActGB;
	}

	public void setActGB(String actGB) {
		mActGB = actGB;
	}

	public Date getActDate() {
		return mActDate;
	}

	public void setActDate(Date actDate) {
		mActDate = actDate;
	}
	
	public enum adsDefine {
		DEFAULT_ADSID("IPTV01");
		
		private String code;
		
		private adsDefine(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return code;
		}
	}
}
