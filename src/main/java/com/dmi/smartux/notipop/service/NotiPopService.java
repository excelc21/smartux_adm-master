package com.dmi.smartux.notipop.service;


import com.dmi.smartux.notipop.vo.getNotiPopCacheVo;

public interface NotiPopService {
	
	/**
	 * 긴급(비상)/팝업 공지 캐싱
	 * @param callByScheduler
	 * @throws Exception
	 */
	public void refreshCacheOfNotiPop(String callByScheduler) throws Exception;
	
	/**
	 * 캐시 리스트 가져오기
	 * @param version
	 * @param app_gbn
	 * @return
	 * @throws Exception
	 */
	public getNotiPopCacheVo getStartupNoti(String version, String app_gbn, String sa_id) throws Exception;

}