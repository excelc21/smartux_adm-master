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
													구매내역 상세
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
																		<th width="25%">구매키</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.pa_key}</td>
																	</tr>
																	<tr align="center">
																	<tr class="N1View" align="center">
																		<th width="25%">결제수단</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">
																			<c:choose>
																				<c:when test="${view.pa_type eq 'H'}">
																					<label>휴대폰</label>
																					<span>
																						<c:choose>
																							<c:when test="${view.pa_status != 'B'}">
																								<c:choose>
																									<c:when test="${view.pg_status eq 'A'}">(결제 요청중)</c:when>
																									<c:when test="${view.pg_status eq '0'}">(결제 성공)</c:when>
																									<c:when test="${view.pg_status eq '1'}">(취소 성공)</c:when>
																									<c:when test="${view.pg_status eq '2'}">(결제 실패)</c:when>
																									<c:when test="${view.pg_status eq '3'}">(취소 실패)</c:when>
																									<c:when test="${view.pg_status eq '4'}">(망취소 실패)</c:when>
																									<c:when test="${empty view.pg_status}">(미요청)</c:when>
																									<c:otherwise>(정의되지 않음)</c:otherwise>
																								</c:choose>
																							</c:when>
																							<c:otherwise>대기중</c:otherwise>
																						</c:choose>
									                                    			</span>
																				</c:when>
																				<c:when test="${view.pa_type eq 'P'}">
																					<label>페이나우</label>
																					<span>
																						<c:choose>
																							<c:when test="${view.pa_status != 'B'}">
																								<c:choose>
																									<c:when test="${view.pa_status eq 'S'}">(성공)</c:when>
																									<c:when test="${view.pa_status eq 'A'}">(진행중)</c:when>
																									<c:when test="${view.pa_status eq 'B'}">(대기중)</c:when>
																									<c:when test="${view.pa_status eq 'C'}">(취소)</c:when>
																									<c:when test="${view.pa_status eq 'E'}">(실패)</c:when>
																									<c:when test="${view.pa_status eq 'T'}">(취소실패)</c:when>
																									<c:otherwise>(정의되지 않음)</c:otherwise>
																								</c:choose>
																							</c:when>
																							<c:otherwise>대기중</c:otherwise>
																						</c:choose>
									                                    			</span>
																				</c:when>
																				<c:when test="${view.pa_type eq 'S'}">
																					<label>기본</label>
																					<span>
																						<c:choose>
																							<c:when test="${view.pa_status != 'B'}">
																								<c:choose>
																									<c:when test="${view.pa_status eq 'S'}">(성공)</c:when>
																									<c:when test="${view.pa_status eq 'A'}">(진행중)</c:when>
																									<c:when test="${view.pa_status eq 'B'}">(대기중)</c:when>
																									<c:when test="${view.pa_status eq 'C'}">(취소)</c:when>
																									<c:when test="${view.pa_status eq 'E'}">(실패)</c:when>
																									<c:when test="${view.pa_status eq 'T'}">(취소실패)</c:when>
																									<c:when test="${view.pa_status eq 'U'}">(취소성공(사용자))</c:when>
																									<c:when test="${view.pa_status eq 'I'}">(취소실패(사용자))</c:when>
																									<c:when test="${view.pa_status eq 'D'}">(취소성공(관리자))</c:when>
																									<c:when test="${view.pa_status eq 'F'}">(취소실패(관리자))</c:when>
																									<c:otherwise>(정의되지 않음)</c:otherwise>
																								</c:choose>
																							</c:when>
																							<c:otherwise>대기중</c:otherwise>
																						</c:choose>
									                                    			</span>
																				</c:when>
																				<c:when test="${view.pa_type eq 'T'}">
																					<label>TV페이</label>
																					<span>
																						<c:choose>
																							<c:when test="${view.pa_status != 'B'}">
																								<c:choose>
																									<c:when test="${view.pa_status eq 'S'}">(성공)</c:when>
																									<c:when test="${view.pa_status eq 'A'}">(진행중)</c:when>
																									<c:when test="${view.pa_status eq 'B'}">(대기중)</c:when>
																									<c:when test="${view.pa_status eq 'C'}">(취소)</c:when>
																									<c:when test="${view.pa_status eq 'E'}">(실패)</c:when>
																									<c:when test="${view.pa_status eq 'T'}">(취소실패)</c:when>
																									<c:otherwise>(정의되지 않음)</c:otherwise>
																								</c:choose>
																							</c:when>
																							<c:otherwise>대기중</c:otherwise>
																						</c:choose>
									                                    			</span>
																				</c:when>
																				<c:otherwise>정의되지 않음</c:otherwise>
																			</c:choose>
																		</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">할인수단</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">
																			<table border="0" cellpadding="0" cellspacing="0" width="100%" >
																				<tr>
																					<th style="line-height: 20px">대상</th>
																					<th style="line-height: 20px">적용</th>
																					<th style="line-height: 20px">결과</th>
																					<th style="line-height: 20px">금액</th>
																				</tr>
																				<c:set var="discount_price" value="${fn:split(view.discount_price, ',')}" />
																				<c:set var="discount_div" value="${fn:replace(view.discount_div, ',', '')}" />
																				<tr>
																					<td width="10%">쿠폰</td>
																					<td width="10%">
																						<c:choose>
																							<c:when test = "${fn:substring(discount_div, 0, 1) eq '1'}">Y</c:when>
																							<c:otherwise>N</c:otherwise>
																						</c:choose>
																					</td>
																					<td width="10%">
																						<c:choose>
																							<c:when test = "${fn:substring(discount_div, 0, 1) eq '1'}">
																								<c:choose>
																									<c:when test="${view.pa_status eq 'B' || view.pa_status eq 'A' }">대기중</c:when>
																									<c:when test="${view.pa_status eq 'C' }">-</c:when>
																									<c:when test="${view.pa_status eq 'E' || view.pa_status eq 'S' || view.pa_status eq 'T' }">
																										<c:choose>
																											<c:when test="${view.coupon_status == 'Y'}">사용된 쿠폰</c:when>
																											<c:when test="${view.coupon_status == 'P'}">사용가능 쿠폰</c:when>
																											<c:when test="${view.coupon_status == 'N'}">미사용 쿠폰</c:when>
																											<c:when test="${view.coupon_status == 'X'}">사용 불가</c:when>
																											<c:when test="${empty view.coupon_status}">쿠폰 없음</c:when>
																											<c:otherwise>정의되지 않은 코드</c:otherwise>
																										</c:choose>
																									</c:when>
																									<c:otherwise>미정의 상태코드</c:otherwise>
																								</c:choose>
																							</c:when>
																							<c:otherwise>-</c:otherwise>
																						</c:choose>
																					</td>
																					<td width="10%">
																						<c:out value="${discount_price[0]}"/>
																						<c:if test="${fn:substring(discount_div, 0, 1) eq '1'}">
																							<br>(
																							<c:choose>
																								<c:when test="${view.cpn_type eq '1'}">정액형</c:when>
																								<c:when test="${view.cpn_type eq '2'}">정률형</c:when>
																								<c:when test="${view.cpn_type eq '3'}">상품권</c:when>
																								<c:otherwise>잘못된유형</c:otherwise>
																							</c:choose>
																							/
																							<c:choose>
																								<c:when test="${view.cpn_offer_type eq '1'}">컨텐츠</c:when>
																								<c:when test="${view.cpn_offer_type eq '2'}">DATAFREE</c:when>
																								<c:when test="${view.cpn_offer_type eq '3'}">컨텐츠+DATAFREE</c:when>
																								<c:otherwise>잘못된유형</c:otherwise>
																							</c:choose>
																							)
																						</c:if>

																					</td>
																				</tr>
																				<tr>
																					<td width="10%">멤버쉽</td>
																					<td width="10%">
																						<c:choose>
																							<c:when test = "${fn:substring(discount_div, 1, 2) eq '1'}">Y</c:when>
																							<c:otherwise>N</c:otherwise>
																						</c:choose>
																					</td>
																					<td width="10%">
																						<c:choose>
																							<c:when test = "${fn:substring(discount_div, 1, 2) eq '1'}">
																								<c:choose>
																									<c:when test="${view.pa_status eq 'B' || view.pa_status eq 'A' }">대기중</c:when>
																									<c:when test="${view.pa_status eq 'C' }">-</c:when>
																									<c:when test="${view.pa_status eq 'E' || view.pa_status eq 'S' || view.pa_status eq 'T' }">
																										<c:choose>
																											<c:when test="${view.membership_status == 'A'}">요청중</c:when>
																											<c:when test="${view.membership_status == '0'}">성공</c:when>
																											<c:when test="${view.membership_status == '1'}">취소 성공</c:when>
																											<c:when test="${view.membership_status == '2'}">사용 실패</c:when>
																											<c:when test="${view.membership_status == '3' || view.membership_status == '4'}"><font color ="red">취소 실패</font></c:when>
																											<c:when test="${empty view.membership_status}">내역 없음</c:when>
																											<c:otherwise>정의되지 않은 코드</c:otherwise>
																										</c:choose>
																									</c:when>
																									<c:otherwise>미정의 상태코드</c:otherwise>
																								</c:choose>
																							</c:when>
																							<c:otherwise>-</c:otherwise>
																						</c:choose>
																					</td>
																					<td width="10%"><c:out value="${discount_price[1]}"/></td>
																				</tr>
																				<tr>
																					<td width="10%">TV포인트</td>
																					<td width="10%">
																						<c:choose>
																							<c:when test = "${fn:substring(discount_div, 2, 3) eq '1'}">Y</c:when>
																							<c:otherwise>N</c:otherwise>
																						</c:choose>
																					</td>
																					<td width="10%">
																						<c:choose>
																							<c:when test = "${fn:substring(discount_div, 2, 3) eq '1'}">
																								<c:choose>
																									<c:when test="${view.pa_status eq 'B' || view.pa_status eq 'A' }">대기중</c:when>
																									<c:when test="${view.pa_status eq 'C' }">-</c:when>
																									<c:when test="${view.pa_status eq 'E' || view.pa_status eq 'S' || view.pa_status eq 'T' }">
																										<c:choose>
																											<c:when test="${view.tvpoint_status == 'A'}">요청중</c:when>
																											<c:when test="${view.tvpoint_status == '0'}">성공</c:when>
																											<c:when test="${view.tvpoint_status == '1'}">취소 성공</c:when>
																											<c:when test="${view.tvpoint_status == '2'}">사용 실패</c:when>
																											<c:when test="${view.tvpoint_status == '3' || view.tvpoint_status == '4'}"><font color ="red">취소 실패</font></c:when>
																											<c:when test="${empty view.tvpoint_status}">내역 없음</c:when>
																											<c:otherwise>정의되지 않은 코드</c:otherwise>
																										</c:choose>
																									</c:when>
																									<c:otherwise>미정의 상태코드</c:otherwise>
																								</c:choose>
																							</c:when>
																							<c:otherwise>-</c:otherwise>
																						</c:choose>
																					</td>
																					<td width="10%"><c:out value="${discount_price[2]}"/></td>
																				</tr>

																			</table>
																			<c:if test="${view.pa_status eq 'E' || view.pa_status eq 'T'}">
																				<p><small>* 쿠폰의 경우 해당 결제 요청시 사용 실패했으나, 동일 쿠폰으로 사용자가 결제를 재시도 하여 성공할 경우, 사용된 쿠폰으로 표시될 수 있습니다.</small></p>
																			</c:if>
																		</td>
																	</tr>
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
																				<c:when test="${view.pa_status eq 'U'}"><span style="color: red">취소성공(사용자)</span></c:when>
																				<c:when test="${view.pa_status eq 'I'}"><span style="color: red">취소실패(사용자)</span></c:when>
																				<c:when test="${view.pa_status eq 'D'}"><span style="color: red">취소성공(관리자)</span></c:when>
																				<c:when test="${view.pa_status eq 'F'}"><span style="color: red">취소실패(관리자)</span></c:when>
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
																	<c:if test="${!empty view.failover_div and ( view.pa_status eq 'E' or view.pa_status eq 'T' ) }">
																		<tr class="N1View" align="center">
																			<th width="25%">취소요청 결과</th>
																			<td width="5%"></td>
																			<td width="70%" align="left">
																				<c:set var="failover_div" value="${fn:split(view.failover_div, '|')}" />
																				<table border="0" cellpadding="0" cellspacing="0" width="100%" >
																					<tr>
																						<th style="line-height: 20px">대상</th>
																						<th style="line-height: 20px">적용</th>
																					</tr>
																					<tr>
																						<td width="20%">PG</td>
																						<td width="80%">
																							<c:choose>
																								<c:when test="${failover_div[0] == '2'}">실패</c:when>
																								<c:when test="${failover_div[0] == '1'}">성공</c:when>
																								<c:when test="${failover_div[0] == '0'}">미요청</c:when>
																								<c:otherwise>-</c:otherwise>
																							</c:choose>
																						</td>
																					</tr>
																					<tr>
																						<td width="20%">쿠폰</td>
																						<td width="80%">
																							<c:choose>
																								<c:when test="${fn:substring(discount_div, 0, 1) eq '1' and fn:substring(failover_div[1], 0, 1) eq '2'}">실패</c:when>
																								<c:when test="${fn:substring(discount_div, 0, 1) eq '1' and fn:substring(failover_div[1], 0, 1) eq '1'}">성공</c:when>
																								<c:when test="${fn:substring(discount_div, 0, 1) eq '1' and fn:substring(failover_div[1], 0, 1) eq '0'}">미요청</c:when>
																								<c:otherwise>-</c:otherwise>
																							</c:choose>
																						</td>
																					</tr>
																					<tr>
																						<td width="20%">멤버십</td>
																						<td width="80%">
																							<c:choose>
																								<c:when test="${fn:substring(discount_div, 1, 2) eq '1' and fn:substring(failover_div[1], 1, 2) eq '2'}">실패</c:when>
																								<c:when test="${fn:substring(discount_div, 1, 2) eq '1' and fn:substring(failover_div[1], 1, 2) eq '1'}">성공</c:when>
																								<c:when test="${fn:substring(discount_div, 1, 2) eq '1' and fn:substring(failover_div[1], 1, 2) eq '0'}">미요청</c:when>
																								<c:otherwise>-</c:otherwise>
																							</c:choose>
																						</td>
																					</tr>
																					<tr>
																						<td width="20%">TVPoint</td>
																						<td width="80%">
																							<c:choose>
																								<c:when test="${fn:substring(discount_div, 2, 3) eq '1' and fn:substring(failover_div[1], 2, 3) eq '2'}">실패</c:when>
																								<c:when test="${fn:substring(discount_div, 2, 3) eq '1' and fn:substring(failover_div[1], 2, 3) eq '1'}">성공</c:when>
																								<c:when test="${fn:substring(discount_div, 2, 3) eq '1' and fn:substring(failover_div[1], 2, 3) eq '0'}">미요청</c:when>
																								<c:otherwise>-</c:otherwise>
																							</c:choose>
																						</td>
																					</tr>
																				</table>
																			</td>
																		</tr>
																	</c:if>
																	<tr class="N1View" align="center">
																		<th width="25%">가입번호</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.sa_id}</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">가입MAC</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.stb_mac}</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">카테고리ID</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.prod_cate}</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">컨텐츠ID</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.prod_id}</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">컨텐츠명</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">${view.prod_name}</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">구매컨텐츠유형</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">
																			<c:choose>
																				<c:when test = "${view.buy_type eq '1'}">컨텐츠</c:when>
																				<c:when test = "${view.buy_type eq '2'}">데이터FREE</c:when>
																				<c:when test = "${view.buy_type eq '3'}">컨텐츠 +데이터FREE</c:when>
																			</c:choose>
																		</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">컨텐츠금액</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">
																			<c:choose>
																				<c:when test = "${view.buy_type eq '1' || view.buy_type eq '3'}">
																					<fmt:formatNumber value="${view.prod_price}" pattern="#,###" />&nbsp;&nbsp;<font color ="red">(-<fmt:formatNumber value="${view.prod_price_sale}" pattern="#,###" />)</font>
																				</c:when>
																				<c:otherwise>0</c:otherwise>
																			</c:choose>
																		</td>

																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">데이터FREE금액</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">
																			<c:choose>
																				<c:when test = "${view.buy_type eq '2' || view.buy_type eq '3'}">
																					<fmt:formatNumber value="${view.datafree_price}" pattern="#,###" />&nbsp;&nbsp;<font color ="red">(-<fmt:formatNumber value="${view.datafree_price_sale}" pattern="#,###" />)</font>
																				</c:when>
																				<c:otherwise>0</c:otherwise>
																			</c:choose>
																		</td>
																	</tr>
																	<tr class="N1View" align="center">
																		<th width="25%">결제금액(부가세포함)</th>
																		<td width="5%"></td>
																		<td width="70%" align="left">
																			<!-- 16-6차 에선 total_price 항목과 할인 수단이 없었으므로 total_price 가 null 이면 -1로 읽고, 상품 금액 * 1.1 해서 직접 계산한다. -->
																			<!-- 할인 수단이 적용 되었는데, total_price가  비어있을 경우 안맞을 수 있다.  -->
																			<c:choose>
																				<c:when test="${view.total_price == '-1'}">
																					<c:choose>
																						<c:when test = "${view.buy_type eq '1'}">
																							<fmt:formatNumber value="${(view.prod_price-view.prod_price_sale) * 1.1}" pattern="#,###" />
																						</c:when>
																						<c:when test = "${view.buy_type eq '2'}">
																							<fmt:formatNumber value="${(view.datafree_price-view.datafree_price_sale) * 1.1}" pattern="#,###" />
																						</c:when>
																						<c:when test = "${view.buy_type eq '3'}">
																							<fmt:formatNumber value="${((view.prod_price-view.prod_price_sale) + (view.datafree_price-view.datafree_price_sale))  * 1.1}" pattern="#,###" />
																						</c:when>
																					</c:choose>
																				</c:when>
																				<c:otherwise>
																					<fmt:formatNumber value="${view.total_price}" pattern="#,###" />
																				</c:otherwise>
																			</c:choose>

																		</td>
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
																			<a href="./livelist.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}&status=${vo.status}&paType=${vo.paType}&startDt=${vo.startDt}&endDt=${vo.endDt}&discount_div=${vo.discount_div}&mtype=${vo.mtype}&find_pa_key=${vo.find_pa_key}">
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
