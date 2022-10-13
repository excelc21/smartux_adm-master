package com.dmi.smartux.admin.mainpanel.vo;

public class PanelSearchVo {
	
	//검색
	private String findName;		//검색구분
	private String findValue;		//검색어
	private String startDt;			//시작일자
	private String endDt;			//종료일자
	private String abtestYnChk;  //AB연동여부
	//상세
	private String panel_id;
	
	public String getFindName() {
		return findName;
	}
	public void setFindName(String findName) {
		this.findName = findName;
	}
	public String getFindValue() {
		return findValue;
	}
	public void setFindValue(String findValue) {
		this.findValue = findValue;
	}
	public String getStartDt() {
		return startDt;
	}
	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}
	public String getEndDt() {
		return endDt;
	}
	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}
	public String getAbtestYnChk() {
		return abtestYnChk;
	}
	public void setAbtestYnChk(String abtestYnChk) {
		this.abtestYnChk = abtestYnChk;
	}
	public String getPanel_id() {
		return panel_id;
	}
	public void setPanel_id(String panel_id) {
		this.panel_id = panel_id;
	}
}
