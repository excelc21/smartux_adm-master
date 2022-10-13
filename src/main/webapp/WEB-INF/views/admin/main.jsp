<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
//String domain_http 	= (String)request.getAttribute("domain_http");
String user_auth 	= (String)request.getAttribute("user_auth");
String msa_open_Yn 	= (String)request.getAttribute("msa_open_Yn");

if(StringUtils.isNotBlank(msa_open_Yn) && msa_open_Yn.equals("Y")) {
	response.sendRedirect("/smartux_adm/admin/login/loginMain.do");
} else {
	if (user_auth.equals("00")) {//슈퍼관리자
		response.sendRedirect("/smartux_adm/admin/login/list.do");
	} 
	else if (user_auth.equals("01") || user_auth.equals("03")) {//일반관리자, VOD프로모션관리자
		response.sendRedirect("/smartux_adm/admin/login/list.do");
	} 
	else if (user_auth.equals("02")) {//세컨드TV관리자
		response.sendRedirect("/smartux_adm/admin/startup/getEmergency.do?scr_tp=T&display_type=S");
	} 
	/* 
	else if (user_auth.equals("03")) {//VOD프로모션관리자
		response.sendRedirect(domain_http+"/smartux_adm/admin/gpack/pack/getTvReplayPackView.do");
	} 
	*/
	else if (user_auth.equals("04")) {//시즌관리자
		response.sendRedirect("/smartux_adm/admin/season/seasonList.do");
	}
}

%>
</body>
</html>