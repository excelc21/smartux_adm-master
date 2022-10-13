<%@page import="org.springframework.web.util.CookieGenerator"%>
<%@page import="com.dmi.smartux.common.util.aes.StringEncrypter"%>
<%@page import="com.dmi.smartux.common.util.aes.Aes_Key"%>
<%@page import="com.dmi.smartux.common.util.CookieUtil"%>
<%
	Aes_Key aec_key = new Aes_Key();
	StringEncrypter encrypter = new StringEncrypter(aec_key.key,aec_key.vec);
	Cookie idCookie = CookieUtil.getCookie(request, "smartUXManager");
	Cookie authCookie = CookieUtil.getCookie(request, "smartUXManagerAuth");
	
	Cookie cookieTimeout = CookieUtil.getCookie(request, "smartUXCookieTimeout");
	
	
	
	if(cookieTimeout == null){
		response.sendRedirect("/smartux_adm/admin/login/login.do");
	}else{
		if(cookieTimeout.getValue().equals("")){
			response.sendRedirect("/smartux_adm/admin/login/login.do");
		}else{
			long cookieStartTime = Long.parseLong(cookieTimeout.getValue());
			long cookieEndTime =  (long) (System.currentTimeMillis()/1000.0);
			
			System.out.println("cookieStartTime = "+cookieStartTime);
			System.out.println("cookieEndTime = "+cookieEndTime);
			
			if((cookieEndTime-cookieStartTime) <= 600){		//쿠키 유효성 10분 체크
				//Cookie cookie = new Cookie("smartUXCookieTimeout",Long.toString(cookieEndTime));
				//response.addCookie(cookie);
				CookieGenerator cg = new CookieGenerator();
				cg.setCookieName("smartUXCookieTimeout");
				cg.addCookie(response,Long.toString(cookieEndTime));
			}else{
				response.sendRedirect("/smartux_adm/admin/login/login.do");
			}
		}
	}
	
	String id_encrypt = "";
	String id_decrypt = "";
	String auth_encrypt = "";
	String auth_decrypt = "";
	String authStr = "";
	if(idCookie == null){
		//out.println("<script type='text/javascript'>alert('로그인 후 이용해 주시기 바랍니다.');location.href='/smartux_adm/admin/login/login.do';</script>");
		//out.flush();
		//response.setHeader ( "Refresh", "0; URL=/smartux_adm/admin/login/login.do" );
		response.sendRedirect("/smartux_adm/admin/login/login.do");
		//return;
	}else{
		if(idCookie.getValue().equals("")){
			//out.println("<script type='text/javascript'>alert('로그인 후 이용해 주시기 바랍니다.');location.href='/smartux_adm/admin/login/login.do';</script>");
			//out.flush();	
			//response.setHeader ( "Refresh", "0; URL=/smartux_adm/admin/login/login.do" );
			response.sendRedirect("/smartux_adm/admin/login/login.do");
			//return;
		}else{
			id_encrypt = java.net.URLDecoder.decode(idCookie.getValue()); //쿠키사용시 decoding
			id_decrypt = encrypter.decrypt(id_encrypt);
			
			auth_encrypt = java.net.URLDecoder.decode(authCookie.getValue()); //쿠키사용시 decoding
			auth_decrypt = encrypter.decrypt(auth_encrypt);
			
			if(auth_decrypt.equals("00")){
				authStr = "슈퍼관리자";
			}else if(auth_decrypt.equals("01")){
				authStr = "일반관리자";
			}else if(auth_decrypt.equals("02")){
				authStr = "세컨드TV관리자";
			}else if(auth_decrypt.equals("03")){
				authStr = "VOD프로모션관리자";
			}else if(auth_decrypt.equals("04")){
				authStr = "시즌관리자";
			}else{
				authStr = "미확인관리자";
			} 
		}
		//out.println(id_encrypt);
		//out.println(id_decrypt);
		
		//out.println(auth_encrypt);
		//out.println(auth_decrypt);
	}
%>
