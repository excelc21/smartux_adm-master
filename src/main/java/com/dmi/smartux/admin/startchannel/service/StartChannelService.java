package com.dmi.smartux.admin.startchannel.service;

import com.dmi.smartux.admin.code.vo.CodeItemVO;

public interface StartChannelService {
	
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
