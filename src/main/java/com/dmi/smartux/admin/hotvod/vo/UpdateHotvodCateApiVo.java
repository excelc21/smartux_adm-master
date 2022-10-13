package com.dmi.smartux.admin.hotvod.vo;

import java.io.Serializable;
import java.util.List;

public class UpdateHotvodCateApiVo implements Serializable{
	
	private static final long serialVersionUID = -8132260599165358720L;
	
	String flag;
	String message;
	List<HotvodContentIdListVo> success;
	List<HotvodContentIdListVo> failure;
	
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
	public List<HotvodContentIdListVo> getSuccess() {
		return success;
	}
	public void setSuccess(List<HotvodContentIdListVo> success) {
		this.success = success;
	}
	public List<HotvodContentIdListVo> getFailure() {
		return failure;
	}
	public void setFailure(List<HotvodContentIdListVo> failure) {
		this.failure = failure;
	}
	
}
