package com.dmi.smartux.admin.rule.service;

import java.util.List;

import com.dmi.smartux.admin.rule.vo.RuleDetailVO;
import com.dmi.smartux.admin.rule.vo.RuleVO;

public interface RuleService {
	/**
	 * VOD 랭킹 룰 목록 조회
	 * @return		VOD 랭킹 룰 목록
	 * @throws Exception
	 */
	public List<RuleVO> getRuleList() throws Exception;
	
	/**
	 * VOD 랭킹 룰 상세조회
	 * @param rule_code		상세조회 하고자 하는 rule_code
	 * @return				상세조회된 VOD 랭킹 룰 정보
	 * @throws Exception
	 */
	public RuleVO viewRule(String rule_code) throws Exception;
	
	/**
	 * 일자별 랭킹 등록
	 * @param rule_name		VOD 랭킹 룰 이름
	 * @param rule_type		VOD 랭킹 룰 타입(D : 일자별 랭킹, P : 가격별 랭킹, C : 유/무료별 랭킹, G : 장르별 랭킹)
	 * @param dweights		일자별 가중치 값들 배열
	 * @param create_id		관리자 사이트에 로그인 한 사람의 로그인 id
	 * @throws Exception
	 */
	public void insertDayRule(String rule_name, String rule_type, String [] dweights, String create_id) throws Exception;
	
	/**
	 * 가격별 랭킹 등록
	 * @param rule_name		VOD 랭킹 룰 이름
	 * @param rule_type		VOD 랭킹 룰 타입(D : 일자별 랭킹, P : 가격별 랭킹, C : 유/무료별 랭킹, G : 장르별 랭킹)
	 * @param pstart		시작 가격 배열
	 * @param pend			끝 가격 배열
	 * @param pweights		가격대별 가중치 값들 배열
	 * @param create_id		관리자 사이트에 로그인 한 사람의 로그인 id
	 * @throws Exception
	 */
	public void insertPriceRule(String rule_name, String rule_type, String [] pstart, String [] pend, String [] pweights, String create_id) throws Exception;
	
	/**
	 * 유/무료별 랭킹 등록
	 * @param rule_name		VOD 랭킹 룰 이름
	 * @param rule_type		VOD 랭킹 룰 타입(D : 일자별 랭킹, P : 가격별 랭킹, C : 유/무료별 랭킹, G : 장르별 랭킹)
	 * @param cweight		유료 가중치
	 * @param fweight		무료 가중치
	 * @param create_id		관리자 사이트에 로그인 한 사람의 로그인 id
	 * @throws Exception
	 */
	public void insertFreeRule(String rule_name, String rule_type, String cweight, String fweight, String create_id) throws Exception;
	
	/**
	 * 장르별 랭킹 등록
	 * @param rule_name		VOD 랭킹 룰 이름
	 * @param rule_type		VOD 랭킹 룰 타입(D : 일자별 랭킹, P : 가격별 랭킹, C : 유/무료별 랭킹, G : 장르별 랭킹)
	 * @param hgenre		장르코드 배열
	 * @param gweights		장르에 따른 가중치 값들 배열
	 * @param create_id		관리자 사이트에 로그인 한 사람의 로그인 id
	 * @throws Exception
	 */
	public void insertGenreRule(String rule_name, String rule_type, String [] hgenre, String [] gweights, String create_id) throws Exception;
	
	/**
	 * 시리즈별 랭킹 등록
	 * @param rule_name		VOD 랭킹 룰 이름
	 * @param rule_type		VOD 랭킹 룰 타입(D : 일자별 랭킹, P : 가격별 랭킹, C : 유/무료별 랭킹, G : 장르별 랭킹, S : 시리즈별 랭킹)
	 * @param create_id		관리자 사이트에 로그인 한 사람의 로그인 id
	 * @throws Exception
	 */
	public void insertSeriesRule(String rule_name, String rule_type, String create_id) throws Exception;
	
	/**
	 * VOD 랭킹 룰 수정 작업
	 * @param rule_code		VOD 랭킹 룰의 rule_code
	 * @param rule_name		VOD 랭킹 룰의 룰 이름
	 * @param update_id		관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return
	 * @throws Exception
	 */
	public int updateRule(String rule_code, String rule_name, String update_id) throws Exception;
	
	/**
	 * VOD 랭킹 룰 삭제 작업
	 * @param rule_codes	삭제하고자 하는 VOD 랭킹 룰의 rule_code 배열
	 * @throws Exception
	 */
	public void deleteRule(String [] rule_codes) throws Exception;
	
	/**
	 * VOD 랭킹 룰의 서브 정보 조회
	 * @param rule_code		서브 정보를 조회하고자 하는 VOD 랭킹 룰의 rule_code
	 * @return				VOD 랭킹 룰의 서브 정보 목록
	 * @throws Exception
	 */
	public List<RuleDetailVO> getRuleDetailList(String rule_code) throws Exception;
	
	/**
	 * 랭킹 데이터에서 현재 사용중인 rule code 값들을 조회
	 * @return	랭킹 데이터에서 사용중인 rule code 값들의 목록
	 * @throws Exception
	 */
	public List<String> selectUseRule() throws Exception;
	
	/**
	 * 현재 사용중인 VOD 랭킹 룰의 정보들을 조회
	 * @param rule_codes	현재 사용중인 VOD 랭킹 룰들의 rule-code 값들이 구분자로 결합되어 있는 문자열
	 * @return				현재 사용중인 VOD 랭킹 룰의 정보들
	 * @throws Exception
	 */
	public List<RuleVO> getRuleList2(String rule_codes) throws Exception;
}
