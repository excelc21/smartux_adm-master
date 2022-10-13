<%@ page language="java" contentType="text/html; charset=euc-kr" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<title>LG U+ HDTV</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<link href="/smartux_adm/css/anytime_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
    <!-- Color Picker -->
    <script src="/smartux_adm/js/colpick.js" type="text/javascript"></script>
    <link rel="stylesheet" href="/smartux_adm/css/colpick.css" type="text/css"/>
    <!-- Color Picker -->
<script src="/smartux_adm/js/anytime.js"></script>
<script type="text/javascript">
$(document).ready(function () {
	<c:if test="${vo.updateYn ne 'N'}">
    	AnyTime.picker('startTime', { format: '%z-%m-%d %H:00', labelTitle: '날짜', labelHour: '시간', time: ''});
    </c:if>
    AnyTime.picker('expiredTime', { format: '%z-%m-%d %H:59', labelTitle: '날짜', labelHour: '시간', time: ''});

    $('#addBtn').click(function () {
        insert();
    });

    $('#listBtn').click(function () {
        location.href = '${pageContext.request.contextPath}/admin/ads/getAdsList.do?adsListMode=${vo.adsListMode}&masterID=${vo.masterID}&scr_tp=${vo.scr_tp}';
    });


    var picker = $('#picker');

    $('#removeColorBtn').click(function () {
        picker.css('border-color','#000000');
        picker.val('');
    });

    picker.colpick({
        layout:'hex',
        submit:0,
        colorScheme:'dark',
        onChange:function(hsb,hex,rgb,el,bySetColor) {
            $(el).css('border-color','#'+hex);
            // Fill the text box just if the color was set using the picker, and not the colpickSetColor function.
            if(!bySetColor) $(el).val(hex);
        }
    }).keyup(function(){
        $(this).colpickSetColor(this.value);
    });

    var bgColor = '<c:out value="${vo.bgColor}"/>';
    picker.css('border-color','#' + bgColor);

    $('#selectBtn').click(function () {
        var value = $('#adsType').val();
        var url;
        
        switch (value) {
        	case '1':
        		url = '${pageContext.request.contextPath}/admin/gpack/event/getGpackPromotionChannelView.do?opener=ads';
	            category_window = window.open(url, 'viewchannel', 'width=700,height=540,scrollbars=yes');
	            break;
            case '2':
                url = '${pageContext.request.contextPath}/admin/commonMng/getOnceCategoryAlbum.do?categoryId=VC&hiddenName=linkUrl&textName=textUrl&textHtml=ChoiceData&isTypeChange=Y&type=I20&isAds=Y';
                category_window = window.open(url, 'getCategoryAlbumList', 'width=800,height=600,scrollbars=yes');
                break;
            case '3':
            	url = '${pageContext.request.contextPath}/admin/mainpanel/getPageCategoryList.do?category_id=VC&isAds=Y';
                category_window = window.open(url, 'getPageCategoryList', 'width=800,height=150,scrollbars=yes');
                break;          
            case '6':
            	url = '${pageContext.request.contextPath}/admin/imcs/getGoodsList.do?hiddenName=linkUrl&textName=textUrl';
            	category_window = window.open(url, 'getGoodsList', 'width=500,height=500,left=100,top=50,scrollbars=yes');
                break;
            default:
                break;
        }
    });

    //이미지 파일만 선택
    $('#file').change(function () {
        if ('' == $('#file').val()) {
            return;
        }

        if (imageFileCheck($('#file').val())) {
            alert('이미지 파일이 아닙니다.');
        }
    });

    $('#adsType').change(function () {
        $('#linkUrl').val('');
        $('#textUrl').val('');
        changeAdsType();
    });

    changeAdsType();
});

function changeAdsType() {
    var value = $('#adsType').val();

    switch (value) {
        case '1':
        	showCategory();
            break;
        case '2':
            showCategory();
            break;
        case '3':
        	showCategory();
	        break;
        case '4':
        	showInputBox();
            break;
        case '5':
        	showInputBox();
            break;
        case '6':
        	showCategory();
	        break;        	
        default:
            break;
    }
}

function showCategory() {
    $('#linkUrl').hide();
    $('#textUrl').show();
    $('#selectBtn').show();
}

function showInputBox() {
    $('#linkUrl').show();
    $('#textUrl').hide();
    $('#selectBtn').hide();
}

function submitForm() {
    if (confirm('등록 하시겠습니까?')) {
    	var f = document.getElementById('uploadForm');

        $.blockUI({
            blockMsgClass: 'ajax-loading',
            showOverlay: true,
            overlayCSS: { backgroundColor: '#CECDAD' },
            css: { border: 'none' },
            message: '<b>로딩중..</b>'
        });

        f.submit();
    }
}

function insert() {
    var title = $('#title').val();
    var rolTime = $('#rolTime').val();
    var startTime = $('#startTime').val();
    var expiredTime = $('#expiredTime').val();
    var adsType = $('#adsType').val();
    var linkUrl = $('#linkUrl').val();
    var file = $('#file').val();
    var fileName = $('#fileName').val();
    var dateType = $('input:radio[name="dateType"]:checked').val();
    /*2020-06-04 수정*/
    var textEtc = $('#textEtc').val();
    var textEtc2 = $('#textEtc2').val();

    if (isBlank(title)) {
        alert('제목을 입력해 주세요.');
        $('#title').focus();
        return;
    }

    if (isBlank(fileName) && isBlank(file)) {
        alert('배너 이미지를 입력해 주세요.');
        return;
    }

    if (isBlank(rolTime)) {
        alert('배너 요청 시간을 입력해 주세요.');
        $('#rolTime').focus();
        return;
    }

    if (isBlank(startTime)) {
        alert('시작 일시를 입력해 주세요.');
        return;
    }

    if (isBlank(expiredTime)) {
        alert('종료 일시를 입력해 주세요.');
        return;
    }

    if (startTime.split(' ').join('') >= expiredTime.split(' ').join('')) {
        alert('종료일시가 시작일시보다 빠르거나 같습니다.');
        return;
    }

    if ('01' == dateType) {
        var s1 = startTime.substring(startTime.length - 5, startTime.length);
        var s2 = expiredTime.substring(expiredTime.length - 5, expiredTime.length);

        if (s1 >= s2) {
            alert('종료시간이 시작시간보다 작거나 같습니다.');
            return;
        }
    }

    if (isNaN(rolTime)) {
        alert('배너 요청 시간은 숫자만 입력 가능합니다.');
        return;
    }

    if (1 > rolTime) {
        alert('배너 요청 시간은 1초 이상 등록 가능합니다.');
        return;
    }

    if (isBlank(linkUrl)) {
        if ('1' == adsType) {
            alert('실시간 채널을 선택해 주세요.');
            return;
        } else if ('2' == adsType) {
            alert('컨텐츠를 선택해 주세요.');
            return;
        } else if('3' == adsType) {
        	alert('카테고리를 선택해 주세요.');
            return;            
        } else if('4' == adsType) {
            alert('외부URL 값이 없습니다.');
            return;
        } else if('5' == adsType) {
            alert('앱링크 값이 없습니다.');
            return;
        } else if('6' == adsType) {
        	alert('신청 상품을 선택해주세요.');
        	return;
        }
    }

    submitForm();
}

function winOpen(imagePath, Width, Height) {
	window.open(imagePath, 'windowName', 'toolbar=no,scrollbars=yes, top=20,left=200,width=' + Width + ',height=' + Height);
}

function isBlank(obj) {
    return jQuery.isEmptyObject(obj) || jQuery.isEmptyObject(obj.trim());
}

function isNotBlank(obj) {
    return !isBlank(obj);
}

function deleteImage(type) {
    switch (type) {
        case 1:
            if (confirm('이미지를 삭제하시겠습니까?')) {
                $('#image').remove();
                $('#fileName').val('');
            }
            break;
        case 2:
            if (confirm('배경 세로 이미지를 삭제하시겠습니까?')) {
                $('#verticalBgImage').remove();
                $('#verticalBgFileName').val('');
            }
            break;
        case 3:
            if (confirm('배경 가로 이미지를 삭제하시겠습니까?')) {
                $('#horizontalBgImage').remove();
                $('#horizontalBgFileName').val('');
            }
            break;
        case 4:
            if (confirm('배너 이미지2를 삭제하시겠습니까?')) {
                $('#image2').remove();
                $('#fileName2').val('');
            }
            break;
        default:
            break;
    }
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
<td>
<!-- ######################## body start ######################### -->
<table border="0" cellpadding="15" cellspacing="0" width="100%" align="center">
<tr>
<td>
<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
<tr>
    <td class="3_line" height="1"/>
</tr>
<!-- 리스트 시작 -->
<tr>
<td>
<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
    <tr>
        <td height="25">
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                <tr>
                    <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif">
                    </td>
                    <td class="bold">배너 등록</td>
                </tr>
            </table>
        </td>
    </tr>
</table>

<form id="uploadForm" action="${pageContext.request.contextPath}/admin/ads/insertAds.do" method="POST" enctype="multipart/form-data">
	<input type="hidden" name="masterID" value="${vo.masterID}"/>
	<input type="hidden" name="num" value="${vo.number}"/>
	<input type="hidden" name="scr_tp" id="scr_tp" value="${vo.scr_tp}"/>
	<input type="hidden" name="adsListMode" id="adsListMode" value="${vo.adsListMode}"/>
	<table border="0" cellpadding="0" cellspacing="0" width="660" class="board_data">
	    <!-- 제목 -->
	    <tr align="center">
	        <th width="25%">제목</th>
	        <td width="5%"/>
	        <td width="70%" align="left">
	            <input type="text" value="${vo.title}" id="title" name="title" size="35" style="font-size: 12px;" onKeyUp="checkByte($(this),'50')"/>
	            <br> 사용제한 / 50byte 까지 허용
	        </td>
	    </tr>

	    <!-- 배너 요청 시간 -->
	    <tr align="center">
	        <th width="25%">배너 요청 시간</th>
	        <td width="5%"/>
	        <td width="70%" align="left">
	            <input type="text" id="rolTime" name="rolTime" size="35" value="<fmt:parseNumber value="${vo.rolTime}"/>" integerOnly="true" style="font-size: 12px;"/>초
	            <br> 배너가 보여지는 시간(기본 10초)
	        </td>
	    </tr>

	    <!-- 배너 이미지 URL -->
	    <tr align="center">
	        <th width="25%">배너 이미지</th>
	        <td width="5%"/>
	        <td width="70%" align="left">
	            <input type="file" id="file" accept="image/*" name="file" value="파일선택" onchange="imgHeaderCheck('file', 'ads',${imgSize});"/>
	            ${imgSize}kb / ${imgFormat}       
	            <c:if test="${not empty vo and not empty vo.fileName}">
	                <div id="image">
	                    <input type="text" id="fileName" name="fileName" value="${vo.fileName}" readonly/>
	                    <a href="javascript:winOpen('${vo.saveFileName}','400','400')">
	                        <span class="button small blue">미리보기</span>
	                    </a>
	                    <a href="javascript:deleteImage(1)">
	                        <span class="button small rosy">삭제</span>
	                    </a>
	                </div>
	            </c:if>
	        </td>
	    </tr>
	
	    <!-- 배너 이미지2 URL -->
	    <tr align="center">
	        <th width="25%">배너 이미지2</th>
			<td width="5%"/>
			<td width="70%" align="left">
	            <input type="file" id="file2" accept="image/*" name="file2" value="파일선택"  onchange="imgHeaderCheck('file2', 'ads',${imgSize});"/>
	            ${imgSize}kb / ${imgFormat}
	            <c:if test="${not empty vo and not empty vo.fileName2}">
	                <div id="image2">
	                    <input type="text" id="fileName2" name="fileName2" value="${vo.fileName2}" readonly/>
	                    <a href="javascript:winOpen('${vo.saveFileName2}','400','400')">
	                        <span class="button small blue">미리보기</span>
	                    </a>
	                    <a href="javascript:deleteImage(4)">
	                        <span class="button small rosy">삭제</span>
	                    </a>
	                </div>
	            </c:if>
	        </td>
	    </tr>
	
	    <!-- 배너 배경 가로 URL -->
	    <tr align="center">
	        <th width="25%">배경 가로 이미지</th>
	        <td width="5%"/>
	        <td width="70%" align="left">
	            <input type="file" id="horizontalBgFile" accept="image/*" name="horizontalBgFile" value="파일선택" onchange="imgHeaderCheck('horizontalBgFile', 'ads',${imgSize});"/>
	            ${imgSize}kb / ${imgFormat}
	            <c:if test="${not empty vo and not empty vo.horizontalBgFileName}">
	                <div id="horizontalBgImage">
	                    <input type="text" id="horizontalBgFileName" name="horizontalBgFileName" value="${vo.horizontalBgFileName}" readonly/>
	                    <a href="javascript:winOpen('${vo.horizontalBgSaveFileName}','400','400')">
	                        <span class="button small blue">미리보기</span>
	                    </a>
	                    <a href="javascript:deleteImage(3)">
	                        <span class="button small rosy">삭제</span>
	                    </a>
	                </div>
	            </c:if>
	        </td>
	    </tr>
	
	    <!-- 배너 배경 세로 URL -->
	    <tr align="center">
	        <th width="25%">배경 세로 이미지</th>
	        <td width="5%"/>
	        <td width="70%" align="left">
	            <input type="file" id="verticalBgFile" accept="image/*" name="verticalBgFile" value="파일선택"  onchange="imgHeaderCheck('verticalBgFile', 'ads',${imgSize});"/>
	            ${imgSize}kb / ${imgFormat}
	            <c:if test="${not empty vo and not empty vo.verticalBgFileName}">
	                <div id="verticalBgImage">
	                    <input type="text" id="verticalBgFileName" name="verticalBgFileName" value="${vo.verticalBgFileName}" readonly/>
	                    <a href="javascript:winOpen('${vo.verticalBgSaveFileName}','400','400')">
	                        <span class="button small blue">미리보기</span>
	                    </a>
	                    <a href="javascript:deleteImage(2)">
	                        <span class="button small rosy">삭제</span>
	                    </a>
	                </div>
	            </c:if>
	        </td>
	    </tr>
	
	    <!-- 배경색 -->
	    <tr align="center">
	        <th width="25%">배경색</th>
	        <td width="5%"/>
	        <td width="70%" align="left">
	            <input type="text" id="picker" name='picker' value="${vo.bgColor}" />
	            <span class="button small rosy" id="removeColorBtn">배경색 제거</span>
	        </td>
	    </tr>
		<!-- 추가내용(etc) -->
	    <tr align="center">
	        <th width="25%">추가내용</th>
	        <td width="5%"/>
	        <td width="70%" align="left">
	            <input type="text" value="${vo.textEtc}" id="textEtc" name="textEtc" size="70" style="font-size: 12px;" onKeyUp="checkByte($(this),'1024')"/>
	        </td>
	    </tr>
		<!-- 추가내용2(etc2)  -->
	    <tr align="center">
	        <th width="25%">추가내용2</th>
	        <td width="5%"/>
	        <td width="70%" align="left">
	            <input type="text" value="${vo.textEtc2}" id="textEtc2" name="textEtc2" size="70" style="font-size: 12px;" onKeyUp="checkByte($(this),'256')"/>
	        </td>
	    </tr>
	    
	    <!-- 배너 타입 -->
	    <tr align="center">
	        <th width="25%">배너 타입</th>
	        <td width="5%"/>
	        <td width="70%" align="left">
	            <select class="select" id="adsType" name="adsType">
	            	<option
                    	<c:if test="${vo.type eq '1'}">selected="selected"</c:if>
                    	value="1">실시간 채널
	                </option>
	                <option
	                	<c:if test="${vo.type eq '2'}">selected="selected"</c:if>
	                	value="2">컨텐츠
	                </option>
	                <option
	                	<c:if test="${vo.type eq '3'}">selected="selected"</c:if>
	                	value="3">카테고리
	                </option>
	                <option
	                	<c:if test="${vo.type eq '4'}">selected="selected"</c:if>
	                	value="4">외부URL
	                </option>
	                <option
	                	<c:if test="${vo.type eq '5'}">selected="selected"</c:if>
	                	value="5">앱링크
	                </option>
	                <option
	                	<c:if test="${vo.type eq '6'}">selected="selected"</c:if>
	                	value="6">신청
	                </option>
	                
	            </select>
	            <span class="button small blue" id="selectBtn">선택</span>
	        </td>
	    </tr>
	
	    <!-- 배너 링크 URL -->
	    <tr align="center">
	        <th width="25%">배너 링크 URL</th>
	        <td width="5%"/>
	        <td width="70%" align="left">
	            <input type="text" id="textUrl" size="70" style="font-size: 12px;" disabled="disabled" value="<c:out value="${textUrl}"/>"/>
	            <input type="text" id="linkUrl" name="linkUrl" size="35"
	                   style="font-size: 12px;"
	                   onKeyUp="checkByte($(this),'1024')" value="<c:out value="${vo.linkUrl}"/>"/>
	        </td>
	    </tr>
	
	    <!-- 배너 기간 타입 -->
	    <tr align="center">
	        <th width="25%">배너 기간 타입</th>
	        <td width="5%"></td>
	        <td width="70%" align="left" >
	            <input type="radio" id="dateType00" name="dateType" <c:if test="${empty vo.dateType or '00' eq vo.dateType}">checked</c:if> value="00"><label for="dateType00">기간</label>
	            <input type="radio" id="dateType01" name="dateType" <c:if test="${'01' eq vo.dateType}">checked</c:if> value="01"><label for="dateType01">매일</label>
	        </td>
	    </tr>
	
	    <!-- 배너 기간 -->
	    <tr align="center">
	        <th width="25%">배너 기간</th>
	        <td width="5%"/>
	        <td width="70%" align="left">
				시작일시:<input type="text" id="startTime" name="startTime" size="20" title="시작일시" value="<fmt:formatDate value="${vo.startTime}" type="date" pattern="yyyy-MM-dd HH:mm"/>"
	                <c:if test="${vo.updateYn eq 'N'}">
	                	readonly
	                </c:if>
				/>
				종료일시:<input type="text" id="expiredTime" name="expiredTime" size="20" title="종료일시"
	                <c:choose>
	                    <c:when test="${empty vo.expiredTime}">value="${nextWeekDate}"</c:when>
	                    <c:otherwise>value="<fmt:formatDate value="${vo.expiredTime}" type="date" pattern="yyyy-MM-dd HH:mm"/>"</c:otherwise>
	                </c:choose> 
				/>
	        </td>
	    </tr>
	
	    <!-- 라이브 여부 선택 -->
	    <tr align="center">
	        <th width="25%">라이브 여부</th>
	        <td width="5%"/>
	        <td width="70%" align="left">
	            Yes<input id="liveType" name="liveType" type="checkbox" class="checkbox" value="Y" <c:if test="${vo.liveType eq 'Y' || empty vo.liveType}">checked</c:if>>
	        </td>
	    </tr>
	    <!-- 등급 선택 -->
	    <tr align="center">
	        <th width="25%">등급 선택</th>
	        <td width="5%"/>
	        <td width="70%" align="left">
	            <select class="select" id="grade" name="grade">
	                <option
	                        <c:if test="${vo.grade eq '01'}">selected="selected"</c:if>
	                        value="01">제한없음
	                </option>
	                <option
	                        <c:if test="${vo.grade eq '02'}">selected="selected"</c:if>
	                        value="02">7세 이상
	                </option>
	                <option
	                        <c:if test="${vo.grade eq '03'}">selected="selected"</c:if>
	                        value="03">12세 이상
	                </option>
	                <option
	                        <c:if test="${vo.grade eq '04'}">selected="selected"</c:if>
	                        value="04">15세 이상
	                </option>
	                <option
	                        <c:if test="${vo.grade eq '05'}">selected="selected"</c:if>
	                        value="05">19세 이상
	                </option>
	            </select>
	        </td>
	    </tr>
	    
	    <!-- 참여 통계 -->
		<tr align="center">
			<th width="25%">참여통계</th> 
			<td width="5%"></td>
			<td width="70%" align="left" >
				<select class="select" name=eventID id="eventID">
				    <option value="">::선택::</option>
					<c:forEach items="${sbox_vo}" var="s_rec">
				    	<option value="${s_rec.stat_no}" <c:if test="${vo.eventID == s_rec.stat_no}">selected="selected"</c:if>>${s_rec.title}</option>
                    </c:forEach>
				</select>
			</td>
		</tr>
		
	</table>

	<!-- 등록/목록 버튼 -->
	<table border="0" cellpadding="0" cellspacing="0" width="660" align="left">
	    <tr>
	        <td align="right">
	        	<span class="button small blue" id="addBtn">
	        		<c:choose>
	        			<c:when test="${vo.number eq '0'}">등록</c:when>
	        			<c:otherwise>수정</c:otherwise>
	        		</c:choose>
				</span>
	            <span class="button small blue" id="listBtn">목록</span>
	        </td>
	    </tr>
	</table>
</form>

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