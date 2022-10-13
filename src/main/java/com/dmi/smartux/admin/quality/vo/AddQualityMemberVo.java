package com.dmi.smartux.admin.quality.vo;

import com.dmi.smartux.common.util.HTMLCleaner;

public class AddQualityMemberVo {

	HTMLCleaner cleaner = new HTMLCleaner();

	private String sa_id;
	private String find_type;
	private String size;
	private String log_type;
	private String log_level;
	
	public String getLog_level() {
		return log_level;
	}
	public void setLog_level(String log_level) {
		this.log_level = cleaner.clean(log_level);
	}
	public String getSa_id() {
		return sa_id;
	}
	public void setSa_id(String sa_id) {
		this.sa_id = cleaner.clean(sa_id);
	}
	public String getFind_type() {
		return find_type;
	}
	public void setFind_type(String find_type) {
		this.find_type = cleaner.clean(find_type);
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = cleaner.clean(size);
	}
	public String getLog_type() {
		return log_type;
	}
	public void setLog_type(String log_type) {
		this.log_type = cleaner.clean(log_type);
	}

}
