package com.dmi.smartux.gpack.promotion.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

@XmlRootElement(name="record_mov")
@XmlType(name="GpackPlaylistVO", namespace="com.dmi.smartux.gpack.promotion.vo.GpackPlaylistVO"
, propOrder={"p_title","p_albumid","albumid","category_id","service_id"})
@JsonPropertyOrder({"p_title","p_albumid","albumid","category_id","service_id"}) 
public class GpackPlaylistVO extends BaseVO implements Serializable{

	String p_title = "";
	String p_albumid = "";
	String albumid = "";
	String service_id = "";
	String category_id = "";

	public String getP_title() {
		return GlobalCom.isNull(p_title);
	}
	
	@XmlElement(name="p_title")
	public void setP_title(String p_title) {
		this.p_title = p_title;
	}
	
	public String getP_albumid() {
		return GlobalCom.isNull(p_albumid);
	}
	
	@XmlElement(name="p_albumid")
	public void setP_albumid(String p_albumid) {
		this.p_albumid = p_albumid;
	}
	
	public String getAlbumid() {
		return GlobalCom.isNull(albumid);
	}
	
	@XmlElement(name="albumid")
	public void setAlbumid(String albumid) {
		this.albumid = albumid;
	}
	
	public String getCategory_id() {
		return GlobalCom.isNull(category_id);
	}

	@XmlElement(name="category_id")
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	
	public String getService_id() {
		return GlobalCom.isNull(service_id);
	}
	
	@XmlElement(name="service_id")
	public void setService_id(String service_id) {
		this.service_id = service_id;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append(getP_title());
		
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getP_albumid()));
	
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getAlbumid()));
	
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getCategory_id()));
	
		sb.append(GlobalCom.colsep);
		sb.append(GlobalCom.isNull(getService_id()));
		
		return sb.toString();
	}
	
}
