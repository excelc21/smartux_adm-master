<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
	$(document).ready(function() {
		// 등록 버튼 클릭
		$("#createbtn").click(function() {
			var url = "<%=webRoot%>/admin/paynow/addBanner.do"; 
			window.open(url, "addBanner", "width=450,height=260");
		});
		
		// 삭제 버튼 클릭
		$("#deletebtn").click(function() {
			var items = "";
			
			if(!$(".checkbox").is(":checked")) {
				alert("삭제 할 배너를 선택해 주세요.");
				return false;
			} else {
				var statCnt = 0;
	            $(".checkbox").each(function() {
	                if ($(this).is(":checked")) {
	                	if(statCnt == 0) {
	                		items = items + $(this).val();
	                	} else {
	                		items = items + "," + $(this).val();
	                	}
	                	statCnt++;
	                }
	            });
			}
			
			if(confirm("선택한 배너를 삭제하시겠습니까?")) {	
			    $.ajax({
					url: './deleteBannerProc.do',
					type:'POST',
					dataType: 'json',
					timeout : 30000,
				    data: {
				        "banner_id": items
				    },
					success:function(textDoc) {
						var flag = textDoc.flag;
				    	var message = textDoc.message;
						if(flag=="0000") {
							alert("정상적으로 삭제되었습니다.");
						} else {
							alert(message);
						}
					},
					error:function() {
						alert("작업 중 오류가 발생하였습니다.");
					},
					complete:function() {
						$(location).attr('href',"./bannerList.do"); 
					}
				});
			}
		});
		
		//즉시적용
		$("#applybtn").click(function() {
			if(confirm("배너를 상용에 적용하시겠습니까?")) {	
			    $.blockUI({
					blockMsgClass: "ajax-loading",
					showOverlay: true,
					overlayCSS: { backgroundColor: '#CECDAD' },
					css: { border: 'none' },
					message: "<b>처리 중..</b>"
				});
			    
			    $.ajax({
					url: './bannerApplyCache.do',
					type:'POST',
					dataType: 'json',
					timeout : 30000,
				    data: {
				    },
					success:function(textDoc) {
						var flag = textDoc.flag;
				    	var message = textDoc.message;
						if(flag=="0000") {
							alert("정상적으로 적용되었습니다.");
						} else {
							alert(message);
						}
						$.unblockUI();
					},
					error:function() {
						alert("작업 중 오류가 발생하였습니다.");
					}
				});
			}
		});
	});
	
	function updateBanner(banner_id){
		var url = "<%=webRoot%>/admin/paynow/updateBanner.do?banner_id=" + banner_id;
		window.open(url, "updateBanner", "width=450,height=260");
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
                <tr style="display:block">
                    <td height="42" width="100%">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tbody>
                            <tr>
                                <td width="300" class="boldTitle">
						            Paynow 배너 조회
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
							                                        <td class="bold">배너 리스트</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>						                                						                                
						                            </tr>
						                       		</tbody>
						                       	</table>
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list">
					                                <tbody>
						                                <tr align="center">
			                                			   <th scope="col" width="5%" height="102">선택</th>
						                                   <th scope="col" width="5%">번호</th>
														   <th scope="col" width="20%">배너 ID</th>		
													 	   <th scope="col" width="30%">배너 이미지</th>
													 	   <th scope="col">안내 문구</th>			
													 	   <th scope="col" width="10%">사용 여부</th>			
						                                </tr>
						                                <c:if test="${bList == '[]'}"> 
							                                <tr align="center">
							                                    <td class="table_td_04" colspan="5">데이터가 존재 하지 않습니다.</td>					                                    
							                                </tr>
														</c:if>
					                                </tbody>
					                                <c:forEach items="${bList}" var="banner">
					                            	<c:set var="i" value="${i+1}" />
						                            	<tr align="center">
						                            		<!-- 체크 박스 -->
						                                    <td class="table_td_04" >
						                                    	<input type="checkbox" class="checkbox" value="${banner.banner_id}"/>
						                                    </td>
						                                    <!-- 번호 -->
						                                    <td class="table_td_04" >
						                                   		${i}
						                                    </td>
															<td class="table_td_04">
						                                    	<a href="#" onClick="updateBanner('${banner.banner_id}')" style="font-weight:bold;text-decoration:underline">${banner.banner_id}</a>				                                    	 
						                                    </td>
						                                    <td class="table_td_04">
						                                    	<img src="${banner.img_url}${banner.banner_img}" width="100" height="100">				                                    	 
						                                    </td>					                                    						                                    
						                                    <td class="table_td_04" style="text-align:left">
						                                    	&nbsp;${banner.banner_text}		                                    	 
						                                    </td>					                                    						                                    
						                                    <td class="table_td_04" >
						                                    	<c:if test="${banner.use_yn == 'Y'}">예</c:if>	
						                                    	<c:if test="${banner.use_yn != 'Y'}">아니오</c:if>				                                    	 
						                                    </td>					                                    						                                    
						                                </tr>
					                            	</c:forEach>
					                            </table><br/>
					                            
					                            <!-- 등록/초기화 버튼 -->
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            <tbody>
						                            <tr>
						                                <td align="right">
						                                	<span class="button small blue" id="createbtn">등록</span>
						                                	<span class="button small blue" id="deletebtn">삭제</span>
						                                	<span class="button small blue" id="applybtn">즉시적용</span>
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
	        
	        <!-- 하단 로그인 사용자 정보 종료 -->
	    </td>
	 </tr>
</tbody>
</table>
</div>
</body>
</html>