<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>광고 마스터 선택</title>
    <link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
    <jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
    
<script type="text/javascript">
$(document).ready(function () {
    $("#closebtn").click(function () {
        self.close();
    });
});

function slt(sltid){
	var ay = sltid.split("|");
	var dataobj = new Object(); 
	dataobj.ads_id =ay[0];
	dataobj.ads_nm =ay[1];
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
                    <td width="15">&nbsp;<img src="/smartux_adm/images/admin/blt_07.gif"></td>
                    <td class="bold">광고 마스터 선택</td>
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
         <th scope="col" width="200">광고 마스터 아이디</th>
         <th scope="col" width="300">광고 마스터명</th>
         <th scope="col" width="60">선택</th>
      </tr>
   </tbody>
   
   <c:forEach var="item" items="${itemList}" varStatus="status">
   <tr align="center" onMouseOver="this.style.backgroundColor='#d0e8fd'" onMouseOut="this.style.backgroundColor=''">      				                                    						                                    
      <td align=center>
      	${item.ADS_ID}	 
      </td>
       <td align=left>
        &nbsp;${item.ADS_NM}	 
      </td>
      <td class="table_td_04" >
         <span class="button small blue" onclick="slt('${item.ADS_ID}|${item.ADS_NM}')">선택</span>
      </td>	
   </tr>
   </c:forEach>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
   <tr >      				                                    						                                    
      <td align="center" height=40>
         <span class="button small blue" id="closebtn">닫기</span>				                                    
      </td>
   </tr>
</table>
<p>&nbsp;</p>