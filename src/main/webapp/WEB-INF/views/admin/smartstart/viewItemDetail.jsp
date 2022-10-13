<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>

<script type="text/javascript">
//Smart Start 장르 컨트롤 
function TargetChange(val) {
	var frm = document.regfrm;
	
	// 1 : VOD	, 2 : VOD 가 아닐 경우
	if ( val == "VOD" ) {
		document.all.selitemgenre.style.display='block';
	} else {
		document.all.selitemgenre.style.display='none';
	}
}

$(document).ready(function(){
	$("#updbtn").click(function(){
		var code = $("#code").val();
		var item_code = $("#item_code").val();
		var newItem_code = $("#newItem_code").val();
		var item_nm = $("#item_nm").val();
		var newItem_nm = $("#newItem_nm").val();
		var selitemgenre = $("#selitemgenre").val();
		var selitemtype = $("#selitemtype").val();			
		var ordered = $("#ordered").val();
		var newOrdered = $("#newOrdered").val();
				
		var use_yn = $(':input:radio[name="use_yn"]:checked').val();
		
		if(newItem_code == ""){
			alert("수정하고자 하는 코드를 입력해주세요");
			$("#newitem_code").focus();
		}else if(newItem_nm == ""){
			alert("수정하고자 하는 코드명을 입력해주세요");
			$("#newItem_nm").focus();
		}else{
			$.post("<%=webRoot%>/admin/smartstart/updateCodeItem.do", 
					 {code : code, item_code : item_code, newItem_code : newItem_code,  item_nm : item_nm, newItem_nm : newItem_nm, selitemgenre : selitemgenre, selitemtype : selitemtype, use_yn : use_yn, ordered : ordered, newOrdered : newOrdered},
					  function(data) {
						 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
						 	var flag = data.flag;
						 	var message = data.message;
						 	
						 	if(item_nm == newItem_nm){
						 		alert("현재 수정하고자 하는 항목명 " + newItem_nm + "은 이미 등록되어 있는 항목명입니다\n다른 항목명을 사용해주세요");
					 			$("#item_nm").focus();	
						 	}else if(flag == "0000"){						// 정상적으로 처리된 경우
						 		opener.location.reload();
								alert("스마트스타트 항목이 수정되었습니다");
								self.close();
						 	}else{
						 		if(flag == "9000"){
						 			if(message == "item_code"){
						 				alert("현재 수정하고자 하는 코드 " + newItem_code + "은 이미 등록되어 있는 코드입니다\n다른 코드를 사용해주세요");	
						 				$("#newitem_code").focus();
						 			}else{
						 				alert("현재 수정하고자 하는 항목명 " + newItem_nm + "은 이미 등록되어 있는 항목명입니다\n다른 항목명을 사용해주세요");
						 				$("#item_nm").focus();
						 			}
						 		}else{
						 			alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
						 		}
						 	}
					  },
					  "json"
		    );
		}
		
	});
	
	$("#closebtn").click(function(){
		//$("#updfrm")[0].reset();
		 window.close();		
	});
});
</script>
</head>

<body leftmargin="0" topmargin="0">
<div id="divBody" style="height:100%">
<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
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
						                                            <td class="bold">스마트스타트 항목 상세조회</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                       	<form id="regfrm" name="regfrm" method="post" action="">
					                            <table border="0" cellpadding="0" cellspacing="0" width="400" class="board_data">
					                                <tbody>
						                                <tr align="center">
						                                    <th width="25%">코드</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																${result.item_code}
															</td>
						                                </tr>
						                                <tr align="center">
						                                    <th width="25%">항목명</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<input type="text" id="newItem_nm" name="newItem_nm" value="${result.item_nm}" size="35" style="font-size: 12px;"  />							
															</td>
						                                </tr>
						                                <tr align="center">
						                                    <th width="25%">Smart Start 타입</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																${result.ss_gbn}																	
															</td>
						                                </tr>
						                                <tr align="center">
						                                    <th width="25%">Smart Start 장르</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																${result.ss_genre}																		
															</td>
						                                </tr>
						                                <tr align="center">
						                                    <th width="25%">사용여부</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
						                                    	<c:choose>
																	<c:when test="${result.use_yn == 'Y'}">
																		<input type="radio" id="use_ynY" name="use_yn" value="Y" size="35" style="font-size: 12px;" checked/>예
																		<input type="radio" id="use-ynN" name="use_yn" value="N" size="35" style="font-size: 12px;" />아니오
																	</c:when>
																	<c:otherwise>
																		<input type="radio" id="use_ynY" name="use_yn" value="Y" size="35" style="font-size: 12px;" />예
																		<input type="radio" id="use-ynN" name="use_yn" value="N" size="35" style="font-size: 12px;" checked/>아니오
																	</c:otherwise>
																</c:choose>							
															</td>
						                                </tr>
						                                 <tr align="center">
						                                    <th width="25%">항목표시순서</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<input type="text" id="newOrdered" name="newOrdered" value="${result.ordered}" size="35" style="font-size: 12px;" readonly />							
															</td>
						                                </tr>
					                            	</tbody>
					                            </table>
					                            	<input type="hidden" id="code" value="${result.code}" />
													<input type="hidden" id="item_code" value="${result.item_code}" />
													<input type="hidden" id="item_nm" value="${result.item_nm}" />
													<input type="hidden" id="newItem_code" value="${result.item_code}" />
													<input type="hidden" id="selitemtype"  value="${result.ss_gbn}" />
													<input type="hidden" id="selitemgenre"  value="${result.ss_genre}" />
													<input type="hidden" id="ordered"  value="${result.ordered}" />
													<input type="hidden" id="newOrdered"  value="${result.ordered}" />
					                            </form>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="400" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<input type="button" id="updbtn" value="확인"  class="button small blue"/>
						                                	<input type="button" id="closebtn" value="닫기"  class="button small blue"/>	
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
					                            
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