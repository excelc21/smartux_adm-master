package com.dmi.smartux.mainpanel.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

@XmlRootElement(name="result")
@XmlType(name="AlbumInfoVO", namespace="com.dmi.smartux.mainpanel.vo.AlbumInfoVO"
, propOrder={"rank_no", "album_id", "cat_id", "album_title","img_url", "w_img", "h_img", "series_flag", "series_desc","close_yn", "is_hd", "program_info", "onair_date", "is_caption","genre1","release_date","watch_right_yn","is_fh", "service_gb"})
public class AlbumInfoVO extends BaseVO implements Serializable, Cloneable {

	String rank_no;				// 랭킹 순위
	String album_id;			// 앨범 id
	String cat_id;				// 카테고리 id
	String album_title;			// 앨범 제목
	String img_url;				// 이미지 서버 URL
	String w_img;				//가로형 포스터 이미지 URL
	String h_img;				//세로형 포스터 이미지 URL	
	String series_flag;			// 시리즈여부(N : 단편, Y : 시리즈)
	String series_desc;			// 회차 설명
	String is_hd;				// HD 영상 구분(Y : HD, N : SD)
	String program_info;		// 시청가능연령(01 : 일반, 02 : 7세이상, 03 : 12세이상, 04 : 15세이상, 05 : 19세이상, 06 : 방송불가)
	String onair_date;			// 방영일
	String is_caption;			// 자막여부(N:없음, Y:있음, D:더빙)
	String close_yn;			// 종영작 여부
	String genre1;				// 장르 대분류
	String release_date;		// 개봉일
	String watch_right_yn;		// 2ndTV 판권 정보
	String is_fh;				// FullHD 여부
	String service_gb;			// 서비스 구분
	
	@XmlElement(name="is_fh")
	public void setIs_fh(String is_fh) {
		this.is_fh = is_fh;
	}
	
	public String getIs_fh() {
		return GlobalCom.isNull(is_fh);
	}
	
	public String getRank_no() {
		return GlobalCom.isNull(rank_no);
	}
	
	@XmlElement(name="rank_no")
	public void setRank_no(String rank_no) {
		this.rank_no = rank_no;
	}
	
	public String getAlbum_id() {
		return GlobalCom.isNull(album_id);
	}
	@XmlElement(name="album_id")
	public void setAlbum_id(String album_id) {
		this.album_id = album_id;
	}
	
	public String getCat_id() {
		return GlobalCom.isNull(cat_id);
	}
	
	@XmlElement(name="cat_id")
	public void setCat_id(String cat_id) {
		this.cat_id = cat_id;
	}
	
	public String getAlbum_title() {
		return GlobalCom.isNull(album_title);
	}
	
	@XmlElement(name="album_title")
	public void setAlbum_title(String album_title) {
		this.album_title = album_title;
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
	
	
	
	/**
	 * @return the img_url
	 */
	public String getImg_url() {
		return img_url;
	}

	/**
	 * @param img_url the img_url to set
	 */
	@XmlElement(name="img_url")
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		
		boolean result = true;
		AlbumInfoVO dest = (AlbumInfoVO)obj;
		
		if(!(getAlbum_id().equals(dest.getAlbum_id()))){
			result = false;
		}
		
		if(!(getCat_id().equals(dest.getCat_id()))){
			result = false;
		}
		
		if(!(getAlbum_title().equals(dest.getAlbum_title()))){
			result = false;
		}
		
		if(!(getImg_url().equals(dest.getImg_url()))){
			result = false;
		}
		
		if(!(getW_img().equals(dest.getW_img()))){
			result = false;
		}
		
		if(!(getH_img().equals(dest.getH_img()))){
			result = false;
		}			
		
		if(!(getSeries_flag().equals(dest.getSeries_flag()))){
			result = false;
		}
		
		if(!(getSeries_desc().equals(dest.getSeries_desc()))){
			result = false;
		}
		
		if(!(getRank_no().equals(dest.getRank_no()))){
			result = false;
		}
		
		if(!(getIs_hd().equals(dest.getIs_hd()))){
			result = false;
		}
		
		if(!(getProgram_info().equals(dest.getProgram_info()))){
			result = false;
		}
		
		if(!(getOnair_date().equals(dest.getOnair_date()))){
			result = false;
		}
		
		if(!(getIs_caption().equals(dest.getIs_caption()))){
			result = false;
		}
		
		return result;
	}
	
	/**
	 * @return the close_yn
	 */
	public String getClose_yn() {
		return close_yn;
	}

	/**
	 * @param close_yn the close_yn to set
	 */
	@XmlElement(name="close_yn")
	public void setClose_yn(String close_yn) {
		this.close_yn = close_yn;
	}
	
	/**
	 * @return the genre1
	 */
	public String getGenre1() {
		return GlobalCom.isNull(genre1);
	}

	/**
	 * @param genre1 the genre1 to set
	 */
	@XmlElement(name="genre1")
	public void setGenre1(String genre1) {
		this.genre1 = genre1;
	}

	/**
	 * @return the release_date
	 */
	public String getRelease_date() {
		return GlobalCom.isNull(release_date);
	}

	/**
	 * @param release_date the release_date to set
	 */
	@XmlElement(name="release_date")
	public void setRelease_date(String release_date) {
		this.release_date = release_date;
	}
	
	public String getWatch_right_yn() {
		return watch_right_yn;
	}
	@XmlElement(name="watch_right_yn")
	public void setWatch_right_yn(String watch_right_yn) {
		this.watch_right_yn = watch_right_yn;
	}

	public String getService_gb() {
		return GlobalCom.isNull(service_gb);
	}

	@XmlElement(name="service_gb")
	public void setService_gb(String service_gb) {
		this.service_gb = service_gb;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		
		StringBuffer sb = new StringBuffer();
		sb.append(getRank_no());
		sb.append(GlobalCom.colsep);
		sb.append(getAlbum_id());
		sb.append(GlobalCom.colsep);
		sb.append(getCat_id());
		sb.append(GlobalCom.colsep);
		sb.append(getAlbum_title());
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
		sb.append(GlobalCom.colsep);
		sb.append(getService_gb());

		return sb.toString();
	}
	
	public Object clone() throws CloneNotSupportedException{
		AlbumInfoVO gVo = (AlbumInfoVO)super.clone();
		return gVo;
	}
	
	
	
	
}
