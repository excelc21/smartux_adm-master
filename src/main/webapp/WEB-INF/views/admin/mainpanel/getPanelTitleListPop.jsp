<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
$(document).ready(function(){
	
	$("#selPanel").change(function(){
		var pannel_id = $("#selPanel").val();
		console.log(pannel_id)
		$('#selTitle').children('option:not(:first)').remove();
        $("#selTitle").val("");
		if(pannel_id != ""){
			$.ajax({
                type: "get",
                url: '<%=webRoot%>/admin/mainpanel/getTitleList',
                dataType: "json",
                data: {
                    pannel_id: pannel_id
                },
                success: function(data) {  
                    console.log(data) 
                	var dHtml = "";
                    for(var i = 0; i < data.length; i++){
                        dHtml += "<option value="+data[i].title_id+">"+decodeURI(data[i].title_nm)+"</option>";
                    }
                    $('#selTitle').append(dHtml);                    
                }
            });
		}
	});
	
	$("#regbtn").click(function(){
		var pannel_id = $("#selPanel").val();
		var title_id = $("#selTitle").val();

	    if(pannel_id == ""){
	        alert("지면을 선택해주세요.")
	        return false;
		}

	    if(title_id == ""){
            alert("지면을 선택해주세요.")
            return false;
        }
		
		var dataobj = new Object(); 
	    dataobj.pannel_id =pannel_id;
	    dataobj.title_id =title_id;
	    var callbak_m = eval("opener."+"${param.callbak}");    
	    callbak_m(dataobj);
	    self.close();
		
	});
	
	$("#resetbtn").click(function(){
		$("#regfrm")[0].reset();
	});
});
</script>
<body leftmargin="0" topmargin="0">
<div id="divBody" style="height:100%">
<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
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
						                                            <td class="bold">지면 선택</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="regfrm" name="regfrm" method="post" action="">
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">패널선택</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<select id="selPanel" name="selPanel" style="width: 250px">
                                                               <option value="">== 패널선택 ==</option>
                                                                <c:forEach var="item" items="${panel}" varStatus="status">
                                                                    <option value="${item.pannel_id}">${item.pannel_nm}</option>
                                                                </c:forEach>
                                                            </select>					
														</td>
					                                </tr>
					                                <tr align="center">
                                                        <th width="25%">지면선택</th>
                                                        <td width="5%"></td>
                                                        <td width="70%" align="left" >
                                                            <select id="selTitle" name="selTitle" style="width: 250px">
                                                               <option value="">== 지면선택 ==</option>
                                                            </select>                   
                                                        </td>
                                                    </tr>
					                                
					                            	</tbody>
					                            </table>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="center">
						                                	<span class="button small blue" id="regbtn">등록</span>	
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
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