package com.dmi.smartux.admin.season.vo;

public class SeasonSearchVo {
	
	private String findName;
	private String findValue;
	private String series_yn;
	private String app_tp;
	
	//페이징
	private int pageSize;			//페이징시 게시물의 노출 개수
	private int blockSize;			//한 화면에 노출할 페이지 번호 개수
	private int pageNum;			//현재 페이지 번호
	private int pageCount;			//페이지 번호 전체 사이즈
	private int start_rnum;			//DB(오라클) 페이징 시작 번호
	private int end_rnum;			//DB(오라클) 페이징 끝 번호
	
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

	public String getSeries_yn() {
		return series_yn;
	}

	public void setSeries_yn(String series_yn) {
		this.series_yn = series_yn;
	}

	public String getApp_tp() {
		return app_tp;
	}

	public void setApp_tp(String app_tp) {
		this.app_tp = app_tp;
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
	
}
