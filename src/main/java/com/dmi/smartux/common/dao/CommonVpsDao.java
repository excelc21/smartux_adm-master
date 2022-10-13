package com.dmi.smartux.common.dao;

import com.ibatis.sqlmap.client.SqlMapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class CommonVpsDao extends SqlMapClientDaoSupport{
	@Autowired
	public void setSuperSqlMapClient(SqlMapClient sqlMapClient_pvs) {
		super.setSqlMapClient(sqlMapClient_pvs);
	}
}
