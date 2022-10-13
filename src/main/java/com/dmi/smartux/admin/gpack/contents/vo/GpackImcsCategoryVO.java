package com.dmi.smartux.admin.gpack.contents.vo;

import com.dmi.smartux.common.vo.BaseVO;

public class GpackImcsCategoryVO extends BaseVO {
	
	String category_id;			// 카테고리 ID
	String category_name;		// 카테고리 이름
	String album_yn;			// 앨범여부
	String sort_no;				//
	String left_yn;				// 하위 콘텐츠 여부
	String contents_id;			// 콘텐츠 ID
	String contents_name;		// 콘텐츠 명
	String series_no;			// 편성 회차 정보
	String series_yn;			// 시리즈여부
	
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public String getAlbum_yn() {
		return album_yn;
	}
	public void setAlbum_yn(String album_yn) {
		this.album_yn = album_yn;
	}
	public String getSort_no() {
		return sort_no;
	}
	public void setSort_no(String sort_no) {
		this.sort_no = sort_no;
	}
	public String getLeft_yn() {
		return left_yn;
	}
	public void setLeft_yn(String left_yn) {
		this.left_yn = left_yn;
	}
	public String getContents_id() {
		return contents_id;
	}
	public void setContents_id(String contents_id) {
		this.contents_id = contents_id;
	}
	public String getContents_name() {
		return contents_name;
	}
	public void setContents_name(String contents_name) {
		this.contents_name = contents_name;
	}
	public String getSeries_no() {
		return series_no;
	}
	public void setSeries_no(String series_no) {
		this.series_no = series_no;
	}
	public String getSeries_yn() {
		return series_yn;
	}
	public void setSeries_yn(String series_yn) {
		this.series_yn = series_yn;
	}
}
