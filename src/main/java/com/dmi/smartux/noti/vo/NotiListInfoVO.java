package com.dmi.smartux.noti.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

@SuppressWarnings( "serial" )
@XmlRootElement( name = "record" )
@XmlType( name = "NotiListInfoVO", namespace = "com.dmi.smartux.noti.vo.NotiListInfoVO", propOrder = { "reg_no", "title", "reg_dt", "is_fixed", "is_new" } )
public class NotiListInfoVO extends BaseVO implements Serializable {
	private String reg_no = "";
	private String title = "";
	private String reg_dt = "";
	private String is_fixed = "";
	private String is_new = "";

	public String getReg_no ( ) {
		return reg_no;
	}

	@XmlElement( name = "reg_no" )
	public void setReg_no ( String reg_no ) {
		this.reg_no = reg_no;
	}

	public String getTitle ( ) {
		return title;
	}

	@XmlElement( name = "title" )
	public void setTitle ( String title ) {
		this.title = title;
	}

	public String getReg_dt ( ) {
		return reg_dt;
	}

	@XmlElement( name = "reg_dt" )
	public void setReg_dt ( String reg_dt ) {
		this.reg_dt = reg_dt;
	}

	public String getIs_fixed ( ) {
		return is_fixed;
	}

	@XmlElement( name = "is_fixed" )
	public void setIs_fixed ( String is_fixed ) {
		this.is_fixed = is_fixed;
	}

	public String getIs_new ( ) {
		return is_new;
	}

	@XmlElement( name = "is_new" )
	public void setIs_new ( String is_new ) {
		this.is_new = is_new;
	}

	@Override
	public String toString ( ) {
		StringBuffer sb = new StringBuffer ( );
		sb.append ( getReg_no ( ) );
		sb.append ( GlobalCom.colsep );
		sb.append ( getTitle ( ) );
		sb.append ( GlobalCom.colsep );
		sb.append ( getReg_dt ( ) );
		sb.append ( GlobalCom.colsep );
		sb.append ( getIs_fixed ( ) );
		sb.append ( GlobalCom.colsep );
		sb.append ( getIs_new ( ) );

		return sb.toString ( );
	}
}