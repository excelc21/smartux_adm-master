package com.dmi.smartux.admin.quality.vo;

import com.dmi.smartux.common.util.GlobalCom;

public class QualityMemberCacheListVo {
	
	private String sa_id;
	private String log_type;
	
	public String getSa_id() {
		return GlobalCom.isNull(sa_id);
	}
	public void setSa_id(String sa_id) {
		this.sa_id = sa_id;
	}
	public String getLog_type() {
		return GlobalCom.isNull(log_type);
	}
	public void setLog_type(String log_type) {
		this.log_type = log_type;
	}

}
