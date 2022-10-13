<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%@include file="/WEB-INF/views/include/test_include.jsp" %>
<img src="/smartux_adm/images/choiye.png" /><br/>
sample ${mytest}<br/>
되라<br/>
<jsp:include page="/WEB-INF/views/include/test_include.jsp">
<jsp:param name="param1" value="abc" />
</jsp:include>
</body>
</html>