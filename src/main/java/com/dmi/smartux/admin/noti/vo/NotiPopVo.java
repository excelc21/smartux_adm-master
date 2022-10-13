package com.dmi.smartux.admin.noti.vo;

public class NotiPopVo {

	private String rowno;// 번호
	private String reg_no; // 게시글 등록번호
	private String bbs_id; // 게시판 아이디
	private String title; // 공지제목
	private String s_date;// 공지(이벤트) 시작일시
	private String e_date;// 공지(이벤트) 종료일시
	private String n_date;// "New" 디스플레이 마감일
	private String win_dt;// 당첨자 발표일
	private String save_file_nm;// 저장파일명
	private String reg_dt;// 등록일시
	private String reg_id;// 등록자 아이디
	private String mod_dt;// 수정일시
	private String mod_id;// 수정자 아이디
	private String is_adt; // 검수 여부
	private String ev_type;// 이벤트 종류
	private String ev_stat_id;// 참여통계
	private String bbs_nm;// BBS명

	public String getRowno ( ) {
		return rowno;
	}

	public void setRowno ( String rowno ) {
		this.rowno = rowno;
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

	public String getN_date ( ) {
		return n_date;
	}

	public void setN_date ( String n_date ) {
		this.n_date = n_date;
	}

	public String getWin_dt ( ) {
		return win_dt;
	}

	public void setWin_dt ( String win_dt ) {
		this.win_dt = win_dt;
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

	public String getIs_adt ( ) {
		return is_adt;
	}

	public void setIs_adt ( String is_adt ) {
		this.is_adt = is_adt;
	}

	public String getEv_type ( ) {
		return ev_type;
	}

	public void setEv_type ( String ev_type ) {
		this.ev_type = ev_type;
	}

	public String getEv_stat_id ( ) {
		return ev_stat_id;
	}

	public void setEv_stat_id ( String ev_stat_id ) {
		this.ev_stat_id = ev_stat_id;
	}

	public String getBbs_nm ( ) {
		return bbs_nm;
	}

	public void setBbs_nm ( String bbs_nm ) {
		this.bbs_nm = bbs_nm;
	}

}
