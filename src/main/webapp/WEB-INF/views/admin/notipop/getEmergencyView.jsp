<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>긴급(비상)공지</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
	$(document).ready(function(){
		$("#applybtn").click(function(){
			var msg = "긴급공지를 상용에 적용하시겠습니까?";
			if(confirm(msg)){
				$.blockUI({
					blockMsgClass: "ajax-loading",
					showOverlay: true,
					overlayCSS: { backgroundColor: '#CECDAD' } ,
					css: { border: 'none' } ,
					 message: "<b>로딩중..</b>"
				});
				$.ajax({
				    url: '/smartux_adm/admin/notipop/notiPopApplyCache.do',
				    type: 'POST',
				    dataType: 'json',
				    data: {        
				    	callByScheduler:'A'
				    },
				    error: function(){
				    	alert("작업 중 오류가 발생하였습니다");
				 		$.unblockUI();
				    },
				    success: function(rs){
				    	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
					 	var flag = rs.flag;
					 	var message = rs.message;
					 	if(flag == "0000"){// 정상적으로 처리된 경우
					 		alert("정상처리 되었습니다.");
					 		$.unblockUI();
					 	}else{
					 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
					 		$.unblockUI();
					 	}
				    }
				});
			}
		});
		$("#submitBtn").click(function(){
			$("form[name=form1]").submit();
		});
	});
</script>
<style type="text/css">
.line {
	border-bottom: 1px solid black;
}
</style>
</head>

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
                                    	공지 관리	
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
                	
					<form id="form1" name="form1" action="./getEmergencyProc.do" method="POST">
					<input type="hidden" id="noti_no" name="noti_no" value="${vo.noti_no}">
					<table border="0" cellpadding="0" cellspacing="0" style="width:700px;">
						<tr>
							<td class="3_line" height="1"></td>
						</tr>
						<tr>
							<td height="20"></td>
						</tr>
						<tr>
							<td width="60%">
								<table border="0" cellpadding="0" cellspacing="5" width="100%" class="board_data">
									<tr>
										<th colspan="2" scope="col" width="100%">긴급(비상)공지 설정</th>
									</tr>
									<tr>
										<th scope="col" width="20%">긴급(비상) 여부</th>
										<td align="left">
											<select id="status" name="status">
												<option <c:if test="${vo.status == '0'}">selected="selected"</c:if> value="0">정상</option>
												<option <c:if test="${vo.status == '1'}">selected="selected"</c:if> value="1">긴급</option>
											</select>
											<input type="hidden" id="scr_tp" name="scr_tp" value="${scr_tp}">
											<input type="hidden" id="display_type" name="display_type" value="${display_type}">
										</td>
									</tr>
									<tr>
										<th scope="col" width="20%">메시지 여부</th>
										<td align="left">
											<select id="message_yn" name="message_yn">
												<option <c:if test="${vo.message_yn == 'Y'}">selected="selected"</c:if> value="Y">출력</option>
												<option <c:if test="${vo.message_yn == 'N'}">selected="selected"</c:if> value="N">미출력</option>
											</select>
										</td>
									</tr>
									<tr>
										<th scope="col" width="20%">메시지 내용</th>
										<td align="left"><input type="text" id="message" name="message" size="80" value="${vo.message}" onKeyUp="checkByte($(this),'512')"></td>

									</tr>
									<tr>
										<th scope="col" width="20%">네트워크 타입</th>
										<td align="left">
											<select id="net_type" name="net_type">
												<option <c:if test="${vo.net_type == '0'}">selected="selected"</c:if> value="0">ALL</option>
												<option <c:if test="${vo.net_type == '1'}">selected="selected"</c:if> value="1">Wi-fi</option>
												<option <c:if test="${vo.net_type == '2'}">selected="selected"</c:if> value="2">LTE</option>
												<option <c:if test="${vo.net_type == '3'}">selected="selected"</c:if> value="3">3G</option>
											</select>
										</td>
									</tr>
								</table>
								<p align="right">
									<span class="button small blue" id="submitBtn">설정저장</span>
									<span class="button small blue" id="applybtn">즉시적용</span>
								</p>
							</td>
						</tr>
					</table>
					</form>
						
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