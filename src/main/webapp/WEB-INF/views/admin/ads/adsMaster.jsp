<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>배너 마스터</title>

    <link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
    <jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>

    <script type="text/javascript">
	    var newId = "${newId}";
	    $(document).ready(function () {
	    	$('[name=autoId]').attr('readonly', true);
			$('[name=autoId]').val(newId);
	    });
    
        function insertAdsMaster() {
            var masterID = $('#masterID');
            var masterName = $('#masterName');
            var liveCount = $('#liveCount');

            if (0 >= masterID.val().length) {
                alert('배너 마스터 아이디를 입력해주세요.');
                return;
            }

            if (0 >= masterName.val().length) {
                alert('배너 마스터명 입력해주세요.');
                return;
            }

            if ('false' == checkByteReturn(masterID, '20')) {
                alert('배너 마스터 아이디는 20Byte 이내로 입력해야 합니다.');
                return;
            }

            if ('false' == checkByteReturn(masterName, '50')) {
                alert('배너 마스터명은 50Byte 이내로 입력해야 합니다.');
                return;
            }

            if (isNaN(liveCount.text()) || 0 >= liveCount.text()) {
                alert('라이브 개수는 숫자만 입력 가능합니다.');
                return;
            }
			
            if($('#_chk').is(':checked')){
	            var idRegExp = /^[a-zA-Z0-9]{1,19}$/g;
	
	            if (!idRegExp.test(masterID.val())) {
	                alert('배너 마스터 아이디는 영문, 숫자 만 입력이 가능 합니다.');
	                return;
	            }
            }
            
            if (confirm('등록 하시겠습니까?')) {
                $.ajax({
                    url: '/smartux_adm/admin/ads/adsMaster',
                    type: 'POST',
                    data: {masterID: masterID.val(), masterName: masterName.val(), liveCount: liveCount.text()},
                    dataType: 'json',
                    //timeout: 3000,
                    success: function (rs) {
                        //noinspection JSUnresolvedVariable
                        var flag = rs.flag;
                        var message = rs.message;

                        if (flag == '0000') {					// 정상적으로 처리된 경우
                            alert('등록 성공');
                            location.reload();
                        } else {
                            alert('작업 중 오류가 발생하였습니다\nflag : ' + flag + '\nmessage : ' + message);
                            location.reload();
                        }
                    },
                    error: function () {
                        alert('작업 중 오류가 발생하였습니다');
                        location.reload();
                    }
                });
            }
        }

        function updateAdsMaster(index) {
            var masterID = $('#masterID' + index);
            var masterName = $('#masterName' + index);

            if (0 >= masterName.val().length) {
                alert('배너 마스터명 입력해주세요.');
                return;
            }

            if ('false' == checkByteReturn(masterName, '50')) {
                alert('배너 마스터명은 50Byte 이내로 입력해야 합니다.');
                return;
            }

            if (confirm('수정 하시겠습니까?')) {
                $.ajax({
                    url: '/smartux_adm/admin/ads/adsMaster',
                    type: 'PUT',
                    data: {masterID: masterID.text(), masterName: masterName.val()},
                    dataType: 'json',
                    //timeout: 3000,
                    success: function (rs) {
                        //noinspection JSUnresolvedVariable
                        var flag = rs.flag;
                        var message = rs.message;

                        if (flag == '0000') {					// 정상적으로 처리된 경우
                            alert('수정 성공');
                            location.reload();
                        } else {
                            alert('작업 중 오류가 발생하였습니다\nflag : ' + flag + '\nmessage : ' + message);
                        }
                    },
                    error: function () {
                        alert('작업 중 오류가 발생하였습니다');
                    }
                })
            }
        }

        function deleteAdsMaster(index) {
            var masterID = $('#masterID' + index);

            if (confirm('삭제 하시겠습니까?')) {
                $.ajax({
                    url: '/smartux_adm/admin/ads/adsMaster',
                    type: 'DELETE',
                    data: {masterID: masterID.text()},
                    dataType: 'json',
                    //timeout: 10000,
                    success: function (rs) {
                        //noinspection JSUnresolvedVariable
                        var flag = rs.flag;
                        var message = rs.message;

                        console.log(rs);
                        if (flag == '0000') { // 정상적으로 처리된 경우
                            alert('삭제 성공');
                            location.reload();
                        } else if (flag == '8001') { // 배너가 존재하는 경우
                            alert('해당 마스터 아이디로 등록된 배너가 존재합니다.\n배너를 삭제하고 마스터를 삭제해주시기 바랍니다.');
                        } else if (flag == '3000') { // 기존 배너를 삭제할 경우
                            alert(message);
                        } else {
                            alert('작업 중 오류가 발생하였습니다\nflag : ' + flag + '\nmessage : ' + message);
                        }
                    },
                    error: function (request,status,error) {
                    	//console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
                        alert('작업 중 오류가 발생하였습니다');
                    }
                })
            }
        }
        
        function checkChk(){
        	if($('#_chk').is(':checked')){
        		$('[name=autoId]').attr('readonly', false);
        		$('[name=autoId]').val('');
        		//$('[name=autoId]').hide();
        		//$('[name=dirId]').show();
        	}else{
        		
        		$('[name=autoId]').attr('readonly', true);
        		$('[name=autoId]').val(newId);
        		//$('[name=autoId]').show();
        		//$('[name=dirId]').hide();
        	}
        }
    </script>
</head>
<body>
<div id="divBody" style="height: 100%">
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
            <td height="10"/>
        </tr>

        <!-- 시작 -->
        <tr>
            <td colspan="2" valign="top">  <!-- 1번의 td -->
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
                                <tr style="display: block">
                                    <td height="42" width="100%">
                                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                            <tbody>
                                            <tr>
                                                <td width="300" class="boldTitle">배너 마스터</td>
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
                                    <td class="3_line" height="1"></td>
                                </tr>
                                <tr>
                                    <td height="20"></td>
                                </tr>
                                <tr>
                                    <td width="60%">
                                        <!-- 중심 테이블 -->
                                        <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list">
                                            <tbody>
                                            <!--  첫번째 행 의 칼럼별 제목이 들어가는 부분 시작-->
                                            <tr align="center">
                                                <th scope="col" width="5%">번호</th>
                                                <th scope="col" width="10%">배너 마스터 아이디</th>
                                                <th scope="col" width="30%">배너 마스터명</th>
                                                <!-- 라이브 개수 제한 해제 -->
                                                <th scope="col" width="10%" style="display:none">라이브 개수</th>
                                                <th scope="col" width="20%">수정일시</th>
                                                <th scope="col" width="15%">#</th>
                                            </tr>

                                            <tr>
                                                <td align="center">직접입력<input type="checkbox" id="_chk" name="_chk" onClick="checkChk();"/></td>
                                                <td align="center"><input type="text" id="masterID" name="autoId" style="width:90%;font-size: 12px;ime-mode:disabled;" onKeyUp="checkByte($(this),'20')"/></td>
                                                <td align="center"><input type="text" id="masterName" style="width:90%;font-size: 12px;" onKeyUp="checkByte($(this),'50')"/></td>
                                                <!-- 라이브 개수 제한 해제 -->
                                                <td id="liveCount" align="center" style="display:none">10</td>
                                                <td></td>
                                                <td align="center">
                                                    <a href="javascript:insertAdsMaster()">
	                                                    <span class="button small blue">등록
	                                                    </span>
                                                    </a>
                                                </td>
                                            </tr>
                                            <!--  첫번째 행 의 칼럼별 제목이 들어가는 부분 끝-->
                                            <!-- 두번째 행 데이터들이 들어가는 부분 시작 -->
                                            <c:forEach items="${adsMaster}" var="list">
                                                <c:set var="i" value="${i+1}"/>
                                                <tr>
                                                    <td align="center">${i}</td>
                                                    <td id="masterID${i}" align="center">${list.masterID}</td>
                                                    <td><input type="text" id="masterName${i}" style="width:90%;font-size: 12px;" value="${list.masterName}" onKeyUp="checkByte($(this),'50')"/></td>
                                                    <td id="liveCount${i}" align="center" style="display:none">${list.liveCount}</td>
                                                    <td align="center">
                                                        <fmt:formatDate value="${list.regDate}" type="date" pattern="yyyy-MM-dd HH:mm"/>
                                                    </td>
                                                    <td align="center">
                                                        <a href="javascript:updateAdsMaster('${i}')">
                                                            <span class="button small blue"> 수정</span>
                                                        </a>
                                                        <a href="javascript:deleteAdsMaster('${i}')">
                                                            <span class="button small blue"> 삭제</span>
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
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
        </tbody>
    </table>
</div>
</body>
</html>
