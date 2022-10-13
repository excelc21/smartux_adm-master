<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<title>LG U+ IPTV SmartUX</title>

<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<link href="/smartux_adm/css/anytime_style.css" rel="stylesheet" type="text/css">
<script src="/smartux_adm/js/anytime.js"></script>
<script type="text/javascript">
$(document).ready(function(){
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
						                    	<form id="form1" name="form1" action="./getAlbumHistoryList.do" method="get">
						                    	<input type="hidden" id="sr_pid" name="sr_pid" value="${sr_vo.sr_pid }"/>
						                    	<input type="hidden" id="system_gb" name="system_gb" value="${sr_vo.system_gb }"/>
						                    	<input type="hidden" id="use_yn" name="use_yn" value="${sr_vo.use_yn }"/>
						                    	<input type="hidden" id="pageNum" name="pageNum" value="${vo.pageNum }"/>
						                    	<input type="hidden" id="sr_findName" name="sr_findName" value="${sr_vo.findName }"/>
						                    	<input type="hidden" id="sr_findValue" name="sr_findValue" value="${sr_vo.findValue }"/>
						                    	<input type="hidden" id="sr_pageNum" name="sr_pageNum" value="${sr_vo.pageNum }"/>
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
						                                                <select class="select" id="findName" name="findName">
						                                                	<option value="ALBUM_ID" <c:if test="${vo.findName eq 'ALBUM_ID'}">selected="selected"</c:if>>앨범ID</option>
						                                                	<option value="ALBUM_NAME" <c:if test="${vo.findName eq 'ALBUM_NAME'}">selected="selected"</c:if>>앨범이름</option>
						                                                </select>
						                                            <td>
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
							                                        <td class="bold">별점 내역</td>
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
						                                   <th scope="col" width="3%">번호</th>
						                                   <th scope="col" width="10%">앨범ID</th>
						                                   <th scope="col" width="10%">앨범이름</th>
						                                   <th scope="col" width="7%">별점평균</th>
						                                </tr>
						                            </thead>    
													<tbody>
														<c:if test="${list == '[]' }"> 
							                                <tr align="center">
							                                    <td class="table_td_04" colspan="9">데이터가 존재 하지 않습니다.</td>					                                    
							                                </tr>
														</c:if>
						                                <c:forEach items="${list}" var="list">
						                                <tr align="center">
						                                    <td class="table_td_04">${vo.pageCount-(list.rowno-1)}</td>
						                                    <td class="table_td_04">${list.album_id }</td>
						                                    <td class="table_td_04"><a href="./getSrHistoryList.do?system_gb=${sr_vo.system_gb }&use_yn=${sr_vo.use_yn}&sr_pid=${sr_vo.sr_pid }&album_findName=${vo.findName}&album_findValue=${vo.findValue}&album_pageNum=${vo.pageNum}&sr_findName=${sr_vo.findName}&sr_findValue=${sr_vo.findValue}&sr_pageNum=${sr_vo.pageNum}&album_id=${list.album_id}">
						                                    <B>
						                                    <c:choose>
						                                    	<c:when test="${not empty list.album_name }">
																	${list.album_name }		                                    
						                                    	</c:when>
						                                    	<c:otherwise>
						                                    		미편성 앨범
						                                    	</c:otherwise>
						                                    </c:choose>
						                                    </B>
						                                    </a></td>
						                                    <td class="table_td_04">${list.sr_point }</td>
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
																<jsp:param value="getAlbumHistoryList.do" name="actionUrl"/>
																<jsp:param value="?system_gb=${sr_vo.system_gb }&use_yn=${sr_vo.use_yn}&sr_pid=${sr_vo.sr_pid }&findName=${vo.findName}&findValue=${vo.findValue}&sr_findName=${sr_vo.findName}&sr_findValue=${sr_vo.findValue}&sr_pageNum=${sr_vo.pageNum}" name="naviUrl" />
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
						                                	<a href="./albumExcelDown.do?sr_pid=${sr_vo.sr_pid }&findName=${vo.findName}&findValue=${vo.findValue}"><span class="button small blue">엑셀출력</span></a>
						                                	<a href="./getStarRatingList.do?system_gb=${sr_vo.system_gb }&use_yn=${sr_vo.use_yn}&sr_pid=${sr_vo.sr_pid }&findName=${sr_vo.findName}&findValue=${sr_vo.findValue}&pageNum=${sr_vo.pageNum}"><span class="button small blue">별점목록</span></a>
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