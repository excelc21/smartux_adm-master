<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>LG U+ IPTV SmartUX</title>
	<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
	<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
	<script type="text/javascript">

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
								<tr>
									<td height="42" width="100%">
										<table border="0" cellpadding="0" cellspacing="0" width="100%">
											<tbody>
											<tr>
												<td width="300" class="boldTitle">
													상품가입 내역 관리
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
																					<td class="bold">구매 내역 관리</td>
																				</tr>
																				</tbody>
																			</table>
																		</td>
																	</tr>
																	</tbody>
																</table>

																<table border="0" cellpadding="0" cellspacing="0" width="660" class="board_data">
																	<tbody>
																	<tr class="N1View" align="center">
																		<th width="25%">구매키</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.pa_key}</td>
																	</tr>
																	<tr align="center">
																	<tr class="N1View" align="center">
																		<th width="25%">상태</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">
																			<c:choose>
																				<c:when test="${view.pa_status eq 'S'}"><span style="color: blue">성공</span></c:when>
																				<c:when test="${view.pa_status eq 'B'}"><span>대기중</span></c:when>
																				<c:when test="${view.pa_status eq 'A'}"><span>진행중</span></c:when>
																				<c:when test="${view.pa_status eq 'C'}"><span>취소</span></c:when>
																				<c:when test="${view.pa_status eq 'E'}"><span style="color: red">실패</span></c:when>
																				<c:when test="${view.pa_status eq 'T'}"><span style="color: red">취소실패</span></c:when>
																				<c:otherwise>미정의 상태코드</c:otherwise>
																			</c:choose>
																		</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">응답코드</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">
																			<c:choose>
																				<c:when test="${empty view.pa_flag}">
																					응답 코드 없음
																				</c:when>
																				<c:otherwise>
																					<c:out value="${view.pa_flag}"/>(<c:out value="${view.pa_message}"/>)
																				</c:otherwise>
																			</c:choose>

																		</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">가입번호</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.sa_id}</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">MAC</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.stb_mac}</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">상품코드</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.product_code}</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">상품명</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.product_name}</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">결제일시</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.pa_reg_dt}</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">수정일시</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.pa_mod_dt}</td>
																	</tr>
																	</tbody>
																</table>

																<table border="0" cellpadding="0" cellspacing="0" width="660" align="left">
																	<tbody>
																	<tr>
																		<td height="25" align="right">
																			<!-- <a href="./svodlist.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&startDt=${vo.start_dt}&endDt=${vo.end_dt}">-->
																				<a href="javascript:history.back();">
																				<span class="button small blue">목록</span></a>
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
