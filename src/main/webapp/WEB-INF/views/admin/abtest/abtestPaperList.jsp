<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>

<script type="text/javascript">
$(document).ready(function () {
	
	var radioId = $('input[name=rd_abms]:checked').val();
	var radioNm = $("label[for='"+radioId+"']").text(); 
	$('#ab_paper_info').text(radioNm + '(${mims_id})');
	
	//variation_id 선택
	$('input[name=rd_abms]').click(function () {
		$('#variation_id').val($('input[name=rd_abms]:checked').val());
		$('#form').submit();
	});
	
	//목록 버튼
	$('#listBtn').click(function () {
		function_list();
	});
	
	//패널 복사
	$('#replaceBtn').click(function () {
		function_replace();
	});
	
	//지면 순서변경
	$('#changeOrdBtn').click(function () {
		function_change_ord();
	});
	
	//지면 추가
	$('#insertBtn').click(function () {
		function_insert();
	});
	
	//지면 삭제
	$('#deleteBtn').click(function () {
		function_delete();
	});
	
	//즉시 적용
	$('#applyBtn').click(function () {
		function_apply();
	});
	
	//최종 완료
	$('#finishBtn').click(function () {
		function_finish();
	});
	
});

//목록 이동
function function_list(){
	location.href = '/smartux_adm/admin/abtest/getABTestList.do?findName=${findName}&findValue=${findValue}&pageNum=${pageNum}';
}

//패널 복사
function function_replace() {
	url = '/smartux_adm/admin/abtest/popPanelList.do?callback=selectPanelPopCallback'; 
	mims_window = window.open(url, "getMimsPop", "width=700,height=500,left=10,top=10,scrollbars=yes");
}

//패널 복사 callback
function selectPanelPopCallback(dataobj){
	/* if(confirm('선택한 패널 ('+ dataobj.id + ') 의 지면들로 변경하시겠습니까?')){ */
		$.ajax({
			url:  "/smartux_adm/admin/abtest/updateABTestPaper.do",
			type: "POST",
			data: { 
				'new_panel_id' : dataobj.id,
				'mims_id' : $('#mims_id').val(),
				'variation_id' : $('#variation_id').val()
			},
			dataType : "json",
			success: function (rtn) {
				if (rtn.flag=="0000") {
					alert("등록 성공");
					location.href = '/smartux_adm/admin/abtest/getABTestPaperList.do?findName=' + $('#findName').val() 
							+ '&findValue=' + $('#findValue').val() + '&pageNum=' + $('#pageNum').val() 
							+ '&test_id=' + $('#test_id').val() + '&variation_id=' + $('#variation_id').val()
							+ '&mims_id=' + $('#mims_id').val() + '&org_mims_id=' + $('#org_mims_id').val();
				} else {
					alert("작업 중 오류가 발생하였습니다\nflag : " + rtn.flag + "\ Message : " + rtn.message);
				}
			},
			error: function () {					
				alert("일시적인 장애가 발생하였습니다. 다시 시도해 주세요.");
			}
		});
	/* } */
}

//지면 순서변경
function function_change_ord() {
	url = '/smartux_adm/admin/abtest/popUpdatePaperOrder.do?variation_id=' + $('#variation_id').val() + '&mims_id=' + $('#mims_id').val(); 
	mims_window = window.open(url, "getMimsPop", "width=400,height=500,left=10,top=10,scrollbars=yes");
}

//지면 추가
function function_insert() {
	url = '/smartux_adm/admin/abtest/popInsertABTestPaper.do?variation_id=' + $('#variation_id').val() + '&mims_id=' + $('#mims_id').val();
	mims_window = window.open(url, "getMimsPop", "width=550,height=600,left=10,top=10,scrollbars=yes");
}

//지면 삭제
function function_delete() {
	var msg = '지면을 삭제 하시겠습니까?';
	if(confirm(msg)){
		
		if(!$(".checkbox").is(":checked")){
			alert("삭제 할 항목을 선택하세요.");
			return;
		}else{
			
			var chk_array = new Array();
			 $('input:checkbox[name="chk"]').each(function() {
			 	if(this.checked){
			 		var chk_val = $(this).val();
			 		chk_array.push(chk_val);
			 	}
			 });
				 
			var chk_array_str = chk_array.join('|');
			
			$.ajax({
				url:  "/smartux_adm/admin/abtest/deleteABTestPaper.do",
				type: "POST",
				data: { 
					'mims_id' : $('#mims_id').val(),
					'title_id_arr_str' : chk_array_str
				},
				dataType : "json",
				success: function (rtn) {
					if (rtn.flag=="0000") {
						alert("삭제 성공");
						location.href = '/smartux_adm/admin/abtest/getABTestPaperList.do?findName=' + $('#findName').val() 
								+ '&findValue=' + $('#findValue').val() + '&pageNum=' + $('#pageNum').val() 
								+ '&test_id=' + $('#test_id').val() + '&variation_id=' + $('#variation_id').val()
								+ '&mims_id=' + $('#mims_id').val() + '&org_mims_id=' + $('#org_mims_id').val();
					} else {
						alert("작업 중 오류가 발생하였습니다\nflag : " + rtn.flag + "\ Message : " + rtn.message);
					}
				},
				error: function () {					
					alert("일시적인 장애가 발생하였습니다. 다시 시도해 주세요.");
				}
			});
		}
	}
}

//지면정보
function function_view_title(panel_id, title_id, test_type){
	var url = "<%=webRoot%>/admin/abtest/popUpdatePaperInfo.do?panel_id=" + panel_id + "&title_id=" + title_id + "&variation_id=" + $('#variation_id').val() + "&abmsUptFlag=" + $('#abmsUptFlag').val()+"&test_type="+ test_type;
	window.open(url, "function_view_title", "width=500,height=400,scrollbars=yes");
}

//지면데이터정보
function function_view_category(panel_id, title_id, category_id, category_type, album_cnt, category_gb, test_type){
	var url = "<%=webRoot%>/admin/abtest/popUpdatePaperDataType.do?panel_id=" + panel_id + "&title_id=" + title_id + "&category_id=" + category_id + "&category_type=" + category_type + "&album_cnt=" + album_cnt + "&category_gb=" +category_gb + "&variation_id=" + $('#variation_id').val() + "&abmsUptFlag=" + $('#abmsUptFlag').val()+"&test_type="+ test_type; 
	window.open(url, "function_view_category", "width=700,height=600,scrollbars=yes");
}

//즉시적용
function function_apply() {
	var msg = '해당 Variation_id 하위 지면을 즉시적용 하시겠습니까?';
	if(confirm(msg)){
		$.ajax({
			url:  "/smartux_adm/admin/abtest/applyABTestPaperCache.do",
			type: "POST",
			data: { 
				'panel_id' : $('#mims_id').val(),
				'test_id' : $('#test_id').val(),
				'variation_id' : $('#variation_id').val(),
				'org_mims_id' : $('#org_mims_id').val(),
				'd_mims_id' :$('#d_mims_id').val()
			},
			dataType : "json",
			success: function (rtn) {
				if (rtn.flag=="0000") {
					alert("즉시적용 성공");
					location.href = '/smartux_adm/admin/abtest/getABTestPaperList.do?findName=' + $('#findName').val() 
							+ '&findValue=' + $('#findValue').val() + '&pageNum=' + $('#pageNum').val() 
							+ '&test_id=' + $('#test_id').val() + '&variation_id=' + $('#variation_id').val()
							+ '&mims_id=' + $('#mims_id').val() + '&org_mims_id=' + $('#org_mims_id').val();
				} else {
					alert("작업 중 오류가 발생하였습니다\nflag : " + rtn.flag + "\ Message : " + rtn.message);
				}
			},
			error: function () {					
				alert("일시적인 장애가 발생하였습니다. 다시 시도해 주세요.");
			}
		});
	}
}

//최종등록
function function_finish() {
	var msg = '최종등록 하시겠습니까?';
	if(confirm(msg)){
		$.ajax({
			url:  "/smartux_adm/admin/abtest/finishABTestPaper.do",
			type: "POST",
			data: { 
				'test_id' : $('#test_id').val()
			},
			dataType : "json",
			success: function (rtn) {
				if (rtn.flag=="0000") {
					alert("최종등록 성공");
					location.href = '/smartux_adm/admin/abtest/getABTestPaperList.do?findName=' + $('#findName').val() 
							+ '&findValue=' + $('#findValue').val() + '&pageNum=' + $('#pageNum').val() 
							+ '&test_id=' + $('#test_id').val() + '&variation_id=' + $('#variation_id').val()
							+ '&mims_id=' + $('#mims_id').val() + '&org_mims_id=' + $('#org_mims_id').val();
				} else {
					alert("작업 중 오류가 발생하였습니다\nflag : " + rtn.flag + "\ Message : " + rtn.message);
				}
			},
			error: function () {					
				alert("일시적인 장애가 발생하였습니다. 다시 시도해 주세요.");
			}
		});
	}
}

</script>
<body leftmargin="0" topmargin="0">
<div id="divBody" style="height:100%">
<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
	<tbody>
		<tr>
			<td colspan="2" height="45" valign="bottom">
			<%@include file="/WEB-INF/views/include/top.jsp" %>
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
            				<%@include file="/WEB-INF/views/include/left.jsp" %>
            			</td>
						<td background="/smartux_adm/images/admin/bg_02.gif" width="35">&nbsp;</td>
            			<td valign="top">
            				<table border="0" cellpadding="0" cellspacing="0" width="98%">
                				<tbody>
                					<tr style="display:block">
                    					<td height="42" width="100%">
                        					<table border="0" cellpadding="0" cellspacing="0" width="100%">
                            					<tbody>
						                            <tr>
						                                <td width="300" class="boldTitle">
												            <a href="#" onclick="winOpen('','300','300')">AB테스트 관리</a>
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
	                						<table border="0" cellpadding="15" cellspacing="0" width="100%" align="center" >
	                    						<tbody>
                    								<tr>
                    									<td>
                    										<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" >
			                            						<tbody>
													                <tr>
													                    <td class="3_line" height="1"></td>
													                </tr>
													                <tr>
													                    <td height="20"></td>
													                </tr>
						                							<tr>
						                    							<td colspan="2">
						                        							<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            							<tbody>
														                            <tr>
														                                <td height="25">
														                                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
														                                        <tbody>
															                                        <tr>
															                                            <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
																                                        <td class="bold">AB테스트 등록</td>
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
                    									</td>
                    								</tr>
                    							</tbody>
                    						</table>
                    						<form id="form" name="form" action="./getABTestPaperList.do">
                    						<input type="hidden" id="findName" name="findName" value="${findName}">
                    						<input type="hidden" id="findValue" name="findValue" value="${findValue}">
                    						<input type="hidden" id="pageNum" name="pageNum" value="${pageNum}">
                    						<input type="hidden" id="test_id" name="test_id" value="${test_id}">
                    						<input type="hidden" id="variation_id" name="variation_id" value="${variation_id}">
                    						<input type="hidden" id="mims_id" name="mims_id" value="${mims_id}">
                    						<input type="hidden" id="org_mims_id" name="org_mims_id" value="${org_mims_id}">
                    						<input type="hidden" id="abmsUptFlag" name="abmsUptFlag" value="${abmsUptFlag}">
                    						<input type="hidden" id="d_mims_id" name="d_mims_id" value="${d_mims_id}">
                    						
                    						<div style="margin-bottom: 15px;">
                    							<c:forEach items="${abmsList}" var="abms_data">
                    								<input type="radio" id="${abms_data.variation_id}" name="rd_abms" value="${abms_data.variation_id}" <c:if test="${variation_id eq abms_data.variation_id}">checked = "checked"</c:if>>
                    									<label for="${abms_data.variation_id}">${abms_data.variation_name}</label>
                    							</c:forEach>
                    						</div>
                    						
                    						<!-- 오리지날 지면 -->
                    						<div style="width: 50%; float: left;">
												<table id="board_list"  border="0" cellpadding="0" cellspacing="0" style="width:90%;" class="board_list">
													<thead>
														<tr align="center">
															<th scope="col" colspan="4"><span><b>대조군 (${org_mims_id})</b></span></th>	
														</tr>
														<tr align="center">
															<th scope="col" width="20%">지면ID</th>	
													 	   	<th scope="col" width="*%">지면명</th>
													 	   	<th scope="col" width="15%">데이터타입</th>
													 	   	<th scope="col" width="15%">UI타입명</th>
														</tr>
													</thead>    
													<tbody>
														<c:if test="${orgPaperList == '[]' ||orgPaperList == null}"> 
							                                <tr align="center">
							                                    <td class="table_td_04" colspan="4">데이터가 존재 하지 않습니다.</td>					                                    
							                                </tr>
														</c:if>
						                                <c:forEach items="${orgPaperList}" var="org_data">
							                                <tr align="center">
							                                	<td class="table_td_04">${org_data.title_id}</td>
							                                	<td class="table_td_04" align="left">${org_data.title_nm}</td>
							                                	<td class="table_td_04">${org_data.page_type}</td>
							                                	<td class="table_td_04">${org_data.paper_ui_type}</td>
							                                </tr>
						                                </c:forEach>
													</tbody>
												</table>
                    						</div>
                    						
                    						<!-- AB지면 -->
                    						<div style="width: 50%; float: right;">
                    							<table id="board_list"  border="0" cellpadding="0" cellspacing="0" style="width:90%;" class="board_list">
													<thead>
														<tr align="center">
															<th scope="col" colspan="7"><span id="ab_paper_info"></span></th>	
														</tr>
                               							<tr align="center">
                               							   <th scope="col" width="5%"></th>
						                                   <th scope="col" width="10%">지면ID</th>	
													 	   <th scope="col" width="*%">지면명</th>
													 	   <th scope="col" width="10%">데이터타입</th>
													 	   <th scope="col" width="15%">UI타입명</th>
													 	   <th scope="col" width="10%">사용여부</th>
													 	   <th scope="col" width="15%">AB TEST 지면여부</th>
													 	   
						                                </tr>
                           							</thead>    
													<tbody>
														<c:if test="${abPaperList == '[]' ||abPaperList == null}"> 
							                                <tr align="center">
							                                    <td class="table_td_04" colspan="4">데이터가 존재 하지 않습니다.</td>					                                    
							                                </tr>
														</c:if>
						                                <c:forEach items="${abPaperList}" var="ab_data">
							                                <tr align="center">
							                                    <td class="table_td_04">
							                                		<input class="checkbox" type="checkbox" id="chk_${ab_data.title_id}" name="chk" value="${ab_data.title_id}" <c:if test="${ab_data.del_yn eq 'Y'}">disabled=disabled </c:if>>
							                                	</td>
							                                	<td class="table_td_04">
							                                	<c:if test="${ab_data.del_yn eq 'Y'}"><del></c:if>
							                                	${ab_data.title_id}
							                                	<c:if test="${ab_data.del_yn eq 'Y'}"></del></c:if>
							                                	</td>
							                                	<td class="table_td_04" align="left" style="text-decoration: underline;">
							                                	<a href="#" onclick="function_view_title('${mims_id}', '${ab_data.title_id}', '${test_type}')">
							                                	<c:if test="${ab_data.del_yn eq 'Y'}"><del></c:if>
							                                	${ab_data.title_nm}
							                                	<c:if test="${ab_data.del_yn eq 'Y'}"></del></c:if>
							                                	</a>
							                                	</td>
							                                	<td class="table_td_04" align="left" style="text-decoration: underline;">
							                                	<a href="#" onclick="function_view_category('${mims_id}', '${ab_data.title_id}', '${ab_data.category_id}', '${ab_data.album_desc}', '${ab_data.album_cnt}', '${ab_data.category_gb }', '${test_type}')">
							                                	<c:if test="${ab_data.del_yn eq 'Y'}"><del></c:if>
							                                	${ab_data.page_type}
							                                	<c:if test="${ab_data.del_yn eq 'Y'}"></del></c:if>
							                                	</a>
							                                	</td>
							                                	<td class="table_td_04">
							                                	<c:if test="${ab_data.del_yn eq 'Y'}"><del></c:if>
							                                	${ab_data.paper_ui_type}
							                                	<c:if test="${ab_data.del_yn eq 'Y'}"></del></c:if>
							                                	</td>
							                                	<td class="table_td_04">
							                                	<c:if test="${ab_data.del_yn eq 'Y'}"><del></c:if>
							                                		<c:if test="${ab_data.use_yn eq 'Y'}">사용</c:if>
									                                <c:if test="${ab_data.use_yn eq 'N'}">사용안함</c:if>
									                            <c:if test="${ab_data.del_yn eq 'Y'}"></del></c:if>
							                                	</td>
							                                	<td class="table_td_04">
						                                		<c:if test="${ab_data.abtest_yn eq 'Y'}">예</c:if>
								                                <c:if test="${ab_data.abtest_yn eq 'N'}">아니오</c:if>
							                                	</td>
							                                </tr>
						                                </c:forEach>
                           							</tbody>
                       							</table>
                    						</div>
                    						</form>
                    						<!-- 버튼목록 -->
                    						<table border="0" cellpadding="15" cellspacing="0" width="90%" align="center">
	                    						<tbody>
                    								<tr>
                    									<td align="right" >
                    										<c:if test="${abmsUptFlag}">
	                    										<c:if test="${test_type ne 'M'}">
	                    											<c:if test="${test_type ne 'D'&& test_type ne 'O'}">
		                    											<span class="button small blue" id="replaceBtn">패널복사</span>
		                    										</c:if>
								                                	<span class="button small blue" id="changeOrdBtn">순서변경</span>
								                                	<span class="button small blue" id="insertBtn">추가</span>
								                                	<span class="button small blue" id="deleteBtn">삭제</span>
	                    										</c:if>
						                                	</c:if>
						                                	<span class="button small blue" id="applyBtn">즉시적용</span>
						                                	<span class="button small red" id="finishBtn">최종완료</span>
						                                	<span class="button small blue" id="listBtn">목록</span>
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
	  	</td>
	  	</tr>
		<tr>
		<td height="30">&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr>
	    <td colspan="2" background="/smartux_adm/images/admin/copy_bg.gif" height="60" align="left">
	        <!-- 하단 로그인 사용자 정보 시작 -->
	        
	        <!-- 하단 로그인 사용자 정보 종료 -->
	    </td>
	 	</tr>
</tbody>
</table>
</div>
</body>
</html>