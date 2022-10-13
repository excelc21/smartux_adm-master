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
	$("#delStatbbsBtn").click(function(){
		var statno_comma="";
		
		if(!$(".checkbox").is(":checked")){
			alert("삭제 할 항목을 선택하세요.");
			return;
		}else{
			var statCnt = 0;
            $(".checkbox").each(function () {
                if ($(this).is(":checked")) {
                	if(statCnt==0){
                		statno_comma = statno_comma+$(this).val();
                	}else{
                		statno_comma = statno_comma+","+$(this).val();
                	}
                	statCnt++;
                }
            });
		 }
		if(confirm("참여통계를 삭제 하시겠습니까?")){	
		    $.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>처리 중..</b>"
			});
		    
		    $.ajax({
				url: './statbbsDelete.do',
				type:'POST',
				dataType: 'json',
				timeout : 30000,
			    data: {
			        "stat_no": statno_comma
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
					$(location).attr('href',"./statbbsList.do?findName=${vo.findName}&findValue=${vo.findValue}&use_yn=${vo.use_yn}&pageNum=${vo.pageNum}"); 
				}
			});
		}
	});
	
	$("#applybtn").click(function(){
		var msg = "참여통계를 상용에 적용하시겠습니까?";
		if(confirm(msg)){
			$.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>로딩중..</b>"
			});
			$.ajax({
			    url: '/smartux_adm/admin/statbbs/statbbsApplyCache.do',
			    type: 'POST',
			    dataType: 'json',
			    data: {        
			    	callByScheduler :'A'
			    },
			    error: function(){
			    	alert("작업 중 오류가 발생하였습니다");
			    },
			    success: function(rs){
			    	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
				 	var flag = rs.flag;
				 	var message = rs.message;
				 	if(flag == "0000"){// 정상적으로 처리된 경우
				 		alert("참여통계가 상용에 적용되었습니다.");
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
                                    참여통계 관리
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
				                                    	<form id="form2" method="get" action="statbbsList.do"> 
					                                    <table border="0" cellpadding="0" cellspacing="0" width="450">		                                    
		                                        			<tbody>
		                                        			<tr><td>&nbsp;</td></tr>
		                                        			<tr>
					                                            <td width="10">
					                                            	<img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9" />
					                                            </td>
					                                            <td>
					                                                <select class="select" id="cateOne" name="findName">
					                                                    <option <c:if test="${vo.findName == 'TITLE'}">selected="selected"</c:if> value="TITLE">제목</option>
					                                                    <option <c:if test="${vo.findName == 'CONTENT'}">selected="selected"</c:if> value="CONTENT">내용</option>
					                                                </select></td><td>
					                                            	<input type="text" name="findValue" value="${vo.findValue }" size="20" style="font-size: 12px;" />
					                                            	&nbsp;&nbsp;&nbsp;
					                                            	<select class="select" id="use_yn" name="use_yn">
					                                                    <option <c:if test="${vo.use_yn == ''}">selected="selected"</c:if> value="">전체</option>
					                                                    <option <c:if test="${vo.use_yn == 'Y'}">selected="selected"</c:if> value="Y">사용</option>
					                                                    <option <c:if test="${vo.use_yn == 'N'}">selected="selected"</c:if> value="N">미사용</option>
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
						                                            <td class="bold">참여통계 리스트</td>
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
					                                    <th scope="col">제목</th>
					                                    <th scope="col" width="18%">종료일시</th>
					                                    <th scope="col" width="10%">사용여부</th>
					                                </tr>
					                                
					                                <c:if test="${vo.list == '[]' }">
					                                <tr align="center">
					                                    <td class="table_td_04" colspan="5">데이터가 존재 하지 않습니다.</td>					                                    
					                                </tr>
					                                </c:if>
					                            	<c:forEach items="${vo.list}" var="rec">
					                            	<c:set var="i" value="${i+1}" />
						                            	<tr align="center">
						                                    <td class="table_td_04"><input type="checkbox" class="checkbox" value="${rec.stat_no}" /></td>
						                                    <td class="table_td_04">${i}</td>
						                                    <td class="table_td_04" align="left"><a href="./statbbsUpdate.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&use_yn=${vo.use_yn}&stat_no=${rec.stat_no}">${rec.title}</a></td>
						                                    <td class="table_td_04">${rec.e_date}</td>
						                                    <td class="table_td_04">${rec.use_yn}</td>
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
																<jsp:param value="statbbsList.do" name="actionUrl"/>
																<jsp:param value="?findName=${vo.findName}&findValue=${vo.findValue}&use_yn=${vo.use_yn}" name="naviUrl" />
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
						                                	<span class="button small blue" id="delStatbbsBtn">삭제</span>
						                                	<a href="statbbsInsert.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&use_yn=${vo.use_yn}"><span class="button small blue">등록</span></a>
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