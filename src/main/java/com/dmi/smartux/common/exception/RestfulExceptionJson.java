package com.dmi.smartux.common.exception;

public class RestfulExceptionJson extends RuntimeException {

	String flag;
	String message;

	public String getFlag ( ) {
		return flag;
	}

	public void setFlag ( String flag ) {
		this.flag = flag;
	}

	public String getMessage ( ) {
		return message;
	}

	public void setMessage ( String message ) {
		this.message = message;
	}

}
