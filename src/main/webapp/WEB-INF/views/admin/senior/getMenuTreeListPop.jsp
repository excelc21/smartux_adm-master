<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>메뉴트리 선택</title>
	<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
	<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>

	<script type="text/javascript">
        $(document).ready(function () {
            function init() {
                const openPanelId = "${openPanelId}";
                const openPanelTarget = $(".paperRow" + openPanelId).eq(0).closest('tr');
                openPanel(openPanelTarget);
            }

            $("#closebtn").click(function () {
                self.close();
            });

            $(".paperRow").click(function () {
                openPanel($(this))
            });


            $("#selcode").change(function(){
                location.replace('<%=webRoot%>/admin/teleport/getMenuTreeList.do?callbak=getMenuTreeCallbak&panel_id='+$(this).val());
            });




            function openPanel(target) {
                $.each(target, function () {
                    const panelId = target.find('input:hidden[name="panelId"]').val();
                    console.log(panelId);
                    $('.paperRow' + panelId).css('display', '');
                });
                target.focus();
            }

            init();
        });

        function slt(sltid){
            const ay = sltid.split("|");
            let dataobj = {};
            dataobj.panel_id = ay[0];
            dataobj.paper_code = ay[1];
            dataobj.paper_name = ay[2];
            const callbak_m = eval("opener."+"${callbak}");
            callbak_m(dataobj);
            self.close();
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
					<td class="bold">메뉴트리 선택</td>
				</tr>
				</tbody>
			</table>
			<table border="0" cellpadding="0" cellspacing="0" style="width:100%;">
				<tbody>
				<tr>
					<td width="10%">
						<select id="selcode" name="selcode">
							<c:forEach var="item" items="${panel_result}" varStatus="status">
								<c:choose>
									<c:when test="${item.pannel_id == panel_id}">
										<option value="${item.pannel_id}" selected>${item.pannel_nm}</option>
									</c:when>
									<c:otherwise>
										<option value="${item.pannel_id}">${item.pannel_nm}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
					</td>
				</tr>
				</tbody>
			</table>
		</td>
	</tr>
	</tbody>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%" class="board_list">
	<tbody>
	<tr align="center">
		<th scope="col" width="300">메뉴명</th>
		<th scope="col" width="60">선택</th>
	</tr>
	</tbody>

	<c:forEach var="item" items="${result}" varStatus="status">
		<tr align="left" onMouseOver="this.style.backgroundColor='#d0e8fd'" onMouseOut="this.style.backgroundColor=''">
			<td align="left" style="text-align: left">
				&nbsp;
				<c:forEach var="gp" begin="2" end="${item.level}" step="1" >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</c:forEach>
				<img src='/smartux_adm/images/menu/folder.gif' border=0>
				<c:choose>
					<c:when test="${item.level == 1}">
						<b>${item.title_nm}</b>
					</c:when>
					<c:otherwise>
						${item.title_nm}
					</c:otherwise>
				</c:choose>
				&nbsp;&nbsp;&nbsp;<span style="margin-right:10px; float:right;color:black;">(${item.pannel_id}_${item.title_id})</span>
			</td>
			<td class="table_td_04" >
				<input type="hidden" name="panel_id" value="${item.pannel_id}">
				<input type="hidden" name="paper_code" value="${item.pannel_id}_${item.title_id}">
				<span class="button small blue" onclick="slt('${item.pannel_id}|${item.pannel_id}_${item.title_id}|${item.title_nm}')">선택</span>
			</td>
		</tr>
	</c:forEach>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr >
		<td align="center" height=40>
			<span class="button small blue" id="closebtn">닫기</span>
		</td>
	</tr>
</table>
<p>&nbsp;</p>