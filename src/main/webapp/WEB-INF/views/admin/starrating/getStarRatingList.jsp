<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<title>LG U+ IPTV SmartUX</title>

<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
$(document).ready(function(){
	$("#applyBtn").click(function(){
		var msg = "즉시적용 하시겠습니까?";
		if(confirm(msg)){
			$.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>로딩중..</b>"
			});
			$.ajax({
			    url: './applyCache.do',
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
				 		alert("게시글이 상용에 적용되었습니다.");
				 		$.unblockUI();
				 	}else{
				 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
				 		$.unblockUI();
				 	}
			    }
			});
		}
	});
});

function doSearch(){
	$("form[name=form1]").submit();
}

//활성화상태 수정
function fnChangeUseYn(sr_id, use_yn){
	$.blockUI({
		blockMsgClass: "ajax-loading",
		showOverlay: true,
		overlayCSS: { backgroundColor: '#CECDAD' } ,
		css: { border: 'none' } ,
		 message: "<b>처리중..</b>"
	});
	
	$.ajax({
		url: './updateUseYn.do',
		type: "GET",
		data: { 
				"sr_id" : sr_id,
				"use_yn" : use_yn,
				"system_gb" : "${vo.system_gb}"
			  },
		dataType : "json",
		success: function (rtn) {
			if (rtn.res=="0000") {
				alert("정상처리 되었습니다.");
			} else if(rtn.res=="8001") {
				alert("이미 활성화 되어 있는 별점이 존재합니다.\n기존의 별점을 비활성 상태로 변경 후 다시 시도해 주세요.");
			} else {
				alert("작업 중 오류가 발생하였습니다\nflag : " + rtn.res + "\ Message : " + rtn.msg);
			}
			$.unblockUI();
		},
		complete: function () {
			$(location).attr('href',"./getStarRatingList.do?system_gb=${vo.system_gb }&use_yn=${vo.use_yn}&findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}"); 
		},
		error: function (e) {					
			alert("일시적인 장애가 발생하였습니다. 다시 시도해 주세요.");
			$.unblockUI();
		}
	});
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
						            	별점 관리
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
						                    	<form id="form1" name="form1" action="./getStarRatingList.do" method="get">
						                    	<input type="hidden" id="system_gb" name="system_gb" value="${vo.system_gb }">
						                        <!-- 검색 시작 -->
						                        <table border="0" cellpadding="0" cellspacing="0" width="100%"
						                               class="search_table">
						                            <tr>
						                                <td width="15"/>
						                                <td width="80"><img
						                                        src="/smartux_adm/images/admin/search_title4.gif"
						                                        border="0" height="47" width="62"></td>
						                                <td>
						                                    <table border="0" cellpadding="0"
						                                           cellspacing="0">
						                                        <tr>
						                                            <td width="10"><img
						                                                    src="/smartux_adm/images/admin/blt_05.gif"
						                                                    height="9" width="9"/></td>
						                                            <td>
						                                                <select class="select" id="use_yn" name="use_yn">
						                                                	<option value="" >모두</option>
						                                                	<option value="Y" <c:if test="${vo.use_yn eq 'Y'}">selected="selected"</c:if>>활성화</option>
						                                                	<option value="N" <c:if test="${vo.use_yn eq 'N'}">selected="selected"</c:if>>비활성화</option>
						                                                </select>
						                                            </td>
						                                            <td>
						                                                <select class="select" id="findName" name="findName">
						                                                	<option value="SR_TITLE" <c:if test="${vo.findName eq 'SR_TITLE'}">selected="selected"</c:if>>제목</option>
						                                                </select>
						                                            </td>
						                                            <td>
						                                            	<input type="text" id="findValue" name="findValue" value="${vo.findValue}" size="20" style="font-size: 12px;" onKeyPress="if(event.keyCode==13){ doSearch();}" />
						                                            </td>
						                                            <td>&nbsp;</td>
						                                            <td width="66" align="left"><input
						                                                    src="/smartux_adm/images/admin/search.gif"
						                                                    border="0" height="22"
						                                                    type="image" width="65" class="searchBtn"></td>
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
							                                        <td class="bold">별점 관리 목록</td>
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
						                                   <th scope="col" width="5%">번호</th>	
													 	   <th scope="col" width="70%">제목</th>
													 	   <th scope="col" width="15%">선택</th>
													 	   <th scope="col" width="10%">별점조회</th>
						                                </tr>
						                            </thead>    
													<tbody>
														<c:if test="${list == '[]' }"> 
							                                <tr align="center">
							                                    <td class="table_td_04" colspan="4">데이터가 존재 하지 않습니다.</td>					                                    
							                                </tr>
														</c:if>
						                                <c:forEach items="${list}" var="list">
						                                <tr align="center">
						                                    <td class="table_td_04">${vo.pageCount-(list.rowno-1)}</td>
						                                    <td class="table_td_04" align="left"><a href="./updateStarRating.do?system_gb=${vo.system_gb }&use_yn=${vo.use_yn}&findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&sr_pid=${list.sr_id}">${list.sr_title}</a></td>
						                                    <td class="table_td_04" align="center">
						                                    	<select id="change_use_yn" name="change_use_yn" onchange="javascript:fnChangeUseYn('${list.sr_id }', this.value);">
						                                    		<option value="Y" <c:if test="${list.use_yn eq 'Y' }">selected="selected"</c:if>>활성화</option>
						                                    		<option value="N" <c:if test="${list.use_yn eq 'N' }">selected="selected"</c:if>>비활성화</option>
						                                    	</select>
						                                    </td>
						                                    <td class="table_td_04"><a href="./getAlbumHistoryList.do?system_gb=${vo.system_gb }&use_yn=${vo.use_yn}&sr_pid=${list.sr_id }&sr_findName=${vo.findName}&sr_findValue=${vo.findValue}&sr_pageNum=${vo.pageNum}"><span class="button small blue" id="historyBtn">조회</span></a></td>
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
																<jsp:param value="getStarRatingList.do" name="actionUrl"/>
																<jsp:param value="?system_gb=${vo.system_gb }&use_yn=${vo.use_yn }&findName=${vo.findName}&findValue=${vo.findValue}" name="naviUrl" />
																<jsp:param value="${vo.pageNum }" name="pageNum"/>
																<jsp:param value="${vo.pageSize }" name="pageSize"/>
																<jsp:param value="${vo.blockSize }" name="blockSize"/>
																<jsp:param value="${vo.pageCount }" name="pageCount"/>			  
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
						                                	<a href="./insertStarRating.do?system_gb=${vo.system_gb }&use_yn=${vo.use_yn}&findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}"><span class="button small blue" id="insertBtn">등록</span></a>
						                                	<span class="button small blue" id="applyBtn">즉시적용</span>
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
	        
	        <!-- 하단 로그인 사용자 정보 종료 -->
	    </td>
	 </tr>
</tbody>
</table>
</div>
</body>
</html>