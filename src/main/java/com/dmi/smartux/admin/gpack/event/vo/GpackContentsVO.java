/**
 * Class Name : GpackContentsVO.java
 * Description : 
 *  컨텐츠 정보 VO class
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
package com.dmi.smartux.admin.gpack.event.vo;

public class GpackContentsVO {
	
	/** 팩 ID */
	private String pack_id = "";

	/** 프로모션 ID */
	private String category_id = "";
	
	/** 콘텐츠 ID */
	private String contents_id = "";
	
	/** 콘텐츠 명 */
	private String contents_nm = "";
	
	/** 
	 * 이동경로 타입 
	 * MT001:월정액, MT002:VOD상세, MT003:양방향어플, MT004:DAL, MT005:채널
	 */
	private String movepath_type = "";
	
	/** 이동경로ID */
	private String movepath_id = "";
	
	/** 이동경로URL */
	private String movepath_url = "";
	
	/** IMCS 카테고리 ID */
	private String imcs_category_id = "";
	
	/** ALBUM ID */
	private String album_id = "";
	
	/** 이미지 경로 */
	private String img_path = "";

	/** 이미지 파일 */
	private String img_file = "";
	
	/** 순번 */
	private String ordered = "";
	
	/** 사용 여부 */
	private String use_yn = "";

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
	 * @return the category_id
	 */
	public String getCategory_id() {
		return category_id;
	}

	/**
	 * @param category_id the category_id to set
	 */
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	/**
	 * @return the contents_id
	 */
	public String getContents_id() {
		return contents_id;
	}

	/**
	 * @param contents_id the contents_id to set
	 */
	public void setContents_id(String contents_id) {
		this.contents_id = contents_id;
	}

	/**
	 * @return the contents_nm
	 */
	public String getContents_nm() {
		return contents_nm;
	}

	/**
	 * @param contents_nm the contents_nm to set
	 */
	public void setContents_nm(String contents_nm) {
		this.contents_nm = contents_nm;
	}

	/**
	 * @return the movepath_type
	 */
	public String getMovepath_type() {
		return movepath_type;
	}

	/**
	 * @param movepath_type the movepath_type to set
	 */
	public void setMovepath_type(String movepath_type) {
		this.movepath_type = movepath_type;
	}

	/**
	 * @return the movepath_id
	 */
	public String getMovepath_id() {
		return movepath_id;
	}

	/**
	 * @param movepath_id the movepath_id to set
	 */
	public void setMovepath_id(String movepath_id) {
		this.movepath_id = movepath_id;
	}

	/**
	 * @return the movepath_url
	 */
	public String getMovepath_url() {
		return movepath_url;
	}

	/**
	 * @param movepath_url the movepath_url to set
	 */
	public void setMovepath_url(String movepath_url) {
		this.movepath_url = movepath_url;
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
	 * @return the album_id
	 */
	public String getAlbum_id() {
		return album_id;
	}

	/**
	 * @param album_id the album_id to set
	 */
	public void setAlbum_id(String album_id) {
		this.album_id = album_id;
	}

	/**
	 * @return the img_path
	 */
	public String getImg_path() {
		return img_path;
	}

	/**
	 * @param img_path the img_path to set
	 */
	public void setImg_path(String img_path) {
		this.img_path = img_path;
	}

	/**
	 * @return the img_file
	 */
	public String getImg_file() {
		return img_file;
	}

	/**
	 * @param img_file the img_file to set
	 */
	public void setImg_file(String img_file) {
		this.img_file = img_file;
	}

	/**
	 * @return the ordered
	 */
	public String getOrdered() {
		return ordered;
	}

	/**
	 * @param ordered the ordered to set
	 */
	public void setOrdered(String ordered) {
		this.ordered = ordered;
	}

	/**
	 * @return the use_yn
	 */
	public String getUse_yn() {
		return use_yn;
	}

	/**
	 * @param use_yn the use_yn to set
	 */
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
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
