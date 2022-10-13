package com.dmi.smartux.admin.code.vo;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

public class CodeItemVO extends BaseVO {

	String code;			// 코드
	String item_code;		// 아이템 코드
	String item_nm;			// 아이템명
	String item_enm;		// 아이템영문명
	int ordered;			// 표시순서
	String ss_gbn = "";		// 스마트스타트 구분
	String app_type = "";	// 어플타입(PT_UX_CODE_ITEMS 테이블에서 CODE가 A0010 인 것으로 조회)
	String use_yn;			// 사용여부
	String created;			// 입력일
	String updated;			// 수정일
	String create_id;		// 입력자
	String update_id;		// 수정자
	
	public String getCode() {
		return GlobalCom.isNull(code);
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getItem_code() {
		return GlobalCom.isNull(item_code);
	}
	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}
	public String getItem_nm() {
		return GlobalCom.isNull(item_nm);
	}
	public void setItem_nm(String item_nm) {
		if(item_nm.split("\\|\\|").length > 1){
			this.item_nm = item_nm.split("\\|\\|")[0];
			setItem_enm(item_nm.split("\\|\\|")[1]);
		}else{
			this.item_nm = item_nm;
		}
	}
	public int getOrdered() {
		return ordered;
	}
	public void setOrdered(int ordered) {
		this.ordered = ordered;
	}
	public String getSs_gbn() {
		return GlobalCom.isNull(ss_gbn);
	}
	public void setSs_gbn(String ss_gbn) {
		this.ss_gbn = ss_gbn;
	}
	
	public String getApp_type() {
		return GlobalCom.isNull(app_type);
	}
	public void setApp_type(String app_type) {
		this.app_type = app_type;
	}
	public String getUse_yn() {
		return GlobalCom.isNull(use_yn);
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}
	public String getCreated() {
		return GlobalCom.isNull(created);
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getUpdated() {
		return GlobalCom.isNull(updated);
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}
	public String getCreate_id() {
		return GlobalCom.isNull(create_id);
	}
	public void setCreate_id(String create_id) {
		this.create_id = create_id;
	}
	public String getUpdate_id() {
		return GlobalCom.isNull(update_id);
	}
	public void setUpdate_id(String update_id) {
		this.update_id = update_id;
	}
	public String getItem_enm() {
		return item_enm;
	}
	public void setItem_enm(String item_enm) {
		this.item_enm = item_enm;
	}
	
}
