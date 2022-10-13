package com.dmi.smartux.admin.statbbsAdmin.vo;

import java.util.List;

public class StatParticipateFileVo {
	
	private List<StatPaticipantListArrVo> statpaticipantlistarrVo;
	private int totalCnt = 0;

	public List<StatPaticipantListArrVo> getStatpaticipantlistarrVo() {
		return statpaticipantlistarrVo;
	}
	public void setStatpaticipantlistarrVo(
			List<StatPaticipantListArrVo> statpaticipantlistarrVo) {
		this.statpaticipantlistarrVo = statpaticipantlistarrVo;
	}
	public int getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}

}
