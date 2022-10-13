package com.dmi.smartux.admin.season.vo;

import java.util.List;

public class SeasonVo {
	
	private String rowno;
	private String season_id;
	private String season_title;
	private String parent_season_id;
	private String season_name;
	private String category_id;
	private String content_id;
	private String content_name;
	private String reg_dt;
	private String reg_id;
	private String mod_dt;
	private String mod_id;
	private String cache_mod_dt;
	private int orders;
	private String series_yn;
	private String app_tp;
	
	private List<String> seasonData;
	
	public String getRowno() {
		return rowno;
	}

	public void setRowno(String rowno) {
		this.rowno = rowno;
	}

	public String getSeason_id() {
		return season_id;
	}

	public void setSeason_id(String season_id) {
		this.season_id = season_id;
	}

	public String getSeason_title() {
		return season_title;
	}

	public void setSeason_title(String season_title) {
		this.season_title = season_title;
	}

	public String getParent_season_id() {
		return parent_season_id;
	}

	public void setParent_season_id(String parent_season_id) {
		this.parent_season_id = parent_season_id;
	}

	public String getSeason_name() {
		return season_name;
	}

	public void setSeason_name(String season_name) {
		this.season_name = season_name;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getContent_id() {
		return content_id;
	}

	public String getContent_name() {
		return content_name;
	}

	public void setContent_name(String content_name) {
		this.content_name = content_name;
	}

	public void setContent_id(String content_id) {
		this.content_id = content_id;
	}

	public String getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}

	public String getReg_id() {
		return reg_id;
	}

	public void setReg_id(String reg_id) {
		this.reg_id = reg_id;
	}

	public String getMod_dt() {
		return mod_dt;
	}

	public void setMod_dt(String mod_dt) {
		this.mod_dt = mod_dt;
	}

	public String getMod_id() {
		return mod_id;
	}

	public void setMod_id(String mod_id) {
		this.mod_id = mod_id;
	}

	public String getCache_mod_dt() {
		return cache_mod_dt;
	}

	public void setCache_mod_dt(String cache_mod_dt) {
		this.cache_mod_dt = cache_mod_dt;
	}

	public List<String> getSeasonData() {
		return seasonData;
	}

	public void setSeasonData(List<String> seasonData) {
		this.seasonData = seasonData;
	}

	public int getOrders() {
		return orders;
	}

	public void setOrders(int orders) {
		this.orders = orders;
	}

	public String getSeries_yn() {
		return series_yn;
	}

	public void setSeries_yn(String series_yn) {
		this.series_yn = series_yn;
	}

	public String getApp_tp() {
		return app_tp;
	}

	public void setApp_tp(String app_tp) {
		this.app_tp = app_tp;
	}
	
}
