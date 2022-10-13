package com.dmi.smartux.admin.abtest.vo;

import java.util.List;

public class BPASSearchVo {

	// 페이징
	private int pageSize; // 페이징시 게시물의 노출 개수
	private int blockSize; // 한 화면에 노출할 페이지 번호 개수
	private int pageNum; // 현재 페이지 번호
	private int pageCount; // 페이지 번호 전체 사이즈
	private int start_rnum; // DB(오라클) 페이징 시작 번호
	private int end_rnum; // DB(오라클) 페이징 끝 번호
	
	private String startDt;
	private String endDt;
	private String ab_yn;
	private List<String> mims_type_list;
	private String mims_type_arr;
	private String findType;
	private String findValue;

	
	
	
	@Override
	public String toString() {
		return "BPASSearchVo [pageSize=" + pageSize + ", blockSize=" + blockSize
				+ ", pageNum=" + pageNum + ", pageCount=" + pageCount
				+ ", start_rnum=" + start_rnum + ", end_rnum=" + end_rnum
				+ ", startDt=" + startDt + ", endDt=" + endDt + ", ab_yn="
				+ ab_yn + ", mims_type_list=" + mims_type_list
				+ ", mims_type_arr=" + mims_type_arr + ", findType=" + findType
				+ ", findValue=" + findValue + "]";
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

	public String getStartDt() {
		return startDt;
	}

	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}

	public String getEndDt() {
		return endDt;
	}

	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}

	public String getAb_yn() {
		return ab_yn;
	}

	public void setAb_yn(String ab_yn) {
		this.ab_yn = ab_yn;
	}

	public List<String> getMims_type_list() {
		return mims_type_list;
	}

	public void setMims_type_list(List<String> mims_type_list) {
		this.mims_type_list = mims_type_list;
	}

	public String getFindType() {
		return findType;
	}

	public void setFindType(String findType) {
		this.findType = findType;
	}

	public String getFindValue() {
		return findValue;
	}

	public void setFindValue(String findValue) {
		this.findValue = findValue;
	}

	public String getMims_type_arr() {
		return mims_type_arr;
	}

	public void setMims_type_arr(String mims_type_arr) {
		this.mims_type_arr = mims_type_arr;
	}

}
