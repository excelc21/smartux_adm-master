<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/common.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>LG U+ IPTV SmartUX</title>
<link href="${pageContext.request.contextPath}/css/basic_style.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/css/anytime_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script src="${pageContext.request.contextPath}/js/anytime.js"></script>
<script type="text/javascript">;
	$(document).ready(function(){
		var Today = new Date();
		AnyTime.picker('display_date1', { format: '%y.%m.%d', labelTitle: '날짜', labelHour: '', time: '', defaultYear: Today.getFullYear()});
		AnyTime.picker('display_date2', { format: '%m.%d', labelTitle: '날짜', labelHour: '', time: '', defaultYear: Today.getFullYear()});
		
		var callbak = '${callbak}';
		
		$("#sForm").ajaxForm({
			dataType: 'json', 
        	beforeSubmit: function() {
        		if($("#message").val()==""){
    				alert("문구를 입력해 주세요.")
    				$("#message").focus();
    				return false;
    			}
        		
        		if ('false' == checkByteMessage($("#message").val(), 1200, 2)) {
    				alert('문구는 1200Byte 이내로 입력해야 합니다.');
    				$("#message").focus();
    				return false;
    			}
    			
    			if($("#display_time").val()==""){
    				alert("노출시간을 입력해 주세요.")
    				$("#display_time").focus();
    				return false;
    			}
    			
    			if($("#display_time").val()==""){
    				alert("노출시간을 입력해 주세요.")
    				$("#display_time").focus();
    				return false;
    			}
    			
    			if($("#display_date").val()==""){
    				alert("날짜를 입력해 주세요.")
    				$("#display_date"+$("#display_type option:selected").attr("name")).focus();
    				return false;
    			}
    			
    			if ('false' == checkByteMessage($("#speaker").val(), 30, 2)) {
    				alert('화자는 30Byte 이내로 입력해야 합니다.');
    				$("#speaker").focus();
    				return false;
    			}
    			
    			if ('false' == checkByteMessage($("#etc").val(), 100, 2)) {
    				alert('추가내용은 100Byte 이내로 입력해야 합니다.');
    				$("#etc").focus();
    				return false;
    			}
            }, 
   			beforeSend : function(data) {
   				$.blockUI({
   					blockMsgClass: "ajax-loading",
   					showOverlay: true,
   					overlayCSS: { backgroundColor: '#CECDAD' } ,
   					css: { border: 'none' } ,
   					 message: "<b>로딩중..</b>"
   				});
   			},
            success: function(data, status){
				if("success"==status){
					if("0000"==data.flag){
						alert('정상 수정되었습니다.');
						(opener.window[callbak]||function(){})();
						self.close();
					}
					else alert(data.message);
				}else{
					alert('수정에 문제가 생겼습니다.')
				}
            }, 
   			complete : function() {
   				$.unblockUI();
   			},
            error: function(xhr, textStatus, errorThrown) {
            	alert('수정을 실패하였습니다.')
            }                              
        });
		
		$('#closeBtn').click(function(){
			self.close();
		});
		
		$('.dpDate').change(function(){
			$("#display_date").val(this.value);
		});
		
		$("#display_type").change(function(){
			$("#display_date").val("");
			$("#display_date1").val("");
			$("#display_date2").val("");
			if($("#display_type option:selected").attr("name")=="1"){
				$("#trDate2").hide();
				$("#trDate1").show();
			}else{
				$("#trDate1").hide();
				$("#trDate2").show();
			}
		});

		$(".numeric").keypress(function(e){
			if(e.which && (e.which > 47 && e.which < 58 || e.which==8)){
			}else{
				e.preventDefault();
			}
		});
		$(".numeric").css("ime-mode","disabled");
		$(".nohangul").css("ime-mode","disabled");
	});
</script>
<body leftmargin="0" topmargin="0">
	<div id="divBody" style="height: 100%">
		<table border="0" cellpadding="0" cellspacing="0" height="100%"
			width="100%">
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
														<table border="0" cellpadding="15" cellspacing="0"
															width="100%" align="center">
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
																													<td class="bold">일별정보 수정</td>
																												</tr>
																											</tbody>
																										</table>
																									</td>
																								</tr>
																							</tbody>
																						</table>
																						<form id="sForm" name="sForm" method="POST" action="./updateDayInfoProc.do">
																							<table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_data">
																								<tbody>
																									<tr>
																										<th width="25%">문구</th>
																										<td width="2%">&nbsp;</td>
																										<td align="left">
																											<input type="text" name="message" id="message" style="font-size: 12px;width:95%" maxlength="1200" value="${vo.message}"/>	
																											<input type="hidden" id="display_date" name="display_date" value="<c:if test="${vo.display_year !='00'}">${vo.display_year}.</c:if>${fn:substring(vo.display_date,0,2)}.${fn:substring(vo.display_date,2,4)}">
																											<input type="hidden" id="seq" name="seq" value="${vo.seq}">
																										</td>
																									</tr>
																									<tr>
																										<th width="25%">타입</th>
																										<td width="2%">&nbsp;</td>
																										<td align="left">
																											<select class="select" id="display_type" name="display_type">
																	                                    		<option value="1" name="1" <c:if test="${vo.display_type =='1'}">selected="selected"</c:if>>절기</option>
																	                                    		<option value="2" name="1" <c:if test="${vo.display_type =='2'}">selected="selected"</c:if>>절일</option>
																	                                    		<option value="3" name="1" <c:if test="${vo.display_type =='3'}">selected="selected"</c:if>>잡절</option>
																	                                    		<option value="4" name="2" <c:if test="${vo.display_type =='4'}">selected="selected"</c:if>>명언</option>
																	                                    		<option value="5" name="2" <c:if test="${vo.display_type =='5'}">selected="selected"</c:if>>아름다운 우리말</option>
																	                                    		<option value="6" name="2" <c:if test="${vo.display_type =='6'}">selected="selected"</c:if>>명언2</option>
															                                                </select>
																										</td>
																									</tr>
																									<tr>
																										<th width="25%">노출시간</th>
																										<td width="2%">&nbsp;</td>
																										<td align="left">
																											<input type="text" name="display_time" id="display_time" style="font-size: 12px;width:40px;text-align:right;" class="numeric" maxlength="4" value="${vo.display_time}"/>
																										</td>
																									</tr>
																									<tr id="trDate1" <c:if test="${vo.display_year =='00'}">style="display:none;"</c:if>>
																										<th width="25%">날짜1</th>
																										<td width="2%">&nbsp;</td>
																										<td align="left">
																											<input type="text" id="display_date1" name="display_date1" size="10" style="text-align: center" class="dpDate" value="${vo.display_year}.${fn:substring(vo.display_date,0,2)}.${fn:substring(vo.display_date,2,4)}">
																										</td>
																									</tr>
																									<tr id="trDate2" <c:if test="${vo.display_year !='00'}">style="display:none;"</c:if>>
																										<th width="25%">날짜</th>
																										<td width="2%">&nbsp;</td>
																										<td align="left">
																											<input type="text" id="display_date2" name="display_date2" size="10" style="text-align: center" class="dpDate" value="${fn:substring(vo.display_date,0,2)}.${fn:substring(vo.display_date,2,4)}">
																										</td>
																									</tr>
																									<tr>
																										<th width="25%">화자</th>
																										<td width="2%">&nbsp;</td>
																										<td align="left">
																											<input type="text" name="speaker" id="speaker" style="font-size: 12px;width:120px" value="${vo.speaker}"/>	
																										</td>
																									</tr>
																									<tr>
																										<th width="25%">추가내용</th>
																										<td width="2%">&nbsp;</td>
																										<td align="left">
																											<input type="text" name="etc" id="etc" style="font-size: 12px;width:95%" value="${vo.etc}"/>	
																										</td>
																									</tr>
																								</tbody>
																							</table>
																							<table border="0" cellpadding="0" cellspacing="0" width="100%" align="left">
																								<tbody>
																									<tr>
																										<td height="40" align="right"><input type="submit" value="수정" class="button small blue"/> 
																										<span class="button small blue" id="closeBtn">닫기</span>&nbsp;&nbsp;
																										</td>
																									</tr>
																								</tbody>
																							</table>
																						</form>

																						<table border="0" cellpadding="0" cellspacing="0"
																							width="100%">
																							<tbody>
																								<tr>
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
														</table> <!-- ########################### body end ########################## -->
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