package com.dmi.smartux.admin.statbbsAdmin.vo;

public class StatbbsVo {
	private int p_pageNum; // 현재 페이지 번호
	private String p_findName; // 검색 구분
	private String p_findValue; // 검색어
	private String p_use_yn; // 사용여부

	public int getP_pageNum ( ) {
		return p_pageNum;
	}

	public void setP_pageNum ( int p_pageNum ) {
		this.p_pageNum = p_pageNum;
	}

	public String getP_findName ( ) {
		return p_findName;
	}

	public void setP_findName ( String p_findName ) {
		this.p_findName = p_findName;
	}

	public String getP_findValue ( ) {
		return p_findValue;
	}

	public void setP_findValue ( String p_findValue ) {
		this.p_findValue = p_findValue;
	}

	public String getP_use_yn ( ) {
		return p_use_yn;
	}

	public void setP_use_yn ( String p_use_yn ) {
		this.p_use_yn = p_use_yn;
	}
}
