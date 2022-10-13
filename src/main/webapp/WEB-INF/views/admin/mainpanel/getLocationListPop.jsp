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

$(document).ready(function(){
	var callback = '${callback}';
	
	$("#setLocationBtn").click(function(){

		if($("#step3_select").val()=="---"){
			alert("마지막을 선택해 주세요(국내:동/해외:나라)");
			return false;
		}
		
		var dataobj = new Object(); 
		dataobj.location_yn =$("#location_select").val();
		dataobj.location_code =$("#step3_select").val();
		var callbak_m = eval("opener."+"${param.callbak}");    
		callbak_m(dataobj);
		self.close();
	});
	

	$("#closebtn").click(function(){
		self.close();
	});
	
})


function location_change(step){
	var location_yn = $("select[name='location_select'] option:selected").val();		
	var code = ""; 

	if(step == "step2"){
		code = $("#step1_select").val(); 
	}else if(step == "step3"){
		code = $("#step2_select").val();
	}
	
	$.post("<%=webRoot%>/admin/mainpanel/getLocationListPop.do",
            {location_yn: location_yn, step: step, code: code},
            function (data) {
            	
                // 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
                if (data.length == 0) {
                	//$("select[name='country_select']").html('');
                } else {
                	
					if(data[0].step == "step1"){
						$("select[name='step1_select']").html('');
						$("select[name='step2_select']").html('');
						$("select[name='step3_select']").html('');
						
						$("select[name='step2_select']").html('<option value="---">선택해주세요</option>');
						$("select[name='step3_select']").html('<option value="---">선택해주세요</option>');
						
                		var category_select_html = '<option value="---">선택해주세요</option>';
	                    for (var i = 0; i < data.length; i++) {
	                    	category_select_html=category_select_html+"<option value="+data[i].step1_code+">"+data[i].step1_name+"</option>";
	                    }
	                  	$("select[name='step1_select']").html(category_select_html);
					}else if(data[0].step == "step2"){    
                		$("select[name='step2_select']").html('');
                		$("select[name='step3_select']").html('');
                		$("select[name='step3_select']").html('<option value="---">선택해주세요</option>');
                		var category_select_html = '<option value="---">선택해주세요</option>';
	                    for (var i = 0; i < data.length; i++) {
	                    	category_select_html=category_select_html+"<option value="+data[i].step2_code+">"+data[i].step2_name+"</option>";
	                    }
	                  	$("select[name='step2_select']").html(category_select_html);
                	}else if(data[0].step == "step3"){
                		$("select[name='step3_select']").html('');
                		var category_select_html = '<option value="---">선택해주세요</option>';
                        for (var i = 0; i < data.length; i++) {
                        	category_select_html=category_select_html+"<option value="+data[i].step3_code+">"+data[i].step3_name+"</option>";
                        }
                      	$("select[name='step3_select']").html(category_select_html);
                	}
                }
            },
            "json"
    );
	
	
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
                    <td class="bold">지역 선택</td>
                </tr>
            	</tbody>
            </table>
        </td>
    </tr>
	</tbody>
</table>
<table class="sidebarMain">
	<tr id="category_tr">
		<td name="" valign="top">
    		<select id="location_select" name="location_select" onchange="location_change('step1');">
				<option value="Y" selected="selected">국내</option>
				<option value="N" >해외</option>
			</select>
    	</td>
		<td name="category_td" valign="top">
			<c:choose>
				<c:when test="${locationCode==null || fn:length(locationCode) == 0}">
					<div>검색된 지역이 없습니다</div>
				</c:when>
				<c:otherwise>
					<select id="step1_select" name="step1_select" onchange="location_change('step2');">
						<option value="---">선택해주세요</option>
					<c:forEach var="item" items="${locationCode}" varStatus="status">
						<option value="${item.step1_code}">${item.step1_name}</option>
					</c:forEach>
					</select>
				</c:otherwise>
			</c:choose>
			
			<select id="step2_select" name="step2_select" onchange="location_change('step3');">
				<option value="---">선택해주세요</option>		
			</select>
			
			<select id="step3_select" name="step3_select">
				<option value="---">선택해주세요</option>		
			</select>
		</td>
	</tr>
</table>
<input type="hidden" id="albumElementid" name="albumElementid" value="" />
<table border="0" cellpadding="0" cellspacing="0" width="100%" align="left">
<tbody>
    <tr>
        <td height="40" align="left">
        	<span class="button small blue" id="setLocationBtn">설정</span>
			<span class="button small blue" id="closebtn">닫기</span>
    	</td>
    </tr>
	</tbody>
</table>
</body>
</html>