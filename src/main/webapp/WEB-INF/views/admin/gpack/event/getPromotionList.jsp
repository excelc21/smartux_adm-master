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
	
	// 전체 선택
	$("#allchk").click(function(){
		if($("#allchk").is(":checked")){
	        $("input[name=delchk]").each(function(i) {
	            $(this).attr('checked', true);
	        });
		}else{
		    $("input[name=delchk]").each(function(i) {
	            $(this).attr('checked', false);
	        });
		}
	});
	
	// 추가
	$("#insertbtn").click(function (){ 
		//alert("카테고리 추가버튼");
		var pack_id = $("#pack_id").val();
		
		var url = "<%=webRoot%>/admin/gpack/event/getGpackPromotionView.do";
		var param = "pack_id=" + pack_id;
	<c:if test="${packVO.template_type ne 'TP001'}">
		window.open(url+"?"+param, "promotion_add", "width=600,height=300");
	</c:if>
	<c:if test="${packVO.template_type ne 'TP002'}">
		window.open(url+"?"+param, "promotion_add", "width=600,height=350");
	</c:if>
	
	});
	
	// 삭제
	$("#deletebtn").click(function (){
		var pack_id = $("#pack_id").val();
		var checkeditems = $("input[name='delchk']:checked");
		var smartUXManager = $("#smartUXManager").val();
		
		if(checkeditems.length == 0){
			alert("삭제할 프로모션를 선택해주세요");
		}else{
			if(confirm("프로모션를 삭제할 경우 하위 콘텐츠는 자동으로 삭제됩니다.\n선택된 프로모션들을 삭제하시겠습니까?")){
				var size = checkeditems.length;
				var checkboxarray = new Array();
				
				for(var i=0; i < size; i++){
					var checkboxval = $(checkeditems[i]).val();
					checkboxarray.push(checkboxval);
				}
	
				//alert("카테고리 삭제 >> "+checkboxarray);
				var postUrl = "<%=webRoot%>/admin/gpack/event/deleteGpackPromotion.do";			
				$.post(	postUrl, 
						{pack_id : pack_id, category_ids : checkboxarray, smartUXManager : smartUXManager},
						function(data) {
							// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
							var flag = data.flag;
							var message = data.message;
							
							if(flag == "0000"){						// 정상적으로 처리된 경우
								alert("프로모션이 삭제되었습니다");
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
	
	// 순서변경
	$("#orderbybtn").click(function (){
		var pack_id = $("#pack_id").val();
		var url = "<%=webRoot%>/admin/gpack/event/changeGpackPromotionOrder.do?pack_id=" + pack_id; 
		window.open(url, "promotion_order", "width=450,height=300");
	});
	
	// 미리보기
	$("#previewbtn").click(function(){
		var pack_id = $("#pack_id").val();
		var template_type = $("#template_type").val();
		var url = "<%=webRoot%>/admin/gpack/event/previewGpackPromotion.do?pack_id=" + pack_id + "&template_type=" + template_type; 
		window.open(url, "preview", "scrollbars=yes,width=800,height=600");
	});
});


//수정(조회)
function fn_categoryView(pack_id, category_id){
	
	var url = "<%=webRoot%>/admin/gpack/event/getGpackPromotionView.do";
	var param = "pack_id=" + pack_id + "&category_id="+category_id;
<c:if test="${packVO.template_type ne 'TP001'}">
	window.open(url+"?"+param, "promotion_modify", "width=600,height=300");
</c:if>
<c:if test="${packVO.template_type ne 'TP002'}">
	window.open(url+"?"+param, "promotion_modify", "width=600,height=350");
</c:if>
}

//플레이리스트 관리
function fn_playlistView(pack_id, category_id){
	var url = "<%=webRoot%>/admin/gpack/event/getGpackPromotionPlaylistView.do";
	var pop_name = "category";
	var features = "width=600,height=600,scrollbars=yes";
	var param = "pack_id=" + pack_id + "&category_id=" + category_id;
	
	window.open(url+"?"+param, pop_name, features);
}

//콘텐츠 관리
function fn_contentsManage(pack_id, category_id, auto_yn){
	var url = "";
	var features = "";
	var pop_name = "";
	var param = "pack_id=" + pack_id + "&category_id=" + category_id;
	
	if(auto_yn === 'Y'){
		url = "<%=webRoot%>/admin/gpack/event/getGpackOneCategoryView.do";
		pop_name = "category";
		features = "width=500,height=400";
	}else {
		url = "<%=webRoot%>/admin/gpack/contents/getGpackContentsList.do";
		pop_name = "contnets";
		features = "width=800,height=600,scrollbars=yes";
	}
	
	window.open(url+"?"+param, pop_name, features);
}

//콘텐츠 순서관리
function fn_contentsOrderby(pack_id, category_id){
	var url = "<%=webRoot%>/admin/gpack/contents/updateGpackContentsOrderby.do";
	var param = "pack_id=" + pack_id + "&category_id=" + category_id;
	
	window.open(url+"?"+param, "contents_order", "width=450,height=300");
}
</script>
</head>
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
                <tr style="display:block">
                    <td height="42" width="100%">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tbody>
                            <tr>
                                <td width="300" class="boldTitle">
                                	<!-- 카테고리 타이틀 -->
                                    프로모션 관리
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
						                                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
						                                        <tbody>
						                                        <tr>
						                                            <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
						                                            <td class="bold">${pack_default_title} 프로모션 목록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        
						                        <form id="frm_category_list" name="frm_category_list" action="/admin/gpack/category/getPromotionView.do" method="get">
						                        	<input type="hidden" id="pack_id" name="pack_id" value="${pack_id }" />
						                        	<input type="hidden" id="category_id" name="category_id" />
						                        	<input type="hidden" id="template_type" name="template_type" value="${packVO.template_type}"/>
						                        	<input type="hidden" id="smartUXManager" name="smartUXManager" value="<%=id_decrypt %>" />
						                        </form>
					                            <table border="0" cellspacing="0" cellpadding="0" class="board_list" style="width:100%">
					                            	<colgroup>
					                            		<col width="3%">
					                            		<col width="5%">
					                            		<c:if test="${packVO.template_type eq 'TP002'}">
					                            		<col width="7%">
					                            		</c:if>
					                            		<col width="*">
					                            		<col width="7%">
					                            		<col width="7%">
					                            		<col width="15%">
					                            		<col width="10%">
					                            		<col width="15%">
					                            	</colgroup>
													<tr>
														<th style="padding-top:8px"><input type="checkbox" id="allchk" name="allchk"></th>
														<th>순번</th>
														<c:if test="${packVO.template_type eq 'TP002'}">
														<th>영상표시</th>
														</c:if>
														<th>타이틀</th>
														<th>자동/수동</th>
														<th>사용여부</th>
														<th>플레이리스트</th>
														<th>콘텐츠관리</th>
														<th>콘텐츠순서바꾸기</th>
													</tr>
													<c:choose>
														<c:when test="${result==null || fn:length(result) == 0}">
															<tr>
																<td colspan="9" class="table_td_04">검색된 프로모션이 없습니다</td>
															</tr>
														</c:when>
														<c:otherwise>
															<c:forEach var="item" items="${result}" varStatus="status">
																<tr>
																	<td class="table_td_04">
																		<input type="checkbox" name="delchk" value="${item.category_id}">
																	</td>
																	<td class="table_td_04">
																		<c:out value="${status.index+1}"></c:out>
																	</td>
																	<c:if test="${packVO.template_type eq 'TP002'}">
																	<td class="table_td_04" style="text-align:center;">
																		<c:if test="${item.use_yn eq 'Y'}"><c:if test="${item.promotion_video_gb ne 'PV003'}">
																			표시
																		</c:if></c:if>&nbsp;
																	</td>
																	</c:if>
																	<td class="table_td_04" style="text-align: left;">
																		<a href="javascript:fn_categoryView('${item.pack_id}', '${item.category_id}');">${item.category_nm}</a>
																	</td>
																	<td class="table_td_04" style="text-align:center;">
																		<!-- 자동여부 -->
																		<c:choose>
																			<c:when test="${item.auto_yn eq 'Y' }">자동</c:when>
																			<c:otherwise>수동</c:otherwise>
																		</c:choose>
																	</td>
																	<td class="table_td_04" style="text-align:center;">
																		<!-- 사용여부 -->
																		<c:choose>
																			<c:when test="${item.use_yn eq 'Y' }">사용</c:when>
																			<c:otherwise>사용안함</c:otherwise>
																		</c:choose>
																	</td>
																	<td class="table_td_04" align="center">
																		<!-- 플레이리스트관리 버튼 -->
																		<c:if test="${item.promotion_video_gb eq 'PV002'}">
																		<span class="button small blue" onclick="javascript:fn_playlistView('${item.pack_id}', '${item.category_id}');">플레이리스트</span>
																		</c:if>&nbsp;
																	</td>
																	<td class="table_td_04" align="center">
																		<!-- 콘텐츠관리 버튼 -->
																		<c:if test="${item.c_use_yn eq 'Y' }">
																			<span class="button small blue" onclick="javascript:fn_contentsManage('${item.pack_id}', '${item.category_id}', '${item.auto_yn}');">콘텐츠</span>
																		</c:if>
																		<c:if test="${item.c_use_yn eq 'N' }">
																			<span class="button small red" onclick="javascript:fn_contentsManage('${item.pack_id}', '${item.category_id}', '${item.auto_yn}');">콘텐츠(X)</span>
																		</c:if>
																	</td>
																	<td class="table_td_04" align="center">
																		<!-- 콘텐츠순서관리 버튼 -->
																		<c:if test="${item.contents_count > 1 && item.auto_yn ne 'Y'}">
																		<span class="button small blue" onclick="javascript:fn_contentsOrderby('${item.pack_id}', '${item.category_id}');">콘텐츠순서바꾸기</span>
																		</c:if>&nbsp;
																	</td>
																</tr>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</table>
					                           
												<table border="0" cellpadding="0" cellspacing="0" style="width:100%" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="40" align="center">
						                                	<!-- 버튼 (추가, 삭제, 순서변경) -->
						                                	<span class="button small blue" id="insertbtn">추가</span>
						                                    <c:if test="${fn:length(result) != 0}">
						                                	<span class="button small blue" id="deletebtn">삭제</span>
						                                	<span class="button small blue" id="orderbybtn">순서바꾸기</span>
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