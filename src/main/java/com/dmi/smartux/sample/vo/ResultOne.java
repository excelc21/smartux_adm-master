package com.dmi.smartux.sample.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

@XmlRootElement(name = "record") // vo의 XmlRootElement name속성은 record로 통일
public class ResultOne extends BaseVO {

	String flag;
	String message;
	
	int idx;
	String name;
	
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
	public int getIdx() {
		return idx;
	}
	@XmlElement(name="idx")
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getName() {
		return name;
	}
	@XmlElement(name="name")
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append(idx);
		sb.append(GlobalCom.colsep);
		sb.append(name);
		return sb.toString();
	}
}
