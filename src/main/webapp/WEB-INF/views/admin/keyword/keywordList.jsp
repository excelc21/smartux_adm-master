<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>LG U+ IPTV SmartUX</title>
<link href="${pageContext.request.contextPath}/css/basic_style.css" rel="stylesheet" type="text/css">
<style>
  .div-ImageView {
    position: absolute;
    display: block;
    width: 100px;
    height: 100px;
    text-align: center;
  }
</style>
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<body leftmargin="0" topmargin="0">
<div id="divBody" style="height:100%">
<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
    <tbody>
      <tr>
        <td colspan="2" height="45" valign="bottom">
       		 <!-- top menu start -->
			<!-- jsp:include page="/WEB-INF/views/include/top.jsp"--><!-- /jsp:include -->
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
      		<!-- 이미 전체 페이지에서 treeview를 커스톰해서 쓰고있어서 따로 추가해서 씀 -->
			<link href="${pageContext.request.contextPath}/css/jquery.custom.treeview.css" rel="stylesheet" type="text/css">
			<script src="${pageContext.request.contextPath}/js/underscore-min.js"></script>
			<script src="${pageContext.request.contextPath}/js/jquery.custom.treeview.js"></script>
			<script src="${pageContext.request.contextPath}/js/jquery.custom.treeview.async.js"></script>
			<script type="text/javascript">
				//언더스코어 템플릿 사용을 위한 셋팅
				_.templateSettings = {
					    evaluate:    /\{\{(.+?)\}\}/g,
					    interpolate: /\{\{=(.+?)\}\}/g,
					    escape:      /\{\{-(.+?)\}\}/g
					};

				$(function(){
					//#######트리뷰#######
		            $("#root").custom_treeview({
						url: "${pageContext.request.contextPath}/admin/keyword/searchKeywordProc.do",
						root: "root",
						ajax: {
							type: "post",
							data: {
								view_type: "A"
							},
						},collapsed: true
					});
					//#######트리뷰#######
					
					//트리뷰 네임 클릭 시
		            $(".treeVw").on("click", "a", function() {
		            	$(".treeVw A").each(function(){
		            		$(this).css("background-color","white");
			            	$(this).css("color","black");
		            	});
		            	$(this).css("background-color","blue");
		            	$(this).css("color","white");
		            	$("#currentKeyword").val($(this).attr("data-id"));
		            	$("#currentKeywordName").val($(this).find("#tree_name").text());
		            	$("#currentKeywordType").val($(this).attr("data-type"));
		            	
		            	if($(this).attr("data-id")=="root"){
		            		$("#prod_img_area").html("");
		            	} else {
		            		$.ajax({
		            			cache : false,
		            			url : '${pageContext.request.contextPath}/admin/keyword/getKeywordDetail.do',
		            			contentType : 'application/json',
		            			dataType : 'json',
		            			data : { 
		            				keyword_id : $(this).attr("data-id")
		            			},
		            			beforeSend : function(data) {
		            				$.blockUI({
		            					blockMsgClass: "ajax-loading",
		            					showOverlay: true,
		            					overlayCSS: { backgroundColor: '#CECDAD' } ,
		            					css: { border: 'none' } ,
		            					 message: "<b>로딩중..</b>"
		            				});
		            			}, 
		            			complete : function() {
		            				$.unblockUI();
		            			},
		            			success: function(data, status){
		            				if("success"==status){
		            					if(data.flag=="0000"){
			            					var t = _.template($('#template-updatekeyword').html());
			            					$("#prod_img_area").html(t({ data: data, imgSize: '${imgSize}', repsSize: '${repsSize}', thumbSize: '${thumbSize}'}));
		            					} else {
		            						alert(data.message);
		            					}
		            				}else{
		            					alert('조회 결과에 문제가 생겼습니다.')
		            				}
		                        },
		                        error: function(xhr, textStatus, errorThrown) {
		                        	alert('조회에 실패하였습니다.')
		                        }
		            		});
		            	}
		            });
					
					//컨텐츠 선택 팝업
					$(document).on("click","#selectBtn",function () {
				        url = '${pageContext.request.contextPath}/admin/commonMng/getOnceCategoryAlbum.do?categoryId=VC&hiddenName=content_info&textName=&textHtml=ChoiceData&isTypeChange=Y&type=I30';
		                category_window = window.open(url, 'getCategoryAlbumList', 'width=800,height=600,scrollbars=yes');
				    });
					
					//키워드 타입 변경 시
					$(document).on("change","#keyword_type",function () {
						if("D"==$(this).val()){
							$('#keyword_th').text("카테고리 명");
						}else{
							$('#keyword_th').text("키워드 명");
						}
					});
					
					//등록 폼
					$("#regbtn").click(function(){
						if($("#currentKeyword").val()==""){
							alert("카테고리를 선택해 주세요.");
							return false;
						}
						
						if($("#currentKeywordType").val()=="C"){
							alert("키워드 타입이 컨텐츠일 경우 하위 리스트를 등록 할 수 없습니다.");
							return false;
						}
						
						var t = _.template($('#template-insertkeyword').html());
						$("#prod_img_area").html(t({ pKeywordId:$("#currentKeyword").val(), aName:$("#currentKeywordName").val(), imgSize: '${imgSize}', repsSize: '${repsSize}', thumbSize: '${thumbSize}' }));
						
					});
					
					//등록/수정 취소
					$(document).on("click","#cancelbtn",function () {
						$(".treeVw #tree_"+$("#currentKeyword").val()).trigger("click"); 
				    });
					
					//등록/수정 클릭
					$("#sFrom").ajaxForm({
						dataType: 'json', 
			        	beforeSubmit: function() {
			        		if ($("#keyword_name").val() == "") {
			    				alert("키워드명을 입력해 주세요.");
			    				$("#keyword_name").focus();
			    				return false;
			    			} else if ('false' == checkByteMessage($("#keyword_name").val(), 128, 2)) {
			    				alert('키워드명은 128Byte 이내로 입력해야 합니다.');
			    				$("#keyword_name").focus();
			    				return false;
			    			}
			            }, 
            			beforeSend : function(data) {
            				$.blockUI({
            					blockMsgClass: "ajax-loading",
            					showOverlay: true,
            					overlayCSS: { backgroundColor: '#CECDAD' } ,
            					css: { border: 'none' } ,
            					 message: "<b>로딩중..</b>"
            				});
            			},
			            success: function(data, status){
							if("success"==status){
								if("0000"==data.flag){
									
									if(data.proc_type=="1"){ //등록
										//현재 커서가 가있는 곳의 상세정보로 갱신 되도록
										if(""==data.p_keyword_id) data.p_keyword_id = "root";
										$(".treeVw #tree_" + data.p_keyword_id).trigger("click"); 
										alert('정상 등록되었습니다.');
										fnResetCategory(data.p_keyword_id);
									} else { //수정
										//현재 커서가 가있는 곳의 상세정보로 갱신 되도록
										$(".treeVw #tree_"+data.keyword_id).trigger("click"); 
										//수정시 이름이 바꼈을 경우를 위해 
										fnUpdateCategory(data);
										alert('정상 수정되었습니다.');
									}
								}
								else alert(data.message);
							}else{
								alert('등록에 문제가 생겼습니다.')
							}
			            }, 
            			complete : function() {
            				$.unblockUI();
            			},
			            error: function(xhr, textStatus, errorThrown) {
			            	alert('등록을 실패하였습니다.')
			            }                              
			        });
					
					//키워드 삭제
					$("#delbtn").click(function(){
						if($("#currentKeyword").val()=="" || $("#currentKeyword").val()=="root"){
							alert("키워드를 선택해 주세요.");
							return false;
						}
						
						if(confirm("정말 삭제하시겠습니까?")){
							$.ajax({
		            			cache : false,
		            			url : '${pageContext.request.contextPath}/admin/keyword/deleteKeywordProc.do',
		            			contentType : 'application/json',
		            			dataType : 'json',
		            			data : { 
		            				keyword_id : $("#currentKeyword").val()
		            			},
		            			beforeSend : function(data) {
		            				$.blockUI({
		            					blockMsgClass: "ajax-loading",
		            					showOverlay: true,
		            					overlayCSS: { backgroundColor: '#CECDAD' } ,
		            					css: { border: 'none' } ,
		            					 message: "<b>로딩중..</b>"
		            				});
		            			}, 
		            			complete : function() {
		            				$.unblockUI();
		            			},
		            			success: function(data, status){
		            				if("success"==status){
		            					if(data.flag=="0000"){
		            						fnResetCursor();
			            					alert('삭제되었습니다.')
		            						fnResetCategory(data.p_keyword_id);
		            					} else {
		            						alert(data.message);
		            					}
		            				}else{
		            					alert('처리 결과에 문제가 생겼습니다.')
		            				}
		                        },
		                        error: function(xhr, textStatus, errorThrown) {
		                        	alert('처리에 실패하였습니다.')
		                        }
		            		});
						}
			    	});
					
					//순서변경
					$("#orderbtn").click(function(){
						if($("#currentKeyword").val()==""){
							alert("순서를 변경할 카테고리를 선택해 주세요.");
							return false;
						}
						if($("#currentKeywordType").val()!="D"){
							alert("카테고리가 아닙니다.");
							return false;
						}
				        url = '${pageContext.request.contextPath}/admin/keyword/keywordOrderChangePop.do?callbak=fnResetCategory&keyword_id=' + $("#currentKeyword").val();
		                category_window = window.open(url, 'keywordOrderChangePop', 'width=400,height=320,scrollbars=yes');
						
					});
					
					//즉시적용
					$("#applybtn").click(function(){
						if(confirm("즉시적용 하시겠습니까?")){
							$.blockUI({
								blockMsgClass: "ajax-loading",
								showOverlay: true,
								overlayCSS: { backgroundColor: '#CECDAD' } ,
								css: { border: 'none' } ,
								 message: "<b>로딩중..</b>"
							});
							$.post("${pageContext.request.contextPath}/admin/keyword/applyCache.do", 
									{callByScheduler : 'A'},
									function(data) {
									 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
									 	var flag = data.flag;
									 	var message = data.message;
									 	
									 	if(flag == "0000"){						// 정상적으로 처리된 경우						 		
									 		alert("키워드 정보를 즉시적용하였습니다.");	
									 		//location.reload();
									 	}else{
									 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
									 	}
									 	$.unblockUI();
								  },
								  "json"
						    );
						}
					});
					
					//이미지 등록 폼 이벤트
					$(document).on("change",".img_file",function () {
						if(this.value != ''){
							if(imageFileCheck(this.value,this.id)){
								alert("이미지 파일이 아닙니다.");
							}
						}
					});
					
					//등록된 이미지 Viewer
					$("#div-ImageView").draggable();
					
				});
				
				//커서 초기화
				function fnResetCursor(){
					$(".treeVw A").each(function(){
	            		$(this).css("background-color","white");
		            	$(this).css("color","black");
	            	});
					$("#prod_img_area").html("");
	            	$("#currentKeyword").val("");
	            	$("#currentKeywordName").val("");
	            	$("#currentKeywordType").val("");
				}
				
				//이미지 파일 검사
				function imageFileCheck(filename, inputname){
					
					   var fileName=filename;	 
					   var fileSuffix =fileName.substring(fileName.lastIndexOf(".") + 1);
					   fileSuffix = fileSuffix.toLowerCase();
					    if (!( "jpg" == fileSuffix || "jpeg" == fileSuffix  || "gif" == fileSuffix || "bmp" == fileSuffix || "png" == fileSuffix )){
					    	
					    	//익스플로러 버전 비교 파일 초기화.
					    	browserCheckReset(inputname);
					    	return true;
					    }
					    return false;
				}
				
				//트리를 재갱신 한다.
				function fnResetCategory(str){
					if(str=="root" || str==""){//ROOT일 경우 재갱신
						location.reload(); 
					}else{
						
						//새로갱신하도록 셋팅
						$(".treeVw #"+str).addClass("hasChildren");
						//열려있으면 닫았다가 다시 오픈
						if(!$(".treeVw #"+str).hasClass("expandable")){
							$(".treeVw #"+str+" div").trigger("click");
							$(".treeVw #"+str+" div").trigger("click");
						} 
					}
				}
				
				//트리를 업데이트 한다.
				function fnUpdateCategory(data){
					if("Y"==data.use_yn) $(".treeVw #" + data.keyword_id + " #tree_" + data.keyword_id + " span").html(data.keyword_name);
					else $(".treeVw #" + data.keyword_id + " #tree_" + data.keyword_id + " span").html("<strike>" + data.keyword_name + "</strike>");
					$("#currentKeywordName").val(data.keyword_name);
				}
				
				//선택한 이미지를 보여준다.
				function fnImageView(arg){
					$("#viewImg").attr("src", $("#"+arg).val());
					$("#div-ImageView").css({'top':$("#div-ImageArea").offset().top, 'left':$("#div-ImageArea").offset().left});
					$("#div-ImageView").show();
				}
				
				//선택한 이미지를 숨긴다.
				function fnImageViewClose(){
					$("#div-ImageView").hide();
					$("#viewImg").attr("src", "${pageContext.request.contextPath}/images/progressbar.png");
				}
				
				//이미지삭제
				function fnDelImage(arg){
					$("#keyword_"+arg+"_old").val(""); 
					$("#text_"+arg).remove();
					$("#btn_"+arg).remove();
		    	}
				
				//파일 초기화
				function browserCheckReset(fileId) {
					//익스플로러 버전 비교 파일 초기화.
					if(/MSIE/.test(navigator.userAgent)){
					    //IE 10.0 이하면
					    if(navigator.appVersion.indexOf("MSIE 10.0")>=0 || navigator.appVersion.indexOf("MSIE 8.0")>=0){
					    	$("#"+fileId+"").replaceWith( $("#"+fileId+"").clone(true) );
					    }else{	 //IE 10.0 이상 또는 크롭 / 파폭
					    	$("#"+fileId+"").attr("value","");	
				 		}
					} else {
						$("#"+fileId+"").attr("value","");	
					}
				}
				
				
			</script>
            </td>
			<td background="${pageContext.request.contextPath}/images/admin/bg_02.gif" width="35">&nbsp;</td>
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
                                    <!-- <img src="${pageContext.request.contextPath}/images/admin/category_map_title.gif">-->
                                    키워드 관리
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
						                <tr>
						                    <td height="10"></td>
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
						                                            <td width="15"><img src="${pageContext.request.contextPath}/images/admin/blt_07.gif"></td>
						                                            <td class="bold">키워드</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        
												<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%" class="board_data">
													<colgroup>
														<col width="25%">
														<col width="75%">
													</colgroup>
													<tbody>
														<tr>
															<th style="height:35px;">카테고리</th>
															<th>상세</th>
														</tr>
														<tr>
															<td style="border-right: 1px #9ecbdf solid; max-width: 200px;" valign="top">
																<!-- 카테고리 영역 -->
																<table border="0" cellpadding="0" cellspacing="0" width="100%">
																	<tbody><tr><td style="text-align:right;"><span class="button small blue" id="regbtn">등록</span>&nbsp;<span class="button small blue" id="delbtn">삭제</span>&nbsp;<span class="button small blue" id="orderbtn">순서변경</span>&nbsp;<span class="button small red" id="applybtn">즉시적용</span></td></tr></tbody>
																</table>
																<div id="wrapperCategory" style="height:500px;padding:5px;overflow-y: auto;overflow-x: hidden;">
																	<span class="treeVw"><a style="cursor:pointer;" data-id="root" data-type="D" id="tree_root">ROOT</a></span>
																	<ul id="root" class="treeVw">
																	</ul>
																</div>
																<!-- 카테고리 영역 -->
															</td>
															<!-- 상세 영역 -->
															<td style="padding:5px;" valign="top">
																<div id="div-ImageArea" sytel="border:0px;">
																	<div id="div-ImageView" class="div-ImageView" style="display:none;">
																		<table>
																			<tr><td style="text-align:left"><img src="${pageContext.request.contextPath}/images/x.gif" style="cursor:pointer;" onClick="fnImageViewClose();"></td></tr>
																			<tr>
																				<td>
																					<img id="viewImg" src="${pageContext.request.contextPath}/images/progressbar.png">
																				</td>
																			</tr>
																		</table>
																	</div>
																</div>
																<input type="hidden" id="currentKeyword" name="currentKeyword" value="" />
																<input type="hidden" id="currentKeywordName" name="currentKeywordName" value="" />
																<input type="hidden" id="currentKeywordType" name="currentKeywordType" value="" />
																<form name="sFrom" id="sFrom" method="POST" action="./insertKeywordProc.do" encType="multipart/form-data">
																<table border="0" cellpadding="0" cellspacing="0" width="100%">
																	<tbody>
																		<tr>
																			<td valign="top" id="prod_img_area" style="border:0px;">
																			</td>
																		</tr>
																	</tbody>
																</table>
																</form>
																<script id="template-insertkeyword" type="text/template">
																<table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_data">
																<tbody>
																	<tr align="center">
																	    <th width="25%">상위 카테고리</th>
																	    <td width="5"></td>
																	    <td align="left" >
																			{{=aName}}
																			<input type="hidden" id="keyword_id" name="keyword_id" value=""/>
																			<input type="hidden" id="p_keyword_id" name="p_keyword_id" value="{{ if("root"!=pKeywordId) { }}{{=pKeywordId}}{{ } }}" />
																			<input type="hidden" id="proc_type" name="proc_type" value="1" />
																	    </td>
																	</tr>
																	<tr align="center">
																	    <th width="25%">타입</th>
																	    <td width="5"></td>
																	    <td align="left" >
																		<select id="keyword_type" name="keyword_type">
																			<option value="D" selected>카테고리</option>
																			<option value="C">키워드</option>
																		</select>
																	    </td>
																	</tr>
																	<tr align="center">
																	    <th id="keyword_th" width="25%">키워드 명</th>
																	    <td width="5"></td>
																	    <td align="left" >
																		<input type="text" id="keyword_name" name="keyword_name" size="35" maxlength="128" value=""/>
																	    </td>
																	</tr>
																	<tr align="center" id="trRepsImage">
																	    <th width="25%">이미지</th>
																	    <td width="5"></td>
																	    <td align="left" >
																		<input type="file" id="keyword_image" name="keyword_image" value="" class="img_file">
																		<input type="hidden" id="keyword_image_old" name="keyword_image_old" value=""/>
																	    </td>
																	</tr>
																	<tr align="center" id="trRepsImage">
																	    <th width="25%">이미지2</th>
																	    <td width="5"></td>
																	    <td align="left" >
																		<input type="file" id="keyword_image2" name="keyword_image2" value="" class="img_file">
																		<input type="hidden" id="keyword_image_old2" name="keyword_image_old2" value=""/>
																	    </td>
																	</tr>
																	<tr align="center">
																	    <th width="25%">사용여부</th>
																	    <td width="5"></td>
																	    <td align="left" >
																			<input type="radio" id="use_yn" name="use_yn" value="Y" checked/>예
																			<input type="radio" id="use_yn" name="use_yn" value="N" />아니오							
																	    </td>
																	</tr>
																	<tr>
																		<td colspan="3" style="text-align:center;color:red;">
																			이미지 파일은 {{=imgSize}} KB 이하로 등록가능</br>
																		</td>
																	</tr>
																	<tr>
																		<td colspan="3" style="text-align:center">
																			<input type="submit" value="등록" class="button small blue"/>&nbsp;<span class="button small blue" id="cancelbtn">취소</span>
																	    </td>
																	</tr>
																</tbody>
																</table>
																</script>
																<script id="template-updatekeyword" type="text/template">
																{{ if(_.isNull(data) || _.isEmpty(data)) { }}
																<table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_data">
																<tbody>
																	<tr align="center"><td>데이터 미존재</td></tr>
																</tbody>
																</table>
																{{ } }} 
																<table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_data">
																<tbody>
																	<tr align="center">
																	    <th width="25%">키워드 ID</th>
																	    <td width="5"></td>
																	    <td align="left" >
																		<span>
																			{{=data.keyword_id}}
																		</span>
																		<input type="hidden" id="keyword_type" name="p_keyword_type" value="{{=data.keyword_type}}" />	
																		<input type="hidden" id="p_keyword_id" name="p_keyword_id" value="{{=data.p_keyword_id}}" />		
																		<input type="hidden" id="keyword_id" name="keyword_id" value="{{=data.keyword_id}}" />				
																		<input type="hidden" id="image_url" name="image_url" value="{{=data.image_url}}" />					
																		<input type="hidden" id="image_url2" name="image_url2" value="{{=data.image_url2}}" />					
																		<input type="hidden" id="proc_type" name="proc_type" value="2" />	
																	    </td>
																	</tr>
																	<tr align="center">
																	    <th width="25%">타입</th>
																	    <td width="5"></td>
																	    <td align="left" >
																		<span>
																			{{ if(data.keyword_type=="D") { }}카테고리{{ }else{ }}컨텐츠 {{ } }}
																		</span>
																	    </td>
																	</tr>
																	<tr align="center">
																	    <th width="25%">{{ if(data.keyword_type=="D") { }}카테고리{{ }else{ }}컨텐츠 {{ } }} 명</th>
																	    <td width="5"></td>
																	    <td align="left" >
																		<input type="text" id="keyword_name" name="keyword_name" size="35" maxlength="128" value="{{=data.keyword_name}}"/>
																	    </td>
																	</tr>
																	<tr align="center" id="trImage">
																	    <th width="25%">이미지</th>
																	    <td width="5"></td>
																	    <td align="left" >
																		<input type="file" id="keyword_image" name="keyword_image" value="" class="img_file">
																		{{ if(!(_.isNull(data.image) || _.isEmpty(data.image))) { }}&nbsp;<span id="text_image" style="cursor:pointer" onClick="fnImageView('image_url');">{{=data.image}}</span><span class="button small red" id="btn_image"  onClick="fnDelImage('image');">삭제</span>{{ } }}
																		<input type="hidden" id="keyword_image_old" name="keyword_image_old" size="50" value="{{=data.image}}"/>	
																	    </td>
																	</tr>
																	<tr align="center" id="trImage">
																	    <th width="25%">이미지2</th>
																	    <td width="5"></td>
																	    <td align="left" >
																		<input type="file" id="keyword_image2" name="keyword_image2" value="" class="img_file">
																		{{ if(!(_.isNull(data.image2) || _.isEmpty(data.image2))) { }}&nbsp;<span id="text_image2" style="cursor:pointer" onClick="fnImageView('image_url2');">{{=data.image2}}</span><span class="button small red" id="btn_image2"  onClick="fnDelImage('image2');">삭제</span>{{ } }}
																		<input type="hidden" id="keyword_image2_old" name="keyword_image2_old" size="50" value="{{=data.image2}}"/>	
																	    </td>
																	</tr>
																	<tr align="center">
																	    <th width="25%">사용여부</th>
																	    <td width="5"></td>
																	    <td align="left" >
																			<input type="radio" id="use_yn" name="use_yn" value="Y" {{ if(data.use_yn=="Y") { }} checked {{ } }}/>예
																			<input type="radio" id="use_yn" name="use_yn" value="N" {{ if(data.use_yn=="N") { }} checked {{ } }}/>아니오							
																	    </td>
																	</tr>
																	<tr>
																		<td colspan="3" style="text-align:center;color:red;">
																			이미지 파일은 {{=imgSize}} KB 이하로 등록가능</br>
																		</td>
																	</tr>
																	<tr>
																		<td colspan="3" style="text-align:center">
																			<input type="submit" value="수정" class="button small blue"/>&nbsp;<span class="button small blue" id="cancelbtn">취소</span>
																	    </td>
																	</tr>
																</tbody>
																</table>
																</script>
															</td>
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
	    <td colspan="2" background="${pageContext.request.contextPath}/images/admin/copy_bg.gif" height="60" align="left">
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