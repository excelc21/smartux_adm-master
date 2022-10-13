package com.dmi.smartux.admin.gpack.contents.vo;

import com.dmi.smartux.common.vo.BaseVO;

public class GpackContentsAutoVO extends BaseVO {

	private String pack_id;			//팩 ID
	private String category_id;		//카테고리 ID
	private String category_nm;		//카테고리 제목
	private String auto_set_id;		//자동설정 ID
	private String imcs_category_id;	//IMCS 카테고리 ID
	private String imcs_category_text;	//IMCS 카테고리 제목
	private String ordered;			//순번
	private String use_yn;			//사용여부
	private String created;
	private String updated;
	private String create_id;
	private String update_id;
	
	
	public String getPack_id() {
		return pack_id;
	}
	public void setPack_id(String pack_id) {
		this.pack_id = pack_id;
	}
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getCategory_nm() {
		return category_nm;
	}
	public void setCategory_nm(String category_nm) {
		this.category_nm = category_nm;
	}
	public String getAuto_set_id() {
		return auto_set_id;
	}
	public void setAuto_set_id(String auto_set_id) {
		this.auto_set_id = auto_set_id;
	}
	public String getImcs_category_id() {
		return imcs_category_id;
	}
	public void setImcs_category_id(String imcs_category_id) {
		this.imcs_category_id = imcs_category_id;
	}
	public String getImcs_category_text() {
		return imcs_category_text;
	}
	public void setImcs_category_text(String imcs_category_text) {
		this.imcs_category_text = imcs_category_text;
	}
	public String getOrdered() {
		return ordered;
	}
	public void setOrdered(String ordered) {
		this.ordered = ordered;
	}
	public String getUse_yn() {
		return use_yn;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}
	public String getCreate_id() {
		return create_id;
	}
	public void setCreate_id(String create_id) {
		this.create_id = create_id;
	}
	public String getUpdate_id() {
		return update_id;
	}
	public void setUpdate_id(String update_id) {
		this.update_id = update_id;
	}
	
	
}
