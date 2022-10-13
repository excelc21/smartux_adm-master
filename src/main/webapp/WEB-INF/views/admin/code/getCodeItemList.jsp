<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
$(document).ready(function(){
	// 검색 버튼
	$("#searchbtn").click(function(){
		$("#code").val($("#selcode option:selected").val());
		$("#listfrm").submit();
	});
	
	$("#regbtn").click(function(){
		var code = $("#code").val();
		var url = "<%=webRoot%>/admin/code/insertCodeItem.do?code=" + code; 
		window.open(url, "regcodeitem", "width=450,height=250");
	});
	
	$("#delbtn").click(function(){
		var code = $("#code").val();
		var checkeditems = $("input[name='delchk']:checked");
		var checkeditemslength = checkeditems.length;
		
		if(checkeditemslength == 0){
			alert("삭제할 항목을 선택해주세요");
		}else{
			if(confirm("선택된 코드들을 삭제하시겠습니까")){
				var size = checkeditemslength;
				var checkboxarray = new Array();
				var ssgbnarray = new Array();
				
				for(var i=0; i < size; i++){
					// alert($('#codeitemlist option').eq(i).val());
					var checkboxval = $(checkeditems[i]).val().split('\|')[0];
					var delSsgbn = $(checkeditems[i]).val().split('\|')[1];
					checkboxarray.push(checkboxval);
					ssgbnarray.push(delSsgbn);
				}
				
				
				$.post("<%=webRoot%>/admin/code/deleteCodeItem.do", 
						 {code : code, item_code : checkboxarray, ss_gbn : ssgbnarray},
						  function(data) {
							 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
							 	var flag = data.flag;
							 	var message = data.message;
							 	
							 	if(flag == "0000"){						// 정상적으로 처리된 경우
							 		alert("코드가 삭제되었습니다");
							 		location.reload();
							 	}else if(flag == "NOT FOUND CODE"){
							 		alert("삭제 할 코드들이 먼저 선택되어야 합니다");
							 	}else if(flag == "IN USE SS_GBN"){
							 		alert("선택하신 코드 " + message + "는 지면에서 사용중인 코드입니다\n지면을 먼저 삭제하신 후에 삭제해주세요");
							 	}else{
							 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
							 	}
						  },
						  "json"
			    );
			}
		}
	});
	
	$("#orderbtn").click(function(){
		var code = $("#code").val();
		var url = "<%=webRoot%>/admin/code/changeCodeItemOrder.do?code=" + code; 
		window.open(url, "chnageorder", "width=450,height=300");
	});
	
	$("#applybtn").click(function(){
		var code = $("#code").val();
		var msg = "";
		
		if(code == "A0005"){
			msg = "테마코드를 적용하시겠습니까?";
		}else if(code == "A0008"){
			msg = "SmartUX 지면 항목 설정 정보를 적용하시겠습니까?";
		}else if(code == "A0009"){
			msg = "설정 정보를 적용하시겠습니까?";
		}else if(code == "A0012"){
			msg = "Cash 갱신 주기를 적용하시겠습니까?";
		}else if(code == "A0011"){
			msg = "RetryCash를 갱신하시겠습니까?";
		}
		
		if(confirm(msg)){
			$.post("<%=webRoot%>/admin/code/applyCache.do", 
					 {code : code,
				      callByScheduler : 'A'},
					  function(data) {
						 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
						 	var flag = data.flag;
						 	var message = data.message;
						 	
						 	if(flag == "0000"){						// 정상적으로 처리된 경우
						 		if(code == "A0005"){
						 			alert("테마코드가 적용되었습니다");
						 		}else if(code == "A0008"){
						 			alert("SmartUX 지면 항목 설정 정보가 적용되었습니다");
						 		}else if(code == "A0009"){
						 			alert("설정 정보가 적용되었습니다");
						 		}else if(code == "A0012"){
						 			alert("Cash 갱신 주기가 적용되었습니다");
						 		}else if(code == "A0011"){
						 			alert("Cash를 갱신하였습니다.");
						 		}
						 	}else{
						 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
						 	}
						 	
						 	console.log(data);
					  },
					  "json"
		    );
		}
	});
	
	$("#allchk").click(function(){
		var chkallchecked = false;
		
		if($("#allchk").is(":checked")){
			chkallchecked = true;
		}
		
		$("input[name='delchk']").attr("checked", chkallchecked);
	});
});

function view_code(code, item_code){
	var url = "<%=webRoot%>/admin/code/viewCodeItem.do?code=" + code + "&item_code=" + item_code;
	window.open(url, "viewcode", "width=450,height=250");
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
                                    코드
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
						                                            <td class="bold">SmartUX 코드값 목록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                    <table border="0" cellpadding="0" cellspacing="0" style="width:100%;">
						                                        <tbody>
						                                        <tr>
						                                            <td width="20%">
						                                            	<select id="selcode" name="selcode">
																			<c:forEach var="item" items="${result}" varStatus="status">
																				<c:choose>
																					<c:when test="${item.code == code}">
																						<option value="${item.code}" selected>${item.code_nm}</option>
																					</c:when>
																					<c:otherwise>
																						<option value="${item.code}">${item.code_nm}</option>
																					</c:otherwise>
																				</c:choose>
																			</c:forEach>
																		</select>
						                                            </td>
						                                            <td width="80%" align="left">
						                                            	<input id="searchbtn" src="/smartux_adm/images/admin/search.gif" border="0" height="22" type="image" width="65">
						                                            </td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                    <input type="hidden" id="code" name="code" value="${code}" /> 
						                                    <!-- <input type="hidden" id="smartUXManager" name="smartUXManager" value="<%//=id_decrypt %>" />-->
						                                    </form>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        
					                            <table border="0" cellspacing="0" cellpadding="0" class="board_list" style="width:100%;">
													<tr>
														<th width="3%" style="padding-top:8px"><input type="checkbox" id="allchk" name="allchk" value=""></th>
														<c:choose>
															<c:when test="${code=='A0012'}">
																<th width="60%">코드</th>
															</c:when>
															<c:otherwise>
																<th width="20%">코드</th>
															</c:otherwise>
														</c:choose>
														<c:choose>
															<c:when test="${code=='A0005' || code == 'A0008' || code == 'A0009'}">
																<c:if test="${code == 'A0005'}">
																	<th width="37%">코드명</th>
																	<th width="30%">영문 코드명</th>
																	<th width="10%">사용여부</th>
																</c:if>
																<c:if test="${code == 'A0008'}">
																	<th width="57%">코드명</th>
																	<th width="10%">SmartUX 타입</th>
																	<th width="10%">사용여부</th>
																</c:if>
																<c:if test="${code == 'A0009'}">
																	<th width="57%">코드명</th>
																	<th width="10%">어플타입</th>
																	<th width="10%">사용여부</th>
																</c:if>
															</c:when>
															<c:otherwise>
																<c:choose>
																	<c:when test="${code=='A0012'}">
																		<th width="27%">코드명</th>
																	</c:when>
																	<c:otherwise>
																		<th width="67%">코드명</th>
																	</c:otherwise>
																</c:choose>
																<th width="10%">사용여부</th>
															</c:otherwise>
														</c:choose>
													</tr>
													<c:choose>
														<c:when test="${itemresult==null || fn:length(itemresult) == 0}">
															<tr>
																<c:choose>
																	<c:when test="${code=='A0005' || code=='A0008' || code == 'A0009'}">
																		<td colspan="5" class="table_td_04">검색된 코드가 없습니다</td>
																	</c:when>
																	<c:otherwise>
																		<td colspan="4" class="table_td_04">검색된 코드가 없습니다</td>
																	</c:otherwise>
																</c:choose>
															</tr>
														</c:when>
														<c:otherwise>
															<c:forEach var="item" items="${itemresult}" varStatus="status">
																<tr>
																	<td class="table_td_04">
																		<input type="checkbox" name="delchk" value="${item.item_code}|${item.ss_gbn }">
																	</td>
																	<td class="table_td_04">${item.item_code}</td>
																	<td class="table_td_04" style="text-align: left;">
																		<a href="#" onclick="view_code('${code}', '${item.item_code}')">${item.item_nm}</a>
																	</td>
																	
																	
																	<c:if test="${code == 'A0005'}"> 
																		<td class="table_td_04">${item.item_enm}</td>
																	</c:if>
																	<c:if test="${code == 'A0008'}"> <!--  Smart Start 항목 정보 조회시엔 ss_gbn이 출력되어야 한다 -->
																		<td class="table_td_04">${item.ss_gbn}</td>
																	</c:if>
																	<c:if test="${code == 'A0009'}"> <!--  어플 타입 항목 정보 조회시엔 어플 타입 이름이 출력되어야 한다 -->
																		<td class="table_td_04">${item.app_type}</td>
																	</c:if>
																	
																	
																	<td class="table_td_04">
																		<c:choose>
																			<c:when test="${item.use_yn == 'Y'}">
																				예
																			</c:when>
																			<c:otherwise>
																				아니오
																			</c:otherwise>
																		</c:choose>
																	</td>
																</tr>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</table>
					                           
												
												<table border="0" cellpadding="0" cellspacing="0" style="width:95%;" align="center">
						                            <tbody>
						                            <tr>
						                                <td height="40" align="center">
															<c:if test="${code != 'A0011'}">
						                                	<span class="button small blue" id="regbtn">등록</span>
															</c:if>
						                                	<c:if test="${itemresult!=null && fn:length(itemresult) != 0}">
																<c:if test="${code != 'A0011'}">
																<span class="button small blue" id="delbtn">삭제</span>
																</c:if>
																<c:choose>
																	<c:when test="${code=='A0011' || code=='A0012'}">
																	</c:when>
																	<c:otherwise>
																		<span class="button small blue" id="orderbtn">순서바꾸기</span>
																	</c:otherwise>
																</c:choose>
																<c:if test="${code == 'A0005' || code == 'A0008' || code == 'A0009' || code == 'A0012' || code == 'A0011'}">
																	<span class="button small blue" id="applybtn">즉시적용</span>
																</c:if>
															</c:if>
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