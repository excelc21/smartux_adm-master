package com.dmi.smartux.quality.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import com.dmi.smartux.common.util.GlobalCom;

@SuppressWarnings("serial")
@JsonPropertyOrder({"server_ip", "size", "log_type"})
@XmlType(name="getQualityServerVo", namespace="com.dmi.smartux.quality.vo"
	, propOrder={"server_ip", "size", "log_type"}
)
public class getQualityServerVo {
	
	private String server_ip = "";
	private String size = "";
	private String log_type = "";
	
	public String getServer_ip() {
		return server_ip;
	}
	@XmlElement(name="server_ip")
	public void setServer_ip(String server_ip) {
		this.server_ip = server_ip;
	}
	public String getSize() {
		return size;
	}
	@XmlElement(name="size")
	public void setSize(String size) {
		this.size = size;
	}
	public String getLog_type() {
		return log_type;
	}
	@XmlElement(name="log_type")
	public void setLog_type(String log_type) {
		this.log_type = log_type;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getServer_ip());
		sb.append(GlobalCom.colsep);
		sb.append(getSize());
		sb.append(GlobalCom.colsep);
		sb.append(getLog_type());

		return sb.toString();
	}

}
