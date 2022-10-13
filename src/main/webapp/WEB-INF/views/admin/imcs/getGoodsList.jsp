<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>신청</title>
    <link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
    <jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>

<script type="text/javascript">

var sendnum ='none';



$(document).ready(function(){
	

	
	$(".numberchk").change(function(){
		
		if($(this).is(":checked")){ 
			var name = $(this).attr('name');
			var selector = "input[name="+name+"]";
			$(selector).not(this).attr('checked',false);
			
			
			sendnum = $(this).val();
			
		} else if($(this).is(":checked") == false){
			
			sendnum = 'none';
		}
	});
	
	


});

function send_data(){
	
	if(sendnum == 'none'){
		alert('체크박스를 선택해주세요.')	
	} else {
		if("${hiddenName}"!="" && "${textName}"!="" && "${textHtml}"!="")	{
			
		     $(opener.document).find("#${hiddenName}").val(sendnum);
		     $(opener.document).find("#${textName}").val(sendnum);
		     $(opener.document).find("#${textHtml}").html(sendnum);	
		}else {	
		opener.document.getElementById("linkUrl").value = sendnum;
		opener.document.getElementById("textUrl").value = sendnum;
		}
		self.close();
	}
}




</script>
<body leftmargin="0" topmargin="0">
<div id="divBody" style="height:100%">
<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
    <tbody>

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
		
            <td valign="top">
            <table border="0" cellpadding="0" cellspacing="0" width="98%">
                <tbody>
            
                
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
						                                            <td class="bold">${popupName}</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        
					                            <table border="0" cellspacing="0" cellpadding="0" class="board_list" style="width:98%">
													<tr>
														<th width="14%"></th>														
														<th width="25%">상품ID</th>
														<th>상품명</th>
													</tr>
													<c:choose>
														<c:when test="${result==null || fn:length(result) == 0}">
															<tr>
																<td colspan="5" class="table_td_04">검색된 공지가 없습니다</td>
															</tr>
														</c:when>
														<c:otherwise>
															<c:forEach var="item" items="${result}" varStatus="status">
																<tr>
																	<td class="table_td_04" style="text-align: center;">
																		<input type="checkbox" class="numberchk" name="numberchk" value="${item.goods_id}">
																	</td>		
																	<td class="table_td_04" style="text-align: center;">${item.goods_id}</td>																	
																	<td class="table_td_04" style="text-align: center;">
																		${item.goods_nm}
																	</td>
																</tr>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</table>
												<input type="hidden" id="smartUXManager" name="smartUXManager" value="<%=id_decrypt %>" />
												<table border="0" cellpadding="0" cellspacing="0" style="width:98%" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="40" align="right">
														<a href="#" onclick="send_data()">
														<span class="button small blue" id="regbtn">선택</span></a>
																	
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
</tbody>
</table>
</div>
</body>
</html>