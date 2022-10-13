package com.dmi.smartux.push.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.push.vo.NextUiPushContentVO;
import com.dmi.smartux.push.vo.NextUiPushUserParamVO;
import com.dmi.smartux.push.vo.NextUiPushUserVO;

@Repository
public class NextUiPushDao extends CommonDao {
	
	/**
	 * 예약발송 컨텐츠 리스트 조회
	 * @return
	 * @throws DataAccessException
	 */
	public List<NextUiPushContentVO> getNextUiPushContentList() throws DataAccessException{
		List<NextUiPushContentVO> result = null;
		result = getSqlMapClientTemplate().queryForList("admin_nextuipush.getNextUiPushContentList");
		return result;
	}
	
	/**
	 * 예약발송 컨텐츠 별 사용자 리스트 총 건수 조회
	 * CommMimsDao 로 이동
	 * @return
	 * @throws DataAccessException
	 */
/*	public int getNextUiPushUserCount(NextUiPushContentVO param) throws DataAccessException{
		int result = 0;
		result = (Integer)getSqlMapClientTemplate().queryForObject("admin_nextuipush.getNextUiPushUserCount", param); 
		return result;
	}*/
	
	/**
	 * 예약발송  컨텐츠 별 사용자 리스트 조회
	 * CommMimsDao 로 이동
	 * @param cat_id
	 * @return
	 * @throws DataAccessException
	 */
	/*public List<NextUiPushUserVO> getNextUiPushUserList(NextUiPushUserParamVO param) throws DataAccessException{
		List<NextUiPushUserVO> result = null; 
		result = getSqlMapClientTemplate().queryForList("admin_nextuipush.getNextUiPushUserList", param);
		return result;
	}*/
	
	/**
	 * Push후 결과 저장
	 * @param pushcompleteVo
	 * @throws DataAccessException
	 */
	/*public void updatePushComplete(NextUiPushContentVO pushcompleteVo) throws DataAccessException{
		getSqlMapClientTemplate().update("admin_nextuipush.updatePushComplete", pushcompleteVo);
		
	}*/
	
}
