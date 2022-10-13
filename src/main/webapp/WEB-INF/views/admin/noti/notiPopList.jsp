<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<title>LG U+ SmartUX</title>

<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
$(document).ready(function(){		
	$(".checkbox").click(function (e) {
		//체크했다면 자신을 제외한 다른 체크를 해제
		$(".checkbox:checked").not(this).removeAttr("checked"); 
	});	
	$("#choiceBtn").click(function (e) {
       	if("${hiddenName}"!="") $(opener.document).find("#${hiddenName}").val($('.checkbox:checked').val());
       	if("${textName}"!="") $(opener.document).find("#${textName}").val($('.checkbox:checked').attr("name"));
       	if("${textHtml}"!="") $(opener.document).find("#${textHtml}").html($('.checkbox:checked').attr("name"));
    	self.close();
	});
});

function doSearch(){
	$("#pageNum").attr("value","1");	
	$("#form1").submit();
}
</script>
<body leftmargin="0" topmargin="0">
<div id="divBody" style="height:100%">
<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
    <tbody>
    	<tr>
    		<td valign="top">
               	<!-- ######################## body start ######################### -->
               		<table border="0" cellpadding="15" cellspacing="0" width="100%" align="center" >
                   		<tbody>
                   		<tr>
                   			<td>
                   				<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" >
		                            <tbody>
		                            <tr>
		                            <td>
		                                <!-- 검색 시작 -->
										<form id="form1" method="get" action="./getNotiPopList.do">	
										<input type="hidden" name="pageNum" id="pageNum" value="${vo.pageNum}">
										<input type="hidden" name="bbs_gbn" id="bbs_gbn" value="${vo.bbs_gbn}">
										<input type="hidden" name="bbs_id" id="bbs_id" value="${vo.bbs_id}">
										<input type="hidden" id="scr_tp" name="scr_tp" value="${vo.scr_tp}">
										<input type="hidden" id="hiddenName" name="hiddenName" value="${hiddenName}">
										<input type="hidden" id="textHtml" name="textHtml" value="${textHtml}">
										<input type="hidden" id="textName" name="textName" value="${textName}">
										<input type="hidden" id="delimiter" name="delimiter" value="${s_delimiter}">
										
		                                <table border="0" cellpadding="0" cellspacing="0" width="100%" class="search_table" >
		                                
		                                	<tbody >
		                                	<tr >
			                                    <td width="15">&nbsp;</td>
			                                    <td width="80"><img src="/smartux_adm/images/admin/search_title4.gif" border="0" height="47" width="62"></td>
			                                    <td>
				                                    <table border="0" cellpadding="0" cellspacing="0" width="280">		                                    
	                                        			<tbody>
	                                        			<tr><td>&nbsp;</td></tr>
	                                        			<tr>
				                                            <td width="10">
				                                            	<img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9" />
				                                            </td>
				                                            <td>
				                                                <select class="select"  name="findName">
				                                                    <option <c:if test="${vo.findName == 'noti.title'}">selected="selected"</c:if> value="TITLE">제목</option>					                                                    
				                                                </select>  
				                                            	<input type="text" name="findValue" value="${vo.findValue}" size="20" style="font-size: 12px;" onKeyPress="if(event.keyCode==13){ doSearch();}" />
				                                            </td>					
				                                            <td width="66" align="left">
				                                                <img src="/smartux_adm/images/admin/search.gif" border="0" height="22"  width="65" onclick="doSearch();" style="cursor:pointer;">
				                                            </td>
				                                         </tr>
		                                    			</tbody>
		                                    		</table>
		                                    		<%-- </form> --%>
		                                    	</td>
				                            </tr> 					                            
				                        	</tbody>
				                        </table>
				                        </form>
				                        <!-- 검색 종료 -->
				                    </td>
					                </tr>
					                <tr>
					                    <td class="3_line" height="1"></td>
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
					                                            <td class="bold">공지 리스트</td>
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
												 	   <th scope="col">제목</th>
					                                   <th scope="col" width="13%">종류</th>
					                                   <th scope="col" width="15%">타입</th>
					                                   <th scope="col" width="10%">검수</th>									                                   
					                                   <th scope="col" width="10%">등록일</th>
					                                </tr>
					                                
					                                <c:if test="${list == '[]' }"> 
						                                <tr align="center">
						                                    <td class="table_td_04" colspan="7">데이터가 존재 하지 않습니다.</td>					                                    
						                                </tr>
													</c:if>
				                                </tbody>
				                          
				                                <c:forEach items="${list}" var="noti">
				                            	<c:set var="i" value="${i+1}" />
				                            	
					                            	<tr align="center">
					                            		<!-- 체크 박스 -->
					                                    <td class="table_td_04" >
					                                    	<input type="checkbox" class="checkbox" value="${noti.bbs_id}${delimiter}${noti.reg_no}" name="${noti.title}"/>
					                                    </td>
					                                    
					                                    <!-- 번호 -->
					                                    <td class="table_td_04" >
					                                   		${vo.pageCount-(noti.rowno-1)}
					                                    </td>
					                                    
					                                    <!-- 제목 -->						                                    						                                    
					                                    <td class="table_td_04">
					                                    	${noti.title}						                                    	 
					                                    </td>
					                                    
					                                    <!-- 공지 종류 -->						                                    						                                    
					                                    <td class="table_td_04">
					                                    	${noti.bbs_nm}						                                    	 
					                                    </td>
					                                    
					                                    <!-- 타입 -->
					                                    <td class="table_td_04" >
					                                    	<!-- 별도 테이블화 할 필요성을 못느껴서 하드코딩 -->
				                                    		<c:choose>
				                                    			<c:when test="${noti.ev_type=='ev1'}">
						                                    		VOD 상세정보
						                                    	</c:when>
				                                    			<c:when test="${noti.ev_type=='ev2'}">
						                                    		월정액 가입
						                                    	</c:when>
				                                    			<c:when test="${noti.ev_type=='ev3'}">
						                                    		참여하기
						                                    	</c:when>
				                                    			<c:when test="${noti.ev_type=='ev4'}">
						                                    		초대하기
						                                    	</c:when>
				                                    			<c:when test="${noti.ev_type=='ev5'}">
						                                    		URL링크
						                                    	</c:when>
						                                    	<c:when test="${noti.ev_type=='ev6'}">
						                                    		특정카테고리
						                                    	</c:when>
				                                    			<c:when test="${noti.ev_type=='ev7'}">
						                                    		공지/이벤트 게시글
						                                    	</c:when>
				                                    			<c:when test="${empty noti.ev_type}">
						                                    		&nbsp;
						                                    	</c:when>
						                                    </c:choose>
					                                    </td>
														
														<!-- 검수 -->
														<td class="table_td_04 ">
					                                   		<c:if test="${noti.is_adt=='0'}">확인</c:if>
															<c:if test="${noti.is_adt=='1'}">완료</c:if>
														</td>
														
														<!-- 등록일 -->
						                                <td class="table_td_04 ">
						                                    	${fn:substring(noti.reg_dt, 0, 10)} 
					                                    </td>
					                                </tr>
				                            	</c:forEach>
				                            </table>
				                            
				                            <!-- 페이징 -->
				                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
					                            <tbody>
					                            <tr>
					                                <td height="5"></td> 
					                            </tr>
					                            <tr>
					                            	<td align="center">						                            		
					                            		 <jsp:include page="/WEB-INF/views/include/naviControll.jsp">
															<jsp:param value="getNotiPopList.do" name="actionUrl"/>
															<jsp:param value="?findName=${vo.findName}&findValue=${vo.findValue}&bbs_id=${vo.bbs_id }&bbs_gbn=${vo.bbs_gbn }&scr_tp=${vo.scr_tp}&hiddenName=${hiddenName}&textHtml=${textHtml}&textName=${textName}&delimiter=${s_delimiter}" name="naviUrl" />
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
				                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
					                            <tbody>
					                            <tr>
					                                <td align="right">
					                                	<span class="button small blue" id="choiceBtn" name="choiceBtn">선택</span>
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
</div>
</body>
</html>