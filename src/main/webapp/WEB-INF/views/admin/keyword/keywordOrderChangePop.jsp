<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/common.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>LG U+ IPTV SmartUX</title>
<link href="${pageContext.request.contextPath}/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<style>
  .div-ImageView {
    position: absolute;
    display: block;
    width: 100px;
    height: 100px;
    text-align: center;
  }
</style>
<script type="text/javascript">;
	$(document).ready(function(){
		var callbak = '${callbak}';
		
		$('#closeBtn').click(function(){
			self.close();
		});
		
		$('#orderBtn').click(function(){
			var size = $("#keyword_list option").size();
			if(size <= 0 ){
				alert("순서를 변경할 데이터가 없습니다.");
				return false;
			}
			if(confirm("순서를 변경합니까?")){
				var optionarray = new Array();
				
				for(var i=0; i < size; i++){
					var list = $('#keyword_list option').eq(i).val();
					optionarray.push(list);
				}
				
				$.post("${pageContext.request.contextPath}/admin/keyword/keywordOrderChangeProc.do", 
						 {keyword_ids : optionarray},
						  function(data) {
							if(data.flag=="0000"){
								(opener.window[callbak]||function(){})('${keyword_id}');
								self.close();
         					} else {
         						alert(data.message);
         					}
						  },
						  "json"
			    );
			}
		});
		
		$("#upbtn").click(function(){
			var idx = $("#keyword_list option").index($("#keyword_list option:selected"));
			if(idx == -1){			// 선택한 항목이 없으면
				alert("순서를 바꾸고자 하는 항목을 선택해주세요");
			}else{					// 선택한 항목이 있으면
				fnSortCateogry("keyword_list", "U");
			}
		});
		
		$("#downbtn").click(function(){
			var idx = $("#keyword_list option").index($("#keyword_list option:selected"));
			if(idx == -1){			// 선택한 항목이 없으면
				alert("순서를 바꾸고자 하는 항목을 선택해주세요");
			}else{					// 선택한 항목이 있으면
				fnSortCateogry("keyword_list", "D");
			}
		});
	});
</script>
<body leftmargin="0" topmargin="0">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
		<tbody>
			<tr>
				<td height="25">
					<table border="0" cellpadding="0" cellspacing="0" width="100%">
						<tbody>
							<tr>
								<td width="15"><img src="${pageContext.request.contextPath}/images/admin/blt_07.gif"></td>
								<td class="bold">발화어 관리</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table border="0" cellpadding="0" cellspacing="0" class="board_list"  style="width:400px;">
						<tbody>
							<tr>
								<th colspan="3">순서변경</th>
							</tr>
							<tr>
								<td style="text-align:center;">
									<select id="keyword_list" name="keyword_list" size="20" style="width:230px;height:200px;">
										<c:forEach var="item" items="${vo}" varStatus="status">
											<option value="${item.keyword_id}">${item.keyword_name}</option>
										</c:forEach>
									</select>
								</td>
				                <td width="10"></td>
				                <td width="70" align="left" >
				                	<span class="button small blue" id="upbtn">위로</span><br/><br/>
				                	<span class="button small blue" id="downbtn">아래로</span>
				                </td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
		</tbody>
	</table>
	<table border="0" cellpadding="0" cellspacing="0" width="100%" align="left"  style="width:400px;">
		<tbody>
			<tr>
				<td height="40" align="right"><span class="button small blue" id="orderBtn">확인</span> 
				<span class="button small blue" id="closeBtn">닫기</span>&nbsp;&nbsp;
				</td>
			</tr>
		</tbody>
	</table>
</body>
</html>