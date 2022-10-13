package com.dmi.smartux.bonbang.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.bonbang.vo.RegistrationIDParamVO;
import com.dmi.smartux.common.dao.CommonDao;

/**
 * RegistrationID 관리 DAO 클래스
 * @author YJ KIM
 *
 */
@Repository
public class RegistrationIDDao extends CommonDao {
	private final Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * RegistrationID 존재여부 확인(STB)
	 * 기존로직에서 merge기능만 제거.
	 * @param pvo
	 * @return
	 * @throws DataAccessException
	 */
	public String getUXRegistrationID(RegistrationIDParamVO pvo) throws DataAccessException {
		
		return (String)getSqlMapClientTemplate().queryForObject("bonbang.getUXRegistrationID",pvo);
	}
	
	/**
	 * RegistrationID 등록(STB)
	 * 기존로직에서 merge기능만 제거.
	 * @param pvo
	 * @throws DataAccessException
	 */
	public void insertUXRegistrationID(RegistrationIDParamVO pvo) throws DataAccessException {

		getSqlMapClientTemplate().insert("bonbang.insertUXRegistrationID",pvo);
	}
	
	/**
	 * RegistrationID 수정(STB)
	 * 기존로직에서 merge기능만 제거.
	 * @param pvo
	 * @throws DataAccessException
	 */
	public void updateUXRegistrationID(RegistrationIDParamVO pvo) throws DataAccessException {

		getSqlMapClientTemplate().update("bonbang.updateUXRegistrationID",pvo);
	}
	
	/**
	 * RegistrationID 존재여부 확인(SMA)
	 * 기존로직에서 merge기능만 제거.
	 * @param pvo
	 * @return
	 * @throws DataAccessException
	 */
	public String getSMARegistrationID(RegistrationIDParamVO pvo) throws DataAccessException {
		
		return (String)getSqlMapClientTemplate().queryForObject("bonbang.getSMARegistrationID",pvo);
	}
	
	/**
	 * RegistrationID 등록(SMA)
	 * 기존로직에서 merge기능만 제거.
	 * @param pvo
	 * @throws DataAccessException
	 */
	public void insertSMARegistrationID(RegistrationIDParamVO pvo) throws DataAccessException {

		getSqlMapClientTemplate().insert("bonbang.insertSMARegistrationID",pvo);
	}
	
	/**
	 * RegistrationID 수정(SMA)
	 * 기존로직에서 merge기능만 제거.
	 * @param pvo
	 * @throws DataAccessException
	 */
	public void updateSMARegistrationID(RegistrationIDParamVO pvo) throws DataAccessException {

		getSqlMapClientTemplate().update("bonbang.updateSMARegistrationID",pvo);
	}

	/**
	 * RegistrationID 삭제 메서드
	 * @param pvo RegistrationIDParamVO 객체
	 */
	public int removeRegistrationID(RegistrationIDParamVO pvo) {
		
		int result = 0;
		
		//STB 페어링 등록
		//130703 타입 로직 변경
		if(pvo.getApp_type().equalsIgnoreCase("sma")){
			result = (Integer)getSqlMapClientTemplate().delete("bonbang.removeSMARegistrationID", pvo);
		//SMA 페어링 등록
		}else{
			result = (Integer)getSqlMapClientTemplate().delete("bonbang.removeUXRegistrationID", pvo);
		}
		return result;
	}

	/**
	 * RegistrationID 목록 조회 메서드 (STB정보 기준으로 자신을 제외한 REG_ID 정보 목록을 추출한다.-Push Noti 시 사용)
	 * @param sa_id			셋탑 가입 번호
	 * @param stb_mac		셋탑 맥주소
	 * @param sma_mac		모바일앱 맥주소
	 * @param app_type		어플 타입
	 * @return				리스트 객체(RegistrationIDParamVO)
	 */
	public List<RegistrationIDParamVO> getRegIDList(String sa_id,
			String stb_mac, String sma_mac, String app_type) {
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("sa_id", sa_id);
		param.put("stb_mac", stb_mac);
		//130703 타입 로직 변경
		if(app_type.equalsIgnoreCase("sma")){
			param.put("sma_mac", sma_mac);
		}else{
			param.put("sma_mac", "");
		}
		param.put("app_type", app_type);
		
		List<RegistrationIDParamVO> result = getSqlMapClientTemplate().queryForList("bonbang.listRegistrationID", param);
		logger.debug("result.size() = " + result.size());
		
		return result;
	}
}
