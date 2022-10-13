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
		var rowGbn = "\f^";
		var colsGbn = "\b^";

		$(document).ready(function () {
			already_content();

			$("#choicebtn").click(function () {
				var choiceCts = "";
				var textHtml = "";
				var val = "";

				if($("#choiceContentList") && $("#choiceContentList").length) {
					var cc = 0;
					$.each($("#choiceContentList option"), function(key) {
						var ch_value = $("#choiceContentList option:eq("+key+")").attr("value");
						var ch_text = $("#choiceContentList option:eq("+key+")").text();
						if(cc==0){
							choiceCts = ch_value + colsGbn + ch_text;
							textHtml = ch_text;
						}else{
							choiceCts = choiceCts + rowGbn + ch_value + colsGbn + ch_text;
							textHtml = textHtml+"<br>"+ch_text;
						}
						cc++;
					});

					
				}

				
				if(choiceCts=="" || choiceCts=="---"){
					alert("상품을 선택해 주세요.");
				}else{
					if("${hiddenName}"!="") $(opener.document).find("#${hiddenName}").val(choiceCts);
					if("${textName}"!="") $(opener.document).find("#${textName}").val(textHtml);
					if("${textHtml}"!="") $(opener.document).find("#${textHtml}").html(textHtml);
					
					//부모창에 setChoiceData function이 있는지 확인 : 메뉴 카테고리에서 월정액상품 목록때문에 추가 2016.02.04
					if(typeof(opener.getMenuTreeList_setChoiceData) != "undefined") {
						if("${hiddenName}" == "link_detail"){
					 	    opener.getMenuTreeList_setChoiceData();
						}else if("${hiddenName}" == "link_detail2"){
                            opener.getMenuTreeList_setChoiceData2();
                        }
					 	
					 }
					self.close();
				}
			});

			$("#closebtn").click(function () {
				self.close();
			});

			$("#addbtn").click(function(){
				var choiceCnt = $("#contentList option:selected").size();
				var choiceSize = $("#choiceContentList option").size();

				if(choiceCnt <= 0){			// 선택한 항목이 없으면
					alert("추가하고자 하는 상품을 선택해주세요");
				}else {
					var checkFlag = false;
					for( var i=0; i<$('#choiceContentList option').size(); i++){
						var seloption = $("#choiceContentList option").eq(i);
						var selval = $(seloption).val();

						if("ALL" == selval){
							checkFlag = true;
						}
					}

					if(checkFlag == true){
						alert("월정액상품 리스트 선택시 다른 상품은 선택할 수 없습니다.");
					}else{
						for( var i=0; i<$('#contentList option').size(); i++){
							if( $("#contentList option:eq("+i+")").attr("selected")){
								// 선택한 항목이 있으면
								var seloption = $("#contentList option").eq(i);
								var seltext = $(seloption).text();
								var selval = $(seloption).val();

								if("ALL" == selval){
									$("#choiceContentList option").remove();
									var html = "<option value=\"" + selval + "\">" + seltext + "</option>\n";
									$("#choiceContentList").append(html);
									break;
								} else {
									var singleChoice = '<c:out value="${singleChoice}"/>';
									var isSingleChoice = singleChoice.toUpperCase() === "y".toUpperCase();

									if (isSingleChoice && (choiceSize+choiceCnt) > 1) {
										alert("상품선택은 최대 1개까지 가능합니다.");
										return;
									}
								}
								// 추가하고자 하는 것이 기존에 있는지를 확인한다
								var size = $("#choiceContentList option").size();
								var isexist = false;
								if(size != 0){
									$("#choiceContentList option").each(function(){
										if($(this).val() == selval){
											isexist = true;
										}
									});
								}

								if(isexist == true){
									//alert("추가하고자 하는 앨범이 이미 설정되어 있습니다");
								}else{
									var html = "<option value=\"" + selval + "\">" + seltext + "</option>\n";
									$("#choiceContentList").append(html);
								}
							}
						}
					}
				}
			});

			$("#delbtn").click(function(){
				//var idx = $("#choiceContentList option").index($("#choiceContentList option:selected"));
				var choiceCnt = $("#choiceContentList option:selected").size();

				if(choiceCnt <= 0){			// 선택한 항목이 없으면
					alert("삭제하고자 하는 상품을 선택해주세요");
				}else{					// 선택한 항목이 있으면
					$("#choiceContentList option:selected").remove();
					//for( var i=0; i<$('#choiceContentList option').size(); i++){
					//	if( $("#choiceContentList option:eq("+i+")").attr("selected")){
					//		$("#choiceContentList option").eq(i).remove();
					//	}
					//}
				}
			});
			$("#upbtn").click(function(){
				var choiceCnt = $("#choiceContentList option:selected").size();
				if(choiceCnt > 1){
					alert("순서변경은 하나씩 선택해서 변경해 주세요.");
				}else{
					var idx = $("#choiceContentList option").index($("#choiceContentList option:selected"));
					var size = $("#choiceContentList option").size();
					if(idx == -1){			// 선택한 항목이 없으면
						alert("순서를 바꾸고자 하는 항목을 선택해주세요");
					}else{					// 선택한 항목이 있으면
						fnSortCateogry("choiceContentList", "U");
					}
				}
			});

			$("#downbtn").click(function(){
				var choiceCnt = $("#choiceContentList option:selected").size();
				if(choiceCnt > 1){
					alert("순서변경은 하나씩 선택해서 변경해 주세요.");
				}else{
					var idx = $("#choiceContentList option").index($("#choiceContentList option:selected"));
					var size = $("#choiceContentList option").size();
					if(idx == -1){			// 선택한 항목이 없으면
						alert("순서를 바꾸고자 하는 항목을 선택해주세요");
					}else{					// 선택한 항목이 있으면
						fnSortCateogry("choiceContentList", "D");
					}
				}
			});
		});

		function already_content(){
			alreadyCnt = $(opener.document).find("#${hiddenName}").val();
			var choiceCnt_html = [];
			var cntArr = alreadyCnt.split(rowGbn);
			for(var i=0;cntArr.length > i;i++){
				if(cntArr[i]!=null && cntArr[i]!=""){
					var subArr = cntArr[i].split(colsGbn);
					if(subArr.length==2){
						choiceCnt_html[i] = "<option value=\"" + subArr[0] + "\">" + subArr[1] + "</option>\n";
					}
				}
			}
			$("#choiceContentList").html(choiceCnt_html.join(''));
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
					<td class="bold">월정액상품 선택</td>
				</tr>
				</tbody>
			</table>
		</td>
	</tr>
	</tbody>
</table>
<table>
	<tr>
		<td valign="top" width="304">
			<select id="contentList" name="contentList" size="10" style="width:500px;" multiple="multiple">
				<c:choose>
					<c:when test="${flatRateList==null || fn:length(flatRateList) == 0}">
						<div>검색된 월정액상품이 없습니다</div>
					</c:when>
					<c:otherwise>
						<option value="ALL" name="월정액상품 리스트">월정액상품 리스트</option>
						<c:forEach var="item" items="${flatRateList}" varStatus="status">
							<option value="${item.productID}" name="${item.productName}">${item.productName}(${item.productID})</option>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</select>
		</td>
		<td width="70" align="center">
			<span class="button small blue" id="addbtn">추가</span><br/><br/>
			<span class="button small blue" id="delbtn">삭제</span>
		</td>
		<td valign="top" width="304">
			<select id="choiceContentList" name="choiceContentList" size="10" style="width:500px;" max="20" multiple="multiple"></select>
		</td>
		<td width="70" align="center">
			<span class="button small blue" id="upbtn">위로</span><br/><br/>
			<span class="button small blue" id="downbtn">아래로</span>
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