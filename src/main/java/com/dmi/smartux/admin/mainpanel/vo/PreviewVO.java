package com.dmi.smartux.admin.mainpanel.vo;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

public class PreviewVO extends BaseVO {
	String code;
	String p_code;
	String panelviewname;
	String category_code;
	String category_type;
	String ui_type;
	String bg_img_file;
	String description;
	String order_seq;
	String album_desc;
	String level;
	String page_code;
	String page_type;
	String category_gb;
	
	public String getCode() {
		return GlobalCom.isNull(code);
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getP_code() {
		return GlobalCom.isNull(p_code);
	}
	public void setP_code(String p_code) {
		this.p_code = p_code;
	}
	public String getPanelviewname() {
		return GlobalCom.isNull(panelviewname);
	}
	public void setPanelviewname(String panelviewname) {
		this.panelviewname = panelviewname;
	}
	public String getCategory_code() {
		return GlobalCom.isNull(category_code);
	}
	public void setCategory_code(String category_code) {
		this.category_code = category_code;
	}
	public String getCategory_type() {
		return GlobalCom.isNull(category_type);
	}
	public void setCategory_type(String category_type) {
		this.category_type = category_type;
	}
	public String getUi_type() {
		return GlobalCom.isNull(ui_type);
	}
	public void setUi_type(String ui_type) {
		this.ui_type = ui_type;
	}
	public String getBg_img_file() {
		return GlobalCom.isNull(bg_img_file);
	}
	public void setBg_img_file(String bg_img_file) {
		this.bg_img_file = bg_img_file;
	}
	public String getDescription() {
		return GlobalCom.isNull(description);
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOrder_seq() {
		return GlobalCom.isNull(order_seq);
	}
	public void setOrder_seq(String order_seq) {
		this.order_seq = order_seq;
	}
	public String getAlbum_desc() {
		return GlobalCom.isNull(album_desc);
	}
	public void setAlbum_desc(String album_desc) {
		this.album_desc = album_desc;
	}
	public String getLevel() {
		return GlobalCom.isNull(level);
	}
	public void setLevel(String level) {
		this.level = level;
	}
	/**
	 * @return the page_code
	 */
	public String getPage_code() {
		return page_code;
	}
	/**
	 * @param page_code the page_code to set
	 */
	public void setPage_code(String page_code) {
		this.page_code = page_code;
	}
	/**
	 * @return the page_type
	 */
	public String getPage_type() {
		return page_type;
	}
	/**
	 * @param page_type the page_type to set
	 */
	public void setPage_type(String page_type) {
		this.page_type = page_type;
	}
	public String getCategory_gb() {
		return category_gb;
	}
	public void setCategory_gb(String category_gb) {
		this.category_gb = category_gb;
	}
	
}
