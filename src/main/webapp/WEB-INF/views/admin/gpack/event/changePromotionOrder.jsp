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
	$("#upbtn").bind("click", function(){
		var idx = $("#packlist option").index($("#packlist option:selected"));
		var size = $("#packlist option").size();
		if(idx == -1){			// 선택한 항목이 없으면
			alert("순서를 바꾸고자 하는 항목을 선택해주세요");
		}else{					// 선택한 항목이 있으면
			fnSortCateogry("packlist", "U");
		}
		
	});
	
	$("#downbtn").click(function(){
		var idx = $("#packlist option").index($("#packlist option:selected"));
		var size = $("#packlist option").size();
		if(idx == -1){			// 선택한 항목이 없으면
			alert("순서를 바꾸고자 하는 항목을 선택해주세요");
		}else{					// 선택한 항목이 있으면
			fnSortCateogry("packlist", "D");
		}
	});
	
	
	
	$("#changeorderbtn").click(function(){
		var pack_id = $("#pack_id").val();
		
		if(confirm("다음의 순서로 정하시겠습니까?")){
			var size = $("#packlist option").size();
			var optionarray = new Array();
			var pack_id = "${pack_id}";
			
			for(var i=0; i < size; i++){
				// alert($('#codeitemlist option').eq(i).val());
				var selval = $('#packlist option').eq(i).val();
				optionarray.push(selval);
			}
			
			var smartUXManager = $("#smartUXManager").val();
			var postUrl = "<%=webRoot%>/admin/gpack/event/changeGpackPromotionOrder.do";
			$.post(  postUrl, 
					 {pack_id : pack_id, category_ids : optionarray, smartUXManager : smartUXManager},
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
						                                            <td class="bold">프로모션 순서 바꾸기</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="orderfrm" name="orderfrm" method="get" action="">
												<input type="hidden" id="pack_id" name="pack_id" value="${param.pack_id}" />
												<input type="hidden" id="smartUXManager" name="smartUXManager" value="<%=id_decrypt %>" />
					                            <table border="0" cellpadding="0" cellspacing="0" width="300" align="center">
					                                <tbody>
					                                <tr align="center">
					                                    <td width="40%">
					                                    	<select id="packlist" name="packlist" size="${fn:length(result)}" style="width:200px; height:200px;">
																<c:forEach var="item" items="${result}" varStatus="status">
																	<option value="${item.category_id}">${item.category_nm}</option>
																</c:forEach>
															</select>
														</td>
					                                    <td width="10%"></td>
					                                    <td width="*" align="left" >
					                                    	<span class="button small blue" id="upbtn">위로</span><br/><br/>
															<span class="button small blue" id="downbtn">아래로</span>
														</td>
					                                </tr>
					                            	</tbody>
					                            </table>
					                            <br/>
					                            <table border="0" cellpadding="0" cellspacing="0" width="300" align="center">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="center">
						                                	<span class="button small blue" id="changeorderbtn">확인</span>	
						                                	<span class="button small blue" id="resetbtn">재작성</span>	
						                                	<span class="button small blue" id="closebtn">닫기</span>
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
</tbody>
</table>
</div>

</body>
</html>