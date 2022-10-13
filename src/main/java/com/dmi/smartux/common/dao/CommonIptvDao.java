package com.dmi.smartux.common.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapClient;

public class CommonIptvDao extends SqlMapClientDaoSupport{
	@Autowired
    public void setSuperSqlMapClient(SqlMapClient sqlMapClient_iptv) {
        super.setSqlMapClient(sqlMapClient_iptv);
    }
}
