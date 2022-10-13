<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>

<script type="text/javascript">
$(document).ready(function () {
	$('input[name=mtype]').change(function () {
		$('#mims_id').val('');
	});
	
	//등록
	$('#insertBtn').click(function () {
		if($('#mims_id').val() == ''){
			//alert('패널/장르를 선택해주세요.');
			alert('패널을 선택해주세요.');
			return;
		}
		
		if(confirm("등록하시겠습니까?")) {
			$.ajax({
				url: "./insertABTest.do",
				type: "POST",
				data: { 
					'org_mims_id' : $('#mims_id').val(),
					'test_id' : '${test_id}',
					'mtype' : $(":radio[name=mtype]:checked").val(),
					'test_type' : $(":radio[name=test_type]:checked").val()
				},
				dataType : "json",
				success: function (rtn) {
					if (rtn.flag=="0000") {
						alert("등록 성공");
						$(location).attr('href', './getABTestPaperList.do?org_mims_id=' + $('#mims_id').val() + '&test_id=' + '${test_id}');
					} else {
						alert("작업 중 오류가 발생하였습니다\nflag : " + rtn.flag + "\ Message : " + rtn.message);
					}
				},
				error: function () {					
					alert("일시적인 장애가 발생하였습니다. 다시 시도해 주세요.");
				}
			});
		}
		
	});
});

//패널 또는 장르 선택 팝업
function getMimsPop(){
	var check_type = $('input[name=mtype]:checked').val();
	
	if('P'== check_type){
		//패널 팝업
		url = '/smartux_adm/admin/abtest/popPanelList.do?callback=selectPanelPopCallback'; 
	}else{
		//주요장르 팝업
	}
	
   	mims_window = window.open(url, "getMimsPop", "width=700,height=500,left=10,top=10,scrollbars=yes");
}
	
////패널 선택 팝업 callback
function selectPanelPopCallback(data){
    $("#mims_id").val(data.id);
}

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
                <tr>
                    <td height="42" width="100%">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tbody>
                            <tr>
                                <td width="300" class="boldTitle">
                                    	AB테스트 목록 관리
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
						                                            <td class="bold">AB테스트 등록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="form1" name="form1" method="post">
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" class="board_data">
					                                <tbody>	
					                                	<tr align="center">
						                                    <th width="25%">타입</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<input type="radio" name="mtype" value="P" checked="checked"/>패널
																<!-- <input type="radio" name="mtype" value="G"/>장르 -->
															</td>
						                                </tr>
						                                	
						                                <tr align="center">
						                                    <th width="25%">테스트 ID</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >${test_id}</td>
						                                </tr>
						                                <tr align="center">
						                                    <th width="25%">패널/장르</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<span class="button small blue" id="searchBtn" onClick="getMimsPop()">선택</span>		
																<input type="text" id="mims_id" name="mims_id" value="" readonly="readonly">
															</td>
						                                </tr>
						                                <tr align="center">
						                                    <th width="25%">AB TEST 타입</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<input type="radio" name="test_type" value="O" checked="checked"/>순서변경
																<input type="radio" name="test_type" value="M" />메타편성
																<input type="radio" name="test_type" value="T" />패널변경타입
																<input type="radio" name="test_type" value="D" />상이한 지면 타입
																<!-- <input type="radio" name="mtype" value="G"/>장르 -->
															</td>
						                                </tr>
					                                </tbody>
				                                </table>
				                                
					                            <!-- 등록 버튼 -->
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<span class="button small blue" id="insertBtn" name="insertBtn">확인</span>
						                                	<a href="./getABTestList.do?findName=${searchVo.findName}&findValue=${searchVo.findValue}&pageNum=${searchVo.pageNum}"><span class="button small blue">취소</span></a> 
						                                </td>
						                            </tr>
						                       		</tbody>
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
	        <%@include file="/WEB-INF/views/include/bottom.jsp" %>
	        <!-- 하단 로그인 사용자 정보 종료 -->
	    </td>
	 </tr>
</tbody>
</table>
</div>
</body>
</html>
