<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css"> 
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>

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
					                        <!-- 검색 종료 -->
					                    </td>
						                </tr>
						                <tr>
						                    <td class="3_line" height="1"></td>
						                </tr>
						                <tr>
						                    <td height="20"></td>
						                </tr>
						                <!-- 리스트 시작 -->
						                <tr>
						                    <td>
						                        <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            <tbody>
						                            <tr>
						                                <td height="25">
						                                	<form id="listfrm" name="listfrm" method="get" action="">
						                                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
						                                        <tbody>
						                                        <tr>
						                                            <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
						                                            <td class="bold">패널별 지면 목록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                    <c:if test="${fn:length(panel_result) != 0}">
						                                    <table border="0" cellpadding="0" cellspacing="0" style="width:100%;">
						                                        <tbody>
						                                        <tr>
						                                            <td width="10%">
																		<select id="selcode" name="selcode">
																			<c:forEach var="item" items="${panel_result}" varStatus="status">
																				<c:choose>
																					<c:when test="${item.pannel_id == panel_id}">
																						<option value="${item.pannel_id}" selected>${item.pannel_nm}</option>
																					</c:when>
																					<c:otherwise>
																						<option value="${item.pannel_id}">${item.pannel_nm}</option>
																					</c:otherwise>
																				</c:choose>
																			</c:forEach>
																		</select>
						                                            </td>
						                                            <td width="90%" align="left">
						                                            	<input id="searchbtn" src="/smartux_adm/images/admin/search.gif" border="0" height="22" type="image" width="65">
						                                            </td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                    </c:if>
						                                    <input type="hidden" id="panel_id" name="panel_id" value="${panel_id}" /> 
						                                    <!-- <input type="hidden" id="smartUXManager" name="smartUXManager" value="<%//=id_decrypt %>" /> -->
						                                    </form>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        
					                            <table border="0" cellspacing="0" cellpadding="0" class="board_list" style="width:100%">
													<tr>
														<th width="3%" style="padding-top:8px"><input type="checkbox" id="allchk" name="allchk" value=""></th>
														<th width="3%">포커스</th>
														<th>지면명</th>
														<th width="10%">지면 타입</th>
														<th width="10%">지면 코드</th>
														<th width="10%">데이터 타입</th>
														<th width="15%">하위지면 추가</th>
														<th width="15%">지면 데이터 설정</th>
														<th width="10%">순서 바꾸기</th>
													</tr>
													<c:choose>
														<c:when test="${result==null || fn:length(result) == 0}">
															<tr>
																<td colspan="8" class="table_td_04">검색된 지면이 없습니다</td>
															</tr>
														</c:when>
														<c:otherwise>
															<c:forEach var="item" items="${result}" varStatus="status">
																<tr onMouseOver="this.style.backgroundColor='#FFCCCC';" onMouseOut="this.style.backgroundColor=''">
																	<td class="table_td_04">
																		<input type="checkbox" name="delchk" value="${item.title_id}">
																	</td>
																	
																	<td>
																   <c:choose>
       																 <c:when test="${item.focus_yn == 'Y'}">
          															<input class="focuscheckbox" type="checkbox" name="${item.p_title_id}" value="${item.title_id}" checked >
																	 </c:when>         
       																 <c:otherwise>
        																<input class="focuscheckbox" type="checkbox" name="${item.p_title_id}" value="${item.title_id}" >
																	 </c:otherwise>
    																	</c:choose>			
																	</td>
															
																	
																	
																	<td class="table_td_04" style="text-align: left;">
																		<c:if test="${item.level > 1}">
																			<c:forEach var="i" begin="2" end="${item.level}">
																			&nbsp;&nbsp;&nbsp;&nbsp;
																			</c:forEach>
																			<!-- 답변형 게시판 ㄴ 형태 아이콘 추가 -->
																			<img src="/smartux_adm/images/admin/subtitle.gif">
																		</c:if>
																		
																	<c:choose>	
																		<c:when test="${item.use_yn == 'N'}">
																			<a href="#" onclick="view_title('${panel_id}', '${item.title_id}')"><del>${item.title_nm}</del></a>
																		</c:when>
																		<c:when test="${item.use_yn == 'T'}">
																			<a href="#" onclick="view_title('${panel_id}', '${item.title_id}')"><font color=red>${item.title_nm}</font></a>
																		</c:when>
																		<c:otherwise>
																			<a href="#" onclick="view_title('${panel_id}', '${item.title_id}')">${item.title_nm}</a>
																		</c:otherwise>
																		
																	</c:choose>
																	
																	
																	</td>
																	<td class="table_td_04" style="text-align:center;">
																		<c:if test="${item.page_type == 'LIVE'}">편성표</c:if>
																		<c:if test="${item.page_type == 'CAT'}">메뉴 카테고리</c:if>
																		<c:if test="${item.page_type == 'APP'}">어플 연동</c:if>
																		<c:if test="${item.page_type == 'AHOME'}">추천 연동</c:if>
																		<c:if test="${item.page_type == 'ETC'}">기타</c:if>
																		<c:if test="${item.page_type == 'EXT'}">사용자 정의</c:if>
																		<c:if test="${item.page_type == 'PAPAGO'}">시니어 파파고</c:if>
																		<c:if test="${item.page_type == 'PANEL'}">패널연동</c:if>
																	</td>
																	<td class="table_td_04" style="text-align:center;">
																		${item.page_code}
																	</td>
																	<td class="table_td_04" style="text-align:center;">
																		<c:if test="${item.category_desc == null}">
																			&nbsp;
																		</c:if>
																		<c:if test="${item.category_desc != null}">
																			${item.category_desc}
																		</c:if>
																	</td>
																	<td class="table_td_04" align="center">
																		<span class="button small blue" onclick="reg_title('${panel_id}', '${item.title_id}')">하위 지면 추가</span>
																	</td>
																	<td class="table_td_04" align="center">
																		<c:if test="${item.existsub == 'Y'}">
																			&nbsp;
																		</c:if>
																		<c:if test="${item.existsub == 'N'}">
																			<span class="button small blue" onclick="view_category('${panel_id}', '${item.title_id}', '${item.category_id}', '${item.album_desc}', '${item.album_cnt}', '${item.category_gb }')">지면 데이터 설정</span>
																		</c:if>
																	</td>
																	<td class="table_td_04" align="center">
																		<c:if test="${item.existsub == 'N'}">
																			&nbsp;
																		</c:if>
																		<c:if test="${item.existsub == 'Y'}">
																			<span class="button small blue" onclick="change_order('${panel_id}', '${item.title_id}')">순서 바꾸기</span>
																		</c:if>
																	</td>
																</tr>
																
																
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</table>
					                           
												<c:if test="${fn:length(panel_result) != 0}">
												<table border="0" cellpadding="0" cellspacing="0" style="width:100%" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="40" align="center">
						                                <span class="button small blue" id="focusbtn">포커싱 저장</span>
						                                	<span class="button small blue" id="regbtn" onClick="reg_title('${panel_id}', '');">하위지면 추가</span>
															<c:if test="${result!=null && fn:length(result) > 0}">
																<span class="button small blue" id="delbtn">지면삭제</span>
																<span class="button small blue" id="orderbtn" onclick="change_order('${panel_id}', '')">순서바꾸기</span>
																<span class="button small blue" id="previewTempbtn" onClick="previewTempbtn('${panel_id}', '');">프리뷰</span>
																<c:if test="${realTitleYN=='Y'}">
																	<span class="button small blue" id="previewbtn" onClick="previewbtn('${panel_id}', '');">프리뷰(상용)</span>
																</c:if>
																<span class="button small blue" id="confirmbtn">최종등록</span>	
																<span class="button small blue" id="cjconfirmbtn"> CJ 최종등록</span>	
																<span class="button small blue" id="confirmByPanelIdBtn">부분즉시적용</span>	
															</c:if>
															
															
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
					                            </c:if>
					                            
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
<script type="text/javascript">
$(document).ready(function(){
	// 검색 버튼
	$("#searchbtn").click(function(){
		$("#panel_id").val($("#selcode option:selected").val());
		$("#listfrm").submit();
	});
	
	$("#delbtn").click(function(){
		var panel_id = $("#panel_id").val();
		var checkeditems = $("input[name='delchk']:checked");
		var checkeditemslength = checkeditems.length;
		
		if(checkeditemslength == 0){
			alert("삭제할 항목을 선택해주세요");
		}else{
			if(confirm("상위지면을 지울경우 하위지면은 자동으로 삭제됩니다\n선택된 지면들을 삭제하시겠습니까")){
				var size = checkeditemslength;
				var checkboxarray = new Array();
				
				for(var i=0; i < size; i++){
					// alert($('#codeitemlist option').eq(i).val());
					var checkboxval = $(checkeditems[i]).val();
					checkboxarray.push(checkboxval);
				}
				
				
				$.post("<%=webRoot%>/admin/mainpanel/deletePanelTitleTemp.do", 
						 {panel_id : panel_id, title_id : checkboxarray},
						  function(data) {
							 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
							 	var flag = data.flag;
							 	var message = data.message;
							 	
							 	if(flag == "0000"){						// 정상적으로 처리된 경우
							 		alert("지면이 삭제되었습니다");
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
	
	
	
	
	
	$("#focusbtn").click(function(){
		var panel_id = $("#panel_id").val();
		var checkeditems = $(".focuscheckbox:checked");
		var checkeditemslength = checkeditems.length;
		
		if(checkeditemslength == 0){
			alert("포커스 항목을 선택해주세요");
		}else{
			if(confirm("선택된 지면들을 포커싱하시겠습니까")){
				var size = checkeditemslength;
				var checkboxarray = new Array();
				
				for(var i=0; i < size; i++){
					// alert($('#codeitemlist option').eq(i).val());
					var checkboxval = $(checkeditems[i]).val();
					checkboxarray.push(checkboxval);
				}
				
				
				$.post("<%=webRoot%>/admin/mainpanel/focusPanelTitleTemp.do", 
						 {panel_id : panel_id, title_id : checkboxarray},
						  function(data) {
							 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
							 	var flag = data.flag;
							 	var message = data.message;
							 	
							 	if(flag == "0000"){						// 정상적으로 처리된 경우
							 		alert("지면 포커스 완료");
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
	
	
	
	$(".focuscheckbox").change(function(){
		
		if($(this).is(":checked")){ 
			var name = $(this).attr('name');
			var selector = "input[name="+name+"]";
			$(selector).not(this).attr('checked',false);
		}
	});
	
	
	$("#confirmbtn").click(function(){
		var smartUXManager = '<%=id_decrypt%>';
		var confirmmsg = "현재 작업한 패널의 지면을 최종 등록하시겠습니까?";
		
		if(confirm(confirmmsg)){
			var panel_id = $("#panel_id").val();
			requestSetPanel(panel_id,smartUXManager,false, '');
		}
		
	});
	
	//2021.12.14 AMIMS 개선 : 부분즉시적용 추가
	$("#confirmByPanelIdBtn").click(function(){
		var smartUXManager = '<%=id_decrypt%>';
		var confirmmsg = "현재 작업한 패널의 지면을 최종 등록하시겠습니까?";
		
		if(confirm(confirmmsg)){
			var panel_id = $("#panel_id").val();
			requestSetPanel(panel_id,smartUXManager,false, 'P');
		}
		
	});
	
	
	$("#cjconfirmbtn").click(function(){
		var smartUXManager = '<%=id_decrypt%>';
		var confirmmsg = "현재 작업한 패널의 지면을 CJ 최종 등록하시겠습니까?";
		
		if(confirm(confirmmsg)){
			var panel_id = $("#panel_id").val();
			requestCjSetPanel(panel_id,smartUXManager,false);
		}
		
	});
});

function requestSetPanel(panel_id,smartUXManager,isForce, type)
{
	$.blockUI();
	var jsonParam = {};
	jsonParam.panel_id = panel_id;
	jsonParam.smartUXManager = smartUXManager;
	jsonParam.callByScheduler = "A";
	jsonParam.type = type;
	
	if(isForce)
		jsonParam.isForceUpdate = isForce;	
	
	$.post("<%=webRoot%>/admin/mainpanel/applyPanelTitle.do", 
				jsonParam,
			  function(data) {
				 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
				 	var flag = data.flag;
				 	var message = data.message;
				 	
				 	if(flag == "0000"){						// 정상적으로 처리된 경우
				 		console.log(data);
				 		alert("지면이 최종 등록되었습니다");
				 		$.unblockUI();
				 		location.reload();
				 	} else if(flag == "0001"){						// 정상적으로 처리된 경우
				 		console.log(data);
				 		if(confirm("지면의 변경내역을 찾을 수 없습니다.강제로 즉시적용을 하시겠습니까?")){
				 			requestSetPanel(panel_id,smartUXManager,true, type);					 			
						}
				 		else{
				 			$.unblockUI();
				 			location.reload();
				 		}
				 	}else{
				 		console.log(data);
				 		if(flag == "EXIST NOT_CATEGORY_TITLE"){			// 하위 지면중 카테고리 코드를 등록해야 할 지면이 있을 경우
				 			alert("다음의 화면에 카테고리 코드를 등록해주세요\n\n" + message);
				 		}else{
				 			alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
				 		}
				 		$.unblockUI();
				 	}
			  },
			  "json"
    );
}

function requestCjSetPanel(panel_id,smartUXManager,isForce)
{
	$.blockUI();
	var jsonParam = {};
	jsonParam.panel_id = panel_id;
	jsonParam.smartUXManager = smartUXManager;
	jsonParam.callByScheduler = "A";
	if(isForce)
		jsonParam.isForceUpdate = isForce;	
	
	$.post("<%=webRoot%>/admin/mainpanel/applyCjPanelTitle.do", 
				jsonParam,
			  function(data) {
				 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
				 	var flag = data.flag;
				 	var message = data.message;
				 	
				 	if(flag == "0000"){						// 정상적으로 처리된 경우
				 		console.log(data);
				 		alert("지면이 최종 등록되었습니다");
				 		$.unblockUI();
				 		location.reload();
				 	} else if(flag == "0001"){						// 정상적으로 처리된 경우
				 		console.log(data);
				 		if(confirm("지면의 변경내역을 찾을 수 없습니다.강제로 즉시적용을 하시겠습니까?")){
				 			requestCjSetPanel(panel_id,smartUXManager,true);					 			
						}
				 		else{
				 			$.unblockUI();
				 			location.reload();
				 		}
				 	}else{
				 		console.log(data);
				 		if(flag == "EXIST NOT_CATEGORY_TITLE"){			// 하위 지면중 카테고리 코드를 등록해야 할 지면이 있을 경우
				 			alert("다음의 화면에 카테고리 코드를 등록해주세요\n\n" + message);
				 		}else{
				 			alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
				 		}
				 		$.unblockUI();
				 	}
			  },
			  "json"
    );
}
function view_title(panel_id, title_id){
	var url = "<%=webRoot%>/admin/mainpanel/viewPanelTitleTemp.do?panel_id=" + panel_id + "&title_id=" + title_id;
	window.open(url, "viewtitle", "width=500,height=400,scrollbars=yes");
}

function reg_title(panel_id, p_title_id){
	var url = "<%=webRoot%>/admin/mainpanel/insertPanelTitleTemp.do?panel_id=" + panel_id + "&p_title_id=" + p_title_id; 
	window.open(url, "regtitle", "width=520,height=340");
}


function view_category(panel_id, title_id, category_id, category_type, album_cnt, category_gb){
	// var url = "<%=webRoot%>/admin/mainpanel/getCategoryList.do?panel_id=" + panel_id + "&title_id=" + title_id + "&category_id=VC";
	
	var url = "<%=webRoot%>/admin/mainpanel/getTypeSelect.do?panel_id=" + panel_id + "&title_id=" + title_id + "&category_id=" + category_id + "&category_type=" + category_type + "&album_cnt=" + album_cnt + "&category_gb=" +category_gb; 
	window.open(url, "view_category", "width=700,height=600,scrollbars=yes");
}

function change_order(panel_id, p_title_id){
	var url = "<%=webRoot%>/admin/mainpanel/changePanelTitleTempOrder.do?panel_id=" + panel_id + "&p_title_id=" + p_title_id; 
	window.open(url, "chnageorder", "width=450,height=300");
}

function previewTempbtn(panel_id){
	var url = "<%=webRoot%>/admin/mainpanel/previewPanelTitleTemp.do?panel_id=" + panel_id;
	window.open(url, "previewTempbtn", "width=800,height=800,scrollbars=yes,resizable=no");
}

function previewbtn(panel_id){
	var url = "<%=webRoot%>/admin/mainpanel/previewPanelTitle.do?panel_id=" + panel_id;
	window.open(url, "previewbtn", "width=800,height=800,scrollbars=yes,resizable=no");
}
</script>
</html>