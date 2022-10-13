package com.dmi.smartux.bonbang.vo;


import java.util.ArrayList;

import com.dmi.smartux.common.util.GlobalCom;

/**
 * 시청예약 파라미터 VO 클래스
 * @author YJ KIM
 *
 */
public class ReservedProgramParamVO{
	
	private String sa_id;	//가입 번호
	private String stb_mac;	//맥주소
	private String[] reg_id;		//REG_ID
	private String stb_reg_id;	//STB REG_ID
	private String sma_mac;	//모바일앱 맥주소
	private String ctn_reg_id;	//CTN REG_ID
	private String app_type;	//어플 타입
	private String service_id;	//서비스 ID
	private String channel_no;	//채널 번호
	private String channel_name;	//채널 명
	private String program_id;	//프로그램 ID
	private String program_name;	//프로그램 명
	private String program_info;	//프로그램 연령
	private String defin_flag;	//프로그램 화질
	private String program_stime;	//프로그램 시작 시간
	private String repeat_day;	//반복 요일 정보   0~6 : 일~토
	
	private String [] arr_service_id;	//서비스 ID
	private String [] arr_channel_no;	//채널 번호
	private String [] arr_channel_name;	//채널 명
	private String [] arr_program_id;	//프로그램 ID
	private String [] arr_program_name;	//프로그램 명
	private String [] arr_program_info;	//프로그램 연령
	private String [] arr_defin_flag;	//프로그램 화질
	private String [] arr_program_stime;	//프로그램 시작 시간
	private String [] arr_repeat_day;	//반복 요일 정보   0~6 : 일~토
	private ArrayList<String> list_repeat_day;	//반복 요일 정보   0~6 : 일~토
	
	
	private String is_push; //PUSH 알림 설정값 0:미설정 1:설정
	
	private String create_date;	//시청예약 생성일자
	
	private String start_num;	//검색 시작 인덱스
	private String req_count;	//검색할 레코드 수
	
	/**
	 * 셋탑 가입번호
	 * @return the sa_id
	 */
	public String getSa_id() {
		return GlobalCom.isNull(sa_id);
	}
	/**
	 * 셋탑 가입 번호
	 * @param sa_id the sa_id to set
	 */
	public void setSa_id(String sa_id) {
		this.sa_id = GlobalCom.isNull(sa_id);
	}
	/**
	 * 셋탑 맥주소
	 * @return the stb_mac
	 */
	public String getStb_mac() {
		return GlobalCom.isNull(stb_mac);
	}
	/**
	 * 셋탑 맥주소
	 * @param stb_mac the stb_mac to set
	 */
	public void setStb_mac(String stb_mac) {
		this.stb_mac = GlobalCom.isNull(stb_mac);
	}
	/**
	 * 셋탑 REG_ID
	 * @return the stb_reg_id
	 */
	public String getStb_reg_id() {
		return GlobalCom.isNull(stb_reg_id);
	}
	/**
	 * 셋탑 REG_ID
	 * @param stb_reg_id the stb_reg_id to set
	 */
	public void setStb_reg_id(String stb_reg_id) {
		this.stb_reg_id = GlobalCom.isNull(stb_reg_id);
	}
	/**
	 * 모바일앱 REG_ID
	 * @return the ctn_reg_id
	 */
	public String getCtn_reg_id() {
		return GlobalCom.isNull(ctn_reg_id);
	}
	/**
	 * 모바일앱 REG_ID
	 * @param ctn_reg_id the ctn_reg_id to set
	 */
	public void setCtn_reg_id(String ctn_reg_id) {
		this.ctn_reg_id = GlobalCom.isNull(ctn_reg_id);
	}
	/**
	 * 어플 타입
	 * @return the app_type
	 */
	public String getApp_type() {
		return GlobalCom.isNull(app_type);
	}
	/**
	 * 어플 타입
	 * @param app_type the app_type to set
	 */
	public void setApp_type(String app_type) {
		this.app_type = GlobalCom.isNull(app_type);
	}
	/**
	 * 서비스 ID
	 * @return the service_id
	 */
	public String getService_id() {
		return GlobalCom.isNull(service_id);
	}
	/**
	 * 서비스 ID
	 * @param service_id the service_id to set
	 */
	public void setService_id(String service_id) {
		this.service_id = GlobalCom.isNull(service_id);
	}
	/**
	 * 채널 번호
	 * @return the channel_no
	 */
	public String getChannel_no() {
		return GlobalCom.isNull(channel_no);
	}
	/**
	 * 채널 번호
	 * @param channel_no the channel_no to set
	 */
	public void setChannel_no(String channel_no) {
		this.channel_no = GlobalCom.isNull(channel_no);
	}
	/**
	 * 채널 명
	 * @return the channel_name
	 */
	public String getChannel_name() {
		return GlobalCom.isNull(channel_name);
	}
	/**
	 * 채널 명
	 * @param channel_name the channel_name to set
	 */
	public void setChannel_name(String channel_name) {
		this.channel_name = GlobalCom.isNull(channel_name);
	}
	/**
	 * 프로그램 ID
	 * @return the program_id
	 */
	public String getProgram_id() {
		return GlobalCom.isNull(program_id);
	}
	/**
	 * 프로그램 ID
	 * @param program_id the program_id to set
	 */
	public void setProgram_id(String program_id) {
		this.program_id = GlobalCom.isNull(program_id);
	}
	/**
	 * 프로그램 명
	 * @return the program_name
	 */
	public String getProgram_name() {
		return GlobalCom.isNull(program_name);
	}
	/**
	 * 프로그램 명
	 * @param program_name the program_name to set
	 */
	public void setProgram_name(String program_name) {
		this.program_name = GlobalCom.isNull(program_name);
	}
	/**
	 * 프로그램 연령
	 * @return the program_info
	 */
	public String getProgram_info() {
		return GlobalCom.isNull(program_info);
	}
	/**
	 * 프로그램 연령
	 * @param program_info the program_info to set
	 */
	public void setProgram_info(String program_info) {
		this.program_info = GlobalCom.isNull(program_info);
	}
	/**
	 * 프로그램 화질
	 * @return the defin_flag
	 */
	public String getDefin_flag() {
		return GlobalCom.isNull(defin_flag);
	}
	/**
	 * 프로그램 화질
	 * @param defin_flag the defin_flag to set
	 */
	public void setDefin_flag(String defin_flag) {
		this.defin_flag = GlobalCom.isNull(defin_flag);
	}
	/**
	 * 프로그램 시작 시간
	 * @return the program_stime
	 */
	public String getProgram_stime() {
		return GlobalCom.isNull(program_stime);
	}
	/**
	 * 프로그램 시작 시간
	 * @param program_stime the program_stime to set
	 */
	public void setProgram_stime(String program_stime) {
		this.program_stime = GlobalCom.isNull(program_stime);
	}
	/**
	 * 시청예약 등록일
	 * @return the create_date
	 */
	public String getCreate_date() {
		return GlobalCom.isNull(create_date);
	}
	/**
	 * 시청예약 등록일
	 * @param create_date the create_date to set
	 */
	public void setCreate_date(String create_date) {
		this.create_date = GlobalCom.isNull(create_date);
	}
	/** 알림 설정 값
	 * @return the is_push
	 */
	public String getIs_push() {
		return GlobalCom.isNull(is_push);
	}
	/**
	 * 알림 설정값
	 * @param is_push the is_push to set
	 */
	public void setIs_push(String is_push) {
		this.is_push = GlobalCom.isNull(is_push);
	}
	/**
	 * 페이징 시작 레코드 번호
	 * @return the start_num
	 */
	public String getStart_num() {
		return GlobalCom.isNull(start_num);
	}
	/**
	 * 페이징 시작 레코드 번호
	 * @param start_num the start_num to set
	 */
	public void setStart_num(String start_num) {
		this.start_num = GlobalCom.isNull(start_num);
	}
	/**
	 * 페이징 요청 레코드 개수
	 * @return the req_count
	 */
	public String getReq_count() {
		return GlobalCom.isNull(req_count);
	}
	/**
	 * 페이징 요청 레크드 개수
	 * @param req_count the req_count to set
	 */
	public void setReq_count(String req_count) {
		this.req_count = GlobalCom.isNull(req_count);
	}
	/**
	 * REG_ID
	 * @return the reg_id
	 */
	public String[] getReg_id() {
		return reg_id;
	}
	/**
	 * REG_ID
	 * @param reg_id the reg_id to set
	 */
	public void setReg_id(String[] reg_id) {
		this.reg_id = reg_id;
	}
	/**
	 * 모바일앱 맥주소
	 * @return the sma_mac
	 */
	public String getSma_mac() {
		return sma_mac;
	}
	/**
	 * 모바일앱 맥주소
	 * @param sma_mac the sma_mac to set
	 */
	public void setSma_mac(String sma_mac) {
		this.sma_mac = sma_mac;
	}
	
	/**
	 * 파라미터로 전달되어온 REG_ID 값을 REG_ID 배열변수로 삽입한다. (구분자 !^)
	 * @param reg_id	String형 REG_ID
	 */
	public String[] getReg_idStr(String reg_id){
		if(reg_id.indexOf(GlobalCom.colsep) != -1){
			String[] _reg_id = reg_id.split(GlobalCom.splitcolsep);
			//for(int i=0;i<_reg_id.length;i++){
			//}
			return _reg_id;
		}else{
			String[] _reg_id = {reg_id};
			return _reg_id;			
		}
	}
	/**
	 * 반복 요일 정보   0~6 : 일~토
	 * @return the repeat_day
	 */
	public String getRepeat_day() {
		return GlobalCom.isNull(repeat_day);
	}
	/**
	 * 반복 요일 정보   0~6 : 일~토
	 * @param repeat_day the repeat_day to set
	 */
	public void setRepeat_day(String repeat_day) {
		this.repeat_day = GlobalCom.isNull(repeat_day);
	}
	/**
	 * @return the arr_service_id
	 */
	public String[] getArr_service_id() {
		return arr_service_id;
	}
	/**
	 * @param arr_service_id the arr_service_id to set
	 */
	public void setArr_service_id(String[] arr_service_id) {
		this.arr_service_id = arr_service_id;
	}
	/**
	 * @return the arr_channel_no
	 */
	public String[] getArr_channel_no() {
		return arr_channel_no;
	}
	/**
	 * @param arr_channel_no the arr_channel_no to set
	 */
	public void setArr_channel_no(String[] arr_channel_no) {
		this.arr_channel_no = arr_channel_no;
	}
	/**
	 * @return the arr_channel_name
	 */
	public String[] getArr_channel_name() {
		return arr_channel_name;
	}
	/**
	 * @param arr_channel_name the arr_channel_name to set
	 */
	public void setArr_channel_name(String[] arr_channel_name) {
		this.arr_channel_name = arr_channel_name;
	}
	
	public void setArr_channel_name(String channel_name, int index) {
		this.arr_channel_name[index] = channel_name;
	}
	
	/**
	 * @return the arr_program_id
	 */
	public String[] getArr_program_id() {
		return arr_program_id;
	}
	/**
	 * @param arr_program_id the arr_program_id to set
	 */
	public void setArr_program_id(String[] arr_program_id) {
		this.arr_program_id = arr_program_id;
	}
	/**
	 * @return the arr_program_name
	 */
	public String[] getArr_program_name() {
		return arr_program_name;
	}
	/**
	 * @param arr_program_name the arr_program_name to set
	 */
	public void setArr_program_name(String[] arr_program_name) {
		this.arr_program_name = arr_program_name;
	}
	
	public void setArr_program_name(String program_name, int index) {
		this.arr_program_name[index] = program_name;
	}
	
	/**
	 * @return the arr_program_info
	 */
	public String[] getArr_program_info() {
		return arr_program_info;
	}
	/**
	 * @param arr_program_info the arr_program_info to set
	 */
	public void setArr_program_info(String[] arr_program_info) {
		this.arr_program_info = arr_program_info;
	}
	/**
	 * @return the arr_defin_flag
	 */
	public String[] getArr_defin_flag() {
		return arr_defin_flag;
	}
	/**
	 * @param arr_defin_flag the arr_defin_flag to set
	 */
	public void setArr_defin_flag(String[] arr_defin_flag) {
		this.arr_defin_flag = arr_defin_flag;
	}
	/**
	 * @return the arr_program_stime
	 */
	public String[] getArr_program_stime() {
		return arr_program_stime;
	}
	/**
	 * @param arr_program_stime the arr_program_stime to set
	 */
	public void setArr_program_stime(String[] arr_program_stime) {
		this.arr_program_stime = arr_program_stime;
	}
	/**
	 * @return the arr_repeat_day
	 */
	public String[] getArr_repeat_day() {
		return arr_repeat_day;
	}
	/**
	 * @param arr_repeat_day the arr_repeat_day to set
	 */
	public void setArr_repeat_day(String[] arr_repeat_day) {
		this.arr_repeat_day = arr_repeat_day;
	}
	/**
	 * @return the list_repeat_day
	 */
	public ArrayList<String> getList_repeat_day() {
		return list_repeat_day;
	}
	/**
	 * @param list_repeat_day the list_repeat_day to set
	 */
	public void setList_repeat_day(ArrayList<String> list_repeat_day) {
		this.list_repeat_day = list_repeat_day;
	}
	
	
}
