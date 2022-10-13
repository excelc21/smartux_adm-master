package com.dmi.smartux.admin.code.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.code.vo.CodeItemVO;
import com.dmi.smartux.admin.code.vo.CodeVO;
import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;

@Repository
public class CodeDao extends CommonDao {

	/**
	 * SmartUX 코드 테이블에서 코드의 최대값을 구하는 함수(등록할 때 사용하기 때문에 실제로는 1이 더 증가된 값이 리턴된다)
	 * @return 코드의 최대값
	 * @throws Exception
	 */
	public String getMaxCode() throws Exception{
		String maxcode = (String)(getSqlMapClientTemplate().queryForObject("admin_code.getMaxCode"));
		return "C" + GlobalCom.appendLeftZero(maxcode, 4);
	}
	
	/**
	 * SmartUX 코드 테이블에서 목록 조회
	 * @return	SmartUX 코드 테이블 목록 조회 결과
	 * @throws Exception
	 */
	public List<CodeVO> getCodeList() throws Exception {
		// TODO Auto-generated method stub
		// Smart Start 항목은 검색이 되지 않게끔 해야 한다
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", SmartUXProperties.getProperty("smartstart.config_code"));
		List<CodeVO> result = getSqlMapClientTemplate().queryForList("admin_code.getCodeList", param);
		return result;
	}

	/**
	 * SmartUX ss_gbn 목록 조회
	 * @return smartsmart(ss_gbn) 목록
	 * @throws Exception
	 */
	public List<String> getSmartstartList() throws Exception {
		return getSqlMapClientTemplate().queryForList("admin_code.getSmartstartList");
	}
	
	/**
	 * 지면에서 사용중인 ss_gbn 개수 조회 
	 * CommMimsDao로 이동
	 * @param ss_gbn
	 * @return
	 * @throws Exception
	 */
	/*public int getUseSsgbnCnt(String ss_gbn) throws Exception {
		int count = (Integer)(getSqlMapClientTemplate().queryForObject("admin_code.getUseSsgbnCnt", ss_gbn));
		return count;
	}*/
	
	/**
	 * SmartUX 코드 테이블에서 목록 상세 조회
	 * @param code 		SmartUX 코드 테이블에서 상세 조회를 할 코드값
	 * @return			입력받은 코드값으로 SmartUX 코드 테이블에서 상세 조회를 한 결과
	 * @throws Exception
	 */
	public CodeVO viewCode(String code) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		
		CodeVO result = (CodeVO)(getSqlMapClientTemplate().queryForObject("admin_code.viewCode", param));
		return result;
	}
	
	/**
	 *  SmartUX 코드 테이블에서 입력받은 코드명으로 몇개가 있는지 갯수를 조회
	 * @param code_nm			SmartUX 코드 테이블에서 갯수를 확인할 코드명
	 * @return					SmartUX 코드 테이블에서 입력받은 코드명의 갯수
	 * @throws Exception
	 */
	public int getCodenmCnt(String code_nm) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("code_nm", code_nm);
		
		int count = (Integer)(getSqlMapClientTemplate().queryForObject("admin_code.getCodenmCnt", param));
		return count;
	}

	/**
	 * SmartUX 코드 테이블에 등록 작업 처리
	 * @param code			SmartUX 코드 테이블에 등록할 코드값
	 * @param code_nm		SmartUX 코드 테이블에 등록할 코드명
	 * @param create_id		SmartUX 코드 테이블에 등록하는 사람의 로그인 ID
	 * @throws Exception
	 */
	public void insertCode(String code, String code_nm, String create_id) throws Exception {
		// TODO Auto-generated method stub
		// code 변수에 값이 아무것도 없을 경우는 신규로 입력하는 것이므로 getMaxCode() 값을 가져와야 한다
		// code 변수에 값이 있을 경우엔 입력된 코드로 입력하도록 한다
		if(code.equals("")){
			code = getMaxCode();
		}
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		param.put("code_nm", code_nm);
		param.put("create_id", create_id);
		param.put("update_id", create_id);
		
		getSqlMapClientTemplate().insert("admin_code.insertCode", param);
	}

	/**
	 * SmartUX 코드 테이블에 수정 작업 처리
	 * @param code			SmartUX 테이블에서 수정할 코드의 코드값
	 * @param code_nm		SmartUX 테이블에서 수정할 코드의 코드명
	 * @param update_id		SmartUX 코드 테이블에 수정하는 사람의 로그인 ID
	 * @return				SmartUX 테이블에 수정된 레코드 갯수
	 * @throws Exception
	 */
	public int updateCode(String code, String code_nm, String update_id) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		param.put("code_nm", code_nm);
		param.put("update_id", update_id);
		
		int result = getSqlMapClientTemplate().update("admin_code.updateCode", param);
		return result;
	}

	/**
	 * SmartUX 코드 테이블에 삭제작업 처리
	 * @param code			SmartUX 테이블에서 삭제할 코드의 코드값
	 * @return				SmartUX 테이블에서 삭제된 레코드 갯수
	 * @throws Exception
	 */
	public int deleteCode(String code) throws Exception {
		// TODO Auto-generated method stub
		
		int result = getSqlMapClientTemplate().delete("admin_code.deleteCode", code);
		return result;
	}
	
	/**
	 * 입력받은 코드로 코드명과 아이템 코드의 갯수를 조회하는 함수
	 * @param code			코드명과 아이템 코드의 갯수를 조회할 코드 
	 * @return				코드명과 갯수가 담겨있는 VO
	 * @throws Exception
	 */
	public CodeVO checkItemCode(String code) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		
		CodeVO result = (CodeVO)(getSqlMapClientTemplate().queryForObject("admin_code.checkItemCode", param));
		return result;
	}
	
	
	// PT_UX_ITEM_CODE

	/**
	 * SmartUX 코드값 테이블에서 입력받은 코드에 대한 목록을 조회하는 함수
	 * @param code				SmartUX 코드값 테이블에서 조회하고자 하는 code
	 * @return					입력받은 code로 조회된 SmartUX 코드값 테이블 목록
	 * @throws Exception
	 */
	public List<CodeItemVO> getCodeItemList(String code) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		
		List<CodeItemVO> result = getSqlMapClientTemplate().queryForList("admin_code.getCodeItemList", param);
		return result;
	}

	/**
	 * SmartUX 코드값 테이블에서 입력받은 코드와 아이템 코드로 상세 조회하는 함수 
	 * @param code			SmartUX 코드값 테이블에서 상세조회 하고자 하는 코드
	 * @param item_code		SmartUX 코드값 테이블에서 상세조회 하고자 하는 아이템 코드
	 * @return				입력받은 code와 item_code를 이용하여 조회된 SmartUX 코드값 테이블 상세 조회
	 * @throws Exception
	 */
	public CodeItemVO viewCodeItem(String code, String item_code) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		param.put("item_code", item_code);
		
		CodeItemVO result = (CodeItemVO)(getSqlMapClientTemplate().queryForObject("admin_code.viewCodeItem", param));
		return result;
	}

	/**
	 * 입력받은 코드와 아이템 코드로 SmartUX 코드값 테이블에서 해당 갯수 조회
	 * @param code			SmartUX 코드값 테이블에서 갯수를 조회하기 위해 사용되는 코드 
	 * @param item_code		SmartUX 코드값 갯수를 조회하기 위해 사용되는 아이템 코드
	 * @return				입력받은 code와 item_code로 SmartUX 코드값 테이블에서 갯수를 조회한 결과값
	 * @throws Exception
	 */
	public int getCodeItemcodeCnt(String code, String item_code) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		param.put("item_code", item_code);
		
		int count = (Integer)(getSqlMapClientTemplate().queryForObject("admin_code.getCodeItemCnt", param));
		
		return count;
	}

	/**
	 * 입력받은 코드와 아이템명로 SmartUX 코드값 테이블에서 해당 갯수 조회
	 * @param code			SmartUX 코드값 테이블에서 갯수를 조회하기 위해 사용되는 코드 
	 * @param item_nm		SmartUX 코드값 갯수를 조회하기 위해 사용되는 아이템명
	 * @return				입력받은 code와 item_nm으로 SmartUX 코드값 테이블에서 갯수를 조회한 결과값
	 * @throws Exception
	 */
	public int getCodeItemnmCnt(String code, String item_nm) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		param.put("item_nm", item_nm);
		
		int count = (Integer)(getSqlMapClientTemplate().queryForObject("admin_code.getCodeItemNmCnt", param));
		
		return count;
	}
	
	/**
	 * 입력받은 ss_gbn으로 SmartUX 타입의 갯수 조회
	 * @param ss_gbn		스마트스타트 구분
	 * @return				입력받은 ss_gbn으로 갯수를 조회한 결과값
	 * @throws Exception
	 */
	public int getCodeItemssgbnCnt(String ss_gbn) throws Exception {
		int count = (Integer)(getSqlMapClientTemplate().queryForObject("admin_code.getCodeItemssgbnCnt", ss_gbn));
		return count;
	}

	/**
	 * SmartUX 코드값 테이블 등록 처리
	 * @param code				SmartUX 코드값 테이블에 등록할 코드
	 * @param item_code			SmartUX 코드값 테이블에 등록할 아이템 코드
	 * @param item_nm			SmartUX 코드값 테이블에 등록할 아이템명
	 * @param ss_gbn			Smart Start 관련 코드 아이템 등록할때 들어가지는 스마트스타트 구분 코드
	 * @param app_type			설정 정보 코드 아이템 등록할때 들어가지는 어플타입 코드 
	 * @param use_yn			SmartUX 코드값 테이블에 등록할때 해당 아이템코드의 사용여부(Y/N)
	 * @param create_id			관리자 사이트에 로그인 한 사람의 로그인 id	
	 * @throws Exception
	 */
	public void insertCodeItem(String code, String item_code, String item_nm, String ss_gbn, String app_type, String use_yn, String create_id) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		param.put("item_code", item_code);
		param.put("item_nm", item_nm);
		param.put("ss_gbn", ss_gbn);
		param.put("app_type", app_type);
		param.put("use_yn", use_yn);
		param.put("create_id", create_id);
		param.put("update_id", create_id);
		
		getSqlMapClientTemplate().insert("admin_code.insertCodeItem", param);
	}

	/**
	 * SmartUX 코드값 테이블 수정 처리
	 * @param code				SmartUX 코드값 테이블에서 수정하고자 하는 코드
	 * @param item_code			SmartUX 코드값 테이블에서 기존에 사용중인 아이템 코드
	 * @param newItem_code		SmartUX 코드값 테이블에서 새로이 수정될 아이템 코드
	 * @param item_nm			SmartUX 코드값 테이블에서  새로이 수정될 아이템명
	 * @param ss_gbn			Smart Start 관련 코드 아이템 등록할때 들어가지는 스마트스타트 구분 코드
	 * @param app_type			설정 정보 코드 아이템 등록할때 들어가지는 어플타입 코드 
	 * @param use_yn			SmartUX 코드값 테이블에 수정할때 해당 아이템코드의 사용여부(Y/N)
	 * @param update_id			관리자 사이트에 로그인 한 사람의 로그인 id 
	 * @return					수정된 레코드 갯수
	 * @throws Exception
	 */
	public int updateCodeItem(String code, String item_code, String newItem_code, String item_nm, String ss_gbn, String app_type, String use_yn, String update_id) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		param.put("item_code", item_code);
		param.put("newitem_code", newItem_code);
		param.put("item_nm", item_nm);
		
		if(!("".equals(GlobalCom.isNull(ss_gbn)))){
			param.put("update_type", "1");
		}else if(!("".equals(GlobalCom.isNull(app_type)))){
			param.put("update_type", "2");
		}
		
		param.put("ss_gbn", ss_gbn);
		param.put("app_type", app_type);
		
		param.put("use_yn", use_yn);
		param.put("update_id", update_id);
		
		int result = getSqlMapClientTemplate().update("admin_code.updateCodeItem", param);
		return result;
	}

	/**
	 * SmartUX 코드값 테이블 삭제 처리
	 * @param code			SmartUX 코드값 테이블에서 삭제하고자 하는 코드
	 * @param item_code		SmartUX 코드값 테이블에서 삭제하고자 하는 아이템 코드
	 * @return				삭제된 레코드 갯수
	 * @throws Exception
	 */
	public int deleteCodeItem(String code, String item_code) throws Exception {
		// TODO Auto-generated method stub
		CodeItemVO item = new CodeItemVO();
		item.setCode(code);
		item.setItem_code(item_code);
		
		int result = getSqlMapClientTemplate().delete("admin_code.deleteCodeItem", item);
		return result;
	}
	
	/**
	 * SmartUX 코드값 테이블 순서 수정 처리
	 * @param ordered			입력받은 SmartUX 코드값 테이블의 코드와 아이템 코드가 셋팅이 되는 순서값
	 * @param code				SmartUX 코드값 테이블에서 순서를 재지정하고자 하는 코드
	 * @param item_code			SmartUX 코드값 테이블에서 순서를 재지정하고자 하는 아이템 코드
	 * @param update_id			관리자 사이트에 로그인 한 사람의 로그인 id 
	 * @return					수정된 레코드 갯수
	 * @throws Exception
	 */
	public int changeCodeItemOrder(int ordered, String code, String item_code, String update_id) throws Exception {
		CodeItemVO item = new CodeItemVO();
		item.setOrdered(ordered);
		item.setCode(code);
		item.setItem_code(item_code);
		item.setUpdate_id(update_id);
		
		int result = getSqlMapClientTemplate().update("admin_code.changeCodeItemOrder", item);
		return result;
	}
}
