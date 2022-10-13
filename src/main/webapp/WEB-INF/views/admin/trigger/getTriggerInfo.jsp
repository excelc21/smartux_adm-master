<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<link href="${pageContext.request.contextPath}/css/anytime_style.css" rel="stylesheet" type="text/css">
<script src="${pageContext.request.contextPath}/js/anytime.js"></script>
<script src="${pageContext.request.contextPath}/js/papaparse.js"></script>

<style>
input[type=text]
{
	padding: 5px;
	box-sizing: border-box;
	-moz-box-sizing: border-box;
	-webkit-box-sizing: border-box;
	border: 1px solid #CCC;
}
</style>

<script type="text/javascript">
var delCnt = 0;

$(document).ready(function(){
	AnyTime.picker('trigger_start', { format: '%H%i', labelTitle: '시작시간', time:''});
	AnyTime.picker('trigger_end', { format: '%H%i', labelTitle: '종료시간', time:''});
	
	var item_code = "${item_code}";
	var item_nm = "${item_nm}";
	var tvapp_tr = item_nm.split("|");
	
	if(tvapp_tr.length < 8 || (tvapp_tr.length - 3) % 5 != 0){
		alert("정의 되지 않은 정보입니다. 신규등록 하십시오.");
		$("#trigger_cd").val(item_code);
	}else{
		$("#trigger_cd").val(item_code);
		$("#api_pd").val(tvapp_tr[0]);
		$("#trigger_pd").val(tvapp_tr[1]);
		$("#trigger_exp").val(tvapp_tr[2]);
		
		var infoArray = [];
		for(var i = 3; i < tvapp_tr.length ; i+=5){
			infoArray.push({val1 : tvapp_tr[i], val2 : tvapp_tr[i+1], val3 : tvapp_tr[i+2], val4 : tvapp_tr[i+3], val5 : tvapp_tr[i+4]});
		}
		
		var tbCnt = 0;
		for(var key in infoArray){
			if(tbCnt == 0){
				$("#trigger_ch").val(infoArray[key].val1);
				$("#trigger_start").val(infoArray[key].val2);
				$("#trigger_end").val(infoArray[key].val3);
				$("#trigger_uri").val(infoArray[key].val4);
				$("input:radio[name=trigger_live_yn][value=" + infoArray[key].val5 + "]").attr("checked","checked");
			} else{
				var addText = 
					'<tr align="center" id="trTrigger">'+
					'	<td class="table_td_04" style="border-bottom: 0px;">'+
					'       <input type="text" name="trigger_ch" id="trigger_ch'+tbCnt+'" maxlength="3" style="width: 50%;"/>'+
					'		<span class="button small blue" name="chbtn" >선택</span>'+
					'	</td>'+
					'	<td class="table_td_04" style="border-bottom: 0px;">'+
					'       <input type="text" name="trigger_start" id="trigger_start'+tbCnt+'" maxlength="4" style="width: 90%;"/>'+
					'	</td>'+
					'	<td class="table_td_04" style="border-bottom: 0px;">'+
					'       <input type="text" name="trigger_end" id="trigger_end'+tbCnt+'" maxlength="4" style="width: 90%;"/>'+
					'	</td>'+
					'	<td class="table_td_04" style="border-bottom: 0px;">'+
					'       <input type="text" name="trigger_uri" id="trigger_uri'+tbCnt+'" style="width: 90%;"/>'+
					'	</td>'+
					'	<td class="table_td_04" style="border-bottom: 0px;">'+
					'		<input type="radio" name="trigger_live_yn'+tbCnt+'" value="1" /> 라이브'+
					'		<input type="radio" name="trigger_live_yn'+tbCnt+'" value="0" /> 비라이브'+
					'	</td>'+
					'	<td class="table_td_04" style="border-bottom: 0px;">'+
					'		<span class="button small blue" name="delbtn">삭제</span>'+
					'	</td>'+
					'</tr>';	
				
				var trHtml = $( "tr[id=trTrigger]:last" );
				trHtml.after(addText);
				
				AnyTime.picker('trigger_start'+tbCnt, { format: '%H%i', labelTitle: '시작시간', time:''});
				AnyTime.picker('trigger_end'+tbCnt, { format: '%H%i', labelTitle: '종료시간', time:''});
				
				$("#trigger_ch"+tbCnt).val(infoArray[key].val1);
				$("#trigger_start"+tbCnt).val(infoArray[key].val2);
				$("#trigger_end"+tbCnt).val(infoArray[key].val3);
				$("#trigger_uri"+tbCnt).val(infoArray[key].val4);
				$("input:radio[name=trigger_live_yn"+tbCnt+"][value=" + infoArray[key].val5 + "]").attr("checked","checked");
			}
			tbCnt++;
		}
	}
	
	//즉시 적용
	$("#applybtn").click(function(){
		var code = "A0009";
		var msg = "트리거 정보를 적용 하시겠습니까?";
		
		if(confirm(msg)){
			$.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>처리 중..</b>"
			});
			$.post("<%=webRoot%>/admin/code/applyCache.do", 
				 {code : code,
			      callByScheduler : 'A'},
				  function(data) {
					 	var flag = data.flag;
					 	var message = data.message;
					 	
					 	if(flag == "0000"){
					 		alert("트리거 정보가 적용되었습니다");
					 	}else{
					 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
					 	}
					 	$.unblockUI();
					 	//console.log(data);
				  },
				  "json"
		    );
		}
	});
	
	//저장 버튼
	$("#regbtn").click(function(){
		var check_length = false;
		var check_validation = false;

		var api_pd = $("#tbTrigger tbody tr td input").eq(1).val();
		var trigger_pd = $("#tbTrigger tbody tr td input").eq(2).val();
		var trigger_exp = $("#tbTrigger tbody tr td input").eq(3).val();
		
		var regexp_num = /^[0-9]*$/;
		var regexp_time = /^(2[0-3]|[01][0-9])([0-5][0-9])$/;
		var regexp_blank = /^[\s]*$/;
		
		if(!regexp_num.test(api_pd) || regexp_blank.test(api_pd)){
			alert("API 호출 주기는 숫자만 입력 가능합니다.");
			$("#api_pd").focus();
			return;
		}else{
			if(api_pd < 86400){
				alert("API 호출 주기의 최소 값은 86400 입니다.");
				$("#api_pd").focus();
				return;
			}					
		}
		
		if(!regexp_num.test(trigger_pd) || regexp_blank.test(trigger_pd)){
			alert("노출 주기는 숫자만 입력 가능합니다.");
			$("#trigger_pd").focus();
			return;
		}else{
			if(parseInt(trigger_pd) < parseInt(trigger_exp)){
				alert("노출 주기는 노출 시간보다 크거나 같아야 합니다.");
				$("#trigger_pd").focus();
				return;
			}
		}
		
		if(!regexp_num.test(trigger_exp) || regexp_blank.test(trigger_exp)){
			alert("노출 시간은 숫자만 입력 가능합니다.");
			$("#trigger_exp").focus();
			return;
		}
		
		var tvapp_tr_result = api_pd + "|" + trigger_pd + "|" + trigger_exp;

		$("#tbTriggerCh tr").each(function(){
			
			var trigger_ch = $(this).find('input').eq(0).val();
			var trigger_start = $(this).find('input').eq(1).val();
			var trigger_end = $(this).find('input').eq(2).val();
			var trigger_uri = $(this).find('input').eq(3).val();
			var trigger_live_yn = $(this).find('input[type=radio]:checked').val();
			
			if(!regexp_num.test(trigger_ch) || regexp_blank.test(trigger_ch)){
				alert("채널SID는 숫자만 입력 가능합니다.");
				$(this).find('input').eq(0).focus();
				check_validation = true;
				return false;
			 }else{
				 if(trigger_ch.length != 3){
					 alert("채널SID는 3자리 숫자만 입력 가능합니다.");
					 $(this).find('input').eq(0).focus();
					 check_validation = true;
					 return false;
				 }
			 }
			 
			 if(!regexp_num.test(trigger_start) || regexp_blank.test(trigger_start)){
				 alert("트리거 시작시간은 숫자만 입력 가능합니다.");
				 $(this).find('input').eq(1).focus();
				 check_validation = true;
				 return false;
			 }else{
				 if(trigger_start.length != 4){
					 alert("트리거 시작시간은 4자리 숫자만 입력 가능합니다.");
					 $(this).find('input').eq(1).focus();
					 check_validation = true;
					 return false;
				 }else{
					 if(!regexp_time.test(trigger_start)){
						 alert("트리거 시작시간은 HHMM만 입력 가능합니다. (ex. 2460 불가)");
						 $(this).find('input').eq(1).focus();
						 check_validation = true;
						 return false;
					 }
				 }
			 }
			 
			 if(!regexp_num.test(trigger_end) || regexp_blank.test(trigger_end)){
				 alert("트리거 종료시간은 숫자만 입력 가능합니다.");
				 $(this).find('input').eq(2).focus();
				 check_validation = true;
				 return false;
			 }else{
				 if(trigger_end.length != 4){
					 alert("트리거 종료시간은 4자리 숫자만 입력 가능합니다.");
					 $(this).find('input').eq(2).focus();
					 check_validation = true;
					 return false;
				 }else{
					 if(!regexp_time.test(trigger_end)){
						 alert("트리거 종료시간은 HHMM만 입력 가능합니다. (ex. 2460 불가)");
						 $(this).find('input').eq(2).focus();
						 check_validation = true;
						 return false;
					 }else{
						 if(parseInt(trigger_end) <= parseInt(trigger_start)){
							 alert("트리거 종료시간은 트리거 시작시간보다 커야합니다. (시작시간 : " + trigger_start +")");
							 $(this).find('input').eq(2).focus();
							 check_validation = true;
							 return false;
						 }									 											 
					 }
				 }
			 }
			 
			 if(regexp_blank.test(trigger_uri)){
				 alert("URI 값이 입력되지 않았습니다.");
				 $(this).find('input').eq(3).focus();
				 check_validation = true;
				 return false;
			 }
			
			 var tr_result = "|"+ trigger_ch + "|" + trigger_start + "|" + trigger_end + "|" + trigger_uri + "|" + trigger_live_yn;
			
			 if('false' == checkByteMessage(tvapp_tr_result+tr_result, 300, 2)){
			 	check_length = true;
				
				var trHtml = $(this);
			    trHtml.remove();
			    delCnt++;
			}else{
				tvapp_tr_result += tr_result;
			}
		});
		
		if(!check_validation){
			if(check_length){
				alert('트리거 정보는 300Byte 이내로 입력해야 합니다. 초과된 세트는 절삭됩니다.(다시 저장 해주세요.)');
				return;
			}else{
				var itemCode = $("#trigger_cd").val();
				var itemNm = tvapp_tr_result;
				var smartUXManager = $("#smartUXManager").val();
				$.post("<%=webRoot%>/admin/trigger/updateTriggerInfo.do", 
					 {itemCode : itemCode, itemNm : itemNm, smartUXManager : smartUXManager},
					  function(data) {
						 	var flag = data.flag;
						 	var message = data.message;
						 	
						 	if(flag == "0000"){
						 		location.reload();
								alert("트리거 정보가 등록되었습니다");
						 	}else{
						 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
						 	}
					  },
					  "json"
			    );
			}
		}
	});
});

//추가 버튼
$(document).on("click","span[id=addbtn]",function(){
	var tbCnt = $('#tbTriggerCh tr').length + delCnt;
	var addText = 
		'<tr align="center" id="trTrigger">'+
		'	<td class="table_td_04" style="border-bottom: 0px;">'+
		'       <input type="text" name="trigger_ch" id="trigger_ch'+tbCnt+'" maxlength="3" style="width: 50%;"/>'+
		'		<span class="button small blue" name="chbtn" >선택</span>'+
		'	</td>'+
		'	<td class="table_td_04" style="border-bottom: 0px;">'+
		'       <input type="text" name="trigger_start" id="trigger_start'+tbCnt+'" maxlength="4" style="width: 90%;"/>'+
		'	</td>'+
		'	<td class="table_td_04" style="border-bottom: 0px;">'+
		'       <input type="text" name="trigger_end" id="trigger_end'+tbCnt+'" maxlength="4" style="width: 90%;"/>'+
		'	</td>'+
		'	<td class="table_td_04" style="border-bottom: 0px;">'+
		'       <input type="text" name="trigger_uri" id="trigger_uri'+tbCnt+'" style="width: 90%;"/>'+
		'	</td>'+
		'	<td class="table_td_04" style="border-bottom: 0px;">'+
		'		<input type="radio" name="trigger_live_yn'+tbCnt+'" checked="checked" value="1" /> 라이브'+
		'		<input type="radio" name="trigger_live_yn'+tbCnt+'" value="0" /> 비라이브'+
		'	</td>'+
		'	<td class="table_td_04" style="border-bottom: 0px;">'+
		'		<span class="button small blue" name="delbtn">삭제</span>'+
		'	</td>'+
		'</tr>';	
		
	var trHtml = $( "tr[id=trTrigger]:last" );
	trHtml.after(addText);
	
	AnyTime.picker('trigger_start'+tbCnt, { format: '%H%i', labelTitle: '시작시간', time:''});
	AnyTime.picker('trigger_end'+tbCnt, { format: '%H%i', labelTitle: '종료시간', time:''});
});
 
//삭제 버튼
$(document).on("click","span[name=delbtn]",function(){
	var msg = "삭제하시겠습니까?";
	if(confirm(msg)){
	    var trHtml = $(this).parent().parent();
	    trHtml.remove();
	    delCnt++;
	}
});

//채널 선택
$(document).on("click","span[name=chbtn]",function(){
	var target = $(this).parent().children('input').attr('id');
	var url = "<%=webRoot%>/admin/trigger/getChannelInfo.do?opener=trigger&target="+target;
	window.open(url, "viewchannel", "width=700,height=540");
});
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
                                    트리거 정보
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
						                                            <td class="bold">트리거 정보</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        
					                            <table border="0.1" cellspacing="0" cellpadding="0" id="tbTrigger" class="board_list" style="border-collapse:collapse; border:1px gray solid;width:95%">
													<tbody>
					                                <tr align="center">
					                                    <th width="10%">트리거코드</th>
					                                    <td width="15%" align="left" style="border-bottom: 0px;">
															<input type="text" id="trigger_cd" name="trigger_cd" maxlength="100" style="font-size: 12px;" readonly/>				
														</td>
														
														<th width="10%">API 호출주기</th>
					                                    <td width="15%" align="left" style="border-bottom: 0px;">
															<input type="text" id="api_pd" name="api_pd" maxlength="100" style="font-size: 12px;" />				
														</td>
														
														<th width="10%">트리거 노출주기</th>
					                                    <td width="15%" align="left" style="border-bottom: 0px;">
															<input type="text" id="trigger_pd" name="trigger_pd" maxlength="100" style="font-size: 12px;" />				
														</td>
														
														<th width="10%">트리거 노출시간</th>
					                                    <td width="15%" align="left" style="border-bottom: 0px;">
															<input type="text" id="trigger_exp" name="trigger_exp" maxlength="100" style="font-size: 12px;" />				
														</td>
					                                </tr>
												</table>
												
												<table border="0" cellpadding="0" cellspacing="0" width="100%">
						                            <tbody><tr>
						                                <td height="10"></td>
						                            </tr>
						                            <tr>
						                                <td class="3_line" height="3"></td>
						                            </tr>
						                            <tr>
						                                <td>&nbsp;</td>
						                            </tr>
						                        </tbody>
						                        </table>
						                        
												<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            <tbody>
						                            <tr>
						                                <td height="10"></td>
						                            </tr>
						                            <tr>
						                                <td height="25">
						                                    <table border="0" cellpadding="0" cellspacing="0" width="95%">
						                                        <tbody>
						                                        <tr>
						                                            <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
						                                            <td class="bold">채널 정보</td>
						                                            <td class="table_td_04" align="right">
						                                            	<span class="button small blue" id="addbtn">추가</span>
						                                            </td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        
					                            <table border="0" cellspacing="0" cellpadding="0" class="board_list" style="width:95%">
													<tr>
														<th width="19%">채널</th>
														<th width="10%">시작시간</th>
														<th width="10%">종료시간</th>
														<th width="37%">연동앱 URI</th>
														<th width="19%">LIVE여부</th>
														<th width="5%"></th>
													</tr>
												</table>
												
												 <table border="0.1" cellspacing="0" cellpadding="0" id="tbTriggerCh" class="board_list" style="border-collapse:collapse; border:1px gray solid;width:95%">
					                                <tr align="center" id="trTrigger">
										                <td width="19%" class="table_td_04" style="border-bottom: 0px;">
										                    <input type="text" name="trigger_ch" id="trigger_ch" maxlength="3" style="width: 50%;"/>
										                    <span class="button small blue" name="chbtn">선택</span>
										             	</td>
										             	<td width="10%" class="table_td_04" style="border-bottom: 0px;">
										                    <input type="text" name="trigger_start" id="trigger_start" maxlength="4" style="width: 90%;"/>
										             	</td>
										             	<td width="10%" class="table_td_04" style="border-bottom: 0px;">
										                    <input type="text" name="trigger_end" id="trigger_end" maxlength="4" style="width: 90%;"/>
										             	</td>
										             	<td width="37%" class="table_td_04" style="border-bottom: 0px;">
										                    <input type="text" name="trigger_uri" id="trigger_uri" style="width: 90%;"/>
										             	</td>
										             	<td width="19%" class="table_td_04" align="center" style="border-bottom: 0px;">
										             		<input type="radio" name="trigger_live_yn" checked="checked" value="1" /> 라이브
  															<input type="radio" name="trigger_live_yn" value="0" /> 비라이브
										             	</td>
										             	<td class="table_td_04" align="center" width="5%" style="border-bottom: 0px;"></td>
					                                </tr>
												</table>
												
					                            <input type="hidden" id="smartUXManager" name="smartUXManager" value="<%=id_decrypt %>" />
												
												<table border="0" cellpadding="0" cellspacing="0" style="width:95%" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="40" align="right">
						                                	<span class="button small blue" id="regbtn">저장</span>
						                                	<span class="button small blue" id="applybtn">즉시적용</span>
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
	    <td colspan="2" background="/smartux_adm/images/admin/copy_bg.gif" height="60" align="left">
	        <!-- 하단 로그인 사용자 정보 시작 -->
	        <%@include file="/WEB-INF/views/include/bottom.jsp" %>
	        <!-- 하단 로그인 사용자 정보 종료 -->
	    </td>
	 </tr>
</tbody>
</table>
</div>
</body>
</html>