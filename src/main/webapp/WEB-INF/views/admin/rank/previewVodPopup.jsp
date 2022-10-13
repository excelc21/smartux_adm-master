<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
// rank_code Parameter Accept
String rank_code = request.getParameter("rank_code");
if(rank_code == null || rank_code == "") rank_code = "R0001";
%>
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
		window.open("<%=webRoot%>/admin/rule/insertRule.do", "regrule", "width=650,height=500,resizable=yes,scrollbars=yes");
	});
	
	$("#allchk").click(function(){
		var chkallchecked = false;
		
		if($("#allchk").is(":checked")){
			chkallchecked = true;
		}
		
		$("input[name='selchk']").attr("checked", chkallchecked);
	});
	
	$("#closebtn").click(function(){
		 window.close();		
	});
	
});

function view_rule(rule_code){
	var url = "<%=webRoot%>/admin/rule/viewRule.do?rule_code=" + rule_code;
	window.open(url, "viewrule", "width=650,height=500,resizable=yes,scrollbars=yes");
}
</script>
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
						                                            <td class="bold">VOD 앨범 목록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                     <form id="regfrm" name="regfrm" method="post" action="">
					                            <table border="0" cellspacing="0" cellpadding="0" class="board_list" style="width:400px;">
													<tr>
														<th width="15%">앨범 랭킹</th>
														<th width="45%">VOD 앨범명</th>
														<th width="30%">카테고리ID</th>
													</tr>
													<c:choose>
														<c:when test="${result==null || fn:length(result) == 0}">
															<tr>
																<td colspan="3" class="table_td_04">검색된 VOD 랭킹 룰이 없습니다</td>
															</tr>
														</c:when>
														<c:otherwise>
															<c:forEach var="item" items="${result}" varStatus="status">
																<tr>
																	<td class="table_td_04" >
																		${item.rank_no}
																	</td>
																	<td class="table_td_04" style="text-align: left;">
																		${item.album_title}
																	</td>
																	<td class="table_td_04">
																		${item.cat_id}
																	</td>																	
																</tr>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</table>
												<input type="hidden" id="rulesHap" name="rulesHap" value="" />
												<input type="hidden" id="rank_code" name="rank_code" value="<%=rank_code%>>" />
												
											</form>	
												
												<table border="0" cellpadding="0" cellspacing="0" style="width:400px;" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="40" align="right">
						                                	<span class="button small blue" id="closebtn">확인</span>
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


