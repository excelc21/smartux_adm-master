package com.dmi.smartux.common.util;

import com.dmi.smartux.common.util.aes.Aes_Key;
import com.dmi.smartux.common.util.aes.StringEncrypter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {
	public static Cookie getCookie(HttpServletRequest request, String name) {
//  Cookie[] cookies = request.getCookies();
//  Cookie returnCookie = null;
//  
//  if(cookies != null){
//   for(int i=0;i < cookies.length; i++){
//    if(cookies[i].getName().equals(name)){
//     returnCookie = cookies[i];
//     break;
//    }
//   }
//  }
//  return returnCookie;

		Cookie returnCookie = null;
		String cookies = request.getHeader("cookie");
		cookies = GlobalCom.isNull(cookies);    // smartUXManager=b4O1pWOPMINraVWrZCB3Ug==; smartUXManagerAuth=zdgTniQEigc/Bb2b7ZuHHA==

		if (!cookies.equals("")) {
			if (cookies.indexOf(";") != -1) {
				String[] _cookies = cookies.split(";");    //smartUXManager=b4O1pWOPMINraVWrZCB3Ug==
				for (int i = 0; i < _cookies.length; i++) {
					if (_cookies[i].indexOf("=") != -1) {
						String[] _cookieName = _cookies[i].split("=");    //smartUXManager
						if (name.equals(_cookieName[0].trim())) {
							String _cookieVal = _cookies[i].substring(_cookieName[0].length() + 1, _cookies[i].length());
							_cookieVal = _cookieVal.replaceAll("\"", "");
							returnCookie = new Cookie(name, _cookieVal);
							break;
						}
					}
				}
			}
		}
		return returnCookie;
	}

	public static String getCookieUserID(HttpServletRequest request) throws Exception{

		Cookie hdtvManager = getCookie(request,"smartUXManager");
		Aes_Key aec_key = new Aes_Key();
		StringEncrypter encrypter = new StringEncrypter(aec_key.key,aec_key.vec);

		return encrypter.decrypt(java.net.URLDecoder.decode(hdtvManager.getValue()));
	}
	
	public static String getCookieUserAuth(HttpServletRequest request) throws Exception{

		Cookie hdtvManager = getCookie(request,"smartUXManagerAuth");
		Aes_Key aec_key = new Aes_Key();
		StringEncrypter encrypter = new StringEncrypter(aec_key.key,aec_key.vec);

		return encrypter.decrypt(java.net.URLDecoder.decode(hdtvManager.getValue()));
	}
}


/**
 <%@ page import = "javacan.util.CookieUtil" %>
 <%@ page language="java" contentType="text/html; charset=EUC-KR"
 pageEncoding="EUC-KR"%>
 <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 <html>
 <head>
 <meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
 <title>Insert title here</title>
 </head>
 <body>
 javacan.util.CookieUtil 클래스 테스트
 <%
 String cookieName = "myCookie";
 Cookie cookie = CookieUtil.getCookie(request, cookieName);
 if(cookie == null){%>
 이름이 "<%=cookieName%>"인 쿠키는 존재하지 않습니다.
 <%} else{%>

 "<%=cookieName %>"의 값 : <%=cookie.getValue() %>
 <%
 }
 %>
 </body>
 </html>
 **/
