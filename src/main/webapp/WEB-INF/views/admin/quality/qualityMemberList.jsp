<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>긴급(비상)공지</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
function doSearch(){
	$("form[name=form1]").submit(); //폼 전송
}
$(document).ready(function(){
	$("#applybtn").click(function(){
		var msg = "품질대상 정보를 상용에 적용하시겠습니까?";
		if(confirm(msg)){
			$.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>로딩중..</b>"
			});
			$.ajax({
			    url: './qualityApplyCache.do',
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
			    }
			});
		}
	});
	
	// 삭제 버튼 클릭
	$("#deleteBtn").click(function(){
		var fileId_comma="";
		if(!$(".checkbox").is(":checked")){
			alert("삭제 할 항목을 선택하세요.");
			return;
		}else{
			var statCnt = 0;
            $(".checkbox").each(function () {
                if ($(this).is(":checked")) {
                	if(statCnt==0){
                		fileId_comma = fileId_comma+$(this).val();
                	}else{
                		fileId_comma = fileId_comma+","+$(this).val();
                	}
                	statCnt++;
                }
            });
		 }
		if(confirm("선택한 품질대상 가번을 삭제 하시겠습니까?")){	
		    $.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>처리 중..</b>"
			});
		    
		    $.ajax({
				url: './qualityMemberDelete.do',
				type:'POST',
				dataType: 'json',
				timeout : 30000,
			    data: {
			        "file_id": fileId_comma,
			        "serviceType" : $("#serviceType").val()
			    },
				success:function(data){
					if(data.result.flag=="0000"){
						alert("정상적으로 처리 되었습니다.");
					}else{
						alert(data.result.message);
					}
				},
				error:function(){
					alert("정상적으로 처리되지 않았습니다.");
				},
				complete:function(){
					//$.unblockUI();
					$(location).attr('href',"./qualityMemberList.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&s_log_type=${vo.s_log_type}&serviceType=${vo.serviceType}"); 
				}
			});
		}
	});
	
    $('#excelDownBtn').click(function () {
        location.href = './downloadQualityExcel.do';
    });
    
    $("#excelFile").change(function(){
		var IMG_FORMAT = "\.(xlsx|xls)$";
		if(!(new RegExp(IMG_FORMAT, "i")).test(this.value)){
	    	//ie일경우
	    	alert("엑셀파일만 업로드해주세요.");
	    	$("#excelFile").attr("value","");	    			    	
	    	//크롬일경우
	    	$("#excelFile")[0].select();		    	
	    	document.selection.clear();
		}
    });
    
    $("#excelUploadBtn").click(function(){
    	if($("#excelFile").val()==""){
    		alert("엑셀파일을 업로드 해주세요.");
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

    		var file = $("#excelFile")[0].files[0];
    		var formData = new FormData();
    		formData.append('excelFile',file);
    		formData.append("serviceType", $('#serviceType').val());

    	    $.ajax({
    			type:'POST',
    			async: false,
    			url: '/smartux_adm/admin/quality/qualityMemberExcelUpload.do',
    			processData: false,
    			contentType: false,
    			cache: false,
    			data: formData,
    			dataType: 'json',
    			timeout : 30000,
    			success:function(data){
    				var retVal = data.result.flag;
    				if(retVal=="1"){
    					alert("처리가 완료되었습니다. "+data.result.message);
    				}else if(retVal=="2"){
    					alert("데이터가 존재하지 않습니다.");
    				}else if(retVal=="3"){
    					alert("처리에 문제가 생겼습니다.");
    				}
    			},
    			error:function(){
    				alert("문제가 생겼습니다. 다시한번 시도해 주세요.");
    			},
    			complete:function(){
					$(location).attr('href',"./qualityMemberList.do?serviceType="+$('#serviceType').val());
    			}
    		});
    	}
    });

	$(".numeric").keypress(function(e){
		if(e.which && (e.which > 47 && e.which < 58 || e.which==8)){
		}else{
			e.preventDefault();
		}
	});
	$(".numeric").css("ime-mode","disabled");
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
                                	품질수집 관리
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
                    				<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" >
			                            <tbody>
			                            <tr>
			                            <td>
			                                <!-- 검색 시작 -->
											<form id="form1" method="get" action="./qualityMemberList.do">
											<input type="hidden" id="serviceType" name="serviceType" value="${vo.serviceType}">
			                                <table border="0" cellpadding="0" cellspacing="0" width="100%" class="search_table" >
			                                	<tbody >
			                                	<tr >
				                                    <td width="15">&nbsp;</td>
				                                    <td width="80"><img src="/smartux_adm/images/admin/search_title4.gif" border="0" height="47" width="62"></td>
				                                    <td>
					                                    <table border="0" cellpadding="0" cellspacing="0" width="390">		                                    
		                                        			<tbody>
		                                        			<tr><td>&nbsp;</td></tr>
		                                        			<tr>
					                                            <td width="10">
					                                            	<img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9" />
					                                            </td>
					                                            <td>
					                                                <select class="select"  name="findName">
					                                                    <option <c:if test="${vo.findName == 'sa_id'}">selected="selected"</c:if> value="sa_id">가번</option>					                                                    
					                                                </select>
					                                            	<input type="text" name="findValue" value="${vo.findValue}" size="20" style="font-size: 12px;" onKeyPress="if(event.keyCode==13){ doSearch();}" />
					                                            	<font size="2">로그타입 :</font><input type="text" name="s_log_type" value="${vo.s_log_type}" size="3" style="font-size: 12px;" onKeyPress="if(event.keyCode==13){ doSearch();}" class="numeric"/>
					                                            </td>
					                                            <td width="65" align="left">
					                                                <input src="/smartux_adm/images/admin/search.gif" border="0" height="22" type="image" width="65">
					                                            </td>
					                                         </tr>
			                                    			</tbody>
			                                    		</table>
			                                    	</td>
					                            </tr> 					                            
					                        	</tbody>
					                        </table>
					                        </form>
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
							                                        <td class="bold">품질대상 단말 리스트</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>						                                						                                
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <span style="font-size:11px;color:red;">※로그타입※ 1:APP로그 2:통합통계로그, 3:APP로그+통합통계로그</span>
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list">
					                                <tbody>
						                                <tr align="center">
			                                			   <th scope="col" width="45">선택</th>	
													 	   <th scope="col">가번</th>										                                   
						                                   <th scope="col" width="15%">로그타입</th>
														   <th scope="col" width="10%">로그사이즈</th>						                                   
						                                   <th scope="col" width="10%">대상타입</th>	
						                                   <th scope="col" width="10%">로그레벨</th>	
						                                </tr>
						                                <c:if test="${list == '[]' }"> 
							                                <tr align="center">
							                                    <td class="table_td_04" colspan="14">데이터가 존재 하지 않습니다.</td>					                                    
							                                </tr>
														</c:if>
					                                </tbody>
					                                <c:forEach items="${vo.list}" var="qltmem">
					                            	<c:set var="i" value="${i+1}" />
						                            	<tr align="center">
						                            		<!-- 체크 박스 -->
						                                    <td class="table_td_04" >
						                                    	<input type="checkbox" class="checkbox" value="${qltmem.file_id}" />
						                                    </td>
						                                    <!-- 가번 -->						                                    						                                    
						                                    <td class="table_td_04" >
						                                    	<a href="./qualityMemberUpdate.do?file_id=${qltmem.file_id}&findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&s_log_type=${vo.s_log_type}&serviceType=${vo.serviceType}">
						                                    	<c:if test="${qltmem.find_type == 'DE'}">${qltmem.sa_id}</c:if>
						                                    	<c:if test="${qltmem.find_type == 'PR'}">${qltmem.sa_id}*</c:if>
						                                    	<c:if test="${qltmem.find_type == 'PO'}">*${qltmem.sa_id}</c:if>
						                                    	</a>						                                    	 
						                                    </td>
						                                    <!-- 로그타입 -->	
						                                    <td class="table_td_04 ">
						                                    	${qltmem.log_type}
						                                    </td>
						                                    <!--  사이즈 -->
						                                    <td class="table_td_04 ">
						                                    	${qltmem.size} M
						                                    </td>
						                                    <!-- 대상타입 -->
						                                    <td class="table_td_04 ">
						                                    	<c:if test="${qltmem.find_type == 'DE'}">default</c:if>
						                                    	<c:if test="${qltmem.find_type == 'PR'}">prefix</c:if>
						                                    	<c:if test="${qltmem.find_type == 'PO'}">postfix</c:if>
						                                    </td>
						                                    <td class="table_td_04">
						                                    	<c:if test="${qltmem.log_level == '1'}">Verbose</c:if>
						                                    	<c:if test="${qltmem.log_level == '2'}">Debug</c:if>
						                                    	<c:if test="${qltmem.log_level == '3'}">Info </c:if>
						                                    	<c:if test="${qltmem.log_level == '4'}">Warning </c:if>
						                                    	<c:if test="${qltmem.log_level == '5'}">Error</c:if>
						                                    </td>
						                                </tr>
					                            	</c:forEach>
					                            </table>
					                            
					                            <!-- 페이징 -->
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
						                            <tbody>
						                            <tr>
						                                <td height="5"></td> 
						                            </tr>
						                            <tr>
						                            	<td align="center">						                            		
						                            		 <jsp:include page="/WEB-INF/views/include/naviControll.jsp">
																<jsp:param value="qualityMemberList.do" name="actionUrl"/>
																<jsp:param value="?findName=${vo.findName}&findValue=${vo.findValue}&s_log_type=${vo.s_log_type}&serviceType=${vo.serviceType}" name="naviUrl" />
																<jsp:param value="${vo.pageNum }" name="pageNum"/>
																<jsp:param value="${vo.pageSize }" name="pageSize"/>
																<jsp:param value="${vo.blockSize }" name="blockSize"/>
																<jsp:param value="${vo.pageCount }" name="pageCount"/>																			  
															</jsp:include> 
						                            	</td>
						                            </tr>
						                        	</tbody>
						                        </table>
						                        
						                        <!-- 등록/수정 버튼 -->
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            <tbody>
						                            <tr>
						                                <td align="left">&nbsp;
						                                	<span class="button small blue" id="deleteBtn">삭제</span>
						                                	<span class="button small red" id="excelDownBtn">엑셀다운</span>
						                                </td>
						                                <td align="right">
						                                	<a href="./qualityMemberInsert.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&s_log_type=${vo.s_log_type}&serviceType=${vo.serviceType}"><span class="button small blue">등록</span></a>
						                                	<span class="button small blue" id="applybtn">즉시적용</span>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table><br/>
						                       	<form id="ex_form" method="post" action="./qualityMemberExcelUpload.do">
					                            <table border="0" cellpadding="0" cellspacing="0" class="board_list" style="width:350px;" align="right">
						                            <tbody>
						                            <tr>
						                            	<th colspan="2">엑셀파일 업로드</th>
						                            </tr>
						                            <tr>
						                                <td align="left">
						                                	<input type="file" name="excelFile" id="excelFile">
						                                </td>
						                                <td align="right">
						                                	<span class="button small blue" id="excelUploadBtn">엑셀업로드</span>
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