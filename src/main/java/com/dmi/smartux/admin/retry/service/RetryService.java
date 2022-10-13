package com.dmi.smartux.admin.retry.service;

public interface RetryService {
	
	/**
	 * PushG/W RETRY 횟수 캐쉬저장
	 * @return
	 */
	public String setPushGateWayRetry(String code_id, String callByScheduler) throws Exception;

}
