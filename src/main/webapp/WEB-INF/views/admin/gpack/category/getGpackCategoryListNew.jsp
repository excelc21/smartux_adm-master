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
		var view_gb = "INSERT";
		
		var url = "<%=webRoot%>/admin/gpack/category/getGpackCategoryView.do";
		var param = "pack_id=" + pack_id + "&category_id=&view_gb=" + view_gb ;
		window.open(url+"?"+param, "category_add", "width=450,height=300");
	});
	
	// 삭제
	$("#deletebtn").click(function (){
		var pack_id = $("#pack_id").val();
		var checkeditems = $("input[name='delchk']:checked");
		
		if(checkeditems.length == 0){
			alert("삭제할 카테고리를 선택해주세요");
		}else{
			var size = checkeditems.length;
			var checkboxarray = new Array();
			
			for(var i=0; i < size; i++){
				var checkboxval = $(checkeditems[i]).val();
				checkboxarray.push(checkboxval);
			}

			//alert("카테고리 삭제 >> "+checkboxarray);
			var postUrl = "<%=webRoot%>/admin/gpack/category/deleteGpackCategory.do";			
			$.post(	postUrl, 
					{pack_id : pack_id, category_ids : checkboxarray},
					function(data) {
						// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
						var flag = data.flag;
						var message = data.message;
						
						if(flag == "0000"){						// 정상적으로 처리된 경우
							alert("카테고리가 삭제되었습니다");
							location.reload();
						}else{
							alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
						}
					},
					"json"
		    );
		}
	});
	
	// 순서변경
	$("#orderbybtn").click(function (){
		var pack_id = $("#pack_id").val();
		var url = "<%=webRoot%>/admin/gpack/category/updateGpackCategoryOrderby.do?pack_id=" + pack_id; 
		window.open(url, "category_order", "width=450,height=300");
	});
	
	// 미리보기
	$("#previewbtn").click(function(){
		var pack_id = $("#pack_id").val();
		var url = "<%=webRoot%>/admin/gpack/category/previewGpackCategory.do?pack_id=" + pack_id; 
		window.open(url, "preview", "scrollbars=yes,width=800,height=600");
	});

	// 미리보기(상용)
	$("#previewBizBtn").click(function(){
		var pack_id = $("#pack_id").val();
		var url = "<%=webRoot%>/admin/gpack/category/previewGpackCategory.do?pack_id=" + pack_id+"&preview_gb=BIZ"; 
		window.open(url, "preview", "scrollbars=yes,width=800,height=600");
	});
});


//수정(조회)
function fn_categoryView(pack_id, category_id){
	var view_gb = "MODIFY";
	
	var url = "<%=webRoot%>/admin/gpack/category/getGpackCategoryView.do";
	var param = "pack_id=" + pack_id + "&category_id="+category_id+"&view_gb="+view_gb ;

	window.open(url+"?"+param, "category_modify", "width=450,height=300");
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
                                    카테고리
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
						                                            <td class="bold">카테고리 목록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        
						                        <form id="frm_category_list" name="frm_category_list" action="/admin/gpack/category/getCategoryView.do" method="get">
						                        	<input type="hidden" id="pack_id" name="pack_id" value="${pack_id }" />
						                        	<input type="hidden" id="category_id" name="category_id" />
						                        </form>
					                            <table border="0" cellspacing="0" cellpadding="0" class="board_list" style="width:100%">
					                            	<colgroup>
					                            		<col width="5%">
					                            		<col width="10%">
					                            		<col width="*">
					                            		<col width="10%">
					                            	</colgroup>
													<tr>
														<th><input type="checkbox" id="allchk" name="allchk"></th>
														<th>순번</th>
														<th>카테고리명</th>
														<th>사용여부</th>
													</tr>
													<c:choose>
														<c:when test="${result==null || fn:length(result) == 0}">
															<tr>
																<td colspan="9" class="table_td_04">검색된 카테고리가 없습니다</td>
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
																	<td class="table_td_04" style="text-align: left;">
																		<a href="javascript:fn_categoryView('${item.pack_id}', '${item.category_id}');">${item.category_nm}</a>
																	</td>
																	<td class="table_td_04" style="text-align:center;">
																		<!-- 사용여부 -->
																		<c:choose>
																			<c:when test="${item.use_yn eq 'Y' }">사용</c:when> 
																			<c:otherwise>사용안함</c:otherwise>
																		</c:choose>
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