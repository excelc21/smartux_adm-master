<%@page import="com.dmi.smartux.common.property.SmartUXProperties"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<%@ taglib prefix="fn"  	uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" 	uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
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
		var code =  $("#code").val();
		var category =  $("#category").val();
		var recommend_text =  $("#recommend_text").val();
		var write_id =  $("#write_id").val();

		if(category == ""){
			alert("구분을 선택해 주세요");
			$("#category").focus();
		}else if(recommend_text == ""){
			alert("검색어를 입력해 주세요");
			$("#recommend_text").focus();
		}else{
			$.post("<%=webRoot%>/admin/youtube/update.do", 
					 {category : category, recommend_text : recommend_text, write_id : write_id, code : code},
					  function(data) {
						 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
						 	var flag = data.flag;
						 	var message = data.message;
						 	
						 	if(flag == "0000"){						// 정상적으로 처리된 경우
						 		opener.location.reload();
								alert("Youtube 초기 검색어가 수정되었습니다");
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
		$("#regfrm")[0].reset();
	});
	
	$("#closebtn").click(function(){
		 window.close();		
	});	
});
</script>
</head>

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
						                                            <td class="bold">Youtube 초기 검색어 등록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>						                       	
						                       	<form id="regfrm" name="regfrm" method="post" action="">
					                            <table border="0" cellpadding="0" cellspacing="0" width="450" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">구분</th>
					                                    <td width="1%"></td>
					                                    <td width="74%" align="left" >
															<select id="category" name="category">
																<option <c:if test="${vo.category == 'q'}">selected="selected"</c:if> value="q"><%=SmartUXProperties.getProperty("Youtube.category.q") %></option>
																<option <c:if test="${vo.category == 'v'}">selected="selected"</c:if> value="v"><%=SmartUXProperties.getProperty("Youtube.category.v") %></option>
																<option <c:if test="${vo.category == 'vv'}">selected="selected"</c:if> value="vv"><%=SmartUXProperties.getProperty("Youtube.category.vv") %></option>
																<option <c:if test="${vo.category == 'p'}">selected="selected"</c:if> value="p"><%=SmartUXProperties.getProperty("Youtube.category.p") %></option>
															</select>								
														</td>
					                                </tr>					                                
					                                <tr align="center">
					                                    <th width="25%">검색어</th>
					                                    <td width="1%"></td>
					                                    <td width="74%" align="left" >
															<input type="text" id="recommend_text" name="recommend_text" size="45" maxlength="100" style="font-size: 12px;" value="${vo.recommend_text }" />																	
														</td>
					                                </tr>
							                       	</tbody>
					                            </table>
					                            
					                        	<br>
												<br>
					                            <table border="0" cellpadding="0" cellspacing="0" width="450" >
					                              <tr  align="right">
				                                    <td >				                                    	
												    	<input type="button" id="regbtn"   value="수정"    class="button small blue" align="right"/>
												    	<input type="button" id="resetbtn" value="재작성"  class="button small blue"/>
						                               	<input type="button" id="closebtn" value="닫기"    class="button small blue"/>		
												    </td>
												  </tr>  
												</table>  
												<input type="hidden" id="write_id" name="write_id" value="<%=id_decrypt %>" />
											 	<input type="hidden" id="code" name="code" value="${vo.code }" />
					                            	
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


