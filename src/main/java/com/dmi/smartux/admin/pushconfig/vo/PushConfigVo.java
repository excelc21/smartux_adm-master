package com.dmi.smartux.admin.pushconfig.vo;

public class PushConfigVo {
	
	private String service_type;
	private String page_count;
	private String sleep_time;
	private String block_push;
	
	public PushConfigVo(){
		this.service_type = "";
		this.page_count = "";
		this.sleep_time = "";
		this.block_push = "";
	}
	
	public PushConfigVo(String service_type, String page_count, String sleep_time, String block_push){
		this.service_type = service_type;
		this.page_count = page_count;
		this.sleep_time = sleep_time;
		this.block_push = block_push;
	}
	
	public String getService_type() {
		return service_type;
	}
	public void setService_type(String service_type) {
		this.service_type = service_type;
	}
	public String getPage_count() {
		return page_count;
	}
	public void setPage_count(String page_count) {
		this.page_count = page_count;
	}
	public String getSleep_time() {
		return sleep_time;
	}
	public void setSleep_time(String sleep_time) {
		this.sleep_time = sleep_time;
	}
	public String getBlock_push() {
		return block_push;
	}
	public void setBlock_push(String block_push) {
		this.block_push = block_push;
	}
}
