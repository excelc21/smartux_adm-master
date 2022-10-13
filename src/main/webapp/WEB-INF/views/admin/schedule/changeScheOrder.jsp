<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<style>
    .selectTable {
      border: 1px solid #000000;
    }
 
    .selected {
      background-color: #d0e8fd;
    }
</style>
<script type="text/javascript">
$(document).ready(function(){
	$("#titlelist").on('click', '#titlelist li', function (e) {
        if (e.ctrlKey || e.metaKey) {
            $(this).toggleClass("selected");
           
        } else {
            $(this).addClass("selected").siblings().removeAttr('class');
         
        }
    });
	
	$('#titlelist').sortable({
        delay: 150, 
        revert: 0,
        helper: function (e, item) {
            if (!item.hasClass('selected')) {
                item.addClass('selected').siblings().removeClass('selected');
            }
            var elements = item.parent().children('.selected').clone();
            item.data('multidrag', elements).siblings('.selected').remove();

            var helper = $('<li/>');
            return helper.append(elements);
        },
        stop: function (e, ui) {
            var elements = ui.item.data('multidrag');

            ui.item.after(elements).remove();
            elements.siblings().removeAttr('id');
            elements.siblings().removeAttr('class');
         
        }
    });
	
	
	$('#changeorderbtn').click(function () {
		if (confirm('다음의 순서로 정하시겠습니까?')) {
            var size = $('#titlelist li').size();
            var optionArray = new Array();
			var schedule_code = $('#schedule_code').val();
            for (var i = 0; i < size; i++) {
                var selval = $('#titlelist li').eq(i).attr("data-val");
                optionArray.push(selval);
            }
			
            $.post("<%=webRoot%>/admin/schedule/changeOrder.do", 
					 {optionArray: optionArray, schedule_code:schedule_code},
					  function(data) {
						 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
						 	var flag = data.flag;
						 	var message = data.message;
						 	
						 	if(flag == "0000"){						// 정상적으로 처리된 경우
						 		alert("순서를 재지정했습니다");
						 		opener.location.reload();
						 		self.close();
						 	}else{
						 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
						 	}
					  },
					  "json"
		    );
            
           
        }
    });
	
	$("#resetbtn").click(function(){
		// $("#orderfrm")[0].reset();
		location.reload();
	});
	
	$("#closebtn").click(function(){
		self.close();
	});
	
});


</script>
<body leftmargin="0" topmargin="0">
<div id="divBody" style="height:100%">
<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
<input type="hidden" id="schedule_code" name="schedule_code" value="${schedule_code}">
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
						                                            <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
						                                            <td class="bold">순서 바꾸기</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="orderfrm" name="orderfrm" method="get" action="">
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
					                                <tbody>
					                                <tr align="left">
					                                    <td width="100%">
					                                    	
															<c:if test="${result == '[]' }">
		                
												                    <td class="table_td_04" colspan="6">데이터가 존재 하지 않습니다.</td>
												               
												            </c:if>
												            <c:if test="${result != '[]' }">
													           
												                   <ul id="titlelist" name="titlelist" style="border:1px solid grey;padding:10px;list-style:none">
												                       	<c:forEach var="item" items="${result}"  varStatus="status">
												                     		<li style="list-style:none" data-val="${item.category_id}^${item.album_id}">${item.album_name}</li>
												                     		
																		</c:forEach>
																	</ul>
																
															</c:if> 
														</td>
					                                    
					                                </tr>
					                            	</tbody>
					                            </table>
					                            <br/>
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="center">
						                                	<span class="button small blue" id="changeorderbtn">확인</span>	
						                                	<span class="button small blue" id="resetbtn">재작성</span>	
						                                	<span class="button small blue" id="closebtn">닫기</span>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                       <input type="hidden" id="panel_id" name="panel_id" value="${panel_id}" />
						                       <input type="hidden" id="smartUXManager" name="smartUXManager" value="<%=id_decrypt %>" />
						                       	</form>
					                            
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
</tbody>
</table>
</div>

</body>
</html>