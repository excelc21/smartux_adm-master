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

	//TEST용
	//fn_categoryContentsLoad('63', '22', 'VODB');
	fn_categoryContentsLoad('63', '21', 'VODA');
});

//카테고리 조회시 하위 콘텐츠 불러오기
function fn_categoryContentsLoad(pack_id, promotion_id, promotion_video_gb, auto_yn){

	var postUrl = "<%=webRoot%>/admin/gpack/category/getPreviewContentsList.do";			
	$.post(	postUrl, 
			{pack_id : pack_id, promotion_id : promotion_id, promotion_video_gb : promotion_video_gb, auto_yn : auto_yn},
			function(data) {
				//todo :: 각 콘텐츠 타입에 따라 화면에 그려줌 
				if(contents_type === "Y")	fn_autoVodHtml(category_id, data);
				if(contents_type === "N")	fn_vodHtml(category_id, data);
			},
			"json"
    );
	
}

/**
 * 자동VOD 설정 
 */
function fn_autoVodHtml(category_id, data){

	var addhtml = [];
	var ah = 0;
	//alert(data[1].category_id);
	var rank_no, album_id, album_name, imcs_category_id, imcs_category_name, imcs_img_path, imcs_img_file = "";

	var length = data.length;
	addhtml[ah++]	= "<br/>";
	for(var i=0; i<length; i++){
		album_name		= data[i].album_name;
		imcs_img_path	= data[i].imcs_img_path;
		imcs_img_file	= data[i].imcs_img_file;
		addhtml[ah++]	= "<img src=\""+imcs_img_path+"/"+imcs_img_file+"\" /><br/>"+album_name+"<br/><br/>\n";
	}

	//alert("category_id >> "+category_id+", "+$("#"+category_id)+"\n"+addhtml.join(''));

	$("#gpack_div_"+category_id).append(addhtml.join(''));
}

/**
 * 수동VOD 설정
 */
function fn_vodHtml(category_id, data){

	var addhtml = [];
	var ah = 0;
	//alert(data[1].category_id);
	var pack_id, category_id, vod_id, vod_nm, img_file, imcs_category_id, album_id, imcs_img_path, imcs_img_file, contents_text = "";

	var length = data.length;
	addhtml[ah++]	= "<br/>";
	for(var i=0; i<length; i++){
		vod_nm			= data[i].vod_nm;
		contents_text	= data[i].contents_text;
		img_file		= data[i].img_file;
		imcs_img_path	= data[i].imcs_img_path;
		imcs_img_file	= data[i].imcs_img_file;

		addhtml[ah++]	= "<img src=\""+imcs_img_path+"/"+imcs_img_file+"\" /><br/>"+contents_text;
	}

	//alert("category_id >> "+category_id+", "+$("#"+category_id)+"\n"+addhtml.join(''));

	$("#gpack_div_"+category_id).append(addhtml.join(''));
	
}

</script>
</head>
<body leftmargin="0" topmargin="0">
	<br/>
<c:choose>
	<c:when test="${result==null || fn:length(result) == 0}">
	<table>
		<tr>
			<td>유효한 프로모션이 없습니다.</td>
		</tr>
	</table>
	</c:when>
	<c:otherwise>
	<table>
		<c:forEach items="${result }" var="promotion" varStatus="status">
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
							<td style="vertical-align: top;overflow:hidden;white-space:nowrap;">
									영상:<br />
								<c:forEach items="${promotion.gpackPlaylistVOList }" var="playlist" varStatus="status">
									<c:out value="${playlist.playlist_nm }"></c:out><br />
								</c:forEach>
							</td>
						</c:if>
						<c:if test="${promotion.auto_yn eq 'Y'}">
							<c:forEach items="${promotion.gpackPromotionContentsVOList }" var="contents" varStatus="status">
								<td>
									<c:out value="${contents.album_title }"></c:out><br />
									<img src="${contents.img_url}/${contents.w_img}" /><br />
								</td>
							</c:forEach>
						</c:if>
						<c:if test="${promotion.auto_yn eq 'N'}">
							<c:forEach items="${promotion.gpackPromotionContentsVOList }" var="contents" varStatus="status">
								<td style="vertical-align: top;overflow:hidden;white-space:nowrap;">
									<c:out value="${contents.title }"></c:out><br />
									<c:if test="${contents.type ne 'MT002'}">
										<img src="/${bg_img_url}/${contents.img}" /><br />
									</c:if>
									<c:if test="${contents.type eq 'MT002'}">
										<c:choose>
											<c:when test="${contents.img eq '' }">
												<img src="${contents.img_url}/${contents.w_img}" />
											</c:when>
											<c:otherwise>
												<img src="/${bg_img_url}/${contents.img}" /><br />
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
	
</body>
</html>