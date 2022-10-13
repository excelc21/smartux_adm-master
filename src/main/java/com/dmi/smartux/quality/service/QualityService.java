package com.dmi.smartux.quality.service;

import java.util.Map;


public interface QualityService {
	
	/**
	 * 품질 대상 단말 캐싱
	 * @param callByScheduler
	 * @throws Exception
	 */
	public void refreshCacheOfQualityMember(String callByScheduler) throws Exception;

	/**
	 * 품질대상 단말 캐싱데이터 가져오기
	 * @return
	 */
	public Map<String, Object> getQualityMemberList();
}
