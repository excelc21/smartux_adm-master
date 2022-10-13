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
	$("#updbtn").click(function(){
		var panel_id = $("#panel_id").val();
		var panel_nm = $("#panel_nm").val();
		var existCategory = $("#existCategory").val();
		var newPanel_id = "";
		var newPanel_nm = "";
		var use_yn = $(':input:radio[name="use_yn"]:checked').val();
		var passValidate = false;
		var panel_image = $("#panel_image").val();
		var panel_image_nm = $("#panel_image_nm").val();
		var focus_type = $("#focus_type").val();
		
		if(panel_image != ""){
			var imgval = $("#panel_image").val();
			if(!imgval.toLowerCase().match(/.(gif|jpg|png|jpeg|bmp)$/i)){
				alert("업로드하고자 하는 파일을 이미지 파일(gif,jpg,png,jpeg,bmp)로 설정해주세요");
				$("#panel_image").focus();
				return false;
			}
		}
		
		if(existCategory == "Y"){
			passValidate = true;
			newPanel_id = $("#panel_id").val();
			newPanel_nm = $("#panel_nm").val();
		}else{
			newPanel_id = $("#newPanel_id").val();
			newPanel_nm = $("#newPanel_nm").val();
			
			if(newPanel_id == ""){
				alert("수정하고자 하는 패널코드를 입력해주세요");
				$("#newPanel_id").focus();
				return;
			}else if(newPanel_nm == ""){
				alert("수정하고자 하는 패널명을 입력해주세요");
				$("#newPanel_nm").focus();
				return;
			}else if(checkByte(newPanel_id) > 4) {
				alert("패널 코드는 4바이트까지 입력 가능합니다");
				$("#panel_id").focus();
				return;
			}else if(checkByte(newPanel_nm) > 100) {
				alert("패널명은 100바이트까지 입력 가능합니다");
				$("#panel_nm").focus();
				return;
			}else{
				passValidate = true;
			}
		}
		
		if(passValidate){
			var smartUXManager = $("#smartUXManager").val();
			var panel_ui_type = $("#panel_ui_type").val();
			
		    var formData = new FormData();
			formData.append("panel_id", panel_id);
			formData.append("newPanel_id", newPanel_id);
			formData.append("panel_nm", panel_nm);
			formData.append("newPanel_nm", newPanel_nm);
			formData.append("use_yn", use_yn);
			formData.append("smartUXManager", smartUXManager);
			formData.append("panel_image_nm", panel_image_nm);
			formData.append("panel_ui_type", panel_ui_type);
			formData.append("focus_type", focus_type);
			
			if(panel_image != ""){
				formData.append("panel_image", $("#panel_image")[0].files[0]);
			}
			
			$.ajax({
				url: '<%=webRoot%>/admin/mainpanel/updatePanel.do',
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
						alert("패널정보가 수정되었습니다");
						self.close();
				 	}else{
				 		if(flag == "EXISTS PANEL_ID"){
				 			alert("현재 수정하고자 하는 패널코드 " + newPanel_id + "은 이미 등록되어 있는 패널코드입니다\n다른 패널코드를 사용해주세요");
				 			$("#newPanel_id").focus();
				 		}else if(flag == "EXISTS PANEL_NM"){
				 			alert("현재 수정하고자 하는 패널명 " + newPanel_nm + "은 이미 등록되어 있는 패널명입니다\n다른 패널명을 사용해주세요");
				 			$("#newPanel_nm").focus();	
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


function deleteImage(){
	if(confirm("이미지를 삭제하시겠습니까?")){
		$("#panel_image_nm").attr("value","");
		$("#panel_image_del").remove();
	}
}

//패널UI타입 선택 팝업
function getPanelUiTypeListPop() {
	var data_type_val = $("#data_type option:selected").val();
	url = '<%=webRoot%>/admin/mainpanel/getPanelUiTypeListPop.do?frame_type=30&callbak=getPanelUiTypeListPopCallbak&data_type=' + data_type_val;
	category_window = window.open(url, 'getFrameListPop', 'width=700,height=650,left=100,top=50,scrollbars=yes');
}

function getPanelUiTypeListPopCallbak(data) {
	$("#panel_ui_type").val(data.frame_type_code);
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
						                                            <td class="bold">패널 상세조회</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="updfrm" name="updfrm" method="post" action="" encType="multipart/form-data">
					                            <table border="0" cellpadding="0" cellspacing="0" width="400" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">패널 코드</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<c:choose>
																<c:when test="${existCategory == 'Y'}">
																	${result.pannel_id}
																</c:when>
																<c:otherwise>
																	<input type="text" id="newPanel_id" name="newPanel_id" value="${result.pannel_id}" size="35" maxlength="4" style="font-size: 12px;" />
																</c:otherwise>
															</c:choose>		
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">패널명</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<c:choose>
																<c:when test="${existCategory == 'Y'}">
																	${result.pannel_nm}
																</c:when>
																<c:otherwise>
																	<input type="text" id="newPanel_nm" name="newPanel_nm" value="${result.pannel_nm}" size="35" maxlength="100" style="font-size: 12px;"/>
																</c:otherwise>
															</c:choose>								
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">지면 이미지</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<input type="file" id="panel_image" name="panel_image" value="">
					                                    	<c:if test="${not empty result.panel_image }">
					                                    		<div id="panel_image_del">
																	  <br><a href="javascript:winopen('${result.img_url}${result.panel_image }','200','300')">${result.panel_image}</a>									 
																	<a href="javascript:deleteImage()"><span class="button small rosy">삭제</span></a>
																</div>
																<input type="hidden" id="panel_image_nm" name="panel_image_nm" value="${result.panel_image}">
					                                    	</c:if>
														</td>
					                                </tr>					                                
					                                <tr align="center">
<!-- 2019.11.04 : 패널 UI타입 -> UI타입 으로 수정 - 이태광 Start -->					                                
					                                    <th width="25%">UI타입</th>
<!-- 2019.11.04 : 패널 UI타입 -> UI타입 으로 수정 - 이태광 End -->					                                    
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="text" name="panel_ui_type" id="panel_ui_type" size="8" value="${result.panel_ui_type}" style="font-size: 12px;text-align:center" readonly>
<!-- 2019.11.04 : 패널 UI타입 선택 -> UI타입 선택 으로 수정 - 이태광 Start -->															
															<span class="button small blue" id="searchBtn" onclick="getPanelUiTypeListPop()">UI타입 선택</span>
<!-- 2019.11.04 : 패널 UI타입 선택 -> UI타입 선택 으로 수정 - 이태광 End -->															
														</td>
					                                </tr>	
					                                
					                                   <tr align="center">
					                                    <th width="25%">포커스타입</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" > <!-- onchange="PageTypeBtnOpenCheck();" -->
														    <select id="focus_type" name="focus_type" >
																<option <c:if test="${result.focus_type == ''}">selected="selected"</c:if> value="">미선택</option>
																<option <c:if test="${result.focus_type == '1'}">selected="selected"</c:if> value="1">지정</option>
																<option <c:if test="${result.focus_type == '2'}">selected="selected"</c:if> value="2">히스토리</option>
															</select>
														</td>
					                                </tr>	
					                                
					                                
					                                
					                                				                                
					                                <tr align="center">
					                                    <th width="25%">사용여부</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<c:choose>
																<c:when test="${result.use_yn == 'Y'}">
																	<input type="radio" id="use_ynY" name="use_yn" value="Y" checked/>예
																	<input type="radio" id="use-ynN" name="use_yn" value="N" />아니오
																</c:when>
																<c:otherwise>
																	<input type="radio" id="use_ynY" name="use_yn" value="Y" />예
																	<input type="radio" id="use-ynN" name="use_yn" value="N" checked/>아니오
																</c:otherwise>
															</c:choose>					
														</td>
					                                </tr>
					                            	</tbody>
					                            </table>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="400" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<span class="button small blue" id="updbtn">수정</span>	
						                                	<span class="button small blue" id="resetbtn">재작성</span>	
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                       	<input type="hidden" id="panel_id" value="${result.pannel_id}" />
												<input type="hidden" id="panel_nm" value="${result.pannel_nm}" />
												<input type="hidden" id="existCategory" value="${existCategory}" />
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