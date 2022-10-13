package com.dmi.smartux.admin.schedule.vo;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

public class ScheduleVO extends BaseVO {
	int schedule_code;
	String schedule_name;
	String category_gb;
	String trailer_viewing_type;
	String test_id;
	String variation_id;
	
	public int getSchedule_code() {
		return schedule_code;
	}
	public void setSchedule_code(int schedule_code) {
		this.schedule_code = schedule_code;
	}
	public String getSchedule_name() {
		return GlobalCom.isNull(schedule_name);
	}
	public void setSchedule_name(String schedule_name) {
		this.schedule_name = schedule_name;
	}
	public String getCategory_gb() {
		return category_gb;
	}
	public void setCategory_gb(String category_gb) {
		this.category_gb = category_gb;
	}
	public String getTrailer_viewing_type() {
		return trailer_viewing_type;
	}
	public void setTrailer_viewing_type(String trailer_viewing_type) {
		this.trailer_viewing_type = trailer_viewing_type;
	}
	public String getTest_id() {
		return test_id;
	}
	public void setTest_id(String test_id) {
		this.test_id = test_id;
	}
	public String getVariation_id() {
		return variation_id;
	}
	public void setVariation_id(String variation_id) {
		this.variation_id = variation_id;
	}
}
