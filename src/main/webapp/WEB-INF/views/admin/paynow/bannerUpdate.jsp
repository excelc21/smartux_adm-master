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
		// 수정 버튼 클릭
		$('#regfrm').ajaxForm({
			dataType: 'json',
        	beforeSubmit: function() {
    			if($("#banner_img").val() == "" && $("#org_banner_img").val() == "" && $("#banner_text").val() == "") {
    				alert("배너 이미지 또는 안내 문구를 입력해 주세요.");
    				return false;
    			}
            },
            success: function(responseText){
            	var flag = responseText.flag;
		    	var message = responseText.message;
		    	if(flag == "0000") {
		    		opener.location.reload();
			 		alert("배너가 성공적으로 수정되었습니다.");
					self.close();
		    	} else {
			 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);    		
		    	}
            },
            error: function(){
            	alert("접속 할  수 없습니다.");
            }                               
        });
		
		$("#resetbtn").click(function(){
			$("#regfrm")[0].reset();
		});
	});
	
	function winOpen(imagePath, width, height) {
		window.open(imagePath, 'imgWin', 'toolbar=no,scrollbars=yes,resizable=no, top=20,left=20, width=' + width + ', height=' + height);
	}

	function deleteImage() {
		if (confirm('이미지를 삭제하시겠습니까?')) {
			$('#image').remove();
			$('#org_banner_img').val('');
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
						                                            <td class="bold">Paynow 배너 수정</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="regfrm" name="regfrm" method="post" action="./updateBannerProc.do" enctype="multipart/form-data">
					                            <table border="0" cellpadding="0" cellspacing="0" width="400" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">배너 ID</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >${vo.banner_id}
															<input type="hidden" id="banner_id" name="banner_id" value="${vo.banner_id}" />		
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">배너 이미지 URL</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left">
															<input type="file" id="banner_img" name="banner_img" accept="image/*" value="파일선택"/>
															<c:if test="${not empty vo.banner_img}">
																<input type="hidden" id="org_banner_img" name="org_banner_img" value="${vo.banner_img}"/>
																<div id="image">
																	<a href="javascript:winOpen('${vo.img_url}${vo.banner_img}','300','300')" style="font-weight:bold;text-decoration:underline">${vo.banner_img}</a>
																</div>
															</c:if>
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">안내 문구</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="text" id="banner_text" name="banner_text" size="35" maxlength="200" style="font-size: 12px;" value="${vo.banner_text}" />								
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">사용여부</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="radio" id="use_yn" name="use_yn" value="Y" <c:if test="${vo.use_yn == 'Y'}">checked</c:if>/>예
															<input type="radio" id="use_yn" name="use_yn" value="N" <c:if test="${vo.use_yn != 'Y'}">checked</c:if>/>아니오								
														</td>
					                                </tr>
					                            	</tbody>
					                            </table>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="400" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<input type="submit" value="수정" class="button small blue"/>
						                                	<span class="button small blue" id="resetbtn">재작성</span>	
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