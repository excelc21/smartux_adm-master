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
var category_window;
$(document).ready(function(){
	var album_list_str = '${album_list_str}';
    $('#changeOrder').click(function () {

        var option_list = [];
        $('#titlelist2 option').each(function(){
            option_list.push($(this).val());
        })
        var option_list_str = option_list.join(", ");
        option_list_str = "["+option_list_str+"]";


        if(album_list_str == option_list_str){
            var schedule_code = $("#schedule_code").val();
            popupCenter('${pageContext.request.contextPath}/admin/schedule/changeScheOrder.do?schedule_code=' + schedule_code, '순서변경', 450, 300)
        }else{
            var schedule_code = $("#schedule_code").val();
            var schedule_name = $("#schedule_name").val();
            
            if(schedule_name == ""){
                alert("편성 이름을 입력해주세요");
                $("#schedule_name").focus();
            }else if($("#titlelist2 option").size() == 0){
                alert("편성 수정시 사용할 앨범을 설정해주세요");
            }else{
                var categoryarray = new Array();
                var albumarray = new Array();
                var smartUXManager = $("#smartUXManager").val();
                var category_gb = $("#category_gb").val();
                var test_id = $("#test_id").val();
                var variation_id = $("#variation_id").val();
                
                // 사용자가 설정한 앨범들 값들 읽어서 그 안에 있는 category_id와 album_id를 분리해낸다
                $("#titlelist2 option").each(function(){
                    var selval = $(this).val();
                    var temp = selval.split('^');
                    categoryarray.push(temp[0]);
                    albumarray.push(temp[1]);
                });
                
                $.post("<%=webRoot%>/admin/schedule/updateSchedule.do", 
                         {schedule_code : schedule_code, schedule_name : schedule_name, album_id : albumarray, category_id : categoryarray, smartUXManager : smartUXManager, category_gb : category_gb, test_id : test_id, variation_id : variation_id},
                          function(data) {
                                // 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
                                var flag = data.flag;
                                var message = data.message;
                                album_list_str = data.album_list_str;
                                
                                if(flag == "0000"){                     // 정상적으로 처리된 경우
                                    var schedule_code = $("#schedule_code").val();
                                    popupCenter('${pageContext.request.contextPath}/admin/schedule/changeScheOrder.do?schedule_code=' + schedule_code, '순서변경', 450, 300)
                                }else if(flag == "NOT FOUND SCHEDULE_NM"){
                                    alert("편성명은 필수로 들어가야 합니다");
                                    $("#schedule_name").focus();
                                }else if(flag == "SCHEDULE_NM LENGTH"){
                                    alert("편성명은 100자 이내이어야 합니다");
                                    $("#schedule_name").focus();
                                }else if(flag == "NOT FOUND ALBUM_ID"){
                                    alert("앨범이 입력되지 않았습니다");
                                }else if(flag == "DUPLICATION ALBUM_ID"){
                                    alert("동일한 앨범ID는 편성할 수 없습니다.");                                     
                                }else if(flag == "NOT FOUND CATEGORY_ID"){
                                    alert("카테고리가 입력되지 않았습니다");
                                }else{
                                    alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
                                }
                          },
                          "json"
                   );
            }
        }
    });
	
	$("#category_btn").click(function(){
		view_category();
	});
	
	$("#image_btn").click(function(){
		view_image();
	});
	
	$("#category_gb").change(function(){
		$("#titlelist2 option").remove();
	});
	
	// AB 테스트 검색 버튼
	$("#searchAbtestBtn").click(function(){
		window.open("<%=webRoot%>/admin/schedule/getABTestList.do", "abtestPop", "left=100,width=1600,height=700,resizable=yes,scrollbars=yes");
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
		var schedule_code = $("#schedule_code").val();
		var schedule_name = $("#schedule_name").val();
		
		if(schedule_name == ""){
			alert("편성 이름을 입력해주세요");
			$("#schedule_name").focus();
		}else if($("#titlelist2 option").size() == 0){
			alert("편성 수정시 사용할 앨범을 설정해주세요");
		}else{
			var categoryarray = new Array();
			var albumarray = new Array();
			var smartUXManager = $("#smartUXManager").val();
			var category_gb = $("#category_gb").val();
			var test_id = $("#test_id").val();
			var variation_id = $("#variation_id").val();
			
			// 사용자가 설정한 앨범들 값들 읽어서 그 안에 있는 category_id와 album_id를 분리해낸다
			$("#titlelist2 option").each(function(){
				var selval = $(this).val();
				var temp = selval.split('^');
				categoryarray.push(temp[0]);
				albumarray.push(temp[1]);
			});
			
			$.post("<%=webRoot%>/admin/schedule/updateSchedule.do", 
					 {schedule_code : schedule_code, schedule_name : schedule_name, album_id : albumarray, category_id : categoryarray, smartUXManager : smartUXManager, category_gb : category_gb, test_id : test_id, variation_id : variation_id},
					  function(data) {
						 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
						 	var flag = data.flag;
						 	var message = data.message;
						 	
						 	if(flag == "0000"){						// 정상적으로 처리된 경우
						 		alert("자체편성이 수정되었습니다");
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
						 	}else if(flag == "DUPLICATION ALBUM_ID"){
						 		alert("동일한 앨범ID는 편성할 수 없습니다.");								 		
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
	
})

function view_category(){
	var url = "<%=webRoot%>/admin/schedule/getCategoryAlbumList.do?category_id=VC&albumElementid=titlelist1&type=" + $("#category_gb").val();
	category_window = window.open(url, "getCategoryAlbumList", "left=500,width=550,height=200,scrollbars=yes");
}

function view_image(){
	var idx = $("#titlelist2 option").index($("#titlelist2 option:selected"));
	if(idx == -1){			// 선택한 항목이 없으면
		alert("앨범을 선택해주세요");
	}else{					// 선택한 항목이 있으면
		var category_album = $("#titlelist2").val().split("^");
		var category = category_album[0];
		var album = category_album[1];
	
		var url = "<%=webRoot%>/admin/schedule/getAlbumListByCategoryIdAlbumId.do?category_id="+category+"&album_id="+album+"&type=" + $("#category_gb").val();
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
						                                            <td class="bold">자체편성 상세조회</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="regfrm" name="regfrm" method="post" action="">
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">편성 이름</th>
					                                    <td width="1%"></td>
					                                    <td width="24%" align="left" >
															<input type="text" id="schedule_name" name="schedule_name" size="35" maxlength="100" style="font-size: 12px;" value="${main.schedule_name}" />								
														</td>
					                                    <th width="25%">테스트 ID</th>
					                                    <td width="1%"></td>
					                                    <td width="14%" align="left" >
															<input type="text" id="test_id" name="test_id" size="35" maxlength="32" style="font-size: 12px;" value="${main.test_id}" />								
														</td>
														<td>&nbsp;</td>
                           								<td width="9%" align="left">
                           									<span id="searchAbtestBtn" class="button small blue">AB 테스트 검색</span>
                           								</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">카테고리 구분</th>
					                                    <td width="1%"></td>
					                                    <td width="24%" align="left" >
															<select id="category_gb" name="category_gb">
																<option value="I20" <c:if test="${main.category_gb eq 'I20' }">selected="selected"</c:if>>I20</option>
																<option value="I30" <c:if test="${main.category_gb eq 'I30' }">selected="selected"</c:if>>I30</option>
															</select>
														</td>
					                                    <th width="25%">Variation ID</th>
					                                    <td width="1%"></td>
					                                    <td width="24%" align="left" colspan="3">
															<input type="text" id="variation_id" name="variation_id" size="35" maxlength="32" style="font-size: 12px;" value="${main.variation_id}" />								
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <td colspan="8" align="left" >
															<span class="button small blue" id="category_btn">앨범선택</span>
														</td>
					                                </tr>
					                                <tr align="center"  valign="top">
					                                   <td colspan="8" id="uiarea" align="center">
					                                   		<table border="0" cellspacing="0" cellpadding="0" width="100%">
					                                   			<tr align="center">
						                                   			<td width="40%">
						                                   				<select id="titlelist1" name="titlelist1" size="20" style="width:690px;height:100%;">
																			
																		</select>
						                                   			</td>
						                                   			<td width="15%">
						                                   			</td>
						                                   			<td width="20%" align="center">
						                                   				<span class="button small blue" id="addbtn">추가</span><br/><br/>
						                                   				<span class="button small blue" id="delbtn">삭제</span>	
						                                   			</td>
						                                   			<td width="40%">
						                                   				<select id="titlelist2" name="titlelist2" size="20" style="width:690px;height:100%;">
																			<c:choose>
																				<c:when test="${detail==null || fn:length(detail) == 0}">
																					
																				</c:when>
																				<c:otherwise>
																					<c:forEach var="item" items="${detail}" varStatus="status">
																						<option value="${item.category_id}^${item.album_id}">${item.album_name}<c:if test="${item.series_no ne null }">&nbsp&nbsp${item.series_no}</c:if>
																						</option>
																					</c:forEach>
																				</c:otherwise>
																			</c:choose>
																		</select>
						                                   			</td>
						                                   			<!-- <td width="15%">
						                                   				
						                                   				<span class="button small blue" id="upbtn">위로</span><br/><br/>
						                                   				<span class="button small blue" id="downbtn">아래로</span>	
						                                   			</td> -->
					                                   			</tr>
					                                   		</table>
					                                   </td> 
					                                </tr>
					                                
					                            	</tbody>
					                            </table>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="center">
						                                	<span class="button small blue" id="updbtn">수정</span>	
						                                	<span class="button small blue" id="resetbtn">재작성</span>	
						                                	<span class="button small blue" id="changeOrder">순서변경</span>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                       	<input type="hidden" id="schedule_code" name="schedule_code" value="${main.schedule_code}" />
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