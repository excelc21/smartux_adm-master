/**
 * Class Name : GpackPackVO.java
 * Description : 
 *  팩 관리 구현을 위한 VO class
 *
 * Modification Information
 *  
 * 수정일         수정자         수정내용
 * ----------     --------       ---------------------------
 * 2013.03.14     kimhahn		신규
 *    
 * @author kimhahn
 * @since 2014.03.14
 * @version 1.0
 */
package com.dmi.smartux.gpack.promotion.vo;


public class GpackPackVO {

	/** 팩 ID */
	private String pack_id = "";

	/** 팩 명 */
	private String pack_nm = "";

	/** 버젼 */
	private String version = "";

	/** 템플릿 타입 */
	private String template_type = "";

	/** IMCS 카테고리 ID */
	private String imcs_category_id = "";

	/** 생성일자 */
	private String created = "";

	/** 수정일자 */
	private String updated = "";

	/** 생성자ID */
	private String create_id = "";

	/** 수정자ID */
	private String update_id = "";

	/**
	 * @return the pack_id
	 */
	public String getPack_id() {
		return pack_id;
	}

	/**
	 * @param pack_id the pack_id to set
	 */
	public void setPack_id(String pack_id) {
		this.pack_id = pack_id;
	}

	/**
	 * @return the pack_nm
	 */
	public String getPack_nm() {
		return pack_nm;
	}

	/**
	 * @param pack_nm the pack_nm to set
	 */
	public void setPack_nm(String pack_nm) {
		this.pack_nm = pack_nm;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the template_type
	 */
	public String getTemplate_type() {
		return template_type;
	}

	/**
	 * @param template_type the template_type to set
	 */
	public void setTemplate_type(String template_type) {
		this.template_type = template_type;
	}

	/**
	 * @return the imcs_category_id
	 */
	public String getImcs_category_id() {
		return imcs_category_id;
	}

	/**
	 * @param imcs_category_id the imcs_category_id to set
	 */
	public void setImcs_category_id(String imcs_category_id) {
		this.imcs_category_id = imcs_category_id;
	}

	/**
	 * @return the created
	 */
	public String getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(String created) {
		this.created = created;
	}

	/**
	 * @return the updated
	 */
	public String getUpdated() {
		return updated;
	}

	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(String updated) {
		this.updated = updated;
	}

	/**
	 * @return the create_id
	 */
	public String getCreate_id() {
		return create_id;
	}

	/**
	 * @param create_id the create_id to set
	 */
	public void setCreate_id(String create_id) {
		this.create_id = create_id;
	}

	/**
	 * @return the update_id
	 */
	public String getUpdate_id() {
		return update_id;
	}

	/**
	 * @param update_id the update_id to set
	 */
	public void setUpdate_id(String update_id) {
		this.update_id = update_id;
	}
	
}
