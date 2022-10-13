package com.dmi.smartux.gpack.promotion.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

@XmlRootElement(name="record_vod")
@XmlType(name="GpackPromotionContentsVO", namespace="com.dmi.smartux.gpack.promotion.vo.GpackPromotionContentsVO"
, propOrder={"type", "title", "imgurl", "img", "link", "service_id", "category_id", "product_code", "dal_type"
		, "album_id", "cat_id", "album_title", "ordered", "img_url", "w_img", "h_img", "series_flag", "series_desc", "series_no", "close_yn", "is_hd", "program_info"
		, "onair_date", "is_caption", "genre1", "release_date", "watch_right_yn", "is_fh"})
public class GpackPromotionContentsVO extends BaseVO implements Serializable, Cloneable{
	
	String type = "";
	String title = "";
	String imgurl = "";
	String img = "";
	String link = "";
	
	String service_id = "";
	String category_id = "";
	String product_code = "";
	String dal_type = "";
	String album_id = "";
	String cat_id = "";
	String album_title = "";
	
	String ordered = "";
	String img_url = "";
	String w_img = "";
	String h_img = "";
	String series_flag = "";
	
	String series_desc = "";
	String series_no = "";
	String close_yn = "";
	String is_hd = "";
	String program_info = "";
	String onair_date = "";
	
	String is_caption = "";
	String genre1 = "";
	String release_date = "";
	String watch_right_yn = "";
	String is_fh = "";

	public String getType() {
		return GlobalCom.isNull(type);
	}
	@XmlElement(name="type")
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return GlobalCom.isNull(title);
	}
	@XmlElement(name="title")
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImgurl() {
		return GlobalCom.isNull(imgurl);
	}
	@XmlElement(name="imgurl")
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getImg() {
		return GlobalCom.isNull(img);
	}
	@XmlElement(name="img")
	public void setImg(String img) {
		this.img = img;
	}
	public String getLink() {
		return GlobalCom.isNull(link);
	}
	@XmlElement(name="link")
	public void setLink(String link) {
		this.link = link;
	}
	public String getService_id() {
		return GlobalCom.isNull(service_id);
	}
	@XmlElement(name="service_id")
	public void setService_id(String service_id) {
		this.service_id = service_id;
	}
	public String getCategory_id() {
		return GlobalCom.isNull(category_id);
	}
	@XmlElement(name="category_id")
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getProduct_code() {
		return product_code;
	}
	@XmlElement(name="product_code")
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public String getDal_type() {
		return dal_type;
	}
	@XmlElement(name="dal_type")
	public void setDal_type(String dal_type) {
		this.dal_type = dal_type;
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
	public String getOrdered() {
		return ordered;
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
		sb.append(GlobalCom.isNull(getType()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getTitle()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getImgurl()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getImg()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getLink()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getService_id()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getCategory_id()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getProduct_code()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getDal_type()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getAlbum_id()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getCat_id()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getAlbum_title()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getOrdered()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getImg_url()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getW_img()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getH_img()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getSeries_flag()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getSeries_desc()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getSeries_no()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getClose_yn()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getIs_hd()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getProgram_info()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getOnair_date()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getIs_caption()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getGenre1()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getRelease_date()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getWatch_right_yn()));
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getIs_fh()));
		
		return sb.toString();
	}
	
	public Object clone() throws CloneNotSupportedException{
		GpackPromotionContentsVO gVo = (GpackPromotionContentsVO)super.clone();
		return gVo;
	}
}
