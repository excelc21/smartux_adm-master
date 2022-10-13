<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
function goUpdate(){
	var f = document.getElementById("form1");
	f.submit();
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
                <tr>
                    <td height="42" width="100%">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tbody>
                            <tr>
                                <td width="300" class="boldTitle">
                                	<!-- 카테고리 타이틀 -->
                                    <!-- <img src="/smartux_adm/images/admin/category_map_title.gif">-->
                                    HDTV
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
						                                            <td class="bold">HDTV STARTUP 설정</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                       
						                        <form id="form1" action="update.do" method="post">
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">비상 여부</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                 	   <select id="status" name="status">
																<option <c:if test="${vo.status == '0'}">selected="selected"</c:if> value="0">정상</option>
																<option <c:if test="${vo.status == '1'}">selected="selected"</c:if> value="1">긴급</option>
															</select>		
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">메세지 여부</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<select id="message_yn" name="message_yn">
																<option <c:if test="${vo.message_yn == 'Y'}">selected="selected"</c:if> value="Y">출력</option>
																<option <c:if test="${vo.message_yn == 'N'}">selected="selected"</c:if> value="N">미출력</option>
															</select>
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">메세지 내용</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<input type="text" value="${vo.message }" maxlength="100" name="message" size="50" />
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">네트워크 타입</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<select id="net_type" name="net_type">
																<option <c:if test="${vo.net_type == '0'}">selected="selected"</c:if> value="0">ALL</option>
																<option <c:if test="${vo.net_type == '1'}">selected="selected"</c:if> value="1">Wi-Fi</option>
																<option <c:if test="${vo.net_type == '2'}">selected="selected"</c:if> value="2">LTE</option>
																<option <c:if test="${vo.net_type == '3'}">selected="selected"</c:if> value="3">3G</option>
															</select>
														</td>
					                                </tr>
					                               
					                            	</tbody>
					                            </table>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<a href="javascript:goUpdate();"><span class="button small blue">설정</span></a>
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
<c:choose>
<c:when test="${validate == 'ERROR'}">
	<script type="text/javascript">
		alert('에러가 발생하였습니다.');
	</script>
</c:when>
<c:when test="${validate == 'SUCCESS'}">
	<script type="text/javascript">
		alert('정상적으로 설정 되었습니다.');
	</script>
</c:when>

</c:choose>
</body>
</html>