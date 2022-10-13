package com.dmi.smartux.statbbs.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.statbbs.vo.BbsStatListVo;

@Repository
public class StatbbsDao extends CommonDao {

	/**
	 * 쿠폰 리스트 조회
	 * @param couponlistparamvo
	 * @return
	 */
	public List<BbsStatListVo> refreshCacheOfStatBbs() {
		List<BbsStatListVo> list = getSqlMapClientTemplate().queryForList("bbsstat.refreshCacheOfStatBbs");
		
		return list;
	}

}
