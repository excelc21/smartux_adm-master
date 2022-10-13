package com.dmi.smartux.admin.common.service.impl;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.dmi.commons.consts.BootstrapProperties;
import com.dmi.commons.model.HttpResult;
import com.dmi.commons.utility.HttpClientUtils;
import com.dmi.smartux.admin.common.service.SmartuxScheduleService;
import com.dmi.smartux.common.util.GlobalCom;

@Service("smartuxScheduleService")
public class SmartuxScheduleServiceImpl implements SmartuxScheduleService{

	private final Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * 자체편성 스케줄
	 */
	@Override
	public void suxmAlbumListSchedule() throws Exception {
		if(GlobalCom.isAdminMyTurn(Calendar.HOUR_OF_DAY)){
			try {
				HttpResult httpResult = HttpClientUtils.sendRequest(
					BootstrapProperties.SERVER_IP,
					BootstrapProperties.SERVER_PORT,
					"/smartux_adm/admin/schedule/activateCache?callByScheduler=Y&schedule_yn=Y",
					"POST");
				logger.info(String.format("자체편성 스케줄 호출 %s", httpResult.getResponseBody()));
			} catch (Exception e) {
				logger.warn(String.format("자체편성 스케줄 호출 실패 : %s", e.getMessage()));
			}
		}
	}

	/**
	 * YOUTUBE 검색어관리 스케줄
	 */
	@Override
	public void youtubeSearchKeywordSchedule() throws Exception {
		if(GlobalCom.isAdminMyTurn(Calendar.HOUR_OF_DAY)){
			try {
				HttpResult httpResult = HttpClientUtils.sendRequest(
					BootstrapProperties.SERVER_IP,
					BootstrapProperties.SERVER_PORT,
					"/smartux_adm/admin/youtube/activateCache?callByScheduler=Y",
					"POST");
				logger.info(String.format("YOUTUBE 검색어관리 스케줄 호출 %s", httpResult.getResponseBody()));
			} catch (Exception e) {
				logger.warn(String.format("YOUTUBE 검색어관리 스케줄 호출 실패 : %s", e.getMessage()));
			}
		}
	}

	/**
	 * 추천이용동의 약관관리 스케줄
	 */
	@Override
	public void ahomeTermsSchedule() throws Exception {
		if(GlobalCom.isAdminMyTurn(Calendar.HOUR_OF_DAY)){
			try {
				HttpResult httpResult = HttpClientUtils.sendRequest(
					BootstrapProperties.SERVER_IP,
					BootstrapProperties.SERVER_PORT,
					"/smartux_adm/admin/terms/termsApplyCache?callByScheduler=Y",
					"POST");
				logger.info(String.format("추천이용동의 약관관리 스케줄 호출 %s", httpResult.getResponseBody()));
			} catch (Exception e) {
				logger.warn(String.format("추천이용동의 약관관리 스케줄 호출 실패 : %s", e.getMessage()));
			}
		}
	}

	/**
	 * 공지/이미지 정보관리 스케줄
	 */
	@Override
	public void noticeInfoSchedule() throws Exception {
		if(GlobalCom.isAdminMyTurn(Calendar.HOUR_OF_DAY)){
			try {
				HttpResult httpResult = HttpClientUtils.sendRequest(
					BootstrapProperties.SERVER_IP,
					BootstrapProperties.SERVER_PORT, 
					"/smartux_adm/admin/noticeinfo/cacheNoticeInfo?callByScheduler=Y",
					"POST");
				logger.info(String.format("공지/이미지 정보관리 스케줄 호출 %s", httpResult.getResponseBody()));
			} catch (Exception e) {
				logger.warn(String.format("공지/이미지 정보관리 스케줄 호출 실패 : %s", e.getMessage()));
			}

			// 공지/이미지 정보관리 스케줄
			try {
				HttpResult httpResult = HttpClientUtils.sendRequest(
					BootstrapProperties.SERVER_IP,
					BootstrapProperties.SERVER_PORT, 
					"/smartux_adm/admin/noticeinfo/cacheCjNoticeInfo?callByScheduler=Y",
					"POST");
				logger.info(String.format("CJ 공지/이미지 정보관리 스케줄 호출 %s", httpResult.getResponseBody()));
			} catch (Exception e) {
				logger.warn(String.format("CJ 공지/이미지 정보관리 스케줄 호출 실패 : %s", e.getMessage()));
			}
		}
	}

	/**
	 * 참여통계 스케줄
	 */
	@Override
	public void statBbsSchedule() throws Exception {
		if(GlobalCom.isAdminMyTurn(Calendar.HOUR_OF_DAY)){
			try {
				HttpResult httpResult = HttpClientUtils.sendRequest(
					BootstrapProperties.SERVER_IP,
					BootstrapProperties.SERVER_PORT,
					"/smartux_adm/admin/statbbs/statbbsApplyCache?callByScheduler=Y",
					"POST");
				logger.info(String.format("참여통계 스케줄 호출 %s", httpResult.getResponseBody()));
			} catch (Exception e) {
				logger.warn(String.format("참여통계 스케줄 호출 실패 : %s", e.getMessage()));
			}
		}
	}

	/**
	 * 메인 스케줄
	 */
	@Override
	public void mainPanelSchedule() throws Exception {
		if(GlobalCom.isAdminMyTurn(Calendar.HOUR_OF_DAY)){
			try {
				HttpResult httpResult = HttpClientUtils.sendRequest(
					BootstrapProperties.SERVER_IP,
					BootstrapProperties.SERVER_PORT,
					"/smartux_adm/admin/mainpanel/applyPanelTitleSchedule?callByScheduler=Y",
					"POST");
				logger.info(String.format("패널 스케줄 호출 %s", httpResult.getResponseBody()));
			} catch (Exception e) {
				logger.warn(String.format("패널 스케줄 호출 실패 : %s", e.getMessage()));
			}

			// CJ 메인 스케줄
			try {
				HttpResult httpResult = HttpClientUtils.sendRequest(
					BootstrapProperties.SERVER_IP,
					BootstrapProperties.SERVER_PORT,
					"/smartux_adm/admin/mainpanel/applyCjPanelTitleSchedule?callByScheduler=Y",
					"POST");
				logger.info(String.format("CJ 패널 스케줄 호출 %s", httpResult.getResponseBody()));
			} catch (Exception e) {
				logger.warn(String.format("CJ 패널 스케줄 호출 실패 : %s", e.getMessage()));
			}
		}
	}

	/**
	 * GENRE VOD BEST 스케줄
	 */
	@Override
	public void genreVodBestSchedule() throws Exception {
		if(GlobalCom.isAdminMyTurn(Calendar.HOUR_OF_DAY)){
			try {
				HttpResult httpResult = HttpClientUtils.sendRequest(
					BootstrapProperties.SERVER_IP,
					BootstrapProperties.SERVER_PORT,
					"/smartux_adm/admin/rank/activateCache?callByScheduler=Y&rank_code=",
					"POST");
				logger.info(String.format("GENRE VOD BEST 스케줄 호출 %s", httpResult.getResponseBody()));
			} catch (Exception e) {
				logger.warn(String.format("GENRE VOD BEST 스케줄 호출 실패 : %s", e.getMessage()));
			}
		}
	}
	
	/**
	 * 설정 스케줄
	 */
	@Override
	public void settingSchedule() throws Exception {
		if(GlobalCom.isAdminMyTurn(Calendar.HOUR_OF_DAY)){
			try {
				HttpResult httpResult = HttpClientUtils.sendRequest(
					BootstrapProperties.SERVER_IP,
					BootstrapProperties.SERVER_PORT,
					"/smartux_adm/admin/code/applyCache?callByScheduler=Y&code=",
					"POST");
				logger.info(String.format("설정 스케줄 호출 %s", httpResult.getResponseBody()));
			} catch (Exception e) {
				logger.warn(String.format("설정 스케줄 호출 실패 : %s", e.getMessage()));
			}
		}
	}

	/**
	 * 긴급(비상) 팝업 스케줄
	 */
	@Override
	public void notiPopSchedule() throws Exception {
		if(GlobalCom.isAdminMyTurn(Calendar.HOUR_OF_DAY)){
			try {
				HttpResult httpResult = HttpClientUtils.sendRequest(
					BootstrapProperties.SERVER_IP,
					BootstrapProperties.SERVER_PORT,
					"/smartux_adm/admin/notipop/notiPopApplyCache?callByScheduler=Y",
					"POST");
				logger.info(String.format("긴급(비상) 팝업 공지 스케줄 호출 %s", httpResult.getResponseBody()));
			} catch (Exception e) {
				logger.warn(String.format("긴급(비상) 팝업 스케줄 호출 실패 : %s", e.getMessage()));
			}
		}
	}

	/**
	 * 품질 단말 스케줄
	 */
	@Override
	public void qualitySchedule() throws Exception {
		if(GlobalCom.isAdminMyTurn(Calendar.HOUR_OF_DAY)){
			try {
				HttpResult httpResult = HttpClientUtils.sendRequest(
					BootstrapProperties.SERVER_IP,
					BootstrapProperties.SERVER_PORT,
					"/smartux_adm/admin/quality/qualityApplyCache?callByScheduler=Y",
					"POST");
				logger.info(String.format("품질 단말 스케줄 호출 %s", httpResult.getResponseBody()));
			} catch (Exception e) {
				logger.warn(String.format("품질 단말 스케줄 호출 실패 : %s", e.getMessage()));
			}
		}		
	}
	
	

}
