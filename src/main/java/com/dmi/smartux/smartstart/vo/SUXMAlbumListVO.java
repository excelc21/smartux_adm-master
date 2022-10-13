package com.dmi.smartux.smartstart.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

//@XmlType(name="클래스명" namespace="풀패키지명.클래스명", propOrder={위에서 아래로 보여줘야 할 멤버 순서들})
//@XmlType에서 name과 namespace는 필수, propOrder는 옵션으로 주지 않았을 경우엔 멤버변수 정의된 순서대로 보여준다
@JsonPropertyOrder({"category_id","album_id","album_title","ordered","img_url","w_img", "h_img","series_flag","series_desc","close_yn","is_hd","program_info","onair_date","is_caption","genre1","release_date","watch_right_yn","is_fh","studio","director_display","writer","player","starring_actor","public_cnt","cast_name","producing_date","awrd","run_time","is_51_ch","is_3d","is_uhd"})
@XmlRootElement(name = "record") 	// vo의 XmlRootElement name속성은 record로 통일
@XmlType(name="SUXMAlbumListVO", namespace="com.dmi.smartux.smartstart.vo.SUXMAlbumListVO", propOrder={"category_id","album_id","album_title","ordered","img_url","w_img", "h_img","series_flag","series_desc","close_yn","is_hd","program_info","onair_date","is_caption","genre1","release_date","watch_right_yn","is_fh","studio","director_display","writer","player","starring_actor","public_cnt","cast_name","producing_date","awrd","run_time","is_51_ch","is_3d","is_uhd"})
public class SUXMAlbumListVO extends BaseVO  implements Serializable, Cloneable{ 
    
    //String schedule_code;
	String category_id;
	String album_id;
	String album_title;
	String ordered;
	String img_url;				//이미지 서버 URL
	String w_img;				//가로형 포스터 이미지 URL
	String h_img;				//세로형 포스터 이미지 URL		
	String series_flag;
	String series_desc;
	String is_hd;
	String program_info;
	String onair_date;
	String is_caption;
	String close_yn;			// 종영작 여부
	String genre1;				// 장르 대분류
	String release_date;		// 개봉일
	String watch_right_yn;		// 2ndTV 판권 정보
	String is_fh;				// FullHD 여부
	String studio;
	String director_display;
	String writer;
	String player;
	String starring_actor;
	String public_cnt;
	String cast_name;              
	String producing_date;
	String awrd;     
	String run_time;
	String is_51_ch;
	String is_3d;            
	String is_uhd;
	
	@XmlElement(name="is_fh")
	public void setIs_fh(String is_fh) {
		this.is_fh = is_fh;
	}
	
	public String getIs_fh() {
		return GlobalCom.isNull(is_fh);
	}

	@XmlElement(name="category_id")
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}	

	public String getCategory_id() {
		return GlobalCom.isNull(category_id);
	}
	
	@XmlElement(name="album_id")
	public void setAlbum_id(String album_id) {
		this.album_id = album_id;
	}		
	
	public String getAlbum_id() {
		return GlobalCom.isNull(album_id);
	}	
	
	@XmlElement(name="album_title")
	public void setAlbum_title(String album_title) {
		this.album_title = album_title;
	}		

	public String getAlbum_title() {
		return GlobalCom.isNull(album_title);
	}
	
	@XmlElement(name="ordered")
	public void setOrdered(String ordered) {
		this.ordered = ordered;
	}	
	
	public String getOrdered() {
		return GlobalCom.isNull(ordered);
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
	
	@XmlElement(name="series_flag")
	public void setSeries_flag(String series_flag) {
		this.series_flag = series_flag;
	}		
	
	public String getSeries_flag() {
		return GlobalCom.isNull(series_flag);
	}
	
	@XmlElement(name="series_desc")
	public void setSeries_desc(String series_desc) {
		this.series_desc = series_desc;
	}		
	
	public String getSeries_desc() {
		return GlobalCom.isNull(series_desc);
	}
	
	@XmlElement(name="is_hd")
	public void setIs_hd(String is_hd) {
		this.is_hd = is_hd;
	}		
	
	public String getIs_hd() {
		return GlobalCom.isNull(is_hd);
	}
	
	@XmlElement(name="program_info")
	public void setProgram_info(String program_info) {
		this.program_info = program_info;
	}		
	
	public String getProgram_info() {
		return GlobalCom.isNull(program_info);
	}
	
	@XmlElement(name="onair_date")
	public void setOnair_date(String onair_date) {
		this.onair_date = onair_date;
	}		
	
	public String getOnair_date() {
		return GlobalCom.isNull(onair_date);
	}
		
	@XmlElement(name="is_caption")
	public void setIs_caption(String is_caption) {
		this.is_caption = is_caption;
	}
	
	public String getIs_caption() {
		return GlobalCom.isNull(is_caption);
	}
	
	
	
	/**
	 * 이미지 서버 URL
	 * @return the img_url
	 */
	public String getImg_url() {
		return img_url;
	}

	/**
	 * 이미지 서버 URL 
	 * @param img_url the img_url to set
	 */
	@XmlElement(name="img_url")
	public void setImg_url(String img_url) {
		this.img_url = img_url;
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

	public String getStudio() {
		return GlobalCom.isNull(studio);
	}
	
	@XmlElement(name="studio")
	public void setStudio(String studio) {
		this.studio = studio;
	}

	public String getDirector_display() {
		return GlobalCom.isNull(director_display);
	}
	
	@XmlElement(name="director_display")
	public void setDirector_display(String director_display) {
		this.director_display = director_display;
	}

	public String getWriter() {
		return GlobalCom.isNull(writer);
	}

	@XmlElement(name="writer")
	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getPlayer() {
		return GlobalCom.isNull(player);
	}

	@XmlElement(name="player")
	public void setPlayer(String player) {
		this.player = player;
	}

	public String getStarring_actor() {
		return GlobalCom.isNull(starring_actor);
	}

	@XmlElement(name="starring_actor")
	public void setStarring_actor(String starring_actor) {
		this.starring_actor = starring_actor;
	}

	public String getPublic_cnt() {
		return GlobalCom.isNull(public_cnt);
	}

	@XmlElement(name="public_cnt")
	public void setPublic_cnt(String public_cnt) {
		this.public_cnt = public_cnt;
	}

	public String getCast_name() {
		return GlobalCom.isNull(cast_name);
	}

	@XmlElement(name="cast_name")
	public void setCast_name(String cast_name) {
		this.cast_name = cast_name;
	}

	public String getProducing_date() {
		return GlobalCom.isNull(producing_date);
	}

	@XmlElement(name="producing_date")
	public void setProducing_date(String producing_date) {
		this.producing_date = producing_date;
	}

	public String getAwrd() {
		return GlobalCom.isNull(awrd);
	}

	@XmlElement(name="awrd")
	public void setAwrd(String awrd) {
		this.awrd = awrd;
	}

	public String getRun_time() {
		return GlobalCom.isNull(run_time);
	}

	@XmlElement(name="run_time")
	public void setRun_time(String run_time) {
		this.run_time = run_time;
	}

	public String getIs_51_ch() {
		return GlobalCom.isNull(is_51_ch);
	}

	@XmlElement(name="is_51_ch")
	public void setIs_51_ch(String is_51_ch) {
		this.is_51_ch = is_51_ch;
	}

	public String getIs_3d() {
		return GlobalCom.isNull(is_3d);
	}

	@XmlElement(name="is_3d")
	public void setIs_3d(String is_3d) {
		this.is_3d = is_3d;
	}

	public String getIs_uhd() {
		return GlobalCom.isNull(is_uhd);
	}

	@XmlElement(name="is_uhd")
	public void setIs_uhd(String is_uhd) {
		this.is_uhd = is_uhd;
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
		sb.append(getStudio());
		sb.append(GlobalCom.colsep);
		sb.append(getDirector_display());
		sb.append(GlobalCom.colsep);
		sb.append(getWriter());
		sb.append(GlobalCom.colsep);
		sb.append(getPlayer());
		sb.append(GlobalCom.colsep);
		sb.append(getStarring_actor());
		sb.append(GlobalCom.colsep);
		sb.append(getPublic_cnt());
		sb.append(GlobalCom.colsep);
		sb.append(getCast_name());        
		sb.append(GlobalCom.colsep);
		sb.append(getProducing_date());
		sb.append(GlobalCom.colsep);
		sb.append(getAwrd());
		sb.append(GlobalCom.colsep);
		sb.append(getRun_time());
		sb.append(GlobalCom.colsep);
		sb.append(getIs_51_ch());
		sb.append(GlobalCom.colsep);
		sb.append(getIs_3d());
		sb.append(GlobalCom.colsep);
		sb.append(getIs_uhd());
		
		return sb.toString();	
	}	
	
	public Object clone() throws CloneNotSupportedException{
		SUXMAlbumListVO gVo = (SUXMAlbumListVO)super.clone();
		return gVo;
	}
}