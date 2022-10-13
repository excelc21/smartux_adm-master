package com.dmi.smartux.admin.gallery.vo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class GalleryProcResultVo implements Serializable{
	
	private static final long serialVersionUID = 7583391300554489259L;
	
	String flag;
	String message;
	String proc_type;
	String gallery_id;
	String gallery_name;
	String p_gallery_id;
	String gallery_type;
	String use_yn;
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getProc_type() {
		return StringUtils.defaultString(proc_type);
	}
	public void setProc_type(String proc_type) {
		this.proc_type = proc_type;
	}
	public String getGallery_id() {
		return StringUtils.defaultString(gallery_id);
	}
	public void setGallery_id(String gallery_id) {
		this.gallery_id = gallery_id;
	}
	public String getGallery_name() {
		return StringUtils.defaultString(gallery_name);
	}
	public void setGallery_name(String gallery_name) {
		this.gallery_name = gallery_name;
	}
	public String getP_gallery_id() {
		return StringUtils.defaultString(p_gallery_id);
	}
	public void setP_gallery_id(String p_gallery_id) {
		this.p_gallery_id = p_gallery_id;
	}
	public String getGallery_type() {
		return StringUtils.defaultString(gallery_type);
	}
	public void setGallery_type(String gallery_type) {
		this.gallery_type = gallery_type;
	}
	public String getUse_yn() {
		return StringUtils.defaultString(use_yn);
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}

}
