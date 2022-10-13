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
	}else if(!CheckPassword(f.smartux_id.value,f.smartux_pwd.value)){
		f.smartux_pwd.value = '';
		f.smartux_pwd.focus();
		return;
	}else if(f.smartux_pwd_chk.value == ''){
		alert('비밀번호 확인을 입력해 주세요.');
		f.smartux_pwd_chk.focus();
		return;
	}else if(f.smartux_pwd.value != f.smartux_pwd_chk.value){
		alert('비밀번호와 비밀번호 확인이 일치하지 않습니다.');
		f.smartux_pwd.value = '';
		f.smartux_pwd_chk.value = '';
		f.smartux_pwd.focus();
		return;
	}else if(f.smartux_exp_day.value == ''){
		alert('만료일수을 입력해 주세요.');
		return;
	}else if(!CheckNumber(f.smartux_exp_day.value)){
		f.smartux_exp_day.value = '';
		f.smartux_exp_day.focus();
		return;
	}else if(f.smartux_exp_day.value > 90){
		alert('만료일은 90일 이하만 입력 가능합니다.');
		return;
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

function CheckNumber(day){
	var chk_num = day.search(/[0-9]/g);
	
	if(chk_num < 0){
		alert('만료일수은 숫자를 입력하여야 합니다.');
        return false;
	}
	return true;
}

function enterKey(){
	
	if(event.keyCode==13){
		go();
	}
}

function CheckKeys(){
    if ( event.keyCode < 48 || event.keyCode > 57 )
    {
         event.keyCode = 0;
    }
}
</script>
</head>
<body leftmargin="0" topmargin="0" background="/smartux_adm/images/admin/login/back6.gif" marginheight="0" marginwidth="0">
<div id="minical" oncontextmenu="return false" ondragstart="return false" onselectstart="return false" style="background:#E7F3FF; margin:5; padding:5;margin-top:2;border-top:1 solid buttonshadow;border-left: 1 solid buttonshadow;border-right: 1 solid buttonshadow;border-bottom:1 solid buttonshadow;width:160;display:none;position: absolute; z-index: 99"></div>
		
	<form name="form1" action="/smartux_adm/admin/login/expdate.do" method="POST">
		   
			<div align="center">
				<table background="/smartux_adm/images/admin/login/upback.gif" border="0" cellpadding="0" cellspacing="0" height="121" width="100%">
					<tbody><tr>
						<td>
							
						</td>
					</tr>
				</tbody></table>
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
								<tr height="5"><td colspan="5">비밀번호가 만료되어 비밀번호 변경 후 이용이 가능합니다.</td></tr>
								<tr height="29">
									<td colspan="5" background="/smartux_adm/images/admin/login/copy.gif">
										<table background="/smartux_adm/images/admin/login/copy.gif" width="100%" border="0">
											<tbody>
												<tr>
													<td width="5"></td>
													<td width="110" align="left" class="expdate_txt">아이디</td>
													<td><input name="smartux_id" maxlength="12" style="font-size: 12px;" tabindex="1" type="text" value="${smartux_id}" size="20" readonly="readonly"></td>
													<td width="250"></td>
												</tr>
												<tr>
													<td width="5"></td>
													<td align="left" class="expdate_txt">비밀번호</td>
													<td><input name="smartux_pwd" maxlength="20" style="font-size: 12px;" tabindex="2" type="password" value="" size="21"></td>
													<td width="160" align="left" class="expdate_txt2">[숫자와 영문자 조합으로 10~20자리를 사용]</td>
												</tr>
												<tr>
													<td width="5"></td>
													<td align="left" class="expdate_txt">비밀번호 확인</td>
													<td><input name="smartux_pwd_chk" maxlength="20" style="font-size: 12px;" tabindex="2" type="password" value="" size="21"></td>
													<td width="160"></td>
												</tr>
												<tr>
													<td width="5"></td>
													<td align="left" class="expdate_txt">만료일수</td>
													<td><input name="smartux_exp_day" maxlength="2" style="font-size: 12px;" tabindex="2" type="text" value="" size="20" onKeyPress="CheckKeys()"></td>
													<td width="160" align="left" class="expdate_txt2">[숫자만 입력(Ex: 90 -> 90일)]</td>
												</tr>
												<tr>
													<td width="5"></td>
													<td align="left"></td>
													<td><img src="/smartux_adm/images/admin/login/loginbutton.gif" onkeypress="javascript:enterKey();" style="" onclick="javascript:go();" tabindex="3" height="20" width="45" align="right"  ></td>
												</tr>
											</tbody>
											</table> 	
										
									</td>
								<tr height="5">
									<td colspan="5"></td>
								</tr>		   
							</tbody></table>
						</td>
					</tr>
				</tbody></table>
			</div>
		
	</form>
<c:choose>
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
		document.form1.smartux_pwd_chk.value='';
		document.form1.smartux_pwd.focus();
	</script>
</c:when>
<c:when test="${result == 'PWD LENGTH'}">
	<script type="text/javascript">
	alert('비밀번호는 숫자와 영문자 조합으로 10~20자리를 사용해야 합니다.');
	document.form1.smartux_pwd.value='';
	document.form1.smartux_pwd_chk.value='';
	document.form1.smartux_pwd.focus();
	</script>
</c:when>
<c:when test="${result == 'PWD MIX'}">
	<script type="text/javascript">
	alert('비밀번호는 숫자와 영문자를 혼용하여야 합니다.');
	document.form1.smartux_pwd.value='';
	document.form1.smartux_pwd_chk.value='';
	document.form1.smartux_pwd.focus();
	</script>
</c:when>


<c:when test="${result == 'NOT EXPDATE'}">
	<script type="text/javascript">
		alert('만료일수을 입력해주세요.');
		document.form1.smartux_exp_day.value='';
		document.form1.smartux_exp_day.focus();
	</script>
</c:when>
<c:when test="${result == 'EXPDATE NOT NUMBER TYPE'}">
	<script type="text/javascript">
		alert('만료일수은 숫자만 사용해야 합니다.');
		document.form1.smartux_exp_day.value='';
		document.form1.smartux_exp_day.focus();
	</script>
</c:when>
<c:when test="${result == 'PASS HIST'}">
	<script type="text/javascript">
		alert('history에 있는 비밀번호는 변경 불가합니다.');
		document.form1.smartux_exp_day.value='';
		document.form1.smartux_exp_day.focus();
	</script>
</c:when>
<c:when test="${result == 'PASS DUP'}">
	<script type="text/javascript">
		alert('이전 비밀번호는 변경 불가합니다.');
		document.form1.smartux_exp_day.value='';
		document.form1.smartux_exp_day.focus();
	</script>
</c:when>
</c:choose>
</body>
</html>