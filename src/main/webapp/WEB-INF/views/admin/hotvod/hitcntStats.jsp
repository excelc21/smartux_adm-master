<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<title>LG U+ IPTV</title>

<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<link href="/smartux_adm/css/anytime_style.css" rel="stylesheet" type="text/css">
<script src="/smartux_adm/js/anytime.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	AnyTime.picker( "startDt", { format: "%Y-%m-%d",  labelTitle: "시작일자"} );
	AnyTime.picker( "endDt", { format: "%Y-%m-%d",  labelTitle: "종료일자"} );
});

/**
 * 기간 선택
 */
function selPeriod(type){
	var startDt = "";
	var endDt = "";
	
	if(type == 1){
		//전일
		startDt = getCalculatedDate(0,0,-1,"-");
		endDt = getCalculatedDate(0,0,-1,"-");
	}else if(type == 2){
		//주간
		startDt = getCalculatedDate(0,0,-8,"-");
		endDt = getCalculatedDate(0,0,-1,"-");
	}else if(type == 3){
		//월간
		startDt = getCalculatedDate(0,0,-31,"-");
		endDt = getCalculatedDate(0,0,-1,"-");
	}
	$("#startDt").val(startDt);
	$("#endDt").val(endDt);
}

/*
 * 날짜 계산기
 */
function getCalculatedDate(iYear, iMonth, iDay, seperator){
	 //현재 날짜 객체를 얻어옴.
	 var gdCurDate = new Date();
	 //현재 날짜에 날짜 게산.
	 gdCurDate.setYear( gdCurDate.getFullYear() + iYear );
	 gdCurDate.setMonth( gdCurDate.getMonth() + iMonth );
	 gdCurDate.setDate( gdCurDate.getDate() + iDay );
	 
	 //실제 사용할 연, 월, 일 변수 받기.
	 var giYear = gdCurDate.getFullYear();
	 var giMonth = gdCurDate.getMonth()+1;
	 var giDay = gdCurDate.getDate();

	 //월, 일의 자릿수를 2자리로 맞춘다.
	 giMonth = "0" + giMonth;
	 giMonth = giMonth.substring(giMonth.length-2,giMonth.length);
	 giDay   = "0" + giDay;
	 giDay   = giDay.substring(giDay.length-2,giDay.length);

	 //display 형태 맞추기.
	 return giYear + seperator + giMonth + seperator +  giDay;
}

/**
 * 엑셀 다운로드
 */
function excelDown(){
	var startDt = $('#startDt').val().replace(/-/gi,'');
	var endDt = $('#endDt').val().replace(/-/gi,'');
	var pageSize = $('#pageSize').val();
	var sortOrder = '';
	
	if($('#sortOrder1').is(":checked")){
		sortOrder = $('#sortOrder1').val();
	}else if($('#sortOrder2').is(":checked")){
		sortOrder = $('#sortOrder2').val();
	}
	var param = "?startDt="+startDt+"&endDt="+endDt+"&pageSize="+pageSize+"&sortOrder="+sortOrder;
	location.href = "<%=webRoot%>/admin/hotvod/downloadHotvodExcelFile.do" + param;
	return false;
    
}

function doSearch(){
	$("form[name=form1]").submit(); //폼 전송
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
                                	<!-- 카테고리 타이틀 -->
                                    <!-- <img src="/smartux_adm/images/admin/category_map_title.gif">-->
                                   화제동영상 관리
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
											<form id="form1" method="get" action="./hitcntStats.do">
			                                <table border="0" cellpadding="0" cellspacing="0" width="100%" class="search_table" >
			                                	<tbody >
			                                	<tr >
				                                    <td width="15">&nbsp;</td>
				                                    <td width="80"><img src="/smartux_adm/images/admin/search_title4.gif" border="0" height="47" width="62"></td>
				                                    <td>
					                                    <table border="0" cellpadding="0" cellspacing="0" width="850">		                                    
		                                        			<tbody>
		                                        			<tr><td>&nbsp;</td></tr>
		                                        			<tr>
					                                            <td width="10">
					                                            	<img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9" />
					                                            </td>
					                                            <td>
					                                            	<a href="#" onclick="javascript:selPeriod(1);">전일</a>|<a href="#" onclick="javascript:selPeriod(2);">주간</a>|<a href="#" onclick="javascript:selPeriod(3);">월간</a>
					                                            </td>
					                                            <td>
					                                            	기간 <input type="text" name="startDt" id="startDt" size="10"  value="${fn:substring(vo.startDt,0,4)}-${fn:substring(vo.startDt,4,6)}-${fn:substring(vo.startDt,6,8)}" readonly="readonly"/> ~ 
					                                            	<input type="text" name="endDt" id="endDt" size="10"  value="${fn:substring(vo.endDt,0,4)}-${fn:substring(vo.endDt,4,6)}-${fn:substring(vo.endDt,6,8)}" readonly="readonly"/>
					                                            </td>
					                                            <td>
					                                            	범위(순위)
					                                                <select class="select" id="pageSize" name="pageSize">
					                                            		<option value="20" <c:if test="${vo.pageSize eq 20 }">selected="selected"</c:if>>20</option>
					                                            		<option value="100" <c:if test="${vo.pageSize eq 100 }">selected="selected"</c:if>>100</option>
					                                            		<option value="200" <c:if test="${vo.pageSize eq 200 }">selected="selected"</c:if>>200</option>
					                                            		<option value="300" <c:if test="${vo.pageSize eq 300 }">selected="selected"</c:if>>300</option>
					                                            		<option value="500" <c:if test="${vo.pageSize eq 500 }">selected="selected"</c:if>>500</option>
					                                            		<option value="1000" <c:if test="${vo.pageSize eq 1000 }">selected="selected"</c:if>>1000</option>
					                                            	</select>
					                                            </td>					
					                                            <td>
					                                            	<input type="radio" id="sortOrder1" name="sortOrder" value="I" <c:if test="${vo.sortOrder eq 'I'}">checked="checked"</c:if>>기간별 순
					                                            	<input type="radio" id="sortOrder2" name="sortOrder" value="E" <c:if test="${vo.sortOrder eq 'E'}">checked="checked"</c:if>>누적 순
					                                            </td>
					                                            <td width="66" align="left">
					                                                <input src="/smartux_adm/images/admin/search.gif" border="0" height="22" type="image" width="65"/>
					                                            </td>
					                                            <td>
					                                            	<img src="/smartux_adm/images/admin/excel.gif" style="margin-left:20px;cursor: pointer;" onclick="javascript:excelDown();"/>
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
							                                        <td class="bold">조회수 통계</td>
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
			                                			   <th scope="col" width="5%">TOP</th>
			                                			   <th scope="col" width="23%">컨텐츠명</th>
			                                			   <th scope="col" width="23%">동영상URL정보</th>
						                                   <th scope="col" width="9%">장르</th>
						                                   <th scope="col" width="7%">출처</th>
						                                   <th scope="col" width="11%">초기 조회수</th>
						                                   <th scope="col" width="11%">기간별 조회수</th>
						                                   <th scope="col" width="11%">누적 조회수</th>
						                                </tr>
						                                <c:if test="${list == '[]' }"> 
							                                <tr align="center">
							                                    <td class="table_td_04" colspan="7">데이터가 존재 하지 않습니다.</td>					                                    
							                                </tr>
														</c:if>
					                                </tbody>
					                                <c:forEach items="${list}" var="list">
						                            	<tr align="center">
						                            		<!-- 순위 -->
						                                    <td class="table_td_04" >
						                                    	${list.ranking }
						                                    </td>
						                                    <!-- 컨텐츠명 -->
						                                    <td class="table_td_04 " align="left">
						                                    	${list.content_name }
						                                    </td>
						                                    <!-- 컨텐츠url -->
						                                    <td class="table_td_04 ">
						                                    	${list.content_url }
						                                    </td>
						                                    <!-- 장르(카테고리명) -->
						                                    <td class="table_td_04" >
						                                    	${list.parent_cate }
						                                    </td>
						                                    <!-- 출처사이트 -->
															<td class="table_td_04 ">
																${list.site_name }
															</td>
															<!-- 초기 조회수 -->
															<td class="table_td_04 ">
																${list.s_hit_cnt }
															</td>
															<!-- 기간별 조회수 -->
															<td class="table_td_04 ">
																${list.i_hit_cnt }
															</td>
															<!-- 누적 조회수 -->
															<td class="table_td_04 ">
																${list.e_hit_cnt }
															</td>
						                                </tr>
					                            	</c:forEach>
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