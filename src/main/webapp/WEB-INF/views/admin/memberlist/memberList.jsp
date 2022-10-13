<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>가입자 그룹 관리</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
$(document).ready(function(){
	$("#applybtn").click(function(){
		var msg = "가입자 그룹을 상용에 적용하시겠습니까?";
		if(confirm(msg)){
			$.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>로딩중..</b>"
			});
			$.ajax({
			    url: './memberListApplyCache.do',
			    type: 'POST',
			    dataType: 'json',
			    data: {   
			    	callByScheduler:'A'
			    },
			    error: function(){
			    	alert("작업 중 오류가 발생하였습니다");
			 		$.unblockUI();
			    },
			    success: function(rs){
			    	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
				 	var flag = rs.flag;
				 	var message = rs.message;
				 	if(flag == "0000"){// 정상적으로 처리된 경우
				 		alert("정상처리 되었습니다.");
				 		$.unblockUI();
				 	}else{
				 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
				 		$.unblockUI();
				 	}
			    },
			});
		}
	});
	
	// 삭제 버튼 클릭
	$("#deleteBtn").click(function(){
		var fileId_comma="";
		if(confirm("등록된 가입자 그룹을 삭제 하시겠습니까?")){	
		    $.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>처리 중..</b>"
			});
		    
		    $.ajax({
				url: './memberListDelete.do',
				type:'POST',
				dataType: 'json',
				timeout : 30000,
				success:function(data){
					if(data.result.flag=="0000"){
						alert("정상적으로 처리 되었습니다.");
					}else{
						alert(data.result.message);
					}
			 		$.unblockUI();
				},
				error:function(){
					alert("정상적으로 처리되지 않았습니다.");
			 		$.unblockUI();
				},
    			complete:function(){
					$(location).attr('href',"./memberList.do");
    			}
			});
		}
	});
	
    $('#templateDownBtn').click(function () {
        location.href = './downloadTemplate.do';
    });
	
    $('#memberListDownBtn').click(function () {
	    $.ajax({
			url: './checkFile.do',
			dataType: 'json',
			timeout : 30000,
			success:function(data){
				if(data.result.flag=="0000"){
			        location.href = './downloadMemberList.do';
				}else if(data.result.flag=="2"){
					alert("등록된 파일이 없습니다.");
				}else{
					alert("파일 확인이 실패하였습니다.");
				}
		 		$.unblockUI();
			},
			error:function(){
				alert("파일 확인이 실패하였습니다.");
		 		$.unblockUI();
			},
		});
    });
    
    $("#uploadFile").change(function(){
		var IMG_FORMAT = "\.(txt)$";
		if(!(new RegExp(IMG_FORMAT, "i")).test(this.value)){
	    	//ie일경우
	    	alert("txt파일만 업로드해주세요.");
	    	$("#uploadFile").attr("value","");	    			    	
	    	//크롬일경우
	    	$("#uploadFile")[0].select();		    	
	    	document.selection.clear();
		}
    });
    
    $("#uploadBtn").click(function(){
    	if($("#uploadFile").val()==""){
    		alert("txt파일을 업로드 해주세요.");
    		return false;
    	}
    	if(confirm("해당 파일의 리스트를 업로드하시겠습니까?")){
    		$.blockUI({
    			blockMsgClass: "ajax-loading",
    			showOverlay: true,
    			overlayCSS: { backgroundColor: '#CECDAD' } ,
    			css: { border: 'none' } ,
    			 message: "<b>처리 중..</b>"
    		});

    		var file = $("#uploadFile")[0].files[0];
    		var formData = new FormData();
    		formData.append('uploadFile',file);

    	    $.ajax({
    			type:'POST',
    			async: true,
    			url: './configurationMemberListUpload.do',
    			processData: false,
    			contentType: false,
    			cache: false,
    			data: formData,
    			dataType: 'json',
    			timeout : 30000,
    			success:function(data){
    				if (data.result.flag == 1) {
	    				alert("처리가 완료되었습니다.");
    				} else {
        				alert("처리가 실패했습니다. " + data.result.message);
    				}
    			},
    			error:function(){
    				alert("문제가 생겼습니다. 다시한번 시도해 주세요.");
    			},
    			complete:function(){
					$(location).attr('href',"./memberList.do");
    			}
    		});
    	}
    });
});
</script>
</head>

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
                                	가입자 그룹 관리
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
                    				<table border="0" cellpadding="0" cellspacing="0" width="400px" >
			                            <tbody>
						                <tr>
						                    <td class="3_line" height="1"></td>
						                </tr>
						                <tr>
						                    <td height="20"></td>
						                </tr>
						                <!-- 리스트 시작 -->
						                <tr>
						                    <td>
						                       	<form id="ex_form" method="post" action="./configurationMemberListUpload.do">
					                            <table border="0" cellpadding="0" cellspacing="0" class="board_list">
						                            <tbody>
						                            <tr>
						                            	<th colspan="2">가입자 그룹 등록</th>
						                            </tr>
						                            <tr>
						                                <td align="left">
						                                	<input type="file" name="uploadFile" id="uploadFile">
						                                </td>
						                                <td align="right">
						                                	<span class="button small blue" id="uploadBtn">업로드</span>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table><br/>
						                       	</form>
						                        <!-- 등록/수정 버튼 -->
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            <tbody>
						                            <tr>
						                                <td align="left">&nbsp;
						                                	<span class="button small blue" id="deleteBtn">삭제</span>
						                                	<span class="button small red" id="templateDownBtn">템플릿 다운</span>
						                                </td>
						                                <td align="right">
						                                	<span class="button small blue" id="memberListDownBtn">가입자 목록 다운로드</span>
						                                	<span class="button small blue" id="applybtn">즉시적용</span>&nbsp;&nbsp;
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
						                                <td>현재 업로드된 파일 : ${fileName}</td>
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