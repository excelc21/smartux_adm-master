<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>LG U+ IPTV SmartUX</title>
    <link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
    <link href="/smartux_adm/css/anytime_style.css" rel="stylesheet" type="text/css">
    <jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
    <script src="/smartux_adm/js/anytime.js"></script>

    <script type="text/javascript">
        $(document).ready(function () {
        	AnyTime.picker('startDt', { format: '%z-%m-%d', labelTitle: '날짜', labelHour: '시간', time: ''});
            AnyTime.picker('endDt', { format: '%z-%m-%d', labelTitle: '날짜', labelHour: '시간', time: ''});
            
            $('#searchBtn').click(function () {
            	var param = searchParam();
            	if (param == '') return;
            	location.href = '<%=webRoot%>/admin/paynow/list.do?' + param;
            });
            
            $('#excelDownBtn').click(function () {
            	var param = searchParam();
            	if (param == '') return;
                location.href = '<%=webRoot%>/admin/paynow/downloadExcelFile.do?' + param;
            });
        });

        function searchParam() {
            var findName = $('#findName').val();
            var findValue = $('#findValue').val();
            var status = $('#status').val();
            var startDt = $('#startDt').val();
            var endDt = $('#endDt').val();
            
            if (startDt != '' && endDt != '') {
            	var date = new Date();
            	date.setYear(date.getFullYear() - 1);
            	var yearDate = $.datepicker.formatDate('yy-mm-dd', date);
				
            	if (startDt < yearDate) {
            		alert('최근 일년이내 거래만 조회 가능합니다. 시작일을 다시 설정해 주십시오.');
            		return '';
            	}
        	}

            return jQuery.param({findName: findName, findValue: findValue, status: status, startDt:startDt, endDt:endDt});
        }
    </script>
</head>
<body>
<div id="divBody" style="height:100%">
<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
<tr>
    <td colspan="2" height="45" valign="bottom">
        <!-- top menu start -->
        <%@include file="/WEB-INF/views/include/top.jsp" %>
        <!-- top menu end -->
    </td>
</tr>
<tr height="10"/>
<tr>
<td colspan="2" valign="top">
<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
<tr>
<td width="4"/>
<td valign="top" width="180">
    <!-- left menu start -->
    <%@include file="/WEB-INF/views/include/left.jsp" %>
    <!-- left menu end -->
</td>
<td background="/smartux_adm/images/admin/bg_02.gif" width="35"/>
<td valign="top">
<table border="0" cellpadding="0" cellspacing="0" width="98%">
<tr style="display:block">
    <td height="42" width="100%">
        <table border="0" cellpadding="0" cellspacing="0" width="100%">
            <tr>
                <td width="300" class="boldTitle">Paynow 결제 리스트</td>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <td class="3_line" height="1"/>
</tr>
<tr>
    <td class="td_bg04" height="2"/>
</tr>
<tr>
<td>
<!-- ######################## body start ######################### -->
<table border="0" cellpadding="15" cellspacing="0" width="100%" align="center">
    <tr>
        <td>
            <table border="0" cellpadding="0" cellspacing="0" width="100%"
                   align="center">
                <tr>
                    <td>
                        <!-- 검색 시작 -->
                        <table border="0" cellpadding="0" cellspacing="0" width="100%"
                               class="search_table">
                            <tr>
                                <td width="15"/>
                                <td width="80"><img
                                        src="/smartux_adm/images/admin/search_title4.gif"
                                        border="0" height="47" width="62"></td>
                                <td>
                                    <table border="0" cellpadding="0" cellspacing="0">
                                        <tr>
                                            <td width="16" height="26"><img
                                                    src="/smartux_adm/images/admin/blt_05.gif"
                                                    height="9" width="9"/></td>
                                            <td width="120">
                                                <select class="select" id="status">
                                                    <option value="" <c:if test="${empty vo.status}">selected</c:if>>전체</option>
                                                    <option value="00" <c:if test="${'00' eq vo.status}">selected</c:if>>결제완료</option>
                                                    <option value="10" <c:if test="${'10' eq vo.status}">selected</c:if>>결제인증대기중</option>
                                                    <option value="20" <c:if test="${'20' eq vo.status}">selected</c:if>>결제승인요청중</option>
                                                    <option value="30" <c:if test="${'30' eq vo.status}">selected</c:if>>결제취소</option>
                                                    <option value="99" <c:if test="${'99' eq vo.status}">selected</c:if>>결제실패</option>
                                                </select>
                                            </td>
                                            <td width="85">
                                                <select class="select" id="findName">
                                                    <option value="TID" <c:if test="${empty vo.findName or 'TID' eq vo.findName}">selected</c:if>>TID</option>
                                                    <option value="SA_ID" <c:if test="${'SA_ID' eq vo.findName}">selected</c:if>>가입번호</option>
                                                    <option value="ALBUM_NAME" <c:if test="${'ALBUM_NAME' eq vo.findName}">selected</c:if>>앨범명</option>
                                                    <option value="CTN" <c:if test="${'CTN' eq vo.findName}">selected</c:if>>CTN</option>
                                                </select>
                                            </td>
                                            <td><input type="text" id="findValue"
                                                       value="${vo.findValue}"
                                                       size="40"
                                                       style="font-size: 12px;"/>
                                            </td>
                                            <td width="90" align="center"><input
                                                    src="/smartux_adm/images/admin/search.gif"
                                                    border="0" height="22"
                                                    type="image" width="65" id="searchBtn">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td width="16" height="26"><img
                                                    src="/smartux_adm/images/admin/blt_05.gif"
                                                    height="9" width="9"/></td>
                                            <td colspan="4">
									                    시작일 : <input type="text" id="startDt" size="14" title="시작일" style="text-align: center"
									               <c:if test="${not empty vo.startDt}">value="${vo.startDt}"</c:if>/>
									                    종료일 : <input type="text" id="endDt" size="14" title="종료일" style="text-align: center"
									               <c:if test="${not empty vo.endDt}">value="${vo.endDt}"</c:if>/>
									        </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                        <!-- 검색 종료 -->
                    </td>
                </tr>
                <tr>
                    <td class="3_line" height="1"/>
                </tr>
                <tr>
                    <td height="20"/>
                </tr>
                <!-- 리스트 시작 -->
                <tr>
                    <td>
                        <table border="0" cellpadding="0" cellspacing="0" width="100%"
                               align="center">
                            <tr>
                                <td height="25">
                                    <table border="0" cellpadding="0" cellspacing="0"
                                           width="100%">
                                        <tr>
                                            <td width="15"><img
                                                    src="/smartux_adm/images/admin/blt_07.gif">
                                            </td>
                                            <td class="bold">전체 목록</td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>

                        <table border="0" cellpadding="0" cellspacing="0" width="100%"
                               class="board_list">
                            <tr align="center">
                                <th scope="col" width="10%">TID</th>
                                <th scope="col" width="10%">가입번호</th>
                                <th scope="col">앨범명</th>
                                <th scope="col" width="10%">결제일</th>
                                <th scope="col" width="10%">CTN</th>
                                <th scope="col" width="10%">상태</th>
                            </tr>
                            <c:if test="${empty list}">
                                <tr align="center">
                                    <td class="table_td_04" colspan="6">데이터가 존재 하지  않습니다.
                                    </td>
                                </tr>
                            </c:if>
                            <c:forEach items="${list}" var="list">
                                <c:set var="i" value="${i+1}"/>
                                <tr align="center">
                                    <td class="table_td_04">${list.tid}</td>
                                    <td class="table_td_04">${list.sa_id}</td>
                                    <td class="table_td_04"><a href="./view.do?tid=${list.tid}&ptYear=${list.pt_year}&ptMonth=${list.pt_month}&pageNum=${vo.pageNum}&findName=${vo.findName}&findValue=${vo.findValue}&status=${vo.status}&startDt=${vo.startDt}&endDt=${vo.endDt}">${list.album_name}</a></td>
                                    <td class="table_td_04">${list.reg_dt}</td>
                                    <td class="table_td_04">${list.ctn}</td>
                                    <td class="table_td_04">${list.status}</td>
                                </tr>
                            </c:forEach>
                        </table>

                        <!-- 페이징 -->
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
                                    <c:if test="${not empty vo.status}">
                                        <c:set value="${tmpParams}&status=${vo.status}" var="tmpParams"/>
                                    </c:if>
                                    <c:if test="${not empty vo.startDt}">
                                        <c:set value="${tmpParams}&startDt=${vo.startDt}" var="tmpParams"/>
                                    </c:if>
                                    <c:if test="${not empty vo.endDt}">
                                        <c:set value="${tmpParams}&endDt=${vo.endDt}" var="tmpParams"/>
                                    </c:if>
                                    <jsp:include page="/WEB-INF/views/include/naviControll.jsp">
                                        <jsp:param value="list.do" name="actionUrl"/>
                                        <jsp:param value="${tmpParams}" name="naviUrl"/>
                                        <jsp:param value="${vo.pageNum }" name="pageNum"/>
                                        <jsp:param value="${vo.pageSize }" name="pageSize"/>
                                        <jsp:param value="${vo.blockSize }" name="blockSize"/>
                                        <jsp:param value="${vo.pageCount }" name="pageCount"/>
                                    </jsp:include>
                                </td>
                            </tr>
                        </table>
                        
                        <!-- 엑셀 다운로드 버튼 -->
				        <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" style="margin-top:10px">
				            <tr>
				                <td align="right">
				                    <span class="button small blue" id="excelDownBtn">엑셀 다운로드</span>
				                </td>
				            </tr>
				        </table>

                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tr>
                                <td height="1"/>
                            </tr>
                            <tr>
                                <td class="3_line" height="3"/>
                            </tr>
                        </table>
                    </td>
                </tr>
                <!-- 리스트 종료 -->
            </table>
        </td>
    </tr>
</table>
<!-- ########################### body end ########################## -->
</td>
</tr>
</table>
</td>
</tr>
</table>
</td>
</tr>
<tr>
    <td height="30"/>
</tr>
<tr>
    <td colspan="2" background="/smartux_adm/images/admin/copy_bg.gif" height="60" align="left">
        <!-- 하단 로그인 사용자 정보 시작 -->
        <%@include file="/WEB-INF/views/include/bottom.jsp" %>
        <!-- 하단 로그인 사용자 정보 종료 -->
    </td>
</tr>
</table>
</div>
</body>
</html>