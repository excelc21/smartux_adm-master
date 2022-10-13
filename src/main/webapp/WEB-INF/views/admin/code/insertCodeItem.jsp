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
// 	if($("#code").val() == "A0008"){
// 		$("#item_nm").attr("readonly", true);
// 		setCodeName();
// 	}
	
	$("#ss_gbn").change(function(){
		setCodeName();
	});
	
	
	$("#regbtn").click(function(){	
		var code = $("#code").val();
		var item_code = trim($("#item_code").val());
		var item_nm = trim($("#item_nm").val());
		var ss_gbn = "";
		var app_type = "";
		var use_yn = $(':input:radio[name="use_yn"]:checked').val();
		
		if(item_code == ""){
			alert("등록하고자 하는 코드를 입력해주세요");
			$("#item_code").focus();
		}else if(item_nm == ""){
			alert("등록하고자 하는 코드명을 입력해주세요");
			$("#item_nm").focus();
	
		}else{
			if ('false' == checkByteMessage(item_code, 100, 2)) {
				alert('코드는 100Byte 이내로 입력해야 합니다.');
				$("#item_code").focus();
				return;
			}
			
			var codeTitle = "코드명"
			if(code == "A0005"){		//테마코드 입력시 한글코드명||영문코드명 식으로 DB Insert
				var item_enm = trim($("#item_enm").val());
				if(item_enm != ""){
					item_nm = item_nm+"||"+item_enm;
				}
				codeTitle = "코드명 + 영문코드명"
			}
			
			if (!(code == "A0009" && item_code == "TVapp_tr") && 'false' == checkByteMessage(item_nm, 300, 2)) {
				alert(codeTitle + '은 300Byte 이내로 입력해야 합니다.');
				$("#item_nm").focus();
				return;
			}
			
			//설정정보의 TVapp_tr 입력시 validation 시작
			if(code == "A0009" && item_code == "TVapp_tr"){
				if(item_nm.indexOf("||") != -1){
					alert(" (| 구분자) 는 반복되어 사용 될 수 없습니다. ");
					$("#item_nm").focus();
					return;
				}
				
				var tvapp_tr = item_nm.split("|");
				if(tvapp_tr.length < 8){
					alert("8개 이상의 값이 입력되어야 합니다. (| 로 구분)");
					$("#item_nm").focus();
					return;
				}
				
				var regexp_num = /^[0-9]*$/;
				var regexp_time = /^(2[0-3]|[01][0-9])([0-5][0-9])$/;
				var regexp_blank = /^[\s]*$/;
				if(!regexp_num.test(tvapp_tr[0])){
					alert("API 호출 주기는 숫자만 입력 가능합니다.");
					return;
				}else{
					if(tvapp_tr[0] < 86400){
						alert("API 호출 주기의 최소 값은 86400 입니다.");
						return;
					}					
				}
				
				if(!regexp_num.test(tvapp_tr[1])){
					alert("노출 주기는 숫자만 입력 가능합니다.");
					return;
				}else{
					if(parseInt(tvapp_tr[1]) < parseInt(tvapp_tr[2])){
						alert("노출 주기는 노출 시간보다 크거나 같아야 합니다.");
						return;
					}
				}
				
				if(!regexp_num.test(tvapp_tr[2])){
					alert("노출 시간은 숫자만 입력 가능합니다.");
					return;
				}
				
				if((tvapp_tr.length - 3) % 5 != 0){
					alert("정의되지 않은 세트 값이 있습니다. (ex. ...채널SID|트리거시작시간|트리거종료시간|URI|KLPGA여부...)");
					return;
				}
				
				var infoArray = [];
				for(var i = 3; i < tvapp_tr.length ; i+=5){
					infoArray.push({val1 : tvapp_tr[i], val2 : tvapp_tr[i+1], val3 : tvapp_tr[i+2], val4 : tvapp_tr[i+3], val5 : tvapp_tr[i+4]});
				}

				for(var key in infoArray){
					infoArray[key].validation = function () {
						 if(!regexp_num.test(this.val1)){
							return "채널SID는 숫자만 입력 가능합니다.";
						 }else{
							 var num = this.val1.toString();
							 if(num.length != 3){
								 return "채널SID는 3자리 숫자만 입력 가능합니다.";
							 }
						 }
						 
						 if(!regexp_num.test(this.val2)){
							 return "트리거 시작시간은 숫자만 입력 가능합니다.";
						 }else{
							 var num = this.val2.toString();
							 if(num.length != 4){
								 return "트리거 시작시간은 4자리 숫자만 입력 가능합니다.";
							 }else{
								 if(!regexp_time.test(this.val2)){
									 return "트리거 시작시간은 HHMM만 입력 가능합니다. (ex. 2460 불가)";
								 }
							 }
						 }
						 
						 if(!regexp_num.test(this.val3)){
							 return "트리거 종료시간은 숫자만 입력 가능합니다.";
						 }else{
							 var num = this.val3.toString();
							 if(num.length != 4){
								 return "트리거 종료시간은 4자리 숫자만 입력 가능합니다.";
							 }else{
								 if(!regexp_time.test(this.val3)){
									 return "트리거 종료시간은 HHMM만 입력 가능합니다. (ex. 2460 불가)";
								 }else{
									 if(parseInt(this.val3) <= parseInt(this.val2)){
										 return "트리거 종료시간은 트리거 시작시간보다 커야합니다. (시작시간 : " + this.val2 +")";
									 }									 											 
								 }
							 }
						 }
						 
						 if(regexp_blank.test(this.val4)){
							  return "URI 값이 입력되지 않았습니다.";
						 }
						 
						 if(!(this.val5 == '0' || this.val5 == '1')){
							  return "KLPGA Live 여부는 0또는 1이어야 합니다. (0:비라이브, 1:라이브)";
						 }
						 
						 return "SUCCESS";
					};
					
					infoArray[key].toString = function () {
						return "|" + this.val1 + "|" + this.val2 + "|" + this.val3 + "|" + this.val4 + "|" + this.val5; 
					};
				}
				
				var tvapp_tr_result = tvapp_tr[0] + "|" + tvapp_tr[1] + "|" + tvapp_tr[2];
				for (var prop in infoArray) {
					if(infoArray[prop].validation() != "SUCCESS"){
						alert(infoArray[prop].validation());
						return;
					}else{
						if('false' == checkByteMessage(tvapp_tr_result+infoArray[prop].toString(), 300, 2)){
							alert(codeTitle + '은 300Byte 이내로 입력해야 합니다. 초과된 세트는 절삭됩니다.');
							$("#item_nm").val(tvapp_tr_result);
							$("#item_nm").focus();
							return;
						}else{
							tvapp_tr_result += infoArray[prop].toString();
						}
					}
				}
			}
			//설정정보의 TVapp_tr 입력시 validation 종료
			
			if(code == "A0008"){				// Smart Start 관련 코드 아이템 등록시
				ss_gbn = trim($("#ss_gbn").val());
				if(ss_gbn == ""){
					alert("등록하고자 하는 SmartUX 타입을 입력해주세요");
					$("#ss_gbn").focus();
					return;
				}
			}else if(code == "A0009"){			// 설정 정보 등록시
				app_type = $("#app_type").val();	
			}
			
			var smartUXManager = $("#smartUXManager").val();
			$.post("<%=webRoot%>/admin/code/insertCodeItem.do", 
					 {code : code, item_code : item_code, item_nm : item_nm, ss_gbn : ss_gbn, app_type : app_type, use_yn : use_yn, smartUXManager : smartUXManager},
					  function(data) {
						 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
						 	var flag = data.flag;
						 	var message = data.message;
						 	
						 	if(flag == "0000"){						// 정상적으로 처리된 경우
						 		opener.location.reload();
						 		//opener.location.href='/smartux_adm/admin/code/getCodeItemList.do?code='+code;
								alert("코드가 등록되었습니다");
								self.close();
						 	}else{
						 		if(flag == "EXISTS ITEM_CODE"){
						 			alert("현재 등록하고자 하는 코드 " + item_code + "은 이미 등록되어 있는 코드입니다\n다른 코드를 사용해주세요");	
						 			$("#item_code").focus();
						 		}else if(flag == "EXISTS ITEM_NM"){
						 			alert("현재 등록하고자 하는 코드명 " + item_nm + "은 이미 등록되어 있는 코드명입니다\n다른 코드명을 사용해주세요");
					 				$("#item_nm").focus();
						 		}else if(flag == "EXISTS SS_GBN"){
						 			alert("현재 등록하고자 하는 SmartUX 타입 " + ss_gbn + "은 이미 등록되어 있는 타입입니다\n다른 타입을 사용해주세요");
					 				$("#item_nm").focus();
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
});

function setCodeName(){
	if($("#ss_gbn").val() == "LIVE"){
		$("#item_nm").val("실시간 시청률 TOP");
	}else if($("#ss_gbn").val() == "VOD"){
		$("#item_nm").val("Best VOD");
	}else if($("#ss_gbn").val() == "CAT_MAP"){
		$("#item_nm").val("카테고리 매핑");
	}else if($("#ss_gbn").val() == "SCHEDULE"){
		$("#item_nm").val("자체편성");
	}else if($("#ss_gbn").val() == "WISH"){
		$("#item_nm").val("Wish List");
	}else if($("#ss_gbn").val() == "AD_H"){
		$("#item_nm").val("HALF AD");
	}else if($("#ss_gbn").val() == "AD_F"){
		$("#item_nm").val("FULL AD");
	}else if($("#ss_gbn").val() == "RCM"){
		$("#item_nm").val("추천시스템");
	}
}
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
						                                            <td class="bold">SmartUX 코드값 등록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="regfrm" name="regfrm" method="post" action="">
					                            <table border="0" cellpadding="0" cellspacing="0" width="400" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">코드</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="text" id="item_code" name="item_code" size="35" style="font-size: 12px;" />	
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">코드명</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="text" id="item_nm" name="item_nm" size="35" style="font-size: 12px;" />								
														</td>
					                                </tr>
					                               <c:if test="${code == 'A0005'}">
					                               	<tr align="center">
					                                    <th width="25%">영문 코드명</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="text" id="item_enm" name="item_enm" size="35" maxlength="100" style="font-size: 12px;" value="" />								
														</td>
					                                </tr>
					                               </c:if>
					                                
					                               <c:if test="${code == 'A0008'}">
														<tr align="center">
					                                    <th width="25%">SmartUX 타입</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<input type="text" id="ss_gbn" name="ss_gbn" maxlength="10" style="font-size: 12px; IME-MODE: disabled;"/>
<!-- 															<select id="ss_gbn" name="ss_gbn"> -->
<%-- 																<c:forEach var="item" items="${smartstart}" varStatus="status"> --%>
<!-- 																	${status.count} -->
<%-- 																		<c:choose> --%>
<%-- 																			<c:when test="${status.count == 1}"> --%>
<%-- 																				<option value="${item}" selected>${item}</option> --%>
<%-- 																			</c:when> --%>
<%-- 																			<c:otherwise> --%>
<%-- 																				<option value="${item}">${item}</option> --%>
<%-- 																			</c:otherwise> --%>
<%-- 																		</c:choose> --%>
<%-- 																</c:forEach> --%>
<!-- 															</select>								 -->
														</td>
					                                </tr>
													</c:if>
													<c:if test="${code == 'A0009'}">
														<tr align="center">
					                                    <th width="25%">어플타입</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<select id="app_type" name="app_type">
																<c:forEach var="item" items="${app_type_list}" varStatus="status">
																	<c:choose>
																		<c:when test="${status.count == 1}">
																			<option value="${item.item_code}" selected>${item.item_nm}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${item.item_code}">${item.item_nm}</option>
																		</c:otherwise>
																	</c:choose>
																</c:forEach>
															</select>								
														</td>
					                                </tr>
													</c:if>
					                                
					                                
					                                <tr align="center">
					                                    <th width="25%">사용여부</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="radio" id="use_ynY" name="use_yn" value="Y" checked/>예
															<input type="radio" id="use-ynN" name="use_yn" value="N" />아니오								
														</td>
					                                </tr>
					                            	</tbody>
					                            </table>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="400" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<span class="button small blue" id="regbtn">등록</span>	
						                                	<span class="button small blue" id="resetbtn">재작성</span>	
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                       	<input type="hidden" id="code" name="code" value="${code}" />
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