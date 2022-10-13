package com.dmi.smartux.gpack.pack.service;

public interface GpackPackVersionService {
	
	/**
	 * 팩 버전 정보를 조회
	 * @param sa_id				가입번호
	 * @param stb_mac			맥주소
	 * @param app_type			어플타입(UX : 셋탑, SMA : 스마트폰)
	 * @param callByScheduler	단말에서 호출 했을 경우엔 N, 스케쥴러에서 호출했을땐 Y, 관리자툴에서 조회했을땐 A가 들어간다
	 * @param pack_id			팩ID
	 * @return					팩 버전 정보
	 * @throws Exception
	 */
	public String getPackVersion(String sa_id, String stb_mac, String app_type, String callByScheduler, String pack_id) throws Exception;
	
}
