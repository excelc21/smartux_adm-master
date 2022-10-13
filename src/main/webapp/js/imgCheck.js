/**
 * 이미지 포멧, 사이즈, width, height제한 스크립트
 */
//---------------이미지 제한-----------------//
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

/*
 * 이미지 헤더를 통해 이미지 포맷 / 가로,세로 사이즈 확인 후 json 응답 (비동기식 ajax로 구현)
 * fileId : 체크 하고자 하는 input(type=file) 의 ID/NAME 값
 * propertyNm : 프로퍼티에 지정해놓은 사이즈와 타입, 포맷을 찾기 위한 프로퍼티 네임
 */
function imgHeaderCheck(fileId, propertyNm, imgSize) { 
	 
	 //onchange 이벤트로 인해 중복체크오류존재 제외 처리
	 if ($('#'+fileId).val()=="") {
		 return;
	 }
    /*이미지 용량 확인은 ImageIo로 체크불가 (스크립트에서 처리) */    
    var size = document.getElementById(fileId).files[0].size;
	//var maxSize = 102400;
	var maxSize = imgSize;
	var fileSize = Math.round(size);
	
	if(fileSize > maxSize*1024) {
		alert("이미지 용량을 초과 했습니다.\n최대 이미지 사이즈 : "+maxSize+"KB");
		browserCheckReset(fileId);
        return;
	}
	
	var formData = new FormData();          
	formData.append("imgPath", $("#"+fileId+"")[0].files[0]); // 체크해야할 첨부파일을 보냄   
	formData.append("propertyNm", propertyNm);   // 프로퍼티에 지정해놓은 사이즈와 타입, 포맷을 찾기 위해 프로퍼티 네임을 입력한다. ex) theme.tv.main.width 의 경우 theme.tv.main 만 가져간다  
	
	$.ajax({            
		url: '/smartux_adm/admin/commonMng/imgHeaderCheck.do',            
		data: formData,            
		processData: false,            
		contentType: false,            
		type: 'POST',   
		dataType: 'json',
		success: function(data){       
			if(data.result.flag=="0000"){ //사이즈, 포맷이 모두 맞으면 PASS
			} else if(data.result.flag=="1001"){ //포맷이 맞지 않으면
				alert(data.result.message + "는 이미지 포맷에 맞지 않습니다. \n 적합한 포맷 형식 : " + data.result.format);
				browserCheckReset(fileId);
			} else if(data.result.flag=="2002"){ //사이즈가 맞지 않으면
				var sizeType;
				if (data.result.type == "equal") { 
					sizeType = "같아야";
				} else if(data.result.type == "max") {
					sizeType = "같거나 작아야";
				}
				var msg1 = " 사이즈가 맞지 않습니다";
				var msg2 = "최적화 사이즈 : " + data.result.width + "px * " + data.result.height + "px";
				var msg3 = "본 이미지는 최적화 사이즈와 "+ sizeType + " 합니다.";
				
				if (data.result.message == "width") { //가로 사이즈가 맞지 않을 경우,
					alert("가로"+msg1+"\n"+msg2+"\n"+msg3);
				} else if (data.result.message == "height") { //세로 사이즈가 맞지 않을 경우,
					alert("세로"+msg1+"\n"+msg2+"\n"+msg3);
				} else if (data.result.message == "all") { //가로*세로 모두 사이즈가 맞지 않을 경우,
					alert("가로 * 세로"+msg1+"\n"+msg2+"\n"+msg3);
				}
				browserCheckReset(fileId);
			}
		}      
	});
}
//--------------------------------------------------------------//