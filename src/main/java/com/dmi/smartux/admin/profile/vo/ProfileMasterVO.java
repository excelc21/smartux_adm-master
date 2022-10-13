package com.dmi.smartux.admin.profile.vo;

import java.util.Date;

/**
 * 광고 마스터 VO
 */
public class ProfileMasterVO {
	private String profileMstId;	// 마스터 아이디
	private String profileMstName;	// 마스터명
	private String serviceType;	// 서비스타입
	private String regDate;		// 등록일
	private String regID;		// 등록자 아이디
	private String actID;		// 사용자 접근 아이디
	private String actIP;		// 사용자 접근 아이피
	private String actGB;		// 사용자 접근 내용(삽입/삭제/수정)
	private Date actDate;		// 사용자 접근 날짜
	
	public String getProfileMstId() {
		return profileMstId;
	}
	
	public void setProfileMstId(String profileMstId) {
		this.profileMstId = profileMstId;
	}
	
	public String getProfileMstName() {
		return profileMstName;
	}
	
	public void setProfileMstName(String profileMstName) {
		this.profileMstName = profileMstName;
	}
	
	public String getRegDate() {
		return regDate;
	}
	
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getRegID() {
		return regID;
	}

	public void setRegID(String regID) {
		this.regID = regID;
	}

	public String getActID() {
		return actID;
	}

	public void setActID(String actID) {
		this.actID = actID;
	}

	public String getActIP() {
		return actIP;
	}

	public void setActIP(String actIP) {
		this.actIP = actIP;
	}

	public String getActGB() {
		return actGB;
	}

	public void setActGB(String actGB) {
		this.actGB = actGB;
	}

	public Date getActDate() {
		return actDate;
	}

	public void setActDate(Date actDate) {
		this.actDate = actDate;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	
}
