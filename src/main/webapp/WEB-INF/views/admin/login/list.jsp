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
	
	if(!confirm("["+f_id+"] 잠금 해제를 하시겠습니까?")) {
		return;
	}else{
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
		    		alert("["+f_id+"] 잠금 해제 되었습니다.")
		    		$("#"+div_id).html("&nbsp;");
		    	}else{
		    		alert("에러가 발생하였습니다.");	
		    	}	    	
		    }
		});	
	}
}


function view_password(user_id, findName, findValue, pageNum, type){
	var url = "<%=webRoot%>/admin/login/password.do?callbak=getView_passwordCallbak&user_id=" + user_id + "&findName=" + findName +"&findValue="+ findValue+ "&pageNum="+ pageNum+ "&type=" + type;
	category_window = window.open(url, 'view_password', 'width=500,height=100,left=100,top=50,scrollbars=yes');
}

function getView_passwordCallbak(data) {
	$("#password").val(data.password);
	$("#user_id").val(data.user_id);
	$("#findName").val(data.findName);
	$("#findValue").val(data.findValue);
	$("#pageNum").val(data.pageNum);

	var url = "";
	var form = "";
	
	if(data.type =='U'){
		url = "<%=webRoot%>/admin/login/updatePage.do?findName=" + data.findName +"&findValue="+ data.findValue+ "&pageNum="+ data.pageNum;
		form = document.createElement('form');
		form.setAttribute('method', 'post');
		form.setAttribute('action', url);
		document.charset = "utf-8";
		
		var hiddenField = document.createElement('input');
		hiddenField.setAttribute('type', 'hidden');
		hiddenField.setAttribute('name', 'pass');
		hiddenField.setAttribute('value', data.password);
		form.appendChild(hiddenField);
		
		
		var userIdHiddenField = document.createElement('input');
		userIdHiddenField.setAttribute('type', 'hidden');
		userIdHiddenField.setAttribute('name', 'user_id');
		userIdHiddenField.setAttribute('value', data.user_id);
		form.appendChild(userIdHiddenField);
	
	}else{
		url = "<%=webRoot%>/admin/login/insert.do?findName=" + data.findName +"&findValue="+ data.findValue+ "&pageNum="+ data.pageNum;
		form = document.createElement('form');
		form.setAttribute('method', 'get');
		form.setAttribute('action', url);
		document.charset = "utf-8";
		
		var hiddenField = document.createElement('input');
		hiddenField.setAttribute('type', 'hidden');
		hiddenField.setAttribute('name', 'pass');
		hiddenField.setAttribute('value', data.password);
		form.appendChild(hiddenField);
		
	}
	document.body.appendChild(form);
	form.submit();
}
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
                <tr style="display:block">
                    <td height="42" width="100%">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tbody>
                            <tr>
                                <td width="300" class="boldTitle">
                                	<!-- 카테고리 타이틀 -->
                                    <!-- <img src="/smartux_adm/images/admin/category_map_title.gif">-->
                                    관리자
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
			                                <table border="0" cellpadding="0" cellspacing="0" width="100%" class="search_table">
			                                	<tbody>
			                                	<tr>
				                                    <td width="15">&nbsp;</td>
				                                    <td width="80"><img src="/smartux_adm/images/admin/search_title4.gif" border="0" height="47" width="62"></td>
				                                    <td>
				                                    	<form id="form2" method="get" action="list.do"> 
					                                    <table border="0" cellpadding="0" cellspacing="0" width="280">		                                    
		                                        			<tbody>
		                                        			<tr><td>&nbsp;</td></tr>
		                                        			<tr>
					                                            <td width="10">
					                                            	<img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9" />
					                                            </td>
					                                            <td>
					                                                <select class="select" id="cateOne" name="findName">
					                                                    <option <c:if test="${vo.findName == 'USER_ID'}">selected="selected"</c:if> value="USER_ID">아이디</option>
					                                                    <option <c:if test="${vo.findName == 'NAME'}">selected="selected"</c:if> value="NAME">이름</option>
					                                                </select>  
					                                            	<input type="text" name="findValue" value="${vo.findValue }" size="20" style="font-size: 12px;" />
					                                            </td>					
					                                            <td width="66" align="left"><input src="/smartux_adm/images/admin/search.gif" border="0" height="22" type="image" width="65"></td>
		                                        			</tr>
			                                    			</tbody>
			                                    		</table>
			                                    		</form>
			                                    	</td>
					                            </tr> 
					                        	</tbody>
					                        </table>
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
						                                            <td class="bold">관리자 리스트</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                                <!-- td align="right">
						                                	<a href="insert.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}"><span class="button small blue">계정 등록</span></a>
						                                </td>-->
						                            </tr>
						                       		</tbody>
						                       	</table>
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list">
					                                <tbody>
					                                <tr align="center">
					                                    <th scope="col" width="15%">아이디</th>
					                                    <th scope="col" width="10%">권한</th>
					                                    <th scope="col" width="10%">이름</th>
					                                    <th scope="col" width="20%">이메일</th>
					                                    <th scope="col" width="20%">만료일</th>
					                                    <th scope="col">잠금상태</th>
					                                </tr>
					                                
					                                <c:if test="${vo.list == '[]' }">
					                                <tr align="center">
					                                    <td class="table_td_04" colspan="5">데이터가 존재 하지 않습니다.</td>					                                    
					                                </tr>
					                                </c:if>
					                            	<c:forEach items="${vo.list}" var="rec">
					                            	<c:set var="i" value="${i+1}" />
						                            	<tr align="center">
						                                    <td class="table_td_04">
						                                    		<c:choose>
						                                    			<c:when test="${auth_decrypt == '00'}">
								                                    	<!-- 	<a href="update.do?user_id=${rec.user_id_aes }&findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}">${rec.user_id }</a> -->
								                                    		<a href="#" onclick="view_password('${rec.user_id_aes }', '${vo.findName}', '${vo.findValue}','${vo.pageNum}', 'U')">${rec.user_id }</a>
						                                    			</c:when>
						                                    			<c:otherwise>
						                                    				<c:choose>
								                                    			<c:when test="${rec.user_id == id_decrypt}">
								                                    			<!--  	<a href="update.do?user_id=${rec.user_id_aes }&findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}">${rec.user_id }</a>  -->
								                                    				<a href="#" onclick="view_password('${rec.user_id_aes }', '${vo.findName}', '${vo.findValue}','${vo.pageNum}','U')">${rec.user_id }</a>
								                                    			</c:when>
									                                    		<c:otherwise>
									                                    				${rec.user_id }
									                                    		</c:otherwise>
								                                    		</c:choose>
						                                    			</c:otherwise>
						                                    		</c:choose>
						                                    </td>
						                                    <td class="table_td_04">
						                                    <c:choose>
						                                    	<c:when test="${rec.user_auth == '00'}">
						                                    		슈퍼관리자
																</c:when>
						                                    	<c:when test="${rec.user_auth == '01'}">
						                                    		일반관리자
																</c:when>
						                                    	<c:when test="${rec.user_auth == '02'}">
						                                    		세컨드TV관리자
																</c:when>
																<c:when test="${rec.user_auth == '03'}">
						                                    		VOD프로모션관리자
																</c:when>	
																<c:when test="${rec.user_auth == '04'}">
						                                    		시즌관리자
																</c:when>	
																<c:otherwise>
																	미확인관리자
																</c:otherwise>																				                
															</c:choose>
						                                    </td>
						                                    <td class="table_td_04">&nbsp;${rec.name }</td>
						                                    <td class="table_td_04">&nbsp;${rec.email }</td>
						                                    <td class="table_td_04">
						                                    		<c:choose>
							                                    		<c:when test="${fn:length(rec.exp_date) > 7}">
							                                    			${fn:substring(rec.exp_date,0,4)}-${fn:substring(rec.exp_date,4,6)}-${fn:substring(rec.exp_date,6,8)}
							                                    		</c:when>
						                                    		</c:choose>
						                                    		</td>
						                                    <td class="table_td_04" id="clear_${i}">
						                                    		<c:choose>
						                                    			<c:when test="${auth_decrypt == '00'}">
								                                    		<c:choose>
								                                    			<c:when test="${rec.loginfailcnt >= 3}">
								                                    				<a href="javascript:setLoginFailClear('${rec.user_id }','clear_${i}');"><span class="button small blue">잠금 해제</span></a>
								                                    			</c:when>
								                                    		</c:choose>						                                    				
						                                    			</c:when>
						                                    			<c:otherwise>
						                                    				<c:choose>
								                                    			<c:when test="${rec.loginfailcnt >= 3}">
								                                    				<span class="button small blue">잠김</span>
								                                    			</c:when>
								                                    		</c:choose>
						                                    			</c:otherwise>
						                                    		</c:choose>
						                                    		&nbsp;</td>
						                                </tr>
					                            	</c:forEach>
					                            	</tbody>
					                            </table>
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
						                            <tbody>
						                            <tr>
						                                <td height="5"></td>
						                            </tr>
						                            <tr>
						                            	<td align="center">
						                            		<jsp:include page="/WEB-INF/views/include/naviControll.jsp">
																<jsp:param value="list.do" name="actionUrl"/>
																<jsp:param value="?findName=${vo.findName}&findValue=${vo.findValue}" name="naviUrl" />
																<jsp:param value="${vo.pageNum }" name="pageNum"/>
																<jsp:param value="${vo.pageSize }" name="pageSize"/>
																<jsp:param value="${vo.blockSize }" name="blockSize"/>
																<jsp:param value="${vo.pageCount }" name="pageCount"/>			  
															</jsp:include>
						                            	</td>
						                            </tr>
						                        	</tbody>
						                        </table>

					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            <tbody>
						                            <tr>
						                                <td align="right">
						                                	<!--<a href="insert.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}"><span class="button small blue">계정 등록</span></a> -->
           									                <a href="#" onclick="view_password('', '${vo.findName}', '${vo.findValue}','${vo.pageNum}', 'I')"><span class="button small blue">계정 등록</span></a>
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
<c:choose>
<c:when test="${vo.validate == 'NOT ID'}">
	<script type="text/javascript">
		alert('잘못된 접근입니다.');		
	</script>
</c:when>
</c:choose>
<c:choose>
<c:when test="${print == '1'}">
	<script type="text/javascript">
		alert('정상적으로 처리되었습니다.');		
	</script>
</c:when>
<c:when test="${print == '0'}">
	<script type="text/javascript">
		alert('정상적으로 처리 되지 않습니다.');		
	</script>
</c:when>
<c:when test="${print == '2'}">
	<script type="text/javascript">
		alert('비밀번호가 동일하여 정상적으로 처리 되지 않았습니다.');		
	</script>
</c:when>
<c:when test="${print == '3'}">
	<script type="text/javascript">
		alert('history에 있는 비밀번호는 변경 불가합니다.');
	</script>
</c:when>
<c:when test="${print == '4'}">
	<script type="text/javascript">
		alert('만료일은 90일 이하만 입력 가능합니다.');
	</script>
</c:when>
<c:when test="${print == '5'}">
	<script type="text/javascript">
		alert('비밀번호가 맞지 않습니다.');
	</script>
</c:when>
<c:when test="${print == '6'}">
	<script type="text/javascript">
		alert('잠금상태 입니다. 해제 후 이용하세요.');
	</script>
</c:when>
</c:choose>

</body>
</html>