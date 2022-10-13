package com.dmi.smartux.admin.rank.vo;

import com.dmi.smartux.common.vo.BaseVO;

public class EcrmRankVO extends BaseVO {
	String rank_code;		// VOD 랭킹 코드
	String rank_name;		// VOD 랭킹 data name
	String genre_code;		// VOD 장르 코드
	String rule_code;		// VOD rule code
	String rule_code_origin;	// VOD rule code original
	String rank_term;		// VOD 랭킹 기간
	String genre_large;		// 장르 대분류
	String genre_mid;		// 장르 중분류
	String genre_small;		// 장르 소분류
	String pannel_id;
	String title_id;
	
	String category_id;			// category_id
	String album_id;			// album_id
	String album_name;			// album_name
	String adi_product_id;		// adi_product_id
	int ordered;				// ordered
	String category_gb;			// 카테고리 구분
	String rule_type;			// 룰타입
	
	public String getRank_code() {
		return rank_code;
	}
	
	public void setRank_code(String rank_code) {
		this.rank_code = rank_code;
	}

	public String getRank_name() {
		return rank_name;
	}
	
	public void setRank_name(String rank_name) {
		this.rank_name = rank_name;
	}
	
	public String getGenre_code() {
		return genre_code;
	}
	
	public void setGenre_code(String genre_code) {
		this.genre_code = genre_code;
	}

	public String getRule_code() {
		return rule_code;
	}
	
	public void setRule_code(String rule_code) {
		this.rule_code = rule_code;
	}
	
	public String getRule_code_origin() {
		return rule_code_origin;
	}
	
	public void setRule_code_origin(String rule_code_origin) {
		this.rule_code_origin = rule_code_origin;
	}
	
	public String getRank_term() {
		return rank_term;
	}
	
	public void setRank_term(String rank_term) {
		this.rank_term = rank_term;
	}
	
	public String getGenre_large() {
		return genre_large;
	}
	
	public void setGenre_large(String genre_large) {
		this.genre_large = genre_large;
	}
	
	public String getGenre_mid() {
		return genre_mid;
	}
	
	public void setGenre_mid(String genre_mid) {
		this.genre_mid = genre_mid;
	}
	
	public String getGenre_small() {
		return genre_small;
	}
	
	public void setGenre_small(String genre_small) {
		this.genre_small = genre_small;
	}
	
	public String getPannel_id() {
		return pannel_id;
	}
	
	public void setPannel_id(String pannel_id) {
		this.pannel_id = pannel_id;
	}
	
	public String getTitle_id() {
		return title_id;
	}
	
	public void setTitle_id(String title_id) {
		this.title_id = title_id;
	}
	
	public String getCategory_id() {
		return category_id;
	}
	
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}		
	
	public String getAlbum_id() {
		return album_id;
	}
	
	public void setAlbum_id(String album_id) {
		this.album_id = album_id;
	}
	
	public String getAlbum_name() {
		return album_name;
	}
	
	public void setAlbum_name(String album_name) {
		this.album_name = album_name;
	}
	
	public String getAdi_product_id() {
		return adi_product_id;
	}
	
	public void setAdi_product_id(String adi_product_id) {
		this.adi_product_id = adi_product_id;
	}
	
	public int getOrdered() {
		return ordered;
	}
	
	public void setOrdered(int ordered) {
		this.ordered = ordered;
	}

	public String getCategory_gb() {
		return category_gb;
	}

	public void setCategory_gb(String category_gb) {
		this.category_gb = category_gb;
	}

	public String getRule_type() {
		return rule_type;
	}

	public void setRule_type(String rule_type) {
		this.rule_type = rule_type;
	}
	
}
