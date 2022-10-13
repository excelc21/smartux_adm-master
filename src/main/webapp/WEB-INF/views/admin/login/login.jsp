<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<script src="/smartux_adm/js/basic_script.js" type="text/javascript"></script>
<script type="text/javascript">
function go(){
	
	var f = document.form1;
	
	if(f.smartux_id.value == ''){
		alert('아이디를 입력해주세요.');
		f.smartux_id.focus();
		return;	
	}else if(f.smartux_pwd.value == ''){
		alert('비밀번호를 입력해 주세요.');
		f.smartux_pwd.focus();
		return;
	//}else if(!CheckPassword(f.smartux_id.value,f.smartux_pwd.value)){
	//	f.smartux_pwd.value = '';
	//	f.smartux_pwd.focus();
	//	return;
	}
	
	f.submit();
}

function CheckPassword(uid, upw){
    if(!/^[a-zA-Z0-9]{10,20}$/.test(upw)){ 
        alert('비밀번호는 숫자와 영문자 조합으로 10~20자리를 사용해야 합니다.'); 
        return false;
    }

  
    var chk_num = upw.search(/[0-9]/g);
    var chk_eng = upw.search(/[a-z]/ig);

    if(chk_num < 0 || chk_eng < 0){
        alert('비밀번호는 숫자와 영문자를 혼용하여야 합니다.');
        return false;
    }
    
    if(/(\w)\1\1\1/.test(upw)){
        alert('비밀번호에 같은 문자를 4번 이상 사용하실 수 없습니다.'); 
        return false;
    }

    if(upw.search(uid)>-1){
        alert('ID가 포함된 비밀번호는 사용하실 수 없습니다.'); 
        return false;
    }
    
    return true;

} 

function enterKey(){
	
	if(event.keyCode==13){
		go();
	}
}
</script>
</head>
<%
String domain_https = (String)request.getAttribute("domain_https");
%>
<body leftmargin="0" topmargin="0" background="/smartux_adm/images/admin/login/back6.gif" marginheight="0" marginwidth="0">
<div id="minical" oncontextmenu="return false" ondragstart="return false" onselectstart="return false" style="background:#E7F3FF; margin:5; padding:5;margin-top:2;border-top:1 solid buttonshadow;border-left: 1 solid buttonshadow;border-right: 1 solid buttonshadow;border-bottom:1 solid buttonshadow;width:160;display:none;position: absolute; z-index: 99"></div>
		
	<form name="form1" action="/smartux_adm/admin/login/login.do" method="POST">
		   
			<div align="center">
				<table background="/smartux_adm/images/admin/login/upback.gif" border="0" cellpadding="0" cellspacing="0" height="121" width="100%">
					<tbody><tr>
						<td>
							
						</td>
					</tr>
				</tbody></table>
				
				
				<!--
				<p>-아이디 
					<label for="textfield"></label>
					<input type="text" name="userID" id="textfield" />
					-비밀번호 
					<label for="label"></label>
					<input type="password" name="password" id="label" />
					<input type="button" value="로그인" onclick="go('a')" />
				</p>
				-->

			
				<p>&nbsp;</p>
				
				<table bgcolor="white" border="0" cellspacing="2" height="320" width="510">
					<tbody><tr>
						<td align="center">
							<table border="0" cellpadding="0" cellspacing="0" height="310" width="499">
								<tbody><tr height="50">
									<td colspan="4">
										<img src="/smartux_adm/images/admin/login/IPTV01.gif" style="vertical-align: top;" height="45" hspace="0" vspace="" width="289">
									</td>
									<td></td>
								</tr>
								<tr bgcolor="#5599CC" height="3"><td colspan="5"></td></tr>
								<tr bgcolor="#FFFFFF" height="1"><td colspan="5"></td></tr>
								<tr bgcolor="#5599CC" height="1"><td colspan="5"></td></tr>
								<tr height="180">
									<td colspan="5">
										<img src="/smartux_adm/images/admin/login/logo_iptv.png" style="vertical-align: top;" height="180" hspace="0" vspace="" width="499">
									</td>			   
								</tr>
								<tr height="5"><td colspan="5"></td></tr>
								<tr height="29">
									<td colspan="5" background="/smartux_adm/images/admin/login/copy.gif">
<table background="/smartux_adm/images/admin/login/copy.gif" width="100%">
<tbody>
	<tr>
		<td width="110"></td><td><img src="/smartux_adm/images/admin/login/ID.gif" height="17" width="49" align="absmiddle"></td>
		<td><input name="smartux_id" maxlength="12" style="font-size: 12px;" tabindex="1" type="text" value="${smartux_id}"></td>
		<td><img src="/smartux_adm/images/admin/login/password.gif" height="17" width="63" align="absmiddle"></td>
		<td><input name="smartux_pwd" maxlength="20" onkeypress="javascript:enterKey();" style="font-size: 12px;" tabindex="2" type="password" value="${smartux_pwd}"></td>
		<td><img src="/smartux_adm/images/admin/login/loginbutton.gif" onkeypress="javascript:enterKey();" style="" onclick="javascript:go();" tabindex="3" height="20" width="45" align="absmiddle"></td>
	</tr>
</tbody>
</table> 
	  
										
									</td>
									
								</tr>
								<tr height="5">
									<td colspan="5">
										
									</td>
								</tr>		   
							</tbody></table>
						</td>
					</tr>
				</tbody></table>
			</div>
		
	</form>
<c:choose>
<c:when test="${flagtype == '1'}">
	<script type="text/javascript">
		alert('정보가 수정되었습니다. 재로그인하세요.');		
	</script>
</c:when>
<c:when test="${result == 'NOT ID'}">
	<script type="text/javascript">
		alert('아이디를 입력해 주세요.');
		document.form1.smartux_id.value='';
		document.form1.smartux_id.focus();
	</script>
</c:when>
<c:when test="${result == 'NOT PWD'}">
	<script type="text/javascript">
		alert('비밀번호를 입력해 주세요.');
		document.form1.smartux_pwd.value='';
		document.form1.smartux_pwd.focus();
	</script>
</c:when>
<c:when test="${result == 'PWD LENGTH'}">
	<script type="text/javascript">
	//alert('비밀번호는 숫자와 영문자 조합으로 10~20자리를 사용해야 합니다.');
	alert('잘못된 요청입니다.');
	document.form1.smartux_pwd.value='';
	document.form1.smartux_pwd.focus();
	</script>
</c:when>
<c:when test="${result == 'PWD MIX'}">
	<script type="text/javascript">
	//alert('비밀번호는 숫자와 영문자를 혼용하여야 합니다.');
	alert('잘못된 요청입니다.');
	document.form1.smartux_pwd.value='';
	document.form1.smartux_pwd.focus();
	</script>
</c:when>


<c:when test="${result == 'NOT DATA'}">
	<script type="text/javascript">
		//alert('아이디/비밀번호를 정확하게 입력해 주세요.');
		alert('잘못된 요청입니다.');
		document.form1.smartux_id.value='';
		document.form1.smartux_pwd.value='';
		document.form1.smartux_id.focus();
	</script>
</c:when>
<c:when test="${result == 'PASSWORD FAIL'}">
	<script type="text/javascript">
		//alert('비밀번호를 정확하게 입력해 주세요.');
		alert('잘못된 요청입니다.');
		document.form1.smartux_pwd.value='';
		document.form1.smartux_pwd.focus();
	</script>
</c:when>
<c:when test="${result == 'LOGIN FAIL CTN'}">
	<script type="text/javascript">
		alert('계정이 잠금 상태입니다.(관리자 문의)');
		document.form1.smartux_pwd.value='';
		document.form1.smartux_pwd.focus();
	</script>
</c:when>
<c:when test="${result == 'INSERT SUCCESS'}">
	<script type="text/javascript">
		alert('정상적으로 등록 되었습니다.');
		document.form1.smartux_pwd.value='';
		document.form1.smartux_pwd.focus();
	</script>
</c:when>

</c:choose>
</body>
</html>