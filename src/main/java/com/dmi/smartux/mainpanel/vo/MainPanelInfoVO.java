package com.dmi.smartux.mainpanel.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

@XmlRootElement(name="result")
@XmlType(name="MainPanelInfoVO", namespace="com.dmi.smartux.mainpanel.vo.MainPanelInfoVO"
, propOrder={"code","pdata_version", "p_code", "panelviewname","page_type","page_code", "category_code", "category_type", "ui_type", "page_icon", "description", "order_seq"})
public class MainPanelInfoVO extends BaseVO implements Serializable{

	String code = "";
	String p_code = "";
	String pdata_version = "";
	String panelviewname = "";
	String category_code = "";
	String category_type = "";
	String ui_type = "";
	String page_icon = "";
	String description = "";
	String order_seq = "";
	String page_type = "";
	String page_code = "";
	
	public String getCode() {
		return GlobalCom.isNull(code);
	}
	
	@XmlElement(name="code")
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getP_code() {
		return GlobalCom.isNull(p_code);
	}
	
	@XmlElement(name="p_code")
	public void setP_code(String p_code) {
		this.p_code = p_code;
	}
	
	public String getPanelviewname() {
		return GlobalCom.isNull(panelviewname);
	}
	
	@XmlElement(name="panelviewname")
	public void setPanelviewname(String panelviewname) {
		this.panelviewname = panelviewname;
	}
	
	public String getCategory_code() {
		return GlobalCom.isNull(category_code);
	}
	
	@XmlElement(name="category_code")
	public void setCategory_code(String category_code) {
		this.category_code = category_code;
	}
	
	public String getCategory_type() {
		return GlobalCom.isNull(category_type);
	}

	@XmlElement(name="category_type")
	public void setCategory_type(String category_type) {
		this.category_type = category_type;
	}


	public String getUi_type() {
		return GlobalCom.isNull(ui_type);
	}

	@XmlElement(name="ui_type")
	public void setUi_type(String ui_type) {
		this.ui_type = ui_type;
	}

	public String getPage_icon() {
		return GlobalCom.isNull(page_icon);
	}

	@XmlElement(name="page_icon")
	public void setPage_icon(String page_icon) {
		this.page_icon = page_icon;
	}

	public String getDescription() {
		return GlobalCom.isNull(description);
	}

	@XmlElement(name="description")
	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrder_seq() {
		return GlobalCom.isNull(order_seq);
	}
	
	@XmlElement(name="order_seq")
	public void setOrder_seq(String order_seq) {
		this.order_seq = order_seq;
	}
	
	
	
	/**
	 * @return the page_type
	 */
	public String getPage_type() {
		return GlobalCom.isNull(page_type);
	}

	/**
	 * @param page_type the page_type to set
	 */
	@XmlElement(name="page_type")
	public void setPage_type(String page_type) {
		this.page_type = page_type;
	}

	/**
	 * @return the page_code
	 */
	public String getPage_code() {
		return GlobalCom.isNull(page_code);
	}

	/**
	 * @param page_code the page_code to set
	 */
	@XmlElement(name="page_code")
	public void setPage_code(String page_code) {
		this.page_code = page_code;
	}
	
	/**
	 * @return the pdata_version
	 */
	public String getPdata_version() {
		return pdata_version;
	}

	/**
	 * @param pdata_version the pdata_version to set
	 */
	@XmlElement(name="pdata_version")
	public void setPdata_version(String pdata_version) {
		this.pdata_version = pdata_version;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append(getCode());
		sb.append(GlobalCom.colsep);
		sb.append(getPdata_version());
		sb.append(GlobalCom.colsep);
		sb.append(getP_code());
		sb.append(GlobalCom.colsep);
		sb.append(getPanelviewname());
		sb.append(GlobalCom.colsep);
		sb.append(getPage_type());
		sb.append(GlobalCom.colsep);
		sb.append(getPage_code());
		sb.append(GlobalCom.colsep);
		sb.append(getCategory_code());
		sb.append(GlobalCom.colsep);
		sb.append(getCategory_type());
		sb.append(GlobalCom.colsep);
		sb.append(getUi_type());
		sb.append(GlobalCom.colsep);
		sb.append(getPage_icon());
		sb.append(GlobalCom.colsep);
		sb.append(getDescription());
		sb.append(GlobalCom.colsep);
		sb.append(getOrder_seq());
		
		return sb.toString();
	}
	
}
