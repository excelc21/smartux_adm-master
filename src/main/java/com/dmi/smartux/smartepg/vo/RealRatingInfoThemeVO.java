package com.dmi.smartux.smartepg.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

@XmlRootElement(name = "record") // vo의 XmlRootElement name속성은 record로 통일
@XmlType(name="RealRatingInfoVO", namespace="com.dmi.smartux.smartepg.vo.RealRatingInfoThemeVO"
, propOrder={"service_id", "channel_no", "channel_name", "program_id", "program_name", "defin_flag", "program_info", "program_stime", "program_etime", "series_flag", "series_desc", "img_url"})
public class RealRatingInfoThemeVO extends BaseVO{

	String service_id;
	String channel_no;
	String channel_name;
	String program_id;
	String program_name;
	String defin_flag;
	String program_info;
	String program_stime;
	String program_etime;
	String series_flag;
	String series_desc;
	String img_url;
	
	public String getService_id() {
		return service_id;
	}
	
	@XmlElement(name="service_id")
	public void setService_id(String service_id) {
		this.service_id = service_id;
	}
	
	public String getChannel_no() {
		return GlobalCom.isNull(channel_no);
	}
	
	@XmlElement(name="channel_no")
	public void setChannel_no(String channel_no) {
		this.channel_no = channel_no;
	}
	
	public String getChannel_name() {
		return GlobalCom.isNull(channel_name);
	}
	
	@XmlElement(name="channel_name")
	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}
	
	public String getProgram_id() {
		return GlobalCom.isNull(program_id);
	}
	
	@XmlElement(name="program_id")
	public void setProgram_id(String program_id) {
		this.program_id = program_id;
	}
	
	public String getProgram_name() {
		return GlobalCom.isNull(program_name);
	}
	
	@XmlElement(name="program_name")
	public void setProgram_name(String program_name) {
		this.program_name = program_name;
	}
	
	public String getDefin_flag() {
		return GlobalCom.isNull(defin_flag);
	}
	
	@XmlElement(name="defin_flag")
	public void setDefin_flag(String defin_flag) {
		this.defin_flag = defin_flag;
	}
	
	public String getProgram_info() {
		return GlobalCom.isNull(program_info);
	}
	
	@XmlElement(name="program_info")
	public void setProgram_info(String program_info) {
		this.program_info = program_info;
	}
	
	public String getProgram_stime() {
		return GlobalCom.isNull(program_stime);
	}
	
	@XmlElement(name="program_stime")
	public void setProgram_stime(String program_stime) {
		this.program_stime = program_stime;
	}
	
	public String getProgram_etime() {
		return GlobalCom.isNull(program_etime);
	}
	
	@XmlElement(name="program_etime")
	public void setProgram_etime(String program_etime) {
		this.program_etime = program_etime;
	}
	
	public String getSeries_flag() {
		return GlobalCom.isNull(series_flag);
	}
	
	@XmlElement(name="series_flag")
	public void setSeries_flag(String series_flag) {
		this.series_flag = series_flag;
	}
	
	public String getSeries_desc() {
		return GlobalCom.isNull(series_desc);
	}
	
	@XmlElement(name="series_desc")
	public void setSeries_desc(String series_desc) {
		this.series_desc = series_desc;
	}
	
	public String getImg_url() {
		return GlobalCom.isNull(img_url);
	}
	
	@XmlElement(name="img_url")
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub

		StringBuffer sb = new StringBuffer();
		sb.append(getService_id());
		sb.append(GlobalCom.colsep);
		sb.append(getChannel_no());
		sb.append(GlobalCom.colsep);
		sb.append(getChannel_name());
		sb.append(GlobalCom.colsep);
		sb.append(getProgram_id());
		sb.append(GlobalCom.colsep);
		sb.append(getProgram_name());
		sb.append(GlobalCom.colsep);
		sb.append(getDefin_flag());
		sb.append(GlobalCom.colsep);
		sb.append(getProgram_info());
		sb.append(GlobalCom.colsep);
		sb.append(getProgram_stime());
		sb.append(GlobalCom.colsep);
		sb.append(getProgram_etime());
		sb.append(GlobalCom.colsep);
		sb.append(getSeries_flag());
		sb.append(GlobalCom.colsep);
		sb.append(getSeries_desc());
		sb.append(GlobalCom.colsep);
		sb.append(getImg_url());
		
		return sb.toString();
	}
	
	
}
