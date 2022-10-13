<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
	
	$(document).ready(function(){
		
		// ajax 작업시 캐쉬를 사용하지 않도록 한다
		$.ajaxSetup({ cache: false });
		
		// 검색
		$("#searchbtn").click(function(){
			$("#pageNum").val("1");
			$("#srchfrm").submit();
		});
	});
	
	function fn_selectProduct(produt_id, product_name){
		
		if(confirm(product_name + " 상품을 선택하시겠습니까?")){

			window.opener.jQuery("#product_id").val(produt_id);
			window.opener.jQuery("#movepath").val(product_name);
			self.close();
		}

	}
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
                <tr style="display:block">
                    <td height="42" width="100%">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tbody>
                            <tr>
                                <td width="400" class="boldTitle">
                                	<!-- 카테고리 타이틀 -->
                                    <!-- <img src="/smartux_adm/images/admin/category_map_title.gif">-->
                                    월정액 등록
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
						                                            <td class="bold">월정액 상품 목록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                       	<form id="srchfrm" name="srchfrm" method="post" action="<%=webRoot%>/admin/gpack/event/getGpackPromotionProductView.do">
						                       		<input type="hidden" id="pageNum" name="pageNum" value="${pageNum}" />
						                       		<input type="hidden" id="pageSize" name="pageSize" value="${pageSize}" />
						                       		<input type="hidden" id="blockSize" name="blockSize" value="${blockSize}" />
						                       	<table border="0" cellpadding="0" cellspacing="0" style="width:60%;" class="board_data">
			                                        <tbody>
			                                        <tr>
														<th width="20%">상품명</th>
			                                            <td width="50%">
															&nbsp;<input type="text" id="srch_product_name" name="srch_product_name" value="${srch_product_name}" size="25" style="font-size: 12px;" />
			                                            </td>
			                                            <td width="30%" align="left">
			                                            	<input id="searchbtn" src="/smartux_adm/images/admin/search.gif" border="0" height="22" type="image" width="65">
			                                            </td>
			                                        </tr>
			                                    	</tbody>
			                                    </table>
			                                    </form>
			                                    <table border="0" cellpadding="0" cellspacing="0" style="width:100%;">
			                                        <tr>
			                                            <td height="5"> </td>
			                                        </tr>
			                                    </table>
						                       	<table border="0" cellspacing="0" cellpadding="0" class="board_list" style="width:98%">
													<tr>
														<th width="20%">상품ID</th>
														<th>상품명</th>
														<th width="20%">가격</th>
													</tr>
													<c:choose>
														<c:when test="${result.list==null || fn:length(result.list) == 0}">
															<tr>
																<td colspan="3" class="table_td_04">검색된 상품이 없습니다</td>
															</tr>
														</c:when>
														<c:otherwise>
															<c:forEach var="item" items="${result.list}" varStatus="status">
																<tr>
																	<td class="table_td_04"><a href="javascript:fn_selectProduct('${item.product_id}', '${item.product_name}');">${item.product_id}</a></td>
																	<td class="table_td_04"><a href="javascript:fn_selectProduct('${item.product_id}', '${item.product_name}');">${item.product_name}</a></td>
																	<td class="table_td_04"><a href="javascript:fn_selectProduct('${item.product_id}', '${item.product_name}');">${item.price}</a></td>
																</tr>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</table>
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
						                            <tbody>
						                            <tr>
						                                <td height="5"></td>
						                            </tr>
						                            <tr>
						                            	<td align="center">
						                            		<jsp:include page="/WEB-INF/views/include/gpack_naviControll.jsp">
																<jsp:param value="/smartux_adm/admin/gpack/event/getGpackPromotionProductView.do" name="actionUrl"/>
																<jsp:param value="?pack_id=${pack_id}&srch_product_name=${srch_product_name_encode}" name="naviUrl" />
																<jsp:param value="${result.pageNum}" name="pageNum"/>
																<jsp:param value="${result.pageSize}" name="pageSize"/>
																<jsp:param value="${result.blockSize}" name="blockSize"/>
																<jsp:param value="${result.pageCount}" name="pageCount"/>			  
															</jsp:include>
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
</tbody>
</table>
</div>

</body>
</html>