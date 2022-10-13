<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script src="/smartux_adm/js/jquery.simple-color.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	$('.simpleColor').simpleColor({displayColorCode: true, boxWidth : '50px', livePreview: true, defaultColor:'${result.title_color}', colorCodeColor:'${result.title_color}',
		onSelect:function(hex){
			$('#title_color').val(hex);
		}
	});
	
	$("#updbtn").click(function(){
		var panel_id = $("#panel_id").val();
		var p_title_id = $("#p_title_id").val();
		var title_id = $("#title_id").val();
		var title_nm = $("#title_nm").val();
		var title_color = $("#title_color").val();
		var newTitle_nm = $("#newTitle_nm").val();
		var title_bg_img_file = $("#title_bg_img_file").val();
		var title_bg_img_file_nm = $("#title_bg_img_file_nm").val();
		var smartUXManager = $("#smartUXManager").val();
		var bg_img_file = $("#bg_img_file").val();
		var bg_img_file_nm = $("#bg_img_file_nm").val();
		var bg_img_file2 = $("#bg_img_file2").val();
		var bg_img_file2_nm = $("#bg_img_file2_nm").val();
		var bg_img_file3 = $("#bg_img_file3").val();
		var bg_img_file3_nm = $("#bg_img_file3_nm").val();
		
		
		if(title_bg_img_file != ""){
			var imgval = $("#title_bg_img_file").val();
			if(!imgval.toLowerCase().match(/.(gif|jpg|png|jpeg|bmp|zip)$/i)){
				alert("업로드하고자 하는 파일을 이미지 파일(gif,jpg,png,jpeg,bmp,zip)로 설정해주세요");
				$("#title_bg_img_file").focus();
				return false;
			}
		}
		if(bg_img_file != ""){
			var imgval = $("#bg_img_file").val();
			if(!imgval.toLowerCase().match(/.(gif|jpg|png|jpeg|bmp|zip)$/i)){
				alert("업로드하고자 하는 파일을 이미지 파일(gif,jpg,png,jpeg,bmp,zip)로 설정해주세요");
				$("#bg_img_file").focus();
				return false;
			}
		}
		if(bg_img_file2 != ""){
			var imgval = $("#bg_img_file2").val();
			if(!imgval.toLowerCase().match(/.(gif|jpg|png|jpeg|bmp|zip)$/i)){
				alert("업로드하고자 하는 파일을 이미지 파일(gif,jpg,png,jpeg,bmp,zip)로 설정해주세요");
				$("#bg_img_file2").focus();
				return false;
			}
		}

		if(bg_img_file3 != ""){
			var imgval = $("#bg_img_file3").val();
			if(!imgval.toLowerCase().match(/.(gif|jpg|png|jpeg|bmp|zip)$/i)){
				alert("업로드하고자 하는 파일을 이미지 파일(gif,jpg,png,jpeg,bmp,zip)로 설정해주세요");
				$("#bg_img_file3").focus();
				return false;
			}
		}
		
		var use_yn = $(':input:radio[name="use_yn"]:checked').val();
		
		if(newTitle_nm == ""){
			alert("수정하고자 하는 지면명을 입력해주세요");
			$("#newTitle_nm").focus();
			return;
		}else if(checkByte(newTitle_nm) > 100) {
			alert("지면명은 100바이트까지 입력 가능합니다");
			$("#newTitle_nm").focus();
			return;	
		}else{
			var formData = new FormData();
			formData.append("panel_id", panel_id);
			formData.append("p_title_id", p_title_id);
			formData.append("title_id", title_id);
			formData.append("title_nm", title_nm);
			formData.append("title_color", title_color);
			formData.append("use_yn", use_yn);
			formData.append("newTitle_nm", newTitle_nm);
			formData.append("smartUXManager", smartUXManager);
			formData.append("title_bg_img_file_nm", title_bg_img_file_nm);
			formData.append("bg_img_file_nm", bg_img_file_nm);
			formData.append("bg_img_file2_nm", bg_img_file2_nm);
			formData.append("bg_img_file3_nm", bg_img_file3_nm);
			
			if(title_bg_img_file != ""){
				formData.append("title_bg_img_file", $("#title_bg_img_file")[0].files[0]);
			}
			if(bg_img_file != ""){
				formData.append("bg_img_file", $("#bg_img_file")[0].files[0]);
			}
			if(bg_img_file2 != ""){
				formData.append("bg_img_file2", $("#bg_img_file2")[0].files[0]);
			}
			if(bg_img_file3 != ""){
				formData.append("bg_img_file3", $("#bg_img_file3")[0].files[0]);
			}
			
			$.ajax({
				url: '<%=webRoot%>/admin/mainpanel/updatePanelTitleTemp.do',
				data: formData,
				processData: false,
				contentType: false,
				async: false,
				type: 'POST',
				dataType: 'json',
				success: function(data){
					// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
				 	var flag = data.flag;
				 	var message = data.message;
				 	
				 	if(flag == "0000"){						// 정상적으로 처리된 경우
				 		opener.location.reload();
						alert("지면 정보가 수정되었습니다");
						self.close();
				 	}else{
				 		if(flag == "EXISTS NEW_TITLE_NM"){
			 				alert("현재 수정하고자 하는 지면명 " + newTitle_nm + "은 이미 등록되어 있는 지면명입니다\n다른 지면명을 사용해주세요");
			 				$("#title_nm").focus();
				 		}else if(flag == "NEW_TITLE_NM LENGTH"){
				 			alert("지면명은 100자 이내이어야 합니다");
				 			$("#title_nm").focus();
				 		}else{
				 			alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
				 		}
				 	}
				},
				error: function(e){
					alert("일시적인 장애가 발생하였습니다. 다시 시도해 주세요.");
					$.unblockUI();
				}
			});
		}
		
	});
	
	$("#resetbtn").click(function(){
		$("#updfrm")[0].reset();
	});
});
function winopen(imagePath, Width, Height) {
    window.open(imagePath, 'windowName', 'toolbar=no,scrollbars=yes, top=20,left=200,width=' + Width + ',height=' + Height);
}

function deleteImage(chImg){
	if(confirm("이미지를 삭제하시겠습니까?")){
		if(chImg == "1"){
			$("#title_bg_img_file_nm").attr("value","");
			$("#title_bg_img_file_del").remove();
		}else if(chImg == "2"){
			$("#bg_img_file_nm").attr("value","");
			$("#bg_img_file_del").remove();
		}else if(chImg == "3"){
			$("#bg_img_file2_nm").attr("value","");
			$("#bg_img_file2_del").remove();
		}else if(chImg == "4"){
			$("#bg_img_file3_nm").attr("value","");
			$("#bg_img_file3_del").remove();
		}
	}
}

//필드에 입력된 문자의 바이트 수를 체크
function checkByte(message) {
	var totalByte = 0;
	var charCount = 0;

	for (var i=0; i < message.length; i++) {
		var currentByte = message.charCodeAt(i);
		if (currentByte > 128 || currentByte==10) {
			totalByte += 2;
		} else {
			totalByte++;
		}	
	}
	
	return totalByte;
}
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
						                                            <td class="bold">지면 상세조회</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="updfrm" name="updfrm" method="post" action="" encType="multipart/form-data">
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="30%">지면명</th>
					                                    <td width="5%"></td>
					                                    <td width="65%" align="left" >
															<input type="text" id="newTitle_nm" name="newTitle_nm" value="${result.title_nm}" size="35" maxlength="100" style="font-size: 12px;" />
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="30%">지면명 색상</th>
					                                    <td width="5%"></td>
					                                    <td width="65%" align="left" >
															<input class="simpleColor" value="${result.title_color}"/><input type="hidden" id="title_color" name="title_color" value="${result.title_color }"/>
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="30%">지면 이미지</th>
					                                    <td width="5%"></td>
					                                    <td width="65%" align="left" >
					                                    	<input type="file" id="title_bg_img_file" name="title_bg_img_file" value="">
					                                    	<c:if test="${not empty result.title_bg_img_file }">
					                                    		<div id="title_bg_img_file_del">																			
																	  <br><a href="javascript:winopen('${result.img_url}${result.title_bg_img_file }','200','300')">${result.title_bg_img_file}</a>									 
																	<a href="javascript:deleteImage('1')"><span class="button small rosy">삭제</span></a>
																</div>
																<input type="hidden" id="title_bg_img_file_nm" name="title_bg_img_file_nm" value="${result.title_bg_img_file}">
					                                    	</c:if>
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="30%">지면 아이콘 이미지1</th>
					                                    <td width="5%"></td>
					                                    <td width="65%" align="left" >
					                                    	<input type="file" id="bg_img_file" name="bg_img_file" value="">
					                                    	<c:if test="${not empty result.bg_img_file }">
					                                    		<div id="bg_img_file_del">																			
																	  <br><a href="javascript:winopen('${result.img_url}${result.bg_img_file }','200','300')">${result.bg_img_file}</a>									 
																	<a href="javascript:deleteImage('2')"><span class="button small rosy">삭제</span></a>
																</div>
																<input type="hidden" id="bg_img_file_nm" name="bg_img_file_nm" value="${result.bg_img_file}">
					                                    	</c:if>
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="30%">지면 아이콘 이미지2</th>
					                                    <td width="5%"></td>
					                                    <td width="65%" align="left" >
					                                    	<input type="file" id="bg_img_file2" name="bg_img_file2" value="">
					                                    	<c:if test="${not empty result.bg_img_file2 }">
					                                    		<div id="bg_img_file2_del">																			
																	  <br><a href="javascript:winopen('${result.img_url}${result.bg_img_file2 }','200','300')">${result.bg_img_file2}</a>									 
																	<a href="javascript:deleteImage('3')"><span class="button small rosy">삭제</span></a>
																</div>
																<input type="hidden" id="bg_img_file2_nm" name="bg_img_file2_nm" value="${result.bg_img_file2}">
					                                    	</c:if>
														</td>
					                                </tr>
					                                
					                                <tr align="center">
					                                    <th width="30%">지면 아이콘 이미지3</th>
					                                    <td width="5%"></td>
					                                    <td width="65%" align="left" >
					                                    	<input type="file" id="bg_img_file3" name="bg_img_file3" value="">
					                                    	<c:if test="${not empty result.bg_img_file3 }">
					                                    		<div id="bg_img_file3_del">																			
																	  <br><a href="javascript:winopen('${result.img_url}${result.bg_img_file3 }','200','300')">${result.bg_img_file3}</a>									 
																	<a href="javascript:deleteImage('4')"><span class="button small rosy">삭제</span></a>
																</div>
																<input type="hidden" id="bg_img_file3_nm" name="bg_img_file3_nm" value="${result.bg_img_file3}">
					                                    	</c:if>
														</td>
					                                </tr>
					                                
					                                
					                                <tr align="center">
					                                    <th width="30%">사용여부</th>
					                                    <td width="5%"></td>
					                                    <td width="65%" align="left" >
															<c:choose>
															<c:when test="${result.use_yn == 'Y'}">			
															<c:choose>
																<c:when test="${p_result.use_yn == 'T'}">
																	<input type="radio" id="use_ynY" name="use_yn" value="Y" disabled checked/>예
																	<input type="radio" id="use-ynN" name="use_yn" value="N" />아니오
																	<input type="radio" id="use-ynT" name="use_yn" value="T" />검수
																</c:when>
																<c:when test="${p_result.use_yn == 'N'}">
																	<input type="radio" id="use_ynY" name="use_yn" value="Y" disabled checked/>예
																	<input type="radio" id="use-ynN" name="use_yn" value="N" />아니오
																	<input type="radio" id="use-ynT" name="use_yn" value="T" disabled />검수
																</c:when>
																<c:otherwise>
																	<input type="radio" id="use_ynY" name="use_yn" value="Y"  checked/>예
																	<input type="radio" id="use-ynN" name="use_yn" value="N" />아니오
																	<input type="radio" id="use-ynT" name="use_yn" value="T" />검수
																</c:otherwise>
															</c:choose>		
															</c:when>
															
															<c:when test="${result.use_yn == 'T'}">
																
																
															<c:choose>
																<c:when test="${p_result.use_yn == 'T'}">
																	<input type="radio" id="use_ynY" name="use_yn" value="Y" disabled />예
																	<input type="radio" id="use-ynN" name="use_yn" value="N" />아니오
																	<input type="radio" id="use-ynT" name="use_yn" value="T" checked/>검수
																</c:when>
																<c:when test="${p_result.use_yn == 'N'}">
																	<input type="radio" id="use_ynY" name="use_yn" value="Y" disabled />예
																	<input type="radio" id="use-ynN" name="use_yn" value="N" />아니오
																	<input type="radio" id="use-ynT" name="use_yn" value="T" disabled checked/>검수
																</c:when>
																<c:otherwise>
																	<input type="radio" id="use_ynY" name="use_yn" value="Y"   />예
																	<input type="radio" id="use-ynN" name="use_yn" value="N" />아니오
																	<input type="radio" id="use-ynT" name="use_yn" value="T" checked/>검수
																</c:otherwise>
															</c:choose>	
																
																
														</c:when>
														<c:otherwise>
															<c:choose>
																<c:when test="${p_result.use_yn == 'T'}">
																	<input type="radio" id="use_ynY" name="use_yn" value="Y" disabled/>예
																	<input type="radio" id="use-ynN" name="use_yn" value="N" checked/>아니오
																	<input type="radio" id="use-ynT" name="use_yn" value="T" />검수
																</c:when>
																<c:when test="${p_result.use_yn == 'N'}">
																	<input type="radio" id="use_ynY" name="use_yn" value="Y" disabled />예
																	<input type="radio" id="use-ynN" name="use_yn" value="N" checked/>아니오
																	<input type="radio" id="use-ynT" name="use_yn" value="T" disabled />검수
																</c:when>
																<c:otherwise>
																	<input type="radio" id="use_ynY" name="use_yn" value="Y"   />예
																	<input type="radio" id="use-ynN" name="use_yn" value="N" checked/>아니오
																	<input type="radio" id="use-ynT" name="use_yn" value="T" />검수
																</c:otherwise>
															</c:choose>	
														</c:otherwise>
															</c:choose>						
														</td>
					                                </tr>
					                            	</tbody>
					                            </table>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="center">
						                                	<span class="button small blue" id="updbtn">확인</span>	
						                                	<span class="button small blue" id="resetbtn">재작성</span>	
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                       	<input type="hidden" id="panel_id" value="${result.pannel_id}" />
												<input type="hidden" id="p_title_id" value="${result.p_title_id}" />
												<input type="hidden" id="title_id" value="${result.title_id}" />
												<input type="hidden" id="title_nm" value="${result.title_nm}" />
												<input type="hidden" id="smartUXManager" name="smartUXManager" value="<%=id_decrypt %>" />
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