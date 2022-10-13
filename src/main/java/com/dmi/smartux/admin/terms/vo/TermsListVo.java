package com.dmi.smartux.admin.terms.vo;

public class TermsListVo {
	
	///////////////////////페이징
	private int pageSize;			//페이징시 게시물의 노출 개수
	private int blockSize;			//한 화면에 노출할 페이지 번호 개수
	private int pageNum;			//현재 페이지 번호
	private int pageCount;			//페이지 번호 전체 사이즈
	
	private String findName;		//검색 구분
	private String findValue;		//검색어
	
	private int start_rnum;		//DB(오라클) 페이징 시작 번호
	private int end_rnum;			//DB(오라클) 페이징 끝 번호
	
	private String s_service_type;		//약관종류
	private String s_service_gbn;		//약관종류
	private String s_terms_gbn;		//약관종류
	private String s_display_yn;		//비노출 컨텐츠만 조회 여부
		
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
	public int getStart_rnum() {
		return start_rnum;
	}
	public void setStart_rnum(int start_rnum) {
		this.start_rnum = start_rnum;
	}
	public int getEnd_rnum() {
		return end_rnum;
	}
	public void setEnd_rnum(int end_rnum) {
		this.end_rnum = end_rnum;
	}
	public String getS_service_type() {
		return s_service_type;
	}
	public void setS_service_type(String s_service_type) {
		this.s_service_type = s_service_type;
	}
	public String getS_service_gbn() {
		return s_service_gbn;
	}
	public void setS_service_gbn(String s_service_gbn) {
		this.s_service_gbn = s_service_gbn;
	}
	public String getS_terms_gbn() {
		return s_terms_gbn;
	}
	public void setS_terms_gbn(String s_terms_gbn) {
		this.s_terms_gbn = s_terms_gbn;
	}
	public String getS_display_yn() {
		return s_display_yn;
	}
	public void setS_display_yn(String s_display_yn) {
		this.s_display_yn = s_display_yn;
	}

}
