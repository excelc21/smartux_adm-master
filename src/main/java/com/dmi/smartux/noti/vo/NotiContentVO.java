package com.dmi.smartux.noti.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.ResultVO;

@SuppressWarnings( "serial" )
@XmlRootElement( name = "result" )
@XmlType( name = "NotiContentVO", namespace = "com.dmi.smartux.noti.vo.NotiContentVO", propOrder = { "flag", "message", "title", "content", "ev_cont_id", "img_url" } )
public class NotiContentVO extends ResultVO implements Serializable {
	private String flag = "";
	private String message = "";
	private String title = "";
	private String content = "";
	private String ev_cont_id = "";
	private String img_url = "";

	public String getFlag ( ) {
		return flag;
	}

	@XmlElement( name = "flag" )
	public void setFlag ( String flag ) {
		this.flag = flag;
	}

	public String getMessage ( ) {
		return message;
	}

	@XmlElement( name = "message" )
	public void setMessage ( String message ) {
		this.message = message;
	}

	public String getTitle ( ) {
		return title;
	}

	@XmlElement( name = "title" )
	public void setTitle ( String title ) {
		this.title = title;
	}

	public String getContent ( ) {
		return content;
	}

	@XmlElement( name = "content" )
	public void setContent ( String content ) {
		this.content = content;
	}

	public String getEv_cont_id ( ) {
		return ev_cont_id;
	}

	@XmlElement( name = "ev_cont_id" )
	public void setEv_cont_id ( String ev_cont_id ) {
		this.ev_cont_id = ev_cont_id;
	}

	public String getImg_url ( ) {
		return img_url;
	}

	@XmlElement( name = "img_url" )
	public void setImg_url ( String img_url ) {
		this.img_url = img_url;
	}

	@Override
	public String toString ( ) {
		StringBuffer sb = new StringBuffer ( );
		sb.append ( getFlag ( ) );
		sb.append ( GlobalCom.colsep );
		sb.append ( getMessage ( ) );
		sb.append ( GlobalCom.colsep );
		sb.append ( getTitle ( ) );
		sb.append ( GlobalCom.colsep );
		sb.append ( getContent ( ) );
		sb.append ( GlobalCom.colsep );
		sb.append ( getEv_cont_id ( ) );
		sb.append ( GlobalCom.colsep );
		sb.append ( getImg_url ( ) );

		return sb.toString ( );
	}
}
