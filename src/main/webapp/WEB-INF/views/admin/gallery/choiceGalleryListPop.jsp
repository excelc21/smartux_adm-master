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
<link href="${pageContext.request.contextPath}/css/jquery.custom.treeview.css" rel="stylesheet" type="text/css">
<script src="${pageContext.request.contextPath}/js/jquery.custom.treeview.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.custom.treeview.async.js"></script>
<script type="text/javascript">;
	$(document).ready(function(){
		var callbak = '${callbak}';
		
		//#######트리뷰#######
        $("#root").custom_treeview({
			url: "${pageContext.request.contextPath}/admin/gallery/searchGalleryProc.do",
			root: "${gallery_id}",
			ajax: {
				type: "post",
				data: {
					view_type: "P",
					pop_type: "${type}"
				},
			},collapsed: true
		});
		//#######트리뷰#######
		
		 $(".treeVw").on("click", "a", function() {
           	$(".treeVw A").each(function(){
           		$(this).css("background-color","white");
            	$(this).css("color","black");
           	});
           	$(this).css("background-color","blue");
           	$(this).css("color","white");
           	$("#currentGallery").val($(this).attr("data-id"));
           	$("#currentGalleryName").text($(this).find("#tree_name").text());
		 });
		
		$("#sendBtn").click(function(){
			if($("#currentGallery").val()==""){
				alert("갤러리를 선택해 주세요.");
				return false;
			}
			(opener.window[callbak]||function(){})($("#currentGallery").val());
			self.close();
		});
		
		$("#closeBtn").click(function(){
			self.close();
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
								<td class="bold">갤러리 관리</td>
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
								<th>갤러리 선택 : <span id="currentGalleryName"></span></th>
							</tr>
							<tr>
								<td style="text-align:left;">
									<input type="hidden" id="currentGallery" name="currentGallery" value="" />
									<div id="wrapperCategory" style="height:400px;padding:5px;overflow-y: auto;overflow-x: hidden;">
										<span>갤러리</span>
										<ul id="root" class="treeVw"></ul>
									</div>
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
				<td height="40" align="right"><span class="button small blue" id="sendBtn">확인</span> 
				<span class="button small blue" id="closeBtn">닫기</span>&nbsp;&nbsp;
				</td>
			</tr>
		</tbody>
	</table>
</body>
</html>