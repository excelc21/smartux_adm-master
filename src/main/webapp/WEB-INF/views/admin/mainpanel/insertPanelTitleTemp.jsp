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
	$('.simpleColor').simpleColor({displayColorCode: true, boxWidth : '50px', livePreview: true,
		onSelect:function(hex){
			$('#title_color').val(hex);
		}
	});
	
	$("#regbtn").click(function(){	
		var panel_id = $("#panel_id").val();
		var p_title_id = $("#p_title_id").val();
		var title_nm = $("#title_nm").val();
		var title_color = $("#title_color").val();
		var use_yn = $(':input:radio[name="use_yn"]:checked').val();
		var smartUXManager = $("#smartUXManager").val();
		var title_bg_img_file = $("#title_bg_img_file").val();
		var bg_img_file = $("#bg_img_file").val();
		var bg_img_file2 = $("#bg_img_file2").val();
		var bg_img_file3 = $("#bg_img_file3").val();
		
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
		
		if(title_nm == ""){
			alert("등록하고자 하는 지면명을 입력해주세요");
			$("#title_nm").focus();
			return;
		}else if(checkByte(title_nm) > 100) {
			alert("지면명은 100바이트까지 입력 가능합니다");
			$("#title_nm").focus();
			return;
		}else{
			var formData = new FormData();
			formData.append("panel_id", panel_id);
			formData.append("p_title_id", p_title_id);
			formData.append("title_nm", title_nm);
			formData.append("title_color", title_color);
			formData.append("use_yn", use_yn);
			formData.append("smartUXManager", smartUXManager);
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
				url: '<%=webRoot%>/admin/mainpanel/insertPanelTitleTemp.do',
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
						alert("지면이 등록되었습니다");
						self.close();
				 	}else{
				 		if(flag == "EXISTS TITLE_NM"){
				 			alert("현재 등록하고자 하는 지면명 " + title_nm + "은 이미 등록되어 있는 지면명입니다\n다른 지면명을 사용해주세요");
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
		$("#regfrm")[0].reset();
	});
});

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
						                                            <td class="bold">지면 등록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="regfrm" name="regfrm" method="post" encType="multipart/form-data">
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">지면명</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="text" id="title_nm" name="title_nm" size="35" maxlength="100" style="font-size: 12px;" />				
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">지면명 색상</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<input class="simpleColor" value=""/><input type="hidden" id="title_color" name="title_color" value=""/>
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">지면 이미지</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<input type="file" id="title_bg_img_file" name="title_bg_img_file" value="">
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">지면 아이콘 이미지1</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<input type="file" id="bg_img_file" name="bg_img_file" value="">
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">지면 아이콘 이미지2</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<input type="file" id="bg_img_file2" name="bg_img_file2" value="">
														</td>
					                                </tr>
					                                <tr align="center">
					                                <th width="25%">지면 아이콘 이미지3</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<input type="file" id="bg_img_file3" name="bg_img_file3" value="">
														</td>
					                                </tr>
					                                <tr id="trcodenm" align="center">
					                                    <th width="25%">사용여부</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="radio" id="use_ynY" name="use_yn" value="Y" checked/>예
															<input type="radio" id="use-ynN" name="use_yn" value="N" />아니오			
															<input type="radio" id="use-ynN" name="use_yn" value="T" />검수						
														</td>
					                                </tr>
					                                
					                            	</tbody>
					                            </table>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="left">
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
						                       	<input type="hidden" id="panel_id" name="panel_id" value="${panel_id}" />
												<input type="hidden" id="p_title_id" name="p_title_id" value="${p_title_id}" />
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