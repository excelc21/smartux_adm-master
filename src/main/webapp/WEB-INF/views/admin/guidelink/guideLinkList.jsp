<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<title>LG U+ IPTV SmartUX</title>

<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
    $(document).ready(function() {

        $("#applyBtn").click(function(){
            var msg = "가이드링크을 상용에 적용하시겠습니까?";
            if(confirm(msg)){
                $.blockUI({
                    blockMsgClass: "ajax-loading",
                    showOverlay: true,
                    overlayCSS: { backgroundColor: '#CECDAD' } ,
                    css: { border: 'none' } ,
                     message: "<b>로딩중..</b>"
                });
                $.ajax({
                    url: '/smartux_adm/admin/guidelink/applyCache',
                    type: 'POST',
                    dataType: 'json',
                    data: {},
                    error: function(){
                        alert("작업 중 오류가 발생하였습니다");
                        $.unblockUI();
                    },
                    success: function(rs){
                        // 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
                        var flag = rs.flag;
                        var message = rs.message;
                        if(flag == "0000"){// 정상적으로 처리된 경우
                            alert("정상처리 되었습니다.");
                            $.unblockUI();
                        }else{
                            alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
                            $.unblockUI();
                        }
                    }
                });
            }
        });
        
        $('#deleteBtn').click(function(){
            var delList="";
            if(!$(".checkbox").is(":checked")){
                alert("삭제 할 가이드링크을 선택하세요.");
                return;
            }else{
                var statCnt = 0;
                $(".checkbox").each(function () {
                    if ($(this).is(":checked")) {
                        if(statCnt==0){
                            delList = delList+$(this).val();
                        }else{
                            delList = delList+","+$(this).val();
                        }
                        statCnt++;
                    }
                });
             }
            
            if(confirm("선택한 가이드링크을 삭제하시겠습니까?")) {  
                $.blockUI({
                    blockMsgClass: "ajax-loading",
                    showOverlay: true,
                    overlayCSS: { backgroundColor: '#CECDAD' } ,
                    css: { border: 'none' } ,
                     message: "<b>로딩중..</b>"
                });
                $.ajax({
                    url: './deleteGuideLink.do',
                    type:'POST',
                    dataType: 'json',
                    timeout : 30000,
                    data: {
                        "delList": delList
                    },
                    dataType:"json",
                    success:function(data){
                        $.unblockUI();
                        if(data.flag=="0000"){
                            alert("정상적으로 삭제되었습니다.");
                        }else{
                            alert("작업 중 오류가 발생하였습니다.\nflag : " + data.flag + "\ Message : " + data.message);
                        }
                    },
                    error:function(){
                        $.unblockUI();
                        alert("작업 중 오류가 발생하였습니다.");
                    },
                    complete:function(){
                        $(location).attr('href',"./guideLinkList.do"); 
                    }
                });
            } 
        });
    });

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
                                   가이드채널 링크관리
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
                                                <form id="form1" method="get" action="./guideLinkList.do">
                                                    <table border="0" cellpadding="0" cellspacing="0" width="100%" class="search_table">
                                                        <tbody>
                                                            <tr>
                                                                <td width="15">&nbsp;</td>
                                                                <td width="80"><img src="/smartux_adm/images/admin/search_title4.gif" border="0" height="47" width="62"></td>
                                                                <td>
                                                                    <table border="0" cellpadding="0" cellspacing="0" width="320">                                          
                                                                        <tbody>
                                                                        <tr><td>&nbsp;</td></tr>
                                                                        <tr>
                                                                            <td width="10">
                                                                                <img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9">
                                                                            </td>
                                                                            <td>
                                                                                <select class="select" name="type" id="type">
                                                                                    <option value="1" <c:if test="${type == '1'}">selected="selected"</c:if> >제목</option>
                                                                                    <option value="2" <c:if test="${type == '2'}">selected="selected"</c:if> >DCA</option>
                                                                                </select>  
                                                                                <input type="text" name="search_text" id="search_text" size="20" style="font-size: 12px;" onkeypress="if(event.keyCode==13){ doSearch();}" value="${search_text}">
                                                                            </td>
                                                                            <td width="66" align="left">
                                                                                <input src="/smartux_adm/images/admin/search.gif" border="0" height="22" type="image" width="65">
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
                                            <td>
                                                <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
                                                    <tbody>
                                                    
                                                    <tr>
                                                        <td height="25">
                                                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                                                <tbody>
                                                                <tr>
                                                                    <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
                                                                   <td class="bold">가이드채널 목록</td>
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
                                                            <th scope="col" width="5%">선택</th>
                                                            <th scope="col" width="5%">순번</th>
                                                            <th scope="col" width="30%">제목</th>
                                                            <th scope="col" width="10%">DCA코드</th>
                                                            <th scope="col" width="10%">링크타입</th>
                                                            <th scope="col" width="15%">등록일</th>
                                                            <th scope="col" width="15%">수정일</th>
                                                            <th scope="col" width="10%">사용여부</th>
                                                        </tr>
                                                        <c:if test="${list == '[]' }">
                                                        <tr align="center">
                                                            <td class="table_td_04" colspan="5">데이터가 존재 하지 않습니다.</td>                                                       
                                                        </tr>
                                                        </c:if>
                                                    </thead>
                                                    <tbody>
                                                    <c:forEach items="${list}" var="guide">
                                                        <tr align="center">
                                                            <td class="table_td_04"><input type="checkbox" class="checkbox" value="${guide.seq}" /></td>
                                                            <td class="table_td_04">${guide.rowno}</td>
                                                            <td class="table_td_04" align="left"><a href="./updateGuideLink.do?seq=${guide.seq}">${guide.title}</a></td>
                                                            <td class="table_td_04">${guide.dca}</td>
                                                            <td class="table_td_04">
                                                                <c:if test="${guide.linkType == '3'}">앱연동</c:if>
                                                                <c:if test="${guide.linkType == '4'}">지면</c:if>
                                                                <c:if test="${guide.linkType == '5'}">서비스연동</c:if>
                                                                <c:if test="${guide.linkType == '6'}">채널</c:if>
                                                            </td>
                                                            <td class="table_td_04">${guide.regDate}</td>
                                                            <td class="table_td_04">${guide.modDate}</td>
                                                            <td class="table_td_04">${guide.useYn}</td>
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
                                                            <jsp:include page="/WEB-INF/views/include/naviControll.jsp">
                                                                <jsp:param value="./guideLinkList.do" name="actionUrl"/>
                                                                <jsp:param value="?" name="naviUrl" />
                                                                <jsp:param value="${vo.pageNum }" name="pageNum"/>
                                                                <jsp:param value="${vo.pageSize }" name="pageSize"/>
                                                                <jsp:param value="${vo.blockSize }" name="blockSize"/>
                                                                <jsp:param value="${vo.pageCount }" name="pageCount"/>            
                                                            </jsp:include>
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                                <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
                                                    <tbody>
                                                    <tr>
                                                        <td align="right">
                                                            <a href="insertGuideLink.do"><span class="button small blue">등록</span></a>
                                                            <span class="button small blue" id="deleteBtn">삭제</span>
                                                            <c:if test="${auth_decrypt eq '00'}">
                                                            <span class="button small blue" id="applyBtn">즉시적용</span>
                                                            </c:if>
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