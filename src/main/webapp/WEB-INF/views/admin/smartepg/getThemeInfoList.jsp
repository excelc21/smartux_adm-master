<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#regbtn").click(function(){
		window.open("<%=webRoot%>/admin/smartepg/insertThemeInfo.do", "regwin", "width=300,height=200");
	});
	
	$("#delbtn").click(function(){
		alert("222");
	});
	
	$("#change_order").click(function(){
		alert("333");
	});
	
	$("#allchk").click(function(){

	});
});
</script>
</head>
<body>
테마 코드 목록<br/>
<table>
	<tr>
		<td><input type="checkbox" id="allchk" name="allchk" value=""></td>
		<td>테마코드</td>
		<td>테마명</td>
		<td>사용여부</td>
	</tr>
	<c:choose>
		<c:when test="${result==null || fn:length(result) == 0}">
			<tr>
				<td colspan="4">검색된 테마가 없습니다</td>
			</tr>
		</c:when>
		<c:otherwise>
			<c:forEach var="item" items="${result}" varStatus="status">
				<tr>
					<td><input type="checkbox" name="delchk" value="${item.theme_code }"></td>
					<td>${item.theme_code }</td>
					<td>
						<a href="<%=webRoot%>/admin/smartepg/viewThemeInfo.do?theme_code=${item.theme_code}">${item.theme_name }</a>
					</td>
					<td>${item.use_yn }</td>
				</tr>
			</c:forEach>
		</c:otherwise>
	</c:choose>
</table>
<form id="frm" name="frm" method="post">
	<input type="hidden" id="delthemecodes" name="delthemecodes" value="" />
</form>
<input type="button" id="regbtn" value="등록" />
<input type="button" id="delbtn" value="삭제" />
<input type="button" id="change_order" value="순서 바꾸기" />
</body>
</html>