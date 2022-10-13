<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>공지사항 상세</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
 
 <link href="/smartux_adm/css/anytime_style.css" rel="stylesheet" type="text/css">
<script src="/smartux_adm/js/anytime.js"></script>

<script type="text/javascript">
	
$(document).ready(function(){
	
	//이미지 파일만 선택
	$("#file").change(function(){
		if($("#file").val()==""){
			return;
		}
		
		if(imageFileCheckNoti($("#file").val())){
			alert("이미지 파일이 아닙니다.");			
		}					    	
	});
	
	$("#termPopup").click(function(){
		termCheck=window.open("<%=webRoot%>/admin/noti/getTerm.do?scr_tp=${notiListVO.scr_tp}", "termCheck", "width=500,height=700,scrollbars=yes" );
		termCheck.opener=self;
	});
	
	AnyTime.picker( "s_date", { format: "%z-%m-%d %H:00",  labelTitle: "날짜",   labelHour: "시간" , time:""} );
	AnyTime.picker( "e_date", { format: "%z-%m-%d %H:00",  labelTitle: "날짜",   labelHour: "시간",  time:""} );
	AnyTime.picker( "win_dt", { format: "%z-%m-%d %H:00",  labelTitle: "날짜",   labelHour: "시간"} );
	AnyTime.picker( "n_date", { format: "%z-%m-%d",  labelTitle: "날짜",   labelHour: "시간"} );
	
	$(".checkbox").click(function (e) {				
		if($(".checkbox").is(":checked")){
			$("#terminal_div").html("");
			$("#termPopup").attr("style","display:none");
		}else{			
			$("#termPopup").attr("style","display:block");	
		}
	});
	
	$("#ev_type").change(function(){
		var ev_type = this.value;
		if(ev_type=="ev1"){
			$(".eve_ev1").show();
			$(".eve_ev2").hide();
			$(".eve_ev7").hide();
			$("#ev_detail_prod").val("");
		}else if(ev_type=="ev7"){
			$(".eve_ev7").show();
			$(".eve_ev1").hide();
			$(".eve_ev2").hide();
			$("#ev_detail_prod").val("");
		}else if(ev_type=="ev2"){
			$("#ev2_5_txt").html("가입상품 번호");
			$(".eve_ev1").hide();
			$(".eve_ev7").hide();
			$(".eve_ev2").show();
			$("#ev_detail_prod").val("");
			$("#ev_detail_cont").val("");
			$("#div_choiceContents").html("");
		}else if(ev_type=="ev5"){
			$("#ev2_5_txt").html("URL링크");
			$(".eve_ev1").hide();
			$(".eve_ev7").hide();
			$(".eve_ev2").show();
			$("#ev_detail_prod").val("");
			$("#ev_detail_cont").val("");
			$("#div_choiceContents").html("");
		}else if(ev_type=="ev6"){
			$("#ev2_5_txt").html("특정카테고리");
			$(".eve_ev1").hide();
			$(".eve_ev7").hide();
			$(".eve_ev2").show();
			$("#ev_detail_prod").val("");
			$("#ev_detail_cont").val("");
			$("#div_choiceContents").html("");
		}else{
			$(".eve_ev1").hide();
			$(".eve_ev2").hide();
			$(".eve_ev7").hide();
			$("#ev_detail_prod").val("");
			$("#ev_detail_cont").val("");
			$("#div_choiceContents").html("");
			$("#div_choiceNotices").html("");
		}
	});
	
	
	$("#contentBtn").click(function(){
		var url = "<%=webRoot%>/admin/commonMng/getCategoryAlbumList.do?categoryId=VC&isTypeChange=Y";
    	category_window = window.open(url, "getCategoryAlbumList", "width=800,height=330,scrollbars=yes");
	});
	
	$("#noticeBtn").click(function(){
	    var url = '<%=webRoot%>/admin/noti/getNotiPopList.do?scr_tp=T&bbs_id=&bbs_gbn=EV&hiddenName=noti_detail&textName=noti_detail_name&delimiter=comma&textHtml=noti_title';
	    notice_window = window.open(url, 'getNotiPopList', 'width=800,height=600,scrollbars=yes');
	});

}); 
/*
 * basic_script.js imageFileCheck() 공통로직으로 사용되고 있어 해당페이지 전용으로 추가함.
 * 변경내용 : 확장자(webp) 추가
*/
function imageFileCheckNoti(filename){
	
	   var fileName=filename;	 
	   var fileSuffix =fileName.substring(fileName.lastIndexOf(".") + 1);
	   var browser = navigator.userAgent.toLowerCase();
	   
	   fileSuffix = fileSuffix.toLowerCase();
	   
	    if (!( "jpg" == fileSuffix || "jpeg" == fileSuffix  || "gif" == fileSuffix || "bmp" == fileSuffix || "png" == fileSuffix || "webp" == fileSuffix )){
	    	
	    	//크롬일경우
	    	if(-1 != browser.indexOf("chrome")){
	    		$("#file")[0].select();
	    		$("#file").val("");
	    	}else{ //ie : if(-1 != browser.indexOf("msie")
	    		$("#file").attr("value","");
	    		document.selection.clear();
	    	}
	    			    	
	    	
	    	return true;
	    }
	    return false;
}

function goInsert(){
	
 	var f = document.getElementById("form1");
 	
 	if($("#s_date").val().split(" ").join("")>=$("#e_date").val().split(" ").join("")){		
		alert("종료일시가 시작일시보다 빠르거나 같습니다.");
		return;
	}
	
 	if(f.s_date==""){
 		alert("시작일시를 입력해 주세요."); 		
 		f.s_date.focus();
 		return;
 	} 
 	
 	if(f.e_date==""){
 		alert("종료일시를 입력해 주세요."); 		
 		f.e_date.focus();
 		return;
 	}
	
 	if(f.title.value == ''){
		alert('제목을 입력해 주세요.');
		f.title.focus();
		return;	
		/*2020-06-25 컨텐츠 벨리데이션 제거 */
	}else if(f.content.value == ''){
		/*이미지가 존재하지 않으면 컨텐츠 값 필수*/
		var fileCheck = $("#file").val();
		var beForFilecheck = $("#org_file_nm").val();
		if(!fileCheck&&!beForFilecheck){
			alert("이미지가 없는 경우는 내용을 입력해주세요.");
			f.content.focus();
			return;
		}
	}	 	
 	
    if(textareaCheck(f.content.value)){
 		var check=textareaCheck(f.content.value)
 		alert(check+"는 입력할수 없습니다."); 		
  		f.content.focus();
  		return;
  	}
	if($("input[name=terminal_list]").length<=0){	
		if(!($(".checkbox").is(":checked"))){
			alert("단말기를 선택해 주세요");
			return;
		}	
	}

    if(f.ev_type.value == 'ev1' && f.ev_detail_cont.value==""){//컨텐츠 연동
		alert('컨텐츠를 1개이상 선택해 주세요.');
		f.ev_detail_cont.focus();
		return;
  	}
    if(f.ev_type.value == 'ev5' && f.ev_detail_prod.value==""){//URL링크
		alert('링크할 URL을 입력해 주세요.');
		f.ev_detail_prod.focus();
		return;
  	}
    if(f.ev_type.value == 'ev6' && f.ev_detail_prod.value==""){//특정카테고리
		alert('카테고리를 입력해 주세요.');
		f.ev_detail_prod.focus();
		return;
  	}
    
	$("#form1").attr("action","/smartux_adm/admin/noti/createNoti.do");
	
	$.blockUI({
		blockMsgClass: "ajax-loading",
		showOverlay: true,
		overlayCSS: { backgroundColor: '#CECDAD' } ,
		css: { border: 'none' } ,
		 message: "<b>로딩중..</b>"
	});
	
	f.submit();	
}

function CheckNumber(day){
	var chk_num = day.search(/[0-9]/g);
	
	if(chk_num < 0){
		alert('만료일은 숫자를 입력하여야 합니다.');
        return false;
	}
	return true;
}

function CheckKeys(){
    if ( event.keyCode < 48 || event.keyCode > 57 )
    {
         event.keyCode = 0;
    }
}

function winopen(imagePath,Width,Height){	
	
	window.open(imagePath,"windowName","toolbar=no,scrollbars=yes,resizable=no, top=20,left=200,width="+Width+",height="+Height);
}

function deleteImage(reg_no){
	
	 var msg="이미지를 삭제하시겠습니까?";
			
	if(confirm(msg)){
		
		$("#delete_img_flag").val('Y');
		$("#image").remove();
	/*
		$("#save_file_nm").attr("value","");
		$("#org_file_nm").attr("value","");
	
		$.ajax({
		    url: '/smartux_adm/admin/noti/deleteImageFile.do',
		    type: 'POST',
		    dataType: 'json',
		    data: {
		        "reg_no": reg_no,
		        "save_file_nm" : save_file_nm,
		        "org_file_nm"  : org_file_nm
		    },
		    error: function(){
		    	alert("접속 할  수 없습니다.");
		    },
		    success: function(textDoc){		    	
		    	var flag=textDoc.flag;
		    	var message=textDoc.message;
		    	if(flag=="0000"){
		    		$("#image").remove();
		    		alert("이미지가 삭제되었습니다.");
		    	}else{
		    		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);		    		
		    	}
		    }
		});
	*/
	} 
}
	 
</script>
</head>

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
						                                             <c:if test="${notiListVO.bbs_gbn=='PU'}"> <td class="bold">팝업공지/이벤트 등록</td></c:if>
						                                           <c:if test="${notiListVO.bbs_gbn=='GN'}"> <td class="bold">일반공지 등록</td></c:if>
						                                           <c:if test="${notiListVO.bbs_gbn=='EV'}"> <td class="bold">공지/이벤트 등록</td></c:if>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="form1" action="/smartux_adm/admin/noti/createNoti.do" method="post" enctype="multipart/form-data">
						                        <input type="hidden" name="findName" value="${notiListVO.findName}" />
						                        <input type="hidden" name="findValue" value="${notiListVO.findValue}" />
						                        <input type="hidden" name="pageNum" value="${notiListVO.pageNum}" />
						                        <input type="hidden" name="bbs_id" value="${notiListVO.bbs_id}" />						                        
						                        <input type="hidden" name="bbs_gbn" value="${notiListVO.bbs_gbn}" />						                          
						                        <input type="hidden" name="isUpdate" value="${isUpdate}" />
						                        <input type="hidden" name="reg_no" value="${reg_no}" />
						                        <input type="hidden" name="scr_tp" value="${notiListVO.scr_tp}" />
						                        
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" class="board_data">
					                                <tbody>		
			                                		<c:choose>
														<c:when test="${notiListVO.bbs_gbn=='PU' || notiListVO.bbs_gbn == 'EV'}">
														
															<!-- 팝업공지 일 경우 -->
															<c:if test="${notiListVO.bbs_gbn=='PU'}">
															
								                                  
								                                <!-- 공지 일시 -->					                             	
								                                  <tr align="center">
								                                    <th width="25%">공지 일시</th>
								                                    <td width="5%"></td>								                                    
								                                    <td width="70%" align="left" >
								                                    	<c:if test="${isUpdate=='0'}">
								                                    		시작일시:<input type="text" id="s_date" name="s_date" size="20"  value="${currentDate}" title="시작일시"/>
									                                    	종료일시:<input type="text" id="e_date" name="e_date" size="20"  value="${nextWeekDate}" title="종료일시"/>
								                                    	</c:if>
								                                    	<c:if test="${isUpdate=='1'}">
								                                    		<!-- 수정일경우 팝업 고정여부 및 검수여부 -->
								                                    		<input type="hidden" name="is_fixed" value="${notiVO.is_fixed}" />
								                                    		<input type="hidden" name="is_adt" value="${notiVO.is_adt}" />
								                                    		<!-- 공지일시 -->		
									                                    	시작일시:<input type="text" id="s_date" name="s_date" size="20"  value="${fn:substring(notiVO.s_date, 0, 16)}" title="시작일시"/>
									                                    	종료일시:<input type="text" id="e_date" name="e_date" size="20"  value="${fn:substring(notiVO.e_date, 0, 16)}" title="종료일시"/>
								                                    	</c:if>
																	</td>									
																 </tr>			
																 <!-- 제목 -->
								                                <tr align="center">
								                                    <th width="25%">제목</th>
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
																		<input type="text" name="title" size="35" style="font-size: 12px;" value="${notiVO.title}"onKeyUp="checkByte($(this),'50')"/>
																		<br> 사용제한 / 50byte 까지 허용					
																	</td>
								                                </tr>
								                                
								                                <!-- 내용 -->
								                                <tr align="center" >
								                                    <th width="25%">공지 내용</th>
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >								                                  
																		<textarea rows="10" cols="40" name="content" onKeyUp="checkByte($(this),'2000')" >${notiVO.content}</textarea>
																		  <br>[\f, !^, \b, \f88, \f99]  사용제한 / 2000byte 까지 허용
																											
																	</td>
								                                </tr>
								                                 <!-- 노출 단말 선택 -->
								                                <tr align="center" >
								                                    <th width="25%">노출 단말 선택</th>
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >		
								                                    
								                                   		<c:if test="${notiTerminal==null || notiTerminal=='[]'}"> 	
								                                   		 		All <input type="checkbox"  class="checkbox" checked="checked">								                                   			       
								                                   				<a href="#" id="termPopup" style="display:none;"><span class="button small blue" > 단말기 선택</span> </a>	    
								                                   		</c:if>
									                                    	
																		<c:if test="${!(notiTerminal==null || notiTerminal=='[]')}">
																			 	All <input type="checkbox"  class="checkbox">									                                    										                                     
										                                   		<a href="#" id="termPopup"  style="display:block;"><span class="button small blue" > 단말기 선택</span> </a>												                                    
																		</c:if>
																		
																		<div  id="terminal_div">									                                    	                                 
									                                    	<c:forEach items="${notiTerminal}" var="term">
									                                    		<input type='hidden' name='terminal_list' value="${term.TERM_MODEL}">
									                                    		<c:set var="i" value="${i+1}" />
			                                            				 			${term.TERM_MODEL} /      			                                            				 			
			                                            				 			<c:choose>
									                                    			<c:when test="${ i%5==0}"><br></c:when>
									                                    			</c:choose>	 
			                                            					</c:forEach>
									                                    </div>
																		</td>													
								                                </tr>
								                                
								                                <!-- 노출망 선택 -->
								                                <tr align="center" >
								                                    <th width="25%">노출망 선택</th>
								                                    <td width="5%"></td>
								                                    <td align="left" >
																		<select id="show_type" name="show_type">
																			<c:forEach var="item" items="${netTypeList}" varStatus="status">
																				<c:choose>
																					<c:when test="${item.item_code == notiVO.show_type}">
																						<option value="${item.item_code}" selected>${item.item_nm }</option>
																					</c:when>
																					<c:otherwise>
																						<option value="${item.item_code}">${item.item_nm }</option>
																					</c:otherwise>	
																				</c:choose>
																				
																			</c:forEach>
																		</select>				
																	</td>												
								                                </tr>
								                                
								                                <!-- 이미지 파일 -->					                                
								                                <tr align="center">
								                                    <th width="25%">이미지</th>
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
																		<input type="file" id="file"   accept="image/*" name="file" value="파일선택" />
																		<c:if test="${ notiVO.org_file_nm!=null}"> 
																			<div id="image">
																				 <%-- <br><a href="javascript:imgOpen('${ notiVO.save_file_nm}')">${ notiVO.org_file_nm}</a> --%> 																				
																				  <br><a href="javascript:winopen('${ notiVO.save_file_nm}','200','300')">${ notiVO.org_file_nm}</a>
																				<%-- <br><a href="javascript:winopen('http://static.naver.net/www/u/2010/0611/nmms_215646753.gif','200','300')">${ notiVO.org_file_nm}</a> --%>
																				<%-- <br>${ notiVO.org_file_nm} --%>																				 
																				<a href="javascript:deleteImage('${ notiVO.reg_no}')"><span class="button small rosy">삭제</span></a>
																			</div>
																			<input type="hidden" id="org_file_nm" name="org_file_nm" value="${ notiVO.org_file_nm}">
																			<input type="hidden" id="save_file_nm" name="save_file_nm" value="${ notiVO.save_file_nm}">																			
																		</c:if>
																	</td>
					                                			</tr>      					                                
								                                <!-- 이벤트 컨텐츠 ID -->
								                                <tr align="center">
								                                    <th width="25%">이벤트 컨텐츠</th> 
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
																		<input type="text" name="ev_cont_id" size="35" onKeyUp="checkByte($(this),'200')" style="font-size: 12px;" value="${notiVO.ev_cont_id}"/>															 							
																	</td>
								                                </tr>	
								                                <!-- 이동경로 -->
								                                <tr align="center">
								                                    <th width="25%">이동 경로</th> 
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
																		<select name="ev_type" id="ev_type">
																			<option value="" <c:if test="${notiVO.ev_type == ''}">selected="selected"</c:if>>::경로 없음::</option>
																			<option value="ev1" <c:if test="${notiVO.ev_type == 'ev1'}">selected="selected"</c:if>>VOD상세 정보</option>
																			<option value="ev2" <c:if test="${notiVO.ev_type == 'ev2'}">selected="selected"</c:if>>월정액 가입</option>
																			<option value="ev3" <c:if test="${notiVO.ev_type == 'ev3'}">selected="selected"</c:if>>참여하기</option>
																			<option value="ev4" <c:if test="${notiVO.ev_type == 'ev4'}">selected="selected"</c:if>>초대하기</option>
																			<option value="ev5" <c:if test="${notiVO.ev_type == 'ev5'}">selected="selected"</c:if>>외부 URL</option>
																			<option value="ev6" <c:if test="${notiVO.ev_type == 'ev6'}">selected="selected"</c:if>>특정카테고리</option>
																			<option value="ev7" <c:if test="${notiVO.ev_type == 'ev7'}">selected="selected"</c:if>>공지/이벤트 게시글</option>
																		</select>								 							
																	</td>
								                                </tr>
								                                
								                                <!-- 이벤트 컨텐츠 ID -->
								                                <tr align="center">
								                                    <th width="25%">참여통계</th> 
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
																		<select name=ev_stat_id id="ev_stat_id">
																		    <option value="">::선택::</option>
																			<c:forEach items="${sbox_vo}" var="s_rec">
																		    	<option value="${s_rec.stat_no}" <c:if test="${notiVO.ev_stat_id == s_rec.stat_no}">selected="selected"</c:if>>${s_rec.title}</option>
											                            	</c:forEach>
																		</select>														 							
																	</td>
								                                </tr>
																<!-- 노출 타입 2020-06-05추가-->
								                                <tr align="center">
								                                    <th width="25%">노출타입</th> 
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
																		<select name=display_type id="display_type">
																			<option value="A"    <c:if test="${notiVO.display_type == ''||notiVO.display_type == 'A'}">selected="selected"</c:if>>매일반복</option>
																			<option value="O"    <c:if test="${notiVO.display_type == 'O'}">selected="selected"</c:if>>1회반복</option>
																		</select>														 							
																	</td>
								                                </tr>										                                
								                                <!-- 가입하기 번호 -->
								                                <tr align="center" class="eve_ev2" <c:if test="${notiVO.ev_type != 'ev2' && notiVO.ev_type != 'ev5' && notiVO.ev_type != 'ev6'}">style="display:none;"</c:if>>
								                                    <th width="25%">
								                                    	<span id="ev2_5_txt">
								                                    		<c:if test="${notiVO.ev_type == 'ev2'}">가입상품 번호</c:if>
								                                    		<c:if test="${notiVO.ev_type == 'ev5'}">URL입력</c:if>
								                                    		<c:if test="${notiVO.ev_type == 'ev6'}">특정카테고리</c:if>
								                                    	</span>
								                                    </th> 
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
																		<input type="text" name="ev_detail_prod" id="ev_detail_prod" size="50" maxlength="100" style="font-size: 12px;" <c:if test="${notiVO.ev_type == 'ev2' || notiVO.ev_type == 'ev5' || notiVO.ev_type == 'ev6'}">value="${notiVO.ev_detail}"</c:if>/>															 							
																	</td>
								                                </tr>
								                                
								                                 <!-- 컨텐츠 선택 -->
								                                <tr align="center" class="eve_ev1"  <c:if test="${notiVO.ev_type != 'ev1'}">style="display:none;"</c:if>>
								                                    <th width="25%">이동경로 상세</th> 
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
																		<span class="button small blue" id="contentBtn">VOD 선택</span>									 							
																	</td>
								                                </tr>
								                                <tr align="center" class="eve_ev1"  <c:if test="${notiVO.ev_type != 'ev1'}">style="display:none;"</c:if>>
								                                    <th width="25%">&nbsp;</th> 
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
								                                    	<input type="hidden" name="ev_detail_cont" id="ev_detail_cont" <c:if test="${notiVO.ev_type == 'ev1'}">value="${notiVO.ev_detail}"</c:if>>
								                                    	<div id="div_choiceContents">
							                                				<c:forEach items="${choiceContent}" var="chCnt">
							                            						<c:set var="cntNum" value="${cntNum+1}" />
							                            						<c:if test="${cntNum == 1}">
							                                						${chCnt}
							                            						</c:if>
							                            						<c:if test="${cntNum != 1}">
							                                						<br/>${chCnt}
							                            						</c:if>
							                                				</c:forEach>
								                                    	</div>								 							
																	</td>
								                                </tr>
								                                <!-- 공지/이벤트 게시판 선택 -->
								                                <tr align="center" class="eve_ev7"  <c:if test="${notiVO.ev_type != 'ev7'}">style="display:none;"</c:if>>
								                                    <th width="25%">이동경로 상세</th> 
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
																		<span class="button small blue" id="noticeBtn">공지/이벤트 게시글 선택</span>									 							
																	</td>
								                                </tr>
								                                <tr align="center" class="eve_ev7"  <c:if test="${notiVO.ev_type != 'ev7'}">style="display:none;"</c:if>>
								                                    <th width="25%">&nbsp;
									                                    <c:set var="delims_arr" value="${fn:split(notiVO.ev_detail,',')}"/>
																		<c:forEach var="x" items="${delims_arr}" varStatus="s">
																			<c:if test="${s.count==1}"><c:set var="noti_tile" value="${x}"/></c:if>
																		</c:forEach>
								                                    	<input type="hidden" name="noti_detail" id="noti_detail" <c:if test="${notiVO.ev_type == 'ev7'}">value="${notiVO.ev_detail}"</c:if>>
								                                    	<input type="hidden" name="noti_detail_name" id="noti_detail_name">
								                                    </th> 
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" id="noti_title">${noti_tile}
								                                    </td>
								                                </tr>
															</c:if>
															
															<!-- 이벤트공지 일 경우 -->
															<c:if test="${notiListVO.bbs_gbn=='EV'}">
																<input type="hidden" name="is_adt" value="${notiVO.is_adt}" />
															  <!-- 공지 일시 -->					                             	
								                                <tr align="center">
								                                    <th width="25%">이벤트 기간</th>
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >		
								                                    	<c:if test="${isUpdate=='0'}">
								                                    		시작일시:<input type="text" id="s_date" name="s_date" size="20"  value="${currentDate}" title="시작일시"/>
									                                    	종료일시:<input type="text" id="e_date" name="e_date" size="20"  value="${nextWeekDate}" title="종료일시"/>
								                                    	</c:if>
								                                    	<c:if test="${isUpdate=='1'}">		
									                                    	시작일시:<input type="text" id="s_date" name="s_date" size="20"  value="${fn:substring(notiVO.s_date, 0, 16)}" title="시작일시"/>
									                                    	종료일시:<input type="text" id="e_date" name="e_date" size="20"  value="${fn:substring(notiVO.e_date, 0, 16)}" title="종료일시"/>
								                                    	</c:if>
																			
																	</td>									
																 </tr>					
																
								                                
								                                <!-- New 마감일 -->
								                                <tr align="center">
								                                    <th width="25%">New 마감일</th>
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
																			<input type="text" name="n_date" id="n_date" size="20"  value="${fn:substring(notiVO.n_date, 0,10)}" title="new 마감일"/>
																	</td>													
								                                </tr>
								                                
								                                <!-- 제목 -->
								                                <tr align="center"  >
								                                    <th width="25%">제목</th>
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
																		<input type="text" name="title" size="35" style="font-size: 12px;" value="${notiVO.title}"onKeyUp="checkByte($(this),'50')"/>
																		<br> 사용제한 / 50byte 까지 허용							
																	</td>
								                                </tr>
								                                
								                                <!-- 내용 -->
								                                <tr align="center" >
								                                    <th width="25%">이벤트 내용</th>
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
																		<textarea rows="10" cols="40" name="content" onKeyUp="checkByte($(this),'2000')" >${notiVO.content}</textarea>
																		<br>[\f, !^, \b, \f88, \f99]  사용제한 / 2000byte 까지 허용								
																	</td>
								                                </tr>
								                                
								                                   <!-- 노출 단말 선택 -->
								                                <tr align="center" >
								                                    <th width="25%">노출 단말 선택</th>
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >		
								                                    
								                                   		<c:if test="${notiTerminal==null || notiTerminal=='[]'}"> 	
								                                   		 		All <input type="checkbox"  class="checkbox" checked="checked">								                                   			       
								                                   				<a href="#" id="termPopup" style="display:none;"><span class="button small blue" > 단말기 선택</span> </a>	    
								                                   		</c:if>
									                                    	
																		<c:if test="${!(notiTerminal==null || notiTerminal=='[]')}">
																			 	All <input type="checkbox"  class="checkbox">									                                    										                                     
										                                   		<a href="#" id="termPopup"  style="display:block;"><span class="button small blue" > 단말기 선택</span> </a>												                                    
																		</c:if>
																		
																		<div  id="terminal_div">									                                    	                                 
									                                    	<c:forEach items="${notiTerminal}" var="term">
									                                    		<input type='hidden' name='terminal_list' value="${term.TERM_MODEL}">
									                                    		<c:set var="i" value="${i+1}" />
			                                            				 			${term.TERM_MODEL} /      			                                            				 			
			                                            				 			<c:choose>
									                                    			<c:when test="${ i%5==0}"><br></c:when>
									                                    			</c:choose>	 
			                                            					</c:forEach>
									                                    </div>
																		</td>													
								                                </tr>
								                                
								                                <!-- 이미지 파일 -->					                                
								                                 <tr align="center">
								                                    <th width="25%">이미지</th>
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
																		<input type="file" id="file"   accept="image/*" name="file" value="파일선택" onchange="imgHeaderCheck('file', 'eventnoti',${imgSize});"/>
																		<br>${imgSize/1024}mb / ${imgFormat}
																		<c:if test="${ notiVO.org_file_nm!=null}"> 
																			<div id="image">
																				 <%-- <br><a href="javascript:imgOpen('${ notiVO.save_file_nm}')">${ notiVO.org_file_nm}</a> --%> 																				
																				  <br><a href="javascript:winopen('${ notiVO.save_file_nm}','200','300')">${ notiVO.org_file_nm}</a>
																				<%-- <br><a href="javascript:winopen('http://static.naver.net/www/u/2010/0611/nmms_215646753.gif','200','300')">${ notiVO.org_file_nm}</a> --%>
																				<%-- <br>${ notiVO.org_file_nm} --%>																				 
																				<a href="javascript:deleteImage('${ notiVO.reg_no}')"><span class="button small rosy">삭제</span></a>
																			</div>
																			<input type="hidden" id="org_file_nm" name="org_file_nm" value="${ notiVO.org_file_nm}">
																			<input type="hidden" id="save_file_nm" name="save_file_nm" value="${ notiVO.save_file_nm}">
																			<input type="hidden" id="delete_img_flag" name="delete_img_flag" value="N">
																		</c:if>
																	</td>
					                                			</tr> 
					                                			
					                                			<!-- 상단 고정 여부 -->
					                                			<c:if test="${isUpdate=='0'}">
									                                <tr align="center" >
									                                    <th width="25%">상단 고정 여부</th>
									                                    <td width="5%"></td>
									                                    <td width="70%" align="left" >																
																			<select name="is_fixed">
																				<option value="0" selected="selected">N</option>
																				<c:if test="${isFixedCheck=='Y'}"><option value="1">Y</option>	</c:if>
																			</select>
																		</td>								                                
									                                </tr>
								                                </c:if>
								                                <c:if test="${isUpdate=='1'}">
								                                 <tr align="center" >
									                                    <th width="25%">상단 고정 여부</th>
									                                    <td width="5%"></td>
									                                    <td width="70%" align="left" >																
																			<select name="is_fixed">
																				<c:if test="${isFixedCheck=='Y'}">
																					<option value="0" <c:if test="${notiVO.is_fixed==0}">selected="selected" </c:if>>N</option>
																					<option value="1" <c:if test="${notiVO.is_fixed==1}">selected="selected" </c:if>>Y</option>
																				</c:if>
																				<c:if test="${isFixedCheck=='N'}">
																					<c:if test="${notiVO.is_fixed==1}">
																						<option value="1" selected="selected">Y</option>
																						<option value="0">N</option>
																					</c:if>
																					<c:if test="${notiVO.is_fixed==0}">
																						<option value="0">N</option>
																					</c:if>
																				</c:if>
																			</select>
																		</td>								                                
									                             </tr>
								                                </c:if>
								                                
								                                <!-- 상시 게시 여부 -->
								                                <tr align="center">
								                                    <th width="25%">상시 게시 여부</th>
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
								                                    	<select name="isPermanent">
																			<option value="N" <c:if test="${isPermanent=='N'}">selected="selected" </c:if>>N</option>
																			<option value="Y" <c:if test="${isPermanent=='Y'}">selected="selected" </c:if>>Y</option>
																		</select>
																		&nbsp;  <font color="red">* 등록/수정일 관계없이 캐시에 상시노출</font>
																	</td>													
								                                </tr>    
											                                					                                
								                                <!-- 이벤트 컨텐츠 ID -->
								                                <tr align="center">
								                                    <th width="25%">이벤트 컨텐츠</th> 
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
																		<input type="text" name="ev_cont_id" size="35" onKeyUp="checkByte($(this),'200')" style="font-size: 12px;" value="${notiVO.ev_cont_id}"/>													 							
																	</td>
								                                </tr>			
								                                
								                                 <!-- 당첨자 발표 -->
								                                <tr align="center">
								                                    <th width="25%">당첨자 발표</th>
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
								                                    	<input type="text" name="win_dt" id="win_dt" size="20"  value="${fn:substring(notiVO.win_dt, 0, 16)}" title="당첨자 발표"/>																		
																	</td>													
								                                </tr>
								                                <!-- 이동경로 -->
								                                <tr align="center">
								                                    <th width="25%">이동 경로</th> 
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
																		<select name="ev_type" id="ev_type">
																			<option value="" <c:if test="${notiVO.ev_type == ''}">selected="selected"</c:if>>::경로 없음::</option>
																			<option value="ev1" <c:if test="${notiVO.ev_type == 'ev1'}">selected="selected"</c:if>>VOD상세 정보</option>
																			<option value="ev2" <c:if test="${notiVO.ev_type == 'ev2'}">selected="selected"</c:if>>월정액 가입</option>
																			<option value="ev3" <c:if test="${notiVO.ev_type == 'ev3'}">selected="selected"</c:if>>참여하기</option>
																			<option value="ev4" <c:if test="${notiVO.ev_type == 'ev4'}">selected="selected"</c:if>>초대하기</option>
																			<option value="ev5" <c:if test="${notiVO.ev_type == 'ev5'}">selected="selected"</c:if>>외부 URL</option>
																			<option value="ev6" <c:if test="${notiVO.ev_type == 'ev6'}">selected="selected"</c:if>>특정카테고리</option>
																			<option value="ev7" <c:if test="${notiVO.ev_type == 'ev7'}">selected="selected"</c:if>>공지/이벤트 게시글</option>
																		</select>														 							
																	</td>
								                                </tr>
								                                
								                                <!-- 이벤트 컨텐츠 ID -->
								                                <tr align="center">
								                                    <th width="25%">참여통계</th> 
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
																		<select name=ev_stat_id id="ev_stat_id">
																		    <option value="">::선택::</option>
																			<c:forEach items="${sbox_vo}" var="s_rec">
																		    	<option value="${s_rec.stat_no}" <c:if test="${notiVO.ev_stat_id == s_rec.stat_no}">selected="selected"</c:if>>${s_rec.title}</option>
											                            	</c:forEach>
																		</select>														 							
																	</td>
								                                </tr>
								                                <!-- 노출 타입 2020-06-05추가-->
								                                <tr align="center">
								                                    <th width="25%">노출타입</th> 
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
																		<select name=display_type id="display_type">
																			<option value="A"    <c:if test="${notiVO.display_type == ''||notiVO.display_type == 'A'}">selected="selected"</c:if>>매일반복</option>
																			<option value="O"    <c:if test="${notiVO.display_type == 'O'}">selected="selected"</c:if>>1회반복</option>
																		</select>														 							
																	</td>
								                                </tr>							                                
								                                <!-- 가입하기 번호 -->
								                                <tr align="center" class="eve_ev2" <c:if test="${notiVO.ev_type != 'ev2' && notiVO.ev_type != 'ev5' && notiVO.ev_type != 'ev6'}">style="display:none;"</c:if>>
								                                    <th width="25%">
								                                    	<span id="ev2_5_txt">
								                                    		<c:if test="${notiVO.ev_type == 'ev2'}">가입상품 번호</c:if>
								                                    		<c:if test="${notiVO.ev_type == 'ev5'}">URL  입력</c:if>
								                                    		<c:if test="${notiVO.ev_type == 'ev6'}">특정카테고리</c:if>
								                                    	</span>
								                                    </th> 
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
																		<input type="text" name="ev_detail_prod" id="ev_detail_prod" size="50" maxlength="100" style="font-size: 12px;" <c:if test="${notiVO.ev_type == 'ev2' || notiVO.ev_type == 'ev5'|| notiVO.ev_type == 'ev6'}">value="${notiVO.ev_detail}"</c:if>/>															 							
																	</td>
								                                </tr>
								                                
								                                <!-- 컨텐츠 선택 -->
								                                <tr align="center" class="eve_ev1"  <c:if test="${notiVO.ev_type != 'ev1'}">style="display:none;"</c:if>>
								                                    <th width="25%">이동경로 상세</th> 
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
																		<span class="button small blue" id="contentBtn">VOD 선택</span>									 							
																	</td>
								                                </tr>
								                                <tr align="center" class="eve_ev1"  <c:if test="${notiVO.ev_type != 'ev1'}">style="display:none;"</c:if>>
								                                    <th width="25%">&nbsp;</th> 
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
								                                    	<input type="hidden" name="ev_detail_cont" id="ev_detail_cont" <c:if test="${notiVO.ev_type == 'ev1'}">value="${notiVO.ev_detail}"</c:if>>
								                                    	<div id="div_choiceContents">
							                                				<c:forEach items="${choiceContent}" var="chCnt">
							                            						<c:set var="cntNum" value="${cntNum+1}" />
							                            						<c:if test="${cntNum == 1}">
							                                						${chCnt}
							                            						</c:if>
							                            						<c:if test="${cntNum != 1}">
							                                						<br/>${chCnt}
							                            						</c:if>
							                                				</c:forEach>
								                                    	</div>								 							
																	</td>
								                                </tr>
								                                <!-- 공지/이벤트 게시판 선택 -->
								                                <tr align="center" class="eve_ev7"  <c:if test="${notiVO.ev_type != 'ev7'}">style="display:none;"</c:if>>
								                                    <th width="25%">이동경로 상세</th> 
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" >
																		<span class="button small blue" id="noticeBtn">공지/이벤트 게시글 선택</span>									 							
																	</td>
								                                </tr>
								                               <tr align="center" class="eve_ev7"  <c:if test="${notiVO.ev_type != 'ev7'}">style="display:none;"</c:if>>
								                                    <th width="25%">&nbsp;
									                                    <c:set var="delims_arr" value="${fn:split(notiVO.ev_detail,',')}"/>
																		<c:forEach var="x" items="${delims_arr}" varStatus="s">
																			<c:if test="${s.count==1}"><c:set var="noti_tile" value="${x}"/></c:if>
																		</c:forEach>
								                                    	<input type="hidden" name="noti_detail" id="noti_detail" <c:if test="${notiVO.ev_type == 'ev7'}">value="${notiVO.ev_detail}"</c:if>>
								                                    	<input type="hidden" name="noti_detail_name" id="noti_detail_name">
								                                    </th> 
								                                    <td width="5%"></td>
								                                    <td width="70%" align="left" id="noti_title">${noti_tile}
								                                    </td>
								                                </tr>
															</c:if>
														</c:when>														
													</c:choose> 
													</tbody>
					                            </table>
					                            
					                            <!-- 등록/수정 버튼 -->
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                
						                                <c:if test="${isUpdate==0}">	<a href="javascript:goInsert();"><span class="button small blue">등록</span></a></c:if>
						                                <c:if test="${isUpdate==1}">	<a href="javascript:goInsert();"><span class="button small blue">수정</span></a></c:if>
						                                	<a href="/smartux_adm/admin/noti/getNotiList.do?findName=${notiListVO.findName}&findValue=${notiListVO.findValue}&pageNum=${notiListVO.pageNum}&bbs_id=${notiListVO.bbs_id}&bbs_gbn=${notiListVO.bbs_gbn}&scr_tp=${notiListVO.scr_tp}"><span class="button small blue">목록</span></a> 
						                                		
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