package com.dmi.smartux.sample.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

//@XmlType(name="클래스명" namespace="풀패키지명.클래스명", propOrder={위에서 아래로 보여줘야 할 멤버 순서들})
//@XmlType에서 name과 namespace는 필수, propOrder는 옵션으로 주지 않았을 경우엔 멤버변수 정의된 순서대로 보여준다
@XmlRootElement(name = "record") // vo의 XmlRootElement name속성은 record로 통일
@XmlType(name="SampleVO2", namespace="com.dmi.smartux.sample.vo.SampleVO2", propOrder={"name", "idx"})
public class SampleVO2 extends BaseVO {

	int idx;
	String name;

	public int getIdx() {
		return idx;
	}
	
	@XmlElement(name="myidx")
	public void setIdx(int idx) {
		this.idx = idx;
	}
	
	public String getName() {
		return name;
	}
	
	@XmlElement(name="myname")
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
