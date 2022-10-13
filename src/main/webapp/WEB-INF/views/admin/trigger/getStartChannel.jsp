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
	
	var item_code = "${item_code}";
	var item_nm = "${item_nm}";
	
	$("#startchannel_cd").val(item_code);

	if (item_nm != null) {
		$("input[name=startchannel_yn][value=" + item_nm.split("|")[0] + "]").attr("checked", "checked")
		$("input[name=startchannel_sec]").val(item_nm.split("|")[1])
	}

	//즉시 적용
	$("#applybtn").click(function(){
		var code = "A0009";
		var msg = "시작채널 정보를 적용 하시겠습니까?";
		
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
					 		alert("시작채널 정보가 적용되었습니다");
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

		var startchannel_sec = $("#startchannel_sec").val();
		var startchannel_yn = $("input[name=startchannel_yn]:checked").val();
		
		var regexp_num = /^[0-9]*$/;
		var regexp_blank = /^[\s]*$/;
		
		if(!regexp_num.test(startchannel_sec) || regexp_blank.test(startchannel_sec) || startchannel_sec < 10 || startchannel_sec > 240){
			alert("종료 후 시간은 10 ~ 240사이의 숫자만 입력 가능합니다.");
			$("#startchannel_sec").focus();
			return;
		}
		
		var item_nm = startchannel_yn + "|" + startchannel_sec;
		
		var itemCode = $("#startchannel_cd").val();
		var itemNm = item_nm;
		var smartUXManager = $("#smartUXManager").val();
		$.post("<%=webRoot%>/admin/trigger/updateStartChannel.do", 
			 {itemCode : itemCode, itemNm : itemNm, smartUXManager : smartUXManager},
			  function(data) {
				 	var flag = data.flag;
				 	var message = data.message;
				 	
				 	if(flag == "0000"){
				 		location.reload();
						alert("시작채널 정보가 등록되었습니다");
				 	}else{
				 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
				 	}
			  },
			  "json"
	    );
	});
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
                                    시작채널 설정
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
						                                            <td class="bold">시작채널 설정</td>
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
					                                    <th width="10%">시작채널 코드</th>
					                                    <td width="15%" align="left" style="border-bottom: 0px;">
															<input type="text" id="startchannel_cd" name="startchannel_cd" maxlength="100" style="font-size: 12px;" readonly/>				
														</td>
														
														<th width="10%">초기화여부 Y/N</th>
					                                    <td width="15%" align="left" style="border-bottom: 0px;">
										             		<input type="radio" name="startchannel_yn" checked="checked" value="Y" /> Y
  															<input type="radio" name="startchannel_yn" value="N" /> N				
														</td>
														
														<th width="10%">종료 후 시간(분)</th>
					                                    <td width="15%" align="left" style="border-bottom: 0px;">
															<input type="text" id="startchannel_sec" name="startchannel_sec" maxlength="3" style="font-size: 12px;" />				
														</td>
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