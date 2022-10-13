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
var pannel_id = "${panelVO.pannel_id}";
var next_pannel_id = "${panelVO.next_pannel_id}";

$(document).ready(function(){
	
	if(next_pannel_id != null && next_pannel_id != ''){
		$('[name=panel_id]').attr('readonly', true);
		$('#panel_id').val(next_pannel_id);
	}else{
		if(pannel_id == 'ZZZZ'){
			alert("자동채번의 마지막 입니다. 직접입력하세요.");
			$('[name=panel_id]').attr('readonly', false);
			$('#_chk').attr('checked', true);
		}else{
			$('[name=panel_id]').attr('readonly', true);
			$('#panel_id').val(pannel_id);
		}
		
	}
	
	$('[name=panel_id]').keyup(function () {
		var chkval = $(this).val().toUpperCase();
		if(chkval.substring(0,1) == 'Z'){
			alert("Z로 시작하는 패널코드는 자동채번만 가능합니다.");
			$(this).val('');
		}
	});
	
	var initsel = $("#selpanel_id").val();
	if(initsel == "ETC"){
		$("#trpannel_id").show();
		$("#trpannel_nm").show();
	}else{
		$("#trpannel_id").hide();
		$("#trpannel_nm").hide();
	}
	
	$("#selpanel_id").change(function(){
		var panel_id = $("#selpanel_id").val();
		if(panel_id == "ETC"){
			$("#trpannel_id").show();
			$("#trpannel_nm").show();
		}else{
			$("#trpannel_id").hide();
			$("#trpannel_nm").hide();
		}
	});
	
	$("#regbtn").click(function(){
		var selpanel_id = $("#selpanel_id").val();
		var panel_id = "";
		var panel_nm = "";
		
		if(selpanel_id == "ETC"){
			panel_id = $("#panel_id").val();
			panel_nm = $("#panel_nm").val();
		}else{
			panel_id = selpanel_id;
			panel_nm = $("#selpanel_id option:selected").text();
		}
		
		var panel_ui_type = $("#panel_ui_type").val();
		var use_yn = $(':input:radio[name="use_yn"]:checked').val();
		var panel_image_tmp = $("#panel_image").val();
		var panel_image = $("#panel_image").val();
		var focus_type = $("#focus_type").val();
		
		if(panel_image != ""){
			var imgval = $("#panel_image").val();
			if(!imgval.toLowerCase().match(/.(gif|jpg|png|jpeg|bmp)$/i)){
				alert("업로드하고자 하는 파일을 이미지 파일(gif,jpg,png,jpeg,bmp)로 설정해주세요");
				$("#panel_image").focus();
				return false;
			}
		}
		
		if(panel_id == ""){
			alert("등록하고자 하는 패널 코드를 입력해주세요");
			$("#panel_id").focus();
			return;
		}else if(panel_nm == ""){
			alert("등록하고자 하는 패널명을 입력해주세요");
			$("#panel_nm").focus();
			return;
		}else if(checkByte(panel_id) > 4) {
			alert("패널 코드는 4바이트까지 입력 가능합니다");
			$("#panel_id").focus();
			return;
		}else if(checkByte(panel_nm) > 100) {
			alert("패널명은 100바이트까지 입력 가능합니다");
			$("#panel_nm").focus();
			return;	
		}else{
			if($('#_chk').is(':checked')){
	            var idRegExp = /^[a-zA-Z0-9]{1,19}$/g;
	
	            if (!idRegExp.test(panel_id)) {
	                alert('패널 코드는 영문, 숫자 만 입력이 가능 합니다.');
	                return;
	            }
            }
			
			var smartUXManager = $("#smartUXManager").val();

			var formData = new FormData();
			formData.append("panel_id", panel_id);
			formData.append("panel_nm", panel_nm);
			formData.append("use_yn", use_yn);
			formData.append("smartUXManager", smartUXManager);
			formData.append("panel_ui_type", panel_ui_type);
			if(panel_image != ""){
				formData.append("panel_image", $("#panel_image")[0].files[0]);
			}
			formData.append("focus_type", focus_type);
			
			$.ajax({
				url: '<%=webRoot%>/admin/mainpanel/insertPanel.do',
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
				 		//opener.location.reload();
				 		opener.location.href = '<%=webRoot%>/admin/mainpanel/getPanelList.do';
						alert("패널이 등록되었습니다");
						self.close();
				 	}else{
				 		
			 			if(flag == "EXISTS PANEL_ID"){
			 				alert("현재 등록하고자 하는 패널코드 " + panel_id + "은 이미 등록되어 있는 패널코드입니다\n다른 패널코드를 사용해주세요");	
			 				$("#panel_id").focus();
			 			}else if(flag == "EXISTS PANEL_NM"){
			 				alert("현재 등록하고자 하는 패널명 " + panel_nm + "은 이미 등록되어 있는 패널명입니다\n다른 패널명을 사용해주세요");
			 				$("#panel_nm").focus();
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
		$("#regfrm")[0].reset();
	});
});

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

function checkChk(){
	if($('#_chk').is(':checked')){
		$('[name=panel_id]').attr('readonly', false);
		$('[name=panel_id]').val('');
		//$('[name=autoId]').hide();
		//$('[name=dirId]').show();
	}else{
		var newId;
		if(next_pannel_id != null && next_pannel_id != ''){
			newId = next_pannel_id;
		}else{
			newId = pannel_id;
		}
		
		$('[name=panel_id]').attr('readonly', true);
		$('[name=panel_id]').val(newId);
		//$('[name=autoId]').show();
		//$('[name=dirId]').hide();
	}
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
						                                            <td class="bold">패널 등록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <!-- <form id="regfrm" name="regfrm" method="post" action=""> -->
						                        <form id="regfrm" name="regfrm" method="post" encType="multipart/form-data">
					                            <table border="0" cellpadding="0" cellspacing="0" width="400" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">패널선택</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<c:choose>
																<c:when test="${result==null || fn:length(result) == 0}">
																	<select id="selpanel_id" name="selpanel_id">
																		<option value="ETC">기타</option>
																	</select>
																</c:when>
																<c:otherwise>
																	<select id="selpanel_id" name="selpanel_id">
																	<c:forEach var="item" items="${result}" varStatus="status">
																		<option value="${item.pannel_id}">${item.pannel_nm}</option>
																	</c:forEach>
																		<option value="ETC">기타</option>
																	</select>
																</c:otherwise>
															</c:choose>				
														</td>
					                                </tr>
					                                <tr id="trpannel_id" align="center">
					                                    <th width="25%">패널코드</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="text" id="panel_id" name="panel_id" size="8" maxlength="4" style="font-size: 12px;" />		
															직접입력<input type="checkbox" id="_chk" name="_chk" style=" vertical-align:-2px;" onClick="checkChk();"/>						
														</td>
												    </tr>
					                                <tr id="trpannel_nm" align="center">
					                                    <th width="25%">패널명</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="text" id="panel_nm" name="panel_nm" size="35" maxlength="100" style="font-size: 12px;" />								
														</td>
					                                </tr>
					                                <tr id="trpanel_image" align="center">
					                                    <th width="25%">패널 이미지</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<input type="file" id="panel_image" name="panel_image" value="">
														</td>
					                                </tr>
					                                <tr id="trpanel_ui_type" align="center">
					                                    <th width="25%">패널UI타입</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                        <input type="text" name="panel_ui_type" id="panel_ui_type" size="8" value="" style="font-size: 12px;text-align:center" readonly>
															<span class="button small blue" id="searchBtn" onclick="getPanelUiTypeListPop()">패널 UI타입 선택</span></td>							
														</td>
					                                </tr>
					                                 <th width="25%">포커스타입</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" > <!-- onchange="PageTypeBtnOpenCheck();" -->
														    <select id="focus_type" name="focus_type" >
																<option  value="">미선택</option>
																<option  value="1">지정</option>
																<option  value="2">히스토리</option>
															</select>
													</td>
					                                
					                                <tr align="center">
					                                    <th width="25%">사용여부</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="radio" id="use_ynY" name="use_yn" value="Y" checked/>예
															<input type="radio" id="use-ynN" name="use_yn" value="N" />아니오							
														</td>
					                                </tr>
					                            	</tbody>
					                            </table>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="400" align="center">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="center">
						                                	<span class="button small blue" id="regbtn">등록</span>	
						                                	<span class="button small blue" id="resetbtn">재작성</span>	
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
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