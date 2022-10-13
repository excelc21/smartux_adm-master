package com.dmi.smartux.admin.rule.vo;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

public class RuleVO extends BaseVO{
	int rule_code;				// rule 코드
	String rule_name;			// rule 이름
	String rule_type;			// rule 타입
	
	public int getRule_code() {
		return rule_code;
	}
	public void setRule_code(int rule_code) {
		this.rule_code = rule_code;
	}
	public String getRule_name() {
		return GlobalCom.isNull(rule_name);
	}
	public void setRule_name(String rule_name) {
		this.rule_name = rule_name;
	}
	public String getRule_type() {
		return GlobalCom.isNull(rule_type);
	}
	public void setRule_type(String rule_type) {
		this.rule_type = rule_type;
	}
	
	
}
