package com.dmi.smartux.admin.hotvod.vo;

import java.util.List;

import com.dmi.smartux.common.util.GlobalCom;

/**
 * 화제동영상 - 컨텐츠관리 VO
 * @author JKJ
 */
public class HotvodContentVO {
	private String content_id;			//컨텐츠 아이디
	private String content_type;		//컨텐츠 구분
	private String parent_id;			//상위 컨텐츠 아이디
	private String pre_parent_id;       //이전 상위컨텐츠 ID
	private String content_name;		//컨텐츠명
	private String content_info;		//컨텐츠 설명
	private String content_order;		//컨텐츠 순서
	private String content_img;			//컨텐츠 이미지(HDTV)
	private String content_img_url;		//컨텐츠 이미지 URL(HDTV)
	private String content_img_tv;		//컨텐츠 이미지(IPTV)
	private String content_img_tv_url;	//컨텐츠 이미지URL(IPTV)
	private String content_url;			//컨텐츠 url
	private String duration;			//상영시간
	private String hit_cnt;				//조회수
	private String site_id;				//출처 사이트 아이디
	private String reg_dt;				//등록일시
	private String reg_id;				//등록자 아이디
	private String mod_dt;				//수정일시
	private String mod_id;				//수정자 아이디
	private String parent_name;			//상위 컨텐츠명
	private String site_name;			//출처 사이트명
	private String level;				//컨텐츠 레벨
	private String level_pad;			//컨텐츠 레벨 패딩
	private String sub_cnt;				//하위 목록 갯수
	private String sub_content_cnt;		//최상위 레벨의 컨텐츠(카테고리가 아닌 컨텐츠) 갯수
	private String category_id;			//카테고리 아이디
	private String album_id;			//앨범 아이디
	private String contents_name;		//시리즈명
	private String series_no;			//시리즈번호
	private String sponsor_name;		//방송사
	private String still_img;			//스틸컷
	private String start_time;			//영상 시작시간
	private String end_time;			//영상 종료시간
	private String content_img_hl;		//하이라이트 컨텐츠 이미지
	private String del_yn;				//노출여부 del_yn은 원래 삭제여부였으나 고도화로 인해 재활용하여 노출여부로 사용한다 N:노출, Y:비노출
	private String root_id;				//최상위 카테고리 ID
	private String badge_data;
	private String test_yn;				//검수단말기 사용여부
	private String unify_search_yn;     //통합검색여부
	private String multi_service_type;		//다중선택 서비스타입	
	private String start_dt;			//노출 시작일
	private String end_dt;				//노출 종료일
		
	private List<String> content_id_list;
	
	//	2019.11.08 IPTV 브릿지홈 개편으로 추가
	private String hv_ui_type;	//화제동영상 UI타입
	private String category_name;			//카테고리 명
	
	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getHv_ui_type() {
		return hv_ui_type;
	}

	public void setHv_ui_type(String hv_ui_type) {
		this.hv_ui_type = hv_ui_type;
	}

	public String getBadge_data() {
		return badge_data;
	}

	public void setBadge_data(String badge_data) {
		this.badge_data = badge_data;
	}

	public String getContent_id() {
		return content_id;
	}

	public void setContent_id(String content_id) {
		this.content_id = content_id;
	}

	public String getContent_type() {
		return content_type;
	}

	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getPre_parent_id() {
		return pre_parent_id;
	}

	public void setPre_parent_id(String pre_parent_id) {
		this.pre_parent_id = pre_parent_id;
	}

	public String getContent_name() {
		return content_name;
	}

	public void setContent_name(String content_name) {
		this.content_name = content_name;
	}

	public String getContent_info() {
		return content_info;
	}

	public void setContent_info(String content_info) {
		this.content_info = content_info;
	}

	public String getContent_order() {
		return content_order;
	}

	public void setContent_order(String content_order) {
		this.content_order = content_order;
	}

	public String getContent_img() {
		return content_img;
	}

	public void setContent_img(String content_img) {
		this.content_img = content_img;
	}

	public String getContent_img_tv() {
		return content_img_tv;
	}

	public void setContent_img_tv(String content_img_tv) {
		this.content_img_tv = content_img_tv;
	}

	public String getContent_url() {
		return content_url;
	}

	public void setContent_url(String content_url) {
		this.content_url = content_url;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getHit_cnt() {
		return hit_cnt;
	}

	public void setHit_cnt(String hit_cnt) {
		this.hit_cnt = hit_cnt;
	}

	public String getSite_id() {
		return site_id;
	}

	public void setSite_id(String site_id) {
		this.site_id = site_id;
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

	public String getParent_name() {
		return parent_name;
	}

	public void setParent_name(String parent_name) {
		this.parent_name = parent_name;
	}

	public String getSite_name() {
		return site_name;
	}

	public void setSite_name(String site_name) {
		this.site_name = site_name;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getLevel_pad() {
		return level_pad;
	}

	public void setLevel_pad(String level_pad) {
		this.level_pad = level_pad;
	}

	public String getSub_cnt() {
		return sub_cnt;
	}

	public void setSub_cnt(String sub_cnt) {
		this.sub_cnt = sub_cnt;
	}

	public String getSub_content_cnt() {
		return sub_content_cnt;
	}

	public void setSub_content_cnt(String sub_content_cnt) {
		this.sub_content_cnt = sub_content_cnt;
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

	public String getContents_name() {
		return contents_name;
	}

	public void setContents_name(String contents_name) {
		this.contents_name = contents_name;
	}

	public String getSeries_no() {
		return series_no;
	}

	public void setSeries_no(String series_no) {
		this.series_no = series_no;
	}

	public String getSponsor_name() {
		return sponsor_name;
	}

	public void setSponsor_name(String sponsor_name) {
		this.sponsor_name = sponsor_name;
	}

	public String getStill_img() {
		return still_img;
	}

	public void setStill_img(String still_img) {
		this.still_img = still_img;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getContent_img_url() {
		return content_img_url;
	}

	public void setContent_img_url(String content_img_url) {
		this.content_img_url = content_img_url;
	}

	public String getContent_img_tv_url() {
		return content_img_tv_url;
	}

	public void setContent_img_tv_url(String content_img_tv_url) {
		this.content_img_tv_url = content_img_tv_url;
	}

	public String getContent_img_hl() {
		return content_img_hl;
	}

	public void setContent_img_hl(String content_img_hl) {
		this.content_img_hl = content_img_hl;
	}

	public String getDel_yn() {
		return del_yn;
	}

	public void setDel_yn(String del_yn) {
		this.del_yn = GlobalCom.isNull(del_yn, "N");
	}

	public String getRoot_id() {
		return root_id;
	}

	public void setRoot_id(String root_id) {
		this.root_id = root_id;
	}

	public String getTest_yn() {
		return test_yn;
	}

	public void setTest_yn(String test_yn) {
		this.test_yn = test_yn;
	}

	public String getUnify_search_yn() {
		return unify_search_yn;
	}

	public void setUnify_search_yn(String unify_search_yn) {
		this.unify_search_yn = unify_search_yn;
	}
	
	public String getMulti_service_type() {
		return multi_service_type;
	}

	public void setMulti_service_type(String multi_service_type) {
		this.multi_service_type = multi_service_type;
	}

	public List<String> getContent_id_list() {
		return content_id_list;
	}

	public void setContent_id_list(List<String> content_id_list) {
		this.content_id_list = content_id_list;
	}
	
	public String getStart_dt() {
		return start_dt;
	}

	public void setStart_dt(String start_dt) {
		this.start_dt = start_dt;
	}

	public String getEnd_dt() {
		return end_dt;
	}

	public void setEnd_dt(String end_dt) {
		this.end_dt = end_dt;
	}

	@Override
	public String toString() {
		return "HotvodContentVO [content_id=" + content_id + ", content_type=" + content_type + ", parent_id="
				+ parent_id + ", pre_parent_id=" + pre_parent_id + ", content_name=" + content_name + ", content_info="
				+ content_info + ", content_order=" + content_order + ", content_img=" + content_img
				+ ", content_img_url=" + content_img_url + ", content_img_tv=" + content_img_tv
				+ ", content_img_tv_url=" + content_img_tv_url + ", content_url=" + content_url + ", duration="
				+ duration + ", hit_cnt=" + hit_cnt + ", site_id=" + site_id + ", reg_dt=" + reg_dt + ", reg_id="
				+ reg_id + ", mod_dt=" + mod_dt + ", mod_id=" + mod_id + ", parent_name=" + parent_name + ", site_name="
				+ site_name + ", level=" + level + ", level_pad=" + level_pad + ", sub_cnt=" + sub_cnt
				+ ", sub_content_cnt=" + sub_content_cnt + ", category_id=" + category_id + ", album_id=" + album_id
				+ ", contents_name=" + contents_name + ", series_no=" + series_no + ", sponsor_name=" + sponsor_name
				+ ", still_img=" + still_img + ", start_time=" + start_time + ", end_time=" + end_time
				+ ", content_img_hl=" + content_img_hl + ", del_yn=" + del_yn + ", root_id=" + root_id + ", badge_data="
				+ badge_data + ", test_yn=" + test_yn + ", unify_search_yn=" + unify_search_yn + ", multi_service_type="
				+ multi_service_type + ", content_id_list=" + content_id_list + "start_dt=" + start_dt +", end_dt=" + end_dt +"]"; 
	}
	
}
