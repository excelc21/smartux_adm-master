package com.dmi.smartux.admin.secondtv_push.service;

/**
 * Push Schedule Service
 *
 * @author dongho
 */
public interface PushScheduleService {
	/**
	 * 최신회차 스케쥴(1분 단위)
	 *
	 * @throws Exception
	 */
	public void requestLatestPush() throws Exception;

	/**
	 * 새소식 스케쥴(1분 단위)
	 *
	 * @throws Exception
	 */
	public void requestNewsPush() throws Exception;
}
