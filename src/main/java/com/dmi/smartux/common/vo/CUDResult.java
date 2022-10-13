package com.dmi.smartux.common.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;

/**
 * Insert, Delete, Update 작업을 진행할땐 결과 코드와 메시지만을 전달하면 되므로 이런 작업에 대한 공통 결과 클래스를 만들었다
 * @author Terry Chang
 *
 */
@XmlRootElement(name = "result")
@XmlType(name="CUDResult", namespace="com.dmi.smartux.common.vo.CUDResult")
public class CUDResult extends BaseVO {

	String flag;
	String message;
	
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
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append(flag);
		sb.append(GlobalCom.colsep);
		sb.append(message);
		return sb.toString();
	}
	
	
}
