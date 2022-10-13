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
$(document).ready(function(){
	var loc = "/smartux_adm/admin/hotvod/";
	
	// URL등록영역에 포커스
	$("#regUrlText").focus();
	
	$("#selSvcType").change(function(){
		var svc = $(this).val();
		if (svc!="") {
			goFilteringSiteList(svc);
		}
	});
	// 등록
	$("[name=btnRegister]").click(function(){
		var siteUrl = $("#regUrlText").val();
		if (!validParameter(siteUrl)) {
			$("#regUrlText").focus();
			return false;
		}
		if (confirm("등록 하시겠습니까?")) {
			doProcess("reg",siteUrl,"");
		}
	});
	// 수정
	$("[name=btnModify]").click(function(){
		var idx = $(this).parent().parent().find("td").eq(0).text();
		var siteUrl = $("#modUrlText_"+idx).val();
		if (!validParameter(siteUrl)) {
			return false;
		}
		if (confirm("수정 하시겠습니까?")) {
			doProcess("mod",siteUrl,idx);
		}
	});
	// 삭제	
	$("[name=btnDelete]").click(function(){
		var idx = $(this).parent().parent().find("td").eq(0).text();
		if (confirm("삭제 하시겠습니까?")) {
			doProcess("del","",idx);
		}
	});
	// 즉시적용	
	$("#btnApply").click(function(){
		if (confirm("적용 하시겠습니까?")) {
			doProcess("app","","");
		}
	});
	
	//CJ 아이들나라 추가 2019.12.20 : CJ서버 필터링 사이트 즉시적용 추가	
	$("#cjApplybtn").click(function(){
		if (confirm("적용 하시겠습니까?")) {
			$.blockUI({
	            blockMsgClass: "ajax-loading",
	            showOverlay: true,
	            overlayCSS: { backgroundColor: '#CECDAD' } ,
	            css: { border: 'none' } ,
	            message: "<b>로딩중..</b>"
	        });
			
			$.ajax({
				url: './appCacheCjFilteringSite.do',
				type: "POST",
				dataType: "json",
				success: function (rs) {
	                var flag = rs.flag;
	                var message = rs.message;
	                if(flag == "0000"){// 정상적으로 처리된 경우
	                    alert('정상처리 되었습니다.');
	                }else{
	                    alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
	                }
	                console.log(rs)
	                $.unblockUI();
					goFilteringSiteList(svc);
				},
	            error: function(){
	                alert("작업 중 오류가 발생하였습니다");
	                $.unblockUI();
	            }
			});
		}
	});
	// 목록조회
	function goFilteringSiteList(svc) {
		$("#svcType").val(svc);
		$("#frm").attr("action", loc + "filteringSiteList.do");
		$("#frm").attr("method", "GET");
		$("#frm").submit();
	}
	// 프로세스(등록/수정/삭제/적용 실행)
	function doProcess(type,siteUrl,orderNo) {
		var svc = $("#svcType").val();
        $.blockUI({
            blockMsgClass: "ajax-loading",
            showOverlay: true,
            overlayCSS: { backgroundColor: '#CECDAD' } ,
            css: { border: 'none' } ,
            message: "<b>로딩중..</b>"
        });
		$.ajax({
			url: loc + type + "FilteringSite.do",
			type: "POST",
			dataType: "json",
			data: {"svcType":svc,"siteUrl":siteUrl,"orderNo":orderNo},
			success: function (rs) {
                var flag = rs.flag;
                var message = rs.message;
                if(flag == "0000"){// 정상적으로 처리된 경우
                    alert('정상처리 되었습니다.');
                }else{
                    alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
                }
                console.log(message)
                $.unblockUI();
				goFilteringSiteList(svc);
			},
            error: function(){
                alert("작업 중 오류가 발생하였습니다");
                $.unblockUI();
            }
		});
	}
	// Check Validation
	function validParameter(param) {
		var result = true;
		if (param=="") {
			alert("URL을 입력해주세요.");
			result = false;
			refresh();
		}
		return result;
	}
	// Enter Key
	/* 
	if (e.keyCode==13) {
		$("[name=btnRegister]").click();
	}
	 */
	function refresh() {
		window.location.reload(true);
	}
});
//검수 단말기 정보 캐쉬
function getTestServerUser(){
	if(confirm("검수 단말기 정보를 갱신 하시겠습니까?")){
		$.blockUI({
			blockMsgClass: "ajax-loading",
			showOverlay: true,
			overlayCSS: { backgroundColor: '#CECDAD' } ,
			css: { border: 'none' } ,
			 message: "<b>로딩중..</b>"
		});
		$.ajax({
		    url: '/smartux_adm/admin/hotvod/testServerUserCache.do',
		    type: 'POST',
		    dataType: 'json',
		    /*timeout : 30000,*/
			async: true,
		    data: {
		    },
		    error: function(){
		    	alert("작업 중 오류가 발생하였습니다");
		 		$.unblockUI();
		    },
		    success: function(rs){
		    	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
			 	var flag = rs.flag;
			 	var message = rs.message;
			 	console.log(rs);
			 	if(flag == "0000"){// 정상적으로 처리된 경우
			 		alert('정상처리 되었습니다.');
			 	}else if(flag.match('1502')){
		 			alert("상용적용 진행중입니다. 잠시 후 확인해주세요. ");
			 		window.location.reload();	
			 	}else{
			 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
			 	}
                $.unblockUI();
		    }
		});
	}
}
</script>
</head>

<body leftmargin="0" topmargin="0">
<form name="frm" id="frm">
	<input type="hidden" name="svcType" id="svcType" value="${vo.svcType}" />
</form>

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
								<tr style="display: block">
									<td height="42" width="100%">
										<table border="0" cellpadding="0" cellspacing="0"
											width="100%">
											<tbody>
												<tr>
													<td width="300" class="boldTitle">필터링사이트</td>
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
										<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
											<tbody>
											<tr>
												<td align="left" style="width: 30px; padding: 5px;">
													<!-- 서비스목록(셀렉트박스) -->
													<select id="selSvcType" name="selSvcType" style="width:100px;">
														<c:forEach items="${svcList}" var="list">
														<option value="${list}" <c:if test="${list eq vo.svcType}">selected</c:if>>${list}</option>
														</c:forEach> 
													</select>
												</td>
											<tr>
											</tbody>
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
															<tr align="center">
																<th scope="col" width="5%">번호</th>
																<th scope="col" width="*">필터링사이트(URL)</th>
																<th scope="col" width="15%">등록일</th>
																<th scope="col" width="20%">#</th>
															</tr>
														<!-- URL 등록:입력영역 -->
															<tr align="center">
																<td></td>
																<td>
																	<input type="text" id="regUrlText" name="regUrlText" size="130" style="font-size: 12px;ime-mode:disabled;" onKeyUp="checkByte($(this),'50')"/>
																</td>
																<td>-</td>
																<td>
																	<span id="btnRegister" name="btnRegister" class="button small blue">등록</span>
																</td>
															</tr>
														<!-- URL 수정/삭제:목록조회 -->
														<c:choose>
														<c:when test="${fn:length(urlList)>0}">
															<c:forEach items="${urlList}" var="list">
															<tr align="center">
																<td>${list.orderNo}</td>
																<td>
																	<input type="text" name="modUrlText" id="modUrlText_${list.orderNo}" size="130" value="${list.siteUrl}" style="font-size: 12px;ime-mode:disabled;" onKeyUp="checkByte($(this),'50')"/>
																</td>
																<td>${list.regDate}</td>
																<td>
																	<span name="btnModify" class="button small blue"> 수정</span> 
																	<span name="btnDelete" class="button small blue"> 삭제</span>
																</td>
															</tr>
															</c:forEach>
														</c:when>
														<c:otherwise>
															<tr>
																<td colspan="4" align="center">필터링할 사이트가 존재하지 않습니다.</td>
															</tr>	
														</c:otherwise>
														</c:choose>
														</tbody>
													</table>
													<table border="0" cellpadding="0" cellspacing="0" width="100%" height="40px" align="center">
														<tr>
															<td colspan="4" align="right">
																<span class="button small blue" id="btnApply">즉시적용</span>
																<span class="button small blue" id="cjApplybtn">CJ 즉시적용</span>
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
										<!-- ######################## body end ######################### -->
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