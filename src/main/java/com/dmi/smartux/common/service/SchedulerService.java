package com.dmi.smartux.common.service;

public interface SchedulerService {
	/**
	 * 주기적으로 캐쉬 URL을 호출
	 * @throws Exception
	 */
	public void checkCache() throws Exception;
	
	/**
	 * 주기적으로 캐쉬 URL을 호출(실시간시청률 용) - 주기의 텀이 너무 차이가 나서 별로도 구현됨
	 * @throws Exception
	 */
	public void checkCache2nd() throws Exception;
}
