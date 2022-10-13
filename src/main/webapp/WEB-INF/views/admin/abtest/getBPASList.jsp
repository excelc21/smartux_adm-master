<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<title>LG U+ IPTV SmartUX</title>

<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<link href="/smartux_adm/css/anytime_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script src="/smartux_adm/js/anytime.js"></script>
<script type="text/javascript">
$(document).ready(function() {
    AnyTime.picker('startDt', { format: '%z-%m-%d', labelTitle: '날짜', labelHour: '시간', time: ''});
    AnyTime.picker('endDt', { format: '%z-%m-%d', labelTitle: '날짜', labelHour: '시간', time: ''});

    $('.searchBtn').click(function(){

        if($('#startDt').val() > $('#endDt').val()) {
            alert("시작일은 종료일보다 빨라야합니다.");
            return false;
        }
        
        var mims_type_arr = "";
        if($('#mims_type_pap').is(":checked") == true){
            mims_type_arr = "PAP";
        }
        if($('#mims_type_ban').is(":checked") == true){
            mims_type_arr += ",BAN";
        }
        if($('#mims_type_hot').is(":checked") == true){
            mims_type_arr += ",HOT";
        }
        if($('#mims_type_sum').is(":checked") == true){
            mims_type_arr += ",SUM";
        }

        $('#mims_type_arr').val(mims_type_arr);
        
        if(mims_type_arr == ''){
            alert("타입을 선택해주세요.");
            return false;
        }

        if($('#findType').val() != '' && $('#findValue').val() == ''){
            alert("검색어를 입력해주세요.");
            return false;
        }

        $('#bpasForm').attr("action", "./getBPASList.do")
        $('#bpasForm').submit();
    })

    $('#excelDownBtn').click(function(){
        var mims_type_arr = "";
        if($('#mims_type_pap').is(":checked") == true){
            mims_type_arr = "PAP";
        }
        if($('#mims_type_ban').is(":checked") == true){
            mims_type_arr += ",BAN";
        }
        if($('#mims_type_hot').is(":checked") == true){
            mims_type_arr += ",HOT";
        }
        if($('#mims_type_sum').is(":checked") == true){
            mims_type_arr += ",SUM";
        }

        $('#mims_type_arr').val(mims_type_arr);
        
        if(mims_type_arr == ''){
            alert("타입을 선택해주세요.");
            return false;
        }

        $('#bpasForm').attr("action", "./downloadExcelFile.do")
        $('#bpasForm').submit();
    })
    
});

function moveDetail(mims_type, mims_id){
    if(mims_type == "PAP"){
        var url = '<%=webRoot%>/admin/mainpanel/getPanelTitleTempList.do';
        window.open(url, 'pap', 'width=1200,height=900');
    }else if(mims_type == "BAN"){
        var url = '<%=webRoot%>/admin/ads/getUpdateAdsView.do?num='+mims_id+'&scr_tp=L&adsListMode=total';
        window.open(url, 'ban', 'width=1200,height=900');
    }else if(mims_type == "SUM"){
        var url = '<%=webRoot%>/admin/schedule/viewSchedule.do?schedule_code='+mims_id;
        window.open(url, 'sum', 'width=1200,height=900');
    }else if(mims_type == "HOT"){
        var url = '<%=webRoot%>/admin/hotvod/subDetail.do?content_id='+mims_id;
        window.open(url, 'hot', 'width=1200,height=900');
    }
}


function showAlbumCategory(imcs_type, imcs_id){
    var popupWidth = 750;
    
    var popupHeight = 750;
    if(imcs_type == "CAT"){
        popupHeight = 200;
    }
    
    var popupX = (window.screen.width / 2) - (popupWidth / 2);
    var popupY= (window.screen.height / 2) - (popupHeight / 2);
    window.open("/smartux_adm/admin/abtest/getAlbumCategoryInfo.do?imcs_type="+imcs_type+"&imcs_id="+imcs_id, "regcode", "width="+popupWidth+",height="+popupHeight+",left="+ popupX + ", top="+ popupY);
}

function panelTitleInfo(mims_type, mims_id, imcs_type, imcs_id){

    mims_id = mims_id.replace("|", "%7C");
    
    var popupWidth = 750;
    
    var popupHeight = 500;
    
    var popupX = (window.screen.width / 2) - (popupWidth / 2);
    var popupY= (window.screen.height / 2) - (popupHeight / 2);
    window.open("/smartux_adm/admin/abtest/getPanelTitleInfo.do?mims_type="+mims_type+"&mims_id="+mims_id+"&imcs_type="+imcs_type+"&imcs_id="+imcs_id, "regcode", "width="+popupWidth+",height="+popupHeight+",left="+ popupX + ", top="+ popupY);
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
                                   BPAS 편성 관리
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
                                            <td class="3_line" height="1"></td>
                                        </tr>
                                        <tr>
                                            <td height="20"></td>
                                        </tr>
                                        <!-- 리스트 시작 -->
                                        <tr>
                                            <td>
                                                <!-- 검색 시작 -->
                                                <form id="bpasForm" name="bpasForm" action="./getBPASList.do">
                                                    <table border="0" cellpadding="0" cellspacing="0" width="100%" class="search_table">
                                                        <tbody>
                                                            <tr>
                                                                <td width="15">&nbsp;</td>
                                                                <td width="80"><img src="/smartux_adm/images/admin/search_title4.gif" border="0" height="47" width="62"></td>
                                                                <td>
                                                                    <table border="0" cellpadding="0" cellspacing="0">
                                                                        <tr style="height: 30px;">
                                                                            <td colspan="1"><img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9" /> 날짜 </td>
                                                                            <td colspan="5">
                                                                                    <input type="text" id="startDt" name="startDt" size="14" title="시작일" style="text-align: center" value="${vo.startDt}" />~
                                                                                    <input type="text" id="endDt" name="endDt" size="14" title="종료일" style="text-align: center" value="${vo.endDt}" />
                                                                            </td>
                                                                            
                                                                            <td width="30"></td>
                                                                            
                                                                            <td width="100"><img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9" /> AB연동여부 </td>
                                                                            <td colspan="4">
                                                                                <input type="radio" id="ab_yn_a" name="ab_yn" checked value="ALL" <c:if test="${vo.ab_yn == 'ALL'}">checked</c:if> /> <label for="ab_yn_a">전체</label> 
                                                                                <input type="radio" id="ab_yn_y" name="ab_yn" value="Y" <c:if test="${vo.ab_yn == 'Y'}">checked</c:if>/> <label for="ab_yn_y">연동</label> 
                                                                                <input type="radio" id="ab_yn_n" name="ab_yn" value="N" <c:if test="${vo.ab_yn == 'N'}">checked</c:if>/> <label for="ab_yn_n">미연동</label>
                                                                            </td>
                                                                        </tr>
                                                                        <tr style="height: 30px;">
                                                                           <td colspan="1"><img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9" /> 타입선택</td>
                                                                           <td colspan="4">
                                                                                <input type="checkbox" id="mims_type_pap" name="mims_type_pap" value="Y" <c:if test="${mims_type_pap == 'Y'}">checked</c:if> /> <label for="mims_type_pap">지면</label>
                                                                                <input type="checkbox" id="mims_type_ban" name="mims_type_ban" value="Y" <c:if test="${mims_type_ban == 'Y'}">checked</c:if>/> <label for="mims_type_ban">배너</label>
                                                                                <input type="checkbox" id="mims_type_sum" name="mims_type_sum" value="Y" <c:if test="${mims_type_sum == 'Y'}">checked</c:if>/> <label for="mims_type_sum">자체편성</label>
                                                                                <input type="checkbox" id="mims_type_hot" name="mims_type_hot" value="Y" <c:if test="${mims_type_hot == 'Y'}">checked</c:if>/> <label for="mims_type_hot">화제동영상</label>
                                                                                <input type="hidden" id="mims_type_arr" name="mims_type_arr"/>
                                                                            </td>
                                                                        </tr>
                                                                        <tr style="height: 30px;">
                                                                            <td colspan="1"><img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9" /> 검색</td>
                                                                           <td colspan="4">
                                                                               <select class="select" id="findType" name="findType">
                                                                                   <option value="">선택</option>
                                                                                   <option <c:if test="${vo.findType == '01'}">selected="selected"</c:if> value="01">앨범ID</option>
                                                                                   <option <c:if test="${vo.findType == '02'}">selected="selected"</c:if> value="02">앨범명</option>
                                                                                   <option <c:if test="${vo.findType == '03'}">selected="selected"</c:if> value="03">카테고리ID</option>
                                                                                   <option <c:if test="${vo.findType == '04'}">selected="selected"</c:if> value="04">카테고리명</option>
                                                                               </select>
                                                                               <input type="text" id="findValue" name="findValue" value="${vo.findValue }" size="20" style="font-size: 12px;" />
                                                                            </td>
                                                                            <%-- <td colspan="2">
                                                                                <input type="text" name="findValue" value="${vo.findValue }" size="20" style="font-size: 12px;" />
                                                                            </td> --%>
                                                                            
                                                                            <td>&nbsp;</td>
                                                                            <td width="66" align="left">
                                                                                <input src="/smartux_adm/images/admin/search.gif" border="0" height="22" type="image" width="65" class="searchBtn">
                                                                            </td>
                                                                        </tr>
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
                                            <td>
                                                <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
                                                    <tbody>
                                                    
                                                    <tr>
                                                        <td height="25">
                                                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                                                <tbody>
                                                                <tr>
                                                                    <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
                                                                   <td class="bold">BPAS 관리</td>
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
                                                            <th scope="col" width="7%">타입</th>
                                                            <th scope="col" width="15%">메뉴명</th>
                                                            <th scope="col" width="15%">앨범명</th>
                                                            <th scope="col" width="7%">앨범ID</th>
                                                            <th scope="col" width="7%">회차</th>
                                                            <th scope="col" width="7%">시리즈명</th>
                                                            <th scope="col" width="7%">시리즈ID</th>
                                                            <th scope="col" width="15%">카테고리명</th>
                                                            <th scope="col" width="7%">카테고리ID</th>
                                                            <th scope="col" width="7%">AB여부</th>
                                                            <th scope="col" width="7%">등록일자</th>
                                                        </tr>
                                                        <c:if test="${list == '[]' }">
                                                        <tr align="center">
                                                            <td class="table_td_04" colspan="11">데이터가 존재 하지 않습니다.</td>                                                       
                                                        </tr>
                                                        </c:if>
                                                    </thead>
                                                    <tbody>
                                                    <c:forEach items="${list}" var="bpas">
                                                        <tr align="center">
                                                        
                                                            <c:choose>
                                                                <c:when test="${bpas.mims_type == 'PAP'}">
                                                                    <td class="table_td_04"><a href='/smartux_adm/admin/mainpanel/getPanelTitleTempList.do' target="_blank" style="text-decoration: underline;">지면</a></td>
                                                                </c:when>
                                                                <c:when test="${bpas.mims_type == 'BAN'}">
                                                                    <td class="table_td_04"><a href='/smartux_adm/admin/ads/getAdsList.do?scr_tp=L' target="_blank" style="text-decoration: underline;">배너</a></td>
                                                                </c:when>
                                                                <c:when test="${bpas.mims_type == 'SUM'}">
                                                                    <td class="table_td_04"><a href='/smartux_adm/admin/schedule/getScheduleList.do' target="_blank" style="text-decoration: underline;">자체편성</a></td>
                                                                </c:when>
                                                                <c:when test="${bpas.mims_type == 'HOT'}">
                                                                    <td class="table_td_04"><a href='/smartux_adm/admin/hotvod/contentList.do' target="_blank" style="text-decoration: underline;">화제동영상</a></td>
                                                                </c:when>
                                                            </c:choose>
                                                        
                                                        
                                                            <td class="table_td_04"><a onclick="moveDetail('${bpas.mims_type}', '${bpas.mims_id}')" style="cursor:pointer; text-decoration: underline;">${bpas.title}</a></td>
                                                        
                                                            <c:choose>
                                                                <c:when test="${bpas.imcs_type == 'ALB'}">
                                                                    <td class="table_td_04" align="left"><a onclick="showAlbumCategory('${bpas.imcs_type}', '${bpas.imcs_id}')" style="cursor:pointer; text-decoration: underline;">${bpas.album_name}</a></td>
                                                                    <td class="table_td_04"><a onclick="showAlbumCategory('${bpas.imcs_type}', '${bpas.imcs_id}')" style="cursor:pointer; text-decoration: underline;">${bpas.imcs_id}</a></td>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <td class="table_td_04"></td>
                                                                    <td class="table_td_04"></td>
                                                                </c:otherwise>
                                                            </c:choose>
                                                            
                                                            <td class="table_td_04"></td>
                                                            <td class="table_td_04"></td>
                                                            <td class="table_td_04"></td>
                                                            <c:choose>
                                                                <c:when test="${bpas.imcs_type == 'CAT'}">
                                                                    <td class="table_td_04" align="left"><a onclick="showAlbumCategory('${bpas.imcs_type}', '${bpas.imcs_id}')" style="cursor:pointer; text-decoration: underline;">${bpas.category_name}</a></td>
                                                                    <td class="table_td_04"><a onclick="showAlbumCategory('${bpas.imcs_type}', '${bpas.imcs_id}')" style="cursor:pointer; text-decoration: underline;">${bpas.imcs_id}</a></td>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <td class="table_td_04"></td>
                                                                    <td class="table_td_04"></td>
                                                                </c:otherwise>
                                                            </c:choose>
                                                            
                                                            <c:choose>
                                                                <c:when test="${bpas.ab_yn == 'N'}">
                                                                    <td class="table_td_04">N</td>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <td class="table_td_04"><a onclick="panelTitleInfo('${bpas.mims_type}', '${bpas.mims_id}', '${bpas.imcs_type}', '${bpas.imcs_id}')" style="cursor:pointer; text-decoration: underline;">${bpas.ab_yn}</a></td>
                                                                </c:otherwise>
                                                            </c:choose>
                                                            
                                                            <td class="table_td_04">${bpas.reg_date}</td>
                                                            
                                                            
                                                        </tr>
                                                    </c:forEach>
                                                    </tbody>
                                                </table>
                                                
                                                <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                                    <tbody>
                                                    <tr>
                                                        <td height="5"></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="center">
                                                            
                                                            <c:set value="?" var="tmpParams"/>
                                                            <c:if test="${not empty vo.startDt}">
                                                                <c:set value="${tmpParams}&startDt=${vo.startDt}" var="tmpParams"/>
                                                            </c:if>
                                                            <c:if test="${not empty vo.endDt}">
                                                                <c:set value="${tmpParams}&endDt=${vo.endDt}" var="tmpParams"/>
                                                            </c:if>
                                                            <c:if test="${not empty vo.ab_yn}">
                                                                <c:set value="${tmpParams}&ab_yn=${vo.ab_yn}" var="tmpParams"/>
                                                            </c:if>


                                                            <c:if test="${not empty vo.mims_type_arr}">
                                                                <c:set value="${tmpParams}&mims_type_arr=${vo.mims_type_arr}" var="tmpParams"/>
                                                            </c:if>
                                                            <c:if test="${not empty vo.findType}">
                                                                <c:set value="${tmpParams}&findType=${vo.findType}" var="tmpParams"/>
                                                            </c:if>
                                                            <c:if test="${not empty vo.findValue}">
                                                                <c:set value="${tmpParams}&findValue=${vo.findValue}" var="tmpParams"/>
                                                            </c:if>
                                                        
                                                            <jsp:include page="/WEB-INF/views/include/naviControll.jsp">
                                                                <jsp:param value="getBPASList.do" name="actionUrl"/>
                                                                <jsp:param value="${tmpParams}" name="naviUrl"/>
                                                                <jsp:param value="${vo.pageNum}" name="pageNum"/>
                                                                <jsp:param value="${vo.pageSize}" name="pageSize"/>
                                                                <jsp:param value="${vo.blockSize}" name="blockSize"/>
                                                                <jsp:param value="${vo.pageCount}" name="pageCount"/>
                                                            </jsp:include>
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                                
                                                <!-- 엑셀 다운로드 버튼 -->
                                                <c:if test="${loginUser == 'amims' or loginUser == 'admin'}">
                                                    <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" style="margin-top:10px">
                                                        <tr>
                                                            <td align="right">
                                                                <span class="button small blue" id="excelDownBtn">엑셀 다운로드</span>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </c:if>
                                                
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