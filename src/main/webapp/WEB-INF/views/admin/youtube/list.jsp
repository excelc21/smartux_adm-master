<%@page import="com.dmi.smartux.common.property.SmartUXProperties"%>
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
	$("#regbtn").click(function(){
		var url = window.open("<%=webRoot%>/admin/youtube/insert.do", "regcodeitem","toolbar=yes,resizable=yes,width=520,height=220"); 
		url.moveTo(15,50);
	});
	
	$("#allchk").click(function(){
		var chkallchecked = false;
		
		if($("#allchk").is(":checked")){
			chkallchecked = true;
		}		
		$("input[name='delchk']").attr("checked", chkallchecked);
	});
	
	$("#delbtn").click(function(){
		var checkeditems = $("input[name='delchk']:checked");
		var checkeditemslength = checkeditems.length;
		
		if(checkeditemslength == 0){
			alert("삭제할 항목을 체크해주세요");
		}else{
			if(confirm("체크된 검색어들을 삭제하시겠습니까")){
				var size = checkeditemslength;
				var checkboxarray = new Array();
				
				for(var i=0; i < size; i++){
					var checkboxval = $(checkeditems[i]).val();
					var checkUseYN = $("#use_"+checkboxval).text();
					if(checkUseYN == 'Y'){
						alert("사용중인 검색어가 포함되어 있습니다.");
						return;
					}else{
						checkboxarray.push(checkboxval);
					}
				}
				
				$.post("<%=webRoot%>/admin/youtube/delete.do", 
						 { code : checkboxarray },
						  function(data) {
							 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
							 	var flag = data.flag;
							 	var message = data.message;
							 	
							 	if(flag == "0000"){						// 정상적으로 처리된 경우
							 		alert("랭킹 데이터가 삭제되었습니다");
							 		location.reload();
							 	}else{
							 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
							 	}
						  },
						  "json"
				 );
			}
		}
	});
	
	
	$("#activatebtn").click(function(){
		var rank_code = $("#rank_code").val();
		var msg = "Youtube 초기 검색어를 변경하시겠습니까?";
				
		if(confirm(msg)){
			$.blockUI({ 
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>로딩중..</b>"
			});
			
			$.ajax({
	            url: '<%=webRoot%>/admin/youtube/activateCache.do',
	            type: 'POST',
	            dataType: 'json',
	            data: {
	            	callByScheduler : 'N'
	            },
	            error: function () {
	                alert('작업 중 오류가 발생하였습니다');
	                $.unblockUI();
	            },
	            
	            success: function (data) {
				 	console.log(data);
				 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
				 	var flag = data.flag;
				 	var message = data.message;
				 	
				 	if(flag == "0000"){						// 정상적으로 처리된 경우						 		
			 			alert("Youtube 초기 검색어 API 정보가 업데이트되었습니다");		
			 			location.reload();
				 	}else{
				 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
				 	}
			 	  $.unblockUI();
	            }
	        });
		}
	});
});


function useUpdate(_code,_use_yn){
	$.post("<%=webRoot%>/admin/youtube/useUpdate.do", 
			 { code : _code,
			   use_yn : _use_yn
			 },
			  function(data) {
				 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
				 	var flag = data.flag;
				 	var message = data.message;
				 	
				 	if(flag == "0000"){						// 정상적으로 처리된 경우
				 		alert("사용여부가 변경되었습니다.");
				 		location.reload();
				 	}else{
				 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
				 	}
			  },
			  "json"
	 );
}

function YoutubeUpdate(_code){
	var url = window.open("<%=webRoot%>/admin/youtube/update.do?code="+_code, "regcodeitem","toolbar=yes,resizable=yes,width=520,height=220"); 
	url.moveTo(15,50);
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
                                    Youtube
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
						                                            <td class="bold">Youtube 초기 검색어 리스트</td>
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
					                                	<th scope="col" width="5%" style="padding-top:8px;"><input type="checkbox" id="allchk" name="allchk" value=""></th>
					                                    <th scope="col" width="20%">구분</th>
					                                    <th scope="col" width="35%">검색어</th>
					                                    <th scope="col" width="10%">등록일</th>
					                                    <th scope="col" width="10%">수정일</th>
					                                    <th scope="col" width="10%">사용여부</th>
					                                    <th scope="col">비고</th>
					                                </tr>
					                                
					                                <c:if test="${vo.list == '[]' }">
					                                <tr align="center">
					                                    <td class="table_td_04" colspan="7">데이터가 존재 하지 않습니다.</td>					                                    
					                                </tr>
					                                </c:if>
					                            	<c:forEach items="${vo.list}" var="rec">
					                            	<c:set var="i" value="${i+1}" />
					                            		<tr align="center">
						                            		<td class="table_td_04">
																<input type="checkbox" name="delchk" value="${rec.code}">
															</td>
							                                <td class="table_td_04">
							                                		<c:choose>
							                                			<c:when test="${rec.category == 'q'}">
					                                    					<%=SmartUXProperties.getProperty("Youtube.category.q") %>
					                                    				</c:when>
					                                    				<c:when test="${rec.category == 'v'}">
					                                    					<%=SmartUXProperties.getProperty("Youtube.category.v") %>
					                                    				</c:when>
					                                    				<c:when test="${rec.category == 'vv'}">
					                                    					<%=SmartUXProperties.getProperty("Youtube.category.vv") %>
					                                    				</c:when>
					                                    				<c:when test="${rec.category == 'p'}">
					                                    					<%=SmartUXProperties.getProperty("Youtube.category.p") %>
					                                    				</c:when>
					                                    			</c:choose>							                                
							                                &nbsp;</td>
							                                <td class="table_td_04">
							                                		<a href="javascript::" onclick="YoutubeUpdate('${rec.code}');">${rec.recommend_text}</a>
							                                		</td>
							                                <td class="table_td_04">
							                                <c:choose>
					                                    		<c:when test="${fn:length(rec.created) > 10}">
					                                    			${fn:substring(rec.created,0,4)}-${fn:substring(rec.created,5,7)}-${fn:substring(rec.created,8,10)}
					                                    		</c:when>
				                                    		</c:choose>
							                                &nbsp;</td>
							                                <td class="table_td_04">
							                                 <c:choose>
					                                    		<c:when test="${fn:length(rec.updated) > 10}">
					                                    			${fn:substring(rec.updated,0,4)}-${fn:substring(rec.updated,5,7)}-${fn:substring(rec.updated,8,10)}
					                                    		</c:when>
				                                    		</c:choose>
							                                &nbsp;</td>
						                            		<td class="table_td_04" id="use_${rec.code }">${rec.use_yn}</td>
						                            		<td class="table_td_04">
						                            			<c:choose>
						                            			<c:when test="${rec.use_yn == 'N'}">
						                            				<input type="button" id="usebtn" onclick="useUpdate('${rec.code}','Y')" value="사용함" class="button small blue"  />
						                            			</c:when>
						                            			<c:otherwise>
						                            				<input type="button" id="usebtn" onclick="useUpdate('${rec.code}','N')" value="사용 안함" class="button small blue"  />
						                            			</c:otherwise>
						                            			</c:choose>
						                            		&nbsp;</td>
						                            	</tr>
					                            	</c:forEach>
					                            	</tbody>
					                            </table>
					                             <table border="0" cellpadding="0" cellspacing="0" width="100%">
						                            <tbody>
						                            <tr>
						                                <td height="5"></td>
						                            </tr>
						                        	</tbody>
						                        </table>
					                           <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            <tbody>
						                            <tr>
						                                <td align="right">
						                                	<input type="button" id="regbtn" value="등록" class="button small blue"  />
															<input type="button" id="delbtn" value="삭제" class="button small blue"  />
														  	<input type="button" id="activatebtn" value="즉시적용" class="button small blue"/>
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
<c:choose>
<c:when test="${vo.validate == 'NOT ID'}">
	<script type="text/javascript">
		alert('잘못된 접근입니다.');		
	</script>
</c:when>
</c:choose>
</body>
</html>