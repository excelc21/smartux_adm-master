package com.dmi.smartux.push.vo;

/**
 * NextUi 예약 push  VO
 *
 * @author 
 */
public class NextUiPushUserVO {
	private String sa_id;			// 가번,가번
	private String buy_date;		// 구매일
	private String reg_id;			// reg_id
	
	public String getSa_id() {
		return sa_id;
	}
	public void setSa_id(String sa_id) {
		this.sa_id = sa_id;
	}
	public String getBuy_date() {
		return buy_date;
	}
	public void setBuy_date(String buy_date) {
		this.buy_date = buy_date;
	}
	public String getReg_id() {
		return reg_id;
	}
	public void setReg_id(String reg_id) {
		this.reg_id = reg_id;
	}
	
}
