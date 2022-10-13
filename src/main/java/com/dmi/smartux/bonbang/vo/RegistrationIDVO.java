package com.dmi.smartux.bonbang.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;

/**
 * RegistrationID VO 클래스
 * @author YJ KIM
 *
 */
@XmlRootElement(name = "result")
@XmlType(name="RegistrationIDVO", namespace="com.dmi.smartux.bonbang.vo.RegistrationIDVO")
public class RegistrationIDVO {
	private String flag;	//결과 코드
	private String message;	//결과 메시지
	
	
	/**
	 * 결과 코드
	 * @return the flag
	 */
	public String getFlag() {
		return flag;
	}


	/**
	 * 결과 코드
	 * @param flag the flag to set
	 */
	@XmlElement(name="flag")
	public void setFlag(String flag) {
		this.flag = flag;
	}


	/**
	 * 결과 메세지
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}


	/**
	 * 결과 메세지
	 * @param message the message to set
	 */
	@XmlElement(name="message")
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer result = new StringBuffer();
		result.append(flag);
		result.append(GlobalCom.colsep);
		result.append(message);
		return result.toString();
	}
}
