package com.dmi.smartux.admin.hotvod.vo;

/**
 * 화제동영상 - 사이트관리 VO
 * @author JKJ
 */
public class HotvodSiteVO {
	private String site_id;		//사이트 아이디
	private String site_name;	//사이트명
	private String site_url;	//사이트URL
	private String site_img;	//사이트 로고 이미지(HDTV)
	private String site_img_tv;	//사이트 로고 이미지(IPTV)
	private String reg_dt;		//등록일시
	private String reg_id;		//등록자 아이디
	private String mod_dt;		//수정일시
	private String mod_id;		//수정자 아이디

	public String getSite_id() {
		return site_id;
	}

	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}

	public String getSite_name() {
		return site_name;
	}

	public void setSite_name(String site_name) {
		this.site_name = site_name;
	}

	public String getSite_url() {
		return site_url;
	}

	public void setSite_url(String site_url) {
		this.site_url = site_url;
	}

	public String getSite_img() {
		return site_img;
	}

	public void setSite_img(String site_img) {
		this.site_img = site_img;
	}

	public String getSite_img_tv() {
		return site_img_tv;
	}

	public void setSite_img_tv(String site_img_tv) {
		this.site_img_tv = site_img_tv;
	}

	public String getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}

	public String getReg_id() {
		return reg_id;
	}

	public void setReg_id(String reg_id) {
		this.reg_id = reg_id;
	}

	public String getMod_dt() {
		return mod_dt;
	}

	public void setMod_dt(String mod_dt) {
		this.mod_dt = mod_dt;
	}

	public String getMod_id() {
		return mod_id;
	}

	public void setMod_id(String mod_id) {
		this.mod_id = mod_id;
	}
}
