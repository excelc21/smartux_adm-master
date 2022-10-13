package com.dmi.smartux.admin.hotvod.vo;

/**
 * 화제동영상 - 조회수 통계 VO
 * @author JKJ
 *
 */
public class HotvodHitstatsVO {
	private int ranking;			//랭킹
	private String content_name;	//컨텐츠명
	private String content_url;		//컨텐츠 URL
	private String parent_cate;		//상위 카테고리(장르)
	private String site_name;		//출처사이트명
	private int s_hit_cnt;			//초기 조회수
	private int i_hit_cnt;			//기간별 조회수
	private int e_hit_cnt;			//누적 조회수
	
	
	public int getRanking() {
		return ranking;
	}
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
	public String getContent_name() {
		return content_name;
	}
	public void setContent_name(String content_name) {
		this.content_name = content_name;
	}
	public String getContent_url() {
		return content_url;
	}
	public void setContent_url(String content_url) {
		this.content_url = content_url;
	}
	public String getParent_cate() {
		return parent_cate;
	}
	public void setParent_cate(String parent_cate) {
		this.parent_cate = parent_cate;
	}
	public String getSite_name() {
		return site_name;
	}
	public void setSite_name(String site_name) {
		this.site_name = site_name;
	}
	public int getS_hit_cnt() {
		return s_hit_cnt;
	}
	public void setS_hit_cnt(int s_hit_cnt) {
		this.s_hit_cnt = s_hit_cnt;
	}
	public int getI_hit_cnt() {
		return i_hit_cnt;
	}
	public void setI_hit_cnt(int i_hit_cnt) {
		this.i_hit_cnt = i_hit_cnt;
	}
	public int getE_hit_cnt() {
		return e_hit_cnt;
	}
	public void setE_hit_cnt(int e_hit_cnt) {
		this.e_hit_cnt = e_hit_cnt;
	}
}
