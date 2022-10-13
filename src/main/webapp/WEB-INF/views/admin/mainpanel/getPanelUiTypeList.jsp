<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!-- 2019.09.09 : 길이 체크를 위해 함수 추가 - 이태광 Start -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!-- 2019.09.09 : 길이 체크를 위해 함수 추가 - 이태광 End -->
<title>LG U+ IPTV SmartUX</title>

<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
$(document).ready(function(){
	$("#regbtn").click(function(){
		var frame_type 	= $("#frame_type option:selected").val();
		window.open("<%=webRoot%>/admin/mainpanel/insertPanelUiType.do?frame_type="+frame_type, "regpanel", "width=480,height=320");
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
			alert("비사용 처리할 패널UI타입을 선택해주세요");
		}else{
			if(confirm("선택된 패널UI타입들을 비사용 처리 하시겠습니까")){
				$.blockUI({
					blockMsgClass: "ajax-loading",
					showOverlay: true,
					overlayCSS: { backgroundColor: '#CECDAD' } ,
					css: { border: 'none' } ,
					 message: "<b>로딩중..</b>"
				});	
				var size = checkeditemslength;
				var checkboxarray = new Array();
				
				for(var i=0; i < size; i++){
					var checkboxval = $(checkeditems[i]).val();
					checkboxarray.push(checkboxval);
				}
				var smartUXManager = $("#smartUXManager").val();
				
				$.post("<%=webRoot%>/admin/mainpanel/deletePanelUiType.do", 
						 {frame_type_code : checkboxarray, smartUXManager : smartUXManager},
						  function(data) {
							 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
							 	var flag = data.flag;
							 	var message = data.message;
							 	
							 	if(flag == "0000"){						// 정상적으로 처리된 경우
							 		alert("패널UI타입들을 비사용 처리하였습니다");
							 		location.reload();
							 	}else{
							 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
							 		$.unblockUI();
							 	}
						  },
						  "json"
			    );
			}
		}
		
	});
});

function view_Frame(frame_type_code){
	var url = "<%=webRoot%>/admin/mainpanel/viewPanelUiType.do?frame_type_code=" + frame_type_code;
	window.open(url, "viewcode", "width=550,height=670");
}
</script>
<body leftmargin="0" topmargin="0">
<div id="divBody" style="height:100%">
<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
    <tbody>
      <tr>
        <td colspan="2" height="45" valign="bottom">
       		 <!-- top menu start -->
			<!-- jsp:include page="/WEB-INF/views/include/top.jsp"--><!-- /jsp:include -->
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
                                    패널/지면
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
                    				<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" >
			                            <tbody>
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
							                                        <td class="bold">
<!-- 2019.11.04 : 패널UI타입 리스트 -> UI타입 리스트 으로 수정 - 이태광 Start -->							                                        
							                                        	UI타입 리스트
<!-- 2019.11.04 : 패널UI타입 리스트 -> UI타입 리스트 으로 수정 - 이태광 End -->							                                        	
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
						                    <td class="3_line" height="1"></td>
						                </tr>
						                <tr>
						                    <td height="20"></td>
						                </tr>
						                <!-- 리스트 시작 -->
						                <tr>
						                    <td>
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list">
					                                <tbody>
						                                <tr align="center">
			                                			   <th scope="col" width="5%" height="102" style="padding-top:8px"><input type="checkbox" id="allchk" name="allchk" value="" ></th>
						                                   <th scope="col" width="10%">코드</th>
<!-- 2019.09.09 : 타입 안보이게 제거 - 이태광 Start -->						                                   
													 	   <!-- <th scope="col" width="15%">타입</th> -->
<!-- 2019.09.09 : 타입 안보이게 제거 - 이태광 End -->	
<!-- 2019.11.04 : 패널UI타입 명 -> UI타입 명 으로 수정 - 이태광 Start -->												 	   
													 	   <th scope="col" width="25%">UI타입 명</th>
<!-- 2019.11.04 : 패널UI타입 명 -> UI타입 명 으로 수정 - 이태광 End -->
													 	   <th scope="col">이미지</th>		
						                                   <th scope="col" width="10%">사용여부</th>
						                                </tr>
						                            </tbody> 
						                       	<c:choose>
													<c:when test="${result==null || fn:length(result) == 0}">
							                                <tr align="center">
<!-- 2019.11.04 : colspan 6 -> 5로 변경 - 이태광 Start  -->							                                
							                                    <td class="table_td_04" colspan="5">데이터가 존재 하지 않습니다.</td>		
<!-- 2019.11.04 : colspan 6 -> 5로 변경 - 이태광 End  -->							                                    			                                    
							                                </tr>
													</c:when>
													<c:otherwise>
					                                
					                                <c:forEach var="item" items="${result}" varStatus="status">
						                            	<tr align="center">
						                            		<!-- 체크 박스 선택 -->
						                                    <td class="table_td_04" >
						                                    	<input type="checkbox" name="delchk" class="delchk" value="${item.frame_type_code}" />
						                                    </td>
						                                    <!-- 코드 -->
						                                    <td class="table_td_04" >
						                                   		${item.frame_type_code}
						                                    </td>
															<!-- 타입 -->
<!-- 2019.09.09 : 타입 안보이게 제거 - 이태광 Start -->															
															 <%-- <td class="table_td_04" >
						                                   		<c:choose>
						                                   			<c:when test="${item.frame_type == 30}">
						                                   				패널
						                                   			</c:when>
						                                   			<c:otherwise>
						                                   				
						                                   			</c:otherwise>
						                                   		</c:choose>
						                                    </td> --%>
<!-- 2019.09.09 : 타입 안보이게 제거 - 이태광 End -->						                                    
						                                    <!-- 프레임 명 -->						                                    						                                    
						                                    <td class="table_td_04" >
						                                    	<a href="#" onclick="view_Frame('${item.frame_type_code}')">
<!-- 2019.09.09 : 길이 체크를 위해 함수 추가 - 이태광 Start -->	
																	<c:set var="list_cnt" value="0" />
						                                    		<c:if test="${fn:length(item.frame_nm) > 20}">
						                                    			<c:if test="${(fn:length(item.frame_nm) % 20) != 0}">
						                                    				<c:set var="list_cnt" value="${(fn:length(item.frame_nm))/20+1}" />
						                                    			</c:if>
						                                    			<c:if test="${(fn:length(item.frame_nm) % 20) == 0}">
						                                    				<c:set var="list_cnt" value="${(fn:length(item.frame_nm) / 20)}" />
						                                    			</c:if>

						                                    			<c:forEach begin="1" end="${list_cnt}" var="cnt">
						                                    				<c:if test="${(cnt*20) >= (fn:length(item.frame_nm))}">
						                                    					${fn:substring(item.frame_nm, (cnt-1)*20, fn:length(item.frame_nm))}
						                                    				</c:if>
						                                    				<c:if test="${(cnt*20) < (fn:length(item.frame_nm))}">
						                                    					${fn:substring(item.frame_nm, (cnt-1)*20, (20 * cnt))}<br>
						                                    				</c:if>						                                    				
						                                    			</c:forEach>
						                                    		</c:if>
						                                    		<c:if test="${fn:length(item.frame_nm) <= 20}">
						                                    			${item.frame_nm}
						                                    		</c:if>
<!-- 2019.09.09 : 길이 체크를 위해 함수 추가 - 이태광 End -->						                                    		
						                                    	</a>						                                    	 
						                                    </td>
						                                    <!--  이미지 -->					                                    						                                    
						                                    <td class="table_td_04" >
						                                    	<c:if test="${item.img_file!=null}"> 
						                                    		<img src="${dir}${item.img_file}" width="250" />	
						                                    	</c:if>				                                    	 
						                                    </td>			
						                                    		
						                                    <!-- 데이터 타입 -->	
						                                    <%-- 
						                                    <td class="table_td_04 ">
						                                    	${item.data_type}
						                                    </td>
						                                     --%>                                    						                                    
						                                    <!--  사용여부 -->
						                                    <td class="table_td_04 ">
						                                    	<c:choose>
																	<c:when test="${item.use_yn == 'Y'}">
																		예
																	</c:when>
																	<c:otherwise>
																		아니오
																	</c:otherwise>
																</c:choose>
						                                    </td>
						                                </tr>
					                            	</c:forEach>
													</c:otherwise>
												</c:choose>
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
																<jsp:param value="getPanelUiTypeList.do" name="actionUrl"/>
																<jsp:param value="?panel_type=${frame_type}" name="naviUrl"/>
																<jsp:param value="${pageNum}" 	name="pageNum"/>
							                                    <jsp:param value="${pageSize}" 	name="pageSize"/>
							                                    <jsp:param value="${blockSize}"	name="blockSize"/>
							                                    <jsp:param value="${pageCount}"	name="pageCount"/>
															</jsp:include> 
						                            	</td>
						                            </tr>
						                        	</tbody>
						                        </table>					                            
					                            
												<input type="hidden" id="smartUXManager" name="smartUXManager" value="<%=id_decrypt %>" />
						                       	<!-- 등록/비사용 버튼 -->
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            <tbody>
						                            <tr>
						                                <td align="right">
						                                	<span class="button small blue" id="regbtn">등록</span>
						                                	<c:if test="${result != null && fn:length(result) > 0}">
																<span class="button small blue" id="delbtn">비사용</span>
															</c:if>
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