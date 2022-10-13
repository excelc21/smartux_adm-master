package com.dmi.smartux.ios_push.dao;

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
public class IOS_PushDao extends CommonDao {

private final Log logger = LogFactory.getLog(this.getClass());
	
	public int getPushRetryCnt() throws DataAccessException{
		logger.debug("############ getPushRetryCnt 실행됨!!!!!!!!!!!##########");
		
		int result = 0;
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("code","A0011");
		param.put("use_yn","Y");
		
		try{
		List<CodeItemVO> list = getSqlMapClientTemplate().queryForList("admin_itemlist.getCodeItemUseYNList", param);
		for(int i=0;i<list.size();i++){
			CodeItemVO data = (CodeItemVO)list.get(i);
				result = Integer.parseInt(data.getItem_nm());
		}
		}catch (Exception e) {
			logger.debug("############ getPushRetryCnt 에러");
		}
		logger.debug("############ getPushRetryCnt result = "+result+" !!!!!!!!!!!##########");
		return result;
	}
}
