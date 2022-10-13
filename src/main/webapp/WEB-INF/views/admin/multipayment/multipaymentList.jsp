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
				var findName = $('#findName').val();
				var findValue = $('#findValue').val();
				var status = $('#status').val();
				var paType = $('#paType').val();
				var startDt = $('#startDt').val();
				var discount_div = $('#discount_div').val();
				var endDt = $('#endDt').val();
				var mtype = $('#mtype').val();
				if (startDt != '' && endDt != '') {
					var sDate = new Date(startDt)
					var eDate = new Date(endDt)
					if(eDate.getTime() - sDate.getTime() > 1000 * 60 * 60 * 24 * 31) {
						alert('최근 31일이내 거래만 조회 가능합니다. 시작일을 다시 설정해 주십시오.')
						return;
					}
				} else {
					alert('기간을 설정하세요');
					return;
				}
				var param = jQuery.param({findName: findName, findValue: findValue, status: status, paType: paType, startDt:startDt, endDt:endDt, discount_div:discount_div, mtype:mtype});
				location.href = '<%=webRoot%>/admin/multipayment/list.do?' + param;
			});

			$('#excelDownBtn').click(function () {
				var findName = '<c:out value="${vo.findName}"/>';
				var findValue = '<c:out value="${vo.findValue}"/>';
				var status = '<c:out value="${vo.status}"/>';
				var paType = '<c:out value="${vo.paType}"/>';
				var startDt = '<c:out value="${vo.startDt}"/>';
				var endDt = '<c:out value="${vo.endDt}"/>';
				var discount_div = '<c:out value="${vo.discount_div}"/>';
				var mtype = '<c:out value="${vo.mtype}"/>';

				if (startDt != '' && endDt != '') {
					var sDate = new Date(startDt)
					var eDate = new Date(endDt)
					if(eDate.getTime() - sDate.getTime() > 1000 * 60 * 60 * 24 * 31) {
						alert('최근 31일이내 거래만 조회 가능합니다. 시작일을 다시 설정해 주십시오.')
						return;
					}
				} else {
					alert('기간을 설정하세요');
					return;
				}
				var param = jQuery.param({findName: findName, findValue: findValue, status: status, paType: paType, startDt:startDt, endDt:endDt, discount_div:discount_div, mtype:mtype});
				location.href = '<%=webRoot%>/admin/multipayment/downloadExcelFile.do?' + param;
			});

			$('.chk_discount_div').click(function() {
				if($(this).attr('id') === 'discount_div0') {
					if($('#discount_div0').is(':checked')){
						$('#discount_div').val('');
						$('#discount_div1').attr('checked', false);
						$('#discount_div2').attr('checked', false);
						$('#discount_div3').attr('checked', false);
					} else {
						$('#discount_div').val('000');
						$('#discount_div1').attr('checked', false);
						$('#discount_div2').attr('checked', false);
						$('#discount_div3').attr('checked', false);
					}
				} else {
					var discount_div = (($('#discount_div1').is(':checked') ? '1' : '0') + ($('#discount_div2').is(':checked') ? '1' : '0') + ($('#discount_div3').is(':checked') ? '1' : '0'));
					$('#discount_div').val(discount_div);
					$('#discount_div0').attr('checked', false);
				}

			})
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
                                                <td width="300" class="boldTitle">구매내역 리스트</td>
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
                                                                                            <option value="S" <c:if test="${'S' eq vo.status}">selected</c:if>>결제완료</option>
                                                                                            <option value="A" <c:if test="${'A' eq vo.status}">selected</c:if>>진행중</option>
                                                                                            <option value="B" <c:if test="${'B' eq vo.status}">selected</c:if>>결제대기</option>
                                                                                            <option value="C" <c:if test="${'C' eq vo.status}">selected</c:if>>결제취소</option>
                                                                                            <option value="E" <c:if test="${'E' eq vo.status}">selected</c:if>>결제실패</option>
                                                                                            <option value="T" <c:if test="${'T' eq vo.status}">selected</c:if>>취소실패</option>
                                                                                        </select>
                                                                                    </td>
                                                                                    <td width="120">
                                                                                        <select class="select" id="paType">
                                                                                            <option value="S" <c:if test="${'S' eq vo.paType or empty vo.paType}">selected</c:if>>기본</option>
                                                                                            <option value="P" <c:if test="${'P' eq vo.paType}">selected</c:if>>페이나우</option>
                                                                                            <option value="H" <c:if test="${'H' eq vo.paType}">selected</c:if>>휴대폰</option>
                                                                                            <option value="T" <c:if test="${'T' eq vo.paType}">selected</c:if>>TV페이</option>
                                                                                        </select>
                                                                                    </td>
                                                                                    <td width="85">
                                                                                        <select class="select" id="findName">
                                                                                            <option value="PA_KEY" <c:if test="${empty vo.findName or 'PA_KEY' eq vo.findName}">selected</c:if>>결제키</option>
                                                                                            <option value="SA_ID" <c:if test="${'SA_ID' eq vo.findName}">selected</c:if>>가입번호</option>
                                                                                        </select>
                                                                                    </td>
                                                                                    <td>
	                                                                                    <input type="text" id="findValue" value="${vo.findValue}" size="40" style="font-size: 12px;"/>
	                                                                                    <input type="hidden" id="mtype" value="${vo.mtype}" size="1" style="font-size: 12px;"/>
                                                                                    </td>
                                                                                    <td width="90" align="center"><input src="/smartux_adm/images/admin/search.gif" border="0" height="22" type="image" width="65" id="searchBtn"></td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td width="16" height="26"><img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9"/></td>
                                                                                    <td colspan="4">할인 수단 :
                                                                                        <label><input type="checkbox" id="discount_div0" class="chk_discount_div" <c:if test = "${vo.discount_div eq ''}">checked="checked"</c:if>>전체</label>
                                                                                        <label><input type="checkbox" id="discount_div1" class="chk_discount_div" <c:if test = "${fn:substring(vo.discount_div, 0, 1) eq '1'}">checked="checked"</c:if>>쿠폰</label>
                                                                                        <label><input type="checkbox" id="discount_div2" class="chk_discount_div" <c:if test = "${fn:substring(vo.discount_div, 1, 2) eq '1'}">checked="checked"</c:if>>멤버십</label>
                                                                                        <label><input type="checkbox" id="discount_div3" class="chk_discount_div" <c:if test = "${fn:substring(vo.discount_div, 2, 3) eq '1'}">checked="checked"</c:if>>TvPoint</label>
                                                                                        <input type="hidden" id="discount_div" name="discount_div" value="${vo.discount_div}"/>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td width="16" height="26"><img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9"/></td>
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
                                                                                    <td class="bold">전체 목록 (${vo.pageCount})</td>
                                                                                </tr>
                                                                            </table>
                                                                        </td>
                                                                    </tr>
                                                                </table>

                                                                <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list">
                                                                    <tr align="center">
                                                                        <!-- <th scope="col" width="5%"><input type="checkbox" id="checkAll"  onclick="selectAll()"/></th> -->
                                                                        <th scope="col" width="8%">가입번호</th>
                                                                        <th scope="col" width="8%">컨텐츠ID</th>
                                                                        <th scope="col" width="*">컨텐츠명</th>
                                                                        <th scope="col" width="8%">구매유형</th>
                                                                        <th scope="col" width="7%">결제금액(VAT포함)</th>
                                                                        <th scope="col" width="7%">결제수단</th>
                                                                        <th scope="col" width="7%">할인수단-쿠폰</th>
                                                                        <th scope="col" width="7%">할인수단-멤버십</th>
                                                                        <th scope="col" width="7%">할인수단-TVPoint</th>
                                                                        <th scope="col" width="5%">상태</th>
                                                                        <th scope="col" width="10%">결제키</th>
                                                                        <th scope="col" width="8%">결제날짜</th>
                                                                    </tr>
                                                                    <c:if test="${empty list}">
                                                                        <tr align="center">
                                                                            <td class="table_td_04" colspan="12">데이터가 존재 하지  않습니다.
                                                                            </td>
                                                                        </tr>
                                                                    </c:if>
                                                                    <c:forEach var="item" items="${list}" >
                                                                        <c:set var="discount_price" value="${fn:split(item.discount_price, ',')}" />
                                                                        <tr align="center">
                                                                            <td class="table_td_04">${item.sa_id}</td>
                                                                            <td class="table_td_04">${item.prod_id}</td>
                                                                            <td class="table_td_04"><a class="pointer" style="color: blue" href="./view.do?paKey=${item.pa_key}&ptYear=${item.pt_year}&type=${item.pa_type}&ptMonth=${item.pt_month}&pageNum=${vo.pageNum}&findName=${vo.findName}&findValue=${vo.findValue}&status=${vo.status}&paType=${vo.paType}&startDt=${vo.startDt}&endDt=${vo.endDt}&discount_div=${vo.discount_div}&mtype=${vo.mtype}">${item.prod_name}</a></td>
                                                                            <td class="table_td_04">
                                                                                <c:choose>
                                                                                    <c:when test="${item.buy_type eq '1' }">컨텐츠</c:when>
                                                                                    <c:when test="${item.buy_type eq '2' }">DATAFREE</c:when>
                                                                                    <c:when test="${item.buy_type eq '3' }">컨텐츠+DATAFREE</c:when>
                                                                                </c:choose>
                                                                            </td>
                                                                            <td class="table_td_04">
                                                                                <c:choose>
                                                                                    <c:when test="${item.total_price == '-1'}">
                                                                                        <c:choose>
                                                                                            <c:when test = "${item.buy_type eq '1'}">
                                                                                                <fmt:formatNumber value="${(item.prod_price-item.prod_price_sale) * 1.1}" pattern="#,###" />원
                                                                                            </c:when>
                                                                                            <c:when test = "${item.buy_type eq '2'}">
                                                                                                <fmt:formatNumber value="${(item.datafree_price-item.datafree_price_sale) * 1.1}" pattern="#,###" />원
                                                                                            </c:when>
                                                                                            <c:when test = "${item.buy_type eq '3'}">
                                                                                                <fmt:formatNumber value="${((item.prod_price-item.prod_price_sale) + (item.datafree_price-item.datafree_price_sale))  * 1.1}" pattern="#,###" />원
                                                                                            </c:when>
                                                                                        </c:choose>
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <fmt:formatNumber value="${item.total_price}" pattern="#,###" />원
                                                                                    </c:otherwise>
                                                                                </c:choose>
                                                                            </td>
                                                                            <td class="table_td_04">
                                                                                <c:choose>
                                                                                    <c:when test="${item.pa_type eq 'H'}">휴대폰</c:when>
                                                                                    <c:when test="${item.pa_type eq 'P'}">페이나우</c:when>
                                                                                    <c:when test="${item.pa_type eq 'S'}">기본</c:when>
                                                                                    <c:when test="${item.pa_type eq 'T'}">TV페이</c:when>
                                                                                    <c:otherwise>정의되지 않음</c:otherwise>
                                                                                </c:choose>
                                                                            </td>
                                                                            <td class="table_td_04"><fmt:formatNumber value="${discount_price[0]}" pattern="#,###" />원</td>
                                                                            <td class="table_td_04"><fmt:formatNumber value="${discount_price[1]}" pattern="#,###" />원</td>
                                                                            <td class="table_td_04"><fmt:formatNumber value="${discount_price[2]}" pattern="#,###" />원</td>
                                                                            <td class="table_td_04">
                                                                                <c:choose>
                                                                                    <c:when test="${item.pa_status eq 'S'}"><span style="color:blue">성공</span></c:when>
                                                                                    <c:when test="${item.pa_status eq 'B'}"><span>대기중</span></c:when>
                                                                                    <c:when test="${item.pa_status eq 'C'}"><span>취소</span></c:when>
                                                                                    <c:when test="${item.pa_status eq 'E'}"><span style="color: red">실패</span></c:when>
                                                                                    <c:when test="${item.pa_status eq 'T'}"><span style="color: red">취소실패</span></c:when>
                                                                                    <c:when test="${item.pa_status eq 'A'}"><span>진행중</span></c:when>
                                                                                    <c:otherwise>정의되지 않음</c:otherwise>
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
                                                                            <c:if test="${not empty vo.status}">
                                                                                <c:set value="${tmpParams}&status=${vo.status}" var="tmpParams"/>
                                                                            </c:if>
                                                                            <c:if test="${not empty vo.paType}">
                                                                                <c:set value="${tmpParams}&paType=${vo.paType}" var="tmpParams"/>
                                                                            </c:if>
                                                                            <c:if test="${not empty vo.startDt}">
                                                                                <c:set value="${tmpParams}&startDt=${vo.startDt}" var="tmpParams"/>
                                                                            </c:if>
                                                                            <c:if test="${not empty vo.endDt}">
                                                                                <c:set value="${tmpParams}&endDt=${vo.endDt}" var="tmpParams"/>
                                                                            </c:if>
                                                                            <c:if test="${not empty vo.discount_div}">
                                                                                <c:set value="${tmpParams}&discount_div=${vo.discount_div}" var="tmpParams"/>
                                                                            </c:if>
                                                                            <c:if test="${not empty vo.mtype}">
										                                        <c:set value="${tmpParams}&mtype=${vo.mtype}" var="tmpParams"/>
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
