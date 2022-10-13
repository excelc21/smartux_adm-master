<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
var bk_level = 0;

$(document).ready(function(){
	
	$("div[name='category_div']").unbind("mouseover");
	$("div[name='category_div']").unbind("click");
	bind_div();
	
	$("#regbtn").click(function(){
		var panel_id = $("#panel_id").val();
		var title_id = $("#title_id").val();
		var category_id = $("#select_category_id").val();
		
		if(category_id == ""){
			alert("등록하고자 하는 카테고리를 선택해주세요");
		}else{
			$.post("<%=webRoot%>/admin/mainpanel/updateCategory.do", 
					 {panel_id : panel_id, title_id : title_id, category_id : category_id},
					  function(data) {
							// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
						 	var flag = data.flag;
						 	var message = data.message;
						 	
						 	if(flag == "0000"){						// 정상적으로 처리된 경우
						 		alert("카테고리를 등록했습니다");
						 		opener.location.reload();
						 		self.close();
						 	}else{
						 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
						 	}
						 	
						 	
						 
					  },
					  "json"
		    );
		}
	});
	
	$("#closebtn").click(function(){
		self.close();
	});
});

function bind_div(){
	$("div[name='category_div']").bind({
		mouseover : function(){
			$(this).css('cursor','pointer');
		},click : function(){
			var category_id = $(this).find("input[name='category_id']").val();
			var category_level = parseInt($(this).find("input[name='category_level']").val(), 10);
			var album_yn = $(this).find("input[name='album_yn']").val();
			
			clearselect();
			
			if(album_yn == "Y"){
				// console.log($("#category_tr").html());	
				if(category_level > bk_level){
					
				}else if(category_level == bk_level){
					$("#td_" + (category_level + 1)).remove();
					
				}else{
					var tds = $("#category_tr td");
					var idx = tds.length-1;
					while(true){
						var tdid = "td_" + category_level;
						
						if($(tds[idx]).attr("id") == tdid){
							break;
						}else{
							$(tds[idx]).remove();
							idx--;
						}
					}
					
				}
				
				$("#select_category_id").val(category_id);
				$(this).css("background-color", "yellow");
			}else if(album_yn == "N"){
				$.post("<%=webRoot%>/admin/mainpanel/getCategoryList.do", 
						 {category_id : category_id},
						  function(data) {
							 if(data.length == 0){
								alert("검색된 서브카테고리가 없습니다"); 
							 }else{
								if(category_level > bk_level){
									
								}else if(category_level == bk_level){
									$("#td_" + (category_level + 1)).remove();
									
								}else{
									var tds = $("#category_tr td");
									var idx = tds.length-1;
									while(true){
										var tdid = "td_" + category_level;
										
										if($(tds[idx]).attr("id") == tdid){
											break;
										}else{
											$(tds[idx]).remove();
											idx--;
										}
									}
									
								}
								drawsubcategory(category_level, data);
								bk_level = category_level; 
							 	
							 	$("div[name='category_div']").unbind("mouseover");
								$("div[name='category_div']").unbind("click");
							 	bind_div();
							 	
							 	
							 }
						  },
						  "json"
			    );
			}
			
		}
	});
}
function view_panel(panel_id){
	var url = "<%=webRoot%>/admin/mainpanel/viewPanel.do?panel_id=" + panel_id;
	window.open(url, "viewcode", "width=300,height=200");
}

function drawsubcategory(category_level, data){
	var length = data.length;
	var html = [], h=-1;
	var tdlevel = parseInt(category_level, 10) + 1
	html[++h] = "<td id=\"td_" + tdlevel + "\" valign=\"top\">\n";
	for(var i=0; i < length; i++){
		html[++h] = "<div class='sidebarmenu' name=\"category_div\">\n";
		html[++h] = data[i].category_name;
		if(data[i].album_yn == "N"){
			html[++h] = "▶";
		}
		html[++h] = "<input type=\"hidden\" name=\"category_id\" value=\"" + data[i].category_id + "\" />\n";
		html[++h] = "<input type=\"hidden\" name=\"category_level\" value=\"" + data[i].category_level + "\" />\n";
		html[++h] = "<input type=\"hidden\" name=\"album_yn\" value=\"" + data[i].album_yn + "\" />\n";
		html[++h] = "</div>\n";
	}
	html[++h] = "</td>\n";
	$("#category_tr").append(html.join(''));
	
	// alert($("#category_tr").html());
}

function clearselect(){
	var tds = $("div[name='category_div']");
	var tdslength = tds.length;
	for(var i=0; i < tdslength; i++){
		$(tds[i]).css("background-color" , "#93DAFF");
	}
	$("#select_category_id").val("");
}
</script>
</head>
<body>
<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
    <tbody>
    <tr>
        <td height="25">
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                <tbody>
                <tr>
                    <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
                    <td class="bold">카테고리 선택</td>
                </tr>
            	</tbody>
            </table>
        </td>
    </tr>
	</tbody>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%" align="left">
<tbody>
    <tr>
        <td height="40" align="left">
            <span class="button small blue" id="regbtn">카테고리 등록</span>
			<span class="button small blue" id="closebtn">닫기</span>
    	</td>
    </tr>
	</tbody>
</table>
<table class="sidebarMain">
	<tr id="category_tr">
		<td id="td_1" valign="top">
			<c:choose>
				<c:when test="${result==null || fn:length(result) == 0}">
					<div>검색된 카테고리가 없습니다</div>
				</c:when>
				<c:otherwise>
					<c:forEach var="item" items="${result}" varStatus="status">
						<div class='sidebarmenu' name="category_div">
							${item.category_name}
							<c:if test="${item.album_yn == 'Y'}">
								<!-- 하위 카테고리 없음 -->
							</c:if>
							<c:if test="${item.album_yn == 'N'}">
								<!-- 하위 카테고리 있음 -->
								▶
							</c:if>
							<input type="hidden" name="category_id" value="${item.category_id}" />
							<input type="hidden" name="category_level" value="${item.category_level}" />
							<input type="hidden" name="album_yn" value="${item.album_yn}" />
						</div>	
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
<input type="hidden" id="panel_id" value="${panel_id }" />
<input type="hidden" id="title_id" value="${title_id }" />
<input type="hidden" id="select_category_id" value="" />
</body>
</html>