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
	$("#service_type").change(function(){
		if($(this).val() == "U") {
			$('#display').removeAttr("disabled");
		} else if($(this).val() == "V") {
			$('#display').attr("disabled","disabled");
			$("#display option:eq(0)").attr("selected", "selected");
		}
	});
	
	$('#sumitBtn').click(function(){
		
		if(!$('#access_mst_title').val()){
			alert("약관 마스터 제목을 입력해주세요.");
			$("#access_mst_title").focus();
			return;
		} else {
		
			if(textareaCheck($("#access_mst_title").val())){
		 		var check=textareaCheck($("#access_mst_title").val())
		 		alert(check+"는 입력할수 없습니다."); 		
		 		$("#access_mst_title").focus();
		  		return;
		  	}
		}
		
		if(textareaCheck($("#header_text").val())){
	 		var check=textareaCheck($("#header_text").val())
	 		alert(check+"는 입력할수 없습니다."); 		
	 		$("#header_text").focus();
	  		return;
	  	}
		
		if(textareaCheck($("#footer_text").val())){
	 		var check=textareaCheck($("#footer_text").val())
	 		alert(check+"는 입력할수 없습니다."); 		
	 		$("#footer_text").focus();
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
				
				if($('#isUpdate').val() == '0'){
					url = "./insertProc.do";
				}else{
					url = "./updateProc.do";
				}
				
				$.ajax({
					url: url,
					type: "GET",
					data: { 
							"access_mst_id" : "${accessInfo.access_mst_id}",
							"service_type" : $('#service_type').val(),
							"display" : $('#display').val(),
							"access_mst_title" : $('#access_mst_title').val(),
							"header_text" : $('#header_text').val(),
							"footer_text" : $('#footer_text').val(),
							"is_adt" : 'N',
							"access_info_id" : $('#access_info_id').val()
						  },
					dataType : "json",
					success: function (rtn) {
						if (rtn.res=="0000") {
							alert("정상처리 되었습니다.");
							$(location).attr('href',"./getAccessList.do?display=${vo.display }&findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}");
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
		checkboxarray.push("${accessInfo.access_mst_id}");
		
		if(confirm("약관 마스터를 삭제 하시겠습니까?")){
			
			$.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>로딩중..</b>"
			});	
			
			$.get("./deleteProc.do", 
				 {access_mst_ids : checkboxarray},
				  function(data) {
					 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
					 	var flag = data.res;
					 	var message = data.msg;
					 	
					 	if(flag == "0000"){						// 정상적으로 처리된 경우
					 		alert("해당 약관 마스터가 삭제 처리되었습니다");
					 		$.unblockUI();
					 		$(location).attr('href',"./getAccessList.do?display=${vo.display }&findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}");
					 	}else{
					 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
					 		location.reload();
					 	}
				  },
				  "json"
		    );
		}
	});
	
	$('#agreeBtn').click(function(){
		url = "<%=webRoot%>/admin/terms/getAccessInfoListPop.do?callback=getAccessInfoListPopCallBack&access_mst_id=${accessInfo.access_mst_id}";
	   	g_window = window.open(url, "getAccessInfoListPop", "width=800,height=500,left=10,top=10,scrollbars=yes");
	});
});

function getAccessInfoListPopCallBack(data){
	for(var i=0; i<data.length; i++) {
		if(i==0){
			$('#access_info_id').val(data[i].id);	
			$('#access_info_titles').text('['+data[i].id+'] '+data[i].name);
			$('#access_info_titles').append('<br/>');
		} else {
			$('#access_info_id').val($('#access_info_id').val()+","+data[i].id);
			$('#access_info_titles').append('['+data[i].id+'] '+data[i].name);
			$('#access_info_titles').append('<br/>');
		}
	}
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
                                	<!-- 카테고리 타이틀 -->
                                    <!-- <img src="/smartux_adm/images/admin/category_map_title.gif">-->
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
						                                            	약관 마스터 <c:if test="${isUpdate eq '0'}">등록</c:if><c:if test="${isUpdate eq '1'}">수정</c:if>
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
						                                    <th width="25%">서비스타입</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<select class="select" id="service_type" name="service_type">
																	<c:forEach items="${serviceType}" var="item2">
                           												<option value="${item2.code}" <c:if test="${accessInfo.service_type eq item2.code}">selected="selected"</c:if>>${item2.name}</option>
                           											</c:forEach> 
																</select>	
															</td>
														 </tr>				                             	
						                                <tr align="center">
						                                    <th width="25%">노출화면</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
						                                    	<select class="select" id="display" name="display"<c:if test="${accessInfo.service_type eq 'V'}"> disabled</c:if>>
						                                    		<c:forEach items="${displayType}" var="item3">			
                           												<option value="${item3.code}" <c:if test="${accessInfo.display eq item3.code}">selected="selected"</c:if>>${item3.name}</option>
                           											</c:forEach>
						                                    	</select>
															</td>
														 </tr>
														 <tr align="center">
						                                    <th width="25%">약관 마스터 제목</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
						                                    	<input type="text" name="access_mst_title" id="access_mst_title" size="75" maxlength="100" value="${accessInfo.access_mst_title}" style="font-size: 12px;" onKeyUp="checkByte($(this),'100')">	
															</td>
														 </tr>		
														 <tr align="center">
						                                    <th width="25%">상단 텍스트</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
						                                    	<textarea rows="10" cols="40" name="header_text" id="header_text" onKeyUp="checkByte($(this),'512')" >${accessInfo.header_text}</textarea>
																<br>[\f, !^, \b, \f88, \f99]  사용제한 / 512byte 까지 허용	
															</td>
														 </tr>		
						                                <tr align="center">
						                                    <th width="25%">하단 텍스트</th> 
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
						                                    	<textarea rows="10" cols="40" name="footer_text" id="footer_text" onKeyUp="checkByte($(this),'512')" >${accessInfo.footer_text}</textarea>
																<br>[\f, !^, \b, \f88, \f99]  사용제한 / 512byte 까지 허용												 							
															</td>
						                                </tr>
						                                <%-- <tr align="center">
															<th width="25%">검수용</th> 
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
						                                    	<select class="select" id="is_adt" name="is_adt">
						                                    		<option value="N" <c:if test="${accessInfo.is_adt eq 'N'}">selected="selected"</c:if>>N</option>
						                                    		<option value="Y" <c:if test="${accessInfo.is_adt eq 'Y'}">selected="selected"</c:if>>Y</option>
						                                    	</select>																
															</td>
														</tr> --%>
						                                <tr align="center" >
						                                    <th width="25%">약관항목</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" style="word-break:break-all;">								                                  
						                                    	<span id="access_info_titles">
							                                    	<c:forEach items="${access_info_titles}" var="item6">
							                                    		[${item6.access_info_id}] ${item6.access_title}<br/>
							                                    	</c:forEach>
						                                    	</span>
						                                    	<span class="button small blue" id="agreeBtn">약관항목</span><br/>
						                                    	<input type="hidden" id="access_info_id" name="access_info_id" value="${access_info_ids}">					
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
						                                	<a href="./getAccessList.do?display=${vo.display }&findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}"><span class="button small blue">목록</span></a>
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