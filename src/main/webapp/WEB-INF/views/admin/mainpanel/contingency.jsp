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

function getView(f_v){
	if(f_v == "Y"){
		$("#contingency_view").show();
	}else{
		$("#contingency_view").hide();
	}
}

function goUpdate(){
	var f = document.getElementById("form1");
	if(f.contingency_type.value == "Y"){
		if(f.contingency_time.value == ''){
			alert("Contingency Mode 시 시간설정이 필요합니다.");
			f.contingency_time.focus();
			return;
		}else{
			if(!CheckNumber(f.contingency_time.value)){
				f.contingency_time.value = '';
				f.contingency_time.focus();
				return;
			}
			
		}
	}
	f.submit();
}

function CheckNumber(day){
	var chk_num = day.search(/[0-9]/g);
	
	if(chk_num < 0){
		alert('숫자를 입력하여야 합니다.');
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

$(document).ready(function(){
	getView('${contingency_type}');
});

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
                                    Contingency Mode
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
						                                            <td class="bold">Contingency Mode 설정/해제</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="form1" action="setContingency.do" method="post">
						                        <input type="hidden" name="user_id" value="<%=id_decrypt%>"/>
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">Contingency Mode 설정</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<select name="contingency_type" onchange="getView(this.value);">
					                                    		<option value="N"  <c:if test="${contingency_type == 'N'}">selected="selected"</c:if>>Normal Mode</option>
					                                    		<option value="Y"  <c:if test="${contingency_type == 'Y'}">selected="selected"</c:if>>Contingency Mode</option>
					                                    	</select>
														</td>
					                                </tr>
					                                <tr align="center" id="contingency_view">
					                                    <th width="25%">Contingency 시간 설정</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="text" id="contingency_time" name="contingency_time" size="20" style="font-size: 12px;" onKeyPress="CheckKeys()" value="${contingency_time}" maxlength="19" /> 초						
														</td>
					                                </tr>
					                            	</tbody>
					                            </table>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<a href="javascript:goUpdate();"><span class="button small blue">설정</span></a>						                                	
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
<c:when test="${validate == 'SUCCESS'}">
	<script type="text/javascript">
		alert('정상적으로 설정 되었습니다.');
	</script>
</c:when>
<c:when test="${validate == 'ERROR'}">
	<script type="text/javascript">
		alert('에러가 발생되었습니다.');
	</script>
</c:when>
</c:choose>
</body>
</html>