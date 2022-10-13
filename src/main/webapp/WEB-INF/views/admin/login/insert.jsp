<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
function setLoginFailClear(f_id,div_id){
	$.ajax({
	    url: '/smartux_adm/admin/login/clear.do',
	    type: 'POST',
	    dataType: 'text',
	    data: {
	        "smartux_id": f_id	        
	    },
	    error: function(){
	    	alert("에러가 발생하였습니다.");
	    },
	    success: function(textDoc){
	    	if(textDoc == "SUCCESS"){
	    		alert(f_id+" 잠금 해제 되었습니다.")
	    		$("#"+div_id).html("");
	    	}else{
	    		alert("에러가 발생하였습니다.");	
	    	}	    	
	    }
	});	
}

function goInsert(){
	var f = document.getElementById("form1");
	
	if(f.user_id.value == ''){
		alert('아이디를 입력해주세요.');
		f.user_id.focus();
		return;	
	}else if(f.name.value == ''){
		alert('이름을 입력해 주세요.');
		f.name.focus();
		return;
	}else if(f.password.value == ''){
		alert('비밀번호를 입력해 주세요.');
		f.password.focus();
		return;
	}else if(!CheckPassword(f.user_id.value,f.password.value)){
		f.password.value = '';
		f.password.focus();
		return;
	}else if(f.password_chk.value == ''){
		alert('비밀번호 확인을 입력해 주세요.');
		f.password_chk.focus();
		return;
	}else if(f.password.value != f.password_chk.value){
		alert('비밀번호 확인과 일치 하지 않습니다.');
		f.password.value = '';
		f.password_chk.value = '';
		f.password.focus();
		return;
	}else if(f.exp_day.value == ''){
		alert('만료일수을 입력해 주세요.');
		return;
	}else if(f.exp_day.value > 90){
		alert('만료일은 90일 이하만 입력 가능합니다.');
		return;
	}else if(!CheckNumber(f.exp_day.value)){
		f.exp_day.value = '';
		f.exp_day.focus();
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
		alert('만료일은 숫자를 입력하여야 합니다.');
        return false;
	}
	return true;
}

function CheckKeys(){
    if ( event.keyCode < 48 || event.keyCode > 57 )
    {
         event.keyCode = 0;
    }
}

function Limit(obj) {
 var maxLength = parseInt(obj.getAttribute("maxlength"));
 if ( obj.value.length >= maxLength ) {
  alert(maxLength+"자 이상 등록 할수 없습니다.");
  obj.value = obj.value.substring(0, maxLength);
 }
}

	 
</script>
<body leftmargin="0" topmargin="0">
<div id="divBody" style="height:100%">
<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
    <tbody>
      <tr>
        <td colspan="2" height="45" valign="bottom">
       		 <!-- top menu start -->
			<%@include file="/WEB-INF/views/include/top.jsp" %>
            <!-- top menu end -->
	   </td>
	  </tr>
	  <tr>
        <td height="10"></td>
        <td></td>
      </tr>
      <tr>
      <td colspan="2" valign="top">
        <table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
        <tbody>
        <tr>
            <td width="4"></td>
            <td valign="top" width="180">
      		<!-- left menu start -->
      		<%@include file="/WEB-INF/views/include/left.jsp" %>
      		<!-- left menu end -->
            </td>
			<td background="/smartux_adm/images/admin/bg_02.gif" width="35">&nbsp;</td>
            <td valign="top">
            <table border="0" cellpadding="0" cellspacing="0" width="98%">
                <tbody>
                <tr>
                    <td height="42" width="100%">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tbody>
                            <tr>
                                <td width="300" class="boldTitle">
                                	<!-- 카테고리 타이틀 -->
                                    <!-- <img src="/smartux_adm/images/admin/category_map_title.gif">-->
                                    관리자
                                </td>
                            </tr>
                        	</tbody>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td class="3_line" height="1"></td>
                </tr>
                <tr>
                    <td class="td_bg04" height="2"></td>
                </tr>   
                <tr>
                	<td>
                	<!-- ######################## body start ######################### -->
                		<table border="0" cellpadding="15" cellspacing="0" width="100%" align="center">
                    		<tbody>
                    		<tr>
                    			<td>
                    				<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
			                            <tbody>			                            
						                <tr>
						                    <td class="3_line" height="1"></td>
						                </tr>
						                <!-- 리스트 시작 -->
						                <tr>
						                    <td>
						                        <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            <tbody>
						                            <tr>
						                                <td height="25">
						                                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
						                                        <tbody>
						                                        <tr>
						                                            <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
						                                            <td class="bold">관리자 계정 등록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                       	<%
													String domain_https = (String)request.getAttribute("domain_https");
						                       		String domain_http = (String)request.getAttribute("domain_https");
												%>
						                        <form id="form1" action="/smartux_adm/admin/login/insert.do" method="post">
						                        <input type="hidden" name="findName" value="${vo.findName }" />
						                        <input type="hidden" name="findValue" value="${vo.findValue }" />
						                        <input type="hidden" name="pageNum" value="${vo.pageNum }" />
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">아이디</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="text" name="user_id" size="35" style="font-size: 12px;" />								
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">이름</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="text" name="name" size="35" style="font-size: 12px;" />								
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">비밀번호</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="password" name="password" size="36" style="font-size: 12px;" maxlength="20"/>
															숫자와 영문자 조합으로 10~20자리를 사용						
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">비밀번호 확인</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="password" name="password_chk" size="36" style="font-size: 12px;" maxlength="20" />								
														</td>
					                                </tr>
					                                <c:choose>
		                                    			<c:when test="${auth_decrypt == '00'}">
				                                    		<tr align="center">
							                                    <th width="25%">계정 권한</th>
							                                    <td width="5%"></td>
							                                    <td width="70%" align="left" >
																	<select	name="user_auth">
																		<option value="01">일반관리자</option>
																		<option value="02">세컨드TV관리자</option>
																		<option value="03">VOD프로모션관리자</option>
																		<option value="00">슈퍼관리자</option>
																		<option value="04">시즌관리자</option>
																	</select>								
																</td>
							                                </tr>								                                    				
		                                    			</c:when>
		                                    			<c:otherwise>
															<input type="hidden" name="user_auth" value="01" />		                                    				
		                                    			</c:otherwise>
						                            </c:choose>
					                                <tr align="center">
					                                    <th width="25%">이메일</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="text" name="email" size="35" style="font-size: 12px;" />	
															ex) test@test.co.kr 							
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">만료일수</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="text" name="exp_day" size="35" style="font-size: 12px;" onKeyPress="CheckKeys()" maxlength="2"/>	
															ex) 90 -> 90일							
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">메모</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<textarea rows="10" cols="40" name="memo" maxlength="1000" onkeyup="return Limit(this);"></textarea>
														</td>
					                                </tr>
					                            	</tbody>
					                            </table>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<a href="javascript:goInsert();"><span class="button small blue">등록</span></a>
						                                	<a href="list.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}"><span class="button small blue">목록</span></a>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                       	</form>
					                            
						                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
						                            <tbody><tr>
						                                <td height="1"></td>
						                            </tr>
						                            <tr>
						                                <td class="3_line" height="3"></td>
						                            </tr>
						                            <tr>
						                                <td>&nbsp;</td>
						                            </tr>
						                        </tbody>
						                        </table>						                        
						                    </td>
						                </tr>
						                <!-- 리스트 종료 -->
						          </tbody>
						          </table>	                                    	
                    			</td>
                    		</tr>
                    		</tbody>
                    	</table>
                	 <!-- ########################### body end ########################## -->
                	</td>
                </tr>             
			 	</tbody>
			 </table>
            </td>
		</tr>
		</tbody>
		</table>
	  </td>
	  </tr>
	  <tr>
	    <td height="30">&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr>
	    <td colspan="2" background="/smartux_adm/images/admin/copy_bg.gif" height="60" align="left">
	        <!-- 하단 로그인 사용자 정보 시작 -->
	        <%@include file="/WEB-INF/views/include/bottom.jsp" %>
	        <!-- 하단 로그인 사용자 정보 종료 -->
	    </td>
	 </tr>
</tbody>
</table>
</div>
<c:choose>
<c:when test="${vo.validate == 'NOT ID'}">
	<script type="text/javascript">
		alert('아이디를 입력해 주세요.');
	</script>
</c:when>
<c:when test="${vo.validate == 'NOT PWD'}">
	<script type="text/javascript">
		alert('비밀번호를 입력해 주세요.');
	</script>
</c:when>
<c:when test="${vo.validate == 'PWD LENGTH'}">
	<script type="text/javascript">
	alert('비밀번호는 숫자와 영문자 조합으로 10~20자리를 사용해야 합니다.');
	</script>
</c:when>
<c:when test="${vo.validate == 'PWD MIX'}">
	<script type="text/javascript">
	alert('비밀번호는 숫자와 영문자를 혼용하여야 합니다.');
	</script>
</c:when>


<c:when test="${vo.validate == 'NOT EXPDATE'}">
	<script type="text/javascript">
		alert('만료일을 입력해주세요.');
	</script>
</c:when>
<c:when test="${vo.validate == 'EXPDATE NOT NUMBER TYPE'}">
	<script type="text/javascript">
		alert('만료일은 숫자만 사용해야 합니다.');
	</script>
</c:when>
<c:when test="${vo.validate == 'INSERT SUCCESS'}">
	<script type="text/javascript">
		alert('정상적으로 생성 되었습니다.');
		location.href="/smartux_adm/admin/login/list.do";
	</script>
</c:when>
<c:when test="${vo.validate == 'IS DATA'}">
	<script type="text/javascript">
		alert('아이디가 존재합니다.');
		location.href="insert.do";
	</script>
</c:when>
</c:choose>
</body>
</html>