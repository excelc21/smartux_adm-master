<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>긴급(비상)공지</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
	
$(document).ready(function(){
	
	$("#sumitBtn").click(function () {
	 	
	    if($("#sa_id").val()==""){
			alert('가입자번호를 입력해 주세요.');
	 		$("#sa_id").focus();
			return false;
	  	}
	    if($("#size").val()==""){
			alert('로그사이즈를 입력해 주세요.');
	 		$("#size").focus();
			return false;
	  	}
	    if($("#log_type").val()==""){
			alert('로그타입을 입력해 주세요.');
	 		$("#log_type").focus();
			return false;
	  	}
	    
	    if(confirm("수정 하시겠습니까?")){
			$("form[name=form1]").submit();
	    }
	 	
	});

	$(".numeric").keypress(function(e){
		if(e.which && (e.which > 47 && e.which < 58 || e.which==8)){
		}else{
			e.preventDefault();
		}
	});
	$(".numeric").css("ime-mode","disabled");
	$(".nohangul").css("ime-mode","disabled");
});
</script>
</head>

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
                                    	공지 관리	
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
						                                            <td class="bold">품질수집 단말 수정</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="form1" name="form1" action="./qualityMemberUpdate.do" method="post">
						                        <input type="hidden" name="file_id" value="${vo.file_id}" />
						                        <input type="hidden" name="findName" value="${vo.findName}" />
						                        <input type="hidden" name="findValue" value="${vo.findValue}" />
						                        <input type="hidden" name="pageNum" value="${vo.pageNum}" />
						                        <input type="hidden" name="s_log_type" value="${vo.s_log_type}" />
						                        <input type="hidden" name="serviceType" value="${vo.serviceType}" />
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" class="board_data">
					                                <tbody>				                             	
						                                <tr align="center">
						                                    <th width="25%">가입자번호</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
						                                    	${viewVo.sa_id}
																<input type="hidden" name="sa_id" id="sa_id" class="nohangul" size="35" style="font-size: 12px;" value="${viewVo.sa_id}"/>	
															</td>
														 </tr>				                             	
						                                <tr align="center">
						                                    <th width="25%">지정타입</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<select name="find_type" id="find_type">
																	<option value="DE" <c:if test="${viewVo.find_type == 'DE'}">selected="selected"</c:if>>default</option>
																	<option value="PR" <c:if test="${viewVo.find_type == 'PR'}">selected="selected"</c:if>>prefix</option>
																	<option value="PO" <c:if test="${viewVo.find_type == 'PO'}">selected="selected"</c:if>>postfix</option>
																</select>	
															</td>
														 </tr>
						                                <tr align="center">
						                                    <th width="25%">로그사이즈</th> 
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<input type="text" name="size" id="size" style="font-size: 12px;width:40px;text-align:right;" value="${viewVo.size}" class="numeric" maxlength="5"/> M
															</td>
						                                </tr>
						                                <tr align="center">
						                                    <th width="25%">로그타입</th> 
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<select name="log_type" id="log_type">
																	<option value="1" <c:if test="${viewVo.log_type == '1'}">selected="selected"</c:if>>APP로그</option>
																	<option value="2" <c:if test="${viewVo.log_type == '2'}">selected="selected"</c:if>>통합통계로그</option>
																	<option value="3" <c:if test="${viewVo.log_type == '3'}">selected="selected"</c:if>>APP로그 +통합통계로그</option>
																</select>
															</td>
						                                </tr>
						                                <tr align="center">
						                                    <th width="25%">로그레벨</th> 
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<select name="log_level" id="log_level">
																	<option value="1" <c:if test="${viewVo.log_level == '1'}">selected="selected"</c:if>>Verbose</option>
																	<option value="2" <c:if test="${viewVo.log_level == '2'}">selected="selected"</c:if>>Debug</option>
																	<option value="3" <c:if test="${viewVo.log_level == '3'}">selected="selected"</c:if>>Info</option>
																	<option value="4" <c:if test="${viewVo.log_level == '4'}">selected="selected"</c:if>>Warning </option>
																	<option value="5" <c:if test="${viewVo.log_level == '5'}">selected="selected"</c:if>>Error</option>
																</select>
															</td>
						                                </tr>
						                                <tr align="center">
						                                    <th width="25%">등록자</th> 
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																${viewVo.userId}
															</td>
						                                </tr>
													</tbody>
					                            </table>
					                            
					                            <!-- 등록/수정 버튼 -->
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<span class="button small blue" id="sumitBtn">수정</span>
						                                	<a href="./qualityMemberList.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&s_log_type=${vo.s_log_type}&serviceType=${vo.serviceType}"><span class="button small blue">목록</span></a>
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
</body>
</html>