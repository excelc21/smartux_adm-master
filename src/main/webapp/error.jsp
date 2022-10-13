<%@page import="com.dmi.smartux.common.util.GlobalCom"%>
<%@ page import="com.dmi.smartux.common.property.SmartUXProperties" %>
<%
	String errorCode = GlobalCom.isNull(request.getParameter("error_code"),"500");
	String result = "";
	//String test = request.getHeader("accept");
	//out.println(test);
	if(request.getHeader("accept") == null ){
		response.setHeader("content-type", "text/plain;charset=UTF-8");
		result = SmartUXProperties.getProperty("error_" + errorCode + "_text");
	}else{
		if(request.getHeader("accept").indexOf("application/json") != -1){
			response.setHeader("content-type", "application/json;charset=UTF-8");
			result = SmartUXProperties.getProperty("error_"+errorCode+"_json");
		}else if(request.getHeader("accept").indexOf("application/xml") != -1 && request.getHeader("accept").indexOf("text/html") == -1){
			response.setHeader("content-type", "application/xml;charset=UTF-8");
			result = SmartUXProperties.getProperty("error_"+errorCode+"_xml");
		}else{
			response.setHeader("content-type", "text/plain;charset=UTF-8");
			result = SmartUXProperties.getProperty("error_"+errorCode+"_text");
		}
	}
%>
<%=result%>