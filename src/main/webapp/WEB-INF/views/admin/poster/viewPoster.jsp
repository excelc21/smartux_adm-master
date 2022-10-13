<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
var category_window;
$(document).ready(function(){

	init();
	
	$("#searchAlbumBtn").click(function(){
		
		var closeCheck = setInterval(function(){
			if($("#choiceCts").val() != ''){
				
				var ContentArray = $("#choiceCts").val().split('||');
				
				// console.log(repsContentArray);
				// 선택
				// 0 : 카테고리ID
				// 1 : 앨범ID
				// 2 : 시리즈YN
				// 3 : 시리즈no
				// 검색
				// 0 : 카테고리ID
				// 1 : 앨범ID
				// 2 : 카테고리구분(I20/I30)
				// 3 : 시리즈YN
				// 4 : 시리즈no
				
				if(ContentArray.length == 4) {
					$('#album_id').val(ContentArray[1]);
				}else if(ContentArray.length == 5) { 
					$('#album_id').val(ContentArray[1]);
				}else {
					alert("앨범 정보를 가져오지 못했습니다.\n해당 앨범이 현재 편성 불가능하거나 네트워크 문제일 수 있습니다.");
				}
				$("#choiceCts").val('');
				clearInterval(closeCheck);
			}
		}, 1000);
		
		var category_gb = $("#category_gb").val();
		var url = "<%=webRoot%>/admin/commonMng/getOnceCategoryAlbum.do?categoryId=VC&hiddenName=choiceCts&textName=album_name&isAds=Y&type="+category_gb;
        category_window = window.open(url,'getCategoryAlbumList','width=800,height=600,scrollbars=yes');
	});
	
	//패널이미지 적용 버튼 클릭
	$('#imgInsertBtn').click(function(){
		
		var album_id = $('#album_id').val();
		
		if(album_id == ''){
			alert('컨텐츠를 선택해 주세요');
			return;
		}
		
		if (confirm('이미지를 적용 하시겠습니까?')) {
			$('#UploadForm').ajaxSubmit({ 
				contentType : 'application/json',
				dataType:'json',
				success : function(data, statusText, xhr) {
					var msg='';
					if(data.code == '0000') {
						alert(data.msg);
						parent.opener.location.reload();
						self.close();
					}else {
						alert(data.msg);
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
	
	$('#closeBtn').bind('click', function(){
		self.close();
	});
	
});

function init(){
	
	var album_id = '${result.album_id}'
	if(album_id != ''){
		$("#service_type").attr('style','background-color:#f2eded').attr('onFocus','this.initialSelect = this.selectedIndex;').attr('onChange','this.selectedIndex = this.initialSelect;');
		$("#category_gb").hide();
		$("#searchAlbumBtn").hide();
		$("#UploadForm").attr('action','./updatePoster.do');
	}
	
	$('#h_file').val('');
	$('#h_img').attr('src','').removeAttr('width').removeAttr('height');
	$('#h_file_name').val('');
	$('#w_file').val('');
	$('#w_img').attr('src','').removeAttr('width').removeAttr('height');
	$('#w_file_name').val('');
	
	var w_fileName = '${result.width_img}';
	var h_fileName = '${result.height_img}';
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
}

</script>
<body leftmargin="0" topmargin="0">
	<div id="divBody" style="height: 100%">
		<table border="0" cellpadding="0" cellspacing="0" height="100%"
			width="100%">
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
														<table border="0" cellpadding="15" cellspacing="0"
															width="100%" align="center">
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
																													<td class="bold">전용 포스터 등록</td>
																												</tr>
																											</tbody>
																										</table>
																									</td>
																								</tr>
																							</tbody>
																						</table>
																						<form id="UploadForm" name="UploadForm" method="post" action="./insertPoster.do" enctype="multipart/form-data">
																							<input type="hidden" id="album_id" name="album_id" value="${result.album_id}"> 
																							<input type="hidden" id="imgDir" name="imgDir" value="${imgDir}">
																							<table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_data">
																								<tbody>
																									<tr>
																										<th width="25%">서비스 타입</th>
																										<td width="5%"></td>
																										<td align="left">
																											<select id="service_type" name="service_type">
																												<option value="I" <c:if test="${result.service_type eq 'I'}">selected="selected"</c:if>>IPTV</option>
																												<option value="S" <c:if test="${result.service_type eq 'S'}">selected="selected"</c:if>>시니어</option>
																											</select>
																										</td>
																									</tr>
																									<tr>
																										<th width="25%">앨범</th>
																										<td width="5%"></td>
																										<td width="60%" align="left">
																				F							<select id="category_gb" name="category_gb">
																												<option value="I20" <c:if test="${category_gb eq 'I20'}">selected="selected"</c:if>>I20</option>
																												<option value="I30" <c:if test="${category_gb eq 'I30'}">selected="selected"</c:if>>I30</option>
																											</select>
																											<input type="hidden" name="choiceCts" id="choiceCts" />
																											<input	type="text" name="album_name" id="album_name" size="20" style="font-size: 12px;" readonly="readonly" value="${result.album_name}"/> 
																											<span class="button small blue"	id="searchAlbumBtn">검색</span>
																										</td>
																									</tr>
																									<tr align="center" valign="top">
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
																														<td align="center"><input type="hidden" id="w_file_name" name="w_file_name" value=""> 
																															<img src="" id="w_img" name="w_img">
																														</td>
																														<td align="center"><input type="hidden" id="h_file_name" name="h_file_name" value=""> 
																															<img src="" id="h_img" name="h_img">
																														</td>
																													</tr>
																													<tr>
																														<td align="center"><input id="w_file" name="w_file" type="file" value="" />
																															<button id="w_delete_Btn" name="w_delete_Btn" type="button">삭제</button>
																														</td>
																														<td align="center"><input id="h_file" name="h_file" type="file" value="" />
																															<button id="h_delete_Btn" name="h_delete_Btn" type="button">삭제</button>
																														</td>
																													</tr>
																												</tbody>
																											</table>
																										</td>
																									</tr>
																								</tbody>
																							</table>
																							<table border="0" cellpadding="0" cellspacing="0"
																								width="100%" align="left">
																								<tbody>
																									<tr>
																										<td height="25" align="center">
																											<span class="button small blue" id="imgInsertBtn">적용</span>
																											<span class="button small blue" id="closeBtn">닫기</span>
																										</td>
																									</tr>
																								</tbody>
																							</table>
																							<input type="hidden" id="smartUXManager" name="smartUXManager" value="<%=id_decrypt %>" />
																						</form>

																						<table border="0" cellpadding="0" cellspacing="0"
																							width="100%">
																							<tbody>
																								<tr>
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
														</table> <!-- ########################### body end ########################## -->
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