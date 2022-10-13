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
$(document).ready(function(){
	// 검색 버튼
	$("#upbtn").bind("click", function(){
		var idx = $("#codeitemlist option").index($("#codeitemlist option:selected"));
		var size = $("#codeitemlist option").size();
		if(idx == -1){			// 선택한 항목이 없으면
			alert("순서를 바꾸고자 하는 항목을 선택해주세요");
		}else{					// 선택한 항목이 있으면
			$("#codeitemlist option:selected").each(function(){
				var newPos = $("#codeitemlist option").index(this) - 1;
				if(newPos > -1){
					$('#codeitemlist option').eq(newPos).before("<option value='"+$(this).val()+"' selected='selected'>"+$(this).text()+"</option>");
					$(this).remove();
				}
			});
		}		
	});
	
	$("#downbtn").click(function(){
		var idx = $("#codeitemlist option").index($("#codeitemlist option:selected"));
		var size = $("#codeitemlist option").size();
		if(idx == -1){			// 선택한 항목이 없으면
			alert("순서를 바꾸고자 하는 항목을 선택해주세요");
		}else{					// 선택한 항목이 있으면
			$("#codeitemlist option:selected").each(function(){
				var newPos = $("#codeitemlist option").index(this) + 1;
				if(newPos < size){
					$('#codeitemlist option').eq(newPos).after("<option value='"+$(this).val()+"' selected='selected'>"+$(this).text()+"</option>");
					$(this).remove();
				}
			});
		}
	});
		
	$("#changeorderbtn").click(function(){
		var code = $("#code").val();
		
		if(confirm("다음의 순서로 정하시겠습니까?")){
			var size = $("#codeitemlist option").size();
			var optionarray = new Array();
			
			for(var i=0; i < size; i++){
				// alert($('#codeitemlist option').eq(i).val());
				var selval = $('#codeitemlist option').eq(i).val();
				optionarray.push(selval);
			}
			
			$.post("<%=webRoot%>/admin/smartstart/changeCodeItemOrder.do", 
					 {code : code, item_code : optionarray},
					  function(data) {
						 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
						 	var flag = data.flag;
						 	var message = data.message;
						 	
						 	if(flag == "0000"){						// 정상적으로 처리된 경우
						 		alert("순서를 재지정했습니다");
						 		opener.location.reload();
						 		self.close();
						 	}else{
						 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
						 	}
					  },
					  "json"
		    );
		}
	});
	
	$("#resetbtn").click(function(){
		// $("#orderfrm")[0].reset();
		location.reload();
	});
	
	$("#closebtn").click(function(){
		self.close();
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
						                                            <td class="bold">스마트스타트 항목 순서 바꾸기</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                       	
						                       	<form id="orderfrm" name="orderfrm" method="get" action="">
						                       	
						                       	<table border="0" cellpadding="0" cellspacing="0" width="300" >
						                       		<tbody>
													<tr align="center">
													<!--
														<td>
															<select id="codeitemlist" name="codeitemlist" size="${fn:length(result)}">
																<c:forEach var="item" items="${result}" varStatus="status">
																	<option value="${item.item_code}">${item.item_nm}</option>
																</c:forEach>
															</select>
														</td>
														<td>
															<input type="button" id="upbtn" value="위로" /><br/>
															<input type="button" id="downbtn" value="아래로" />
														</td>
														 -->	
		 
														<th width="40%">
															<select id="codeitemlist" name="codeitemlist" size="${fn:length(result)}">
																<c:forEach var="item" items="${result}" varStatus="status">
																	<option value="${item.item_code}">${item.item_nm}</option>
																</c:forEach>
															</select>
														</th>
														<td width="20%"></td>
														<td width="40%" align="left" >
															<input type="button" id="upbtn" value="위로" /><br/>
															<input type="button" id="downbtn" value="아래로" />
														</td>
													
													</tr>
													</tbody>
												</table>
					                            	<input type="hidden" id="code" name="code" value="${code}" /> 
					                            </form>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="300" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<input type="button" id="changeorderbtn" value="확인"  class="button small blue"/>
						                                	<input type="button" id="resetbtn" value="초기화"  class="button small blue"/>
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