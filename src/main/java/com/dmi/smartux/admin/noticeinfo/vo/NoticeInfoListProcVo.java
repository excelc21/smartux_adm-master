package com.dmi.smartux.admin.noticeinfo.vo;

import java.util.List;

import com.dmi.smartux.common.vo.BaseVO;

public class NoticeInfoListProcVo extends BaseVO{
	
	private int pageSize;			//페이징시 게시물의 노출 개수
	private int pageNum;			//현재 페이지 번호
	private int blockSize;			//한 화면에 노출할 페이지 번호 개수
	private int pageCount;			//페이지 번호 전체 사이즈
	
	private String findValue;		//검색어
	private String s_use_yn;			//사용여부
	private String s_category;			//카테고리
	private String s_ntype;			//공지/배너 구분
	private String s_service;		//서비스 구분
	
	private int start_rnum;		//DB(오라클) 페이징 시작 번호
	private int end_rnum;			//DB(오라클) 페이징 끝 번호
	
	private List<NoticeInfoListVo> list; //공지목록
	
	private List<NotiMainCodeListVo> service_code; //메인코드 목록
	
	private List<NotiCodeListVo> category_code; //장르코드 목록
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
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
	public List<NoticeInfoListVo> getList() {
		return list;
	}
	public void setList(List<NoticeInfoListVo> list) {
		this.list = list;
	}
	public String getS_use_yn() {
		return s_use_yn;
	}
	public void setS_use_yn(String s_use_yn) {
		this.s_use_yn = s_use_yn;
	}
	public String getS_category() {
		return s_category;
	}
	public void setS_category(String s_category) {
		this.s_category = s_category;
	}
	public String getS_ntype() {
		return s_ntype;
	}
	public void setS_ntype(String s_ntype) {
		this.s_ntype = s_ntype;
	}
	public List<NotiMainCodeListVo> getService_code() {
		return service_code;
	}
	public void setService_code(List<NotiMainCodeListVo> service_code) {
		this.service_code = service_code;
	}
	public List<NotiCodeListVo> getCategory_code() {
		return category_code;
	}
	public void setCategory_code(List<NotiCodeListVo> category_code) {
		this.category_code = category_code;
	}
	public String getS_service() {
		return s_service;
	}
	public void setS_service(String s_service) {
		this.s_service = s_service;
	}
	
}
