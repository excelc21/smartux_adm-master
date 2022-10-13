<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#regbtn").click(function(){
		var theme_code = $("#theme_code").val();
		var theme_name = $("#theme_name").val();
		var use_yn = $(':input:radio[name="use_yn"]:checked').val();
		
		if($("#theme_code").val() == ""){
			alert("등록하고자 하는 테마의 테마 코드를 입력해주세요");
			$("#theme_code").focus();
		}else if($("#theme_name").val() == ""){
			alert("등록하고자 하는 테마의 테마명을 입력해주세요");
			$("#theme_name").focus();
		}else{
			
			$.post("<%=webRoot%>/admin/smartepg/insertThemeInfo.do", 
					 {theme_code : theme_code, theme_name : theme_name, use_yn : use_yn},
					  function(data) {
						 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
						 	var flag = data.flag;
						 	var message = data.message;
						 	
						 	if(flag == "0000"){						// 정상적으로 처리된 경우
						 		opener.location.reload();
								alert("테마가 등록되었습니다");
								self.close();
						 	}else{
						 		if(flag == "9000"){
						 			alert("현재 등록하고자 하는 테마 코드 " + theme_code + "는 이미 등록되어 있는 테마 코드입니다\n다른 코드를 사용해주세요");
						 			$("#theme_code").focus();
						 		}else{
						 			alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
						 		}
						 	}
					  },
					  "json"
		    );
		}
		
	});
	
	$("#resetbtn").click(function(){
		$("#regfrm")[0].reset();
	});
});
</script>
</head>
<body>
<form id="regfrm" name="regfrm" method="post" action="">
테마 등록<br/>
<table>
	<tr>
		<td>테마 코드</td>
		<td>
			<input type="text" id="theme_code" name="theme_code" value="" />
		</td>
	</tr>
	<tr>
		<td>테마명</td>
		<td>
			<input type="text" id="theme_name" name="theme_name" value="" />
		</td>
	</tr>
	<tr>
		<td>사용여부</td>
		<td>
			<input type="radio" id="use_ynY" name="use_yn" value="Y" checked/>예
			<input type="radio" id="use-ynN" name="use_yn" value="N" />아니오
		</td>
	</tr>
</table>
<input type="button" id="regbtn" value="등록" />
<input type="button" id="resetbtn" value="재작성" />
</form>
</body>
</html>