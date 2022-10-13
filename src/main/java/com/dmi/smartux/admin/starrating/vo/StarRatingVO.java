package com.dmi.smartux.admin.starrating.vo;

import java.util.List;

public class StarRatingVO {
	private String rowno;
	private String sr_id;
	private String sr_pid;
	private String use_yn;
	private String sr_type;
	private String sr_order;
	private String sr_title;
	private String sr_point;
	private String img_file;
	private String system_gb;
	private String reg_id;
	private String reg_date;
	private String mod_id;
	private String mod_date;
	private List<String> srList;
	
	public String getRowno() {
		return rowno;
	}
	public void setRowno(String rowno) {
		this.rowno = rowno;
	}
	public String getSr_id() {
		return sr_id;
	}
	public void setSr_id(String sr_id) {
		this.sr_id = sr_id;
	}
	public String getSr_pid() {
		return sr_pid;
	}
	public void setSr_pid(String sr_pid) {
		this.sr_pid = sr_pid;
	}
	public String getUse_yn() {
		return use_yn;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}
	public String getSr_type() {
		return sr_type;
	}
	public void setSr_type(String sr_type) {
		this.sr_type = sr_type;
	}
	public String getSr_order() {
		return sr_order;
	}
	public void setSr_order(String sr_order) {
		this.sr_order = sr_order;
	}
	public String getSr_title() {
		return sr_title;
	}
	public void setSr_title(String sr_title) {
		this.sr_title = sr_title;
	}
	public String getSr_point() {
		return sr_point;
	}
	public void setSr_point(String sr_point) {
		this.sr_point = sr_point;
	}
	public String getImg_file() {
		return img_file;
	}
	public void setImg_file(String img_file) {
		this.img_file = img_file;
	}
	public String getSystem_gb() {
		return system_gb;
	}
	public void setSystem_gb(String system_gb) {
		this.system_gb = system_gb;
	}
	public String getReg_id() {
		return reg_id;
	}
	public void setReg_id(String reg_id) {
		this.reg_id = reg_id;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getMod_id() {
		return mod_id;
	}
	public void setMod_id(String mod_id) {
		this.mod_id = mod_id;
	}
	public String getMod_date() {
		return mod_date;
	}
	public void setMod_date(String mod_date) {
		this.mod_date = mod_date;
	}
	public List<String> getSrList() {
		return srList;
	}
	public void setSrList(List<String> srList) {
		this.srList = srList;
	}
}
