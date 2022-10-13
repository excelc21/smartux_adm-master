package com.dmi.smartux.admin.hdtv.vo;

import com.dmi.smartux.common.util.GlobalCom;

public class HDTVVO {
	
	private String fileName;
	private String status;
	private String message_yn;
	private String message;
	private String net_type;
	
	
	
	public String getFileName() {
		return GlobalCom.isNull(fileName);
	}
	public void setFileName(String fileName) {
		this.fileName = GlobalCom.isNull(fileName);
	}
	public String getStatus() {
		return GlobalCom.isNull(status);
	}
	public void setStatus(String status) {
		this.status = GlobalCom.isNull(status);
	}
	public String getMessage_yn() {
		return GlobalCom.isNull(message_yn);
	}
	public void setMessage_yn(String message_yn) {
		this.message_yn = GlobalCom.isNull(message_yn);
	}
	public String getMessage() {
		return GlobalCom.isNull(message);
	}
	public void setMessage(String message) {
		this.message = GlobalCom.isNull(message);
	}
	public String getNet_type() {
		return GlobalCom.isNull(net_type);
	}
	public void setNet_type(String net_type) {
		this.net_type = GlobalCom.isNull(net_type);
	}
	
	
	
	
}
