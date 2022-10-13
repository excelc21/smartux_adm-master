package com.dmi.smartux.admin.startchannel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.code.vo.CodeItemVO;
import com.dmi.smartux.admin.startchannel.dao.StartChannelDao;
import com.dmi.smartux.admin.startchannel.service.StartChannelService;

@Service
public class StartChannelServiceImpl implements StartChannelService{
	
	@Autowired
	StartChannelDao dao;
	
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
