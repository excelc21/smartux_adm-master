<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<style type="text/css">
.selectTable {
   border: 1px solid #000000;
 }

 .selected {
   background-color: #d0e8fd;
 }
 
 ol{
   list-style:none;
   }
</style>
<script type="text/javascript">
$(document).ready(function(){
	$("#contentList").on('click', '#contentList li', function (e) {
        if (e.ctrlKey || e.metaKey) {
            $(this).toggleClass("selected");
           
        } else {
            $(this).addClass("selected").siblings().removeAttr('class');
         
        }
    });
	
	$('#contentList').sortable({
        delay: 150, 
        revert: 0,
        helper: function (e, item) {
            if (!item.hasClass('selected')) {
                item.addClass('selected').siblings().removeClass('selected');
            }
            var elements = item.parent().children('.selected').clone();
            item.data('multidrag', elements).siblings('.selected').remove();

            var helper = $('<li/>');
            return helper.append(elements);
        },
        stop: function (e, ui) {
            var elements = ui.item.data('multidrag');

            ui.item.after(elements).remove();
            elements.siblings().removeAttr('id');
            elements.siblings().removeAttr('class');
         
        }
    });
    
	$('#closebtn').click(function(){
		self.close();
	});
	
	$('#savebtn').click(function(){
		var size = $("#contentList li").size();
		var optionarray = new Array();
		var pararray = new Array();
		for(var i=0; i < size; i++){
			var selval = $('#contentList li').eq(i).attr("data-val");		
			var parval = $('#contentList li').eq(i).attr("data-par");		
			if(parval != null && parval != "") pararray.push(parval);
			
			optionarray.push(selval);
		}
		
		
		$.ajax({
            url: '<%=webRoot%>/admin/hotvod/changeSave',
            type: 'PUT',
            dataType: 'json',
            data: {
                optionArray: optionarray, parArray:pararray
            },
            error: function () {
                alert('작업 중 오류가 발생하였습니다');
            },
            success: function (rs) {
                // 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
                var flag = rs.flag;

                var message = rs.message;

                if (flag == '0000') {						// 정상적으로 처리된 경우
                    alert('순서를 재지정했습니다');
                    $(opener.location).attr('href',"./contentList.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&pageSize=${vo.pageSize}&delYn=${vo.delYn}&serviceType=${vo.serviceType}&isLock=${vo.isLock}");
					self.close();
                } else {
                    alert('작업 중 오류가 발생하였습니다\nflag : ' + flag + '\nmessage : ' + message);
                }
            }
        }); 
		/*var contentId_comma="";
		var statCnt = 0;
		
        $("#contentList li").each(function (idx) {
           	if(statCnt==0){
           		contentId_comma = contentId_comma+$(this).attr('id');
           	}else{
           		contentId_comma = contentId_comma+","+$(this).attr('id');
           	}
           	statCnt++;
        });

		if(confirm("현재 순서로 저장하시겠습니까?")){	
		    $.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>처리 중..</b>"
			});
		    
		    $.ajax({
				url: './changeSave.do',
				type:'POST',
				dataType: 'json',
				timeout : 30000,
			    data: {
			        "content_id": contentId_comma,
			        "parent_id": $("#parent_id li").attr('id')
			    },
				success:function(data){
					if(data.result.flag=="0000"){
						alert("정상적으로 처리 되었습니다.");
					}else{
						alert(data.result.message);
					}
				},
				error:function(){
					alert("정상적으로 처리되지 않았습니다.");
				},
				complete:function(){
					//$.unblockUI();
					$(opener.location).attr('href',"./contentList.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&pageSize=${vo.pageSize}&delYn=${vo.delYn}&serviceType=${vo.serviceType}&isLock=${vo.isLock}");
					self.close();
				}
			});
		}*/
		
	});
});
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
                    <td class="bold">순서 바꾸기</td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    </tbody>
</table>
<table cellspacing="0" width="80%" align="center" height="500">
    <tr>
        <td valign="top" width="100%"  height="500">
        	 <div style="height:480px;overflow:scroll;">
        		<ol id="contentList" class="example">
        			<c:forEach var="item" items="${list}" varStatus="status">
						<li data-val="${item.content_id}!^${item.content_type}" data-par="${item.parent_id}" style="none">
							<b>[${status.index+1}]</b>              		
					
							<c:if test="${item.content_type eq 'C'}">
							<img src="/smartux_adm/images/folder.gif" width="20" height="20"/>
							</c:if>
							${item.content_name}
	              	
	              		</li>
	   				</c:forEach>
				</ol>
				
			</div>
        </td>
    </tr>
</table>
<table width="100%" align="center">
    <tbody>
    <tr>
        <td height="40" align="center">
            <span class="button small blue" id="savebtn">저장</span>
            <span class="button small blue" id="closebtn">닫기</span>
        </td>
    </tr>
    </tbody>
</table>

</body>
</html>