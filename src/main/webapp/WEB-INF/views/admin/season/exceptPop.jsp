<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet"
	type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
KEYWORD_LIST = {};
ORDER = 1;
$(document).ready(function(){
	
	loadList();
	
	//추가버튼 클릭시
	$('#addBtn').click(function(){
		var keyword = $('#keyword').val();
		var addList = {};
		
		if(keyword == ''){
			alert('키워드를 입력해 주세요.');
			return;
		}
		
		addList = {"keyword" : keyword, "flag" : true};
		KEYWORD_LIST[ORDER] = addList;
		ORDER++;
		
		refreshList();
		
		$('#keyword').val('');
	});
	
	//등록버튼 클릭시
	$('#regBtn').click(function(){
		var addList = '';
		var startCnt = 0;
		for(key in KEYWORD_LIST){
			if(KEYWORD_LIST[key].flag == true){
				if(startCnt == 0){
					addList = KEYWORD_LIST[key].keyword;
				}else{
					addList += "\|\^" + KEYWORD_LIST[key].keyword ;
				}
				startCnt++;
			}
		}
		if(addList == ''){
			alert('키워드를 추가해 주세요.');
			return false;
		}
		
		if(confirm("저장하시겠습니까?")){
			try{
				$.blockUI({
					blockMsgClass: "ajax-loading",
					showOverlay: true,
					overlayCSS: { backgroundColor: '#CECDAD' } ,
					css: { border: 'none' } ,
					 message: "<b>처리중..</b>"
				});
				
				$.ajax({
					url: "./exceptProc.do",
					type: "POST",
					data: { 
							"addList" : addList,
						  },
					dataType:"json",
					success: function (rtn) {
						if (rtn.res=="0000") {
							alert("정상처리 되었습니다.");
						} else {
							alert("작업 중 오류가 발생하였습니다\nflag : " + rtn.res + "\ Message : " + rtn.msg);
						}
						$.unblockUI();
						$('#frm').submit();
					},
					error: function () {					
						alert("일시적인 장애가 발생하였습니다. 다시 시도해 주세요.");
						$.unblockUI();
					}
				});
			}catch(e){
				alert("작업 중 오류가 발생하였습니다");
			}
			
		}
	});
	
	//닫기버튼 클릭시
	$('#closeBtn').click(function(){
		self.close();
	});
});

//목록 갱신
function refreshList(){
	var html = '';
	for(key in KEYWORD_LIST){
		if(KEYWORD_LIST[key].flag == true){
			html += '<tr class="keyword_tr"><td>&nbsp;'+ KEYWORD_LIST[key].keyword +'</td>'
			 + '<td align="center"><span class="button small red" onclick="javascript:delBtn('+key+');">삭제</span></td></tr>';
		}
	}
	$('.keyword_tr').html('');
	$('#keyword_table').append(html);
}

//삭제버튼 클릭시
function delBtn(order){
	KEYWORD_LIST[order].flag = false;
	refreshList();
}

//초기 리스트 존재시 로딩
function loadList(){
	var list = '${list}';
	var addList = {};
	if(list != ''){
		var arr = list.split("\|\^");
		
		for(var i=0;i<arr.length;i++){
			addList = {"keyword" : arr[i], "flag" : true};
			KEYWORD_LIST[ORDER] = addList;
			ORDER++;
		}
	}
	refreshList();
}
</script>
<body leftmargin="0" topmargin="0">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
		<tbody>
			<tr>
				<td height="25">
					<table border="0" cellpadding="0" cellspacing="0" width="100%">
						<tbody>
							<tr>
								<td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
								<td class="bold">시즌 제외카테고리 키워드 관리</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
		</tbody>
	</table>
	<table width="100%">
		<tr>
			<td>
				<table border="0" cellpadding="0" cellspacing="0" width="100%"	class="board_list">
					<tbody id="keyword_table">
						<tr>
							<th width="70%">키워드</th>
							<th width="30%">&nbsp;</th>
						</tr>
						<tr align="center">
							<td><input type="text" id="keyword" style="width:96%" onKeyUp="checkByte($(this),'30')"></td>
							<td><span class="button small blue" id="addBtn">추가</span></td>
						</tr>
					</tbody>
				</table>
			</td>
		</tr>
	</table>
	<table border="0" cellpadding="0" cellspacing="0" width="100%" align="left">
		<tbody>
			<tr>
				<td height="40" align="right"><span class="button small blue" id="regBtn">저장</span> 
				<span class="button small blue" id="closeBtn">닫기</span>&nbsp;&nbsp;
				</td>
			</tr>
		</tbody>
	</table>
</body>
</html>