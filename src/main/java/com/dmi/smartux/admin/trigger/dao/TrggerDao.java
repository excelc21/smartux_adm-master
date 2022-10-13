package com.dmi.smartux.admin.trigger.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.code.vo.CodeItemVO;
import com.dmi.smartux.admin.gpack.event.vo.TVChannelVO;
import com.dmi.smartux.common.dao.CommonDao;

@Repository
public class TrggerDao extends CommonDao{
	/**
	 * TV 채널 목록 조회
	 * @param channelVO		조회 조건 정보
	 * @return 조회된 TV 채널 목록
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TVChannelVO> getTVChannelList(TVChannelVO channelVO) throws Exception {
		return (List<TVChannelVO>)(getSqlMapClientTemplate().queryForList("admin_trigger.getTVChannelList", channelVO));
	}
	
	/**
	 * TV 채널 목록 조회
	 * @param channelVO		조회 조건 정보
	 * @return 조회된 TV 채널 목록
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public int getTVChannelCount(TVChannelVO channelVO) throws Exception {
		return (Integer)(getSqlMapClientTemplate().queryForObject("admin_trigger.getTVChannelCount", channelVO));
	}

	/**
	 * 코드 조회
	 * @param itemCode
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public CodeItemVO getCodeItem(String itemCode) throws Exception {
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("item_code", itemCode);
		
		return (CodeItemVO)(getSqlMapClientTemplate().queryForObject("admin_trigger.getCodeItem", param));
	}
	
	/**
	 * 코드 수정
	 * @param itemCode
	 * @param itemNm
	 * @param updateId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public int updateCodeItem(String itemCode, String itemNm, String updateId) throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put("item_code", itemCode);
		param.put("item_nm", itemNm);
		param.put("update_id", updateId);
		
		return getSqlMapClientTemplate().update("admin_trigger.updateCodeItem", param);
	}
}
