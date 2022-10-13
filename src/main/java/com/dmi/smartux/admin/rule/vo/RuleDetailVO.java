package com.dmi.smartux.admin.rule.vo;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

public class RuleDetailVO extends BaseVO {
	int rule_code;				// rule_code
	String rule_type_data1;		// rule 타입 data1
	String rule_type_data2;		// rule 타입 data2
	float weight;				// 가중치
	
	public int getRule_code() {
		return rule_code;
	}
	public void setRule_code(int rule_code) {
		this.rule_code = rule_code;
	}
	public String getRule_type_data1() {
		return GlobalCom.isNull(rule_type_data1);
	}
	public void setRule_type_data1(String rule_type_data1) {
		this.rule_type_data1 = rule_type_data1;
	}
	public String getRule_type_data2() {
		return GlobalCom.isNull(rule_type_data2);
	}
	public void setRule_type_data2(String rule_type_data2) {
		this.rule_type_data2 = rule_type_data2;
	}
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	
	
}
