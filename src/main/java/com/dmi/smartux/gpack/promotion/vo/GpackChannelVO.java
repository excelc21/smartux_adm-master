package com.dmi.smartux.gpack.promotion.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

@XmlRootElement(name="record_mov")
@XmlType(name="GpackChannelVO", namespace="com.dmi.smartux.gpack.promotion.vo.GpackChannelVO"
, propOrder={"p_title","service_id"})
public class GpackChannelVO extends BaseVO implements Serializable{

	String p_title = "";
	String service_id = "";
	
	public String getP_title() {
		return GlobalCom.isNull(p_title);
	}
	
	@XmlElement(name="p_title")
	public void setP_title(String p_title) {
		this.p_title = p_title;
	}
	
	public String getService_id() {
		return GlobalCom.isNull(service_id);
	}
	
	@XmlElement(name="service_id")
	public void setService_id(String service_id) {
		this.service_id = service_id;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append(getP_title());
		
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getService_id()));
		
		return sb.toString();
	}
	
}
