<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<title>LG U+ IPTV</title>

<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<script type="text/javascript">
function doSearch(){
	$("form[name=form1]").submit(); //폼 전송
}

$(document).ready(function(){
	imagePreview();
	
	$('#closebtn').click(function(){
		//$(opener.location).attr('href',"./contentList.do?findName=&findValue=&pageNum=&delYn=${vo.delYn}");
		self.close();
	});
	// 삭제 버튼 클릭
	$("#deleteBtn").click(function(){
		var contentId_comma="";
		if(!$(".checkbox").is(":checked")){
			alert("삭제 할 항목을 선택하세요.");
			return;
		}else{
			var statCnt = 0;
            $(".checkbox").each(function () {
                if ($(this).is(":checked")) {
                    if ($(this).val().length != 2){
                		if(statCnt==0){
                    		contentId_comma = contentId_comma+$(this).val();
                    	}else{
                    		contentId_comma = contentId_comma+","+$(this).val();
                    	}
                    	statCnt++;
                	}
                }
            });
		 }
		if(confirm("선택한 컨텐츠를 삭제 하시겠습니까?\n*하위 컨텐츠가 존재할 경우 하위 컨텐츠까지 전부 삭제됩니다.")){	
		    $.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>처리 중..</b>"
			});
		    
		    $.ajax({
				url: './contentDelete.do',
				type:'POST',
				dataType: 'json',
				timeout : 30000,
			    data: {
			        "content_id": contentId_comma,
			        "parent_id" : $("#parent_id li").attr('id')
			    },
				success:function(data){
					if(data.result.flag=="0000"){
						alert("정상적으로 처리 되었습니다.");
					}else{
						alert(data.result.message);
					}
				},
				error:function(){
					alert("정상적으로 처리되지 않았습니다.");
				},
				complete:function(){
					//$.unblockUI();
					$(location).attr('href',"./openSubListPop.do?content_id=${vo.content_id}&parent_id=${vo.parent_id}"); 
				}
			});
		}
	});
	
 	$("#copyBtn").click(function(){
		
		var contentId_comma="";
		if(!$(".checkbox").is(":checked")){
			alert("복사 할 항목을 선택하세요.");
			return;
		}else{
			var statCnt = 0;
            $(".checkbox").each(function () {
                if ($(this).is(":checked")) {
                	 if ($(this).val().length != 2){
	            		var index = $(this).val().indexOf("!^");
	            		var temp_id = $(this).val().substring(0, index);
	                	if(statCnt==0){
	                		contentId_comma = contentId_comma+temp_id;
	                	}else{
	                		contentId_comma = contentId_comma+","+temp_id.replace("!^","");
	                	}
	                	statCnt++;
                	 }
                }
            });            
		 }
		
		var copyChk = true; 
		$("#checkbox:checked").each(function() {
			var trNum = $(this).closest('tr').prevAll().length;
			var content_type = $("#content_type"+trNum).val();
			if (content_type=="C") {
				copyChk = false;
			}
		});
		if (!copyChk) {
			alert("카테고리는 복사할 수 없습니다.");
			return false;
		}
			
		var url = "<%=webRoot%>/admin/hotvod/openCategoryPop.do?content_type=C&choice_type=S&parent_id=${vo.parent_id}&content_id="+contentId_comma;
    	category_window = window.open(url, "openCategoryPop", "width=600,height=550,scrollbars=yes");
	});
 	
	$("#excelBtn").click(function () {
		var findName = $("select[name=findName]").val();	
		var findValue = $("input[name=findValue]").val();	
		var delYn = $("input[name=delYn]").val();
		location.href = '<%=webRoot%>/admin/hotvod/contents/downloadExcelFile.do?parent_id=${vo.parent_id}&findName='+findName+'&findValue='+findValue+'&delYn='+delYn;
	}); 	
	
});

function allcheckfn(obj){
	if(obj.is(':checked') == true){
		$('.checkbox').each(function (){
			this.checked = true;
		});
	}else{
		$('.checkbox').each(function (){
			this.checked = false;
		});
	}
}

function pageChange(obj){
	pageSize = obj.val();
	$(location).attr('href',"./openSubListPop.do?findName=${vo.findName}&findValue=${vo.findValue}&content_id=${vo.content_id}&parent_id=${vo.parent_id}&delYn=${vo.delYn}&pageNum=1&pageSize="+pageSize);
}

function imagePreview(){	
	xOffset = 10;
	yOffset = 30;
		
	$("a.preview").hover(function(e){
		this.t = this.title;
		this.title = "";	
		var c = (this.t != "") ? "<br/>" + this.t : "";
		$(this).append("<p id='preview'><img src='"+ this.rel +"' alt='Image preview' />"+ c +"</p>");
		$("#preview")
			.css("top",(e.pageY - xOffset) + "px")
			.css("left",(e.pageX + yOffset) + "px")
			.css("position","absolute")
			.css("z-index","1")
			.fadeIn("fast");	
		
    },
	function(){
		this.title = this.t;	
		$("#preview").remove();
    });	
	$("a.preview").mousemove(function(e){
		$("#preview")
			.css("top",(e.pageY - xOffset) + "px")
			.css("left",(e.pageX + yOffset) + "px")
			.css("position","absolute")
			.css("z-index","1");
	});			
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
                	<td>
                	<!-- ######################## body start ######################### -->
                		<table border="0" cellpadding="15" cellspacing="0" width="100%" align="center" >
                    		<tbody>
                    		<tr>
                    			<td>
                    				<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" >
			                            <tbody>
			                            <tr>
			                            <td>
			                                <!-- 검색 시작 -->
											<form id="form1" method="get" action="./openSubListPop.do">
			                                <table border="0" cellpadding="0" cellspacing="0" width="100%" class="search_table" >
			                                	<tbody >
			                                	<tr >
				                                    <td width="15">&nbsp;</td>
				                                    <td width="80"><img src="/smartux_adm/images/admin/search_title4.gif" border="0" height="47" width="62"></td>
				                                    <td>
					                                    <table border="0" cellpadding="0" cellspacing="0" width="350">		                                    
		                                        			<tbody>
		                                        			<tr><td>&nbsp;</td></tr>
		                                        			<tr>
					                                            <td width="10">
					                                            	<img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9" />
					                                            </td>
					                                            <td>
					                                                <select class="select"  name="findName">
					                                                    <option <c:if test="${vo.findName == 'CONTENT_NAME'}">selected="selected"</c:if> value="CONTENT_NAME">컨텐츠명</option>
					                                                    <option <c:if test="${vo.findName == 'SITE_NAME'}">selected="selected"</c:if> value="SITE_NAME">사이트명</option>
					                                                </select>  
					                                            	<input type="text" name="findValue" value="${vo.findValue}" size="20" style="font-size: 12px;" onKeyPress="if(event.keyCode==13){ doSearch();}" />
					                                            	<input type="hidden" name="content_id" value="${vo.content_id }"/>
					                                            	<input type="hidden" name="parent_id" value="${vo.parent_id }">
					                                            	<input type="hidden" name="delYn" value="${vo.delYn }">
					                                            </td>					
					                                            <td width="66" align="left">
					                                                <input src="/smartux_adm/images/admin/search.gif" border="0" height="22" type="image" width="65">
					                                            </td>
					                                         </tr>
			                                    			</tbody>
			                                    		</table>
			                                    	</td>
					                            </tr> 					                            
					                        	</tbody>
					                        </table>
					                        </form>
					                        <!-- 검색 종료 -->
					                    </td>
						                </tr>
						                <tr>
						                    <td class="3_line" height="1"></td>
						                </tr>
						                <tr>
						                    <td height="20"></td>
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
							                                        <td class="bold">${list[0].parent_name }</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>						                                						                                
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list">
					                                <tbody>
						                                <tr align="center">
			                                			   <th scope="col" width="3%">
			                                			   <c:if test="${list != '[]' }">
			                                			   	<input type="checkbox" class="checkbox" onclick="allcheckfn($(this))">
			                                			   </c:if>
			                                			   </th>
			                                			   <th scope="col" width="23%">컨텐츠명</th>
			                                			   <th scope="col" width="10%">컨텐츠 타입</th>
			                                			   <th scope="col" width="10%">컨텐츠 이미지</th>
			                                			   <th scope="col" width="20%">시리즈명</th>
						                                   <th scope="col" width="7%">사이트명</th>
						                                   <th scope="col" width="10%">조회수</th>
						                                   <th scope="col" width="10%">등록일시</th>
						                                   <th scope="col" width="10%">노출여부</th>
						                                </tr>
						                                <c:if test="${list == '[]' }"> 
							                                <tr align="center">
							                                    <td class="table_td_04" colspan="9">데이터가 존재 하지 않습니다.</td>					                                    
							                                </tr>
														</c:if>
					                                </tbody>
					                                <c:forEach items="${list}" var="list" varStatus="status">
						                            	<tr align="center">
						                            		<!-- 체크 박스 -->
						                                    <td class="table_td_04" >
																<ol id="parent_id" style="display:none;"><li id="${list.parent_id}"></li></ol>
					                                			<input type="checkbox" id="checkbox" class="checkbox" value="${list.content_id}!^${list.content_type}">
						                                    </td>
						                                    <!--  컨텐츠명 -->
						                                    <td class="table_td_04 " style="text-align: left">
						                                    	<a href="./subDetail.do?content_id=${list.content_id}&content_type=${list.content_type}&findName=${vo.findName}&findValue=${vo.findValue}&parent_id=${vo.parent_id}&delYn=${vo.delYn}&rootId=${list.root_id}&serviceType=${vo.serviceType}&isLock=${vo.isLock}"><c:if test="${list.content_type eq 'C'}"><img src="/smartux_adm/images/folder.gif" width="20" height="20"></c:if> ${list.content_name }</a>
						                                    </td>
						                                    <td class="table_td_04 " align="center">
						                                    	<input type="hidden" id="content_type${status.index}" name="content_type${status.index}" value="${list.content_type}" />
						                                    	<c:if test="${list.content_type eq 'C' }">카테고리</c:if>
						                                    	<c:if test="${list.content_type eq 'V' }">컨텐츠</c:if>
																<c:if test="${list.content_type eq 'M' }">VOD 상세</c:if>
						                                    	<c:if test="${list.content_type eq 'N' }">VOD 카테고리</c:if>
						                                    	<c:if test="${list.content_type eq 'L' }">링크 URL</c:if>
						                                    </td>
						                                    <!--  컨텐츠 이미지 -->
						                                    <td class="table_td_04 ">
						                                    	<!-- 역사 항목 수정 및 IMCS VOD 추가 -->
						                                    	<c:if test="${list.content_type eq 'V' or list.content_type eq 'N' or list.content_type eq 'L'}">
						                                    		<c:if test="${not empty list.content_img_tv}"><a href="#!" class="preview" rel="${list.content_img_tv}"><span class="button small blue">이미지</span></a></c:if>
						                                    	</c:if>
						                                    	<c:if test="${list.content_type eq 'M' }">
						                                    		<c:choose>
						                                    			<c:when test="${not empty list.content_img_tv}">
						                                    				<a href="#!" class="preview" rel="${list.content_img_tv}"><span class="button small blue">이미지</span></a>
						                                    			</c:when>
						                                    			<c:otherwise>
						                                    				<c:if test="${not empty list.still_img}">
								                                    			<a href="#!" class="preview" rel="${list.still_img }"><span class="button small blue">이미지</span></a>
								                                    		</c:if>
						                                    			</c:otherwise>
						                                    		</c:choose>
						                                    	</c:if>
						                                    	<!-- 역사 항목 수정 및 IMCS VOD 추가 -->
						                                    </td>
						                                    <!-- 시리즈명 -->
						                                    <td class="table_td_04" align="left">
						                                    	${list.contents_name }
						                                    </td>
						                                    <!-- 사이트명 -->
						                                    <td class="table_td_04" >
						                                    	${list.site_name }
						                                    </td>
						                                    <!-- 조회수 -->
															<td class="table_td_04 ">
																<c:if test="${list.content_type ne 'C' }">${list.hit_cnt }</c:if>
															</td>
															<!-- 등록일시 -->
															<td class="table_td_04 ">
																${fn:substring(list.reg_dt, 0, 19)}
															</td>
															<!-- 노출여부 -->
															<td class="table_td_04 ">
																<c:if test="${list.del_yn eq 'Y' }">비노출</c:if>
																<c:if test="${list.del_yn eq 'N' }">노출</c:if>
															</td>
						                                </tr>
					                            	</c:forEach>
					                            </table>
					                            
					                            <!-- 페이징 -->
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
						                            <tbody>
						                            <tr>
						                                <td height="5"></td> 
						                            </tr>
						                            <tr>
						                            	<td align="center">						                            		
						                            		 <jsp:include page="/WEB-INF/views/include/naviControll.jsp">
																<jsp:param value="openSubListPop.do" name="actionUrl"/>
																<jsp:param value="?findName=${vo.findName}&findValue=${vo.findValue}&content_id=${vo.content_id}&parent_id=${vo.parent_id}&pageSize=${vo.pageSize}&blockSize=${vo.blockSize}&delYn=${vo.delYn }" name="naviUrl" />
																<jsp:param value="${vo.pageNum }" name="pageNum"/>
																<jsp:param value="${vo.pageSize }" name="pageSize"/>
																<jsp:param value="${vo.blockSize }" name="blockSize"/>
																<jsp:param value="${vo.pageCount }" name="pageCount"/>
																<jsp:param value="${vo.delYn }" name="delYn"/>
															</jsp:include> 
						                            	</td>
						                            </tr>
						                        	</tbody>
						                        </table>
						                        
						                        <!-- 등록/수정 버튼 -->
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            <tbody>
						                            <tr>
						                            	<td align="left">
						                            		목록 갯수 :
			                                            	<select class="select" onchange="pageChange($(this))">
			                                            		<option value="20" <c:if test="${vo.pageSize eq 20 }">selected="selected"</c:if>>20</option>
			                                            		<option value="50" <c:if test="${vo.pageSize eq 50 }">selected="selected"</c:if>>50</option>
			                                            		<option value="100" <c:if test="${vo.pageSize eq 100 }">selected="selected"</c:if>>100</option>
			                                            		<option value="200" <c:if test="${vo.pageSize eq 200 }">selected="selected"</c:if>>200</option>
			                                            		<option value="300" <c:if test="${vo.pageSize eq 300 }">selected="selected"</c:if>>300</option>
			                                            		<option value="400" <c:if test="${vo.pageSize eq 400 }">selected="selected"</c:if>>400</option>
			                                            		<option value="500" <c:if test="${vo.pageSize eq 500 }">selected="selected"</c:if>>500</option>
			                                            	</select>
						                            	</td>
						                                <td align="right">
						                                	<span class="button small blue" id="copyBtn">컨텐츠 복사</span>
						                                	<span class="button small blue" id="deleteBtn">삭제</span>
						                                	<span class="button small blue" id="closebtn">닫기</span>
						                                </td>
						                            </tr>
						                            <c:if test="${!empty vo.parent_id}">
						                            <tr height="40px;">
						                            	<td colspan="2" align="right">
						                            		<span class="button small blue" id="excelBtn">엑셀 다운로드</span>
						                            	</td>
						                            </tr>
						                            </c:if>					                            
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
	  <tr>
	    <td height="30">&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr>
	    <td colspan="2" background="/smartux_adm/images/admin/copy_bg.gif" height="60" align="left">
	        <!-- 하단 로그인 사용자 정보 시작 -->
	        
	        <!-- 하단 로그인 사용자 정보 종료 -->
	    </td>
	 </tr>
</tbody>
</table>
</div>
</body>
</html>