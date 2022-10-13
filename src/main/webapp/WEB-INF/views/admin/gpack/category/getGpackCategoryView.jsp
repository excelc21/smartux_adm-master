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
	
	$("#savebtn").click(function (){
		
		var postUrl = "<%=webRoot%>/admin/gpack/category/InsertGpackCategory.do";
		$("input[name=auto_yn]").attr("disabled", false); 

		$("#frm_category").ajaxSubmit({
			dataType : 'json',
			url : postUrl,
			success : function(result) {
			 	var flag = result.flag;
				var message = result.message;

			 	if(flag == "0000"){						// 정상적으로 처리된 경우
			 		opener.location.reload();
					alert("저장되었습니다.");
					self.close();
				}else if(flag == "NOT FOUND CONTENTS"){
				 	alert("카테고리를 사용하려면 하위 콘텐츠가 필요합니다.\n사용여부를 [아니오]로 저장하시고, 하위콘텐츠를 등록하신 후에 사용여부에 [예]를 체크해주세요.");
				}else{
			 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
			 	}
			},
			error : function(data, status, err) {
			 	var flag = data.flag;
			 	var message = data.message;
				alert("error 작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
			}
		});

	});

	//초기화
	$("#resetbtn").click(function (){
		frm_category.reset();
	});

	//닫기 버튼
	$("#closebtn").click(function (){
		self.close();
	});
	
});


</script>
</head>
<body leftmargin="0" topmargin="0" style="font-size: 12px;">
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
						                                            <td class="bold">카테고리 상세조회</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="frm_category" name="frm_category" method="post" action="/smartux_adm/admin/gpack/category/InsertCategory.do">
						                        <input type="hidden" id="view_gb"		name="view_gb"		value="${param.view_gb }"/>
						                        <input type="hidden" id="pack_id"		name="pack_id"		value="${categoryVO.pack_id }"/>
						                        <input type="hidden" id="category_id"	name="category_id"	value="${categoryVO.category_id }"/>
						                        <input type="hidden" id="ordered"		name="ordered"		value="${categoryVO.ordered }"/>
												<input type="hidden" id="smartUXManager" name="smartUXManager" value="<%=id_decrypt %>" />
					                            <table border="0" cellpadding="0" cellspacing="0" width="400" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">카테고리명 *</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
						                                    <input type="text" id="category_nm" name="category_nm" value="${categoryVO.category_nm}" size="35" maxlength="100" style="font-size: 12px;" />
														</td>
					                                </tr>
					                                
					                                <tr align="center">
					                                    <th width="25%">카테고리 멘트</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
						                                    <input type="text" id="category_comment" name="category_comment" value="${categoryVO.category_comment}" size="35" maxlength="300" style="font-size: 12px;" />
														</td>
					                                </tr>
					                                
					                                <tr align="center">
					                                    <th width="25%">자동여부 *</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<input type="radio" name="auto_yn" value="Y" <c:if test="${categoryVO.auto_yn ne 'N' }">checked="checked"</c:if> />예
					                                    	<input type="radio" name="auto_yn" value="N" <c:if test="${categoryVO.auto_yn eq 'N' }">checked="checked"</c:if> />아니오
														</td>
					                                </tr>
					                                
					                                <tr align="center">
					                                    <th width="25%">사용여부 *</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<input type="radio" name="use_yn" value="Y" <c:if test="${categoryVO.use_yn eq 'Y' }">checked="checked"</c:if> />예
					                                    	<input type="radio" name="use_yn" value="N" <c:if test="${categoryVO.use_yn ne 'Y' }">checked="checked"</c:if> />아니오
					                                    	<br> (하위콘텐츠가 있어야 사용가능합니다.)
														</td>
					                                </tr>
					                                
					                            	</tbody>
					                            </table>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="400" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<span class="button small blue" id="savebtn">저장</span>	
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