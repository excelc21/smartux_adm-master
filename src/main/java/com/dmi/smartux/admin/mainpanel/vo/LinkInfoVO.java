package com.dmi.smartux.admin.mainpanel.vo;

import com.dmi.smartux.common.vo.BaseVO;

public class LinkInfoVO extends BaseVO {
	
	String pairing_type;			
	String parent_ott_cat_id;
	String mims_link_type;
	
	public String getPairing_type() {
		return pairing_type;
	}
	public void setPairing_type(String pairing_type) {
		this.pairing_type = pairing_type;
	}
	public String getParent_ott_cat_id() {
		return parent_ott_cat_id;
	}
	public void setParent_ott_cat_id(String parent_ott_cat_id) {
		this.parent_ott_cat_id = parent_ott_cat_id;
	}
	public String getMims_link_type() {
		return mims_link_type;
	}
	public void setMims_link_type(String mims_link_type) {
		this.mims_link_type = mims_link_type;
	}
	
	
}
