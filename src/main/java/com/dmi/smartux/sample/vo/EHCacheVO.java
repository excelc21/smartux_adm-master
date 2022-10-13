package com.dmi.smartux.sample.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

@XmlRootElement(name = "record") // vo의 XmlRootElement name속성은 record로 통일
@XmlType(name="EHCacheVO", namespace="com.dmi.smartux.sample.vo.EHCacheVO")
public class EHCacheVO extends BaseVO {
	
	String theme_code;
	String theme_name;
	String pro_cd;
	String service_id;
	String event_name;
	String start_time;
	String end_time;
	String rating;
	String resolution;
	
	public String getTheme_code() {
		return theme_code;
	}
	@XmlElement(name="theme_code")
	public void setTheme_code(String theme_code) {
		this.theme_code = theme_code;
	}
	
	public String getTheme_name() {
		return theme_name;
	}
	@XmlElement(name="theme_name")
	public void setTheme_name(String theme_name) {
		this.theme_name = theme_name;
	}
	
	public String getPro_cd() {
		return pro_cd;
	}
	@XmlElement(name="pro_cd")
	public void setPro_cd(String pro_cd) {
		this.pro_cd = pro_cd;
	}
	
	public String getService_id() {
		return service_id;
	}
	@XmlElement(name="service_id")
	public void setService_id(String service_id) {
		this.service_id = service_id;
	}
	
	public String getEvent_name() {
		return event_name;
	}
	@XmlElement(name="event_name")
	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}
	
	public String getStart_time() {
		return start_time;
	}
	@XmlElement(name="start_time")
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	
	public String getEnd_time() {
		return end_time;
	}
	@XmlElement(name="end_time")
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	
	public String getRating() {
		return rating;
	}
	@XmlElement(name="rating")
	public void setRating(String rating) {
		this.rating = rating;
	}
	
	public String getResolution() {
		return resolution;
	}
	@XmlElement(name="resolution")
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append(theme_code);
		sb.append(GlobalCom.colsep);
		sb.append(theme_name);
		sb.append(GlobalCom.colsep);
		sb.append(pro_cd);
		sb.append(GlobalCom.colsep);
		sb.append(service_id);
		sb.append(GlobalCom.colsep);
		sb.append(event_name);
		sb.append(GlobalCom.colsep);
		sb.append(start_time);
		sb.append(GlobalCom.colsep);
		sb.append(end_time);
		sb.append(GlobalCom.colsep);
		sb.append(rating);
		sb.append(GlobalCom.colsep);
		sb.append(resolution);
		return sb.toString();
	}
}
