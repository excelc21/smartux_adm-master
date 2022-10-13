<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ HDTV</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>

<script type="text/javascript">
$(document).ready(function () {
    $('#addBtn').click(function () {
        location.href = '${pageContext.request.contextPath}/admin/ads/getInsertAdsView.do?masterID=${vo.masterID}&scr_tp=${vo.scr_tp}&adsListMode=${vo.adsListMode}';
    });

    $('#deleteBtn').click(function () {
        deleteAds();
    });

    $('#applyBtn').click(function () {
        applyCache('A');
    });
    
    //2021.12.07 AMIMS 개선 : 부분즉시적용 추가
    $('#applyIdBtn').click(function () {
        applyCache('P');
    });

    $('#versionApplyBtn').click(function () {
    	versionApplyBtn();
    });

    $('#changeOrder').click(function () {
        popupCenter('${pageContext.request.contextPath}/admin/ads/changeOrder.do?masterID=${vo.masterID}', '순서변경', 600, 300)
    });

    $('#adsListMode, #masterID').change(function () {
        search();
    });

});

function applyCache(type) {
	var msg = '게시글을 상용에 적용하시겠습니까?';

    if (confirm(msg)) {
		var master_id = '';
		
		if('P' == type){
			//부분즉시적용일 경우
			master_id = '${vo.masterID}';
		}
		
    	var check = false;

        $.ajax({
            url: '${pageContext.request.contextPath}/admin/ads/checkLastApply.do',
            type: 'POST',
            dataType: 'json',
            async: false,
            data: {
            },
            error: function () {
                $.unblockUI();
                alert('작업 중 오류가 발생하였습니다');
            },
            success: function (rs) {
                // 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
                var flag = rs.flag;
                if(flag == "0000"){
                    check = true;
                }else{
                    check = false;
                }
            }
        });
        
        
        $.ajax({
            url: '${pageContext.request.contextPath}/admin/ads/applyCache.do',
            type: 'POST',
            dataType: 'json',
            data: {master_id: master_id},
            error: function () {
                alert('작업 중 오류가 발생하였습니다');
            },
            success: function (rs) {
                // 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
                var flag = rs.flag;

                var message = rs.message;

                if (flag == '0000') {                       // 정상적으로 처리된 경우
                    console.log(message);
                    alert('게시글이 상용에 적용되었습니다.');
                    location.reload();
                } else {
                    alert('작업 중 오류가 발생하였습니다\nflag : ' + flag + '\nmessage : ' + message);
                }
            }
        });
    }
}


function versionApplyBtn() {
    var msg = '상단메뉴 즉시적용항목입니다. 변경하시겠습니까?';

    if (confirm(msg)) {

    	var check = false;

    	$.ajax({
            url: '${pageContext.request.contextPath}/admin/ads/checkLastApply.do',
            type: 'POST',
            dataType: 'json',
            async: false,
            data: {
            },
            error: function () {
            	$.unblockUI();
                alert('작업 중 오류가 발생하였습니다');
            },
            success: function (rs) {
                // 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
                var flag = rs.flag;
                if(flag == "0000"){
                    check = true;
                }else{
                    check = false;
                }
            }
        });
    	
        if(check){
	    	$.blockUI();
	    	
	        $.ajax({
	            url: '${pageContext.request.contextPath}/admin/ads/applyVersionCache.do',
	            type: 'POST',
	            dataType: 'json',
	            data: {
	            },
	            error: function () {
	            	$.blockUI();
	                alert('작업 중 오류가 발생하였습니다');
	            },
	            success: function (rs) {
	                // 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
	                var flag = rs.flag;
	
	                if (flag == '0000') {                       // 정상적으로 처리된 경우
	                	applyVersionCache2();
	                } else {
	                	$.unblockUI();
	                    alert('작업 중 오류가 발생하였습니다.');
	                }
	            }
	        });
        }else{
            alert("상단메뉴 즉시적용 후 5분간 즉시적용 할 수 없습니다.");
        }
    }
}

function applyVersionCache2(){
	$.post("<%=webRoot%>/admin/code/applyCache.do", 
	    {code : "A0009",
	     callByScheduler : 'A'},
	     function(data) {
	           // 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
	           var flag = data.flag;
	           var message = data.message;

	           applyVersionCache3();
	           
	           /* if(flag == "0000"){                     // 정상적으로 처리된 경우
	        	   applyVersionCache3();
	           }else{
	               alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
	           } */
	           
	           console.log(data);
	     },
	     "json"
   );
}

function applyVersionCache3(){
	$.ajax({
        url: '${pageContext.request.contextPath}/admin/ads/applyCache.do',
        type: 'POST',
        dataType: 'json',
        data: {
        },
        error: function () {
        	$.unblockUI();
            alert('작업 중 오류가 발생하였습니다');
        },
        success: function (rs) {
            // 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
            var flag = rs.flag;

            var message = rs.message;

            if (flag == '0000') {                       // 정상적으로 처리된 경우
            	$.unblockUI();
                console.log(message);
                alert('상단메뉴가 변경되었습니다.');
                location.reload();
            } else {
            	$.unblockUI();
                alert('작업 중 오류가 발생하였습니다.');
            }
        }
    });
}

function updateAds(num) {
    location.href = '${pageContext.request.contextPath}/admin/ads/getUpdateAdsView.do?num=' + num+'&scr_tp=${vo.scr_tp}&adsListMode=${vo.adsListMode}';
}

function deleteAds() {
    var numbers = '';

    $('.checkbox').each(function () {
        if ($(this).is(':checked')) {
            numbers += $(this).val() + ',';
        }
    });

    if (0 >= numbers.length) {
        alert('삭제 할 항목을 선택하세요.');
        return;
    }


    if (confirm('삭제 하시겠습니까?')) {
        $.ajax({
            url: '${pageContext.request.contextPath}/admin/ads/deleteAds',
            type: 'POST',
            dataType: 'json',
            data: {numbers: numbers},
            success: function (data) {
                var flag = data.flag;
                var message = data.message;

                if (flag == '0000') {
                    alert('삭제되었습니다.');
                    location.reload();
                } else {
                    alert('작업 중 오류가 발생하였습니다\nflag : ' + flag + '\nmessage : ' + message);
                }
            },
            error: function () {
                alert('작업 중 오류가 발생하였습니다');
                location.reload();
            }
        });
    }
}

function search() {
    var buffer = [];
    var adsListMode = $('#adsListMode').val();
    var masterID = $('#masterID').val();
    var findName = $('#findName').val();
    var findValue = $('#findValue').val();  
    location.href =  '${pageContext.request.contextPath}/admin/ads/getAdsList.do?scr_tp=${vo.scr_tp}&findName=' + findName + '&findValue=' + findValue + '&adsListMode=' + adsListMode + '&masterID=' + masterID
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
                <td width="300" class="boldTitle">배너</td>
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
<td <c:if test="${masterYn eq 'N'}">align="center"</c:if>>
<!-- ######################## body start ######################### -->
<c:if test="${masterYn eq 'N'}">
    <span align="center" style="font-size:32px;font-weight:bold;">배너 마스터를 등록하시기 바랍니다.</span>
</c:if>
<table border="0" cellpadding="15" cellspacing="0" width="100%" align="center" <c:if test="${masterYn eq 'N'}">style="display:none"</c:if>>
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
                <td width="80"><img src="/smartux_adm/images/admin/search_title4.gif" border="0" height="47" width="62"></td>
                <td>
                    <table border="0" cellpadding="0" cellspacing="0" width="280">
                        <tr>
                            <td width="10"><img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9"/></td>
                            <td>
                                <select class="select" id="findName">
                                    <option value="title">제목</option>
                                </select>
                            </td>
                            <td><input type="text" id="findValue" value="${vo.findValue}" size="20" style="font-size: 12px;"/>
                            </td>
                            <td width="66" align="left">
                                <img src="/smartux_adm/images/admin/search.gif" border="0" height="22" type="image" width="65" onclick="search();" style="cursor:pointer;">
                            </td>
                        </tr>
                    </table>
                </td>
                <td align="left" style="width: 30px; padding: 5px;">
                    <select class="select" id="adsListMode">
                        <option <c:if test="${vo.adsListMode eq 'total'}">selected="selected"</c:if> value="total">전체 목록</option>
                        <option <c:if test="${vo.adsListMode eq 'live'}">selected="selected"</c:if> value="live">라이브된 목록</option>
                        <option <c:if test="${vo.adsListMode eq 'expire'}">selected="selected"</c:if> value="expire">만료된 목록</option>
                    </select>
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
    <td height="10"/>
</tr>

<tr>
    <td align="left" style="width: 30px; padding: 5px;">
        <select class="select" id="masterID">
            <c:forEach items="${masterList}" var="master">
                <option <c:if test="${master.masterID eq vo.masterID}">selected="selected"</c:if> value="${master.masterID}">${master.masterName}</option>
            </c:forEach>
        </select>
    </td>
</tr>

<tr>
    <td height="10"/>
</tr>

<!-- 리스트 시작 -->
<tr>
    <td>
        <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
            <tr>
                <td height="25">
                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
                        <tr>
                            <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
                            <td class="bold">전체 목록</td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>

        <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list">
            <tr align="center">
                <th scope="col" width="5%">선택</th>
                <th scope="col" width="5%">번호</th>
                <th scope="col">제목</th>
                <th scope="col" width="5%">노출 시간</th>
                <th scope="col" width="10%">시작일</th>
                <th scope="col" width="10%">만료일</th>
                <th scope="col" width="10%">배너 타입</th>
                <th scope="col" width="5%">순서</th>
                <th scope="col" width="5%">날짜 타입</th>
                <th scope="col" width="5%">라이브여부</th>
                <th scope="col" width="5%">노출등급</th>
            </tr>
            <c:if test="${vo.list == '[]' }">
                <tr align="center">
                    <td class="table_td_04" colspan="11">데이터가 존재 하지 않습니다.</td>
                </tr>
            </c:if>
            <c:forEach items="${vo.list}" var="list">
                <c:set var="i" value="${i+1}"/>
                <tr align="center">
                    <!-- 체크 박스 -->
                    <td class="table_td_04"><input type="checkbox" class="checkbox" value="${list.number}"/></td>
                    <!-- 번호 -->
                    <td class="table_td_04" >${((vo.pageNum - 1) * vo.blockSize) + i}</td>
                    <!-- 제목 -->
                    <td class="table_td_04"><a href="javascript:updateAds('${list.number}')">${list.title}</a></td>
                    <!-- 배너 시간 -->
                    <td class="table_td_04">${list.rolTime}초</td>
                    <!-- 시작일 -->
                    <td class="table_td_04"><fmt:formatDate value="${list.startTime}" type="date" pattern="yyyy-MM-dd HH:mm"/></td>
                    <!-- 만료일 -->
                    <td class="table_td_04"><fmt:formatDate value="${list.expiredTime}" type="date" pattern="yyyy-MM-dd HH:mm"/></td>
                    <!-- 배너 타입 -->
                    <td class="table_td_04">
                        <c:if test="${list.type eq '1'}">실시간 채널</c:if>
                        <c:if test="${list.type eq '2'}">컨텐츠</c:if>
                        <c:if test="${list.type eq '3'}">카테고리</c:if>
                        <c:if test="${list.type eq '4'}">외부URL</c:if>
                        <c:if test="${list.type eq '5'}">앱링크</c:if>
                        <c:if test="${list.type eq '6'}">신청</c:if>
                    </td>
                    <!-- 순서 -->
                    <td class="table_td_04">${list.order}</td>
                    <!-- 날짜 타입 -->
                    <td class="table_td_04">
                        <c:if test="${empty list.dateType or list.dateType eq '00'}">기간</c:if>
                        <c:if test="${list.dateType eq '01'}">매일</c:if>
                    </td>
                    <!-- 라이브 여부 -->
                    <td class="table_td_04">
                        <c:if test="${list.liveType eq 'Y'}">사용</c:if>
                        <c:if test="${list.liveType eq 'N'}">사용안함</c:if>
                        <c:if test="${list.liveType eq 'D'}">만료</c:if>
                    </td>
                    <!-- 노출등급 -->
                    <td class="table_td_04">
                        <c:if test="${list.grade eq '01'}">전체</c:if>
                        <c:if test="${list.grade eq '02'}">7세이상</c:if>
                        <c:if test="${list.grade eq '03'}">12세이상</c:if>
                        <c:if test="${list.grade eq '04'}">15세이상</c:if>
                        <c:if test="${list.grade eq '05'}">19세이상</c:if>
                    </td>
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
                    <jsp:include page="/WEB-INF/views/include/naviControll.jsp">
                        <jsp:param value="getAdsList.do" name="actionUrl"/>
                        <jsp:param value="?findName=${vo.findName}&findValue=${vo.findValue}&masterID=${vo.masterID}&adsListMode=${vo.adsListMode}&scr_tp=${vo.scr_tp}" name="naviUrl"/>
                        <jsp:param value="${vo.pageNum }" name="pageNum"/>
                        <jsp:param value="${vo.pageSize }" name="pageSize"/>
                        <jsp:param value="${vo.blockSize }" name="blockSize"/>
                        <jsp:param value="${vo.pageCount }" name="pageCount"/>
                    </jsp:include>
                </td>
            </tr>
        </table>

        <!-- 등록/수정 버튼 -->
        <table border="0" cellpadding="0" cellspacing="0" width="100%"
               align="center">
            <tr style="height: 35px;">
                <td align="right">
                	<span class="button small blue" id="addBtn">등록</span>
                	<span class="button small blue" id="deleteBtn">삭제</span>
                	<span class="button small blue" id="changeOrder">노출순서변경</span>
                	<span class="button small blue" id="applyBtn">즉시적용</span>
                	<span class="button small blue" id="applyIdBtn">부분즉시적용</span>
                </td>
            </tr>
            <tr>
                <td align="right">
                    <span class="button small red" id="versionApplyBtn">상단메뉴 변경</span>
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