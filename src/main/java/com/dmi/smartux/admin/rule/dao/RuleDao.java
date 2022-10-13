package com.dmi.smartux.admin.rule.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.rule.vo.RuleDetailVO;
import com.dmi.smartux.admin.rule.vo.RuleVO;
import com.dmi.smartux.common.dao.CommonDao;

@Repository
public class RuleDao extends CommonDao {
	
	/**
	 * VOD 랭킹 룰 정보 테이블에서 VOD 랭킹 룰 목록 조회
	 * @return				조회된 VOD 랭킹 룰 목록
	 * @throws Exception
	 */
	public List<RuleVO> getRuleList() throws Exception {
		// TODO Auto-generated method stub
		List<RuleVO> result = getSqlMapClientTemplate().queryForList("admin_rule.getRuleList");
		return result;
	}

	/**
	 * VOD 랭킹 룰 정보 테이블에서 VOD 랭킹 룰 상세조회
	 * @param rule_code		상세조회하고자 하는 VOD 랭킹 룰의 rule_code
	 * @return				상세조회된 VOD 랭킹 룰 정보
	 * @throws Exception
	 */
	public RuleVO viewRule(String rule_code) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("rule_code", rule_code);
		
		RuleVO result = (RuleVO)(getSqlMapClientTemplate().queryForObject("admin_rule.viewRule", param));
		return result;
	}
	
	/**
	 * VOD 랭킹 룰 정보 테이블에 VOD 랭킹 룰 등록
	 * @param rule_name		등록하고자 하는 VOD 랭킹 룰 이름
	 * @param rule_type		등록하고자 하는 VOD 랭킹 룰 타입(D : 일자별 랭킹, P : 가격별 랭킹, C : 유/무료별 랭킹, G : 장르별 랭킹)
	 * @param create_id		관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return
	 * @throws Exception
	 */
	public long insertRule(String rule_name, String rule_type, String create_id) throws Exception {
		// TODO Auto-generated method stub
		
		Map<String, String> param = new HashMap<String, String>();
		// param.put("rule_code", "");
		param.put("rule_name", rule_name);
		param.put("rule_type", rule_type);
		param.put("create_id", create_id);
		
		return (Long)(getSqlMapClientTemplate().insert("admin_rule.insertRule", param));
	}

	/**
	 * VOD 랭킹 룰 정보 테이블에 VOD 랭킹 룰 수정
	 * @param rule_code		수정하고자 하는 VOD 랭킹 룰의 rule_code
	 * @param rule_name		수정하고자 하는 VOD 랭킹 룰 이름
	 * @param update_id		관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return
	 * @throws Exception
	 */
	public int updateRule(String rule_code, String rule_name, String update_id) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("rule_name", rule_name);
		param.put("update_id", update_id);
		param.put("rule_code", rule_code);
		
		int result = getSqlMapClientTemplate().update("admin_rule.updateRule", param);
		return result;
	}

	/**
	 * VOD 랭킹 룰 정보 테이블에서 VOD 랭킹 룰 삭제
	 * @param rule_code		삭제하고자 하는 VOD 랭킹 룰의 rule_code
	 * @return				삭제된 VOD 랭킹 룰 갯수
	 * @throws Exception
	 */
	public int deleteRule(String rule_code) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("rule_code", rule_code);
		int result = getSqlMapClientTemplate().delete("admin_rule.deleteRule", param);
		return result;
	}
	
	/**
	 * VOD 랭킹 룰 상세 테이블에서 VOD 랭킹 룰 서브 데이터 조회
	 * @param rule_code		조회하고자 하는 VOD 랭킹 룰의 rule_code
	 * @return				조회된 VOD 랭킹 룰 서브 데이터 목록
	 * @throws Exception
	 */
	public List<RuleDetailVO> getRuleDetailList(String rule_code) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("rule_code", rule_code);
		List<RuleDetailVO> result = getSqlMapClientTemplate().queryForList("admin_rule.getRuleDetailList", param);
		return result;
	}
	
	/**
	 * VOD 랭킹 룰 상세 테이블에서 VOD 랭킹 률 서브 데이터 등록
	 * @param rule_code			등록하고자 하는 VOD 랭킹 룰 서브데이터가 속하는 VOD 랭킹 룰의 rule_code
	 * @param rule_type_data1	RULE_TYPE_DATA1
	 * @param rule_type_data2	RULE_TYPE_DATA2
	 * @param weight			가중치
	 * @param create_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @throws Exception
	 */
	public void insertRuleDetail(String rule_code, String rule_type_data1, String rule_type_data2, String weight, String create_id) throws Exception {
		// TODO Auto-generated method stub
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("rule_code", rule_code);
		param.put("rule_type_data1", rule_type_data1);
		param.put("rule_type_data2", rule_type_data2);
		param.put("weight", weight);
		param.put("create_id", create_id);
		
		getSqlMapClientTemplate().insert("admin_rule.insertRuleDetail", param);
	}
	
	/**
	 * VOD 랭킹 룰 상세 테이블에서 VOD 랭킹 룰 서브 데이터 삭제
	 * @param rule_code			삭제하고자 하는 VOD 랭킹 룰 서브데이터가 속하는 VOD 랭킹 룰의 rule_code
	 * @return					삭제된 VOD 랭킹 룰 서브 데이터 갯수
	 * @throws Exception
	 */
	public int deleteRuleDetail(String rule_code) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("rule_code", rule_code);
		int result = getSqlMapClientTemplate().delete("admin_rule.deleteRuleDetail", param);
		return result;
	}
	
	/**
	 * VOD 랭킹 DATA 관리 정보 테이블에서 현재 사용중인 rule code 값들을 조회
	 * @return	현재 사용중인 rule_code 값 목록
	 * @throws Exception
	 */
	public List<String> selectUseRule() throws Exception {
		// TODO Auto-generated method stub
		List<String> result = getSqlMapClientTemplate().queryForList("admin_rule.selectUseRule");
		return result;
	}
	
	/**
	 * 현재 사용중인 VOD 랭킹 룰의 정보들을 VOD 랭킹 룰 정보 테이블에서 조회
	 * @param rule_codes	현재 사용중인 VOD 랭킹 룰들의 rule_code 값들이 구분자로 결합되어 있는 문자열
	 * @return				현재 사용중인 VOD 랭킹 룰의 정보들
	 * @throws Exception
	 */
	public List<RuleVO> getRuleList2(String rule_codes) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("rule_codes", rule_codes);
		List<RuleVO> result = getSqlMapClientTemplate().queryForList("admin_rule.getRuleList2", param);
		return result;
	}
}
