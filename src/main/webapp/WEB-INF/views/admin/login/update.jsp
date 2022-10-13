<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<style type="text/css">
.pop-layer .pop-container {
  padding: 20px 25px;
}

.pop-layer p.ctxt {
  color: #666;
  line-height: 25px;
}

.pop-layer .btn-r {
  width: 100%;
  margin: 10px 0 20px;
  padding-top: 10px;
  border-top: 1px solid #DDD;
  text-align: right;
}

.pop-layer {
  display: none;
  position: absolute;
  top: 50%;
  left: 50%;
  width: 410px;
  height: auto;
  background-color: #fff;
  border: 5px solid #3571B5;
  z-index: 10;
}

.dim-layer {
  display: none;
  position: fixed;
  _position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 100;
}

.dim-layer .dimBg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: #000;
  opacity: .5;
  filter: alpha(opacity=50);
}

.dim-layer .pop-layer {
  display: block;
}

a.btn-layerClose {
  display: inline-block;
  height: 25px;
  padding: 0 14px 0;
  border: 1px solid #304a8a;
  background-color: #3f5a9d;
  font-size: 13px;
  color: #fff;
  line-height: 25px;
}

a.btn-layerClose:hover {
  border: 1px solid #091940;
  background-color: #1f326a;
  color: #fff;
}
</style>
<script type="text/javascript">
$(document).ready(function(){
        layer_popup("#layer2");
	
    
});

function layer_popup(el){

    var $el = $(el);    //레이어의 id를 $el 변수에 저장
    var isDim = $el.prev().hasClass('dimBg'); //dimmed 레이어를 감지하기 위한 boolean 변수

    isDim ? $('.dim-layer').fadeIn() : $el.fadeIn();

    var $elWidth = ~~($el.outerWidth()),
        $elHeight = ~~($el.outerHeight()),
        docWidth = $(document).width(),
        docHeight = $(document).height();

    // 화면의 중앙에 레이어를 띄운다.
    if ($elHeight < docHeight || $elWidth < docWidth) {
        $el.css({
            marginTop: -$elHeight /2,
            marginLeft: -$elWidth/2
        })
    } else {
        $el.css({top: 0, left: 0});
    }

    $el.find('a.btn-layerClose').click(function(){
        isDim ? $('.dim-layer').fadeOut() : $el.fadeOut(); // 닫기 버튼을 클릭하면 레이어가 닫힌다.
        return false;
    });

    $('.layer .dimBg').click(function(){
        $('.dim-layer').fadeOut();
        return false;
    });

}

function goDelete(f_id){
	
	if(!confirm("계정을 삭제하시겠습니까?")) {
		return;
	}else{
		$.ajax({
		    url: '/smartux_adm/admin/login/delete.do',
		    type: 'POST',
		    dataType: 'text',
		    data: {
		        "smartux_id": f_id	        
		    },
		    error: function(){
		    	alert("에러가 발생하였습니다.");
		    },
		    success: function(textDoc){
		    	if(textDoc == "SUCCESS"){
		    		alert(" 계정이 삭제 되었습니다.");
		    		location.href="list.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}";
		    	}else{
		    		alert("에러가 발생하였습니다.");	
		    	}	    	
		    }
		});	
	}
}

function goUpdate(){
	var f = document.getElementById("form1");
	
	/* if(f.password.value == ''){
		alert('비밀번호를 입력해 주세요.');
		f.password.focus();
		return;
	}else if(!CheckPassword(f.user_id.value,f.password.value)){
		f.password.value = '';
		f.password.focus();
		return;
	}else if(f.password_chk.value == ''){
		alert('비밀번호 확인을 입력해 주세요.');
		f.password_chk.focus();
		return;
	}else if(f.password.value != f.password_chk.value){
		alert('비밀번호 확인과 일치 하지 않습니다.');
		f.password.value = '';
		f.password_chk.value = '';
		f.password.focus();
		return;
	}
	 */
	if(f.user_id.value == ''){
		alert('아이디를 입력해주세요.');
		f.user_id.focus();
		return;	
	}else if(f.name.value == ''){
		alert('이름을 입력해 주세요.');
		f.name.focus();
		return;
	}else if(f.exp_day.value == ''){
		alert('만료일을 입력해 주세요.');
		return;
	}else if(!CheckNumber(f.exp_day.value)){
		f.exp_day.value = '';
		f.exp_day.focus();
		return;
	}else if(f.exp_day.value > 90){
		alert('만료일은 90일 이하만 입력 가능합니다.');
		return;
	}
	
	if(f.oldpassword.value != ''){
		
		if(f.password.value != '' && f.password_chk.value !=''){
			if(!CheckPassword(f.user_id.value,f.oldpassword.value)){
				f.oldpassword.value = '';
				f.oldpassword.focus();
				return;
			}else if(!CheckPassword(f.user_id.value,f.password.value)){
				f.password.value = '';
				f.password.focus();
				return;
			}else if(f.password.value != f.password_chk.value){
				alert('비밀번호 확인과 일치 하지 않습니다.');
				f.password.value = '';
				f.password_chk.value = '';
				f.password.focus();
				return;
			}
		}else if(f.password.value == '' || f.password_chk.value !=''){
			alert('비밀번호를 입력해 주세요. 비밀번호 변경을 원치 않을 시 기존 비밀번호를 삭제해주세요.');
			f.password.focus();
			return;
		}else if(f.password.value != '' || f.password_chk.value ==''){
			alert('비밀번호 확인을 입력해 주세요. 비밀번호 변경을 원치 않을 시 기존 비밀번호를 삭제해주세요.');
			f.password_chk.focus();
			return;
			
		}
	}else{
		if(!confirm("기존비밀번호 미입력시 비밀번호는 변경되지 않습니다. 수정하시겠습니까?")) {
			f.oldpassword.focus();
		}
	}
	f.submit();
}

function CheckPassword(uid, upw){
    if(!/^[a-zA-Z0-9]{10,20}$/.test(upw)){ 
        alert('비밀번호는 숫자와 영문자 조합으로 10~20자리를 사용해야 합니다.'); 
        return false;
    }

  
    var chk_num = upw.search(/[0-9]/g);
    var chk_eng = upw.search(/[a-z]/ig);

    if(chk_num < 0 || chk_eng < 0){
        alert('비밀번호는 숫자와 영문자를 혼용하여야 합니다.');
        return false;
    }
    
    if(/(\w)\1\1\1/.test(upw)){
        alert('비밀번호에 같은 문자를 4번 이상 사용하실 수 없습니다.'); 
        return false;
    }

    if(upw.search(uid)>-1){
        alert('ID가 포함된 비밀번호는 사용하실 수 없습니다.'); 
        return false;
    }
    
    return true;

} 

function CheckNumber(day){
	var chk_num = day.search(/[0-9]/g);
	
	if(chk_num < 0){
		alert('만료일은 숫자를 입력하여야 합니다.');
        return false;
	}
	return true;
}

function CheckKeys(){
    if ( event.keyCode < 48 || event.keyCode > 57 )
    {
         event.keyCode = 0;
    }
}

function Limit(obj) {
 var maxLength = parseInt(obj.getAttribute("maxlength"));
 if ( obj.value.length >= maxLength ) {
  alert(maxLength+"자 이상 등록 할수 없습니다.");
  obj.value = obj.value.substring(0, maxLength);
 }
}

function enterKey(){

	if(event.keyCode==13){
		go();
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
                                    관리자
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
						                                            <td class="bold">관리자 계정 수정</td>
						                                        </tr>
						                                    	</tbody>
						                                    </table>
						                                </td>
						                            </tr>
						                       		</tbody>
						                       	</table>
						                       	<%
													String domain_https = (String)request.getAttribute("domain_https");
												%>
						                        <form id="form1" action="/smartux_adm/admin/login/update.do" method="post">
						                        <input type="hidden" name="findName" value="${vo.findName }" />
						                        <input type="hidden" name="findValue" value="${vo.findValue }" />
						                        <input type="hidden" name="pageNum" value="${vo.pageNum }" />
						                        <input type="hidden" name="user_id" value="${vo.user_id }"/>
						                        <input type="hidden" name="exp_date" value="${vo.exp_date }"/>
						                        
						                        <input type="hidden" name="org_exp_day" value="${vo.exp_day }"/>
						                        <input type="hidden" name="org_user_id" value="<%=id_decrypt%>"/>
						                        <input type="hidden" name="org_user_auth" value="<%=auth_decrypt%>"/>
						                         
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" class="board_data">
					                                <tbody>
					                                <tr align="center">
					                                    <th width="25%">아이디</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >${vo.user_id }																						
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">이름</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="text" name="name" size="35" style="font-size: 12px;" value="${vo.name }"/>								
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">기존비밀번호</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="password" name="oldpassword" size="36" style="font-size: 12px;" maxlength="20" value=""/>
															숫자와 영문자 조합으로 10~20자리를 사용						
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">비밀번호</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="password" name="password" size="36" style="font-size: 12px;" maxlength="20" value=""/>
															숫자와 영문자 조합으로 10~20자리를 사용						
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">비밀번호 확인</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="password" name="password_chk" size="36" style="font-size: 12px;" maxlength="20" value=""/>								
														</td>
					                                </tr>
					                                <c:choose>
		                                    			<c:when test="${auth_decrypt == '00'}">
				                                    		<tr align="center">
							                                    <th width="25%">계정 권한</th>
							                                    <td width="5%"></td>
							                                    <td width="70%" align="left" >
																	<select	name="user_auth">
																		<option value="01" <c:if test="${vo.user_auth == '01'}">selected="selected"</c:if>>일반관리자</option>
																		<option value="02" <c:if test="${vo.user_auth == '02'}">selected="selected"</c:if>>세컨드TV관리자</option>
																		<option value="03" <c:if test="${vo.user_auth == '03'}">selected="selected"</c:if>>VOD프로모션관리자</option>
																		<option value="04" <c:if test="${vo.user_auth == '04'}">selected="selected"</c:if>>시즌관리자</option>
																		<option value="00" <c:if test="${vo.user_auth == '00'}">selected="selected"</c:if>>슈퍼관리자</option>
																	</select>								
																</td>
							                                </tr>								                                    				
		                                    			</c:when>
		                                    			<c:otherwise>
															<input type="hidden" name="user_auth" value="${vo.user_auth }" />		                                    				
		                                    			</c:otherwise>
						                            </c:choose>
					                                <tr align="center">
					                                    <th width="25%">이메일</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="text" name="email" size="35" style="font-size: 12px;" value="${vo.email }"/>	
															ex) test@test.co.kr 							
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">만료일수</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<input type="text" name="exp_day" size="35" style="font-size: 12px;" onKeyPress="CheckKeys()" maxlength="2" value="${vo.exp_day }"/>	
															ex) 90 -> 90일							
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">만료일</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
															<c:choose>
				                                    		<c:when test="${fn:length(vo.exp_date) > 7}">
				                                    			${fn:substring(vo.exp_date,0,4)}-${fn:substring(vo.exp_date,4,6)}-${fn:substring(vo.exp_date,6,8)}
				                                    		</c:when>
				                                    		</c:choose>						
														</td>
					                                </tr>
					                                <tr align="center">
					                                    <th width="25%">메모</th>
					                                    <td width="5%"></td>
					                                    <td width="70%" align="left" >
					                                    	<textarea rows="10" cols="40" name="memo" maxlength="1000" onkeyup="return Limit(this);">${vo.memo }</textarea>
														</td>
					                                </tr>
					                            	</tbody>
					                            </table>
					                            
					                            <table border="0" cellpadding="0" cellspacing="0" width="660" align="left">
						                            <tbody>
						                            <tr>
						                                <td height="25" align="right">
						                                	<a href="javascript:goUpdate();"><span class="button small blue">수정</span></a>
						                                	<a href="javascript:goDelete('${vo.user_id }');"><span class="button small blue">삭제</span></a>
						                                	<a href="list.do?findName=${vo.findName}&findValue=${vo.findValue}&pageNum=${vo.pageNum}"><span class="button small blue">목록</span></a>
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
<c:choose>
<c:when test="${vo.validate == 'NOT ID'}">
	<script type="text/javascript">
		alert('아이디를 입력해 주세요.');
	</script>
</c:when>
<c:when test="${vo.validate == 'NOT PWD'}">
	<script type="text/javascript">
		alert('비밀번호를 입력해 주세요.');
	</script>
</c:when>
<c:when test="${vo.validate == 'PWD LENGTH'}">
	<script type="text/javascript">
	alert('비밀번호는 숫자와 영문자 조합으로 10~20자리를 사용해야 합니다.');
	</script>
</c:when>
<c:when test="${vo.validate == 'PWD MIX'}">
	<script type="text/javascript">
	alert('비밀번호는 숫자와 영문자를 혼용하여야 합니다.');
	</script>
</c:when>
<c:when test="${vo.validate == 'PASSWORD AGREE'}">
	<script type="text/javascript">
		alert('기존 비밀번호와 동일 합니다.');
	</script>
</c:when>  


<c:when test="${vo.validate == 'NOT EXPDATE'}">
	<script type="text/javascript">
		alert('만료일을 입력해주세요.');
	</script>
</c:when>
<c:when test="${vo.validate == 'EXPDATE NOT NUMBER TYPE'}">
	<script type="text/javascript">
		alert('만료일은 숫자만 사용해야 합니다.');
	</script>
</c:when>
<c:when test="${vo.validate == 'PASSWORD HIS'}">
	<script type="text/javascript">
		alert('history에 있는 비밀번호는 변경 불가합니다.');
	</script>
</c:when>


</c:choose>


</body>
</html>