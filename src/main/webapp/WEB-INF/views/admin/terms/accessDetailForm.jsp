<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>약관 항목 상세</title>
    <link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
    <jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
    
<script type="text/javascript">
$(document).ready(function(){
	
	$('#sumitBtn').click(function(){
				
		if ('' == $('#text_file').val()) {
		}else{
			if (htmlCheckInputPatch($('#text_file').val(), 'text_file')) {
  				alert('파일등록은 html,htm,txt 파일만 입력 가능합니다.');
  				return;
			}
		}
		
		if ('' == $('#access_url').val()) {
			
		} else {
			if(!checkUrlForm($('#access_url').val())) {
				alert('URL 형식을 확인해주십시요.');
				return;
			}
		}
		
		if(confirm('저장하시겠습니까?')){
			
			var url = '';
			
			<c:if test="${isUpdate eq '0'}">
			url = "/smartux_adm/admin/terms/insertProcDetail.do";
			</c:if>
			<c:if test="${isUpdate eq '1'}">
			url = "/smartux_adm/admin/terms/updateProcDetail.do";
			</c:if>
			
			var option = {			
				url : url,
				type:'POST',
				dataType: 'json',
				timeout : 30000,
				contentType: false,
				cache: false,
				processData:false,
				success:function(data){
					if(data.flag=="0000"){				
						alert("정상적으로 완료 되었습니다.");
						$(location).attr('href',"./getAccessDetailListPop.do?access_info_id=${accessDetail.access_info_id }&pageNum=${vo.pageNum}");
					}else if(data.flag=="1112"){	
						alert(data.message);
					}else{
						alert("작업 중 오류가 발생하였습니다\nflag : " + data.flag + "\nmessage : " + data.message);
					}
				},
				error:function(){
					alert("정상적으로 완료되지 않았습니다.\n재시도 하시기 바랍니다.");
				}
			};
			$("#form1").ajaxSubmit(option);					
		}
	});
	
	$('#delBtn').click(function(){
		var checkboxarray = new Array();
		var tmp;
		checkboxarray.push("${accessDetail.access_detail_id}");
		
		if(confirm("약관 항목 상세를 삭제 하시겠습니까?")){
			
			$.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>로딩중..</b>"
			});	
			
			$.get("./deleteProcDetail.do", 
				 {access_detail_ids : checkboxarray},
				  function(data) {
					 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
					 	var flag = data.res;
					 	var message = data.msg;
					 	
					 	if(flag == "0000"){						// 정상적으로 처리된 경우
					 		alert("해당 약관 항목 상세가 삭제 처리되었습니다");
					 		$.unblockUI();
					 		$(location).attr('href',"./getAccessDetailListPop.do?access_info_id=${accessDetail.access_info_id }&pageNum=${vo.pageNum}");
					 	}else{
					 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
					 		location.reload();
					 	}
				  },
				  "json"
		    );
		}
	});
});

function downloadFile(filePath) {
	location.href = filePath;
}

function deleteFile(){
	if(confirm("파일을 삭제하시겠습니까?")){
		$("#org_file_nm").val("");
		$("#save_file_nm").val("");
		$("#html_file").remove();
	}
}

function checkUrlForm(strUrl) {
    var expUrl = /^http[s]?\:\/\//i;
    return expUrl.test(strUrl);
}

function htmlCheckInputPatch(filename, inputname){
	
   var fileName=filename;	 
   var fileSuffix =fileName.substring(fileName.lastIndexOf(".") + 1);
   var browser = navigator.userAgent.toLowerCase();
   
   fileSuffix = fileSuffix.toLowerCase();
    if (!( "html" == fileSuffix || "htm" == fileSuffix || "txt" == fileSuffix) ){
    	//크롬일경우
    	if(-1 != browser.indexOf("chrome")){
    		$("#"+inputname)[0].select();
    		$("#"+inputname).val("");
    	}else{ //ie : if(-1 != browser.indexOf("msie")
    		$("#"+inputname).attr("value","");	    
    		document.selection.clear();
    	}
    	return true;
    }
    return false;
}

</script>
	
<body leftmargin="0" topmargin="0">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
		<tbody>
		    <tr>
				<td height="25">
					<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tbody>
					<tr>
						<td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
						<td class="bold">약관 항목 : ( ${accessDetail.access_info_id} ) ${access_title} <c:if test="${isUpdate eq '0'}">등록</c:if><c:if test="${isUpdate eq '1'}">수정</c:if></td>
					</tr>
					</tbody>
		            </table>
		        </td>
		    </tr>
		</tbody>
	</table>
	
	<form id="form1" name="form1" method="post" enctype="multipart/form-data">
	<table border="0" cellpadding="0" cellspacing="0" width="700" class="board_data">
		<tbody>
			<tr align="center">
				<th width="25%">약관 항목 ID</th>
				<td width="5%"></td>								                                    
				<td width="70%" align="left">${accessDetail.access_info_id}<input type="hidden" name="access_info_id" id="access_info_id" value="${accessDetail.access_info_id}"></td>
			</tr>
			<c:if test="${isUpdate eq '1'}">
			<tr align="center">
				<th width="25%">약관 항목 번호</th>
				<td width="5%"></td>								                                    
				<td width="70%" align="left">${accessDetail.access_detail_id}<input type="hidden" name="access_detail_id" id="access_detail_id" value="${accessDetail.access_detail_id}"></td>									
			</tr>
			</c:if>
			<tr align="center">
			    <th width="25%">약관 URL</th>
			    <td width="5%"></td>
			    <td width="70%" align="left" style="word-break:break-all;">
			    	<input type="text" id="access_url" name="access_url" value="${accessDetail.access_url}" maxlength="512" onKeyUp="checkByte($(this),'512')">
				</td>
			</tr>
			<tr align="center">
				<th width="25%">파일등록</th>
				<td width="5%"></td>
				<td width="70%" align="left">
					<input type="file" id="text_file" name="text_file" value="파일선택"/>
					<c:if test="${not empty accessDetail.org_file_nm}">
						<div id="html_file">
						<a href="javascript:downloadFile('${filePath}')">
							${accessDetail.org_file_nm}
						</a>
						<span class="button small rosy"><a href="javascript:deleteFile('${filePath}')">삭제</a></span>
						</div>
					</c:if>
					<br/>
						<font color="red">
							* 등록가능파일 : html, htm, txt<br/>
							* 인코딩을 utf-8로 저장하여 등록하십시요.
						</font>
					<input type="hidden" id="org_file_nm" name="org_file_nm" value="${accessDetail.org_file_nm}"/>
					<input type="hidden" id="save_file_nm" name="save_file_nm" value="${accessDetail.save_file_nm}"/>
				</td>
			</tr>
			<c:if test="${isUpdate eq '1'}">
			<tr align="center">
				<th width="25%">버전</th>
				<td width="5%"></td>
				<td width="70%" align="left" >
					<input type="text" id="access_version" name="access_version" value="${accessDetail.access_version}" disabled>
				</td>
			</tr>
			<tr align="center">
				<th width="25%">노출여부</th>
				<td width="5%"></td>
				<td width="70%" align="left" >
					<select class="select" id="display_yn" name="display_yn">
						<c:if test="${access_version ne accessDetail.access_version}">
							<option value="Y" <c:if test="${accessDetail.display_yn eq 'Y'}">selected="selected"</c:if>>Y</option>
							<option value="N" <c:if test="${accessDetail.display_yn eq 'N'}">selected="selected"</c:if>>N</option>
						</c:if>
						<c:if test="${access_version eq accessDetail.access_version}">
							<option value="Y" selected>Y</option>
						</c:if>
					</select>
				</td>
			</tr>
			<tr align="center">
				<th width="25%">등록일</th>
				<td width="5%"></td>
				<td width="70%" align="left" >
					<c:out value="${accessDetail.reg_dt}"/>
				</td>
			</tr>
			<tr align="center">
				<th width="25%">수정일</th>
				<td width="5%"></td>
				<td width="70%" align="left" >
					<c:out value="${accessDetail.mod_dt}"/>
				</td>
			</tr>
			</c:if>
		</tbody>
	</table>
	</form>
	<!-- 수정/삭제 버튼 -->
	<table border="0" cellpadding="0" cellspacing="0" width="700" align="left">
		<tbody>
			<tr>
			<td height="25" align="right">
				<c:if test="${isUpdate eq '0'}">
					<span class="button small blue" id="sumitBtn">등록</span>
				</c:if>
				<c:if test="${isUpdate eq '1'}">
					<span class="button small blue" id="sumitBtn">수정</span>
					<c:if test="${access_version ne accessDetail.access_version}">
					<span class="button small blue" id="delBtn">삭제</span>
					</c:if>
				</c:if>
				<a href="./getAccessDetailListPop.do?access_info_id=${accessDetail.access_info_id }&pageNum=${vo.pageNum}"><span class="button small blue">목록</span></a>
			</td>
			</tr>
		</tbody>
	</table>
</body>