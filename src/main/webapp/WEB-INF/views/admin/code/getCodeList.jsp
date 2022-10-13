<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
		window.open("<%=webRoot%>/admin/code/insertCode.do", "regcode", "width=450,height=200");
	});
	
	$("#delbtn").click(function(){
		var checkeditems = $("input[name='delchk']:checked");
		var checkeditemslength = checkeditems.length;
		
		if(checkeditemslength == 0){
			alert("삭제할 항목을 선택해주세요");
		}else{
			if(confirm("선택된 코드들을 삭제하시겠습니까")){
				var smartUXManager = $("#smartUXManager").val();
				var size = checkeditemslength;
				var checkboxarray = new Array();
				
				for(var i=0; i < size; i++){
					// alert($('#codeitemlist option').eq(i).val());
					var checkboxval = $(checkeditems[i]).val();
					checkboxarray.push(checkboxval);
				}
				
				$.post("<%=webRoot%>/admin/code/deleteCode.do", 
						 {code : checkboxarray},
						  function(data) {
							 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
							 	var flag = data.flag;
							 	var message = data.message;
							 	
							 	if(flag == "0000"){						// 정상적으로 처리된 경우
							 		alert("코드가 삭제되었습니다");
							 		location.reload();
							 	}else if(flag == "FOUND ITEM CODE"){
							 		alert("다음의 코드들에 대해 하위 코드들을 먼저 삭제해주세요\n\n[" + message + "]");
							 	}else if(flag == "NOT FOUND CODE"){
							 		alert("삭제 할 코드들이 먼저 선택되어야 합니다");
							 	}else{
							 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
							 	}
						  },
						  "json"
			    );
			}
		}
		
	});
	
	$("#allchk").click(function(){
		var chkallchecked = false;
		
		if($("#allchk").is(":checked")){
			chkallchecked = true;
		}
		
		$("input[name='delchk']").attr("checked", chkallchecked);
	});
});

function view_code(code){
	var url = "<%=webRoot%>/admin/code/viewCode.do?code=" + code;
	window.open(url, "viewcode", "width=450,height=200");
}
</script>
<body leftmargin="0" topmargin="0">
<div id="divBody" style="height:100%">
<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
    <tbody>
      <tr>
        <td colspan="2" height="45" valign="bottom">
       		 <!-- top menu start -->
			<!-- jsp:include page="/WEB-INF/views/include/top.jsp"--><!-- /jsp:include -->
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
                <tr style="display:block">
                    <td height="42" width="100%">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tbody>
                            <tr>
                                <td width="300" class="boldTitle">
                                	<!-- 카테고리 타이틀 -->
                                    <!-- <img src="/smartux_adm/images/admin/category_map_title.gif">-->
                                    코드
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
			                            <td>
			                                <!-- 검색 시작 -->			                              
					                        <!-- 검색 종료 -->
					                    </td>
						                </tr>
						                <tr>
						                    <td class="3_line" height="1"></td>
						                </tr>
						                <tr>
						                    <td height="20"></td>
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
						                                            <td class="bold">코드 목록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        
					                            <table border="0" cellspacing="0" cellpadding="0" class="board_list" style="width:95%">
													<tr>
														<th width="3%" style="padding-top:8px"><input type="checkbox" id="allchk" name="allchk" value=""></th>
														<th width="20%">코드</th>
														<th width="77%">코드명</th>
													</tr>
													<c:choose>
														<c:when test="${result==null || fn:length(result) == 0}">
															<tr>
																<td colspan="3" class="table_td_04">검색된 코드가 없습니다</td>
															</tr>
														</c:when>
														<c:otherwise>
															<c:forEach var="item" items="${result}" varStatus="status">
																<tr>
																	<td class="table_td_04">
																		<c:if test="${item.existProperty == 'Y'}">
																			&nbsp;
																		</c:if>
																		<c:if test="${item.existProperty == 'N'}">
																			<input type="checkbox" name="delchk" value="${item.code}">
																		</c:if>
																	</td>
																	<td class="table_td_04">${item.code}</td>
																	<td class="table_td_04" style="text-align: left;">
																		<a href="#" onclick="view_code('${item.code}')">${item.code_nm}</a>
																	</td>
																</tr>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</table>
					                            <form id="frm" name="frm" method="post">
													<input type="hidden" id="delthemecodes" name="delthemecodes" value="" />
													<input type="hidden" id="smartUXManager" name="smartUXManager" value="<%=id_decrypt %>" />
												</form>
												
												<table border="0" cellpadding="0" cellspacing="0" style="width:95%" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="40" align="center">
						                                	<span class="button small blue" id="regbtn">등록</span>
						                                	<c:if test="${result!=null && fn:length(result) != 0}">
																<span class="button small blue" id="delbtn">삭제</span>
															</c:if>
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
</body>
</html>