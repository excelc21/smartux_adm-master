package com.dmi.smartux.gpack.pack.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

@XmlRootElement(name="result")
@XmlType(name="GPackPackVersionVO", namespace="com.dmi.smartux.gpack.pack.vo.GPackPackVersionVO"
, propOrder={"flag", "message", "pack_version"})
public class GPackPackVersionVO extends BaseVO implements Serializable{
	
	String flag = "";
	String message = "";
	String pack_version = "";
	
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
	
	public String getPack_version() {
		return GlobalCom.isNull(pack_version);
	}
	
	@XmlElement(name="pack_version")
	public void setPack_version(String pack_version) {
		this.pack_version = pack_version;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub

		StringBuffer sb = new StringBuffer();
		
		sb.append(getFlag());
		sb.append(GlobalCom.colsep);
		sb.append(getMessage());
		
		// 에러가 발생할 경우 version 정보가 셋팅이 되지 않으므로 문자열 결합시 version이 결합되지 않도록 한다
		if(!(GlobalCom.isNull(pack_version).equals(""))){
			sb.append(GlobalCom.colsep);
			sb.append(getPack_version());
		}
		
		return sb.toString();
	}
}
