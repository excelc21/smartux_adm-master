package com.dmi.smartux.admin.rule.vo;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

public class RuleTypeVO extends BaseVO {
	String rule_type;				// rule_type 코드
	String rule_type_name;			// rule_type 명칭
	
	public String getRule_type() {
		return GlobalCom.isNull(rule_type);
	}
	public void setRule_type(String rule_type) {
		this.rule_type = rule_type;
	}
	public String getRule_type_name() {
		return GlobalCom.isNull(rule_type_name);
	}
	public void setRule_type_name(String rule_type_name) {
		this.rule_type_name = rule_type_name;
	}
}
