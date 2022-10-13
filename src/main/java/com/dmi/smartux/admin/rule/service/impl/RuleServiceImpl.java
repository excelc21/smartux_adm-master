package com.dmi.smartux.admin.rule.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.rule.dao.RuleDao;
import com.dmi.smartux.admin.rule.service.RuleService;
import com.dmi.smartux.admin.rule.vo.RuleDetailVO;
import com.dmi.smartux.admin.rule.vo.RuleVO;
import com.dmi.smartux.common.util.GlobalCom;

@Service
public class RuleServiceImpl implements RuleService {

	@Autowired
	RuleDao dao;

	@Override
	@Transactional(readOnly=true)
	public List<RuleVO> getRuleList() throws Exception {
		// TODO Auto-generated method stub
		return dao.getRuleList();
	}

	@Override
	@Transactional(readOnly=true)
	public RuleVO viewRule(String rule_code) throws Exception {
		// TODO Auto-generated method stub
		return dao.viewRule(rule_code);
	}
	

	@Override
	@Transactional
	public int updateRule(String rule_code, String rule_name, String update_id) throws Exception {
		// TODO Auto-generated method stub
		return dao.updateRule(rule_code, rule_name, update_id);
	}

	@Override
	@Transactional
	public void deleteRule(String [] rule_codes) throws Exception {
		// TODO Auto-generated method stub
		for(String rule_code : rule_codes){
			dao.deleteRule(rule_code);
			dao.deleteRuleDetail(rule_code);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<RuleDetailVO> getRuleDetailList(String rule_code)
			throws Exception {
		// TODO Auto-generated method stub
		return dao.getRuleDetailList(rule_code);
	}

	/**
	 * VOD 랭킹 룰의 마스터 정보를 등록
	 * @param rule_name			등록하고자 하는 VOD 랭킹 룰의 이름
	 * @param rule_type			등록하고자 하는 VOD 랭킹 룰의 타입(D : 일자별 랭킹, P : 가격별 랭킹, C : 유/무료별 랭킹, G : 장르별 랭킹)
	 * @param create_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return
	 * @throws Exception
	 */
	private long insertRule(String rule_name, String rule_type, String create_id)	throws Exception {
		// TODO Auto-generated method stub
		long result = dao.insertRule(rule_name, rule_type, create_id);
		return result;
	}
	
	/**
	 * VOD 랭킹 룰의 디테일 정보를 등록
	 * @param rule_code			등록하고자 하는 VOD 랭킹 룰의 rule_code
	 * @param rule_type_data1	RULE_TYPE_DATA1
	 * @param rule_type_data2	RULE_TYPE_DATA2
	 * @param weight			가중치
	 * @param create_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @throws Exception
	 */
	private void insertRuleDetail(String rule_code, String rule_type_data1,	String rule_type_data2, String weight, String create_id) throws Exception {
		// TODO Auto-generated method stub
		dao.insertRuleDetail(rule_code, rule_type_data1, rule_type_data2, weight, create_id);
	}
	
	@Override
	@Transactional
	public void insertDayRule(String rule_name, String rule_type,	String[] dweights, String create_id) throws Exception{
		// TODO Auto-generated method stub
		String rule_code = String.valueOf(insertRule(rule_name, rule_type, create_id));
		int idx = 1;
		for(String weight : dweights){
			insertRuleDetail(rule_code, String.valueOf(idx), "", weight, create_id);
			idx++;
		}
	}

	@Override
	@Transactional
	public void insertPriceRule(String rule_name, String rule_type, String[] pstart, String[] pend, String[] pweights, String create_id) throws Exception{
		// TODO Auto-generated method stub
		String rule_code = String.valueOf(insertRule(rule_name, rule_type, create_id));
		int length = pstart.length;
		for(int i=0; i < length; i++){
			insertRuleDetail(rule_code, GlobalCom.isNull(pstart[i]), GlobalCom.isNull(pend[i]), GlobalCom.isNull(pweights[i]), create_id);
		}
	}

	@Override
	@Transactional
	public void insertFreeRule(String rule_name, String rule_type, String cweight, String fweight, String create_id) throws Exception{
		// TODO Auto-generated method stub
		String rule_code = String.valueOf(insertRule(rule_name, rule_type, create_id));
		insertRuleDetail(rule_code, "F", "", fweight, create_id);
		insertRuleDetail(rule_code, "C", "", cweight, create_id);
		
	}

	@Override
	@Transactional
	public void insertGenreRule(String rule_name, String rule_type, String [] hgenre, String [] gweights, String create_id) throws Exception{
		// TODO Auto-generated method stub
		String rule_code = String.valueOf(insertRule(rule_name, rule_type, create_id));
		int length = hgenre.length;
		for(int i=0; i < length; i++){
			insertRuleDetail(rule_code, GlobalCom.isNull(hgenre[i]), "", GlobalCom.isNull(gweights[i]), create_id);
		}
	}
	
	@Override
	@Transactional
	public void insertSeriesRule(String rule_name, String rule_type, String create_id) throws Exception{
		// TODO Auto-generated method stub
		insertRule(rule_name, rule_type, create_id);
	}

	@Override
	@Transactional(readOnly=true)
	public List<String> selectUseRule() throws Exception {
		// TODO Auto-generated method stub
		return dao.selectUseRule();
	}

	@Override
	@Transactional(readOnly=true)
	public List<RuleVO> getRuleList2(String rule_codes) throws Exception {
		// TODO Auto-generated method stub
		return dao.getRuleList2(rule_codes);
	}
	
	

}
