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
	
	$(".checkbox").click(function (e) {
		//체크했다면 자신을 제외한 다른 체크를 해제
		$(".checkbox:checked").not(this).removeAttr("checked"); 
	});	
	
	
	//등록화면 이동
	$("#insertBtn").click(function () {
		var check_cnt = $('input:checkbox[name="chk"]:checked').length;
		if(check_cnt==0){
			alert('테스트 ID를 선택해주세요.');
		}else if(check_cnt >1){
			alert('테스트 ID를 한 가지만 선택해주세요.');
		}else{
			//편성 가능여부 체크
			var variation_id = $('input[name=chk]:checked').attr('data_val');

			$.ajax({
				url:  "/smartux_adm/admin/abtest/checkABMSStatus.do",
				type: "POST",
				data: { 
					'variation_id' : variation_id
				},
				dataType : "json",
				success: function (rtn) {
					if (rtn.flag=="0000") {
						if(rtn.abmsUptFlag == 'true'){
							var check_value = $('input[name=chk]:checked').val();
							location.href = '/smartux_adm/admin/abtest/setABTestMapping.do?findName=${searchVo.findName}&findValue=${searchVo.findValue}&pageNum=${searchVo.pageNum}&test_id=' + check_value;
						}else{
							alert('선택한 테스트ID의 진행 상태 값이 편성 불가능 합니다.');
						}
					} else {
						alert("작업 중 오류가 발생하였습니다\nflag : " + rtn.flag + "\ Message : " + rtn.message);
					}
				},
				error: function () {					
					alert("일시적인 장애가 발생하였습니다. 다시 시도해 주세요.");
				}
			});
		}
	});
	
	//검색버튼 
	$('#searchBtn').click(function () {
		doSearch();
	});
});
 
//검색
function doSearch(){
	var content_type_val = $("#findName option:selected").val();
	if(content_type_val == ''){
		alert('검색할 타입을 선택 해 주세요.');
	}else{
		if($.trim($('#findValue').val()) == ''){
			alert('검색어를 입력해 주세요.');
		}else{
			$('#offset').val('0');
			$('#form1').attr('action','./getABTestList.do');
			$("form[name=form1]").submit();		
		}
	}
}

//2021.09.03 AB테스트 수정건 : 즉시적용 추가
function function_apply(test_id, variation_id, mims_id, org_mims_id, d_mims_id) {
	
	var msg = '해당 Variation_id 하위 지면을 즉시적용 하시겠습니까?';
	if(confirm(msg)){
		$.ajax({
			url:  "/smartux_adm/admin/abtest/applyABTestPaperCache.do",
			type: "POST",
			data: { 
				'panel_id' : mims_id,
				'test_id' : test_id,
				'variation_id' : variation_id,
				'org_mims_id' : org_mims_id,
				'd_mims_id' : d_mims_id
			},
			dataType : "json",
			success: function (rtn) {
				if (rtn.flag=="0000") {
					alert("즉시적용 성공");
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
						                    							<td>
						                    								<form id="form1" name="form1" action="./getABTestList.do" method="get">
						                        								<!-- 검색 시작 -->
						                        								<table border="0" cellpadding="0" cellspacing="0" width="100%" class="search_table">
						                            								<tr>
						                                								<td width="15"/>
														                                <td width="80">
														                                	<img src="/smartux_adm/images/admin/search_title4.gif" border="0" height="47" width="62">
														                                </td>
														                                <td>
						                                    								<table border="0" cellpadding="0" cellspacing="0">
						                                        								<tr>
														                                            <td width="10">
														                                            	<img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9"/>
														                                            </td>
														                                            <td>
														                                                <select class="select" id="findName" name="findName">
														                                                	<option value="">선택하세요</option>
														                                                	<option value="test_id" <c:if test="${searchVo.findName eq 'test_id'}">selected="selected"</c:if>>테스트ID</option>
														                                                	<option value="test_name" <c:if test="${searchVo.findName eq 'test_name'}">selected="selected"</c:if>>테스트명</option>
														                                                	<option value="variation_id" <c:if test="${searchVo.findName eq 'variation_id'}">selected="selected"</c:if>>VariationID</option>
														                                                	<option value="variation_name" <c:if test="${searchVo.findName eq 'variation_name'}">selected="selected"</c:if>>Variation명</option>
														                                                </select>
														                                            	<input type="hidden" id="offset" name="offset" value="-1">
														                                            </td>
														                                            <td></td>
														                                            <td>
														                                            	<input type="text" id="findValue" name="findValue" value="${searchVo.findValue}" size="20" style="font-size: 12px;" onKeyPress="if(event.keyCode==13){ doSearch();}" />
														                                            </td>
						                                            								<td>&nbsp;</td>
						                                            								<td width="66" align="left">
						                                            									<span id="searchBtn" class="button small blue">검색</span>
						                                            								</td>
						                                        								</tr>
						                                    								</table>
						                                								</td>
						                            								</tr>
						                        								</table>
						                        								<!-- 검색 종료 -->
						                        							</form>
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
																                                        <td class="bold">AB테스트 목록 관리</td>
															                                        </tr>
														                                    	</tbody>
														                                    </table>
														                                </td>						                                						                                
														                            </tr>
						                       									</tbody>
						                       								</table>
					                            							<table id="board_list"  border="0" cellpadding="0" cellspacing="0" style="width:100%;" class="board_list">
					                                							<thead>
						                                							<tr align="center">
						                                								<th scope="col" width="1%"></th>	
													                                   <th scope="col" width="8%">테스트ID</th>	
																				 	   <th scope="col" width="5%">테스트명</th>
																				 	   <th scope="col" width="8%">VariationID</th>
																				 	   <th scope="col" width="5%">Variation명</th>
																				 	   <th scope="col" width="5%">오리진패널ID</th>
																				 	   <th scope="col" width="5%">패널ID</th>
																				 	   <th scope="col" width="10%">패널명</th>
																				 	   <th scope="col" width="10%">진행상태</th>
																				 	   <th scope="col" width="5%">지면ID</th>
																				 	   <th scope="col" width="*">지면명</th>
																				 	   <th scope="col" width="10%">ABMS 요청 상태</th>
																				 	   <th scope="col" width="5%">AB TEST 타입</th>
																				 	   <th scope="col" width="8%">테스트시작일</th>
																				 	   <th scope="col" width="8%">테스트종료일</th>
																				 	   <th scope="col" width="5%"></th>
													                                </tr>
						                            							</thead>    
																				<tbody>
																					<c:if test="${list == '[]' || list == null}"> 
														                                <tr align="center">
														                                    <td class="table_td_04" colspan="15">데이터가 존재 하지 않습니다.</td>					                                    
														                                </tr>
																					</c:if>
													                                <c:forEach items="${list}" var="data">
														                                <tr align="center">
														                                	<td class="table_td_04">
														                                		<input class="checkbox" type="checkbox" id="chk_${data.variation_id}" name="chk" value="${data.test_id}" data_val = "${data.variation_id}" <c:if test="${data.org_mims_id != null}"> disabled="disabled"</c:if>>
														                                	</td>
														                                	<td class="table_td_04">${data.test_id}</td>
														                                	<td class="table_td_04">${data.test_name}</td>
														                                	<td class="table_td_04" align="left"  style="text-decoration: underline;">
														                                		<c:choose>
																								<c:when test="${not empty data.org_mims_id}">
																								<a href="./getABTestPaperList.do?findName=${searchVo.findName}&findValue=${searchVo.findValue}&pageNum=${searchVo.pageNum}&test_id=${data.test_id}&variation_id=${data.variation_id}&mims_id=${data.mims_id}&org_mims_id=${data.org_mims_id}">
														                                			${data.variation_id}
														                                		</a>
																								</c:when>
																								<c:otherwise>
																								<a  href="void(0);" onclick="alert('테스트 패널을 등록해주세요.');return false;">
																									${data.variation_id}
																								</a>
																								</c:otherwise>
																								</c:choose>
														                                	</td>
														                                	<td class="table_td_04">${data.variation_name}</td>
														                                	<td class="table_td_04">${data.org_mims_id}</td>
														                                	<td class="table_td_04">${data.mims_id}</td>
														                                	<td class="table_td_04">${data.pannel_nm}</td>
														                                	<td class="table_td_04">${data.abms_status_name}</td>
														                                	<td class="table_td_04">${data.mod_id}</td>
																							<td class="table_td_04">${data.mod_name}</td>
														                                	<td class="table_td_04">
														                                		<c:choose>
														                                			<c:when test="${data.status eq 'C'}">완료</c:when>
														                                			<c:when test="${data.status eq 'F'}">실패</c:when>
														                                			<c:otherwise>미완료</c:otherwise>
														                                		</c:choose>
														                                	</td>
														                                	<td class="table_td_04">
														                                		<c:choose>
														                                			<c:when test="${data.test_type eq 'O'}">순서변경</c:when>
														                                			<c:when test="${data.test_type eq 'M'}">메타편성</c:when>
														                                			<c:when test="${data.test_type eq 'T'}">패널변경타입</c:when>
														                                			<c:when test="${data.test_type eq 'D'}">상이한 지면 타입</c:when>
														                                		</c:choose>
														                                	</td>
														                                	<td class="table_td_04">${data.start_date_time}</td>
														                                	<td class="table_td_04">${data.end_date_time}</td>
																							<!-- 2021.09.03 AB테스트 수정건 : 즉시적용 추가 -->
																							<td class="table_td_04">
																								<c:if test="${not empty data.org_mims_id}"> 
																									<c:if test="${data.abms_status eq '120' or data.abms_status eq '130' or data.abms_status eq '145' or data.abms_status eq '150'}">
																										<span class="button small blue" onclick="function_apply('${data.test_id}','${data.variation_id}', '${data.mims_id}', '${data.org_mims_id}', '${data.d_mims_id}')">즉시적용</span>
																									</c:if>
																								</c:if> 
														                                	</td>
														                                </tr>
													                                </c:forEach>
					                                							</tbody>
					                            							</table>
												                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
													                            <tbody>
													                            <tr>
													                                <td height="5"></td>
													                            </tr>
													                            <tr>
													                            	<td align="center">
													                            		<jsp:include page="/WEB-INF/views/include/naviControll.jsp">
																							<jsp:param value="getABTestList.do" name="actionUrl"/>
																							<jsp:param value="?findName=${searchVo.findName}&findValue=${searchVo.findValue}" name="naviUrl" />
																							<jsp:param value="${searchVo.pageNum }" name="pageNum"/>
																							<jsp:param value="${searchVo.pageSize }" name="pageSize"/>
																							<jsp:param value="${searchVo.blockSize }" name="blockSize"/>
																							<jsp:param value="${searchVo.pageCount }" name="pageCount"/>
																						</jsp:include>
													                            	</td>
													                            </tr>
													                        	</tbody>
													                        </table>
													                        <!-- 등록/수정 버튼 -->
												                            <table border="0" cellpadding="0" cellspacing="0" style="width:100%;">
													                            <tbody>
													                            <tr>
													                                <td align="right">
													                                	<span class="button small blue" id="insertBtn">등록</span>
													                                </td>
													                            </tr>
													                       		</tbody>
													                       	</table>
													                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
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
	        
	        <!-- 하단 로그인 사용자 정보 종료 -->
	    </td>
	 	</tr>
</tbody>
</table>
</div>
</body>
</html>