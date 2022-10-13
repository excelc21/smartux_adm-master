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

$(document).ready(function(){
	$("#applybtn_pop").click(function(){
		var msg = "캐시 인터페이스용 팝업공지를 상용에 적용하시겠습니까?";
		if(confirm(msg)){
			$.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>로딩중..</b>"
			});
			$.ajax({
			    url: '/smartux_adm/admin/notipop/notiPopApplyCache.do',
			    type: 'POST',
			    dataType: 'json',
			    data: {
			    	callByScheduler : 'A'
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
	
	// 삭제 버튼 클릭
	$("#deleteBtn").click(function(){
		var deleteList="";
		var isDelete=true;
		var bbs_gbn='${notiListVO.bbs_gbn}';
		
		if(!$(".checkbox").is(":checked")){
			alert("삭제 할 항목을 선택하세요.");
			return;
		}else{			
			$(".checkbox").each(function(index){
				
				if(isDelete){
					if($(this).is(":checked")){
						if(bbs_gbn=='PU'){
							if($("input[name=fixed]").eq(index).val()=="1"){
								alert("상용 적용 해제 후 삭제해 주세요.");
								isDelete=false;						
							}					
							if($("input[name=adt]").eq(index).val()=="1"){
								alert("검수 완료 해제 후 삭제해 주세요.");
								isDelete=false;
							}
						}else{
							if($("input[name=fixed]").eq(index).val()=="1"){
								alert("상단여부 해제 후 삭제해 주세요.");
								isDelete=false;						
							}		
						}
						deleteList+=$(this).val()+",";
					}
				}
			 });
		 }
					
		if(isDelete){
			$("#reg_no").attr("value",deleteList);
			$("#form1").attr("action","deleteNoti.do");
			$("#form1").submit();
		}			
	
	});

	// 복사하기 버튼 클릭
	$("#copyPopBtn").click(function(){
		var reg_no="";
		
		if(!$(".checkbox").is(":checked")){
			alert("복사 할 항목을 선택하세요.");
			return false;
		}else{
			if(confirm("팝업게시판으로 복사하시겠습니까?")){
				var x = 0;
				$(".checkbox:checked").each(function(index){
					if(x==0){
						reg_no+=$(this).val();
					}else{
						reg_no+=","+$(this).val();
					}
					x++;
				 });
			}
		 }
		
		if(reg_no!=""){

		    $.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>처리 중..</b>"
			});
		    
			$.ajax({
				url: './copyNotiPopup.do',
				type:'POST',
				dataType: 'json',
				timeout : 30000,
			    data: {
			        "reg_no": reg_no
			        ,"scr_tp": "${notiListVO.scr_tp}"
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
					//$("#copyTable").hide();
					$(location).attr('href',"./getNotiList.do?bbs_gbn=PU&scr_tp=${notiListVO.scr_tp}"); 
				}
			});
		}
	});
	
	$("#bbs_id").change(function(){
		
		var bbs_id=$("#bbs_id option:selected").val();
		//var bbs_gbn=$("#bbs_gbn option:selected").val();
		var bbs_gbn='${notiListVO.bbs_gbn }';
		//var scr_tp=$("#scr_tp").val();
		var scr_tp='${notiListVO.scr_tp}';
		
		location.href = "/smartux_adm/admin/noti/getNotiList.do?bbs_id="+bbs_id+"&bbs_gbn="+bbs_gbn+"&scr_tp="+scr_tp;
		
	});
	
	$("#applybtn").click(function(){
		
		var msg = "게시글을 상용에 적용하시겠습니까?";
		
		if(confirm(msg)){
			
			$.ajax({
			    url: '/smartux_adm/admin/noti/applyCache.do',
			    type: 'POST',
			    dataType: 'json',
			    data: {		
			    	callByScheduler : 'A'
			    },
			    error: function(){
			    	alert("작업 중 오류가 발생하였습니다");
			    },
			    success: function(rs){
			    	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
				 	var flag = rs.flag;			    
			    	
				 	var message = rs.message;
				 	
				 	if(flag == "0000"){						// 정상적으로 처리된 경우
				 		alert("게시글이 상용에 적용되었습니다.");
				 	}else{
				 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
				 	}
			    }
			});
			
		}
		
	});
	
	//2021.12.08 AMIMS 개선 : 게시한 부분 즉시적용 추가
	$("#applyMasterIdBtn").click(function(){
		
		var msg = "해당 게시판만 상용에 적용하시겠습니까?";
		
		if(confirm(msg)){
			
			$.ajax({
			    url: '/smartux_adm/admin/noti/applyCache.do',
			    type: 'POST',
			    dataType: 'json',
			    data: {		
			    	callByScheduler : 'A',
			    	bbs_id : '${notiListVO.bbs_id}'
			    },
			    error: function(){
			    	alert("작업 중 오류가 발생하였습니다");
			    },
			    success: function(rs){
			    	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
				 	var flag = rs.flag;			    
			    	
				 	var message = rs.message;
				 	
				 	if(flag == "0000"){						// 정상적으로 처리된 경우
				 		alert("게시글이 상용에 적용되었습니다.");
				 	}else{
				 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
				 	}
			    }
			});
			
		}
		
	});
	
	$("#cjapplybtn").click(function(){
		
		var msg = "게시글을 CJ 상용에 적용하시겠습니까?";
		
		if(confirm(msg)){
			
			$.ajax({
			    url: '/smartux_adm/admin/noti/applyCjCache.do',
			    type: 'POST',
			    dataType: 'json',
			    data: {		
			    	callByScheduler : 'A'
			    },
			    error: function(){
			    	alert("작업 중 오류가 발생하였습니다");
			    },
			    success: function(rs){
			    	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
				 	var flag = rs.flag;			    
			    	
				 	var message = rs.message;
				 	
				 	if(flag == "0000"){						// 정상적으로 처리된 경우
				 		alert("게시글이 CJ 상용에 적용되었습니다.");
				 	}else{
				 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
				 	}
			    }
			});
			
		}
		
	});
	
});

//팝업공지 일 경우 설정/해지
function setPopSettion(bbs_id,reg_no,is_fixed){
	var msg="";
	if(is_fixed==1){		
		msg="상용 적용 하시겠습니까?";
		
	} else if(is_fixed==0){
		msg="상용 해제 하시겠습니까?";
	}
	if(confirm(msg)){
		$.blockUI({
			blockMsgClass: "ajax-loading",
			showOverlay: true,
			overlayCSS: { backgroundColor: '#CECDAD' } ,
			css: { border: 'none' } ,
			 message: "<b>로딩중..</b>"
		});
		$.ajax({
		    url: '/smartux_adm/admin/noti/popUpSetting.do',
		    type: 'POST',
		    dataType: 'json',
		    data: {
		        "reg_no": reg_no,
		        "bbs_id": bbs_id,
		        "scr_tp": "${notiListVO.scr_tp}",
		        "is_fixed": is_fixed
		    },
		    error: function(){
		    	alert("접속 할  수 없습니다.");
		    },
		    success: function(textDoc){
		    	$.unblockUI();
		    	var resultstate=textDoc.resultstate;
		    	var flag=textDoc.flag;
		    	var message=textDoc.message;
		    	if(flag=="0000"){
			    	if(resultstate == "SETTING"){
			    		alert("상용 적용 되었습니다");
			    		location.reload();			    		
			    	}
			    	if(resultstate == "CANCLE"){
			    		alert("상용 해제 되었습니다.");
			    		location.reload();			    		
			    	}			    	
		    	}else{
		    		$.unblockUI();
		    		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);		    		
		    	}
		    }
		});	
	}
}

function updateConfirmAdt(bbs_id,reg_no,is_adt){
	var msg="";
	
	
	if(is_adt=='1'){		
		msg="검수 확인 하시겠습니까?";
		
	} else if(is_adt==0){
		msg="검수 해제 하시겠습니까?";
	}	
	if(confirm(msg)){
		$.blockUI({
			blockMsgClass: "ajax-loading",
			showOverlay: true,
			overlayCSS: { backgroundColor: '#CECDAD' } ,
			css: { border: 'none' } ,
			 message: "<b>로딩중..</b>"
		});
		$.ajax({
		    url: '/smartux_adm/admin/noti/updateConfirmAdt.do',
		    type: 'POST',
		    dataType: 'json',
		    data: {
		        "reg_no": reg_no,
		        "is_adt": is_adt,
		        "bbs_id":bbs_id,
		        "scr_tp": "${notiListVO.scr_tp}",
		        "bbs_gbn":"${notiListVO.bbs_gbn}"
		    },
		    success: function(textDoc){
		    	$.unblockUI();
		    	var resultstate=textDoc.resultstate;
		    	var flag=textDoc.flag;
		    	var message=textDoc.message;
		    	if(flag=="0000"){
			    	if(resultstate == "SETTING"){
			    		alert("검수 확인 되었습니다");
			    		location.reload();
			    		
			    	}
			    	if(resultstate == "CANCLE"){
			    		alert("검수 해제 되었습니다.");
			    		location.reload();	
			    	}			    	
		    	}else{		    		
		    		alert("작업 중 오류가 발생하였습니다.\nflag : " + flag + "\nmessage : " + message);		    		
		    	}
		    },
		    error: function(){
		    	$.unblockUI();
		    	alert("에러가 발생하였습니다.");
		    }
		});	
	}
}

//팝업게시판이 아닌 그 외 게시판 상단여부 

function updateIsFixed(reg_no,is_fixed){
	
	if(is_fixed==1){
		if( '${isFixedCheck}'=='N'){
			alert("설정이 3개 이상입니다.");
			return;
		}
	}
	var msg="";
	if(is_fixed==1){		
		msg="상단에 고정하시겠습니까?";
		
	} else if(is_fixed==0){
		msg="해제하시겠습니까?";
	}
	
	if(confirm(msg)){
		$.ajax({
		    url: '/smartux_adm/admin/noti/updateIsFixed.do',
		    type: 'POST',
		    dataType: 'json',
		    data: {
		        "reg_no": reg_no,
		        "is_fixed": is_fixed
		    },
		    error: function(){
		    	alert("접속 할 수 없습니다.");
		    },
		    success: function(textDoc){	    	
		    	var resultstate=textDoc.resultstate;
		    	var flag=textDoc.flag;
		    	var message=textDoc.message;
		    	if(flag=="0000"){
			    	if(resultstate == "SETTING"){			    		
			    		alert("설정 되었습니다");	    	
			    		location.reload();
			    	}
			    	if(resultstate == "CANCLE"){			    		
			    		alert("해제 되었습니다.");
			    		location.reload();	
			    	}			    	
		    	}else{		    		
		    		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);		    		
		    	}
		    }
		});	
	}
}

function formRegistration(){	   
	$("#form1").attr("action","getNotiView.do");
	$("#form1").submit();		
}

function formUpdate(reg_no){	
	$("#reg_no").attr("value",reg_no);
	$("#form1").attr("action","getNotiUpateView.do");
	$("#form1").submit();	
}

function doSearch(){
	$("#pageNum").attr("value","1");	
	$("#form1").submit();
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
                                   공지 관리
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
			                            <td>
			                                <!-- 검색 시작 -->
											<form id="form1" method="get" action="/smartux_adm/admin/noti/getNotiList.do">													
											<input type="hidden" id="reg_no" name="reg_no" value="">
											<input type="hidden" name="pageNum" id="pageNum" value="${notiListVO.pageNum}">
											<input type="hidden" name="bbs_gbn" id="bbs_gbn" value="${notiListVO.bbs_gbn}">
											<input type="hidden" id="scr_tp" name="scr_tp" value="${notiListVO.scr_tp}">																																															
											
			                                <table border="0" cellpadding="0" cellspacing="0" width="100%" class="search_table" >
			                                
			                                	<tbody >
			                                	<tr >
				                                    <td width="15">&nbsp;</td>
				                                    <td width="80"><img src="/smartux_adm/images/admin/search_title4.gif" border="0" height="47" width="62"></td>
				                                    <td>
				                                  
				                                    	<%-- <form id="form2" method="get" action="/smartux_adm/admin/noti/getNotiList.do"> --%> 
					                                    <table border="0" cellpadding="0" cellspacing="0" width="280">		                                    
		                                        			<tbody>
		                                        			<tr><td>&nbsp;</td></tr>
		                                        			<tr>
					                                            <td width="10">
					                                            	<img src="/smartux_adm/images/admin/blt_05.gif" height="9" width="9" />
					                                            </td>
					                                            <td>
					                                                <select class="select"  name="findName">
					                                                    <option <c:if test="${notiListVO.findName == 'noti.title'}">selected="selected"</c:if> value="TITLE">제목</option>					                                                    
					                                                </select>  
					                                            	<input type="text" name="findValue" value="${notiListVO.findValue}" size="20" style="font-size: 12px;" onKeyPress="if(event.keyCode==13){ doSearch();}" />
					                                            </td>					
					                                            <td width="66" align="left">
					                                                <img src="/smartux_adm/images/admin/search.gif" border="0" height="22"  width="65" onclick="doSearch();" style="cursor:pointer;">                                            	
					                                            
					                                            </td>
					                                         </tr>
			                                    			</tbody>
			                                    		</table>
			                                    		<%-- </form> --%>
			                                    	</td>
					                                 
		                                            <td align="left" style="width: 30px;padding: 5px;" >
		                                            	
		                                            	 <select  id="bbs_id" name="bbs_id">
		                                            			<c:forEach items="${boardList}" var="board">		                                            			
		                                            				 <option <c:if test="${board.BBS_ID==notiListVO.bbs_id}">selected="selected"</c:if> value="${board.BBS_ID}">${board.BBS_NM}</option>		                                            				 
		                                            			</c:forEach>		                                            					                                            				                                            		                                            	
		                                            	</select> 
		                                            			 
		                                            	<!-- screentype hidden -->           
		                                            	<%-- <c:forEach items="${boardList}" var="board">		                                            			
		                                            		<c:if test="${board.BBS_ID==notiListVO.bbs_id}"><input type="hidden" id="scr_tp" name="scr_tp" value="${board.SCR_TP}">${board.SCR_TP} </c:if>
		                                            	</c:forEach>		                                               		           --%>
		                                            	
		                                            </td>
					                            </tr> 					                            
					                        	</tbody>
					                        </table>
					                        </form>
					                        <!-- 검색 종료 -->
					                    </td>
						                </tr>
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
						                                           <c:if test="${notiListVO.bbs_gbn=='PU'}"> <td class="bold">팝업 공지/이벤트 리스트</td></c:if>
						                                           <c:if test="${notiListVO.bbs_gbn=='EV'}"> <td class="bold">공지/이벤트 리스트</td></c:if>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>						                                						                                
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list">
					                                <tbody>
						                                <tr align="center">
						                                			   <th scope="col" width="5%">선택</th>
									                                   <th scope="col" width="5%">번호</th>
									                                  							                                   
						                                	<c:choose>
															<c:when test="${notiListVO.bbs_gbn=='PU' || notiListVO.bbs_gbn == 'EV'}">
																<c:if test="${notiListVO.bbs_gbn=='PU'}">
																	<c:set var="columnCnt" value="6" /> 
																 		<th scope="col">제목</th>		
									                                   <th scope="col" width="20%">팝업 기간</th>
									                                   <th scope="col" width="10%">타입</th>
									                                   <th scope="col" width="10%">상용</th>
									                                   <th scope="col" width="10%">검수</th>				
									                                   <th scope="col" width="10%">등록일</th>
																</c:if>
																<c:if test="${notiListVO.bbs_gbn=='EV'}">
																	<c:set var="columnCnt" value="7" /> 
																 		<th scope="col">제목</th>		
																	   <th scope="col" width="20%">이벤트 기간</th>	
									                                   <th scope="col" width="10%">타입</th>					                                									                                   
									                                   <th scope="col" width="8%">상단 여부</th>
									                                   <th scope="col" width="10%">New 마감일</th>
									                                   <th scope="col" width="10%">검수</th>									                                   
									                                   <th scope="col" width="10%">등록일</th>
																</c:if>
															</c:when>														
															</c:choose>
						                                  
						                                </tr>
						                                
						                                <c:if test="${notiVO == '[]' }"> 
							                                <tr align="center">
							                                    <td class="table_td_04" colspan="${columnCnt}">데이터가 존재 하지 않습니다.</td>					                                    
							                                </tr>
														</c:if>
					                                </tbody>
					                          
					                                <c:forEach items="${notiList}" var="noti">
					                            	<c:set var="i" value="${i+1}" />
					                            	
						                            	<tr align="center">
						                            		<!-- 체크 박스 -->
						                                    <td class="table_td_04" >
						                                    	<input type="checkbox" class="checkbox" value="${noti.REG_NO}" />
						                                    </td>
						                                    
						                                    <!-- 번호 -->
						                                    <td class="table_td_04" >
						                                   		<%-- ${(notiListVO.pageCount-(notiListVO.pageNum-1* notiListVO.pageSize))-i} --%>
						                                   		
						                                   		${notiListVO.pageCount-(noti.ROWNO-1)}
						                                    </td>
						                                    
						                                    <!-- 제목 -->						                                    						                                    
						                                    <td class="table_td_04 EllipsText">
						                                    	<a href="javascript:formUpdate(${noti.REG_NO})" class="EllipsText" >${noti.TITLE}</a>						                                    	 
						                                    </td>
						                                    
						                                    <!--  기간 -->
						                                    <td class="table_td_04 ">
						                                    	${noti.S_DATE} ~ ${noti.E_DATE}
						                                    </td>
						                                    <!-- 타입 -->
						                                    <td class="table_td_04" >
						                                    	<!-- 별도 테이블화 할 필요성을 못느껴서 하드코딩 -->
					                                    		<c:choose>
					                                    			<c:when test="${noti.EV_TYPE=='ev1'}">
							                                    		VOD 상세정보
							                                    	</c:when>
					                                    			<c:when test="${noti.EV_TYPE=='ev2'}">
							                                    		월정액 가입
							                                    	</c:when>
					                                    			<c:when test="${noti.EV_TYPE=='ev3'}">
							                                    		참여하기
							                                    	</c:when>
					                                    			<c:when test="${noti.EV_TYPE=='ev4'}">
							                                    		초대하기
							                                    	</c:when>
					                                    			<c:when test="${noti.EV_TYPE=='ev5'}">
							                                    		URL링크
							                                    	</c:when>
					                                    			<c:when test="${noti.EV_TYPE=='ev6'}">
							                                    		특정카테고리
							                                    	</c:when>
					                                    			<c:when test="${noti.EV_TYPE=='ev7'}">
							                                    		공지/이벤트 게시글
							                                    	</c:when>
					                                    			<c:when test="${empty noti.EV_TYPE}">
							                                    		&nbsp;
							                                    	</c:when>
							                                    </c:choose>
						                                    </td>
						                                    
						                                     <!-- 상단 여부 -->
						                                    <td class="table_td_04 ">						   
						                                    	<input type="hidden" name="fixed" value="${noti.IS_FIXED}">                                 	
							                                    <c:choose>						                                    	
							                                    	<c:when test="${notiListVO.bbs_gbn == 'PU'}">
							                                    		<c:choose>
							                                    			<c:when test="${noti.IS_FIXED==0}">
									                                    			<a href="javascript:setPopSettion('${notiListVO.bbs_id}','${noti.REG_NO}','1')"> <span class="button small rosy">대상 </span></a>
									                                    	</c:when>
									                                    	<c:when test="${noti.IS_FIXED==1}">
									                                    			<a href="javascript:setPopSettion('${notiListVO.bbs_id}','${noti.REG_NO}','0')"> <span class="button small blue">적용</span></a>
									                                    	</c:when>
									                                    </c:choose>
							                                    								                                    			
								                                    	</c:when>
								                                   	<c:otherwise>	
								                                   		<c:choose>							                                    
									                                   	<c:when test="${noti.IS_FIXED==0}">
									                                    			<a href="javascript:updateIsFixed('${noti.REG_NO}','1')"><span class="button small rosy"> N</span></a>
									                                   	</c:when>
									                                   	<c:when test="${noti.IS_FIXED==1}">
									                                    			<a href="javascript:updateIsFixed('${noti.REG_NO}','0')"><span class="button small blue"> Y</span></a>
									                                   	</c:when>
									                                   	</c:choose>
								                                   	</c:otherwise>
						                                    	</c:choose>						                                    
						                                    </td>
						                                    
						                                    <!-- NEW 마감일(일반/이벤트 공지) -->
						                                    <c:choose>
															<c:when test="${notiListVO.bbs_gbn == 'EV'}">
																<td class="table_td_04 ">
																	<if test="${not empty noti.N_DATE }">
																		${fn:substring(noti.N_DATE, 0, 10)}
																	</if>
																	<if test="${empty noti.N_DATE }">
																		&nbsp;
																	</if>
																</td>
															</c:when>														
															</c:choose>
															
															<!-- 검수 -->
															<td class="table_td_04 ">
															<input type="hidden" name="adt" value="${noti.IS_ADT}">  
															<c:choose>						                                    	
							                                    	<c:when test="${notiListVO.bbs_gbn == 'PU'}">
							                                    		<c:choose>
							                                    			<c:when test="${noti.IS_ADT==0}">
									                                    			<a href="javascript:updateConfirmAdt('${notiListVO.bbs_id}','${noti.REG_NO}','1')"><span class="button small rosy"> 대상</span></a>
									                                    	</c:when>
									                                    	<c:when test="${noti.IS_ADT==1}">
									                                    			<a href="javascript:updateConfirmAdt('${notiListVO.bbs_id}','${noti.REG_NO}','0')"><span class="button small blue"> 완료</span></a>
									                                    	</c:when>
									                                    </c:choose>							                                    								                                    			
								                                    </c:when>
								                                   	<c:otherwise>	
								                                   		<c:if test="${noti.IS_ADT=='0'}"><a href="javascript:updateConfirmAdt('${notiListVO.bbs_id}','${noti.REG_NO}','1')"> <span class="button small blue"> 확인 </span></a></c:if>
																		<c:if test="${noti.IS_ADT=='1'}">완료</c:if>
								                                   	</c:otherwise>
						                                    	</c:choose>
															</td>
															
															<!-- 등록일 -->
							                                <td class="table_td_04 ">
							                                    	${fn:substring(noti.REG_DT, 0, 10)} 
						                                    </td>
						                                </tr>
					                            	</c:forEach>
					                            </table>
					                            
					                            <!-- 페이징 -->
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
						                            <tbody>
						                            <tr>
						                                <td height="5"></td> 
						                            </tr>
						                            <tr>
						                            	<td align="center">						                            		
						                            		 <jsp:include page="/WEB-INF/views/include/naviControll.jsp">
																<jsp:param value="getNotiList.do" name="actionUrl"/>
																<jsp:param value="?findName=${notiListVO.findName}&findValue=${notiListVO.findValue}&bbs_id=${notiListVO.bbs_id }&bbs_gbn=${notiListVO.bbs_gbn }&scr_tp=${notiListVO.scr_tp}" name="naviUrl" />
																<jsp:param value="${notiListVO.pageNum }" name="pageNum"/>
																<jsp:param value="${notiListVO.pageSize }" name="pageSize"/>
																<jsp:param value="${notiListVO.blockSize }" name="blockSize"/>
																<jsp:param value="${notiListVO.pageCount }" name="pageCount"/>																			  
															</jsp:include> 
						                            	</td>
						                            </tr>
						                        	</tbody>
						                        </table>
						                        
						                        <!-- 등록/수정 버튼 -->
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            <tbody>
						                            <tr>
						                                <td align="right">
						                                	<%-- <a href="/smartux_adm/admin/noti/getNotiView.do?findName=${notiListVO.findName}&findValue=${notiListVO.findValue}&pageNum=${notiListVO.pageNum}"><span class="button small blue"> 등록</span></a> --%>
						                                	<a href="javascript:formRegistration()"><span class="button small blue"> 등록</span></a>
						                                	<span class="button small blue" id="deleteBtn"> 삭제</span>
						                                	<c:if test="${notiListVO.bbs_gbn == 'EV'}">
						                                		<span class="button small blue" id="applybtn">즉시적용</span>
						                                		<span class="button small blue" id="cjapplybtn">CJ 즉시적용</span>
						                                		<span class="button small blue" id="applyMasterIdBtn">부분즉시적용</span>
						                                		<c:if test="${notiListVO.scr_tp == 'T'}">
						                                		<span class="button small blue" id="copyPopBtn">팝업공지로 복사</span>
						                                		</c:if>
						                                	</c:if>
						                                	<c:if test="${notiListVO.bbs_gbn == 'PU'}">
						                                		<span class="button small blue" id="applybtn_pop">팝업 즉시적용</span>
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