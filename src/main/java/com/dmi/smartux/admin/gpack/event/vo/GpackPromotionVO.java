/**
 * Class Name : GpackPromotionVO.java
 * Description : 
 *  프로모션 정보 VO class
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

import java.util.List;

public class GpackPromotionVO {
	
	/** 팩 ID */
	private String pack_id = "";

	/** 프로모션 ID */
	private String category_id = "";
	
	/** 프로모션 명 */
	private String category_nm = "";
	
	/** 프로모션 멘트 */
	private String category_comment = "";
	
	/** 
	 * 카테고리/프로모션 여부 
	 * Y : 카테고리
	 * N : 프로모션
	 */
	private String category_yn = "";
	
	/** 
	 * 프로모션 영상 구분
	 * PV001 : 채널
	 * PV002 : 플레이리스트
	 */
	private String promotion_video_gb = "";
	
	/** 프로모션 채널 */
	private String promotion_chnl = "";
	
	/** 프로모션 채널 정보 */
	private String promotion_chnl_info = "";
	
	/** 자동 여부 */
	private String auto_yn = "";
	
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
	
	/** 컨텐츠 개수 */
	private String contents_count = "";
	
	/** 플레이리스트 정보 목록 */
	private List<GpackPlaylistVO> gpackPlaylistVOList;
	
	/** 컨텐츠 정보 목록 */
	private List<GpackPromotionContentsVO> gpackPromotionContentsVOList;

	/** 조건:사용 여부 */
	private String where_use_yn = "";
	
	/** 컨텐츠 사용 여부 */
	private String c_use_yn = "";
	
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
	 * @return the category_nm
	 */
	public String getCategory_nm() {
		return category_nm;
	}

	/**
	 * @param category_nm the category_nm to set
	 */
	public void setCategory_nm(String category_nm) {
		this.category_nm = category_nm;
	}

	/**
	 * @return the category_comment
	 */
	public String getCategory_comment() {
		return category_comment;
	}

	/**
	 * @param category_comment the category_comment to set
	 */
	public void setCategory_comment(String category_comment) {
		this.category_comment = category_comment;
	}

	/**
	 * @return the category_yn
	 */
	public String getCategory_yn() {
		return category_yn;
	}

	/**
	 * @param category_yn the category_yn to set
	 */
	public void setCategory_yn(String category_yn) {
		this.category_yn = category_yn;
	}

	/**
	 * @return the promotion_video_gb
	 */
	public String getPromotion_video_gb() {
		return promotion_video_gb;
	}

	/**
	 * @param promotion_video_gb the promotion_video_gb to set
	 */
	public void setPromotion_video_gb(String promotion_video_gb) {
		this.promotion_video_gb = promotion_video_gb;
	}

	/**
	 * @return the promotion_chnl
	 */
	public String getPromotion_chnl() {
		return promotion_chnl;
	}

	/**
	 * @param promotion_chnl the promotion_chnl to set
	 */
	public void setPromotion_chnl(String promotion_chnl) {
		this.promotion_chnl = promotion_chnl;
	}

	/**
	 * @return the promotion_chnl_info
	 */
	public String getPromotion_chnl_info() {
		return promotion_chnl_info;
	}

	/**
	 * @param promotion_chnl_info the promotion_chnl_info to set
	 */
	public void setPromotion_chnl_info(String promotion_chnl_info) {
		this.promotion_chnl_info = promotion_chnl_info;
	}

	/**
	 * @return the auto_yn
	 */
	public String getAuto_yn() {
		return auto_yn;
	}

	/**
	 * @param auto_yn the auto_yn to set
	 */
	public void setAuto_yn(String auto_yn) {
		this.auto_yn = auto_yn;
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
	 * @return the contents_count
	 */
	public String getContents_count() {
		return contents_count;
	}

	/**
	 * @param contents_count the contents_count to set
	 */
	public void setContents_count(String contents_count) {
		this.contents_count = contents_count;
	}

	/**
	 * @return the gpackPlaylistVOList
	 */
	public List<GpackPlaylistVO> getGpackPlaylistVOList() {
		return gpackPlaylistVOList;
	}

	/**
	 * @param gpackPlaylistVOList the gpackPlaylistVOList to set
	 */
	public void setGpackPlaylistVOList(List<GpackPlaylistVO> gpackPlaylistVOList) {
		this.gpackPlaylistVOList = gpackPlaylistVOList;
	}

	/**
	 * @return the gpackPromotionContentsVOList
	 */
	public List<GpackPromotionContentsVO> getGpackPromotionContentsVOList() {
		return gpackPromotionContentsVOList;
	}

	/**
	 * @param gpackPromotionContentsVOList the gpackPromotionContentsVOList to set
	 */
	public void setGpackPromotionContentsVOList(
			List<GpackPromotionContentsVO> gpackPromotionContentsVOList) {
		this.gpackPromotionContentsVOList = gpackPromotionContentsVOList;
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

	public String getC_use_yn ( ) {
		return c_use_yn;
	}

	public void setC_use_yn ( String c_use_yn ) {
		this.c_use_yn = c_use_yn;
	}

}
