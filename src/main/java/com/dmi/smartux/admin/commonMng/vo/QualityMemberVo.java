package com.dmi.smartux.admin.commonMng.vo;

import java.io.Serializable;

public class QualityMemberVo implements Serializable {
	
	private String sa_id;
	private String find_type;
	private String size;
	private String log_type;
	private String userId;
	private String userIP;
	
	public String getSa_id() {
		return sa_id;
	}
	public void setSa_id(String sa_id) {
		this.sa_id = sa_id;
	}
	public String getFind_type() {
		return find_type;
	}
	public void setFind_type(String find_type) {
		this.find_type = find_type;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getLog_type() {
		return log_type;
	}
	public void setLog_type(String log_type) {
		this.log_type = log_type;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserIP() {
		return userIP;
	}
	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}
	
	public QualityMemberVo(){
	}
	
	public QualityMemberVo(String sa_id,String log_type,String find_type,String size,String userId,String userIP){
		this.sa_id = sa_id;
		this.find_type = find_type;
		this.size = size;
		this.log_type = log_type;
		this.userId = userId;
		this.userIP = userIP;
	}
	
	
}