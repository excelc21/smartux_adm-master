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

var last_select_val = "";
var last_select_text = "";

$(document).ready(function(){
    var callback = '${callback}';
    
    $("#setLinkInfoBtn").click(function(){

        if(last_select_val == '' || last_select_val == '---' || last_select_val == 'O000'){
            if('${link_type}' == 'OTT'){
                alert("OTT를 선택해주세요.");
            }else if('${link_type}' == 'APP'){
                alert("추천앱을 선택해주세요.");
            }else if('${link_type}' == 'MIX'){
                alert("MIX를 선택해주세요.");
            }
            
            return false;
        }
        
        $(opener.document).find("#link_des").val(last_select_text);
        $(opener.document).find("#link_code").val(last_select_val);
        self.close();
    });
    

    $("#closebtn").click(function(){
        self.close();
    });
})

function selectCategory(step){

    var sel_value = $("select[name='select_"+step+"'] option:selected").val();

    last_select_val = sel_value;
    last_select_text = $("select[name='select_"+step+"'] option:selected").text();

    $('select').each(function(){
        var select_step = $(this).attr("data");
        if(select_step > step){
            $(this).remove();
        }
        console.log($(this).attr("data"))
    })
    
    $.ajax({
        url:  "./getLinkInfoPop.do",
        type: "POST",
        data: { 
            'sel_value' : sel_value
            ,'link_type' : '${link_type}'
        },
        dataType : "json",
        success: function (data) {
            if(data.length > 0){
                var next_num = (step*1)+1;
                var dHtml = "";
                if(next_num >= 3){
                    dHtml += "<select id='select_"+next_num+"' name='select_"+next_num+"' data='"+next_num+"' onchange='selectCategory("+next_num+");' style='margin-left:4px;'>";
                }else{
                    dHtml += "<select id='select_"+next_num+"' name='select_"+next_num+"' data='"+next_num+"' onchange='selectCategory("+next_num+");'>";
                }
                dHtml += "<option value='---'>선택해주세요</option>";
                for(var i = 0 ; i < data.length; i++){
                    dHtml += "<option value="+data[i].category_id+">"+data[i].category_name+"</option>";
                }
                dHtml += "</select>";
    
                $('#category_td').append(dHtml);
            }
        },
        error: function () {                    
            alert("일시적인 장애가 발생하였습니다. 다시 시도해 주세요.");
        }
    });
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
                    <td class="bold">OTT/추천앱/MIX 선택</td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    </tbody>
</table>
<table class="sidebarMain">
    <tr id="category_tr">
        <td name="" valign="top">
            <select id="link_type" name="link_type" disabled="disabled"> 
                <option value="OTT" <c:if test="${link_type eq 'OTT'}">selected="selected"</c:if>>OTT</option>
                <option value="REC_APP" <c:if test="${link_type eq 'APP'}">selected="selected"</c:if>>추천앱</option>
                <option value="MIX" <c:if test="${link_type eq 'MIX'}">selected="selected"</c:if>>MIX</option>
            </select>
        </td>
        <td id="category_td" name="category_td" valign="top">
        
            <select id="select_1" name="select_1" data="1" onchange="selectCategory('1');">
                <option value="---">선택해주세요</option>
                <c:forEach var="item" items="${category_list}" varStatus="status">
                    <option value="${item.category_id}">${item.category_name}</option>
                </c:forEach>
            </select>
            
            <!-- <select id="step2_select" name="step2_select" onchange="location_change('step3');">
                <option value="---">선택해주세요</option>     
            </select>
            
            <select id="step3_select" name="step3_select">
                <option value="---">선택해주세요</option>     
            </select> -->
        </td>
    </tr>
</table>
<input type="hidden" id="albumElementid" name="albumElementid" value="" />
<table border="0" cellpadding="0" cellspacing="0" width="100%" align="left">
<tbody>
    <tr>
        <td height="40" align="left">
            <span class="button small blue" id="setLinkInfoBtn">설정</span>
            <span class="button small blue" id="closebtn">닫기</span>
        </td>
    </tr>
    </tbody>
</table>
</body>
</html>