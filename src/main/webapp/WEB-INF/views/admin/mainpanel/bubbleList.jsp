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
            var msg = "말풍선을 상용에 적용하시겠습니까?";
            if(confirm(msg)){
                $.blockUI({
                    blockMsgClass: "ajax-loading",
                    showOverlay: true,
                    overlayCSS: { backgroundColor: '#CECDAD' } ,
                    css: { border: 'none' } ,
                     message: "<b>로딩중..</b>"
                });
                $.ajax({
                    url: '/smartux_adm/admin/mainpanel/bubbleApplyCache.do',
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
                alert("삭제 할 말풍선을 선택하세요.");
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
            console.log(delList);
            
            if(confirm("선택한 말풍선을 삭제하시겠습니까?")) {  
                $.blockUI({
                    blockMsgClass: "ajax-loading",
                    showOverlay: true,
                    overlayCSS: { backgroundColor: '#CECDAD' } ,
                    css: { border: 'none' } ,
                     message: "<b>로딩중..</b>"
                });
                $.ajax({
                    url: './deleteBubble.do',
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
                        $(location).attr('href',"./getBubbleList.do"); 
                    }
                });
            } 
        });
    });

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
                                   말풍선 관리
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
                                                <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
                                                    <tbody>
                                                    
                                                    <tr>
                                                        <td height="25">
                                                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                                                <tbody>
                                                                <tr>
                                                                    <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
                                                                   <td class="bold">말풍선 목록</td>
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
                                                            <th scope="col" width="5%">번호</th>
                                                            <th scope="col" width="50%">제목</th>
                                                            <th scope="col" width="25%">기간</th>
                                                            <th scope="col" width="15%">메뉴ID</th>
                                                        </tr>
                                                        <c:if test="${list == '[]' }">
                                                        <tr align="center">
                                                            <td class="table_td_04" colspan="5">데이터가 존재 하지 않습니다.</td>                                                       
                                                        </tr>
                                                        </c:if>
                                                    </thead>
                                                    <tbody>
                                                    <c:forEach items="${list}" var="bubble">
                                                        <tr align="center">
                                                            <td class="table_td_04"><input type="checkbox" class="checkbox" value="${bubble.reg_no}" /></td>
                                                            <td class="table_td_04">${bubble.rowno}</td>
                                                            <td class="table_td_04" align="left"><a href="./updateBubble.do?reg_no=${bubble.reg_no}">${bubble.title}</a></td>
                                                            <c:choose>
                                                                <c:when test="${empty bubble.s_date || empty bubble.e_date}">
                                                                    <td class="table_td_04">무제한</td>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <td class="table_td_04">${bubble.s_date} ~ ${bubble.e_date}</td>
                                                                </c:otherwise>
                                                            </c:choose>
                                                            <td class="table_td_04">${bubble.link}</td>
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
                                                                <jsp:param value="./getBubbleList.do" name="actionUrl"/>
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
                                                            <a href="insertBubble.do"><span class="button small blue">등록</span></a>
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