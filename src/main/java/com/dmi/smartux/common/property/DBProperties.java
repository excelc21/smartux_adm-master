package com.dmi.smartux.common.property;

import java.util.Properties;

public class DBProperties extends Properties {
	private static final long serialVersionUID = -3418021230641873252L;
	
	/**
	 * 서버별 이미지서버 URL을 분산하기(장비별 이미지서버 IP를 다르게 설정) 위해 특정 장비가 DB질의하여 나머지 장비에 동기화 시킬 경우
	 * 이미지서버 IP부분을 해당 String으로 동기화 하게되며 동기화 받는 쪽에서 해당 문자열을 이미지서버IP로 치환한다.
	 */
	public static final String ImgSvFilterStr = "_ImgSvFilterString_";
	/**
	 * 장비별 이미지서버 IP 분리
	 */
	public static final String ImgServerNo = System.getProperty("img.server.no", "0");
	/**
	 * 이미지서버 IP만 구할 때 코드 값
	 */
	public static final String ImgServerIpCode = "O";

	/**
	 * 이미지서버 IP만 구할 때 코드 값
	 */
	public static final String ImgServerIpCodeMims = "I";
	
	/**
	 * 전역변수 지정
	 */
	public DBProperties(){
		super.setProperty("P.ImgSv.No", ImgServerNo);
		super.setProperty("P.ImgSvFilter.Str", ImgSvFilterStr);
		super.setProperty("P.ImgSvIp.Code", ImgServerIpCode);
		super.setProperty("P.ImgSvIp.Code.Mims", ImgServerIpCodeMims);
	}

}
