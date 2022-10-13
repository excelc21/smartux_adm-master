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

$(document).ready(function(){

	
});


function formRegistration(){
		
	var count=0;
	var teminal_array="";
	
	$("#terminal_div",opener.document).html("");
	$("input[name=terminal_list]").remove();
	
	$("input:checkbox[name=termCheck]").each(function(){
		
		if($(this).is(":checked")){			
			
			if(count%5==0){
				$("#terminal_div",opener.document).append("<br>");
			}	
			$("#terminal_div",opener.document).append($(this).val()+"  /  ") ;
			
			teminal_array+=$(this).val()+",";
			count++;						
			$("#terminal_div",opener.document).append("<input type='hidden' name='terminal_list' value=' "+$(this).val()+" '>") ; 
			
		}		
	});
	self.close();	
}


function selectAll(){
	 var ischeck = $('#checkAll').attr('checked');
	 
	 if(!ischeck){
		 ischeck=false;
	 }
	 
	 $('input[name=termCheck]').attr('checked',ischeck);
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
                		<table border="0" cellpadding="15" cellspacing="0" width="100%" align="center" >
                    		<tbody>
                    		<tr>
                    			<td>
                    				<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" >
			                            <tbody>                          
						             
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
						                                            <td class="bold">단말 리스트</td>
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
						                                    <th scope="col" width="10%"><input type="checkbox" id="checkAll"  onclick="selectAll()"/></th>
						                                    <th scope="col" width="30%" >모델명</th>
						                                    <th scope="col" width="20%" >제조사</th>
						                                    <th scope="col" >LCD사양</th>
						                                </tr>						                                						                             
					                                </tbody>
					                                <c:forEach items="${termList}" var="terminal">
					                            	<c:set var="i" value="${i+1}" />
					                            	
						                            	<tr align="center">
						                            		<!-- 체크 박스 -->
						                                    <td class="table_td_04" >
						                                    	<input type="checkbox" name="termCheck" value="${terminal.TERM_MODEL}" />						                                    	
						                                    </td>
						                                                                       
						                                    <!-- 모델명 -->
						                                    <td class="table_td_04 " >
						                                    	 ${terminal.TERM_MODEL}                                   
						                                    </td>
						                                    
						                                    <!-- 제조사 -->
						                                    						                                    
						                                    <td class="table_td_04 " > 
						                                    	${terminal.TERM_MAKE} 
						                                    </td>
						                                    
						                                    
						                                    <!-- LCD사양 -->
						                                    <td class="table_td_04 ">
						                                    	${terminal.LCD_SPEC}
						                                    </td>
						                                    
						                                
						                                </tr>
					                            	</c:forEach>
					                            </table>			                            
					                       						                        
						                        <!-- 등록/수정 버튼 -->
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            <tbody>
						                            <tr>
						                                <td align="right">
						                                	<a href="javascript:formRegistration()"><span class="button small blue"> 등록</span></a>					                                	
						                                
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
	      <td colspan="2" background="/smartux_adm/images/admin/copy_bg.gif" height="60" align="left"/>
	  </tr>    
	      
</tbody>
</table>
</div>



</body>
</html>