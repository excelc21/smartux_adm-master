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
var td_idx = 2;

$(document).ready(function(){
	select_bind();
	
	$('#selectType').on('change', function() {
		if(this.value == "0"){
			$('.typeB').attr('style','display:none');
			$('.typeA').removeAttr('style');
		}else{
			$('.typeA').attr('style','display:none');
			$('.typeB').removeAttr('style');
		}
	});
	
	var searchBtnEvnt = function(){
        var series = "${series}";
        var specifyYn = "N";
        var serviceType = "";
        var isTypeChange = "${isTypeChange}";
        var type = "";
        if(isTypeChange == "Y"){
        	type = $("select[name='category_type_select']").val();
        }else{
        	type = "${type}";
        }
        
		$.post("<%=webRoot%>/admin/commonMng/getCategoryAlbumList.do",
                {searchType: $('#searchType').val(), searchVal: $('#searchVal').val(), specifyYn : specifyYn, serviceType : serviceType,type : type},
                function (data) {
                    // 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
                    var html = [], album_html = [], h = -1, c = -1, a = -1;
                    if (data.length == 0) {
                    	var albumElementid = $("#albumElementid").val();
			 			var openerelement = window.opener.jQuery("#" + albumElementid);
			 			openerelement.html([].join(''));
                    } else {
                         for (var i = 0; i < data.length; i++) {
							var optionval = "";
							if(series=="Y"){
                        		var seriesNo = "";
                        		var categoryNo = "";
                        		if("Y"==data[i].series_yn){
                            		seriesNo = data[i].series_no;
                            		categoryNo = data[i].category_no;
                        		}
                        		//optionval = data[i].category_id + "^" + data[i].album_id + "^" + seriesNo + "^" + categoryNo;
                        		optionval = data[i].category_id + "^" + data[i].album_id + "^" + "X";
							}else{
								optionval = data[i].category_id + "^" + data[i].album_id + "^" + "X";
							}
							
							//album_html[++a] = "<option title=\"" + data[i].album_name + "(" + data[i].album_id + ")" + "\" value=\"" + optionval + "\">[" + data[i].category_name + "] " + data[i].album_name + "(" + data[i].album_id + ")" + "</option>\n";
							//album_html[++a] = "<option title=\"" + data[i].album_name + "(" + data[i].album_id + ")" + "\" value=\"" + optionval + "\">" + data[i].album_name + "</option>\n";
							album_html[++a] = "<option title=\"" + data[i].album_name  + "\" value=\"" + optionval + "\">[" + data[i].category_name + "] " + data[i].album_name + "(" + data[i].album_id + ")" + "</option>\n";
                         }
                         
     			 		if(album_html.length != 0){
    			 			var albumElementid = $("#albumElementid").val();
    			 			var openerelement = window.opener.jQuery("#" + albumElementid);
    			 			openerelement.html(album_html.join(''));
    			 		}                         
                    }
                    $.unblockUI();
                },
                "json"
        );
	};
	
	$('#searchbtn').on('click', function(){
		if($('#searchVal').val() == ''){
			alert('검색어를 입력해주세요.');
			return;
		}else{
				$.blockUI({
					blockMsgClass: "ajax-loading",
					showOverlay: true,
					overlayCSS: { backgroundColor: '#CECDAD' } ,
					css: { border: 'none' } ,
					 message: "<b>로딩중..</b>"
				});
			searchBtnEvnt();
		}
	});
	
	$('#searchVal').on('keypress', function(e){
		if(e.keyCode == '13'){
			if($('#searchVal').val() == ''){
    			alert('검색어를 입력해주세요.');
    			return;
    		}else{
					$.blockUI({
						blockMsgClass: "ajax-loading",
						showOverlay: true,
						overlayCSS: { backgroundColor: '#CECDAD' } ,
						css: { border: 'none' } ,
						 message: "<b>로딩중..</b>"
					});
    			searchBtnEvnt();
    		}
		}
	});	
	
	$("#closebtn").click(function(){
		self.close();
	});
	
})

function select_bind(){
	$("select[name='category_select']").unbind("change");
	
	$("select[name='category_select']").bind("change", function(){
		var category_id = $(this).val();
		var thissel = this;
		var type = "${type}";
		var series_yn = "${series_yn}";
		
		// 현재 select 태그의 부모인 td의 이후에 나오는 모든 td를 구한다
 		var nexttd = $(thissel).parent().nextAll("td"); 
 		var length = nexttd.length;
 		for(var i = length-1; i >= 0; i--){
 			$(nexttd[i]).remove();
 		}
 		
		if(category_id == "---"){		// 선택해주세요를 선택한 경우
			
		}else{
			
			$.post("<%=webRoot%>/admin/backgroundimage/getCategoryAlbumList.do", 
					 {category_id : category_id, type : type, series_yn : series_yn},
					  function(data) {
							// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
						 	if(data.length == 0){
						 		
						 	}else{
						 		
						 		var html = [], category_html = [], album_html = [], h=-1, c=-1, a=-1;
						 		
						 		for(var i=0 ; i < data.length; i++){
						 			if(data[i].album_id == ""){
						 				category_html[++c] = "<option value=\"" + data[i].category_id + "\">" + data[i].category_name + "</option>\n";	
						 			}else{
						 				//var optionval = category_id + "^" + data[i].album_id;
						 				var optionval = category_id + "^" + data[i].album_id + "^" + "X";
						 				//album_html[++a] = "<option value=\"" + optionval + "\">" + data[i].album_name + "</option>\n";
						 				album_html[++a] = "<option title=\"" + data[i].album_name + "\" value=\"" + optionval + "\">" + data[i].album_name + "</option>\n";
						 			}
						 		}
						 		
						 		if(category_html.length != 0){
							 		html[++h] = "<td name=\"category_td\" valign=\"top\">\n";
							 		html[++h] = "<select name=\"category_select\" class=\"select\">\n";
							 		html[++h] = "<option value=\"---\" selected>선택해주세요</option>\n";
							 		html[++h] = category_html.join('');
							 		html[++h] = "</select>\n";
							 		html[++h] = "</td>\n";
						 		}
						 		
						 		if(album_html.length != 0){
						 			var albumElementid = $("#albumElementid").val();
						 			var openerelement = window.opener.jQuery("#" + albumElementid);
						 			openerelement.html(album_html.join(''));
						 		}
						 		
						 		$("#category_tr").append(html.join(''));
						 	}
						 	select_bind();
						 
					  },
					  "json"
		    );
		}
		
		
	});
	
}



</script>
<body leftmargin="0" topmargin="0">
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
<table class="sidebarMain">
	<tr class="sidebarMain">
		<td></td>
		<td>
			<select id="selectType" name="selectType" class="select">
				<option value="0">선택형</option>
				<option value="1" selected="selected">검색형</option>
			</select>
		</td>
	</tr>
</table>
<table class="sidebarMain typeA" style="display:none">
	<tr id="category_tr">
		<td name="category_td" valign="top">
			<c:choose>
				<c:when test="${categoryResult==null || fn:length(categoryResult) == 0}">
					<div>검색된 카테고리가 없습니다</div>
				</c:when>
				<c:otherwise>
					<select name="category_select" class="select">
						<option value="---">선택해주세요</option>
					<c:forEach var="item" items="${categoryResult}" varStatus="status">
						<option value="${item.category_id}">${item.category_name }</option>
					</c:forEach>
					</select>
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>


<table class="sidebarMain typeB">
	<tr>
		<td valign="top" width="430">
			<select id="searchType" name="searchType" class="select">
				<option value="A">앨범명</option>
				<option value="I">앨범ID</option>
				<option value="C">카테고리명</option>
			</select>
			<input type="text" id="searchVal" name="searchVal" style="width:250px">
			<span class="button small blue" id="searchbtn">검색</span>
		</td>
	</tr>
</table>


<input type="hidden" id="albumElementid" name="albumElementid" value="${albumElementid}" />
<table border="0" cellpadding="0" cellspacing="0" width="100%" align="left">
<tbody>
    <tr>
        <td height="40" align="left">
			<span class="button small blue" id="closebtn">닫기</span>
    	</td>
    </tr>
	</tbody>
</table>
</body>
</html>