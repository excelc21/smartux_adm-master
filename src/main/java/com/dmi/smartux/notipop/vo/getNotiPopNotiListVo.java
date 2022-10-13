package com.dmi.smartux.notipop.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import com.dmi.smartux.common.util.GlobalCom;

@SuppressWarnings("serial")
@JsonPropertyOrder({"noti_tit", "noti_cont", "net_type", "s_date", "e_date", "model_cnt", "models"
	, "noti_no", "ev_cont_id", "img_url", "ev_type", "ev_detail", "ev_detail_list", "ev_stat_id"})
@XmlType(name="getNotiPopNotiListVo", namespace="com.dmi.smartux.notipop.vo"
	, propOrder={"noti_tit", "noti_cont", "net_type", "s_date", "e_date", "model_cnt", "model"
		, "noti_no", "ev_cont_id", "img_url", "ev_type", "ev_detail", "ev_detail_list", "ev_stat_id"}
)
@JsonAutoDetect(value=JsonMethod.FIELD)
public class getNotiPopNotiListVo {

	@JsonProperty("noti_tit")
	private String noti_tit = "";
	@JsonProperty("noti_cont")
	private String noti_cont = "";
	@JsonProperty("net_type")
	private String net_type = "";
	@JsonProperty("s_date")
	private String s_date = "";
	@JsonProperty("e_date")
	private String e_date = "";
	@JsonProperty("model_cnt")
	private String model_cnt = "";
	@JsonProperty("models")
	private List<String> model;
	@JsonProperty("noti_no")
	private String noti_no = "";
	@JsonProperty("ev_cont_id")
	private String ev_cont_id = "";
	@JsonProperty("img_url")
	private String img_url = "";
	@JsonProperty("ev_type")
	private String ev_type = "";
	@JsonProperty("ev_detail")
	private String ev_detail = "";
	@JsonProperty("ev_detail_list")
	private List<getNotiPopContListVo> ev_detail_list;
	@JsonProperty("ev_stat_id")
	private String ev_stat_id = "";
	
	public String getNoti_tit() {
		return noti_tit;
	}
	@XmlElement(name="noti_tit")
	public void setNoti_tit(String noti_tit) {
		this.noti_tit = noti_tit;
	}
	public String getNoti_cont() {
		return noti_cont;
	}
	@XmlElement(name="noti_cont")
	public void setNoti_cont(String noti_cont) {
		this.noti_cont = noti_cont;
	}
	public String getNet_type() {
		return net_type;
	}
	@XmlElement(name="net_type")
	public void setNet_type(String net_type) {
		this.net_type = net_type;
	}
	public String getS_date() {
		return s_date;
	}
	@XmlElement(name="s_date")
	public void setS_date(String s_date) {
		this.s_date = s_date;
	}
	public String getE_date() {
		return e_date;
	}
	@XmlElement(name="e_date")
	public void setE_date(String e_date) {
		this.e_date = e_date;
	}
	public String getModel_cnt() {
		return model_cnt;
	}
	@XmlElement(name="model_cnt")
	public void setModel_cnt(String model_cnt) {
		this.model_cnt = model_cnt;
	}
	public List<String> getModel() {
		return model;
	}
	@XmlElementWrapper(name="models")
	public void setModel(List<String> model) {
		this.model = model;
	}
	public String getNoti_no() {
		return noti_no;
	}
	@XmlElement(name="noti_no")
	public void setNoti_no(String noti_no) {
		this.noti_no = noti_no;
	}
	public String getEv_cont_id() {
		return ev_cont_id;
	}
	@XmlElement(name="ev_cont_id")
	public void setEv_cont_id(String ev_cont_id) {
		this.ev_cont_id = ev_cont_id;
	}
	public String getImg_url() {
		return img_url;
	}
	@XmlElement(name="img_url")
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public String getEv_type() {
		return ev_type;
	}
	@XmlElement(name="ev_type")
	public void setEv_type(String ev_type) {
		this.ev_type = ev_type;
	}
	public String getEv_detail() {
		return ev_detail;
	}
	@XmlElement(name="ev_detail")
	public void setEv_detail(String ev_detail) {
		this.ev_detail = ev_detail;
	}
	public List<getNotiPopContListVo> getEv_detail_list() {
		return ev_detail_list;
	}
	@XmlElementWrapper(name="ev_detail_list")
	@XmlElement(name="record")
	public void setEv_detail_list(List<getNotiPopContListVo> ev_detail_list) {
		this.ev_detail_list = ev_detail_list;
	}
	public String getEv_stat_id() {
		return ev_stat_id;
	}
	@XmlElement(name="ev_stat_id")
	public void setEv_stat_id(String ev_stat_id) {
		this.ev_stat_id = ev_stat_id;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getNoti_tit());
		sb.append(GlobalCom.colsep);
		sb.append(getNoti_cont());
		sb.append(GlobalCom.colsep);
		sb.append(getNet_type());
		sb.append(GlobalCom.colsep);
		sb.append(getS_date());
		sb.append(GlobalCom.colsep);
		sb.append(getE_date());
		sb.append(GlobalCom.colsep);
		sb.append(getModel_cnt());
		if(model!=null){
			if(model.size() != 0){
				StringBuffer record = new StringBuffer();
				record.append(GlobalCom.colsep);
				boolean start = true;
				for(String subModel : model){
					if(start) {
						start = false;
					} else {
						record.append(GlobalCom.rssep);
					}
					record.append(subModel);
				}
				sb.append(record.toString());
			}
		}
		sb.append(GlobalCom.colsep);
		sb.append(getNoti_no());
		sb.append(getEv_cont_id());
		sb.append(GlobalCom.colsep);
		sb.append(getImg_url());
		sb.append(GlobalCom.colsep);
		sb.append(getEv_type());
		if(ev_detail!=null){
			sb.append(GlobalCom.colsep);
			sb.append(getEv_detail());
		}
		if(ev_detail_list != null){
			if(ev_detail_list.size() != 0){
				StringBuffer record = new StringBuffer();
				record.append(GlobalCom.colsep);
				boolean start = true;
				for(getNotiPopContListVo item : ev_detail_list){
					if(start) {
						start = false;
					} else {
						record.append(GlobalCom.rssep);
					}
					record.append(item);
				}
				sb.append(record.toString());
			}
		}

		sb.append(GlobalCom.colsep);
		sb.append(getEv_stat_id());
		return sb.toString();
	}
}
