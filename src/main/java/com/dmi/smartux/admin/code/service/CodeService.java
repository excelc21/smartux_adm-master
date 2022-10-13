package com.dmi.smartux.admin.code.service;

import java.util.List;

import com.dmi.smartux.admin.code.vo.CodeItemVO;
import com.dmi.smartux.admin.code.vo.CodeVO;

public interface CodeService {
	/**
	 * SmartUX 코드 목록 조회
	 * @return	SmartUX 코드 목록
	 * @throws Exception
	 */
	public List<CodeVO> getCodeList() throws Exception;
	
	/**
	 * SmartUX 코드 상세 조회
	 * @param code				SmartUX 코드 상세 조회를 할 코드값
	 * @return					SmartUX 코드 상세 정보
	 * @throws Exception
	 */
	public CodeVO viewCode(String code) throws Exception;
	
	/**
	 * 입력받은 코드명의 갯수를 리턴
	 * @param code_nm			코드명
	 * @return					코드명의 갯수
	 * @throws Exception
	 */
	public int getCodenmCnt(String code_nm) throws Exception;
	
	/**
	 * SmartUX 코드 등록
	 * @param code				코드
	 * @param code_nm			코드명
	 * @param create_id			등록자 로그인 id
	 * @throws Exception
	 */
	public void insertCode(String code, String code_nm, String create_id) throws Exception;
	
	/**
	 * SmartUX 코드 수정
	 * @param code				수정할 SmartUX 코드의 코드
	 * @param code_nm			수정할 SmartUX 코드의 코드명
	 * @param update_id			수정자 로그인 id
	 * @return					수정된 갯수
	 * @throws Exception
	 */
	public int updateCode(String code, String code_nm, String update_id) throws Exception;
	
	/**
	 * SmartUX 코드 삭제
	 * @param codes				삭제할 SmartUX 코드의 코드값 배열
	 * @throws Exception
	 */
	public void deleteCode(String [] codes) throws Exception;
	
	/**
	 * 입력받은 코드에 대해서 아이템 코드가 있는지를 확인하여 있을 경우 예외를 던지도록 한다
	 * @param code				아이템 코드가 있는지를 확인할 코드
	 * @return
	 * @throws Exception
	 */
	public CodeVO checkItemCode(String code) throws Exception;

	
	
	
	
	/**
	 * SmartUX Item 코드값 목록 조회
	 * @param code				SmartUX Code Item을 조회하고자 하는 code
	 * @return					입력받은 code로 조회된 SmartUX Code Item 목록
	 * @throws Exception
	 */
	public List<CodeItemVO> getCodeItemList(String code) throws Exception;
	
	/**
	 * SmartUX ss_gbn 목록 조회
	 * @return smartsmart(ss_gbn) 목록
	 * @throws Exception
	 */
	public List<String> getSmartstartList() throws Exception;
	
	/**
	 * 지면에서 사용중인 ss_gbn 개수 조회 
	 * @param ss_gbn
	 * @return
	 * @throws Exception
	 */
	public int getUseSsgbnCnt(String ss_gbn) throws Exception;
	
	/**
	 * SmartUX Item 코드값 상세 조회 
	 * @param code			SmartUX Code Item을 상세조회 하고자 하는 code
	 * @param item_code		SmartUX Code Item을 상세조회 하고자 하는 item_code
	 * @return				입력받은 code와 item_code를 이용하여 조회된 SmartUX Item 코드값 상세 조회
	 * @throws Exception
	 */
	public CodeItemVO viewCodeItem(String code, String item_code) throws Exception;
	
	/**
	 * 입력받은 code와 item_code로 SmartUX Code Item의 갯수 조회
	 * @param code			SmartUX Code Item 갯수를 조회하기 위해 사용되는 code 
	 * @param item_code		SmartUX Code Item 갯수를 조회하기 위해 사용되는 item_code
	 * @return				입력받은 code와 item_code로 갯수를 조회한 결과값
	 * @throws Exception
	 */
	public int getCodeItemcodeCnt(String code, String item_code) throws Exception;
	
	/**
	 * 입력받은 code와 item_nm으로 SmartUX Code Item의 갯수 조회
	 * @param code			SmartUX Code Item 갯수를 조회하기 위해 사용되는 code 
	 * @param item_nm		SmartUX Code Item 갯수를 조회하기 위해 사용되는 item_nm
	 * @return				입력받은 code와 item_code로 갯수를 조회한 결과값
	 * @throws Exception
	 */
	public int getCodeItemnmCnt(String code, String item_nm) throws Exception;
	
	/**
	 * 입력받은 ss_gbn으로 SmartUX 타입의 갯수 조회
	 * @param ss_gbn		스마트스타트 구분
	 * @return				입력받은 ss_gbn으로 갯수를 조회한 결과값
	 * @throws Exception
	 */
	public int getCodeItemssgbnCnt(String ss_gbn) throws Exception;
	
	/**
	 * SmartUX Item 코드값 등록 처리
	 * @param code				SmartUX Code Item을 등록하고자 하는 code
	 * @param item_code			SmartUX Code Item의 item_code
	 * @param item_nm			SmartUX Code Item의 item_nm
	 * @param ss_gbn			Smart Start 관련 코드 아이템 등록할때 들어가지는 스마트스타트 구분 코드
	 * @param app_type			설정 정보 코드 아이템 등록할때 들어가지는 어플타입 코드 
	 * @param use_yn			SmartUX Item의 사용여부(Y/N)
	 * @param create_id			관리자 사이트에 로그인 한 사람의 로그인 id	
	 * @throws Exception
	 */
	public void insertCodeItem(String code, String item_code, String item_nm, String ss_gbn, String app_type, String use_yn, String create_id) throws Exception;
	
	/**
	 * SmartUX Item 코드값 수정 처리
	 * @param code				SmartUX Code Item을 수정하고자 하는 code
	 * @param item_code			SmartUX Code Item의 기존 item_code
	 * @param newItem_code		SmartUX Code Item의 새로운 item_code
	 * @param item_nm			SmartUX Code Item의 새로운 item_nm	
	 * @param ss_gbn			Smart Start 관련 코드 아이템 등록할때 들어가지는 스마트스타트 구분 코드
	 * @param app_type			설정 정보 코드 아이템 등록할때 들어가지는 어플타입 코드 
	 * @param use_yn			SmartUX Item의 사용여부(Y/N)
	 * @param update_id			관리자 사이트에 로그인 한 사람의 로그인 id 
	 * @return					수정된 레코드 갯수
	 * @throws Exception
	 */
	public int updateCodeItem(String code, String item_code, String newItem_code, String item_nm, String ss_gbn, String app_type, String use_yn, String update_id) throws Exception;
	
	/**
	 * SmartUX Item 코드값 삭제 처리 작업
	 * @param code			SmartUX Code Item을 삭제하고자 하는 code	
	 * @param item_codes	SmartUX Code Item의 삭제하고자 하는 item_code들의 문자열 배열
	 * @throws Exception
	 */
	public void deleteCodeItem(String code, String [] item_codes) throws Exception;
	
	/**
	 * SmartUX Item 순서바꾸기 작업
	 * @param code				순서를 바꾸고자 하는 SmartUX Item의 code 
	 * @param item_codes		순서를 바꾸고자 하는 SmartUX Item의 item_code들의 문자열 배열
	 * @param update_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @throws Exception
	 */
	public void changeCodeItemOrder(String code, String [] item_codes, String update_id) throws Exception;
	
	
	
}
