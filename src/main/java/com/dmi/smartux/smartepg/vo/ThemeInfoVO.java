package com.dmi.smartux.smartepg.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

/**
 * 실시간 시청률을 조회하기 위한 테마 정보 객체
 * @author Terry Chang
 *
 */
@XmlRootElement(name = "record")
@XmlType(name="ThemeInfo", namespace="com.dmi.smartux.smartepg.vo.ThemeInfo", propOrder={"theme_code", "theme_name", "theme_ename"})
public class ThemeInfoVO extends BaseVO implements Serializable{

	String theme_code;
	String theme_name;
	String theme_ename;
	
	/**
	 * 테마코드를 리턴한다
	 * @return 테마코드
	 */
	public String getTheme_code() {
		return GlobalCom.isNull(theme_code);
	}
	
	/**
	 * 테마코드를 셋팅한다
	 * @param theme_code 테마코드
	 */
	@XmlElement(name="theme_code")
	public void setTheme_code(String theme_code) {
		this.theme_code = theme_code;
	}
	
	/**
	 * 표현테마명을 리턴한다
	 * @return 표현테마명
	 */
	public String getTheme_name() {
		return GlobalCom.isNull(theme_name);
	}
	
	/**
	 * 표현테마명을 셋팅한다
	 * @param theme_name 표현테마명
	 */
	@XmlElement(name="theme_name")
	public void setTheme_name(String theme_name) {
		if(theme_name.split("\\|\\|").length > 1){
			this.theme_name = theme_name.split("\\|\\|")[0];
			setTheme_ename(theme_name.split("\\|\\|")[1]);
		}else{
			this.theme_name = theme_name;
		}
	}

	/**
	 * 표현테마명(영문)을 셋팅한다
	 * @return the theme_ename
	 */
	public String getTheme_ename() {
		return GlobalCom.isNull(theme_ename);
	}

	/**
	 * 표현테마명(영문)을 셋팅한다
	 * @param theme_ename the theme_ename to set
	 */
	@XmlElement(name="theme_ename")
	public void setTheme_ename(String theme_ename) {
		this.theme_ename = theme_ename;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append(getTheme_code());
		sb.append(GlobalCom.colsep);
		sb.append(getTheme_name());
		sb.append(GlobalCom.colsep);
		sb.append(getTheme_ename());
		return sb.toString();
	}

}
