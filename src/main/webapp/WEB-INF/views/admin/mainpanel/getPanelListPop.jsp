<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>패널리스트</title>
    <link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
    <jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
    
<script type="text/javascript">
$(document).ready(function(){

	
	$("#delbtn").click(function(){
		var checkeditems = $("input[name='delchk']:checked");
		var checkeditemslength = checkeditems.length;
		
		if(checkeditemslength == 0){
			alert("삭제할 패널을 선택해주세요");
		}else{
			if(confirm("선택된 패널들을 삭제하시겠습니까")){
				
				var size = checkeditemslength;
				var checkboxarray = new Array();
				
				for(var i=0; i < size; i++){
					// alert($('#codeitemlist option').eq(i).val());
					var checkboxval = $(checkeditems[i]).val();
					checkboxarray.push(checkboxval);
				}
				var smartUXManager = $("#smartUXManager").val();
				
				$.post("<%=webRoot%>/admin/mainpanel/deletePanel.do", 
						 {panel_id : checkboxarray, smartUXManager : smartUXManager},
						  function(data) {
							 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
							 	var flag = data.flag;
							 	var message = data.message;
							 	
							 	if(flag == "0000"){						// 정상적으로 처리된 경우
							 		alert("패널이 삭제되었습니다");
							 		location.reload();
							 	}else{
							 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
							 	}
						  },
						  "json"
			    );
			}
		}
		
	});
	

});

function view_panel(panel_id){
	
	opener.document.getElementById("page_code").value = panel_id;
	self.close();

}




</script>
<body leftmargin="0" topmargin="0">
<div id="divBody" style="height:100%">
<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
    <tbody>

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
		
            <td valign="top">
            <table border="0" cellpadding="0" cellspacing="0" width="98%">
                <tbody>
            
                
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
			                            <td>
			                                <!-- 검색 시작 -->			                              
					                        <!-- 검색 종료 -->
					                    </td>
						                </tr>
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
						                                            <td class="bold">패널 목록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        
					                            <table border="0" cellspacing="0" cellpadding="0" class="board_list" style="width:98%">
													<tr>
														<th width="14%">패널 코드</th>														
														<th>패널명</th>
														<th width="14%">패널UI타입</th>
														<th width="14%">사용여부</th>
														<th width="14%">선택</th>
													</tr>
													<c:choose>
														<c:when test="${result==null || fn:length(result) == 0}">
															<tr>
																<td colspan="5" class="table_td_04">검색된 패널이 없습니다</td>
															</tr>
														</c:when>
														<c:otherwise>
															<c:forEach var="item" items="${result}" varStatus="status">
																<tr>
								
																	<td class="table_td_04">${item.pannel_id}</td>																	
																	<td class="table_td_04" style="text-align: left;">
																		${item.pannel_nm}
																	</td>
																	<td class="table_td_04">${item.panel_ui_type}</td>
																	<td>
																		<c:choose>
																			<c:when test="${item.use_yn == 'Y'}">
																				예
																			</c:when>
																			<c:otherwise>
																				아니오
																			</c:otherwise>
																		</c:choose>
																	</td>
																	<td>
																	<a href="#" onclick="view_panel('${item.pannel_id}')">
																	<span class="button small blue" id="regbtn">선택</span></a>
																	</td>
																	
																</tr>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</table>
												<input type="hidden" id="smartUXManager" name="smartUXManager" value="<%=id_decrypt %>" />
												<table border="0" cellpadding="0" cellspacing="0" style="width:98%" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="40" align="center">

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
	  <tr>
	    <td height="30">&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
</tbody>
</table>
</div>
</body>
</html>