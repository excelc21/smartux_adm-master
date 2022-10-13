package com.dmi.smartux.admin.profile.vo;

import java.util.Date;
import java.util.List;


public class ProfileVO {
	
	
	private String profileMstId;	// 마스터 아이디
	private String profileMstName;	// 마스터명
	private String serviceType;	// 서비스타입
	private String regDate;		// 등록일
	private String profileImgId  ; 
	private String profileImgName; 
	private String profileImgUrl ; 
	private String modDate        ; 
	private int ord;
	private int orders;
	private String seqs;
	private String orgImgUrl;
	
	//private int number;
	private int pageSize;              // 페이징시 게시물의 노출 개수
	private int blockSize;             // 한 화면에 노출할 페이지 번호 개수
	private int pageNum;               // 현재 페이지 번호
	private int pageCount;             // 페이지 번호 전체 사이즈
	
	private List<ProfileVO> list;          // 리스트 객체
	
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

	public String getProfileImgId() {
		return profileImgId;
	}

	public void setProfileImgId(String profileImgId) {
		this.profileImgId = profileImgId;
	}

	public String getProfileImgName() {
		return profileImgName;
	}

	public void setProfileImgName(String profileImgName) {
		this.profileImgName = profileImgName;
	}

	public String getProfileImgUrl() {
		return profileImgUrl;
	}

	public void setProfileImgUrl(String profileImgUrl) {
		this.profileImgUrl = profileImgUrl;
	}

	public String getModDate() {
		return modDate;
	}

	public void setModDate(String modDate) {
		this.modDate = modDate;
	}

	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public List<ProfileVO> getList() {
		return list;
	}

	public void setList(List<ProfileVO> list) {
		this.list = list;
	}

	public int getOrd() {
		return ord;
	}

	public void setOrd(int ord) {
		this.ord = ord;
	}
	
	
	public String getSeqs() {
		return seqs;
	}

	public void setSeqs(String seqs) {
		this.seqs = seqs;
	}

	public int getOrders() {
		return orders;
	}

	public void setOrders(int orders) {
		this.orders = orders;
	}

	public String getOrgImgUrl() {
		return orgImgUrl;
	}

	public void setOrgImgUrl(String orgImgUrl) {
		this.orgImgUrl = orgImgUrl;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
}
