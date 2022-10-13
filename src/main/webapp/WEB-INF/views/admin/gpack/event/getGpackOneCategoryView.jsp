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
	
	$("#savebtn").click(function (){
		fn_save_contents();
	});

	$("#resetbtn").click(function (){
		location.reload();
	});

	$("#closebtn").click(function (){
		self.close();
	});

	select_bind();

	//선택버튼
	$("#selectbtn").click(function (){
		fn_select_imcs_category();
	});
});

/**
 * 카테고리 연동 저장하기
 */
function fn_save_contents(){

	var use_yn			= $("input[name=use_yn]:checked").val();			//사용여부
	var imcs_category_id= $("#imcs_category_id").val();	//IMCS 카테고리

	if(!use_yn){
		alert("사용여부를 선택해주세요");
		$("input[name=use_yn]").focus();
	}else if(!imcs_category_id){
		alert("카테고리를 선택해주세요");
		$("select[name='category_select']").eq($("select[name='category_select']").length-1).focus();
	}else{
		var postUrl = "<%=webRoot%>/admin/gpack/event/updateGpackOneCategoryView.do";
		$("#frmVodAuto").ajaxSubmit({
			dataType : 'json',
			url : postUrl,
			success : function(result) {
			 	var flag = result.flag;
				var message = result.message;

			 	if(flag == "0000"){						// 정상적으로 처리된 경우
					alert("저장되었습니다.");
					location.reload();
				}else{
			 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
			 	}
			},
			error : function(data, status, err) {
			 	var flag = data.flag;
			 	var message = data.message;
				alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
			}
		});
		
	}
}

/**
 * Category Selectbox 만들기 (전체 감싸는 div = category_select)
 */
var imcs_contents_yn = false;	//IMCS 카테고리의 하위 앨범이 있는지 체크 
function select_bind(){
	imcs_contents_yn = false;

	$("select[name='category_select']").unbind("change");
	
	$("select[name='category_select']").bind("change", function(){
		var imcs_category_id = $(this).val();

		// 현재 select 태그의 부모 이후에 나오는 모든 select를 구한다
 		var selObj = $("div#div_category_select select");
 		var length = selObj.length;
 		var bol_remove = false;
 		for(var i=0; i<length; i++){
			if(bol_remove){
				$(selObj[i]).remove();
			}
			if($(selObj[i]).val() === imcs_category_id){
				bol_remove = true;
			}
 	 	}
		
		//선택한 IMCS카테고리 값 초기화
		//$("#imcs_category_id").val("");
		//$("#imcs_category_text").val("");
		
		if(imcs_category_id == "---"){		// 선택해주세요를 선택한 경우
		}else{
			$.post("<%=webRoot%>/admin/gpack/contents/getGpackImcsCategoryList.do", 
					 {category_id : imcs_category_id},
					  function(data) {
						 	if(data.length == 0){
						 	}else{
						 		var html = [], inHtml = [], h=-1, ih=-1;
						 		for(var i=0 ; i < data.length; i++){
							 		if(data[i].contents_id){ 
								 		//TODO :: 카테고리 선택시 여기를 안 타면 (콘텐츠 ID가 없으면) 저장 못하게 체크!
							 			imcs_contents_yn = true;
								 	}else{
								 		inHtml[++ih] = "<option value=\"" + data[i].category_id + "\">" + data[i].category_name + "</option>\n";
								 	}
						 		}

						 		if(inHtml.length > 0) {

							 		html[++h] = "<select name=\"category_select\" style=\"float: left;\" >\n";
							 		html[++h] = "<option value=\"---\" selected>선택해주세요</option>\n";
							 		html[++h] = inHtml;
							 		html[++h] = "</select>\n";
							 		
						 			$("#div_category_select").append(html.join(''));

							 		select_bind();
						 		}
						 	}
					  },
					  "json"
		    );
		}
	});
}

function fn_select_imcs_category(){

	var selectLen = $("select[name='category_select']").length;
	var tmpVal = "";
	var selectVal = "";
	var selectText = "";

	for(var i=0; i<selectLen; i++){
		<%--
		//어느 단계의 카테고리든지 선택 가능함 
		tmpVal = $("select[name='category_select']").eq(i).val();
		if(tmpVal !== "---"){
			selectVal = tmpVal;
			selectText = $("select[name='category_select'] option:selected").eq(i).text();
		}
		--%>

		//제일 하위 카테고리만 선택 가능함
		selectVal = $("select[name='category_select']").eq(i).val();
		selectText = $("select[name='category_select'] option:selected").eq(i).text();
	}


	//alert("selectVal >> "+selectVal+" , imcs_contents_yn >> "+imcs_contents_yn);
	if(selectVal === "---"){
		alert("하위 카테고리를 선택해주세요.");
		$("select[name='category_select']").eq($("select[name='category_select']").length-1).focus();
	}else if(!imcs_contents_yn){
		alert("해당 카테고리는 콘텐츠(앨범)가 없습니다.\n다른 카테고리를 선택해주세요.");
		$("select[name='category_select']").eq($("select[name='category_select']").length-1).focus();
	}else{
		$("#imcs_category_id").val(selectVal);
		$("#imcs_category_text").val(selectText);
		alert(selectText+" 카테고리를 선택하였습니다.");
	}
}

</script>
</head>
<body leftmargin="0" topmargin="0" style="font-size: 12px;">
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
						                                        <tr><c:if test="${promotionVO ne null}"><c:if test="${promotionVO.category_nm ne ''}">
						                                        	<td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
						                                            <td width="100px" style="vertical-align: top;overflow:hidden;white-space:nowrap;" class="bold">${promotionVO.category_nm}</td>
						                                        </c:if></c:if>
						                                            <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
						                                            <td class="bold">콘텐츠 자동 편성</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="frmVodAuto" name="frmVodAuto" method="post" action="/smartux_adm/admin/gpack/category/InsertCategory.do">
						                        <input type="hidden" id="pack_id"		name="pack_id"		value="${pack_id }"/>
						                        <input type="hidden" id="category_id"	name="category_id"	value="${category_id }"/>
						                        <input type="hidden" id="auto_set_id"	name="auto_set_id"	value="${autoVO.auto_set_id }"/>
												<input type="hidden" id="smartUXManager" name="smartUXManager" value="<%=id_decrypt %>" />
					                            <table border="0" cellpadding="0" cellspacing="0" width="450px" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">사용여부</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<input type="radio" id="use_ynY" name="use_yn" value="Y" <c:if test="${autoVO.use_yn eq 'Y' }">checked="checked"</c:if> /><label for="use_ynY">예</label>
					                                    	<input type="radio" id="use_ynN" name="use_yn" value="N" <c:if test="${autoVO.use_yn ne 'Y' }">checked="checked"</c:if> /><label for="use_ynN">아니오</label>
														</td>
					                                </tr>
					                                
					                                <tr align="center">
					                                    <th width="25%">카테고리</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<input type="text"  size="35" name="imcs_category_text" id="imcs_category_text" value="${autoVO.imcs_category_text }" disabled="disabled"/>
					                                    	<input type="hidden" name="imcs_category_id" id="imcs_category_id" value="${autoVO.imcs_category_id }"/>
					                                    	<div id="div_category_select">
								                            	
								                            	<!-- admin/schedule/getCategoryAlbumList -->
																<c:choose>
																	<c:when test="${categoryResult==null || fn:length(categoryResult) == 0}">
																		<div>검색된 카테고리가 없습니다</div>
																	</c:when>
																	<c:otherwise>
																		<select name="category_select" style="float: left;">
																			<option value="---">선택해주세요</option>
																		<c:forEach var="item" items="${categoryResult}" varStatus="status">
																			<option value="${item.category_id}">${item.category_name }</option>
																		</c:forEach>
																		</select>
																	</c:otherwise>
																</c:choose>
								                            	<!-- admin/schedule/getCategoryAlbumList -->
								                            </div>
								                            <!-- 카테고리 선택버튼 -->
								                            <span class="button small blue" id="selectbtn" >선택</span>
														</td>
					                                </tr>
					                            	</tbody>
					                            </table>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="450" align="left" style="padding-top: 20px;">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<span class="button small blue" id="savebtn">저장</span>	 
						                                	<span class="button small blue" id="resetbtn">재작성</span>
						                                	<span class="button small blue" id="closebtn">닫기</span>	
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
</tbody>
</table>
</div>

</body>
</html>