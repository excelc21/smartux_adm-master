package com.dmi.smartux.bonbang.vo;

/**
 * RegistrationID 파라미터 VO 클래스
 * @author YJ KIM
 *
 */
public class RegistrationIDParamVO {
	private String sa_id;			//셋탑 가입 번호
	private String stb_mac;		//셋탑 맥주소
	private String sma_mac;		//모바일앱 번호
	private String reg_id;			//Registration ID
	private String app_type;		//어플 타입
	private String pushagent_yn;	// 자사/타사 단말 구분(Y:자사 단말 N:타사 단말)
	/**
	 * 셋탑 가입 번호
	 * @return the sa_id
	 */
	public String getSa_id() {
		return sa_id;
	}
	/**
	 * 셋탑 가입 번호
	 * @param sa_id the sa_id to set
	 */
	public void setSa_id(String sa_id) {
		this.sa_id = sa_id;
	}
	/**
	 * 셋탑 맥주소
	 * @return the stb_mac
	 */
	public String getStb_mac() {
		return stb_mac;
	}
	/**
	 * 셋탑 맥주소
	 * @param stb_mac the stb_mac to set
	 */
	public void setStb_mac(String stb_mac) {
		this.stb_mac = stb_mac;
	}
	/**
	 * RegistrationID
	 * @return the reg_id
	 */
	public String getReg_id() {
		return reg_id;
	}
	/**
	 * RegistrationID
	 * @param reg_id the reg_id to set
	 */
	public void setReg_id(String reg_id) {
		this.reg_id = reg_id;
	}
	/**
	 * 어플타입
	 * @return the app_type
	 */
	public String getApp_type() {
		return app_type;
	}
	/**
	 * 어플타입
	 * @param app_type the app_type to set
	 */
	public void setApp_type(String app_type) {
		this.app_type = app_type;
	}
	/**
	 * 모바일앱 번호
	 * @return the sma_mac
	 */
	public String getSma_mac() {
		return sma_mac;
	}
	/**
	 * 모바일앱 번호
	 * @param sma_mac the sma_mac to set
	 */
	public void setSma_mac(String sma_mac) {
		this.sma_mac = sma_mac;
	}
	/**
	 * 자사/타사 단말 구분(Y:자사 단말 N:타사 단말)
	 * @return the pushagent_yn
	 */
	public String getPushagent_yn() {
		return pushagent_yn;
	}
	/**
	 * 자사/타사 단말 구분(Y:자사 단말 N:타사 단말)
	 * @param pushagent_yn the pushagent_yn to set
	 */
	public void setPushagent_yn(String pushagent_yn) {
		this.pushagent_yn = pushagent_yn;
	}
	
	
	
}
