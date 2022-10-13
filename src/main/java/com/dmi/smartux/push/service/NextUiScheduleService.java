package com.dmi.smartux.push.service;

import java.util.List;

import com.dmi.smartux.push.vo.NextUiPushContentVO;
import com.dmi.smartux.push.vo.NextUiPushUserParamVO;
import com.dmi.smartux.push.vo.NextUiPushUserVO;

public interface NextUiScheduleService {
	/**
	 * 
	 * @throws Exception
	 */
	public List<NextUiPushContentVO> getNextUiPushContentList() throws Exception;
	
	/**
	 * 
	 * @throws Exception
	 */
	public int getNextUiPushUserCount(NextUiPushContentVO param) throws Exception;
	
	/**
	 * 
	 * @throws Exception
	 */
	public List<NextUiPushUserVO> getNextUiPushUserList(NextUiPushUserParamVO param) throws Exception;

	/**
	 * Push후 결과 저장
	 * @param pushcompleteVo
	 * @throws Exception
	 */
	public void updatePushComplete(NextUiPushContentVO pushcompleteVo) throws Exception;
	
	
}
