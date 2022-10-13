package com.dmi.smartux.notipop.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.quality.vo.getQualityServerVo;

@SuppressWarnings("serial")
@XmlRootElement(name = "result")
@JsonPropertyOrder({"flag", "message", "emergency", "noti", "quality"})
@XmlType(name="getNotiPopCacheVo", namespace="com.dmi.smartux.notipop.vo"
, propOrder={"flag", "message", "emergency", "noti", "quality"}
)
public class getNotiPopCacheVo {

	private String flag = "";
	private String message = "";
	
	private getNotiPopEmergencyVo emergency;
	
	private getNotiPopNotiListVo noti;
	
	private getQualityServerVo quality;
	
	public String getFlag() {
		return flag;
	}
	
	@XmlElement(name="flag")
	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getMessage() {
		return message;
	}
	
	@XmlElement(name="message")
	public void setMessage(String message) {
		this.message = message;
	}

	public getNotiPopEmergencyVo getEmergency() {
		return emergency;
	}

	@XmlElement(name="emergency")
	public void setEmergency(getNotiPopEmergencyVo emergency) {
		this.emergency = emergency;
	}

	public getNotiPopNotiListVo getNoti() {
		return noti;
	}

	@XmlElement(name="noti")
	public void setNoti(getNotiPopNotiListVo noti) {
		this.noti = noti;
	}
	
	public getQualityServerVo getQuality() {
		return quality;
	}

	@XmlElement(name="quality")
	public void setQuality(getQualityServerVo quality) {
		this.quality = quality;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getFlag());
		sb.append(GlobalCom.colsep);
		sb.append(getMessage());
		sb.append(GlobalCom.rowsep);
		if(getEmergency()!=null)
			sb.append(getEmergency().toString());
		sb.append(GlobalCom.rowsep);
		if(getNoti()!=null)
			sb.append(getNoti().toString());
		sb.append(GlobalCom.rowsep);
		if(getQuality()!=null)
			sb.append(getQuality().toString());

		return sb.toString();
	}

}
