package com.dmi.smartux.common.vo;

import java.io.Serializable;

/**
 * @author 	deoksan
 * @package	com.dmi.smartux.common.vo
 * @name 	HotvodFilteringSiteVo.java
 * @comment	화제동영상 필터링사이트 VO	
 */
public class HotvodFilteringSiteVo implements Serializable {

	private static final long serialVersionUID = -4045602165027467401L;
	
	private String svcType = "";	// 서비스타입(HDTV,IPTV,TVG,UFLIX)
	private String orderNo = "";	// 순번(index)
	private String siteUrl = "";	// 필터링대상(URL)
	private String regDate = "";	// 등록일
	
	public String getSvcType() {
		return svcType;
	}
	public void setSvcType(String svcType) {
		this.svcType = svcType;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getSiteUrl() {
		return siteUrl;
	}
	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HotvodFilteringSiteVo [svcType=");
		builder.append(svcType);
		builder.append(", orderNo=");
		builder.append(orderNo);
		builder.append(", siteUrl=");
		builder.append(siteUrl);
		builder.append(", regDate=");
		builder.append(regDate);
		builder.append("]");
		return builder.toString();
	}
	
	
}
