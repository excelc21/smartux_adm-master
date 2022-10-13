package com.dmi.smartux.admin.hotvod.vo;

import java.io.Serializable;

public class HotvodContentIdListVo implements Serializable{
	private static final long serialVersionUID = 5810723872567959254L;
	
	public HotvodContentIdListVo(String content_id){
		this.content_id = content_id;
	}
	
	String content_id;

	public String getContent_id() {
		return content_id;
	}

	public void setContent_id(String content_id) {
		this.content_id = content_id;
	}
	
}
