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

	$("#regbtn").click(function(){
		var frame_nm			= $("#frame_nm").val().replace(/(^\s*)|(\s*$)/gi, "");
		var status 				= $(':input:radio[name="status"]:checked').val();
		
		$("#use_yn").val(status);

		if(frame_nm == ""){
/* 2019.11.05 : 패널UI타입 명 -> UI타입 명 으로 변경 Start - 이태광 */			
			alert("등록하고자 하는 UI타입 명을 입력해주세요");
/* 2019.11.05 : 패널UI타입 명 -> UI타입 명 으로 변경 End - 이태광 */			
			$("#frame_nm").val("");
			$("#frame_nm").focus();
			return;
		}else if(checkByte(frame_nm) > 200) {
			alert("Title은 200바이트까지 입력 가능합니다.");
			return;
		}else if ('' == $('#img_file').val()) {
        	alert('이미지 파일을 등록 하십시오.');
        	//$("#img_file").focus();
        	return;
		}else{	
			if($('#img_file').val() != ""){
				var imgval = $("#img_file").val();
				if(!imgval.toLowerCase().match(/.(gif|jpg|png|jpeg|bmp)$/i)){
					alert("이미지 파일만 입력 가능합니다.");
					//$("#img_file").focus();
					return;
				}
			}
		    $.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>처리 중..</b>"
			});
			
			var option = {
				type:'POST',
				dataType: 'json',
				timeout : 30000,
				contentType: false,
				cache: false,
			    processData:false,
				success:function(data){
					if(data.flag=="0000"){
						opener.location.reload();
						alert("정상적으로 등록 되었습니다.");
						self.close(); 
					}else{
						alert(data.message);
						$.unblockUI();
					}
				},
				error:function(){
					alert("정상적으로 처리되지 않았습니다.\n재등록 하시기 바랍니다.");
					$.unblockUI();
				}
			};
			$("#multiform").ajaxSubmit(option);
		}
	});
	
	$("#resetbtn").click(function(){
		self.close();
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
<!-- 2019.11.05 : 패널UI타입 등록 -> UI타입 등록 으로 변경 Start - 이태광 -->						                                            
						                                            <td class="bold">UI타입 등록</td>
<!-- 2019.11.05 : 패널UI타입 등록 -> UI타입 등록 으로 변경 End - 이태광 -->						                                            
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="multiform" name="multiform" method="post" action="./insertPanelUiType.do" enctype="multipart/form-data">
					                            <table border="0" cellpadding="0" cellspacing="0" width="450" class="board_data">
					                                <tbody>
					                                <tr id="trpanel_nm" align="center">
					                                    <th width="30%">Title</th>
					                                    <td width="5%"></td>
					                                    <td width="65%" align="left" >
															<input type="text" id="frame_nm" name="frame_nm" size="35" maxlength="200" style="font-size: 12px;" />								
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="30%">Type 이미지</th>
					                                    <td width="5%"></td>
					                                    <td width="65%" align="left" >
															<input type="file" id="img_file" name="img_file" value="파일선택"/>					
														</td>
					                                </tr>
<!-- 2019.09.09 : 타입 안보이게 제거 - 이태광 Start -->					                                
					                                <!-- <tr align="center">
					                                	<th width="30%">Type</th>
					                                    <td width="5%"></td>
					                                    <td width="65%" align="left" >
															패널UI타입	
														</td>
					                                </tr> -->
<!-- 2019.09.09 : 타입 안보이게 제거 - 이태광 End -->					                                
					                                <tr align="center">
					                                	<th width="30%">사용여부</th>
					                                	<td width="5%"></td>
					                                	<td width="65%" align="left" >
															<input type="radio" id="use_ynY" name="status" value="Y" checked/>사용
															<input type="radio" id="use-ynN" name="status" value="N" />미사용
														</td>
					                                </tr>
					                            	</tbody>
					                            </table>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="400" align="center">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="center">
						                                	<span class="button small blue" id="regbtn">등록</span>	
						                                	<span class="button small blue" id="resetbtn">취소</span>	
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                       	<input type="hidden" id="frame_type" name="frame_type" value="30"/>
						                       	<input type="hidden" id="use_yn" name="use_yn" value=""/>	
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