package com.dmi.smartux.push.vo;

/**
 * 예약구매 push VO
 *
 * @author 
 */
public class NextUiPushContentVO {
	private String category_id;		// 카테고리 id
	private String contents_id;		// 컨텐츠 id
	private String type;			// 타입(MI:컨텐츠 편성 시, CU:카테고리 수정 시, CD:카테고리 삭제 시, RP:예약구매)
	private String category_gb;		// 카테고리 구분 (I30)

	private String contents_name;	// 예약구매 - 컨텐츠명
	private String watch_end_date;	// 예약구매 - 시청유효기간
	private String series_yn;		// 예약구배 - 시리즈 여부 (Y/N)
	private String series_no;		// 예약구배 - 시리즈 번호
	
	// 시청유효기간 시간으로 표기 되어야 하는데 DB 컬럼 date 형식
	// contents_name 앞에 '|'자로 데이터 삽입	(ex: 48|컨텐츠 명)
	private String end_date;		// 예약구매 - 시청유효기간
	private String complete_yn;
	
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getContents_id() {
		return contents_id;
	}
	public void setContents_id(String contents_id) {
		this.contents_id = contents_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCategory_gb() {
		return category_gb;
	}
	public void setCategory_gb(String category_gb) {
		this.category_gb = category_gb;
	}
	public String getContents_name() {
		return contents_name;
	}
	public void setContents_name(String contents_name) {
		this.contents_name = contents_name;
	}
	public String getWatch_end_date() {
		return watch_end_date;
	}
	public void setWatch_end_date(String watch_end_date) {
		this.watch_end_date = watch_end_date;
	}
	public String getSeries_yn() {
		return series_yn;
	}
	public void setSeries_yn(String series_yn) {
		this.series_yn = series_yn;
	}
	public String getSeries_no() {
		return series_no;
	}
	public void setSeries_no(String series_no) {
		this.series_no = series_no;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getComplete_yn() {
		return complete_yn;
	}
	public void setComplete_yn(String complete_yn) {
		this.complete_yn = complete_yn;
	}
	
}
