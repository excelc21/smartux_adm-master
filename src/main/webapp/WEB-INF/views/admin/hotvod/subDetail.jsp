<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib prefix="hotvod" uri="/WEB-INF/tlds/hotvod.tld" %>
<title>LG U+ IPTV</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet"	type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<link href="/smartux_adm/css/anytime_style.css" rel="stylesheet" type="text/css">
<script src="/smartux_adm/js/anytime.js"></script>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<script type="text/javascript">
	
$(document).ready(function(){
	var result = '${result}';
	if(result == "0000"){
		alert("수정 되었습니다.");
	}else if(result == "0001"){
		alert("등록에 실패했습니다. 앨범 이미지를 찾을 수 없습니다.");
	}else if(result == "0002"){
		alert("최대 이미지 사이즈를 초과 했습니다.\n컨텐츠이미지(IPTV):${iptvSize}");
	}
	
	var type = '${content.content_type}';
	
	$("#service_check_all").click(function(){
		$(".service_check").prop("checked", false);
	});
	$(".service_check").click(function() {
		$("#service_check_all").prop('checked', false);
	});
	
	drawCheckBoxData('form1','badge_check','badge_data');
	
	drawServiceTypeData('form1','service_check','multi_service_type');
	
	typeChange(type);
	
	$('#closebtn').click(function(){
		self.close();
	});
	
	$('#viewClear').click(function() {
        $('#start_dt').val('');
        $('#end_dt').val('');
  	});
	$("#sumitBtn").click(function () {		
	 	if($("#content_id").val()==""){
	 		alert("컨텐츠 아이디를 입력해 주세요."); 		
	 		$("#content_id").focus();
	 		return;
	 	}
	 	if($("#content_name").val()==""){
	 		alert("컨텐츠명을 입력해 주세요."); 		
	 		$("#content_name").focus();
	 		return;
	 	}
	 	
	 	if($('#content_type').val() == 'V' && $("#content_url").val() == ''){
			alert('컨텐츠URL을 입력해 주세요.');
			$("#content_url").focus();
			return;	
		}
	 	
	 	if($('#content_type').val() == 'V'){
	 		var start_dt = $('#start_dt').val();
	 		var end_dt = $('#end_dt').val();
	 		if(start_dt !== '' || end_dt !== '') {
	 			if(end_dt <= start_dt){
	 				alert('노출기간의 종료일은 시작일과 같거나 작을 수 없습니다.');
			 		$('#end_dt').focus();
			 		return;
			 	}
	 		}
	 	}
	 	
	 	if($('#content_type').val() == 'L' && $("#content_url").val() == ''){
			alert('링크URL을 입력해 주세요.');
			$("#content_url").focus();
			return;
		}

	 	if($('#content_type').val() == 'L'){
	 		var start_dt = $('#start_dt').val();
	 		var end_dt = $('#end_dt').val();
	 		if(start_dt !== '' || end_dt !== '') {
	 			if(end_dt <= start_dt){
	 				alert('노출기간의 종료일은 시작일과 같거나 작을 수 없습니다.');
			 		$('#end_dt').focus();
			 		return;
			 	}
	 		}
	 	}

	 	if($('#content_type').val() == 'M'){
	 		if($('#category_id').val() == ''){
		 		alert('시리즈를 선택해주세요.');
		 		return;
		 	}
	 	}
	 	
	 	//2019.11.07 브릿지홈 개편 : 카테고리 ID 필수 체크
	 	if($('#content_type').val() == 'N'){
	 		if($.trim($('#category_id').val()) == ''){
		 		alert('카테고리를 선택해주세요.');
		 		
		 		return;
		 	}
	 	}
	 	
	    if($("#content_order").val() == ''){
			alert('컨텐츠 순서를 입력해 주세요.');
			$("#content_order").focus();
			return;
	  	}
	    
	 	if($('#content_type').val() == 'C'){
	    	setServiceTypeData('form1','service_check','multi_service_type');
	    }
	    setCheckBoxData('form1','badge_check','badge_data');
	   
		
	    if($("#del_yn").val() == 'Y'){
	    	if(confirm("선택한 컨텐츠를 비노출 상태로 저장하시겠습니까?\n*하위 컨텐츠가 존재할 경우 하위 컨텐츠까지 전부 비노출 상태가 됩니다.")){
	    		$("form[name=form1]").submit();	
	    	}
	    }else
	    {
	    	$.blockUI();
	 		$.ajax({
	 		    url: './getBadgeDataInsertYn.do',
	 		    type: 'POST',
	 		    dataType: 'json',
	 		    data: {
	 		        "content_id" : $('#content_id').val(),
	 		        "type" : $('#content_type').val()
	 		    },
	 		    success: function(data)
	 		    {
	 		    	//console.log(data);
	 		    	if(data.result.flag == '0000')
	 		    	{
	 		    		if(data.result.isValid == 'true')
	 		    		{
							$.ajax({
			 				    url: './getParentContentDelYn.do',
			 				    type: 'POST',
			 				    dataType: 'json',
			 				    data: {
			 				        "parent_id" : $('#parent_id').val(),
			 				        "content_id" : $('#content_id').val(),
			 				        "content_type" : $('#content_type').val()
			 				    },
			 				    success: function(data){
			 				    	if(data.result.delYn == 'Y'){
			 				    		alert("상위 카테고리인 [" + data.result.contentName + "]카테고리가 비노출 상태입니다.\n상위 카테고리를 먼저 노출상태로 변경해 주세요.");
			 				    	}else{
			 				    		if(confirm("수정하시겠습니까?")){
			 						    	$("form[name=form1]").submit();	
			 						    }
			 				    	}
			 				    	$.unblockUI();
			 				    },
			 				    error: function(){
			 				    	$.unblockUI();
			 				    	alert("에러가 발생하였습니다.");
			 				    }
			 				});	
							
							
	 		    		}
	 		    		else
	 		    		{
	 		    			$.unblockUI();
	 		    			alert("파라메터 값이 정확하지 않습니다.");
	 		    		}
	 		    	}
	 		    	else
	 		    	{
	 		    		$.unblockUI();
	 		    		alert("에러가 발생하였습니다.");
	 		    	}
	 		    },
	 		    error: function(){
	 		    	$.unblockUI();
	 		    	alert("에러가 발생하였습니다.");
	 		    }
	 		});
	    }
	});
	
	$("#searchBtn").click(function(){
		var url = "<%=webRoot%>/admin/hotvod/openCategoryPop.do?content_type=C&parent_id=${vo.parent_id}";
    	category_window = window.open(url, "openCategoryPop", "width=600,height=550,scrollbars=yes");
	});
	
	$("#searchAlbumBtn").click(function(){
		
    	var closeCheck = setInterval(function(){
			if($("#choiceCts").val() != ''){
				$.ajax({
				    url: './getAlbumCateInfo.do',
				    type: 'POST',
				    dataType: 'json',
				    data: {
				        "choiceCts" : $('#choiceCts').val()
				    },
				    success: function(data){
				    	if(data.result.flag == '0000'){
					    	$('#album_id').val(data.result.album_id);
					    	$('#category_id').val(data.result.category_id);
					    	$('#content_name').val(data.result.contents_name);
					    	$('#contents_name').text(data.result.contents_name);
					    	$('#series_no').text(data.result.series_no);
					    	$('#sponsor_name').text(data.result.sponsor_name);
					    	$('#still_img').val(data.result.still_img);
					    	$('#category_nm').val(data.result.category_name);
					    	$("#choiceCts").val('');
				    	}else if(data.result.flag == '0001'){
				    		$("#choiceCts").val('');
				    		alert('앨범 정보를 가져오지 못했습니다. \n다른 앨범을 선택해 주세요.')
				    	}else{
				    		$("#choiceCts").val('');
				    		alert("앨범 정보를 가져오지 못했습니다.\n해당 앨범이 현재 편성 불가능하거나 네트워크 문제일 수 있습니다.");
				    		//console.log(data.result.flag);
					    	//console.log(data.result.mesage);
				    	}
				    	
				    	$.unblockUI();
				    },
				    error: function(e){
				    	$.unblockUI();
				    	$("#choiceCts").val('');
				    	alert("에러가 발생하였습니다.");
				    }
				});	
				clearInterval(closeCheck);
			}
		}, 1000);
    	
		var url = "<%=webRoot%>/admin/commonMng/getOnceCategoryAlbum.do?categoryId=VC&hiddenName=choiceCts&series=Y&isTypeChange=Y&type=I20";
		//콘서트,방송인 경우에 편성앨범중 비노출인경우도 표시
		if($('#content_type').val() == 'B' || $("#content_type").val() == 'A'){
			url += "&conBrodYn=Y";			
		}
		
    	category_window = window.open(url, "categoryAlbumPop", "width=850,height=430,scrollbars=yes,resizable=yes");
	});
	
	$("#file_tv").change(function(){
		if($("#file_tv").val()==""){
			return;
		}
		if(imageCheckInputPatch($("#file_tv").val(),"file_tv")){
			alert("이미지 파일이 아닙니다.");			
		}
	});
	
});

function addFrontStr(value,strLen)
{
	while(value.length < strLen)
	{
		value = "0"+value;
	}
	return value; 
}

function drawCheckBoxData(formId,checkBoxName,targetId)
{
	var isCheck = '';
	var badgeData = $('#'+targetId).val();
	
	if(badgeData == null)
		return;
	if(badgeData == '')
		return;
		
	var hotvodBadgeList = '${hotvodBadgeList}';	
	
	try
	{
		badgeData = parseInt(badgeData);
		badgeData = (badgeData).toString(2);
		badgeData = addFrontStr(badgeData,hotvodBadgeList.split("|").length);
		badgeData = badgeData.split("").reverse().join("");
	}
	catch(error)
	{
		badgeData = 0;
	}
	
	//console.log(badgeData);
	//console.log('badgeData.length:'+badgeData.length);
	
	$('#'+formId).find("input[name="+checkBoxName+"][class=badge]").each(function(idx){
    	
		if(badgeData.length > idx)
		{
			var value = badgeData.substring(idx,idx+1);
			if(value == '1')
				$(this).attr('checked', true) ;
			else
				$(this).attr('checked', false) ;
		}
		
	});
	
}

function setCheckBoxData(formId,checkBoxName,targetId)
{
	var isCheck = '';
	
	$($('#'+formId).find("input[name="+checkBoxName+"][class=badge]").get().reverse()).each(function(idx){
		if(this.type == 'checkbox')
		{
			var tempIsCheck = $(this).is(":checked") ? 1 : 0;
			var attrName = $(this).val();
			//console.log(attrName);

			isCheck = isCheck + ''+tempIsCheck;
		}
		
	});
		
	isCheck = parseInt(isCheck,2);
	$('#'+targetId).val(isCheck);
	//console.log($('#'+targetId).val());
}

function setServiceTypeData(formId,checkBoxName,targetId)
{
	var isCheck = '';
	
	var isAllService = $("#service_check_all").prop('checked');
	
	if(isAllService === true) {
		$('#'+targetId).val('');
	} else if(isAllService === false) {
		$($('#'+formId).find("input[name="+checkBoxName+"][class=service_check]").get().reverse()).each(function(idx){
	    	
			if(this.type == 'checkbox')
			{
				var tempIsCheck = $(this).is(":checked") ? 1 : 0;
				var attrName = $(this).val();
				//console.log(attrName);
				
				isCheck = isCheck + ''+tempIsCheck;
			}
			
		});
		isCheck = parseInt(isCheck,2);
		$('#'+targetId).val(isCheck);
	}
	//console.log($('#'+targetId).val());
}

function drawServiceTypeData(formId,checkBoxName,targetId)
{
	var serviceData = $('#'+targetId).val();
	
	if(serviceData == null || serviceData == '') {
		$("#service_check_all").prop('checked', true);
		return;
	}
	
	if(serviceData == '0')
		return;
	
	var hotvodServiceList = '${hotvodServiceList}';
	//console.log("hotvodServiceList:"+hotvodServiceList);
	
	try
	{
		serviceData = parseInt(serviceData);
		serviceData = (serviceData).toString(2);
		serviceData = addFrontStr(serviceData,hotvodServiceList.split("|").length)
		//console.log("serviceData1:"+serviceData);
		serviceData = serviceData.split("").reverse().join("");
	}
	catch(error)
	{
		serviceData = 0;
	}
	
	//console.log("serviceData2:"+serviceData);
	//console.log("serviceData.length:"+serviceData.length);

	$($('#'+formId).find("input[name="+checkBoxName+"][class=service_check]")).each(function(idx){
		if(serviceData.length > idx)
		{
			var value = serviceData.substring(idx,idx+1);
			if(value == '1')
				$(this).attr('checked', true) ;
			else
				$(this).attr('checked', false) ;
		}		
	});
}

function checkNum(obj){
	var txt = obj.value;
	var num = "1234567890";
	var i=0;
	for(i=0;i<txt.length;i++){
		if(num.indexOf(txt.charAt(i)) < 0){
			alert("숫자만 입력 가능합니다.");
			obj.value = txt.substring(0,i);
			obj.focus();
			return false;
		}
	}
}

function checkNumAndColon(obj){
	var txt = obj.value;
	var num = "1234567890:";
	var i=0;
	for(i=0;i<txt.length;i++){
		if(num.indexOf(txt.charAt(i)) < 0){
			alert("숫자만 입력 가능합니다.");
			obj.value = txt.substring(0,i);
			obj.focus();
			return false;
		}
	}
}

//1. 화면 초기화
function resetDisplay(){
	$('#content_url').attr('style','display:block');

	$('#iptv_image').text('컨텐츠 이미지( IPTV )');
	
	AnyTime.noPicker("start_time");
	AnyTime.noPicker("end_time");
	AnyTime.noPicker("start_dt");
	AnyTime.noPicker("end_dt");
	AnyTime.noPicker("duration");
	
	$('.contentTypeA').attr('style','display:none');
	$('.contentTypeB').attr('style','display:none');
	
	//2019.11.12 브릿지홈 개편 : contentTypeM-> VOD상세  contentTypeN -> VOD 카테고리
	$('.contentTypeM').attr('style','display:none');
	$('.contentTypeN').attr('style','display:none');
	
	$('#service_type_tr').attr('style','display:none');	//서비스 구분
	$('#test_yn').attr('style','display:none');	//검수 단말기 전용메뉴 여부
	$('#unify_search_yn').attr('style','display:none');	//통합 검색여부
	
	
	//2019.11.06 브릿지홈 개편 : 화면 제어 처리 추가
	$('#searchAlbumCateBtn').attr('style','display:none');//카테고리ID 검색 버튼 비노출 처리
	$('#category_nm').attr('style','display:none');//카테고리명 비노출 처리
	
}

//3. 화면 제어 (type : 컨텐츠 구분)
function typeChange(type){
	
	//화면 초기화
	resetDisplay();
	
	//2019.11.11 브릿지홈 개편 : VOD 상세 추가
	if(type == 'M'){
		$('.contentTypeM').removeAttr('style');
		
		$('#view_tr').removeAttr('style');	//노출기간
		$('#hit_cnt').removeAttr("disabled");
		
		AnyTime.picker('start_dt', { format: '%z-%m-%d %H:%i:%S', labelTitle: '노출 시작일', labelYear: "년", labelMonth: "월", labelDayOfMonth: "일", labelHour: "시", labelMinute:"분", labelSecond:"초", time:''});
		AnyTime.picker('end_dt', { format: '%z-%m-%d %H:%i:%S', labelTitle: '노출 종료일', labelYear: "년", labelMonth: "월", labelDayOfMonth: "일", labelHour: "시", labelMinute:"분", labelSecond:"초", time:''});
	}
	
	//2019.11.11 브릿지홈 개편 : VOD 카테고리 추가
	if(type == 'N'){
		$('.contentTypeN').removeAttr('style');	//카테고리ID 필드 노출 처리
		
		$('#hit_cnt').removeAttr("disabled");
		$('#searchAlbumCateBtn').removeAttr('style');	//카테고리ID 검색 버튼 노출 처리
		$('#category_nm').removeAttr('style');	//카테고리명 노출 처리
		$('#view_tr').removeAttr('style');	//노출기간
		
		AnyTime.picker('start_dt', { format: '%z-%m-%d %H:%i:%S', labelTitle: '노출 시작일', labelYear: "년", labelMonth: "월", labelDayOfMonth: "일", labelHour: "시", labelMinute:"분", labelSecond:"초", time:''});
		AnyTime.picker('end_dt', { format: '%z-%m-%d %H:%i:%S', labelTitle: '노출 종료일', labelYear: "년", labelMonth: "월", labelDayOfMonth: "일", labelHour: "시", labelMinute:"분", labelSecond:"초", time:''});
	}	
	
	//카테고리
	if(type == 'C'){
		$('.contentTypeA').removeAttr('style');
		
		$('#content_url').attr("disabled","disabled");
		$('#duration').attr("disabled","disabled");
		$('#site_id').attr("disabled","disabled");
		$('#hit_cnt').attr("disabled","disabled");
		$('#view_tr').attr('style','display:none');
		
		$('#test_yn').removeAttr('style');
		$('#service_type_tr').removeAttr('style');
		$('#unify_search_yn').removeAttr('style');			
	}
	
	//컨텐츠
	if(type == 'V' || type == 'L'){
		$('.contentTypeA').removeAttr('style');
		
    	$('#duration_tr').removeAttr('style','display:none');
		$('#content_url').removeAttr("disabled");
		$('#duration').removeAttr("disabled");
		$('#site_id').removeAttr("disabled");
		$('#hit_cnt').removeAttr("disabled");
		$('#view_tr').removeAttr('style');
					
		AnyTime.picker( "duration", { format: "%H:%i:%S",  labelTitle: "상영시간", labelHour: "시", labelMinute:"분", labelSecond:"초"} );			
		AnyTime.picker('start_dt', { format: '%z-%m-%d %H:%i:%S', labelTitle: '노출 시작일', labelYear: "년", labelMonth: "월", labelDayOfMonth: "일", labelHour: "시", labelMinute:"분", labelSecond:"초", time:''});
		AnyTime.picker('end_dt', { format: '%z-%m-%d %H:%i:%S', labelTitle: '노출 종료일', labelYear: "년", labelMonth: "월", labelDayOfMonth: "일", labelHour: "시", labelMinute:"분", labelSecond:"초", time:''});			
	}
}

function checkKeyIn(obj, id){
	var newId = obj.val();
	
	if(newId != id){
		checkByte(obj,'9');
	}else{
		checkByte(obj,'10');
	}
}

function inputColon(obj){
	var val = obj.value.replace(/:/gi, "");
	var temp1 = val.substr(0,2);
	var temp2 = val.substr(2,2);
	var temp3 = val.substr(4,2);
		
	if(2 < val.length && val.length < 5){
		obj.value = temp1 + ":" + temp2;
	}
	
	if(4 < val.length){
		obj.value = temp1 + ":" + temp2 + ":" + temp3;
	}
}

function deleteImage(type){
	if(type == 'IP'){
		if(confirm("컨텐츠 이미지(IPTV)를 삭제하시겠습니까?")){
			$("#content_img_tv").val("");
			$("#image_tv").remove();
		}
	}
}

function winOpen(imagePath, Width, Height) {
    window.open(imagePath, 'windowName', 'toolbar=no,scrollbars=yes,resizable=no, top=20,left=200,width=' + Width + ',height=' + Height);
}

//2019.11.06 브릿지홈 개편 : 화제동영상 UI타입 선택 팝업추가
function getUITypeListPop() {
	var url = '<%=webRoot%>/admin/mainpanel/getPanelUiTypeListPop.do?frame_type=30&callbak=getUITypeListPopCallbak';
	window.open(url, 'getFrameListPop', 'width=700,height=650,left=100,top=50,scrollbars=yes');
}

function getUITypeListPopCallbak(data) {
	$("#hv_ui_type").val(data.frame_type_code);
}

//2019.11.07 브릿지 홈 개편 : IMCS 카테고리 ID 선택 팝업추가
//isAds=Y -> 팝업창에서 카테고리 구분을 선택하려면 isAds =Y로 호출해야함
function getVodCategoryPop(){
	var url = '<%=webRoot%>/admin/mainpanel/getPageCategoryList.do?category_id=VC&isCategoryGbUse=Y';
	window.open(url, "getPageCategoryList", "left=500,width=800,height=200,scrollbars=yes");
}
</script>
<body leftmargin="0" topmargin="0">
	<div id="divBody" style="height: 100%">
		<table border="0" cellpadding="0" cellspacing="0" width="100%"	align="center">
			<tbody>
				<tr>
					<td class="3_line" height="1"></td>
				</tr>
				<!-- 리스트 시작 -->
				<tr>
					<td>
						<table border="0" cellpadding="0" cellspacing="0" width="100%"
							align="center">
							<tbody>
								<tr>
									<td height="25">
										<table border="0" cellpadding="0" cellspacing="0" width="100%">
											<tbody>
												<tr>
													<td width="15"><img	src="/smartux_adm/images/admin/blt_07.gif"></td>
													<td class="bold">컨텐츠 상세</td>
												</tr>
											</tbody>
										</table>
									</td>
								</tr>
							</tbody>
						</table>
						<form id="form1" name="form1" action="./subUpdate.do" method="post" enctype="multipart/form-data">
							<input type="hidden" name="findName" value="${vo.findName}" /> 
							<input type="hidden" name="findValue" value="${vo.findValue}" />
							<input type="hidden" name="delYn" value="${vo.delYn}" />
							<input type="hidden" id="auth_decrypt" value="${auth_decrypt }" />
							<input type="hidden" name="pre_parent_id" value="${content.parent_id }" />
							<table border="0" cellpadding="0" cellspacing="0" width="660"
								class="board_data">
								<tbody>
                                	<!-- 노출기간(컨텐츠타입:V) -->
                                	<tr align="center" id="view_tr" style="display: none">
                                		<th width="25%">노출기간</th>
	                                    <td width="5%"></td>
	                                    <td width="70%" align="left" >
											<input type="text" name="start_dt" id="start_dt" size="19" onKeyUp="checkByte($(this),'19');checkNumAndColon(this);" style="font-size: 12px;" value="${content.start_dt}"/> ~ <input type="text" name="end_dt" id="end_dt" size="19" onKeyUp="checkByte($(this),'19');checkNumAndColon(this);" style="font-size: 12px;" value="${content.end_dt}"/>
											<input type="button" style="font-size: 12px;" id="viewClear" value="Clear"/>
										</td>
                                	</tr>					                                
									<!-- 컨텐츠ID -->
									<tr align="center">
										<th width="25%">컨텐츠ID</th>
										<td width="5%"></td>
										<td width="70%" align="left">
										<input type="text"	name="content_id_t" id="content_id_t" size="10"	value="${vo.content_id}" style="font-size: 12px;" onKeyUp="checkByte($(this),'10')" disabled="disabled"/>
										<input type="hidden" name="content_id" id="content_id" value="${vo.content_id }"/>
										</td>
									</tr>
									<!-- 컨텐츠 구분 -->
									<tr align="center">
										<th width="25%">컨텐츠 구분</th>
										<td width="5%"></td>
										<td width="70%" align="left">
										<select id="content_type_t" name="content_type_t" onchange="typeChange(this.value);" disabled="disabled">
											<option	<c:if test="${content.content_type eq 'C'}">selected='selected'</c:if> value="C">카테고리</option>
											<option	<c:if test="${content.content_type eq 'V'}">selected='selected'</c:if> value="V">컨텐츠</option>
											<option	<c:if test="${content.content_type eq 'M'}">selected='selected'</c:if> value="M">VOD 상세</option>
											<option	<c:if test="${content.content_type eq 'N'}">selected='selected'</c:if> value="N">VOD 카테고리</option>
											<option	<c:if test="${content.content_type eq 'L'}">selected='selected'</c:if> value="L">링크 URL</option>
										</select>
										<input type="hidden" name="content_type" id="content_type" value="${content.content_type }"/>
										</td>
									</tr>
									<c:if test="${content.content_type eq 'C'}">
										<!-- 서비스타입 -->
		                                <tr align="center" id="service_type_tr">
											<th width="25%">서비스 타입</th>
											<td width="5%"></td>
											<td width="70%" align="left">
												<input type="checkbox" id="service_check_all" value="" name="service_check" size="50" style="font-size: 12px;" >
						                    	<label for="service_check_all">전체</label>
						                  		
						                  		<hotvod:service-input isLock="${vo.isLock}" serviceType="${vo.serviceType}"/>
						                    	
						                    	<input type="hidden" name="multi_service_type" id="multi_service_type" value="${content.multi_service_type}" >
					                    		<input type="hidden" name="isLock" id="isLock" value="${vo.isLock}" >
											</td>
										</tr>						                                
										<!-- 검수 단말기 전용 메뉴 -->
										<tr align="center" id="test_yn">
											<th width="25%">검수 단말기 전용 메뉴</th>
											<td width="5%"></td>
											<td width="70%" align="left">
												<label><input type="radio" name="test_yn" value="N" <c:if test="${content.test_yn eq 'N' || content.test_yn eq null}">checked="checked"</c:if>>전체</label>
												<label><input type="radio" name="test_yn" value="Y" <c:if test="${content.test_yn eq 'Y'}">checked="checked"</c:if>>검수단말기 전용</label>
												<label><input type="radio" name="test_yn" value="S" <c:if test="${content.test_yn eq 'S'}">checked="checked"</c:if>>상용단말기 전용</label>
											</td>
										</tr>
										<!-- 통합 검색 여부 -->
										<tr align="center" id="unify_search_yn">
											<th width="25%">통합 검색 여부</th>
											<td width="5%"></td>
											<td width="70%" align="left">
												<label><input type="radio" name="unify_search_yn" value="Y" <c:if test="${content.unify_search_yn eq 'Y' || content.unify_search_yn eq null}">checked="checked"</c:if>>허용</label>
												<label><input type="radio" name="unify_search_yn" value="N" <c:if test="${content.unify_search_yn eq 'N'}">checked="checked"</c:if>>미허용</label>
											</td>
										</tr>
									</c:if>
				                     <!-- 뱃지 데이터-->
									<tr align="center">
										<th width="25%">뱃지</th>
										<td width="5%"></td>
										<td width="70%" align="left">
											<hotvod:badge-input/>
                                  		 	<input type="hidden" name="badge_data" id="badge_data" size="50" value="${content.badge_data}" style="font-size: 12px;" >
										</td>
									</tr>  
									<!-- 2019.11.12 브릿지홈 개편 : UI타입 선택 추가 -->
									<tr align="center"  class="contentTypeM contentTypeN" style="display: none">
                                		<th width="25%">UI타입</th>
	                                    <td width="5%"></td>
	                                    <td width="70%" align="left" >
											<input type="text" name="hv_ui_type" id="hv_ui_type" size="5" style="font-size: 12px;" readonly="readonly" value="${content.hv_ui_type}"/>
											<span class="button small blue" id="searchUITypeBtn" onclick="getUITypeListPop()">UI타입 선택</span>
										</td>
                                	</tr>            		 		
									<!-- 컨텐츠명 -->
									<tr align="center">
										<th width="25%">컨텐츠명</th>
										<td width="5%"></td>
										<td width="70%" align="left">
										<input type="text" name="content_name" id="content_name" size="50" value="<c:out value='${content.content_name }'/>" style="font-size: 12px;" onKeyUp="checkByte($(this),'100')">
										</td>
									</tr>
									<!-- 컨텐츠URL -->
	                                <tr align="center" class="contentTypeA">
	                                    <th width="25%">컨텐츠URL</th>
	                                    <td width="5%"></td>
	                                    <td width="70%" align="left" >
                                  		 	<input type="text" name="content_url" id="content_url" size="50" value="<c:out value='${content.content_url }'/>" style="font-size: 12px;" onKeyUp="checkByte($(this),'512')">
										</td>													
	                                </tr>
	                                <!-- 컨텐츠 설명 -->
	                                <tr align="center">
	                                    <th width="25%">컨텐츠 설명</th>
	                                    <td width="5%"></td>
	                                    <td width="70%" align="left" >
											<textarea rows="10" cols="40" name="content_info" id="content_info" style="font-size: 12px;" onKeyUp="checkByte($(this),'255')" >${content.content_info }</textarea>
										</td>
	                                </tr>
                              			<tr align="center" class="contentTypeA contentTypeN contentTypeM">
	                                    <th width="25%">컨텐츠 이미지( IPTV )</th>
	                                    <td width="5%"></td>
	                                    <td width="70%" align="left" >
	                                    	<input type="file" id="file_tv"   accept="image/*" name="file_tv" value="파일선택" onchange="imgHeaderCheck('file_tv', 'hotvod',${imgIptvSize});"/>
											${imgIptvSize}kb / ${imgFormat}
											<c:if test="${content.content_img_tv!=null}"> 
												<div id="image_tv">																			
													  <br><a href="javascript:winOpen('${content.content_img_tv_url}','200','300')">
													  <c:choose>
													  	<c:when test="${fn:indexOf(content.content_img_tv, '/') > -1 }">
													  		${fn:substring(content.content_img_tv, 8, fn:length(content.content_img_tv))}
													  	</c:when>
													  	<c:otherwise>
													  		${content.content_img_tv }
													  	</c:otherwise>
													  </c:choose>
													  </a>
													<a href="javascript:deleteImage('IP')"><span class="button small rosy">삭제</span></a>
												</div>
												<input type="hidden" id="content_img_tv" name="content_img_tv" value="${content.content_img_tv}">
											</c:if>
										</td>
                              			 </tr>  
	                                 <!-- 상영시간 -->
	                                <tr align="center" class="contentTypeA" id="duration_tr">
	                                    <th width="25%">상영시간</th>
	                                    <td width="5%"></td>
	                                    <td width="70%" align="left" >
	                                    	<input type="text" name="duration" id="duration" size="10" value="${content.duration }"/>																		
										</td>													
	                                </tr>
	                                 <!-- 출처 사이트-->
	                                <tr align="center" class="contentTypeA">
	                                    <th width="25%">출처 사이트</th>
	                                    <td width="5%"></td>
	                                    <td width="70%" align="left" >
											<select name="site_id" id="site_id">
												<option value="">:::선택:::</option>
												<c:forEach items="${siteList }" var="list">
													<option <c:if test="${content.site_id eq list.site_id}">selected='selected'</c:if> value="${list.site_id}">${list.site_name }</option>
												</c:forEach>
											</select>																			
										</td>													
	                                </tr>
					                                	
	                                <!-- 앨범ID -->
                                	<tr align="center" class="contentTypeB contentTypeM" style="display: none">
                                		<th width="25%">앨범ID</th>
	                                    <td width="5%"></td>
	                                    <td width="70%" align="left" >
											<input type="text" name="album_id" id="album_id" size="20" style="font-size: 12px;" readonly="readonly" value="${content.album_id }"/>
											<input type="hidden" name="choiceCts" id="choiceCts" value="" />
											<input type="hidden" name="still_img" id="still_img" value="${content.still_img }" />
											<span class="button small blue" id="searchAlbumBtn">검색</span>
										</td>
                                	</tr>
                                	<!-- 카테고리ID -->
	                                <!-- 2019.11.12 브릿지 홈 개편 : IMCS 카테고리 ID 검색버튼 추가  -->
                                	<tr align="center" class="contentTypeB contentTypeN contentTypeM" style="display: none">
                                		<th width="25%">카테고리ID</th>
	                                    <td width="5%"></td>
	                                    <td width="70%" align="left" >
											<input type="text" name="category_id" id="category_id" size="20" style="font-size: 12px;" readonly="readonly" value="${content.category_id }"/>
											<span class="button small blue" id="searchAlbumCateBtn" onclick="getVodCategoryPop()">검색</span>
											<span id="category_nm">${content.category_name}</span>
										</td>
                                	</tr>
                                	<!-- 시리즈명 -->
                                	<tr align="center" class="contentTypeB contentTypeM" style="display: none">
                                		<th width="25%">시리즈명</th>
	                                    <td width="5%"></td>
	                                    <td width="70%" align="left" id="contents_name">
	                                    	${content.contents_name }
										</td>
                                	</tr>
                                	<!-- 시리즈번호 -->
                                	<tr align="center" class="contentTypeB contentTypeM" style="display: none">
                                		<th width="25%">시리즈번호</th>
	                                    <td width="5%"></td>
	                                    <td width="70%" align="left" id="series_no">
	                                    	${content.series_no }
										</td>
                                	</tr>
                                	<!-- 방송사 -->
                                	<tr align="center" class="contentTypeB contentTypeM" style="display: none">
                                		<th width="25%">방송사</th>
	                                    <td width="5%"></td>
	                                    <td width="70%" align="left" id="sponsor_name">
	                                    	${content.sponsor_name }
										</td>
                                	</tr>
                                	<!-- 상영시간 -->
                                	<tr align="center" class="contentTypeB" style="display: none"  id="duration_trB">
                                		<th width="25%">상영시간</th>
	                                    <td width="5%"></td>
	                                    <td width="70%" align="left" >
											<input type="text" name="start_time" id="start_time" size="10" onKeyUp="checkByte($(this),'8');checkNumAndColon(this);inputColon(this);" style="font-size: 12px;" value="${content.start_time}"/> ~ <input type="text" name="end_time" id="end_time" size="10" onKeyUp="checkByte($(this),'8');checkNumAndColon(this);inputColon(this);" style="font-size: 12px;" value="${content.end_time }"/>
										</td>
                                	</tr>
									<!-- 상위 컨텐츠ID -->
									<tr align="center">
										<th width="25%">상위 컨텐츠ID</th>
										<td width="5%"></td>
										<td width="70%" align="left">
										<input type="text" name="parent_id" id="parent_id" size="10" style="font-size: 12px;" value="${content.parent_id }" readonly="readonly" /> 
										<span class="button small blue"	id="searchBtn">검색</span>
										</td>
									</tr>
									<!-- 컨텐츠 순서 -->
									<tr align="center">
										<th width="25%">컨텐츠 순서</th>
										<td width="5%"></td>
										<td width="70%" align="left">
										<input type="text" name="content_order" id="content_order" size="15" onKeyUp="checkByte($(this),'15');checkNum(this);" style="font-size: 12px;" value="${content.content_order }" />
										</td>
									</tr>
									<!-- 조회수-->
									<tr align="center">
										<th width="25%">조회수</th>
										<td width="5%"></td>
										<td width="70%" align="left">
										<input type="text" name="hit_cnt" id="hit_cnt" size="15" onKeyUp="checkByte($(this),'15');checkNum(this);" style="font-size: 12px;" value="${content.hit_cnt }">
										</td>
									</tr>
									<!-- 노출여부 (del_yn은 원래 삭제여부였으나 고도화로 인해 재활용하여 노출여부로 사용한다 N:노출, Y:비노출)-->
	                                <tr align="center">
	                                    <th width="25%">노출여부</th> 
	                                    <td width="5%"></td>
	                                    <td width="70%" align="left" >
											<select id="del_yn" name="del_yn">
												<option value="N" <c:if test="${content.del_yn eq 'N'}">selected='selected'</c:if>>Y</option>
												<option value="Y" <c:if test="${content.del_yn eq 'Y'}">selected='selected'</c:if>>N</option>
											</select>												 							
										</td>
	                                </tr>
								</tbody>
							</table>

							<!-- 등록/수정 버튼 -->
							<table border="0" cellpadding="0" cellspacing="0" width="660"
								align="left">
								<tbody>
									<tr>
										<td height="25" align="right">
		                                	<span class="button small blue" id="sumitBtn">수정</span>
											<a href="./openSubListPop.do?findName=${vo.findName}&findValue=${vo.findValue}&content_id=${vo.parent_id}&parent_id=${vo.parent_id}&delYn=${vo.delYn}">
											<span class="button small blue">목록</span></a> 
											<span class="button small blue" id="closebtn">닫기</span>
										</td>
									</tr>
								</tbody>
							</table>
						</form>

						<table border="0" cellpadding="0" cellspacing="0" width="100%">
							<tbody>
								<tr>
									<td height="1"></td>
								</tr>
								<tr>
									<td class="3_line" height="3"></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td colspan="2" background="/smartux_adm/images/admin/copy_bg.gif" height="60" align="left">
										<!-- 하단 로그인 사용자 정보 시작 --> 
										<%@include file="/WEB-INF/views/include/bottom.jsp"%>
										<!-- 하단 로그인 사용자 정보 종료 -->
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