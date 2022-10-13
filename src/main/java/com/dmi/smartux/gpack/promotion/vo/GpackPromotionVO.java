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
package com.dmi.smartux.gpack.promotion.vo;

public class GpackPromotionVO {
	
	/** 팩 ID */
	private String pack_id = "";
	
	/** 템플릿 타입 */
	private String template_type = "";

	/** 프로모션 ID */
	private String category_id = "";
	
	/** 프로모션 명 */
	private String category_nm = "";
	
	/** 프로모션 멘트 */
	private String category_comment = "";
	
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
	
	/** IMCS 카테고리 ID */
	private String imcs_category_id = "";
	
	/** 컨텐츠 개수 */
	private String contents_count = "";

	public String getPack_id() {
		return pack_id;
	}

	public void setPack_id(String pack_id) {
		this.pack_id = pack_id;
	}

	public String getTemplate_type() {
		return template_type;
	}

	public void setTemplate_type(String template_type) {
		this.template_type = template_type;
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

	public String getPromotion_video_gb() {
		return promotion_video_gb;
	}

	public void setPromotion_video_gb(String promotion_video_gb) {
		this.promotion_video_gb = promotion_video_gb;
	}

	public String getPromotion_chnl() {
		return promotion_chnl;
	}

	public void setPromotion_chnl(String promotion_chnl) {
		this.promotion_chnl = promotion_chnl;
	}

	public String getPromotion_chnl_info() {
		return promotion_chnl_info;
	}

	public void setPromotion_chnl_info(String promotion_chnl_info) {
		this.promotion_chnl_info = promotion_chnl_info;
	}

	public String getAuto_yn() {
		return auto_yn;
	}

	public void setAuto_yn(String auto_yn) {
		this.auto_yn = auto_yn;
	}

	public String getImcs_category_id() {
		return imcs_category_id;
	}

	public void setImcs_category_id(String imcs_category_id) {
		this.imcs_category_id = imcs_category_id;
	}

	public String getContents_count() {
		return contents_count;
	}

	public void setContents_count(String contents_count) {
		this.contents_count = contents_count;
	}
}
