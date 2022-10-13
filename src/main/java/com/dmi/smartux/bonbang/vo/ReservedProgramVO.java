package com.dmi.smartux.bonbang.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;


/**
 * 시청예약 VO 클래스
 * @author YJ KIM
 *
 */
@XmlRootElement(name = "record")
@XmlType(name="ReservedProgramVO", namespace="com.dmi.smartux.bonbang.vo.ReservedProgramVO", 
				propOrder={"service_id"
						  ,"channel_no"
						  ,"channel_name"
						  ,"program_id"
						  ,"program_name"
						  ,"program_info"
						  ,"defin_flag"
						  ,"program_stime"
						  ,"repeat_day"
						  }
)
public class ReservedProgramVO extends BaseVO{
	
	private String service_id;	//서비스 ID
	private String channel_no;	//채널 번호
	private String channel_name;	//채널 명
	private String program_id;	//프로그램 ID
	private String program_name;	//프로그램 명
	private String program_info;	//프로그램 연령
	private String defin_flag;	//프로그램 화질
	private String program_stime;	//프로그램 시작 시간
	private String repeat_day="";	//반복 요일 정보   0~6 : 일~토
	
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
	@XmlElement(name="service_id")
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
	@XmlElement(name="channel_no")
	public void setChannel_no(String channel_no) {
		this.channel_no = GlobalCom.isNull(channel_no);
	}
	/**
	 * 채널명
	 * @return the channel_name
	 */
	public String getChannel_name() {
		return GlobalCom.isNull(channel_name);
	}
	/**
	 * 채널명
	 * @param channel_name the channel_name to set
	 */
	@XmlElement(name="channel_name")
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
	@XmlElement(name="program_id")
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
	@XmlElement(name="program_name")
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
	@XmlElement(name="program_info")
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
	@XmlElement(name="defin_flag")
	public void setDefin_flag(String defin_flag) {
		this.defin_flag = GlobalCom.isNull(defin_flag);
	}
	/**
	 * 프로그램 시작시간
	 * @return the program_stime
	 */
	public String getProgram_stime() {
		return GlobalCom.isNull(program_stime);
	}
	/**
	 * 프로그램 시작시간
	 * @param program_stime the program_stime to set
	 */
	@XmlElement(name="program_stime")
	public void setProgram_stime(String program_stime) {
		this.program_stime = GlobalCom.isNull(program_stime);
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
	@XmlElement(name="repeat_day")
	public void setRepeat_day(String repeat_day) {
		this.repeat_day = GlobalCom.isNull(repeat_day);
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append(service_id);
			sb.append(GlobalCom.colsep);
		sb.append(channel_no);
			sb.append(GlobalCom.colsep);
		sb.append(channel_name);
			sb.append(GlobalCom.colsep);
		sb.append(program_id);
			sb.append(GlobalCom.colsep);
		sb.append(program_name);
			sb.append(GlobalCom.colsep);
		sb.append(program_info);
			sb.append(GlobalCom.colsep);
		sb.append(defin_flag);
			sb.append(GlobalCom.colsep);
		sb.append(program_stime);
			sb.append(GlobalCom.colsep);
		sb.append(repeat_day);
		return sb.toString();	
		
	}
}
