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
	$('#deleteBtn').click(function(){
		var delList="";
		if(!$(".checkbox").is(":checked")){
			alert("삭제 할 항목을 선택하세요.");
			return;
		}else{
			var statCnt = 0;
            $(".checkbox").each(function () {
                if ($(this).is(":checked")) {
                	if(statCnt==0){
                		delList = delList+$(this).val();
                	}else{
                		delList = delList+","+$(this).val();
                	}
                	statCnt++;
                }
            });
		 }
		if(confirm("선택한 시즌을 삭제 하시겠습니까? \n삭제된 시즌은 서버 캐시에서 자동 삭제됩니다.")){	
		    $.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>처리 중..</b>"
			});
		    
		    $.ajax({
				url: './deleteProc.do',
				type:'POST',
				dataType: 'json',
				timeout : 30000,
			    data: {
			        "delList": delList,
			        "app_tp": "${vo.app_tp}"
			    },
			    dataType:"json",
				success:function(data){
					if(data.res=="0000"){
						alert("정상처리 되었습니다.");
					}else{
						alert("작업 중 오류가 발생하였습니다.\nflag : " + rtn.res + "\ Message : " + rtn.msg);
					}
				},
				error:function(){
					alert("작업 중 오류가 발생하였습니다.");
				},
				complete:function(){
					$.unblockUI();
					$(location).attr('href',"./seasonList.do?findName=${vo.findName}&findValue=${vo.findValue}&seriesYn=${vo.series_yn}&app_tp=${vo.app_tp}&pageNum=${vo.pageNum}"); 
				}
			});
		}
	});
		
	$('#applyBtn').click(function(){
		var msg = "전체 시즌을 상용에 적용하시겠습니까?";
		if(confirm(msg)){
			$.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>로딩중..</b>"
			});
			$.ajax({
			    url: '/smartux_adm/admin/season/applyCache.do',
			    type: 'POST',
			    dataType: 'json',
			    data: {        
			    },
			    error: function(){
			    	alert("작업 중 오류가 발생하였습니다");
			 		$.unblockUI();
			    },
			    success: function(rs){
			    	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
				 	var flag = rs.flag;
				 	var message = rs.message;
				 	if(flag == "0000"){// 정상적으로 처리된 경우
				 		alert("정상처리 되었습니다.");
	 			 	}else{
	 			 		alert("작업 중 오류가 발생하였습니다.\nflag : " + flag + "\nmessage : " + message);
	 			 	}
			 		$.unblockUI();
			    }
			});
		}
	});
	
	$('#exceptBtn').click(function(){
		var url = "<%=webRoot%>/admin/season/exceptPop.do";
	   	season_window = window.open(url, "exceptPop", "width=500,height=500,scrollbars=yes");
	});
});

function doSearch(){
	$("form[name=form1]").submit(); //폼 전송
}

function applyCacheById(seasonId, app_tp) {
	var msg = "선택한 시즌을 상용에 적용하시겠습니까?";
	if(confirm(msg)){
		$.blockUI({
			blockMsgClass: "ajax-loading",
			showOverlay: true,
			overlayCSS: { backgroundColor: '#CECDAD' } ,
			css: { border: 'none' } ,
			 message: "<b>로딩중..</b>"
		});
		$.ajax({
		    url: '/smartux_adm/admin/season/applyCacheById.do',
		    type: 'POST',
		    dataType: 'json',
		    data: {        
		    	'season_id' : seasonId,
		    	'app_tp' : app_tp
		    },
		    error: function(){
		    	alert("작업 중 오류가 발생하였습니다");
		 		$.unblockUI();
		    },
		    success: function(rs){
		    	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
			 	var flag = rs.flag;
			 	var message = rs.message;
			 	if(flag == "0000"){// 정상적으로 처리된 경우
			 		alert("정상처리 되었습니다.");
			 		$(location).attr('href',"./seasonList.do?findName=${vo.findName}&findValue=${vo.findValue}&seriesYn=${vo.series_yn}&app_tp=${vo.app_tp}&pageNum=${vo.pageNum}"); 
 			 	}else{
 			 		alert("작업 중 오류가 발생하였습니다.\nflag : " + flag + "\nmessage : " + message);
 			 	}
			 	$.unblockUI();
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
                                    	시즌 관리
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
			                                	<form id="form1" name="form1" method="get" action="./seasonList.do">
			                                	<tbody>
			                                	<tr>
				                                    <td width="15">&nbsp;</td>
				                                    <td width="80"><img src="/smartux_adm/images/admin/search_title4.gif" border="0" height="47" width="62"></td>
				                                    <td>
					                                    <table border="0" cellpadding="0" cellspacing="0" width="300">		                                    
		                                        			<tbody>
		                                        			<tr>
					                                            <td width="10">
					                                            	<img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9" />
					                                            </td>
					                                            <td>&nbsp;
					                                                <select class="select"  name="findName">
					                                                    <option <c:if test="${vo.findName == 'SEASON_TITLE'}">selected="selected"</c:if> value="SEASON_TITLE">제목</option>
					                                                </select>  
					                                            	<input type="text" name="findValue" value="${vo.findValue}" size="20" style="font-size: 12px;" onKeyPress="if(event.keyCode==13){ doSearch();}" />
					                                            </td>					
					                                            <td width="66" align="left">
					                                                <input src="/smartux_adm/images/admin/search.gif" border="0" height="22" type="image" width="65">
					                                            </td>
		                                        			</tr>
			                                    			</tbody>
			                                    		</table>
			                                    	</td>
			                                    	<td align="left" style="width:30px;padding:5px;">
		                                            	<select name="app_tp" onchange="doSearch()">
		                                            		<option value="N" <c:if test="${vo.app_tp == 'N'}">selected="selected"</c:if>>I30</option>		                                            				 
		                                            		<option value="I" <c:if test="${vo.app_tp == 'I'}">selected="selected"</c:if>>I20</option>		
		                                            	</select> 
		                                            </td>
			                                    	<td align="left" style="width:30px;padding:5px;">
		                                            	<select name="seriesYn" onchange="doSearch()">
		                                            		<option value=""  <c:if test="${vo.series_yn == ''}">selected="selected"</c:if>>전체</option>		
		                                            		<option value="Y" <c:if test="${vo.series_yn == 'Y'}">selected="selected"</c:if>>시리즈</option>		
		                                            		<option value="N" <c:if test="${vo.series_yn == 'N'}">selected="selected"</c:if>>단편</option>		                                            				 
		                                            	</select> 
		                                            </td>
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
						                                            <td class="bold">시즌 리스트</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list">
					                                <tbody>
					                                <tr align="center">
					                                    <th scope="col" width="5%">선택</th>
					                                    <th scope="col" width="5%">번호</th>
					                                    <th scope="col" width="35%">제목</th>
					                                    <th scope="col" width="10%">타입</th>
					                                    <th scope="col" width="15%">등록일시</th>
					                                    <th scope="col" width="15%">즉시적용</th>
					                                    <th scope="col" width="15%">즉시적용일시</th>
					                                </tr>
					                                <c:if test="${list == '[]' }">
					                                <tr align="center">
					                                    <td class="table_td_04" colspan="7">데이터가 존재 하지 않습니다.</td>					                                    
					                                </tr>
					                                </c:if>
					                            	<c:forEach items="${list}" var="season">
						                            	<tr align="center">
						                                    <td class="table_td_04"><input type="checkbox" class="checkbox" value="${season.season_id}" /></td>
						                                    <td class="table_td_04">${season.rowno}</td>
						                                    <td class="table_td_04" align="left">&nbsp;<a href="./updateSeason.do?season_id=${season.season_id}&findName=${vo.findName}&findValue=${vo.findValue}&seriesYn=${vo.series_yn}&app_tp=${vo.app_tp }&pageNum=${vo.pageNum}">${season.season_title}</a></td>
						                                    <td class="table_td_04">
						                                    	<c:if test="${season.series_yn == 'Y'}">시리즈</c:if>
						                                    	<c:if test="${season.series_yn != 'Y'}">단편</c:if>
						                                    </td>
						                                    <td class="table_td_04">${season.reg_dt}</td>
						                                    <td class="table_td_04"><a href="javascript:applyCacheById('${season.season_id}', '${season.app_tp }')"><span class="button small blue">즉시적용</span></a></td>
						                                    <td class="table_td_04">${season.cache_mod_dt}</td>
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
																<jsp:param value="seasonList.do" name="actionUrl"/>
																<jsp:param value="?findName=${vo.findName}&findValue=${vo.findValue}&seriesYn=${vo.series_yn}&app_tp=${vo.app_tp }" name="naviUrl" />
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
						                            	<td align="left">
						                                	<span class="button small blue" id="exceptBtn">제외카테고리 키워드 관리</span>
						                                </td>
						                            	<td align="right">
						                                	<a href="insertSeason.do?findName=${vo.findName}&findValue=${vo.findValue}&seriesYn=${vo.series_yn}&app_tp=${vo.app_tp }&pageNum=${vo.pageNum}"><span class="button small blue">등록</span></a>
						                                	<span class="button small blue" id="deleteBtn">삭제</span>
						                                	<c:if test="${auth_decrypt eq '00'}">
						                                	<span class="button small blue" id="applyBtn">즉시적용</span>
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