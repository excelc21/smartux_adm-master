package com.dmi.smartux.admin.retry.dao;

import org.springframework.stereotype.Repository;

import com.dmi.smartux.common.dao.CommonDao;

@Repository
public class RetryDao extends CommonDao {
	
	public String setPushGateWayRetry(String code_id) throws Exception{
		String version = (String)(getSqlMapClientTemplate().queryForObject("admin_retry.getRetryString",code_id));
		return version;
	}

}
