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
		fn_save_category();
	});

	//초기화
	$("#resetbtn").click(function (){
		//frm_category.reset();
		location.reload();
	});

	//닫기 버튼
	$("#closebtn").click(function (){
		self.close();
	});

	select_bind();

	//선택버튼
	$("#btn_category").click(function (){
		fn_select_imcs_category();
	});
});

/*
 * 저장 
 */
function fn_save_category(){

	var category_nm 	= $("#category_nm").val();		//카테고리 명
	var use_yn			= $("input[name=use_yn]:checked").val();			//사용여부
	var imcs_category_id= $("#imcs_category_id").val();	//IMCS 카테고리
	

	if(!category_nm){
		alert("카테고리명을 입력해주세요.");
		$("#category_nm").focus();
	}else if(!use_yn){
		alert("사용여부를 선택해주세요");
		$("input[name=use_yn]").focus();
	}else if(!imcs_category_id){
		alert("카테고리를 선택해주세요");
		$("select[name='category_select']").eq($("select[name='category_select']").length-1).focus();
	}else{
		
		var postUrl = "<%=webRoot%>/admin/gpack/category/InsertGpackCategory.do";
		$("input[name=auto_yn]").attr("disabled", false); 
	
		$("#frm_category").ajaxSubmit({
			dataType : 'json',
			url : postUrl,
			success : function(result) {
			 	var flag = result.flag;
				var message = result.message;
	
			 	if(flag == "0000"){						// 정상적으로 처리된 경우
			 		opener.location.reload();
					alert("저장되었습니다.");
					self.close();
				}else if(flag == "NOT FOUND CONTENTS"){
				 	alert("카테고리를 사용하려면 하위 콘텐츠가 필요합니다.\n사용여부를 [아니오]로 저장하시고, 하위콘텐츠를 등록하신 후에 사용여부에 [예]를 체크해주세요.");
				}else{
			 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
			 	}
			},
			error : function(data, status, err) {
			 	var flag = data.flag;
			 	var message = data.message;
				alert("error 작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
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
 		var selObj = $("div#div_category_select select")
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
		$("#imcs_category_id").val("");
		$("#imcs_category_text").val("");
		
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
						                                        <tr>
						                                            <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
						                                            <td class="bold"><c:out value="${categoryVO.category_nm}"/> 카테고리 상세조회</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="frm_category" name="frm_category" method="post" action="/smartux_adm/admin/gpack/category/InsertCategory.do">
						                        <input type="hidden" id="view_gb"		name="view_gb"		value="${param.view_gb }"/>
						                        <input type="hidden" id="pack_id"		name="pack_id"		value="${categoryVO.pack_id }"/>
						                        <input type="hidden" id="category_id"	name="category_id"	value="${categoryVO.category_id }"/>
						                        <input type="hidden" id="ordered"		name="ordered"		value="${categoryVO.ordered }"/>
												<input type="hidden" id="smartUXManager" name="smartUXManager" value="<%=id_decrypt %>" />
												<input type="hidden" id="auto_yn"		name ="auto_yn"		value="Y"/>	<!-- 카테고리 자동여부 (직접편성 없어져서 Y로 고정) -->
												
					                            <table border="0" cellpadding="0" cellspacing="0" width="400" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">카테고리명</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
						                                    <input type="text" id="category_nm" name="category_nm" value="${categoryVO.category_nm}" size="35" maxlength="50" style="font-size: 12px;" />
														</td>
					                                </tr>
					                                
					                                <tr align="center">
					                                    <th width="25%">카테고리 멘트</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
						                                    <input type="text" id="category_comment" name="category_comment" value="${categoryVO.category_comment}" size="35" maxlength="150" style="font-size: 12px;" />
														</td>
					                                </tr>
					                                
					                                <tr align="center">
					                                    <th width="25%">사용여부</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<input type="radio" name="use_yn" value="Y" <c:if test="${categoryVO.use_yn eq 'Y' }">checked="checked"</c:if> />예
					                                    	<input type="radio" name="use_yn" value="N" <c:if test="${categoryVO.use_yn ne 'Y' }">checked="checked"</c:if> />아니오
					                                    	<br>
														</td>
					                                </tr>
					                                
					                                <tr align="center">
					                                    <th width="25%">카테고리</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<input type="text" name="imcs_category_text" id="imcs_category_text" value="${autoVO.imcs_category_text }" disabled="disabled"/>
														</td>
					                                </tr>
					                                
					                            	</tbody>
					                            </table>
					                            
					                            <div id="div_category_select">
					                            	<input type="hidden" name="imcs_category_id" id="imcs_category_id" value="${autoVO.imcs_category_id }"/>
					                            	
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
					                            <span class="button small blue" id="btn_category">선택</span>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="400" align="left">
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