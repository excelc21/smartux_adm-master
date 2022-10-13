package com.dmi.smartux.admin.multipayment.vo;

import java.util.ArrayList;
import java.util.List;

public class MultiPaymentSearchVo {
	private String type;			//결제타입구분(조회용)
	//검색
	private String findName;		//검색구분
	private String findValue;		//검색어
	private String paKey;			//결제키
	private String paType;			//결제타입(검색용)
	private String mtype;			//단말타입
	private String startDt;			//시작일자
	private String endDt;			//종료일자
	private String status;			//결제상태
	private String discount_div;	//할인수단
	private String downloadDt;		//엑셀 다운로드 요청 시간(페이징을 하기 때문에 요청 시점 이하로 구분짓기 위함)

	private String find_pa_key;			// 라이브 결제키

	//페이징
	private int pageSize;			//페이징시 게시물의 노출 개수
	private int blockSize;			//한 화면에 노출할 페이지 번호 개수
	private int pageNum;			//현재 페이지 번호
	private int pageCount;			//페이지 번호 전체 사이즈
	private int start_rnum;			//DB(오라클) 페이징 시작 번호
	private int end_rnum;			//DB(오라클) 페이징 끝 번호
	
	//파티션 키
	private String pt_year;			//파티션 연도 필드(0: 짝수, 1: 홀수)
	private String pt_month;			//파티션 월 필드
	private int compareYear;		//파티션 연도 비교(1: 동일한 경우) 
	private List<String> month_list = new ArrayList<String>();

	public List<String> getMonth_list() {
		return month_list;
	}

	public void setMonth_list(List<String> month_list) {
		this.month_list = month_list;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getPaKey() {
		return paKey;
	}
	public void setPaKey(String paKey) {
		this.paKey = paKey;
	}
	public String getPaType() {
		return paType;
	}
	public void setPaType(String paType) {
		this.paType = paType;
	}
	public String getMtype() {
		return mtype;
	}
	public void setMtype(String mtype) {
		this.mtype = mtype;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getPt_year() {
		return pt_year;
	}
	public void setPt_year(String pt_year) {
		this.pt_year = pt_year;
	}
	public String getPt_month() {
		return pt_month;
	}
	public void setPt_month(String pt_month) {
		this.pt_month = pt_month;
	}
	public int getCompareYear() {
		return compareYear;
	}
	public void setCompareYear(int compareYear) {
		this.compareYear = compareYear;
	}

	public String getDiscount_div() {
		return discount_div;
	}

	public void setDiscount_div(String discount_div) {
		this.discount_div = discount_div;
	}

	public String getDownloadDt() {
		return downloadDt;
	}

	public void setDownloadDt(String downloadDt) {
		this.downloadDt = downloadDt;
	}

	public String getFind_pa_key() {
		return find_pa_key;
	}

	public void setFind_pa_key(String find_pa_key) {
		this.find_pa_key = find_pa_key;
	}
}
