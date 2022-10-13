package com.dmi.smartux.statbbs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dmi.smartux.common.vo.StatParticipateVo;
import com.dmi.smartux.statbbs.vo.BbsStatListVo;

public interface StatbbsService {

	/**
	 * @param callByScheduler 스케줄러 호출과 관리자의 강제 호출을 구분하기 위한 플래그로 사용한다.<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;관리자 호출 시 A 스케줄러 호출 시 Y
	 * @return 
	 * @throws Exception
	 */
	public List<BbsStatListVo> refreshCacheOfBbsStat(String callByScheduler) throws Exception;
	
	/**
	 * 캐싱되어 있는 참여통계 가져오기
	 * @param stat_no
	 * @return
	 * @throws Exception
	 */
	public BbsStatListVo getBbsStat(String stat_no) throws Exception;
	
	/**
	 * 참여통계 참여 데이터 저장
	 * @param statparticipateVo
	 * @throws Exception
	 */
	public void addBbsStat(StatParticipateVo statparticipateVo, String bbsFilePath) throws Exception;

}
