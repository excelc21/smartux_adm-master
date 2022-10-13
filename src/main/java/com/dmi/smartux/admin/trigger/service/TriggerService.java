package com.dmi.smartux.admin.trigger.service;

import java.util.List;

import com.dmi.smartux.admin.code.vo.CodeItemVO;
import com.dmi.smartux.admin.gpack.event.vo.TVChannelVO;

public interface TriggerService {
	/**
	 * TV 채널 목록 조회
	 * @param channelVO
	 * @return
	 * @throws Exception
	 */
	public List<TVChannelVO> getTVChannelList(TVChannelVO channelVO) throws Exception;
	
	/**
	 * TV 채널 목록 카운트
	 * @param channelVO
	 * @return
	 * @throws Exception
	 */
	public int getTVChannelCount(TVChannelVO channelVO) throws Exception;
	
	/**
	 * 코드 조회
	 * @param itemCode
	 * @return
	 * @throws Exception
	 */
	public CodeItemVO getCodeItem(String itemCode) throws Exception;
	
	/**
	 * 코드 수정
	 * @param itemCode
	 * @param itemNm
	 * @param updateId
	 * @return
	 * @throws Exception
	 */
	public int updateCodeItem(String itemCode, String itemNm, String updateId) throws Exception;
}
