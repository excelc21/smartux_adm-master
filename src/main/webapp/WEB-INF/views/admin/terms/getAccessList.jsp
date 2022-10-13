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
	
	$("#delbtn").click(function(){
		
		var checkeditems = $("input[name='delchk']:checked");
		var checkeditemslength = checkeditems.length;
		
		if(checkeditemslength == 0){
			alert("삭제할 약관 마스터를 선택해주세요");
		}else{
			var size = checkeditemslength;
			var checkboxarray = new Array();
			var tmp;

			for(var i=0; i < size; i++){
				tmp =  $(checkeditems[i]).val();
				checkboxarray.push(tmp);
			}
			
			if(confirm("선택된 약관 마스터들을 삭제 하시겠습니까?")){
				
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
						 		location.reload();
						 	}else{
						 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
						 		location.reload();
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

function doSearch(){
	$("form[name=form1]").submit();
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
                <tr style="display:block">
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
			                            <td>
			                                <!-- 검색 시작 -->
			                                <table border="0" cellpadding="0" cellspacing="0" width="100%" class="search_table">
			                                	<tbody>
			                                	<tr>
				                                    <td width="15">&nbsp;</td>
				                                    <td width="80"><img src="/smartux_adm/images/admin/search_title4.gif" border="0" height="47" width="62"></td>
				                                    <td>
														<form id="form1" method="get" action="./getAccessList.do">
					                                    <table border="0" cellpadding="0" cellspacing="0">
					                                        <tr>
					                                            <td width="10"><img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9"/></td>
					                                            <td>
					                                                <select class="select" id="findName" name="findName">
					                                                	<option value="ACCESS_MST_TITLE" <c:if test="${vo.findName eq 'ACCESS_MST_TITLE'}">selected="selected"</c:if>>제목</option>
					                                                </select>
					                                            </td>
					                                            <td>
					                                            	<input type="text" id="findValue" name="findValue" value="${vo.findValue}" size="20" style="font-size: 12px;" onKeyPress="if(event.keyCode==13){ doSearch();}" />
					                                            </td>
					                                            <td>
					                                                <select class="select" id="display" name="display">
					                                                	<option value="" >모두</option>
					                                                	<option value="M" <c:if test="${vo.display eq 'M'}">selected="selected"</c:if>>Mobile</option>
					                                                	<option value="P" <c:if test="${vo.display eq 'P'}">selected="selected"</c:if>>PC</option>
					                                                	<option value="T" <c:if test="${vo.display eq 'T'}">selected="selected"</c:if>>TV</option>
					                                                </select>
					                                            </td>					                                            
					                                            <td>&nbsp;</td>
					                                            <td width="66" align="left"><input src="/smartux_adm/images/admin/search.gif" border="0" height="22"type="image" width="65" class="searchBtn"></td>
					                                        </tr>
						                                </table>
			                                    		</form>
			                                    	</td>
					                            </tr> 
					                        	</tbody>
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
						                                            <td class="bold">약관 마스터 관리</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list">
					                            	<thead>
						                                <tr align="center">
						                                   <th width="3%" style="padding-top:8px">
						                                   	<input type="checkbox" id="allchk" name="allchk" value="">
						                                   </th>
						                                   <th scope="col" width="4%">번호</th>	
													 	   <th scope="col" width="7%">서비스타입</th>
													 	   <th scope="col" width="7%">노출화면</th>
													 	   <th scope="col" width="64%">약관마스터명</th>
													 	   <th scope="col" width="15%">최종 수정일</th>
						                                </tr>
						                            </thead>  
					                                <tbody>
						                                <c:if test="${list == '[]' }"> 
							                                <tr align="center">
							                                    <td class="table_td_04" colspan="7">데이터가 존재 하지 않습니다.</td>					                                    
							                                </tr>
														</c:if>
							                            <c:forEach items="${list}" var="list">
							                                <tr align="center">
							                                	<td class="table_td_04" style="text-align: center;">
																	<input type="checkbox" name="delchk" value="${list.access_mst_id}">
																</td>
							                                    <td class="table_td_04">${vo.pageCount-(list.rowno-1)}</td>
							                                    <td class="table_td_04" align="center">
								                                    <c:forEach items="${serviceType}" var="item2">
								                                    	<c:if test="${list.service_type eq item2.code}">${item2.name}</c:if>
								                                    </c:forEach>
							                                    </td>
							                                    <td class="table_td_04" align="center">
							                                    	<c:forEach items="${displayType}" var="item3">
							                                    		<c:if test="${list.display eq item3.code}">${item3.name}</c:if>
							                                    	</c:forEach>
							                                    </td>
							                                    <td class="table_td_04" align="left" style="word-break:break-all;"><a href="./updateAccess.do?display=${vo.display}&findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&access_mst_id=${list.access_mst_id}">${list.access_mst_title}</a></td>
							                                    <td class="table_td_04" align="center">${list.mod_dt}</td>
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
																<jsp:param value="getAccessList.do" name="actionUrl"/>
																<jsp:param value="?findName=${vo.findName}&findValue=${vo.findValue}&display=${vo.display}" name="naviUrl" />
																<jsp:param value="${vo.pageNum }" name="pageNum"/>
																<jsp:param value="${vo.pageSize }" name="pageSize"/>
																<jsp:param value="${vo.blockSize }" name="blockSize"/>
																<jsp:param value="${vo.pageCount }" name="pageCount"/>			  
															</jsp:include>
						                            	</td>
						                            </tr>
						                        	</tbody>
						                        </table>
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            <tbody>
						                            <tr>
						                                <td align="right">
							                                <a href="./insertAccess.do?display=${vo.display}&findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}"><span class="button small blue" id="insertBtn">등록</span></a>
						                                	<span class="button small blue" id="delbtn">삭제</span>
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