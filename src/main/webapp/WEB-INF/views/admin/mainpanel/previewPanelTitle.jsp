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
var titleColors = new Array("#ffe4e1", "#ffa500", "#deb887", "#adff2f", "#228b22", "#c0c0c0", "#008080", "#1e90ff", "#ffc0cb", "#ff1493");
$(document).ready(function(){
	
	
	callDraw();
	
	$("#closebtn").click(function(){
		self.close();
	});
	//setTimeout("imgResize()",3000);
});

function callDraw(){
	
	var panel_id = $("#panel_id").val();
	var separator = "<br/>";
	$.post("<%=webRoot%>/admin/mainpanel/previewPanelTitle.do", 
			{panel_id : panel_id, separator : separator},
			  function(data) {
				 drawPreview(data);
			  },
			  "json"
  	);
}
function drawPreview(data){
	var bg_img_url = $("#bg_img_url").val();
	var toptitle = $("#panel_id").val();
	
	var length = data.length;
	for(var i=0; i < length; i++){
		var code = data[i].code;
		var p_code = data[i].p_code;
		var panelviewname = data[i].panelviewname;
		var album_desc=data[i].album_desc;
		var bg_img_file=data[i].bg_img_file;
		var coloridx=parseInt(data[i].level, 10) % 10;
		var page_type=data[i].page_type;
		var page_code=data[i].page_code;
		
		if(page_type == "LIVE"){
			page_type = "편성표";
			if(page_code != ""){
				page_code = "("+page_code+")";
			}
		}else if(page_type == "CAT"){
			page_type = "메뉴 카테고리";
			page_code = "("+page_code+")";
		}else if(page_type == "APP"){
			page_type = "어플 연동";
			page_code = "("+page_code+")";
		}else if(page_type == "ETC"){
			page_type = "기타";
			if(page_code != ""){
				page_code = "("+page_code+")";
			}
		}
		
		if(p_code == toptitle){		// 최상위지면
			// var html = $("#previewtr").html() + "\n<td id=\"" + code + "\">\n" + panelviewname + "\n</td>";
			//var subhtml = "<b>" + panelviewname + "</b>\n";
			//if(album_desc != ""){
			//	subhtml += "<br/>" + album_desc + "\n";
			//}
			var subhtml = "<span class='freeviewTitle'>[" + page_type + "]</span><br />";
			
			subhtml += "<img src='" + bg_img_url + bg_img_file + "'width='60px' height='60px' style='margin-left: 5px;'>"+"<br/>";
			subhtml += "<span class='freeviewSubTitle'>"+page_code+"</span>"+"<br /><br />"+"<span class='freeviewTitle2'>" + panelviewname + "</span>\n";
			if(album_desc != ""){
				subhtml += "<br/>" + album_desc + "\n";
			}
			
			//if(bg_img_file != ""){
			//	subhtml += "<img src='" + bg_img_url + bg_img_file + "' width='0px' height='0px'  id='"+code+"_img' name='bg_img' style='z-index: -10;position:absolute;top:0;left:0;'/>"
			//}
			
			var html = $("#previewtr").html() + "\n<td id=\"" + code + "\" class='' style='border-left:1px solid #cccccc;;border-top:1px solid #cccccc;border-right:1px solid #cccccc; border-bottom:1px solid #dddddd;'>\n" + subhtml + "</td><td width=\"20\"></td>\n";
			$("#previewtr").html(html);
			/*
			var html = $("#previewtr").html() + "\n<td id=\"" + code + "\" class=''>\n" + subhtml + "\n"+
			"<img src='/smartux_adm/images/admin/copy_bg.gif' width='0px' height='0px'  id='"+code+"_img' style='z-index: -10;position:absolute;top:0;left:0;'/>"			
			+"</td><td width=\"20\"></td>";
			$("#previewtr").html(html);
			*/
		}else{						// 최상위 지면이 아닌 경우
			var divhtml = "<div id=\"" + code + "\" style=\"border-style:solid;border-width:1px;margin:5px;\">\n";
			
			divhtml += "<b>" + panelviewname + "</b>" + "\n";
			if(album_desc != ""){
				divhtml += "<br/>\n" + album_desc + "\n";
			}
			
			if(bg_img_file != ""){
				divhtml += "<img src='" + bg_img_url+ bg_img_file + "' width='0px' height='0px'  id='"+code+"_img' name='bg_img' style='z-index: -10;position:absolute;top:0;left:0;'/>"
			}
			divhtml += "</div>";
			var html = $("#" + p_code).html() + divhtml;
			$("#" + p_code).html(html);
		}
	}
}


function imgResize(){
	//alert($("#previewtr").html());
	
	$("img[name='bg_img']").each(function(){
		/*
		alert($(this).parent().html());
		$(this).css("width", $(this).parent().css("width"));
		$(this).css("height", $(this).parent().css("height"));
		*/
		
		var parenttitle = $(this).parent();
		if($(parenttitle).parent().attr("id") == "previewtr"){ // 최상위 지면일 경우
			var offTop = $(parenttitle).offset().top;
			var offLeft = $(parenttitle).offset().left;
			
			$(this).css("top", offTop + 25);
			$(this).css("left", offLeft);
			
			$(this).css("width",$(parenttitle).css("width"));
			$(this).css("height",$(parenttitle).css("height"));
		}else{
			var offTop3 = $("#CP01_06");
			
			$(this).css("top",$(parenttitle).position().top+5);
			$(this).css("left",$(parenttitle).position().left+5);
			
			$(this).css("width",$(parenttitle).css("width"));
			$(this).css("height",$(parenttitle).css("height"));
		}
	});
	
	
	
	//최상위
	
	/*
	var offTop = document.getElementById("CP01_02").offsetTop;
	var offLeft = document.getElementById("CP01_02").offsetLeft;
	
	$("#CP01_02_img").css("top",offTop+25);
	$("#CP01_02_img").css("left",offLeft);
	
	$("#CP01_02_img").css("width",$("#CP01_02").css("width"));
	$("#CP01_02_img").css("height",$("#CP01_02").css("height"));
	
	var offTop2 = document.getElementById("CP01_04").offsetTop;
	var offLeft2 = document.getElementById("CP01_04").offsetLeft;
	
	$("#CP01_04_img").css("top",offTop2+25);
	$("#CP01_04_img").css("left",offLeft2);
	
	$("#CP01_04_img").css("width",$("#CP01_04").css("width"));
	$("#CP01_04_img").css("height",$("#CP01_04").css("height"));
	
	//최상위 지면이 아닌 경우
	
	var offTop3 = $("#CP01_06");
	
	$("#CP01_06_img").css("top",offTop3.position().top+5);
	$("#CP01_06_img").css("left",offTop3.position().left+5);
	
	$("#CP01_06_img").css("width",$("#CP01_06").css("width"));
	$("#CP01_06_img").css("height",$("#CP01_06").css("height"));
	
	var offTop4 = $("#CP01_10");
	
	$("#CP01_10_img").css("top",offTop4.position().top+5);
	$("#CP01_10_img").css("left",offTop4.position().left+5);
	
	$("#CP01_10_img").css("width",$("#CP01_10").css("width"));
	$("#CP01_10_img").css("height",$("#CP01_10").css("height"));
	*/
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
                    <td class="bold">프리뷰(상용)</td>
                </tr>
            	</tbody>
            </table>
        </td>
    </tr>
	</tbody>
</table>
<table border="0" cellspacing="3" cellpadding="3">
	<tr	id="previewtr" valign="top">
	<tr>
</table>
<input type="hidden" id="panel_id" name="panel_id" value="${panel_id}" />
<input type="hidden" id="bg_img_url" name="bg_img_url" value="${bg_img_url}" />
</body>
</html>