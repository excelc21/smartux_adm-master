<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>약관 항목 선택</title>
    <link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
    <jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
    
<script type="text/javascript">
$(document).ready(function () {
	
	//sortable 크기 유지
	var fixHelper = function(e, ui) {
		ui.children().each(function() {
			$(this).width($(this).width());
		});
		return ui;
	};
	
    //sortable (tbody table이 form 태그 안에 있어야함)
	$("#data_list tbody").sortable({
	    helper: fixHelper,
	    axis:'y',
	    update: function(event, ui) {
			$(".order_no").each(function(index) {
				$(this).text(index+1);
			}); 
	    }
    }).disableSelection();
    
    $("#closebtn").click(function () {
        self.close();
    });
    
	$("#sumitBtn").click(function(){
		var checkeditems = $("input[name='delchk']:checked");
		var checkeditemslength = checkeditems.length;
		
		if(checkeditemslength == 0){
			alert("약관 항목을 선택해주세요");
		}else{
			var size = checkeditemslength;
			var checkboxarray = new Array();
			var tmp = new Object(); 

			for(var i=0; i < size; i++){
				tmp = new Object();
				tmp.id =  $(checkeditems[i]).val();
				tmp.name = $(checkeditems[i]).attr("data-name");
				checkboxarray.push(tmp);
			}
			
			var callbak_m = eval("opener."+"${param.callback}");  
			callbak_m(checkboxarray);
			self.close();
		}
	});
    
    $("#allchk").click(function(){
		var chkallchecked = false;
		
		if($("#allchk").is(":checked")){
			chkallchecked = true;
		}
		
		$("input[name='delchk']").attr("checked", chkallchecked);
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
                    <td class="bold">약관 항목 선택</td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>    
    </tbody>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list" id="data_list">
   <thead>
      <tr align="center">
      	<th scope="col" width="10" style="padding-top:8px"><input type="checkbox" id="allchk" name="allchk" value=""></th>
        <th scope="col" width="60">항목ID</th>
      	<th scope="col" width="60">우선순위</th>	
        <th scope="col" width="240">타이틀</th>
        <th scope="col" width="240">비고</th>
      </tr>
   </thead>
   <tbody>
   <c:if test="${list == '[]' }"> 
	<tr align="center">
	<td class="table_td_04" colspan="5">데이터가 존재 하지 않습니다.</td>					                                    
	</tr>
   </c:if>
   <c:forEach var="item" items="${list}" varStatus="status">
   <tr align="center" onMouseOver="this.style.backgroundColor='#d0e8fd'" onMouseOut="this.style.backgroundColor=''">
   	  <td class="table_td_04">
   		<input type="checkbox" name="delchk" value="${item.access_info_id}"<c:if test="${not empty item.col}"> checked</c:if> data-name="${item.access_title}">
   	  </td>
   	  <td class="table_td_04" align="cneter">
      	${item.access_info_id}
      </td>
      <td class="table_td_04 order_no">
      	${item.rowno}
      </td>
      <td class="table_td_04" align="left" style="word-break:break-all;">
      	${item.access_title}
      </td>
      <td class="table_td_04" align="left" style="word-break:break-all;">
      	${item.etc}
      </td>
   </tr>
   </c:forEach>
   </tbody>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
   <tr >      				                                    						                                    
      <td align="center" height=40>
      	<c:if test="${list != '[]' }"> 
      	 <span class="button small blue" id="sumitBtn">선택</span>
      	</c:if>
         <span class="button small blue" id="closebtn">닫기</span>				                                    
      </td>
   </tr>
</table>
<p>&nbsp;</p>