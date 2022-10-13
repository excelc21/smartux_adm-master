package com.dmi.smartux.admin.smartepg.vo;

public class ThemeInfoVO {

	String theme_code;					// 테마 코드
	String theme_name;					// 테마명
	int seq;							// 정렬순서
	String use_yn;						// 사용 여부
	
	public String getTheme_code() {
		return theme_code;
	}
	public void setTheme_code(String theme_code) {
		this.theme_code = theme_code;
	}
	
	public String getTheme_name() {
		return theme_name;
	}
	public void setTheme_name(String theme_name) {
		this.theme_name = theme_name;
	}
	
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	
	public String getUse_yn() {
		return use_yn;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}
}
