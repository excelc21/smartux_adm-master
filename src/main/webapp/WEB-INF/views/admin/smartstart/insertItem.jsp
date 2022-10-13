<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">

//Smart Start 장르 컨트롤 
function TargetChange(val) {
	var frm = document.regfrm;
	
	// 1 : VOD	, 2 : VOD 가 아닐 경우
	if ( val == "VOD" ) {
		document.all.selitemgenre.style.display='block';
	} else {
		document.all.selitemgenre.style.display='none';
	}
}

$(document).ready(function(){
	$("#regbtn").click(function(){	
		var code = $("#code").val();
		var item_code = $("#item_code").val();
		var item_nm = $("#item_nm").val();
		var selitemgenre = $("#selitemgenre").val();
		var selitemtype = $("#selitemtype").val();		
		var use_yn = $(':input:radio[name="use_yn"]:checked').val();
		
		if(item_code == ""){
			alert("등록하고자 하는 코드를 입력해주세요");
			$("#item_code").focus();
		}else if(item_nm == ""){
			alert("등록하고자 하는 항목명을 입력해주세요");
			$("#item_nm").focus();
	
		}else{
			
			$.post("<%=webRoot%>/admin/smartstart/insertItem.do", 
					 {code : code, item_code : item_code, item_nm : item_nm, selitemgenre : selitemgenre, selitemtype : selitemtype,  use_yn : use_yn},
					  function(data) {
						 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
						 	var flag = data.flag;
						 	var message = data.message;
						 	
						 	if(flag == "0000"){						// 정상적으로 처리된 경우
						 		opener.location.reload();
								alert("코드가 등록되었습니다");
								self.close();
						 	}else{
						 		if(flag == "9000"){
						 			if(message == "item_code"){
						 				alert("현재 등록하고자 하는 코드 " + item_code + "은 이미 등록되어 있는 코드입니다\n다른 코드를 사용해주세요");	
						 				$("#item_code").focus();
						 			}else{
						 				alert("현재 등록하고자 하는 항목명 " + item_nm + "은 이미 등록되어 있는 코드명입니다\n다른 코드명을 사용해주세요");
						 				$("#item_nm").focus();
						 			}
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
		//$("#updfrm")[0].reset();
		 window.close();		
	});
});
</script>
</head>

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
						                                            <td class="bold">스마트스타트 항목 등록</td>
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
																<input type="text" id="item_code" name="item_code" value=""  size="35" style="font-size: 12px;" />								
															</td>
						                                </tr>
						                                <tr align="center">
						                                    <th width="25%">항목명</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<input type="text" id="item_nm" name="item_nm" value="" size="35" style="font-size: 12px;" />								
															</td>
						                                </tr>
						                                <tr align="center">
						                                    <th width="25%">Smart Start 타입</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<select id="selitemtype" name="selitemtype"  onChange="TargetChange(this.value);" >
																	<option value="" selected >===선택===</option>
																	<c:forEach var="item" items="${itemResult}" varStatus="status">
																		<option value="${item.itemtype}" >${item.itemtype}</option>
																	</c:forEach>
																</select>								
															</td>
						                                </tr>
						                                <tr align="center">
						                                    <th width="25%">Smart Start 장르</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<select id="selitemgenre" name="selitemgenre"  style="display:block" >
																	<option value="" selected >===선택===</option>
																	<option value="방송" >방송</option>
																	<option value="영화" >영화</option>
																	<option value="성인" >성인</option>
																	<option value="다큐" >다큐</option>
																	<option value="라이프" >라이프</option>
																	<option value="교육" >교육</option>
																</select>							
															</td>
						                                </tr>
						                                <tr align="center">
						                                    <th width="25%">사용여부</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<input type="radio" id="use_ynY" name="use_yn" value="Y" size="35" style="font-size: 12px;" checked/>예
																<input type="radio" id="use_ynY" name="use_yn" value="N" size="35" style="font-size: 12px;" />아니오								
															</td>
						                                </tr>
					                            	</tbody>
					                            </table>
					                            	<input type="hidden" id="code" name="code" value="${code}" />
					                            	<!-- 
													<input type="button" id="regbtn" value="등록" />
													<input type="button" id="resetbtn" value="재작성" />
													<input type="button" id="closebtn" value="닫기" />
													 -->
					                            </form>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="400" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<input type="button" id="regbtn" value="등록" class="button small blue"/>
						                                	<input type="button" id="resetbtn" value="재작성"  class="button small blue"/>
						                                	<input type="button" id="closebtn" value="닫기"  class="button small blue"/>					                                	
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