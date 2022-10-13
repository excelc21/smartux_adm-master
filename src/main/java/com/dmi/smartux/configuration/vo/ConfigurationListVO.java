package com.dmi.smartux.configuration.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.vo.BaseVO;
import com.dmi.smartux.common.util.GlobalCom;

//@XmlType(name="클래스명" namespace="풀패키지명.클래스명", propOrder={위에서 아래로 보여줘야 할 멤버 순서들})
//@XmlType에서 name과 namespace는 필수, propOrder는 옵션으로 주지 않았을 경우엔 멤버변수 정의된 순서대로 보여준다
@XmlRootElement(name = "record") 	// vo의 XmlRootElement name속성은 record로 통일
@XmlType(name="ConfigurationListVO", namespace="com.dmi.smartux.configuration.vo.ConfigurationListVO", propOrder={"config_id","config_contents"})
public class ConfigurationListVO extends BaseVO {  

	String config_id;
	String config_contents;
		
	@XmlElement(name="config_id")
	public void setConfig_id(String config_id) {
		this.config_id = config_id;
	}		
	
	public String getConfig_id() {
		return GlobalCom.isNull(config_id);
	}
	
	@XmlElement(name="config_contents")
	public void setConfig_contents(String config_contents) {
		this.config_contents = config_contents;
	}		
	
	public String getConfig_contents() {
		return GlobalCom.isNull(config_contents);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append(getConfig_id());
		sb.append(GlobalCom.colsep);
		sb.append(getConfig_contents());
		sb.append(GlobalCom.colsep);
			
		return sb.toString();
	}	
}