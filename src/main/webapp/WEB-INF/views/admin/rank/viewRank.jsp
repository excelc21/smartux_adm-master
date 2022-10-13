<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<%@ taglib prefix="fn"  	uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" 	uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">

	$(document).ready(function(){
	$("#selGenrebtn").click(function(){
		var url = window.open("<%=webRoot%>/admin/rank/selectGenrePopup.do?multiCheck=Y&genreElementid=appendGenreList&genreHiddenid=hiddenGenreList"
				,"selectGenrePopup","scrollbars=yes,toolbar=yes,resizable=yes,width=650,height=390"); 
		url.moveTo(530,10);
	});	
		
	$("#selRulebtn").click(function(){
		var url = window.open("<%=webRoot%>/admin/rank/selectRulePopup.do?ruleCheck=Y"
				, "selectRulePopup","width=460,height=424,resizable=yes,scrollbars=yes"); 
		url.moveTo(530,100);
	});	

	$("#regbtn").click(function(){	
		var rank_code = $("#rank_code").val();
		var rank_term = $("#rank_term").val();
		var rank_name = $("#rank_name").val();
		var hgenre = $("#hgenre").val();
		var tgenre = $("#tgenre").val();
		var rule_name = $("#rule_name").val();	
		var smartUXManager = $("#smartUXManager").val();
		
		if(rank_term == ""){
			alert("등록하고자 하는 기한을 입력해주세요");
			$("#rank_term").focus();
		}else if(rank_name == ""){
			alert("등록하고자 하는 랭킹 이름을 입력해주세요");
			$("#rank_name").focus();
		}else if(hgenre == ""){
			alert("등록하고자 하는 장르를 선택해 주세요");
			$("#tgenre").focus();
		}else if(rule_name == ""){
			alert("등록하고자 하는 룰을 선택해주세요");
			$("#rule_name").focus();
		}else{
			
			$.post("<%=webRoot%>/admin/rank/updateRank.do", 
					 {rank_code : rank_code , rank_term : rank_term, rank_name : rank_name, hgenre : hgenre, rule_name : rule_name , smartUXManager : smartUXManager},
					  function(data) {
						 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
						 	var flag = data.flag;
						 	var message = data.message;
						 	
						 	if(flag == "0000"){						// 정상적으로 처리된 경우
						 		opener.location.reload();
								alert("랭킹 데이터가 변경되었습니다");
								self.close();
						 	}else{
						 		if(flag == "9000"){
						 				alert("현재 변경하고자 하는 랭킹명 " + rank_name + "은 이미 등록되어 있는 랭킹 명입니다\n다른 랭킹명을 사용해주세요");
						 				$("#rank_name").focus();
						 		}else{
						 			alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
						 		}
						 	}
					  },
					  "json"
		    );
		}
		
	});
	
	$("#resetbtn").click(function(){
		$("#regfrm")[0].reset();
	});
	
	$("#closebtn").click(function(){
		 window.close();		
	});	
});
	
</script>
</head>

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
						                                            <td class="bold">VOD 랭킹 데이터 변경</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>						                       	
						                       	<form id="regfrm" name="regfrm" method="post" action="">
					                            <table border="0" cellpadding="0" cellspacing="0" width="450" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">랭킹 기한</th>
					                                    <td width="1%"></td>
					                                    <td width="74%" align="left" >
															<input type="text" id="rank_term" name="rank_term" value="${result.rank_term}" size="35" maxlength="100" style="font-size: 12px;" />								
														</td>
					                                </tr>					                                
					                                <tr align="center">
					                                    <th width="25%">랭킹 데이터명</th>
					                                    <td width="1%"></td>
					                                    <td width="74%" align="left" >
															<input type="text" id="rank_name" name="rank_name" value="${result.rank_name}" size="35" maxlength="100" style="font-size: 12px;" />																	
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">VOD 장르 코드</th>
					                                    <td width="1%"></td>
					                                    <td width="74%" align="left" >
					                                   	<input type="text"   id="tgenre"  name="tgenre"  value="${fn:replace( result.genre_code, '__',' > ' )}"  maxlength="100" size="35" style="font-size: 12px;" readonly/>
															<input type="hidden" id="hgenre"  name="hgenre"  value="${fn:replace( result.genre_code, ' + ','||' )}"  maxlength="100"  />
															<input type="button" value="장르" id="selGenrebtn"  align="left"  class="button small blue"/>								
														</td>
					                                </tr>					                                
					                                <tr align="center">
					                                    <th width="25%">랭킹 룰 코드명</th>
					                                    <td width="1%"></td>
					                                    <td width="74%" align="left" >
					                                    	<input type="text" id="rule_name_on" name="rule_name_on" value="${fn:replace( result.rule_code, '||',' + ' )}"  size="35" maxlength="100" style="font-size: 12px;" readonly /> 
															<input type="hidden" id="rule_name" name="rule_name" value="${fn:replace( result.rule_code_origin, ' + ','||' )}"  />
															<input type="button" value=" 룰  "   id="selRulebtn" align="left"  class="button small blue"/>																	
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">카테고리 구분</th>
					                                    <td width="1%"></td>
					                                    <td width="74%" align="left" >
					                                    	${result.category_gb}
														</td>
					                                </tr>
							                       	</tbody>
					                            </table>
					                            
					                        	<br>
												<br>
					                            <table border="0" cellpadding="0" cellspacing="0" width="450" >
					                              <tr  align="right">
				                                    <td >				                                    	
												    	<input type="button" id="regbtn"   value="확인"    class="button small blue" align="right"/>
												    	<input type="button" id="resetbtn" value="재작성"  class="button small blue"/>
						                               	<input type="button" id="closebtn" value="닫기"    class="button small blue"/>		
												    </td>
												  </tr>  
												</table>  
												<input type="hidden" id="smartUXManager" name="smartUXManager" value="<%=id_decrypt %>" />
											 	<input type="hidden" id="rank_code" name="rank_code" value="${rank_code}" />
					                            	
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


