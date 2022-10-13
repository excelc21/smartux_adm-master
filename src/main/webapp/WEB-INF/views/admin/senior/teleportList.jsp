<%@page import="com.dmi.smartux.common.property.SmartUXProperties"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
$(document).ready(function(){
    $('#searchType [value="${searchType}"]').prop('selected', true);
    
    $(".upTel").attr("style", "cursor:pointer");
    $(".upTel").click(function (){
        var code = $(this).parent().find('.code').text();
        teleportUpdate(code);
    });

    // 순서변경
    $("#orderbybtn").click(function (){
        var url = "<%=webRoot%>/admin/teleport/order/update.do";
        window.open(url, "teleportOrder", "width=450,height=300");
    });
	$("#regbtn").click(function(){
		var url = window.open("<%=webRoot%>/admin/teleport/insert.do", "regcodeitem","toolbar=yes,resizable=yes,width=520,height=380");
		url.moveTo(15,50);
	});

    $('#searchbtn').on('click', function(){
        document.form1.submit();
    });

	$("#allchk").click(function(){
		var chkallchecked = false;

		if($("#allchk").is(":checked")){
			chkallchecked = true;
		}
		$("input[name='delchk']").attr("checked", chkallchecked);
	});

	$("#delbtn").click(function(){
		var checkeditems = $("input[name='delchk']:checked");
		var checkeditemslength = checkeditems.length;

		if(checkeditemslength == 0){
			alert("삭제할 항목을 체크해주세요");
		}else{
			if(confirm("체크된 리스트를 삭제하시겠습니까")){
				var size = checkeditemslength;
				var checkboxarray = [];

				for(var i=0; i < size; i++){
					checkboxarray.push($(checkeditems[i]).val());
				}

				$.post("<%=webRoot%>/admin/teleport/delete.do",
						 { order : checkboxarray },
						  function(data) {
							 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
							 	var flag = data.flag;
							 	var message = data.message;

							 	if(flag == "0000"){						// 정상적으로 처리된 경우
							 		alert("순간이동 데이터가 삭제되었습니다");
							 		location.reload();
							 	}else{
							 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
							 	}
						  },
						  "json"
				 );
			}
		}
	});


	$("#activatebtn").click(function(){
		var rank_code = $("#rank_code").val();
		var msg = "순간이동 데이터를 즉시적용 하시겠습니까?";

		if(confirm(msg)){
			$.blockUI({
				blockMsgClass: "ajax-loading",
				showOverlay: true,
				overlayCSS: { backgroundColor: '#CECDAD' } ,
				css: { border: 'none' } ,
				 message: "<b>로딩중..</b>"
			});

			$.ajax({
	            url: '<%=webRoot%>/admin/teleport/activateCache.do',
	            type: 'POST',
	            dataType: 'json',
	            data: {
	            	callByScheduler : 'N'
	            },
	            error: function () {
	                alert('작업 중 오류가 발생하였습니다');
	                $.unblockUI();
	            },

	            success: function (data) {
				 	console.log(data);
				 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
				 	var flag = data.flag;
				 	var message = data.message;

				 	if(flag == "0000"){						// 정상적으로 처리된 경우
			 			alert("순간이동 데이터 API 정보가 업데이트되었습니다");
			 			location.reload();
				 	}else{
				 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
				 	}
			 	  $.unblockUI();
	            }
	        });
		}
	});
});


function teleportUpdate(code){
	var url = window.open("<%=webRoot%>/admin/teleport/update.do?anchor_id="+code, "regcodeitem","toolbar=yes,resizable=yes,width=520,height=380");
	url.moveTo(15,50);
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
                                    순간이동 관리
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
			                            <td>
			                                <!-- 검색 시작 -->

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
												<form name="form1" action="<%=webRoot%>/admin/teleport/list.do" method="POST">
						                        <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            <tbody>
						                            <tr>
						                                <td height="25">
						                                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
						                                        <tbody>
						                                        <tr>
						                                            <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
						                                            <td class="bold">순간이동 목록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
															<table border="0" cellpadding="0" cellspacing="0" style="width:100%;">
																<tbody>
																<tr>
																	<td width="4%">
																		<select id="searchType" name="searchType">
																			<option value="anchor_txt">노출명</option>
																			<option value="panel_id">패널ID</option>
																			<option value="paper_code">지면ID</option>
																		</select>
																	</td>
																	<td>
																		<input type="text" id="searchText" name="searchText" value="${searchText}" size="20" style="font-size: 12px;"/>
																	</td>
																	<td width="90%" align="left">
																		<input id="searchbtn" src="/smartux_adm/images/admin/search.gif" border="0" height="22" type="image" width="65">
																	</td>
																</tr>
																</tbody>
															</table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
												</form>

					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list">
					                                <tbody>
					                                <tr align="center">
					                                	<th scope="col" width="5%" style="padding-top:8px;"><input type="checkbox" id="allchk" name="allchk" value=""></th>
					                                    <th scope="col" width="5%">순번</th>
														<th scope="col" width="5%">타입</th>
														<th scope="col" width="5%">코드</th>
					                                    <th scope="col" width="20%">노출명</th>
														<th scope="col" width="20%">상위명</th>
														<th scope="col" width="15%">패널ID</th>
														<th scope="col" width="15%">지면ID</th>
					                                    <th scope="col" width="10%">등록일</th>
					                                </tr>
					                                <c:if test="${list == '[]' }">
					                                <tr align="center">
					                                    <td class="table_td_04" colspan="9">데이터가 존재 하지 않습니다.</td>
					                                </tr>
					                                </c:if>
					                            	<c:forEach items="${list}" var="rec">
														<tr align="center">
															<td class="table_td_04"><c:if test="${rec.anchor_id != '1' }"><input type="checkbox" name="delchk" value="${rec.anchor_id}"></c:if></td>
															<td class="table_td_04 upTel">${rec.order}</td>
															<td class="table_td_04 upTel">${rec.anchor_type_txt}</td>
															<td class="table_td_04 upTel code">${rec.anchor_id}</td>
															<td class="table_td_04 upTel">${rec.anchor_txt}</td>
															<td class="table_td_04 upTel">${rec.parent_txt}</td>
															<td class="table_td_04 upTel">${rec.panel_id}</td>
															<td class="table_td_04 upTel">${rec.paper_code}</td>
															<td class="table_td_04 upTel">${rec.insert_date}</td>
														</tr>
					                            	</c:forEach>
					                            	</tbody>
					                            </table>
					                             <table border="0" cellpadding="0" cellspacing="0" width="100%">
						                            <tbody>
						                            <tr>
						                                <td height="5"></td>
						                            </tr>
						                        	</tbody>
						                        </table>
					                           <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						                            <tbody>
						                            <tr>
						                                <td align="right">
						                                	<input type="button" id="regbtn" value="등록" class="button small blue"  />
															<input type="button" id="delbtn" value="삭제" class="button small blue"  />
															<input type="button" id="orderbybtn" value="순서바꾸기" class="button small blue"  />
														  	<input type="button" id="activatebtn" value="즉시적용" class="button small blue"/>
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
	        <%@include file="/WEB-INF/views/include/bottom.jsp" %>
	        <!-- 하단 로그인 사용자 정보 종료 -->
	    </td>
	 </tr>
</tbody>
</table>
</div>
<c:choose>
<c:when test="${vo.validate == 'NOT ID'}">
	<script type="text/javascript">
		alert('잘못된 접근입니다.');
	</script>
</c:when>
</c:choose>
</body>
</html>