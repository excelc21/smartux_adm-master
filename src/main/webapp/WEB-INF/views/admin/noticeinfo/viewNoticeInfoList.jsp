<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX</title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<script type="text/javascript">
</script>
</head>
<body leftmargin="0" topmargin="0">
	<div id="divBody" style="height:100%">
		<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%" height="100%" background="/smartux_adm/images/admin/bg/noticeinfo_bg.gif">
		    <tbody>
	    		<tr>
	    			<td valign="bottom" style="padding-bottom:22px;">
	    				<table cellpadding="0" cellspacing="0" border="0" bgcolor="#D8D8D8" width="350">
	    					<tr>
	    						<td style="padding:2px;">
		    						<c:forEach items="${txtVo}" var="rec">
					                 <c:set var="i" value="${i+1}" />
										<b>${i}.</b> ${rec.nvalue}<br/>
									</c:forEach>
	    						</td>
	    					</tr>
	    				</table>
	    			</td>
	    			<td valign="top" style="padding-top:22px;" align="right">
	    				<table cellpadding="0" cellspacing="0" border="0" bgcolor="#D8D8D8" width="350">
	    					<tr>
	    						<td style="padding:2px;">
		    						<c:forEach items="${imgVo}" var="rec">
					                 <c:set var="i" value="${i+1}" />
										<image src="${rec.nvalue}" width="350" height="30">
									</c:forEach>
	    						</td>
	    					</tr>
	    				</table>
	    			</td>
	    		</tr>
		    </tbody>
		</table>
	</div>
</body>
</html>