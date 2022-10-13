<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<title>LG U+ IPTV</title>


<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<link href="/smartux_adm/css/anytime_style.css" rel="stylesheet" type="text/css">
<script src="/smartux_adm/js/anytime.js"></script>

<script type="text/javascript">
	
$(document).ready(function(){
	var result = '${result}';
	if(result == "0000"){
		alert("등록 되었습니다.");
	}
	
	$("#sumitBtn").click(function () {	
		if($("#site_id").val() == ''){
			alert('사이트 아이디를 입력해주세요.');
			$("#site_id").focus();
			return;	
		}
		$.ajax({
		    url: './siteIdChk.do',
		    type: 'POST',
		    dataType: 'json',
		    data: {
		        "site_id": $('#site_id').val()
		    },
		    success: function(data){
		    	$.unblockUI();
		    	var flag = data.result.flag;
		    	var chkCnt = data.result.resultCount;
				if(flag == "0000"){
			    	if(chkCnt == "0"){
			    		if($("#site_name").val() == ''){
			    			alert('사이트명을 입력해주세요.');
			    			$("#site_name").focus();
			    			return;
			    		}	
			    	    if($("#site_url").val() == ''){
			    			alert('사이트 URL을 입력해주세요.');
			    			$("#site_url").focus();
			    			return;
			    	  	}
			    	    $("form[name=form1]").submit();
			    	}else{
			    		alert("중복된 아이디입니다.");
			    		return false;
			    	}
				}else{
			    	$.unblockUI();
					alert(data.result.message);
				}
		    },
		    error: function(){
		    	$.unblockUI();
		    	alert("에러가 발생하였습니다.");
		    }
		});
	});
	
	
	$("#file").change(function(){
		if($("#file").val()==""){
			return;
		}
		if(imageFileCheckInput($("#file").val(),"file")){
			alert("이미지 파일이 아닙니다.");			
		}					    	
	});
	
	$("#file_tv").change(function(){
		if($("#file_tv").val()==""){
			return;
		}
		if(imageFileCheckInput($("#file_tv").val(),"file_tv")){
			alert("이미지 파일이 아닙니다.");			
		}					    	
	});
});

function checkKeyIn(obj, id){
	var newId = obj.val();
	
	if(newId != id){
		checkByte(obj,'4');
	}else{
		checkByte(obj,'5');
	}
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
			<td background="smartux_adm/images/admin/bg_02.gif" width="35">&nbsp;</td>
            <td valign="top">
            <table border="0" cellpadding="0" cellspacing="0" width="98%">
                <tbody>
                <tr>
                    <td height="42" width="100%">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tbody>
                            <tr>
                                <td width="300" class="boldTitle">
                                	<!-- 카테고리 타이틀 -->
                                    	화제동영상 관리	
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
						                                            <td class="bold">사이트 등록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="form1" name="form1" action="./siteInsert.do" method="post" enctype="multipart/form-data">
						                        <input type="hidden" name="findName" value="${vo.findName}" />
						                        <input type="hidden" name="findValue" value="${vo.findValue}" />
						                        <input type="hidden" name="pageNum" value="${vo.pageNum}" />
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" class="board_data">
					                                <tbody>
					                                	<!-- 사이트 아이디 -->
						                                <tr align="center"  >
						                                    <th width="25%">사이트ID</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<input type="text" name="site_id" id="site_id" size="35" style="font-size: 12px;" value="${vo.site_id}" onKeyUp="checkKeyIn($(this),'${vo.site_id}')"/>
															</td>
						                                </tr>
						                                <!-- 사이트명 -->
						                                <tr align="center"  >
						                                    <th width="25%">사이트명</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<input type="text" name="site_name" id="site_name" size="35" style="font-size: 12px;" value="" onKeyUp="checkByte($(this),'50')"/>
															</td>
						                                </tr>
						                                <!-- 사이트 URL -->
						                                <tr align="center" >
						                                    <th width="25%">사이트URL</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<input type="text" name="site_url" id="site_url" size="80" style="font-size: 12px;" value="" onKeyUp="checkByte($(this),'100')"/>
															</td>
						                                </tr>						                              
						                                <!-- 이미지 파일 -->					                                
			                                			<tr align="center">
						                                    <th width="25%">사이트 로고 이미지</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<input type="file" id="file_tv"  accept="image/*" name="file_tv" value="파일선택" onchange="imgHeaderCheck('file_tv', 'hotvod',${imgIptvSize});"/>
																<br>${imgIptvSize}kb / ${imgFormat}
															</td>
			                                			</tr> 
													</tbody>
					                            </table>
					                            
					                            <!-- 등록/수정 버튼 -->
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<span class="button small blue" id="sumitBtn">등록</span>
						                                	<a href="./siteList.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}"><span class="button small blue">목록</span></a>
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