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
	$("#upbtn").click(function(){
		var frame_nm			= $("#frame_nm").val();
		var status 				= $(':input:radio[name="status"]:checked').val();
		var check 				= false;

		if(frame_nm == ""){
			alert("수정하고자 하는 패널UI타입 명을 입력해주세요");
			$("#frame_nm").focus();
			return;
		}else if(checkByte(frame_nm) > 200) {
			alert("Title은 200바이트까지 입력 가능합니다.");
			return;
		}
		if ('' == $('#img_file').val()){
			check = validate(frame_nm, status);
			if(check){
				check = confirm('이미지 변경 없이 수정을 진행합니다.');
			}
		}else{
			if($('#img_file').val() != ""){
				var imgval = $("#img_file").val();
				if(!imgval.toLowerCase().match(/.(gif|jpg|png|jpeg|bmp)$/i)){
					alert("이미지 파일만 입력 가능합니다.");
					return;
				}
			}
			
			check = confirm($('#img_file').val() + '\n상기 이미지를 포함하여  수정 진행합니다.');
			$("#use_yn").val(status);
		}	
		
		if(check){
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
						alert("정상적으로 수정 되었습니다.");
						self.close(); 
					}else{
						alert(data.message);
						$.unblockUI();
					}
				},
				error:function(){
					alert("정상적으로 수정되지 않았습니다.\n재시도 하시기 바랍니다.");
					$.unblockUI();
				}
			};
			$("#multiform").ajaxSubmit(option);
		}else{
			$("#frame_nm").focus();
		}
	});
	
	$("#resetbtn").click(function(){
		self.close();
	});
	
	function validate(frame_nm, use_yn){
		var frame_nm_old 	= $("#frame_nm_old").val();
		var use_yn_old 		= $("#use_yn").val();
		
		var result			= false; 		
		if(frame_nm == frame_nm_old && use_yn == use_yn_old){
			alert("변경 사항이 없습니다.");
		}else{
			$("#use_yn").val(use_yn);
			result = true;
		}
		return result;
	}
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
<!-- 2019.11.04 : 패널UI타입 상세조회 -> UI타입 상세조회 으로 수정 - 이태광 Start -->						                                            
						                                            <td class="bold">UI타입 상세조회</td>
<!-- 2019.11.04 : 패널UI타입 상세조회 -> UI타입 상세조회 으로 수정 - 이태광 End -->						                                            
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="multiform" name="multiform" method="post" action="./updatePanelUiType.do" enctype="multipart/form-data">
					                            <table border="0" cellpadding="0" cellspacing="0" width="500" class="board_data">
					                                <tbody>
					                                <tr id="trpanel_nm" align="center">
					                                    <th width="30%">Title</th>
					                                    <td width="5%"></td>
					                                    <td width="65%" align="left" >
															<input type="text" id="frame_nm" name="frame_nm" size="35" maxlength="200" style="font-size: 12px;" value="${result.frame_nm}"/>								
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="30%">Type 이미지(현재)</th>
					                                    <td width="5%"></td>
					                                    <td width="65%" align="left" >
															<c:if test="${result.img_file!=null}"> 
						                                    		<img src="${dir}${result.img_file}" 
						                                    			<%-- <c:if test="${result.frame_type == '10'}">height="140"</c:if>
						                                    			<c:if test="${result.frame_type == '20'}">height="350"</c:if> --%>
						                                    			<c:if test="${result.frame_type == '30'}">height="350"</c:if>
						                                    		  width="245"/>	
						                                    </c:if>					
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="30%">Type 이미지(수정)</th>
					                                    <td width="5%"></td>
					                                    <td width="65%" align="left" >
															<input type="file" id="img_file" name="img_file" value="파일선택"/>					
														</td>
					                                </tr>
<!-- 2019.09.09 : 타입 안보이게 제거 - 이태광 Start -->					                                
					                                <%-- <tr align="center">
					                                    <th width="30%">Type</th>
					                                    <td width="5%"></td>
					                                    <td width="65%" align="left" >
		                                                   <c:if test="${result.frame_type == '10'}">지면 타입</c:if>	
		                                                   <c:if test="${result.frame_type == '20'}">프레임 타입</c:if>
		                                                   <c:if test="${result.frame_type == '30'}">패널UI타입</c:if>				                                                    
														</td>
					                                </tr> --%>
<!-- 2019.09.09 : 타입 안보이게 제거 - 이태광 End -->					                                
				                                  	<tr align="center">
					                                    <th width="30%">사용여부</th>
					                                    <td width="5%"></td>
					                                    <td width="65%" align="left" >
															<input type="radio" id="use_ynY" name="status" value="Y" <c:if test="${result.use_yn == 'Y'}">checked</c:if>/>사용
															<input type="radio" id="use-ynN" name="status" value="N" <c:if test="${result.use_yn == 'N'}">checked</c:if>/>미사용
														</td>
					                                </tr>
					                            	</tbody>
					                            </table>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="400" align="center">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="center">
						                                	<span class="button small blue" id="upbtn">수정</span>	
						                                	<span class="button small blue" id="resetbtn">취소</span>	
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                       	<input type="hidden" id="frame_nm_old" name="frame_nm_old" value="${result.frame_nm}"/>
						                       	<input type="hidden" id="frame_type_code" name="frame_type_code" value="${result.frame_type_code}"/>
						                       	<input type="hidden" id="frame_type" name="frame_type" value="${result.frame_type}"/>
						                       	<input type="hidden" id="use_yn" name="use_yn" value="${result.use_yn}"/>	
						                       	<input type="hidden" id="old_file" name="old_file" value="${result.img_file}"/>
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