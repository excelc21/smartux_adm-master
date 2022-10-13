package com.dmi.smartux.noti.vo;

import java.util.Date;

import com.dmi.smartux.common.util.GlobalCom;

/**
 * PT_HDTV_BBS 테이블과 맵핑 되는 VO
 * 
 */
public class BaseNotiVO {
	/**
	 * 단말기 모델명
	 */
	private String term_model;
	/**
	 * PK 게시글 등록번호
	 */
	private String reg_no;
	/**
	 * 게시판 아이디
	 */
	private String bbs_id;
	/**
	 * 공지제목
	 */
	private String title;
	/**
	 * 공지내용
	 */
	private String content;
	/**
	 * 상단고정여부
	 */
	private String is_fixed;
	/**
	 * 공지(이벤트) 시작일시
	 */
	private Date s_date;
	/**
	 * 공지(이벤트) 종료일시
	 */
	private Date e_date;
	/**
	 * "New" 디스플레이 마감일
	 */
	private Date n_date;
	/**
	 * 이벤트 컨텐츠 아이디
	 */
	private String ev_cont_id;
	/**
	 * 당첨자 발표일
	 */
	private String win_dt;
	/**
	 * 원파일명
	 */
	private String org_file_nm;
	/**
	 * 저장 파일명
	 */
	private String save_file_nm;
	/**
	 * 등록일시
	 */
	private String reg_dt;
	/**
	 * 등록자 아이디
	 */
	private String reg_id;
	/**
	 * 수정일시
	 */
	private String mod_dt;
	/**
	 * 수정자 아이디
	 */
	private String mod_id;

	private String ev_type;// 공지 이벤트 타입
	private String ev_detail;// 이벤트 상세 데이터
	private String ev_stat_id;// 참여통계 번호

	/**
	 * @return 단말기 모델명
	 */
	public String getTerm_model ( ) {
		return term_model;
	}

	/**
	 * @param term_model 단말기 모델명
	 */
	public void setTerm_model ( String term_model ) {
		this.term_model = term_model;
	}

	/**
	 * @return 게시글 등록번호
	 */
	public String getReg_no ( ) {
		return reg_no;
	}

	/**
	 * @param reg_no 게시글 등록번호
	 */
	public void setReg_no ( String reg_no ) {
		this.reg_no = reg_no;
	}

	/**
	 * @return 게시판 아이디
	 */
	public String getBbs_id ( ) {
		return bbs_id;
	}

	/**
	 * @param bbs_id 게시판 아이디
	 */
	public void setBbs_id ( String bbs_id ) {
		this.bbs_id = bbs_id;
	}

	/**
	 * @return 공지제목
	 */
	public String getTitle ( ) {
		return title;
	}

	/**
	 * @param title 공지제목
	 */
	public void setTitle ( String title ) {
		this.title = title;
	}

	/**
	 * @return 공지내용
	 */
	public String getContent ( ) {
		return content;
	}

	/**
	 * @param content 공지내용
	 */
	public void setContent ( String content ) {
		this.content = content;
	}

	/**
	 * @return 상단고정여부
	 */
	public String getIs_fixed ( ) {
		return is_fixed;
	}

	/**
	 * @param is_fixed 상단고정여부
	 */
	public void setIs_fixed ( String is_fixed ) {
		this.is_fixed = is_fixed;
	}

	/**
	 * @return 공지(이벤트) 시작일시
	 */
	public Date getS_date ( ) {
		return s_date;
	}

	/**
	 * @param s_date 공지(이벤트) 시작일시
	 */
	public void setS_date ( Date s_date ) {
		this.s_date = s_date;
	}

	/**
	 * @return 공지(이벤트) 종료일시
	 */
	public Date getE_date ( ) {
		return e_date;
	}

	/**
	 * @param e_date 공지(이벤트) 종료일시
	 */
	public void setE_date ( Date e_date ) {
		this.e_date = e_date;
	}

	/**
	 * @return "New" 디스플레이 마감일
	 */
	public Date getN_date ( ) {
		return n_date;
	}

	/**
	 * @param n_date "New" 디스플레이 마감일
	 */
	public void setN_date ( Date n_date ) {
		this.n_date = n_date;
	}

	/**
	 * @return 이벤트 컨텐츠 아이디
	 */
	public String getEv_cont_id ( ) {
		return ev_cont_id;
	}

	/**
	 * @param ev_cont_id 이벤트 컨텐츠 아이디
	 */
	public void setEv_cont_id ( String ev_cont_id ) {
		this.ev_cont_id = GlobalCom.isNull ( ev_cont_id );
	}

	/**
	 * @return 당첨자 발표일
	 */
	public String getWin_dt ( ) {
		return win_dt;
	}

	/**
	 * @param win_dt 당첨자 발표일
	 */
	public void setWin_dt ( String win_dt ) {
		this.win_dt = win_dt;
	}

	/**
	 * @return 원파일명
	 */
	public String getOrg_file_nm ( ) {
		return org_file_nm;
	}

	/**
	 * @param org_file_nm 원파일명
	 */
	public void setOrg_file_nm ( String org_file_nm ) {
		this.org_file_nm = org_file_nm;
	}

	/**
	 * @return 저장 파일명
	 */
	public String getSave_file_nm ( ) {
		return save_file_nm;
	}

	/**
	 * @param save_file_nm 저장 파일명
	 */
	public void setSave_file_nm ( String save_file_nm ) {
		this.save_file_nm = GlobalCom.isNull ( save_file_nm );
	}

	/**
	 * @return 등록일시
	 */
	public String getReg_dt ( ) {
		return reg_dt;
	}

	/**
	 * @param reg_dt 등록일시
	 */
	public void setReg_dt ( String reg_dt ) {
		this.reg_dt = reg_dt;
	}

	/**
	 * @return 등록자 아이디
	 */
	public String getReg_id ( ) {
		return reg_id;
	}

	/**
	 * @param reg_id 등록자 아이디
	 */
	public void setReg_id ( String reg_id ) {
		this.reg_id = reg_id;
	}

	/**
	 * @return 수정일시
	 */
	public String getMod_dt ( ) {
		return mod_dt;
	}

	/**
	 * @param mod_dt 수정일시
	 */
	public void setMod_dt ( String mod_dt ) {
		this.mod_dt = mod_dt;
	}

	/**
	 * @return 수정자 아이디
	 */
	public String getMod_id ( ) {
		return mod_id;
	}

	/**
	 * @param mod_id 수정자 아이디
	 */
	public void setMod_id ( String mod_id ) {
		this.mod_id = mod_id;
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

	public String getEv_stat_id ( ) {
		return ev_stat_id;
	}

	public void setEv_stat_id ( String ev_stat_id ) {
		this.ev_stat_id = ev_stat_id;
	}

}
