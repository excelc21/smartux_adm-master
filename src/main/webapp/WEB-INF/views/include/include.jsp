<%@ page import="org.springframework.web.bind.ServletRequestUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
    //response.setHeader("Cache-Control","no-cache"); 
    //response.setHeader("Pragma","no-cache");
    //response.setDateHeader ("Expires", 0);
    response.setDateHeader("Expires", -1);
	response.setHeader("Pragma","no-cache");
	response.setHeader("Cache-Control","no-store"); //file://HTTP 1.0
	response.setHeader("Cache-Control","no-cashe"); //file://HTTP 1.1
    response.setCharacterEncoding("utf-8");
    String webRoot = request.getContextPath();
    //out.println("<!-- webRoot : " + webRoot + " -->"); 
    
%>