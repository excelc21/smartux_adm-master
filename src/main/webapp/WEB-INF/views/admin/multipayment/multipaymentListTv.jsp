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
			AnyTime.picker('start_dt', { format: '%z-%m-%d', labelTitle: '날짜', labelHour: '시간', time: ''});
			AnyTime.picker('end_dt', { format: '%z-%m-%d', labelTitle: '날짜', labelHour: '시간', time: ''});

			$('#searchBtn').click(function () {
				var findName = $('#findName').val();
				var findValue = $('#findValue').val();
				var start_dt = $('#start_dt').val();
				var end_dt = $('#end_dt').val();
				if (start_dt != '' && end_dt != '') {
					var sDate = new Date(start_dt)
					var eDate = new Date(end_dt)
					if(eDate.getTime() - sDate.getTime() > 1000 * 60 * 60 * 24 * 31) {
						alert('최근 31일이내 거래만 조회 가능합니다. 시작일을 다시 설정해 주십시오.')
						return;
					}
				} else {
					alert('기간을 설정하세요');
					return;
				}
				var param = jQuery.param({findName: findName, findValue: findValue, startDt:start_dt, endDt:end_dt});
				location.href = '<%=webRoot%>/admin/multipayment/ppmlist.do?' + param;
			});

			$('#excelDownBtn').click(function () {
				var startDt = '<c:out value="${vo.start_dt}"/>';
				var endDt = '<c:out value="${vo.end_dt}"/>';
				var findName = '<c:out value="${vo.findName}"/>';
				var findValue = '<c:out value="${vo.findValue}"/>';
				
				var param = jQuery.param({ startDt:startDt , endDt:endDt , findName:findName , findValue:findValue});
				location.href = '<%=webRoot%>/admin/multipayment/downloadExcelFileTv.do?' + param;
			});

		});

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
                                                <td width="300" class="boldTitle">상품가입 내역 관리</td>
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

                                                                                    <td width="85">
                                                                                        <select class="select" id="findName">
                                                                                            <option value="PA_KEY" <c:if test="${empty vo.findName or 'PA_KEY' eq vo.findName}">selected</c:if>>결제키</option>
                                                                                            <option value="SA_ID" <c:if test="${'SA_ID' eq vo.findName}">selected</c:if>>가입번호</option>
                                                                                        </select>
                                                                                    </td>
                                                                                    <td><input type="text" id="findValue" value="${vo.findValue}" size="40" style="font-size: 12px;"/></td>
                                                                                    <td width="90" align="center"><input src="/smartux_adm/images/admin/search.gif" border="0" height="22" type="image" width="65" id="searchBtn"></td>
                                                                                </tr>

                                                                                <tr>
                                                                                    <td width="16" height="26"><img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9"/></td>
                                                                                    <td colspan="4">
                                                                                       	 시작일 : <input type="text" id="start_dt" size="14" title="시작일" style="text-align: center"
                                                                                                     <c:if test="${not empty vo.start_dt}">value="${vo.start_dt}"</c:if>/>
                                                                                        	종료일 : <input type="text" id="end_dt" size="14" title="종료일" style="text-align: center"
                                                                                                     <c:if test="${not empty vo.end_dt}">value="${vo.end_dt}"</c:if>/>
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
                                                                                    <td class="bold">전체 목록 (${vo.pageCount})</td>
                                                                                </tr>
                                                                            </table>
                                                                        </td>
                                                                    </tr>
                                                                </table>

                                                                <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list">
                                                                    <tr align="center">
                                                                    
                                                                        <th scope="col" width="10%">가입번호</th>
                                                                        <th scope="col" width="10%">상품코드</th>
                                                                        <th scope="col" width="*">상품명</th>
                                                                        <th scope="col" width="10%">상태</th>
                                                                        <th scope="col" width="15%">결제키</th>
                                                                        <th scope="col" width="10%">결제날짜</th>
                                                                    </tr>
                                                                    <c:if test="${empty list}">
                                                                        <tr align="center">
                                                                            <td class="table_td_04" colspan="12">데이터가 존재 하지  않습니다.
                                                                            </td>
                                                                        </tr>
                                                                    </c:if>
                                                                    <c:forEach var="item" items="${list}" >
                                                                        <tr align="center">
                                                                            <td class="table_td_04">${item.sa_id}</td>
                                                                            <td class="table_td_04">${item.product_code}</td>
                                                                            <td class="table_td_04"><a class="pointer" style="color: blue" href="./ppmview.do?findValue=${item.pa_key}&findName=PA_KEY&pageSize=${vo.pageSize}&pageNum=${vo.pageNum}&startDt=${vo.start_dt}&endDt=${vo.end_dt}">${item.product_name}</a></td>        
                                                                            <td class="table_td_04">
                                                                            <c:choose>
																				<c:when test="${item.pa_status eq 'S'}"><span style="color: blue">성공</span></c:when>
																				<c:when test="${item.pa_status eq 'B'}"><span>대기중</span></c:when>
																				<c:when test="${item.pa_status eq 'A'}"><span>진행중</span></c:when>
																				<c:when test="${item.pa_status eq 'C'}"><span>취소</span></c:when>
																				<c:when test="${item.pa_status eq 'E'}"><span style="color: red">실패</span></c:when>
																				<c:when test="${item.pa_status eq 'T'}"><span style="color: red">취소실패</span></c:when>
																				<c:otherwise>미정의 상태코드</c:otherwise>
																			</c:choose>                                                                        
                                                                            </td>
                                                                            <td class="table_td_04">${item.pa_key}</td>
                                                                            <td class="table_td_04">${item.pa_reg_dt}</td>
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


                                                                            <c:if test="${not empty vo.start_dt}">
                                                                                <c:set value="${tmpParams}&startDt=${vo.start_dt}" var="tmpParams"/>
                                                                            </c:if>
                                                                            <c:if test="${not empty vo.end_dt}">
                                                                                <c:set value="${tmpParams}&endDt=${vo.end_dt}" var="tmpParams"/>
                                                                            </c:if>

                                                                            <jsp:include page="/WEB-INF/views/include/naviControll.jsp">
                                                                                <jsp:param value="ppmlist.do" name="actionUrl"/>
                                                                                <jsp:param value="${tmpParams}" name="naviUrl"/>
                                                                                <jsp:param value="${vo.pageNum}" name="pageNum"/>
                                                                                <jsp:param value="${vo.pageSize}" name="pageSize"/>
                                                                                <jsp:param value="${vo.blockSize}" name="blockSize"/>
                                                                                <jsp:param value="${vo.pageCount}" name="pageCount"/>
                                                                            </jsp:include>
                                                                        </td>
                                                                    </tr>
                                                                </table>

                                                                <!-- 엑셀 다운로드 버튼 -->
                                                                <c:if test="${loginUser == 'amims' or loginUser == 'admin'}">
                                                                    <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" style="margin-top:10px">
                                                                        <tr>
                                                                            <td align="right">
                                                                                <span class="button small blue" id="excelDownBtn">엑셀 다운로드</span>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </c:if>

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
