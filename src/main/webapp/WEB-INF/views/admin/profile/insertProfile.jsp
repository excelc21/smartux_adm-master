<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<link href="/smartux_adm/css/anytime_style.css" rel="stylesheet" type="text/css">
<script src="/smartux_adm/js/anytime.js"></script>
<script type="text/javascript">

$(document).ready(function () {

    
    $('#addBtn').click(function () {
        insert();
    });

    $('#listBtn').click(function () {
        var val = $("#profileMstId").val();
        location.href = '${pageContext.request.contextPath}/admin/profile/getProfileList.do?profileMstId='+ val+'&service_type='+$('#service_type').val();
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
 
});




function submitForm() {
    var profileImgId = $('#profileImgId').val();
    var msg;
    
    if(profileImgId != ''){
        msg = "수정";
    }else{
        msg = "등록";
    }
    if (confirm(msg +' 하시겠습니까?')) {
       
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
    
    var profileImgName = $('#profileImgName').val();
    var profileImgUrl = $('#profileImgUrl').val();
    var file = $('#file').val();

    if (isBlank(profileImgName)) {
        alert('이미지 타이틀을 입력해 주세요.');
        $('#profileImgName').focus();
        return;
    }
    
    if (isBlank(profileImgUrl) && isBlank(file)) {
        alert('이미지를 입력해 주세요.');
        return;
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

function deleteImage() {
    if (confirm('이미지를 삭제하시겠습니까?')) {
        $('#image').remove();
       
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
                <tr>
                    <td height="42" width="100%">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tbody>
                            <tr>
                                <td width="300" class="boldTitle">
                                	<!-- 카테고리 타이틀 -->
                                    <!-- <img src="/smartux_adm/images/admin/category_map_title.gif">-->
                                   프로필 이미지 관리
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
                		<table border="0" cellpadding="15" cellspacing="0" width="100%" align="center">
                    		<tbody>
                    		<tr>
                    			<td>
                    				<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
			                            <tbody>	
			                            <c:if test="${not empty profileMstName}">
										    <tr>
										        <td align="left" class="bold">
										            ${profileMstName}
										        </td>
										    </tr>
										</c:if>		                            
						                <tr>
						                    <td class="3_line" height="1"></td>
						                </tr>
						                <!-- 리스트 시작 -->
						                <tr>
						                  <td>
						                    <form id="uploadForm" action="${pageContext.request.contextPath}/admin/profile/insertProfile.do" method="POST" enctype="multipart/form-data">
												<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
												    <tr>
												        <td height="25">
												            <table border="0" cellpadding="0" cellspacing="0" width="100%">
												                <tr>
												                    <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif">
												                    </td>
												                    
												                    <c:choose>
												                        <c:when test="${vo.profileImgId eq null}"><td class="bold">등록</td></c:when>
												                        <c:otherwise><td class="bold">수정</td></c:otherwise>
												                    </c:choose>
												                </tr>
												            </table>
												        </td>
												    </tr>
												</table>
												
												
												    <input type="hidden" id="profileMstId" name="profileMstId" value="${vo.profileMstId}"/>
												    <input type="hidden" id="profileImgId" name="profileImgId" value="${vo.profileImgId}"/>
												    <input type="hidden" id="service_type" name="service_type" value="${service_type}"/>
												    <input type="hidden" id="orgImgUrl" name="orgImgUrl" value="${vo.orgImgUrl}"/>
												    
												    <table border="0" cellpadding="0" cellspacing="0" width="660" class="board_data">
												        <!-- 수정시 id 노출 -->
												        <c:choose>
												            <c:when test="${vo.profileImgId ne null}">
												                <tr align="center">
												                    <th width="25%">이미지 ID</th>
												                    <td width="5%"/>
												                    <td width="70%" align="left">
												                        ${vo.profileImgId}
												                    </td>
												                </tr>
												            </c:when>
												        </c:choose>
												        <!-- 제목 -->
												        <tr align="center">
												            <th width="25%">이미지 타이틀</th>
												            <td width="5%"/>
												            <td width="70%" align="left">
												                <input type="text" value="${vo.profileImgName}" id="profileImgName" name="profileImgName" size="35" style="font-size: 12px;" onKeyUp="fn_checkByte($(this),'50')"/>
												                <br> 사용제한 / 50byte 까지 허용
												            </td>
												        </tr>
												
												        <!-- 배너 이미지 URL -->
												        <tr align="center">
												            <th width="25%">이미지</th>
												            <td width="5%"/>
												            <td width="70%" align="left">
												                <input type="file" id="file" accept="image/*" name="file" value="파일선택" onchange="imgHeaderCheck('file', 'ads',${imgSize});"/>
												                ${imgSize}kb / ${imgFormat}       
												                <c:if test="${not empty vo and not empty vo.profileImgUrl}">
												                    <div id="image">
												                        <input type="text" id="profileImgUrl" name="profileImgUrl" value="${vo.profileImgUrl}" readonly/>
												                        <a href="javascript:winOpen('${vo.profileImgUrl}','400','400')">
												                            <span class="button small blue">미리보기</span>
												                        </a>
												                        <a href="javascript:deleteImage(1)">
												                            <span class="button small rosy">삭제</span>
												                        </a>
												                    </div>
												                </c:if>
												            </td>
												        </tr>
												         
												    </table>
												
												    <!-- 등록/목록 버튼 -->
												    <table border="0" cellpadding="0" cellspacing="0" width="660" align="left">
												        <tr>
												            <td align="right">
												                <span class="button small blue" id="addBtn">
												                    <c:choose>
												                        <c:when test="${vo.profileImgId eq null}">등록</c:when>
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
	        <%@include file="/WEB-INF/views/include/bottom.jsp" %>
	        <!-- 하단 로그인 사용자 정보 종료 -->
	    </td>
	 </tr>
</tbody>
</table>
</div>
</body>
</html>