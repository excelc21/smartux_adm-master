<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<style>
    .selectTable {
      border: 1px solid #000000;
    }
 
    .selected {
      background-color: #d0e8fd;
    }
</style>
<script type="text/javascript">
function goInsert(){
	var f = document.getElementById("form1");
	
	if(f.password.value == ''){
		alert('비밀번호를 입력해 주세요.');
		f.password.focus();
		return;
	}

	var dataobj = new Object(); 
	dataobj.password =f.password.value.replace(" ","");
	dataobj.user_id =f.user_id.value;
	dataobj.findName =f.findName.value;
	dataobj.findValue =f.findValue.value;
	dataobj.pageNum =f.pageNum.value;
	dataobj.type =f.type.value;
	var callbak_m = eval("opener."+"${param.callbak}");    

	callbak_m(dataobj);
	self.close();
}

function enterKey(){

	if(event.keyCode==13){
		goInsert();
	}
}

</script>
<body leftmargin="0" topmargin="0">

<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
  <tbody>
  	 <tr>
  	 	<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
		    <tbody>
		    <tr>
		        <td height="25">
		            <table border="0" cellpadding="0" cellspacing="0" width="100%">
		                <tbody>
		                <tr>
		                    <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
		                    <td class="bold">비밀번호 입력</td>
		                </tr>
		            	</tbody>
		            </table>
		        </td>
		    </tr>
			</tbody>
		</table>
  	 </tr>
     <tr>
        <td>
            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
                        <tbody>
                        <tr>
                            <td height="25">
                        		 <form id="form1" action="/smartux_adm/admin/login/chkpass.do" method="post">  
                        		<input type="hidden" name="user_id" value="${user_id }" />
                        		<input type="hidden" name="findName" value="${findName }" />
						        <input type="hidden" name="findValue" value="${findValue }" />
						        <input type="hidden" name="pageNum" value="${pageNum }" />
						        <input type="hidden" name="type" value="${type }" />
                                <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                    <tbody>
                                    <tr>
                                        <td class="bold">비밀번호</td>
                                        <td><input type="password" name="password" size="36" onkeypress="javascript:enterKey();" style="font-size: 12px;" maxlength="20" value="" placeholder="비밀번호를 입력하세요."/> </td>
                                        <td><a href="javascript:goInsert();"><span class="button small blue">확인</span></a></td>
                                    </tr>
                                	</tbody>
                                </table>
                                 </form> 
                            </td>
                        </tr>
                   		</tbody>
              </table>
        </td>
     </tr>
   </tbody>
</table>
</body>
</html>