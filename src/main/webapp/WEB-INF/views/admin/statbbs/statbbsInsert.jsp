<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<link href="/smartux_adm/css/anytime_style.css" rel="stylesheet" type="text/css">
<script src="/smartux_adm/js/anytime.js"></script>
<script type="text/javascript"> 
$(document).ready(function(){
	AnyTime.picker( "e_date", { format: "%z-%m-%d %H:00",  labelTitle: "날짜",   labelHour: "시간" , time:""} );
	$("#submitBtn").click(function(){
		if($("#title").val() == ''){
			alert('제목을 입력해 주세요.');
			$("input[name='title']").focus();
			return false;
		}else if($("#content").val() == ''){
			alert('내용을 입력해 주세요.');
			$("#content").focus();
			return false;
		}else if(textareaCheck($("#content").val())){
	 		var check=textareaCheck($("#content").val())
	 		alert(check+"는 입력할수 없습니다."); 		
	 		$("#content").focus();
			return false;
	  	}else if($("#e_date").val() == ''){
			alert('종료일을 입력해 주세요.');
			$("#e_date").focus();
			return false;
		}
		$("#form1").submit();
	});
});
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
                                    참여통계 관리
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
						                                            <td class="bold">참여통계 등록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="form1" action="./statbbsInsertProc.do" method="post">
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">*제목</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="text" id="title" name="title" onKeyUp="checkByte($(this),'50')" size="35" style="font-size: 12px;" />								
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">*내용</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >							                                  
															<textarea rows="10" cols="40" name="content" id="content" onKeyUp="checkByte($(this),'2000')" ></textarea>
															  <br>[\f, !^, \b, \f88, \f99]  사용제한 / 2000byte 까지 허용
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">*종료일시</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="text" id="e_date" name="e_date" width="95%">			
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">*사용여부</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
			                                            	<select class="select" id="use_yn" name="use_yn">
			                                                    <option value="Y">사용</option>
			                                                    <option value="N">미사용</option>
			                                                </select>			
														</td>
					                                </tr>
					                            	</tbody>
					                            </table>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<span class="button small blue" id="submitBtn">등록</span>
						                                	<a href="statbbsList.do?findName=${vo.p_findName}&findValue=${vo.p_findValue}&pageNum=${vo.p_pageNum}&use_yn=${vo.p_use_yn}"><span class="button small blue">목록</span></a>
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