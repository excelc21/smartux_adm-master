package com.dmi.smartux.cache.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.code.vo.CodeItemVO;
import com.dmi.smartux.common.dao.CommonDao;

@Repository
public class CacheIntervalDao extends CommonDao {
	private final Log logger = LogFactory.getLog(this.getClass());
	
	public List<CodeItemVO> getCacheInterval() throws DataAccessException{
		logger.debug("############Cache DAO getCacheInterval 실행됨!!!!!!!!!!!##########");
		Map<String, String> param = new HashMap<String, String>();
		param.put("code","A0012");
		param.put("use_yn","Y");
		
		List<CodeItemVO> list = getSqlMapClientTemplate().queryForList("admin_itemlist.getCodeItemUseYNList", param);
		list.size();
		return list;
	}
}
