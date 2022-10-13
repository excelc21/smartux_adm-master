package com.dmi.smartux.common.aop;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.dmi.smartux.authentication.vo.AuthenticationCommon;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;

@Aspect
public class AuthenticationAspect {
	
	@Around("execution(* *com.dmi.smartux..*_OpenApi_XX(..))")
	public Object testBefore(ProceedingJoinPoint joinPoint) throws Throwable {
		SmartUXException exception = new SmartUXException();
		
		HttpServletRequest request = null;
        HttpServletResponse response = null;
        for ( Object o : joinPoint.getArgs() ){ 
            if ( o instanceof HttpServletRequest ) {
                request = (HttpServletRequest)o;
            } 
            if ( o instanceof HttpServletResponse ) {
                response = (HttpServletResponse)o;
            }
        }
		Enumeration penum = request.getParameterNames();
		String access_key = "";
		String cp_id = "";
		while(penum.hasMoreElements()){
			String key = (String)penum.nextElement();
			String value = (new String(request.getParameter(key)) == null) ? "" : new String(request.getParameter(key));
			
			if(key.equalsIgnoreCase("access_key")){
				access_key = value;
			}
			if(key.equalsIgnoreCase("cp_id")){
				cp_id = value;
			}
		}
		String uri = request.getRequestURI();
		String method = request.getMethod();
		uri = AuthenticationUri(uri);
		
//		String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,method,uri));
		String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationChek(access_key,cp_id,method,uri));
		
		if(!authResult.equals("SUCCESS")){

			if(authResult.equals("ACCESS_KEY")){
				exception.setFlag(SmartUXProperties.getProperty("flag.authentication.accessKey"));
				exception.setMessage(SmartUXProperties.getProperty("message.authentication.accessKey"));
			}else if(authResult.equals("CP_ID")){
				exception.setFlag(SmartUXProperties.getProperty("flag.authentication.cp_id"));
				exception.setMessage(SmartUXProperties.getProperty("message.authentication.cp_id"));
			}else if(authResult.equals("EXDATE")){
				exception.setFlag(SmartUXProperties.getProperty("flag.authentication.exdate"));
				exception.setMessage(SmartUXProperties.getProperty("message.authentication.exdate"));
			}else if(authResult.equals("URL")){
				exception.setFlag(SmartUXProperties.getProperty("flag.authentication.url"));
				exception.setMessage(SmartUXProperties.getProperty("message.authentication.url"));
			}else if(authResult.equals("FAIL")){
				exception.setFlag(SmartUXProperties.getProperty("flag.authentication.fail"));
				exception.setMessage(SmartUXProperties.getProperty("message.authentication.fail"));
			}
			response.setStatus(401);
			
			throw exception;
		}
		return joinPoint.proceed();
	}
	
	public String AuthenticationUri(String uri){
		String returnUri = "";

		returnUri = uri.replace("/smartux", "");
		
		return returnUri;
	}

}
