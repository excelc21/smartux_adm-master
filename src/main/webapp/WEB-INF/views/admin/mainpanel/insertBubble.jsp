<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<link href="/smartux_adm/css/anytime_style.css" rel="stylesheet" type="text/css">
<script src="/smartux_adm/js/anytime.js"></script>
<script type="text/javascript">

	var type = '${type}';
	var imageDelete = false;
	
	function fnImageDelete(){
	    if(!imageDelete){
	        imageDelete = true;
	        $('#image_url_text').hide();
	        $('#imageDeleteBtn').text("삭제취소");
	    }else{
	        imageDelete = false;
	        $('#image_url_text').show();
	        $('#imageDeleteBtn').text("삭제");
	    }
	}

    //등록
    function goInsert() {

        if($("#unlimit").is(":checked")){
            $('#infinity_yn').val("Y");
        }else{
            $('#infinity_yn').val("N");
            if($('#s_date').val() == ''){
                alert('시작일시를 선택해주세요.');
                $('#s_date').focus();
                return;
            }

            if($('#e_date').val() == ''){
                alert('종료일시를 선택해주세요.');
                $('#e_date').focus();
                return;
            }

            if($('#s_date').val() > $('#e_date').val()){
                alert('종료일시보다 시작일시가 클 수 없습니다.');
                $('#e_date').focus();
                return;
           }
        }
        
        if($('#show_cnt').val() == ''){
            alert('노출횟수를 입력해주세요');
            $('#show_cnt').focus();
            return;
        }

        if($('#show_sec').val() == ''){
            alert('노출시간(초)를 입력해주세요');
            $('#show_sec').focus();
            return;
        }
        
        if($('#title').val() == ''){
            alert('제목을 입력해주세요');
            $('#title').focus();
            return;
        }

        if(textareaCheck($('#content').val())){
            var check=textareaCheck($('#content').val());
            alert(check+'는 입력할수 없습니다.');
            $('#content').focus();
            return false;
        }

        var terminal_arr = "";
        if($("#terminal_all").is(":checked")){
            $('#terminal_all_yn').val("Y");
        }else{
            $('#terminal_all_yn').val("N");
            if($("input[name=terminal_list]").length<=0){ 
                alert("노출단말을 선택해 주세요");
                return false;
            }else{
                var values = [];
                $("input[name='terminal_list']").each(function() {
                    values.push($(this).val());
                });
                terminal_arr = values.join();
            }
        }

        if($('#link').val() == ''){
            alert('지면을 선택해주세요.');
            $('#link').focus();
            return;
        } 


        var formData = new FormData();
        formData.append("reg_no", $('#reg_no').val()); 
        formData.append("infinity_yn", $('#infinity_yn').val()); 
        formData.append("s_date", $('#s_date').val()); 
        formData.append("e_date", $('#e_date').val()); 
        formData.append("show_cnt", $('#show_cnt').val());
        formData.append("show_sec", $('#show_sec').val()); 
        formData.append("title", $('#title').val()); 
        formData.append("content", $('#content').val()); 
        formData.append("terminal_all_yn", $('#terminal_all_yn').val()); 
        formData.append("terminal_arr", terminal_arr); 
        formData.append("link", $('#link').val()); 
        formData.append("org_image_url", $('#org_image_url').val()); 
        formData.append("imageDelete", imageDelete); 
        if($('#image_url').val() != '') {
            console.log("image O")
            formData.append("image_url", $("#image_url")[0].files[0]);
        } else{
            console.log("image X")
        }

        console.log(formData)
        
        if(type == "insert"){
            if(confirm("등록하시겠습니까?")) {
                $.blockUI({
                    blockMsgClass: "ajax-loading",
                    showOverlay: true,
                    overlayCSS: { backgroundColor: '#CECDAD' } ,
                    css: { border: 'none' } ,
                     message: "<b>로딩중..</b>"
                });

                $.ajax({
                    url: "./insertBubble.do",
                    type: "POST",
                    data: formData,
                    processData: false,
                    contentType: false,
                    async: true,
                    dataType : "json",
                    success: function (rtn) {
                        if (rtn.flag=="0000") {
                            alert("말풍선이 성공적으로 수정되었습니다.");
                            $(location).attr('href', './getBubbleList.do');
                        } else {
                            alert("작업 중 오류가 발생하였습니다\nflag : " + rtn.flag + "\ Message : " + rtn.message);
                        }
                        $.unblockUI();
                    },
                    complete: function () {
                        $(location).attr('href',"./getBubbleList.do"); 
                    },
                    error: function (e) {                   
                        alert("일시적인 장애가 발생하였습니다. 다시 시도해 주세요.");
                        $.unblockUI();
                    }
                });
            }
        }else if(type == "update"){
            if(confirm("수정하시겠습니까?")) {
                $.blockUI({
                    blockMsgClass: "ajax-loading",
                    showOverlay: true,
                    overlayCSS: { backgroundColor: '#CECDAD' } ,
                    css: { border: 'none' } ,
                     message: "<b>로딩중..</b>"
                });

                $.ajax({
                    url: "./updateBubble.do",
                    type: "POST",
                    data: formData,
                    processData: false,
                    contentType: false,
                    async: true,
                    dataType : "json",
                    success: function (rtn) {
                        if (rtn.flag=="0000") {
                            alert("말풍선이 성공적으로 수정되었습니다.");
                            $(location).attr('href', './getBubbleList.do');
                        } else {
                            alert("작업 중 오류가 발생하였습니다\nflag : " + rtn.flag + "\ Message : " + rtn.message);
                        }
                        $.unblockUI();
                    },
                    complete: function () {
                        $(location).attr('href',"./getBubbleList.do"); 
                    },
                    error: function (e) {                   
                        alert("일시적인 장애가 발생하였습니다. 다시 시도해 주세요.");
                        $.unblockUI();
                    }
                });
            }
        }
    }
    
    function getPanel(){
    	window.open("<%=webRoot%>/admin/mainpanel/getPanelTitleListPop.do?callbak=getTitleCallcack", "regcode", "width=450,height=200");
    }

    function getTitleCallcack(data){
        var pannel_id = data.pannel_id;
        var title_id = data.title_id;

        $('#link').val(pannel_id+"_"+title_id);
    }

    $(document).ready(function() {

        AnyTime.picker('s_date', { format: '%z-%m-%d %H:00', labelTitle: '날짜', labelHour: '시간', time: ''});
        AnyTime.picker('e_date', { format: '%z-%m-%d %H:59', labelTitle: '날짜', labelHour: '시간', time: ''});

        //무제한 체크박스
        $('#unlimit').click(function(){
            if($("#unlimit").is(":checked") == true) {
                $('#s_date').val("");
                $('#e_date').val("");

                $('#s_date').attr("disabled", true);
                $('#e_date').attr("disabled", true);
            }else{
                $('#s_date').attr("disabled", false);
                $('#e_date').attr("disabled", false);
            }
        })

        //노출단말 버튼
        $("#termPopup").click(function(){
            //HDTV 관리의 단말 팝업을 같이 쓴다.
            termCheck=window.open("<%=webRoot%>/admin/noti/getTerm.do?scr_tp=${scr_tp}", "termCheck", "width=500,height=700,scrollbars=yes" );
            termCheck.opener=self;
        });

        $("#terminal_all").click(function (e) {             
            if($("#terminal_all").is(":checked")){
                $("#terminal_div").html("");
                $("#termPopup").attr("style","display:none");
            }else{          
                $("#termPopup").attr("style","display:block");  
            }
        });

        $("#img_file").change(function(){
            if(this.value != ''){
                if(imageFileCheck(this.value,this.id)){
                    alert("이미지 파일이 아닙니다.");
                }
            }
        });
        
        $("#image_url").change(function(){
            if(this.value != ''){
                if(imageFileCheck(this.value,this.id)){
                    alert("이미지 파일이 아닙니다.");
                }
            }
        });

        if(type == "update"){
        	$.ajax({
                url: '<%=webRoot%>/admin/mainpanel/getBubbleDetail',
                type: 'POST',
                dataType: 'json',
                data: {
                    reg_no: '${reg_no}'
                },
                error: function () {
                    alert('화면로딩중 오류가 발생하였습니다');
                },
                success: function (bubble) {
                	$('#reg_no').val(bubble.reg_no);
                    
                    if(bubble.infinity_yn == "Y"){
                        $("#unlimit").attr("checked", true);
                        $('#s_date').val("");
                        $('#e_date').val("");

                        $('#s_date').attr("disabled", true);
                        $('#e_date').attr("disabled", true);
                    }else{
                        $('#s_date').val(bubble.s_date);
                        $('#e_date').val(bubble.e_date);
                        $('#s_date').attr("disabled", false);
                        $('#e_date').attr("disabled", false);
                    }

                    $('#show_cnt').val(bubble.show_cnt);
                    $('#show_sec').val(bubble.show_sec);
                    
                    var Ca = /\+/g;
                    $('#title').val(decodeURIComponent( bubble.title.replace(Ca, " ")));

                    

                    var content = bubble.content;
                    if(content != '' && content != null){
                    	content = decodeURIComponent( bubble.content.replace(Ca, " "));
                        content = content.replace(/(newline)/g, '\r\n');
                    }
                    $('#content').val(content);

                    if(bubble.terminal_arr != null && bubble.terminal_arr != ""){
                        var terminal_arr = bubble.terminal_arr.split(",");
                        var terminal_html = "<br>";
                        if(terminal_arr.length > 0){
                            for(var i = 0 ; i < terminal_arr.length; i++){
                                console.log(terminal_arr[i])
                                if(terminal_arr[i] != ''){
                                    var dHmtl = "";
                                    dHtml = terminal_arr[i]+"  /  <input type='hidden' name='terminal_list' value="+terminal_arr[i]+">";
                                    terminal_html += dHtml;
                                }
                            }
                            $("#terminal_all").attr("checked", false);
                            $("#termPopup").attr("style","display:block");  
                            $('#terminal_div').html(terminal_html);
                        }
                    }else{
                        $("#terminal_all").attr("checked", true);
                        $("#termPopup").attr("style","display:none");  
                    }
                    
                    

                    $('#link').val(bubble.link);
                }
            });
            
        }

        $('#image_url').change(function(){
            imageDelete = false;
            $('#image_url_text').hide();
            $('#imageDeleteBtn').hide();
        })
    })
    
    //이미지 파일 검사
    function imageFileCheck(filename, inputname){
    
       var fileName=filename;    
       var fileSuffix =fileName.substring(fileName.lastIndexOf(".") + 1);
       fileSuffix = fileSuffix.toLowerCase();
       if (!( "jpg" == fileSuffix || "jpeg" == fileSuffix  || "gif" == fileSuffix || "bmp" == fileSuffix || "png" == fileSuffix )){
            
            //익스플로러 버전 비교 파일 초기화.
            browserCheckReset(inputname);
            return true;
       }else{
            var size = document.getElementById(inputname).files[0].size;
            var maxSize = 102400;
            var fileSize = Math.round(size);
            
            if(fileSize > maxSize) {
                alert("이미지 용량을 초과 했습니다.\n최대 이미지 사이즈 : 100KB");
                browserCheckReset(inputname);
                return;
            }
        }
        return false;
    }

    function checkNum(obj){
        var txt = obj.value;
        var num = "1234567890";
        var i=0;
        for(i=0;i<txt.length;i++){
            if(num.indexOf(txt.charAt(i)) < 0){
                alert("숫자만 입력 가능합니다.");
                obj.value = txt.substring(0,i);
                obj.focus();
                return false;
            }
        }
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
                <tr>
                    <td height="42" width="100%">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tbody>
                            <tr>
                                <td width="300" class="boldTitle">
                                	<!-- 카테고리 타이틀 -->
                                    <!-- <img src="/smartux_adm/images/admin/category_map_title.gif">-->
                                   말풍선 관리
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
						                                            <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
						                                            <td class="bold">말풍선 등록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="form1" name="form1" method="post">
						                        <input type="hidden" id="reg_no" name="reg_no" />
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" class="board_data">
					                                <tbody>
					                                   <tr align="center">
                                                            <th width="25%">기간</th>
                                                            <td width="5%"></td>
                                                            <td width="70%" align="left">
                                                                <input type="checkbox" class="checkbox" id="unlimit"/><label>무제한</label>
                                                                <input type="hidden" id="infinity_yn" name="infinity_yn" />
                                                                <input type="text" id="s_date" name="s_date" size="16" title="시작일시" value=""> ~
                                                                <input type="text" id="e_date" name="e_date" size="16" title="종료일시" value="">
                                                            </td>
                                                        </tr>
                                                        <tr align="center">
                                                            <th width="25%">노출횟수</th>
                                                            <td width="5%"></td>
                                                            <td width="70%" align="left" >
                                                                <input type="text" id="show_cnt" name="show_cnt" size="5" style="font-size: 12px;" value="" onkeyup="checkNum(this);" maxlength="3"/>
                                                            </td>
                                                        </tr>
                                                        <tr align="center">
                                                            <th width="25%">노출시간(초)</th>
                                                            <td width="5%"></td>
                                                            <td width="70%" align="left" >
                                                                <input type="text" id="show_sec" name="show_sec" size="5" style="font-size: 12px;" value="" onkeyup="checkNum(this);" maxlength="3"/>
                                                            </td>
                                                        </tr>
                                                        <tr align="center">
                                                            <th width="25%">제목</th>
                                                            <td width="5%"></td>
                                                            <td width="70%" align="left" >
                                                                <input type="text" id="title" name="title" style="width:60%" style="font-size: 12px;" value="" onKeyUp="checkByte($(this),'50')"/>
                                                                (길이제한 / 50byte)    
                                                            </td>
                                                        </tr>
                                                        
                                                        <tr align="center">
                                                            <th width="25%">내용</th>
                                                            <td width="5%"></td>
                                                            <td width="70%" align="left" >
                                                                <textarea rows="7" cols="40" name="content" id="content" onKeyUp="checkByte($(this),'4000')"></textarea>
                                                                <br>
                                                                [\f, !^, \b, \f88, \f99] 사용제한 / 4000byte 까지 허용
                                                                
                                                            </td>
                                                        </tr>
                                                        
                                                        <tr align="center" >
                                                            <th width="25%">노출 단말 선택</th>
                                                            <td width="5%"></td>
                                                            <td width="70%" align="left" >
                                                                All <input type="checkbox"  id="terminal_all" checked="checked"> 
                                                                <input type="hidden" id="terminal_all_yn" name="terminal_all_yn" />                                                                               
                                                                <a href="#" id="termPopup" style="display:none;"><span class="button small blue" > 단말기 선택</span> </a>
                                                                <div  id="terminal_div">
                                                                </div>
                                                                </td>                                                   
                                                        </tr>
                                                        
                                                        <tr align="center">
                                                            <th width="25%">이미지</th>
                                                            <td width="5%"></td>
                                                            <td width="70%" align="left" >
                                                                <input type="file" id="image_url"  accept="image/*" name="image_url" value="파일선택" class="imgfile"/>
                                                                <input type="hidden" id="org_image_url" name="org_image_url" value="${bubble.img}"/>
                                                                <c:if test="${not empty bubble.img}">  
                                                                    <span id="image_url_text" style="line-height:22px;font-weight:bold">
                                                                        ${bubble.img}
                                                                    </span>
                                                                    <span class="button small blue" id="imageDeleteBtn" onclick="fnImageDelete();">삭제</span>
                                                                </c:if>                         
                                                            </td>
                                                        </tr> 
                                                        
                                                        <tr align="center">
                                                            <th width="25%">지면</th>
                                                            <td width="5%"></td>
                                                            <td width="70%" align="left" >
                                                                <input type="text" id="link" name="link" size="20" style="font-size: 12px;" value="" READONLY />
                                                                <span class="button small blue" id="searchBtn" onClick="getPanel()">지면선택</span>         
                                                            </td>
                                                        </tr>
					                            	</tbody>
					                            </table>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
                                                            <a href="javascript:goInsert();"><span class="button small blue">등록</span></a>
                                                            <a href="./getBubbleList.do"><span class="button small blue">목록</span></a> 
                                                        </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
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