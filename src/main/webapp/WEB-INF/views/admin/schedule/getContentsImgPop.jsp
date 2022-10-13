<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
var category_window;
$(document).ready(function(){
	
	//패널이미지 적용 버튼 클릭
	$('#imgInsertBtn').click(function(){
		
		var selectAlbumIdx = $("#contentsList option").index($("#contentsList option:selected"));
		
		if(selectAlbumIdx ==-1){
			alert('컨텐츠를 선택해 주세요');
			return;
		}
		
		var selectAlbum = $("#contentsList option").eq(selectAlbumIdx).val();
		if(selectAlbum.indexOf("^") > -1){
			$('#album_id').val(selectAlbum.split("^")[0]);
		}
		
		if (confirm('이미지를 적용 하시겠습니까?')) {
			$('#UploadForm').ajaxSubmit({ 
				contentType : 'application/json',
				dataType:'json',
				success : function(data, statusText, xhr) {
					var msg='';
					if(data.code == '0000') {
						alert(data.msg);
						location.reload();
					}
				}, 
				error : function() {
					alert('파일 업로드에 실패했습니다.')
				},
				beforeSend : function(data) {
					$.blockUI({
						blockMsgClass: "ajax-loading",
						showOverlay: true,
						overlayCSS: { backgroundColor: '#CECDAD' } ,
						css: { border: 'none' } ,
						 message: "<b>이미지 업로드중..</b>"
					});
				}, 
				complete : function() {
					$.unblockUI();
				}
			});
		}
		
	});
	
	//가로이미지 삭제
	$('#w_delete_Btn').click(function(){
		if($('#w_file_name').val() ==''){
			alert('삭제할 이미지가 없습니다.');
			return;
		}
		
		if(confirm('가로형 이미지를 삭제하시겠습니까?')){
			$('#w_file_name').val('delete');
			$('#w_file').val('');
			$('#w_img').attr('src','').removeAttr('width').removeAttr('height');
		}
	});
	
	//세로이미지 삭제
	$('#h_delete_Btn').click(function(){
		if($('#h_file_name').val() ==''){
			alert('삭제할 이미지가 없습니다.');
			return;
		}
		
		if(confirm('세로형이미지를 삭제하시겠습니까?')){
			$('#h_file_name').val('delete');
			$('#h_file').val('');
			$('#h_img').attr('src','').removeAttr('width').removeAttr('height');
		}
	});
	
	//선택된 컨텐츠 클릭 이벤트
	$('#contentsList').bind('click', function(){
		$('#h_file').val('');
		$('#h_img').attr('src','').removeAttr('width').removeAttr('height');
		$('#h_file_name').val('');
		$('#w_file').val('');
		$('#w_img').attr('src','').removeAttr('width').removeAttr('height');
		$('#w_file_name').val('');
		
		//이미지 set
		var selectAlbumIdx = $("#contentsList option").index($("#contentsList option:selected"));
		
		var selectAlbum = $("#contentsList option").eq(selectAlbumIdx).val();
		var albumAy = selectAlbum.split('^');
		var w_fileName = albumAy[1];
		var h_fileName = albumAy[2];
		$('#album_id').val(albumAy[0]);
		var imgDir = $('#imgDir').val();
		
		//가로이미지
		if(w_fileName != ''){
			$('#w_img').attr('src', imgDir+w_fileName).attr('width','300').attr('height','200');
			$('#w_file_name').val(w_fileName);
		}
		
		//세로이미지
		if(h_fileName != ''){
			$('#h_img').attr('src', imgDir+h_fileName).attr('width','200').attr('height','300');
			$('#h_file_name').val(h_fileName);
		}
		
	});
	
	
	//닫기 2016.12.09
	$('#closeBtn').bind('click', function(){
		self.close();
	});
	
});

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
															                                            	<td width="15"><img src="/smartux/images/admin/blt_07.gif"></td>
															                                            	<td class="bold">컨텐츠 목록 조회</td>
															                                        	</tr>
															                                    		</tbody>
															                                    	</table>
															                                	</td>
															                            	</tr>
															                       		</tbody>
															                       	</table>
															                       	<form id="UploadForm" name="UploadForm" method="post" action="./insertImg.do" enctype="multipart/form-data">
														                            <input type="hidden" id="album_id" name="album_id" value="">
														                            <input type="hidden" id="imgDir" name="imgDir" value="${imgDir}">
														                            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_data">
															                            <colgroup>
															                            	<col width="20%">
															                            	<col width="30%">
															                            	<col width="20%">
															                            	<col width="30%">
															                            </colgroup>
														                                <tbody>
															                                <tr align="center">
															                                    <th>선택한 카테고리ID</th>
															                                    <td>${category_id}</td>
															                                    <th>선택한 앨범ID</th>
															                                    <td>${album_id}</td>
															                                </tr>
															                                <tr>
															                                	<th>컨텐츠 목록</th>
															                                	<th colspan="3">컨텐츠 이미지</th>
															                                </tr>
														                                	<tr align="center"  valign="top">
														                                   		<td id="uiarea" align="center">
															                                   		<table border="0" cellspacing="0" cellpadding="0" width="100%">
															                                   			<tr align="center">
																                                   			<td width="35%">
																                                   				<select id="contentsList" name="contentsList" size="20" style="width:200px;">
																		                                   					<c:forEach items="${resultList}" var="item">
																		                                   						<option value="${item.album_id}^${item.width_img}^${item.height_img}">${item.album_name}</option>
																		                                   					</c:forEach>
																												</select>
																                                   			</td>
															                                   			</tr>
															                                   		</table>
														                                   		</td>
																								<td colspan="3">
																									<table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_data" height="100%">
																			                    		<colgroup>
																			                    			<col width="50%">
																			                    			<col width="50%">
																			                    		</colgroup>
																                    					<tbody>
																			                    			<tr>
																			                    				<th align="center">가로이미지</th>
																			                    				<th align="center">세로이미지</th>
																			                    			</tr>
																			                    			<tr height="300">
																			                    				<td align="center">
																			                    					<input type="hidden" id="w_file_name" name="w_file_name" value="">
																			                    					<img src="" id="w_img" name="w_img">
																			                    				</td>
																			                    				<td align="center">
																			                    					<input type="hidden" id="h_file_name" name="h_file_name" value="">
																			                    					<img src="" id="h_img" name="h_img">
																			                    				</td>
																			                    			</tr>
																			                    			<tr>
																			                    				<td align="center">
																			                    					<input id="w_file" name="w_file"  type="file"  value=""/>
																			                    					<button id="w_delete_Btn" name="w_delete_Btn" type="button">삭제</button>
																			                    				</td>
																			                    				<td align="center">
																			                    					<input id="h_file" name="h_file"  type="file"  value=""/>
																			                    					<button id="h_delete_Btn" name="h_delete_Btn" type="button">삭제</button>
																			                    				</td>
																			                    			</tr>
																                    					</tbody>
																                    				</table>
																                    			</td>
																                    		</tr>
														                            	</tbody>
														                            </table>
														                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="left">
															                            <tbody>
															                            <tr>
															                                <td height="25" align="center">
															                                	<span class="button small blue" id="imgInsertBtn">적용</span>
															                                	<span class="button small blue" id="closeBtn">닫기</span>
															                                </td>
															                            </tr>
															                       		</tbody>
															                       	</table>
															                       	<input type="hidden" id="schedule_code" name="schedule_code" value="${main.schedule_code}" />
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