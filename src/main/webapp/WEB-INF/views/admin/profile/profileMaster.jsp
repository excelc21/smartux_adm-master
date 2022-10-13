<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>

<script type="text/javascript">
var service_type = '${service_type}';

function insertProfileMaster() {
    var profileMstId = $('#profileMstId');
    var profileMstName = $('#profileMstName');
    
    if (0 >= profileMstId.val().length) {
        alert('프로필이미지 코드를 입력해주세요.');
        return;
    }

    if (0 >= profileMstName.val().length) {
        alert('프로필이미지 그룹명을 입력해주세요.');
        return;
    }

    if ('false' == fn_checkByteReturn(profileMstId, '10')) {
        alert('프로필이미지 아이디는 10Byte 이내로 입력해야 합니다.');
        return;
    }

    if ('false' == fn_checkByteReturn(profileMstName, '50')) {
        alert('프로필이미지 그룹명은 50Byte 이내로 입력해야 합니다.');
        return;
    }


    var idRegExp = /^[a-zA-Z0-9]{1,19}$/g;

    if (!idRegExp.test(profileMstId.val())) {
        alert('프로필이미지 아이디는 영문, 숫자 만 입력이 가능 합니다.');
        return;
    }

    

    if (confirm('등록 하시겠습니까?')) {
        $.ajax({
            url: '/smartux_adm/admin/profile/profileMaster',
            type: 'POST',
            data: {profileMstId: profileMstId.val(), profileMstName: profileMstName.val(), serviceType:service_type},
            dataType: 'json',
            timeout: 3000,
            success: function (rs) {
                //noinspection JSUnresolvedVariable
                var flag = rs.flag;
                var message = rs.message;

                if (flag == '0000') {                   // 정상적으로 처리된 경우
                    alert('등록 성공');
                    location.reload();
                }else if (flag == '5010') {                 // 동일 마스터ID 존재
                    alert('동일한 프로필이미지코드가 이미 등록되어있습니다.');
                } else {
                    alert('작업 중 오류가 발생하였습니다\nflag : ' + flag + '\nmessage : ' + message);
                }
            },
            error: function () {
                alert('작업 중 오류가 발생하였습니다');
            }
        });
    }
}

function updateProfileMaster(index) {
    var profileMstId = $('#profileMstId' + index);
    var profileMstName = $('#profileMstName' + index);
   
    if (0 >= profileMstName.val().length) {
        alert('프로필이미지그룹명을 입력해주세요.');
        return;
    }

    if ('false' == fn_checkByteReturn(profileMstName, '50')) {
        alert('프로필이미지그룹명은 50Byte 이내로 입력해야 합니다.');
        return;
    }

    if (confirm('수정 하시겠습니까?')) {
        $.ajax({
            url: '/smartux_adm/admin/profile/profileMaster',
            type: 'PUT',
            data: {profileMstId: profileMstId.text(), profileMstName: profileMstName.val(), serviceType:service_type},
            dataType: 'json',
            timeout: 3000,
            success: function (rs) {
                //noinspection JSUnresolvedVariable
                var flag = rs.flag;
                var message = rs.message;

                if (flag == '0000') {                   // 정상적으로 처리된 경우
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

function deleteProfileMaster(index) {
    var profileMstId = $('#profileMstId' + index);

    if (confirm('삭제 하시겠습니까?')) {
        $.ajax({
            url: '/smartux_adm/admin/profile/profileMaster',
            type: 'DELETE',
            data: {profileMstId: profileMstId.text()},
            dataType: 'json',
            timeout: 3000,
            success: function (rs) {
                //noinspection JSUnresolvedVariable
                var flag = rs.flag;
                var message = rs.message;

                console.log(rs);
                if (flag == '0000') { // 정상적으로 처리된 경우
                    alert('삭제 성공');
                    location.reload();
                } else if (flag == 'dup') { // 라이브된 배너가 존재하는 경우
                    alert('해당 마스터 아이디로 등록된 프로필 이미지가 존재합니다.\n프로필 이미지를 삭제 후 삭제해주시기 바랍니다.');
                } else if (flag == '3000') { // 기존 배너를 삭제할 경우
                    alert(message);
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

function fn_checkByte(frm,limitByte) {
    
    var totalByte = 0;
    var message = frm.val();
    var charCount=0;

    for ( var i = 0; i < message.length; i++) {
        var currentByte = message.charCodeAt(i);
        if (currentByte > 128 || currentByte==10)
            totalByte += 2;
        else
            totalByte++;        
        
        if(totalByte>limitByte){
            charCount=i+1;
            break;
        }
    }
    
    if (totalByte > limitByte) {        
        alert(limitByte + "바이트까지 전송가능합니다.");
        var cutMessage=message.substring(0,charCount-1);
        frm.attr("value",cutMessage);       
    }
}

function fn_checkByteReturn(frm,limitByte) {
    
    var totalByte = 0;
    var message = frm.val();
    var charCount=0;
    
    var rtn = "true";

    for ( var i = 0; i < message.length; i++) {
        var currentByte = message.charCodeAt(i);        
        if (currentByte > 128 || currentByte==10)
            totalByte += 2;
        else
            totalByte++;        
        
        if(totalByte>limitByte){
            charCount=i+1;
            break;
        }
    }
    
    if (totalByte > limitByte) {        
        rtn = "false";  
    }
    
    return rtn;
}
</script>
</head>

<body leftmargin="0" topmargin="0">

<div id="divBody" style="height: 100%">
	<table border="0" cellpadding="0" cellspacing="0"  height="100%" width="100%">
		<tbody>
		<tr>
			<td colspan="2" height="45" valign="bottom">
				<!-- top menu start --> 
				<%@include file="/WEB-INF/views/include/top.jsp"%>
				<!-- top menu end -->
			</td>
		</tr>
		<tr>
			<td height="10"></td>
			<td></td>
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
							<%@include file="/WEB-INF/views/include/left.jsp"%>
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
				                                    <!-- 카테고리 타이틀 -->
				                                    <!-- <img src="/smartux_adm/images/admin/category_map_title.gif">-->
				                                                                                    프로필이미지 관리자
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
                                    <td width="60%">
                                        
                                        <!-- 중심 테이블 -->
                                        <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list">
                                            <tbody>
                                            <!--  첫번째 행 의 칼럼별 제목이 들어가는 부분 시작-->
                                            <tr align="center">
                                                <th scope="col" width="5%">번호</th>
                                                <th scope="col" width="10%">프로필이미지코드</th>
                                                <th scope="col" width="30%">프로필이미지그룹명</th>
                                                <th scope="col" width="20%">수정일시</th>
                                                <th scope="col" width="15%">#</th>
                                            </tr>

                                            <tr>
                                                <td></td>
                                                <td align="center"><input type="text" id="profileMstId" style="width:90%;font-size: 12px;ime-mode:disabled;" onKeyUp="fn_checkByte($(this),'10')"/></td>
                                                <td align="center"><input type="text" id="profileMstName" style="width:90%;font-size: 12px;" onKeyUp="fn_checkByte($(this),'50')"/></td>
                                                <td></td>
                                                <td align="center">
                                                    <a href="javascript:insertProfileMaster()">
                                                                                <span class="button small blue">등록
                                                                                </span>
                                                    </a>
                                                </td>
                                            </tr>
                                            <!--  첫번째 행 의 칼럼별 제목이 들어가는 부분 끝-->
                                            <!-- 두번째 행 데이터들이 들어가는 부분 시작 -->
                                            <c:forEach items="${profileMaster}" var="list">
                                                <c:set var="i" value="${i+1}"/>
                                                <tr>
                                                    <td align="center">${i}</td>
                                                    <td id="profileMstId${i}" align="center">${list.profileMstId}</td>
                                                    <td><input type="text" id="profileMstName${i}" style="width:90%;font-size: 12px;" value="${list.profileMstName}" onKeyUp="fn_checkByte($(this),'50')"/></td>
                                                    <td align="center">
                                                        ${list.regDate}
                                                        
                                                    </td>
                                                    <td align="center">
                                                        <a href="javascript:updateProfileMaster('${i}')">
                                                            <span class="button small blue"> 수정</span>
                                                        </a>
                                                        <a href="javascript:deleteProfileMaster('${i}')">
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