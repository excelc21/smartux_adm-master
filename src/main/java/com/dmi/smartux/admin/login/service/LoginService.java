package com.dmi.smartux.admin.login.service;

import java.util.List;

import com.dmi.smartux.admin.login.vo.AdminMenuVO;
import com.dmi.smartux.admin.login.vo.LoginVO;

/**
 * 관리자 계정 관리 서비스 인터페이스
 * @author YJ KIM
 *
 */
public interface LoginService {

	/**
	 * 로그인 기능
	 * @param id	아이디
	 * @param pwd	비밀번호
	 * @return		결과 코드
	 * @throws Exception
	 */
	public String setLogin(String id, String pwd) throws Exception;
	
	
	/**
	 * 만료일 해제 기능
	 * @param id		아이디
	 * @param pwd		비밀번호
	 * @param expdate	만료일
	 * @return			결과 코드
	 * @throws Exception
	 */
	public String setExpDate(String id,String pwd,String expday,String salt) throws Exception;

	/**
	 * 관리자 계정 리스트 조회 기능 
	 * @param loginVO	관리자 계정 정보 객체
	 * @return	관리자 계정 리스트 
	 * @throws Exception
	 */
	public List<LoginVO> getAdminList(LoginVO loginVO) throws Exception;

	/**
	 * 관리자 계정 리스트 개수 조회 기능
	 * @param loginVO	관리자 계정 정보 객체
	 * @return	관리자 계정 리스트 개수
	 */
	public int getAdminListCtn(LoginVO loginVO);

	
	/**
	 * 관리자 잠금 해제 기능
	 * @param id	아이디
	 * @throws Exception
	 */
	public void setLoginFailClear(String id) throws Exception;

	/**
	 * 관리자 계정 생성 기능
	 * @param vo	관리자 계정 정보 객체
	 * @throws Exception
	 */
	public String setAdminInsert(LoginVO vo) throws Exception;


	/**
	 * 관리자 계정 정보 조회 기능
	 * @param vo	관리자 계정 정보 객체
	 * @return	관리자 계정 조회 정보
	 * @throws Exception
	 */
	public LoginVO getAdminView(LoginVO vo) throws Exception;


	/**
	 * 관리자 계정 정보 수정 기능
	 * @param vo	관리자 계정 정보 객체
	 * @throws Exception
	 */
	public void setAdminUpdate(LoginVO vo) throws Exception;

	/**
	 * 관리자 계정 삭제 기능
	 * @param id	관리자 아이디
	 * @throws Exception
	 */
	public void setAdminDelete(String id) throws Exception;


	/**
	 * 슈퍼관리자 권한 초기화(비공개)
	 * @throws Exception
	 */
	public void setSuperAdminAuthinit() throws Exception;
	
	/**
	 * 메뉴리스트
	 * @throws Exception
	 */
	public List<AdminMenuVO> getMenuList(String auth, String strArr) throws Exception;
	
	public String getCodeItem(String code, String item_code_1, String item_code_2) throws Exception;

	public String getCodeItemNm(String code, String item_code) throws Exception;
}
