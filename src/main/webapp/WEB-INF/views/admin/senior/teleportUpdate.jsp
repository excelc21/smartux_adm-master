<%@page import="com.dmi.smartux.common.property.SmartUXProperties"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<%@ taglib prefix="fn"  	uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" 	uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>LG U+ IPTV SmartUX</title>
	<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
	<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
	<script type="text/javascript">
        $(document).ready(function(){

            displayParentCode();
            function displayParentCode() {
                const target = $('#parent_id');
                const nationType = "${nationType}";
                const anchorType = $('#anchor_type').val();
                if(anchorType === nationType) {
                    target.closest('tr').hide();
                } else {
                    target.closest('tr').show();
                }
            }
            $('#anchor_type').on('change', function() {
                displayParentCode();
            });

            $("#paperPopup").click(function(){
                getMenuTreeListPop();
            });
            $("#regbtn").click(function(){
                const order =  $("#order");
                const anchor_id =  $("#anchor_id");
                const anchor_type =  $("#anchor_type");
                const panel_id =  $("#panel_id");
                const paper_code =  $("#paper_code");
                const paper_name =  $("#paper_name");
                const anchor_txt =  $("#anchor_txt");
                const parent_id = $("#parent_id option:selected").val();
                const parent_txt = $("#parent_id option:selected").text();

                const write_id =  $("#write_id");

                if(anchor_type.val() == ""){
                    alert("????????? ????????? ?????????");
                    anchor_type.focus();
                } else if(paper_name.val() == ""){
                    alert("??????????????? ????????? ?????????");
                    paper_name.focus();
                } else if(anchor_txt.val() == ""){
                    alert("???????????? ????????? ?????????");
                    anchor_txt.focus();
                } else if('false' == checkByteReturn(anchor_txt, '40')) {
                    alert('???????????? 40Byte ????????? ???????????? ?????????.');
                    anchor_txt.focus();
                } else{
                    $.post("<%=webRoot%>/admin/teleport/update.do",
                        {order : order.val(),
                            anchor_id : anchor_id.val(),
                            anchor_type : anchor_type.val(),
                            panel_id : panel_id.val(),
                            paper_code : paper_code.val(),
                            paper_name : paper_name.val(),
                            anchor_txt : anchor_txt.val(),
                            parent_id : parent_id,
                            parent_txt : parent_txt,
                            write_id : write_id.val()},
                        function(data) {
                            // ?????? ????????? ?????? ?????? ????????? ?????? ???????????? ????????????
                            var flag = data.flag;
                            var message = data.message;

                            if(flag == "0000"){						// ??????????????? ????????? ??????
                                opener.location.reload();
                                alert("??????????????? ?????????????????????");
                                self.close();
                            }else if(flag == "8000"){
                                alert("?????? ??? ???????????? ???????????????");
                            }else{
                                alert("?????? ??? ????????? ?????????????????????\nflag : " + flag + "\nmessage : " + message);
                            }
                        },
                        "json"
                    );
                }
            });

            // $("#resetbtn").click(function(){
            //     $("#regfrm")[0].reset();
            // });

            $("#closebtn").click(function(){
                window.close();
            });


        });


        function getMenuTreeCallbak(data) {
            $("#panel_id").val(data.panel_id);
            $("#paper_code").val(data.paper_code);
            $("#paper_name").val(data.paper_name);
        }
        //?????????????????? ??????
        function getMenuTreeListPop(){
            const url = "<%=webRoot%>/admin/teleport/getMenuTreeList.do?callbak=getMenuTreeCallbak";
            category_window = window.open(url, 'getMenuTreeList', 'width=800,height=500,left=10,top=10,scrollbars=yes');
        }
	</script>
</head>

<body leftmargin="0" topmargin="0">
<div id="divBody" style="height:100%">

	<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%" >
		<tbody>
		<tr>
			<td colspan="2" valign="top">
				<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
					<tbody>
					<tr>
						<td valign="top">
							<table border="0" cellpadding="0" cellspacing="0" width="98%">
								<tbody>
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
														<!-- ????????? ?????? -->
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
																					<td class="bold">???????????? ??????</td>
																				</tr>
																				</tbody>
																			</table>
																		</td>
																	</tr>
																	</tbody>
																</table>
																<form id="regfrm" name="regfrm" method="post" action="">
																	<table border="0" cellpadding="0" cellspacing="0" width="450" class="board_data">
																		<tbody>
																		<tr align="center">
																			<th width="25%">??????</th>
																			<td width="1%"></td>
																			<td width="74%" align="left" >
																				<select id="anchor_type" name="anchor_type">
																					<c:forEach items="${typeList}" var="rec">
																						<option value="${rec.type}"
																								<c:if test="${rec.type == teleportInfo.anchor_type}">selected</c:if>
																						>${rec.name}</option>
																					</c:forEach>
																				</select>
																			</td>
																		</tr>
																		<tr align="center">
																			<th width="25%">?????? ??????</th>
																			<td width="1%"></td>
																			<td width="74%" align="left" >
																				<select id="parent_id" name="parent_id">
																					<c:forEach items="${list}" var="rec">
																						<option value="${rec.anchor_id}"
																								<c:if test="${rec.anchor_id == teleportInfo.parent_id}">selected</c:if>
																						>${rec.anchor_txt}</option>
																					</c:forEach>
																				</select>
																			</td>
																		</tr>
																		<tr align="center">
																			<th width="25%">????????????</th>
																			<td width="1%"></td>
																			<td width="74%" align="left" >
																				<input type="hidden" id="paper_code" name="paper_code" value="${teleportInfo.paper_code}"/>
																				<input type="hidden" id="panel_id" name="panel_id" value="${teleportInfo.panel_id}"/>
																				<input type="text" id="paper_name" name="paper_name" size="35" maxlength="100" style="font-size: 12px;" readonly value="${teleportInfo.paper_name}"/>
																				<input type="button" id="paperPopup"   value="??????"    class="button small blue" align="right"/>
																			</td>
																		</tr>
																		<tr align="center">
																			<th width="25%">?????????</th>
																			<td width="1%"></td>
																			<td width="74%" align="left" >
																				<input type="text" id="anchor_txt" name="anchor_txt" size="45" maxlength="100" style="font-size: 12px;" value="${teleportInfo.anchor_txt}"/>
																			</td>
																		</tr>
																		</tbody>
																	</table>

																	<br>
																	<br>
																	<table border="0" cellpadding="0" cellspacing="0" width="450" >
																		<tr  align="right">
																			<td >
																				<input type="button" id="regbtn"   value="??????"    class="button small blue" align="right"/>
																				<%--<input type="button" id="resetbtn" value="?????????"  class="button small blue"/>--%>
																				<input type="button" id="closebtn" value="??????"    class="button small blue"/>
																			</td>
																		</tr>
																	</table>
																	<input type="hidden" id="write_id" name="write_id" value="<%=id_decrypt %>" />
																	<input type="hidden" id="code" name="code" value="" />
																	<input type="hidden" id="order" name="order" value="${teleportInfo.order}" />
																	<input type="hidden" id="anchor_id" name="anchor_id" value="${teleportInfo.anchor_id}" />

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
														<!-- ????????? ?????? -->
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
		</tbody>
	</table>
</div>
</body>
</html>


