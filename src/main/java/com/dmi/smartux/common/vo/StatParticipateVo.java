package com.dmi.smartux.common.vo;

import java.io.Serializable;

public class StatParticipateVo implements Serializable {
	
	String sa_id;
	String mac;
	String ctn;
	String r_date;

	public String getSa_id() {
		return sa_id;
	}
	public void setSa_id(String sa_id) {
		this.sa_id = sa_id.replace(",", "|");
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac.replace(",", "|");
	}
	public String getCtn() {
		return ctn;
	}
	public void setCtn(String ctn) {
		this.ctn = ctn.replace(",", "|");
	}
	public String getR_date() {
		return r_date;
	}
	public void setR_date(String r_date) {
		this.r_date = r_date;
	}

    public StatParticipateVo (String sa_id, String mac, String ctn, String r_date) {
        this.sa_id = sa_id;
        this.mac = mac;
        this.ctn = ctn;
        this.r_date = r_date;
   }

   @Override
   public String toString() {
        return sa_id + "," + mac + "," + ctn + "," + r_date + "\r\n";
   }
}
