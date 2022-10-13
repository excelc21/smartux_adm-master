package com.dmi.smartux.admin.multipayment.vo;

public class PtUxPaymentPpmVo {

	private String pa_key;
	private String pt_month;
	private String pt_year;
	private String pa_type;
	private String m_type;
	private String sa_id;
	private String stb_mac;
	private String pa_status;
	private String pa_reg_dt;
	private String pa_mod_dt;
	private String app_type;
	private String product_code;
	private String product_name;
	private String pa_flag;
	private String pa_message;
	
	//페이징
	private int pageSize;			//페이징시 게시물의 노출 개수
	private int blockSize;			//한 화면에 노출할 페이지 번호 개수
	private int pageNum;			//현재 페이지 번호
	private int pageCount;			//페이지 번호 전체 사이즈
	private int start_rnum;			//DB(오라클) 페이징 시작 번호
	private int end_rnum;			//DB(오라클) 페이징 끝 번호
	
	private String start_dt;
	private String end_dt; 
	private String findName;
	private String findValue;
	
	public String getPa_key() {
		return pa_key;
	}
	public void setPa_key(String pa_key) {
		this.pa_key = pa_key;
	}
	public String getPt_month() {
		return pt_month;
	}
	public void setPt_month(String pt_month) {
		this.pt_month = pt_month;
	}
	public String getPt_year() {
		return pt_year;
	}
	public void setPt_year(String pt_year) {
		this.pt_year = pt_year;
	}
	public String getPa_type() {
		return pa_type;
	}
	public void setPa_type(String pa_type) {
		this.pa_type = pa_type;
	}
	public String getM_type() {
		return m_type;
	}
	public void setM_type(String m_type) {
		this.m_type = m_type;
	}
	public String getSa_id() {
		return sa_id;
	}
	public void setSa_id(String sa_id) {
		this.sa_id = sa_id;
	}
	public String getStb_mac() {
		return stb_mac;
	}
	public void setStb_mac(String stb_mac) {
		this.stb_mac = stb_mac;
	}
	public String getPa_status() {
		return pa_status;
	}
	public void setPa_status(String pa_status) {
		this.pa_status = pa_status;
	}
	public String getPa_reg_dt() {
		return pa_reg_dt;
	}
	public void setPa_reg_dt(String pa_reg_dt) {
		this.pa_reg_dt = pa_reg_dt;
	}
	public String getPa_mod_dt() {
		return pa_mod_dt;
	}
	public void setPa_mod_dt(String pa_mod_dt) {
		this.pa_mod_dt = pa_mod_dt;
	}
	public String getApp_type() {
		return app_type;
	}
	public void setApp_type(String app_type) {
		this.app_type = app_type;
	}
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getPa_flag() {
		return pa_flag;
	}
	public void setPa_flag(String pa_flag) {
		this.pa_flag = pa_flag;
	}
	public String getPa_message() {
		return pa_message;
	}
	public void setPa_message(String pa_message) {
		this.pa_message = pa_message;
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
	public String getStart_dt() {
		return start_dt;
	}
	public void setStart_dt(String start_dt) {
		this.start_dt = start_dt;
	}
	public String getEnd_dt() {
		return end_dt;
	}
	public void setEnd_dt(String end_dt) {
		this.end_dt = end_dt;
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
	
	
	
	
}
