<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
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
			var url = window.open("<%=webRoot%>/admin/rank/insertRank.do", "regcodeitem","toolbar=yes,resizable=yes,width=520,height=350"); 
			url.moveTo(15,50);
		});	
	
	$("#previewbtn").click(function(){
		var rank_code = $("#rank_code").val();
		var url = "<%=webRoot%>/admin/rank/previewVodPopup.do?rank_code=" + rank_code;
		window.open(url, "previewVodPopup","scrollbars=yes,toolbar=yes,resizable=yes,width=480,height=440"); 
	});	
	
	$("#delbtn").click(function(){
		var rank_code = $("#rank_code").val();
		var checkeditems = $("input[name='delchk']:checked");
		var checkeditemslength = checkeditems.length;
		
		if(checkeditemslength == 0){
			alert("삭제할 항목을 체크해주세요");
		}else{
			if(confirm("체크된 랭킹 데이터들을 삭제하시겠습니까")){
				var size = checkeditemslength;
				var checkboxarray = new Array();
				
				for(var i=0; i < size; i++){
					var checkboxval = $(checkeditems[i]).val();
					checkboxarray.push(checkboxval);
				}				
				
				$.post("<%=webRoot%>/admin/rank/deleteRank.do", 
					 { rank_code : checkboxarray },
					// {code : code, item_code : checkboxarray},
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
		var msg = "BEST VOD 앨범 정보를 변경하시겠습니까?";
				
		if(confirm(msg)){
			$.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>로딩중..</b>"
			});
			
			$.post("<%=webRoot%>/admin/rank/activateCache.do", 
				 {rank_code:rank_code,
				callByScheduler:'A'},
				  function(data) {
					 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
					 	var flag = data.flag;
					 	var message = data.message;
					 	
					 	if(flag == "0000"){						// 정상적으로 처리된 경우						 		
					 			alert("BEST VOD API 정보가 업데이트되었습니다");
					 	}else{
					 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
					 	}
					 	$.unblockUI();
				  },
				  "json"
		    );
		}
	});
	
	$("#orderbtn").click(function(){
		var code = $("#code").val();
		var url = "<%=webRoot%>/admin/smartstart/changeItemOrder.do"; 
		window.open(url, "chnageorder", "width=344,height=230");
	});	
	
	$("#allchk").click(function(){
		var chkallchecked = false;
		
		if($("#allchk").is(":checked")){
			chkallchecked = true;
		}		
		$("input[name='delchk']").attr("checked", chkallchecked);
	});
});

function viewItemDetail(rank_code){
	var url = "<%=webRoot%>/admin/rank/viewRank.do?rank_code=" + rank_code;
	window.open(url, "viewrank", "width=500,height=330,resizable=yes,scrollbars=yes");
}

function previewVOD(rank_code){
	var url = "<%=webRoot%>/admin/rank/previewVodPopup.do?rank_code=" + rank_code;
	window.open(url, "viewrank", "width=480,height=510,resizable=yes,scrollbars=yes");
}

function viewVODdetail(rank_code, rule_type){
	var url = "<%=webRoot%>/admin/rank/viewAlbumVod.do?rank_code=" + rank_code + "&rule_type=" + rule_type;
	window.open(url, "viewAlbumVod", "width=650,height=516,resizable=yes,scrollbars=yes");
}
</script>
</head>

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
                                    랭킹 데이터 관리
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
						                                            <td class="bold">랭킹 데이터 관리 리스트</td>
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
														 <th scope="col" width="7%">랭킹 코드</th>
														 <th scope="col" width="13%">랭킹 데이터명</th>
														 <th scope="col" width="26%">VOD 장르 코드</th>
														 <th scope="col" width="15%">랭킹 룰 코드명</th>
														 <th scope="col" width="9%">랭킹 기한(일)</th>
														 <th scope="col" width="9%">카테고리 구분</th>
														 <th scope="col" width="13%">VOD 미리보기</th>
													</tr>
													 
					                                <c:choose>
														<c:when test="${result==null || fn:length(result) == 0}">
															<tr align="center">
																<td class="table_td_04" colspan="7">검색된 코드가 없습니다</td>
															</tr>
														</c:when>
														<c:otherwise>
															<c:forEach var="item" items="${result}" varStatus="status">
																<tr align="center">
						                                   			<td class="table_td_04">
																		<input type="checkbox" name="delchk" value="${item.rank_code}">
																	</td>
																	<td class="table_td_04">
																		${item.rank_code}
																	</td>
																	<td class="table_td_04">
																		<a href="#" onclick="viewItemDetail('${item.rank_code}')">${item.rank_name}</a>
																	</td>
																	<td class="table_td_04">
																		${fn:replace( item.genre_code, '__',' > ' )} 
																	</td>
																	<td class="table_td_04">
																		${fn:replace( item.rule_code,  '||',' + ' )}
																	</td>																	
																	<td class="table_td_04">
																		${item.rank_term}
																	</td>
																	<td class="table_td_04">
																		${item.category_gb}
																	</td>
																	<td class="table_td_04">
																	<!--
																	<input type="button"  value="보기" class="button small blue" />
																	<input type="button"  value="변경" class="button small blue" />
																	<input type="button" id="previewbtn" value="미리보기" class="button small blue"/>  
																	-->
																	<a href="#" onclick="previewVOD('${item.rank_code}')">
																		<input type="button"  value="보기" class="button small blue" />
																	</a>
																	<a href="#" onclick="viewVODdetail('${item.rank_code}','${item.rule_type }')">
																		<input type="button"  value="변경" class="button small blue" />
																	</a>
																	</td>
																</tr>
															</c:forEach>			
														</c:otherwise>
													</c:choose>
					                          
					                            	</tbody>
					                            </table>
					                            
					                            <form id="frm" name="frm" method="post">
													<input type="hidden" id="delthemecodes" name="delthemecodes" value="" />
													<input type="hidden" id="rank_code" name="rank_code" value="${rank_code}" /> 
												</form>
												<br>					                            
					                            <table border="0" cellpadding="0" cellspacing="0"  width="100%">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
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