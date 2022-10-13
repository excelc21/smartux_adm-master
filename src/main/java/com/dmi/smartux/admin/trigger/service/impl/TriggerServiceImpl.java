package com.dmi.smartux.admin.trigger.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.code.vo.CodeItemVO;
import com.dmi.smartux.admin.gpack.event.vo.TVChannelVO;
import com.dmi.smartux.admin.trigger.dao.TrggerDao;
import com.dmi.smartux.admin.trigger.service.TriggerService;

@Service
public class TriggerServiceImpl implements TriggerService{
	
	@Autowired
	TrggerDao dao;
	
	/**
	 * TV 채널 목록 조회
	 * @param channelVO		조회 조건 정보
	 * @return 조회된 TV 채널 목록
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public List<TVChannelVO> getTVChannelList(TVChannelVO channelVO) throws Exception {
		return dao.getTVChannelList(channelVO);
	}
	
	/**
	 * TV 채널 목록 카운트
	 * @param channelVO		조회 조건 정보
	 * @return 조회된 TV 채널 개수
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public int getTVChannelCount(TVChannelVO channelVO) throws Exception {
		return dao.getTVChannelCount(channelVO);
	}
	
	/**
	 * 코드 조회
	 * @param itemCode
	 * @return 조회된 코드
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public CodeItemVO getCodeItem(String itemCode) throws Exception {
		return dao.getCodeItem(itemCode);
	}
	
	/**
	 * 코드 수정
	 * @param itemCode
	 * @param itemNm
	 * @param updateId
	 * @return 성공여부
	 * @throws Exception
	 */
	@Override
	public int updateCodeItem(String itemCode, String itemNm, String updateId) throws Exception{
		return dao.updateCodeItem(itemCode, itemNm, updateId);
	}
}
