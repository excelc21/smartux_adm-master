package com.dmi.smartux.smartstart.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.code.vo.CodeItemVO;
import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.smartepg.vo.ThemeInfoVO;
import com.dmi.smartux.smartstart.vo.SmartStartItemListVO;

@Repository
public class SmartStartItemListDao extends CommonDao{

	private final Log logger = LogFactory.getLog(this.getClass()); 	
	/**
	 * SmartStartItemList을 조회하기 위해 사용되는 정보를 조회하는 DAO 클래스
	 * @return				SmartStartItemList 정보가 들어있는 SmartStartItemListVO 클래스가 들어있는 List 객체
	 * @throws Exception
	 */
	public List<SmartStartItemListVO> getSmartStartItemList() throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();	
		
		//String img_url = SmartUXProperties.getProperty("imgserver.weburl") + SmartUXProperties.getProperty("imgserver.imgpath") + "/";
		//param.put("img_url", img_url);
		
		//프로퍼티 파일 경로와 프로퍼티 키를 입력받아 해당 프로퍼티 파일 안에 있는 키에 해당하는 값을 읽어온다
		String config_pannel_code = SmartUXProperties.getProperty("pannel.code6");
		param.put("config_pannel_code", config_pannel_code);	
		
		List<SmartStartItemListVO> result = getSqlMapClientTemplate().queryForList("smartstart_itemlist.getSmartStartItemList", param);
		return result;
	}

	
	
}
