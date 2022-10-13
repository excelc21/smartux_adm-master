package com.dmi.smartux.admin.mainpanel.vo;

import java.util.List;
import com.dmi.smartux.admin.commonMng.vo.FlatRateVO;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

public class ViewVO extends BaseVO{

	String pannel_id;			// 구분
	String pannel_nm;		// 구분
	String title_id;			// 지면 id
	String title_nm;			// 지면명
	String p_title_id;			// 상위 지면 id
	String category_id;			// 카테고리 id
	String category_desc;		// 카테고리 detail
	String album_desc;			// 카테고리에 대한 앨범 detail
	int ordered;				// 순서
	String level;				// Level
	String use_yn;				// 사용여부
	String del_yn;				// 사용여부
	String version;				// 버전
	String created;				// 입력일
	String updated;				// 수정일
	String create_id;			// 입력자
	String update_id;			// 수정자
	String existsub;			// 하위지면 존재여부(Y/N)
	String album_cnt;			// 카테고리 id에서 보여줘야 할 앨범들의 갯수
	String ui_type;				// UI 타입
	String description;			// 지면 설명
	String page_type;			// 지면 타입
	String page_code;			// 지면 코드
	String title_color;			// 지면명 색상
	String title_bg_img_file;	// 지면 이미지 파일
	String img_url;				// 이미지 URL 
	String category_gb;			//카테고리 구분
	String category_code;		// 카테고리ID 또는 배너마스터ID
	String category_des;		// 배너마스터명
	String reps_album_id;		// 대표컨텐츠(앨범ID)
	String reps_album_name;		// 대표컨텐츠(앨범이름)
	String reps_category_id;    // 대표컨텐츠(카테고리ID)
	String trailer_viewing_type;		// 예고편노출타입
	String reps_trailer_viewing_type;	// 대표컨텐츠 예고편노출타입
	String bg_img_file;			// 지면 아이콘 이미지
	String bg_img_file2;		// 지면 아이콘 이미지2
	String bg_img_file3;		// 지면 아이콘 이미지3
	String location_yn;			// 국내/해외 구분
	String location_code;		// 지역코드
	String focus_yn;
/* 2019.11.04 : 지면 UI 타입 추가 Start - 이태광 */	
	String paper_ui_type;		// 지면 UI 타입
	String abtest_yn;
	String product_code;		//월정액 상품
	String product_code_not;	//월정액 상품(비노출)
	private List<FlatRateVO> product_code_list; 	/* 월정액상품코드목록*/
	private List<FlatRateVO> product_code_list_not; /* 월정액상품코드목록(비노출)*/
	String show_cnt;			//노출갯수
	String terminal_arr;		//노출단말
	
	public String getTerminal_arr() {
		return terminal_arr;
	}
	public void setTerminal_arr(String terminal_arr) {
		this.terminal_arr = terminal_arr;
	}
	public String getShow_cnt() {
		return show_cnt;
	}
	public void setShow_cnt(String show_cnt) {
		this.show_cnt = show_cnt;
	}
	public String getProduct_code_not() {
		return product_code_not;
	}
	public void setProduct_code_not(String product_code_not) {
		this.product_code_not = product_code_not;
	}
	public List<FlatRateVO> getProduct_code_list_not() {
		return product_code_list_not;
	}
	public void setProduct_code_list_not(List<FlatRateVO> product_code_list_not) {
		this.product_code_list_not = product_code_list_not;
	}
	public List<FlatRateVO> getProduct_code_list() {
		return product_code_list;
	}
	public void setProduct_code_list(List<FlatRateVO> product_code_list) {
		this.product_code_list = product_code_list;
	}
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public String getPaper_ui_type() {
		return paper_ui_type;
	}
	public void setPaper_ui_type(String paper_ui_type) {
		this.paper_ui_type = paper_ui_type;
	}
/* 2019.11.04 : 지면 UI 타입 추가 End - 이태광 */	

	
		
	public String getPannel_id() {
		return GlobalCom.isNull(pannel_id);
	}
	public void setPannel_id(String pannel_id) {
		this.pannel_id = pannel_id;
	}
	public String getPannel_nm() {
		return pannel_nm;
	}
	public void setPannel_nm(String pannel_nm) {
		this.pannel_nm = pannel_nm;
	}
	public String getTitle_id() {
		return GlobalCom.isNull(title_id);
	}
	public void setTitle_id(String title_id) {
		this.title_id = title_id;
	}
	public String getTitle_nm() {
		return GlobalCom.isNull(title_nm);
	}
	public void setTitle_nm(String title_nm) {
		this.title_nm = title_nm;
	}
	public String getP_title_id() {
		return GlobalCom.isNull(p_title_id);
	}
	public void setP_title_id(String p_title_id) {
		this.p_title_id = p_title_id;
	}
	public String getCategory_id() {
		return GlobalCom.isNull(category_id);
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getCategory_desc() {
		return GlobalCom.isNull(category_desc);
	}
	public void setCategory_desc(String category_desc) {
		this.category_desc = category_desc;
	}
	public String getAlbum_desc() {
		return GlobalCom.isNull(album_desc);
	}
	public void setAlbum_desc(String album_desc) {
		this.album_desc = album_desc;
	}
	public int getOrdered() {
		return ordered;
	}
	public void setOrdered(int ordered) {
		this.ordered = ordered;
	}
	public String getLevel() {
		return GlobalCom.isNull(level);
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getUse_yn() {
		return GlobalCom.isNull(use_yn);
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}
	public String getDel_yn() {
		return GlobalCom.isNull(del_yn);
	}
	public void setDel_yn(String del_yn) {
		this.del_yn = del_yn;
	}
	public String getVersion() {
		return GlobalCom.isNull(version);
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCreated() {
		return GlobalCom.isNull(created);
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getUpdated() {
		return GlobalCom.isNull(updated);
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}
	public String getCreate_id() {
		return GlobalCom.isNull(create_id);
	}
	public void setCreate_id(String create_id) {
		this.create_id = create_id;
	}
	public String getUpdate_id() {
		return GlobalCom.isNull(update_id);
	}
	public void setUpdate_id(String update_id) {
		this.update_id = update_id;
	}
	public String getExistsub() {
		return GlobalCom.isNull(existsub);
	}
	public void setExistsub(String existsub) {
		this.existsub = existsub;
	}
	public String getAlbum_cnt() {
		return GlobalCom.isNull(album_cnt);
	}
	public void setAlbum_cnt(String album_cnt) {
		this.album_cnt = album_cnt;
	}
	public String getUi_type() {
		return GlobalCom.isNull(ui_type);
	}
	public void setUi_type(String ui_type) {
		this.ui_type = ui_type;
	}
	public String getDescription() {
		return GlobalCom.isNull(description);
	}
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the page_type
	 */
	public String getPage_type() {
		return page_type;
	}
	/**
	 * @param page_type the page_type to set
	 */
	public void setPage_type(String page_type) {
		this.page_type = page_type;
	}
	/**
	 * @return the page_code
	 */
	public String getPage_code() {
		return page_code;
	}
	/**
	 * @param page_code the page_code to set
	 */
	public void setPage_code(String page_code) {
		this.page_code = page_code;
	}
	
	public String getTitle_color() {
		return title_color;
	}
	
	public void setTitle_color(String title_color) {
		this.title_color = title_color;
	}
	public String getTitle_bg_img_file() {
		return title_bg_img_file;
	}
	public void setTitle_bg_img_file(String title_bg_img_file) {
		this.title_bg_img_file = title_bg_img_file;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public String getCategory_gb() {
		return category_gb;
	}
	public void setCategory_gb(String category_gb) {
		this.category_gb = category_gb;
	}

	public String getCategory_code() {
		return category_code;
	}
	public void setCategory_code(String category_code) {
		this.category_code = category_code;
	}

	public String getCategory_des() {
		return category_des;
	}
	public void setCategory_des(String category_des) {
		this.category_des = category_des;
	}
	public String getReps_album_id() {
		return GlobalCom.isNull(reps_album_id);
	}
	public void setReps_album_id(String reps_album_id) {
		this.reps_album_id = reps_album_id;
	}
	public String getReps_album_name() {
		return GlobalCom.isNull(reps_album_name);
	}
	public void setReps_album_name(String reps_album_name) {
		this.reps_album_name = reps_album_name;
	}
	public String getReps_category_id() {
		return GlobalCom.isNull(reps_category_id);
	}
	public void setReps_category_id(String reps_category_id) {
		this.reps_category_id = reps_category_id;
	}
	public String getTrailer_viewing_type() {
		return trailer_viewing_type;
	}
	public void setTrailer_viewing_type(String trailer_viewing_type) {
		this.trailer_viewing_type = trailer_viewing_type;
	}
	public String getReps_trailer_viewing_type() {
		return reps_trailer_viewing_type;
	}
	public void setReps_trailer_viewing_type(String reps_trailer_viewing_type) {
		this.reps_trailer_viewing_type = reps_trailer_viewing_type;
	}
	public String getBg_img_file() {
		return bg_img_file;
	}
	public void setBg_img_file(String bg_img_file) {
		this.bg_img_file = bg_img_file;
	}
	public String getBg_img_file2() {
		return bg_img_file2;
	}
	public void setBg_img_file2(String bg_img_file2) {
		this.bg_img_file2 = bg_img_file2;
	}
	public String getBg_img_file3() {
		return bg_img_file3;
	}
	public void setBg_img_file3(String bg_img_file3) {
		this.bg_img_file3 = bg_img_file3;
	}
	public String getLocation_yn() {
		return location_yn;
	}
	public void setLocation_yn(String location_yn) {
		this.location_yn = location_yn;
	}
	public String getLocation_code() {
		return location_code;
	}
	public void setLocation_code(String location_code) {
		this.location_code = location_code;
	}
		
	public String getFocus_yn() {
		return focus_yn;
	}
	public void setFocus_yn(String focus_yn) {
		this.focus_yn = focus_yn;
	}
	public String getAbtest_yn() {
		return abtest_yn;
	}
	public void setAbtest_yn(String abtest_yn) {
		this.abtest_yn = abtest_yn;
	}
	

	
	
}
