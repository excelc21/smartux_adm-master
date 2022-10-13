package com.dmi.smartux.common.vo;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * @author 	deoksan
 * @package	com.dmi.smartux.common.vo
 * @name 	StatPushLogVo.java
 * @comment	통계푸시로그VO	
 */
public class StatPushLogVo implements Serializable {

	private static final long serialVersionUID = -8832486534024378193L;

	private String seq_id = "";			// YYYYMMDDHHmmSSsss+(순차8자리)
	private String log_time = "";		// 로그남기는시간(YYYYMMDDHHmmSS)
	private String log_type = "";		// SVC
	private String sid = "";			// SID(가번 12자리)
	private String result_code = "";	// 결과코드(2xxxxxxx:성공 / 3xxxxxxx:실패-PUSH GW실패)
	private String req_time = "";		// 로그시작시간
	private String rsp_time = "";		// 
	private String client_ip = "";		// 접속 클라이언트 IP
	private String dev_info = "";		// 접속 단말타입
	private String os_info = "";		// OS 정보
	private String nw_info = "";		// 접속네트워크
	private String svc_name = "";		// 서비스명
	private String dev_model = "";		// 단말모델
	private String carrier_type = "";	// 통신사구분
	
	LinkedHashMap<String,String> addInfo = new LinkedHashMap<String,String>();	// 추가정보

	public String getSeq_id() {
		return seq_id;
	}

	public void setSeq_id(String seq_id) {
		this.seq_id = seq_id;
	}

	public String getLog_time() {
		return log_time;
	}

	public void setLog_time(String log_time) {
		this.log_time = log_time;
	}

	public String getLog_type() {
		return log_type;
	}

	public void setLog_type(String log_type) {
		this.log_type = log_type;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getReq_time() {
		return req_time;
	}

	public void setReq_time(String req_time) {
		this.req_time = req_time;
	}

	public String getRsp_time() {
		return rsp_time;
	}

	public void setRsp_time(String rsp_time) {
		this.rsp_time = rsp_time;
	}

	public String getClient_ip() {
		return client_ip;
	}

	public void setClient_ip(String client_ip) {
		this.client_ip = client_ip;
	}

	public String getDev_info() {
		return dev_info;
	}

	public void setDev_info(String dev_info) {
		this.dev_info = dev_info;
	}

	public String getOs_info() {
		return os_info;
	}

	public void setOs_info(String os_info) {
		this.os_info = os_info;
	}

	public String getNw_info() {
		return nw_info;
	}

	public void setNw_info(String nw_info) {
		this.nw_info = nw_info;
	}

	public String getSvc_name() {
		return svc_name;
	}

	public void setSvc_name(String svc_name) {
		this.svc_name = svc_name;
	}

	public String getDev_model() {
		return dev_model;
	}

	public void setDev_model(String dev_model) {
		this.dev_model = dev_model;
	}

	public String getCarrier_type() {
		return carrier_type;
	}

	public void setCarrier_type(String carrier_type) {
		this.carrier_type = carrier_type;
	}

	public LinkedHashMap<String, String> getAddInfo() {
		return addInfo;
	}

	public void setAddInfo(LinkedHashMap<String, String> addInfo) {
		this.addInfo = addInfo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StatPushLogVo [seq_id=");
		builder.append(seq_id);
		builder.append(", log_time=");
		builder.append(log_time);
		builder.append(", log_type=");
		builder.append(log_type);
		builder.append(", sid=");
		builder.append(sid);
		builder.append(", result_code=");
		builder.append(result_code);
		builder.append(", req_time=");
		builder.append(req_time);
		builder.append(", rsp_time=");
		builder.append(rsp_time);
		builder.append(", client_ip=");
		builder.append(client_ip);
		builder.append(", dev_info=");
		builder.append(dev_info);
		builder.append(", os_info=");
		builder.append(os_info);
		builder.append(", nw_info=");
		builder.append(nw_info);
		builder.append(", svc_name=");
		builder.append(svc_name);
		builder.append(", dev_model=");
		builder.append(dev_model);
		builder.append(", carrier_type=");
		builder.append(carrier_type);
		builder.append(", addInfo=");
		builder.append(addInfo);
		builder.append("]");
		return builder.toString();
	}

}
