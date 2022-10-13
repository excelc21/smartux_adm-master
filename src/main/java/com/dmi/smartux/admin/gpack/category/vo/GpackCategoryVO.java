package com.dmi.smartux.admin.gpack.category.vo;

import com.dmi.smartux.common.vo.BaseVO;

public class GpackCategoryVO extends BaseVO {
	
	private String pack_id;			//팩 ID
	private String category_id;		//카테고리 ID
	private String category_nm;		//카테고리 제목
	private String category_comment;//카테고리 멘트
	private String category_yn;		//카테고리여부(Y:카테고리, N:프로모션)
	private String auto_yn;			//자동여부 (Y:IMCS카테고리연동, N:직접편성)
	private String ordered;			//정렬순번
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
	public String getCategory_comment() {
		return category_comment;
	}
	public void setCategory_comment(String category_comment) {
		this.category_comment = category_comment;
	}
	public String getCategory_yn() {
		return category_yn;
	}
	public void setCategory_yn(String category_yn) {
		this.category_yn = category_yn;
	}
	public String getAuto_yn() {
		return auto_yn;
	}
	public void setAuto_yn(String auto_yn) {
		this.auto_yn = auto_yn;
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
