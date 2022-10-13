package com.dmi.smartux.common.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapClient;

public class CommonMimsDao extends SqlMapClientDaoSupport{
	@Autowired
    public void setSuperSqlMapClient(SqlMapClient sqlMapClient_mims) {
        super.setSqlMapClient(sqlMapClient_mims);
    }
}
