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
	type_select_bind();
	
	if($('#category_level').val() != 1){
		select_bind();
	}
	
	$("#closebtn").click(function(){
		self.close();
	});
	
	$("#setPageCategoryBtn").click(function(){
		var setVal = "";
		var setText = "";
		var categorysels = $("select[name='category_select']");
		var category_length = categorysels.length;
		
		for(var i = 0; i < category_length; i++){
			var seltext = $(categorysels[i]).children("option:selected").text();
			var optionval = $(categorysels[i]).val();
			
			if(i == 0){
				setVal = optionval;	
				setText = seltext;
			}else{
				setVal += ":"+optionval;
				setText += ":"+ seltext;
			}
			

		}
		
		if(setVal == "---"){
			alert("선택을 해주십시오.");
			return;
		}else{
			if(setVal.indexOf(":---") != -1){
				setVal = setVal.replace(":---","");
				setText = setText.replace(":선택해주세요","");
			}
			var idx = setVal.lastIndexOf(":");
			//IPTV 브릿지홈 개편 : 카테고리 구분 select box사용 여부 추가
			if(('${isAds}' == 'Y' || '${isCategoryGbUse}'=='Y') && idx != -1){
				setVal = setVal.substring(idx+1);
				setText = setText.substring(setText.lastIndexOf(":")+1);
			}else if('${isImcsContents}' == 'Y' && idx != -1){
				setVal = setVal.substring(idx+1);		
				setText = setText.substring(setText.lastIndexOf(":")+1);
			}
		}
		
		if('${isAds}' == 'Y'){
			opener.document.getElementById("linkUrl").value = setVal + "||" + $("select[name='category_type_select']").val();
			opener.document.getElementById("textUrl").value = setText;
		} else if('${category_level}' == 1){
			opener.document.getElementById("category_des").value = setText;
			opener.document.getElementById("category_code").value = setVal;
		} else if('${isImcsContents}' == 'Y'){
			opener.document.getElementById("imcscontents_des").value = setText;
			opener.document.getElementById("imcscontents_code").value = setVal;
		} else if('${isCategoryGbUse}' == 'Y'){
			//IPTV 브릿지홈 개편 : 카테고리 구분 select box사용 여부 추가
			$(opener.document).find("#category_id").val(setVal);
			$(opener.document).find("#category_nm").html(setText);
			
		}else{
			opener.document.getElementById("page_code").value = setVal;
		}
		
		self.close();
	});
	
})

function select_bind(){
	$("select[name='category_select']").unbind("change");
	
	$("select[name='category_select']").bind("change", function(){
        var panel_id = '${param.panel_id}';
        var category_id = $(this).val();
		var thissel = this;
		var category_gb = '${category_gb}';
		
		//2019.11.12 IPTV 브릿지홈 개편 : isCategoryGbUse -> 카테고리 구분 사용여부 추가
		if('${isAds}' == 'Y' || '${isCategoryGbUse}' == 'Y'){
			category_gb = $("select[name='category_type_select']").val();
		}
		
		// 현재 select 태그의 부모인 td의 이후에 나오는 모든 td를 구한다
 		var nexttd = $(thissel).parent().nextAll("td"); 
 		var length = nexttd.length;
 		for(var i = length-1; i >= 0; i--){
 			$(nexttd[i]).remove();
 		}
 		
		if(category_id == "---"){		// 선택해주세요를 선택한 경우
			
		}else{
			
			$.post("<%=webRoot%>/admin/mainpanel/getPageCategoryList.do",
                    {panel_id : panel_id, category_id : category_id, category_gb : category_gb},
					  function(data) {
					 		
							// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
						 	if(data.length == 0){
						 		
						 	}else{
						 		
						 		
						 		var html = [], category_html = [], album_html = [], h=-1, c=-1, a=-1;
						 		
						 		for(var i=0 ; i < data.length; i++){
					 				category_html[++c] = "<option value=\"" + data[i].category_id + "\">" + data[i].category_name + "</option>\n";	
						 		}
						 		
						 		if(category_html.length != 0){
							 		html[++h] = "<td name=\"category_td\" valign=\"top\">\n";
							 		html[++h] = "<select id=\"category_select\" name=\"category_select\">\n";
							 		html[++h] = "<option value=\"---\" selected>선택해주세요</option>\n";
							 		html[++h] = category_html.join('');
							 		html[++h] = "</select>\n";
							 		html[++h] = "</td>\n";
						 		}
						 		
						 		/*if(album_html.length != 0){
						 			var albumElementid = $("#albumElementid").val();
						 			
						 			var openerelement = window.opener.jQuery("#" + albumElementid);
						 			openerelement.html(album_html.join(''));
						 		}*/
						 		
						 		$("#category_tr").append(html.join(''));
						 	}
						 	select_bind();
						 
					  },
					  "json"
		    );
		}
		
		
	});
	
}

function type_select_bind() {
    $("select[name='category_type_select']").unbind("change");
    $("select[name='category_type_select']").bind("change", function () {
    	var category_id = "${param.category_id}";
        var category_gb = $(this).val();
        
        var thissel = $("select[name='category_select']");
        
     	// 현재 select 태그의 부모인 td의 이후에 나오는 모든 td를 구한다
        var nexttd = $(thissel).parent().nextAll("td");
        var length = nexttd.length;
        for (var i = length - 1; i >= 0; i--) {
            $(nexttd[i]).remove();
        }
        
        $.post("<%=webRoot%>/admin/mainpanel/getPageCategoryList.do",
                {category_id: category_id, 
        		 category_gb: category_gb, 
        		 isAds: "${isAds}", 
        		 category_level: "${category_level}",
        		 isCategoryGbUse: "${isCategoryGbUse}"
                },
                function (data) {
                	
                	$("select[name='category_type_select']").val(category_gb);
                	
                    // 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
                    if (data.length == 0) {
                    	$("select[name='category_select']").html('');
                    } else {
                        var category_select_html = '<option value="---">선택해주세요</option>';
                        for (var i = 0; i < data.length; i++) {
                        	category_select_html=category_select_html+"<option value="+data[i].category_id+">"+data[i].category_name+"</option>";
                        }
                      	$("select[name='category_select']").html(category_select_html);
                    }
                    type_select_bind();
                    
                    if($('#category_level').val() != 1){
                		select_bind();
                	}
                },
                "json"
        );
        		
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
	<tr id="category_tr">
		<td name="category_type" valign="top">
    		<select name="category_type_select"  <c:if test="${isAds eq 'N' && isCategoryGbUse eq 'N'}">style="display:none;"</c:if>>
				<option value="I20"  selected="selected">I20</option>
				<option value="I30" >I30</option>
			</select>
    	</td>
		<td name="category_td" valign="top">
			<c:choose>
				<c:when test="${categoryResult==null || fn:length(categoryResult) == 0}">
					<div>검색된 카테고리가 없습니다</div>
				</c:when>
				<c:otherwise>
					<select id="category_select" name="category_select">
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
<input type="hidden" id="albumElementid" name="albumElementid" value="" />
<input type="hidden" id="category_level" name="category_level" value="${category_level}" />
<table border="0" cellpadding="0" cellspacing="0" width="100%" align="left">
<tbody>
    <tr>
        <td height="40" align="left">
        	<span class="button small blue" id="setPageCategoryBtn">설정</span>
			<span class="button small blue" id="closebtn">닫기</span>
    	</td>
    </tr>
	</tbody>
</table>
</body>
</html>