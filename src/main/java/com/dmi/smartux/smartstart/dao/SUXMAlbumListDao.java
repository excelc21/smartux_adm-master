package com.dmi.smartux.smartstart.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.String;
import java.net.URLDecoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import com.dmi.smartux.admin.rank.vo.EcrmRankVO;

import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.smartstart.vo.SUXMAlbumListVO;

@Repository
public class SUXMAlbumListDao extends CommonDao{

	private final Log logger = LogFactory.getLog(this.getClass()); 
	
	/**
	 * 자체편성을 조회하기 위해 사용되는 정보를 조회하는 DAO 클래스
	 * @return				자체편성 정보가 들어있는 SUXMAlbumListVO 클래스가 들어있는 List 객체
	 * @throws Exception
	 */
	public List<SUXMAlbumListVO> getSUXMAlbumList(String schedule_code) throws Exception {
		
		
		//버전을 업데이트 한다.
		Map<String, String> param2 = new HashMap<String, String>();
		param2.put("category_type", SmartUXProperties.getProperty("Versionitemtype.code4"));
		getSqlMapClientTemplate().update("mainpanel.setMainPanelTitleVersion", param2);
		
		
		Map<String, String> param = new HashMap<String, String>();
				
		List<SUXMAlbumListVO> result = null;
		param.put("schedule_code", schedule_code);
		
		// 자체편성 요청일 경우
		result = getSqlMapClientTemplate().queryForList("suxmalbumlist.getSUXMAlbumList", param);
		
		return result;
	}
	
	/**
	 * 자체편성을 조회하기 위해 사용되는 사전정보를 체크하는 DAO 클래스
	 * @return				자체편성 정보가 들어있는 SUXMAlbumListVO 클래스가 들어있는 List 객체
	 * @throws Exception
	 */
	public List<EcrmRankVO> getCheckSCHEDULEList() throws Exception {
		// TODO Auto-generated method stub
		List<EcrmRankVO> result = null;
		result = getSqlMapClientTemplate().queryForList("suxmalbumlist.getCheckSCHEDULEList");
		
		return result;
	}
	
	/**
	 * 자체편성을 조회하기 위해 사용되는 사전코드정보를 체크하는 DAO 클래스
	 * @return				자체편성 정보가 들어있는 SUXMAlbumListVO 클래스가 들어있는 List 객체
	 * @throws Exception
	 */
	public List<EcrmRankVO> getCheckScheduleCodelist(String schedule_code) throws Exception {
		// TODO Auto-generated method stub
		
		Map<String, String> param = new HashMap<String, String>();
		
		param.put("schedule_code", schedule_code);
		
		List<EcrmRankVO> result = null;
		result = getSqlMapClientTemplate().queryForList("suxmalbumlist.getCheckScheduleCodelist", param);
		
		return result;
	}
	
}

