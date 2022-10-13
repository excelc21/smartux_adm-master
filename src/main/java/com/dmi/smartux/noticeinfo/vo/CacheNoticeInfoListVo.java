package com.dmi.smartux.noticeinfo.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

@XmlRootElement(name = "record") // vo의 XmlRootElement name속성은 record로 통일
@XmlType(name="CacheNoticeInfoListVo", namespace="com.dmi.smartux.noticeinfo.vo.CacheNoticeInfoListVo"
, propOrder={"service", "ntype", "category", "nvalue", "display_sec", "reg_date"})
public class CacheNoticeInfoListVo extends BaseVO implements Serializable{
	
	String service;
	String ntype;
	String category;
	String nvalue;
	String display_sec;
	String reg_date;

	public String getService() {
		return service;
	}
	@XmlElement(name="service")
	public void setService(String service) {
		this.service = service;
	}
	public String getNtype() {
		return ntype;
	}
	@XmlElement(name="ntype")
	public void setNtype(String ntype) {
		this.ntype = ntype;
	}
	public String getCategory() {
		return category;
	}
	@XmlElement(name="category")
	public void setCategory(String category) {
		this.category = category;
	}
	public String getNvalue() {
		return nvalue;
	}
	@XmlElement(name="nvalue")
	public void setNvalue(String nvalue) {
		this.nvalue = nvalue;
	}
	public String getDisplay_sec() {
		return display_sec;
	}
	@XmlElement(name="display_sec")
	public void setDisplay_sec(String display_sec) {
		this.display_sec = display_sec;
	}
	public String getReg_date() {
		return reg_date;
	}
	@XmlElement(name="reg_date")
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getService());
		sb.append(GlobalCom.colsep);
		sb.append(getNtype());
		sb.append(GlobalCom.colsep);
		sb.append(getCategory());
		sb.append(GlobalCom.colsep);
		sb.append(getNvalue());
		sb.append(GlobalCom.colsep);
		sb.append(getDisplay_sec());
		sb.append(GlobalCom.colsep);
		sb.append(getReg_date());
		
		return sb.toString();
	}

}
