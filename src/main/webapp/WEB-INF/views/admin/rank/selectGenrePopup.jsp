<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn"  	uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" 	uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" 	uri="http://www.springframework.org/tags/form"%>

<%
// multiCheck Parameter Accept
String multiCheck = request.getParameter("multiCheck");
if(multiCheck == null || multiCheck == "") multiCheck = "N";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>

<script type="text/javascript">
	
	// 장르 추가 로직 
	function insertform() 
	{ 
		if(document.regfrm.level1.value != null && document.regfrm.level1.value != '') {	
			     		     
			  var multiCheckValue = document.regfrm.multiCheck.value;	
			  var genre_levelInput=document.getElementsByName('genre_level');	
			  var genre_level_onInput=document.getElementsByName('genre_level_on');		
			  var gflag = false;
			  
			  oTable = document.getElementById("tb_list"); 			
			   
			  var addnum=oTable.rows.length; 	// 추가할 배열번호
				  
			  // 행추가 
			  oRow = oTable.insertRow(-1);      // 행 맨 마직막에 attach 
			
			  // 열 추가 
			  oCell = oRow.insertCell(); 
			
			  // innerHTML 복제 
			  oCell.innerHTML = "<input type='text'  name='genre_level_on' value='' size='65' style='font-size: 12px;' readonly /> <input type='hidden'  name='genre_level' value='' size='35'   /> <input type='button' value='삭제' onClick='removeRow(this);' class='button small blue'/>";
			
			  // 열 추가 
			  oCell = oRow.insertCell(); 	
					 
			  // 장르 선택 밸리데이션
			  if(document.regfrm.level2.value == null || document.regfrm.level2.value == ''){	
				  if(document.regfrm.multiCheck.value == 'N') {						// 장르 단건 선택
		   		  	  genre_levelInput[0].value = document.regfrm.level1.value;
		   		  	  genre_level_onInput[0].value = document.regfrm.level1.value;	
					  document.getElementById('tb_list').deleteRow(2);				// 두번째행부터 삭제
				  }else{
				    	for(var i=0 ; i < addnum ; i++) {
						  	if(addnum == i+1){
							    if(i == 0){
							    	genre_levelInput[i].value = document.regfrm.level1.value;
							    	genre_level_onInput[i].value = document.regfrm.level1.value;
							    }else{
						    		for( var h=0 ; h < i ; h++){
						    			if( genre_levelInput[i-h-1].value == document.regfrm.level1.value ) {
						    				alert("동일한 장르는 입력하실 수 없습니다.");
								    		document.getElementById('tb_list').deleteRow(i+1);				// 두번째행부터 삭제
								    		gflag = false;
						    			}else{
						    				genre_levelInput[i].value = document.regfrm.level1.value;
						    				genre_level_onInput[i].value = document.regfrm.level1.value;
						    				gflag = true;
						    			}
						    		}
						    		if(gflag == true) break;								    	
							    }
						  	}
				  		}
				  }	  	//genre_levelInput[0].value = document.regfrm.level1.value;
			  }else{
				  if(document.regfrm.multiCheck.value == 'N') {						// 장르 단건 선택
					   if(document.regfrm.level3.value == null || document.regfrm.level3.value == '') {
								  	genre_levelInput[0].value = document.regfrm.level1.value+"__"+document.regfrm.level2.value;
								  	genre_level_onInput[0].value = document.regfrm.level1.value+" > "+document.regfrm.level2.value;
					   }else{	  
								  	genre_levelInput[0].value = document.regfrm.level1.value+"__"+document.regfrm.level2.value+"__"+document.regfrm.level3.value;
								  	genre_level_onInput[0].value = document.regfrm.level1.value+" > "+document.regfrm.level2.value+" > "+document.regfrm.level3.value;
					   }
					   document.getElementById('tb_list').deleteRow(2);				// 두번째행부터 삭제
				  }else{
					   if(document.regfrm.level3.value == null || document.regfrm.level3.value == '') {
					    	for(var i=0 ; i < addnum ; i++) {
							  if(addnum == i+1){
								  	if(i == 0){
								  		genre_levelInput[i].value = document.regfrm.level1.value+"__"+document.regfrm.level2.value;
								  		genre_level_onInput[i].value = document.regfrm.level1.value+" > "+document.regfrm.level2.value;
								    }else{
								    	for( var h=0 ; h < i ; h++){
							    			if( genre_levelInput[i-h-1].value == (document.regfrm.level1.value+"__"+document.regfrm.level2.value) ) {
							    				alert("동일한 장르는 입력하실 수 없습니다.");
									    		document.getElementById('tb_list').deleteRow(i+1);				// 두번째행부터 삭제
									    		gflag = false;
							    			}else{
							    				genre_levelInput[i].value = document.regfrm.level1.value+"__"+document.regfrm.level2.value;
							    				genre_level_onInput[i].value = document.regfrm.level1.value+" > "+document.regfrm.level2.value;
							    				gflag = true;
							    			}
							    		}
							    		if(gflag == true) break;
								    }
							  }
					  		}
					   }else{	  
						    for(var i=0 ; i < addnum ; i++) {
							  if(addnum == i+1){
								  if(i == 0){
									  genre_levelInput[i].value = document.regfrm.level1.value+"__"+document.regfrm.level2.value+"__"+document.regfrm.level3.value;
									  genre_level_onInput[i].value = document.regfrm.level1.value+" > "+document.regfrm.level2.value+" > "+document.regfrm.level3.value;
								    }else{
								    	for( var h=0 ; h < i ; h++){
							    			if( genre_levelInput[i-h-1].value == (document.regfrm.level1.value+"__"+document.regfrm.level2.value+"__"+document.regfrm.level3.value) ) {
							    				alert("동일한 장르는 입력하실 수 없습니다.");
									    		document.getElementById('tb_list').deleteRow(i+1);				// 두번째행부터 삭제
									    		gflag = false;
							    			}else{
							    				genre_levelInput[i].value = document.regfrm.level1.value+"__"+document.regfrm.level2.value+"__"+document.regfrm.level3.value;
							    				genre_level_onInput[i].value = document.regfrm.level1.value+" > "+document.regfrm.level2.value+" > "+document.regfrm.level3.value;
							    				gflag = true;
							    			}
							    		}
							    		if(gflag == true) break;
								    }
							  }
					  		}
					   }
				  }
			  }	   
		}else{
			alert("장르를 선택 후 추가 버튼을 클릭하여 최종 등록할 장르 조합을 확인하세요");
		}  
	} 
	
	function removeRow(r){ 
		 var i=r.parentNode.parentNode.rowIndex;
		 document.getElementById('tb_list').deleteRow(i);
	}

// 카테고리 3단선택
	function setlv1()
	{
		document.regfrm.level1.options[0] = new Option("==선택==", "");
		document.regfrm.level2.options[0] = new Option("==선택==", "");
		document.regfrm.level3.options[0] = new Option("==선택==", "");

		var selectTxt = new Array();
		var selectVle = new Array();
	
		<c:set var="i" value="0" />
		<c:forEach items="${result_large}" var="level1list">
		<c:set var="i" value="${i+1}" /> 
			selectTxt[${i}] = "${level1list.genre_large}";
			selectVle[${i}] = "${level1list.genre_large}";
		</c:forEach>

		for(var i=1;i<selectVle.length;i++)
		{
			document.regfrm.level1.options[i] = new Option( selectTxt[i], selectVle[i] );
		}
		setlv2();
	}

	function setlv2()
	{
		// 중분류 초기화
		for(var i=document.regfrm.level2.options.length;i>=0;i--)
		{
			document.regfrm.level2.options[i] = null;
		}
		document.regfrm.level2.options[0] = new Option("==선택==", "");

		// 소분류 초기화
		for(var i=document.regfrm.level3.options.length;i>=0;i--)
		{
			document.regfrm.level3.options[i] = null;
		}
		document.regfrm.level3.options[0] = new Option("==선택==", "");

		var selectTxt = new Array();
		var selectVle_large = new Array();
		var selectVle_mid = new Array();

		<c:set var="i" value="0" />
		<c:forEach items="${result_mid}" var="level2list">
		<c:set var="i" value="${i+1}" />
			selectTxt[${i}] = "${level2list.genre_mid}";
			selectVle_large[${i}] = "${level2list.genre_large}";
			selectVle_mid[${i}]   = "${level2list.genre_mid}";
		</c:forEach>

		var optionIndex = 1;
		for(var i=1;i<selectTxt.length;i++)
		{	
			//if(document.regfrm.level1.value == selectVle[i].substring(0,5))
			if(document.regfrm.level1.value == selectVle_large[i])
			{
				document.regfrm.level2.options[optionIndex] = new Option( selectTxt[i],  selectVle_mid[i] );

				optionIndex++;
			}
		}
		setlv3();
	}

	function setlv3()
	{
		// 소분류 초기화
		for(var i=document.regfrm.level3.options.length;i>=0;i--)
		{
			document.regfrm.level3.options[i] = null;
		}
		document.regfrm.level3.options[0] = new Option("==선택==", "");

		var selectTxt = new Array();
		var selectVle_mid = new Array();
		var selectVle_small = new Array();

		<c:set var="i" value="0" />
		<c:forEach items="${result_small}" var="level3list">
		<c:set var="i" value="${i+1}" />
			selectTxt[${i}] = "${level3list.genre_small}";
			selectVle_mid[${i}] = "${level3list.genre_mid}";
			selectVle_small[${i}] = "${level3list.genre_small}";
		</c:forEach>

		var optionIndex = 1;
		for(var i=1;i<selectTxt.length;i++)
		{
			//if(document.regfrm.level2.value == selectVle[i].substring(0,7))
			if(document.regfrm.level2.value == selectVle_mid[i])
			{
				document.regfrm.level3.options[optionIndex] = new Option( selectTxt[i], selectVle_small[i] );
				optionIndex++;
			}
		}
	}
	
	// 장르 최종 적용 로직 함수
	$(document).ready(function(){
		$("#setgenre").click(function(){
			var genre_levelInput  = document.getElementsByName('genre_level');	
			var genre_level_onInput  = document.getElementsByName('genre_level_on');	
			var genre_levelLength = document.getElementsByName("genre_level").length;
			var genre_levelHap = null;
			var genre_levelHap_on = null;
			
			if( genre_levelLength > 0 ) {
				for(var i=0 ; i < genre_levelLength ; i++) {			
					if (i==0) {
						genre_levelHap = genre_levelInput[i].value;
						genre_levelHap_on = genre_level_onInput[i].value;
					}else{
						genre_levelHap = genre_levelHap + "||" + genre_levelInput[i].value;
						genre_levelHap_on = genre_levelHap_on + " + " + genre_level_onInput[i].value;
					}
					document.regfrm.elHap.value  = genre_levelHap;
					document.regfrm.elHapon.value  = genre_levelHap_on;
				}
				
				if(document.regfrm.multiCheck.value == 'Y') {
				    opener.document.regfrm.tgenre.value=document.regfrm.elHapon.value;
				    opener.document.regfrm.hgenre.value=document.regfrm.elHap.value;
				}else{
					if(document.regfrm.genreElementid.value != "" || document.regfrm.genreElementid.value != null) {
						var genreElementid = $("#genreElementid").val(); 			
			 			var openerelement1 = window.opener.jQuery("#" + genreElementid);
			 			openerelement1.val(document.regfrm.elHapon.value);
					}
					if(document.regfrm.genreHiddenid.value != "" || document.regfrm.genreHiddenid.value != null) {
						var genreHiddenid = $("#genreHiddenid").val(); 			
			 			var openerelement2 = window.opener.jQuery("#" + genreHiddenid);
			 			openerelement2.val(document.regfrm.elHap.value);
					}		
				}	 		
		 		self.close();	
			}else{
				alert("장르 선택 후 추가 버튼을 클릭하여 최종 등록할 장르 조합을 확인하세요");
			}
			
		});
	
		$("#resetbtn").click(function(){
			window.location.reload(true);
			//$("#regfrm")[0].reset();			
		});
		
		$("#closebtn").click(function(){
			//$("#updfrm")[0].reset();
			 window.close();		
		});
		
	});
</script>
</head>

<body onload="setlv1(document.regfrm)" leftmargin="0" topmargin="0">
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
						                                            <td width="15"><img src="/smartux_adm/images/admin/blt_07.gif"></td>
						                                            <td class="bold">장르선택화면</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                       	
						                   <form id="regfrm" name="regfrm" method="post" action="">
					                            <table border="0" cellpadding="0" cellspacing="0" width="600" class="board_data">
					                                <tbody>
					                                    <tr align="center">
						                                	<th width="20%">장르선택</th>
						                                	<td width="5%"></td>
						                                	<td width="55%" align="left" >
																<select id="level1" name="level1" onchange="setlv2()" style="width:100px">
																</select>
																<select id="level2" name="level2" onchange="setlv3()" style="width:100px">
																</select>
																<select id="level3" name="level3" style="width:100px">
																</select>
															</td>															
															<td width="15%">
														    	<input type="button" value="장르추가" onclick="javascript:insertform();" class="button small blue" align="right"/>
														    </td>	
														    <td width="5%"></td>													
						                                </tr>
						                                
							                       	</tbody>
					                            </table>
					                           
												<br>		
												<table  border="0" cellpadding="0" cellspacing="0" width="600" class="board_data" >
					                             <tbody>
					                              <tr align="center">
					                                	<th width="60%">선택장르명</th>
					                                	<th width="40%">삭제</td>
					                              </tr>  					                             
												 </tbody>
												</table>			                            
					                            <table id="tb_list" border="0" cellpadding="0" cellspacing="0" width="600" class="board_data" >
					                             <tbody>					                                	
					                              <tr>
				                                    <td> </td>
												  </tr>  
												 </tbody>
												</table>
												
												<br>
												<br>
					                            <table border="0" cellpadding="0" cellspacing="0" width="600" >
					                              <tr  align="right"> 
				                                    <td >				                                    	
												    	<input type="button" id="setgenre" value="확인"    class="button small blue"/>
												    	<input type="button" id="resetbtn" value="재작성"  class="button small blue"/>
						                               	<input type="button" id="closebtn" value="닫기"    class="button small blue"/>		
												    </td>
												  </tr>  
												</table>  
												
					                         	<input type="hidden" id="elHap" name="elHap" value="" />
					                         	<input type="hidden" id="elHapon" name="elHapon" value="" />
					                         	<input type="hidden" id="multiCheck" name="multiCheck" value="<%=multiCheck%>" />	
					                         	<input type="hidden" id="genreElementid" name="genreElementid" value="${genreElementid}" />
					                         	<input type="hidden" id="genreHiddenid"  name="genreHiddenid"  value="${genreHiddenid}" />
					                         					                            	
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
