<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<title>LG U+ IPTV SmartUX</title>

	<link href="${pageContext.request.contextPath}/css/basic_style.css" rel="stylesheet" type="text/css">
	<jsp:include page="/WEB-INF/views/include/js.jsp"/>

	<script type="text/javascript">
		$(document).ready(function() {
			
			$('#deleteBtn').click(function() {
				var numbers = '';
				var checkBox = $('.checkbox');

				if (!checkBox.is(':checked')) {
					alert('삭제 할 항목을 선택하세요.');
					return;
				} else {
					checkBox.each(function () {
						if ($(this).is(':checked')) {
							numbers = numbers + ',' + $(this).val();
						}
					});
				}

				if (confirm('새소식을 삭제 하시겠습니까?')) {
					$.blockUI({
						blockMsgClass: 'ajax-loading',
						showOverlay: true,
						overlayCSS: { backgroundColor: '#CECDAD' } ,
						css: { border: 'none' } ,
						message: '<b>처리 중..</b>'
					});

					$.ajax({
						url: './deleteNews',
						type:'POST',
						dataType: 'json',
						timeout : 30000,
						data: {'numbers': numbers},
						success:function(data){
							//noinspection JSUnresolvedVariable
							if(data.result.flag == '0000'){
								alert('정상적으로 처리 되었습니다.');
							}else{
								alert(data.result.message);
							}
						},
						error:function(){
							alert('정상적으로 처리되지 않았습니다.');
						},
						complete:function(){
							location.href = '<%=webRoot%>/admin/news/newsList.do';
						}
					});
				}
			});
		});

		function updateNews(regNumber) {
			location.href = '<%=webRoot%>/admin/news/insertNews.do?regNumber=' + regNumber;
		}

		function allCheck(object) {
			if (object.is(':checked')) {
				$('.checkbox').each(function (){
					this.checked = true;
				});
			} else {
				$('.checkbox').each(function (){
					this.checked = false;
				});
			}
		}
	</script>
</head>
<body>
<div id="divBody" style="height:100%">
	<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
		<tr>
			<td colspan="2" height="45" valign="bottom">
				<!-- top menu start -->
				<%@include file="/WEB-INF/views/include/top.jsp" %>
				<!-- top menu end -->
			</td>
		</tr>

		<tr>
			<td height="10"></td>
		</tr>

		<tr>
			<td colspan="2" valign="top">
				<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
					<tr>
						<td width="4"></td>
						<td valign="top" width="180">
							<!-- left menu start -->
							<%@include file="/WEB-INF/views/include/left.jsp" %>
							<!-- left menu end -->
						</td>
						<td background="${pageContext.request.contextPath}/images/admin/bg_02.gif" width="35">&nbsp;</td>
						<td valign="top">
							<table border="0" cellpadding="0" cellspacing="0" width="98%">
								<tr style="display:block">
									<td height="42" width="100%">
										<table border="0" cellpadding="0" cellspacing="0" width="100%">
											<tr>
												<td width="300" class="boldTitle">Push 관리</td>
											</tr>
										</table>
									</td>
								</tr>

								<tr>
									<td class="3_line" height="1"></td>
								</tr>

								<tr>
									<td>
										<!-- ######################## body start ######################### -->
										<table border="0" cellpadding="15" cellspacing="0" width="100%" align="center">
											<tr>
												<td>
													<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
														<tr>
															<td>
																<!-- 검색 시작 -->
																<table border="0" cellpadding="0" cellspacing="0" width="100%" class="search_table">
																	<tr>
																		<td width="15">&nbsp;</td>
																		<td width="80"><img src="${pageContext.request.contextPath}/images/admin/search_title4.gif" border="0" height="47" width="62"></td>
																		<td>
																			<form id="form2" method="get" action="newsList.do">
																				<table border="0" cellpadding="0" cellspacing="0" width="550">
																					<tr>
																						<td>&nbsp;</td>
																					</tr>
																					<tr>
																						<td width="10"><img src="${pageContext.request.contextPath}/images/admin/blt_05.gif" height="9" width="9" /></td>
																						<td>
																							<select class="select" id="findName" name="findName">
																								<option <c:if test="${'TITLE' eq vo.findName}">selected="selected"</c:if> value="TITLE">제목</option>
																								<option <c:if test="${'CONTENT' eq vo.findName}">selected="selected"</c:if> value="CONTENT">내용</option>
																							</select>
																						</td>
																						<td>
																							<input type="text" name="findValue" value="${vo.findValue }" size="20" style="font-size: 12px;"/>
																							&nbsp;&nbsp;&nbsp;
																							<select class="select" id="notiType" name="notiType">
																								<option <c:if test="${empty vo.notiType}">selected="selected"</c:if> value="">전체</option>
																								<option <c:if test="${'NON' eq vo.notiType}">selected="selected"</c:if> value="NON">경로 없음</option>
																								<option <c:if test="${'NOT' eq vo.notiType}">selected="selected"</c:if> value="NOT">공지/이벤트</option>
																								<option <c:if test="${'CON' eq vo.notiType}">selected="selected"</c:if> value="CON">컨텐츠</option>
																								<option <c:if test="${'CAT' eq vo.notiType}">selected="selected"</c:if> value="CAT">특정 카테고리</option>
																								<option <c:if test="${'SVO' eq vo.notiType}">selected="selected"</c:if> value="SVO">월정액 가입</option>
																								<option <c:if test="${'URL' eq vo.notiType}">selected="selected"</c:if> value="URL">외부 URL</option>
																								<option <c:if test="${'LIV' eq vo.notiType}">selected="selected"</c:if> value="LIV">실시간 채널</option>
																							</select>
																							&nbsp;&nbsp;&nbsp;
																							<select class="select" id="sendingStatus" name="sendingStatus">
																								<option <c:if test="${empty vo.sendingStatus}">selected="selected"</c:if> value="">전체</option>
																								<option <c:if test="${'Y' eq vo.sendingStatus}">selected="selected"</c:if> value="Y">전송</option>
																								<option <c:if test="${'N' eq vo.sendingStatus}">selected="selected"</c:if> value="N">미전송</option>
																							</select>
																						</td>
																						<td width="66" align="left"><input src="${pageContext.request.contextPath}/images/admin/search.gif" height="22" type="image" width="65"></td>
																					</tr>
																				</table>
																			</form>
																		</td>
																	</tr>
																</table>
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
																	<tr>
																		<td height="25">
																			<table border="0" cellpadding="0" cellspacing="0" width="100%">
																				<tr>
																					<td width="15"><img src="${pageContext.request.contextPath}/images/admin/blt_07.gif"></td>
																					<td class="bold">새소식 리스트</td>
																				</tr>
																			</table>
																		</td>
										                                <td align="right">
										                                	[Android] : SVC_ID:${PushInfo.GcmSvcId} APP_ID:${PushInfo.GcmAppId}
										                                </td>
																	</tr>
																</table>
																<table border="0" cellspacing="0" cellpadding="0" class="board_list" style="width:100%">
																	<tr align="center">
																		<th scope="col" width="60"><input type="checkbox" class="allCheck" onclick="allCheck($(this))"></th>
																		<th scope="col" width="60" style="font-size:12px;">번호</th>
																		<th scope="col" style="font-size:12px;">제목</th>
																		<th scope="col" width="7%" style="font-size:12px;">타입</th>
																		<th scope="col" width="18%" style="font-size:12px;">전송일시</th>
																		<th scope="col" width="10%" style="font-size:12px;">전송여부</th>
																		<th scope="col" width="5%" style="font-size:12px;">결과</th>
																	</tr>
																	<c:if test="${vo.list == '[]' }">
																		<tr align="center">
																			<td colspan="7">데이터가 존재 하지 않습니다.</td>
																		</tr>
																	</c:if>
																	<c:forEach items="${vo.list}" var="rec">
																		<c:set var="i" value="${i+1}" />
																		<tr align="center">
																			<td><input type="checkbox" class="checkbox" value="${rec.regNumber}" /></td>
																			<td>
																				<c:choose>
																					<c:when test="${empty param.pageNum or 1 ge param.pageNum}">
																						${i}
																					</c:when>
																					<c:otherwise>
																						${((param.pageNum - 1) * 10) + i}
																					</c:otherwise>
																				</c:choose>
																			</td>
																			<td><a href="javascript:updateNews('${rec.regNumber}')"><c:out value='${rec.title}'/></a></td>
																			<td>
																				<c:choose>
																					<c:when test="${'NOT' eq rec.notiType}">
																						공지
																					</c:when>
																					<c:when test="${'CON' eq rec.notiType}">
																						컨텐츠
																					</c:when>
																					<c:when test="${'CAT' eq rec.notiType}">
																						특정 카테고리
																					</c:when>
																					<c:when test="${'SVO' eq rec.notiType}">
																						월정액 가입
																					</c:when>
																					<c:when test="${'URL' eq rec.notiType}">
																						외부 URL
																					</c:when>
																					<c:when test="${'LIV' eq rec.notiType}">
																						실시간 채널
																					</c:when>
																					<c:when test="${'NON' eq rec.notiType}">
																						경로 없음
																					</c:when>
																				</c:choose>
																			</td>
																			<td><fmt:formatDate value="${rec.sendDate}" type="date" pattern="yyyy-MM-dd HH:mm"/></td>
																			<td>${rec.sendingStatus}</td>
																			<td>
																				<c:if test="${rec.sendingStatus ne 'N' && rec.pushType eq 'A'}">
																					${rec.resultCode}
																				</c:if>
																			</td>
																		</tr>
																	</c:forEach>
																</table>
																<table border="0" cellpadding="0" cellspacing="0" width="100%">
																	<tr>
																		<td height="5"></td>
																	</tr>
																	<tr>
																		<td align="center">
																			<jsp:include page="/WEB-INF/views/include/naviControll.jsp">
																				<jsp:param value="newsList.do" name="actionUrl"/>
																				<jsp:param value="?findName=${vo.findName}&findValue=${vo.findValue}&sendingStatus=${vo.sendingStatus}&notiType=${vo.notiType}" name="naviUrl" />
																				<jsp:param value="${vo.pageNumber }" name="pageNum"/>
																				<jsp:param value="${vo.pageSize }" name="pageSize"/>
																				<jsp:param value="${vo.blockSize }" name="blockSize"/>
																				<jsp:param value="${vo.pageCount }" name="pageCount"/>
																			</jsp:include>
																		</td>
																	</tr>
																</table>
																<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
																	<tr>
																		<td align="right">
																			<span class="button small blue" id="deleteBtn">삭제</span>
																			<a href="insertNews.do"><span class="button small blue">등록</span></a>
																		</td>
																	</tr>
																</table>
																<table border="0" cellpadding="0" cellspacing="2" align="right">
																	<tr>
																		<td>
										                                	<c:forEach items="${PushCodeList}" var="rec">
										                                		${rec}<br/>
										                                	</c:forEach>
																		</td>
																	</tr>
																</table>
																<table border="0" cellpadding="0" cellspacing="0" width="100%">
																	<tr>
																		<td height="1"></td>
																	</tr>
																	<tr>
																		<td class="3_line" height="3"></td>
																	</tr>
																	<tr>
																		<td>&nbsp;</td>
																	</tr>
																</table>
															</td>
														</tr>
														<!-- 리스트 종료 -->
													</table>
												</td>
											</tr>
										</table>
										<!-- ########################### body end ########################## -->
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td height="30">&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td colspan="2" background="${pageContext.request.contextPath}/images/admin/copy_bg.gif" height="60" align="left">
				<!-- 하단 로그인 사용자 정보 시작 -->
				<%@include file="/WEB-INF/views/include/bottom.jsp" %>
				<!-- 하단 로그인 사용자 정보 종료 -->
			</td>
		</tr>
	</table>
</div>
</body>
</html>