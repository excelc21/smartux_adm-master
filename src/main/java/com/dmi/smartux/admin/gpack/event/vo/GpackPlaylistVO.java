/**
 * Class Name : GpackPlaylistVO.java
 * Description : 
 *  플레이리스트 정보 VO class
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

public class GpackPlaylistVO {
	
	/** 팩 ID */
	private String pack_id = "";

	/** 프로모션 ID */
	private String category_id = "";
	
	/** 플레이리스트 ID */
	private String playlist_id = "";
	
	/** 플레이리스트 명 */
	private String playlist_nm = "";
	
	/** 예고편 IMCS 카테고리 ID */
	private String preview_imcs_category_id = "";
	
	/** 예고편 IMCS 앨범 ID */
	private String preview_imcs_album_id = "";
	
	/** 예고편 IMCS */
	private String preview_imcs_text = "";
	
	/** 본편 IMCS 카테고리 ID */
	private String feature_imcs_category_id = "";
	
	/** 본편  IMCS 앨범 ID */
	private String feature_imcs_album_id = "";
	
	/** 본편  IMCS */
	private String feature_imcs_text = "";
	
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
	
	/** 조건:사용여부 */
	private String where_use_yn = "";

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
	 * @return the playlist_id
	 */
	public String getPlaylist_id() {
		return playlist_id;
	}

	/**
	 * @param playlist_id the playlist_id to set
	 */
	public void setPlaylist_id(String playlist_id) {
		this.playlist_id = playlist_id;
	}

	/**
	 * @return the playlist_nm
	 */
	public String getPlaylist_nm() {
		return playlist_nm;
	}

	/**
	 * @param playlist_nm the playlist_nm to set
	 */
	public void setPlaylist_nm(String playlist_nm) {
		this.playlist_nm = playlist_nm;
	}

	/**
	 * @return the preview_imcs_category_id
	 */
	public String getPreview_imcs_category_id() {
		return preview_imcs_category_id;
	}

	/**
	 * @param preview_imcs_category_id the preview_imcs_category_id to set
	 */
	public void setPreview_imcs_category_id(String preview_imcs_category_id) {
		this.preview_imcs_category_id = preview_imcs_category_id;
	}

	/**
	 * @return the preview_imcs_album_id
	 */
	public String getPreview_imcs_album_id() {
		return preview_imcs_album_id;
	}

	/**
	 * @param preview_imcs_album_id the preview_imcs_album_id to set
	 */
	public void setPreview_imcs_album_id(String preview_imcs_album_id) {
		this.preview_imcs_album_id = preview_imcs_album_id;
	}

	/**
	 * @return the preview_imcs_text
	 */
	public String getPreview_imcs_text() {
		return preview_imcs_text;
	}

	/**
	 * @param preview_imcs_text the preview_imcs_text to set
	 */
	public void setPreview_imcs_text(String preview_imcs_text) {
		this.preview_imcs_text = preview_imcs_text;
	}

	/**
	 * @return the feature_imcs_category_id
	 */
	public String getFeature_imcs_category_id() {
		return feature_imcs_category_id;
	}

	/**
	 * @param feature_imcs_category_id the feature_imcs_category_id to set
	 */
	public void setFeature_imcs_category_id(String feature_imcs_category_id) {
		this.feature_imcs_category_id = feature_imcs_category_id;
	}

	/**
	 * @return the feature_imcs_album_id
	 */
	public String getFeature_imcs_album_id() {
		return feature_imcs_album_id;
	}

	/**
	 * @param feature_imcs_album_id the feature_imcs_album_id to set
	 */
	public void setFeature_imcs_album_id(String feature_imcs_album_id) {
		this.feature_imcs_album_id = feature_imcs_album_id;
	}

	/**
	 * @return the feature_imcs_text
	 */
	public String getFeature_imcs_text() {
		return feature_imcs_text;
	}

	/**
	 * @param feature_imcs_text the feature_imcs_text to set
	 */
	public void setFeature_imcs_text(String feature_imcs_text) {
		this.feature_imcs_text = feature_imcs_text;
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

	/**
	 * @return the where_use_yn
	 */
	public String getWhere_use_yn() {
		return where_use_yn;
	}

	/**
	 * @param where_use_yn the where_use_yn to set
	 */
	public void setWhere_use_yn(String where_use_yn) {
		this.where_use_yn = where_use_yn;
	}

}
