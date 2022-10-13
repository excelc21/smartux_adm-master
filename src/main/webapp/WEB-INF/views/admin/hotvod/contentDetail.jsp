<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib prefix="hotvod" uri="/WEB-INF/tlds/hotvod.tld" %> 
<title>LG U+ IPTV</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
 <link href="/smartux_adm/css/anytime_style.css" rel="stylesheet" type="text/css">
<script src="/smartux_adm/js/anytime.js"></script>

<script type="text/javascript">
	
$(document).ready(function(){
	var type = '${content.content_type}';
	typeChange(type);
	
	$('#viewClear').click(function() {
        $('#start_dt').val('');
        $('#end_dt').val('');
  	});
	$("#service_check_all").click(function(){
		$(".service_check").prop("checked", false);
	});
	$(".service_check").click(function() {
		$("#service_check_all").prop('checked', false);
	});
	
	drawCheckBoxData('form1','badge_check','badge_data');
	
	drawServiceTypeData('form1','service_check','multi_service_type');
	
	var result = '${result}';
	if(result == "0000"){
		alert("수정되었습니다.");
	}else if(result == "0002"){
		alert("최대 이미지 사이즈를 초과 했습니다.\n컨텐츠이미지(HDTV):${hdtvSize}\n컨텐츠이미지(IPTV):${iptvSize}");
	}

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
	 	
	 	if($('#content_type').val() == 'H'){
	 		if($('#category_id').val() == ''){
		 		alert('시리즈를 선택해주세요.');
		 		return;
		 	}
		 	if($('#start_time').val() == ''){
		 		alert('상영시간의 시작시간을 입력해주세요.');
		 		$('#start_time').focus();
		 		return;
		 	}else{
		 		var timePattern = /\d\d\:\d\d\:\d\d/gi;
		 		if(!timePattern.test($('#start_time').val())){
		 			alert('상영시간의 시작시간 형식이 올바르지 않습니다.');
		 			$('#start_time').focus();
		 			return;
		 		}
		 	}
		 	if($('#end_time').val() == ''){
		 		alert('상영시간의 종료시간을 입력해주세요.');
		 		$('#end_time').focus();
		 		return;
		 	}else{
		 		var timePattern = /\d\d\:\d\d\:\d\d/gi;
		 		if(!timePattern.test($('#end_time').val())){
		 			alert('상영시간의 종료시간 형식이 올바르지 않습니다.');
		 			$('#end_time').focus();
		 			return;
		 		}
		 	}
		 	var start_time = new Date();
		 	var end_time = new Date();
		 	var h1 = $('#start_time').val().split(":")[0];
		 	var h2 = $('#end_time').val().split(":")[0];
		 	var m1 = $('#start_time').val().split(":")[1];
		 	var m2 = $('#end_time').val().split(":")[1];
		 	var s1 = $('#start_time').val().split(":")[2];
		 	var s2 = $('#end_time').val().split(":")[2];
		 	
		 	start_time.setHours(h1, m1, s1);
		 	end_time.setHours(h2, m2, s2);
		 	
		 	if(end_time <= start_time){
		 		alert('상영시간의 종료시간은 시작시간과 같거나 작을 수 없습니다.');
		 		$('#end_time').focus();
		 		return;
		 	}
		 			 	
		 	var min = 60000;
		 	
		 	if((min * 3) < (end_time - start_time)){
		 		alert('상영시간은 3분 이하로 설정해 주세요.');
		 		return;
		 	}
	 	}
	 	
	 	if($('#content_type').val() == 'M'){
	 		if($('#category_id').val() == ''){
		 		alert('시리즈를 선택해주세요.');
		 		return;
		 	}
	 	}
	 	/*
		if(($('#content_type').val() != 'H' && $('#content_type').val() != 'M') && $("#content_info").val() == ''){
			alert('컨텐츠 설명을 입력해 주세요.');
			$("#content_info").focus();
			return;
		}
	 	*/
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
	    }else{
	    	
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
		var url = "<%=webRoot%>/admin/hotvod/openCategoryPop.do?content_type=C&parent_id=${content.parent_id}";
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
				    	$('#album_id').val(data.result.album_id);
				    	$('#category_id').val(data.result.category_id);
				    	$('#contents_name').text(data.result.contents_name);
				    	$('#series_no').text(data.result.series_no);
				    	$('#sponsor_name').text(data.result.sponsor_name);
				    	$('#still_img').val(data.result.still_img);
				    	$("#choiceCts").val('');
				    	$.unblockUI();
				    },
				    error: function(){
				    	$.unblockUI();
				    	alert("에러가 발생하였습니다.");
				    }
				});	
				clearInterval(closeCheck);
			}
		}, 1000);
    	
		var url = "<%=webRoot%>/admin/commonMng/getOnceCategoryAlbum.do?categoryId=VC&hiddenName=choiceCts&isTypeChange=Y&type=I20";
		//콘서트,방송인 경우에 편성앨범중 비노출인경우도 표시
		if($('#content_type').val() == 'B' || $("#content_type").val() == 'A'){
			url += "&conBrodYn=Y";			
		}
			
    	category_window = window.open(url, "categoryAlbumPop", "width=800,height=330,scrollbars=yes");
	});
});

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

function typeChange(type){
	
	$('#content_url').attr('style','display:block');
	//역사 항목 수정 및 IMCS VOD 추가
	$('#hdtv_image').text('컨텐츠 이미지( HDTV )');
	$('#iptv_image').text('컨텐츠 이미지( IPTV )');
	//역사 항목 수정 및 IMCS VOD 추가
	
	AnyTime.noPicker("start_time");
	AnyTime.noPicker("end_time");
	AnyTime.noPicker("start_dt");
	AnyTime.noPicker("end_dt");	
	AnyTime.noPicker("duration");
	
	if(type == 'H' || type == 'M'){
		$('.contentTypeC').attr('style','display:none');
		$('.contentTypeB').removeAttr('style');
		$('.contentTypeA').attr('style','display:none');
		
		$('#hit_cnt').removeAttr("disabled");
		
		//역사 항목 수정 및 IMCS VOD 추가
		if(type == 'M'){
			$('.contentTypeB#duration_trB').attr('style','display:none');
		}
		//역사 항목 수정 및 IMCS VOD 추가
		else if(type == 'H') {
			AnyTime.picker( "start_time", { format: "%H:%i:%S",  labelTitle: "상영시간", labelHour: "시", labelMinute:"분", labelSecond:"초"} );
			AnyTime.picker( "end_time", { format: "%H:%i:%S",  labelTitle: "상영시간", labelHour: "시", labelMinute:"분", labelSecond:"초"} );
		}
	}else{
		$('.contentTypeC').attr('style','display:none');
		$('.contentTypeB').attr('style','display:none');
		$('.contentTypeA').removeAttr('style');
		$('.hdtvImgContentTypeA').attr('style','display:none');
		
		if(type == 'C'){
			$('#album_id').val('');
	    	$('#category_id').val('');
	    	$('#contents_name').text('');
	    	$('#series_no').text('');
	    	$('#sponsor_name').text('');
	    	$('#start_time').val('');
	    	$('#end_time').val('');
			$('#content_url').val('');
			$('#duration').val('');
			$('#site_id').val('');
			$('#hit_cnt').val('');
	    	$('#start_dt').val('');
	    	$('#end_dt').val('');
			
			$('#content_url').attr("disabled","disabled");
			$('#duration').attr("disabled","disabled");
			$('#site_id').attr("disabled","disabled");
			$('#hit_cnt').attr("disabled","disabled");
			$('#view_tr').attr('style','display:none');
			
			$('#test_yn').removeAttr('style');
			$('#service_type_tr').removeAttr('style');
			$('#unify_search_yn').removeAttr('style');			
		}
		if(type == 'V' || type == 'L'){
			$('#album_id').val('');
	    	$('#category_id').val('');
	    	$('#contents_name').text('');
	    	$('#series_no').text('');
	    	$('#sponsor_name').text('');
	    	$('#start_time').val('');
	    	$('#end_time').val('');
	    	
	    	$('#duration_tr').removeAttr('style','display:none');
			$('#content_url').removeAttr("disabled");
			$('#duration').removeAttr("disabled");
			$('#site_id').removeAttr("disabled");
			$('#hit_cnt').removeAttr("disabled");
			$('#view_tr').removeAttr('style');
			
			$('#test_yn').attr('style','display:none');
			$('#service_type_tr').attr('style','display:none');
			$('#unify_search_yn').attr('style','display:none');			
			AnyTime.picker( "duration", { format: "%H:%i:%S",  labelTitle: "상영시간", labelHour: "시", labelMinute:"분", labelSecond:"초"} );
			AnyTime.picker('start_dt', { format: '%z-%m-%d %H:%i:%S', labelTitle: '노출 시작일', labelYear: "년", labelMonth: "월", labelDayOfMonth: "일", labelHour: "시", labelMinute:"분", labelSecond:"초", time:''});
			AnyTime.picker('end_dt', { format: '%z-%m-%d %H:%i:%S', labelTitle: '노출 종료일', labelYear: "년", labelMonth: "월", labelDayOfMonth: "일", labelHour: "시", labelMinute:"분", labelSecond:"초", time:''});			
		}
		if(type == 'O'){
			$('#album_id').val('');
	    	$('#category_id').val('');
	    	$('#contents_name').text('');
	    	$('#series_no').text('');
	    	$('#sponsor_name').text('');
	    	$('#start_time').val('');
	    	$('#end_time').val('');
	    	$('#start_dt').val('');
	    	$('#end_dt').val('');	    	
	    	
	    	$('#duration_tr').attr('style','display:none');
	    	$('#content_url').removeAttr("disabled");
			$('#site_id').removeAttr("disabled");
			$('#hit_cnt').removeAttr("disabled");
			$('#view_tr').attr('style','display:none');
			
			$('#test_yn').attr('style','display:none');
			$('#service_type_tr').attr('style','display:none');
			$('#unify_search_yn').attr('style','display:none');			
		}
		
		//역사 항목 수정 및 IMCS VOD 추가
		if(type == 'S'){
			$('#content_url').val('');
			$('#album_id').val('');
	    	$('#category_id').val('');
	    	$('#contents_name').text('');
	    	$('#series_no').text('');
	    	$('#sponsor_name').text('');
	    	$('#start_time').val('');
	    	$('#end_time').val('');
	    	$('#start_dt').val('');
	    	$('#end_dt').val('');	    	
	    	
	    	$('#duration_tr').attr('style','display:none');
			$('#site_id').removeAttr("disabled");
			$('#hit_cnt').removeAttr("disabled");
			$('#content_url').attr("disabled","disabled");
	    	$('#view_tr').attr('style','display:none');			
			
			$('#hdtv_image').text('컨텐츠 이미지( 작은 )');
			$('#iptv_image').text('컨텐츠 이미지( 큰 )');
			$('#test_yn').attr('style','display:none');
			$('#service_type_tr').attr('style','display:none');
			$('#unify_search_yn').attr('style','display:none');			
		}
		//역사 항목 수정 및 IMCS VOD 추가
		
		// 콘서트,방송(뮤직공연) 추가
		if(type == 'B' || type == 'A'){
			$('#album_id').val('');				//앨범ID
	    	$('#category_id').val('');			//카테고리ID
	    	$('#contents_name').text('');		//공연명
	    	$('#schedule').text('');			//공연일자
	    	$('#duration').val('');
	    	
			$('#content_url').removeAttr("disabled");
			$('#site_id').removeAttr("disabled");
			$('#hit_cnt').removeAttr("disabled");
			$('.contentTypeC').removeAttr('style');
			$('#view_tr').attr('style','display:none');
			
			$('#test_yn').attr('style','display:none');
			$('#service_type_tr').attr('style','display:none');
			$('#unify_search_yn').attr('style','display:none');
			$('.contentTypeA').attr('style','display:none');	
			$('#duration_tr').attr('style','display:none');
		}		
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
	if(type == 'HD'){
		if(confirm("컨텐츠 이미지(HDTV)를 삭제하시겠습니까?")){
			$("#content_img").val("");
			$("#image").remove();
		}
	}else{
		if(confirm("컨텐츠 이미지(IPTV)를 삭제하시겠습니까?")){
			$("#content_img_tv").val("");
			$("#image_tv").remove();
		}
	}
}

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
		serviceData = addFrontStr(serviceData,hotvodServiceList.split("|").length);
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
</script>
<body leftmargin="0" topmargin="0">
<div id="divBody" style="height:100%">
<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
    <tbody>
      <tr>
        <td colspan="2" height="45" valign="bottom">
       		 <!-- top menu start -->
			<%@include file="/WEB-INF/views/include/top.jsp" %>
            <!-- top menu end -->
	   </td>
	  </tr>
	  <tr>
        <td height="10"></td>
        <td></td>
      </tr>
      <tr>
      <td colspan="2" valign="top">
        <table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
        <tbody>
        <tr>
            <td width="4"></td>
            <td valign="top" width="180">
      		<!-- left menu start -->
      		<%@include file="/WEB-INF/views/include/left.jsp" %>
      		<!-- left menu end -->
            </td>
			<td background="/smartux_adm/images/admin/bg_02.gif" width="35">&nbsp;</td>
            <td valign="top">
            <table border="0" cellpadding="0" cellspacing="0" width="98%">
                <tbody>
                <tr>
                    <td height="42" width="100%">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tbody>
                            <tr>
                                <td width="300" class="boldTitle">
                                	<!-- 카테고리 타이틀 -->
                                    <!-- <img src="/smartux_adm/images/admin/category_map_title.gif">-->
                                    	화제동영상 관리	
                                </td>
                            </tr>
                        	</tbody>
                        </table>
                    </td>
                </tr>
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
						                                            <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
						                                            <td class="bold">컨텐츠 상세</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="form1" name="form1" action="./contentUpdate.do" method="post" enctype="multipart/form-data">
						                        <input type="hidden" name="findName" value="${vo.findName}" />
						                        <input type="hidden" name="findValue" value="${vo.findValue}" />
						                        <input type="hidden" name="pageNum" value="${vo.pageNum}" />
						                        <input type="hidden" name="pageSize" value="${vo.pageSize}" />
						                        <input type="hidden" name="delYn" value="${vo.delYn}" />
						                        <input type="hidden" name="pre_parent_id" value="${content.parent_id}" />
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" class="board_data">
					                                <tbody>
					                                	<!-- 노출기간(컨텐츠타입:V, L) -->
					                                	<tr align="center" id="view_tr" style="display: none">
					                                		<th width="25%">노출기간</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
						                                    	<input type="text" name="start_dt" id="start_dt" size="19" onKeyUp="checkByte($(this),'19');checkNumAndColon(this);" style="font-size: 12px;" value="${content.start_dt}"/> ~ <input type="text" name="end_dt" id="end_dt" size="19" onKeyUp="checkByte($(this),'19');checkNumAndColon(this);" style="font-size: 12px;" value="${content.end_dt }"/>
																<input type="button" style="font-size: 12px;" id="viewClear" value="Clear"/>						                                    	
															</td>
					                                	</tr>					                                
						                                <!-- 컨텐츠ID -->
						                                <tr align="center">
						                                    <th width="25%">컨텐츠ID</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<input type="text" name="content_id_t" id="content_id_t" size="10" value="${vo.content_id}" style="font-size: 12px;" onKeyUp="checkByte($(this),'10')" disabled="disabled"/>
																<input type="hidden" name="content_id" id="content_id" value="${vo.content_id}"/>
															</td>													
						                                </tr>
						                                <!-- 컨텐츠 구분 -->
						                                <tr align="center"  >
						                                    <th width="25%">컨텐츠 구분</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<select id="content_type_t" name="content_type_t" onchange="typeChange(this.value);" disabled="disabled">
																	<option <c:if test="${content.content_type eq 'C'}">selected='selected'</c:if> value="C" >카테고리</option>
																	<option <c:if test="${content.content_type eq 'V'}">selected='selected'</c:if> value="V">컨텐츠</option>
																	<option <c:if test="${content.content_type eq 'L'}">selected='selected'</c:if> value="L">링크 URL</option>
																</select>
																<input type="hidden" id="content_type" name="content_type" value="${content.content_type}"/>
															</td>
						                                </tr>
						                                <tr align="center" id="service_type_tr" style="display: none">
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
														<tr align="center" id="test_yn" style="display: none">
															<th width="25%">검수 단말기 전용 메뉴</th>
															<td width="5%"></td>
															<td width="70%" align="left">
																<label><input type="radio" name="test_yn" value="N" <c:if test="${content.test_yn eq 'N' || content.test_yn eq null}">checked="checked"</c:if>>전체</label>
																<label><input type="radio" name="test_yn" value="Y" <c:if test="${content.test_yn eq 'Y'}">checked="checked"</c:if>>검수단말기 전용</label>
																<label><input type="radio" name="test_yn" value="S" <c:if test="${content.test_yn eq 'S'}">checked="checked"</c:if>>상용단말기 전용</label>
															</td>
														</tr>
														<!-- 통합 검색 여부 -->
														<tr align="center" id="unify_search_yn" style="display: none">
															<th width="25%">통합 검색 여부</th>
															<td width="5%"></td>
															<td width="70%" align="left">
																<label><input type="radio" name="unify_search_yn" value="Y" <c:if test="${content.unify_search_yn eq 'Y' || content.unify_search_yn eq null}">checked="checked"</c:if>>허용</label>
																<label><input type="radio" name="unify_search_yn" value="N" <c:if test="${content.unify_search_yn eq 'N'}">checked="checked"</c:if>>미허용</label>
															</td>
														</tr>
						                                 <!-- 뱃지 데이터-->
														<tr align="center">
															<th width="25%">뱃지</th>
															<td width="5%"></td>
															<td width="70%" align="left">
																<hotvod:badge-input/>
					                                  		 	<input type="hidden" name="badge_data" id="badge_data" size="50" value="${content.badge_data}" style="font-size: 12px;" >
															</td>
														</tr>            
						                                <!-- 컨텐츠명 -->
						                                <tr align="center" >
						                                    <th width="25%">컨텐츠명</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
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
						                                <!-- 이미지 파일 -->					         
						                                <tr align="center" class="contentTypeA hdtvImgContentTypeA">
						                                    <th width="25%">컨텐츠 이미지( HDTV )</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
						                                    	<input type="file" id="file"   accept="image/*" name="file" value="파일선택" onchange="imgHeaderCheck('file', 'hotvod',${imgHdtvSize});"/>
																<br>${imgHdtvSize}kb / ${imgFormat}
																<c:if test="${content.content_img!=null}"> 
																	<div id="image">																			
																		  <br><a href="javascript:winopen('${content.content_img}','200','300')">
																		  <c:choose>
																		  	<c:when test="${fn:indexOf(content.content_img, '/') > -1 }">
																		  		${fn:substring(content.content_img, 8, fn:length(content.content_img))}
																		  	</c:when>
																		  	<c:otherwise>
																		  		${content.content_img }
																		  	</c:otherwise>
																		  </c:choose>
																		  </a>
																		<a href="javascript:deleteImage('HD')"><span class="button small rosy">삭제</span></a>
																	</div>
																	<input type="hidden" id="content_img" name="content_img" value="${content.content_img}">
																</c:if>
															</td>
			                                			 </tr>
			                                			 <tr align="center" class="contentTypeA">
						                                    <th width="25%">컨텐츠 이미지 ( IPTV )</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
						                                    	<input type="file" id="file_tv"   accept="image/*" name="file_tv" value="파일선택" onchange="imgHeaderCheck('file_tv', 'hotvod',${imgIptvSize});"/>
																<br>${imgIptvSize}kb / ${imgFormat}
																<c:if test="${content.content_img_tv!=null}"> 
																	<div id="image_tv">																			
																		  <br><a href="javascript:winopen('${content.content_img_tv}','200','300')">
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
					                                	<tr align="center" class="contentTypeB contentTypeC" style="display: none">
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
					                                	<tr align="center" class="contentTypeB contentTypeC" style="display: none">
					                                		<th width="25%">카테고리ID</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<input type="text" name="category_id" id="category_id" size="20" style="font-size: 12px;" readonly="readonly" value="${content.category_id }"/>
															</td>
					                                	</tr>
					                                	<!-- 시리즈명 -->
					                                	<tr align="center" class="contentTypeB" style="display: none">
					                                		<th width="25%">시리즈명</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" id="contents_name">
						                                    	${content.contents_name }
															</td>
					                                	</tr>
					                                	<!-- 시리즈번호 -->
					                                	<tr align="center" class="contentTypeB" style="display: none">
					                                		<th width="25%">시리즈번호</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" id="series_no">
						                                    	${content.series_no }
															</td>
					                                	</tr>
					                                	<!-- 방송사 -->
					                                	<tr align="center" class="contentTypeB" style="display: none">
					                                		<th width="25%">방송사</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" id="sponsor_name">
						                                    	${content.sponsor_name }
															</td>
					                                	</tr>
					                                	<!-- 상영시간 -->
					                                	<tr align="center" class="contentTypeB" style="display: none" id="duration_trB">
					                                		<th width="25%">상영시간</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<input type="text" name="start_time" id="start_time" size="10" onKeyUp="checkByte($(this),'8');checkNumAndColon(this);inputColon(this);" style="font-size: 12px;" value="${content.start_time}"/> ~ <input type="text" name="end_time" id="end_time" size="10" onKeyUp="checkByte($(this),'8');checkNumAndColon(this);inputColon(this);" style="font-size: 12px;" value="${content.end_time }"/>
															</td>
					                                	</tr>
					                                	
					                                	<!-- 공연명 -->
					                                	<tr align="center" class="contentTypeC" style="display: none">
					                                		<th width="25%">공연명</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" id="contents_name">
						                                    	${content.contents_name }
															</td>
					                                	</tr>						                                
					                                	<!-- 공연일자 -->
					                                	<tr align="center" class="contentTypeC" style="display: none">
					                                		<th width="25%">공연일자</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" id="schedule">
															</td>
					                                	</tr>					                              		
						                                <!-- 컨텐츠 이미지 -->
					                                	<tr align="center" class="contentTypeC" style="display: none">
						                                    <th width="25%">컨텐츠 이미지</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<input type="file" id="file_hl"   accept="image/*" name="file_hl" value="파일선택" onchange="imgHeaderCheck('file_hl', 'hotvod',${imgHdtvSize});"/>
																${imgHdtvSize}kb / ${imgFormat}
																<c:if test="${content.content_img!=null}"> 
																	<div id="image_hl">																			
																		  <br><a href="javascript:winOpen('${content.content_img_url}','200','300')">
																		  <c:choose>
																		  	<c:when test="${fn:indexOf(content.content_img, '/') > -1 }">
																		  		${fn:substring(content.content_img, 8, fn:length(content.content_img))}
																		  	</c:when>
																		  	<c:otherwise>
																		  		${content.content_img }
																		  	</c:otherwise>
																		  </c:choose>
																		  </a>
																		<a href="javascript:deleteImage('HL')"><span class="button small rosy" id="h_rosy">삭제</span></a>
																	</div>
																	<input type="hidden" id="content_img_hl" name="content_img_hl" value="${content.content_img}">
																</c:if>
															</td>
					                              		</tr>						                                
						                                 <!-- 상위 컨텐츠ID -->
						                                <tr align="center">
						                                    <th width="25%">상위 컨텐츠ID</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<input type="text" name="parent_id" id="parent_id" size="10" style="font-size: 12px;" value="${content.parent_id }" readonly="readonly"/> 
																<span class="button small blue" id="searchBtn">검색</span>
																</td>													
						                                </tr>
						                                 <!-- 컨텐츠 순서 -->
						                                <tr align="center">
						                                    <th width="25%">컨텐츠 순서</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<input type="text" name="content_order" id="content_order" size="15" onKeyUp="checkByte($(this),'15');checkNum(this);" style="font-size: 12px;" value="${content.content_order }"/>																		
															</td>
						                                </tr>
						                                <!-- 조회수-->
						                                <tr align="center">
						                                    <th width="25%">조회수</th> 
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
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
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<span class="button small blue" id="sumitBtn">수정</span>
						                                	<a href="./contentList.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&pageSize=${vo.pageSize}&delYn=${vo.delYn}&serviceType=${vo.serviceType}&isLock=${vo.isLock}"><span class="button small blue">목록</span></a>
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
	  <tr>
	    <td height="30">&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr>
	    <td colspan="2" background="/smartux_adm/images/admin/copy_bg.gif" height="60" align="left">
	        <!-- 하단 로그인 사용자 정보 시작 -->
	        <%@include file="/WEB-INF/views/include/bottom.jsp" %>
	        <!-- 하단 로그인 사용자 정보 종료 -->
	    </td>
	 </tr>
</tbody>
</table>
</div>
</body>
</html>