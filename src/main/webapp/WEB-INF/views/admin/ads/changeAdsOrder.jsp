<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>LG U+ HDTV</title>
    <link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
    <jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
<style>
    .selectTable {
      border: 1px solid #000000;
    }
 
    .selected {
      background-color: #d0e8fd;
    }
</style>
    <script type="text/javascript">
        $(document).ready(function () {
        	$("#titlelist").on('click', '#titlelist li', function (e) {
                if (e.ctrlKey || e.metaKey) {
                    $(this).toggleClass("selected");
                   
                } else {
                    $(this).addClass("selected").siblings().removeAttr('class');
                 
                }
            });
        	
        	$('#titlelist').sortable({
                delay: 150, 
                revert: 0,
                helper: function (e, item) {
                    if (!item.hasClass('selected')) {
                        item.addClass('selected').siblings().removeClass('selected');
                    }
                    var elements = item.parent().children('.selected').clone();
                    item.data('multidrag', elements).siblings('.selected').remove();

                    var helper = $('<li/>');
                    return helper.append(elements);
                },
                stop: function (e, ui) {
                    var elements = ui.item.data('multidrag');

                    ui.item.after(elements).remove();
                    elements.siblings().removeAttr('id');
                    elements.siblings().removeAttr('class');
                 
                }
            });


            $('#changeorderbtn').click(function () {
            	if($('#titlelist li').size() == 0){
            		//alert('????????? ????????? ????????????.');
            		return false;
            	}
                if (confirm('????????? ????????? ??????????????????????')) {
                    var size = $('#titlelist li').size();
                    var optionArray = new Array();

                    for (var i = 0; i < size; i++) {
                        var selval = $('#titlelist li').eq(i).attr("data-val");
                        optionArray.push(selval);
                    }

                    $.ajax({
                        url: '<%=webRoot%>/admin/ads/changeOrder',
                        type: 'PUT',
                        dataType: 'json',
                        data: {
                            optionArray: optionArray
                        },
                        error: function () {
                            alert('?????? ??? ????????? ?????????????????????');
                        },
                        success: function (rs) {
                            // ?????? ????????? ?????? ?????? ????????? ?????? ???????????? ????????????
                            var flag = rs.flag;

                            var message = rs.message;

                            if (flag == '0000') {						// ??????????????? ????????? ??????
                                alert('????????? ?????????????????????');
                                opener.location.reload();
                                self.close();
                            } else {
                                alert('?????? ??? ????????? ?????????????????????\nflag : ' + flag + '\nmessage : ' + message);
                            }
                        }
                    });
                }
            });

            $('#resetbtn').click(function () {
                location.reload();
            });

            $('#closebtn').click(function () {
                self.close();
            });

        });


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
                                                    <table border="0" cellpadding="0" cellspacing="0" width="100%"
                                                           align="center">
                                                        <tbody>
                                                        <tr>
                                                            <td class="3_line" height="1"></td>
                                                        </tr>
                                                        <!-- ????????? ?????? -->
                                                        <tr>
                                                            <td>
                                                                <table border="0" cellpadding="0" cellspacing="0"
                                                                       width="100%" align="center">
                                                                    <tbody>
                                                                    <tr>
                                                                        <td height="25">
                                                                            <table border="0" cellpadding="0"
                                                                                   cellspacing="0" width="100%">
                                                                                <tbody>
                                                                                <tr>
                                                                                    <td width="15"><img
                                                                                            src="/smartux_adm/images/admin/blt_07.gif">
                                                                                    </td>
                                                                                    <td class="bold">?????? ?????? ?????????</td>
                                                                                </tr>
                                                                                </tbody>
                                                                            </table>
                                                                        </td>
                                                                    </tr>
                                                                    </tbody>
                                                                </table>
                                                                <form id="orderfrm" name="orderfrm" method="get"
                                                                      action="">
                                                                    <table border="0" cellpadding="0" cellspacing="0"
                                                                           width="100%" align="center">
                                                                        <tbody>
                                                                        <tr align="left">
                                                                            <td width="75%">
                                                                                <%-- <select id="titlelist" name="titlelist"
                                                                                        size="${fn:length(result)}"
                                                                                        style="width:100%;height:200px;">
                                                                                    <c:forEach var="item"
                                                                                               items="${result}"
                                                                                               varStatus="status">
                                                                                        <option value="${item.number}">[${item.order}] ${item.title}
                                                                                        </option>
                                                                                    </c:forEach>
                                                                                </select> --%>
                                                                                <c:if test="${result == '[]' }">
		                
																	                    <td class="table_td_04" colspan="6">???????????? ?????? ?????? ????????????.</td>
																	               
																	            </c:if>
																	            <c:if test="${result != '[]' }">
																		           
																	                   <ul id="titlelist" name="titlelist" style="border:1px solid grey;padding:10px;list-style:none">
																	                       	<c:forEach var="item" items="${result}"  varStatus="status">
																	                     		<li style="list-style:none" data-val="${item.number}">[${item.order}] ${item.title}</li>
																							</c:forEach>
																						</ul>
																					
																				</c:if> 
                                                                            </td>
                                                                            <td width="5%"></td>
                                                                            <!-- <td width="20%" align="left">
                                                                                <span class="button small blue"
                                                                                      id="upbtn">??????</span><br/><br/>
                                                                                <span class="button small blue"
                                                                                      id="downbtn">?????????</span>
                                                                            </td> -->
                                                                        </tr>
                                                                        </tbody>
                                                                    </table>
                                                                    <br/>
                                                                    <table border="0" cellpadding="0" cellspacing="0"
                                                                           width="200" align="center">
                                                                        <tbody>
                                                                        <tr>
                                                                            <td height="25" align="center">
                                                                                <span class="button small blue"
                                                                                      id="changeorderbtn">??????</span>
                                                                                <span class="button small blue"
                                                                                      id="resetbtn">?????????</span>
                                                                                <span class="button small blue"
                                                                                      id="closebtn">??????</span>
                                                                            </td>
                                                                        </tr>
                                                                        </tbody>
                                                                    </table>
                                                                </form>

                                                                <table border="0" cellpadding="0" cellspacing="0"
                                                                       width="100%">
                                                                    <tbody>
                                                                    <tr>
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
                                                        <!-- ????????? ?????? -->
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