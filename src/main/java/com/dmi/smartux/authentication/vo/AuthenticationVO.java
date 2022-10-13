package com.dmi.smartux.authentication.vo;

public class AuthenticationVO {

	private	String key_type;	//상용키(S) 테스트키(T)       
	private	String service_id;	//API를 사용 할 서비스 ID(개발자가 신청한 API_ID 의 묶음)
	private	String api_id;      //API ID(API 명칭)    
	private	String access_key;	//API 사용을 인증하기 위한 인증키     
	private	String cp_id;		//개발자 ID/CP ID          
	private	String service_life;   //테스트키 발생시 사용 만료일 표시(YY.MM.DD)
	private	String sessionKey = "";	//연동 SessionKey
	private	String method = "";	//메소드
	private 	String use = ""; //Y:사용   N:미사용
	private String uri = ""; //Y:사용   N:미사용

	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = isNull(uri);
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getKey_type() {
		return key_type;
	}
	public void setKey_type(String key_type) {
		this.key_type = isNull(key_type);
	}
	public String getService_id() {
		return service_id;
	}
	public void setService_id(String service_id) {
		this.service_id = isNull(service_id);
	}
	public String getApi_id() {
		return api_id;
	}
	public void setApi_id(String api_id) {
		this.api_id = isNull(api_id);
	}
	public String getAccess_key() {
		return access_key;
	}
	public void setAccess_key(String access_key) {
		this.access_key = isNull(access_key);
	}
	public String getCp_id() {
		return cp_id;
	}
	public void setCp_id(String cp_id) {
		this.cp_id = isNull(cp_id);
	}
	public String getService_life() {
		return service_life;
	}
	public void setService_life(String service_life) {
		this.service_life = isNull(service_life);
	}
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = isNull(sessionKey);
	}
	
	public String getUse() {
		return use;
	}
	public void setUse(String use) {
		this.use = isNull(use);
	}
	/**
	 * 공백 제거 및 Null을 ""으로 처리
	 * @param str
	 * @return
	 */
	private String isNull(String str) {
		if((str == null) || (str.trim().equals("")) || (str.trim().equalsIgnoreCase("null")) || (str.trim().length() == 0) || (str.equalsIgnoreCase("undefined")))
			return "";
		else
			return str.trim();
	}
}
