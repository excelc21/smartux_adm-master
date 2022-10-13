<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">

$(document).ready(function(){
	$("#updbtn").click(function(){
		var rule_code = $("#rule_code").val();
		var rule_name = $("#rule_name").val();
		
		if($("#rule_name").val() == ""){
			alert("수정하고자 하는 Rule 이름을 입력해주세요");
			$("#rule_name").focus();
		}else{
			var smartUXManager = $("#smartUXManager").val();
			$.post("<%=webRoot%>/admin/rule/updateRule.do", 
					 {rule_code : rule_code, rule_name : rule_name, smartUXManager : smartUXManager},
					  function(data) {
						 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
						 	var flag = data.flag;
						 	var message = data.message;
						 	
						 	if(flag == "0000"){						// 정상적으로 처리된 경우
						 		opener.location.reload();
								alert("VOD 랭킹 Rule이 수정되었습니다");
								self.close();
						 	}else{
						 	 	if(flag == "NOT FOUND RULE_CODE"){
						 			alert("Rule Code값은 필수로 들어가야 합니다");
						 		}else if(flag == "NOT FOUND RULE_NAME"){
						 			alert("수정하고자 하는 Rule 이름을 입력해주세요");
									$("#rule_name").focus();
						 		}else if(flag == "RULE_NAME LENGTH"){
						 			alert("Rule 이름은 100자 이내이어야 합니다");
									$("#rule_name").focus();
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
		$("#updfrm")[0].reset();
	});
	
});


</script>
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
						                                            <td class="bold">Rule 상세조회</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="updfrm" name="updfrm" method="post" action="">
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">Rule 이름</th>
					                                    <td width="1%"></td>
					                                    <td width="74%" align="left" >
															<input type="text" id="rule_name" name="rule_name" size="35" maxlength="100" style="font-size: 12px;" value="${main.rule_name}"/>								
														</td>
					                                </tr>
					                                
					                                <tr align="center">
					                                    <th width="25%">Rule Type</th>
					                                    <td width="1%"></td>
					                                    <td width="74%" align="left" >${rule_type.rule_type_name}</td>
					                                </tr>
					                            	</tbody>
					                            </table>
					                            
					                            <c:if test="${(detail != null && fn:length(detail) != 0) }">
				                                   <c:if test="${rule_type.rule_type == 'D'}">
				                                   		<table id="dtable" border="0" cellpadding="0" cellspacing="0" width="100%" class="board_data">
															<tr>
																<th width="20%">일차</th>
																<th align="center">가중치</th>
															</tr>
															<c:forEach var="item" items="${detail}" varStatus="status">
															<tr>
																<td style="text-align : center;">Today - ${item.rule_type_data1}일차</td>
																<td>${item.weight}</td>
															</tr>
															</c:forEach>
														</table>
												   </c:if>
												   <c:if test="${rule_type.rule_type == 'P'}">
												   		<table id="ptable" border="0" cellpadding="0" cellspacing="0" width="100%" class="board_data">
															<tr>
																<th width="25%" align="center">시작가격</th>
																<th width="25%" align="center">끝가격</th>
																<th align="center">가중치</th>
															</tr>
															<c:forEach var="item" items="${detail}" varStatus="status">
															<tr>
																<td style="text-align : center;">${item.rule_type_data1}</td>
																<td style="text-align : center;">${item.rule_type_data2}</td>
																<td>${item.weight}</td>
															</tr>
															</c:forEach>
														</table>
												   </c:if>
												   <c:if test="${rule_type.rule_type == 'C'}">
												   		<table id="ctable" border="0" cellpadding="0" cellspacing="0" width="100%" class="board_data">
												   		<c:forEach var="item" items="${detail}" varStatus="status">
												   			<c:if test="${item.rule_type_data1 == 'C'}">
												   				<tr>
																	<th width="10%" align="center">유료</th>
																	<td>${item.weight}</td>
																</tr>
												   			</c:if>
												   			<c:if test="${item.rule_type_data1 == 'F'}">
												   				<tr>
																	<th width="10%" align="center">무료</th>
																	<td>${item.weight}</td>
																</tr>
												   			</c:if>
												   		</c:forEach>
												   		</table>
												   </c:if>
												   <c:if test="${rule_type.rule_type == 'G'}">
														<table id="gtable" border="0" cellpadding="0" cellspacing="0" width="100%" class="board_data">
														<tr>
															<th width="30%" align="center">장르</th>
															<th align="center">가중치</th>
														</tr>
														<c:forEach var="item" items="${detail}" varStatus="status">
															<tr>
																<td>${item.rule_type_data1}</td>
																<td>${item.weight}</td>
															</tr>
														</c:forEach>
														</table>			
												   </c:if>
			                                   </c:if>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="center">
						                                	<span class="button small blue" id="updbtn">수정</span>	
						                                	<span class="button small blue" id="resetbtn">재작성</span>	
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                       	<input type="hidden" id="rule_code" name="rule_code" value="${rule_code}" />
						                       	<input type="hidden" id="smartUXManager" name="smartUXManager" value="<%=id_decrypt %>" />
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