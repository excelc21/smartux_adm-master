<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib prefix="hotvod" uri="/WEB-INF/tlds/hotvod.tld" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<title>LG U+ IPTV</title>

<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
$(document).ready(function(){
	imagePreview();
	
    $("#serviceType").change(function () {
    	var serviceType = $(this).val();
    	$(location).attr('href',"./contentList.do?findName=${vo.findName}&findValue=${vo.findValue}&delYn=${vo.delYn}&pageNum=1&pageSize=${vo.pageSize}&serviceType="+serviceType);
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
                	if(statCnt==0){
                		contentId_comma = contentId_comma+$(this).val();
                	}else{
                		contentId_comma = contentId_comma+","+$(this).val();
                	}
                	statCnt++;
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
			        "content_type": "C"
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
					$(location).attr('href',"./contentList.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&serviceType=${vo.serviceType}&isLock=${vo.isLock}"); 
				}
			});
		}
		
	});
	
	$("#applybtn").click(function(){
		var msg = "즉시적용 하시겠습니까?";
		if(confirm(msg)){
			$.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>로딩중..</b>"
			});
			$.ajax({
			    url: './hotvodApplyCache.do',
			    type: 'POST',
			    dataType: 'json',
			    data: {        
			    },
			    error: function(xhr, ajaxOptions, thrownError){
			    	console.log(xhr.status);
			    	console.log(xhr.responseText);
			    	console.log(thrownError);
			    	alert("작업 중 오류가 발생하였습니다");
			    	$.unblockUI();
			    },
			    success: function(rs){
			    	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
			    	console.log(rs);
                    var flag = rs.flag;
                    var message = rs.message;
                    if(flag === "0000"){// 정상적으로 처리된 경우
                        alert('정상처리 되었습니다.');
                    }else{
                        alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
                    }
                    $.unblockUI();
			    }
			});
		}
	});
	
	
	//CJ 아이들나라 추가 2019.12.17 : CJ서버 화제동영상 컨텐츠 즉시적용 추가
	$("#cjApplybtn").click(function(){
		var msg = "즉시적용 하시겠습니까?";
		if(confirm(msg)){
			$.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>로딩중..</b>"
			});
			$.ajax({
			    url: './cjHotvodApplyCache.do',
			    type: 'POST',
			    dataType: 'json',
			    data: {        
			    },
			    error: function(xhr, ajaxOptions, thrownError){
			    	console.log(xhr.status);
			    	console.log(xhr.responseText);
			    	console.log(thrownError);
			    	alert("작업 중 오류가 발생하였습니다");
			    	$.unblockUI();
			    },
			    success: function(rs){
			    	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
			    	console.log(rs);
                    var flag = rs.flag;
                    var message = rs.message;
                    if(flag === "0000"){// 정상적으로 처리된 경우
                        alert('정상처리 되었습니다.');
                    }else{
                        alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
                    }
                    $.unblockUI();
			    }
			});
		}
	});
	
});

function doSearch(){
	$("form[name=form1]").submit(); //폼 전송
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

function changebtnfn(content_id){
	var url = "<%=webRoot%>/admin/hotvod/openChangePop.do?content_id="+content_id+"&findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&pageSize=${vo.pageSize}&serviceType=${vo.serviceType}&isLock=${vo.isLock}&delYn=${vo.delYn}";
   	category_window = window.open(url, "openChangePop", "width=450,height=600,scrollbars=no");
}

function sublistbtnfn(content_id){
	var url = "<%=webRoot%>/admin/hotvod/openSubListPop.do?content_id="+content_id+"&parent_id="+content_id+"&serviceType=${vo.serviceType}&isLock=${vo.isLock}&delYn="+$("#delYn").val();
   	category_window = window.open(url, "openSubListPop", "width=1350,height=800,scrollbars=yes");
}

function pageChange(obj){
	pageSize = obj.val();
	$(location).attr('href',"./contentList.do?findName=${vo.findName}&findValue=${vo.findValue}&delYn=${vo.delYn}&pageNum=1&pageSize="+pageSize+"&serviceType=${vo.serviceType}&isLock=${vo.isLock}");
}

//검수 단말기 정보 캐쉬
function getTestServerUser(){
	if(confirm("검수 단말기 정보를 갱신 하시겠습니까?")){
		$.blockUI({
			blockMsgClass: "ajax-loading",
			showOverlay: true,
			overlayCSS: { backgroundColor: '#CECDAD' } ,
			css: { border: 'none' } ,
			 message: "<b>로딩중..</b>"
		});
		$.ajax({
		    url: '/smartux_adm/admin/hotvod/testServerUserCache.do',
		    type: 'POST',
		    dataType: 'json',
		    /*timeout : 30000,*/
			async: true,
		    data: {
		    },
		    error: function(){
		    	alert("작업 중 오류가 발생하였습니다");
		 		$.unblockUI();
		    },
		    success: function(rs){
		    	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
			 	var flag = rs.flag;
			 	var message = rs.message;
			 	console.log(rs);
			 	if(flag == "0000"){// 정상적으로 처리된 경우
			 		alert('정상처리 되었습니다.');
			 	}else if(flag.match('1502')){
		 			alert("상용적용 진행중입니다. 잠시 후 확인해주세요. ");
			 		window.location.reload();	
			 	}else{
			 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
			 	}
                $.unblockUI();
		    }
		});
	}
}

//CJ 아이들나라 추가 2019.12.17 : CJ서버 검수 단말기 정보 갱신 즉시적용 추가
function fnSetCjTestDevice(){
	if(confirm("검수 단말기 정보를 갱신 하시겠습니까?")){
		$.blockUI({
			blockMsgClass: "ajax-loading",
			showOverlay: true,
			overlayCSS: { backgroundColor: '#CECDAD' } ,
			css: { border: 'none' } ,
			 message: "<b>로딩중..</b>"
		});
		$.ajax({
		    url: '/smartux_adm/admin/hotvod/setCjTestDeviceCache.do',
		    type: 'POST',
		    dataType: 'json',
		    /*timeout : 30000,*/
			async: true,
		    data: {
		    },
		    error: function(){
		    	alert("작업 중 오류가 발생하였습니다");
		 		$.unblockUI();
		    },
		    success: function(rs){
		    	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
			 	var flag = rs.flag;
			 	var message = rs.message;
			 	console.log(rs);
			 	if(flag == "0000"){// 정상적으로 처리된 경우
			 		alert('정상처리 되었습니다.');
			 	}else if(flag.match('1502')){
		 			alert("상용적용 진행중입니다. 잠시 후 확인해주세요. ");
			 		window.location.reload();	
			 	}else{
			 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
			 	}
                $.unblockUI();
		    }
		});
	}
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
                <tr style="display:block">
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
                		<table border="0" cellpadding="15" cellspacing="0" width="100%" align="center" >
                    		<tbody>
                    		<tr>
                    			<td>
                    				<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" >
			                            <tbody>
			                            <tr>
			                            <td>
			                                <!-- 검색 시작 -->
											<form id="form1" method="get" action="./contentList.do">
			                                <table border="0" cellpadding="0" cellspacing="0" width="100%" class="search_table" >
			                                	<tbody >
			                                	<tr >
				                                    <td width="15">&nbsp;</td>
				                                    <td width="80"><img src="/smartux_adm/images/admin/search_title4.gif" border="0" height="47" width="62"></td>
				                                    <td>
					                                    <table border="0" cellpadding="0" cellspacing="0" width="400">		                                    
		                                        			<tbody>
		                                        			<tr><td>&nbsp;</td></tr>
		                                        			<tr>
					                                            <td width="10">
					                                            	<img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9" />
					                                            </td>
					                                            <td>
					                                                <select class="select"  name="findName" id="findName">
					                                                    <option <c:if test="${vo.findName == 'CONTENT_NAME'}">selected="selected"</c:if> value="CONTENT_NAME">컨텐츠명</option>
					                                                    <option <c:if test="${vo.findName == 'SITE_NAME'}">selected="selected"</c:if> value="SITE_NAME">사이트명</option>
					                                                    <option <c:if test="${vo.findName == 'PARNT_NAME'}">selected="selected"</c:if> value="PARNT_NAME">상위 컨텐츠명</option>
					                                                </select>  
					                                            	<input type="text" name="findValue" id="findValue" value="${vo.findValue}" size="20" style="font-size: 12px;" onKeyPress="if(event.keyCode==13){ doSearch();}" />
					                                            </td>
					                                            <td>
					                                            	<select class="select" id="delYn" name="delYn">
					                                                    <option <c:if test="${vo.delYn == 'N'}">selected="selected"</c:if> value="N">노출</option>
					                                                    <option <c:if test="${vo.delYn == 'Y'}">selected="selected"</c:if> value="Y">비노출</option>
					                                                </select>
					                                            </td>	
					                                            <td width="66" align="left">
					                                                <input src="/smartux_adm/images/admin/search.gif" border="0" height="22" type="image" width="65">
					                                            </td>
					                                         </tr>
			                                    			</tbody>
			                                    		</table>
			                                    	</td>
													<td align="left" style="width: 30px; padding: 5px;">
														<c:choose>
															<c:when test="${vo.isLock eq true}">
																<c:forTokens items="${hotvodServiceList}" delims="|" var="sel">
															    	<c:set var="service" value="${fn:split(sel,'^')}" />
															    	<c:if test="${vo.serviceType eq service[0] }"><span>${service[1]}</span></c:if>
															    </c:forTokens>
															</c:when>
															<c:otherwise>
																<select class="select" name="serviceType" id="serviceType" style="width: 100px;">
															      <option <c:if test="${vo.serviceType eq null}">selected="selected"</c:if> value="">전체</option>
															      <c:forTokens items="${hotvodServiceList}" delims="|" var="sel">
															        <c:set var="service" value="${fn:split(sel,'^')}" />
															        <option <c:if test="${vo.serviceType eq service[0] }">selected="selected"</c:if> value="${service[0]}">${service[1]}</option>
															      </c:forTokens>
															    </select>
															</c:otherwise>
														</c:choose>
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
							                                        <td class="bold">카테고리 리스트 </td>
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
			                                			   <th scope="col" width="3%"><input type="checkbox" class="checkbox" onclick="allcheckfn($(this))"></th>
			                                			   <th scope="col" width="20%">컨텐츠명</th>
			                                			   <th scope="col" width="7%">컨텐츠 이미지</th>
						                                   <th scope="col" width="10%">사이트명</th>
						                                   <th scope="col" width="5%">서비스타입</th>
						                                   <th scope="col" width="7%">조회수</th>
						                                   <th scope="col" width="7%">등록일시</th>
						                                   <th scope="col" width="5%">노출여부</th>
						                                   <th scope="col" width="13%">기능버튼</th>
						                                </tr>
						                                <c:if test="${list == '[]' }"> 
							                                <tr align="center">
							                                    <td class="table_td_04" colspan="8">데이터가 존재 하지 않습니다.</td>					                                    
							                                </tr>
														</c:if>
					                                </tbody>
					                                <c:forEach items="${list}" var="list">
					                                	<c:set var="authCheck" value="true"/>
					                                	<c:if test="${authCheck eq 'true' }">
					                                		<c:set var="authCheck" value="false"/>
							                            	<tr align="center">
							                            		<!-- 체크 박스 -->
							                                    <td class="table_td_04" >
							                                    	<input type="checkbox" class="checkbox" value="${list.content_id }">
							                                    </td>
							                                    <!--  컨텐츠명 -->
							                                    <td class="table_td_04" style="text-align: left">
							                                    	<a href="./contentDetail.do?content_id=${list.content_id}&content_type=${list.content_type}&findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&pageSize=${vo.pageSize}&delYn=${vo.delYn}&rootId=${list.root_id}&serviceType=${vo.serviceType}&isLock=${vo.isLock}"><c:if test="${list.level != 1}">${list.level_pad }<img src="/smartux_adm/images/admin/subtitle.gif">&nbsp;&nbsp;</c:if><img src="/smartux_adm/images/folder.gif" width="20" height="20"> ${list.content_name }</a>
							                                    </td>
							                                    <!--  컨텐츠 이미지 -->
							                                    <td class="table_td_04">
																	<c:if test="${not empty list.content_img}"><a href="#!" class="preview" rel="${list.content_img }"><span class="button small blue">이미지</span></a></c:if>
							                                    </td>
							                                    <!-- 사이트명 -->
							                                    <td class="table_td_04">
							                                    	${list.site_name }
							                                    </td>
							                                    <!-- 서비스타입 -->
							                                    <td class="table_td_04" >
							                                    	${hotvod:drawServiceTypeName(list.multi_service_type)}
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
																<td class="table_td_04 ">
																	<!-- 하위 목록 버튼 -->
																	<c:if test="${list.content_type eq 'C'}"><span class="button small blue" onclick="sublistbtnfn('${list.content_id}')">하위 목록</span>
																		<!-- 순서 바꾸기버튼 -->
																		<span class="button small blue" onclick="changebtnfn('${list.content_id}')">순서 바꾸기</span>
																	</c:if>
																</td>
							                                </tr>
						                                </c:if>
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
																<jsp:param value="contentList.do" name="actionUrl"/>
																<jsp:param value="?findName=${vo.findName}&findValue=${vo.findValue}&pageSize=${vo.pageSize}&blockSize=${vo.blockSize}&delYn=${vo.delYn }&serviceType=${vo.serviceType}&isLock=${vo.isLock}" name="naviUrl" />
																<jsp:param value="${vo.pageNum }" name="pageNum"/>
																<jsp:param value="${vo.pageSize }" name="pageSize"/>
																<jsp:param value="${vo.blockSize }" name="blockSize"/>
																<jsp:param value="${vo.pageCount }" name="pageCount"/>
																<jsp:param value="${vo.delYn }" name="delYn"/>
																<jsp:param value="${vo.serviceType }" name="serviceType"/>
																<jsp:param value="${vo.isLock }" name="isLock"/>
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
				                                			<span class="button small blue" onclick="sublistbtnfn('')">하위 목록</span>
						                                	<span class="button small blue" onclick="changebtnfn('')">순서 바꾸기</span>
						                                	<a href="./contentInsertForm.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&serviceType=${vo.serviceType}&isLock=${vo.isLock}"><span class="button small blue">등록</span></a>
						                                	<span class="button small blue" id="deleteBtn">삭제</span>
						                                	<span class="button small blue" id="applybtn">즉시적용</span>
						                                	<span class="button small blue" id="cjApplybtn">CJ 즉시적용</span>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                       	<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            <tbody>
						                            <tr>
						                                <td height="5"></td> 
						                            </tr>
						                            <tr>
						                            	<td align="right">
						                            		<span class="button small blue" onClick="getTestServerUser()">검수단말기 정보갱신</span>
						                            		<span class="button small blue" onClick="fnSetCjTestDevice()">CJ 검수단말기 정보갱신</span>
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