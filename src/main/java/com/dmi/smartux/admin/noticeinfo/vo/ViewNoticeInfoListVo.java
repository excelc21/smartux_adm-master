package com.dmi.smartux.admin.noticeinfo.vo;

import com.dmi.smartux.common.vo.BaseVO;

public class ViewNoticeInfoListVo extends BaseVO{
	
	private String service;
	private String ntype;
	private String category;
	private String nvalue;
	private String display_sec;

	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
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
	public String getNvalue() {
		return nvalue;
	}
	public void setNvalue(String nvalue) {
		this.nvalue = nvalue;
	}
	public String getDisplay_sec() {
		return display_sec;
	}
	public void setDisplay_sec(String display_sec) {
		this.display_sec = display_sec;
	}

}