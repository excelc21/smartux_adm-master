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
	$(".checkbox").click(function (e) {
		//체크했다면 자신을 제외한 다른 체크를 해제
		$(".checkbox:checked").not(this).removeAttr("checked"); 
	});	
	
	//패널 선택
	$("#choiceBtn").click(function (e) {
		var check_cnt = $('input:checkbox[name="chk"]:checked').length;
		if(check_cnt == 0){
			alert('패널을 선택해주세요.');
		}else {
			if(confirm('선택한 패널로 적용 하시겠습니까?')){
				var dataobj = new Object(); 
				var check_value = $('input:checkbox[name="chk"]:checked').val();
				
				if(check_value != ''){
					var check_value_arr = check_value.split("|");
					
					if(check_value_arr != '' && check_value_arr.length==2){
						dataobj.id =check_value_arr[0];
						dataobj.name=check_value_arr[1];
						
						var callbak_m = eval("opener."+"${callback}");    
						callbak_m(dataobj);
						self.close();
					}
				}				
			}
		}
	});
});

//검색
function doSearch(){
	var select_value = $("#findName option:selected").val();
	if(select_value != '' && select_value !=null){
		$("#form1").submit();
	}else{
		alert('검색 타입을 선택해주세요.');
	}
}
</script>
<body leftmargin="0" topmargin="0">
<div id="divBody" style="height:100%">
<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
    <tbody>
    	<tr>
    		<td valign="top">
			<!-- ######################## body start ######################### -->
            	<table border="0" cellpadding="15" cellspacing="0" width="100%" align="center" >
					<tbody>
						<tr>
							<td>
								<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" >
	                            	<tbody>
	                            		<tr>
	                            			<td>
												<form id="form1" method="get" action="./popPanelList.do">	
													<input type="hidden" id="callback" name="callback" value="${callback}">
	                                				<table border="0" cellpadding="0" cellspacing="0" width="100%" class="search_table" >
	                                					<tbody >
	                                						<tr >
		                                    					<td width="15">&nbsp;</td>
		                                    					<td width="80">
		                                    						<img src="/smartux_adm/images/admin/search_title4.gif" border="0" height="47" width="62">
		                                    					</td>
		                                   		 				<td>
			                                    					<table border="0" cellpadding="0" cellspacing="0" width="280">		                                    
                                        								<tbody>
                                        									<tr><td>&nbsp;</td></tr>
                                        									<tr>
									                                            <td width="10">
									                                            	<img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9" />
									                                            </td>
			                                            						<td>
									                                                <select class="select"  name="findName" id="findName">
									                                                	<option <c:if test="${searchVo.findName == ''}">selected="selected"</c:if> value="">선택하세요</option>
									                                                    <option <c:if test="${searchVo.findName == 'pannel_id'}">selected="selected"</c:if> value="pannel_id">패널ID</option>					                                                    
									                                                    <option <c:if test="${searchVo.findName == 'pannel_nm'}">selected="selected"</c:if> value="pannel_nm">패널명</option>					                                                    
									                                                </select>  
									                                            </td>
									                                            <td>
									                                            	<input type="text" id="findValue" name="findValue" value="${searchVo.findValue}" size="20" style="font-size: 12px;" onKeyPress="if(event.keyCode==13){ doSearch();}" />
									                                            </td>
									                                            <td align="left">
									                                                <img src="/smartux_adm/images/admin/search.gif" border="0" height="22"  width="65" onclick="doSearch();" style="cursor:pointer;">
									                                            </td>
			                                         						</tr>
	                                    								</tbody>
	                                    							</table>
	                                    						</td>
			                            					</tr> 					                            
			                        					</tbody>
			                        				</table>
			                        			</form>
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
							                                            <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
							                                            <td class="bold">패널 선택</td>
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
			                                			   <th scope="col" width="5%">선택</th>
						                                   <th scope="col" width="10%">패널ID</th>
													 	   <th scope="col" width="*">패널명</th>
						                                </tr>
				                                
						                                <c:if test="${panelList == '[]' }"> 
							                                <tr align="center">
							                                    <td class="table_td_04" colspan="3">데이터가 존재 하지 않습니다.</td>					                                    
							                                </tr>
														</c:if>
			                                		</tbody>
			                          
					                                <c:forEach items="${panelList}" var="data">
						                            	<tr align="center">
						                            		<!-- 체크 박스 -->
						                                    <td class="table_td_04" >
				                                    			<input type="checkbox" class="checkbox" id="${data.PANNEL_ID}" value="${data.PANNEL_ID}|${data.PANNEL_NM}"  name="chk"/>
						                                    </td>
						                                    <td class="table_td_04">${data.PANNEL_ID}	</td>
						                                    <td class="table_td_04">${data.PANNEL_NM}</td>
						                                </tr>
					                            	</c:forEach>
			                            		</table>
				                        
						                        <!-- 등록/수정 버튼 -->
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            <tbody>
						                            <tr>
						                                <td align="right">
						                                	<span class="button small blue" id="choiceBtn" name="choiceBtn">선택</span>
						                                </td>
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
</div>
</body>
</html>