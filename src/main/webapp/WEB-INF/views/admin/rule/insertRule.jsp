<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
var idcnt = 1;
var maxprice = 99999;
$(document).ready(function(){
	// 처음 선택된 rule_type 값에 따라 화면을 초기화해준다
	makeUI();
	
	$("#rule_type").change(function(){
		makeUI();
	});
	
	$("#regbtn").click(function(){
		var selval = $("#rule_type").val();
		var validate = true;
		if(selval == "D"){					// 일자별
			validate = validateDUI();
			if(validate == true){
				regDUI();
			}
		}else if(selval == "P"){			// 가격별
			validate = validatePUI();
			if(validate == true){
				regPUI();
			}
		}else if(selval == "C"){			// 유/무료별
			validate = validateCUI();
			if(validate == true){
				regCUI();
			}
		}else if(selval == "G"){			// 장르별
			validate = validateGUI();
			if(validate == true){
				regGUI();
			}
		}else if(selval == "S"){			// 시리즈별
			validate = validateSUI();
			if(validate == true){
				regSUI();
			}
		}
		
	});
	
});

// 사용자가 Rule Type을 선택할때 마다 그에 따른 UI를 동적으로 그리도록 한다
function makeUI(){
	var selval = $("#rule_type").val();
	if(selval == "D"){			// 일자별
		makeDUI();
	}else if(selval == "P"){	// 가격별
		makePUI();
	}else if(selval == "C"){	// 유/무료별
		makeCUI();
	}else if(selval == "G"){	// 장르별
		makeGUI();
	}else if(selval == "S"){	// 시리즈별
		makeSUI();
	}
}

function makeDUI(){
	var html = [], h=-1;
	html[++h] = "<span class=\"button small blue\" id=\"adddweight\">추가</span><br/><br/>\n";
	html[++h] = "<table id=\"dtable\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"board_data\">\n";
	// html[++h] = "<tr>\n";
	// html[++h] = "<td colspan=\"3\"><span class=\"button small blue\" id=\"adddweight\">추가</span></td>\n";
	// html[++h] = "</tr>\n";
	html[++h] = "<tr align=\"center\">\n";
	html[++h] = "<th width=\"10%\" align=\"center\">일차</td>\n";
	html[++h] = "<th colspan=\"2\">가중치</td>\n";
	// html[++h] = "<th width=\"10%\" align=\"center\"></td>\n";
	// html[++h] = "<td></td>\n";
	html[++h] = "</tr>\n";
	html[++h] = "<tr>\n";
	html[++h] = "<td name=\"daystext\" style=\"text-align:center\">Today - 1</td>\n";
	html[++h] = "<td><input type=\"text\" name=\"dweight\" style=\"text-align:right\"/></td>\n";
	html[++h] = "<td width=\"10%\" align=\"center\"><span class=\"button small blue\" name=\"delrowbtn\">삭제</span></td>\n";
	html[++h] = "</tr>\n";
	html[++h] = "</table>\n";
	
	$("#uiarea").html(html.join(''));
	
	$("#adddweight").unbind("click");
	$("span[name='delrowbtn']").unbind("click");
	$("input[name='dweight']").unbind("keypress");
	
	$("#adddweight").click(function(){
		var html = [], h=-1, idx=1;
		/*
		html[++h] = "<tr>\n";
		html[++h] = "<td><input type=\"text\" name=\"dweight\"/></td>\n";
		html[++h] = "<td><span class=\"button small blue\" name=\"delrowbtn\">삭제</span></td>\n";
		html[++h] = "</tr>\n";
		*/
		html[++h] = "<tr>\n";
		html[++h] = "<td name=\"daystext\" style=\"text-align:center\">Today - 1</td>\n";
		html[++h] = "<td><input type=\"text\" name=\"dweight\" style=\"text-align:right\"/></td>\n";
		html[++h] = "<td width=\"10%\" align=\"center\"><span class=\"button small blue\" name=\"delrowbtn\">삭제</span></td>\n";
		html[++h] = "</tr>\n";
		
		$("#dtable").append(html.join(''));
		
		$("td[name='daystext']").each(function(){
			$(this).text("Today - " + idx++);
		});
		
		$("span[name='delrowbtn']").unbind("click");
		
		$("span[name='delrowbtn']").click(function(){
			var idx=1;
			$(this).parent().parent().remove();
			$("td[name='daystext']").each(function(){
				$(this).text("Today - " + idx++);
			});
		});
		
		$("input[name='dweight']").unbind("keypress");
		$("input[name='dweight']").keypress(function(event){
			var key = event.which;
			var result = onlyNumberAndMark(key);
			if(result == false){
				event.preventDefault();
			}
			
		});
	});
	
	$("span[name='delrowbtn']").click(function(){
		var idx=1;
		$(this).parent().parent().remove();
		$("td[name='daystext']").each(function(){
			$(this).text("Today - " + idx++);
		});
	});
	
	$("input[name='dweight']").keypress(function(event){
		var key = event.which;
		var result = onlyNumberAndMark(key);
		if(result == false){
			event.preventDefault();
		}
		
	});
	
}

function makePUI(){
	var html = [], h=-1;
	html[++h] = "<span class=\"button small blue\" id=\"addpweight\">추가</span><br/><br/>\n";
	html[++h] = "<table id=\"ptable\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"board_data\">\n";
	// html[++h] = "<tr>\n";
	// html[++h] = "<td colspan=\"3\"><span class=\"button small blue\" id=\"addpweight\">추가</span></td>\n";
	// html[++h] = "</tr>\n";
	html[++h] = "<tr>\n";
	html[++h] = "<th width=\"25%\" align=\"center\">시작가격</td>\n";
	html[++h] = "<th width=\"25%\" align=\"center\">끝가격</td>\n";
	html[++h] = "<th colspan=\"2\" align=\"center\">가중치</td>\n";
	html[++h] = "</tr>\n";
	html[++h] = "<tr>\n";
	html[++h] = "<td><input type=\"text\" name=\"pstart\" readonly value=\"0\" style=\"text-align:right\"/></td>\n";
	html[++h] = "<td><input type=\"text\" name=\"pend\" value=\"" + maxprice + "\"  style=\"text-align:right\"/></td>\n";
	html[++h] = "<td><input type=\"text\" name=\"pweight\"  style=\"text-align:right\"/></td>\n";
	html[++h] = "<td width=\"10%\" align=\"center\"><span class=\"button small blue\" name=\"delrowbtn\">삭제</span></td>\n";
	html[++h] = "</tr>\n";
	html[++h] = "</table>\n";
	
	$("#uiarea").html(html.join(''));
	
	$("#addpweight").unbind("click");
	$("span[name='delrowbtn']").unbind("click");
	$("input[name='pend']").unbind("blur");
	$("input[name='pend']").unbind("keypress");
	$("input[name='pweight']").unbind("keypress");
	
	$("#addpweight").click(function(){
		var html = [], h=-1;
		
		var pendlength = parseInt($("input[name='pend']").length, 10);
		var startnum = 0;
		
		if(pendlength == 0){
			startnum = 0;
		}else{
			var lastpend = $("input[name='pend']").get(pendlength-1);
			var pendval = $(lastpend).val();
			startnum = parseInt(pendval, 10) + 1;
		}
		
		if(parseInt(startnum, 10) < (maxprice + 1)){
			/*
			html[++h] = "<tr>\n";
			html[++h] = "<td><input type=\"text\" name=\"pstart\" readonly value=\"" + startnum + "\"/></td>\n";
			html[++h] = "<td><input type=\"text\" name=\"pend\" value=\"" + maxprice + "\"/></td>\n";
			html[++h] = "<td><input type=\"text\" name=\"pweight\"/></td>\n";
			html[++h] = "<td><span class=\"button small blue\" name=\"delrowbtn\">삭제</span></td>\n";
			html[++h] = "</tr>\n";
			*/
			
			html[++h] = "<tr>\n";
			html[++h] = "<td><input type=\"text\" name=\"pstart\" readonly value=\"" + startnum + "\" style=\"text-align:right\"/></td>\n";
			html[++h] = "<td><input type=\"text\" name=\"pend\" value=\"" + maxprice + "\"  style=\"text-align:right\"/></td>\n";
			html[++h] = "<td><input type=\"text\" name=\"pweight\"  style=\"text-align:right\"/></td>\n";
			html[++h] = "<td width=\"10%\" align=\"center\"><span class=\"button small blue\" name=\"delrowbtn\">삭제</span></td>\n";
			html[++h] = "</tr>\n";
			
			$("#ptable").append(html.join(''));
			
			$("span[name='delrowbtn']").unbind("click");
			
			$("span[name='delrowbtn']").click(function(){
				$(this).parent().parent().remove();
			});
			
			$("input[name='pend']").unbind("blur");
			$("input[name='pend']").blur(function(){
				var nextval = parseInt($(this).val(), 10) + 1;
				if(nextval <= maxprice){
					var index = parseInt($("input[name='pend']").index(this), 10);
					var index2 = parseInt($("input[name='pend']").length, 10) -1;
					if(index != index2){
						var nextindex = index + 1;
						$($("input[name='pstart']").get(nextindex)).val(nextval);
					}
				}
				
			});
			
			$("input[name='pend']").unbind("keypress");
			$("input[name='pend']").keypress(function(event){
				var key = event.which;
				var result = onlyNumber(key, $(this), maxprice);
				if(result == false){
					event.preventDefault();
				}
				
			});
			
			$("input[name='pweight']").unbind("keypress");
			$("input[name='pweight']").keypress(function(event){
				var key = event.which;
				var result = onlyNumberAndMark(key);
				if(result == false){
					event.preventDefault();
				}
				
			});
		}
	});
	
	$("span[name='delrowbtn']").click(function(){
		$(this).parent().parent().remove();
	});
	
	$("input[name='pend']").blur(function(){
		var nextval = parseInt($(this).val(), 10) + 1;
		var index = parseInt($("input[name='pend']").index(this), 10);
		var index2 = parseInt($("input[name='pend']").length, 10) -1;
		if(index != index2){
			var nextindex = index + 1;
			$($("input[name='pstart']").get(nextindex)).val(nextval);
		}
		
	});
	
	$("input[name='pend']").keypress(function(event){
		var key = event.which;
		var result = onlyNumber(key, $(this), maxprice);
		if(result == false){
			event.preventDefault();
		}
		
	});
	
	$("input[name='pweight']").keypress(function(event){
		var key = event.which;
		var result = onlyNumberAndMark(key);
		if(result == false){
			event.preventDefault();
		}
		
	});
}

function makeCUI(){
	var html = [], h=-1;
	html[++h] = "<table id=\"ctable\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"board_data\">\n";
	html[++h] = "<tr>\n";
	html[++h] = "<th width=\"10%\" align=\"center\">유료</td>\n";
	html[++h] = "<td><input type=\"text\" id=\"cweight\" style=\"text-align:right\"/></td>\n";
	html[++h] = "</tr>\n";
	html[++h] = "<tr>\n";
	html[++h] = "<th width=\"10%\" align=\"center\">무료</td>\n";
	html[++h] = "<td><input type=\"text\" id=\"fweight\" style=\"text-align:right\"/></td>\n";
	html[++h] = "</tr>\n";
	html[++h] = "</table>\n";
	
	$("#uiarea").html(html.join(''));
	
	$("#cweight").unbind("keypress");
	$("#cweight").keypress(function(event){
		var key = event.which;
		var result = onlyNumberAndMark(key);
		if(result == false){
			event.preventDefault();
		}
		
	});
	
	$("#fweight").unbind("keypress");
	$("#fweight").keypress(function(event){
		var key = event.which;
		var result = onlyNumberAndMark(key);
		if(result == false){
			event.preventDefault();
		}
		
	});
}

function makeGUI(){
	var html = [], h=-1;
	var rowidcnt = idcnt++;
	html[++h] = "<span class=\"button small blue\" id=\"addgweight\">추가</span><br/><br/>\n";
	html[++h] = "<table id=\"gtable\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"board_data\">\n";
	// html[++h] = "<tr>\n";
	// html[++h] = "<td colspan=\"4\"><span class=\"button small blue\" id=\"addgweight\">추가</span></td>\n";
	// html[++h] = "</tr>\n";
	html[++h] = "<tr>\n";
	html[++h] = "<th width=\"20%\" align=\"center\">장르</td>\n";
	html[++h] = "<th colspan=\"3\" align=\"center\">가중치</td>\n";
	html[++h] = "</tr>\n";
	html[++h] = "<tr>\n";
	html[++h] = "<td>\n";
	html[++h] =	"<input type=\"text\" name=\"tgenre\" id=\"tgenre" + rowidcnt + "\" readonly />\n";
	html[++h] =	"<input type=\"hidden\" name=\"hgenre\" id=\"hgenre" + rowidcnt + "\">\n";
	html[++h] =	"</td>\n";
	html[++h] = "<td><input type=\"text\" name=\"gweight\" style=\"text-align:right\"/></td>\n";
	html[++h] = "<td width=\"10%\"><span class=\"button small blue\" name=\"delrowbtn\">삭제</span></td>\n";
	html[++h] = "<td width=\"15%\"><span class=\"button small blue\" name=\"selgenrebtn\" onclick=\"selgenre('tgenre" + rowidcnt + "', 'hgenre" + rowidcnt + "')\">장르선택</span></td>\n";
	html[++h] = "</tr>\n";
	html[++h] = "</table>\n";
	
	$("#uiarea").html(html.join(''));
	
	$("#addgweight").unbind("click");
	$("span[name='delrowbtn']").unbind("click");
	$("input[name='gweight']").unbind("keypress");
	
	$("#addgweight").click(function(){
		var rowidcnt = idcnt++;
		var html = [], h=-1;
		html[++h] = "<tr>\n";
		html[++h] = "<td>\n";
		html[++h] =	"<input type=\"text\" name=\"tgenre\" id=\"tgenre" + rowidcnt + "\" readonly/>\n";
		html[++h] =	"<input type=\"hidden\" name=\"hgenre\" id=\"hgenre" + rowidcnt + "\">\n";
		html[++h] =	"</td>\n";
		html[++h] = "<td><input type=\"text\" name=\"gweight\" style=\"text-align:right\"/></td>\n";
		html[++h] = "<td width=\"10%\"><span class=\"button small blue\" name=\"delrowbtn\">삭제</span></td>\n";
		html[++h] = "<td width=\"15%\"><span class=\"button small blue\" name=\"selgenrebtn\" onclick=\"selgenre('tgenre" + rowidcnt + "', 'hgenre" + rowidcnt + "')\">장르선택</span></td>\n";
		html[++h] = "</tr>\n";
				
		$("#gtable").append(html.join(''));
		
		$("span[name='delrowbtn']").unbind("click");
		
		$("span[name='delrowbtn']").click(function(){
			$(this).parent().parent().remove();
		});
		
		$("input[name='gweight']").unbind("keypress");
		$("input[name='gweight']").keypress(function(event){
			var key = event.which;
			var result = onlyNumberAndMark(key);
			if(result == false){
				event.preventDefault();
			}
			
		});
	});
	
	$("span[name='delrowbtn']").click(function(){
		$(this).parent().parent().remove();
	});
	
	$("input[name='gweight']").keypress(function(event){
		var key = event.which;
		var result = onlyNumberAndMark(key);
		if(result == false){
			event.preventDefault();
		}
		
	});
}

function makeSUI(){
	$("#uiarea").html('');
}

function validateDUI(){
	var result = true;
	
	if($("#rule_name").val() == ""){
		alert("Rule 이름을 입력해주세요");
		$("#rule_name").focus();
		return false;
	}
	
	$("input[name='dweight']").each(function(i){
		if($(this).val() == ""){
			alert("가중치를 입력해주세요");
			$(this).focus();
			result = false;
			return false;
		}
	});
	
	return result;
}

function regDUI(){
	
	var dweights = $("input[name='dweight']");
	var dweightslength = dweights.length;
	var dweightsarray = new Array();
	
	for(var i=0; i < dweightslength; i++){
		// alert($('#codeitemlist option').eq(i).val());
		var dweightsval = $(dweights[i]).val();
		dweightsarray.push(dweightsval);
	}
	var rule_name = $("#rule_name").val();
	var rule_type = $("#rule_type").val();
	var smartUXManager = $("#smartUXManager").val();
	
	$.post("<%=webRoot%>/admin/rule/insertRule.do", 
		 {rule_name : rule_name, rule_type : rule_type, dweights : dweightsarray, smartUXManager : smartUXManager},
		  function(data) {
			 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
			 	var flag = data.flag;
			 	var message = data.message;
			 	
			 	if(flag == "0000"){						// 정상적으로 처리된 경우
			 		alert(" 일자별 랭킹 Rule이 등록되었습니다");
			 		opener.location.reload();
			 		self.close();
			 	}else if(flag == "NOT FOUND RULE_NM"){
			 		alert("Rule 이름을 입력해주세요");
			 		$("#rule_name").focus();
			 	}else if(flag == "RULE_NM LENGTH"){
			 		alert("RULE 이름은 100자 이내이어야 합니다");
			 		$("#rule_name").focus();
			 	}else if(flag == "NOT FOUND DWEIGHTS"){
			 		alert("가중치를 입력해주세요");
			 	}else{
			 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
			 	}
		  },
		  "json"
   );
}

function validatePUI(){
	var result = true;
	
	if($("#rule_name").val() == ""){
		alert("Rule 이름을 입력해주세요");
		$("#rule_name").focus();
		return false;
	}
	
	$("input[name='pstart']").each(function(i){
		if($(this).val() == ""){
			alert("시작가격을 입력해주세요");
			$(this).focus();
			result = false;
			return false;
		}
	});
	
	if(result == true){
		$("input[name='pend']").each(function(i){
			if($(this).val() == ""){
				alert("끝가격을 입력해주세요");
				$(this).focus();
				result = false;
				return false;
			}
		});
	}
	
	if(result == true){
		$("input[name='pweight']").each(function(i){
			if($(this).val() == ""){
				alert("가중치를 입력해주세요");
				$(this).focus();
				result = false;
				return false;
			}
		});
	}
	
	return result;
}

function regPUI(){
	var pstarts = $("input[name='pstart']");
	var pstartslength = pstarts.length;
	var pstartsarray = new Array();
	
	var pends = $("input[name='pend']");
	var pendslength = pends.length;
	var pendsarray = new Array();
	
	var pweights = $("input[name='pweight']");
	var pweightslength = pweights.length;
	var pweightsarray = new Array();
	
	for(var i=0; i < pstartslength; i++){
		// alert($('#codeitemlist option').eq(i).val());
		var pstartval = $(pstarts[i]).val();
		pstartsarray.push(pstartval);
	}
	
	for(var i=0; i < pendslength; i++){
		// alert($('#codeitemlist option').eq(i).val());
		var pendval = $(pends[i]).val();
		pendsarray.push(pendval);
	}
	
	for(var i=0; i < pweightslength; i++){
		// alert($('#codeitemlist option').eq(i).val());
		var pweightsval = $(pweights[i]).val();
		pweightsarray.push(pweightsval);
	}
	var rule_name = $("#rule_name").val();
	var rule_type = $("#rule_type").val();
	var smartUXManager = $("#smartUXManager").val();
	
	$.post("<%=webRoot%>/admin/rule/insertRule.do", 
		 {rule_name : rule_name, rule_type : rule_type, pstart : pstartsarray, pend : pendsarray, pweights : pweightsarray, smartUXManager : smartUXManager},
		  function(data) {
			 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
			 	var flag = data.flag;
			 	var message = data.message;
			 	
			 	if(flag == "0000"){						// 정상적으로 처리된 경우
			 		alert(" 가격별 랭킹 Rule이 등록되었습니다");
			 		opener.location.reload();
			 		self.close();
			 	}else if(flag == "NOT FOUND RULE_NM"){
			 		alert("Rule 이름을 입력해주세요");
			 		$("#rule_name").focus();
			 	}else if(flag == "RULE_NM LENGTH"){
			 		alert("RULE 이름은 100자 이내이어야 합니다");
			 		$("#rule_name").focus();
			 	}else if(flag == "NOT FOUND PSTART"){
			 		alert("시작가격을 입력해주세요");
			 	}else if(flag == "NOT FOUND PEND"){
			 		alert("끝가격을 입력해주세요");
			 	}else if(flag == "NOT FOUND PWEIGHTS"){
			 		alert("가중치를 입력해주세요");
			 	}else{
			 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
			 	}
		  },
		  "json"
   );
}

function validateCUI(){
	var result = true;
	
	if($("#rule_name").val() == ""){
		alert("Rule 이름을 입력해주세요");
		$("#rule_name").focus();
		return false;
	}
	
	if($("#cweight").val() == ""){
		alert("유료 가중치를 입력해주세요");
		$("#cweight").focus();
		result = false;	
	}else if($("#fweight").val() == ""){
		alert("무료 가중치를 입력해주세요");
		$("#fweight").focus();
		result = false;
	}
	return result;
}

function regCUI(){
	var cweightval = $("#cweight").val();
	var fweightval = $("#fweight").val();

	var rule_name = $("#rule_name").val();
	var rule_type = $("#rule_type").val();
	var smartUXManager = $("#smartUXManager").val();
	
	$.post("<%=webRoot%>/admin/rule/insertRule.do", 
		 {rule_name : rule_name, rule_type : rule_type, cweight : cweightval, fweight : fweightval, smartUXManager : smartUXManager},
		  function(data) {
			 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
			 	var flag = data.flag;
			 	var message = data.message;
			 	
			 	if(flag == "0000"){						// 정상적으로 처리된 경우
			 		alert(" 유/무료별 랭킹 Rule이 등록되었습니다");
			 		opener.location.reload();
			 		self.close();
			 	}else if(flag == "NOT FOUND RULE_NM"){
			 		alert("Rule 이름을 입력해주세요");
			 		$("#rule_name").focus();
			 	}else if(flag == "RULE_NM LENGTH"){
			 		alert("RULE 이름은 100자 이내이어야 합니다");
			 		$("#rule_name").focus();
			 	}else if(flag == "NOT FOUND CWEIGHT"){
			 		alert("유료 가중치를 입력해주세요");
					$("#cweight").focus();
			 	}else if(flag == "NOT FOUND FWEIGHT"){
			 		alert("무료 가중치를 입력해주세요");
					$("#fweight").focus();
			 	}else{
			 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
			 	}
		  },
		  "json"
   );
}

function validateGUI(){
	var result = true;
	
	if($("#rule_name").val() == ""){
		alert("Rule 이름을 입력해주세요");
		$("#rule_name").focus();
		return false;
	}
	
	$("input[name='tgenre']").each(function(i){
		if($(this).val() == ""){
			alert("장르를 지정해주세요");
			$(this).focus();
			result = false;
			return false;
		}
	});
	
	if(result == true){
		$("input[name='gweight']").each(function(i){
			if($(this).val() == ""){
				alert("가중치를 입력해주세요");
				$(this).focus();
				result = false;
				return false;
			}
		});
	}
	
	return result;
}

function regGUI(){
	
	var hgenres = $("input[name='hgenre']");
	var hgenreslength = hgenres.length;
	var hgenresarray = new Array();
	
	var gweights = $("input[name='gweight']");
	var gweightslength = gweights.length;
	var gweightsarray = new Array();
	
	for(var i=0; i < hgenreslength; i++){
		// alert($('#codeitemlist option').eq(i).val());
		var hgenreval = $(hgenres[i]).val();
		hgenresarray.push(hgenreval);
	}
	
	for(var i=0; i < gweightslength; i++){
		// alert($('#codeitemlist option').eq(i).val());
		var gweightsval = $(gweights[i]).val();
		gweightsarray.push(gweightsval);
	}
	var rule_name = $("#rule_name").val();
	var rule_type = $("#rule_type").val();
	var smartUXManager = $("#smartUXManager").val();
	
	$.post("<%=webRoot%>/admin/rule/insertRule.do", 
		 {rule_name : rule_name, rule_type : rule_type, hgenre : hgenresarray, gweights : gweightsarray, smartUXManager : smartUXManager},
		  function(data) {
			 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
			 	var flag = data.flag;
			 	var message = data.message;
			 	
			 	if(flag == "0000"){						// 정상적으로 처리된 경우
			 		alert(" 장르별 랭킹 Rule이 등록되었습니다");
			 		opener.location.reload();
			 		self.close();
			 	}else if(flag == "NOT FOUND RULE_NM"){
			 		alert("Rule 이름을 입력해주세요");
			 		$("#rule_name").focus();
			 	}else if(flag == "RULE_NM LENGTH"){
			 		alert("RULE 이름은 100자 이내이어야 합니다");
			 		$("#rule_name").focus();
			 	}else if(flag == "NOT FOUND HGENRE"){
			 		alert("장르코드를 입력해주세요");
			 	}else if(flag == "NOT FOUND GWEIGHTS"){
			 		alert("가중치를 입력해주세요");
			 	}else{
			 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
			 	}
		  },
		  "json"
   );
}

function validateSUI(){
	var result = true;
	
	if($("#rule_name").val() == ""){
		alert("Rule 이름을 입력해주세요");
		$("#rule_name").focus();
		return false;
	}	
	return result;
}

function regSUI(){
	var rule_name = $("#rule_name").val();
	var rule_type = $("#rule_type").val();
	var smartUXManager = $("#smartUXManager").val();
	
	$.post("<%=webRoot%>/admin/rule/insertRule.do", 
		 {rule_name : rule_name, rule_type : rule_type, smartUXManager : smartUXManager},
		  function(data) {
			 	// 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
			 	var flag = data.flag;
			 	var message = data.message;
			 	
			 	if(flag == "0000"){						// 정상적으로 처리된 경우
			 		alert(" 시리즈별 랭킹 Rule이 등록되었습니다");
			 		opener.location.reload();
			 		self.close();
			 	}else if(flag == "NOT FOUND RULE_NM"){
			 		alert("Rule 이름을 입력해주세요");
			 		$("#rule_name").focus();
			 	}else if(flag == "RULE_NM LENGTH"){
			 		alert("RULE 이름은 100자 이내이어야 합니다");
			 		$("#rule_name").focus();
			 	}else{
			 		alert("작업 중 오류가 발생하였습니다\nflag : " + flag + "\nmessage : " + message);
			 	}
		  },
		  "json"
   );
}

function onlyNumber(key, loc, checkval){
	
	var result = true;
	// 8 : back space
	// 46 : .
	// 144 : num lock
	
	if((key == 8) || (key == 144)){
		
	}else{
		if(key>=48&&key<=57){
			var locval = parseInt(loc.val(), 10);
			var intcheckval = parseInt(checkval, 10);
			if(locval >= intcheckval){
				alert(intcheckval + " 숫자까지 입력 가능합니다");
				result = false;
			}
		}else{
			alert('숫자만 입력 가능합니다');
			result = false;
		}
	}
	
	return result;
}

function onlyNumberAndMark(key){
	var result = true;
	
	if((key == 8) || (key == 46) || (key>=48&&key<=57)){
		
		
	}else{
		alert('숫자와 .만 입력 가능합니다');
		result = false;
	}
	
	return result;
}

function selgenre(tgenreid, hgenreid){
	window.open("<%=webRoot%>/admin/rank/selectGenrePopup.do?multiCheck=N&genreElementid=" + tgenreid + "&genreHiddenid=" + hgenreid, "reggenre", "width=650,height=500,resizable=yes,scrollbars=yes");
}

</script>
<body leftmargin="0" topmargin="0">
<div id="divBody" style="height:100%">
<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
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
						                                            <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
						                                            <td class="bold">Rule 등록</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                        <form id="regfrm" name="regfrm" method="post" action="">
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">Rule 이름</th>
					                                    <td width="1%"></td>
					                                    <td width="74%" align="left" >
															<input type="text" id="rule_name" name="rule_name" size="35" maxlength="100" style="font-size: 12px;" />								
														</td>
					                                </tr>
					                                
					                                <tr align="center">
					                                    <th width="25%">Rule Type 선택</th>
					                                    <td width="1%"></td>
					                                    <td width="74%" align="left" >
															<select id="rule_type" name="rule_type">
															<c:forEach var="item" items="${rule_type_list}" varStatus="status">
																<option value="${item.rule_type}">${item.rule_type_name}</option>
															</c:forEach>
															</select>
																				
														</td>
					                                </tr>
					                                <!-- 
					                                <tr align="center">
					                                   <td colspan="3" >작업영역</td> 
					                                </tr>
					                                 -->
					                            	</tbody>
					                            </table>
					                            <div id="uiarea"></div>
					                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="center">
						                                	<span class="button small blue" id="regbtn">등록</span>	
						                                	<span class="button small blue" id="resetbtn">재작성</span>	
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                       	<input type="hidden" id="smartUXManager" name="smartUXManager" value="<%=id_decrypt %>" />
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