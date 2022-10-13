<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">

	var processJob = false;
	
	$(document).ready(function(){
		
		// ajax 작업시 캐쉬를 사용하지 않도록 한다
		$.ajaxSetup({ cache: false });
		
		$("#regfrm").ajaxForm({
			dataType:  "json", 
			url: '<%=webRoot%>/admin/gpack/pack/updatePack.do',
			beforeSubmit : checkForm,
	        success : function(data){
	        	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
			 	var flag = data.flag;
			 	var message = data.message;

                if ('0000' == flag) {
					alert("템플릿이 등록되었습니다");
                } else {
			 		alert("작업 중 오류가 발생하였습니다\nmessage : " + message);
                }
	        	processJob = false;
	        	location.reload();
	        },
	        error:function (error) {
	        	console.log(error.status);
	            console.log(error.response);
	            console.log(error.responseText);
	            console.log(error.statusText);
                alert('작업 중 오류가 발생하였습니다');
	        	processJob = false;
            }
		});
		
		$("#regfrm").submit(function(){
			return false;	
		});
		
		$("#regbtn").click(function(){
			if(processJob == false){
				processJob = true;
				$("#regfrm").submit();
			}else{
				alert("현재 작업중입니다. 잠시만 기다려주세요");
			}
		});
		
		$("#cachebtn").click(function(){

			var pack_id = $("#pack_id").val();
			var smartUXManager = $("#smartUXManager").val();
			var template_type = $("input:checked[name='template_type']").val();
			
			var postUrl = "<%=webRoot%>/admin/gpack/pack/applyGpackBiz.do";
			$.post(  postUrl, 
					 {pack_id : pack_id, smartUXManager : smartUXManager, template_type : template_type},
					  function(data) {
						 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
						 	var flag = data.flag;
						 	var message = data.message;

						 	if(flag == "0000"){						// 정상적으로 처리된 경우
						 		alert("정상적으로 상용에 반영하였습니다.");
						 	}else{
						 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
						 	}
					  },
					  "json"
		    );
		});
		
		// 미리보기
		$("#previewbtn").click(function(){
			var pack_id = $("#pack_id").val();
			var url = "<%=webRoot%>/admin/gpack/pack/previewGpackPack.do?pack_id=" + pack_id + "&template_type=${result.template_type}"; 
			window.open(url, "preview", "scrollbars=yes,width=800,height=600");
		});
		
		// 미리보기(상용)
		$("#previewBizbtn").click(function(){
			var pack_id = $("#pack_id").val();
			var url = "<%=webRoot%>/admin/gpack/pack/previewGpackPackBiz.do?pack_id=" + pack_id + "&template_type=${result.template_type}"; 
			window.open(url, "previewBiz", "scrollbars=yes,width=800,height=600");
		});
	});
	
	//입력체크
	function checkForm(){
		var pack_nm = $("#pack_nm").val();
		
		if(pack_nm == ""){
			$("#pack_nm").val("${pack_default_title}");
		}
		return true;
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
                                    템플릿 관리
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
						                                            <td class="bold">
																		${pack_default_title} 템플릿 관리
						                                            </td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="regfrm" name="regfrm" method="post" action="<%=webRoot%>/admin/gpack/pack/updatePack.do">
						                        <input type="hidden" id="pack_id" name="pack_id" value="${result.pack_id}"/>
						                        <input type="hidden" id="smartUXManager" name="smartUXManager" value="<%=id_decrypt %>" />
						                        
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">타이틀</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    <c:if test="${result.pack_nm ne ''}">
					                                    	<input type="text" name="pack_nm" id="pack_nm" size="35" style="font-size: 12px;" value="${result.pack_nm}"/>
					                                    </c:if>
					                                    <c:if test="${result.pack_nm eq ''}">
															<input type="text" name="pack_nm" id="pack_nm" size="35" style="font-size: 12px;" value="${pack_default_title}"/>
														</c:if>				
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">템플릿 타입</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="radio" id="template_type1" name="template_type" value="TP001" <c:if test="${result.template_type ne 'TP002' }">checked="checked"</c:if> /><label for="template_type1">템플릿1</label>
															<input type="radio" id="template_type2" name="template_type" value="TP002" <c:if test="${result.template_type eq 'TP002' }">checked="checked"</c:if> /><label for="template_type2">템플릿2(영상포함)</label>															
														</td>
					                                </tr>
					                            	</tbody>
					                            </table>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<span class="button small blue" id="regbtn">등록</span>
						                                	<span class="button small blue" id="previewBizbtn">미리보기(상용)</span>
						                                	<span class="button small blue" id="previewbtn">미리보기</span>
						                                	<span class="button small blue" id="cachebtn">상용반영</span>
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