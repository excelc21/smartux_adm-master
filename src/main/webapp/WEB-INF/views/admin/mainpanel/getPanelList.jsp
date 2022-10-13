<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<link href="/smartux_adm/css/anytime_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script src="/smartux_adm/js/anytime.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	AnyTime.picker('startDt', { format: '%z-%m-%d', labelTitle: '날짜', labelHour: '시간', time: ''});
	AnyTime.picker('endDt', { format: '%z-%m-%d', labelTitle: '날짜', labelHour: '시간', time: ''});
	
	$('#changeOrder').click(function () {
		popupCenter('${pageContext.request.contextPath}/admin/mainpanel/changePanelOrder.do', '순서변경', 450, 300)
    });
	
	$("#regbtn").click(function(){
		window.open("<%=webRoot%>/admin/mainpanel/insertPanel.do", "regpanel", "width=450,height=300");
	});
	
	$("#delbtn").click(function(){
		var checkeditems = $("input[name='delchk']:checked");
		var checkeditemslength = checkeditems.length;
		
		if(checkeditemslength == 0){
			alert("삭제할 패널을 선택해주세요");
		}else{
			if(confirm("선택된 패널들을 삭제하시겠습니까")){
				
				var size = checkeditemslength;
				var checkboxarray = new Array();
				
				for(var i=0; i < size; i++){
					// alert($('#codeitemlist option').eq(i).val());
					var checkboxval = $(checkeditems[i]).val();
					checkboxarray.push(checkboxval);
				}
				var smartUXManager = $("#smartUXManager").val();
				
				$.post("<%=webRoot%>/admin/mainpanel/deletePanel.do", 
						 {panel_id : checkboxarray, smartUXManager : smartUXManager},
						  function(data) {
							 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
							 	var flag = data.flag;
							 	var message = data.message;
							 	
							 	if(flag == "0000"){						// 정상적으로 처리된 경우
							 		alert("패널이 삭제되었습니다");
							 		location.reload();
							 	}else{
							 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
							 	}
						  },
						  "json"
			    );
			}
		}
		
	});
	
	$("#allchk").click(function(){
		var chkallchecked = false;
		
		if($("#allchk").is(":checked")){
			chkallchecked = true;
		}
		
		$("input[name='delchk']").attr("checked", chkallchecked);
	});
	
	$('#searchBtn').click(function () {
		var findName = $('#findName').val();
		var findValue = $('#findValue').val();
		var startDt = $('#startDt').val();
		var endDt = $('#endDt').val();
		var abtestYnChk = $('input[name="abtestYnChk"]:checked').val();

		if (startDt != '' && endDt == '') {
			alert('종료일을 설정해 주십시오.')
			return;
		}
		if (endDt != '' && startDt == '') {
			alert('시작일을 설정해 주십시오.')
			return;
		}
		if (startDt != '' && endDt != '') {
			var sDate = new Date(startDt)
			var eDate = new Date(endDt)
			if(eDate.getTime() - sDate.getTime() < 0) {
				alert('시작일은 종료일보다 빨라야합니다. 다시 설정해 주십시오.')
				return;
			}
		}
		if (findValue != '' && findName == '') {
			alert('검색대상을 설정해 주십시오.')
			return;
		}
		
		var param = jQuery.param({findName: findName, findValue: findValue, startDt:startDt, endDt:endDt, abtestYnChk:abtestYnChk});
		location.href = '<%=webRoot%>/admin/mainpanel/getPanelList.do?' + param;
	});
	
	$('#excelDownBtn').click(function () {
		var findName =  '<c:out value="${vo.findName}"/>';
		var findValue =  '<c:out value="${vo.findValue}"/>';
		var startDt =  '<c:out value="${vo.startDt}"/>';
		var endDt =  '<c:out value="${vo.endDt}"/>';
		var abtestYnChk =  '<c:out value="${vo.abtestYnChk}"/>';

		if (startDt != '' && endDt == '') {
			alert('종료일을 설정해 주십시오.')
			return;
		}
		if (endDt != '' && startDt == '') {
			alert('시작일을 설정해 주십시오.')
			return;
		}
		if (startDt != '' && endDt != '') {
			var sDate = new Date(startDt)
			var eDate = new Date(endDt)
			if(eDate.getTime() - sDate.getTime() < 0) {
				alert('시작일은 종료일보다 빨라야합니다. 다시 설정해 주십시오.')
				return;
			}
		}
		if (findValue != '' && findName == '') {
			alert('검색대상을 설정해 주십시오.')
			return;
		}
		
		var param = jQuery.param({findName: findName, findValue: findValue, startDt:startDt, endDt:endDt, abtestYnChk:abtestYnChk});
		location.href = '<%=webRoot%>/admin/mainpanel/downloadExcelFile.do?' + param;
	});
});

function view_panel(panel_id){
	var url = "<%=webRoot%>/admin/mainpanel/viewPanel.do?panel_id=" + panel_id;
	window.open(url, "viewcode", "width=450,height=300");
}

//지면 순서변경
function view_paper(panel_id) {
	url = '/smartux_adm/admin/mainpanel/getPanelTitleTempList.do?selcode=' + panel_id + '&panel_id=' + panel_id; 
	window.open(url, "viewpaper", "width=1800,height=900,left=10,top=10,scrollbars=yes");
}

//AB정보
function view_abinfo(panel_id, abtestYnChk){
	var url = "<%=webRoot%>/admin/mainpanel/showABInfoPopup.do?panel_id=" + panel_id + "&abtestYnChk=" + abtestYnChk;
	window.open(url, "viewabinfo", "width=450,height=300,scrollbars=yes");
}

</script>
<body leftmargin="0" topmargin="0">
<div id="divBody" style="height:100%">
<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
    <tbody>
      <tr>
        <td colspan="2" height="45" valign="bottom">
       		 <!-- top menu start -->
			<!-- jsp:include page="/WEB-INF/views/include/top.jsp"--><!-- /jsp:include -->
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
                <tr style="display:block">
                    <td height="42" width="100%">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tbody>
                            <tr>
                                <td width="300" class="boldTitle">
                                	<!-- 카테고리 타이틀 -->
                                    <!-- <img src="/smartux_adm/images/admin/category_map_title.gif">-->
                                    패널/지면
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
			                            <td>
			                                <!-- 검색 시작 -->		
			                                <table border="0" cellpadding="0" cellspacing="0" width="98%" class="search_table">
                                                <tr>
                                                    <td width="15"/>
                                                    <td width="80"><img
                                                            src="/smartux_adm/images/admin/search_title4.gif"
                                                            border="0" height="47" width="62"></td>
                                                    <td>
                                                        <table border="0" cellpadding="0" cellspacing="0">
                                                        	<tr>
                                                                <td width="16" height="26"><img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9"/></td>
                                                                <td colspan="4">
																	날짜 : <input type="text" id="startDt" size="14" title="시작일" style="text-align: center"
                                                                                 <c:if test="${not empty vo.startDt}">value="${vo.startDt}"</c:if>/>
                                                                    ~ <input type="text" id="endDt" size="14" title="종료일" style="text-align: center"
                                                                                 <c:if test="${not empty vo.endDt}">value="${vo.endDt}"</c:if>/>
                                                                </td>
                                                                <td width="16" height="26"><img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9"/></td>
                                                                <td>
                                                                	AB연동여부 :
                                                                	<input type="radio" name=abtestYnChk value="" <c:if test="${empty vo.abtestYnChk}">checked</c:if>/>전체
                                                                	<input type="radio" name="abtestYnChk" value="Y" <c:if test="${'Y' eq vo.abtestYnChk}">checked</c:if>/>연동
                                                                	<input type="radio" name="abtestYnChk" value="N" <c:if test="${'N' eq vo.abtestYnChk}">checked</c:if>/>미연동
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td width="16" height="26"><img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9"/></td>
                                                                <td width="120">
                                                                    <select class="select" id="findName" name="findName">
                                                                    	<option value="">선택하세요</option>
					                                                    <option <c:if test="${vo.findName == 'PANNEL_NM'}">selected="selected"</c:if> value="PANNEL_NM">패널명</option>
					                                                    <option <c:if test="${vo.findName == 'PANNEL_ID'}">selected="selected"</c:if> value="PANNEL_ID">패널ID</option>
					                                                </select></td><td>
					                                            	<input type="text" id="findValue" name="findValue" value="${vo.findValue }" size="20" style="font-size: 12px;" />
                                                                </td>
                                                                <td width="90" align="center"><input src="/smartux_adm/images/admin/search.gif" border="0" height="22" type="image" width="65" id="searchBtn"></td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
					                        <!-- 검색 종료 -->
					                    </td>
						                </tr>
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
						                                            <td class="bold">패널 목록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        
					                            <table border="0" cellspacing="0" cellpadding="0" class="board_list" style="width:98%">
													<tr>
														<th width="3%" style="padding-top:8px"><input type="checkbox" id="allchk" name="allchk" value=""></th>
														<th width="7%">패널 코드</th>														
														<th>패널명</th>
														<th width="7%">포커스타입</th>
														<th width="7%">패널UI타입</th>
														<th width="7%">사용여부</th>
														<th width="7%">AB연동여부</th>
														<th width="7%">등록날짜</th>
													</tr>
													<c:choose>
														<c:when test="${result==null || fn:length(result) == 0}">
															<tr>
																<td colspan="5" class="table_td_04">검색된 패널이 없습니다</td>
															</tr>
														</c:when>
														<c:otherwise>
															<c:forEach var="item" items="${result}" varStatus="status">
																<tr>
																	<td class="table_td_04">
																		<c:if test="${item.existProperty == 'Y'}">
																			&nbsp;
																		</c:if>
																		<c:if test="${item.existProperty == 'N'}">
																			<input type="checkbox" name="delchk" value="${item.pannel_id}">
																		</c:if>
																	</td>
																	<td class="table_td_04">
																		<a href="#" onclick="view_paper('${item.pannel_id}')">${item.pannel_id}</a>
																	</td>																	
																	<td class="table_td_04" style="text-align: left;">
																		<a href="#" onclick="view_panel('${item.pannel_id}')">${item.pannel_nm}</a>
																	</td>
																	<td class="table_td_04">${item.focus_type}</td>
																	<td class="table_td_04">${item.panel_ui_type}</td>
																	<td>
																		<c:choose>
																			<c:when test="${item.use_yn == 'Y'}">
																				예
																			</c:when>
																			<c:otherwise>
																				아니오
																			</c:otherwise>
																		</c:choose>
																	</td>
																	<td class="table_td_04">
																		<c:choose>
																			<c:when test="${item.abtest_yn == 'Y'}">
																				<a href="#" style="text-decoration: underline;" onclick="view_abinfo('${item.pannel_id}', '${item.abtest_yn}')">${item.abtest_yn}</a>
																			</c:when>
																			<c:otherwise>
																				${item.abtest_yn}
																			</c:otherwise>
																		</c:choose>
																	</td>
																	<td class="table_td_04">${item.created}</td>
																</tr>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</table>
												<input type="hidden" id="smartUXManager" name="smartUXManager" value="<%=id_decrypt %>" />
												<table border="0" cellpadding="0" cellspacing="0" style="width:98%" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="40" align="center">
						                                	<span class="button small blue" id="changeOrder">순서변경</span>
						                                	<span class="button small blue" id="regbtn">등록</span>
						                                	<c:if test="${result != null && fn:length(result) > 0}">
															<span class="button small blue" id="delbtn">삭제</span>
															</c:if>
															<span class="button small blue" id="excelDownBtn">엑셀다운</span>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
					                            
					                            
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