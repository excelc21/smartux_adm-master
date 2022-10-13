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

	$("#preview_selectbtn").hide();
	$("#feature_selectbtn").hide();
	
	select_bind();

	//예고편 선택 버튼
	$("#preview_selectbtn").click(function(){
		fn_imcs_category_data ("preview");
	});
	
	//본편 선택 버튼
	$("#feature_selectbtn").click(function(){
		fn_imcs_category_data ("feature");
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
	
	// 순서변경
	$("#orderbybtn").click(function (){
		var pack_id = $("#pack_id").val();
		var category_id = $("#category_id").val();
		location.href="<%=webRoot%>/admin/gpack/event/changeGpackPlaylistOrder.do?pack_id=" + pack_id + "&category_id=" + category_id;
	});
});

/**
 * 수정, 추가 시 화면 
 */
function fn_detailSet(view_gb, data){

	if(view_gb === "INSERT"){
		$("#playlist_id").val("");
		$("#playlist_nm").val("");
		$("#preview_imcs_category_id").val("");
		$("#preview_imcs_album_id").val("");
		$("#preview_contents_text").val("");
		$("#feature_imcs_category_id").val("");
		$("#feature_imcs_album_id").val("");
		$("#feature_contents_text").val("");
		$("input[name=use_yn]:checked").val("N");

	}else if(view_gb === "MODIFY"){
		
		$("#playlist_id").val(data.playlist_id);
		$("#playlist_nm").val(data.playlist_nm);
		$("#preview_imcs_category_id").val(data.preview_imcs_category_id);
		$("#preview_imcs_album_id").val(data.preview_imcs_album_id);
		$("#preview_contents_text").val(data.preview_contents_text);
		$("#feature_imcs_category_id").val(data.feature_imcs_category_id);
		$("#feature_imcs_album_id").val(data.feature_imcs_album_id);
		$("#preview_contents_text").val(data.preview_imcs_text);
		$("#feature_contents_text").val(data.feature_imcs_text);
		$("input[name=use_yn]").filter("[value="+data.use_yn+"]").prop("checked", true);

		//TODO :: IMCS 앨범 선택 부분에 TEXT 창 하나 둬서 .. 
	}
	
	$("#playlist_nm").focus();

	$("#playlist_nm").attr("disabled", false);
	$("input[name=use_yn]").attr("disabled", false);

	fn_change_movepath();
} 

//이동경로 타입에 맞게 화면 구성 (아예 숨기던지 hide, 입력만 못하게 막던지 disabled)
function fn_change_movepath(){

	// IMCS 카테고리 SELECT BOX 지워!
	var selObj = $("div#div_category_select select");
	for(var i=1; i<selObj.length; i++){
		$(selObj[i]).remove();
 	}
	$("select[name='category_select']").eq(0).val("---");
	
	//imcs 카테고리
	$("select[name=category_select]").attr("disabled", false);
	$("#preview_selectbtn").show();
	$("#feature_selectbtn").show();
	
}


/**
 * 상세보기 
 */
function fn_playlistView(playlist_id){
	make_vod = true;
	var pack_id = $("#pack_id").val();
	var category_id = $("#category_id").val();
	
	$.post("<%=webRoot%>/admin/gpack/event/getGpackPlaylistView.do", 
			 {pack_id : pack_id, category_id : category_id, playlist_id : playlist_id},
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
		alert("삭제할 플레이리스트를 선택해주세요");
	}else{
		if(confirm("선택한 플레이리스트를 삭제하시겠습니까?")){
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
	        
			var postUrl = "<%=webRoot%>/admin/gpack/event/deleteGpackPlaylist.do";
			$.post( postUrl, 
					{	pack_id		: pack_id, 
						category_id	: category_id,
						playlist_ids: contents_arry,
						smartUXManager : smartUXManager},
					function(data) {
						// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
						 	var flag = data.flag;
						 	var message = data.message;
						 	
						 	if(flag == "0000"){						// 정상적으로 처리된 경우
								alert("삭제되었습니다.");
								make_vod = false;
								location.reload();
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
		var postUrl = "<%=webRoot%>/admin/gpack/event/updateGpackPlaylist.do";
		$("#frm_playlist_view").ajaxSubmit({
			dataType : 'json',
			url : postUrl,
			success : function(result) {
			 	var flag = result.flag;
				var message = result.message;
	
			 	if(flag == "0000"){						// 정상적으로 처리된 경우
					alert("플레이리스트를 등록하였습니다.");
					make_vod = false;
					location.reload();
				}else{
			 		alert("작업 중 오류가 발생하였습니다.\nmessage : " + message);
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
 */
function fn_values_check(){
	
	var return_val = false;
	
	var playlist_nm 	= $("#playlist_nm").val();
	var preview_imcs_album_id 	= $("#preview_imcs_album_id").val();
	var feature_imcs_album_id 	= $("#feature_imcs_album_id").val();
	
	if(make_vod){
		if(!preview_imcs_album_id){
			alert("예고편을 선택해주세요.");
	 		$("div#div_category_select select")[selObj.length-1].focus();
		}else if(!feature_imcs_album_id){
			alert("본편을 선택해주세요.");
			$("#contents_nm").focus();
		}else if(!playlist_nm){
			//플레이리스트 명을 입력하지 않았을 경우, 조회된 본편 이름을 넣는다.
			$("#playlist_nm").val($("#feature_contents_text").val());
			return_val = true;
		}else{
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

		// 현재 select 태그의 부모 이후에 나오는 모든 select를 구한다
 		var selObj = $("div#div_category_select select");
 		var length = selObj.length;
 		var bol_remove = false;
 		for(var i=0; i<length; i++){
			if(bol_remove){
				$(selObj[i]).remove();
			}
			if($(selObj[i]).val() === imcs_category_id){
				bol_remove = true;
			}
 	 	}
		
		//선택한 IMCS카테고리 값 초기화
		//$("#preview_imcs_category_id").val("");
		//$("#preview_contents_text").val("");
		
		if(imcs_category_id == "---"){		// 선택해주세요를 선택한 경우
		}else{
			$.post("<%=webRoot%>/admin/gpack/contents/getGpackImcsCategoryList.do", 
					 {category_id : imcs_category_id, product_id : "playlist"},
					  function(data) {
						 	if(data.length == 0){
						 	}else{

						 		var html = [], inHtml = [], h=-1, ih=-1;
						 		
						 		for(var i=0 ; i < data.length; i++){
							 		if(data[i].contents_id){ 
								 		inHtml[++h] = "<option value=\"" + data[i].category_id + "^" + data[i].contents_id + "\">" + data[i].contents_name + "</option>\n";
								 	}else{
								 		inHtml[++ih] = "<option value=\"" + data[i].category_id + "\">" + data[i].category_name + "</option>\n";
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
function fn_imcs_category_data (type){

	var selectLen = $("select[name='category_select']").length;
	var imcs_id_tmp = $("select[name='category_select']").eq(selectLen-1).val();
	var imcs_category_id = "";
	var imcs_contents_id = "";
	var imcs_contents_text = "";
		
	if(imcs_id_tmp === "---"){	
		alert("앨범을 선택해주세요.");
	}else if(imcs_id_tmp.split("^").length !== 2){
		alert("선택할 수 없는 플레이리스트입니다.");
	}else{
		imcs_contents_text = $("select[name='category_select'] option:selected").eq(selectLen-1).text();
		imcs_category_id = imcs_id_tmp.split("^")[0];
		imcs_contents_id = imcs_id_tmp.split("^")[1];

		if(type == "preview"){
			$("#preview_imcs_category_id").val(imcs_category_id);
			$("#preview_imcs_album_id").val(imcs_contents_id);
			$("#preview_contents_text").val(imcs_contents_text);
		}else{
			$("#feature_imcs_category_id").val(imcs_category_id);
			$("#feature_imcs_album_id").val(imcs_contents_id);
			$("#feature_contents_text").val(imcs_contents_text);
		}
		alert("["+imcs_contents_text+"] 플레이리스트를 선택하셨습니다.");
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
						                                            <td class="bold">플레이리스트 상세조회</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
					                            
					                            <table id="tbl_contentsList" border="0" cellspacing="0" cellpadding="0" class="board_list" style="width: 100%;">
					                            	<colgroup>
					                            		<col width="3%"  class="table_td_04" style="vertical-align:middle;">
					                            		<col width="10%" class="table_td_04" style="text-align:center;">
					                            		<col width="*"   class="table_td_04" style="text-align:left;">
					                            		<col width="15%" class="table_td_04" style="text-align:center;">
					                            	</colgroup>
					                            	<thead>
													<tr>
														<th style="vertical-align: middle; height: 36px;"><input type="checkbox" id="allchk" name="allchk"></th>
														<th style="height: 36px;">순번</th> 
														<th style="height: 36px;">플레이리스트</th>
														<th style="height: 36px;">사용여부</th>
													</tr>
					                            	</thead>
					                            	<tbody>
													<c:choose>
														<c:when test="${playlistVOList==null || fn:length(playlistVOList) == 0}">
															<tr id="tr_empty">
																<td colspan="5" class="table_td_04">검색된 플레이리스트가 없습니다</td>
															</tr>
														</c:when>
														<c:otherwise>
															<c:forEach var="item" items="${playlistVOList}" varStatus="status">
																<tr>
																	<td>
																		<input type="checkbox" name="delchk" value="${item.playlist_id}" />
																	</td>
																	<td>
																		<c:out value="${status.index+1}"></c:out>
																	</td>
																	<td>
																		<a href="javascript:fn_playlistView('${item.playlist_id}');">${item.playlist_nm}</a>
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
						                                	<span class="button small blue" id="orderbybtn">순서바꾸기</span>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
					                            
						                        <form id="frm_playlist_view" name="frm_playlist_view" method="post" action="/smartux_adm/admin/gpack/event/updateGpackPlaylist.do">
						                        <input type="hidden" id="pack_id"		name="pack_id"		value="${pack_id }"/>
						                        <input type="hidden" id="category_id"	name="category_id"	value="${category_id }"/>
						                        <input type="hidden" id="playlist_id"	name="playlist_id"/>
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
					                                    <th>타이틀</th>
					                                    <td></td>
					                                    <td>
						                                    <input type="text" id="playlist_nm" name="playlist_nm" size="35" maxlength="100" style="font-size: 12px;"  disabled="disabled" />
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
					                                <tr id="tr_preview_imcs" align="center">
					                                    <th>예고편</th>
					                                    <td></td>
					                                    <td>
							                            	<input type="hidden" name="preview_imcs_category_id" id="preview_imcs_category_id"/>
							                            	<input type="hidden" name="preview_imcs_album_id" id="preview_imcs_album_id"/>
							                            	<input type="text" name="preview_contents_text" id="preview_contents_text" disabled="disabled"/>
														</td>
					                                </tr>
					                                <tr id="tr_feature_imcs" align="center">
					                                    <th>본편</th>
					                                    <td></td>
					                                    <td>
							                            	<input type="hidden" name="feature_imcs_category_id" id="feature_imcs_category_id"/>
							                            	<input type="hidden" name="feature_imcs_album_id" id="feature_imcs_album_id"/>
							                            	<input type="text" name="feature_contents_text" id="feature_contents_text" disabled="disabled"/>
														</td>
					                                </tr>
					                                <tr id="tr_movepath_imcs" align="center">
					                                    <th>IMCS 앨범 선택</th>
					                                    <td></td>
					                                    <td>
								                            <div id="div_category_select">
								                            	
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
								                            <div id="div_category_btn">
								                            	<span class="button small blue" id="preview_selectbtn" >예고편 선택</span>&nbsp;
								                            	<span class="button small blue" id="feature_selectbtn" >본편 선택</span>
								                            </div>
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