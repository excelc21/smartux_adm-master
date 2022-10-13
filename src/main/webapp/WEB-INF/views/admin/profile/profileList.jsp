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
var service_type = '${service_type}';

$(document).ready(function () {
    //Drag & Drop
    
    $("#searchList").on('click', '.board_list #searchList tr', function (e) {
        if (e.ctrlKey || e.metaKey) {
            $(this).toggleClass("selected");
           
        } else {
            $(this).addClass("selected").siblings().removeAttr('class');
         
        }
    });
    
    
    $('#searchList').sortable({
        delay: 150, 
        revert: 0,
        helper: function (e, item) {
            if (!item.hasClass('selected')) {
                item.addClass('selected').siblings().removeClass('selected');
            }
            var elements = item.parent().children('.selected').clone();
            item.data('multidrag', elements).siblings('.selected').remove();

            var helper = $('<tr/>');
            return helper.append(elements);
        },
        stop: function (e, ui) {
            var elements = ui.item.data('multidrag');

            ui.item.after(elements).remove();
            elements.siblings().removeAttr('id');
            elements.siblings().removeAttr('class');
         
        }
    });
    
    
    
    $( "#searchList" ).disableSelection(); 
    
    $('#addBtn').click(function () {
        var profileMstName = $('#profileMstId option:checked').text();
        var profileMstId = $('#profileMstId option:checked').val();
        location.href = '${pageContext.request.contextPath}/admin/profile/getInsertProfileView.do?profileMstId='+ profileMstId +'&profileMstName=' + profileMstName +'&service_type=' + service_type;
    });

    $('#deleteBtn').click(function () {
        deleteProfile();
    });

    
  //캐쉬적용
    $("#applyBtn").click(function(){
        if(confirm("상용에 적용하시겠습니까?")){
            $.blockUI({
                blockMsgClass: "ajax-loading",
                showOverlay: true,
                overlayCSS: { backgroundColor: '#CECDAD' } ,
                css: { border: 'none' } ,
                 message: "<b>로딩중..</b>"
            });
            
            $.ajax({
                url: '/smartux_adm/admin/profile/applyCache.do',
                type: 'POST',
                dataType: 'json',
                data: {
                },
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
    
    $("#changeOrder").click(function(){
        
        //var HDTV_Manager = $("#HDTV_Manager").val();
        
        var searchList  = getSearchList();
        var updateSeq   = new Array();
        var updateOrder = new Array();
        var data = null;

        if(searchList == ''){
            alert("변경 하신 순서가 없습니다. 재변경 이후 시도하세요.");
        }else{
        
            for(key in searchList){
                data = searchList[key];
                updateSeq.push(data.seq);
                updateOrder.push(data.order);
            }   
            $.blockUI({
                blockMsgClass: "ajax-loading",
                showOverlay: true,
                overlayCSS: { backgroundColor: '#CECDAD' } ,
                css: { border: 'none' } ,
                 message: "<b>로딩중..</b>"
            });
            
            
            
            $.ajax({
            
                url: '<%=webRoot%>/admin/profile/changeProfileOrder.do',
                type: 'POST',
                dataType: 'json',
                data:  {seqs : updateSeq, orders : updateOrder, profileMstId: $('#profileMstId option:checked').val()},
                error: function(){
                    alert("작업 중 오류가 발생하였습니다");
                    $.unblockUI();
                },
                success: function(rs){
                    // 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
                    var flag = rs.flag;
                    var message = rs.message;
                    if(flag == "0000"){// 정상적으로 처리된 경우
                        alert("순서가 변경되었습니다");
                        $.unblockUI();
                        location.reload();
                    }else{
                        alert("하기 내용의 확인이 필요합니다.\nflag : " + flag + "\nmessage : " + message);
                        $.unblockUI();
                    }
                }
            });
        }
    });

   

    $('#profileMstId').change(function () {
        
        search($(this).val());
    });

});


function search(val) {
    
    location.href = '${pageContext.request.contextPath}/admin/profile/getProfileList.do?profileMstId='+ val+'&service_type='+service_type;
}

function getSearchList(){
    var checkList = $('#searchList tr');
    var searchList = {};
    var search_id = '';
    var order = '';
    var seq = '';

    var i = 0;
    var count = 0;
    $(checkList).each(function(idx){
        order = idx + 1;
        search_id = this.id;
        
        seq = $(this).find('input[type=hidden]').val();

/*      if(order != search_id){
            searchList[i] = {'seq_num':seq_num, 'order':order};
            count++;
        } */
            
        searchList[i] = {'seq':seq, 'order':order};
        count++;
        i++;
    });
    
    if(count == 0){
        searchList = '';
    } 
    
    return searchList;
}

function updateProfile(imgId) {
    var profileMstName = $('#profileMstId option:checked').text();
    location.href = '${pageContext.request.contextPath}/admin/profile/getUpdateProfileView.do?profileImgId=' + imgId +'&profileMstName='+profileMstName +'&service_type=' + service_type;
}

function deleteProfile() {
    var profileImgIds = '';

    $('.checkbox').each(function () {
        if ($(this).is(':checked')) {
            profileImgIds += $(this).val() + ',';
        }
    });

    if (0 >= profileImgIds.length) {
        alert('삭제 할 항목을 선택하세요.');
        return;
    }


    if (confirm('삭제 하시겠습니까?')) {
        $.ajax({
            url: '${pageContext.request.contextPath}/admin/profile/deleteProfile',
            type: 'POST',
            dataType: 'json',
            data: {profileImgIds: profileImgIds},
            success: function (data) {
                var flag = data.flag;
                var message = data.message;

                if (flag == '0000') {
                    alert('삭제되었습니다.');
                    location.reload();
                } else {
                    alert('작업 중 오류가 발생하였습니다\nflag : ' + flag + '\nmessage : ' + message);
                }
            },
            error: function () {
                alert('작업 중 오류가 발생하였습니다');
                location.reload();
            }
        });
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
                <tr style="display:block">
                    <td height="42" width="100%">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tbody>
                            <tr>
                                <td width="300" class="boldTitle">
                                    <!-- 카테고리 타이틀 -->
                                    <!-- <img src="/smartux_adm/images/admin/category_map_title.gif">-->
                                   프로필이미지 관리
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
                                    <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" >
                                        <tbody>
                                        <tr>
                                            <td class="3_line" height="1"></td>
                                        </tr>
                                        <tr>
                                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
									            <tr>
									                <td>
									                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
									                        <tr>
									                            <td align="right" style="width: 30px; padding: 5px;">
									                                <select class="select" id="profileMstId">
									                                    <c:if test="${masterList == '[]' }">
									                                        <option selected="selected" value="">마스터를 등록하세요.</option>
									                                    </c:if>
									                                    <c:forEach items="${masterList}" var="master">
									                                        <option <c:if test="${master.profileMstId eq vo.profileMstId}">selected="selected"</c:if> value="${master.profileMstId}">${master.profileMstName}</option>
									                                    </c:forEach>
									                                    
									                                </select>
									                        
									                            </td>
									                        </tr>
									                    </table>
									                </td>
									            </tr>
									        </table>
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
                                                                   <td class="bold">프로필이미지 관리</td>
                                                                </tr>
                                                                </tbody>
                                                            </table>
                                                        </td>                                                                                                               
                                                    </tr>
                                                    </tbody>
                                                </table>
                                                
                                                <table id="data_list" border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list"> 
                                                    <tr align="center">
									                <th scope="col" width="5%">선택</th>
									                <th scope="col" width="5%">번호</th>
									                <th scope="col" width="5%">이미지 ID</th>
									                <th scope="col">이미지</th>
									                <th scope="col" width="20%">이미지 타이틀</th>
									                <th scope="col" width="20%">등록일</th>
									                <th scope="col" width="20%">수정일</th>
									
									            </tr>
									            <c:if test="${vo.list == '[]' }">
									                <tr align="center">
									                    <td class="table_td_04" colspan="7">데이터가 존재 하지 않습니다.</td>
									                </tr>
									            </c:if>
									            <tbody id= "searchList" class="tbodyList" >
									            <c:forEach items="${vo.list}" var="list">
									                <c:set var="i" value="${i+1}"/>
									                <tr align="center">
									                    <!-- 체크 박스 -->
									                    <td class="table_td_04"><input type="checkbox" class="checkbox" value="${list.profileImgId}"/></td>
									                    <!-- 번호 -->
									                    <td class="table_td_04" >${((vo.pageNum - 1) * vo.blockSize) + i}</td>
									                    <!-- 프로필 id-->
									                    <td class="table_td_04" >${list.profileImgId}</td>
									                    <!-- 프로필 -->
									                    <td class="table_td_04"><a href="javascript:updateProfile('${list.profileImgId}')"><img src="${list.profileImgUrl}" width="130" height="60"></a></td>
									                    <!-- 프로필타이틀 -->
									                    <td class="table_td_04">${list.profileImgName}</td>
									                    <!-- 등록일 -->
									                    <td class="table_td_04">${list.regDate}</td>
									                    <!-- 수정일 -->
									                    <td class="table_td_04">${list.modDate}
									                    <input type="hidden" id ="ln${list.profileImgId}" value="${list.profileImgId}"/>
									                    </td>
									                    
									                </tr>
									            </c:forEach>
									            </tbody>
                                                </table>
                                                
                                                <!-- 페이징 -->
										        <table border="0" cellpadding="0" cellspacing="0" width="100%">
										            <tr>
										                <td height="5"/>
										            </tr>
										            
										        </table>
                                                
                                                <!-- 등록/수정 버튼 -->
										        <table border="0" cellpadding="0" cellspacing="0" width="100%"
										               align="center">
										            <tr>
										                <td align="right">
										                    <span class="button small blue" id="addBtn">등록</span>
										                    <span class="button small blue" id="deleteBtn">삭제</span>
										                    <span class="button small blue" id="changeOrder">순서변경</span>
										                    <span class="button small blue" id="applyBtn">즉시적용</span>
										                </td>
										            </tr>
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