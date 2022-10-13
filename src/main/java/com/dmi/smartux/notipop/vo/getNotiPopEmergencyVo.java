package com.dmi.smartux.notipop.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import com.dmi.smartux.common.util.GlobalCom;

@SuppressWarnings("serial")
@JsonPropertyOrder({"status", "message_yn", "message", "net_type"})
@XmlType(name="getNotiPopEmergencyVo", namespace="com.dmi.smartux.notipop.vo"
	, propOrder={"status", "message_yn", "message", "net_type"}
)
public class getNotiPopEmergencyVo {
	
	private String status = "";
	private String message_yn = "";
	private String message = "";
	private String net_type = "";
	
	public String getStatus() {
		return status;
	}
	@XmlElement(name="status")
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage_yn() {
		return message_yn;
	}
	@XmlElement(name="message_yn")
	public void setMessage_yn(String message_yn) {
		this.message_yn = message_yn;
	}
	public String getMessage() {
		return message;
	}
	@XmlElement(name="message")
	public void setMessage(String message) {
		this.message = message;
	}
	public String getNet_type() {
		return net_type;
	}
	@XmlElement(name="net_type")
	public void setNet_type(String net_type) {
		this.net_type = net_type;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getStatus());
		sb.append(GlobalCom.colsep);
		sb.append(getMessage_yn());
		sb.append(GlobalCom.colsep);
		sb.append(getMessage());
		sb.append(GlobalCom.colsep);
		sb.append(getNet_type());

		return sb.toString();
	}

}
