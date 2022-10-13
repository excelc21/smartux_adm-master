<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
function doSearch(){
	$("form[name=form1]").submit(); //폼 전송
}

$(document).ready(function(){
	// 삭제 버튼 클릭
	$("#deleteBtn").click(function(){
		var siteId_comma="";
		if(!$(".checkbox").is(":checked")){
			alert("삭제 할 항목을 선택하세요.");
			return;
		}else{
			var statCnt = 0;
            $(".checkbox").each(function () {
                if ($(this).is(":checked")) {
                	if(statCnt==0){
                		siteId_comma = siteId_comma+$(this).val();
                	}else{
                		siteId_comma = siteId_comma+","+$(this).val();
                	}
                	statCnt++;
                }
            });
		 }
		if(confirm("선택한 사이트를 삭제 하시겠습니까?")){	
		    $.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>처리 중..</b>"
			});
		    
		    $.ajax({
				url: './siteDelete.do',
				type:'POST',
				dataType: 'json',
				timeout : 30000,
			    data: {
			        "site_id": siteId_comma
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
					$(location).attr('href',"./siteList.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}"); 
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
			    error: function(){
			    	alert("작업 중 오류가 발생하였습니다");
			    	$.unblockUI();
			    },
			    success: function(rs){
			    	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
				 	var flag = rs.flag;
				 	var message = rs.message;
				 	if(flag == "0000"){// 정상적으로 처리된 경우
				 		alert("정상적으로 처리 되었습니다.");
				 		$.unblockUI();
				 	}else{
				 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
				 		$.unblockUI();
				 	}
			    }
			});
		}
	});
	
	//CJ 아이들나라 추가 2019.12.17 : CJ서버 즉시적용 추가
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
			    error: function(){
			    	alert("작업 중 오류가 발생하였습니다");
			    	$.unblockUI();
			    },
			    success: function(rs){
			    	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
				 	var flag = rs.flag;
				 	var message = rs.message;
				 	if(flag == "0000"){// 정상적으로 처리된 경우
				 		alert("정상적으로 처리 되었습니다.");
				 		$.unblockUI();
				 	}else{
				 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
				 		$.unblockUI();
				 	}
			    }
			});
		}
	});
	
	
});

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
											<form id="form1" method="get" action="./siteList.do">
			                                <table border="0" cellpadding="0" cellspacing="0" width="100%" class="search_table" >
			                                	<tbody >
			                                	<tr >
				                                    <td width="15">&nbsp;</td>
				                                    <td width="80"><img src="/smartux_adm/images/admin/search_title4.gif" border="0" height="47" width="62"></td>
				                                    <td>
					                                    <table border="0" cellpadding="0" cellspacing="0" width="300">		                                    
		                                        			<tbody>
		                                        			<tr><td>&nbsp;</td></tr>
		                                        			<tr>
					                                            <td width="10">
					                                            	<img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9" />
					                                            </td>
					                                            <td>
					                                                <select class="select"  name="findName">
					                                                    <option <c:if test="${vo.findName == 'SITE_NAME'}">selected="selected"</c:if> value="SITE_NAME">사이트명</option>					                                                    
					                                                </select>  
					                                            	<input type="text" name="findValue" value="${vo.findValue}" size="20" style="font-size: 12px;" onKeyPress="if(event.keyCode==13){ doSearch();}" />
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
							                                        <td class="bold">사이트 리스트</td>
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
			                                			   <th scope="col" width="5%">선택</th>
													 	   <th scope="col" width="15%">사이트 로고</th>
													 	   <th scope="col" width="15%">사이트명</th>
														   <th scope="col" width="13%">사이트URL</th>
						                                   <th scope="col" width="10%">등록일시</th>
						                                </tr>
						                                
						                                <c:if test="${list == '[]' }"> 
							                                <tr align="center">
							                                    <td class="table_td_04" colspan="5">데이터가 존재 하지 않습니다.</td>
							                                </tr>
														</c:if>
					                                </tbody>
					                                <c:forEach items="${list}" var="list">
						                            	<tr align="center">
						                            		<!-- 체크 박스 -->
						                                    <td class="table_td_04" >
						                                    	<input type="checkbox" class="checkbox" value="${list.site_id}" />
						                                    </td>
						                                    <!-- 사이트 로고 -->
						                                    <td class="table_td_04" >
						                                   		<img src="${list.site_img_tv}" width="130" height="60">
						                                    </td>
						                                    <!-- 사이트명 -->						                                    						                                    
						                                    <td class="table_td_04">
						                                    	<a href="./siteDetail.do?site_id=${list.site_id}&findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}">${list.site_name}</a>						                                    	 
						                                    </td>
						                                    <!-- 사이트URL -->
							                                <td class="table_td_04 ">
							                                	${list.site_url}
						                                    </td>
															<!-- 등록일시 -->
							                                <td class="table_td_04 ">
							                                	${fn:substring(list.reg_dt, 0, 19)} 
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
																<jsp:param value="siteList.do" name="actionUrl"/>
																<jsp:param value="?findName=${vo.findName}&findValue=${vo.findValue}" name="naviUrl" />
																<jsp:param value="${vo.pageNum }" name="pageNum"/>
																<jsp:param value="${vo.pageSize }" name="pageSize"/>
																<jsp:param value="${vo.blockSize }" name="blockSize"/>
																<jsp:param value="${vo.pageCount }" name="pageCount"/>																			  
															</jsp:include> 
						                            	</td>
						                            </tr>
						                        	</tbody>
						                        </table>
						                        
						                        <!-- 등록/수정 버튼 -->
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            <tbody>
						                            <tr>
						                                <td align="right">
						                                	<a href="./siteInsertForm.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}"><span class="button small blue">등록</span></a>
						                                	<span class="button small blue" id="deleteBtn">삭제</span>
						                                	<span class="button small blue" id="applybtn">즉시적용</span>
						                                	<span class="button small blue" id="cjApplybtn">CJ 즉시적용</span>
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
			<%@include file="/WEB-INF/views/include/bottom.jsp" %>	        
	        <!-- 하단 로그인 사용자 정보 종료 -->
	    </td>
	 </tr>
</tbody>
</table>
</div>
</body>
</html>