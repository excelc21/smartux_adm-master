<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<%
//rank_code Parameter Accept
String rank_code = request.getParameter("rank_code");
if(rank_code == null || rank_code == "") rank_code = "R0001";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
var category_window;
$(document).ready(function(){
	$("#category_btn").click(function(){
		view_category();
	});

	$("#image_btn").click(function(){
		view_image();
	});
	
	$("#addbtn").click(function(){
		var idx = $("#titlelist1 option").index($("#titlelist1 option:selected"));
		if(idx == -1){			// 선택한 항목이 없으면
			alert("추가하고자 하는 앨범을 선택해주세요");
		}else{					// 선택한 항목이 있으면
			var seloption = $("#titlelist1 option").eq(idx);
			var seltext = $(seloption).text();
			var selval = $(seloption).val();
			
			// 추가하고자 하는 것이 기존에 있는지를 확인한다
			var size = $("#titlelist2 option").size();
			var isexist = false;
			if(size != 0){
				$("#titlelist2 option").each(function(){
					if($(this).val() == selval){
						isexist = true;
					}
				});
			}
			
			if(isexist == true){
				alert("추가하고자 하는 앨범이 이미 설정되어 있습니다");
			}else{
				var html = "<option value=\"" + selval + "\">" + seltext + "</option>\n";
				$("#titlelist2").append(html);
			}			
		}
	});
	
	$("#delbtn").click(function(){
		var idx = $("#titlelist2 option").index($("#titlelist2 option:selected"));
		if(idx == -1){			// 선택한 항목이 없으면
			alert("삭제하고자 하는 앨범을 선택해주세요");
		}else{					// 선택한 항목이 있으면
			var seloption = $("#titlelist2 option").eq(idx);
			$(seloption).remove();
		}
	});
	
	$("#upbtn").click(function(){
		var idx = $("#titlelist2 option").index($("#titlelist2 option:selected"));
		var size = $("#titlelist2 option").size();
		if(idx == -1){			// 선택한 항목이 없으면
			alert("순서를 바꾸고자 하는 항목을 선택해주세요");
		}else{					// 선택한 항목이 있으면
			fnSortCateogry("titlelist2", "U");
		}
	});

	$("#downbtn").click(function(){
		var idx = $("#titlelist2 option").index($("#titlelist2 option:selected"));
		var size = $("#titlelist2 option").size();
		if(idx == -1){			// 선택한 항목이 없으면
			alert("순서를 바꾸고자 하는 항목을 선택해주세요");
		}else{					// 선택한 항목이 있으면
			fnSortCateogry("titlelist2", "D");
		}
	});
	
	$("#resetbtn").click(function(){
		location.reload();
	});
	
	$("#updbtn").click(function(){
		if($("#titlelist2 option").size() == 0){
			alert("편성 수정시 사용할 앨범을 설정해주세요");
		}else{
			var categoryarray = new Array();
			var albumarray = new Array();
			var smartUXManager = $("#smartUXManager").val();
			var rank_code = $("#rank_code").val();
			// 사용자가 설정한 앨범들 값들 읽어서 그 안에 있는 category_id와 album_id를 분리해낸다
			$("#titlelist2 option").each(function(){
				var selval = $(this).val();
				var temp = selval.split('^');
				categoryarray.push(temp[0]);
				albumarray.push(temp[1]);
			});
			
			$.post("<%=webRoot%>/admin/rank/updateAlbumVod.do", 
					{ rank_code : rank_code , album_id : albumarray, category_id : categoryarray, smartUXManager : smartUXManager, rule_type : '${rule_type}' },
					  function(data) {
						 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
						 	var flag = data.flag;
						 	var message = data.message;
						 	
						 	if(flag == "0000"){						// 정상적으로 처리된 경우
						 		alert("BEST VOD ALBUM이 수정되었습니다");
						 		opener.location.reload();
						 		if(category_window != null){
						 			category_window.close();
						 		}
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
						 	}else{
						 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
						 	}
					  },
					  "json"
			   );
		}
	});
	
	$("#closebtn").click(function(){
		 window.close();		
	});
})

function view_category(){
	var series_yn = "N";
	
	if('${rule_type}' == "S"){
		series_yn = "Y";
	}
	
	var url = "<%=webRoot%>/admin/schedule/getCategoryAlbumList.do?category_id=VC&albumElementid=titlelist1&type=" + "${result.category_gb}" + "&series_yn=" + series_yn;
	category_window = window.open(url, "getCategoryAlbumList", "width=560,height=400,scrollbars=yes");
}

function view_image(){
	var idx = $("#titlelist2 option").index($("#titlelist2 option:selected"));
	if(idx == -1){			// 선택한 항목이 없으면
		alert("앨범을 선택해주세요");
	}else{					// 선택한 항목이 있으면
		var category_album = $("#titlelist2").val().split("^");
		var category = category_album[0];
		var album = category_album[1];
	
		var url = "<%=webRoot%>/admin/schedule/getAlbumListByCategoryIdAlbumId.do?category_id="+category+"&album_id="+album+"&type="+"${result.category_gb}";
		category_window = window.open(url, "getAlbumListByCategoryIdAlbumId", "left=500,width=800,height=550,scrollbars=yes");
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
						                                            <td class="bold">랭킹 코드 별 앨범 변경</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="regfrm" name="regfrm" method="post" action="">
					                            <table border="0" cellpadding="0" cellspacing="0" width="600" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">랭킹 데이터명</th>
					                                    <td width="1%"></td>
					                                    <td width="74%" align="left" >
															<input type="text" id="schedule_name" name="schedule_name" size="35" maxlength="100" style="font-size: 12px;" value="${result.rank_name}" readonly/>								
														</td>
					                                </tr>
					                                <tr align="center">
					                                    
					                                    <td colspan="3" align="left" >
															<span class="button small blue" id="category_btn">카테고리 선택</span>
														</td>
					                                </tr>
					                                <tr align="center"  valign="top">
					                                   <td colspan="3" id="uiarea" align="center">
					                                   		<table border="0" cellspacing="0" cellpadding="0" width="600">
					                                   			<tr align="center">
						                                   			<td width="35%">
						                                   				<select id="titlelist1" name="titlelist1" size="20" style="width:200px;">
																			
																		</select>
						                                   			</td>
						                                   			<td width="15%">
						                                   				<span class="button small blue" id="addbtn">추가</span><br/><br/>
						                                   				<span class="button small blue" id="delbtn">삭제</span>
						                                   			</td>
						                                   			<td width="35%">
						                                   				<select id="titlelist2" name="titlelist2" size="20" style="width:200px;">
																			<c:choose>
																				<c:when test="${detail==null || fn:length(detail) == 0}">
																																														
																				</c:when>
																				<c:otherwise>
																					<c:forEach var="item" items="${detail}" varStatus="status">
																						<option value="${item.category_id}^${item.album_id}">${item.album_name}</option>
																					</c:forEach>
																				</c:otherwise>
																			</c:choose>
																		</select>
						                                   			</td>
						                                   			<td width="15%">
						                                   				<span class="button small red" id="image_btn">이미지</span><br/><br/>
						                                   				<span class="button small blue" id="upbtn">위로</span><br/><br/>
						                                   				<span class="button small blue" id="downbtn">아래로</span><br/><br/>
						                                   			</td>
					                                   			</tr>
					                                   		</table>
					                                   </td> 
					                                </tr>
					                                
					                            	</tbody>
					                            </table>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="600" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<span class="button small blue" id="updbtn">수정</span>	
						                                	<span class="button small blue" id="resetbtn">재작성</span>
						                                	<span class="button small blue" id="closebtn">닫기</span>		
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                       	<input type="hidden" id="schedule_code" name="schedule_code" value="1" />
						                       	<input type="hidden" id="rank_code" name="rank_code" value="<%=rank_code%>" />
						                       	<input type="hidden" id="smartUXManager" name="smartUXManager" value="<%=id_decrypt %>" />
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