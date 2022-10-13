package com.dmi.smartux.admin.mainpanel.vo;

import com.dmi.smartux.common.vo.BaseVO;

/**
 * @author jeonghun
 *
 */
public class PanelVO extends BaseVO {
	
	String next_pannel_id;			// 패널 ID
	String pannel_id;			// 패널 ID
	String pannel_nm;			// 패널명
	String use_yn;				// 사용여부
	String version;				// 버전
	String created;				// 입력일
	String updated;				// 수정일
	String created_id;			// 입력자
	String updated_id;			// 수정자
	String existProperty;		// 패널이 프로퍼티 파일에 존재하는지의 여부(Y/N)
	String panel_ui_type;		// 패널UI타입
	String panel_image;;		// 패널 이미지 파일
	String img_url;				// 이미지 URL
	String focus_type;
	String order;
	
	String abtest_yn; // AB테스트 
	
	public String getNext_pannel_id() {
		return next_pannel_id;
	}
	public void setNext_pannel_id(String next_pannel_id) {
		this.next_pannel_id = next_pannel_id;
	}
	
	public String getPannel_id() {
		return pannel_id;
	}
	public void setPannel_id(String pannel_id) {
		this.pannel_id = pannel_id;
	}
	public String getPannel_nm() {
		return pannel_nm;
	}
	public void setPannel_nm(String pannel_nm) {
		this.pannel_nm = pannel_nm;
	}
	public String getUse_yn() {
		return use_yn;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
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
	public String getCreated_id() {
		return created_id;
	}
	public void setCreated_id(String created_id) {
		this.created_id = created_id;
	}
	public String getUpdated_id() {
		return updated_id;
	}
	public void setUpdated_id(String updated_id) {
		this.updated_id = updated_id;
	}
	public String getExistProperty() {
		return existProperty;
	}
	public void setExistProperty(String existProperty) {
		this.existProperty = existProperty;
	}
	public String getPanel_ui_type() {
		return panel_ui_type;
	}
	public void setPanel_ui_type(String panel_ui_type) {
		this.panel_ui_type = panel_ui_type;
	}
	public String getPanel_image() {
		return panel_image;
	}
	public void setPanel_image(String panel_image) {
		this.panel_image = panel_image;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	
	public String getFocus_type() {
		return focus_type;
	}
	public void setFocus_type(String focus_type) {
		this.focus_type = focus_type;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	
	public String getAbtest_yn() {
		return abtest_yn;
	}
	public void setAbtest_yn(String abtest_yn) {
		this.abtest_yn = abtest_yn;
	}	
}
