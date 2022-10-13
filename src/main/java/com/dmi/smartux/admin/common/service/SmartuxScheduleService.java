package com.dmi.smartux.admin.common.service;

public interface SmartuxScheduleService {
	
	/**
	 * 자체편성 스케줄
	 * @throws Exception
	 */
	public void suxmAlbumListSchedule() throws Exception;
	
	/**
	 * YOUTUBE 검색어관리 스케줄
	 * @throws Exception
	 */
	public void youtubeSearchKeywordSchedule() throws Exception;
	
	/**
	 * 추천이용동의 약관관리 스케줄
	 * @throws Exception
	 */
	public void ahomeTermsSchedule() throws Exception;
	
	/**
	 * 공지/이미지 정보관리 스케줄
	 * @throws Exception
	 */
	public void noticeInfoSchedule() throws Exception;
	
	/**
	 * 참여통계 스케줄
	 * @throws Exception
	 */
	public void statBbsSchedule() throws Exception;
	
	/**
	 * 메인패널 지면 스케줄
	 * @throws Exception
	 */
	public void mainPanelSchedule() throws Exception;
	
	/**
	 * GENRE VOD BEST 스케줄
	 * @throws Exception
	 */
	public void genreVodBestSchedule() throws Exception;
	
	/**
	 *설정 스케줄
	 * @throws Exception
	 */
	public void settingSchedule() throws Exception;
	
	/**
	 * 긴급(비상)공지 스케줄
	 * @throws Exception
	 */
	public void notiPopSchedule() throws Exception;
	
	/**
	 * 품질단말 정보 스케줄
	 * @throws Exception
	 */
	public void qualitySchedule() throws Exception;
	
}
