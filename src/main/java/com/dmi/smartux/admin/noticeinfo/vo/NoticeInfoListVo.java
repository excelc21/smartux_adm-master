package com.dmi.smartux.admin.noticeinfo.vo;

import com.dmi.smartux.common.vo.BaseVO;

public class NoticeInfoListVo extends BaseVO{
	
	private String seq;
	private String ntype;
	private String category;
	private String category_nm;
	private String nvalue;
	private String ordered;
	private String display_sec;
	private String use_yn;
	private String reg_date;
	private String img_addr;
	

	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getNtype() {
		return ntype;
	}
	public void setNtype(String ntype) {
		this.ntype = ntype;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCategory_nm() {
		return category_nm;
	}
	public void setCategory_nm(String category_nm) {
		this.category_nm = category_nm;
	}
	public String getNvalue() {
		return nvalue;
	}
	public void setNvalue(String nvalue) {
		this.nvalue = nvalue;
	}
	public String getOrdered() {
		return ordered;
	}
	public void setOrdered(String ordered) {
		this.ordered = ordered;
	}
	public String getDisplay_sec() {
		return display_sec;
	}
	public void setDisplay_sec(String display_sec) {
		this.display_sec = display_sec;
	}
	public String getUse_yn() {
		return use_yn;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getImg_addr() {
		return img_addr;
	}
	public void setImg_addr(String img_addr) {
		this.img_addr = img_addr;
	}
}
