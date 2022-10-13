package com.dmi.smartux.mainpanel.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

@XmlRootElement(name="result")
@XmlType(name="MainPanelVersionInfoVO", namespace="com.dmi.smartux.mainpanel.vo.MainPanelVersionInfoVO"
, propOrder={"flag", "message", "version"})
public class MainPanelVersionInfoVO extends BaseVO implements Serializable{

	String flag = "";
	String message = "";
	String version = "";
	
	public String getFlag() {
		return GlobalCom.isNull(flag);
	}
	
	@XmlElement(name="flag")
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public String getMessage() {
		return GlobalCom.isNull(message);
	}
	
	@XmlElement(name="message")
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getVersion() {
		return GlobalCom.isNull(version);
	}
	
	@XmlElement(name="version")
	public void setVersion(String version) {
		this.version = version;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub

		StringBuffer sb = new StringBuffer();
		
		sb.append(getFlag());
		sb.append(GlobalCom.colsep);
		sb.append(getMessage());
		
		// 에러가 발생할 경우 version 정보가 셋팅이 되지 않으므로 문자열 결합시 version이 결합되지 않도록 한다
		if(!(GlobalCom.isNull(version).equals(""))){
			sb.append(GlobalCom.colsep);
			sb.append(getVersion());
		}
		
		return sb.toString();
	}
}
