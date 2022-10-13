<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>약관 항목 상세</title>
    <link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
    <jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
    
<script type="text/javascript">
$(document).ready(function () {
	
	$("#allchk").click(function(){
		var chkallchecked = false;
		
		if($("#allchk").is(":checked")){
			chkallchecked = true;
		}
		
		$("input[name='delchk']").attr("checked", chkallchecked);
	});
	
    $("#closebtn").click(function () {
        self.close();
    });
    
	$("#delBtn").click(function(){
		var checkeditems = $("input[name='delchk']:checked");
		var checkeditemslength = checkeditems.length;
		
		if(checkeditemslength == 0){
			alert("약관 항목 상세를 선택해주세요");
		}else{
			var size = checkeditemslength;
			var checkboxarray = new Array();
			var tmp;

			for(var i=0; i < size; i++){
				tmp =  $(checkeditems[i]).val();
				checkboxarray.push(tmp);
			}
			
			if(confirm("선택된 약관 항목 상세들을 삭제 하시겠습니까?")){
				
				$.blockUI({
					blockMsgClass: "ajax-loading",
					showOverlay: true,
					overlayCSS: { backgroundColor: '#CECDAD' } ,
					css: { border: 'none' } ,
					 message: "<b>로딩중..</b>"
				});	
				
				$.get("./deleteProcDetail.do", 
					 {access_detail_ids : checkboxarray},
					  function(data) {
						 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
						 	var flag = data.res;
						 	var message = data.msg;
						 	
						 	if(flag == "0000"){						// 정상적으로 처리된 경우
						 		alert("해당 약관 항목 상세가 삭제 처리되었습니다");
						 		$.unblockUI();
						 		location.reload();
						 	}else{
						 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
						 		location.reload();
						 	}
					  },
					  "json"
			    );
			}
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
                    <td class="bold">약관 항목 : ( ${vo.access_info_id} ) ${access_title}</td>
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
        <th scope="col" width="20">번호</th>
        <th scope="col" width="40">항목번호</th>
        <th scope="col" width="70">버전 </th>
        <th scope="col" width="40">노출여부</th>
        <th scope="col" width="70">최종수정일</th>
      </tr>
   </thead>
   <tbody>
   <c:if test="${list == '[]' }"> 
	<tr align="center">
	<td class="table_td_04" colspan="6">데이터가 존재 하지 않습니다.</td>					                                    
	</tr>
   </c:if>
   <c:forEach var="item" items="${list}" varStatus="status">
   <tr align="center" onMouseOver="this.style.backgroundColor='#d0e8fd'" onMouseOut="this.style.backgroundColor=''">
   	  <td class="table_td_04">
   	  	<c:if test="${access_version ne item.access_version}">
   	  		<input type="checkbox" name="delchk" value="${item.access_detail_id}">
   	  	</c:if>
   	  </td>
      <td class="table_td_04" align="center">${vo.pageCount-(item.rowno-1)}</td>
      <td class="table_td_04" align="center">${item.access_detail_id}</td>
      <td class="table_td_04" align="center">
      	<a href="./getAccessDetail.do?pageNum=${vo.pageNum}&access_detail_id=${item.access_detail_id}">${item.access_version}</a>
      </td>
      <td class="table_td_04" align="center">
      	<c:if test="${item.display_yn eq 'Y'}">노출</c:if>
		<c:if test="${item.display_yn eq 'N'}">미노출</c:if>
      </td>
      <td class="table_td_04" align="center">${item.mod_dt}</td>
   </tr>
   </c:forEach>
   </tbody>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
   <tr >      				                                    						                                    
      <td align="center" height=40>
      	<a href="./insertAccessDetail.do?pageNum=${vo.pageNum}&access_info_id=${vo.access_info_id}"><span class="button small blue" id="insertBtn">등록</span></a>
		<c:if test="${list != '[]' }"> 
			<span class="button small blue" id="delBtn">삭제</span>
      	</c:if>
		<span class="button small blue" id="closebtn">닫기</span>
      </td>
   </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tbody>
	<tr>
	    <td height="5"></td>
	</tr>
	<tr>
		<td align="center">
			<jsp:include page="/WEB-INF/views/include/naviControll.jsp">
			<jsp:param value="getAccessDetailListPop.do" name="actionUrl"/>
			<jsp:param value="?access_info_id=${vo.access_info_id}" name="naviUrl" />
			<jsp:param value="${vo.pageNum }" name="pageNum"/>
			<jsp:param value="${vo.pageSize }" name="pageSize"/>
			<jsp:param value="${vo.blockSize }" name="blockSize"/>
			<jsp:param value="${vo.pageCount }" name="pageCount"/>			  
			</jsp:include>
    	</td>
    </tr>
	</tbody>
</table>
<p>&nbsp;</p>