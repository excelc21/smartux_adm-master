<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
	$('#closebtn').click(function(){
		self.close();
	});
	
	$('#choicebtn').click(function(){
		$(opener.document).find('#parent_id').val($('#contentList').val());
		self.close();
	});
	
	$('#submitbtn').click(function(){
		
		var categotyChk = $("#contentList option").index($("#contentList option:selected"));
		var parent_id   = $("#contentList option:selected").val();
		var content_id  = $("#content_id").val();
		
		if (categotyChk == -1) {
			alert("복사할 컨텐츠를 선택해주세요.");	
		}else{			
			if (confirm("선택된 카테고리에 해당 컨텐츠를 복사하시겠습니까?")) {
			    $.ajax({
					url: './contentCopyChk.do',
					type:'POST',
					dataType: 'json',
					timeout : 30000,
				    data: {
				        "parent_id" : parent_id,
				        "content_id": content_id
				    },
					success:function(data){
				    	var resultstate = data.resultstate;
				    	if(data.flag == "0000"){
					    	if(resultstate != 0){
					    		alert("선택된 카테고리에 동일한 컨텐츠가 등록되어 있습니다");
					    		return;
					    	}
					    	
							$.blockUI({
								blockMsgClass: "ajax-loading",
								showOverlay: true,
								overlayCSS: { backgroundColor: '#CECDAD' } ,
								css: { border: 'none' } ,
								 message: "<b>처리 중..</b>"
							});
							
					    	$.ajax({
					    		url: './contentCopy.do',
								type:'POST',
								dataType: 'json',
								timeout : 30000,
							    data: {
							        "parent_id" : parent_id,
							        "content_id": content_id
							    },
								success:function(data){
									if(data.result.flag=="0000"){
										alert("정상적으로 처리 되었습니다.");
									}else{
										alert(data.result.message);
									}
								},							    
						    	error:function(){
					 		    },
								complete:function(){
									self.close();
								}
					    	});
				    	}
					}
				});
			}
		}		
	});
	
});
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
<form id="form1" name="form1" action="./contentCopy.do" method="post">
<input type="hidden" id="content_id" name="content_id" value="${content_id}" />
	<table width="100%">
	    <tr>
	        <td width="100%">
				<select id="contentList" name="contentList" size="23" style="width:100%;">
					<option value="">::: 최상위 :::</option>
					<c:forEach var="item" items="${categoryResult}" varStatus="status">
						<option <c:if test="${item.content_id eq parent_id}">selected="selected" </c:if> value="${item.content_id}">${item.content_name}</option>
	                </c:forEach>
				</select>
	        </td>
	    </tr>
	</table>
</form>
<table border="0" cellpadding="0" cellspacing="0" width="100%" align="left">
    <tbody>
    <tr>
        <td height="40" align="left">
        	<c:if test="${choice_type eq 'S'}">
        		<span class="button small blue" id="submitbtn">선택</span>
        	</c:if>
        	<c:if test="${choice_type ne 'S'}">
        		<span class="button small blue" id="choicebtn">선택</span>
        	</c:if>        	            
            <span class="button small blue" id="closebtn">닫기</span>
        </td>
    </tr>
    </tbody>
</table>
</body>
</html>