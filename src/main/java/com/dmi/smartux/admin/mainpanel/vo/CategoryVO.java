package com.dmi.smartux.admin.mainpanel.vo;

import com.dmi.smartux.common.vo.BaseVO;

public class CategoryVO extends BaseVO {
	
	String category_id;			// 카테고리 ID
	String category_name;		// 카테고리 이름
	String album_cnt;			// 카테고리에서 보여줄 앨범의 갯수
	String category_level;		// 카테고리 레벨
	String album_yn;			// 현재 표현하는 카테고리의 한단계 아래에 카테고리가 있는지 아니면 바로 앨범이 있는지의 여부(Y이면 한단계 아래 것이 앨범, N이면 한단계 아래것이 카테고리)
	
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
	public String getAlbum_cnt() {
		return album_cnt;
	}
	public void setAlbum_cnt(String album_cnt) {
		this.album_cnt = album_cnt;
	}
	public String getCategory_level() {
		return category_level;
	}
	public void setCategory_level(String category_level) {
		this.category_level = category_level;
	}
	public String getAlbum_yn() {
		return album_yn;
	}
	public void setAlbum_yn(String album_yn) {
		this.album_yn = album_yn;
	}
}
