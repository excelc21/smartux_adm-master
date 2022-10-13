package com.dmi.smartux.common.exception;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;

import com.dmi.smartux.common.property.SmartUXProperties;
import org.springframework.jdbc.UncategorizedSQLException;

/**
 * Exception을 받아서 Exception을 분석한뒤 관련 flag와 Message를 셋팅하는 Handler 클래스
 * @author Terry Chang
 *
 */
public class ExceptionHandler {

	private String flag;
	private String message;
	
	private Exception e;
	
	public ExceptionHandler(Exception e){
		this.e = e;
		processException();
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Exception getE() {
		return e;
	}

	public void setE(Exception e) {
		this.e = e;
	}

	public void processException(){
		if(e instanceof DuplicateKeyException){
			flag = SmartUXProperties.getProperty("flag.key1");
			message = SmartUXProperties.getProperty("message.key1");
			
		}else if(e instanceof BadSqlGrammarException || e instanceof UncategorizedSQLException){
			flag = SmartUXProperties.getProperty("flag.db");
			message = SmartUXProperties.getProperty("message.db");
		}else if(e instanceof SocketException){
			flag = SmartUXProperties.getProperty("flag.socket");
			message = SmartUXProperties.getProperty("message.socket");
		}else if(e instanceof SocketTimeoutException){
			flag = SmartUXProperties.getProperty("flag.socketime");
			message = SmartUXProperties.getProperty("message.socketime");
			
		}else if(e instanceof SmartUXException){
			SmartUXException se = (SmartUXException)e;
			flag = se.getFlag();
			message = se.getMessage();
			
		}else{
			flag = SmartUXProperties.getProperty("flag.etc");
			message = SmartUXProperties.getProperty("message.etc");
		}
	}
}
