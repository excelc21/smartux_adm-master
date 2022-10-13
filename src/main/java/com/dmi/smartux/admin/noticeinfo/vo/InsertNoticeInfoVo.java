package com.dmi.smartux.admin.noticeinfo.vo;

import com.dmi.smartux.common.vo.BaseVO;

public class InsertNoticeInfoVo extends BaseVO{

	private String service;
	private String category;
	private String nvalue;
	private String ordered;
	private String display_sec;
	private String use_yn;
	private String ntype;
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
	public String getNtype() {
		return ntype;
	}
	public void setNtype(String ntype) {
		this.ntype = ntype;
	}

}
