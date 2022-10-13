package com.dmi.smartux.admin.pushconfig.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.pushconfig.vo.PushConfigVo;
import com.dmi.smartux.common.dao.CommonDao;

@Repository
public class PushConfigDao extends CommonDao{

	public PushConfigVo getPushConfig(String service_type) throws DataAccessException{
		
		Map<String, String> param = new HashMap<String,String>();
		param.put("service_type",service_type);
		
		PushConfigVo result = (PushConfigVo) getSqlMapClientTemplate().queryForObject("pushconfig.getPushConfig",param);
		return result;
	}
}
