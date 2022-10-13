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
													Paynow 결제 리스트
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
																					<td class="bold">결제 정보 상세</td>
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
																		<th width="25%">가맹점 TID</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.tid}</td>
																	</tr>
																	<tr align="center">
																	<tr class="N1View" align="center">
																		<th width="25%">가입번호</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.sa_id}</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">맥 어드레스</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.mac}</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">구매자 CTN</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.ctn}</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">앨범 ID</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.album_id}</td>
																	</tr>
																	<tr align="center">
																	<tr class="N1View" align="center">
																		<th width="25%">앨범명</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.album_name}</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">카테고리 ID</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.cat_id}</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">구매 금액</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.amount}</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">구매 유형</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.buying_gb}</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">결제 상태</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.status}</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">쿠폰 발급 정보</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.coupon_msg}</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">오류 타입</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.err_typ}</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">오류 코드</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.err_cd}</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">오류 메시지</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.err_msg}</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">결제일시</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.reg_dt}</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">마지작 수정일시</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.mod_dt}</td>
																	</tr>
																	</tbody>
																</table>

																<table border="0" cellpadding="0" cellspacing="0" width="660" align="left">
																	<tbody>
																	<tr>
																		<td height="25" align="right">
																			<a href="./list.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&status=${vo.status}&startDt=${vo.startDt}&endDt=${vo.endDt}">
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