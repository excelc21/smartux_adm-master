<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css"> 
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>

<script type="text/javascript">
$(document).ready(function(){
	//닫기
	$("#closebtn").click(function(){
		self.close();
	});
});

//카테고리 조회시 하위 콘텐츠 불러오기
function fn_categoryContentsLoad(pack_id, category_id, preview_gb){

	var postUrl = "<%=webRoot%>/admin/gpack/category/getPrevCategoryContentsList.do";			
	$.post(	postUrl, 
			{pack_id : pack_id, category_id : category_id, preview_gb : preview_gb},
			function(data) {
				fn_contentsHtml(category_id, data);
			},
			"json"
    );
	
}

function fn_contentsHtml(category_id, data){

	var addhtml = [];
	var cat_id = "", album_id = "", album_title = "", img_url = ""; 
	var width_img = "", height_img = "", poster_img = "";

	var length = data.length;
	
	addhtml.push("<br/>");

	for(var i=0; i<length; i++){
		album_title		= data[i].album_title + data[i].series_desc;
		img_url			= data[i].img_url;
		poster_img		= data[i].poster_img;
		
		addhtml.push("<img src=\"" + img_url + "/" + poster_img + "\" /><br/>" + album_title + "<br/><br/>\n");
	}

	$("#gpack_td_"+category_id).append(addhtml.join(''));
	
}
</script>
</head>
<body leftmargin="0" topmargin="0">
	<br/>
	<c:if test="${fn:length(categorylist) eq 0}">
	<table>
		<tr>
			<td>
				사용중인 카테고리 정보가 없습니다.
			</td>
		</tr>
	</table>
	</c:if>
	<c:if test="${fn:length(categorylist) ne 0}">
	<table>
		<tr>
			<c:forEach items="${categorylist }" var="category_list" varStatus="status">
			<td style="vertical-align: top;">
				<table style="border-width: 1px; border-style: solid; border-color: rgb(204, 204, 204) rgb(204, 204, 204) rgb(221, 221, 221);">
					<tr>
						<td><c:out value="${category_list.category_id }"></c:out>.<c:out value="${category_list.category_nm }"></c:out></td>
					</tr>
					<tr>
						<td id="gpack_td_${category_list.category_id }">
							<script type="text/javascript">
							fn_categoryContentsLoad('${category_list.pack_id}', '${category_list.category_id}', '${preview_gb}');
							</script>
						</td>
					</tr>
				</table>
			</td>
			</c:forEach>
		</tr>
	</table>
	
	</c:if>
	
</body>
</html>