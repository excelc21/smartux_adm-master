<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<link href="/smartux_adm/css/anytime_style.css" rel="stylesheet" type="text/css">
<script src="/smartux_adm/js/anytime.js"></script>
<script type="text/javascript">

    var type = '${type}';

    //등록
    function goList() {
        $(location).attr('href', "./guideLinkList.do");
    }
    
    //등록
    function goInsert() {

        if($('#title').val() == ''){
            alert('제목을 입력해주세요.');
            $('#title').focus();
            return;
        }

        if($('#dca').val() == ''){
            alert('DCA코드를 입력해주세요.');
            $('#dca').focus();
            return;
        }

        var linkType = $('#linkType').val();
        if(linkType == "3" || linkType == "5"){            //앱연동
            var link = $('#link').val();
            var detailLink = $('#detailLink').val();

            if($('#link').val() == ''){
                alert('링크정보를 입력해주세요.');
                $('#link').focus();
                return;
            }
        }else{       
            if(linkType == "4"){
                if($('#link').val() == ''){
                    alert('지면을 선택해주세요.');
                    $('#link').focus();
                    return;
                }
            }else if(linkType == "6"){
                if($('#link').val() == ''){
                    alert('채널을 선택해주세요.');
                    $('#link').focus();
                    return;
                }
            }                 
            
        }
        


        var formData = new FormData();
        formData.append("seq", $('#seq').val()); 
        formData.append("title", $('#title').val()); 
        formData.append("dca", $('#dca').val()); 
        formData.append("linkType", $('#linkType').val()); 
        formData.append("link", $('#link').val()); 
        formData.append("detailLink", $('#detailLink').val());
        formData.append("useYn", $(':input:radio[name="useYn"]:checked').val());
         

        if(type == "insert"){
            if(confirm("등록하시겠습니까?")) {
                $.blockUI({
                    blockMsgClass: "ajax-loading",
                    showOverlay: true,
                    overlayCSS: { backgroundColor: '#CECDAD' } ,
                    css: { border: 'none' } ,
                     message: "<b>로딩중..</b>"
                });

                $.ajax({
                    url: "./insertGuideLink.do",
                    type: "POST",
                    data: formData,
                    processData: false,
                    contentType: false,
                    async: true,
                    dataType : "json",
                    success: function (rtn) {
                        if (rtn.flag=="0000") {
                            alert("가이드채널이 등록되었습니다.");
                            goList();
                        } else if(rtn.flag == "5555"){
                            alert("이미 동일한 DCA코드가 등록되어있습니다.\n\nDCA코드를 확인해주세요.");
                        } else {
                            alert("작업 중 오류가 발생하였습니다\nflag : " + rtn.flag + "\ Message : " + rtn.message);
                        }
                        $.unblockUI();
                    },
                    complete: function () {
                    },
                    error: function (e) {                   
                        alert("일시적인 장애가 발생하였습니다. 다시 시도해 주세요.");
                        $.unblockUI();
                    }
                });
            }
        }else if(type == "update"){
            if(confirm("수정하시겠습니까?")) {
                $.blockUI({
                    blockMsgClass: "ajax-loading",
                    showOverlay: true,
                    overlayCSS: { backgroundColor: '#CECDAD' } ,
                    css: { border: 'none' } ,
                     message: "<b>로딩중..</b>"
                });

                $.ajax({
                    url: "./updateGuideLink.do",
                    type: "POST",
                    data: formData,
                    processData: false,
                    contentType: false,
                    async: true,
                    dataType : "json",
                    success: function (rtn) {
                        if (rtn.flag=="0000") {
                            alert("가이드링크가 수정되었습니다.");
                            goList();
                        } else {
                            alert("작업 중 오류가 발생하였습니다\nflag : " + rtn.flag + "\ Message : " + rtn.message);
                        }
                        $.unblockUI();
                    },
                    complete: function () {
                        $(location).attr('href',"./guideLinkList.do"); 
                    },
                    error: function (e) {                   
                        alert("일시적인 장애가 발생하였습니다. 다시 시도해 주세요.");
                        $.unblockUI();
                    }
                });
            }
        }
    }
    
    function getPanel(){

        var linkType = $('#linkType').val();
        if(linkType == "4"){
            window.open("<%=webRoot%>/admin/mainpanel/getPanelTitleListPop.do?callbak=getTitleCallcack", "regcode", "width=450,height=200");
        }else if(linkType == "6"){
            url = '${pageContext.request.contextPath}/admin/gpack/event/getGpackPromotionChannelView.do?opener=guidelink';
            category_window = window.open(url, 'viewchannel', 'width=700,height=540,scrollbars=yes');
        }
    }

    function getTitleCallcack(data){
        var pannel_id = data.pannel_id;
        var title_id = data.title_id;

        $('#link').val(pannel_id+"_"+title_id);
    }

    $(document).ready(function() {

        var linkType = '${linkType}';
        if(linkType == "4" || linkType == "6"){
            $('#searchBtn').css("display", "inline");
            $('#link').prop("disabled", true);
            $('#detailLink').prop("disabled", false);
        }
    })
    
    function selectLinkType(){
        if($('#linkType').val() == "3" || $('#linkType').val() == "5"){
            $('#searchBtn').css("display", "none");
            $('#link').prop("disabled", false);
            $('#detailLink').prop("disabled", false);
        }else{
            $('#searchBtn').css("display", "inline");
            $('#link').prop("disabled", true);
            $('#detailLink').prop("disabled", false);
        }

        $('#link').val("");
        $('#detailLink').val("");
    }

    function checkNum(obj){
        var txt = obj.value;
        var num = "1234567890";
        var i=0;
        for(i=0;i<txt.length;i++){
            if(num.indexOf(txt.charAt(i)) < 0){
                alert("숫자만 입력 가능합니다.");
                obj.value = txt.substring(0,i);
                obj.focus();
                return false;
            }
        }
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
                <tr>
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
                                                                    <td class="bold">가이드채널 링크등록</td>
                                                                </tr>
                                                                </tbody>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                                <form id="form1" name="form1" method="post">
                                                    <input type="hidden" id="seq" name="seq" value="${detail.seq}"/>
                                                    <input type="hidden" id="view" name="view" value="${view}"/>
                                                    <table border="0" cellpadding="0" cellspacing="0" width="660" class="board_data">
                                                        <tbody>
                                                            <tr align="center">
                                                                <th width="25%">제목</th>
                                                                <td width="5%"></td>
                                                                <td width="70%" align="left" >
                                                                    <input type="text" id="title" name="title" style="width:60%" style="font-size: 12px;" value="${detail.title}" onKeyUp="checkByte($(this),'50')"/>
                                                                    (길이제한 / 50byte)    
                                                                </td>
                                                            </tr>
                                                            
                                                            <c:if test="${type == 'insert' }">
                                                                <tr align="center">
                                                                    <th width="25%">DCA코드</th>
                                                                    <td width="5%"></td>
                                                                    <td width="70%" align="left" >
                                                                        <input type="text" id="dca" name="dca" style="width:20%" style="font-size: 12px;" value="${detail.dca}" onkeyup="checkNum(this);" maxlength="9"/>
                                                                    </td>
                                                                </tr>
                                                            </c:if>
                                                            <c:if test="${type == 'update' }">
                                                               <tr align="center">
                                                                    <th width="25%">DCA코드</th>
                                                                    <td width="5%"></td>
                                                                    <td width="70%" align="left" >
                                                                        ${detail.dca}
                                                                    </td>
                                                                </tr>
                                                            </c:if>
                                                            
                                                            <tr align="center">
                                                                <th width="25%">링크타입</th>
                                                                <td width="5%"></td>
                                                                <td width="70%" align="left" >
                                                                    <select class="select" id="linkType" onchange="selectLinkType()">
                                                                        <option value="3" <c:if test="${detail.linkType eq 3}">selected="selected"</c:if>>앱연동</option>
                                                                        <option value="4" <c:if test="${detail.linkType eq 4}">selected="selected"</c:if>>지면</option>
                                                                        <option value="5" <c:if test="${detail.linkType eq 5}">selected="selected"</c:if>>서비스연동</option>
                                                                        <option value="6" <c:if test="${detail.linkType eq 6}">selected="selected"</c:if>>채널</option>
                                                                    </select>
                                                                    
                                                                    <span class="button small blue" id="searchBtn" onClick="getPanel()" style="display: none;">선택</span>
                                                                </td>
                                                            </tr>
                                                            
                                                            <tr align="center">
                                                                <th width="25%">링크정보</th>
                                                                <td width="5%"></td>
                                                                <td width="70%" align="left" >
                                                                    <input type="text" id="link" name="link" style="width:60%" style="font-size: 12px;" onKeyUp="checkByte($(this),'500')" value="${detail.link}"/>    
                                                                </td>
                                                            </tr>
                                                            
                                                            <tr align="center">
                                                                <th width="25%">링크추가정보</th>
                                                                <td width="5%"></td>
                                                                <td width="70%" align="left" >
                                                                    <input type="text" id="detailLink" name="detailLink" style="width:60%" style="font-size: 12px;" onKeyUp="checkByte($(this),'500')" value="${detail.detailLink}"/>    
                                                                </td>
                                                            </tr>
                                                            
                                                            <tr align="center">
                                                                <th>사용여부</th>
                                                                <td></td>
                                                                <td>
                                                                    <input type="radio" id="useYnY" name="useYn" value="Y" <c:if test="${detail.useYn eq 'Y' }">checked="checked"</c:if> checked="checked"/><label for="useYnY">예</label>
                                                                    <input type="radio" id="useYnN" name="useYn" value="N" <c:if test="${detail.useYn eq 'N' }">checked="checked"</c:if> /><label for="useYnN">아니요</label>
                                                                </td>
                                                            </tr>
                                                            
                                                            <c:if test="${type == 'update' }">
                                                                <tr align="center">
                                                                    <th width="25%">등록일</th>
                                                                    <td width="5%"></td>
                                                                    <td width="70%" align="left" >
                                                                        ${detail.regDate}    
                                                                    </td>                                                               </td>
                                                                </tr>
                                                                <tr align="center">
                                                                    <th width="25%">수정일</th>
                                                                    <td width="5%"></td>
                                                                    <td width="70%" align="left" >
                                                                        ${detail.modDate}    
                                                                    </td>                                                               </td>
                                                                </tr>
                                                            </c:if>
                                                        </tbody>
                                                    </table>
                                                    
                                                    <table border="0" cellpadding="0" cellspacing="0" width="660" align="left">
                                                        <tbody>
                                                        <tr>
                                                            <td height="25" align="right">
                                                                <a href="javascript:goInsert();"><span class="button small blue">등록</span></a>
                                                                <a href="javascript:goList();"><span class="button small blue">목록</span></a> 
                                                            </td>
                                                        </tr>
                                                        </tbody>
                                                    </table>
                                                </form>
                                                
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
            <%@include file="/WEB-INF/views/include/bottom.jsp" %>
            <!-- 하단 로그인 사용자 정보 종료 -->
        </td>
     </tr>
</tbody>
</table>
</div>
</body>
</html>