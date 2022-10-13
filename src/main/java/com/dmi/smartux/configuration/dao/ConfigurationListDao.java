package com.dmi.smartux.configuration.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.configuration.vo.ConfigurationListVO;

@Repository
public class ConfigurationListDao extends CommonDao {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * ConfigurationList 를 조회하기 위해 사용되는 정보를 조회하는 DAO 클래스
	 * @param app_type		어플타입
	 * @return				ConfigurationList 정보가 들어있는 ConfigurationListVO 클래스가 들어있는 List 객체
	 * @throws Exception
	 */
	public List<ConfigurationListVO> getConfigurationList(String app_type) throws DataAccessException {
		Map<String, String> param = new HashMap<String, String>();

		//프로퍼티 파일 경로와 프로퍼티 키를 입력받아 해당 프로퍼티 파일 안에 있는 키에 해당하는 값을 읽어온다
		String config_prop_code = SmartUXProperties.getProperty("code.code3");
		logger.debug("config_prop_code : " + config_prop_code );
		
		param.put("config_prop_code", config_prop_code);
		param.put("app_type", app_type);
		
		@SuppressWarnings("unchecked")
		List<ConfigurationListVO> result = getSqlMapClientTemplate().queryForList("configuration.getConfigurationList", param);
		for(ConfigurationListVO item : result){
			logger.debug("config_id : " 		+ item.getConfig_id());
			logger.debug("config_contents : " 	+ item.getConfig_contents());
		}
		return result;
	}
	
	/**
	 * ConfigurationList 조회하기 위해 사용되는 정보를 사전 체크하는 정보를 조회하는 DAO 클래스
	 * @param app_type		어플타입
	 * @return				ConfigurationList 정보가 들어있는 ConfigurationListVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	public List<ConfigurationListVO> getConfigChecklist(String app_type) throws Exception {
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("app_type", app_type);
		
		//프로퍼티 파일 경로와 프로퍼티 키를 입력받아 해당 프로퍼티 파일 안에 있는 키에 해당하는 값을 읽어온다
		String config_type_code = SmartUXProperties.getProperty("code.code4");
		logger.debug("config_type_code : " + config_type_code );
		
		param.put("config_type_code", config_type_code);	
		
		@SuppressWarnings("unchecked")
		List<ConfigurationListVO> result = getSqlMapClientTemplate().queryForList("configuration.getConfigChecklist", param);
		return result;
	}
}

