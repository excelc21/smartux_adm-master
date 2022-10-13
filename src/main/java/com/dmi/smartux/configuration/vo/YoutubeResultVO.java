package com.dmi.smartux.configuration.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.aspectj.ajde.ui.swing.GoToLineThread;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

@XmlRootElement(name = "result")
@XmlType(name="YoutubeResultVO", namespace="com.dmi.smartux.configuration.vo.YoutubeResultVO")
public class YoutubeResultVO extends BaseVO implements Serializable{
	private String flag;	//결과 코드
	private String message;	//결과 메시지
	private String search_key = "";	//초기 검색어 (카테고리명=검색어)
	
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


	/**
	 * 초기 검색어 (카테고리명=검색어)
	 * @return the search_key
	 */
	public String getSearch_key() {
		return GlobalCom.isNull(search_key);
	}


	/**
	 * 초기 검색어 (카테고리명=검색어)
	 * @param search_key the search_key to set
	 */
	@XmlElement(name="search_key")
	public void setSearch_key(String search_key) {
		this.search_key = GlobalCom.isNull(search_key);
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer result = new StringBuffer();
		result.append(flag);
		result.append(GlobalCom.colsep);
		result.append(message);
		result.append(GlobalCom.colsep);
		result.append(search_key);
		return result.toString();
	}
	
}
