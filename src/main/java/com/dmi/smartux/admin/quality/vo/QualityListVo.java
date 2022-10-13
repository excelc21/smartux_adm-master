package com.dmi.smartux.admin.quality.vo;

import java.util.List;

public class QualityListVo extends QualitySearchVo {
	
	private int pageSize;			//페이징시 게시물의 노출 개수
	private int blockSize;			//한 화면에 노출할 페이지 번호 개수
	private int pageCount;			//페이지 번호 전체 사이즈
	
	private int start_rnum;		//페이징 시작 번호
	private int end_rnum;			//페이징 끝 번호
	
	private List<QualityMemberListVo> list;
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
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
	public int getBlockSize() {
		return blockSize;
	}
	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public List<QualityMemberListVo> getList() {
		return list;
	}
	public void setList(List<QualityMemberListVo> list) {
		this.list = list;
	}

}
