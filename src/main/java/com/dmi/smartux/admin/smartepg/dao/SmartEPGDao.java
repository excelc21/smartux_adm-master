package com.dmi.smartux.admin.smartepg.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.smartepg.vo.ThemeInfoVO;
import com.dmi.smartux.common.dao.CommonDao;

@Repository("adminSmartEPGDao")
public class SmartEPGDao extends CommonDao{
	
	/**
	 * 테마 정보 목록을 가져온다
	 * @return				검색된 테마 목록				
	 * @throws Exception
	 */
	public List<ThemeInfoVO> getThemeInfoList() throws Exception {
		// TODO Auto-generated method stub
		List<ThemeInfoVO> result = getSqlMapClientTemplate().queryForList("admin_smartepg.getThemeInfo");
		return result;
	}
	
	/**
	 * 테마 정보를 등록한다
	 * @param theme_code		테마코드
	 * @param theme_name		테마명
	 * @param use_yn			사용여부(Y/N)
	 * @param create_id			등록한 관리자 id
	 * @throws Exception
	 */
	public void insertThemeInfoProc(String theme_code, String theme_name, String use_yn, String create_id) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("theme_code", theme_code);
		param.put("theme_name", theme_name);
		param.put("use_yn", use_yn);
		param.put("create_id", create_id);
		
		getSqlMapClientTemplate().insert("admin_smartepg.insertThemeInfo", param);
		
	}
	
	/**
	 * 테마 정보를 조회한다
	 * @param theme_code		테마코드
	 * @return					검색된 테마 정보
	 * @throws Exception
	 */
	public ThemeInfoVO viewThemeInfo(String theme_code) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("theme_code", theme_code);
		
		ThemeInfoVO result = (ThemeInfoVO)(getSqlMapClientTemplate().queryForObject("admin_smartepg.viewThemeInfo", param));
		return result;
	}

	/**
	 * 테마 정보를 수정한다
	 * @param theme_code		테마코드
	 * @param theme_name		테마명
	 * @param use_yn			사용여부(Y/N)
	 * @param update_id			수정한 관리자 id
	 * @return					수정된 테마 정보 갯수(1개만 수정하도록 Query가 되어 있기 때문에 이것이 1이 아닐 경우 문제가 있는 것이다)
	 * @throws Exception
	 */
	public int updateThemeInfoProc(String theme_code, String theme_name, String use_yn, String update_id) throws Exception {
		// TODO Auto-generated method stub
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("theme_code", theme_code);
		param.put("theme_name", theme_name);
		param.put("use_yn", use_yn);
		param.put("create_id", update_id);
		
		int result = getSqlMapClientTemplate().update("admin_smartepg.updateThemeInfo", param);
		return result;
	}

	/**
	 * 테마 정보를 삭제한다
	 * @param theme_code		테마코드
	 * @return					삭제돤 테마 정보 갯수(1개만 삭제하도록 Query가 되어 있기 때문에 이것이 1이 아닐 경우 문제가 있는 것이다)
	 * @throws Exception
	 */
	public int deleteThemeInfoProc(String theme_code) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("theme_code", theme_code);
		
		int result = getSqlMapClientTemplate().delete("admin_smartepg.deleteThemeInfo", param);
		return result;
	}

	/**
	 * 테마 정보의 순서를 수정한다
	 * @param theme_code		테마코드
	 * @param seq				순서
	 * @return					수정된 테마 정보 갯수(1개만 수정하도록 Query가 되어 있기 때문에 이것이 1이 아닐 경우 문제가 있는 것이다)
	 * @throws Exception
	 */
	public int orderThemeInfoProc(String theme_code, String seq) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("theme_code", theme_code);
		param.put("seq", seq);
		
		int result =getSqlMapClientTemplate().update("admin_smartepg.updateThemeInfoSeq", param);
		return result;
	}

}
