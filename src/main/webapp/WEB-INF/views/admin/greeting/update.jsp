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
	<link href="/smartux_adm/css/anytime_style.css" rel="stylesheet" type="text/css">
	<style>
		.div-ImageView {
			position: absolute;
			width: 100px;
			height: 100px;
			text-align: center;
		}
	</style>
	<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
	<script src="/smartux_adm/js/anytime.js"></script>
	<script type="text/javascript">
        $(document).ready(function(){
            var today = new Date();
            AnyTime.picker('start_point', { format: '%H%i00', labelTitle: '날짜', labelHour: '시간', time: ''});
            AnyTime.picker('end_point', { format: '%H%i59', labelTitle: '날짜', labelHour: '시간', time: ''});
            AnyTime.picker('event_day', { format: '%y.%m.%d', labelTitle: '날짜', labelHour: '', time: '', defaultYear: today.getFullYear()});


            function validImage(target) {
                if("" === target.val()){
                    alert("업로드하고자 하는 이미지 파일을 설정해주세요");
                    target.focus();
                    return false;
                } else {
                    let imgval = target.val();
                    console.log(imgval);
                    const len = imgval.lastIndexOf(".");
                    console.log(len);
                    imgval = imgval.substring(len, imgval.length);
                    console.log(imgval);
                    if(!imgval.toLowerCase().match(/.(gif|jpg|png|jpeg|bmp|zip)$/i)){
                        alert("업로드하고자 하는 파일을 이미지 파일(gif,jpg,png,jpeg,bmp,zip)로 설정해주세요");
                        target.focus();
                        return false;
                    }
                }

                return true;
            }
            function validVoice(target) {
                if("" === target.val()){
                    alert("업로드하고자 하는 음성 파일을 설정해주세요");
                    target.focus();
                    return false;
                } else {
                    let voiceval = target.val();
                    const len = voiceval.lastIndexOf(".", voiceval.length-5);
                    voiceval = voiceval.substring(len, voiceval.length);
                    if(!voiceval.toLowerCase().match(/.(mp3|mp4|wav|zip)$/i)){
                        alert("업로드하고자 하는 음성 이미지 파일(mp3|mp4|wav|zip)로 설정해주세요");
                        target.focus();
                        return false;
                    }
                }

                return true;
            }

            $("#greeting_voice").change(function(){
                if(!validVoice($(this))) {
                    $(this).val('');
                }
            });
            $("#bg_image").change(function(){
                if(!validImage($(this))) {
                    $(this).val('');
                }
            });

            $('[name="date_type"]').change(function(){
                const dateType = $('[name="date_type"]:checked').val();
                if(dateType === '2') {
                    $('#start_point').val('000000');
                    $('#end_point').val('235959');
                    $('#event_day').show();
                } else {
                    $('#event_day').hide();
                }
            });

            /**/
            $("#regbtn").click(function(){

                $("#regfrm").submit();


            });
        	/**/

            //수정 클릭
			/**/
            $("#regfrm").ajaxForm({
                dataType: 'json',
                beforeSubmit: function() {
                    const greeting_txt  = $('#greeting_txt');
                    const date_type = $('[name="date_type"]:checked');
                    const event_day = $('#event_day');
                    const start_point = $('#start_point');
                    const end_point = $('#end_point');

                    if(greeting_txt.val().replace(/\s/gi, '') === ""){
                        alert("인사말을 입력해 주세요");
                        greeting_txt.focus();
                        return false;
                    } else if('false' == checkByteReturn(greeting_txt, '128')) {
                        alert('인사말은 128Byte 이내로 입력해야 합니다.');
                        greeting_txt.focus();
                        return false;
                    } else if(date_type.val() === "2" && event_day.val() === ""){
                        alert("특정일을 입력해 주세요");
                        event_day.focus();
                        return false;
                    } else if(start_point.val() === ""){
                        alert("시작시간을 선택해 주세요");
                        start_point.focus();
                        return false;
                    } else if(end_point.val() === ""){
                        alert("종료시간을 선택해 주세요");
                        end_point.focus();
                        return false;
                    } else if(Number(start_point.val()) > Number(end_point.val())){
                        alert("종료시간이 시작시간보다 큼니다.");
                        start_point.focus();
                        return false;
                    }
                },
                success: function(data, status){
                    if("success"===status) {
                        if("0000"===data.flag) {
							alert('정상 수정되었습니다.');
                            window.opener.location.reload();
							window.close();
                        }
                        else {
                            alert(data.message);
                        }
                    }else{
                        alert('등록에 문제가 생겼습니다.')
                    }
                },
                error: function(xhr, textStatus, errorThrown) {
                    alert('등록을 실패하였습니다.')
                }
            });
            /**/

            $("#closebtn").click(function(){
                window.close();
            });


        });





        //선택한 이미지를 보여준다.
        function fnImageView(){
            $("#div-ImageView").css({'top':$("#div-ImageArea").offset().top, 'left':$("#div-ImageArea").offset().left});
            $("#div-ImageView").show();
        }

        //선택한 이미지를 숨긴다.
        function fnImageViewClose(){
            $("#div-ImageView").hide();
        }

        //이미지삭제
        function fnDelImage(arg){
            $("#text_"+arg).remove();
            $("#btn_"+arg).remove();
        }
	</script>
</head>

<body leftmargin="0" topmargin="0">
<div id="divBody" style="height:100%;">

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
														<!-- 리스트 시작 -->
														<tr>
															<td>
                                                                <div id="div-ImageArea" sytel="border:0px;">
                                                                    <div id="div-ImageView" class="div-ImageView" style="display:none;">
                                                                        <table>
                                                                            <tr><td style="text-align:right"><img src="${pageContext.request.contextPath}/images/x.gif" style="cursor:pointer;" onClick="fnImageViewClose();"></td></tr>
                                                                            <tr>
                                                                                <td>
                                                                                    <img id="viewImg" src="${imgUrl}${greetingInfo.img_url}${greetingInfo.bg_image}">
                                                                                </td>
                                                                            </tr>
                                                                        </table>
                                                                    </div>
                                                                </div>
																<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
																	<tbody>
																	<tr>
																		<td height="25">
																			<table border="0" cellpadding="0" cellspacing="0" width="100%">
																				<tbody>
																				<tr>
																					<td width="15"><img src="<%=webRoot%>/images/admin/blt_07.gif" alt=""></td>
																					<td class="bold">인사말 수정</td>
																				</tr>
																				</tbody>
																			</table>
																		</td>
																	</tr>
																	</tbody>
																</table>
																<form name="regfrm" id="regfrm" method="POST" action='<%=webRoot%>/admin/greeting/update.do' encType="multipart/form-data">
																	<table border="0" cellpadding="0" cellspacing="0" width="450" class="board_data">
																		<tbody>
																		<tr align="center">
																			<th width="25%">인사말</th>
																			<td width="1%"></td>
																			<td width="74%" align="left" >
																				<input type="text" id="greeting_txt" name="greeting_txt" size="20" title="인사말" value="${greetingInfo.greeting_txt}"/>
																			</td>
																		</tr>
																		<tr align="center">
																			<th width="25%">인사 음성</th>
																			<td width="1%"></td>
																			<td width="74%" align="left" >
																				<input type="file" id="greeting_voice" name="greeting_voice" accept="audio/*"/>
																				&nbsp;<span id="text_reps_voice" style="cursor:pointer">${greetingInfo.greeting_voice}</span><span class="button small red" id="btn_reps_voice"  onClick="fnDelImage('reps_voice');">삭제</span>
																			</td>
																		</tr>
																		<tr align="center">
																			<th width="25%">배경이미지</th>
																			<td width="1%"></td>
																			<td width="74%" align="left" >
																				<input type="file" id="bg_image" name="bg_image" accept="image/*"/>
																				&nbsp;<span id="text_reps_image" style="cursor:pointer" onClick="fnImageView();">${greetingInfo.bg_image}</span><span class="button small red" id="btn_reps_image"  onClick="fnDelImage('reps_image');">삭제</span>
																			</td>
																		</tr>
																		<tr align="center">
																			<th width="25%">날짜</th>
																			<td width="1%"></td>
																			<td width="74%" align="left" >
																				<input type="radio" id="date_type1" name="date_type" value="1"
																					   <c:if test="${1 == greetingInfo.date_type}">checked</c:if>
																													><label for="date_type1">기본</label>
																				<input type="radio" id="date_type2" name="date_type" value="2"
																					   <c:if test="${2 == greetingInfo.date_type}">checked</c:if>
																													><label for="date_type2">특정일</label>
																				<input type="text" id="event_day" name="event_day" size="20" title="특정일" pattern="MMdd"
																					   <c:if test="${2 != greetingInfo.date_type}">style="display:none;"</c:if>
																					   <c:if test="${2 == greetingInfo.date_type}">value="${greetingInfo.event_day}"</c:if>
																													/>
																			</td>
																		</tr>
																		<tr align="center">
																			<th width="25%">시작시간</th>
																			<td width="1%"></td>
																			<td width="74%" align="left" >
																				<input type="text" id="start_point" name="start_point" size="20" title="시작시간" pattern="HHmm" value="${greetingInfo.start_point}"/>
																			</td>
																		</tr>
																		<tr align="center">
																			<th width="25%">종료시간</th>
																			<td width="1%"></td>
																			<td width="74%" align="left" >
																				<input type="text" id="end_point" name="end_point" size="20" title="종료시간" pattern="HH:mm" value="${greetingInfo.end_point}"/>
																			</td>
																		</tr>
																		</tbody>
																	</table>

																	<br>
																	<br>
																	<table border="0" cellpadding="0" cellspacing="0" width="450" >
																		<tr  align="right">
																			<td >
																				<input type="button" id="regbtn"   value="확인"    class="button small blue" align="right"/>
																				<input type="button" id="closebtn" value="닫기"    class="button small blue"/>
																			</td>
																		</tr>
																	</table>
																	<input type="hidden" id="write_id" name="write_id" value="<%=id_decrypt %>" />
																	<input type="hidden" id="order" name="order" value="${greetingInfo.order}" />
																	<input type="hidden" id="greeting_id" name="greeting_id" value="${greetingInfo.greeting_id}" />

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
		</tbody>
	</table>
</div>

</body>
</html>