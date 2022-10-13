package com.dmi.smartux.admin.quality.vo;

public class QualitySearchVo {

	private String file_id;			//파일명(키값)
	private String findName;		//검색 구분
	private String findValue;		//검색어
	private String s_log_type;			//로그 타입
	private int pageNum;		//페이지 번호
	private String serviceType;	//타입 (빈값 : 세컨드TV, P : 포터블 TV)

	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getFile_id() {
		return file_id;
	}
	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}
	public String getFindName() {
		return findName;
	}
	public void setFindName(String findName) {
		this.findName = findName;
	}
	public String getFindValue() {
		return findValue;
	}
	public void setFindValue(String findValue) {
		this.findValue = findValue;
	}
	public String getS_log_type() {
		return s_log_type;
	}
	public void setS_log_type(String s_log_type) {
		this.s_log_type = s_log_type;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

}
