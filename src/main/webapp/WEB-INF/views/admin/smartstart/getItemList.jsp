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
		//var code = $("#code").val();
		var url = "<%=webRoot%>/admin/smartstart/insertItem.do"; 
		window.open(url, "regcodeitem", "width=429,height=330");
	});	
	
	$("#delbtn").click(function(){
		//var code = $("#code").val();
		var checkeditems = $("input[name='delchk']:checked");
		var checkeditemslength = checkeditems.length;
		
		if(checkeditemslength == 0){
			alert("삭제할 항목을 체크해주세요");
		}else{
			if(confirm("체크된 코드들을 삭제하시겠습니까")){
				var size = checkeditemslength;
				var checkboxarray = new Array();
				
				for(var i=0; i < size; i++){
					// alert($('#codeitemlist option').eq(i).val());
					
					var checkboxval = $(checkeditems[i]).val();
				
					//alert("code "+code);
					//alert("item_code "+checkboxval);
				
					checkboxarray.push(checkboxval);
				}				
				
				$.post("<%=webRoot%>/admin/smartstart/deleteItem.do", 
						 { item_code : checkboxarray },
						// {code : code, item_code : checkboxarray},
						  function(data) {
							 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
							 	var flag = data.flag;
							 	var message = data.message;
							 	
							 	if(flag == "0000"){						// 정상적으로 처리된 경우
							 		alert("코드가 삭제되었습니다");
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

function viewItemDetail(code, item_code){
	var url = "<%=webRoot%>/admin/smartstart/viewItemDetail.do?code=" + code + "&item_code=" + item_code;
	window.open(url, "viewcode", "width=429,height=350");
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
                                    스마트스타트
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
						                                            <td class="bold">스마트스타트 항목 조회</td>
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
														 <th scope="col" width="10%">코드</th>
														 <th scope="col" width="25%">항목명</th>
														 <th scope="col" width="15%">Smart Start 구분</th>
														 <th scope="col" width="15%">Smart Start 장르</th>
														 <th scope="col" width="15%">사용여부</th>
														 <th scope="col" width="15%">항목표시순서</th>
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
																		<input type="checkbox" name="delchk" value="${item.item_code}">
																	</td>
																	<td class="table_td_04">
																		${item.item_code}
																	</td>
																	<td class="table_td_04">
																		<a href="#" onclick="viewItemDetail('${item.code}','${item.item_code}')">${item.item_nm}</a>
																	</td>
																	<td class="table_td_04">
																		${item.ss_gbn}
																	</td>
																	
																	<c:choose>
																		<c:when test="${item.ss_genre == null || fn:length(item.ss_genre) == 0}">
																			<td class="table_td_04"> &nbsp; </td>
																		</c:when>
																		<c:otherwise>
																			<td class="table_td_04">
																				${item.ss_genre}
																			</td>
																		</c:otherwise>
																	</c:choose>
																	
																	<td class="table_td_04">
																		${item.use_yn}
																	</td>
																	<td class="table_td_04">
																		${item.ordered}
																	</td>
																</tr>
															</c:forEach>			
														</c:otherwise>
													</c:choose>
					                          
					                            	</tbody>
					                            </table>
					                            
					                            <form id="frm" name="frm" method="post">
													<input type="hidden" id="delthemecodes" name="delthemecodes" value="" />
													<input type="hidden" id="code" name="code" value="${code}" /> 
												</form>
																	                            
					                            <table border="0" cellpadding="0" cellspacing="0"  width="100%">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<input type="button" id="regbtn" value="등록" class="button small blue"/>
															<input type="button" id="delbtn" value="삭제" class="button small blue"/>
															<input type="button" id="orderbtn" value="순서바꾸기" class="button small blue"/>					                                	
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