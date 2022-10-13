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
    
});
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
                                <td style="padding-top: 0px">
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
                                                                    <td width="15" style="padding: 15px 0px"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
                                                                    <c:if test="${imcs_type == 'ALB'}">
                                                                         <td class="bold">앨범 경로</td>
                                                                    </c:if>
                                                                    <c:if test="${imcs_type == 'CAT'}">
                                                                         <td class="bold">카테고리 경로</td>
                                                                    </c:if>
                                                                </tr>
                                                                </tbody>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                                
                                                
                                                <table id="data_list" border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list">
                                                    <thead>
                                                    </thead> 
                                                    <tbody>
                                                        <c:forEach items="${list}" var="test">
                                                            <tr align="left">
                                                                <td style="width: 70px; background-color: gainsboro;">연동경로</td>
                                                                <td class="table_td_04" style="text-align: left; padding-left: 10px;">${test}</td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
                                                
                                                <table border="0" cellpadding="0" cellspacing="0" width="100%" style="padding-top: 30px;">
                                                    <tbody>
                                                    <tr>
                                                        <td align="center">
                                                            <span class="button small blue" id="closebtn" onclick="self.close()">닫기</span>
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