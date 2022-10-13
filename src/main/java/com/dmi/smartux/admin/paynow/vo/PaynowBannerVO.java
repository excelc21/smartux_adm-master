package com.dmi.smartux.admin.paynow.vo;

public class PaynowBannerVO {
	
	private String banner_id;	
	private String banner_img;	
	private String banner_text;	
	private String use_yn;
	private String img_url;

	public String getBanner_id() {
		return banner_id;
	}

	public void setBanner_id(String banner_id) {
		this.banner_id = banner_id;
	}
	
	public String getBanner_img() {
		return banner_img;
	}

	public void setBanner_img(String banner_img) {
		String[] str = banner_img.split("\\|\\|");
		if(str.length > 1) {
			this.banner_img = str[0];
			this.banner_text = str[1];
		} else {
			this.banner_img = banner_img.replaceAll("\\|", "");
		}
	}

	public String getBanner_text() {
		return banner_text;
	}

	public void setBanner_text(String banner_text) {
		this.banner_text = banner_text;
	}

	public String getUse_yn() {
		return use_yn;
	}

	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	
}
