<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<title>LG U+ HDTV</title>

<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
$(document).ready(function(){
	if("${success}"=="N"){
		alert("해당 가입자번호가 존재합니다.");
	}else if("${success}"=="X"){
		alert("정상등록하지 못하였습니다\n\n다시한번 시도해 주세요.");
	}
	$(location).attr("href", "./qualityMemberList.do?findName=${vo.findName}&findValue=${vo.findValue}&s_log_type=${vo.s_log_type}&serviceType=${vo.serviceType}");
});
</script>
<body leftmargin="0" topmargin="0">

</body>
</html>