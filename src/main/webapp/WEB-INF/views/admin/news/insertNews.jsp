<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<title>LG U+ IPTV SmartUX</title>

	<link href="${pageContext.request.contextPath}/css/basic_style.css" rel="stylesheet" type="text/css">
	<jsp:include page="/WEB-INF/views/include/js.jsp"/>

	<link href="${pageContext.request.contextPath}/css/anytime_style.css" rel="stylesheet" type="text/css">
	<script src="${pageContext.request.contextPath}/js/anytime.js"></script>
	<script src="${pageContext.request.contextPath}/js/papaparse.js"></script>

	<script type="text/javascript">
		$(document).ready(function() {
			AnyTime.picker('sendDate', { format: '%z-%m-%d %H:%i',  labelTitle: '날짜',   labelHour: '시간' , time:''});

			$('#listBtn').click(function () {
				location.href = '<%=webRoot%>/admin/news/newsList.do';
			});

			$('#notiType').change(function(){
				clearData();
				changeType($(this).val());
			});

			$('#flatRate, #liveCH').change(function(){
				clearData();
			});

            $('.TargetView').change(function () {
                $('#targetCount').text('');
            });

            $('#addLocationBtn').click(function() {
                $('#locationSet').find('option:selected').each(function() {
                    $('#location').append(new Option($(this).text(), $(this).val(), true, false));
                    $(this).remove();
                    $('#targetCount').text('');
                });
            });

            $('#delLocationBtn').click(function() {
                $('#location').find('option:selected').each(function() {
                    $('#locationSet').append(new Option($(this).text(), $(this).val(), true, false));
                    $(this).remove();
                    $('#targetCount').text('');
                });
            });

            $('#addOptionalServiceBtn').click(function() {
                $('#optionalServiceSet').find('option:selected').each(function() {
                    $('#optionalService').append(new Option($(this).text(), $(this).val(), true, false));
                    $(this).remove();
                    $('#targetCount').text('');
                });
            });

            $('#delOptionalServiceBtn').click(function() {
                $('#optionalService').find('option:selected').each(function() {
                    $('#optionalServiceSet').append(new Option($(this).text(), $(this).val(), true, false));
                    $(this).remove();
                    $('#targetCount').text('');
                });
            });

			$('#submitBtn').click(function() {
				insertNews();
			});

			$('input:radio[name="pushType"]').click(function() {
				changeTargetView($(this).val());
				initNotiType();
			});

			$('#file').change(function () {
                $('#targetCount').text('');

				if (jQuery.isEmptyObject($(this).val())) {
					return;
				}

				var ext = $(this).val().toLowerCase().match(/\.([^\.]+)$/)[1];
				switch (ext) {
					case 'csv':
						break;
					default:
						alert('csv(.csv) 파일을 등록해 주세요.');
						$(this).val('');
						return;
				}
			});

			$('#targetAddBtn').click(function () {
				var selInput = $('#targetInput');
				var value = selInput.val();
                var selSaIDList = $('#saIDList');

				var isDuplicate = false;

                selSaIDList.find('option').each(function() {
					if (value == $(this).val()) {
						isDuplicate = true;
						return false;
					}
				});

				if (!jQuery.isEmptyObject(value) && !isDuplicate) {
                    selSaIDList.append(new Option(value, value, true, false));
                    selInput.val('');
				}

                $('#targetCount').text(selSaIDList.find('option').length);
			});

			$('#targetRemoveBtn').click(function () {
                var selSaIDList = $('#saIDList');
                selSaIDList.find('option:selected').remove();
                $('#targetCount').text(selSaIDList.find('option').length);
			});

			$('#targetCountBtn').click(function () {
                checkTargetCount();
			});

			$('#mappingBtn').click(function(){
				var notiType = $('#notiType').find('option:selected').val();
                var url;
                var text;

				if ('SVO' == notiType) {
                    var selFlatRate = $('#flatRate');
					text = selFlatRate.find('option:selected').text();

					$('#notiDetail').val(selFlatRate.val());
					$('#notiDetailName').val(jQuery.trim(text));
					$('#choiceData').html(jQuery.trim(text));
				} else if ('LIV' == notiType) {
                    var selLiveCH = $('#liveCH');
					text = selLiveCH.find('option:selected').text();

					$('#notiDetail').val(selLiveCH.val());
					$('#notiDetailName').val(jQuery.trim(text));
					$('#choiceData').html(jQuery.trim(text));
				} else if ('NOT' == notiType) {
                    url = '<%=webRoot%>/admin/noti/getNotiPopList.do?scr_tp=T&hiddenName=notiDetail&textName=notiDetailName&delimiter=comma&textHtml=choiceData';
                    category_window = window.open(url, 'getNotiPopList', 'width=800,height=600,scrollbars=yes');
                } else if ('CON' == notiType) {
                    url = '<%=webRoot%>/admin/commonMng/getOnceCategoryAlbum.do?categoryId=VC&hiddenName=notiDetail&textName=notiDetailName&textHtml=choiceData&series=Y&isTypeChange=Y&type=I20';
                    category_window = window.open(url, 'getOnceCategoryAlbum', 'width=600,height=330,scrollbars=yes');
                }
			});

			var notiType = '${vo.notiType}';
			var pushType = '${vo.pushType}';

			changeType(notiType);
			changeTargetView(pushType);
			initNotiType();
		});

		function clearData() {
			$('#notiDetail').val('');
			$('#uriData').val('');
			$('#notiDetailName').val('');
			$('#choiceData').html('');
		}

		function changeTargetView(target) {
			$('#file').val('');
			$('#saIDList').find('option').remove();
            $('#locationSet').find('option').remove();
            $('#location').find('option').remove();
			$('#targetCount').text('');

			if (jQuery.isEmptyObject(target) || 'A' == target) {
				$('.TargetView').hide();
				$('.TargetExcelView').hide();
				$('.TargetInputView').hide();
				$('.TargetCountView').hide();
				$('input:radio[name="pushType"]:radio[value="A"]').attr('checked',true);
			} else if ('T' == target) {
				$('.TargetView').show();
				$('.TargetExcelView').hide();
				$('.TargetInputView').hide();
				$('.TargetCountView').show();
				$('input:radio[name="pushType"]:radio[value="T"]').attr('checked',true);

                initLocation();
                initOptionalService();
			} else if ('E' == target) {
				$('.TargetView').hide();
				$('.TargetExcelView').show();
				$('.TargetInputView').hide();
				$('.TargetCountView').show();
				$('input:radio[name="pushType"]:radio[value="E"]').attr('checked',true);
			} else if ('I' == target) {
				$('.TargetView').hide();
				$('.TargetExcelView').hide();
				$('.TargetInputView').show();
				$('.TargetCountView').show();
				$('input:radio[name="pushType"]:radio[value="I"]').attr('checked',true);

                initSaID();
			}
		}
		
		function initNotiType(){
			var pushType = $("input:radio[name=pushType]:checked").val();
			var selectType = '${vo.notiType}';
			
			$('#notiType').empty();
			
			if('A' == pushType){
				var option1 = $("<option value=\"NON\">경로 없음</option>");
				var option2 = $("<option value=\"NOT\">공지/이벤트</option>");
				var option3 = $("<option value=\"CAT\">특정 카테고리</option>");
				var option4 = $("<option value=\"SVO\">월정액 가입</option>");
				var option5 = $("<option value=\"URL\">외부 URL</option>");
				var option6 = $("<option value=\"LIV\">실시간 채널</option>");
				
				$('#notiType').append(option1).append(option2).append(option3).append(option4).append(option5).append(option6);
			}else{
				var option1 = $("<option value=\"NON\">경로 없음</option>");
				var option2 = $("<option value=\"NOT\">공지/이벤트</option>");
				var option3 = $("<option value=\"CON\">컨텐츠 정보</option>");
				var option4 = $("<option value=\"CAT\">특정 카테고리</option>");
				var option5 = $("<option value=\"SVO\">월정액 가입</option>");
				var option6 = $("<option value=\"URL\">외부 URL</option>");
				var option7 = $("<option value=\"LIV\">실시간 채널</option>");
				
				$('#notiType').append(option1).append(option2).append(option3).append(option4).append(option5).append(option6).append(option7);
			}
			
			$("#notiType").val(selectType).prop("selected", true);
		}

        function initSaID() {
            var selSaIDList = $('#saIDList');
            var saID = [];

            <c:forEach items="${saIDList}" var="rec">
                saID.push('${rec}');
            </c:forEach>

            for (var i in saID) {
                var value = saID[i];

                if (!jQuery.isEmptyObject(value)) {
                    selSaIDList.append(new Option(value, value, true, false));
                }
            }

            $('#targetCount').text(selSaIDList.find('option').length);
        }

        function initLocation() {
            var locationCode = '${targetVO.locationCode}';

            var codeAry = [];
            var nameAry = [];

            <c:forEach items="${location}" var="rec">
                codeAry.push('${rec.code}');
                nameAry.push('${rec.name}');
            </c:forEach>

            for (var i in codeAry) {
                var code = codeAry[i];
                var name = nameAry[i];

                if (!jQuery.isEmptyObject(code) && !jQuery.isEmptyObject(name)) {
                    if (-1 != locationCode.indexOf(code)) {
                        $('#location').append(new Option(name, code, true, false));
                    } else {
                        $('#locationSet').append(new Option(name, code, true, false));
                    }
                }
            }
        }

        function initOptionalService() {
            var optionalServiceCode = '${targetVO.optionalServiceCode}';

            var codeAry = [];
            var nameAry = [];

            <c:forEach items="${optionalServiceList}" var="rec">
                codeAry.push('${rec.code}');
                nameAry.push('${rec.name}');
            </c:forEach>

            for (var i in codeAry) {
                var code = codeAry[i];
                var name = nameAry[i];

                if (!jQuery.isEmptyObject(code) && !jQuery.isEmptyObject(name)) {
                    if (-1 != optionalServiceCode.indexOf(code)) {
                        $('#optionalService').append(new Option(name, code, true, false));
                    } else {
                        $('#optionalServiceSet').append(new Option(name, code, true, false));
                    }
                }
            }
        }

		function changeType(value) {
			if (jQuery.isEmptyObject(value) || 'NON' == value) {
				$('#mappingBtn').hide();
				$('#uriData').hide();
				$('#liveCH').hide();
				$('#flatRate').hide();
			} else if ('NOT' == value) {
				$('#mappingBtn').show();
				$('#uriData').hide();
				$('#liveCH').hide();
				$('#flatRate').hide();
			} else if ('CON' == value) {
				$('#mappingBtn').show();
				$('#uriData').hide();
				$('#liveCH').hide();
				$('#flatRate').hide();
			} else if ('SVO' == value) {
				$('#mappingBtn').show();
				$('#uriData').hide();
				$('#liveCH').hide();
				$('#flatRate').show();
			} else if ('URL' == value || 'CAT' == value) {
				$('#mappingBtn').hide();
				$('#uriData').show();
				$('#liveCH').hide();
				$('#flatRate').hide();
			} else if ('LIV' == value) {
				$('#mappingBtn').show();
				$('#uriData').hide();
				$('#liveCH').show();
				$('#flatRate').hide();
			}
		}

		function insertNews() {
			var regNumber = $('#regNumber').val();
			var selSendDate = $('#sendDate');
			var selTitle = $('#title');
			var selContent = $('#content');
			var selNotiDetail = $('#notiDetail');
			var notiType = $('#notiType').val();

			if ('CAT' == notiType || 'URL' == notiType) {
                selNotiDetail.val($('#uriData').val());
				$('#notiDetailName').val('-');
			} else if ('NON' == notiType) {
                selNotiDetail.val('-');
				$('#notiDetailName').val('-');
			}

			var pushType = $('input:radio[name="pushType"]:checked').val();

            if ('T' == pushType) {
                var minAge = $('#minAge').val();
                var maxAge = $('#maxAge').val();

                var i = 0;

                if (!jQuery.isEmptyObject(minAge)) {
                    i++;
                }

                if (!jQuery.isEmptyObject(maxAge)) {
                    i++;
                }

                switch (i) {
                    case 2:
                        if (isNaN(minAge) || isNaN(maxAge)) {
                            alert('나이는 숫자만 입력 가능합니다.');
                            return false;
                        }

                        if (minAge >= maxAge) {
                            alert('최소 나이는 최대 나이보다 크거나 같을 수 없습니다.');
                            return false;
                        }
                        break;
                    case 1:
                        alert('최소 나이 or 최대 나이가 비어있습니다.');
                        return false;
                        break;
                    default:
                        break;
                }

                if (checkOptionalService()) {
                    alert('부가 서비스를 입력해 주세요.');
                    return false;
                }

                $('#location').find('option').each(function() {
                    $(this).attr('selected', true);
                });

                $('#optionalService').find('option').each(function() {
                    $(this).attr('selected', true);
                });

            } else if ('E' == pushType && 0 == regNumber) {
				if (jQuery.isEmptyObject($('#file').val())) {
					alert('파일을 등록해주세요.');
					return false;
				}
			} else if ('I' == pushType) {
                var saIDListSel = $('#saIDList');

				if (0 >= saIDListSel.find('option').length) {
					alert('타겟팅 리스트를 입력해주세요.');
					return false;
				} else {
                    saIDListSel.find('option').each(function() {
						$(this).attr('selected', true);
					});
				}
			}

			if (jQuery.isEmptyObject(selSendDate.val())) {
				alert('전송일시를 입력해 주세요.');
                selSendDate.focus();
				return false;
			} else if (jQuery.isEmptyObject(selTitle.val())) {
				alert('제목을 입력해 주세요.');
                selTitle.focus();
				return false;
			} else if (jQuery.isEmptyObject(selContent.val())) {
				alert('내용을 입력해 주세요.');
                selContent.focus();
				return false;
			} else if (textareaCheck(selContent.val())) {
				var check = textareaCheck(selContent.val());
				alert(check + '는 입력할수 없습니다.');
                selContent.focus();
				return false;
			} else if (jQuery.isEmptyObject(selNotiDetail.val())) {
				alert('타입의 데이터를 선택해 주세요.(선택버튼 클릭)');
				return false;
			}

            // 타겟팅 수 체크
            var count = $('#targetCount').text();
            var checkFile = false;

            if ('E' == pushType) {
                // 파일이 존재하면 무조건 개수 체크
                if (!jQuery.isEmptyObject($('#file').val())) {
                    checkFile = true;
                } else {
                    // 파일이 존재하지 않고 파일경로가 존재하면 수정화면이므로 타겟수 체크하지 않고
                    // 파일경로가 존재하지 않으면 비정상 경로이므로 타겟수 체크
                    var filePath = '${filePath}';

                    if (jQuery.isEmptyObject(filePath)) {
                        checkFile = true;
                    }
                }
            } else if ('T' == pushType || 'I' == pushType) {
                checkFile = true;
            }

            if (checkFile && (jQuery.isEmptyObject(count) || '0' == count)) {
                alert('타겟팅할 대상이 없습니다.\n타겟수 확인을 클릭해주세요.');
                return false;
            }

            // 푸시메시지 길이체크
            if (!validPushMessage()) {
                return false;
            }

            $.blockUI({
                blockMsgClass: 'ajax-loading',
                showOverlay: true,
                overlayCSS: { backgroundColor: '#CECDAD' } ,
                css: { border: 'none' } ,
                message: '<b>처리 중..</b>'
            });

			$('#form1').submit();
		}

		function downloadFile(filePath) {
			location.href = filePath;
		}

		function winOpen(imagePath, width, height) {
			window.open(imagePath, 'windowName', 'toolbar=no,scrollbars=yes,resizable=no, top=20,left=200,width=' + width + ',height=' + height);
		}

		function deleteImage() {
			if (confirm('이미지를 삭제하시겠습니까?')) {
				$('#image').remove();
				$('#imageName').val('');
			}
		}

        function checkOptionalService() {
            var sendType = $('input:radio[name="sendType"]:checked').val();
            var selOptionalService = $('#optionalService');

            return 'X' == sendType && 0 >= selOptionalService.find('option').length;
        }

        // 푸시메시지 길이체크
        function validPushMessage() {
            var result = true;
            var p1 = $('input:radio[name=showType]:checked').val();
            var p2 = $('input:radio[name=netType]:checked').val();
            var p3 = $('input:radio[name=pushType]:checked').val();
            var p4 = $('#title').val();
            var p5 = $('#content').val();
            var p6 = $('#notiType').find('option:selected').val();
            var p7 = $('#notiDetail').val();
            var p8 = $('#imageFile').val();

            if (jQuery.isEmptyObject(p8)) {
                p8 = $('#imageName').val();
            }

            $.ajax({
                url: '/smartux_adm/admin/news/validPushMessage',
                async: false,
                type: 'POST',
                dataType: 'json',
                data: {'showType':p1, 'netType':p2, 'pushType':p3,'title':p4,'content':p5,'notiType':p6,'notiDetail':p7,'imageName':p8},
                success: function (rtn) {
                    //noinspection JSUnresolvedVariable
                    if (rtn.result.flag != '0000') {
                        alert('메시지 허용량이 초과되었습니다. 다시 작성해주세요.');
                        result = false;
                    }
                },
                error: function () {
                    alert('일시적인 장애가 발생하였습니다. 다시 시도해 주세요.');
                    result = false;
                }
            });

            return result;
        }

        function checkTargetCount() {
            var pushType = $('input:radio[name="pushType"]:checked').val();

            if ('T' == pushType) {
                var p1 = $('input:radio[name=targetNetType]:checked').val();
                var p2 = $('input:radio[name=modelType]:checked').val();
                var p3 = $('input:radio[name=gender]:checked').val();
                var p4 = $('#productionCode').find('option:selected').val();
                var p5 = [];
                var p6 = [];
                var p7 = $('#minAge').val();
                var p8 = $('#maxAge').val();
                var p9 = $('input:radio[name=sendType]:checked').val();

                if (checkOptionalService()) {
                    alert('부가 서비스를 입력해 주세요.');
                    return false;
                }

                $('#location').find('option').each(function() {
                    $(this).attr('selected', true);
                    p5.push($(this).val());
                });

                $('#optionalService').find('option').each(function() {
                    $(this).attr('selected', true);
					p6.push($(this).val());
                });

                $.blockUI({
                    blockMsgClass: 'ajax-loading',
                    showOverlay: true,
                    overlayCSS: { backgroundColor: '#CECDAD' } ,
                    css: { border: 'none' } ,
                    message: '<b>처리 중..</b>'
                });

				jQuery.ajaxSettings.traditional = true;

                $.ajax({
                    url: '/smartux_adm/admin/pvs/checkCount',
                    type: 'POST',
                    dataType: 'json',
                    data: {'targetNetType':p1, 'modelType':p2, 'gender':p3, 'productionCode':p4, 'locationCode':p5, 'optionalServiceCode':p6, 'minAge':p7, 'maxAge':p8, 'sendType':p9},
                    success: function (rtn) {
                        $.unblockUI();

                        //noinspection JSUnresolvedVariable
                        var flag = rtn.result.flag;


                        if ('0000' == flag) {
                            $('#targetCount').text(rtn.result.message);
                        } else if ('0001' == flag) {
                            alert('타겟팅 조건을 선택해 주세요.\n조건이 없을 경우 Announcement를 사용해주세요.');
                        }
                    },
                    error: function () {
                        $.unblockUI();
                        alert('일시적인 장애가 발생하였습니다. 다시 시도해 주세요.');
                    }
                });
            } else if ('E' == pushType) {
                $('#file').parse({
                    before: function(file, inputElem) {
                        $.blockUI({
                            blockMsgClass: 'ajax-loading',
                            showOverlay: true,
                            overlayCSS: { backgroundColor: '#CECDAD' } ,
                            css: { border: 'none' } ,
                            message: '<b>처리 중..</b>'
                        });
                    },
                    config: {
                        complete: function(results, file) {
                            $.unblockUI();
                            $('#targetCount').text(results.data.length);
                        }
                    },
                    error: function(err, file, inputElem, reason) {
                        $.unblockUI();
                        alert('csv 파일 분석에 실패했습니다.');
                    }
                });
            } else if ('I' == pushType) {
                $('#targetCount').text($('#saIDList').find('option').length);
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
								<tr>
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
									<td class="td_bg04" height="2"></td>
								</tr>
								<tr>
									<td>
										<!-- ######################## body start ######################### -->
										<table border="0" cellpadding="15" cellspacing="0" width="100%" align="center">
											<tr>
												<td>
													<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
														<tr>
															<td class="3_line" height="1"></td>
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
																					<td class="bold">
																						<c:choose>
																							<c:when test="${empty vo.regNumber or 0 eq vo.regNumber}">
																								새소식 등록
																							</c:when>
																							<c:otherwise>
																								새소식 수정
																							</c:otherwise>
																						</c:choose>
																					</td>
																				</tr>
																			</table>
																		</td>
																	</tr>
																</table>
																<form id="form1" action="./insertNews.do" method="post" enctype="multipart/form-data">
																	<input type="hidden" id="regNumber" name="regNumber" value="${vo.regNumber}">

																	<table border="0" cellpadding="0" cellspacing="0" width="660" class="board_data">
																		<tr align="center">
																			<th width="25%" style="font-size:12px;">타겟팅</th>
																			<td width="5%"></td>
																			<td width="70%" align="left" >
																				<input <c:if test="${empty vo.pushType or 'A' eq vo.pushType}">checked="checked"</c:if> type="radio" id="pushTypeAnnounce" name="pushType" value="A"><label for="pushTypeAnnounce">Announcement</label>
																				<input <c:if test="${'T' eq vo.pushType}">checked="checked"</c:if> type="radio" id="pushTypeTarget" name="pushType" value="T"><label for="pushTypeTarget">타겟팅</label>
																				<input <c:if test="${'E' eq vo.pushType}">checked="checked"</c:if> type="radio" id="pushTypeExcel" name="pushType" value="E"><label for="pushTypeExcel">CSV 파일</label>
																				<input <c:if test="${'I' eq vo.pushType}">checked="checked"</c:if> type="radio" id="pushTypeInput" name="pushType" value="I"><label for="pushTypeInput">직접입력</label>
																			</td>
																		</tr>
																		<tr class="TargetView" align="center" style="display:none">
																			<th width="25%" style="font-size:12px;background:#a9c08c">셋탑망 구분</th>
																			<td width="5%"></td>
																			<td width="70%" align="left" >
																				<input <c:if test="${empty targetVO.targetNetType or '00' eq targetVO.targetNetType}">checked="checked"</c:if> type="radio" id="targetNetTypeAll" name="targetNetType" value="00"><label for="targetNetTypeAll">ALL</label>
																				<input <c:if test="${'01' eq targetVO.targetNetType}">checked="checked"</c:if> type="radio" id="targetNetTypeHfc" name="targetNetType" value="01"><label for="targetNetTypeHfc">HFC</label>
																				<input <c:if test="${'02' eq targetVO.targetNetType}">checked="checked"</c:if> type="radio" id="targetNetTypeOptic" name="targetNetType" value="02"><label for="targetNetTypeOptic">광랜</label>
																			</td>
																		</tr>
																		<tr class="TargetView" align="center" style="display:none">
																			<th width="25%" style="font-size:12px;background:#a9c08c">셋탑구분</th>
																			<td width="5%"></td>
																			<td width="70%" align="left" >
																				<input <c:if test="${empty targetVO.modelType or 'GA' eq targetVO.modelType}">checked="checked"</c:if> type="radio" id="modelTypeAll" name="modelType" value="GA"><label for="modelTypeAll">ALL</label>
																				<input <c:if test="${'G1' eq targetVO.modelType}">checked="checked"</c:if> type="radio" id="modelTypeG1" name="modelType" value="G1"><label for="modelTypeG1">G1</label>
																				<input <c:if test="${'G2' eq targetVO.modelType}">checked="checked"</c:if> type="radio" id="modelTypeG2" name="modelType" value="G2"><label for="modelTypeG2">G2</label>
																			</td>
																		</tr>
																		<tr class="TargetView" align="center" style="display:none">
																			<th width="25%" style="font-size:12px;background:#a9c08c">요금제 선택</th>
																			<td width="5%"></td>
																			<td width="70%" align="left" >
																				<select class="select" id="productionCode" name="productionCode">
                                                                                    <option value="">ALL</option>
                                                                                    <c:forEach var="item" items="${pvsProductList}" varStatus="status">
                                                                                        <option <c:if test="${item.code eq targetVO.productionCode}">selected="selected"</c:if> value="${item.code}">${item.name}</option>
                                                                                    </c:forEach>
																				</select>
																			</td>
																		</tr>
																		<tr class="TargetView" align="center" style="display:none">
																			<th width="25%" style="font-size:12px;background:#a9c08c">지역구분</th>
																			<td width="5%"></td>
																			<td width="70%" align="left">
																				<table border="0" cellspacing="0" cellpadding="0" width="100%">
																					<tr align="center">
                                                                                        <td width="40%">
                                                                                            <select id="locationSet" size="20" multiple="multiple" style="width:200px;">
                                                                                            </select>
                                                                                        </td>
                                                                                        <td width="20%">
                                                                                            <span class="button small blue" id="addLocationBtn">추가</span><br/><br/>
                                                                                            <span class="button small blue" id="delLocationBtn">삭제</span>
                                                                                        </td>
                                                                                        <td width="40%">
                                                                                            <select id="location" name="locationCode" size="20" multiple="multiple" style="width:200px;">

                                                                                            </select>
                                                                                        </td>
																					</tr>
																				</table>
																			</td>
																		</tr>
																		<tr class="TargetView" align="center" style="display:none">
																			<th width="25%" style="font-size:12px;background:#a9c08c">부가 서비스</th>
																			<td width="5%"></td>
																			<td width="70%" align="left">
																				<table border="0" cellspacing="0" cellpadding="0" width="100%">
																					<tr align="center">
																						<td colspan="3">
																							<input <c:if test="${empty targetVO.sendType or 'O' eq targetVO.sendType}">checked="checked"</c:if> type="radio" id="sendTypeInclude" name="sendType" value="O"><label for="sendTypeInclude">특정 대상자만 Push 발송</label>
																							<input <c:if test="${'X' eq targetVO.sendType}">checked="checked"</c:if> type="radio" id="sendTypeExclude" name="sendType" value="X"><label for="sendTypeExclude">특정 대상자만 Push 미발송</label>
																						</td>
																					</tr>
																					<tr align="center">
                                                                                        <td width="40%">
                                                                                            <select id="optionalServiceSet" size="20" multiple="multiple" style="width:200px;">

                                                                                            </select>
                                                                                        </td>
                                                                                        <td width="20%">
                                                                                            <span class="button small blue" id="addOptionalServiceBtn">추가</span><br/><br/>
                                                                                            <span class="button small blue" id="delOptionalServiceBtn">삭제</span>
                                                                                        </td>
                                                                                        <td width="40%">
                                                                                            <select id="optionalService" name="optionalServiceCode" size="20" multiple="multiple" style="width:200px;">

                                                                                            </select>
                                                                                        </td>
																					</tr>
																				</table>
																			</td>
																		</tr>
																		<tr class="TargetView" align="center" style="display:none">
																			<th width="25%" style="font-size:12px;background:#a9c08c">프로파일 지정</th>
																			<td width="5%"></td>
																			<td width="70%" align="left" >
																				성별
																				<input <c:if test="${empty targetVO.gender or 'A' eq targetVO.gender}">checked="checked"</c:if> type="radio" id="genderAll" name="gender" value="A"><label for="genderAll">ALL</label>
																				<input <c:if test="${'M' eq targetVO.gender}">checked="checked"</c:if> type="radio" id="genderMale" name="gender" value="M"><label for="genderMale">남성</label>
																				<input <c:if test="${'F' eq targetVO.gender}">checked="checked"</c:if> type="radio" id="genderFemale" name="gender" value="F"><label for="genderFemale">여성</label>
																				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;연령
																				<input type="text" id="minAge" name="minAge" onKeyUp="checkByte($(this),'3')" size="35" value="${targetVO.minAge}" style="width:30px"/> ~
																				<input type="text" id="maxAge" name="maxAge" onKeyUp="checkByte($(this),'3')" size="35" value="${targetVO.maxAge}" style="width:30px"/>
																			</td>
																		</tr>
																		<tr class="TargetExcelView" align="center">
																			<th width="25%" style="font-size:12px;">가입번호 파일입력</th>
																			<td width="5%"></td>
																			<td width="70%" align="left" >
																				<input type="file" id="file" name="file" accept=".csv, application/vnd.ms-excel" value="파일선택"/>
																				<c:if test="${'E' eq vo.pushType and not empty filePath}">
																					<a href="javascript:downloadFile('${filePath}')">
																						<span class="button small blue">${vo.regNumber}.txt</span>
																					</a>
																				</c:if>
																			</td>
																		</tr>
																		<tr class="TargetInputView" align="center">
																			<th width="25%" style="font-size:12px;">가입번호 직접입력</th>
																			<td width="5%"></td>
																			<td width="70%" align="left" >
																				<input type="text" id="targetInput">
																				<span class="button small blue" id="targetAddBtn">추가</span>
																			</td>
																		</tr>
																		<tr class="TargetInputView" align="center">
																			<th width="25%" style="font-size:12px;">가입번호 리스트</th>
																			<td width="5%"></td>
																			<td width="70%" align="left" >
																				<select id="saIDList" name="saIDList" size="20" multiple="multiple" style="width:200px;height:100px;"></select>
																				<span class="button small blue" id="targetRemoveBtn">해제</span>
																			</td>
																		</tr>
																		<tr class="TargetCountView" align="center">
																			<th width="25%" style="font-size:12px;">타겟수</th>
																			<td width="5%"></td>
																			<td width="70%" align="left" >
																				<span class="button small blue" id="targetCountBtn">타겟수 확인</span>
																				&nbsp;
																				<span id="targetCount"></span>
																			</td>
																		</tr>

																		<tr align="center">
																			<th width="25%" style="font-size:12px;">노출 망 선택</th>
																			<td width="5%"></td>
																			<td width="70%" align="left" >
																				<input <c:if test="${empty vo.netType or 'A' eq vo.netType}">checked="checked"</c:if> type="radio" id="netAll" name="netType" value="A"/><label for="netAll">ALL</label>
																				<input <c:if test="${'L' eq vo.netType}">checked="checked"</c:if> type="radio" id="netLTE" name="netType" value="L"/><label for="netLTE">LTE</label>
																				<input <c:if test="${'W' eq vo.netType}">checked="checked"</c:if> type="radio" id="netWifi" name="netType" value="W"/><label for="netWifi">WIFI</label>
																			</td>
																		</tr>
																		<tr align="center">
																			<th width="25%" style="font-size:12px;">표시 방법선택</th>
																			<td width="5%"></td>
																			<td width="70%" align="left" >
																				<input <c:if test="${empty vo.showType or 'A' eq vo.showType}">checked="checked"</c:if> type="radio" id="showAll" name="showType" value="A"/><label for="showAll">모두 표시</label>
																				<input <c:if test="${'I' eq vo.showType}">checked="checked"</c:if> type="radio" id="showIndicator" name="showType" value="I"/><label for="showIndicator">인디케이터</label>
																				<input <c:if test="${'P' eq vo.showType}">checked="checked"</c:if> type="radio" id="showPopup" name="showType" value="P"/><label for="showPopup">팝업</label>
																			</td>
																		</tr>
																		<tr align="center">
																			<th width="25%" style="font-size:12px;">전송일시</th>
																			<td width="5%"></td>
																			<td width="70%" align="left" >
																				<input type="text" id="sendDate" name="sendDate" size="20" value="<fmt:formatDate value="${vo.sendDate}" type="date" pattern="yyyy-MM-dd HH:mm"/>" title="전송일시" />
																			</td>
																		</tr>
																		<tr align="center">
																			<th width="25%" style="font-size:12px;">*제목</th>
																			<td width="5%"></td>
																			<td width="70%" align="left" >
																				<input type="text" id="title" name="title"value="<c:out value='${vo.title}'/>" size="35" style="font-size: 12px;" onKeyUp="checkByte($(this),'100')"/>
																			</td>
																		</tr>
																		<tr align="center">
																			<th width="25%" style="font-size:12px;">제목 이미지</th>
																			<td width="5%"></td>
																			<td width="70%" align="left" >
																				<input type="file" id="imageFile" name="imageFile" accept="image/*" value="파일선택"/>
																				<c:if test="${not empty vo.imageName}">
																					<input type="hidden" id="imageName" name="imageName" value="${vo.imageName}"/>
																					<div id="image">
																						<a href="javascript:winOpen('${vo.imageURL}','500','500')">${vo.imageName}</a>
																						<a href="javascript:deleteImage()">
																							<span class="button small blue">삭제</span>
																						</a>
																					</div>
																				</c:if>
																			</td>
																		</tr>
																		<tr align="center">
																			<th width="25%" style="font-size:12px;">*내용</th>
																			<td width="5%"></td>
																			<td width="70%" align="left" >
																				<textarea rows="7" cols="40" name="content" id="content" onKeyUp="checkByte($(this),'300')"><c:out value='${vo.content}'/></textarea>
																				<br>[\f, !^, \b, \f88, \f99]  사용제한
																			</td>
																		</tr>
																		<tr align="center">
																			<th width="25%" style="font-size:12px;">*타입</th>
																			<td width="5%"></td>
																			<td width="70%" align="left" >
																				<select class="select" id="notiType" name="notiType">
																					<option value="NON" <c:if test="${'NON' eq vo.notiType}">selected="selected"</c:if>>경로 없음</option>
																					<option value="NOT" <c:if test="${'NOT' eq vo.notiType}">selected="selected"</c:if>>공지/이벤트</option>
																					<option value="CON" <c:if test="${'CON' eq vo.notiType}">selected="selected"</c:if>>컨텐츠 정보</option>
																					<option value="CAT" <c:if test="${'CAT' eq vo.notiType}">selected="selected"</c:if>>특정 카테고리</option>
																					<option value="SVO" <c:if test="${'SVO' eq vo.notiType}">selected="selected"</c:if>>월정액 가입</option>
																					<option value="URL" <c:if test="${'URL' eq vo.notiType}">selected="selected"</c:if>>외부 URL</option>
																					<option value="LIV" <c:if test="${'LIV' eq vo.notiType}">selected="selected"</c:if>>실시간 채널</option>
																				</select>
																				<input type="hidden" name="sendingStatus" id="sendingStatus" value="N">
																			</td>
																		</tr>
																		<tr align="center">
																			<th width="25%" style="font-size:12px;">선택</th>
																			<td width="5%"></td>
																			<td width="70%" align="left" >
																				<input type="text" id="uriData" size="35" style="font-size: 12px;" value="${vo.notiDetail}" />

																				<c:if test="${not empty chInfoList}">
																					<select class="select N1View" id="liveCH">
																						<c:forEach items="${chInfoList}" var="data">
																							<option <c:if test="${vo.notiDetail eq data.serviceID}">selected="selected"</c:if> value="${data.serviceID}">${data.channelName}</option>
																						</c:forEach>
																					</select>
																				</c:if>

																				<c:if test="${not empty flatRateList}">
																					<select class="select N1View" id="flatRate">
																						<c:forEach items="${flatRateList}" var="data">
																							<option <c:if test="${vo.notiDetail eq data.productID}">selected="selected"</c:if> value="${data.productID}">${data.productName}</option>
																						</c:forEach>
																					</select>
																				</c:if>

																				<span class="button small blue" id="mappingBtn">선택</span>
																			</td>
																		</tr>
																		<tr align="center">
																			<th width="25%" style="font-size:12px;">선택 확인</th>
																			<td width="5%">
																				<input type="hidden" name="notiDetail" id="notiDetail" value="${vo.notiDetail}">
																				<input type="hidden" name="notiDetailName" id="notiDetailName" value="${vo.notiDetailName}">
																				<input type="hidden" name="resultCode" id="resultCode" value="${vo.resultCode}">
																			</td>
																			<td width="70%" align="left" id="choiceData">${vo.notiDetailName}</td>
																		</tr>
																	</table>

																	<table border="0" cellpadding="0" cellspacing="0" width="660" align="left">
																		<tr>
																			<td height="25" align="right">
																				<span class="button small blue" id="submitBtn">
																					<c:choose>
																						<c:when test="${empty vo.regNumber or 0 eq vo.regNumber}">
																							등록
																						</c:when>
																						<c:otherwise>
																							수정
																						</c:otherwise>
																					</c:choose>
																				</span>
																				<span class="button small blue" id="listBtn">목록</span>
																			</td>
																		</tr>
																	</table>
																</form>

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
