package com.dmi.smartux.noti.vo;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.ResultVO;

@SuppressWarnings( "serial" )
@XmlRootElement( name = "result" )
@XmlType( name = "NotiListVO", namespace = "com.dmi.smartux.noti.vo.NotiListVO", propOrder = { "flag", "message", "version", "new_cnt", "total_cnt", "recordset" } )
public class NotiListVO extends ResultVO implements Serializable {
	private String flag = "";
	private String message = "";
	private String version = "";
	private String new_cnt = "";
	private String total_cnt = "";

	private List<NotiListInfoVO> recordset;

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

	public String getVersion ( ) {
		return version;
	}

	@XmlElement( name = "version" )
	public void setVersion ( String version ) {
		this.version = version;
	}

	public String getNew_cnt ( ) {
		return new_cnt;
	}

	@XmlElement( name = "new_cnt" )
	public void setNew_cnt ( String new_cnt ) {
		this.new_cnt = new_cnt;
	}

	public String getTotal_cnt ( ) {
		return total_cnt;
	}

	@XmlElement( name = "total_cnt" )
	public void setTotal_cnt ( String total_cnt ) {
		this.total_cnt = total_cnt;
	}

	@XmlElementWrapper( name = "recordset" )
	@XmlElementRefs( { @XmlElementRef( name = "record", type = NotiListInfoVO.class ) } )
	public List<NotiListInfoVO> getRecordset ( ) {
		return recordset;
	}

	public void setRecordset ( List<NotiListInfoVO> recordset ) {
		this.recordset = recordset;
	}

	@Override
	public String toString ( ) {
		StringBuffer sb = new StringBuffer ( );
		sb.append ( getFlag ( ) );
		sb.append ( GlobalCom.colsep );
		sb.append ( getMessage ( ) );
		// 에러가 발생할 경우 version 정보가 셋팅이 되지 않으므로 문자열 결합시 version이 결합되지 않도록 한다
		if ( !( GlobalCom.isNull ( version ).equals ( "" ) ) ) {
			sb.append ( GlobalCom.colsep );
			sb.append ( getVersion ( ) );
		}
		sb.append ( GlobalCom.colsep );
		sb.append ( getNew_cnt ( ) );
		sb.append ( GlobalCom.colsep );
		sb.append ( getTotal_cnt ( ) );
		if ( recordset != null ) {
			if ( recordset.size ( ) != 0 ) {
				StringBuffer record = new StringBuffer ( );
				record.append ( GlobalCom.rssep );
				boolean start = true;
				for ( NotiListInfoVO item : recordset ) {
					if ( start ) {
						start = false;
					} else {
						record.append ( GlobalCom.rowsep );
					}
					record.append ( item );
				}
				sb.append ( record.toString ( ) );
			}
		}

		return sb.toString ( );
	}
}
