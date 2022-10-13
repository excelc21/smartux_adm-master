package com.dmi.smartux.admin.terms.vo;

public class TermsAccessGroupSearchVo {
	
	private String findName;
	private String findValue;
	private String access_mst_id;

	// 페이징
	private int pageSize; // 페이징시 게시물의 노출 개수
	private int blockSize; // 한 화면에 노출할 페이지 번호 개수
	private int pageNum; // 현재 페이지 번호
	private int pageCount; // 페이지 번호 전체 사이즈
	private int start_rnum; // DB(오라클) 페이징 시작 번호
	private int end_rnum; // DB(오라클) 페이징 끝 번호

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

	public String getAccess_mst_id() {
		return access_mst_id;
	}

	public void setAccess_mst_id(String access_mst_id) {
		this.access_mst_id = access_mst_id;
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
