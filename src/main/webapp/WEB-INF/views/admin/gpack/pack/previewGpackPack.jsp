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

	var addhtml = "<table><tr>";
	var album_title = "", img_url = "";

	//alert("category_id > "+category_id+" , img_poster_width > ${img_poster_width} , img_poster_height > ${img_poster_height}");
	var length = data.length;
	for(var i=0; i<length; i++){
		album_title	= data[i].album_title + data[i].series_desc;
		//img_url		= data[i].img_url;
		//poster_img	= data[i].height_img;

		img_url 	= data[i].img_url+"resize.php?filename="+data[i].height_img+"&width=${img_poster_width}&height=${img_poster_height}&type=poster";

		//addhtml += "<td>" + album_title + "<br/><img src=\"" + img_url + poster_img + "\" /></td>\n";
		addhtml += "<td>" + album_title + "<br/><img src=\"" + img_url + "\" /></td>\n";
	}
	addhtml += "</tr></table>"
	document.getElementById("gpack_tr_"+category_id).innerHTML = addhtml;

}
</script>
</head>
<body leftmargin="0" topmargin="0">
	<table>
		<tr>
			<td style="vertical-align:top;overflow:hidden;white-space:nowrap;">
				타이틀:<c:out value="${pack.pack_nm }"></c:out>
			</td>
		</tr>
	</table>
	<br/>
	<br />
<c:choose>
	<c:when test="${promotionList==null || fn:length(promotionList) == 0}">
	<table>
		<tr>
			<td>유효한 프로모션이 없습니다.</td>
		</tr>
	</table>
	</c:when>
	<c:otherwise>
	프로모션<br />
	<table>
		<c:forEach items="${promotionList }" var="promotion" varStatus="status">
		<tr>
			<td style="vertical-align: top;">
				<table border="1" style="border-width: 1px; border-style: solid; border-color: rgb(204, 204, 204) rgb(204, 204, 204) rgb(221, 221, 221);">
					<tr>
						<td style="vertical-align: top;overflow:hidden;white-space:nowrap;">
							타이틀:<c:out value="${promotion.category_nm }"></c:out><br />
							멘트:<c:out value="${promotion.category_comment }"></c:out>
						</td>
					</tr>
					<tr>
						<c:if test="${template_type eq 'TP002'}">
							<c:choose>
								<c:when test="${promotion.gpackPlaylistVOList==null || fn:length(promotion.gpackPlaylistVOList) == 0}">
							<td style="vertical-align: top;overflow:hidden;white-space:nowrap;">
								영상 없음
							</td>
								</c:when>
								<c:otherwise>
							<td style="vertical-align: top;overflow:hidden;white-space:nowrap;">
									영상:<br />
								<c:forEach items="${promotion.gpackPlaylistVOList }" var="playlist" varStatus="status">
									<c:out value="${playlist.playlist_nm }"></c:out><br />
								</c:forEach>
							</td>
								</c:otherwise>
							</c:choose>
						</c:if>
						<c:if test="${promotion.auto_yn eq 'Y'}">
							<c:forEach items="${promotion.gpackPromotionContentsVOList }" var="contents" varStatus="status">
								<td>
									<c:out value="${contents.album_title }"></c:out><br />
									<img width="${img_poster_width}px" height="${img_poster_height}px" src="${contents.img_url}resize.php?filename=${contents.h_img}&width=${img_poster_width}&height=${img_poster_height}&type=poster" /><br />
								</td>
							</c:forEach>
						</c:if>
						<c:if test="${promotion.auto_yn eq 'N'}">
							<c:forEach items="${promotion.gpackPromotionContentsVOList }" var="contents" varStatus="status">
								<td style="vertical-align: top;overflow:hidden;white-space:nowrap;">
									<c:out value="${contents.title }"></c:out><br />
									<c:if test="${contents.type ne 'MT002'}">
										<img width="${img_poster_width}px" height="${img_poster_height}px" src="${bg_img_url}/${contents.img}" /><br />
									</c:if>
									<c:if test="${contents.type eq 'MT002'}">
										<c:choose>
											<c:when test="${contents.img eq null or contents.img eq '' }">
												<img width="${img_poster_width}px" height="${img_poster_height}px" src="${contents.img_url}resize.php?filename=${contents.h_img}&width=${img_poster_width}&height=${img_poster_height}&type=poster" /><br />
											</c:when>
											<c:otherwise>
												<img width="${img_poster_width}px" height="${img_poster_height}px" src="${bg_img_url}/${contents.img}" /><br />
											</c:otherwise>
										</c:choose>
									</c:if>
								</td>
							</c:forEach>
						</c:if>
					</tr>
				</table>
			</td>
		</tr>
		</c:forEach>
	</table>
	</c:otherwise>
</c:choose>
	<br />
	<br />
<c:choose>
	<c:when test="${categorylist==null || fn:length(categorylist) == 0}">
	<table>
		<tr>
			<td>유효한 카테고리가 없습니다.</td>
		</tr>
	</table>
	</c:when>
	<c:otherwise>
	카테고리<br />
	<table>
		<c:forEach items="${categorylist }" var="category" varStatus="status">
		<tr>
			<td style="vertical-align: top;">
				<table border="1" style="border-width: 1px; border-style: solid; border-color: rgb(204, 204, 204) rgb(204, 204, 204) rgb(221, 221, 221);">
					<tr>
						<td style="vertical-align: top;overflow:hidden;white-space:nowrap;">
							타이틀:<c:out value="${category.category_nm }"></c:out><br />
							멘트:<c:out value="${category.category_comment }"></c:out>
						</td>
					</tr>
				</table>
				<div id="gpack_tr_${category.category_id }"></div>
			</td>
		</tr>
		<script type="text/javascript">
		fn_categoryContentsLoad('${category.pack_id}', '${category.category_id}', '${preview_gb}');
		</script>
		</c:forEach>
	</table>
	</c:otherwise>
</c:choose>
	
</body>
</html>