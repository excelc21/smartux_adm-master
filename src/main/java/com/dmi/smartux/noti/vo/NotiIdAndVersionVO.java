package com.dmi.smartux.noti.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;

/**
 * 공지 별 아이디 와 버전 정보를 가진다.
 * 
 * @author jch82@naver.com
 */
@XmlRootElement( name = "result" )
@XmlType( name = "NotiIdAndVersionVO", namespace = "com.dmi.smartux.noti.vo.NotiIdAndVersionVO", propOrder = { "bbs_id", "version" } )
public class NotiIdAndVersionVO {
	private String bbs_id;
	private String version;

	/**
	 * @return 게시판 아이디
	 */
	public String getBbs_id ( ) {
		return bbs_id;
	}

	/**
	 * @param bbs_id 게시판 아이디
	 */
	@XmlElement( name = "bbs_id" )
	public void setBbs_id ( String bbs_id ) {
		this.bbs_id = bbs_id;
	}

	/**
	 * @return 게시판의 마지막 버전
	 */
	public String getVersion ( ) {
		return version;
	}

	/**
	 * @param version 게시판의 마지막 버전
	 */
	@XmlElement( name = "version" )
	public void setVersion ( String version ) {
		this.version = version;
	}

	@Override
	public String toString ( ) {
		StringBuffer sb = new StringBuffer ( );
		sb.append ( getBbs_id ( ) );
		sb.append ( GlobalCom.colsep );
		sb.append ( getVersion ( ) );

		return sb.toString ( );
	}
}
