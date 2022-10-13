<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>패널UI타입 선택</title>
    <link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
    <jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
    
<script type="text/javascript">
$(document).ready(function () {
    /* $('#masterID').change(function () {
        var masterID = $('#masterID').val();
        location.href = '/hdtv_adm/admin/videolte/common/getBannerAdList.do?s_ads_id=' + masterID;
    }); */    
    
    $("#closebtn").click(function () {
        self.close();
    });
});

function slt(sltid){
	var dataobj = new Object(); 
	dataobj.frame_type_code =sltid;
	var callbak_m = eval("opener."+"${param.callbak}");    
	callbak_m(dataobj);
	self.close();
}

</script>
	
<body leftmargin="0" topmargin="0">
<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
    <tbody>
    <tr>
        <td height="25">
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                <tbody>
                <tr>
                    <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
<!-- 2019.11.04 : ${type_nm}선택 -> UI타입 선택 으로 수정 - 이태광 Start -->                    
                    <td class="bold">UI타입 선택</td>
<!-- 2019.11.04 : ${type_nm}선택 -> UI타입 선택 으로 수정 - 이태광 End -->                    
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    </tbody>
</table>
<table border="0" cellpadding="0" cellspacing="0" class="board_list">
   <tbody>
      <tr align="center">
        <th width="150" scope="col">이미지</th>
        <th width="80" scope="col">코드</th>  
<!-- 2019.11.04 : ${type_nm}명 -> UI타입 명 으로 수정 - 이태광 Start -->              
        <th width="211" height="26" scope="col">UI타입 명</th>
<!-- 2019.11.04 : ${type_nm}명 -> UI타입 명 으로 수정 - 이태광 End -->        
        <th width="60" scope="col">선택</th>
     </tr>
   </tbody>
   <c:forEach var="item" items="${itemList}" varStatus="status">
   <tr onMouseOver="this.style.backgroundColor='#d0e8fd'" onMouseOut="this.style.backgroundColor=''">
  	  <c:if test="${item.frame_type  eq '30' }">   
      <td align="center"><img src="${img_path_url}${item.img_file}" width="250" /></td>
      </c:if>      
      <td height="50" align=center >
			<c:choose>
				<c:when test="${item.data_type eq '000'}">
					${item.frame_type_code}
					<br><p style="font-size:8pt"><font color=red>지정된 DATA TYPE 없음</font></p>
				</c:when>
				<c:otherwise>
					<strong>${item.frame_type_code}</strong>
				</c:otherwise>							         
			</c:choose>      	
      </td>
      <td height="50" align=left >
<!-- 2019.09.10 : 길이 체크를 위해 함수 추가 - 이태광 Start -->	
			<c:set var="list_cnt" value="0" />
       		<c:if test="${fn:length(item.frame_nm) > 20}">
       			<c:if test="${(fn:length(item.frame_nm) % 20) != 0}">
       				<c:set var="list_cnt" value="${(fn:length(item.frame_nm))/20+1}" />
       			</c:if>
       			<c:if test="${(fn:length(item.frame_nm) % 20) == 0}">
       				<c:set var="list_cnt" value="${(fn:length(item.frame_nm) / 20)}" />
       			</c:if>

       			<c:forEach begin="1" end="${list_cnt}" var="cnt">
       				<c:if test="${(cnt*20) >= (fn:length(item.frame_nm))}">
       					${fn:substring(item.frame_nm, (cnt-1)*20, fn:length(item.frame_nm))}
       				</c:if>
       				<c:if test="${(cnt*20) < (fn:length(item.frame_nm))}">
       					${fn:substring(item.frame_nm, (cnt-1)*20, (20 * cnt))}<br>
       				</c:if>						                                    				
       			</c:forEach>
       		</c:if>
       		<c:if test="${fn:length(item.frame_nm) <= 20}">
       			${item.frame_nm}
       		</c:if>
<!-- 2019.09.10 : 길이 체크를 위해 함수 추가 - 이태광 End -->
      </td>
      <td  align=center><span class="button small blue" onclick="slt('${item.frame_type_code}')">선택</span>	</td>
   </tr>
   </c:forEach>
</table>
<br>
<center>
<span class="button small blue" id="closebtn">닫기</span>
</center>
<p>&nbsp;</p>