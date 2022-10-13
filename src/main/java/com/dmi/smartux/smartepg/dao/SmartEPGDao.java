package com.dmi.smartux.smartepg.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.smartepg.vo.RealRatingInfoVO;
import com.dmi.smartux.smartepg.vo.ThemeInfoVO;

@Repository
public class SmartEPGDao extends CommonDao{

	private final Log logger = LogFactory.getLog(this.getClass()); 
	
	/**
	 * 테마 정보 목록 조회
	 * @return				조회된 테마 정보 목록
	 * @throws Exception
	 */
	public List<ThemeInfoVO> getThemeInfo() throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		// 테마코드에 대한 코드는 filepath.code 프로퍼티에 정해져 있는 파일 경로에 있는 프로퍼티 파일에서 code.code1 값을 읽는다
		param.put("code", SmartUXProperties.getProperty("code.code1"));
		
		List<ThemeInfoVO> result = getSqlMapClientTemplate().queryForList("smartepg.getThemeInfo", param);
		return result;
	}
	
	public List<RealRatingInfoVO> getRealRatingByTheme(String theme_code) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("theme_code", theme_code);
		
		List<RealRatingInfoVO> result = getSqlMapClientTemplate().queryForList("smartepg.getRealRatingByTheme", param);
		return result;
	}
	
	/**
	 * 실시간 시청률 목록을 조회
	 * @return		조회된 실시간 시청률 정보 목록
	 * @throws Exception
	 */
	public List<RealRatingInfoVO> getRealRating() throws Exception {
		
		//2019.11.13 IPTV DB 분리 건으로 mainpanel.setMainPanelTitleVersion 쿼리는 CommMimsDao.java 로 이동해야하므로 previewPanelTitleTemp.java로 뺐음 start
		//버전을 업데이트 한다.
		/*Map<String, String> param = new HashMap<String, String>();
		param.put("category_type", SmartUXProperties.getProperty("Versionitemtype.code1"));
		getSqlMapClientTemplate().update("mainpanel.setMainPanelTitleVersion", param);*/
		//2019.11.13 IPTV DB분리 end
		
		List<RealRatingInfoVO> result = getSqlMapClientTemplate().queryForList("smartepg.getRealRating");
		return result;
	}
}
