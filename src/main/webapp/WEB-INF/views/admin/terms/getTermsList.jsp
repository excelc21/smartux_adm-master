<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
$(document).ready(function(){
	
	$("#deleteTermsBtn").click(function(){
		var regno_comma="";
		
		if(!$(".checkbox").is(":checked")){
			alert("삭제 할 항목을 선택하세요.");
			return;
		}else{
			var statCnt = 0;
            $(".checkbox").each(function () {
                if ($(this).is(":checked")) {
                	if(statCnt==0){
                		regno_comma = regno_comma+$(this).val();
                	}else{
                		regno_comma = regno_comma+","+$(this).val();
                	}
                	statCnt++;
                }
            });
		 }
		if(confirm("약관을 삭제 하시겠습니까?")){	
		    $.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>처리 중..</b>"
			});

		    $.ajax({
				url: './deleteTermsProc.do',
				type:'POST',
				dataType: 'json',
				timeout : 30000,
			    data: {
			        "terms_id": regno_comma
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
					$(location).attr('href',"./getTermsList.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&s_service_type=${vo.s_service_type}&s_service_gbn=${vo.s_service_gbn}&s_terms_gbn=${vo.s_terms_gbn}&s_display_yn=${vo.s_display_yn}"); 
				}
			});
		}
	});
	
	$("#applybtn").click(function(){
		var msg = "약관정보를 상용에 적용하시겠습니까?";
		if(confirm(msg)){
			$.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>로딩중..</b>"
			});
			$.ajax({
			    url: './termsApplyCache.do',
			    type: 'POST',
			    dataType: 'json',
			    data: {      
			    	callByScheduler :'A'
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
				 		alert("약관정보가 상용에 적용되었습니다.");
				 		$.unblockUI();
				 		window.location.reload();
				 	}else{
				 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
				 		$.unblockUI();
				 		window.location.reload();
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
                                    약관 관리
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
			                            <td>
			                                <!-- 검색 시작 -->
			                                <table border="0" cellpadding="0" cellspacing="0" width="100%" class="search_table">
			                                	<tbody>
			                                	<tr>
				                                    <td width="15">&nbsp;</td>
				                                    <td width="80"><img src="/smartux_adm/images/admin/search_title4.gif" border="0" height="47" width="62"></td>
				                                    <td>
														<form id="form1" method="get" action="./getTermsList.do">
					                                    <table border="0" cellpadding="0" cellspacing="0" width="650">		                                    
		                                        			<tbody>
		                                        			<tr><td>&nbsp;</td></tr>
		                                        			<tr>
					                                            <td width="10">
					                                            	<img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9" />
					                                            </td>
					                                            <td>
					                                                <select class="select" id="findName" name="findName">
					                                                    <option <c:if test="${vo.findName == 'TITLE'}">selected="selected"</c:if> value="TITLE">제목</option>
					                                                </select></td><td>
					                                            	<input type="text" name="findValue" value="${vo.findValue }" size="20" style="font-size: 12px;" />
					                                            	&nbsp;&nbsp;&nbsp;
					                                            	<select class="select" id="s_service_type" name="s_service_type">
					                                                    <option <c:if test="${vo.s_service_type == ''}">selected="selected"</c:if> value="">전체</option>
					                                                    <option <c:if test="${vo.s_service_type == 'AHOME'}">selected="selected"</c:if> value="AHOME">추천</option>
					                                                </select>
					                                                &nbsp;&nbsp;&nbsp;
					                                            	<select class="select" id="s_service_gbn" name="s_service_gbn">
					                                                    <option <c:if test="${vo.s_service_gbn == ''}">selected="selected"</c:if> value="">전체</option>
					                                                    <option <c:if test="${vo.s_service_gbn == '0'}">selected="selected"</c:if> value="0">서비스중</option>
					                                                    <option <c:if test="${vo.s_service_gbn == '1'}">selected="selected"</c:if> value="1">변경예정</option>
					                                                </select>
					                                                &nbsp;&nbsp;&nbsp;
					                                            	<select class="select" id="s_terms_gbn" name="s_terms_gbn">
					                                                    <option <c:if test="${vo.s_terms_gbn == ''}">selected="selected"</c:if> value="">전체</option>
					                                                    <option <c:if test="${vo.s_terms_gbn == '1'}">selected="selected"</c:if> value="1">이용약관</option>
					                                                    <option <c:if test="${vo.s_terms_gbn == '2'}">selected="selected"</c:if> value="2">개인정보 보호정책</option>
					                                                </select>
					                                            	&nbsp;&nbsp;&nbsp;
					                                            	<select class="select" id="s_display_yn" name="s_display_yn">
					                                                    <option <c:if test="${vo.s_display_yn == ''}">selected="selected"</c:if> value="">전체</option>
					                                                    <option <c:if test="${vo.s_display_yn == 'Y'}">selected="selected"</c:if> value="Y">노출</option>
					                                                    <option <c:if test="${vo.s_display_yn == 'N'}">selected="selected"</c:if> value="N">비노출</option>
					                                                </select>
					                                            </td>					
					                                            <td width="66" align="left"><input src="/smartux_adm/images/admin/search.gif" border="0" height="22" type="image" width="65"></td>
		                                        			</tr>
			                                    			</tbody>
			                                    		</table>
			                                    		</form>
			                                    	</td>
					                            </tr> 
					                        	</tbody>
					                        </table>
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
						                                            <td class="bold">약관 리스트</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                                <!-- td align="right">
						                                	<a href="insert.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}"><span class="button small blue">계정 등록</span></a>
						                                </td>-->
						                            </tr>
						                       		</tbody>
						                       	</table>
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list">
					                                <tbody>
					                                <tr align="center">
					                                    <th scope="col" width="60">선택</th>
					                                    <th scope="col" width="60">번호</th>
														<th scope="col" width="15%">약관 타입</th>
					                                    <th scope="col" width="15%">서비스 구분</th>
					                                    <th scope="col" width="15%">서비스 여부</th>
					                                    <th scope="col">제목</th>
					                                    <th scope="col" width="10%">노출 여부</th>
					                                </tr>
					                                
					                                <c:if test="${list == '[]' }">
					                                <tr align="center">
					                                    <td class="table_td_04" colspan="5">데이터가 존재 하지 않습니다.</td>					                                    
					                                </tr>
					                                </c:if>
					                                
					                            	<c:forEach items="${list}" var="rec">
					                            	<c:set var="i" value="${i+1}" />
						                            	<tr align="center">
						                                    <td class="table_td_04"><input type="checkbox" class="checkbox" value="${rec.service_type}|${rec.service_gbn}|${rec.terms_gbn}" /></td>
						                                    <td class="table_td_04">${i}</td>
						                                    
						                                    <td class="table_td_04">
					                                    		<c:choose>
					                                    			<c:when test="${rec.terms_gbn=='1'}">
							                                    		이용약관
							                                    	</c:when>
					                                    			<c:when test="${rec.terms_gbn=='2'}">
							                                    		개인정보 보호정책
							                                    	</c:when>
							                                    </c:choose>
						                                    </td>
						                                    
						                                    <td class="table_td_04">
					                                    		<c:choose>
					                                    			<c:when test="${rec.service_type=='AHOME'}">
							                                    		추천
							                                    	</c:when>
							                                    </c:choose>
						                                    </td>
						                                    
						                                    <td class="table_td_04">
					                                    		<c:choose>
					                                    			<c:when test="${rec.service_gbn=='0'}">
							                                    		서비스중
							                                    	</c:when>
					                                    			<c:when test="${rec.service_gbn=='1'}">
							                                    		변경예정
							                                    	</c:when>
							                                    </c:choose>
						                                    </td>
						                                    
						                                    
						                                    
						                                    <td class="table_td_04" align="left">
						                                    <a href="./updatetTerms.do?service_type=${rec.service_type}&service_gbn=${rec.service_gbn}&terms_gbn=${rec.terms_gbn}&findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&s_service_type=${vo.s_service_type}&s_service_gbn=${vo.s_service_gbn}&s_terms_gbn=${vo.s_terms_gbn}&s_display_yn=${vo.s_display_yn}">
						                                     ${rec.title}
						                                     </a>
						                                    </td>
						                                    <td class="table_td_04">
					                                    		<c:choose>
					                                    			<c:when test="${rec.display_yn=='Y'}">
							                                    		노출
							                                    	</c:when>
					                                    			<c:when test="${rec.display_yn=='N'}">
							                                    		비노출
							                                    	</c:when>
							                                    </c:choose>
						                                    </td>
						                                </tr>
					                            	</c:forEach>
					                            	 
					                            	</tbody>
					                            </table>
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
						                            <tbody>
						                            <tr>
						                                <td height="5"></td>
						                            </tr>
						                            <tr>
						                            	<td align="center">
						                            		<jsp:include page="/WEB-INF/views/include/naviControll.jsp">
																<jsp:param value="getTermsList.do" name="actionUrl"/>
																<jsp:param value="?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&s_display_yn=${vo.s_display_yn}&s_terms_gbn=${vo.s_terms_gbn}" name="naviUrl" />
																<jsp:param value="${vo.pageNum }" name="pageNum"/>
																<jsp:param value="${vo.pageSize }" name="pageSize"/>
																<jsp:param value="${vo.blockSize }" name="blockSize"/>
																<jsp:param value="${vo.pageCount }" name="pageCount"/>			  
															</jsp:include>
						                            	</td>
						                            </tr>
						                        	</tbody>
						                        </table>
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            <tbody>
						                            <tr>
						                                <td align="right">
						                                	<span class="button small blue" id="deleteTermsBtn">삭제</span>
						                                	<a href="insertTerms.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&s_service_type=${vo.s_service_type}&s_service_gbn=${vo.s_service_gbn}&s_terms_gbn=${vo.s_terms_gbn}&s_display_yn=${vo.s_display_yn}"><span class="button small blue">등록</span></a>
						                                	<span class="button small blue" id="applybtn">즉시적용</span>
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