<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<title>LG U+ HDTV</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<link href="/smartux_adm/css/anytime_style.css" rel="stylesheet" type="text/css">

<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script src="/smartux_adm/js/anytime.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	$('#sumitBtn').click(function(){

		if(!onlyEngNumber($("#access_info_id").val())){
			alert("약관 항목 ID는 영어,숫자 3자리로 입력해야 합니다.");
	 		$("#access_info_id").focus();
	  		return;
	  	}
		
		if(!$("#access_title").val()) {
			alert("타이틀을 입력해주세요.");
	 		$("#access_title").focus();
	  		return;
		}
		
		if(confirm('저장하시겠습니까?')){
			try{
				$.blockUI({
					blockMsgClass: "ajax-loading",
					showOverlay: true,
					overlayCSS: { backgroundColor: '#CECDAD' } ,
					css: { border: 'none' } ,
					 message: "<b>처리중..</b>"
				});
			
				var url = '';
				var access_info_id = '${accessInfo.access_info_id}';
				
				if($('#isUpdate').val() == '0'){
					access_info_id = $('#access_info_id').val();
					url = "./insertProcInfo.do";
				}else{
					url = "./updateProcInfo.do";
				}
				
				$.ajax({
					url: url,
					type: "GET",
					data: { 
							"access_info_id" : access_info_id,
							"access_title" : $('#access_title').val(),
							"required_yn" : $('#required_yn').val(),
							"access_version" : $('#access_version').val(),
							"display_yn" : $('#display_yn').val(),
							<c:if test="${isUpdate eq '1'}">
							"version_update_yn" : $('#version_update_yn').val(),
							</c:if>
							"etc" : $('#etc').val()
						  },
					dataType : "json",
					success: function (rtn) {
						if (rtn.res=="0000") {
							alert("정상처리 되었습니다.");
							$(location).attr('href',"./getAccessInfoList.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}");
						} else {
							alert("작업 중 오류가 발생하였습니다\nflag : " + rtn.res + "\ Message : " + rtn.msg);
						}
						$.unblockUI();
					},
					error: function (e) {					
						alert("일시적인 장애가 발생하였습니다. 다시 시도해 주세요.");
						$.unblockUI();
					}
				});
			}catch(e){
				alert("작업 중 오류가 발생하였습니다");
			}
		}
	});
	
	$('#delBtn').click(function(){
		var checkboxarray = new Array();
		var tmp;
		checkboxarray.push("${accessInfo.access_info_id}");
		
		if(confirm("약관 항목을 삭제 하시겠습니까?")){
			
			$.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>로딩중..</b>"
			});	
			
			$.get("./deleteProcInfo.do", 
				 {access_info_ids : checkboxarray},
				  function(data) {
					 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
					 	var flag = data.res;
					 	var message = data.msg;
					 	
					 	if(flag == "0000"){						// 정상적으로 처리된 경우
					 		alert("해당 약관 항목이 삭제 처리되었습니다");
					 		$.unblockUI();
					 		$(location).attr('href',"./getAccessInfoList.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}");
					 	}else{
					 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
					 		location.reload();
					 	}
				  },
				  "json"
		    );
		}
	});
	
	$('#detailBtn').click(function(){
		url = "<%=webRoot%>/admin/terms/getAccessDetailListPop.do?callback=getAccessDetailListPopCallBack&access_info_id=${accessInfo.access_info_id}";
	   	g_window = window.open(url, "getAccessDetailListPop", "width=800,height=550,left=10,top=10,scrollbars=yes");
	});
	
});



function getAccessDetailListPopCallBack(data){
}

//숫자 입력
function onlyEngNumber(val){
	var reg =  /^[A-Za-z0-9]{3}$/i;
	var rst = reg.test(val);
	return rst;
}
</script>
<body leftmargin="0" topmargin="0">
<div id="divBody" style="height:100%">
<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
    <tbody>
      <tr>
        <td colspan="2" height="45" valign="bottom">
       		 <!-- top menu start -->
			<%@include file="/WEB-INF/views/include/top.jsp" %>
            <!-- top menu end -->
	   </td>
	  </tr>
	  <tr>
        <td height="10"></td>
        <td></td>
      </tr>
      <tr>
      <td colspan="2" valign="top">
        <table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
        <tbody>
        <tr>
            <td width="4"></td>
            <td valign="top" width="180">
      		<!-- left menu start -->
      		<%@include file="/WEB-INF/views/include/left.jsp" %>
      		<!-- left menu end -->
            </td>
			<td background="/smartux_adm/images/admin/bg_02.gif" width="35">&nbsp;</td>
            <td valign="top">
            <table border="0" cellpadding="0" cellspacing="0" width="98%">
                <tbody>
                <tr>
                    <td height="42" width="100%">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tbody>
                            <tr>
                                <td width="300" class="boldTitle">
									약관 관리
                                </td>
                            </tr>
                        	</tbody>
                        </table>
                    </td>
                </tr>
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
						                                            <td class="bold">
						                                            	약관 항목 <c:if test="${isUpdate eq '0'}">등록</c:if><c:if test="${isUpdate eq '1'}">수정</c:if>
						                                            </td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
												<form id="form1" name="form1" action="./insertProc.do" method="get">
						                        <input type="hidden" id="isUpdate" name="isUpdate" value="${isUpdate }"/>
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" class="board_data">
					                                <tbody>				                             	
						                                <tr align="center">
						                                    <th width="25%">약관 항목 ID</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<input type="text" name="access_info_id" id="access_info_id" size="10" maxlength="3" value="${accessInfo.access_info_id}" style="font-size: 12px;" onKeyUp="checkByte($(this),'3')"<c:if test="${isUpdate eq '1'}"> disabled</c:if>>
																<c:if test="${isUpdate eq '1'}">
																	<span class="button small blue" id="detailBtn">상세</span>
																</c:if>		
															</td>
														 </tr>
														<c:if test="${isUpdate eq '1'}">
														<tr align="center">
															<th width="25%">약관 항목 번호</th>
															<td width="5%"></td>								                                    
															<td width="70%" align="left" >
																${accessInfo.access_detail_id}
															</td>									
														</tr>
														</c:if>
						                                <tr align="center">
						                                    <th width="25%">타이틀</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" style="word-break:break-all;">
																<input type="text" name="access_title" id="access_title" size="75" maxlength="100" value="${accessInfo.access_title}" style="font-size: 12px;" onKeyUp="checkByte($(this),'100')">
															</td>
														 </tr>
														 <tr align="center">
						                                    <th width="25%">필수여부</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
						                                    	<select class="select" id="required_yn" name="required_yn">
						                                    		<option value="Y" <c:if test="${accessInfo.required_yn eq 'Y'}">selected="selected"</c:if>>Y</option>
						                                    		<option value="N" <c:if test="${accessInfo.required_yn eq 'N'}">selected="selected"</c:if>>N</option>
						                                    	</select>	
															</td>
														 </tr>
														 <c:if test="${isUpdate eq '1'}">
														 <tr align="center">
						                                    <th width="25%">버전갱신여부</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
						                                    	<select class="select" id="version_update_yn" name="version_update_yn">
						                                    		<option value="N">N</option>
						                                    		<option value="Y">Y</option>
						                                    	</select>
															</td>
														 </tr>
														 </c:if>														 
														 <tr align="center">
						                                    <th width="25%">버전</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<input type="text" name="access_version" id="access_version" size="25" maxlength="10" value="${accessInfo.access_version}" style="font-size: 12px;" disabled>	
															</td>
														 </tr>		
						                                <tr align="center">
						                                    <th width="25%">동의설정</th> 
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
						                                    	<select class="select" id="display_yn" name="display_yn">
						                                    		<option value="Y" <c:if test="${accessInfo.display_yn eq 'Y'}">selected="selected"</c:if>>동의</option>
						                                    		<option value="N" <c:if test="${accessInfo.display_yn eq 'N'}">selected="selected"</c:if>>미동의</option>
						                                    	</select>												 							
															</td>
						                                </tr>
						                                <tr align="center">
															<th width="25%">비고</th> 
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" style="word-break:break-all;">
																<input type="text" name="etc" id="etc" size="75" maxlength="512" value="${accessInfo.etc}" style="font-size: 12px;" onKeyUp="checkByte($(this),'512')">															
															</td>
														</tr>
														 <c:if test="${isUpdate eq '1'}">
															 <tr align="center">
							                                    <th width="25%">등록일</th>
							                                    <td width="5%"></td>
							                                    <td width="70%" align="left" >
							                                    	<c:out value="${accessInfo.reg_dt}"/>
																</td>
															 </tr>
															 <tr align="center">
							                                    <th width="25%">수정일</th>
							                                    <td width="5%"></td>
							                                    <td width="70%" align="left" >
							                                    	<c:out value="${accessInfo.mod_dt}"/>
																</td>
															 </tr>
														 </c:if>
													</tbody>
					                            </table>
					                            
					                            <!-- 등록/수정 버튼 -->
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<c:if test="${isUpdate eq '0'}">
						                                		<span class="button small blue" id="sumitBtn">등록</span>
						                                	</c:if>
						                                	<c:if test="${isUpdate eq '1'}">
						                                		<span class="button small blue" id="sumitBtn">수정</span>
						                                		<span class="button small blue" id="delBtn">삭제</span>
						                                	</c:if>
						                                	<a href="./getAccessInfoList.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}"><span class="button small blue">목록</span></a>
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
	  <tr>
	    <td height="30">&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr>
	    <td colspan="2" background="/smartux_adm/images/admin/copy_bg.gif" height="60" align="left">
	        <!-- 하단 로그인 사용자 정보 시작 -->
	        <%@include file="/WEB-INF/views/include/bottom.jsp" %>
	        <!-- 하단 로그인 사용자 정보 종료 -->
	    </td>
	 </tr>
</tbody>
</table>
</div>
</body>
</html>