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
    
    function fn_selectChnl(channel_no, channel_name, service_id, channel_id){
        
        var msg = channel_no + "번 " + channel_name;
        if(confirm(msg + " 채널을 선택하시겠습니까?")){

        
            if("${opener}" == "contents"){
                window.opener.jQuery("#chnl_service_id").val(service_id);
                window.opener.jQuery("#movepath").val(msg);
            } else if("${opener}" == "ads"){
                window.opener.jQuery("#linkUrl").val(channel_id);
                window.opener.jQuery("#textUrl").val(channel_id);
            } else if("${opener}" == "guidelink"){
                window.opener.jQuery("#link").val(service_id);
            } else{
                window.opener.jQuery("#promotion_chnl").val(service_id);
                window.opener.jQuery("#promotion_chnl_info").val(msg);
            }
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
                                    채널 등록
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
                                                                    <td class="bold">채널 목록</td>
                                                                </tr>
                                                                </tbody>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                                <form id="srchfrm" name="srchfrm" method="post" action="<%=webRoot%>/admin/gpack/event/getGpackPromotionChannelView.do">
                                                    <input type="hidden" id="pageNum" name="pageNum" value="${pageNum}" />
                                                    <input type="hidden" id="pageSize" name="pageSize" value="${pageSize}" />
                                                    <input type="hidden" id="blockSize" name="blockSize" value="${blockSize}" />
                                                    <input type="hidden" id="opener" name="opener" value="${opener}" />
                                                <table border="0" cellpadding="0" cellspacing="0" style="width:60%;" class="board_data">
                                                    <tbody>
                                                    <tr>
                                                        <th width="20%">채널명</th>
                                                        <td width="50%">
                                                            &nbsp;<input type="text" id="srch_channel_name" name="srch_channel_name" value="${srch_channel_name}" size="25" style="font-size: 12px;" />
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
                                                        <th width="20%">채널번호</th>
                                                        <th>채널명</th>
                                                        <th width="20%">서비스ID</th>
                                                    </tr>
                                                    <c:choose>
                                                        <c:when test="${tv_list.list==null || fn:length(tv_list.list) == 0}">
                                                            <tr>
                                                                <td colspan="3" class="table_td_04">검색된 채널이 없습니다</td>
                                                            </tr>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:forEach var="item" items="${tv_list.list}" varStatus="status">
                                                                <tr>
                                                                    <td class="table_td_04"><a href="javascript:fn_selectChnl('${item.channel_no}', '${item.channel_name}', '${item.service_id}', '${item.channel_id}');">${item.channel_no}</a></td>
                                                                    <td class="table_td_04"><a href="javascript:fn_selectChnl('${item.channel_no}', '${item.channel_name}', '${item.service_id}', '${item.channel_id}');">${item.channel_name}</a></td>
                                                                    <td class="table_td_04"><a href="javascript:fn_selectChnl('${item.channel_no}', '${item.channel_name}', '${item.service_id}', '${item.channel_id}');">${item.service_id}</a></td>
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
                                                                <jsp:param value="/smartux_adm/admin/gpack/event/getGpackPromotionChannelView.do" name="actionUrl"/>
                                                                <jsp:param value="?pack_id=${pack_id}&chnl_grp_id=${chnl_grp_id}&srch_channel_name=${srch_channel_name}" name="naviUrl" />
                                                                <jsp:param value="${tv_list.pageNum}" name="pageNum"/>
                                                                <jsp:param value="${tv_list.pageSize}" name="pageSize"/>
                                                                <jsp:param value="${tv_list.blockSize}" name="blockSize"/>
                                                                <jsp:param value="${tv_list.pageCount}" name="pageCount"/>            
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