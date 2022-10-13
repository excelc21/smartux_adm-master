package com.dmi.smartux.noti.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.ResultVO;

@SuppressWarnings( "serial" )
@XmlRootElement( name = "result" )
@XmlType( name = "PopupNotiVO", namespace = "com.dmi.smartux.noti.vo.PopupNotiVO", propOrder = { "flag", "message", "noti_tit", "noti_cont" } )
public class PopupNotiVO extends ResultVO implements Serializable {
	protected String flag = "";
	protected String message = "";
	/**
	 * 팝업 게시판 제목
	 */
	protected String noti_tit = "";
	/**
	 * 팝업 게시판 본문
	 */
	protected String noti_cont = "";

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

	/**
	 * @return 팝업 게시판 제목
	 */
	public String getNoti_tit ( ) {
		return noti_tit;
	}

	/**
	 * @param noti_tit 팝업 게시판 제목
	 */
	@XmlElement( name = "noti_tit" )
	public void setNoti_tit ( String noti_tit ) {
		this.noti_tit = noti_tit;
	}

	/**
	 * @return the noti_cont 팝업 게시판 본문
	 */
	public String getNoti_cont ( ) {
		return noti_cont;
	}

	/**
	 * @param noti_cont 팝업 게시판 본문
	 */
	@XmlElement( name = "noti_cont" )
	public void setNoti_cont ( String noti_cont ) {
		this.noti_cont = noti_cont;
	}

	@Override
	public String toString ( ) {
		StringBuffer sb = new StringBuffer ( );
		sb.append ( getFlag ( ) );
		sb.append ( GlobalCom.colsep );
		sb.append ( getMessage ( ) );
		sb.append ( GlobalCom.colsep );
		sb.append ( getNoti_tit ( ) );
		sb.append ( GlobalCom.colsep );
		sb.append ( getNoti_cont ( ) );

		return sb.toString ( );
	}
}
