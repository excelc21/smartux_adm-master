package com.dmi.smartux.admin.youtube.vo;

import java.util.List;

public class YoutubeVO {
	
	private List<YoutubeVO> list;	//리스트 객체

	private String code;		//코드
	private String category;	//구분
	private String recommend_text;		//검색어
	private String use_yn;		//사용여부
	private String write_id;		//작성자 아이디
	private String created;	//등록일자
	private String updated;	//수정일자
	
	private String validate;
	
	/**
	 * @return the list
	 */
	public List<YoutubeVO> getList() {
		return list;
	}
	/**
	 * @param list the list to set
	 */
	public void setList(List<YoutubeVO> list) {
		this.list = list;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the recommend_text
	 */
	public String getRecommend_text() {
		return recommend_text;
	}
	/**
	 * @param recommend_text the recommend_text to set
	 */
	public void setRecommend_text(String recommend_text) {
		this.recommend_text = recommend_text;
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
	 * @return the write_id
	 */
	public String getWrite_id() {
		return write_id;
	}
	/**
	 * @param write_id the write_id to set
	 */
	public void setWrite_id(String write_id) {
		this.write_id = write_id;
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
	 * @return the validate
	 */
	public String getValidate() {
		return validate;
	}
	/**
	 * @param validate the validate to set
	 */
	public void setValidate(String validate) {
		this.validate = validate;
	}
	
	
	
}
