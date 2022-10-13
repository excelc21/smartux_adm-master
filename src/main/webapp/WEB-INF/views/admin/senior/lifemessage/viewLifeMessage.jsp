<%@page import="com.dmi.smartux.common.property.SmartUXProperties"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<%@ taglib prefix="fn"  	uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" 	uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
$(document).ready(function(){
	$("#regbtn").click(function(){
		var cont_msg = $("#cont_msg").val();
		var cont_type = $("#cont_type option:selected").val();
		var cont_type_name = $("#cont_type option:selected").text();
		var display_time = $("#display_time").val();
		var start_point = $("#start_point").val();
		var end_point = $("#end_point").val();
		var writeId =  $("#write_id").val();
		var order =  $("#order").val();
		var reg_num = /^[0-9]+$/;

		if(cont_msg == ""){
			alert("문구를 입력해 주세요.");
			cont_msg.focus();
        }else if(display_time == ""){
            alert("노출시간을 입력해 주세요.");
            display_time.focus();
		}else if(start_point == ""){
			alert("지수 시작을 입력해 주세요.");
			start_point.focus();
        }else if(end_point == ""){
            alert("지수 종료를 입력해 주세요.");
            end_point.focus();
        }else if(checkByte(cont_msg) > 100) {
			alert("문구는 100바이트까지 입력 가능합니다.");
			return;
        }else if(checkByte(display_time) > 3) {
			alert("노출시간은 3바이트까지 입력 가능합니다.");
			return;
        }else if(checkByte(start_point) > 3) {
			alert("지수 시작은 3바이트까지 입력 가능합니다.");
			return;
        }else if(checkByte(end_point) > 3) {
			alert("지수 종료는 3바이트까지 입력 가능합니다.");
			return;
        }else if(!reg_num.test(display_time)){
    		alert("노출시간은 숫자만 입력 가능합니다.");
    		return;
        }else if(!reg_num.test(start_point)){
    		alert("지수 시작은 숫자만 입력 가능합니다.");
    		return;
        }else if(!reg_num.test(end_point)){
    		alert("지수 종료는 숫자만 입력 가능합니다.");
    		return;
        }else if(start_point > end_point){
    		alert("지수 종료가 지수 시작보다 높거나 같아야 합니다.");
    		return;
        }else{
			$.post("<%=webRoot%>/admin/lifemessage/updateLifeMessage.do",
					 {	
						cont_msg : cont_msg,
						cont_type : cont_type,
						cont_type_name : cont_type_name,
						display_time : display_time,
						start_point : start_point,
						end_point : end_point,
                        writeId : writeId,
                        order : order
                     },
					  function(data) {
						 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
						 	var flag = data.flag;
						 	var message = data.message;

						 	if(flag == "0000"){						// 정상적으로 처리된 경우
						 		opener.location.reload();
								alert("생활지수 문구가 등록되었습니다");
								self.close();
						 	}else{
					 			alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
						 	}
					  },
					  "json"
		    );
		}
	});

	
	$("#closebtn").click(function(){
		 window.close();		
	});	
});

//필드에 입력된 문자의 바이트 수를 체크
function checkByte(message) {
	var totalByte = 0;
	var charCount = 0;

	for (var i=0; i < message.length; i++) {
		var currentByte = message.charCodeAt(i);
		if (currentByte > 128 || currentByte==10) {
			totalByte += 2;
		} else {
			totalByte++;
		}	
	}
	
	return totalByte;
}
</script>
</head>

<body leftmargin="0" topmargin="0">
<div id="divBody" style="height:100%">

<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%" >
    <tbody>    
      <tr>
      <td colspan="2" valign="top">
        <table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
        <tbody>
        <tr>            
            <td valign="top">
            <table border="0" cellpadding="0" cellspacing="0" width="98%">
                <tbody>               
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
						                                            <td class="bold">생활지수 문구 수정</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>						                       	
						                       	<form id="regfrm" name="regfrm" method="post" action="">
					                            <table border="0" cellpadding="0" cellspacing="0" width="450" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">문구</th>
					                                    <td width="1%"></td>
					                                    <td width="74%" align="left" >
															<input type="text" id="cont_msg" name="cont_msg" size="45" maxlength="100" style="font-size: 12px;" value="${view.cont_msg}"/>
														</td>
					                                </tr>
													<tr align="center">
														<th width="25%">타입</th>
														<td width="1%"></td>
														<td width="74%" align="left" >
															<select id="cont_type" name="cont_type">
																<option value="1" <c:if test="${view.cont_type eq '1'}">selected="selected"</c:if>>우산</option>
																<option value="2" <c:if test="${view.cont_type eq '2'}">selected="selected"</c:if>>자외선</option>
																<option value="3" <c:if test="${view.cont_type eq '3'}">selected="selected"</c:if>>식중독</option>
																<option value="4" <c:if test="${view.cont_type eq '4'}">selected="selected"</c:if>>감기</option>
																<option value="5" <c:if test="${view.cont_type eq '5'}">selected="selected"</c:if>>빨래</option>
																<option value="6" <c:if test="${view.cont_type eq '6'}">selected="selected"</c:if>>불쾌</option>
																<option value="7" <c:if test="${view.cont_type eq '7'}">selected="selected"</c:if>>외출</option>
																<option value="8" <c:if test="${view.cont_type eq '8'}">selected="selected"</c:if>>냉방</option>
																<option value="9" <c:if test="${view.cont_type eq '9'}">selected="selected"</c:if>>난방</option>
																<option value="10" <c:if test="${view.cont_type eq '10'}">selected="selected"</c:if>>피부</option>
															</select>
														</td>
													</tr>
													<tr align="center">
														<th width="25%">노출시간</th>
														<td width="1%"></td>
														<td width="74%" align="left" >
                                                            <input type="text" id="display_time" name="display_time" size="5" maxlength="5" style="font-size: 12px;" value="${view.display_time}" />
														</td>
													</tr>
					                                <tr align="center">
					                                    <th width="25%">지수 시작</th>
					                                    <td width="1%"></td>
					                                    <td width="74%" align="left" >
															<input type="text" id="start_point" name="start_point" size="5" maxlength="5" style="font-size: 12px;" value="${view.start_point}" />
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">지수 종료</th>
					                                    <td width="1%"></td>
					                                    <td width="74%" align="left" >
															<input type="text" id="end_point" name="end_point" size="5" maxlength="5" style="font-size: 12px;" value="${view.end_point}" />
														</td>
					                                </tr>
							                       	</tbody>
					                            </table> 
												<input type="hidden" id="write_id" name="write_id" value="<%=id_decrypt %>" />
											 	<input type="hidden" id="order" name="order" value="${view.order}" />
					                            <table border="0" cellpadding="0" cellspacing="0" width="450" >
					                              <tr  align="right">
				                                    <td>				                                    	
												    	<input type="button" id="regbtn"   value="수정"    class="button small blue" align="right"/>
						                               	<input type="button" id="closebtn" value="닫기"    class="button small blue"/>		
												    </td>
												  </tr>  
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
</tbody>
</table>
</div>
</body>
</html>


