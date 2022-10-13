package com.dmi.smartux.admin.hotvod.vo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class AddHotvodApiVo implements Serializable{
	
	private static final long serialVersionUID = -1936797394141792905L;
	
	String flag;
	String message;
	String id;
	
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
	public String getId() {
		return StringUtils.defaultString(id, "");
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
