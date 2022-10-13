<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EDGE" />
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>

<script type="text/javascript">

var make_vod = false;	//추가, 수정시 true
$(document).ready(function(){
	
	//추가 버튼
	$("#insertbtn").click(function (){
		make_vod = true;
		fn_addVodList();
	});

	//삭제 버튼
	$("#deletebtn").click(function (){
		fn_deleteVodList();
	});
	
	//저장 버튼
	$("#savebtn").click(function (){
		fn_saveVodFrm();
	});

	//재작성 버튼
	$("#resetbtn").click(function (){
		var contents_id = $("#contents_id").val();
		if(contents_id){
			fn_VodDetailView(contents_id);
		}
	});

	//닫기 버튼
	$("#closebtn").click(function (){
		self.close();
	});
	
	select_bind();

	//카테고리 선택 버튼(이동경로 타입이 MT002 MT006 MT007)
	$("#btn_category").hide();
	$("#btn_category").click(function(){
		fn_imcs_category_data ();
	});

	//채널 선택 버튼 (이동경로 타입이 MT005)
	$("#btn_channel").hide();
	
	//월정액 상품 선택 버튼 (이동경로 타입이 MT001)
	$("#btn_product").hide();
	
	$("#tr_dal_type").hide();
	
	// 채널 설정
	$("#btn_channel").click(function(){
		var url = "<%=webRoot%>/admin/gpack/event/getGpackPromotionChannelView.do?opener=contents";
		window.open(url, "viewchannel", "width=700,height=540");
	});
	
	// 월정액 상품 설정
	$("#btn_product").click(function(){
		var url = "<%=webRoot%>/admin/gpack/event/getGpackPromotionProductView.do";
		window.open(url, "viewProduct", "width=700,height=540");
	});
	
	// 전체 선택
	$("#allchk").click(function(){
		if($("#allchk").is(":checked")){
	        $("input[name=delchk]").each(function(i) {
	            $(this).attr('checked', true);
	        });
		}else{
		    $("input[name=delchk]").each(function(i) {
	            $(this).attr('checked', false);
	        });
		}
	});
});

/**
 * 수정, 추가 시 화면 
 */
function fn_detailSet(view_gb, data){

	if(view_gb === "INSERT"){
		$("#contents_id").val("");
		$("#contents_nm").val("");
		$("#imcs_category_id").val("");
		$("#album_id").val("");
		$("#contents_text").val("");
		$("#img_file").val("");
		$("#ordered").val("");
		$("input[name=use_yn]:checked").val("N");

		$("#movepath_type").val("MT001");
		$("#movepath").val("");

		//TODO :: IMCS 앨범 선택 부분에 TEXT 창 하나 둬서 .. 

	}else if(view_gb === "MODIFY"){
		$("#contents_id").val(data.contents_id);
		$("#contents_nm").val(data.contents_nm);
		$("#imcs_category_id").val(data.imcs_category_id);
		$("#album_id").val(data.album_id);
		$("#contents_text").val(data.imcs_text);
		$("#img_file").val(data.img_file);	
		$("#ordered").val(data.ordered);
		$("input[name=use_yn]").filter("[value="+data.use_yn+"]").prop("checked", true);

		$("#movepath_type").val(data.movepath_type);
		
		if(data.movepath_type == "MT005"){
			$("#movepath").val(data.chnl_info);
			$("#chnl_service_id").val(data.movepath);
		}else if(data.movepath_type == "MT001"){
			$("#movepath").val(data.product_info);
			$("#product_id").val(data.movepath);
		}else if(data.movepath_type == "MT004"){
			$("input[name=dal_type]").filter("[value="+data.dal_type+"]").prop("checked", true);
			$("#movepath").val(data.movepath);
		}else{
			$("#movepath").val(data.movepath);
		}

		//TODO :: IMCS 앨범 선택 부분에 TEXT 창 하나 둬서 .. 
	}
	
	$("#contents_nm").focus();

	$("#img_file").attr("disabled", false);
	$("#contents_nm").attr("disabled", false);
	$("input[name=use_yn]").attr("disabled", false);
	$("#movepath_type").attr("disabled", false);

	fn_change_movepath();
} 

//이동경로 타입에 맞게 화면 구성 (아예 숨기던지 hide, 입력만 못하게 막던지 disabled)
function fn_change_movepath(){
	var move_type = $("#movepath_type").val();

	// IMCS 카테고리 SELECT BOX 지워!
	var selObj = $("div#div_category_select select");
	for(var i=1; i<selObj.length; i++){
		$(selObj[i]).remove();
 	}
	$("select[name='category_select']").eq(0).val("---");

	$("#contents_text").attr("disabled", true);
	$("#movepath").attr("disabled", true);
	$("select[name=category_select]").attr("disabled", true);
	$("#btn_category").hide();
	$("#btn_channel").hide();
	$("#btn_product").hide();
	$("#tr_dal_type").hide();

	if(move_type === "MT003"){
		//이동경로
		$("#movepath").attr("disabled", false);
		
	}else if(move_type === "MT004"){
		$("#movepath").attr("disabled", false);
		$("#tr_dal_type").show();
		
	}else if(move_type === "MT001"){
		$("#btn_product").show();
		
	}else if(move_type === "MT005"){
		$("#btn_channel").show();
		//$("#movepath").attr("disabled", false);
	}else{
		//imcs 카테고리
		$("select[name=category_select]").attr("disabled", false);
		$("#btn_category").show();
	}
}


/**
 * 상세보기 
 */
function fn_VodDetailView(contents_id){
	make_vod = true;
	var pack_id = $("#pack_id").val();
	var category_id = $("#category_id").val();
	
	$.post("<%=webRoot%>/admin/gpack/contents/getGpackContentsView.do", 
			 {pack_id : pack_id, category_id : category_id, contents_id : contents_id},
			  function(data) {
				 	if(data.length == 0){
				 	}else{
						$("#view_gb").val("MODIFY");
						fn_detailSet($("#view_gb").val(), data);
				 	}
			  },
			  "json"
    );	
}

/**
 * 추가 
 */
function fn_addVodList(){
	if($("#tbl_contentsList #tr_add").length > 0){
		alert("입력하시던 상세정보를 저장해주세요.");
	}else if(make_vod){
		//todo :: 상세 화면 Setting
		$("#view_gb").val("INSERT");
		fn_detailSet($("#view_gb").val(), null);
	}
}


/**
 * 삭제
 */
function fn_deleteVodList(){
	if($("input[name=delchk]:checked").length < 1){
		alert("삭제할 콘텐츠를 선택해주세요");
	}else{
		if(confirm("선택한 콘텐츠를 삭제하시겠습니까?")){
			var pack_id = $("#pack_id").val();
			var category_id = $("#category_id").val();
			var smartUXManager = $("#smartUXManager").val();
			var contents_arry = [];
			
			$("input[name=delchk]:checked").each(function(i) {
				if($(this).val() === "un"){	//화면에서 추가한거
					$(this).parent().parent().remove(); 
				}else if($(this).val() !== "un"){	//기존거
					contents_arry.push($(this).val());
				}
	        });
	        
			var postUrl = "<%=webRoot%>/admin/gpack/contents/deleteGpackContents.do";
			$.post( postUrl, 
					{	pack_id		: pack_id, 
						category_id	: category_id,
						contents_ids: contents_arry,
						smartUXManager : smartUXManager},
					function(data) {
						// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
						 	var flag = data.flag;
						 	var message = data.message;
						 	
						 	if(flag == "0000"){						// 정상적으로 처리된 경우
								alert("삭제되었습니다.");
								make_vod = false;
								location.reload();
			                	opener.location.reload();
							}else{
						 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
						 	}
					},
					"json"
		    	);
		}
	}
}
 
/**
 * 저장
 */
function fn_saveVodFrm(){

	if(fn_values_check()){
		var postUrl = "<%=webRoot%>/admin/gpack/contents/insertGpackContents.do";
		$("#frm_category_view").ajaxSubmit({
			dataType : 'json',
			url : postUrl,
			success : function(result) {
			 	var flag = result.flag;
				var message = result.message;
	
			 	if(flag == "0000"){						// 정상적으로 처리된 경우
					alert("저장되었습니다.");
					make_vod = false;
					location.reload();
                	opener.location.reload();
				}else{
			 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
			 	}
			},
			error : function(data, status, err) {
			 	var flag = data.flag;
			 	var message = data.message;
				alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
			}
		});
	}
}

/*
 * 저장 전 데이타 확인
	- 이동경로 타입 
	(MT001 : 월정액가입팝업, MT002 : VOD 상세페이지, MT003 : 양방향 어플, MT004 : DAL, MT005 : 채널, MT006 : 카테고리_시리즈, MT007 : 카테고리_상위)
 */
function fn_values_check(){
	var return_val = false;

	var contents_nm 	= $("#contents_nm").val();
	var move_type 		= $("#movepath_type").val();
	var movepath 		= $("#movepath").val();
	var imcs_category_id= $("#imcs_category_id").val();
	var imcs_album_id 	= $("#album_id").val();
	var img_file 		= $("#img_file").val();

	var view_gb = $("#view_gb").val();

	if(make_vod){
		//콘텐츠 명을 입력하지 않았을 경우, 조회된 콘텐츠 이름을 넣는다. 
		if(!contents_nm && (move_type === "MT002" || move_type === "MT006")){
			$("#contents_nm").val($("#contents_text").val());
			contents_nm 	= $("#contents_nm").val();
		}
		
		if(!contents_nm && (move_type === "MT002" || move_type === "MT006")){
			alert("카테고리를 선택해주세요.");
	 		//$("div#div_category_select select")[selObj.length-1].focus();
		}else if(!contents_nm && (move_type !== "MT002" && move_type !== "MT006")){
			alert("제목을 입력해주세요.");
			$("#contents_nm").focus();
		}else if(!move_type){
			alert("이동경로타입을 선택해주세요.");
			$("#movepath_type").focus();
		}else if(!movepath && (move_type === "MT003" || move_type === "MT005" || move_type === "MT001" || move_type === "MT004")){
			alert("이동경로를 입력해주세요.");
			$("#movepath").focus();
		}else if(!imcs_category_id && (move_type === "MT002" || move_type === "MT006" || move_type === "MT007")){
			alert("카테고리를 선택해주세요.");
	 		//$("div#div_category_select select")[selObj.length-1].focus();
		}else if(!imcs_album_id && move_type === "MT002"){
			alert("콘텐츠를 선택해주세요.");
	 		//$("div#div_category_select select")[selObj.length-1].focus();
		}else if(view_gb === "INSERT" && !img_file && (move_type !== "MT002" && move_type !== "MT006")){
			alert("포스터이미지를 선택해주세요.");
			$("#img_file").focus();
		}else{
			<%--
			var tmp = "pack_id >> "+$("#pack_id").val()+"\n";
			tmp += "category_id >> "+$("#category_id").val()+"\n";
			tmp += "contents_id >> "+$("#contents_id").val()+"\n";
			tmp += "contents_nm >> "+$("#contents_nm").val()+"\n";
			tmp += "movepath_type >> "+$("#movepath_type").val()+"\n";
			tmp += "movepath >> "+$("#movepath").val()+"\n";
			tmp += "imcs_category_id >> "+$("#imcs_category_id").val()+"\n";
			tmp += "album_id >> "+$("#album_id").val()+"\n";
			tmp += "img_file >> "+$("#img_file").val()+"\n";
			tmp += "ordered >> "+$("#ordered").val()+"\n";
			tmp += "use_yn >> "+$("input[name=use_yn]:checked").val()+"\n";
			tmp += "view_gb >> "+$("#view_gb").val()+"\n";
			tmp += "smartUXManager >> "+$("#smartUXManager").val()+"\n";
			
			alert("tmp\n"+tmp);
			--%>
			
			return_val = true;
		}
	}
	
	return return_val;
}


/**
 * Category Selectbox 만들기 (전체 감싸는 div = category_select)
 */
function select_bind(){

	$("select[name='category_select']").unbind("change");
	
	$("select[name='category_select']").bind("change", function(){
		var imcs_category_id = $(this).val();
 		var move_type = $("#movepath_type").val();
 		
 		if( move_type === "MT002" || move_type === "MT006" ) {	//VOD _ 상세
 			if(imcs_category_id.split("^").length === 2){
 				imcs_category_id = imcs_category_id.split("^")[1];
 	 	 	}
 	 	}
 	 	//alert("imcs_category_id >> "+imcs_category_id);

		// 현재 select 태그의 부모 이후에 나오는 모든 select를 구한다
 		var selObj = $("div#div_category_select select");
 		var length = selObj.length;
 		var bol_remove = false;
 		var tmp_category_id = "";
 		for(var i=0; i<length; i++){
 			tmp_category_id = $(selObj[i]).val() ;
 			if(tmp_category_id.split("^").length === 2){
 				tmp_category_id = tmp_category_id.split("^")[1];
 	 	 	}
 			
			if(bol_remove){
				$(selObj[i]).remove();
			}
			if(tmp_category_id === imcs_category_id){
				bol_remove = true;
			}
 	 	}
		
		//선택한 IMCS카테고리 값 초기화
		$("#imcs_category_id").val("");
		$("#contents_text").val("");
		
		if(imcs_category_id == "---"){		// 선택해주세요를 선택한 경우
		}else{
			$.post("<%=webRoot%>/admin/gpack/contents/getGpackImcsCategoryList.do", 
					 {category_id : imcs_category_id},
					  function(data) {
						 	if(data.length == 0){
						 	}else{

						 		var html = [], inHtml = [], h=-1, ih=-1;
						 		
						 		for(var i=0 ; i < data.length; i++){
							 		if(data[i].contents_id){ 
								 		if( move_type === "MT002" ) {	//VOD _ 상세
								 			inHtml[++h] = "<option value=\"" + data[i].category_id + "^" + data[i].contents_id + "\">" + data[i].contents_name + "</option>\n";
								 		}
								 	}else{
									 	if( move_type === "MT006"){	//카테고리 _ 시리즈 
									 		inHtml[++ih] = "<option value=\"" + data[i].series_yn + "^" + data[i].category_id + "\">" + data[i].category_name + "</option>\n";
										}else{
								 			inHtml[++ih] = "<option value=\"" + data[i].category_id + "\">" + data[i].category_name + "</option>\n";
										}
								 	}
						 		}

						 		if(inHtml.length > 0) {

							 		html[++h] = "<br\" >\n";
							 		html[++h] = "<select name=\"category_select\" style=\"float: left;\" >\n";
							 		html[++h] = "<option value=\"---\" selected>선택해주세요</option>\n";
							 		html[++h] = inHtml;
							 		html[++h] = "</select>\n";
							 		
						 			$("#div_category_select").append(html.join(''));

							 		select_bind();
						 		}
						 	}
					  },
					  "json"
		    );
		}
		
	});
}

//선택된 imcs 카테고리와 앨범id를 ..
function fn_imcs_category_data (){

	var movepath_type = $("#movepath_type").val();
	var selectLen = $("select[name='category_select']").length;
	var imcs_id_tmp = $("select[name='category_select']").eq(selectLen-1).val();
	var imcs_category_id = "";
	var imcs_contents_id = "";
	var imcs_contents_text = "";
	
	if( movepath_type === "MT002" ) {
		
		if(imcs_id_tmp === "---"){	
			alert("콘텐츠를 선택해주세요.");
		}else if(imcs_id_tmp.split("^").length !== 2){
			alert("선택할 수 없는 콘텐츠입니다.");
		}else{
			imcs_contents_text = $("select[name='category_select'] option:selected").eq(selectLen-1).text();
			imcs_category_id = imcs_id_tmp.split("^")[0];
			imcs_contents_id = imcs_id_tmp.split("^")[1];
	
			$("#imcs_category_id").val(imcs_category_id);
			$("#album_id").val(imcs_contents_id);
			$("#contents_text").val(imcs_contents_text);

			alert("["+imcs_contents_text+"] 콘텐츠를 선택하셨습니다.");
		}
		
	}else if( movepath_type === "MT006" ){
		
		imcs_id_tmp = $("select[name='category_select']").eq(selectLen-1).val();
		
		if(imcs_id_tmp === "---"){
			alert("하위 카테고리를 선택해주세요.");
		}else if(imcs_id_tmp.split("^").length !== 2){
			alert("선택할 수 없는 콘텐츠입니다.");
		}else{
 
			var series_yn = imcs_id_tmp.split("^")[0];
			if(series_yn !== "Y"){
				alert("시리즈가 아니므로 선택할 수 없습니다.");
			}else{
				imcs_contents_text = $("select[name='category_select'] option:selected").eq(selectLen-1).text();
				imcs_category_id = imcs_id_tmp.split("^")[1];
				
				$("#imcs_category_id").val(imcs_category_id);
				$("#contents_text").val(imcs_contents_text);
	
				alert("["+imcs_contents_text+"] 콘텐츠를 선택하셨습니다.");
			}
		}
	}else if( movepath_type === "MT007" ){
		
		var tmpVal = "";
		var selectIdx = 0;
		for(var i=0; i<selectLen; i++){
			tmpVal = $("select[name='category_select']").eq(i).val();
			//선택해주세요 전의 카테고리 index를 가져옴
			if(tmpVal !== "---")	selectIdx = i;
			
		}
		imcs_id_tmp = $("select[name='category_select']").eq(selectIdx).val();
		
		if(imcs_id_tmp === "---"){
			alert("하위 카테고리를 선택해주세요.");
		}else{
			imcs_contents_text = $("select[name='category_select'] option:selected").eq(selectIdx).text();
			imcs_category_id = imcs_id_tmp.split("^")[0];
			
			$("#imcs_category_id").val(imcs_category_id);
			$("#contents_text").val(imcs_contents_text);

			alert("["+imcs_contents_text+"] 콘텐츠를 선택하셨습니다.");
		}
	}
}

</script>
</head>
<body leftmargin="0" topmargin="0" style="font-size: 12px;">
<div id="divBody" style="height:100%">
<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
    <tbody>    
      <tr>
      <td colspan="2" valign="top">
        <table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
        <tbody>
        <tr>            
            <td valign="top">
            <table border="0" cellpadding="0" cellspacing="0" width="98%">
                <tbody>               
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
						                <tr>
						                    <td class="3_line" height="1"></td>
						                </tr>
						                <!-- 리스트 시작 -->
						                <tr>
						                    <td>
						                        <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            <tbody>
						                            <tr>
						                                <td height="25">
						                                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
						                                        <tbody>
						                                        <tr>
						                                        <c:if test="${promotionVO ne null}"><c:if test="${promotionVO.category_nm ne ''}">
						                                        	<td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
						                                            <td width="100px" style="vertical-align: top;overflow:hidden;white-space:nowrap;" class="bold">${promotionVO.category_nm}</td>
						                                        </c:if></c:if>
						                                            <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
						                                            <td class="bold">직접편성 상세조회</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
					                            
					                            <table id="tbl_contentsList" border="0" cellspacing="0" cellpadding="0" class="board_list" style="width: 100%;">
					                            	<colgroup>
					                            		<col width="3%"  class="table_td_04" style="vertical-align: middle;">
					                            		<col width="10%" class="table_td_04">
					                            		<col width="*"   class="table_td_04" style="text-align: left;">
					                            		<col width="20%" class="table_td_04" style="text-align:center;">
					                            		<col width="15%" class="table_td_04" style="text-align:center;">
					                            	</colgroup>
					                            	<thead>
													<tr>
														<th style="vertical-align: middle; height: 36px;"><input type="checkbox" id="allchk" name="allchk"></th>
														<th style="height: 36px;">순번</th> 
														<th style="height: 36px;">콘텐츠명</th>
														<th style="height: 36px;">이동경로</th>
														<th style="height: 36px;">사용여부</th>
													</tr>
					                            	</thead>
					                            	<tbody>
													<c:choose>
														<c:when test="${contentsList==null || fn:length(contentsList) == 0}">
															<tr id="tr_empty">
																<td colspan="5" class="table_td_04">등록된 콘텐츠가 없습니다</td>
															</tr>
														</c:when>
														<c:otherwise>
															<c:forEach var="item" items="${contentsList}" varStatus="status">
																<tr>
																	<td>
																		<input type="checkbox" name="delchk" value="${item.contents_id}" />
																	</td>
																	<td>
																		<c:out value="${status.index+1}"></c:out>
																	</td>
																	<td>
																		<a href="javascript:fn_VodDetailView('${item.contents_id}');">${item.contents_nm}</a>
																	</td>
																	<td>
																		<c:choose>
																			<c:when test="${item.movepath_type eq 'MT001' }">월정액가입팝업</c:when>
																			<c:when test="${item.movepath_type eq 'MT002' }">VOD 상세페이지</c:when>
																			<c:when test="${item.movepath_type eq 'MT003' }">양방향 어플</c:when>
																			<c:when test="${item.movepath_type eq 'MT004' }">DAL</c:when>
																			<c:when test="${item.movepath_type eq 'MT005' }">채널</c:when>
																			<c:when test="${item.movepath_type eq 'MT006' }">카테고리_시리즈</c:when>
																			<c:when test="${item.movepath_type eq 'MT007' }">카테고리_상위</c:when>
																		</c:choose>
																	</td>
																	<td>
																		<!-- 사용여부 -->
																		<c:choose>
																			<c:when test="${item.use_yn eq 'Y' }">사용</c:when>
																			<c:otherwise>사용안함</c:otherwise>
																		</c:choose>
																	</td>
																</tr>
															</c:forEach>
														</c:otherwise>
													</c:choose>
													</tbody>
					                            </table>
					                            
					                            <!-- 목록 버튼 -->
					                            <table border="0" cellpadding="0" cellspacing="0" style="width: 100%; text-align: left;">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<span class="button small blue" id="insertbtn">추가</span>	
						                                	<span class="button small blue" id="deletebtn">삭제</span>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
					                            
						                        <form id="frm_category_view" name="frm_category_view" method="post" action="/smartux_adm/admin/gpack/contents/insertGpackYoutube.do" enctype="multipart/form-data">
						                        <input type="hidden" id="pack_id"		name="pack_id"		value="${param.pack_id }"/>
						                        <input type="hidden" id="category_id"	name="category_id"	value="${param.category_id }"/>
						                        <input type="hidden" id="contents_id"	name="contents_id"/>
						                        <input type="hidden" id="ordered"		name="ordered"/>
						                        <input type="hidden" id="view_gb"		name="view_gb"/>
												<input type="hidden" id="smartUXManager" name="smartUXManager" value="<%=id_decrypt %>" />

					                            <!-- 직접편성 시이작! -->
					                            <table border="0" cellpadding="0" cellspacing="0" class="board_data" style="width: 100%;">
					                            	<colgroup>
					                            		<col width="30%">
					                            		<col width="5%">
					                            		<col width="*" align="left" >
					                            	</colgroup>
					                                <tbody>
					                                <tr align="center">
					                                    <th>제목</th>
					                                    <td></td>
					                                    <td>
						                                    <input type="text" id="contents_nm" name="contents_nm" size="35" maxlength="100" style="font-size: 12px;"  disabled="disabled" />
														</td>
					                                </tr>
					                                
					                                <tr align="center">
					                                    <th>이동경로 타입</th>
					                                    <td></td>
					                                    <td>
					                                    	<select name="movepath_type" id="movepath_type" onchange="javascript:fn_change_movepath();" disabled="disabled">
																<option value="MT001">월정액가입팝업</option>
																<option value="MT002">VOD 상세페이지</option>
																<option value="MT003">양방향 어플</option>
																<option value="MT004">DAL</option>
																<option value="MT005">채널</option>
																<option value="MT006">카테고리_시리즈</option>
																<option value="MT007">카테고리_상위</option>
					                                    	</select>
														</td>
					                                </tr>
					                                
					                                <tr id="tr_movepath" align="center">
					                                    <th>이동경로</th>
					                                    <td></td>
					                                    <td>
					                                    	<input type="text" id="movepath" name="movepath"  disabled="disabled"/>
								                            <!-- 채널 선택버튼 -->
								                            <span class="button small blue" id="btn_channel" >선택</span>
								                            <span class="button small blue" id="btn_product" >선택</span>
						                       				<input type="hidden" id="chnl_service_id" name="chnl_service_id"/>
						                       				<input type="hidden" id="product_id" name="product_id"/>
														</td>
					                                </tr>
					                                
					                                <tr id="tr_dal_type" align="center">
					                                    <th>DAL 타입</th>
					                                    <td></td>
					                                    <td>
					                                    	<input type="radio" id="dal_typeF" name="dal_type" value="F" /><label for="dal_typeF">Full</label>
					                                    	<input type="radio" id="dal_typeH" name="dal_type" value="H" checked="checked"/><label for="dal_typeH">Half</label>
														</td>
					                                </tr>
					                                
					                                <tr id="tr_movepath_imcs" align="center">
					                                    <th>IMCS 앨범 선택</th>
					                                    <td></td>
					                                    <td>
								                            <div id="div_category_select">
								                            	<input type="hidden" name="imcs_category_id" 	id="imcs_category_id"/>
								                            	<input type="hidden" name="album_id" 			id="album_id"/>
								                            	<input type="text" name="contents_text" 		id="contents_text" disabled="disabled"/>
								                            	<br/> 
								                            	
								                            	<!-- admin/schedule/getCategoryAlbumList -->
																<c:choose>
																	<c:when test="${categoryResult==null || fn:length(categoryResult) == 0}">
																		<div>검색된 카테고리가 없습니다</div>
																	</c:when>
																	<c:otherwise>
																		<select name="category_select" style="float: left;" disabled="disabled">
																			<option value="---">선택해주세요</option>
																		<c:forEach var="item" items="${categoryResult}" varStatus="status">
																			<option value="${item.category_id}">${item.category_name }</option>
																		</c:forEach>
																		</select>
																	</c:otherwise>
																</c:choose>
								                            	<!-- admin/schedule/getCategoryAlbumList -->
								                            </div>
								                            
								                            <!-- 카테고리 선택버튼 -->
								                            <span class="button small blue" id="btn_category" >선택</span>
														</td>
					                                </tr>
					                                
					                                <tr align="center">
					                                    <th>포스터이미지</th>
					                                    <td></td>
					                                    <td>
					                                    	<input type="file" name="img_file" id="img_file" disabled="disabled" />
														</td>
					                                </tr>
					                                
					                                <tr align="center">
					                                    <th>사용여부</th>
					                                    <td></td>
					                                    <td>
					                                    	<input type="radio" id="use_ynY" name="use_yn" value="Y" disabled="disabled" /><label for="use_ynY">예</label>
					                                    	<input type="radio" id="use_ynN" name="use_yn" value="N" checked="checked" disabled="disabled" /><label for="use_ynN">아니오</label>
														</td>
					                                </tr>
					                            	</tbody>
					                            </table>
					                            <!-- vod 상세조회 끝끝끝! -->
					                            
					                            <!-- 상세조회 버튼 -->
					                            <table border="0" cellpadding="0" cellspacing="0" style="width: 100%; text-align: left;">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<span class="button small blue" id="savebtn">저장</span>
						                                	<span class="button small blue" id="closebtn">닫기</span>	
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                       	</form>
					                            
						                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
						                            <tbody><tr>
						                                <td height="1"></td>
						                            </tr>
						                            <tr>
						                                <td class="3_line" height="3"></td>
						                            </tr>
						                            <tr>
						                                <td>&nbsp;</td>
						                            </tr>
						                        </tbody>
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
</tbody>
</table>
</div>

</body>
</html>