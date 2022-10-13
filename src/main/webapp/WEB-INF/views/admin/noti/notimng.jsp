<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>공지관리화면</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>

<script type="text/javascript">
	$(document).ready(function() {
					$("#bbs_Gbn").change(function() {
						
	                    var scr_Type = '${scr_Type}';
						var bbs_gbn = $(
								"#bbs_Gbn option:selected")
								.val();
						// 기존에는 활성화 와 비활성화를 나누어서 했기에 아래와 같이 값을 받았지만 지금은 계속 활성화만 보여주기에..
						/* var del_Yn_Check = $(
						"#del_Yn_Check option:selected").val(); */
						
						var del_Yn_Check = '${del_Yn_Check}';
						location.href = "/smartux_adm/admin/notimng/getNotiList.do?bbs_Gbn="
							+ bbs_gbn+"&del_Yn_Check="+del_Yn_Check+"&scr_tp="+scr_Type;
					});
});
	
	// 게시판 관리 창이 열릴때마다 result 변수를 체크 하여 작업을 수행하고 왔을 경우 
	// 작업의 성공여부를 alert 창을 통해 보여준다. 
	function taskResultCheck(){
		var result = '${result}';
		if(result == ""){	
		}else if(result == "1"){
			alert("작업이 성공 하였습니다.");
		}else if(result == "0"){
			alert(" ERROR가 발생 하였습니다.");
		}else{
			alert(" 게시판 코드가 중복 됩니다.");
		}
	}
	

		
	/*	
		if ((mkey<8 ||mkey>8) &&(mkey<48||mkey>57) &&(mkey<65||mkey>90)&&(mkey<97||mkey>122)&&(mkey<229||mkey>229)) 
		{ 		
			if(mkey==13 || mkey == 16 || mkey == 32 || mkey == 37 || mkey==39){
				
			}else{
			alert("영문 숫자만 가능합니다.");
			alert(mkey);
			}
			//event.returnValue=false;
			return false;
		} 
		
		return true;
		//event.returnValue=true;
		
		*/
		
		 function CheckNumberABC(str) {
			var regx_str = new RegExp("^[0-9a-z\+\=\-\_\)\(\*\&\^\%\$\#\@\!]*$");
			var reState = true;
            
			for(var i=0; i < str.length ; i++) {
				var c = str.charAt(i);
	
				if(!regx_str.test(c)) {
					reState = false;
					break;
				}
			}
			return reState;
		}
		
		function CheckNumberABC2(str) {
			var regx_str = new RegExp("^[0-9a-z]*$");
			var reState = true;
            
			for(var i=0; i < str.length ; i++) {
				var c = str.charAt(i);
	
				if(!regx_str.test(c)) {
					reState = false;
					break;
				}
			}
			return reState;
		}
       
		 function CheckNumberCnt(str){
			 var reState = true;
			 if(str.length>20){
				 reState = false;
			 }
			 
			 return reState;
		 }

  

    
     
	function formUpdate(bbs_id, i) {
		var bbs_nm = $("input[name=" + i + "]").val();
		if(bbs_nm == ""){
			alert("게시판 명이 빈칸입니다.");
		}else{
		var bbs_gbn = $("select[name=bbs_Gbn]").val();
		var scr_Type = '${scr_Type}';
		var del_Yn_Check = '${del_Yn_Check}';
		
		/*  $("input[name=title]").val();
		 $("#id값").attr("value",""); */
		/* location.href = "/smartux_adm/admin/notimng/setUpdateList.do?bbs_Id="
				+ bbs_id + "&bbs_Nm=" + bbs_nm + "&bbs_Gbn=" + bbs_gbn+"&scr_tp="+scr_Type+"&del_Yn_Check="+del_Yn_Check; */
        
		var frm = document.form1;
		
		frm.bbs_Id.value = bbs_id;
		frm.bbs_Nm.value = bbs_nm;
		frm.bbs_Gbn.value = bbs_gbn;
		frm.scr_tp.value = scr_Type;
		frm.del_Yn_Check.value = del_Yn_Check;

		frm.action = "/smartux_adm/admin/notimng/setUpdateList.do";
		
		frm.submit();
		}
	}

	function formDelete(bbs_id, i) {
		var bbs_nm = $("input[name=" + i + "]").val();
		var bbs_gbn = $("select[name=bbs_Gbn]").val();
		var scr_Type = '${scr_Type}';
		var del_Yn_Check = '${del_Yn_Check}';

		/* location.href = "/smartux_adm/admin/notimng/setDeleteList.do?bbs_Id="
				+ bbs_id + "&bbs_Nm=" + bbs_nm + "&bbs_Gbn=" + bbs_gbn+"&scr_tp="+scr_Type+"&del_Yn_Check="+del_Yn_Check; */
		
        var frm = document.form1;
		frm.bbs_Id.value = bbs_id;
		frm.bbs_Nm.value = bbs_nm;
		frm.bbs_Gbn.value = bbs_gbn;
		frm.scr_tp.value = scr_Type;
		frm.del_Yn_Check.value = del_Yn_Check;
		
		frm.action = "/smartux_adm/admin/notimng/setDeleteList.do";
		
		frm.submit();

	}

	function formInsert() {
		var bbs_Id = $("input[name=insertBbsId]").val();
		
		if(!CheckNumberABC2(bbs_Id)){
			$("input[name=insertBbsId]").val("");
			$("input[name=insertBbsId]").focus();
			alert("영문 소문자, 숫자 만 입력이 가능 합니다.");
			return;
		}
		
		if(!CheckNumberCnt(bbs_Id)){
			$("input[name=insertBbsId]").val("");
			$("input[name=insertBbsId]").focus();
			alert("20자 이내로 적어야 합니다.");
			return;
		}
		
		var bbs_nm = $("input[name=insertTitle]").val();
		if(!CheckNumberCnt(bbs_nm)){
			$("input[name=insertTitle]").val("");
			$("input[name=insertTitle]").focus();
			alert("20자 이내로 적어야 합니다.");
			return;
		}
		
		if(bbs_nm == ""){
			alert("게시판 명이 빈칸 입니다.");
		}else if(bbs_Id == ""){
			alert("게시판 코드가 빈칸 입니다.");
		}else{
		var bbs_gbn = $("select[name=bbs_Gbn]").val();
        var scr_Type = '${scr_Type}';
        var del_Yn_Check = '${del_Yn_Check}';

		var frm = document.form1;
		
		frm.bbs_Id.value = bbs_Id;
		frm.bbs_Nm.value = bbs_nm;
		frm.bbs_Gbn.value = bbs_gbn;
		frm.scr_tp.value = scr_Type;
		frm.del_Yn_Check.value = del_Yn_Check;
		
		frm.action = "/smartux_adm/admin/notimng/setInsertList.do";
		
		frm.submit();
		}

	}
	
	/* else if((bbs_Id.search(/[^a-z|^A-Z]/) != -1)){
		alert("게시판 코드는 영문 대문자 와 숫자조합으로 해주세요."); */
	
		// 게시판 목록 관리 창이 열릴 때 마다  taskResultCheck() 메소드를 실행시키게 한다. 
	 window.onload=function(){
		taskResultCheck();
	 } 
</script>
<style type="text/css">
.line {
	border-bottom: 1px solid black;
}
</style>
</head>

<body leftmargin="0" topmargin="0">

<c:if test="${is_none=='Y'}">
		
<script>
alert("생성된 게시판이 없습니다.");
</script>

</c:if>

<form name="form1" method="get" >
	<input type=hidden name="bbs_Id">
	<input type=hidden name="bbs_Nm">
	<input type=hidden name="bbs_Gbn">
	<input type=hidden name="scr_tp">
	<input type=hidden name="del_Yn_Check">
</form>

	<div id="divBody" style="height: 100%">
		<table border="0" cellpadding="0" cellspacing="0"  height="100%" width="100%">
			<tbody>
				<tr>
					<td colspan="2" height="45" valign="bottom">
						<!-- top menu start --> <%@include file="/WEB-INF/views/include/top.jsp"%> <!-- top menu end -->
					</td>
				</tr>
				<tr>
					<td height="10"></td>
					<td></td>
				</tr>
				
				<!-- 시작 -->
				<tr>
					<td colspan="2" valign="top">  <!-- 1번의 td -->
					
						<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
							<tbody>
								<tr>
									<td width="4"></td>
									<td valign="top" width="180">
										<!-- left menu start --> <%@include
											file="/WEB-INF/views/include/left.jsp"%>
										<!-- left menu end -->

									</td>
									
									<td background="/smartux_adm/images/admin/bg_02.gif" width="35">&nbsp;</td>
									<td valign="top">
									
									
										<table border="0" cellpadding="0" cellspacing="0" width="98%">
											<tbody>
												<tr style="display: block">
													<td height="42" width="100%">
													
														<table border="0" cellpadding="0" cellspacing="0"
															width="100%">
															<tbody>
																<tr>
																	<td width="300" class="boldTitle">공지게시판 마스터</td>
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
														
														<table border="0" cellpadding="0" cellspacing="0"
															width="100%" align="center">
															<tbody>
																<tr>
																	<td align="left" style="width: 30px; padding: 5px;">
																		<select id="bbs_Gbn" name="bbs_Gbn">
																			<option
																				<c:if test="${bbs_Gbn=='PU'}">selected="selected"</c:if>
																				value="PU">팝업 공지</option>
																			<option
																				<c:if test="${bbs_Gbn=='EV'}">selected="selected"</c:if>
																				value="EV">이벤트 공지</option>
																	</select>
																	</td>
																	<tr>
															</tbody>
															</tr>
															<!-- 검색 종료 -->
															<tr>
																<td class="3_line" height="1"></td>
															</tr>
															<tr>
																<td height="20"></td>
															</tr>
															<tr>
																<td width="60%">
																<!-- 중심 테이블 -->
																	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list">
																		<tbody>
																		<!--  첫번째 행 의 칼럼별 제목이 들어가는 부분 시작-->
																			<tr align="center">
																				<th scope="col" width="5%">번호</th>
																				<th scope="col" width="10%">게시판코드</th>
																				<th scope="col" width="30%">게시판 명</th>
																				<!--  bbs_Gbn 이 pu 일때는 삭제 컬럼을 생략한다. -->
																				 <c:choose>
																				<c:when test="${bbs_Gbn == 'PU' }">
																				<th scope="col" width="5%"></th>
																				</c:when>
																				<c:otherwise>
																				<th scope="col" width="5%"></th>
																				</c:otherwise>
																				</c:choose> 
																				<th scope="col" width="20%">등록일</th>
																				<th scope="col" width="15%">#</th>
																			</tr>
																			<!--  첫번째 행 의 칼럼별 제목이 들어가는 부분 끝-->
																			<!-- 두번째 행 데이터들이 들어가는 부분 시작 -->
																		     
																		     <!-- 팝업공지의 경우 리스트 가 있을때 는 등록 버튼이 생기지 않게 코딩하였다. -->
																			<c:choose>
																			<c:when test="${listSize != '0' && bbs_Gbn == 'PU'}">
																			</c:when>
																			<c:otherwise>
																			<tr>
																			<td></td>
																			<td><input type="text" name="insertBbsId" maxlength="20" size="17" style="font-size: 12px;ime-mode:disabled;" /></td>
																			<td><input type="text" name="insertTitle"size="35" maxlength="20" style="font-size: 12px;" /></td>
																			<td></td>
																			<td></td>
																			<td align="center">
																			<a href="javascript:formInsert()"><span class="button small blue">등록</span></a></td>
																			</tr>
																			</c:otherwise>
																			</c:choose>
																			
																			
																			
																			<!--  del_Yn_Check 에 따른 정렬방식 선택  
																			        del_Yn_Check 값이 0 이라면 활성화된 게시판만 보여준다. 
																			        del_Yn_Check 값이 1 이라면 비활성화된 게시판만 보여준다. 
																			           현재는 관리자의 요청에 의해 활성화된 게시판만 보여준다.       -->
																			<c:choose>
																			<%-- <c:when test="${del_Yn_Check == 1 }">
																			<c:forEach items="${notiMngList}" var="noti">
																			    <c:if test="${noti.DEL_YN == 1}">
																				<c:set var="i" value="${i+1}" />
																				<tr>
																					<td align="center">${i}</td>
																					<td align="center">${noti.BBS_ID}</td>
																					<td><input type="text" name="${i}" size="35" style="font-size: 12px;" value="${noti.BBS_NM}" /></td>
																					<td align="center">
																					<!--  del yn 에 따라 y , n 으로 나누는 작업도 팝업공지에서는 skip 한다.  -->
																					<!-- 삭제 유무 부분을 관리자의 요청에 따라 아예 안보이게 바꾸었다.  -->
																					<c:if test="${bbs_Gbn != 'PU'}">
                                                                                    <c:choose>
																					<c:when test="${noti.DEL_YN == 0}"> N</c:when>
																					<c:otherwise>Y</c:otherwise>
																					</c:choose>
																					</c:if>
																					</td>
																					<td align="center">${noti.REG_DT}</td>
																					<c:choose>
																					<c:when test="${noti.DEL_YN == '0'}">
																					<td align="center"><a href="javascript:formUpdate('${noti.BBS_ID}','${i}')"><span class="button small blue"> 수정</span></a>
																					<c:if test="${bbs_Gbn != 'PU'}">
																					<a href="javascript:formDelete('${noti.BBS_ID}','${i}')"><span class="button small blue"> 삭제</span></a>
																					</c:if>
																					</td>
																			        </c:when> 
																			        <c:otherwise>
																			        <td></td>
																			       </c:otherwise>
																			       </c:choose> 
																			   </tr>
																			   </c:if>
																			</c:forEach>
																			</c:when> --%>
																			
																			
																		    <%-- <c:otherwise> --%>
																		    
																		    <c:when test="${del_Yn_Check == 0 }">
																			<c:forEach items="${notiMngList}" var="noti">
																				<c:if test="${noti.DEL_YN == 0}">
																				<c:set var="i" value="${i+1}" />
																				<tr>
																					<td align="center">${i}</td>
																					<td align="center">${noti.BBS_ID}</td>
																					<td><input type="text" name="${i}" size="35" style="font-size: 12px;" value="${noti.BBS_NM}" /></td>
																					<td align="center">
																					<!-- del yn 에 따른 y , n 기술 하는 부분은 팝업공지일 경우 생략 한다.  -->
																					<!-- 삭제 유무 부분을 관리자의 요청에 따라 아예 안보이게 바꾸었다.  -->
																					<%-- <c:if test="${bbs_Gbn != 'PU'}">
                                                                                    <c:choose>
																					<c:when test="${noti.DEL_YN == 0}"> N</c:when>
																					<c:otherwise>Y</c:otherwise>
																					</c:choose>
																					</c:if> --%>
																					</td>
																					<td align="center">${noti.REG_DT}</td>
																					<c:choose>
																					<c:when test="${noti.DEL_YN == '0'}">
																					<td align="center"><a href="javascript:formUpdate('${noti.BBS_ID}','${i}')"><span class="button small blue"> 수정</span></a> 
																					<c:if test="${bbs_Gbn != 'PU'}">
																					<a href="javascript:formDelete('${noti.BBS_ID}','${i}')"><span class="button small blue"> 삭제</span></a>
																					</c:if>
																					</td>
																			        </c:when>
																			        </c:choose> 
																			   </tr>
																			   </c:if>
																			</c:forEach>
																			</c:when>
																			<%-- </c:otherwise> --%> 
																			</c:choose>
																			 <!--  del_Yn_Check 종료  -->
																			
																		</tbody>
																	</table>
																</td>
															</tr>
														</table>
														
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
			 </tbody>
		</table>
	</div>						
</body>
</html>