<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
$(document).ready(function(){
	//서비스 구분 선택 시
	$("#s_service").change(function(){
		fnServiceChange($("#s_service").val());
	});
	//저장필드 타입 선택시
	$("#i_ntype").change(function(){
		if($(this).val()=="TXT"){
			fnInsTypeChange(1);
		}else if($(this).val()=="IMG"){
			fnInsTypeChange(2);
		}
	});
	//저장 요청
	$("#istBtn").click(function(){
		$("#i_service").val($("#s_service").val());
		if($("#i_service").val()==""){
			alert("데이터에 이상이 있습니다..");
			return false;
		}else if($("#i_ntype").val()==""){
			alert("타입을 입력해 주세요.");
			$("#i_ntype").focus();
			return false;
		}else if($("#i_category").val()==""){
			alert("카테고리를 입력해 주세요.");
			$("#i_category").focus();
			return false;
		}else if($("#i_nvalue").val()=="" && $("#i_ntype").val()=="TXT"){
			alert("내용을 입력해 주세요.");
			$("#i_nvalue").focus();
			return false;
		}else if($("#i_nvalue_file").val()=="" && $("#i_ntype").val()=="IMG"){
			alert("이미지를 등록해 주세요.");
			$("#i_nvalue_file").focus();
			return false;
		}else if($("#i_display_sec").val()==""){
			alert("노출시간을 입력해 주세요.");
			$("#i_display_sec").focus();
			return false;
		}else if($("#i_ordered").val()==""){
			alert("정렬순서를 입력해 주세요.");
			$("#i_ordered").focus();
			return false;
		}
		$("#istForm").submit();
	});
	//저장 필드 활성화
	$("#istShowBtn").click(function(){
		fnUpdCancel();
		$("#istTr").show();
	})
	//저장 필드 비활성화
	$("#istCancelBtn").click(function(){
		fnIstCancel();
	});
	
	//즉시적용
	$("#cacheBtn").click(function(){
		var confirmmsg = "공지 및 배너를 즉시적용 하시겠습니까?";
		
		if(confirm(confirmmsg)){
			$.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>로딩중..</b>"
			});
			
			$.ajax({
	            url: '<%=webRoot%>/admin/noticeinfo/cacheNoticeInfo.do',
	            type: 'POST',
	            dataType: 'json',
	            data: {
					callByScheduler : 'A'	
	            },
	            error: function () {
	                alert('작업 중 오류가 발생하였습니다');
	                $.unblockUI();
	            },
	            
	            success: function (data) {
				 	console.log(data);
				 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
				 	var flag = data.flag;
				 	var message = data.message;
				 	if(flag == "0000"){						// 정상적으로 처리된 경우
				 		alert("즉시적용 되었습니다.");
				 	}else{
				 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
				 	}
				 	  $.unblockUI();
	            }
	        });
		}
	});
	
	//CJ즉시적용
	$("#cjcacheBtn").click(function(){
		var confirmmsg = "공지 및 배너를 CJ 즉시적용 하시겠습니까?";
		
		if(confirm(confirmmsg)){
			$.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>로딩중..</b>"
			});
			
			$.ajax({
	            url: '<%=webRoot%>/admin/noticeinfo/cacheCjNoticeInfo.do',
	            type: 'POST',
	            dataType: 'json',
	            data: {
					callByScheduler : 'A'	
	            },
	            error: function () {
	                alert('작업 중 오류가 발생하였습니다');
	                $.unblockUI();
	            },
	            
	            success: function (data) {
				 	console.log(data);
				 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
				 	var flag = data.flag;
				 	var message = data.message;
				 	if(flag == "0000"){						// 정상적으로 처리된 경우
				 		alert("즉시적용 되었습니다.");
				 	}else{
				 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
				 	}
				 	  $.unblockUI();
	            }
	        });
		}
	});
	
	//넘버릭 체크
    fnNumberic();
	
    fnServiceChange($("#s_service").val());
    fnUpdServiceChange($("#s_service").val());
    
    $("#preBtn").click(function(){
    	var url = "<%=webRoot%>/admin/noticeinfo/viewNoticeInfoList.do?service="+$("#s_service").val();
    	window.open(url, "viewnoticeinfo", "width=720,height=405,scrollbars=yes");
	});
    
    $("#realpreBtn").click(function(){
    	var url = "<%=webRoot%>/admin/noticeinfo/viewRealNoticeInfoList.do?service="+$("#s_service").val();
    	window.open(url, "viewnoticeinfo", "width=720,height=405,scrollbars=yes");
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
			<!-- jsp:include page="/WEB-INF/views/include/top.jsp"--><!-- /jsp:include -->
			<%@include file="/WEB-INF/views/include/top.jsp" %>
			<script type="text/javascript">
			//바로 위 인클루드에서 로그인 인증후 redirect할때 일정 이상 라인이 존재하면 오류가 발생해서 스크립트 일부를 아래로 내렸다..
			//수정 필드 활성화
			function fnUpdate(i,seq,ntype,category){
				fnIstCancel();
				fnUpdCancel();
				var element = $("#tr_upd_"+i);
				var newitem = $("#cloneUpdTr").html();
				if( element.prev().html() != null){
					element.before("<tr class='updTr'><td colspan='8' style='border-bottom:0px'><form name='updForm' id='updForm' method='POST' action='updateNoticeInfo.do' encType='multipart/form-data'><table cellapdding='0' cellspacing='0' border='0' width='100%'><tr>"+newitem+"</tr><table></form></td></tr>");
				};
				$("#updForm").find("#u_seq").val(seq);
				$("#updForm").find("#u_ntype").val(ntype);
				$("#updForm").find("#u_ntype_view").val(ntype);
				$("#updForm").find("#u_category").val(category);
				if($("#updForm").find("#u_ntype").val()=="TXT") $("#updForm").find("#u_nvalue").val($("#td_nvalue_"+i).html());
				$("#updForm").find("#u_display_sec").val($("#td_display_sec_"+i).html());
				$("#updForm").find("#u_ordered").val($("#td_ordered_"+i).html());
				if($("#td_use_yn_"+i).html()=="Y") $("#updForm").find("#u_use_yn").attr("checked",true);
				$(".tr_upd").show();
				fnUpdNtypeCh();
				element.hide();
				fnNumberic();
			}

			function fnServiceChange(thVal){
				$(".search_sel_cate").find("option:gt(0)").remove();
				$(".sel_cate").find("option").remove();
				if($("#hide_category") && $("#hide_category").length) {
					$.each($("#hide_category option"), function(key) {
						var name = $("#hide_category option:eq("+key+")").attr("name");
						if(thVal==name){
							$(".sel_cate").append('<option value="'+$("#hide_category option:eq("+key+")").attr("value")+'" name="'+$("#hide_category option:eq("+key+")").attr("name")+'" >'+$("#hide_category option:eq("+key+")").text()+'</option>');
							var sel_opt = "";
							if($("#hide_category option:eq("+key+")").attr("selected")=="selected") sel_opt = 'selected="selected"';
							$(".search_sel_cate").append('<option value="'+$("#hide_category option:eq("+key+")").attr("value")+'" name="'+$("#hide_category option:eq("+key+")").attr("name")+'" '+sel_opt+' >'+$("#hide_category option:eq("+key+")").text()+'</option>');
						}
					});
				}
			}

			function fnInsTypeChange(val){
				if(val==1){
					if ($.browser.msie) {
					    $("#i_nvalue_file").replaceWith( $("#i_nvalue_file").clone(true) );
					} else {
					    $("#i_nvalue_file").val("");
					}
					$("#i_nvalue").show();
					$("#i_nvalue_file").hide();
				}else{
					$("#i_nvalue").val("");
					$("#i_nvalue").hide();
					$("#i_nvalue_file").show();
				}
			}

			function fnUpdServiceChange(thVal){
				if($("#hide_category") && $("#hide_category").length) {
					$.each($("#hide_category option"), function(key) {
						var name = $("#hide_category option:eq("+key+")").attr("name");
						if(thVal==name){
							$(".sel_cate_static").append('<option value="'+$("#hide_category option:eq("+key+")").attr("value")+'" name="'+$("#hide_category option:eq("+key+")").attr("name")+'" >'+$("#hide_category option:eq("+key+")").text()+'</option>');
						}
					});
				}
			}

			function fnUpdTypeChange(val){
				if(val==1){
					if ($.browser.msie) {
						$("#updForm").find("#u_nvalue_file").replaceWith( $("#updForm").find("#u_nvalue_file").clone(true) );
					} else {
						$("#updForm").find("#u_nvalue_file").val("");
					}
					$("#updForm").find("#u_nvalue").show();
					$("#updForm").find("#u_nvalue_file").hide();
				}else{
					$("#updForm").find("#u_nvalue").val("");
					$("#updForm").find("#u_nvalue").hide();
					$("#updForm").find("#u_nvalue_file").show();
				}
			}

			function fnIstCancel(){
				$("#istTr").hide();
				$('#istForm').each(function(){this.reset();});
				$("#i_category option:eq(0)").attr("selected", "selected");
				fnInsTypeChange(1);
			}

			function fnUpdCancel(){
				$(".updTr").remove();
				$(".tr_upd").show();
				$('#updForm').each(function(){this.reset();});
				$("#u_category option:eq(0)").attr("selected", "selected");
				fnUpdTypeChange(1);
			}

			//수정 요청
			function fnUpdSumbit(){
				if($("#updForm").find("#u_seq").val()==""){
					alert("데이터에 이상이 있습니다..");
					return false;
				}else if($("#updForm").find("#u_category").val()==""){
					alert("카테고리를 입력해 주세요.");
					$("#updForm").find("#u_category").focus();
					return false;
				}else if($("#updForm").find("#u_nvalue").val()=="" && $("#updForm").find("#u_ntype").val()=="TXT"){
					alert("내용을 입력해 주세요.");
					$("#updForm").find("#u_nvalue").focus();
					return false;
				}else if($("#updForm").find("#u_display_sec").val()==""){
					alert("노출시간을 입력해 주세요.");
					$("#updForm").find("#u_display_sec").focus();
					return false;
				}else if($("#updForm").find("#u_ordered").val()==""){
					alert("정렬순서를 입력해 주세요.");
					$("#updForm").find("#u_ordered").focus();
					return false;
				}
				//서비스는 수정 못하게..
				//$("#updForm").find("#u_service").val($("#s_service").val());
				$("#updForm").submit();
			}

			function fnUpdNtypeCh(){
				if($("#updForm").find("#u_ntype").val()=="TXT"){
					fnUpdTypeChange(1);
				}else if($("#updForm").find("#u_ntype").val()=="IMG"){
					fnUpdTypeChange(2);
				}
			}

			function fnDelete(seq){
				if(confirm("정말 삭제하시겠습니까?")){
					$("#d_seq").val(seq)
					if($("#d_seq").val()==""){
						alert("데이터에 이상이 있습니다.");
						return false;
					}
					$("#delForm").submit();
				}
			}
			function fnShowNotiImg(obj){
				$("#"+obj).show();
				
			}
			function fnNotiImg(obj){
				$("#"+obj).hide();
			}

			function fnNumberic(){
				$(".num_only").css("imeMode","disabled").keypress(function(event) {
			        if(event.which && (event.which < 48 || event.which > 57) ) {
			            event.preventDefault();
			        }
			    }).keyup(function(){
			        if( $(this).val() != null && $(this).val() != '' ) {
			            $(this).val( $(this).val().replace(/[^0-9]/g, '') );
			        }
			    });
			}
			</script>
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
                                    공지 관리
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
			                                <table border="0" cellpadding="0" cellspacing="0" width="100%" class="search_table">
			                                	<tbody>
			                                	<tr>
				                                    <td width="15">&nbsp;</td>
				                                    <td width="80"><img src="/smartux_adm/images/admin/search_title4.gif" border="0" height="47" width="62"></td>
				                                    <td>
				                                    	<select id="hide_category" name="hide_category" style="display:none;">
					                                        <option value=""></option>
		                            						<c:forEach items="${vo.category_code}" var="code">
		                                                    	<option value="${code.item_code}" name="${code.code}" <c:if test="${code.item_code == vo.s_category}">selected="selected"</c:if>>${code.item_nm}</option>
		                            						</c:forEach>
		                                                </select>
				                                    	<form id="form2" method="get" action="getNoticeInfoList.do"> 
					                                    <table border="0" cellpadding="0" cellspacing="0" width="100%">		                                    
		                                        			<tbody>
		                                        			<tr><td>&nbsp;</td></tr>
		                                        			<tr>
					                                            <td width="10">
					                                            	<img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9" />
					                                            </td>
					                                            <td> 
					                                                <select class="select" id="s_service" name="s_service">
					                            						<c:forEach items="${vo.service_code}" var="code">
					                                                    	<option value="${code.code}" <c:if test="${code.code == vo.s_service}">selected="selected"</c:if>>${code.code_nm}</option>
					                            						</c:forEach>
					                                                </select> 
					                                                <select class="select search_sel_cate" id="s_category" name="s_category">
					                                                    <option value="">전체</option>
					                                                </select>
					                                                <select class="select" id="s_ntype" name="s_ntype">
					                                                    <option value="">전체</option>
					                                                    <option <c:if test="${vo.s_ntype == 'TXT'}">selected="selected"</c:if> value="TXT">공지</option>
					                                                    <option <c:if test="${vo.s_ntype == 'IMG'}">selected="selected"</c:if> value="IMG">배너</option>
					                                                </select> 
					                                                <select class="select" id="s_use_yn" name="s_use_yn">
					                                                    <option value="">전체</option>
					                                                    <option <c:if test="${vo.s_use_yn == 'Y'}">selected="selected"</c:if> value="Y">노출</option>
					                                                    <option <c:if test="${vo.s_use_yn == 'N'}">selected="selected"</c:if> value="N">비노출</option>
					                                                </select>  
					                                            	<input type="text" name="findValue" value="${vo.findValue }" size="20" style="font-size: 12px;" />
					                                            </td>					
					                                            <td width="66" align="left"><input src="/smartux_adm/images/admin/search.gif" border="0" height="22" type="image" width="65"></td>
		                                        			</tr>
			                                    			</tbody>
			                                    		</table>
			                                    		</form>
			                                    	</td>
					                            </tr> 
					                        	</tbody>
					                        </table>		                              
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
						                                            <td class="bold">공지 및 이미지 목록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        
					                            <table border="0" cellspacing="0" cellpadding="0" class="board_list" style="width:100%" id="listTab">
													<tr>
														<th width="10%">등록일</th>
														<th width="7%">타입</th>
														<th width="15%">장르</th>
														<th>내용</th>
														<th width="8%">노출시간</th>
														<th width="5%">정렬</th>
														<th width="8%">노출여부</th>
														<th width="15%">수정/삭제</th>
													</tr>
													<form name="istForm" id="istForm" method="POST" action="insertNoticeInfo.do" encType="multipart/form-data">
													<tr id="istTr" style="display:none;">
														<td class="table_td_04">신규<input type="hidden" name="service" id="i_service" value=""></td>
														<td class="table_td_04">
															<select class="select" id="i_ntype" name="ntype">
			                                                    <option value="TXT">공지</option>
			                                                    <option value="IMG">배너</option>
			                                                </select>
														</td>
														<td class="table_td_04">
															<select class="select sel_cate" id="i_category" name="category">
			                                                </select>
														</td>
														<td class="table_td_04" id="istType"><input type="text" value="" id="i_nvalue" class="i_nvalue" name="nvalue" style="width:90%"><input type="file" value="" id="i_nvalue_file" class="i_nvalue_file" name="nvalue_file" style="width:90%;display:none;"></td>
														<td class="table_td_04"><input type="text" value="" id="i_display_sec" name="display_sec" class="num_only" maxlength="10" style="width:90%"></td>
														<td class="table_td_04"><input type="text" value="" id="i_ordered" name="ordered" class="num_only" maxlength="1" style="width:90%"></td>
														<td class="table_td_04"><input type="checkbox" name="use_yn" id="i_use_yn" value="Y"/></td>
														<td class="table_td_04"><span class="button small red" id="istBtn">등록</span><span class="button small red" id="istCancelBtn">취소</span></td>
													</tr>
													</form>
													<tr id="cloneUpdTr" style="display:none;">
														<td class="table_td_04" width="10%">수정
															<input type="hidden" name="seq" id="u_seq" value="">
															<input type="hidden" name="findValue" id="h_findValue" value="${vo.findValue}">
															<input type="hidden" name="pageNum" id="h_pageNum" value="${vo.pageNum}">
															<input type="hidden" name="s_service" id="h_s_service" value="${vo.s_service}">
															<input type="hidden" name="s_category" id="h_s_category" value="${vo.s_category}">
															<input type="hidden" name="s_ntype" id="h_s_ntype" value="${vo.s_ntype}">
															<input type="hidden" name="s_use_yn" id="h_s_use_yn" value="${vo.s_use_yn}">
														</td>
														<td class="table_td_04" width="7%">
															<select class="select" id="u_ntype_view" name="ntype_view" onChange="fnUpdNtypeCh();" disabled>
			                                                    <option value="TXT">공지</option>
			                                                    <option value="IMG">배너</option>
			                                                </select>
			                                                <input type="hidden" id="u_ntype" name="ntype" value="">
														</td>
														<td class="table_td_04" width="15%">
															<select class="select sel_cate_static" id="u_category" name="category">
			                                                </select>
														</td>
														<td class="table_td_04" id="updType"><input type="text" value="" id="u_nvalue" name="nvalue" style="width:90%"><input type="file" id="u_nvalue_file" name="nvalue_file" style="width:90%;display:none;"></td>
														<td class="table_td_04" width="8%"><input type="text" value="" id="u_display_sec" name="display_sec" class="num_only" maxlength="10" style="width:90%"></td>
														<td class="table_td_04" width="5%"><input type="text" value="" id="u_ordered" name="ordered" class="num_only" maxlength="1" style="width:90%"></td>
														<td class="table_td_04" width="8%"><input type="checkbox" name="use_yn" id="u_use_yn" value="Y"/></td>
														<td class="table_td_04" width="15%"><span class="button small red" id="updBtn" onClick="fnUpdSumbit();">수정</span><span class="button small red" onClick="fnUpdCancel();" id="updCancelBtn">취소</span></td>
													</tr>
													<c:if test="${vo.list == '[]' }">
					                                <tr align="center">
					                                    <td class="table_td_04" colspan="8">데이터가 존재 하지 않습니다.</td>					                                    
					                                </tr>
					                                </c:if>
					                            	<c:forEach items="${vo.list}" var="rec">
					                            	<c:set var="i" value="${i+1}" />
														<tr id="tr_upd_${i}" class="tr_upd">
															<td class="table_td_04" id="td_reg_date_${i}"><c:if test="${rec.ntype == 'IMG'}"><div id="notiImg_${i}" style="position:absolute;left:500px;width:370px;height:100px;overflow:auto;display:none;text-align:center;text-valign:middle;background-color:#EBEBEB;border:1px solid #D0D0D0" onClick="fnNotiImg(this.id);"><image src="${rec.img_addr}${rec.nvalue}"></div></c:if>${rec.reg_date}</td>
															<td class="table_td_04" id="td_ntype_${i}">
																<c:choose>
																	<c:when test="${rec.ntype == 'TXT'}">공지</c:when>
																	<c:when test="${rec.ntype == 'IMG'}">배너</c:when>
																	<c:otherwise>에러</c:otherwise>
																</c:choose>
															</td>
															<td class="table_td_04" id="td_category_nm_${i}">${rec.category_nm}</td>
															<td class="table_td_04" id="td_nvalue_${i}"><c:choose><c:when test="${rec.ntype == 'IMG'}"><a onClick="fnShowNotiImg('notiImg_${i}');" style="cursor:pointer;">${rec.nvalue}</a></c:when><c:otherwise>${rec.nvalue}</c:otherwise></c:choose></td>
															<td class="table_td_04" id="td_display_sec_${i}">${rec.display_sec}</td>
															<td class="table_td_04" id="td_ordered_${i}">${rec.ordered}</td>
															<td class="table_td_04" id="td_use_yn_${i}">${rec.use_yn}</td>
															<td class="table_td_04"><span class="button small red updShowBtn" id="updShowBtn" onClick="fnUpdate('${i}','${rec.seq}','${rec.ntype}','${rec.category}');">수정</span><span class="button small red" id="delBtn" onClick="fnDelete('${rec.seq}');">삭제</span></td>
														</tr>
					                            	</c:forEach>
												</table>
												<table border="0" cellpadding="0" cellspacing="0" width="100%">
						                            <tbody>
						                            <tr>
						                                <td height="5">
						                                <form name="delForm" id="delForm" method="POST" action="deleteNoticeInfo.do">
															<input type="hidden" name="seq" id="d_seq" value="">
															<input type="hidden" name="findValue" id="h_d_findValue" value="${vo.findValue}">
															<input type="hidden" name="pageNum" id="h_d_pageNum" value="${vo.pageNum}">
															<input type="hidden" name="s_service" id="h_d_service" value="${vo.s_service}">
															<input type="hidden" name="s_category" id="h_d_category" value="${vo.s_category}">
															<input type="hidden" name="s_ntype" id="h_d_ntype" value="${vo.s_ntype}">
															<input type="hidden" name="s_use_yn" id="h_d_use_yn" value="${vo.s_use_yn}">
						                                </form>
						                                </td>
						                            </tr>
						                            <tr>
						                            	<td align="center">
						                            		<jsp:include page="/WEB-INF/views/include/naviControll.jsp">
																<jsp:param value="getNoticeInfoList.do" name="actionUrl"/>
																<jsp:param value="?findValue=${vo.findValue}&s_service=${vo.s_service}&s_category=${vo.s_category}&s_ntype=${vo.s_ntype}&s_use_yn=${vo.s_use_yn}" name="naviUrl" />
																<jsp:param value="${vo.pageNum }" name="pageNum"/>
																<jsp:param value="${vo.pageSize }" name="pageSize"/>
																<jsp:param value="${vo.blockSize }" name="blockSize"/>
																<jsp:param value="${vo.pageCount }" name="pageCount"/>			  
															</jsp:include>
						                            	</td>
						                            </tr>
						                        	</tbody>
						                        </table>
												<table border="0" cellpadding="0" cellspacing="0" style="width:95%" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="40" align="right">
						                                	<span class="button small blue" id="istShowBtn">등록</span>
						                                	<span class="button small blue" id="preBtn">프리뷰</span>
															<span class="button small blue" id="realpreBtn">프리뷰(상용)</span>
															<span class="button small blue" id="cacheBtn">즉시적용</span>
															<span class="button small blue" id="cjcacheBtn">CJ 즉시적용</span>
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