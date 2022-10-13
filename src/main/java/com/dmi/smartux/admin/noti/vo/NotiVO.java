package com.dmi.smartux.admin.noti.vo;

import java.util.List;

public class NotiVO {

	private String reg_no; // 게시글 등록번호
	private String bbs_id; // 게시판 아이디
	private String bbs_gbn;
	private String title; // 공지제목
	private String content;// 공지내용
	private int is_fixed;// 상단고정여부
	private String s_date;// 공지(이벤트) 시작일시
	private String e_date;// 공지(이벤트) 종료일시
	private String n_date;// "New" 디스플레이 마감일
	private String ev_cont_id;// 이벤트 컨텐츠 아이디
	private String win_dt;// 당첨자 발표일
	private String org_file_nm;// 원파일명
	private String save_file_nm;// 저장파일명
	private String reg_dt;// 등록일시
	private String reg_id;// 등록자 아이디
	private String mod_dt;// 수정일시
	private String mod_id;// 수정자 아이디
	private String[] term_no;// 적용 단말기 종류
	private String terminal_list;
	private String bbs_col_li; // 게시판별 컬럼 리스트
	private String is_adt; // 검수 여부
	private String scr_tp; // screen_type
	private String act_ip;
	private String act_gbn;
	private String act_id;

	private String ev_type;// 이벤트 종류
	private String ev_detail;// 이벤트 상세 데이터
	private String ev_detail_prod;// ev_type이 가입하기일때 ev_detail가 1차로 여기로 들어옴
	private String ev_detail_cont;// ev_type이 컨텐츠 연동일때 ev_detail가 1차로 여기로 들어옴
	private String ev_stat_id;// 참여통계

	private List<String> bbs_col_li_array;
	private List<String> bbs_col_type_array;
	
	private String noti_title;  // ev_type이 공지/이벤트 게시글일때 게시글 제목
	private String noti_detail;// ev_type이 공지/이벤트 게시글일때 noti_detail가 1차로 여기로 들어옴
	private String noti_detail_name;// ev_type이 공지/이벤트 게시글일때 noti_detail가 1차로 여기로 들어옴
	private String show_type;// 

	private String display_type; //2020-06-05 노출타입 추가
	
	public String getAct_ip ( ) {
		return act_ip;
	}

	public void setAct_ip ( String act_ip ) {
		this.act_ip = act_ip;
	}

	public String getAct_id ( ) {
		return act_id;
	}

	public void setAct_id ( String act_id ) {
		this.act_id = act_id;
	}

	public String getAct_gbn ( ) {
		return act_gbn;
	}

	public void setAct_gbn ( String act_gbn ) {
		this.act_gbn = act_gbn;
	}

	public String getScr_tp ( ) {
		return scr_tp;
	}

	public void setScr_tp ( String scr_tp ) {
		this.scr_tp = scr_tp;
	}

	public String getIs_adt ( ) {
		return is_adt;
	}

	public void setIs_adt ( String is_adt ) {
		this.is_adt = is_adt;
	}

	public String getTerminal_list ( ) {
		return terminal_list;
	}

	public void setTerminal_list ( String terminal_list ) {
		this.terminal_list = terminal_list;
	}

	public String getBbs_gbn ( ) {
		return bbs_gbn;
	}

	public void setBbs_gbn ( String bbs_gbn ) {
		this.bbs_gbn = bbs_gbn;
	}

	public List<String> getBbs_col_li_array ( ) {
		return bbs_col_li_array;
	}

	public void setBbs_col_li_array ( List<String> bbs_col_li_array ) {
		this.bbs_col_li_array = bbs_col_li_array;
	}

	public List<String> getBbs_col_type_array ( ) {
		return bbs_col_type_array;
	}

	public void setBbs_col_type_array ( List<String> bbs_col_type_array ) {
		this.bbs_col_type_array = bbs_col_type_array;
	}

	public String getBbs_col_li ( ) {
		return bbs_col_li;
	}

	public void setBbs_col_li ( String bbs_col_li ) {
		this.bbs_col_li = bbs_col_li;
	}

	public String getReg_no ( ) {
		return reg_no;
	}

	public void setReg_no ( String reg_no ) {
		this.reg_no = reg_no;
	}

	public String getBbs_id ( ) {
		return bbs_id;
	}

	public void setBbs_id ( String bbs_id ) {
		this.bbs_id = bbs_id;
	}

	public String getTitle ( ) {
		return title;
	}

	public void setTitle ( String title ) {
		this.title = title;
	}

	public String getContent ( ) {
		return content;
	}

	public void setContent ( String content ) {
		this.content = content;
	}

	public int getIs_fixed ( ) {
		return is_fixed;
	}

	public void setIs_fixed ( int is_fixed ) {
		this.is_fixed = is_fixed;
	}

	public String getS_date ( ) {
		return s_date;
	}

	public void setS_date ( String s_date ) {
		this.s_date = s_date;
	}

	public String getE_date ( ) {
		return e_date;
	}

	public void setE_date ( String e_date ) {
		this.e_date = e_date;
	}

	public String getWin_dt ( ) {
		return win_dt;
	}

	public void setWin_dt ( String win_dt ) {
		this.win_dt = win_dt;
	}

	public String getN_date ( ) {
		return n_date;
	}

	public void setN_date ( String n_date ) {
		this.n_date = n_date;
	}

	public String getEv_cont_id ( ) {
		return ev_cont_id;
	}

	public void setEv_cont_id ( String ev_cont_id ) {
		this.ev_cont_id = ev_cont_id;
	}

	public String getOrg_file_nm ( ) {
		return org_file_nm;
	}

	public void setOrg_file_nm ( String org_file_nm ) {
		this.org_file_nm = org_file_nm;
	}

	public String getSave_file_nm ( ) {
		return save_file_nm;
	}

	public void setSave_file_nm ( String save_file_nm ) {
		this.save_file_nm = save_file_nm;
	}

	public String getReg_dt ( ) {
		return reg_dt;
	}

	public void setReg_dt ( String reg_dt ) {
		this.reg_dt = reg_dt;
	}

	public String getReg_id ( ) {
		return reg_id;
	}

	public void setReg_id ( String reg_id ) {
		this.reg_id = reg_id;
	}

	public String getMod_dt ( ) {
		return mod_dt;
	}

	public void setMod_dt ( String mod_dt ) {
		this.mod_dt = mod_dt;
	}

	public String getMod_id ( ) {
		return mod_id;
	}

	public void setMod_id ( String mod_id ) {
		this.mod_id = mod_id;
	}

	public String[] getTerm_no ( ) {
		return term_no;
	}

	public void setTerm_no ( String[] term_no ) {
		this.term_no = term_no;
	}

	public String getEv_type ( ) {
		return ev_type;
	}

	public void setEv_type ( String ev_type ) {
		this.ev_type = ev_type;
	}

	public String getEv_detail ( ) {
		return ev_detail;
	}

	public void setEv_detail ( String ev_detail ) {
		this.ev_detail = ev_detail;
	}

	public String getEv_detail_prod ( ) {
		return ev_detail_prod;
	}

	public void setEv_detail_prod ( String ev_detail_prod ) {
		this.ev_detail_prod = ev_detail_prod;
	}

	public String getEv_detail_cont ( ) {
		return ev_detail_cont;
	}

	public void setEv_detail_cont ( String ev_detail_cont ) {
		this.ev_detail_cont = ev_detail_cont;
	}

	public String getEv_stat_id ( ) {
		return ev_stat_id;
	}

	public void setEv_stat_id ( String ev_stat_id ) {
		this.ev_stat_id = ev_stat_id;
	}

	public String getNoti_detail ( ) {
		return noti_detail;
	}

	public void setNoti_detail ( String noti_detail ) {
		this.noti_detail = noti_detail;
	}

	public String getNoti_title ( ) {
		return noti_title;
	}

	public void setNoti_title ( String noti_title ) {
		this.noti_title = noti_title;
	}

	public String getNoti_detail_name ( ) {
		return noti_detail_name;
	}

	public void setNoti_detail_name ( String noti_detail_name ) {
		this.noti_detail_name = noti_detail_name;
	}
	
	public String getShow_type() {
		return show_type;
	}

	public void setShow_type(String show_type) {
		this.show_type = show_type;
	}
	
	public String getDisplay_type() {
		return display_type;
	}

	public void setDisplay_type(String display_type) {
		this.display_type = display_type;
	}	
}
