function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}


var target;																	// ȣ���� Object�� ����
var pTarget;
var stime;
var pYear;
var pMonth;
var pDay;
document.write("<div id=minical oncontextmenu='return false' ondragstart='return false' onselectstart='return false' style=\"background:#E7F3FF; margin:5; padding:5;margin-top:2;border-top:1 solid buttonshadow;border-left: 1 solid buttonshadow;border-right: 1 solid buttonshadow;border-bottom:1 solid buttonshadow;width:160;display:none;position: absolute; z-index: 99\"></div>");

function Calendar(obj) {														// jucke
	var now = obj.value.split("-");
	var x, y;
	
	target = obj;																// Object ����;

	x = (document.layers) ? loc.pageX : window.event.x + document.body.scrollLeft;
	y = (document.layers) ? loc.pageY : window.event.y + document.body.scrollTop;

	minical.style.pixelTop	= y+5;
	minical.style.pixelLeft	= x-50;
	//minical.style.display = (minical.style.display == "block") ? "none" : "block";
	// �ΰ��� textbox�� ���� ������
	if(target == pTarget) {  // ���� textbox�̸� none�̸� block, block�̸� none
		minical.style.display = (minical.style.display == "block") ? "none" : "block";
	} else {	// �ٸ� textbox�̸� �Ѱ��� none, �Ѱ��� block
		minical.style.display = "none";
		minical.style.display = "block";
	}
	// ���� textbox�� ���
	pTarget = obj;
	
	if (now.length == 3) {			// ��Ȯ���� �˻�
		pYear = eval(now[0]);
		pMonth = eval(now[1]);
		pDay = eval(now[2]);
		Show_cal(now[0],now[1],now[2]);	// �Ѿ�� ���� ����Ϸ� �и�
	} else {
		now = new Date();
		pYear = eval(now.getFullYear());
		pMonth = eval(now.getMonth()+1);
		pDay = eval(now.getDate());
		Show_cal(now.getFullYear(), now.getMonth()+1, now.getDate());			// ���� ��/��/���� �����Ͽ� �ѱ�.
	}
}
	
function doOver() {																// ���콺�� Į�������� ������
	var el = window.event.srcElement;
	cal_Day = el.title;

	if (cal_Day.length > 7) {													// ���� ���� ������.
		el.style.borderTopColor = el.style.borderLeftColor = "buttonhighlight";
		el.style.borderRightColor = el.style.borderBottomColor = "buttonshadow";
	}
	window.clearTimeout(stime);													// Clear
}

function doClick() {															// ���ڸ� �����Ͽ��� ���
	cal_Day = window.event.srcElement.title;
	window.event.srcElement.style.borderColor = "red";							// �׵θ� ���� ����������
	if (cal_Day.length > 7) {													// ���� ����������
		target.value=cal_Day													// �� ����
	}
	minical.style.display='none';												// ȭ�鿡�� ����
}

function doOut() {
	var el = window.event.fromElement;
	cal_Day = el.title;

	if (cal_Day.length > 7) {
		el.style.borderColor = "white";
	}
	//stime=window.setTimeout("minical.style.display='none';", 200);
}

// �޷� �ݱ�
function doClose() {
	minical.style.display='none';
}

function day2(d) {																// 2�ڸ� ���ڷ� ����
	var str = new String();
	
	if (parseInt(d) < 10) {
		str = "0" + parseInt(d);
	} else {
		str = "" + parseInt(d);
	}
	return str;
}

function Show_cal(sYear, sMonth, sDay) {
	var Months_day = new Array(0,31,28,31,30,31,30,31,31,30,31,30,31)
	var Weekday_name = new Array("��", "��", "ȭ", "��", "��", "��", "��");
	var intThisYear = new Number(), intThisMonth = new Number(), intThisDay = new Number();
	document.all.minical.innerHTML = "";
	datToday = new Date();													// ���� ���� ����
	
	intThisYear = eval(sYear);
	intThisMonth = eval(sMonth);
	intThisDay = eval(sDay);
	
	if (intThisYear == 0) intThisYear = datToday.getFullYear();				// ���� ���� ���
	if (intThisMonth == 0) intThisMonth = parseInt(datToday.getMonth())+1;	// �� ���� ������ ���� -1 �� ���� �ŵ��� ���.
	if (intThisDay == 0) intThisDay = datToday.getDate();
	
	switch(intThisMonth) {
		case 1:
				intPrevYear = intThisYear -1;
				intPrevMonth = 12;
				intNextYear = intThisYear;
				intNextMonth = 2;
				break;
		case 12:
				intPrevYear = intThisYear;
				intPrevMonth = 11;
				intNextYear = intThisYear + 1;
				intNextMonth = 1;
				break;
		default:
				intPrevYear = intThisYear;
				intPrevMonth = parseInt(intThisMonth) - 1;
				intNextYear = intThisYear;
				intNextMonth = parseInt(intThisMonth) + 1;
				break;
	}

	NowThisYear = datToday.getFullYear();	// ���� ��
	NowThisMonth = datToday.getMonth()+1;	// ���� ��
	NowThisDay = datToday.getDate();	// ���� ��
	
	datFirstDay = new Date(intThisYear, intThisMonth-1, 1);		// ���� ���� 1�Ϸ� ���� ��ü ��(���� 0���� 11������ ����(1����� 12��))
	intFirstWeekday = datFirstDay.getDay();						// ���� �� 1���� ������ ���� (0:�Ͽ���, 1:�����)
	
	intSecondWeekday = intFirstWeekday;
	intThirdWeekday = intFirstWeekday;
	
	datThisDay = new Date(intThisYear, intThisMonth, intThisDay);	// �Ѿ�� ���� ���� ��
	intThisWeekday = datThisDay.getDay();							// �Ѿ�� ������ �� ����
	varThisWeekday = Weekday_name[intThisWeekday];					// ���� ���� ����
	
	intPrintDay = 1													// ���� ���� ����
	secondPrintDay = 1
	thirdPrintDay = 1
	
	Stop_Flag = 0
	
	if ((intThisYear % 4)==0) {										// 4�⸶�� 1���̸� (��γ����� ��������)
		if ((intThisYear % 100) == 0) {
			if ((intThisYear % 400) == 0) {
				Months_day[2] = 29;
			}
		} else {
			Months_day[2] = 29;
		}
	}
	intLastDay = Months_day[intThisMonth];							// ������ ���� ����
	Stop_flag = 0
	
	Cal_HTML = "<TABLE WIDTH=164 BORDER=0 CELLPADDING=0 CELLSPACING=2 ONMOUSEOVER=doOver(); ONMOUSEOUT=doOut(); STYLE='font-size:8pt;font-family:Tahoma;'>"
			+ "<TR ALIGN=CENTER><TD COLSPAN=7 nowrap=nowrap ALIGN=CENTER><SPAN TITLE='�����' STYLE=cursor:hand; onClick='Show_cal("+intPrevYear+","+intPrevMonth+",1);'><FONT COLOR=#2992FF>��</FONT></SPAN> "
			+ "<B STYLE=color:red>"+get_Yearinfo(intThisYear,intThisMonth,intThisDay)+"��"+get_Monthinfo(intThisYear,intThisMonth,intThisDay)+"��</B>"
			+ " <SPAN TITLE='������' STYLE=cursor:hand; onClick='Show_cal("+intNextYear+","+intNextMonth+",1);'><FONT COLOR=#2992FF>��</FONT></SPAN></TD></TR>"
			+ "<TR ALIGN=CENTER BGCOLOR=ThreedFace STYLE='color:White;font-weight:bold;'><TD>��</TD><TD>��</TD><TD>ȭ</TD><TD>��</TD><TD>��</TD><TD>��</TD><TD>��</TD></TR>";
			
	for (intLoopWeek=1; intLoopWeek < 7; intLoopWeek++) {		// �ִ��� ���� ����, �ִ� 6��
		Cal_HTML += "<TR ALIGN=RIGHT BGCOLOR=WHITE>"
		for (intLoopDay=1; intLoopDay <= 7; intLoopDay++) {		// ���ϴ��� ���� ����, �Ͽ��� ����
			if (intThirdWeekday > 0) {							// ù�� �������� 1���� ũ��
				Cal_HTML += "<TD onClick=doClick();>";
				intThirdWeekday--;
			} else {
				if (thirdPrintDay > intLastDay) {				// �Է� ��¦ ���� ũ�ٸ�
					Cal_HTML += "<TD onClick=doClick();>";
				} else {										// �Է³�¥�� ����� �ش� �Ǹ�
					Cal_HTML += "<TD onClick=doClick(); title="+intThisYear+"-"+day2(intThisMonth).toString()+"-"+day2(thirdPrintDay).toString()+" STYLE=\"cursor:Hand;border:1px solid white;";
					//if (intThisYear == NowThisYear && intThisMonth==NowThisMonth && thirdPrintDay==intThisDay) {
					if (intThisYear == pYear && intThisMonth == pMonth && thirdPrintDay == pDay) {
						Cal_HTML += "background-color:#BDFF9C;";
					}
					
					switch(intLoopDay) {
						case 1:									// �Ͽ����̸� ���� ������
							Cal_HTML += "color:red;"
							break;
						case 7:
							Cal_HTML += "color:blue;"
							break;
						default:
							Cal_HTML += "color:black;"
							break;
					}
					
					Cal_HTML += "\">"+thirdPrintDay;
					
				}
				thirdPrintDay++;
				
				if (thirdPrintDay > intLastDay) {				// ���� ��¥ ���� �� ������ ũ�� ������ Ż��
					Stop_Flag = 1;
				}
			}
			Cal_HTML += "</TD>";
		}
		Cal_HTML += "</TR>";
		if (Stop_Flag==1) break;
	}
	Cal_HTML += "<TR><td colspan=\"7\" align=\"right\"><font size=\"1\">[<a href=\"javascript:doClose();\"><font color=\"#000000\">close</font></a>]</a></td></tr>";
	Cal_HTML += "</TABLE>";

	document.all.minical.innerHTML = Cal_HTML;
}

function get_Yearinfo(year,month,day) {											// �� ������ �޺� �ڽ��� ǥ��
	var min = parseInt(year) - 100;
	var max = parseInt(year) + 10;
	var i = new Number();
	var str = new String();
	
	str = "<SELECT onChange='Show_cal(this.value,"+month+","+day+");' ONMOUSEOVER=doOver();>";
	for (i=min; i<=max; i++) {
		if (i == parseInt(year)) {
			str += "<OPTION VALUE="+i+" selected ONMOUSEOVER=doOver();>"+i+"</OPTION>";
		} else {
			str += "<OPTION VALUE="+i+" ONMOUSEOVER=doOver();>"+i+"</OPTION>";
		}
	}
	str += "</SELECT>";
	return str;
}


function get_Monthinfo(year,month,day) {										// �� ������ �޺� �ڽ��� ǥ��
	var i = new Number();
	var str = new String();
	
	str = "<SELECT onChange='Show_cal("+year+",this.value,"+day+");' ONMOUSEOVER=doOver();>";
	for (i=1; i<=12; i++) {
		if (i == parseInt(month)) {
			str += "<OPTION VALUE="+i+" selected ONMOUSEOVER=doOver();>"+i+"</OPTION>";
		} else {
			str += "<OPTION VALUE="+i+" ONMOUSEOVER=doOver();>"+i+"</OPTION>";
		}
	}
	str += "</SELECT>";
	return str;
}


function viewLoadingImg() {
    document.getElementById('viewLoading').style.display = '';
    document.getElementById('loadingBar').style.display = '';
}

function viewLoadingImg(moveUrl) {
    location.href = moveUrl;
    //document.getElementById('viewLoading').style.visibility = 'visible';
    document.getElementById('viewLoading').style.display = '';
    document.getElementById('loadingBar').style.display = '';
}

function addComma (target, str){
	var input_str = str.toString();

	if (input_str == '') return false;
	input_str = parseInt(input_str.replace(/[^0-9]/g, '')).toString();
	if (isNaN(input_str)) { return false; }

	var sliceChar = ',';
	var step = 3;
	var step_increment = -1;
	var tmp  = '';
	var retval = '';
	var str_len = input_str.length;

	for (var i=str_len; i>=0; i--)
	{
		tmp = input_str.charAt(i);
		if (tmp == sliceChar) continue;
		if (step_increment%step == 0 && step_increment != 0) retval = tmp + sliceChar + retval;
		else retval = tmp + retval;
		step_increment++;
	}
	
	target.value = retval;
}

// 필드에 입력된 문자의 바이트 수를 체크
function checkByte(frm,limitByte) {
	var totalByte = 0;
	var message = frm.val();
	var charCount=0;

	for ( var i = 0; i < message.length; i++) {
		var currentByte = message.charCodeAt(i);
		if (currentByte > 128)
			totalByte += 2;
		else
			totalByte++;
		if(totalByte>limitByte){
			charCount=i+1;
			break;
		}
	}

	if (totalByte > limitByte) {
		alert(limitByte + "바이트까지 전송가능합니다.");
		var cutMessage=message.substring(0,charCount-1);
		frm.attr("value",cutMessage);
	}
}

//필드에 입력된 문자의 바이트 수를 체크후 true/false 리턴
function checkByteReturn(frm,limitByte) {
	var totalByte = 0;
	var message = frm.val();
	var charCount=0;
	var rtn = "true";

	for ( var i = 0; i < message.length; i++) {
		var currentByte = message.charCodeAt(i);
		if (currentByte > 128)
			totalByte += 2;
		else
			totalByte++;

		if(totalByte>limitByte){
			charCount=i+1;
			break;
		}
	}

	if (totalByte > limitByte) {
		rtn = "false";
	}

	return rtn;
}

//필드에 입력된 문자의 바이트 수를 체크후 true/false 리턴
//입력 => 메시지, 최대사이즈, 한글 1byte 사이즈
function checkByteMessage(message,limitByte,oneByteSize) {
	var totalByte = 0;
	var charCount=0;
	var rtn = "true";

	for ( var i = 0; i < message.length; i++) {
		var currentByte = message.charCodeAt(i);
		if (currentByte > 128)
			totalByte += oneByteSize;
		else
			totalByte++;

		if(totalByte>limitByte){
			charCount=i+1;
			break;
		}
	}

	if (totalByte > limitByte) {
		rtn = "false";
	}

	return rtn;
}

//textarear 특수문제 체크
function textareaCheck(text) {
	if( text.indexOf("!^") > -1 ) {
		return "!^";
	}
	if( text.indexOf("\\b") > -1 ) {
		return "\\b";
	}
	if( text.indexOf("\\f88") > -1 ) {
		return "\\f88";
	}
	if( text.indexOf("\\f99") > -1 ) {
		return "\\f99";
	}
	if( text.indexOf("\\f") > -1 ) {
		return "\\f";
	}
	return false;
}

// 문자열을 입력받아 Trim 기능을 수행한다
function trim(stringToTrim) {
	return stringToTrim.replace(/^\s+|\s+$/g,"");
}

// 문자열을 입력받아 LTrim 기능을 수행한다
function ltrim(stringToTrim) {
	return stringToTrim.replace(/^\s+/,"");
}

// 문자열을 입력받아 RTrim 기능을 수행한다
function rtrim(stringToTrim) {
	return stringToTrim.replace(/\s+$/,"");
}

// select 태그의 id값과 작업 타입(T, U, D, L)을 입력받아 select 태그의 선택된 Option 태그를 이동시킨다
function fnSortCateogry(elementid, clss) {
	var sortkeys = document.getElementById(elementid);
	var idx = sortkeys.selectedIndex;
	
	if ( idx < 0 ) { return; }
	
	var optlen = sortkeys.options.length;
	var newidx = -1;
	
	switch(clss) {
		case 'T': newidx = 0; break;					// 맨 위로 이동시킬때
		case 'U': newidx = idx-1; break;				// 하나 위로 이동시킬때
		case 'D': newidx = idx+1; break;				// 하나 아래로 이동시킬때
		case 'L': newidx = optlen-1; break;			// 맨 아래로 이동시킬때
	}
	
	if ( newidx > optlen-1 || idx == newidx || newidx == -1 ) {
		return;
	}
	
	var oldtext = sortkeys.options[idx].text;
	var oldvalue = sortkeys.options[idx].value;
	
	if ( clss == 'T' ) {
		while (idx > 0) {
			sortkeys.options[idx].text = sortkeys.options[idx-1].text;
			sortkeys.options[idx].value = sortkeys.options[idx-1].value;
			idx--;
		}
	}else if ( clss == 'L' ) {
		while (idx < optlen-1) {
			sortkeys.options[idx].text = sortkeys.options[idx+1].text;
			sortkeys.options[idx].value = sortkeys.options[idx+1].value;
			idx++;
		}
	} else {
		sortkeys.options[idx].text = sortkeys.options[newidx].text;
		sortkeys.options[idx].value = sortkeys.options[newidx].value;
	}
	
	sortkeys.options[newidx].text = oldtext;
	sortkeys.options[newidx].value = oldvalue;
	sortkeys.selectedIndex = newidx;
}

function imageFileCheck(filename){
	
	   var fileName=filename;	 
	   var fileSuffix =fileName.substring(fileName.lastIndexOf(".") + 1);
	   var browser = navigator.userAgent.toLowerCase();
	   
	   fileSuffix = fileSuffix.toLowerCase();
	    if (!( "jpg" == fileSuffix || "jpeg" == fileSuffix  || "gif" == fileSuffix || "bmp" == fileSuffix || "png" == fileSuffix )){
	    	
	    	//크롬일경우
	    	if(-1 != browser.indexOf("chrome")){
	    		$("#file")[0].select();
	    		$("#file").val("");
	    	}else{ //ie : if(-1 != browser.indexOf("msie")
	    		$("#file").attr("value","");
	    		document.selection.clear();
	    	}
	    			    	
	    	
	    	return true;
	    }
	    return false;
}

function imageCheckInputPatch(filename, inputname){
	
	   var fileName=filename;	 
	   var fileSuffix =fileName.substring(fileName.lastIndexOf(".") + 1);
	   var browser = navigator.userAgent.toLowerCase();
	   
	   fileSuffix = fileSuffix.toLowerCase();
	    if (!( "jpg" == fileSuffix || "jpeg" == fileSuffix  || "gif" == fileSuffix || "bmp" == fileSuffix || "png" == fileSuffix )){
	    	
	    	//크롬일경우
	    	if(-1 != browser.indexOf("chrome")){
	    		$("#"+inputname)[0].select();
	    		$("#"+inputname).val("");
	    	}else{ //ie : if(-1 != browser.indexOf("msie")
	    		$("#"+inputname).attr("value","");	    
	    		document.selection.clear();
	    	}
	    	return true;
	    }
	    return false;
}

function popupCenter(url, title, w, h) {
	var dualScreenLeft = window.screenLeft != 'undefined' ? window.screenLeft : screen.left;
	var dualScreenTop = window.screenTop != 'undefined' ? window.screenTop : screen.top;

	var width = window.innerWidth ? window.innerWidth : document.documentElement.clientWidth ? document.documentElement.clientWidth : screen.width;
	var height = window.innerHeight ? window.innerHeight : document.documentElement.clientHeight ? document.documentElement.clientHeight : screen.height;

	var left = ((width / 2) - (w / 2)) + dualScreenLeft;
	var top = ((height / 2) - (h / 2)) + dualScreenTop;
	var newWindow = window.open(url, title, 'scrollbars=yes, width=' + w + ', height=' + h + ', top=' + top + ', left=' + left);
	newWindow.focus();
}
