<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EDGE" />
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">

	var processJob = false;
	
	$(document).ready(function(){
		
		// ajax 작업시 캐쉬를 사용하지 않도록 한다
		$.ajaxSetup({ cache: false });
		
		// 채널 설정
		$("#chnlsearchbtn").click(function(){
			var url = "<%=webRoot%>/admin/gpack/event/getGpackPromotionChannelView.do";
			window.open(url, "viewchannel", "width=700,height=540");
		});
		
		$("#regfrm").ajaxForm({
			dataType:  "json", 
			url: '<%=webRoot%>/admin/gpack/event/updatePromotion.do',
			beforeSubmit : checkForm,
	        success : function(data){
	        	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
	        	
	        	var flag = data.flag;
				var message = data.message;

                if ('0000' == flag) {
                	opener.location.reload();
					alert("프로모션이 등록되었습니다");
					self.close();
                } else {
			 		alert("작업 중 오류가 발생하였습니다\nmessage : " + message);
                }
	        	processJob = false;
	        },
	        error:function (error) {
	        	alert('작업 중 오류가 발생하였습니다');
	        	processJob = false;
            }
		});
		
		$("#regfrm").submit(function(){
			return false;	
		});
		
		$("#regbtn").click(function(){
			if(processJob == false){
				processJob = true;
				$("#regfrm").submit();
			}else{
				alert("현재 작업중입니다. 잠시만 기다려주세요");
			}
		});
		
		$("#resetbtn").click(function(){
			$("#regfrm")[0].reset();
		});
	});
	
	//입력체크
	function checkForm(){
		var category_nm = $("#category_nm").val();
		
		if(category_nm == ""){
			alert("등록하고자 하는 타이틀을 입력해주세요");
			$("#category_nm").focus();
        	processJob = false;
			return false;
		}
		return true;
	}
</script>
</head>

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
						                                        <c:if test="${result ne null}"><c:if test="${result.category_nm ne ''}">
						                                        	<td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
						                                            <td width="100px" style="vertical-align: top;overflow:hidden;white-space:nowrap;" class="bold">${result.category_nm}</td>
						                                        </c:if></c:if>
						                                            <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
						                                            <td width="*" class="bold">프로모션 등록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="regfrm" name="regfrm" method="post" action="<%=webRoot%>/gpack/pack/insertPack.do">
					                            <input type="hidden" id="pack_id" name="pack_id" value="${pack_id}"/>
						                        <input type="hidden" id="category_id" name="category_id" value="${result.category_id}"/>
						                        <input type="hidden" id="promotion_chnl" name="promotion_chnl" value="${result.promotion_chnl}"/>
						                        <input type="hidden" id="old_promotion_video_gb" name="old_promotion_video_gb" value="${result.promotion_video_gb}"/>
						                        <input type="hidden" id="old_use_yn" name="old_use_yn" value="${result.use_yn}"/>
						                        <input type="hidden" id="smartUXManager" name="smartUXManager" value="<%=id_decrypt %>" />
						                        
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">타이틀</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<input type="text" id="category_nm" name="category_nm" size="35" style="font-size: 12px;" value="${result.category_nm}"/>			
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">멘트</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<input type="text" name="category_comment" size="50" style="font-size: 12px;" value="${result.category_comment}"/>
														</td>
					                                </tr>
			                                <c:if test="${packVO.template_type eq 'TP002'}">
			                                	<c:choose>
													<c:when test="${editVideoGb eq 'true'}">
					                                <tr align="center">
					                                    <th width="25%">영상 구분</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
																<input type="radio" id="promotion_video_type1" name="promotion_video_gb" value="PV001" <c:if test="${result.promotion_video_gb eq 'PV001' }">checked="checked"</c:if> /><label for="promotion_video_type1">채널</label>
																<input type="radio" id="promotion_video_type2" name="promotion_video_gb" value="PV002" <c:if test="${result.promotion_video_gb eq 'PV002' }">checked="checked"</c:if> /><label for="promotion_video_type2">플레이리스트</label>
																<input type="radio" id="promotion_video_type3" name="promotion_video_gb" value="PV003" <c:if test="${result.promotion_video_gb ne 'PV001' and result.promotion_video_gb ne 'PV002'}">checked="checked"</c:if> /><label for="promotion_video_type3">영상없음</label>														
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">채널</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<input type="text" id="promotion_chnl_info" name="promotion_chnl_info" disabled="disabled" size="35" value="${result.promotion_chnl_info}"/>
					                                    	<span class="button small blue" id="chnlsearchbtn">설정</span>
														</td>
					                                </tr>
					                                </c:when>
													<c:otherwise>
													<tr align="center">
					                                    <th width="25%">영상 구분</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    		<input type="radio" id="promotion_video_type1" name="promotion_video_gb" value="PV001" disabled="disabled"/><label for="promotion_video_type1">채널</label>
																<input type="radio" id="promotion_video_type2" name="promotion_video_gb" value="PV002" disabled="disabled" /><label for="promotion_video_type2">플레이리스트</label>
																<input type="radio" id="promotion_video_type3" name="promotion_video_gb" value="PV003" checked="checked" /><label for="promotion_video_type3">영상없음</label>															
														</td>
					                                </tr>
			                                		</c:otherwise>
												</c:choose>	
			                                </c:if>
			                                <c:if test="${packVO.template_type eq 'TP001'}">
			                                	<input type="hidden" id="promotion_video_gb" name="promotion_video_gb" value="PV003"/>
			                                </c:if>
					                                <tr align="center">
					                                    <th width="25%">자동/수동</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="radio" id="auto_ynY" name="auto_yn" value="Y" <c:if test="${result.auto_yn eq 'Y' }">checked="checked"</c:if> /><label for="auto_ynY">자동</label>
															<input type="radio" id="auto_ynN" name="auto_yn" value="N" <c:if test="${result.auto_yn ne 'Y' }">checked="checked"</c:if> /><label for="auto_ynN">수동</label>							
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">사용여부</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="radio" id="use_ynY" name="use_yn" value="Y" <c:if test="${result.use_yn eq 'Y' }">checked="checked"</c:if> /><label for="use_ynY">예</label>
															<input type="radio" id="use_ynN" name="use_yn" value="N" <c:if test="${result.use_yn ne 'Y' }">checked="checked"</c:if> /><label for="use_ynN">아니오</label>							
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