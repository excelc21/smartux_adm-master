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
function fn_categoryContentsLoad(pack_id, category_id, contents_type){

	var postUrl = "<%=webRoot%>/admin/gpack/category/getContentsList.do";			
	$.post(	postUrl, 
			{pack_id : pack_id, category_id : category_id, contents_type : contents_type},
			function(data) {
				//todo :: 각 콘텐츠 타입에 따라 화면에 그려줌 
				if(contents_type === "VODA")	fn_autoVodHtml(category_id, data);
				if(contents_type === "VODB")	fn_vodHtml(category_id, data);
				//if(contents_type === "UTUB")	fn_youtubeHtml(category_id, data);
				
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

/**
 * 유투브 설정
 */
function fn_youtubeHtml(category_id, data){

	var addhtml = [];
	var ah = 0;
	//alert(data[1].category_id);
	var pack_id, category_id, youtube_id, youtube_nm, youtube_type, youtube_url, img_file = "";

	var length = data.length;
	addhtml[ah++]	= "<br/>";
	for(var i=0; i<length; i++){
		youtube_nm		= data[i].youtube_nm;
		youtube_type	= data[i].youtube_type;
		youtube_url		= data[i].youtube_url;
		img_file		= data[i].img_file;

		addhtml[ah++]	= "youtube_nm : "+youtube_nm+" | youtube_type : "+youtube_type+" | youtube_url : "+youtube_url+" | img_file : "+img_file+" <br/>\n";
	}

	//alert("category_id >> "+category_id+", "+$("#"+category_id)+"\n"+addhtml.join(''));

	$("#gpack_div_"+category_id).append(addhtml.join(''));
}
</script>
</head>
<body leftmargin="0" topmargin="0">
	bg_img_url :: <c:out value="${bg_img_url }"></c:out>
	<br/>
	<c:forEach items="${categorylist }" var="category_list" varStatus="status">
		<div id="gpack_div_${category_list.category_id }" style="float: left; border-style: solid; border-width: 1px; width: 200px;">
			<c:out value="${category_list.category_id }"></c:out>.<c:out value="${category_list.category_nm }"></c:out>
			<script type="text/javascript">
			//fn_categoryContentsLoad('${category_list.pack_id}', '${category_list.category_id}', '${category_list.contents_type}');
			</script>
		</div>
	</c:forEach>
</body>
</html>