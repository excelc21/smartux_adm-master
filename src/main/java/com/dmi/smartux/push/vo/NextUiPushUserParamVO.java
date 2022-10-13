package com.dmi.smartux.push.vo;

/**
 * NextUi 예약 push  VO
 *
 * @author 
 */
public class NextUiPushUserParamVO {
	private String contents_id;		// 컨텐츠 id
	private String category_gb;		// 카테고리 구분 (I20
	private int start_num;			// 시작 번호
	private int end_num;			// 종료 번호
	
	public String getContents_id() {
		return contents_id;
	}
	public void setContents_id(String contents_id) {
		this.contents_id = contents_id;
	}
	public String getCategory_gb() {
		return category_gb;
	}
	public void setCategory_gb(String category_gb) {
		this.category_gb = category_gb;
	}
	public int getStart_num() {
		return start_num;
	}
	public void setStart_num(int start_num) {
		this.start_num = start_num;
	}
	public int getEnd_num() {
		return end_num;
	}
	public void setEnd_num(int end_num) {
		this.end_num = end_num;
	}
	
}
