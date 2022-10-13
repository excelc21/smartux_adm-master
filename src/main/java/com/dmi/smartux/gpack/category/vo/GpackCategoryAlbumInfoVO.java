package com.dmi.smartux.gpack.category.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

@XmlRootElement(name="record")
@XmlType(name="GpackCategoryAlbumInfoVO", namespace="com.dmi.smartux.gpack.category.vo.GpackCategoryAlbumInfoVO", 
		propOrder={"album_id","category_id","album_title","ordered","img_url","w_img","h_img","series_flag","series_desc","series_no",
				"close_yn","is_hd","program_info","onair_date","is_caption","genre1","release_date","watch_right_yn","is_fh"})
public class GpackCategoryAlbumInfoVO extends BaseVO implements Serializable, Cloneable {

	//IMCS 
	String album_id = "";		// 앨범 ID
	String category_id = "";	// 카테고리 ID
	String album_title = "";	// 앨범 제목 
	String ordered = "";		// 정렬순번 (ranking 이 맞는지 확인)
	String img_url = "";		// 이미지 서버 URL
	String w_img = "";			// 가로형 포스터 이미지 URL
	String h_img = "";			// 세로형 포스터 이미지 URL	
	String series_flag = "";	// 시리즈 여부
	String series_desc = "";	// 회차 설명
	String series_no = "";		// 회차
	String close_yn = "";		// 종영작 여부
	String is_hd = "";			// HD 영상 구분
	String program_info = "";	// 시청가능 연령
	String onair_date = "";		// 방영일
	String is_caption = "";		// 자막여부(N:없음, Y:있음, D:더빙)
	String genre1 = "";			// 장르 대분류
	String release_date = "";	// 개봉일
	String watch_right_yn = "";	// 2ndTV 판권 정보
	String is_fh = "";			// FullHD 여부

	public String getAlbum_id() {
	    return GlobalCom.isNull(album_id);
	}
	@XmlElement(name="album_id")
	public void setAlbum_id(String album_id) {
	    this.album_id = album_id;
	}
	public String getCategory_id() {
	    return GlobalCom.isNull(category_id);
	}
	@XmlElement(name="category_id")
	public void setCategory_id(String category_id) {
	    this.category_id = category_id;
	}
	public String getAlbum_title() {
	    return GlobalCom.isNull(album_title);
	}
	@XmlElement(name="album_title")
	public void setAlbum_title(String album_title) {
	    this.album_title = album_title;
	}
	public String getOrdered() {
	    return GlobalCom.isNull(ordered);
	}
	@XmlElement(name="ordered")
	public void setOrdered(String ordered) {
	    this.ordered = ordered;
	}
	public String getImg_url() {
	    return GlobalCom.isNull(img_url);
	}
	@XmlElement(name="img_url")
	public void setImg_url(String img_url) {
	    this.img_url = img_url;
	}
	public String getW_img() {
		return GlobalCom.isNull(w_img);
	}
	@XmlElement(name="w_img")
	public void setW_img(String w_img) {
		this.w_img = w_img;
	}
	public String getH_img() {
		return GlobalCom.isNull(h_img);
	}
	@XmlElement(name="h_img")
	public void setH_img(String h_img) {
		this.h_img = h_img;
	}
	public String getSeries_flag() {
	    return GlobalCom.isNull(series_flag);
	}
	@XmlElement(name="series_flag")
	public void setSeries_flag(String series_flag) {
	    this.series_flag = series_flag;
	}
	public String getSeries_desc() {
	    return GlobalCom.isNull(series_desc);
	}
	@XmlElement(name="series_desc")
	public void setSeries_desc(String series_desc) {
	    this.series_desc = series_desc;
	}
	public String getSeries_no() {
		return series_no;
	}
	@XmlElement(name="series_no")
	public void setSeries_no(String series_no) {
		this.series_no = series_no;
	}
	public String getClose_yn() {
	    return GlobalCom.isNull(close_yn);
	}
	@XmlElement(name="close_yn")
	public void setClose_yn(String close_yn) {
	    this.close_yn = close_yn;
	}
	public String getIs_hd() {
	    return GlobalCom.isNull(is_hd);
	}
	@XmlElement(name="is_hd")
	public void setIs_hd(String is_hd) {
	    this.is_hd = is_hd;
	}
	public String getProgram_info() {
	    return GlobalCom.isNull(program_info);
	}
	@XmlElement(name="program_info")
	public void setProgram_info(String program_info) {
	    this.program_info = program_info;
	}
	public String getOnair_date() {
	    return GlobalCom.isNull(onair_date);
	}
	@XmlElement(name="onair_date")
	public void setOnair_date(String onair_date) {
	    this.onair_date = onair_date;
	}
	public String getIs_caption() {
	    return GlobalCom.isNull(is_caption);
	}
	@XmlElement(name="is_caption")
	public void setIs_caption(String is_caption) {
	    this.is_caption = is_caption;
	}
	public String getGenre1() {
	    return GlobalCom.isNull(genre1);
	}
	@XmlElement(name="genre1")
	public void setGenre1(String genre1) {
	    this.genre1 = genre1;
	}
	public String getRelease_date() {
	    return GlobalCom.isNull(release_date);
	}
	@XmlElement(name="release_date")
	public void setRelease_date(String release_date) {
	    this.release_date = release_date;
	}
	public String getWatch_right_yn() {
	    return GlobalCom.isNull(watch_right_yn);
	}
	@XmlElement(name="watch_right_yn")
	public void setWatch_right_yn(String watch_right_yn) {
	    this.watch_right_yn = watch_right_yn;
	}
	public String getIs_fh() {
	    return GlobalCom.isNull(is_fh);
	}
	@XmlElement(name="is_fh")
	public void setIs_fh(String is_fh) {
	    this.is_fh = is_fh;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append(getAlbum_id());
		sb.append(GlobalCom.colsep);
		sb.append(getCategory_id());
		sb.append(GlobalCom.colsep);
		sb.append(getAlbum_title());
		sb.append(GlobalCom.colsep);
		sb.append(getOrdered());
		sb.append(GlobalCom.colsep);
		sb.append(getImg_url());
		sb.append(GlobalCom.colsep);
		sb.append(getW_img());
		sb.append(GlobalCom.colsep);
		sb.append(getH_img());
		sb.append(GlobalCom.colsep);
		sb.append(getSeries_flag());
		sb.append(GlobalCom.colsep);
		sb.append(getSeries_desc());
		sb.append(GlobalCom.colsep);
		sb.append(getSeries_no());
		sb.append(GlobalCom.colsep);
		sb.append(getClose_yn());
		sb.append(GlobalCom.colsep);
		sb.append(getIs_hd());
		sb.append(GlobalCom.colsep);
		sb.append(getProgram_info());
		sb.append(GlobalCom.colsep);
		sb.append(getOnair_date());
		sb.append(GlobalCom.colsep);
		sb.append(getIs_caption());
		sb.append(GlobalCom.colsep);
		sb.append(getGenre1());
		sb.append(GlobalCom.colsep);
		sb.append(getRelease_date());
		sb.append(GlobalCom.colsep);
		sb.append(getWatch_right_yn());
		sb.append(GlobalCom.colsep);
		sb.append(getIs_fh());

		return sb.toString();
	}

	public Object clone() throws CloneNotSupportedException{
		GpackCategoryAlbumInfoVO gVo = (GpackCategoryAlbumInfoVO)super.clone();
		return gVo;
	}
}
