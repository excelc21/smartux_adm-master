package com.dmi.smartux.admin.login.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.login.vo.LoginVO;
import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.common.util.GlobalCom;

/**
 * 관리자 계정 관리 DAO 클래스
 * @author YJ KIM
 *
 */
@Repository
public class LoginDao extends CommonDao {
	private final Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * 관리자 계정 존재 여부 확인
	 * @param id	아이디
	 * @return		결과코드
	 * @throws DataAccessException
	 */
	public int ctnAdminData(String id) throws DataAccessException{
		int result = 0;
		Map<String, String> param = new HashMap<String, String>();
		param.put("user_id", id);
		
		result = (Integer)getSqlMapClientTemplate().queryForObject("login.cntAdminData", param); 
		
		logger.debug("ctnAdminData result = "+result);
		return result;
	}
	
	/**
	 * 관리자 계정 정보 조회
	 * @param id	아이디
	 * @return	관리자 계정 정보
	 * @throws DataAccessException
	 */
	public LoginVO getAdminData(String id) throws DataAccessException{
		LoginVO loginVO = null;
		Map<String, String> param = new HashMap<String, String>();
		param.put("user_id", id);
		
		loginVO = (LoginVO)getSqlMapClientTemplate().queryForObject("login.getAdminData", param);
		
		return loginVO;
	}
	
	/**
	 * 관리자 로그인 실패 
	 * @param id	아이디
	 * @throws DataAccessException
	 */
	public void setAdminLoginFail(String id) throws DataAccessException{
		Map<String, String> param = new HashMap<String, String>();
		param.put("user_id", id);
		
		getSqlMapClientTemplate().update("login.setAdminLoginFail", param);
		
	}

	/**
	 * 관리자 로그인 성공
	 * @param id	아이디
	 * @throws DataAccessException
	 */
	public void setAdminLoginSuccess(String id) throws DataAccessException{
		Map<String, String> param = new HashMap<String, String>();
		param.put("user_id", id);
		
		getSqlMapClientTemplate().update("login.setAdminLoginSuccess", param);
	}
	
	/**
	 * 관리자 계정 만료일 설정	
	 * @param id	아이디
	 * @param pwd	비밀번호
	 * @param expday	만료일
	 * @throws DataAccessException
	 */
	public void setAdminExpDate(String id, String pwd, String expday, String salt) throws DataAccessException{
//		Map<String, String> param = new HashMap<String, String>();
//		param.put("user_id", id);
//		param.put("password", pwd);
//		param.put("exp_date", GlobalCom.getTodayAddDate(GlobalCom.getTodayFormat(),Integer.parseInt(expday)));
//		param.put("expday", expday);
		
		LoginVO param = new LoginVO();
		param.setUser_id(id);
		param.setPassword(pwd);
		param.setSalt_key(salt);
		param.setExp_date(GlobalCom.getTodayAddDate(GlobalCom.getTodayFormat(),Integer.parseInt(expday)));
		param.setExp_day(Integer.parseInt(expday));
		
		getSqlMapClientTemplate().update("login.setAdminExpDate", param);
	}
	
	/**
	 * 관리자 계정 리스트 조회
	 * @param loginVO	관리자 계정 정보 객체
	 * @return		관리자 계정 리스트
	 * @throws DataAccessException
	 */
	public List<LoginVO> getAdminList(LoginVO loginVO) throws DataAccessException{
		
		loginVO.setStart_rnum(loginVO.getPageNum()*loginVO.getPageSize()-loginVO.getPageSize()+1);
		loginVO.setEnd_rnum(loginVO.getStart_rnum()+loginVO.getPageSize()-1);
		
		List<LoginVO> result = getSqlMapClientTemplate().queryForList("login.getAdminList", loginVO);

		logger.debug("result.size() = " + result.size());
		
		return result;
	}

	/**
	 * 관리자 계정 리스트 정보 개수 조회
	 * @param loginVO	관리자 계정 정보 객체
	 * @return	관리자 계정 리스트 개수
	 * @throws DataAccessException
	 */
	public int getAdminListCtn(LoginVO loginVO) throws DataAccessException{
		int result = 0;
		
		result = (Integer)getSqlMapClientTemplate().queryForObject("login.getAdminListCtn", loginVO); 
		
		return result;
	}

	/**
	 * 관리자 계정 정보 입력
	 * @param vo	관리자 계정 정보 객체
	 * @throws DataAccessException
	 */
	public void setAdminInsert(LoginVO vo) throws DataAccessException{
		
		vo.setExp_date(GlobalCom.getTodayAddDate(GlobalCom.getTodayFormat(),vo.getExp_day()));
		
		getSqlMapClientTemplate().insert("login.setAdminInsert", vo);
	}
	
	/**
	 * 관리자 계정 정보 수정
	 * @param vo	관리자 계정 정보 객체
	 * @throws DataAccessException
	 */
	public void setAdminUpdate(LoginVO vo)  throws DataAccessException{
		getSqlMapClientTemplate().update("login.setAdminUpdate", vo);
	}

	/**
	 * 관리자 계정 정보 삭제
	 * @param id	아이디
	 * @throws DataAccessException
	 */
	public void setAdminDelete(String id)  throws DataAccessException{
		Map<String, String> param = new HashMap<String, String>();
		param.put("user_id", id);
		
		getSqlMapClientTemplate().delete("login.setAdminDelete", param);
	}

	public void setSuperAdminAuthinit() {
		getSqlMapClientTemplate().update("login.setSuperAdminAuthinit");
	}
	
	public String getCodeItem(String code, String item_code_1, String item_code_2) throws DataAccessException{
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		param.put("item_code_1", item_code_1);
		param.put("item_code_2", item_code_2);

		String result = (String) getSqlMapClientTemplate().queryForObject("login.getCodeItem", param);

		return result;
	}
	
	public String getCodeItemNm(String code, String item_code) throws DataAccessException{
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		param.put("item_code", item_code);

		String result = (String) getSqlMapClientTemplate().queryForObject("login.getCodeItemNm", param);

		return result;
	}

}
