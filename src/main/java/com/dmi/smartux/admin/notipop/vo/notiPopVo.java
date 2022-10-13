package com.dmi.smartux.admin.notipop.vo;

public class notiPopVo {

	public notiPopVo( ) {}

	/**
	 * 캐시용 긴급/팝업 공지 삭제 시
	 * 
	 * @param scr_tp 대상구분 H:HDTV S:SmartTv 등..
	 * @param display_type S:상용 T:검수
	 * @param act_id : 등록자
	 * @param act_ip : 등록자 IP
	 */
	public notiPopVo( String scr_tp, String display_type, String act_id, String act_ip ) {
		this.scr_tp = scr_tp;
		this.display_type = display_type;
		this.act_id = act_id;
		this.act_ip = act_ip;
	}
	
	/**
	 * 캐시용 긴급/팝업 공지 삭제 시
	 * 
	 * @param scr_tp 대상구분 H:HDTV S:SmartTv 등..
	 * @param display_type S:상용 T:검수
	 * @param act_id : 등록자
	 * @param act_ip : 등록자 IP
	 * @param noti_no : 일련번호
	 */
	public notiPopVo( String scr_tp, String display_type, String act_id, String act_ip, String noti_no) {
		this.scr_tp = scr_tp;
		this.display_type = display_type;
		this.act_id = act_id;
		this.act_ip = act_ip;
		this.noti_no = noti_no;
	}

	/**
	 * 캐시용 팝업공지 등록 시
	 * 
	 * @param scr_tp 대상구분 H:HDTV S:SmartTv 등..
	 * @param display_type S:상용 T:검수
	 * @param bbs_id : 게시판ID
	 * @param reg_no : 게시물ID
	 * @param act_id : 등록자
	 * @param act_ip : 등록자 IP
	 */
	public notiPopVo( String scr_tp, String display_type, String bbs_id, String reg_no, String act_id, String act_ip ) {
		this.scr_tp = scr_tp;
		this.display_type = display_type;
		this.bbs_id = bbs_id;
		this.reg_no = reg_no;
		this.act_id = act_id;
		this.act_ip = act_ip;
	}

	/**
	 * 캐시용 긴급공지 등록 시
	 * 
	 * @param scr_tp 대상구분 H:HDTV S:SmartTv 등..
	 * @param display_type S:상용 T:검수
	 * @param status 긴급(비상)여부
	 * @param net_type 네트워크 타입
	 * @param message_yn 메시지 출력 여부
	 * @param message 메시지
	 * @param act_id 등록자
	 * @param act_ip 등록자 IP
	 * @param noti_no 일련번호 (2019.01.24 비디오포털에서 긴급공지가 수정되면서 IPTV에서 오류나서 수정) 
	 */
	public notiPopVo( String scr_tp, String display_type, String status, String net_type, String message_yn, String message, String act_id, String act_ip, String noti_no ) {
		this.scr_tp = scr_tp;
		this.display_type = display_type;
		this.status = status;
		this.net_type = net_type;
		this.message_yn = message_yn;
		this.message = message;
		this.act_id = act_id;
		this.act_ip = act_ip;
		this.noti_no = noti_no;
	}

	private String scr_tp;
	private String display_type;
	private String status;
	private String message_yn;
	private String message;
	private String net_type;
	private String bbs_id;
	private String reg_no;
	private String act_dt;
	private String act_id;
	private String act_ip;
	private String act_gbn;
	private String noti_no;

	public String getNoti_no() {
		return noti_no;
	}

	public void setNoti_no(String noti_no) {
		this.noti_no = noti_no;
	}

	public String getScr_tp ( ) {
		return scr_tp;
	}

	public void setScr_tp ( String scr_tp ) {
		this.scr_tp = scr_tp;
	}

	public String getDisplay_type ( ) {
		return display_type;
	}

	public void setDisplay_type ( String display_type ) {
		this.display_type = display_type;
	}

	public String getStatus ( ) {
		return status;
	}

	public void setStatus ( String status ) {
		this.status = status;
	}

	public String getMessage_yn ( ) {
		return message_yn;
	}

	public void setMessage_yn ( String message_yn ) {
		this.message_yn = message_yn;
	}

	public String getMessage ( ) {
		return message;
	}

	public void setMessage ( String message ) {
		this.message = message;
	}

	public String getNet_type ( ) {
		return net_type;
	}

	public void setNet_type ( String net_type ) {
		this.net_type = net_type;
	}

	public String getBbs_id ( ) {
		return bbs_id;
	}

	public void setBbs_id ( String bbs_id ) {
		this.bbs_id = bbs_id;
	}

	public String getReg_no ( ) {
		return reg_no;
	}

	public void setReg_no ( String reg_no ) {
		this.reg_no = reg_no;
	}

	public String getAct_dt ( ) {
		return act_dt;
	}

	public void setAct_dt ( String act_dt ) {
		this.act_dt = act_dt;
	}

	public String getAct_id ( ) {
		return act_id;
	}

	public void setAct_id ( String act_id ) {
		this.act_id = act_id;
	}

	public String getAct_ip ( ) {
		return act_ip;
	}

	public void setAct_ip ( String act_ip ) {
		this.act_ip = act_ip;
	}

	public String getAct_gbn ( ) {
		return act_gbn;
	}

	public void setAct_gbn ( String act_gbn ) {
		this.act_gbn = act_gbn;
	}

}
