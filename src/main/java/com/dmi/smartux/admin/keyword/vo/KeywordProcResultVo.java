package com.dmi.smartux.admin.keyword.vo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class KeywordProcResultVo implements Serializable{
	
	private static final long serialVersionUID = 7583391300554489259L;
	
	String flag;
	String message;
	String proc_type;
	String keyword_id;
	String keyword_name;
	String p_keyword_id;
	String keyword_type;
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
	public String getKeyword_id() {
		return StringUtils.defaultString(keyword_id);
	}
	public void setKeyword_id(String keyword_id) {
		this.keyword_id = keyword_id;
	}
	public String getKeyword_name() {
		return StringUtils.defaultString(keyword_name);
	}
	public void setKeyword_name(String keyword_name) {
		this.keyword_name = keyword_name;
	}
	public String getP_keyword_id() {
		return StringUtils.defaultString(p_keyword_id);
	}
	public void setP_keyword_id(String p_keyword_id) {
		this.p_keyword_id = p_keyword_id;
	}
	public String getKeyword_type() {
		return StringUtils.defaultString(keyword_type);
	}
	public void setKeyword_type(String keyword_type) {
		this.keyword_type = keyword_type;
	}
	public String getUse_yn() {
		return StringUtils.defaultString(use_yn);
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}

}
