<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">

</script>
<body leftmargin="0" topmargin="0">

<!-- ######################## body start ######################### -->
<table border="0" cellpadding="15" cellspacing="0" width="100%" align="center" >
    <tbody>
    <tr>
        <td>
            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" >
                <tbody>
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
                                           <td class="bold">AB연동 정보</td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>                                                                                                               
                            </tr>
                            </tbody>
                        </table>
                        
                        <table id="data_list" border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list"> 
                            <thead>
                                <tr align="center">
                                    <th scope="col" width="20%">테스트ID</th>
                                    <th scope="col" width="40%">테스트명</th>
                                    <th scope="col" width="20%">패널ID</th>
                                    <th scope="col" width="20%">만료기간</th>
                                </tr>
                                <c:if test="${list == '[]' }">
                                <tr align="center">
                                    <td class="table_td_04" colspan="5">데이터가 존재 하지 않습니다.</td>                                                       
                                </tr>
                                </c:if>
                            </thead>
                            <tbody>
                            <c:forEach items="${list}" var="info">
                                <tr align="center">
                                    <td class="table_td_04">${info.test_id}</td>
                                    <td class="table_td_04">${info.test_name}</td>
                                    <td class="table_td_04">${info.pannel_id}</td>
                                    <td class="table_td_04">${info.end_date_time}</td>
                                </tr>
                            </c:forEach>
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

</body>
</html>