<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<title>LG U+ HDTV</title>


<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
 
<link href="/smartux_adm/css/anytime_style.css" rel="stylesheet" type="text/css">
<script src="/smartux_adm/js/anytime.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	
	$("#sumitBtn").click(function () {
	 	
		 if($("#title").val()==""){
				alert('제목을 입력해 주세요.');
		 		$("#title").focus();
				return false;
		  	}
		    if($("#terms_text").val()==""){
				alert('내용을 입력해 주세요.');
		 		$("#terms_text").focus();
				return false;
		  	}
		    
		    if($("#terms_text").val()!=""){
		    	if (textareaCheck($("#terms_text").val())) {
					var check = textareaCheck($("#terms_text").val());
					alert(check + '는 입력할수 없습니다.');
					$("#terms_text").focus();
					return false;
				}
		  	}
	 	
		$("#service_type").removeAttr("disabled");
		$("#service_gbn").removeAttr("disabled");
		$("#terms_gbn").removeAttr("disabled");
		/* 
		$("form[name=form1]").submit();
		*/
		
		$("form[name=form1").submit(function(e){
			 
		    var formObj = $(this);
		    var formURL = formObj.attr("action");
		    var formData = new FormData(this);
		    
		    $.ajax({
		        url: formURL,
		    	type: 'POST',
		    	dataType: 'json',
		        data:  formData,
		    	mimeType:"multipart/form-data",
		    	contentType: false,
		        cache: false,
		        processData:false,
			    success: function(data, textStatus, jqXHR)
			    {
			    	if(data.result.flag=="0000"){
						alert("정상적으로 처리 되었습니다.");
						$(location).attr('href',"./getTermsList.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&s_service_type=${vo.s_service_type}&s_service_gbn=${vo.s_service_gbn}&s_terms_gbn=${vo.s_terms_gbn}&s_display_yn=${vo.s_display_yn}");
			    	}else{
						alert(data.result.message);
					}
			    },
			     error: function(jqXHR, textStatus, errorThrown) 
			     {
			     }
			});
		    e.preventDefault(); //Prevent Default action. 
		    e.unbind();
		}); 
		$("form[name=form1").submit(); //Submit the form
		
	});
});

function winOpen(imagePath, Width, Height) {
    window.open(imagePath, 'windowName', 'toolbar=no,scrollbars=yes,resizable=yes, top=20,left=200,width=' + Width + ',height=' + Height);
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
                                	<!-- 카테고리 타이틀 -->
                                    <!-- <img src="/smartux_adm/images/admin/category_map_title.gif">-->
                                    	약관 관리
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
						                                            <td class="bold">약관 수정</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="form1" name="form1" action="./updateTermsProc.do" method="post" enctype="multipart/form-data">
						                        <input type="hidden" name="findName" value="${vo.findName}" />
						                        <input type="hidden" name="findValue" value="${vo.findValue}" />
						                        <input type="hidden" name="pageNum" value="${vo.pageNum}" />
						                        <input type="hidden" name="s_service_type" value="${vo.s_service_type}" />
						                        <input type="hidden" name="s_service_gbn" value="${vo.s_service_gbn}" />
						                        <input type="hidden" name="s_terms_gbn" value="${vo.s_terms_gbn}" />
						                        <input type="hidden" name="s_display_yn" value="${vo.s_display_yn}" />
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" class="board_data">
					                                <tbody>				                             	
						                                <tr align="center">
						                                    <th width="25%">약관 타입</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<select name="terms_gbn" id="terms_gbn" disabled="disabled">
																	<option value="1" <c:if test="${viewVo.terms_gbn == '1'}">selected="selected"</c:if>>이용약관</option>
																	<option value="2" <c:if test="${viewVo.terms_gbn == '2'}">selected="selected"</c:if>>개인정보 보호 정책</option>
																</select>		
															</td>
														 </tr>				                             	
						                                <tr align="center">
						                                    <th width="25%">제목</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<input type="text" name="title" id="title" size="35" style="font-size: 12px;" value="${viewVo.title}" onKeyUp="checkByte($(this),'256')"/>
															</td>
														 </tr>
														 <tr align="center">
						                                    <th width="25%">서비스 구분</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<select name="service_type" id="service_type" disabled="disabled">
																	<option value="AHOME" <c:if test="${viewVo.service_type == 'AHOME'}">selected="selected"</c:if>>추천</option>
																</select>		
															</td>
														 </tr>		
														 <tr align="center">
						                                    <th width="25%">서비스 여부</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<select name="service_gbn" id="service_gbn" disabled="disabled">
																	<option value="0" <c:if test="${viewVo.service_gbn == '0'}">selected="selected"</c:if>>서비스중</option>
																	<option value="1" <c:if test="${viewVo.service_gbn == '1'}">selected="selected"</c:if>>변경예정</option>
																	
																</select>		
															</td>
														 </tr>		
						                                <tr align="center">
						                                    <th width="25%">노출 여부</th> 
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<select name="display_yn" id="display_yn">
																	<option value="Y" <c:if test="${viewVo.display_yn == 'Y'}">selected="selected"</c:if>>노출</option>
																	<option value="N" <c:if test="${viewVo.display_yn == 'N'}">selected="selected"</c:if>>비노출</option>
																</select>														 							
															</td>
						                                </tr>
						                                <tr align="center">
															<th width="25%">가이드 이미지</th> 
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
																<input type="file" id="imageFile" name="imageFile" accept="image/*" value="파일선택"/>
																 
																<c:if test="${not empty viewVo.terms_img_name}">
																	<input type="hidden" id="imageName" name="imageName" value="${viewVo.terms_img_name}"/>
																	<div id="image">
																		<a href="javascript:winOpen('${viewVo.terms_img_url}','500','500')">${viewVo.terms_img_name}</a>
																		<%-- 
																		<a href="javascript:deleteImage()">
																			<span class="button small blue">삭제</span>
																		</a>
																		 --%>
																	</div>
																</c:if>
																
															</td>
														</tr>
						                                <tr align="center" >
						                                    <th width="25%">내용</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >								                                  
																<textarea rows="30" cols="52" name="terms_text" id="terms_text">${viewVo.terms_text}</textarea>
																  <br>[\f, !^, \b, \f88, \f99]  사용제한							
															</td>
						                                </tr>
													</tbody>
					                            </table>
					                            
					                            <!-- 등록/수정 버튼 -->
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<span class="button small blue" id="sumitBtn">수정</span>
						                                	<a href="./getTermsList.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&s_service_type=${vo.s_service_type}&s_service_gbn=${vo.s_service_gbn}&s_terms_gbn=${vo.s_terms_gbn}&s_display_yn=${vo.s_display_yn}"><span class="button small blue">목록</span></a>
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