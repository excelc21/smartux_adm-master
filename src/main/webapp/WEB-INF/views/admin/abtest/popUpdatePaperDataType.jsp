<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<%@ taglib prefix="fn"  	uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
// 배열의 삭제 기능을 구현 array.remove(1)
Array.prototype.remove = function(from, to)  
{  
    var rest = this.slice ( (to || from) + 1 || this.length );  
    this.length = from < 0 ? this.length + from : from;  
    return this.push.apply(this, rest);  
};  


var vodcodesarr = new Array();
var papagoList = new Array();
var frm;
var pageType="";

$(document).ready(function(){
	select_bind();

	PageTypeBtnOpenCheck();

    //월정액상품셋팅
    setProductCode();
    //월정액상품셋팅(비노출)
    setProductCode2();

    setTerminal();
	
	$("#gbn_select").change(function(){
		all_hide();
		
		if($(this).val() == "LIVE"){
			
		}else if($(this).val() == "VOD"){
			$("#userselcategorystring").show();
			if($("#category_gb").val() == "I20"){
				$("#bestvodtable").show();
				$("#bestvodtable_i30").hide();
			}else{
				$("#bestvodtable_i30").show();
				$("#bestvodtable").hide();
			}
		}else if($(this).val() == "CAT_MAP" || $(this).val() == "BOOKTV" || $(this).val() == "ENG_PRESCH" || $(this).val() == "BRAND"){
			$("#categorytable").attr("style", "display:block");
			if($("#category_gb").val() == "I20"){
				$("#category_tr").show();
				$("#category_i30_tr").hide();
			}else{
				$("#category_i30_tr").show();
				$("#category_tr").hide();
			}
		}else if($(this).val() == "SCHEDULE"){
			$("#scheduletable").attr("style", "display:block");
			if($("#category_gb").val() == "I20"){
				$("#schedule_tr").show();
				$("#schedule_i30_tr").hide();
			}else{
				$("#schedule_i30_tr").show();
				$("#schedule_tr").hide();
			}
		}else if($(this).val() == "WISH" || $(this).val() == "CONT_LINK" || $(this).val() == "AHOME_04" || $(this).val() == "AHOME_05"){
			$('#category_des').val('');
			$('#category_code').val('');
			$('#categorytr').show();
		}else if($(this).val() == "BANNER"){
			$('#tbanner').val('');
			$('#hbanner').val('');
			$('#bannertr').show();
		}else if($(this).val() == "CA_RANK"){
			$("#userselcategorystring").show();
			if($("#category_gb").val() == "I20"){
				$("#catranktable").show();
				$("#catranktable_i30").hide();
			}else{
				$("#catranktable_i30").show();
				$("#catranktable").hide();
			}
		}else if($(this).val() == "VLIST"){
			$("#imcscontents_code").val('');
			$("#imcscontents_des").val('');
			$("#imcscontentstr").show();
		}else if($(this).val() == "HLIST"){
			$("#hotvod_code").val('');
			$("#hotvod_des").val('');
			$("#hotvodtr").show();
		}else if($(this).val() == "GALLERY"){
			$("#gallery_code").val('');
			$('#gallerytr').show();			
		}else if($(this).val() == "HOTVOD"){
			$("#hotvodiptv_code").val('');
			$("#hotvodiptv_des").val('');
			$("#hotvodiptvtr").show();
		/*2020-06-01 추가 : 유재덕 - 최근이용한메뉴 / 이런메뉴 어때요 */
		}
		PageTypeBtnOpenCheck();
	});
	
	$("#category_gb").change(function(){
		var category_type = $("#gbn_select").val();
		if("CAT_MAP" == category_type || "BOOKTV" == category_type || "ENG_PRESCH" == category_type || "BRAND" == category_type){
			if($(this).val() == "I20"){
				$("#category_tr").show();
				$("#category_i30_tr").hide();
			}else{
				$("#category_i30_tr").show();
				$("#category_tr").hide();
			}
		}else if("SCHEDULE" == category_type){
			if($(this).val() == "I20"){
				$("#schedule_tr").show();
				$("#schedule_i30_tr").hide();
			}else{
				$("#schedule_i30_tr").show();
				$("#schedule_tr").hide();
			}
		}else if("VOD" == category_type){
			vodcodesarr.remove(0, vodcodesarr.length);
			
			if($(this).val() == "I20"){
				$("#bestvodtable").show();
				$("#bestvodtable_i30").hide();
				$("input[name='vodchk_i30']").attr("checked", false);
			}else{
				$("#bestvodtable_i30").show();
				$("#bestvodtable").hide();
				$("input[name='vodchk']").attr("checked", false);
			}
			
			$("#userselcategorystringtd").html('');
		}else if("CA_RANK" == category_type){
			vodcodesarr.remove(0, vodcodesarr.length);
			
			if($(this).val() == "I20"){
				$("#catranktable").show();
				$("#catranktable_i30").hide();
				$("input[name='catchk_i30']").attr("checked", false);
			}else{
				$("#catranktable_i30").show();
				$("#catranktable").hide();
				$("input[name='catchk']").attr("checked", false);
			}
			
			$("#userselcategorystringtd").html('');
		}else if(category_type == "WISH" ||category_type == "CONT_LINK" || category_type == "AHOME_04" || category_type == "AHOME_05"){
			$('#category_des').val('');
			$('#category_code').val('');
		}
		
		
		$("input[name='category_album_chk']").each(function(){
			$(this).parent().parent().remove();
		});
		select_bind();
	});
	
	$("#regbtn").click(function(){
		var abtest_yn = $(':input:radio[name="abtest_yn"]:checked').val();

		if($('#sub_cnt').val() != ''){
			if(isNaN($('#sub_cnt').val())){
				alert('하위개수는 숫자로 입력해주세요');
				return;
			}
		}else if(checkByteReturn($('#page_code'), "100") == "false"){
			alert("지면 코드는 100바이트까지 등록 가능합니다");
			return;
		}
		
		var category_type = $('#gbn_select').val();
		var cateTypeList = '${cateTypeList}';
		cateTypeList = cateTypeList.split('\|');
		
		for(var i=0;i<cateTypeList.length;i++){
			if(category_type == cateTypeList[i]){
				if($('#sub_cnt').val() != ''){
					
					var page_code = $('#page_code').val();
					var pageCodeSet = '';
					if(page_code.split('\^').length > 1){
						pageCodeSet = page_code.split('\^')[0] + '^' + $('#sub_cnt').val();
					}else{
						pageCodeSet =  page_code + '^' + $('#sub_cnt').val();
					}
					$('#page_code').val(pageCodeSet);
				}
			}
		}

		//20220117 월정액상품 추가
        var productCodeInfoList = $('#link_detail').val().split("\f^");
        var productCode="";
        
        if(productCodeInfoList != null && productCodeInfoList != ''){
            
            for(var i =0; i<productCodeInfoList.length;i++){
                
                var productCodeInfo = productCodeInfoList[i].split("\b^");
                
                if(i==0){
                    productCode = productCodeInfo[0];
                }else{
                    productCode += "|" + productCodeInfo[0];
                }
                
            }
        }
        $('#product_code').val(productCode);



        //20220117 월정액상품 추가(비노출)
        var productCodeInfoList2 = $('#link_detail2').val().split("\f^");
        var productCode2="";
        
        if(productCodeInfoList2 != null && productCodeInfoList2 != ''){
            
            for(var i =0; i<productCodeInfoList2.length;i++){
                
                var productCodeInfo2 = productCodeInfoList2[i].split("\b^");
                
                if(i==0){
                    productCode2 = productCodeInfo2[0];
                }else{
                    productCode2 += "|" + productCodeInfo2[0];
                }
                
            }
        }
        $('#product_code2').val(productCode2);

       
        if(productCode2 != ''){
            var productCodeArr = productCode.split("|");
            var productCodeArr2 = productCode2.split("|");
            for(var i = 0 ; i < productCodeArr2.length; i++){
                if(productCodeArr.indexOf(productCodeArr2[i]) > -1){
                    alert("월정액상품과 월정액상품(비노출) 동일상품이 존재합니다.");
                    return;
                }
            }
        }






        var terminal_arr = "";
        if($("#terminal_all").is(":checked")){
            $('#terminal_all_yn').val("Y");
        }else{
            $('#terminal_all_yn').val("N");
            if($("input[name=terminal_list]").length<=0){ 
                alert("노출단말을 선택해 주세요");
                return false;
            }else{
                var values = [];
                $("input[name='terminal_list']").each(function() {
                    values.push($(this).val());
                });
                terminal_arr = values.join();
                $('#terminal_arr').val(terminal_arr);
            }
        }
		
		regcategory();
	});
	
	$("#allvodchk").click(function(){
		var chkallchecked = false;
		
		vodcodesarr.remove(0, vodcodesarr.length);
		
		if($("#allvodchk").is(":checked")){
			chkallchecked = true;
			$("input[name='vodchk']").each(function(){
				vodcodesarr.push($(this).val());
			});
		}
		$("#userselcategorystringtd").html(makeVodTitleString("N"));
		
		$("input[name='vodchk']").attr("checked", chkallchecked);
	});
	
	$("#allvodchk_i30").click(function(){
		var chkallchecked = false;
		
		vodcodesarr.remove(0, vodcodesarr.length);
		
		if($("#allvodchk_i30").is(":checked")){
			chkallchecked = true;
			$("input[name='vodchk_i30']").each(function(){
				vodcodesarr.push($(this).val());
			});
		}
		$("#userselcategorystringtd").html(makeVodTitleString("N"));
		
		$("input[name='vodchk_i30']").attr("checked", chkallchecked);
	});
	$("#allcatchk").click(function(){
		var chkallchecked = false;
		
		vodcodesarr.remove(0, vodcodesarr.length);
		
		if($("#allcatchk").is(":checked")){
			chkallchecked = true;
			$("input[name='catchk']").each(function(){
				vodcodesarr.push($(this).val());
			});
		}
		$("#userselcategorystringtd").html(makeVodTitleString("Y"));
		
		$("input[name='catchk']").attr("checked", chkallchecked);
	});
	
	$("#allcatchk_i30").click(function(){
		var chkallchecked = false;
		
		vodcodesarr.remove(0, vodcodesarr.length);
		
		if($("#allcatchk_i30").is(":checked")){
			chkallchecked = true;
			$("input[name='catchk_i30']").each(function(){
				vodcodesarr.push($(this).val());
			});
		}
		$("#userselcategorystringtd").html(makeVodTitleString("Y"));
		
		$("input[name='catchk_i30']").attr("checked", chkallchecked);
	});
	
	$("#category_album_allchk").click(function(){
		var chkallchecked = false;
		
		if($("#category_album_allchk").is(":checked")){
			chkallchecked = true;
		}
		
		$("input[name='category_album_chk']").attr("checked", chkallchecked);
		
	});
	
	$("#categoryPopBtn").click(function(){
		var panelId = $('#panel_id').val();
		url = '<%=webRoot%>/admin/commonMng/getOnceCategoryAlbum.do?categoryId=VC&hiddenName=page_code&textName=&textHtml=choiceData&type=NSC&series=Y&categoryYn=Y&panelId=' + panelId;
        category_window = window.open(url, 'getOnceCategoryAlbum', 'width=600,height=330,scrollbars=yes');
	});
	
	$("#selCategorybtn").click(function(){
		var panel_id = $("#panel_id").val();
	    var category_gb = $("#category_gb").val();
		
		var url = "<%=webRoot%>/admin/mainpanel/getPageCategoryList.do?panel_id=" + panel_id + "&category_id=VC&category_gb=" + category_gb + "&category_level=1";
		window.open(url, "getPageCategoryList", "left=500,width=300,height=100,scrollbars=yes");
	});
	
	$("#selBannerbtn").click(function(){
		var url = "<%=webRoot%>/admin/commonMng/getBannerMasterPop.do?callbak=getBannerMasterPopCallbak";
		window.open(url, "getBannerMasterPop", "width=600,height=600,left=100,top=50,scrollbars=yes");
	});
	
	$("#searchRepsContentBtn").click(function(){
		
		var closeCheck = setInterval(function(){
			if($("#choiceCts").val() != ''){
				
				var repsContentArray = $("#choiceCts").val().split('||');
				
				// console.log(repsContentArray);
				// 선택
				// 0 : 카테고리ID
				// 1 : 앨범ID
				// 2 : 시리즈YN
				// 3 : 시리즈no
				// 검색
				// 0 : 카테고리ID
				// 1 : 앨범ID
				// 2 : 카테고리구분(I20/I30)
				// 3 : 시리즈YN
				// 4 : 시리즈no
				
				if(repsContentArray.length == 4 || repsContentArray.length == 5) {
					$('#reps_category_id').val(repsContentArray[0]);
					$('#reps_album_id').val(repsContentArray[1]);
				} else {
					alert("앨범 정보를 가져오지 못했습니다.\n해당 앨범이 현재 편성 불가능하거나 네트워크 문제일 수 있습니다.");
				}
				$("#choiceCts").val('');
				clearInterval(closeCheck);
			}
		}, 1000);
		
    	var url = '${pageContext.request.contextPath}/admin/commonMng/getOnceCategoryAlbum.do?categoryId=VC&hiddenName=choiceCts&textName=reps_album_name&type=${category_gb}&isAds=Y';
        category_window = window.open(url, 'getCategoryAlbumList', 'width=800,height=600,scrollbars=yes');
	});
	
	$("#selImcsContentsbtn").click(function(){
		var url = "<%=webRoot%>/admin/mainpanel/getPageCategoryList.do?category_id=VC&isImcsContents=Y&category_gb="+$("#category_gb").val();
		window.open(url, "getPageCategoryList", "left=500,width=800,height=200,scrollbars=yes");
	});
	
	$("#selHotVodbtn").click(function(){		
		var catetype = "1"; // catetype : 1 일반 , catetype : 2 카테고리 data
		var contentType = "";// H 일경우 조회시 하이라이트 형식만 조회한다.값이 없을 경우 하이라이트가 아닌것만 조회된다.
		
		var url = "<%=webRoot%>/admin/commonMng/getMimsCategoryListPop.do?callbak=getMimsCategoryListPopCallbak&catetype=" + catetype + "&contentType=" + contentType;
		window.open(url, "getImcsCategoryList", "left=100,top=50,width=800,height=200,scrollbars=yes");
	});
	
	$("#selGallerybtn").click(function(){
		var url = "<%=webRoot%>/admin/gallery/choiceGalleryListPop.do?gallery_id=&callbak=getGalleryListPopCallbak";
		window.open(url, "getGalleryListPop", "width=600,height=600,left=100,top=50,scrollbars=yes");
	});
	
	$("#selHotVodIptvbtn").click(function(){		
		var catetype = "1"; // catetype : 1 일반 , catetype : 2 카테고리 data
		var contentType = "";// H 일경우 조회시 하이라이트 형식만 조회한다.값이 없을 경우 하이라이트가 아닌것만 조회된다.
		
		var url = "<%=webRoot%>/admin/commonMng/getMimsIptvCategoryListPop.do?callbak=getMimsIptvCategoryListPopCallbak&catetype=" + catetype + "&contentType=" + contentType;
		window.open(url, "getImcsCategoryList", "left=100,top=50,width=800,height=200,scrollbars=yes");
	});
	
	init();
	
	frm = $("#regfrm");
	
	frm.ajaxForm({
		dataType:  "text", 
		beforeSubmit : typeBefore,
        success : function(myresponse){
        	//alert(myresponse);
        	mysuccess(myresponse);
        },
        error : function(){
        	alert("등록에 실패했습니다");
        }
	});
	
	frm.submit(function(){
    	return false;
    });
	
	
	//지면 타입이 PAPAGO 일때 선택한 언어 순서대로 지면 코드에 추가 삭제
	$("input[name='papago_language']").click(function(){
		
		if($(this).is(":checked")){
			papagoList.push($(this).val());
		}else{
			var idx = papagoList.indexOf($(this).val());
			if(idx > -1){
				papagoList.splice(idx, 1);
			}
		}
			
		$("#page_code").val(papagoList.join('_'));
	});


	//노출단말 버튼
    $("#termPopup").click(function(){
        var scr_tp = '${scr_tp}';
        //HDTV 관리의 단말 팝업을 같이 쓴다.
        termCheck=window.open("<%=webRoot%>/admin/noti/getTerm.do?scr_tp=${scr_tp}", "termCheck", "width=500,height=700,scrollbars=yes" );
        termCheck.opener=self;
    });

    $("#terminal_all").click(function (e) {             
        if($("#terminal_all").is(":checked")){
            $("#terminal_div").html("");
            $("#termPopup").attr("style","display:none");
        }else{          
            $("#termPopup").attr("style","display:block");  
        }
    });
	
});

function mysuccess(myresponse){
	// json이 아닌 text로 받으면 결과가 <PRE> </PRE> 문자열이 앞뒤에 붙으므로 이에 대한 제거를 한다
//	var tempval = myresponse.replace('<PRE>', '');
//	tempval = tempval.replace('</PRE>', '');
//	tempval = tempval.replace('<pre>', '');
//	tempval = tempval.replace('</pre>', '');
//	var tmps = tempval.split("!@");				// 결과 코드와 결과 메시지가 !@로 결합되어 있어서 이에 대한 분리작업을 진행한다
	
	var tempval = myresponse.replace(/"/gi, '');
	tempval = tempval.replace('result:', '');
	tempval = tempval.replace('{{', '');
	tempval = tempval.replace('}}', '');
	tempval = tempval.replace('flag:', '');
	tempval = tempval.replace('message:', '');
	var tmps = tempval.split(",");
	
	var flag = tmps[0];
 	var message = tmps[1];
 	
 	if(flag == "0000"){						// 정상적으로 처리된 경우
 		alert("지면 데이터가 설정되었습니다");
 		opener.location.reload();
 		self.close();
 	}else if(flag == "NOT FOUND SCHEDULE_NM"){
 		alert("편성명은 필수로 들어가야 합니다");
 		$("#schedule_name").focus();
 	}else if(flag == "SCHEDULE_NM LENGTH"){
 		alert("편성명은 100자 이내이어야 합니다");
 		$("#schedule_name").focus();
 	}else if(flag == "NOT FOUND ALBUM_ID"){
 		alert("앨범이 입력되지 않았습니다");
 	}else if(flag == "NOT FOUND CATEGORY_ID"){
 		alert("카테고리가 입력되지 않았습니다");
 	}else if(flag == "FTP ERROR"){
 		alert("이미지 업로드 오류입니다\n이미지를 다시 등록해주세요");
 	}else{
 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
 	}
}

function init(){
	var category_id="${category_id}";
	var category_type="${category_type}";
	var category_gb="${category_gb}";
	
	all_hide();
	
	if(category_type == "LIVE"){
		
	}
	
	if(category_type == "VOD"){
		$("#userselcategorystring").show();
		
		var table_name = '';
		var chk_name = '';
		if(category_gb == "I20"){
			table_name = 'bestvodtable';
			chk_name = 'vodchk';
		}else{
			table_name = 'bestvodtable_i30';
			chk_name = 'vodchk_i30';
		}
		$("#"+table_name).show();
		
		if(category_id != ""){
			var tmpcodes = category_id.split("||");
			var tmplength = tmpcodes.length;
			
			for(var i = 0; i < tmplength; i++){
				vodcodesarr.push(tmpcodes[i]);
			}
			
			$("input[name='"+chk_name+"']").each(function(){
				for(var i=0; i < tmplength; i++){
					if($(this).val() == tmpcodes[i]){
						$(this).attr("checked", "checked");			
					}
				}
			});
			
			// alert("result1 : " + makeVodTitleString());
			$("#userselcategorystringtd").html(makeVodTitleString("N"));
		}
	}
	
	if(category_type == "CA_RANK"){
		$("#userselcategorystring").show();
		
		var table_name = '';
		var chk_name = '';
		if(category_gb == "I20"){
			table_name = 'catranktable';
			chk_name = 'catchk';
		}else{
			table_name = 'catranktable_i30';
			chk_name = 'catchk_i30';
		}
		$("#"+table_name).show();
		
		if(category_id != ""){
			var tmpcodes = category_id.split("||");
			var tmplength = tmpcodes.length;
			
			for(var i = 0; i < tmplength; i++){
				vodcodesarr.push(tmpcodes[i]);
			}
			
			$("input[name='"+chk_name+"']").each(function(){
				for(var i=0; i < tmplength; i++){
					if($(this).val() == tmpcodes[i]){
						$(this).attr("checked", "checked");			
					}
				}
			});
			
			// alert("result1 : " + makeVodTitleString());
			$("#userselcategorystringtd").html(makeVodTitleString("Y"));
		}
	}
		
	if(category_type == "CAT_MAP" || category_type == "BOOKTV" || category_type == "ENG_PRESCH" || category_type == "BRAND"){
		$("#categorytable").attr("style", "display:block");
		if(category_gb != "I30"){
			$("#category_tr").show();
			$("#category_i30_tr").hide();
		}else{
			$("#category_i30_tr").show();
			$("#category_tr").hide();
		}
	}
	
	if(category_type == "SCHEDULE"){
		$("#scheduletable").attr("style", "display:block");
		
		if($("#category_gb").val() == "I20"){
			$("#schedule_tr").show();
			$("#schedule_i30_tr").hide();
		}else{
			$("#schedule_tr").hide();
			$("#schedule_i30_tr").show();
		}
	}
	
	if(category_type == "WISH" || category_type == "CONT_LINK" || category_type == "AHOME_04" || category_type == "AHOME_05"){
		$('#categorytr').show();
	}
	
	if(category_type == "BANNER"){
		$('#bannertr').show();
	}
	
	if(category_type == "VLIST"){
		$("#imcscontentstr").show();
	}
	
	if(category_type == "HLIST"){
		$("#hotvodtr").show();
	}
	
	if(category_type == "GALLERY"){
		$("#gallerytr").show();
	}
	
	if(category_type == "HOTVOD"){
		$("#hotvodiptvtr").show();
	}
	
	var page_code = '${mainresult.page_code}';
	var cateTypeList = '${cateTypeList}';
	var isCateType = false;
	cateTypeList = cateTypeList.split('\|');
	for(var i=0;i<cateTypeList.length;i++){
		if(category_type == cateTypeList[i]){
			$('#categoryPopBtn').removeAttr("style");
			$('#subcnt_tr').removeAttr("style");
			$('#page_code').attr('readonly', true);
			var splitArr = page_code.split("\^");
			
			if(splitArr.length > 1){
				$('#page_code').val(splitArr[0]);
				$('#sub_cnt').val(splitArr[1]);
			}
			isCateType = true;
		}
	}
	
	var page_type = "${mainresult.page_type}";
	if(page_type == "PAPAGO" && !isCateType){
		var pageCodeArr = page_code.split('_');
		$("#papagoLanguageList").show();		
		$("input[name=papago_language]").each(function(){
			for(var i=0; i<pageCodeArr.length;i++){
				if($(this).val() == pageCodeArr[i]){
					$(this).attr("checked", "checked");
					papagoList.push($(this).val());
				}
			}
		});
	}
	
	// Best VOD 리스트의 체크 박스들에 클릭 이벤트를 걸어준다
	$("input[name='vodchk']").click(function(){
		var checkval = $(this).val();
		var checked = $(this).attr("checked");
		if(checked == "checked"){			// 배열의 끝에 추가
			vodcodesarr.push(checkval);
		}else{								// 배열에서 삭제
			var arrlength = vodcodesarr.length;
			for(var i=0; i < arrlength; i++){
				if(vodcodesarr[i] == checkval){
					vodcodesarr.remove(i);
				}
			}
		}
		
		// alert("result2 : " + makeVodTitleString());
		$("#userselcategorystringtd").html(makeVodTitleString("N"));
	});
	
	$("input[name='vodchk_i30']").click(function(){
		var checkval = $(this).val();
		var checked = $(this).attr("checked");
		if(checked == "checked"){			// 배열의 끝에 추가
			vodcodesarr.push(checkval);
		}else{								// 배열에서 삭제
			var arrlength = vodcodesarr.length;
			for(var i=0; i < arrlength; i++){
				if(vodcodesarr[i] == checkval){
					vodcodesarr.remove(i);
				}
			}
		}
		
		// alert("result2 : " + makeVodTitleString());
		$("#userselcategorystringtd").html(makeVodTitleString("N"));
	});
	
	$("input[name='catchk']").click(function(){
		var checkval = $(this).val();
		var checked = $(this).attr("checked");
		if(checked == "checked"){			// 배열의 끝에 추가
			vodcodesarr.push(checkval);
		}else{								// 배열에서 삭제
			var arrlength = vodcodesarr.length;
			for(var i=0; i < arrlength; i++){
				if(vodcodesarr[i] == checkval){
					vodcodesarr.remove(i);
				}
			}
		}
		
		// alert("result2 : " + makeVodTitleString());
		$("#userselcategorystringtd").html(makeVodTitleString("Y"));
	});
	
	$("input[name='catchk_i30']").click(function(){
		var checkval = $(this).val();
		var checked = $(this).attr("checked");
		if(checked == "checked"){			// 배열의 끝에 추가
			vodcodesarr.push(checkval);
		}else{								// 배열에서 삭제
			var arrlength = vodcodesarr.length;
			for(var i=0; i < arrlength; i++){
				if(vodcodesarr[i] == checkval){
					vodcodesarr.remove(i);
				}
			}
		}
		
		// alert("result2 : " + makeVodTitleString());
		$("#userselcategorystringtd").html(makeVodTitleString("Y"));
	});
}

function fnRecom(obj){
	$("input[name='recomchk']:checked").each(function(){
			if(obj.value!=this.value) this.checked =false;
	});
}

// 사용자가 선택한 Ranking 제목을 사용자가 클릭한 순서대로 결합하여 만들어주는 함수
function makeVodTitleString(catrank_yn){
	var result = "";
	var chk_name = "";
	
	if(catrank_yn == "Y"){
		if($("#category_gb").val() == "I20"){
			chk_name = "catchk";
		}else{
			chk_name = "catchk_i30";
		}
	}else{
		if($("#category_gb").val() == "I20"){
			chk_name = "vodchk";
		}else{
			chk_name = "vodchk_i30";
		}
	}
	
	
	for(var i = 0; i < vodcodesarr.length; i++){
		var code = vodcodesarr[i];
		$("input[name='"+chk_name+"']").each(function(){
			if($(this).val() == code){
				var text = $(this).parent().next().text();
				if(result == ""){
					result = "<b>" + text + "</b>";
				}else{
					result += " + <b>" + text + "</b>";
				}
			}
		});
	}
	
	return result;
}

function select_bind(){
	var sel_name = "";
	var tr_name = "";
	var td_name = "";
	
	if($("#category_gb").val() == "I20"){
		sel_name = "category_select";
		tr_name = "category_tr";
		td_name = "category_td";
	}else{
		sel_name = "category_i30_select";
		tr_name = "category_i30_tr";
		td_name = "category_i30_td";
	}

	$("select[name='" +sel_name+ "']").unbind("change");
	
	$("select[name='"+sel_name+"']").bind("change", function(){
        var panel_id = '${param.panel_id}';
		var category_id = $(this).val();
		var thissel = this;
		
		/*
		if($(thissel).val() == "---"){
			return;
		}
		*/
		
		// 현재 select 태그의 부모인 td의 이후에 나오는 모든 td를 구한다
 		var nexttd = $(thissel).parent().nextAll("td"); 
 		var length = nexttd.length;
 		for(var i = length-1; i >= 0; i--){
 			$(nexttd[i]).remove();
 		}
 		
 		
		if(category_id == "---"){		// 선택해주세요를 선택한 경우
			
		}else{
			$.post("<%=webRoot%>/admin/mainpanel/getPageCategoryList.do",
					 {panel_id : panel_id, category_id : category_id, category_gb : $("#category_gb").val()},
					  function(data) {
						 	var html = [], h=-1;
							// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
						 	if(data.length == 0){
						 		html[++h] = "<td>\n";
							 	html[++h] = "<span class=\"button small blue\" id=\"setcategorybtn\" onclick=\"setcategory()\">설정</span>";
							 	html[++h] = "<span class=\"button small blue\" id=\"delcategorybtn\" onclick=\"delcategory()\">삭제</span>";
							 	html[++h] = "</td>\n";
						 	}else{
						 		html[++h] = "<td name=\""+td_name+"\" valign=\"top\">\n";
						 		html[++h] = "<select name=\""+sel_name+"\">\n";
						 		html[++h] = "<option value=\"---\" selected>선택해주세요</option>\n";
						 		
						 		for(var i=0 ; i < data.length; i++){
						 			html[++h] = "<option value=\"" + data[i].category_id + "\">" + data[i].category_name + "</option>\n";	
						 		}
						 		
						 		html[++h] = "</select>\n";
							 	html[++h] = "</td>\n";
							 	html[++h] = "<td>\n";
							 	html[++h] = "<span class=\"button small blue\" id=\"setcategorybtn\" onclick=\"setcategory()\">설정</span>";
							 	html[++h] = "<span class=\"button small blue\" id=\"delcategorybtn\" onclick=\"delcategory()\">삭제</span>";
							 	html[++h] = "</td>\n";
							 	
							 	
							 	
						 	}
						 	$("#" + tr_name).append(html.join(''));
						 	
						 	
						 	select_bind();
					  },
					  "json"
		    );
		}
	 	
	 	
		
	});
	
}

function all_hide(){
	$("#userselcategorystring").hide();
	$("#bestvodtable").hide();
	$("#bestvodtable_i30").hide();
	$("#catranktable").hide();
	$("#catranktable_i30").hide();
	$("#categorytable").hide();
	$("#scheduletable").hide();
	$("#categoryPopBtn").hide();
	$('#subcnt_tr').hide();
	$('#categorytr').hide();
	$('#bannertr').hide();
	$("#papagoLanguageList").hide();
	$("#imcscontentstr").hide();
	$("#gallerytr").hide();
	$("#hotvodtr").hide();
	$("#hotvodiptvtr").hide();

}

function setcategory(){
	var sel_name = "";
	
	if($("#category_gb").val() == "I20"){
		sel_name = "category_select";
	}else{
		sel_name = "category_i30_select";
	}
	
	var optionstr = "";
	// 카테고리 select 태그들을 전부 가져온다
	var categorysels = $("select[name='"+sel_name+"']");
	var bktext = "";
	var bkval = "";
	var category_text = "";
	var category_val = "";
	
	var category_length = categorysels.length;
	
	
	
	for(var i = 0; i < category_length; i++){
		var seltext = $(categorysels[i]).children("option:selected").text();
		var selval = $(categorysels[i]).val();
		
		
		if(selval == "---"){
			if(category_length == 1){
				
			}else{
				category_text = bktext;
				category_val = bkval;
				// optionstr = "<option value=\"" + category_val + "\">" + category_text + "</option>";
				
				
				
				optionstr = "<tr>\n";
				optionstr += "<td><input type=\"checkbox\" name=\"category_album_chk\" value=\"\"></td>\n";
				optionstr += "<td style=\"text-align : left;\">\n";
				optionstr += "	<input type=\"text\" name=\"category_album_category_text\" value=\"" + category_text + "\" readonly>\n";
				optionstr += "	<input type=\"hidden\" name=\"category_album_category_id\" value=\"" + category_val + "\">\n";
				optionstr += "</td>\n";
				optionstr += "<td style=\"text-align : left;\"><input type=\"text\" name=\"category_album_album_cnt\"></td>\n";
				optionstr += "</tr>\n";
				
				
				
				
				break;
			}
		}else{
			if(category_length == 1){
				category_text = seltext;
				category_val = selval;
				// optionstr = "<option value=\"" + category_val + "\">" + category_text + "</option>";
				optionstr = "<tr>\n";
				optionstr += "<td><input type=\"checkbox\" name=\"category_album_chk\" value=\"\"></td>\n";
				optionstr += "<td style=\"text-align : left;\">\n";
				optionstr += "	<input type=\"text\" name=\"category_album_category_text\" value=\"" + category_text + "\" readonly>\n";
				optionstr += "	<input type=\"hidden\" name=\"category_album_category_id\" value=\"" + category_val + "\">\n";
				optionstr += "</td>\n";
				optionstr += "<td style=\"text-align : left;\"><input type=\"text\" name=\"category_album_album_cnt\"></td>\n";
				optionstr += "</tr>\n";
				break;
			}else{
				if(i == category_length - 1){
					category_text = seltext;
					category_val = selval;
					// optionstr = "<option value=\"" + category_val + "\">" + category_text + "</option>\n";
					optionstr = "<tr>\n";
					optionstr += "<td><input type=\"checkbox\" name=\"category_album_chk\" value=\"\"></td>\n";
					optionstr += "<td style=\"text-align : left;\">\n";
					optionstr += "	<input type=\"text\" name=\"category_album_category_text\" value=\"" + category_text + "\" readonly>\n";
					optionstr += "	<input type=\"hidden\" name=\"category_album_category_id\" value=\"" + category_val + "\">\n";
					optionstr += "</td>\n";
					optionstr += "<td style=\"text-align : left;\"><input type=\"text\" name=\"category_album_album_cnt\"></td>\n";
					optionstr += "</tr>\n";
				}else{
					bktext = seltext;
					bkval = selval;
				}
			}
			
			
		}
	}
	// alert("optionstr : " + optionstr);
	if(optionstr != ""){
		// 추가하고자 하는 것이 기존에 있는지를 확인한다
		var isexist = false;
		$("input[name='category_album_category_id']").each(function(){
			if($(this).val() == category_val){
				isexist = true;
			}
		});
		
		/*
		var size = $("#categorylist option").size();
		if(size != 0){
			$("#categorylist option").each(function(){
				if($(this).val() == category_val){
					isexist = true;
				}
			});
		}
		*/
		if(isexist == true){
			alert("추가하고자 하는 카테고리가 이미 설정되어 있습니다");
		}else{
			// $("#categorylist").append(optionstr);
			$("#category_album_tbl").append(optionstr);
		}
	}
}

function delcategory(){
	/*
	var idx = $("#categorylist option").index($("#categorylist option:selected"));
	if(idx == -1){			// 선택한 항목이 없으면
		alert("삭제하고자 하는 카테고리를 선택해주세요");
	}else{					// 선택한 항목이 있으면
		var seloption = $("#categorylist option").eq(idx);
		$(seloption).remove();
	}
	*/
	var checkeditems = $("input[name='category_album_chk']:checked");
	var checkeditemslength = checkeditems.length;
	
	if(checkeditemslength == 0){
		alert("삭제할 항목을 선택해주세요");
	}else{
		$("input[name='category_album_chk']").each(function(){
			if($(this).is(":checked")){
				$(this).parent().parent().remove();
			}
		});
	}
}

function typeBefore(){
	var panel_id = $("#panel_id").val();
	var title_id = $("#title_id").val();
	var isUpdate = $("#isUpdate").val();
	var category_id = "";
	var album_cnt = "";
	var gbnselect = $("#gbn_select").val();
	var smartUXManager = $("#smartUXManager").val();
	var page_code = $("#page_code").val();
	var page_type = $("#page_type").val();
	
	if(page_type != "ETC" && page_type != "LIVE" && page_type != "AHOME" && page_type != "APP"){
		if(page_code == ""){
			alert("지면 코드를 입력해주세요");
			$("#page_code").focus();
			return false;
		}
	}
	
	// 새로이 설정하는 작업이고 이미지 파일이 업로드 되지 않았을 경우
	// var imgval = $(this).val();
	/*
	if((isUpdate == "N") && ($("#bg_img_file").val() == "")){
		alert("업로드하고자 하는 이미지 파일을 설정해주세요");
		$("#bg_img_file").focus();
		return false;
	}
	*/
	
	// 등록작업일때의 이미지 파일 체크
	//20180913 이미지 관련 등록 및 삭제는 하위지면 추가와 지면 상세조회에서만 등록 및 수정
	/* if((isUpdate == "N")){
		if($("#bg_img_file").val() == ""){
			alert("업로드하고자 하는 이미지 파일을 설정해주세요");
			$("#bg_img_file").focus();
			return false;
		}else{
			var imgval = $("#bg_img_file").val();
			if(!imgval.toLowerCase().match(/.(gif|jpg|png|jpeg|bmp|zip)$/i)){
				alert("업로드하고자 하는 파일을 이미지 파일(gif,jpg,png,jpeg,bmp,zip)로 설정해주세요");
				$("#bg_img_file").focus();
				return false;
			}
		}
		
		if($("#bg_img_file2").val() == ""){
			alert("업로드하고자 하는 이미지 파일을 설정해주세요");
			$("#bg_img_file2").focus();
			return false;
		}else{
			var imgval = $("#bg_img_file2").val();
			if(!imgval.toLowerCase().match(/.(gif|jpg|png|jpeg|bmp|zip)$/i)){
				alert("업로드하고자 하는 파일을 이미지 파일(gif,jpg,png,jpeg,bmp,zip)로 설정해주세요");
				$("#bg_img_file2").focus();
				return false;
			}
		}
	}
	
	// 수정작업일때의 이미지 파일 체크
	if((isUpdate == "Y")){
		if($("#bg_img_file").val() != ""){
			
			var imgval = $("#bg_img_file").val();
			if(!imgval.toLowerCase().match(/.(gif|jpg|png|jpeg|bmp|zip)$/i)){
				alert("업로드하고자 하는 파일을 이미지 파일(gif,jpg,png,jpeg,bmp,zip)로 설정해주세요");
				$("#bg_img_file").focus();
				return false;
			}
		}
		
		if($("#bg_img_file2").val() != ""){
			
			var imgval = $("#bg_img_file2").val();
			if(!imgval.toLowerCase().match(/.(gif|jpg|png|jpeg|bmp|zip)$/i)){
				alert("업로드하고자 하는 파일을 이미지 파일(gif,jpg,png,jpeg,bmp,zip)로 설정해주세요");
				$("#bg_img_file2").focus();
				return false;
			}
		}
	}
	 */
	if(gbnselect == "LIVE"){
		
	}else if(gbnselect == "VOD"){
		var checkeditems;
		if($("#category_gb").val() == "I20"){
			checkeditems = $("input[name='vodchk']:checked");
		}else{
			checkeditems = $("input[name='vodchk_i30']:checked");
		}
		var checkeditemslength = checkeditems.length;
		
		if(checkeditemslength == 0){
			alert("설정할 VOD 항목을 선택해주세요");
			return false;
		}else{
			if(vodcodesarr.length == 0){
				alert("설정할 VOD 항목을 선택해주세요");
				return false;
			}else{
				category_id = vodcodesarr.join("||");
			}
			
		}
	}else if(gbnselect == "CA_RANK"){
		var checkeditems;
		if($("#category_gb").val() == "I20"){
			checkeditems = $("input[name='catchk']:checked");
		}else{
			checkeditems = $("input[name='catchk_i30']:checked");
		}
		var checkeditemslength = checkeditems.length;
		
		if(checkeditemslength == 0){
			alert("설정할 랭킹 항목을 선택해주세요");
			return false;
		}else{
			if(vodcodesarr.length == 0){
				alert("설정할 랭킹 항목을 선택해주세요");
				return false;
			}else{
				category_id = vodcodesarr.join("||");
			}
			
		}
	}else if(gbnselect == "CAT_MAP" || gbnselect == "BOOKTV" || gbnselect == "ENG_PRESCH" || gbnselect == "BRAND"){
		
		
		var size = $("input[name='category_album_category_id']").length;
		if(size == 0){
			alert("카테고리를 먼저 등록해주세요");
			return false;
		}else{
			var category_album_cnts = $("input[name='category_album_album_cnt']");
			for(var i=0; i < size; i++){
				if($(category_album_cnts[i]).val() == ""){
					alert("앨범 갯수를 입력해주세요");
					$(category_album_cnts[i]).focus();
					return false;
				}
				
				if(isNaN($(category_album_cnts[i]).val())){
					alert("앨범 갯수를 숫자로 입력해주세요");
					$(category_album_cnts[i]).focus();
					return false;
				}
			}
		}
		
		// Validation에 통과했으면
		$("input[name='category_album_category_id']").each(function(){
			if(category_id == ""){
				category_id = $(this).val();
			}else{
				category_id = category_id + "||" + $(this).val();
			}
		});
		
		$("input[name='category_album_album_cnt']").each(function(){
			if(album_cnt == ""){
				album_cnt = $(this).val();
			}else{
				album_cnt = album_cnt + "||" + $(this).val();
			}
		});
		
		
		
	}else if(gbnselect == "SCHEDULE"){
		var selval = $("#schedule_select").val();
		
		if($("#category_gb").val() == "I20"){
			selval = $("#schedule_select").val();
		}else{
			selval = $("#schedule_i30_select").val();
		}
		
		if(selval == "---"){
			alert("자체편성을 선택해주세요");
			return false;
		}else{
			category_id = selval;
		}
	}else if(gbnselect == "WISH" || gbnselect == "CONT_LINK" || gbnselect == "AHOME_04" || gbnselect == "AHOME_05"){
		category_id = $('#category_code').val();
		
		if(category_id == ""){
			alert("카테고리를 선택해주세요.");
			return false;
		}
	}else if(gbnselect == "BANNER"){
		category_id = $('#hbanner').val();
		
		if(category_id == ""){
			alert("배너를 선택해주세요.");
			return false;
		}
	}else if(gbnselect == "VLIST"){
		category_id = $("#imcscontents_code").val();
		
		if(category_id == ""){
			alert("IMCS 컨텐츠를 선택해주세요.");
			return false;
		}
	}else if(gbnselect == "HLIST"){
		category_id = $("#hotvod_code").val();
		
		if(category_id == ""){
			alert("대박영상 목록을 선택해주세요.");
			return false;
		}
	}else if(gbnselect == "GALLERY"){
		category_id = $("#gallery_code").val();
		
		if(category_id == ""){
			alert("갤러리 목록을 선택해주세요.");
			return false;
		}
	}else if(gbnselect == "HOTVOD"){
		category_id = $("#hotvodiptv_code").val();
		
		if(category_id == ""){
			alert("화제동영상 목록을 선택해주세요.");
			return false;
		}
	}
	$("#category_id").val(category_id);
	$("#category_type").val(gbnselect);
	$("#album_cnt").val(album_cnt);
}

function regcategory(){

	frm.submit();
	
}

function appTypeCheck(){

    $('#page_code').val("");
    
    if($("#app_type").val() == ''){    //직접입력
        $('#page_code').attr('readonly', false);
    }else{
        $('#page_code').attr('readonly', true);
        $('#page_code').val( $("#app_type").val() );
    }
}

function PageTypeBtnOpenCheck(){
	var cateTypeList = '${cateTypeList}';
	cateTypeList = cateTypeList.split('\|');
	
	var checktype = false;
	for(var i=0;i<cateTypeList.length;i++){
		if($('#gbn_select').val() == cateTypeList[i]){
			checktype = true;
		}
	}
	
	
	if(checktype == true){
		$('#categoryPopBtn').removeAttr("style");
		$('#subcnt_tr').removeAttr("style");
		$('#page_code').attr('readonly', true);
		$("#setCategoryPageCode").hide();
		$("#papagoLanguageList").hide();
		$("#app_type").hide();
		checkPageType("A");
		pageType="A";
	}else{
		var v = $("#page_type").val();
		if(v == "CAT"){
			$("#page_code").attr("readonly",true);
			$("#setCategoryPageCode").show();
			$("#papagoLanguageList").hide();
			$("#panelPopBtn").hide();
			$("#app_type").hide();
			checkPageType("C");
			pageType="C";
		}else if(v == "PAPAGO"){
			$("#page_code").attr("readonly",true);
			$("#setCategoryPageCode").hide();
			$("#papagoLanguageList").show();
			$("#panelPopBtn").hide();
			$("#app_type").hide();
			checkPageType("D");
			pageType="D";
		}else if(v == "PANEL"){
			$("#page_code").attr("readonly",true);
			$("#setCategoryPageCode").hide();
			$("#papagoLanguageList").hide();
			$("#panelPopBtn").show();
			$("#app_type").hide();
			checkPageType("P");
			pageType="P";
		 }else if(v == "APP"){
            if($("#app_type").val() == ''){
                $("#page_code").attr("readonly",false);
            }else{
                $("#page_code").attr("readonly",true);
            }
            $("#setCategoryPageCode").hide();
            $("#papagoLanguageList").hide();
            $("#panelPopBtn").hide();
            $("#app_type").show();
            checkPageType("Z");
            pageType="Z";
		}else{
			$("#page_code").attr("readonly",false);
			$("#setCategoryPageCode").hide();
			$("#papagoLanguageList").hide();
			$("#panelPopBtn").hide();
			$("#app_type").hide();
			checkPageType("B");
			pageType="B";
		}
	}
}

function checkPageType(_type){
	if(""!=pageType && pageType!=_type){
		$("#page_code").val("");
		$("input[name='papago_language']").attr("checked", false);
		papagoList.remove(0, papagoList.length);
	}
}

function setCategoryPageCode(){
    var panel_id = $("#panel_id").val();
    var category_gb = $("#category_gb").val();
	//alert("카테고리 설정 팝업 출력");
	var url = "<%=webRoot%>/admin/mainpanel/getPageCategoryList.do?panel_id=" + panel_id + "&category_id=VC&category_gb=" + category_gb;
	category_window = window.open(url, "getPageCategoryList", "left=500,width=550,height=100,scrollbars=yes");
}


function setPanelPageCode(){
    var panel_id = $("#panel_id").val();
    var category_gb = $("#category_gb").val();
	var url = "<%=webRoot%>/admin/mainpanel/getPanelListPop.do?";
	category_window = window.open(url, "getPanelListPop", "left=500,width=700,height=600,scrollbars=yes");
}


function view_image(category){
	if(category == "" || category == null){			// 선택한 항목이 없으면
		alert("카테고리를 선택해주세요");
	}else{					// 선택한 항목이 있으면
		var url = "<%=webRoot%>/admin/schedule/getAlbumListByCategoryIdAlbumId.do?category_id="+category+"&album_id=&type=${category_gb}";
		category_window = window.open(url, "getAlbumListByCategoryIdAlbumId", "left=500,width=800,height=550,scrollbars=yes");
	}
}

//광고 마스터 팝업
function getBannerMasterPop() {
	url = '/hdtv_adm/admin/videolte/common/getBannerMasterPop.do?callbak=getBannerMasterPopCallbak';
	category_window = window.open(url, 'getBannerMasterPop', 'width=600,height=600,left=100,top=50,scrollbars=yes');
}
function getBannerMasterPopCallbak(data) {
	$("#hbanner").val(data.ads_id);
	$("#tbanner").val(data.ads_nm);
}

function getLocationListPop(){
	url = '<%=webRoot%>/admin/mainpanel/getLocationListPop.do?callbak=getLocationListPopCallbak';
	category_window = window.open(url, 'getBannerMasterPop', 'width=670,height=150,left=100,top=50,scrollbars=yes');
}

function getLocationListPopCallbak(data){
	$("#location_yn").val(data.location_yn);
	$("#location_code").val(data.location_code);
}

function getMimsCategoryListPopCallbak(data) {
	var cate = data.cate;
	var title = data.catenm;
	
	$("#hotvod_des").val(title);
	$("#hotvod_code").val(cate);
}

function getMimsIptvCategoryListPopCallbak(data) {
	var cate = data.cate;
	var title = data.catenm;
	
	$("#hotvodiptv_des").val(title);
	$("#hotvodiptv_code").val(cate);
}

function getGalleryListPopCallbak(data) {
	$("#gallery_code").val(data);
	
}

/* 2019.11.04 : 지면UI타입 선택 팝업 추가 Start - 이태광 */
//지면UI타입 선택 팝업
function getPanelUiTypeListPop() {
	var data_type_val = $("#data_type option:selected").val();
	url = '<%=webRoot%>/admin/mainpanel/getPanelUiTypeListPop.do?frame_type=30&callbak=getPanelUiTypeListPopCallbak&data_type=' + data_type_val;
	category_window = window.open(url, 'getFrameListPop', 'width=700,height=650,left=100,top=50,scrollbars=yes');
}

function getPanelUiTypeListPopCallbak(data) {
	$("#paper_ui_type").val(data.frame_type_code);
}

/* 2019.11.04 : 지면UI타입 선택 팝업 추가 End - 이태광 */


//20220117 월정액상품 선택 추가
function getProductCodeListPop(){
    url = '<%=webRoot%>/admin/commonMng/getFlatRateList.do?hiddenName=link_detail&textName=link_detail_name&textHtml=ChoiceData';
    category_window = window.open(url, 'getFlatRateList', 'width=1200,height=300,left=100,top=50,scrollbars=yes');
    
}

//20220117 월정액 상품 초기화
function setProductDelete(){
    $('#ChoiceData').html('');
    $('#link_detail').val('');
    $('#link_detail_name').val('');
    $('#product_code').val('');
}

//20220117 유효하지 않은 월정액 상품 삭제
function setUseNProductDelete(){
    $('.p-unuse').hide();
    $('#link_detail').val($('#useN_product_code').val());
    setUseNProductDelFlag();
}

//20220117 유효하지 않은 상품 삭제 버튼
function setUseNProductDelFlag(){
    var productCodeTxt = $('#link_detail').val();
    
    if($('#link_detail').val().indexOf('유효하지 않은 상품')==-1){
        $('#useNPrdDelBtn').attr("disabled", true);
        $('#useNPrdDelBtn').hide();
        $('#useNPrdTxt').remove('#useNPrdTxt');
    }else{
        $('#useNPrdDelBtn').removeAttr("disabled");
        $('#useNPrdDelBtn').show();
        $('#ChoiceData').append("<p id=\"useNPrdTxt\" style=\"font-size:9pt;color:red\">유효하지 않은 상품이 있습니다. 유효하지 않은 상품 삭제 버튼으로 삭제해주세요.</p>");
    }
}

//20220117 선택된 월정액 상품 값 [코드] 상품명 으로 변경
function getMenuTreeList_setChoiceData(){
    
    if($('#link_detail').val().indexOf('ALL')>-1){
        alert('월정액 상품리스트는 선택하실 수 없습니다.');
        setProductDelete();
        return;
    }
    
    var productCodeInfoList = $('#link_detail').val().split("\f^");
    
    var productCodeTxt="";
    $('#ChoiceData').html('');
    
    if(productCodeInfoList != null && productCodeInfoList != ''){
        for(var i =0; i<productCodeInfoList.length;i++){
            
            var productCodeInfo = productCodeInfoList[i].split("\b^");
            
            var productCodeTxt = 
                $('<span/>')
                    .text('['+productCodeInfo[0]+'] '+productCodeInfo[1])
                    .attr('class', productCodeInfo[1] != '유효하지 않은 상품' ? 'p-use' : 'p-unuse');
            if(productCodeInfo[1] == '유효하지 않은 상품') {
                productCodeTxt.css('color', 'red');
            }
            $('#ChoiceData').append(productCodeTxt);
            $('#ChoiceData').append('<br>');
        }
    }
    setUseNProductDelFlag();
    
}

//20220117 월정액상품코드_수정모드
function setProductCode(){
    var useNProductCode="";
    var useYProductCode="";
    var link_detail="";
    var cnt =0;
    $('#ChoiceData').html('');
    
    <c:forEach items="${mainresult.product_code_list}" var="item" varStatus="status">
        var $product = 
            $('<span/>')
                .text('[${item.productID}] ${item.productName}')
                .attr('class', '${item.productCodeUse}' == 'true' ? 'p-use' : 'p-unuse');
        if('${item.productCodeUse}' == 'false' ) {
            $product.css('color', 'red');
        }
        $('#ChoiceData').append($product);
        $('#ChoiceData').append('<br>');
        if('${status.index}' ==0){
            link_detail +="${item.productID}\b^${item.productName}";
        }else{
            link_detail +="\f^${item.productID}\b^${item.productName}";
        }
        
        //현재 사용되는 월정액 상품코드만 담음
        if('${item.productCodeUse}' == 'true'){
            if(cnt ==0){
                useYProductCode+="${item.productID}\b^${item.productName}";
                cnt++;
            }else{
                useYProductCode+="\f^${item.productID}\b^${item.productName}";
            }
        }
    </c:forEach>
    $('#link_detail').val(link_detail);
    $('#useN_product_code').val(useYProductCode);
    setUseNProductDelFlag();
}








//20220117 월정액상품 선택 추가
function getProductCodeListPop2(){
    url = '<%=webRoot%>/admin/commonMng/getFlatRateList.do?hiddenName=link_detail2&textName=link_detail_name2&textHtml=ChoiceData2';
    category_window = window.open(url, 'getFlatRateList', 'width=1200,height=300,left=100,top=50,scrollbars=yes');
    
}

//20220117 월정액 상품 초기화
function setProductDelete2(){
    $('#ChoiceData2').html('');
    $('#link_detail2').val('');
    $('#link_detail_name2').val('');
    $('#product_code2').val('');
}

//20220117 유효하지 않은 월정액 상품 삭제
function setUseNProductDelete2(){
    $('.p-unuse2').hide();
    $('#link_detail2').val($('#useN_product_code2').val());
    setUseNProductDelFlag2();
}

//20220117 유효하지 않은 상품 삭제 버튼
function setUseNProductDelFlag2(){
    var productCodeTxt = $('#link_detail2').val();
    
    if($('#link_detail2').val().indexOf('유효하지 않은 상품')==-1){
        $('#useNPrdDelBtn2').attr("disabled", true);
        $('#useNPrdDelBtn2').hide();
        $('#useNPrdTxt2').remove('#useNPrdTxt2');
    }else{
        $('#useNPrdDelBtn2').removeAttr("disabled");
        $('#useNPrdDelBtn2').show();
        $('#ChoiceData2').append("<p id=\"useNPrdTxt2\" style=\"font-size:9pt;color:red\">유효하지 않은 상품이 있습니다. 유효하지 않은 상품 삭제 버튼으로 삭제해주세요.</p>");
    }
}

//20220117 선택된 월정액 상품 값 [코드] 상품명 으로 변경
function getMenuTreeList_setChoiceData2(){
    
    if($('#link_detail2').val().indexOf('ALL')>-1){
        alert('월정액 상품리스트는 선택하실 수 없습니다.');
        setProductDelete();
        return;
    }
    
    var productCodeInfoList = $('#link_detail2').val().split("\f^");
    
    var productCodeTxt="";
    $('#ChoiceData2').html('');
    
    if(productCodeInfoList != null && productCodeInfoList != ''){
        for(var i =0; i<productCodeInfoList.length;i++){
            
            var productCodeInfo = productCodeInfoList[i].split("\b^");
            
            var productCodeTxt = 
                $('<span/>')
                    .text('['+productCodeInfo[0]+'] '+productCodeInfo[1])
                    .attr('class', productCodeInfo[1] != '유효하지 않은 상품' ? 'p-use2' : 'p-unuse2');
            if(productCodeInfo[1] == '유효하지 않은 상품') {
                productCodeTxt.css('color', 'red');
            }
            $('#ChoiceData2').append(productCodeTxt);
            $('#ChoiceData2').append('<br>');
        }
    }
    setUseNProductDelFlag2();
    
}

//20220117 월정액상품코드_수정모드
function setProductCode2(){
    var useNProductCode="";
    var useYProductCode="";
    var link_detail="";
    var cnt =0;
    $('#ChoiceData2').html('');
    
    <c:forEach items="${mainresult.product_code_list_not}" var="item" varStatus="status">
        var $product = 
            $('<span/>')
                .text('[${item.productID}] ${item.productName}')
                .attr('class', '${item.productCodeUse}' == 'true' ? 'p-use2' : 'p-unuse2');
        if('${item.productCodeUse}' == 'false' ) {
            $product.css('color', 'red');
        }
        $('#ChoiceData2').append($product);
        $('#ChoiceData2').append('<br>');
        if('${status.index}' ==0){
            link_detail +="${item.productID}\b^${item.productName}";
        }else{
            link_detail +="\f^${item.productID}\b^${item.productName}";
        }
        
        //현재 사용되는 월정액 상품코드만 담음
        if('${item.productCodeUse}' == 'true'){
            if(cnt ==0){
                useYProductCode+="${item.productID}\b^${item.productName}";
                cnt++;
            }else{
                useYProductCode+="\f^${item.productID}\b^${item.productName}";
            }
        }
    </c:forEach>
    $('#link_detail2').val(link_detail);
    $('#useN_product_code2').val(useYProductCode);
    setUseNProductDelFlag2();
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

function setTerminal(){
    var terminal_arr = '${mainresult.terminal_arr}';
    var terminal_html = "<br>";
    console.log(terminal_arr);
    if(terminal_arr != null && terminal_arr != ""){
        var terminal_arr = terminal_arr.split(",");
        var terminal_html = "<br>";
        if(terminal_arr.length > 0){
            for(var i = 0 ; i < terminal_arr.length; i++){
                console.log(terminal_arr[i])
                if(terminal_arr[i] != ''){
                    var dHmtl = "";
                    dHtml = terminal_arr[i]+"  /  <input type='hidden' name='terminal_list' value="+terminal_arr[i]+">";
                    terminal_html += dHtml;
                }
            }
            $("#terminal_all").attr("checked", false);
            $("#termPopup").attr("style","display:block");  
            $('#terminal_div').html(terminal_html);
        }
    }else{
        $("#terminal_all").attr("checked", true);
        $("#termPopup").attr("style","display:none");  
    }
}
</script>
<body leftmargin="0" topmargin="0">
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
						                                            <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
						                                            <td class="bold">카테고리 설정</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="regfrm" name="regfrm" method="post" action="<%=webRoot%>/admin/abtest/updatePaperDataType.do" encType="multipart/form-data">
						                        <!-- 20180912 월정액상품 관련 파라미터 -->
                                                <input type="hidden" name="link_detail" id="link_detail" value="">
                                                <input type="hidden" name="link_detail_name" id="link_detail_name" value="">
                                                <input type="hidden" name="product_code" id="product_code" value="">
                                                <input type="hidden" name="useN_product_code" id="useN_product_code" value="">
                                                
                                                <input type="hidden" name="link_detail2" id="link_detail2" value="">
                                                <input type="hidden" name="link_detail_name2" id="link_detail_name2" value="">
                                                <input type="hidden" name="product_code2" id="product_code2" value="">
                                                <input type="hidden" name="useN_product_code2" id="useN_product_code2" value="">
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                	<th width="25%">카테고리 구분</th>
					                                    <td width="2%"></td>
					                                    <td align="left" >
															<select id="category_gb" name="category_gb">
																<option value="I20" <c:if test="${mainresult.category_gb eq 'I20'}">selected="selected"</c:if>>I20</option>
																<option value="I30" <c:if test="${mainresult.category_gb eq 'I30'}">selected="selected"</c:if>>I30</option>
															</select>
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th>지면 타입</th>
					                                    <td></td>
					                                    <td align="left" >
															<select id="page_type" name="page_type" onchange="PageTypeBtnOpenCheck();">
																<option <c:if test="${mainresult.page_type == 'LIVE'}">selected="selected"</c:if> value="LIVE">편성표</option>
																<option <c:if test="${mainresult.page_type == 'CAT'}">selected="selected"</c:if> value="CAT">메뉴 카테고리</option>
																<option <c:if test="${mainresult.page_type == 'APP'}">selected="selected"</c:if> value="APP">어플 연동</option>
																<option <c:if test="${mainresult.page_type == 'AHOME'}">selected="selected"</c:if> value="AHOME">추천 연동</option>
																<option <c:if test="${mainresult.page_type == 'ETC'}">selected="selected"</c:if> value="ETC">연동 없음</option>
																<option <c:if test="${mainresult.page_type == 'EXT'}">selected="selected"</c:if> value="EXT">사용자 정의</option>
																<option <c:if test="${mainresult.page_type == 'PAPAGO'}">selected="selected"</c:if> value="PAPAGO">파파고(시니어)</option>
																<option <c:if test="${mainresult.page_type == 'PANEL'}">selected="selected"</c:if> value="PANEL">패널연동</option>
															</select>
															
															<select id="app_type" name="app_type" style="display:none" onchange="appTypeCheck();">
                                                                
                                                                <option value="">직접입력</option>
                                                                
                                                                <c:forEach var="apptypeunit" items="${apptyperesult}" varStatus="status">
                                                                    <c:choose>
                                                                        <c:when test="${mainresult.page_code == apptypeunit.item_code}">
                                                                            <option value="${mainresult.page_code}" selected>${apptypeunit.item_nm}</option>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <option value="${apptypeunit.item_code}">${apptypeunit.item_nm}</option>
                                                                        </c:otherwise>  
                                                                    </c:choose>
                                                                </c:forEach>      
                                                            </select>
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th>지면 코드</th>
					                                    <td></td>
					                                    <td align="left" >
															<input type="text" id="page_code" name="page_code" value="${mainresult.page_code}" />
															<span class="button small blue" id="setCategoryPageCode" onclick="setCategoryPageCode()">메뉴 카테고리 설정</span>
															<span class="button small blue" id="categoryPopBtn">카테고리 선택</span>
															<span class="button small blue" id="panelPopBtn" onclick="setPanelPageCode()" >패널 선택</span>
															
						
															<div id="papagoLanguageList" style="display:none">
																<label for="papago_language_en"><input type="checkbox" id="papago_language_en" name="papago_language" value="EN">영어</label>
																<label for="papago_language_zh"><input type="checkbox" id="papago_language_zh" name="papago_language" value="ZH">중국어</label>
																<label for="papago_language_ja"><input type="checkbox" id="papago_language_ja" name="papago_language" value="JA">일본어</label>
																<label for="papago_language_fr"><input type="checkbox" id="papago_language_fr" name="papago_language" value="FR">프랑스어</label>
																<label for="papago_language_es"><input type="checkbox" id="papago_language_es" name="papago_language" value="ES">스페인어</label>
															</div>
														</td>
					                                </tr>
					                                <tr align="center" id="subcnt_tr">
					                                	<th>하위 개수</th>
					                                	<td></td>
					                                	<td align="left">
					                                		<input type="text" id="sub_cnt" name="sub_cnt"/>
					                                	</td>
					                                </tr>
					                                <!-- //20180913 이미지 관련 등록 및 삭제는 하위지면 추가와 지면 상세조회에서만 등록 및 수정 
					                                <tr align="center">
					                                    <th>지면 아이콘 이미지</th>
					                                    <td></td>
					                                    <td align="left" >
															<input type="file" id="bg_img_file" name="bg_img_file" value="">
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th>지면 아이콘 이미지2</th>
					                                    <td></td>
					                                    <td align="left" >
															<input type="file" id="bg_img_file2" name="bg_img_file2" value="">
														</td>
					                                </tr>
					                                -->
					                                <tr align="center">
					                                    <th>UI 타입</th>
					                                    <td></td>
					                                    <td align="left" >
															<select id="ui_type" name="ui_type">
																<option <c:if test="${mainresult.ui_type == 'MENU'}">selected="selected"</c:if> value="MENU">메뉴</option>
																<option <c:if test="${mainresult.ui_type == 'LIST'}">selected="selected"</c:if> value="LIST">리스트</option>
															</select>
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">데이터 타입</th>
					                                    <td width="2%"></td>
					                                    <td align="left" >
															<select id="gbn_select" name="gbn_select">
																<c:forEach var="item" items="${coderesult}" varStatus="status">
																	<c:choose>
																		<c:when test="${item.ss_gbn == category_type}">
																			<option value="${item.ss_gbn}" selected>${item.item_nm }</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${item.ss_gbn}">${item.item_nm }</option>
																		</c:otherwise>	
																	</c:choose>
																	
																</c:forEach>																
															</select>				
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th>지면 설명</th>
					                                    <td></td>
					                                    <td align="left" >
															<textarea id="description" name="description" cols="50" rows="2">${mainresult.description}</textarea>
														</td>
					                                </tr>
<!-- 2019.11.04 : 지면UI타입 추가 - 이태광 Start -->					                                
					                                <tr align="center">
					                                    <th width="25%">지면UI타입</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="text" name="paper_ui_type" id="paper_ui_type" size="8" value="${mainresult.paper_ui_type}" style="font-size: 12px;text-align:center" readonly>															
															<span class="button small blue" id="searchBtn" onclick="getPanelUiTypeListPop()">UI타입 선택</span>
<!-- 2019.11.04 : 지면UI타입 추가 - 이태광 End -->															
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">대표컨텐츠</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<input type="text" name="reps_album_name" id="reps_album_name" size="20" style="font-size: 12px;" readonly="readonly" value="${mainresult.reps_album_name }" />
															<input type="hidden" name="choiceCts" id="choiceCts" />
															<input type="hidden" name="reps_album_id" id="reps_album_id" value="${mainresult.reps_album_id }" />
															<input type="hidden" name="reps_category_id" id="reps_category_id" value="${mainresult.reps_category_id }" />
															<span class="button small blue" id="searchRepsContentBtn">검색</span>
														</td>
					                                </tr>
					                                <tr id="categorytr" align="center" style="display:none">
					                                    <th>카테고리</th>
					                                    <td></td>
					                                    <td align="left">
					                                    	<input type="text"   id="category_des"  name="category_des"  value="${mainresult.category_des}"  maxlength="100" size="35" style="font-size: 12px;" readonly/>
															<input type="hidden" id="category_code"  name="category_code"  value="${mainresult.category_code}"  maxlength="100"  />
															<span class="button small blue" id="selCategorybtn">카테고리</span>
														</td>
					                                </tr>
					                                <tr id="bannertr" align="center" style="display:none">
					                                    <th>배너</th>
					                                    <td></td>
					                                    <td align="left">
					                                    	<input type="text"   id="tbanner"  name="tbanner"  value="${mainresult.category_des}"  maxlength="100" size="35" style="font-size: 12px;" readonly/>
															<input type="hidden" id="hbanner"  name="hbanner"  value="${mainresult.category_code}"  maxlength="100"  />
															<span class="button small blue" id="selBannerbtn">배너</span>
														</td>
					                                </tr>
					                                <tr id="userselcategorystring" align="center" style="display:none">
					                                    <th>선택 데이터</th>
					                                    <td></td>
					                                    <td align="left" id="userselcategorystringtd">

														</td>
					                                </tr>
					                                <tr id="imcscontentstr" align="center" style="display:none">
					                                    <th>IMCS 컨텐츠</th>
					                                    <td></td>
					                                    <td align="left">
					                                    	<input type="text"   id="imcscontents_des"  name="imcscontents_des"  value="${mainresult.category_des}"  maxlength="100" size="35" style="font-size: 12px;" readonly/>
															<input type="text"   id="imcscontents_des_all"  name="imcscontents_des_all"  value=""  maxlength="100" size="35" style="font-size: 12px; border:none;"  readonly/>		
															<input type="hidden" id="imcscontents_code"  name="imcscontents_code"  value="${mainresult.category_code}"  maxlength="100"  />
															<span class="button small blue" id="selImcsContentsbtn">IMCS 컨텐츠</span>
														</td>
					                                </tr>
					                                <tr id="hotvodtr" align="center" style="display:none">
					                                    <th>대박영상 목록</th>
					                                    <td></td>
					                                    <td align="left">
					                                    	<input type="text"   id="hotvod_des"  name="hotvod_des"  value="${mainresult.category_des}"  maxlength="100" size="35" style="font-size: 12px;" readonly/>
															<input type="hidden" id="hotvod_code"  name="hotvod_code"  value="${mainresult.category_code}"  maxlength="100"  />
															<span class="button small blue" id="selHotVodbtn">카테고리 ID선택</span>
														</td>
					                                </tr>
					                                <tr id="gallerytr" align="center" style="display:none">
					                                    <th>갤러리 목록</th>
					                                    <td></td>
					                                    <td align="left">
					                                    	<input type="text" id="gallery_code"  name="gallery_code"  value="${mainresult.category_code}"  maxlength="100" size="35" style="font-size: 12px;" readonly/>
															<span class="button small blue" id="selGallerybtn">갤러리</span>
														</td>
					                                </tr>
					                                <tr id="hotvodiptvtr" align="center" style="display:none">
					                                    <th>화제동영상 목록</th>
					                                    <td></td>
					                                    <td align="left">
					                                    	<input type="text"   id="hotvodiptv_des"  name="hotvodiptv_des"  value="${mainresult.category_des}"  maxlength="100" size="35" style="font-size: 12px;" readonly/>
															<input type="hidden" id="hotvodiptv_code"  name="hotvodiptv_code"  value="${mainresult.category_code}"  maxlength="100"  />
															<span class="button small blue" id="selHotVodIptvbtn">카테고리 ID선택</span>
														</td>
					                                </tr>					                                
					                                <tr align="center">
					                                    <th width="27%">대표 컨텐츠 예고편 노출 타입</th>
					                                    <td width="5%"></td>
					                                    <td width="68%" align="left" >
															<c:choose>
																<c:when test="${mainresult.reps_trailer_viewing_type == 'STIL'}">
																	<input type="radio" id="reps_trailer_viewing_type_none" name="reps_trailer_viewing_type" value="NONE" />없음
																	<input type="radio" id="reps_trailer_viewing_type_stil" name="reps_trailer_viewing_type" value="STIL" checked/>스틸컷
																	<input type="radio" id="reps_trailer_viewing_type_stau" name="reps_trailer_viewing_type" value="STAU" />스틸컷 후 자동재생
																</c:when>
																<c:when test="${mainresult.reps_trailer_viewing_type == 'STAU'}">
																	<input type="radio" id="reps_trailer_viewing_type_none" name="reps_trailer_viewing_type" value="NONE" />없음
																	<input type="radio" id="reps_trailer_viewing_type_stil" name="reps_trailer_viewing_type" value="STIL" />스틸컷
																	<input type="radio" id="reps_trailer_viewing_type_stau" name="reps_trailer_viewing_type" value="STAU" checked/>스틸컷 후 자동재생
																</c:when>
																<c:otherwise>
																	<input type="radio" id="reps_trailer_viewing_type_none" name="reps_trailer_viewing_type" value="NONE" checked/>없음
																	<input type="radio" id="reps_trailer_viewing_type_stil" name="reps_trailer_viewing_type" value="STIL" />스틸컷
																	<input type="radio" id="reps_trailer_viewing_type_stau" name="reps_trailer_viewing_type" value="STAU" />스틸컷 후 자동재생
																</c:otherwise>
															</c:choose>		
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th>예고편 노출 타입</th>
					                                    <td></td>
					                                    <td align="left" >
															<c:choose>
																<c:when test="${mainresult.trailer_viewing_type == 'STIL'}">
																	<input type="radio" id="trailer_viewing_type_none" name="trailer_viewing_type" value="NONE" />없음
																	<input type="radio" id="trailer_viewing_type_stil" name="trailer_viewing_type" value="STIL" checked/>스틸컷
																	<input type="radio" id="trailer_viewing_type_stau" name="trailer_viewing_type" value="STAU" />스틸컷 후 자동재생
																</c:when>
																<c:when test="${mainresult.trailer_viewing_type == 'STAU'}">
																	<input type="radio" id="trailer_viewing_type_none" name="trailer_viewing_type" value="NONE" />없음
																	<input type="radio" id="trailer_viewing_type_stil" name="trailer_viewing_type" value="STIL" />스틸컷
																	<input type="radio" id="trailer_viewing_type_stau" name="trailer_viewing_type" value="STAU" checked/>스틸컷 후 자동재생
																</c:when>
																<c:otherwise>
																	<input type="radio" id="trailer_viewing_type_none" name="trailer_viewing_type" value="NONE" checked/>없음
																	<input type="radio" id="trailer_viewing_type_stil" name="trailer_viewing_type" value="STIL" />스틸컷
																	<input type="radio" id="trailer_viewing_type_stau" name="trailer_viewing_type" value="STAU" />스틸컷 후 자동재생
																</c:otherwise>
															</c:choose>		
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">지역 코드</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<input type="hidden" name="location_yn" id="location_yn" value="${mainresult.location_yn}" />
					                                        <input type="text" name="location_code" id="location_code" size="9" value="${mainresult.location_code}" style="font-size: 12px;text-align:center" readonly>
															<span class="button small blue" id="searchBtn" onclick="getLocationListPop()">지역 선택</span>
														</td>
					                                </tr>
					                                
					                                <tr align="center">
					                                    <th width="25%">AB TEST 지면여부</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
						                                    <c:choose>
																<c:when test="${mainresult.abtest_yn == 'Y'}">
																	<input type="radio" id="abtest_ynY" name="abtest_yn" value="Y" checked/>예
																	<input type="radio" id="abtest_ynN" name="abtest_yn" value="N" />아니오
																</c:when>
																<c:otherwise>
																	<input type="radio" id="abtest_ynY" name="abtest_yn" value="Y" />예
																	<input type="radio" id="abtest_ynN" name="abtest_yn" value="N" checked/>아니오
																</c:otherwise>
															</c:choose>		
														</td>
					                                </tr>
					                                
					                                <!-- 월정액상품 선택 추가 20220117 -->
                                                    <tr align="center" id="productCodePopTr" class="val_productCode">
                                                        <th width="25%">월정액상품 선택 버튼</th> 
                                                        <td width="5%"></td>
                                                        <td width="70%" align="left" >
                                                            <span class="button small blue" id="prdBtn" onclick="getProductCodeListPop()">월정액상품 선택</span>
                                                            <span class="button small blue" id="prdDelBtn" onclick="setProductDelete()">초기화</span>      
                                                            <span class="button small blue" id="useNPrdDelBtn" onclick="setUseNProductDelete()">유효하지 않은 상품 삭제</span>        
                                                        </td>
                                                    </tr>
                                                    <tr align="center" id="productCodeTr" class="val_productCode">
                                                        <th width="25%">선택된 월정액상품</th> 
                                                        <td width="5%"></td>
                                                        <td width="70%" align="left" >
                                                            <div id="ChoiceData">
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    
                                                    
                                                    <!-- 월정액상품 선택 추가 20220117 -->
                                                    <tr align="center" id="productCodePopTr" class="val_productCode">
                                                        <th width="25%">월정액상품 선택 버튼(비노출)</th> 
                                                        <td width="5%"></td>
                                                        <td width="70%" align="left" >
                                                            <span class="button small blue" id="prdBtn2" onclick="getProductCodeListPop2()">월정액상품 선택</span>
                                                            <span class="button small blue" id="prdDelBtn2" onclick="setProductDelete2()">초기화</span>      
                                                            <span class="button small blue" id="useNPrdDelBtn2" onclick="setUseNProductDelete2()">유효하지 않은 상품 삭제</span>        
                                                        </td>
                                                    </tr>
                                                    <tr align="center" id="productCodeTr" class="val_productCode">
                                                        <th width="25%">선택된 월정액상품(비노출)</th> 
                                                        <td width="5%"></td>
                                                        <td width="70%" align="left" >
                                                            <div id="ChoiceData2">
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    
                                                    
                                                    <!-- 노출갯수 추가 20220117 -->
                                                    <tr align="center">
                                                        <th width="25%">노출갯수</th> 
                                                        <td width="5%"></td>
                                                        <td width="70%" align="left" >
                                                            <input type="text" id="show_cnt" name="show_cnt" style="width:20%" style="font-size: 12px;" value="${mainresult.show_cnt}" onkeyup="checkNum(this);" maxlength="5"/>        
                                                        </td>
                                                    </tr>
                                                    
                                                    <!-- 노출단말 추가 20220117 -->
                                                    <tr align="center">
                                                        <th width="25%">노출 단말 선택</th> 
                                                        <td width="5%"></td>
                                                        <td width="70%" align="left" >
                                                            All <input type="checkbox"  id="terminal_all" checked="checked"> 
                                                                <input type="hidden" id="terminal_all_yn" name="terminal_all_yn" />                                                                               
                                                                <input type="hidden" id="terminal_arr" name="terminal_arr" />                                                                               
                                                                <a href="#" id="termPopup" style="display:none;"><span class="button small blue" > 단말기 선택</span> </a>
                                                                <div  id="terminal_div"></div>        
                                                        </td>
                                                    </tr>
					                            	</tbody>
					                            </table>
					                            <input type="hidden" id="variation_id" name="variation_id" value="${variation_id}" />
					                            <input type="hidden" id="panel_id" name="panel_id" value="${panel_id}" />
												<input type="hidden" id="title_id" name="title_id" value="${title_id}" />
												<input type="hidden" id="title_nm" name="title_nm" value="${mainresult.title_nm}" />
												<input type="hidden" id="smartUXManager" name="smartUXManager" value="<%=id_decrypt %>" />
												<input type="hidden" id="category_id" name="category_id" value="" />
												<input type="hidden" id="category_type" name="category_type" value="" />
												<input type="hidden" id="album_cnt" name="album_cnt" value="" />
												<input type="hidden" id="isUpdate" name="isUpdate" value="${isUpdate}" />
					                            </form>
					                            
					                            <!-- Best VOD 출력 테이블 -->
					                            <table id="bestvodtable" border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list" style="display:none">
					                                <tbody>
					                                <tr>
														<th width="10%" style="padding-top:8px"><input type="checkbox" id="allvodchk" name="allvodchk" value=""></th>
														<th width="90%">랭킹 제목</th>
													</tr>
													<c:choose>
														<c:when test="${ecrmresult==null || fn:length(ecrmresult) == 0}">
															<tr>
																<td colspan="2" class="table_td_04">검색된 랭킹 정보가 없습니다</td>
															</tr>
														</c:when>
														<c:otherwise>
															<c:forEach var="item" items="${ecrmresult}" varStatus="status">
																<tr>
																	<td class="table_td_04"><input type="checkbox" name="vodchk" value="${item.rank_code}"></td>
																	<td class="table_td_04" style="text-align: left;">${item.rank_name}</td>
																</tr>
															</c:forEach>
														</c:otherwise>
													</c:choose>
					                            	</tbody>
					                            </table>
					                            
					                            <table id="bestvodtable_i30" border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list" style="display:none">
					                                <tbody>
					                                <tr>
														<th width="10%" style="padding-top:8px"><input type="checkbox" id="allvodchk_i30" name="allvodchk_i30" value=""></th>
														<th width="90%">랭킹 제목</th>
													</tr>
													<c:choose>
														<c:when test="${ecrmresult_i30==null || fn:length(ecrmresult_i30) == 0}">
															<tr>
																<td colspan="2" class="table_td_04">검색된 랭킹 정보가 없습니다</td>
															</tr>
														</c:when>
														<c:otherwise>
															<c:forEach var="item" items="${ecrmresult_i30}" varStatus="status">
																<tr>
																	<td class="table_td_04"><input type="checkbox" name="vodchk_i30" value="${item.rank_code}"></td>
																	<td class="table_td_04" style="text-align: left;">${item.rank_name}</td>
																</tr>
															</c:forEach>
														</c:otherwise>
													</c:choose>
					                            	</tbody>
					                            </table>
					                            
					                            <!-- 카테고리랭킹 테이블 -->
					                            <table id="catranktable" border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list" style="display:none">
					                                <tbody>
					                                <tr>
														<th width="10%" style="padding-top:8px"><input type="checkbox" id="allcatchk" name="allcatchk" value=""></th>
														<th width="90%">랭킹 제목</th>
													</tr>
													<c:choose>
														<c:when test="${ecrmresult_cat==null || fn:length(ecrmresult_cat) == 0}">
															<tr>
																<td colspan="2" class="table_td_04">검색된 랭킹 정보가 없습니다</td>
															</tr>
														</c:when>
														<c:otherwise>
															<c:forEach var="item" items="${ecrmresult_cat}" varStatus="status">
																<tr>
																	<td class="table_td_04"><input type="checkbox" name="catchk" value="${item.rank_code}"></td>
																	<td class="table_td_04" style="text-align: left;">${item.rank_name}</td>
																</tr>
															</c:forEach>
														</c:otherwise>
													</c:choose>
					                            	</tbody>
					                            </table>
					                            
					                            <table id="catranktable_i30" border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list" style="display:none">
					                                <tbody>
					                                <tr>
														<th width="10%" style="padding-top:8px"><input type="checkbox" id="allcatchk_i30" name="allcatchk_i30" value=""></th>
														<th width="90%">랭킹 제목</th>
													</tr>
													<c:choose>
														<c:when test="${ecrmresult_cat_i30==null || fn:length(ecrmresult_cat_i30) == 0}">
															<tr>
																<td colspan="2" class="table_td_04">검색된 랭킹 정보가 없습니다</td>
															</tr>
														</c:when>
														<c:otherwise>
															<c:forEach var="item" items="${ecrmresult_cat_i30}" varStatus="status">
																<tr>
																	<td class="table_td_04"><input type="checkbox" name="catchk_i30" value="${item.rank_code}"></td>
																	<td class="table_td_04" style="text-align: left;">${item.rank_name}</td>
																</tr>
															</c:forEach>
														</c:otherwise>
													</c:choose>
					                            	</tbody>
					                            </table>
					                            
					                            <!-- 카테고리 선택 테이블 -->
					                            <table id="categorytable" border="0" cellpadding="0" cellspacing="0" width="100%" style="display:none">
					                                <tbody>
					                                <tr>
														<td>
															<table>
																<tr id="category_tr">
																	<td name="category_td" valign="top">
																		<c:choose>
																			<c:when test="${categoryresult==null || fn:length(categoryresult) == 0}">
																				검색된 카테고리가 없습니다
																			</c:when>
																			<c:otherwise>
																				<select name="category_select">
																					<option value="---">선택해주세요</option>
																				<c:forEach var="item" items="${categoryresult}" varStatus="status">
																					<option value="${item.category_id}">${item.category_name }</option>
																				</c:forEach>
																				</select>
																			</c:otherwise>
																		</c:choose>
																	</td>
																	<td>
																		<span class="button small blue" id="setcategorybtn" onclick="setcategory()">설정</span>
																		<span class="button small blue" id="delcategorybtn" onclick="delcategory()">삭제</span>
																	</td>
																</tr>
																<tr id="category_i30_tr">
																	<td name="category_i30_td" valign="top">
																		<c:choose>
																			<c:when test="${categoryresult_i30==null || fn:length(categoryresult_i30) == 0}">
																				검색된 카테고리가 없습니다
																			</c:when>
																			<c:otherwise>
																				<select name="category_i30_select">
																					<option value="---">선택해주세요</option>
																				<c:forEach var="item" items="${categoryresult_i30}" varStatus="status">
																					<option value="${item.category_id}">${item.category_name }</option>
																				</c:forEach>
																				</select>
																			</c:otherwise>
																		</c:choose>
																	</td>
																	<td>
																		<span class="button small blue" id="setcategorybtn" onclick="setcategory()">설정</span>
																		<span class="button small blue" id="delcategorybtn" onclick="delcategory()">삭제</span>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													
													<tr>
														<td colspan="2" style="height:20px;">
													
													
														</td>
													</tr>
													
													<tr>
														<td>
															<table id="category_album_tbl" border="0" cellpadding="0" cellspacing="0" class="board_list" width="100%">
																<tr align="center">
																	<th width="5%" style="vertical-align : middle;"><input type="checkbox" id="category_album_allchk"></th>
																	<th width="50%">카테고리 이름</th>
																	<th>앨범 갯수</th>
																	<th>이미지 변경</th>
																</tr>
																<c:choose>
																	<c:when test="${userCategoryresult==null || fn:length(userCategoryresult) == 0}">
																		
																	</c:when>
																	<c:otherwise>
																		<c:forEach var="item" items="${userCategoryresult}" varStatus="status">
																			<tr>
																				<td><input type="checkbox" name="category_album_chk" value=""></td>
																				<td>
																					<input type="text" name="category_album_category_text" value="${item.category_name}" readonly>
																					<input type="hidden" name="category_album_category_id" value="${item.category_id}">
																				</td>
																				<td><input type="text" name="category_album_album_cnt" value="${item.album_cnt}"></td>
																				<td><span class="button small red" onclick="view_image('${item.category_id}')">이미지</span></td>
																			</tr>
																		</c:forEach>
																	</c:otherwise>
																</c:choose>
															</table>
														</td>
													</tr>
					                            	</tbody>
					                            </table>
					                           
					                           	<!-- 자체편성 정보 테이블 -->
					                           	<table id="scheduletable" border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list" style="display:none">
					                                <tbody>
					                                <tr id="schedule_tr">
					                                	<th width="20%">자체편성</th>
														<td name="schedule_td" style="text-align:left; text-valign:middle;">
														&nbsp;
															<c:choose>
																<c:when test="${scheduleresult==null || fn:length(scheduleresult) == 0}">
																	검색된 자체 편성이 없습니다
																</c:when>
																<c:otherwise>
																	<select id="schedule_select">
																		<option value="---">선택해주세요</option>
																		<c:forEach var="item" items="${scheduleresult}" varStatus="status">
																			<c:choose>
																				<c:when test="${category_type eq 'SCHEDULE' and item.schedule_code eq category_id}">
																					<option value="${item.schedule_code}" selected="selected">${item.schedule_name }</option>
																				</c:when>
																				<c:otherwise>
																					<option value="${item.schedule_code}">${item.schedule_name }</option>
																				</c:otherwise>
																			</c:choose>
																		</c:forEach>
																	</select>
																</c:otherwise>
															</c:choose>
														</td>
													</tr>
													<tr id="schedule_i30_tr">
					                                	<th width="20%">자체편성</th>
														<td name="schedule_i30_td" style="text-align:left; text-valign:middle;">
														&nbsp;
															<c:choose>
																<c:when test="${scheduleresult_i30==null || fn:length(scheduleresult_i30) == 0}">
																	검색된 자체 편성이 없습니다
																</c:when>
																<c:otherwise>
																	<select id="schedule_i30_select">
																		<option value="---">선택해주세요</option>
																		<c:forEach var="item" items="${scheduleresult_i30}" varStatus="status">
																			<c:choose>
																				<c:when test="${category_type eq 'SCHEDULE' and item.schedule_code eq category_id}">
																					<option value="${item.schedule_code}" selected="selected">${item.schedule_name }</option>
																				</c:when>
																				<c:otherwise>
																					<option value="${item.schedule_code}">${item.schedule_name }</option>
																				</c:otherwise>
																			</c:choose>
																		</c:forEach>
																	</select>
																</c:otherwise>
															</c:choose>
														</td>
													</tr>
					                            	</tbody>
					                            </table>
					                            
					                            <!-- 카테고리 팝업 테이블 -->
					                           	<table id="catepoptable" border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list" style="display:none">
					                                <tbody>
					                                <tr id="schedule_tr">
					                                	<th width="20%">카테고리 선택</th>
														
													</tr>
					                            	</tbody>
					                            </table>

					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                <c:if test="${mainresult.del_yn ne 'Y' && abmsUptFlag}">
						                                    <c:if test="${test_type ne 'O'}">
						                                	<span class="button small blue" id="regbtn">설정</span>
						                                	</c:if>
						                                </c:if>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>

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
