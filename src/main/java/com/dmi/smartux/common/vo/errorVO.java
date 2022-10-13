package com.dmi.smartux.common.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "error" )
public class errorVO {
	private String code;
	private String message;

	public errorVO( ) {

	}

	public errorVO( String code, String message ) {
		this.code = code;
		this.message = message;
	}

	public String getCode ( ) {
		return code;
	}

	@XmlElement( name = "code" )
	public void setCode ( String code ) {
		this.code = code;
	}

	public String getMessage ( ) {
		return message;
	}

	@XmlElement( name = "message" )
	public void setMessage ( String message ) {
		this.message = message;
	}

}
