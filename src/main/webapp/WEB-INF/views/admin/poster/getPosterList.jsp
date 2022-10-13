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
	$('#searchBtn').click(function () {
		var findName = $('#findName').val();
		var findValue = $('#findValue').val();
		var findServiceType = $('#findServiceType').val();

		var param = jQuery.param({findName: findName, findValue: findValue, findServiceType: findServiceType});
		location.href = '<%=webRoot%>/admin/poster/getPosterList.do?' + param;
	});
	
	$("#regbtn").click(function(){
		window.open("<%=webRoot%>/admin/poster/viewPoster.do", "viewposter", "width=650,height=600,resizable=yes,scrollbars=yes");
	});
	
	$("#delbtn").click(function(){
		var checkeditems = $("input[name='delchk']:checked");
		var checkeditemslength = checkeditems.length;
		
		if(checkeditemslength == 0){
			alert("삭제할 항목을 선택해주세요");
		}else{
			if(confirm("체크된 전용 포스터들을 삭제하시겠습니까?")){
				var smartUXManager = $("#smartUXManager").val();
				var size = checkeditemslength;
				var albumidarray = new Array();
				var servicetypearray = new Array();
				
				for(var i=0; i < size; i++){
					var checkboxval = $(checkeditems[i]).val().split("^");
					albumidarray.push(checkboxval[0]);
					servicetypearray.push(checkboxval[1]);
				}
				
				$.post("<%=webRoot%>/admin/poster/deletePoster.do", 
						 {album_id : albumidarray, service_type : servicetypearray},
						  function(data) {
							 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
							 	var flag = data.flag;
							 	var message = data.message;
							 	
							 	if(flag == "0000"){						// 정상적으로 처리된 경우
							 		alert("전용 포스터들이 삭제되었습니다");
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
});

function view_poster(service_type,album_id,album_name,width_img,height_img){
	var url = "<%=webRoot%>/admin/poster/viewPoster.do?service_type=" + service_type + "&album_id=" + album_id + "&album_name=" + encodeURI(encodeURIComponent(album_name)) + "&w_file_name=" + width_img + "&h_file_name=" + height_img;
	window.open(url, "viewposter", "width=700,height=600,resizable=yes,scrollbars=yes");
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
                                        전용포스터 관리
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
                                            <table border="0" cellpadding="0" cellspacing="0" width="100%"
                                                   class="search_table">
                                                <tr>
                                                    <td width="15"/>
                                                    <td width="80"><img src="/smartux_adm/images/admin/search_title4.gif" border="0" height="47" width="62"></td>
                                                    <td>
                                                        <table border="0" cellpadding="0" cellspacing="0">
                                                            <tr>
                                                                <td width="16" height="26"><img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9"/></td>
                                                                <td width="80">
                                                                    <select class="select" id="findServiceType">
                                                                        <option value="" <c:if test="${empty vo.findServiceType}">selected</c:if>>전체</option>
                                                                        <option value="I" <c:if test="${'I' eq vo.findServiceType}">selected</c:if>>IPTV</option>
                                                                        <option value="S" <c:if test="${'S' eq vo.findServiceType}">selected</c:if>>시니어</option>
                                                                    </select>
                                                                </td>
                                                                <td width="80">
                                                                    <select class="select" id="findName">
                                                                        <option value="ALBUM_ID" <c:if test="${empty vo.findName or 'ALBUM_ID' eq vo.findName}">selected</c:if>>앨범ID</option>
                                                                        <option value="ALBUM_NAME" <c:if test="${'ALBUM_NAME' eq vo.findName}">selected</c:if>>앨범명</option>
                                                                    </select>
                                                                </td>
                                                                <td width="100"><input type="text" id="findValue" value="${vo.findValue}" size="40" style="font-size: 12px;"/></td>
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
						                                            <td class="bold">전용포스터 목록 (${vo.pageCount})</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        
					                            <table border="0" cellspacing="0" cellpadding="0" class="board_list" style="width:100%">
													<tr>
														<th width="3%" style="padding-top:8px"><input type="checkbox" id="allchk" name="allchk" value=""></th>
														<th width="5%">순번</th>
														<th>서비스</th>
														<th>앨범ID</th>
														<th>앨범명</th>
														<th>포스터(가로)</th>
														<th>포스터(세로)</th>
														<th>등록일</th>
													</tr>
													<c:choose>
														<c:when test="${result==null || fn:length(result) == 0}">
															<tr>
																<td colspan="8" class="table_td_04">등록된 전용포스터가 없습니다</td>
															</tr>
														</c:when>
														<c:otherwise>
															<c:forEach var="item" items="${result}" varStatus="status">
															<c:set var="i" value="${i+1}" />
																<tr>
																	<td class="table_td_04">
																		<input type="checkbox" name="delchk" value="${item.album_id}^${item.service_type}">
																	</td>
																	<td align="center">
																		${i+(vo.pageNum-1)*vo.pageSize}
																	</td>
																	<td class="table_td_04">
																		<c:if test="${item.service_type eq 'I'}">IPTV</c:if>
																		<c:if test="${item.service_type eq 'S'}">시니어</c:if>
																	</td>
																	<td class="table_td_04">
																		<a href="#" onclick="view_poster('${item.service_type}','${item.album_id}','${item.album_name}','${item.width_img}','${item.height_img}')">${item.album_id}</a>
																	</td>
																	<td class="table_td_04">
																		<a href="#" onclick="view_poster('${item.service_type}','${item.album_id}','${item.album_name}','${item.width_img}','${item.height_img}')">${item.album_name}</a>
																	</td>
																	<td class="table_td_04">
																		${item.width_img}
																	</td>
																	<td class="table_td_04">
																		${item.height_img}
																	</td>
																	<td class="table_td_04">
																		${item.created}
																	</td>
																</tr>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</table>
					                            <form id="frm" name="frm" method="post">
													<input type="hidden" id="smartUXManager" name="smartUXManager" value="<%=id_decrypt %>" />
												</form>
												
												<!-- 페이징 시작-->
			                                  	<table border="0" cellpadding="0" cellspacing="0" width="100%">
			                                      	<tr>
			                                          	<td height="5"/>
			                                      	</tr>
			                                      	<tr>
			                                          	<td align="center">
			                                              	<c:set value="?" var="tmpParams"/>
			                                              	<c:if test="${not empty vo.pageSize}">
			                                                  	<c:set value="${tmpParams}&pageSize=${vo.pageSize}" var="tmpParams"/>
			                                              	</c:if>
			                                              	<c:if test="${not empty vo.findName}">
			                                                  	<c:set value="${tmpParams}&findName=${vo.findName}" var="tmpParams"/>
			                                              	</c:if>
			                                              	<c:if test="${not empty vo.findValue}">
			                                                  	<c:set value="${tmpParams}&findValue=${vo.findValue}" var="tmpParams"/>
			                                              	</c:if>
			                                              	<c:if test="${not empty vo.findServiceType}">
			                                                  	<c:set value="${tmpParams}&findServiceType=${vo.findServiceType}" var="tmpParams"/>
			                                              	</c:if>
			                                              	<jsp:include page="/WEB-INF/views/include/naviControll.jsp">
			                                                  	<jsp:param value="getPosterList.do" name="actionUrl"/>
			                                                  	<jsp:param value="${tmpParams}" name="naviUrl"/>
			                                                  	<jsp:param value="${vo.pageNum }" name="pageNum"/>
			                                                  	<jsp:param value="${vo.pageSize }" name="pageSize"/>
			                                                  	<jsp:param value="${vo.blockSize }" name="blockSize"/>
			                                                  	<jsp:param value="${vo.pageCount }" name="pageCount"/>
			                                              	</jsp:include>
			                                          	</td>
			                                      	</tr>
			                                  	</table>
												<!-- 페이징 종료-->
												<table border="0" cellpadding="0" cellspacing="0" style="width:100%;" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="40" align="center">
						                                	<span class="button small blue" id="regbtn">등록</span>
						                                	<c:if test="${result!=null && fn:length(result) > 0}">
																<span class="button small blue" id="delbtn">삭제</span>
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