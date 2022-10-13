<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
            <script type="text/javascript">
				$(function(){
					var Today = new Date();
					AnyTime.picker('start_dt', { format: '%m.%d', labelTitle: '날짜', labelHour: '', time: '', defaultYear: Today.getFullYear()});
					AnyTime.picker('end_dt', { format: '%m.%d', labelTitle: '날짜', labelHour: '', time: '', defaultYear: Today.getFullYear()});
					
					$("#search_type").change(function(){
						$("#start_dt").val("");
						$("#end_dt").val("");
						$("#display_type").val("");
						if(this.value=="1"){
							$("#sType2").hide();
							$("#sType1").show();
						}else{
							$("#sType1").hide();
							$("#sType2").show();
						}
					});
					
					$("#sForm").on("submit",function(event){
				        if($("#search_type").val()=="1"){
				        	if($("#start_dt").val()==""){
				        		alert("시작일을 입력해 주세요.");
				        		$("#start_dt").focus();
				        		return false;
				        	}else if($("#end_dt").val()==""){
				        		alert("종료일을 입력해 주세요.");
				        		$("#end_dt").focus();
				        		return false;
				        	}
				        }else if($("#search_type").val()=="2"){
				        	if($("#display_type").val()==""){
				        		alert("타입을 선택해 주세요.");
				        		$("#display_type").focus();
				        		return false;
				        	}
				        }
				    });
					
					$(".updBtn").click(function(){
						url = '${pageContext.request.contextPath}/admin/dayinfo/updateDayInfoPop.do?callbak=fnSearchForm&seq='+$(this).attr("data-seq");
		                category_window = window.open(url, 'updateDayInfoPop', 'width=800,height=470');
					});
					
					$("#deleteBtn").click(function(){
						var fileId_comma = "";
						if(!$(".checkbox").is(":checked")){
							alert("삭제 할 항목을 선택하세요.");
							return;
						}else{
							var statCnt = 0;
				            $(".checkbox").each(function () {
				                if ($(this).is(":checked")) {
				                	if(fileId_comma=="") fileId_comma = $(this).val();
				                	else fileId_comma = fileId_comma+","+$(this).val();
				                }
				            });
						}
						
						if(confirm("선택된 일별정보를 정말 삭제하시겠습니까?")){
							$.ajax({
		            			cache : false,
		            			url : '${pageContext.request.contextPath}/admin/dayinfo/deleteDayInfoProc.do',
		            			contentType : 'application/json',
		            			dataType : 'json',
		            			data : { 
		            				seq : fileId_comma
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
		            			complete : function() {
		            				$.unblockUI();
		            			},
		            			success: function(data, status){
		            				if("success"==status){
		            					if(data.flag=="0000"){
			            					alert('삭제처리 하였습니다.');
			            					$("#hForm").submit();
		            					}else {
		            						alert(data.message);
		            					}
		            				}else{
		            					alert('삭제처리에 문제가 발생했습니다.')
		            				}
		                        },
		                        error: function(xhr, textStatus, errorThrown) {
		                        	alert('삭제처리에 실패하였습니다.')
		                        }
		            		});
						}
					});
					
					$("#addBtn").click(function(){
						url = '${pageContext.request.contextPath}/admin/dayinfo/insertDayInfoPop.do?callbak=fnSearchForm';
		                category_window = window.open(url, 'insertDayInfoPop', 'width=800,height=470');
					});
					
					$("#applyBtn").click(function(){
						if(confirm("즉시적용 하시겠습니까?")){
							$.blockUI({
								blockMsgClass: "ajax-loading",
								showOverlay: true,
								overlayCSS: { backgroundColor: '#CECDAD' } ,
								css: { border: 'none' } ,
								 message: "<b>로딩중..</b>"
							});
							$.post("${pageContext.request.contextPath}/admin/dayinfo/applyCache.do", 
									{callByScheduler : 'A'},
									function(data) {
									 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
									 	var flag = data.flag;
									 	var message = data.message;
									 	
									 	if(flag == "0000"){						// 정상적으로 처리된 경우						 		
									 		alert("일별정보를 즉시적용하였습니다.");	
									 		//location.reload();
									 	}else{
									 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
									 	}
								  },
								  "json"
						    );
						 	$.unblockUI();
						}
					});
					
				});
				
				function fnSearchForm(){
					$("#hForm").submit();
				}
			</script>
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
                                    일별정보 관리
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
			                                <table border="0" cellpadding="0" cellspacing="0" width="100%" class="search_table">
			                                	<form id="hForm" method="GET" action="./dayInfoList.do">
			                                		<input type="hidden" id="s_search_type" name="search_type" value="${search_type}">
			                                		<input type="hidden" id="s_start_dt" name="start_dt" value="${start_dt}">
			                                		<input type="hidden" id="s_end_dt" name="end_dt" value="${end_dt}">
			                                		<input type="hidden" id="s_display_type" name="display_type" value="${display_type}">
			                                	</form>
				                                <form id="sForm" method="GET" action="./dayInfoList.do">
			                                	<tbody>
			                                	<tr>
				                                    <td width="15">&nbsp;</td>
				                                    <td width="80"><img src="/smartux_adm/images/admin/search_title4.gif" border="0" height="47" width="62"></td>
				                                    <td width="380">
				                                    	<select class="select" id="search_type" name="search_type">
				                                    		<option value="1" <c:if test="${search_type =='1'}">selected="selected"</c:if>> 날짜 </option>
				                                    		<option value="2" <c:if test="${search_type =='2'}">selected="selected"</c:if>> 타입 </option>
		                                                </select>
		                                                &nbsp;
		                                                <span id="sType1" <c:if test="${search_type != '1'}">style="display:none;"</c:if>>
		                                                	시작일:&nbsp;<input type="text" id="start_dt" name="start_dt" size="10" maxlength="5" style="text-align: center" value="${start_dt}">
		                                                	&nbsp;~&nbsp;
		                                                	종료일:&nbsp;<input type="text" id="end_dt" name="end_dt" size="10" maxlength="5" style="text-align: center" value="${end_dt}">
		                                                </span>
		                                                <span id="sType2" <c:if test="${search_type != '2'}">style="display:none;"</c:if>>
		                                                <select class="select" id="display_type" name="display_type">
				                                    		<option value="" <c:if test="${display_type ==''}">selected="selected"</c:if>>::선택::</option>
				                                    		<option value="1" <c:if test="${display_type =='1'}">selected="selected"</c:if>>절기</option>
				                                    		<option value="2" <c:if test="${display_type =='2'}">selected="selected"</c:if>>절일</option>
				                                    		<option value="3" <c:if test="${display_type =='3'}">selected="selected"</c:if>>잡절</option>
				                                    		<option value="4" <c:if test="${display_type =='4'}">selected="selected"</c:if>>명언</option>
				                                    		<option value="5" <c:if test="${display_type =='5'}">selected="selected"</c:if>>아름다운 우리말</option>
				                                    		<option value="6" <c:if test="${display_type =='6'}">selected="selected"</c:if>>명언2</option>
		                                                </select>
		                                                </span>
			                                    	</td>
			                                    	<td><input src="/smartux_adm/images/admin/search.gif" border="0" height="22" type="image" width="65"></td>
					                            </tr> 
					                        	</tbody>
		                                        </form>
					                        </table>		                              
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
						                                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
						                                        <tbody>
						                                        <tr>
						                                            <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
						                                            <td class="bold">일별정보</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        
					                            <table border="0" cellspacing="0" cellpadding="0" class="board_list" style="width:100%" id="listTab">
													<tr>
														<th width="5%">선택</th>
														<th width="8%">년도</th>
														<th width="8%">날짜</th>
														<th width="6%">타입</th>
														<th width="6%">노출시간</th>
														<th>문구</th>
														<th width="12%">화자</th>
														<th width="15%">추가내용</th>
														<th width="60">수정</th>
													</tr>
													<c:if test="${vo == '[]' }">
					                                <tr align="center">
					                                    <td class="table_td_04" colspan="9">데이터가 존재하지 않습니다.</td>					                                    
					                                </tr>
					                                </c:if>
					                                <c:forEach items="${vo}" var="rec">
														<tr>
															<td class="table_td_04"><input type="checkbox" class="checkbox" value="${rec.seq}"/></td>
															<td class="table_td_04">
																<c:choose>
																	<c:when test="${rec.display_year == '00'}">반복</c:when>
																	<c:otherwise>${rec.display_year}</c:otherwise>
																</c:choose>
															</td>
															<td class="table_td_04">${fn:substring(rec.display_date,0,2)}.${fn:substring(rec.display_date,2,4)}</td>
															<td class="table_td_04">
																<c:choose>
																	<c:when test="${rec.display_type == '1'}">절기</c:when>
																	<c:when test="${rec.display_type == '2'}">절일</c:when>
																	<c:when test="${rec.display_type == '3'}">잡절</c:when>
																	<c:when test="${rec.display_type == '4'}">명언</c:when>
																	<c:when test="${rec.display_type == '5'}">아름다운 우리말</c:when>
																	<c:when test="${rec.display_type == '6'}">명언2</c:when>
																	<c:otherwise>--</c:otherwise>
																</c:choose>
															</td>
															<td class="table_td_04">${rec.display_time}</td>
															<td class="table_td_04" style="text-align:left;">${rec.message}</td>
															<td class="table_td_04">${rec.speaker}</td>
															<td class="table_td_04">
																<c:choose>
																	<c:when test="${fn:length(rec.etc) > '10'}">${fn:substring(rec.etc,0,8)}...</c:when>
																	<c:otherwise>${rec.etc}</c:otherwise>
																</c:choose>
															</td>
															<td class="table_td_04"><span class="button small blue updBtn" id="updBtn" data-seq="${rec.seq}">수정</span></td>
														</tr>
					                            	</c:forEach>
												</table>
												<!-- 등록/수정 버튼 -->
												<br/>
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            <tbody>
						                            <tr>
						                                <td align="right">&nbsp;
						                                	<span class="button small blue" id="addBtn">등록</span>&nbsp;
						                                	<span class="button small red" id="deleteBtn">삭제</span>&nbsp;
						                                	<span class="button small blue" id="applyBtn">즉시적용</span>
						                                </td>
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