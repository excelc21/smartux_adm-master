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
	
	$("#delCheck").change(function(){		
		
		var del_yn=$("#delCheck option:selected").val();
		
		location.href = "/smartux_adm/admin/noti/getManageTerm.do?del_yn="+del_yn+"&scr_tp=${delCheck.scr_tp}";
				
	});

});

function selectAll(){
	 var ischeck = $('#checkAll').attr('checked');
	 
	 if(!ischeck){
		 ischeck=false;
	 }
	 
	 $('input[name=termCheck]').attr('checked',ischeck);
 }
 
 function createTermManage(){
	 
	 var check=true;
	 
	 /*
	 $("input:checkbox[name=termCheck]").each(function(){
		 
		  var model=$(this).val().toLowerCase().trim(); 
		  var inputModel=$("#term_model").val().toLowerCase().trim();
		 if(model==inputModel){
			 alert("중복된 모델명입니다.");
			 check=false;
		 }
		 
	  });
	 */
	 var term_model = $.trim($("#term_model").val());

	 
	 $.ajax({
		    url: '/smartux_adm/admin/noti/isExistModel.do',
		    type: 'POST',
		    dataType: 'json',
		    data: {
		        "term_model": term_model,
				scr_tp:'${delCheck.scr_tp}'
		    },
		    error: function(){
		    	alert("등록할  수 없습니다.");
		    },
		    success: function(textDoc){
		    	
		    	var resultstate=textDoc.resultstate;
		    	var flag=textDoc.flag;
		    	var message=textDoc.message;
		    	
		    	//alert(flag);
		    	//alert(resultstate);
		    	
		    	if(flag=="0000"){
			    	
			    	if(resultstate != 0){
			    		
			    		alert("동일한 모델이 등록되어 있습니다");
			    		 $("#term_model").focus();
			    		check=false;		    
			    		
			    	}	else{
			    		
			    		 if($("#term_model").val()==""){
			    			 alert("모델명을 입력해주세요.");
			    			 $("#term_model").focus();
			    			 return;
			    		 }
			    		
			    		 if($("#term_make").val()==""){
			    			alert("제조사명을 입력해주세요.");
			    			$("#term_make").focus();
			    			return;
			    		}
			    		if($("#lcd_spec").val()==""){
			    			alert("LCD사양을 입력해주세요.");
			    			$("#lcd_spec").focus();			    			
			    			return;
			    		} 
			    		
			    		 
			    		 $("#form1").submit();
			    		
			    		
			    	}
			    	
		    	}else{
		    		$.unblockUI();
		    		alert("등록 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);		
		    		   
		    	}
		    }
		});	
	 
	 	
 }
 
 function  deleteTermManage(del_yn){
	 var teminal_array="";
	 	 
	 $("input:checkbox[name=termCheck]").each(function(){
		 
			if($(this).is(":checked")){				
				teminal_array+=$(this).val()+",";	
			}	
	 });
	 if(teminal_array==""){
		 alert("항목을 선택해주세요.");
		 return;
	 }
	 
	 var msg="삭제 하시겠습니까?";				
		
	if(confirm(msg)){			
		$.ajax({
			url: '/smartux_adm/admin/noti/deleteTermManage.do',
			type:'POST',
			dataType: 'json',
			data:	{
				modelList:teminal_array,
				del_yn:del_yn,
				scr_tp:'${delCheck.scr_tp}'
				},
			success:function(data){				
		    	var flag=data.flag;
		    	var message=data.message;
		    	if(flag=="0000"){
			    		location.reload();	
		    	}else{
		    		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);		    		
		    	}
			},
			error:function(){
				alert("접속할 수 없습니다.");					
			}
		});	
	}
 }
/*  function createTermManage(){	
		var msg="등록 하시겠습니까?";				
				
		if(confirm(msg)){			
			$.ajax({
				url: '/smartux_adm/admin/noti/createTermManage.do',
				type:'POST',
				dataType: 'text',
				data:	{
					term_model:$("#term_model").val(),
					term_model:$("#term_make").val(),
					term_model:$("#lcd_spec").val(),
					scr_tp:'${scr_tp}'
					},
				success:function(data){
					
					if(data=="FAIL"){
						alert("등록 실패입니다.");
						return;
					}
					location.reload();
				},
				error:function(){
					alert("접속할 수 없습니다.");					
				}
			});	
		}	
 } */
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
                                  	공지 관리
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
							                       	
						                       	<form id="form1" action="/smartux_adm/admin/noti/createTermManage.do" method="post">
							                       	<input type="hidden" name="scr_tp" value="${delCheck.scr_tp }">                      
							                       	
						                            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list">
						                            	<tr>
						                            		<td>
						                            		 	<select  id="delCheck">					                                          	 	 
							                                         <option <c:if test="${delCheck.del_yn==1}">selected="selected"</c:if> value="1">삭제</option>
							                                         <option <c:if test="${delCheck.del_yn==0}">selected="selected"</c:if> value="0">미삭제</option>			                                           			                                 		                                            	
			                                           			 </select>		
						                            		</td>
						                            		<td>
						                            			<c:if test="${delCheck.del_yn==0}"><a href="javascript:deleteTermManage('1')"><span class="button small blue">삭제</span></a></c:if>
						                            			<c:if test="${delCheck.del_yn==1}"><a href="javascript:deleteTermManage('0')"><span class="button small blue">삭제 취소</span></a></c:if>
						                            		</td>	
						                            	</tr>
						                            	
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
							                                    	<input type="checkbox" class="termCheck" name="termCheck" value="${terminal.TERM_MODEL}" />						                                    	
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
						                            	
						                            	   <c:if test="${delCheck.del_yn==0}"> 							                       	
									                       	<tr align="center">
							                            		<!-- 체크 박스 -->
							                                     <!-- 등록버튼 -->
							                                    <td class="table_td_04 ">
							                                    	<a href="javascript:createTermManage()"><span class="button small blue"> 등록</span></a>			                                    
							                                    </td>	
							                                                                       
							                                    <!-- 모델명 -->
							                                    <td class="table_td_04 " >
							                                          <input type="text"  id="term_model" name="term_model">                         
							                                    </td>
							                                    
							                                    <!-- 제조사 -->	                                    
							                                    <td class="table_td_04 " > 
							                                    	<input type="text" id="term_make" name="term_make">							                              
							                                    </td>
							                                    
							                                    <!-- LCD사양 -->
							                                    <td class="table_td_04 ">
							                                    	<input type="text" id="lcd_spec" name="lcd_spec">							                                    
							                                    </td>							                                    
							                                   						                                    
									                          </tr>		
							                            </c:if>
						                               
							                           </table>
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
	  <tr>

	    <td height="30">&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr>
	    <td colspan="2" background="/smartux_adm/images/admin/copy_bg.gif" height="60" align="left">
	        <!-- 하단 로그인 사용자 정보 시작 -->
	        
	        <!-- 하단 로그인 사용자 정보 종료 -->
	    </td>
	 </tr>
</tbody>
</table>
</div>


</body>
</html>