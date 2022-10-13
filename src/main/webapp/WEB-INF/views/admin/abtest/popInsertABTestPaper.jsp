<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>

<script type="text/javascript">
	$(document).ready(function () {
       	$('#selectType').on('change', function() {
       		if(this.value == "0"){
       			$('.typeB').attr('style','display:none');
       			$('.typeA').removeAttr('style');
       		}else{
       			$('.typeA').attr('style','display:none');
       			$('.typeB').removeAttr('style');
       		}
       	});
       	
       	//패널 선택
       	$('#panel_select').change(function (e) {
       		
       		var sel_panel_id = $(this).val();
       		
       		$.ajax({
       	        url: '/smartux_adm/admin/abtest/getPaperList.do?sel_panel_id='+sel_panel_id,
       	        type: 'POST',
       	        dataType: 'json',
       	        contentType: 'charset=utf-8',
       	        success: function (data) {
       	        	console.log(data);
       	        	if(data.length > 0){
       	        		$('#paper_select').html('');
       	        		$('#paper_select').append("<option value=''>== 지면선택 ==</option>");
           	        	for(var i = 0 ; i < data.length; i++){
           	        		$('#paper_select').append("<option value=\"" + data[i].title_id + "\" dataval=\""+data[i].pannel_id + "\">" + data[i].title_nm + "</option>\n");
           	        	}
       	        	}
       	        },
       	        error: function () {
       	            alert('지면 정보를 가져올 수 없습니다.');
       	        }
       	    });
       		
       	});
       	
       	//검색
       	$('#searchBtn').click(function (e) {
       		if($('#searchVal').val() == ''){
       			alert('검색어를 입력해주세요.');
       			
       		}else{
        		var findName = $('#findName').val();
        		var findValue = $('#findValue').val();
        		
        		$.ajax({
        	        url: '/smartux_adm/admin/abtest/getPaperList.do?findName='+findName + '&findValue='+findValue,
        	        type: 'POST',
        	        dataType: 'json',
        	        success: function (data) {
        	        	console.log(data);
        	        	if(data.length > 0){
        	        		
        	        		$('[name=contentList]').empty();
        	        		
            	        	for(var i = 0 ; i < data.length; i++){
            	        		$('[name=contentList]').append("<option value=\"" + data[i].title_id + "\" dataval=\""+data[i].pannel_id + "\">["+ data[i].title_id+"\]" + data[i].title_nm + "</option>\n");
            	        	}
        	        	}
        	        },
        	        error: function () {
        	            alert('지면 정보를 가져올 수 없습니다.');
        	        }
        		}); 
       		}
       	});
			 
       	
       	/* $('#searchVal').on('keypress', function(e){
       		if(e.keyCode == '13'){
       			if($('#searchVal').val() == ''){
           			alert('검색어를 입력해주세요.');
           			return;
           		}else{
      					
           			searchBtnEvnt();
           		}
       		}
       	}); */
       	
		$("#choicebtn").click(function () {
			if(confirm('선택한 지면을 등록 하시겠습니까?')){
				var org_title_id ='';
				var org_panel_id = '';
				var mims_id='';
				
				if($('#selectType').val() == "0"){
					org_title_id = $("select[name=paper_select]").val();
					org_panel_id =  $("[name=paper_select] option:selected").attr('dataval');
	             	mims_id='${mims_id}'
				}else{
	             	org_title_id = $("select[name=contentList]").val();
	             	org_panel_id =  $("[name=contentList] option:selected").attr('dataval');
	             	mims_id='${mims_id}'
				}	
	            
				if(!org_title_id || "---" == org_title_id){
					alert("지면을 선택해 주세요.");
				}else{
					$.ajax({
						url: "/smartux_adm/admin/abtest/insertABTestPaper.do",
		      			type: "POST",
		      			data: { 
		      				'org_title_id' : org_title_id,
		      				'org_panel_id' : org_panel_id,
		      				'mims_id' : mims_id
		      			},
		      			dataType : "json",
		      			success: function (rtn) {
		      				if (rtn.flag=="0000") {
		      					alert("등록 성공");
		      					opener.location.reload();
		      					self.close();
		      				} else {
		      					alert("작업 중 오류가 발생하였습니다\nflag : " + rtn.flag + "\ Message : " + rtn.message);
		      				}
		      			},
		      			error: function () {					
		      				alert("일시적인 장애가 발생하였습니다. 다시 시도해 주세요.");
		      			}
		      		});
				}
			}
		});

           //닫기 버튼
		$("#closebtn").click(function () {
			self.close();
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
               		<td class="bold">검색타입 선택</td>
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
			<select id="selectType" name="selectType">
				<option value="0">선택형</option>
				<option value="1" selected="selected">검색형</option>
			</select>
			<input type="hidden" id="test_id" name="test_id" value="${test_id}">
			<input type="hidden" id="variation_id" name="variation_id" value="${variation_id}">
			<input type="hidden" id="mims_id" name="mims_id" value="${mims_id}">
		</td>
	</tr>
</table>
<table>
	<tr class="typeA" style="display:none">
	    <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
	    <td class="bold">카테고리 선택</td>
	</tr>
	<tr class="typeB">
	    <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
	    <td class="bold">카테고리 검색</td>
	</tr>
</table>
<table class="sidebarMain typeA" style="display:none">
    <tr id="category_tr">
        <td name="category_td" valign="top">
            <c:choose>
                <c:when test="${panelList==null || panelList==''}">
                    <div>검색된 패널이 없습니다</div>
                </c:when>
                <c:otherwise>
                    <select name="panel_select" id="panel_select">
                        <option value="---">선택해주세요</option>
                        <c:forEach var="item" items="${panelList}" varStatus="status">
                        	<option value="${item.PANNEL_ID}">${item.PANNEL_NM}</option>
                        </c:forEach>
                    </select>
                    <select name="paper_select" id="paper_select">
                        <option value="---">선택해주세요</option>
                    </select>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
</table>
<table class="sidebarMain typeB">
	<tr>
		<td valign="top" width="483">
			<select id="findName" name="findName">
				<option value="title_nm">지면명</option>
				<option value="title_id">지면ID</option>
			</select>
			<input type="text" id="findValue" name="findValue" style="width:280px">
			<span class="button small blue" id="searchBtn">검색</span>
		</td>
	</tr>
</table>
<table width="100%" class="sidebarMain typeB">
    <tr>
        <td valign="top">
			<select id="contentList" name="contentList" size="10" style="width:100%;height:200px;"></select>
        </td>
    </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%" align="left">
    <tbody>
    <tr>
        <td height="40" align="left">
            <span class="button small blue" id="choicebtn">선택</span>
            <span class="button small blue" id="closebtn">닫기</span>
        </td>
    </tr>
    </tbody>
</table>
</body>
</html>