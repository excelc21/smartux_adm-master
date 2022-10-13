<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
// ruleCheck Parameter Accept
String ruleCheck = request.getParameter("ruleCheck");
if(ruleCheck == null || ruleCheck == "") ruleCheck = "Y";
%>
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
		window.open("<%=webRoot%>/admin/rule/insertRule.do", "regrule", "width=650,height=500,resizable=yes,scrollbars=yes");
	});
	
	$("#selbtn").click(function(){
		var checkeditems = $("input[name='selchk']:checked");
		var checkeditemslength = checkeditems.length;
		
		if(checkeditemslength == 0){
			alert("등록할 항목을 체크해주세요");
		}else{
			if(confirm("체크된 VOD 랭킹 Rule들을 등록하시겠습니까")){
				///////////var smartUXManager = $("#smartUXManager").val();
				var size = checkeditemslength;
				var checkboxarray = new Array();
				var checkboxval = null;
				var checkboxvalon = null;
				
				var chkNum = 0;
				var chkFlag = false;
				
				for(var i=0; i < size; i++){
					var selval = $(checkeditems[i]).val();
					var temp = selval.split('#');
				
					if(i == 0){
						checkboxval = temp[0];		
						checkboxvalon = temp[1];	
					}else{
						checkboxval = checkboxval +"||"+temp[0];		
						checkboxvalon = checkboxvalon +" + "+temp[1];	
					}
					chkNum++;
					if(temp[2] == "시리즈별 랭킹"){
						chkFlag = true;
					}
				}
				
				if(chkFlag && 1 < chkNum){
					alert("시리즈별 랭킹 룰은 단일 등록만 가능합니다. ");
					return false;
				}
				document.regfrm.rulesHap.value  = checkboxval;
				document.regfrm.rulesHapon.value  = checkboxvalon;
				opener.document.regfrm.rule_name.value=document.regfrm.rulesHap.value;	
				opener.document.regfrm.rule_name_on.value=document.regfrm.rulesHapon.value;		
			}
			 //self.close();
			
			 //익스플로러 버전 비교 후 자동 창 닫기.
			   if(/MSIE/.test(navigator.userAgent)){
				    			//IE 7.0 이상
				    if(navigator.appVersion.indexOf("MSIE 7.0")>=0){
				    	window.open('about:blank', '_self').close();
				    }else{		//IE 7.0 이하
					    window.opener = self;
					    self.close();
			    	}
			  }
		}		
	});
	
	$("#allchk").click(function(){
		var chkallchecked = false;
		
		if($("#allchk").is(":checked")){
			chkallchecked = true;
		}		
		$("input[name='selchk']").attr("checked", chkallchecked);
	});
});

function view_rule(rule_code){
	var url = "<%=webRoot%>/admin/rule/viewRule.do?rule_code=" + rule_code;
	window.open(url, "viewrule", "width=650,height=500,resizable=yes,scrollbars=yes");
}
</script>
<body leftmargin="0" topmargin="0">
<div id="divBody" style="height:100%">
<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%" >
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
						                                            <td class="bold">VOD 랭킹 룰 목록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                     <form id="regfrm" name="regfrm" method="post" action="">
					                            <table border="0" cellspacing="0" cellpadding="0" class="board_list" style="width:400px;">
													<tr>
														<th width="10%" style="padding-top:8px"><input type="checkbox" id="allchk" name="allchk" value=""></th>
														<th width="60%">Rule 이름</th>
														<th width="30%">Rule Type</th>
													</tr>
													<c:choose>
														<c:when test="${result==null || fn:length(result) == 0}">
															<tr>
																<td colspan="3" class="table_td_04">검색된 VOD 랭킹 룰이 없습니다</td>
															</tr>
														</c:when>
														<c:otherwise>
															<c:forEach var="item" items="${result}" varStatus="status">
																<tr>
																	<td class="table_td_04">
																		<input type="checkbox" name="selchk" value="${item.rule_code}#${item.rule_name}#${item.rule_type}">
																	</td>
																	<td class="table_td_04" style="text-align: left;">
																		${item.rule_name}
																	</td>
																	<td class="table_td_04">
																		${item.rule_type}
																	</td>
																</tr>
															</c:forEach>
														</c:otherwise>
														
													</c:choose>
												</table>
												<input type="hidden" id="rulesHap" name="rulesHap" value="" />
												<input type="hidden" id="rulesHapon" name="rulesHapon" value="" />
											</form>	
												
												<table border="0" cellpadding="0" cellspacing="0" style="width:400px;" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="40" align="right">
						                                	<span class="button small blue" id="selbtn">확인</span>
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
</tbody>
</table>
</div>
</body>
</html>
