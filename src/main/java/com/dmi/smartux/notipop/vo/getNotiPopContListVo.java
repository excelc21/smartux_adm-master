package com.dmi.smartux.notipop.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

@SuppressWarnings("serial")
@XmlType(name="getNotiPopContListVo", namespace="com.dmi.smartux.notipop.vo"
	, propOrder={"ev_id", "ev_name"}
)
public class getNotiPopContListVo extends BaseVO {
	
	private String ev_id = "";
	private String ev_name = "";
	
	public String getEv_id() {
		return ev_id;
	}
	@XmlElement(name="ev_id")
	public void setEv_id(String ev_id) {
		this.ev_id = ev_id;
	}
	public String getEv_name() {
		return ev_name;
	}
	@XmlElement(name="ev_name")
	public void setEv_name(String ev_name) {
		this.ev_name = ev_name;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getEv_id());
		sb.append(GlobalCom.arrsep);
		sb.append(getEv_name());

		return sb.toString();
	}

}
