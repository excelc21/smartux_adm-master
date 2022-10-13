<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<title>LG U+ IPTV SmartUX</title>


<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>

<script type="text/javascript">
var SR_LIST = {};	//별점 리스트
var DEL_LIST = {};	//삭제된 별점 리스트
var ORDER = 1;		//순서(key)
var DEL_IDX = 1;	//삭제 인덱스
var UPDATE_KEY = '';//수정된 보기의 키
$(document).ready(function(){
	//수정화면 진입시 기존데이터 화면에 출력
	reportDetailView('${srList}');
	
	$('#addBtn').click(function(){
		var img_file = '';
		if($('#sr_title').val() == ''){
			alert('별점 제목을 입력해 주세요.');
			$('#sr_title').focus();
			return;
		}
		
		if($('#sr_point').val() == ''){
			alert('별점 점수를 입력해 주세요.');
			$('#sr_point').focus();
			return;
		}
		
		if($('#img_file').val() != ''){
			img_file = imgFileUpload();
		}
		
		if(UPDATE_KEY == ''){
			refreshOrder();
			var sr = { "newYn"   : "Y", 
						"sr_title" : $('#sr_title').val(),
						"sr_order" : ORDER,
						"sr_point" : $('#sr_point').val(),
						"img_file" : img_file,
						"sr_pid"   : $('#sr_pid').val(),
						"delYn"   : "N",
						"sr_id"     : ""
					  };
			SR_LIST[ORDER] = sr;
			ORDER++;
		}else{
			SR_LIST[UPDATE_KEY].sr_title = $('#sr_title').val();
			SR_LIST[UPDATE_KEY].sr_point = $('#sr_point').val();
			if(img_file != ''){
				img_file = imgFileUpload();
				SR_LIST[UPDATE_KEY].img_file = img_file;
			}
			
			UPDATE_KEY = '';
			$('#addBtn').text('등록');
			refreshOrder();
		}
		
		refreshData();
		$('#sr_title').val('');
		$('#sr_point').val('');
		browserCheckReset('img_file');
		$('#sr_title').focus();
		
	});
	
	$('#sumitBtn').click(function(){
		if($('#p_title').val() == ''){
			alert('제목을 입력해 주세요.');
			$('#p_title').focus();
			return;
		}
		
		var chk_flag = false;
		
		for(key in SR_LIST){
			if(SR_LIST[key] != null){
				if(SR_LIST[key].delYn == 'N'){
					chk_flag = true;
					break;
				}
			}
		}
		
		if(!chk_flag){
			alert('별점을 등록해 주세요.');
			return;
		}
		
		if(confirm('저장하시겠습니까?')){
			try{
				$.blockUI({
					blockMsgClass: "ajax-loading",
					showOverlay: true,
					overlayCSS: { backgroundColor: '#CECDAD' } ,
					css: { border: 'none' } ,
					 message: "<b>처리중..</b>"
				});
			
				var url = '';
				var srList = new Array();
				
				refreshOrder();
				
				for(key in SR_LIST){
					if(SR_LIST[key].delYn == 'N'){
						var data = SR_LIST[key].newYn 
						+ '|' + SR_LIST[key].sr_title 
						+ '|' + SR_LIST[key].sr_order
						+ '|' + SR_LIST[key].sr_point
						+ '|' + SR_LIST[key].img_file
						+ '|' + SR_LIST[key].sr_pid 
						+ '|' + SR_LIST[key].delYn
						+ '|' + SR_LIST[key].sr_id;
					}
					srList.push(data);
				}
				
				if($('#isUpdate').val() == '0'){
					url = "./insertProc.do";
				}else{
					url = "./updateProc.do";
					
					for(key in DEL_LIST){
						var data = DEL_LIST[key].newYn 
						+ '|' + DEL_LIST[key].sr_title 
						+ '|' + DEL_LIST[key].sr_order 
						+ '|' + DEL_LIST[key].sr_point
						+ '|' + DEL_LIST[key].img_file
						+ '|' + DEL_LIST[key].sr_pid 
						+ '|' + DEL_LIST[key].delYn
						+ '|' + DEL_LIST[key].sr_id;
						
						srList.push(data);
					}
				}
				$.ajax({
					url: url,
					type: "GET",
					data: { 
							"p_title" : $('#p_title').val(),
							"sr_pid" : $('#sr_pid').val(),
							"srList" : srList,
							"system_gb" : $('#system_gb').val()
						  },
					dataType : "json",
					success: function (rtn) {
						if (rtn.res=="0000") {
							alert("정상처리 되었습니다.");
						} else {
							alert("작업 중 오류가 발생하였습니다\nflag : " + rtn.res + "\ Message : " + rtn.msg);
						}
						$.unblockUI();
					},
					complete: function () {
						$(location).attr('href',"./getStarRatingList.do?system_gb=${vo.system_gb }&use_yn=${vo.use_yn}&findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}"); 
					},
					error: function (e) {					
						alert("일시적인 장애가 발생하였습니다. 다시 시도해 주세요.");
						$.unblockUI();
					}
				});
			}catch(e){
				alert("작업 중 오류가 발생하였습니다");
			}
		}
	});
	
	$("#img_file").change(function(){
		if(this.value != ''){
			if(imageFileCheck(this.value,this.id)){
				alert("이미지 파일이 아닙니다.");
			}
		}
	});
});

//화면 갱신
function refreshData(){
	var html = '';
	var tr = '';
	var table = '<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%"><tbody id="t_data">';
	var idx = 1;
	var imgServer = '${imgServer}';
	
	for(key in SR_LIST){
		if(SR_LIST[key].delYn == 'N'){
			flag = true;
			
			var img = '';
			
			if(SR_LIST[key].img_file != ''){
				img = '<a href="#!" class="preview" rel="' + imgServer + SR_LIST[key].img_file + '"><span class="button small blue">이미지</span></a>';
			}
			if(1 < idx){
				tr += '<tr><th width="25%">' + idx + '</th>'
					+ '<td width="5%"></td>'
					+ '<td width="70%" align="left" >'
					+ '</td></tr>';
			}
			table += '<tr name="r_row" value="' + key + '">'
				  + '<td width="30%"><a onclick="javascript:fnUpdate(\'' + key + '\');" style="cursor: pointer;"><B>' + SR_LIST[key].sr_title + '</B></a></td>'
				  + '<td width="5%"></td>'
	        	  + '<td width="5%">' + SR_LIST[key].sr_point + '</td>'
	        	  + '<td width="5%"></td>'
	        	  + '<td width="30%">' + img + '</td>'
	        	  + '<td width="20%"><a onclick="javascript:fnDel(\'' + key + '\');"><span class="button small red">삭제</span></a></td>'
	        	  + '</td></tr>';
	        idx++;
		}
	}
	
	
	html = '<tr><th width="25%">1</th>'
	+ '<td width="5%" rowspan="' + idx + '"></td>'
	+ '<td width="70%" align="left" rowspan="' + idx + '">'
	+ table + '<tbody></table>'
	+ '</td></tr>'
	+ tr;
	
	$('#r_data').html(html);
	$( "#t_data" ).sortable();
    $( "#t_data" ).disableSelection();
    imagePreview();
}

//현재 화면에 보이는 순서대로 데이터 갱신
function refreshOrder(){
	var NEW_LIST = {};
	var r_row = $('tr [name=r_row]');
	
	$(r_row).each(function(idx){
		var key = $(r_row[idx]).attr('value');
		var newYn = SR_LIST[key].newYn;
		var sr_title = SR_LIST[key].sr_title;
		var sr_order = idx+1;
		var sr_point = SR_LIST[key].sr_point;
		var img_file = SR_LIST[key].img_file;
		var sr_pid = SR_LIST[key].sr_pid;
		var delYn = 'N';
		var sr_id = SR_LIST[key].sr_id;
		
		NEW_LIST[idx+1] = { "newYn" : newYn, 
						  "sr_title" : sr_title,
						  "sr_order" : sr_order,
						  "sr_point" : sr_point,
						  "img_file" : img_file,
						  "sr_pid"   : sr_pid,
						  "delYn"   : delYn,
						  "sr_id"     : sr_id
						};
	});
	SR_LIST = NEW_LIST;
}

//수정
function fnUpdate(key){
	UPDATE_KEY = key;
	$('#addBtn').text('수정');
	$('#sr_title').val(SR_LIST[key].sr_title);
	$('#sr_point').val(SR_LIST[key].sr_point);
}

//삭제
function fnDel(key){
	SR_LIST[key].delYn = 'Y';
	if(SR_LIST[key].newYn = 'N'){
		DEL_LIST[DEL_IDX] = SR_LIST[key];
		DEL_IDX++;
	}
	refreshData();
}

//수정화면 진입시 기존데이터 화면에 출력
function reportDetailView(obj){
	var srList = obj;
	
	if(srList != ""){
		var row = srList.split("@^");
		var col = "";
		
		for(var i=0;i<row.length;i++){
			col = row[i].split("\|");
			
			var addData = {};
			
			var img_file = '';
			
			if(col[3] != 'null' && col[3] != ''){
				img_file = col[3];
			}
			
			if(SR_LIST[ORDER] == null){
				addData = {
						"newYn"   : "N",
						"sr_order": col[0],
						"sr_point": col[1],
						"sr_title": col[2],
						"img_file": img_file,
						"sr_id"   : col[4],
						"delYn"   : "N",
						"sr_pid"  : $('#sr_pid').val()
						  };
				SR_LIST[ORDER] = addData;
				ORDER ++;
			}
		}
		
		refreshData();
	}
}

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

//파일 초기화
function browserCheckReset(fileId) {
	//익스플로러 버전 비교 파일 초기화.
	if(/MSIE/.test(navigator.userAgent)){
	    //IE 10.0 이하면
	    if(navigator.appVersion.indexOf("MSIE 10.0")>=0 || navigator.appVersion.indexOf("MSIE 8.0")>=0){
	    	$("#"+fileId+"").replaceWith( $("#"+fileId+"").clone(true) );
	    }else{	 //IE 10.0 이상 또는 크롭 / 파폭
	    	$("#"+fileId+"").attr("value","");	
 		}
	} else {
		$("#"+fileId+"").attr("value","");	
	}
}

//이미지 업로드
function imgFileUpload(){
	var img_file = '';
	
	$.blockUI({
		blockMsgClass: "ajax-loading",
		showOverlay: true,
		overlayCSS: { backgroundColor: '#CECDAD' } ,
		css: { border: 'none' } ,
		 message: "<b>처리중..</b>"
	});
	
	var formData = new FormData();
	formData.append("img_file", $("#img_file")[0].files[0]);   
	
	$.ajax({
		url: './imgFileUpload.do',
		data: formData,
		processData: false,
		contentType: false,
		async: false,
		type: 'POST',
		dataType: 'json',
		success: function(data){
			if(data.result.res !="0000"){
				alert("작업 중 오류가 발생하였습니다\nflag : " + data.result.res + "\ Message : " + data.result.msg);
			}else{
				img_file = data.result.file_name;
			}
			$.unblockUI();
		},
		error: function(e){
			alert("일시적인 장애가 발생하였습니다. 다시 시도해 주세요.");
			$.unblockUI();
		}
	});
	
	return img_file;
}

//이미지 미리보기
function imagePreview(){	
	xOffset = 10;
	yOffset = 30;
		
	$("a.preview").hover(function(e){
		this.t = this.title;
		this.title = "";	
		var c = (this.t != "") ? "<br/>" + this.t : "";
		$(this).append("<p id='preview'><img src='"+ this.rel +"' alt='Image preview' />"+ c +"</p>");
		$("#preview")
			.css("top",(e.pageY - xOffset) + "px")
			.css("left",(e.pageX + yOffset) + "px")
			.css("position","absolute")
			.css("z-index","1")
			.fadeIn("fast");	
		
    },
	function(){
		this.title = this.t;	
		$("#preview").remove();
    });	
	$("a.preview").mousemove(function(e){
		$("#preview")
			.css("top",(e.pageY - xOffset) + "px")
			.css("left",(e.pageX + yOffset) + "px")
			.css("position","absolute")
			.css("z-index","1");
	});			
}

//숫자 입력
function onlyNumber(obj){
	var digits="0123456789";
	var temp;
	var val='';
	var num=obj.value;
	 for(var i=0;i<num.length;i++){       
	  temp=num.substring(i,i+1);    
	  if(digits.indexOf(temp)==-1){        
	  }else{
	   val=val+temp;
	  }
	 }
	 if(val>0) obj.value=val;
	 else obj.value=0;  // 기본값
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
                                	별점 관리
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
						                                            <td class="bold">
						                                            	별점 등록
							                                        </td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="form1" name="form1" action="./regStarRatingProc.do" method="get">
						                        <input type="hidden" id="isUpdate" name="isUpdate" value="${isUpdate }"/>
						                        <input type="hidden" id="sr_pid" name="sr_pid" value="${vo.sr_pid }"/>
						                        <input type="hidden" id="system_gb" name="system_gb" value="${vo.system_gb }"/>
					                            <table border="0" cellpadding="0" cellspacing="0" width="700" class="board_data">
					                                <tbody>
					                                	<!-- 제목 -->					                             	
														<tr align="center">
															<th width="25%">제목</th>
															<td width="5%"></td>								                                    
															<td width="70%" align="left" >
																<input type="text" id="p_title" name="p_title" style="width: 90%;" onKeyUp="checkByte($(this),'200')" value="<c:out value="${title }"/>"/>
															</td>									
														</tr>
														<!-- 별점 -->
						                                <tr align="center">
						                                    <th width="25%">별점제목</th>
						                                    <td width="5%"></td>
						                                    <td width="70%" align="left" >
						                                    <input type="text" id="sr_title" name="sr_title" style="width: 30%" onKeyUp="checkByte($(this),'200')"/>
						                                    <input type="text" id="sr_point" name="sr_point" style="width: 5%" onchange="onlyNumber(this)"/>
						                                    <input type="file" id="img_file" name="img_file" style="width: 40%"/>
						                                    <span class="button small blue" id="addBtn">등록</span>
															</td>
														 </tr>
													</tbody>
													<tbody id="r_data">
													</tbody>
												</table>
												
					                            <!-- 등록/수정 버튼 -->
					                            <table border="0" cellpadding="0" cellspacing="0" width="700" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<c:if test="${isUpdate eq '0'}">
						                                		<span class="button small blue" id="sumitBtn">등록</span>
						                                	</c:if>
						                                	<c:if test="${isUpdate eq '1'}">
						                                		<span class="button small blue" id="sumitBtn">수정</span>
						                                	</c:if>
						                                	<a href="./getStarRatingList.do?system_gb=${vo.system_gb }&use_yn=${vo.use_yn}&findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}"><span class="button small blue">목록</span></a>
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