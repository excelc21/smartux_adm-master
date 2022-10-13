package com.dmi.smartux.noti.service;

import java.util.List;

import com.dmi.smartux.noti.vo.BaseNotiVO;
import com.dmi.smartux.noti.vo.NotiIdAndVersionVO;

/**
 * 게시판 관련 Service
 * 
 * @author jch82@naver.com
 */
public interface NotiService {

	/**
	 * bbs_id 에 따른 게시판 버전을 반환한다.
	 * 
	 * @param bbs_id 게시판 아이디
	 * @param callByScheduler 스케줄러 호출과 관리자의 강제 호출을 구분하기 위한 플래그로 사용한다.<br/>
	 *            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;관리자 호출 시 A 스케줄러 호출 시 Y, 단말 호출 시 N 을 사용한다.
	 * @return bbs_id 에 따른 버전 (bbs_id 에 따른 버전이 없을 경우 null 을 리턴한다.) (null 체크 필요)
	 * @throws Exception
	 */
	public String getBBSVersion ( String bbs_id, String callByScheduler ) throws Exception;

	/**
	 * 전체 공지 정보를 캐쉬로 갱신한다.
	 * 
	 * @param callByScheduler 스케줄러 호출과 관리자의 강제 호출을 구분하기 위한 플래그로 사용한다.<br/>
	 *            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;관리자 호출 시 A 스케줄러 호출 시 Y, 단말 호출 시 N 을 사용한다.
	 * @throws Exception
	 */
	public void refreshCacheOfNoti ( String callByScheduler ) throws Exception;

	/**
	 * bbs_id 에 따른 공지 별 캐쉬를 갱신한다.
	 * 
	 * @param bbs_id 게시판 아이디
	 * @param callByScheduler 스케줄러 호출과 관리자의 강제 호출을 구분하기 위한 플래그로 사용한다.<br/>
	 *            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;관리자 호출 시 A 스케줄러 호출 시 Y, 단말 호출 시 N 을 사용한다.
	 * @return bbs_id 에 따른 변경된 게시 내용
	 * @throws Exception
	 */
	public List<BaseNotiVO> refreshCacheOfNoti ( String bbs_id, String callByScheduler ) throws Exception;

	/**
	 * @param callByScheduler 스케줄러 호출과 관리자의 강제 호출을 구분하기 위한 플래그로 사용한다.<br/>
	 *            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;관리자 호출 시 A 스케줄러 호출 시 Y, 단말 호출 시 N 을 사용한다.
	 * @return 게시판 별 아이디와 버전을 캐쉬에 등록 및 반환한다.
	 * @throws Exception
	 */
	public List<NotiIdAndVersionVO> getNotiIdAndVersionList ( String callByScheduler ) throws Exception;

}
