<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String actionUrl=(request.getParameter("actionUrl") != null) ? request.getParameter("actionUrl") : "";	
	String naviUrl=(request.getParameter("naviUrl") != null) ? request.getParameter("naviUrl") : "?";
	String r_pageNum=(request.getParameter("pageNum") != null) ? request.getParameter("pageNum") : "0";
	String r_pageSize=(request.getParameter("pageSize") != null) ? request.getParameter("pageSize") : "0";
	String r_blockSize=(request.getParameter("blockSize") != null) ? request.getParameter("blockSize") : "10";
	String r_pageCount=(request.getParameter("pageCount") != null) ? request.getParameter("pageCount") : "0";
	
	int pageNum=0;
	pageNum=Integer.parseInt(r_pageNum);
	if(pageNum ==0){
		pageNum =1;
	}
	int pageSize=0;
	pageSize=Integer.parseInt(r_pageSize);
	int blockSize=0;
	blockSize=Integer.parseInt(r_blockSize);
	
	//총페이지
	int totalCount=0;
	int pageCount=0;
	totalCount=Integer.parseInt(r_pageCount);
	int remain = totalCount % pageSize;
	if(remain == 0){                             // 총 페이지 수를 구하기 위한 나머지 여부
		pageCount = totalCount/pageSize;
	}else{
		pageCount = totalCount/pageSize + 1;
	}
	
	//시작페이지
	int startPage=0;	
	startPage=(((pageNum-1)/ blockSize) * blockSize)+1;
	if(startPage == 0){
		startPage=1;
	}

	//종료페이지
	int endPage=0;
	//endPage=startPage+blockSize-1;
	endPage = startPage + (pageSize - 1);
	if(startPage>1){
		startPage=startPage-1;
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
<fmt:formatNumber var="page" value="<%=pageCount%>"  pattern="0" />
<fmt:formatNumber var="pageNum" value="<%=pageNum%>"  pattern="0" />
<fmt:formatNumber var="startPage" value="<%=startPage%>"  pattern="0" />
<fmt:formatNumber var="endPage" value="<%=endPage %>"  pattern="0" />
<fmt:formatNumber var="blockSize" value="<%=blockSize%>"  pattern="0" />
<fmt:formatNumber var="pageCount" value="<%=pageCount%>"  pattern="0" />

<c:if test="${page <= endPage}">
	<c:set var="endPage" value="${page}" />
</c:if>
<c:choose>
	<c:when test="${pageNum == 1 }"><img src='/smartux_adm/images/admin/prev_icon_02.gif' alt='맨앞' /></c:when>
	<c:otherwise>
		<a href="<%=actionUrl%><%=naviUrl%>&pageNum=${1}"><img src='/smartux_adm/images/admin/prev_icon_02.gif' alt='맨앞' /></a>
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${pageNum eq 1}">
		<a href="#con" title="이전페이지" class="span"><img src='/smartux_adm/images/admin/prev_icon_01.gif' alt='이전페이지' /></a>
	</c:when>
	<c:otherwise>
		<a href="<%=actionUrl%><%=naviUrl%>&pageNum=${pageNum - 1}" title="이전페이지" class="span"><img src='/smartux_adm/images/admin/prev_icon_01.gif' alt='이전페이지' /></a>
	</c:otherwise>
</c:choose>
<%
	for(int i=startPage;i<=endPage;i++){
		if(pageNum == i){
%>
			<B><a href="#con" title="<%=i%>" class="current"><%=i%></a></B>
<%
		}else{
%>
			<a href="<%=actionUrl%><%=naviUrl%>&pageNum=<%=i%>" title="<%=i%>"><%=i%></a>
<%		
		}		
		if (i == pageCount || pageCount == 0){
			break;
		}
	}
%>
<c:choose>
	<c:when test="${pageNum == page || pageNum > page}">
					<a href="#con" title="다음페이지" class="span"><img src='/smartux_adm/images/admin/next_icon_01.gif' alt='다음페이지' /></a>
			   </c:when>
	<c:otherwise>
		<a href="<%=actionUrl%><%=naviUrl%>&pageNum=${pageNum+1}" title="다음페이지" class="span"><img src='/smartux_adm/images/admin/next_icon_01.gif' alt='다음페이지' /></a>
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${endPage == page}">
					<img src='/smartux_adm/images/admin/next_icon_02.gif' alt='맨뒤' />
	</c:when>
	<c:otherwise>
		<a href="<%=actionUrl%><%=naviUrl%>&pageNum=${pageCount}"><img src='/smartux_adm/images/admin/next_icon_02.gif' alt='맨뒤' /></a>
	</c:otherwise>
</c:choose>
</body>
</html>
