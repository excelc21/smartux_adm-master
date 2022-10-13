package com.dmi.smartux.admin.startchannel.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.code.vo.CodeItemVO;
import com.dmi.smartux.common.dao.CommonDao;

@Repository
public class StartChannelDao extends CommonDao{

	/**
	 * 코드 조회
	 * @param itemCode
	 * @return
	 * @throws Exception
	 */
	public CodeItemVO getCodeItem(String itemCode) throws Exception {
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("item_code", itemCode);
		
		return (CodeItemVO)(getSqlMapClientTemplate().queryForObject("admin_startchannel.getCodeItem", param));
	}
	
	/**
	 * 코드 수정
	 * @param itemCode
	 * @param itemNm
	 * @param updateId
	 * @return
	 * @throws Exception
	 */
	public int updateCodeItem(String itemCode, String itemNm, String updateId) throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put("item_code", itemCode);
		param.put("item_nm", itemNm);
		param.put("update_id", updateId);
		
		return getSqlMapClientTemplate().update("admin_startchannel.updateCodeItem", param);
	}
}
