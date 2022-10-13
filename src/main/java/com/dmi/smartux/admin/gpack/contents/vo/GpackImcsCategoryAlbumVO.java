package com.dmi.smartux.admin.gpack.contents.vo;

import com.dmi.smartux.common.vo.BaseVO;

public class GpackImcsCategoryAlbumVO extends BaseVO {

	// VOD 프로모션
	String pack_id = "";		// ID
	String category_id = "";	// 카테고리 ID
	String contents_id = "";	// VOD ID 
	String contents_nm = "";	// VOD 제목 (수동편성)
	String ordered = "";		// 순번 
	String use_yn = "";			// 사용여부 

	//IMCS 
	String cat_id = "";			// 카테고리 ID
	String album_id = "";		// 앨범 ID
	String service_gb = "";		// 
	String album_title = "";	// 앨범 제목 
	String is_hd = "";			// HD 영상 구분
	String is_sd = "";			// 
	String h_price = "";		// 
	String s_price = "";		//
	String program_info = "";	// 시청가능 연령
	String onair_date = "";		//
	String series_flag = "";	// 시리즈 여부
	String series_desc = "";	// 회차 설명
	String close_yn = "";		// 종영작 여부
	String is_caption = "";		// 자막여부(N:없음, Y:있음, D:더빙)
	String img_url = "";		// 이미지 서버 URL
	String width_img = "";		// 가로형 포스터 이미지 URL
	String height_img = "";		// 세로형 포스터 이미지 URL	
	String category = "";		// 
	String ranking = "";		// 랭킹 순위
	String genre1 = "";			// 장르 대분류
	String release_date = "";	// 개봉일
	String watch_right_yn = "";	// 2ndTV 판권 정보
	String point = "";			// 평점
	String is_fh = "";			// FullHD 여부
	
	public String getPack_id() {
		return pack_id;
	}
	public void setPack_id(String pack_id) {
		this.pack_id = pack_id;
	}
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
	public String getContents_nm() {
		return contents_nm;
	}
	public void setContents_nm(String contents_nm) {
		this.contents_nm = contents_nm;
	}
	public String getOrdered() {
		return ordered;
	}
	public void setOrdered(String ordered) {
		this.ordered = ordered;
	}
	public String getUse_yn() {
		return use_yn;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}
	public String getCat_id() {
		return cat_id;
	}
	public void setCat_id(String cat_id) {
		this.cat_id = cat_id;
	}
	public String getAlbum_id() {
		return album_id;
	}
	public void setAlbum_id(String album_id) {
		this.album_id = album_id;
	}
	public String getService_gb() {
		return service_gb;
	}
	public void setService_gb(String service_gb) {
		this.service_gb = service_gb;
	}
	public String getAlbum_title() {
		return album_title;
	}
	public void setAlbum_title(String album_title) {
		this.album_title = album_title;
	}
	public String getIs_hd() {
		return is_hd;
	}
	public void setIs_hd(String is_hd) {
		this.is_hd = is_hd;
	}
	public String getIs_sd() {
		return is_sd;
	}
	public void setIs_sd(String is_sd) {
		this.is_sd = is_sd;
	}
	public String getH_price() {
		return h_price;
	}
	public void setH_price(String h_price) {
		this.h_price = h_price;
	}
	public String getS_price() {
		return s_price;
	}
	public void setS_price(String s_price) {
		this.s_price = s_price;
	}
	public String getProgram_info() {
		return program_info;
	}
	public void setProgram_info(String program_info) {
		this.program_info = program_info;
	}
	public String getOnair_date() {
		return onair_date;
	}
	public void setOnair_date(String onair_date) {
		this.onair_date = onair_date;
	}
	public String getSeries_flag() {
		return series_flag;
	}
	public void setSeries_flag(String series_flag) {
		this.series_flag = series_flag;
	}
	public String getSeries_desc() {
		return series_desc;
	}
	public void setSeries_desc(String series_desc) {
		this.series_desc = series_desc;
	}
	public String getClose_yn() {
		return close_yn;
	}
	public void setClose_yn(String close_yn) {
		this.close_yn = close_yn;
	}
	public String getIs_caption() {
		return is_caption;
	}
	public void setIs_caption(String is_caption) {
		this.is_caption = is_caption;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public String getWidth_img() {
		return width_img;
	}
	public void setWidth_img(String width_img) {
		this.width_img = width_img;
	}
	public String getHeight_img() {
		return height_img;
	}
	public void setHeight_img(String height_img) {
		this.height_img = height_img;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getRanking() {
		return ranking;
	}
	public void setRanking(String ranking) {
		this.ranking = ranking;
	}
	public String getGenre1() {
		return genre1;
	}
	public void setGenre1(String genre1) {
		this.genre1 = genre1;
	}
	public String getRelease_date() {
		return release_date;
	}
	public void setRelease_date(String release_date) {
		this.release_date = release_date;
	}
	public String getWatch_right_yn() {
		return watch_right_yn;
	}
	public void setWatch_right_yn(String watch_right_yn) {
		this.watch_right_yn = watch_right_yn;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public String getIs_fh() {
		return is_fh;
	}
	public void setIs_fh(String is_fh) {
		this.is_fh = is_fh;
	}
	
	
	
	
}
