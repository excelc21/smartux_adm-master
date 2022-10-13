<%@page import="com.dmi.smartux.common.util.aes.StringEncrypter"%>
<%@page import="com.dmi.smartux.common.util.aes.Aes_Key"%>
<%@page import="com.dmi.smartux.common.util.CookieUtil"%>
<%
/*	Aes_Key aec_key = new Aes_Key();
	StringEncrypter encrypter = new StringEncrypter(aec_key.key,aec_key.vec);
	Cookie idCookie = CookieUtil.getCookie(request, "smartUXManager");
	Cookie authCookie = CookieUtil.getCookie(request, "smartUXManagerAuth");
	
	String id_encrypt = "";
	String id_decrypt = "";
	String auth_encrypt = "";
	String auth_decrypt = "";
	String authStr = "";
	if(idCookie == null){
		out.println("<script type='text/javascript'>alert('로그인 후 이용해 주시기 바랍니다.');location.href='/smartux_adm/admin/login/login.do';</script>");
		out.flush();
	}else{
		if(idCookie.getValue().equals("")){
			out.println("<script type='text/javascript'>alert('로그인 후 이용해 주시기 바랍니다.');location.href='/smartux_adm/admin/login/login.do';</script>");
			out.flush();	
		}else{
			id_encrypt = java.net.URLDecoder.decode(idCookie.getValue()); //쿠키사용시 decoding
			id_decrypt = encrypter.decrypt(id_encrypt);
			
			auth_encrypt = java.net.URLDecoder.decode(authCookie.getValue()); //쿠키사용시 decoding
			auth_decrypt = encrypter.decrypt(auth_encrypt);
			
			if(auth_decrypt.equals("00")){
				authStr = "슈퍼관리자";
			}else{
				authStr = "일반관리자";
			}
		}
		//out.println(id_encrypt);
		//out.println(id_decrypt);
		
		//out.println(auth_encrypt);
		//out.println(auth_decrypt);
	}
*/
%>
<%@include file="/WEB-INF/views/include/common.jsp" %>

<c:set var="id_decrypt" value="<%=id_decrypt%>"></c:set>
<c:set var="auth_decrypt" value="<%=auth_decrypt%>"></c:set>
<script type="text/javascript">
function logout(){ 
	$.ajax({
	    url: '/smartux_adm/admin/login/logout.do',
	    type: 'POST',
	    dataType: 'text',
	    data: {
	    },
	    error: function(){
	    	location.href="/smartux_adm/admin/login/login.do";
	    },
	    success: function(textDoc){
	    	if(textDoc == "SUCCESS"){
	    		alert(" 로그아웃 되었습니다.")
	    		location.href="/smartux_adm/admin/login/login.do";
	    	}else{
	    		location.href="/smartux_adm/admin/login/login.do";	
	    	}	    	
	    }
	});	
}
</script>
<table border="0" cellpadding="0" cellspacing="0" height="35" width="100%" class="top_table">
    <tbody>
    <tr>
        <td>
            <img src="/smartux_adm/images/admin/logo.jpg" height="35" width="203">
        </td>
        <!-- <td width="33"><a href="javascript:bnms_main();" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image2','','/img/t_m_01_o.gif',1)" onfocus="blur()"><img src="/smartux_adm/images/admin/t_m_01.gif" name="Image2" alt="HOME" border="0" height="9" width="33"></a></td>
        <td width="10"></td>
        <td width="23"><a href="javascript:onAlert();" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image3','','/img/t_m_02_o.gif',1)" onfocus="blur()"><img src="/smartux_adm/images/admin/t_m_02.gif" name="Image3" alt="FAQ" border="0" height="9" width="23"></a></td>
        <td width="10"></td>
        <td width="47"><a href="javascript:onAlert();" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image4','','/img/t_m_03_o.gif',1)" onfocus="blur()"><img src="/smartux_adm/images/admin/t_m_03.gif" name="Image4" alt="SITEMAP" border="0" height="9" width="47"></a></td>
        <td width="10"></td>
        <td width="28"><a href="javascript:onAlert();" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image5','','/img/t_m_04_o.gif',1)" onfocus="blur()"><img src="/smartux_adm/images/admin/t_m_04.gif" name="Image5" alt="HELP" border="0" height="9" width="28"></a></td>
        <td width="10">
        </td>
        <td width="68">
            <a href="javascript:person()" onfocus="blur()"><img src="/smartux_adm/images/admin/btn_user_modify.gif" border="0" height="19" width="68"></a>
        </td>
        <td width="6">
        </td>-->
        <td width="420" align="right"> 
            <img src='/smartux_adm/images/admin/user1.png' height="16" width="16" border='0'>&nbsp;<strong>USER</strong>&nbsp;&nbsp;: <%=id_decrypt %> (<%=authStr %>)&nbsp;&nbsp;&nbsp;&nbsp;
        </td>
        <td width="48">
            <a href="javascript:logout()"><img src="/smartux_adm/images/admin/btn_logout.gif" border="0" height="19" width="48"></a>
        </td>
        <td width="30"></td> 
    </tr>   
	</tbody>
</table>
