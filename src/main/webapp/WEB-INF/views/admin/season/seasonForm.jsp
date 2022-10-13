<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<title>LG U+ IPTV SmartUX</title>


<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
 <!-- link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/ -->
 <!-- script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script -->
 
 <link href="/smartux_adm/css/anytime_style.css" rel="stylesheet" type="text/css">
<script src="/smartux_adm/js/anytime.js"></script>

<script type="text/javascript">
var DATA_LIST = {};
var ORDER = 1;

$(document).ready(function(){
	
	if('${isUpdate}' == 1){
		seasonDetailView('${childList}');
	}

    var fixHelper = function(e, ui) {
		ui.children().each(function() {
			$(this).width($(this).width());
		});
		return ui;
	};
	
	$("#seasonTr").sortable({
	    helper: fixHelper,
	    axis:'y'
    }).disableSelection();
    
	//앨범선택 버튼
	$('#searchBtn').click(function(){
		var type = '';
		if($('#app_tp').val() == 'I'){
			type = 'I20';
		}else{
			type = 'I30';
		}
		
		var url = "<%=webRoot%>/admin/commonMng/getOnceCategoryAlbum.do?categoryId=VC&hiddenName=choiceCts&textHtml=textHtml&textName=textName&series=Y&type=" + type;
    	category_window = window.open(url, "categoryAlbumPop", "width=850,height=430,scrollbars=yes,resizable=yes");
	});
	
	//추가버튼
	$('#addBtn').click(function(){
		var seasonNm = $('#season_name').val();
		var albumNm = $('#textName').val();
		var albumId = $('#choiceCts').val();
		var appTp = $('#app_tp').val();
		
		var addData = {};
		
		if(seasonNm == ''){
			alert("시즌명을 입력해주세요.");
			return;
		}
		if(albumId == ''){
			alert("앨범을 선택해주세요");
			return;
		}else{
			albumId = albumId.split('||')[1];
		}
		if(appTp == ''){
			appTp = 'I';
		}
		
		if(DATA_LIST[ORDER] == null){
			
			for(key in DATA_LIST){
				if(DATA_LIST[key].albumId == albumId){
					alert('동일한 앨범을 중복편성 할 수 없습니다');
					$('#textName').val('');
					$('#textHtml').html('');
					$('#choiceCts').val('');
					return;
				}
			}
			addData = {
					"seasonNm" : seasonNm,
					"albumNm" : albumNm,
					"albumId" : albumId,
					"newYn" : "Y",
					"orders" : ORDER,
					"delYn" : "N",
					"appTp" : appTp
			};
			DATA_LIST[ORDER] = addData;
			ORDER ++;
		}
		
		refreshTr();
		
		$('#season_name').val('');
		$('#textName').val('');
		$('#textHtml').html('');
		$('#choiceCts').val('');
	});
});

//시즌 삭제
function deleteBtn(order){
	DATA_LIST[order].delYn = "Y";
	refreshTr();
}

//카테고리 확인
function categoryBtn(albumId){
	var category_gb = '';
	if("I" == $('#app_tp').val()){
		category_gb = 'I20';
	}else{
		category_gb = 'I30';
	}
	$.ajax({
		url: "./getCategoryList",
		type: "POST",
		data: { 
				"albumId" : albumId,
				"series_yn" : $('#series_yn').val(),
				"category_gb" : category_gb
			  },
		dataType:"json",
		success: function (rtn) {
			if (rtn.res=="0000") {
				var categoryList = rtn.categoryList;
				var arr = categoryList.split(',');
				var html = '';

				if(arr.length > 0 && arr != 'null'){
					for(var i=0;i<arr.length;i++){
						if(i == 0){
							html = arr[i];
						}else{
							html = html + "<br>" + arr[i];
						};
					}
				}else{
					html = "검색된 카테고리가 없습니다.";
				}
				$('#categoryTd').html(html);
				$('#categoryTr').show();
			} else {
				alert("작업 중 오류가 발생하였습니다\nflag : " + rtn.res + "\ Message : " + rtn.msg);
			}
			$.unblockUI();
		},
		error: function () {					
			alert("일시적인 장애가 발생하였습니다. 다시 시도해 주세요.");
			$.unblockUI();
		}
	});
}

//시즌 데이터 테이블 갱신
function refreshTr(){
	var trData = '';
	for(key in DATA_LIST){
		if(DATA_LIST[key] != null){
			if(DATA_LIST[key].delYn == "N"){
				trData += '<tr name="detailTr" value="'+DATA_LIST[key].albumId+'"><td><input type="text" id="seasonNm'+key+'" size="30" style="font-size: 12px;" value="'+DATA_LIST[key].seasonNm+'" onKeyUp="checkByte($(this),\'64\')"/></td>'
				 + '<td>'+DATA_LIST[key].albumNm+'</td>'
				 + '<td><div align="center"><span class="button small blue" onclick="javascript:categoryBtn(\''+ DATA_LIST[key].albumId +'\');")>카테고리 확인</span><span class="button small red" onclick="javascript:deleteBtn(\''+ key +'\');")>삭제</span></div></td>'
				 + '<tr>'; 
			}
		}
	}
	
	$('#seasonTr').html(trData);
}

//등록
function goInsert(){
	if($('#season_title').val() == ''){
		alert('제목을 입력해주세요');
		return;
	}
	
	var cnt = 0;
	for(key in DATA_LIST){
		if(DATA_LIST[key].delYn == "N"){
			cnt++;
		}
	}
	if(cnt == 0){
		alert('시즌상세를 등록해주세요.');
		return;
	}
	
	var detailTr = $('tr [name=detailTr]');
	var newOrders = 1;
	var trAlbumId = '';
	
	//현재 화면에 보이는대로 순서 및 시즌명 저장
	detailTr.each(function(idx){
		trAlbumId = $(detailTr[idx]).attr('value');
		for(key in DATA_LIST){
			if(DATA_LIST[key].delYn == "N"){
				if(DATA_LIST[key].albumId == trAlbumId){
					DATA_LIST[key].orders = newOrders;
					DATA_LIST[key].seasonNm = $('#seasonNm' + key).val();
					newOrders++;
				}
			}
		}
	});
	
	if(confirm("저장하시겠습니까?")){
		try{
			$.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>처리중..</b>"
			});
		
			var seasonData = new Array();
			
			var url = '';
			var seasonId = $('#season_id').val();
			if($('#isUpdate').val() == '0'){
				for(key in DATA_LIST){
					var addList = DATA_LIST[key].seasonNm + "|" + DATA_LIST[key].albumId + "|" + DATA_LIST[key].newYn + "|" + DATA_LIST[key].orders + "|" + DATA_LIST[key].delYn + "|" + DATA_LIST[key].appTp;
					seasonData.push(addList);
				}
				url = "./insertProc.do";
			}else{
				for(key in DATA_LIST){
					var addList = DATA_LIST[key].seasonNm + "|" + DATA_LIST[key].albumId + "|" + DATA_LIST[key].newYn + "|" + DATA_LIST[key].orders  + "|" + DATA_LIST[key].delYn + "|" + DATA_LIST[key].seasonId + "|" + DATA_LIST[key].appTp;
					seasonData.push(addList);
				}
				url = "./updateProc.do";
			}
			$.ajax({
				url: url,
				type: "POST",
				data: { 
						"season_title" : $('#season_title').val(),
						"series_yn" : $('#series_yn').val(),
						"app_tp" : $('#app_tp').val(),
						"season_id" : seasonId,
						"seasonData" : seasonData
					  },
				dataType : "json",
				success: function (rtn) {
					if (rtn.res=="0000") {
						alert("정상처리 되었습니다.");
						$(location).attr('href', './seasonList.do?findName=${vo.findName}&findValue=${vo.findValue}&seriesYn=${vo.series_yn}&app_tp=${vo.app_tp}&pageNum=${vo.pageNum}');
					} else {
						alert("작업 중 오류가 발생하였습니다\nflag : " + rtn.res + "\ Message : " + rtn.msg);
					}
					$.unblockUI();
				},
				error: function () {					
					alert("일시적인 장애가 발생하였습니다. 다시 시도해 주세요.");
					$.unblockUI();
				}
			});
		}catch(e){
			alert("작업 중 오류가 발생하였습니다");
		}
		
	}
}

//상세 정보 세팅
function seasonDetailView(obj){
	var childList = obj;
	
	var row = childList.split("@^");
	var col = "";
	
	for(var i=0;i<row.length;i++){
		col = row[i].split("\|");
		
		var addData = {};
		
		if(DATA_LIST[ORDER] == null){
			addData = {
					"seasonId" : col[0],
					"seasonNm" : col[1],
					"albumId" : col[2],
					"albumNm" : col[3],
					"newYn" : "N",
					"orders" : ORDER,
					"delYn" : "N",
					"appTp" : col[4]
			};
			DATA_LIST[ORDER] = addData;
			ORDER ++;
		}
	}
	refreshTr();
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
                                    	시즌 관리	
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
						                                            <td class="bold">시즌 <c:if test="${isUpdate==0}">등록</c:if><c:if test="${isUpdate==1}">수정</c:if></td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="form1" method="get" enctype="multipart/form-data">
						                        <input type="hidden" name="findName" value="${vo.findName}" />
						                        <input type="hidden" name="findValue" value="${vo.findValue}" />
						                        <input type="hidden" name="seriesYn" value="${vo.series_yn}" />
						                        <input type="hidden" name="pageNum" value="${vo.pageNum}" />
						                        <input type="hidden" name="app_tp" id="app_tp" value="${vo.app_tp}" />
						                        <input type="hidden" name="isUpdate" id="isUpdate" value="${isUpdate }"/>
						                        <input type="hidden" name="season_id" id="season_id" value="${parent_season.season_id }"/>
						                        <input type="hidden" name="choiceCts" id="choiceCts"/>
						                        <input type="hidden" name="textName" id="textName"/>
					                            <table border="0" cellpadding="0" cellspacing="0" width="900" class="board_data">
					                                <tbody>		
						                                <tr align="center">
						                                    <th width="18%">제목</th>
						                                    <td width="1%"></td>
						                                    <td width="81%" align="left" >
																<input type="text" id="season_title" name="season_title" size="35" style="font-size: 12px;" value="${parent_season.season_title}" onKeyUp="checkByte($(this),'128')"/>
																(길이제한 / 128byte)					
															</td>
						                                </tr>
						                                <tr align="center">
						                                    <th width="18%">시리즈 여부</th> 
						                                    <td width="1%"></td>
						                                    <td width="81%" align="left" >
																<select name="series_yn" id="series_yn">
																	<option value="Y" <c:if test="${parent_season.series_yn == 'Y'}">selected="selected"</c:if>>Y</option>
																	<option value="N" <c:if test="${parent_season.series_yn == 'N'}">selected="selected"</c:if>>N</option>
																</select>														 							
															</td>
						                                </tr>
						                                <tr align="center" >
						                                    <th width="18%">시즌 상세</th>
						                                    <td width="1%"></td>
						                                    <td width="81%" align="center" >
						                                    	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list">
						                                    		<thead>
						                                    			<tr>
						                                    				<th width="30%" align="center">시즌명</th>
						                                    				<th width="40%" align="center">앨범명</th>
						                                    				<th width="30%"></th>
						                                    			</tr>
						                                    			<tr>
						                                    				<td><input type="text" id="season_name" name="season_name" size="30" style="font-size: 12px;" value="" onKeyUp="checkByte($(this),'64')"></td>
						                                    				<td id="textHtml"></td>
						                                    				<td><div align="center"><span class="button small blue" id="searchBtn">앨범선택</span><span class="button small blue" id="addBtn">추가</span></div></td>
						                                    			</tr>
						                                    		</thead>
						                                    		<tbody id="seasonTr">
					                                    			</tbody>
						                                    	</table>
															</td>
						                                </tr>
						                                <tr align="center" id="categoryTr" style="display:none;">
						                                    <th width="18%">카테고리 확인</th>
						                                    <td width="1%"></td>
						                                    <td width="81%" align="center" id="categoryTd">
															</td>
						                                </tr>
					                                </tbody>
				                                </table>
					                            <!-- 등록/수정 버튼 -->
					                            <table border="0" cellpadding="0" cellspacing="0" width="900" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<c:if test="${isUpdate==0}"><a href="javascript:goInsert();"><span class="button small blue">등록</span></a></c:if>
						                                	<c:if test="${isUpdate==1}"><a href="javascript:goInsert();"><span class="button small blue">수정</span></a></c:if>
						                                	<a href="/smartux_adm/admin/season/seasonList.do?findName=${vo.findName}&findValue=${vo.findValue}&seriesYn=${vo.series_yn}&app_tp=${vo.app_tp }&pageNum=${vo.pageNum}"><span class="button small blue">목록</span></a> 
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