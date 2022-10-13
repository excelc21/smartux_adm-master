package com.dmi.smartux.noticeinfo.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.noticeinfo.vo.NotiMainCodeListVo;
import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.noticeinfo.vo.CacheNoticeInfoListVo;

@Repository
public class NoticeInfoDao extends CommonDao {
	

	/**
	 * NoticeInfo 캐쉬에 담기
	 * @param service
	 * @return
	 * @throws Exception
	 */
	public List<CacheNoticeInfoListVo> cacheNoticeInfo(String service) throws DataAccessException{
		List<CacheNoticeInfoListVo> result = null;
		result = getSqlMapClientTemplate().queryForList("noticeinfo.cacheNoticeInfoList", service);
		return result;
	}
	
	/**
	 * NoticeInfo 메인 코드 목록
	 * @return
	 * @throws DataAccessException
	 */
	public List<NotiMainCodeListVo> getNotiMainCodeList() throws DataAccessException{
		
		List<NotiMainCodeListVo> result = getSqlMapClientTemplate().queryForList("noticeinfo.getNotiMainCodeList");

		logger.debug("result.size() = " + result.size());
		
		return result;
	}

}
