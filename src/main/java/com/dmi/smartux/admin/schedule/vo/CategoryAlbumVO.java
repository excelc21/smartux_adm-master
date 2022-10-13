package com.dmi.smartux.admin.schedule.vo;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

public class CategoryAlbumVO extends BaseVO {

	int rank_no;					// 순위
	String album_id;				// album_id
	String album_name;				// album_name
	String category_id;				// category_id
	String category_name;			// category_name
	String width_img;
	String height_img;	
	String series_no;
	
	public String getSeries_no() {
		return series_no;
	}
	public void setSeries_no(String series_no) {
		this.series_no = series_no;
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
	public int getRank_no() {
		return rank_no;
	}
	public void setRank_no(int rank_no) {
		this.rank_no = rank_no;
	}
	public String getAlbum_id() {
		return GlobalCom.isNull(album_id);
	}
	public void setAlbum_id(String album_id) {
		this.album_id = album_id;
	}
	public String getAlbum_name() {
		return GlobalCom.isNull(album_name);
	}
	public void setAlbum_name(String album_name) {
		this.album_name = album_name;
	}
	public String getCategory_id() {
		return GlobalCom.isNull(category_id);
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getCategory_name() {
		return GlobalCom.isNull(category_name);
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
}
