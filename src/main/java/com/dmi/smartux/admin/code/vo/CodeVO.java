package com.dmi.smartux.admin.code.vo;

import com.dmi.smartux.common.vo.BaseVO;

public class CodeVO extends BaseVO {
	String code;			// 코드
	String code_nm;			// 코드명
	String created;			// 입력일
	String updated;			// 수정일
	String create_id;		// 입력자
	String update_id;		// 수정자
	String existProperty;	// 코드가 프로퍼티 파일에 존재하는지의 여부(Y/N)
	int itemcodecnt;		// 해당 코드 밑에 있는 서브아이템코드들의 갯수
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCode_nm() {
		return code_nm;
	}
	public void setCode_nm(String code_nm) {
		this.code_nm = code_nm;
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
	public String getExistProperty() {
		return existProperty;
	}
	public void setExistProperty(String existProperty) {
		this.existProperty = existProperty;
	}
	public int getItemcodecnt() {
		return itemcodecnt;
	}
	public void setItemcodecnt(int itemcodecnt) {
		this.itemcodecnt = itemcodecnt;
	}
	
	
}
