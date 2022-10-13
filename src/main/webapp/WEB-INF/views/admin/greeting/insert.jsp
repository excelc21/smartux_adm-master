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

    $("#regbtn").click(function(){

        const greeting_txt  = $('#greeting_txt');
        const greeting_voice = $('#greeting_voice');
        const bg_image  = $('#bg_image');
        const date_type = $('[name="date_type"]:checked');
        const event_day = $('#event_day');
        const start_point = $('#start_point');
        const end_point = $('#end_point');
        const write_id = $('#write_id');

        if(greeting_txt.val().replace(/\s/gi, '') === ""){
            alert("인사말을 입력해 주세요");
            greeting_txt.focus();
            return;
        } else if('false' == checkByteReturn(greeting_txt, '128')) {
            alert('인사말은 128Byte 이내로 입력해야 합니다.');
            greeting_txt.focus();
            return;
        } else if(greeting_voice.val() === ""){
            alert("인사음성을 선택해 주세요");
            greeting_voice.focus();
            return;
        } else if(bg_image.val() === ""){
            alert("배경이미지를 선택해 주세요");
            bg_image.focus();
            return;
        } else if(date_type.val() === "2" && event_day.val() === ""){
            alert("특정일을 입력해 주세요");
            event_day.focus();
            return;
        } else if(start_point.val() === ""){
            alert("시작시간을 선택해 주세요");
            start_point.focus();
            return;
        } else if(end_point.val() === ""){
            alert("종료시간을 선택해 주세요");
            end_point.focus();
            return;
        } else if(Number(start_point.val()) > Number(end_point.val())){
            alert("종료시간이 시작시간보다 큼니다.");
            start_point.focus();
            return;
        }

        let formData = new FormData();
        formData.append("greeting_txt", greeting_txt.val());
        formData.append("greeting_voice", greeting_voice[0].files[0]);
        formData.append("bg_image", bg_image[0].files[0]);
        formData.append("date_type", date_type.val());
        formData.append("event_day", event_day.val());
        formData.append("start_point", start_point.val());
        formData.append("end_point", end_point.val());
        formData.append("write_id", write_id.val());


        $.ajax({
            url: '<%=webRoot%>/admin/greeting/insert.do',
            data: formData,
            processData: false,
            contentType: false,
            async: false,
            type: 'POST',
            dataType: 'json',
            success: function(data){
                var flag = data.flag;
                var message = data.message;
                if(flag == "0000"){						// 정상적으로 처리된 경우
                    opener.location.reload();
                    alert("인사말이 등록되었습니다");
                    self.close();
                }else{
                        alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
                }
            },
            error: function(e){
                alert("일시적인 장애가 발생하였습니다. 다시 시도해 주세요.");
            }
        });
    });

	$("#resetbtn").click(function(){
		$("#regfrm")[0].reset();
	});

	$("#closebtn").click(function(){
		 window.close();
	});


});

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
						                                            <td width="15"><img src="<%=webRoot%>/images/admin/blt_07.gif" alt=""></td>
						                                            <td class="bold">인사말 등록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
												<form name="regfrm" id="regfrm" method="POST" action="<%=webRoot%>/admin/greeting/insert.do" encType="multipart/form-data">
					                            <table border="0" cellpadding="0" cellspacing="0" width="450" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">인사말</th>
					                                    <td width="1%"></td>
					                                    <td width="74%" align="left" >
                                                            <input type="text" id="greeting_txt" name="greeting_txt" size="20" title="인사말"/>
														</td>
					                                </tr>
													<tr align="center">
														<th width="25%">인사 음성</th>
														<td width="1%"></td>
														<td width="74%" align="left" >
															<input type="file" id="greeting_voice" name="greeting_voice" accept="audio/*"/>
															<input type="hidden" id="greeting_voice_old" name="greeting_voice_old"/>
														</td>
													</tr>
													<tr align="center">
														<th width="25%">배경이미지</th>
														<td width="1%"></td>
														<td width="74%" align="left" >
															<input type="file" id="bg_image" name="bg_image" accept="image/*"/>
															<input type="hidden" id="bg_image_old" name="bg_image_old" value=""/>
														</td>
													</tr>
                                                    <tr align="center">
                                                        <th width="25%">날짜</th>
                                                        <td width="1%"></td>
                                                        <td width="74%" align="left" >
                                                            <input type="radio" id="date_type1" name="date_type" checked value="1"><label for="date_type1">기본</label>
                                                            <input type="radio" id="date_type2" name="date_type" value="2"><label for="date_type2">특정일</label>
                                                            <input type="text" id="event_day" name="event_day" size="20" title="특정일" pattern="MMdd" style="display:none;"/>
                                                        </td>
                                                    </tr>
													<tr align="center">
														<th width="25%">시작시간</th>
														<td width="1%"></td>
														<td width="74%" align="left" >
															<input type="text" id="start_point" name="start_point" size="20" title="시작시간" pattern="HHmm"/>
														</td>
													</tr>
													<tr align="center">
														<th width="25%">종료시간</th>
														<td width="1%"></td>
														<td width="74%" align="left" >
															<input type="text" id="end_point" name="end_point" size="20" title="종료시간" pattern="HH:mm"/>
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
												    	<input type="button" id="resetbtn" value="재작성"  class="button small blue"/>
						                               	<input type="button" id="closebtn" value="닫기"    class="button small blue"/>
												    </td>
												  </tr>
												</table>
												<input type="hidden" id="write_id" name="write_id" value="<%=id_decrypt %>" />

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


