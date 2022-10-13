<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>LG U+ IPTV SmartUX</title>
    <link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
    <jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
    <script type="text/javascript">
        //var colsGbn = "\b^";

        $(document).ready(function () {
        	type_select_bind();
            select_bind();
			
			$('#selectType').on('change', function() {
        		if(this.value == "0"){
        			$('.typeB').attr('style','display:none');
        			$('.typeA').removeAttr('style');
        		}else{
        			$('.typeA').attr('style','display:none');
        			$('.typeB').removeAttr('style');
        		}
        	});
			
			var searchBtnEvnt = function(){
                var series = "${series}";
                var specifyYn = "N";
                var serviceType = "";
                var isTypeChange = "${isTypeChange}";
                var type = "";
                var isAds = "${isAds}";
                if(isTypeChange == "Y"){
                	type = $("select[name='category_type_select']").val();
                }else{
                	type = "${type}";
                }
                
				$.post("<%=webRoot%>/admin/commonMng/getCategoryAlbumList.do",
                        {searchType: $('#searchType').val(), searchVal: $('#searchVal').val(), specifyYn : specifyYn, serviceType : serviceType,type : type},
                        function (data) {
                            // 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
                            var html = [], album_html = [], h = -1, c = -1, a = -1;
                            if (data.length == 0) {
                                clearAlbumList();
                            } else {
                                 for (var i = 0; i < data.length; i++) {
									var optionval = "";
									if(series=="Y"){
                                		var seriesNo = "";
                                		var categoryNo = "";
                                		if("Y"==data[i].series_yn){
                                    		seriesNo = data[i].series_no;
                                    		categoryNo = data[i].category_no;
                                		}
                                		optionval = data[i].category_id + "||" + data[i].album_id + "||" + seriesNo + "||" + categoryNo;
									}else if(isAds == 'Y'){
                                		optionval = data[i].category_id + "||" + data[i].album_id + "||" + type + "||" + data[i].series_yn + "||" + data[i].category_no;
                                	}else{
										optionval = data[i].category_id + "||" + data[i].album_id;
									}
	                                
									album_html[++a] = "<option value=\"" + optionval + "\">[" + data[i].category_name + "] " + data[i].album_name + "</option>\n";
                                 }
                            }
                            if (album_html.length != 0) {
                               	$("#contentList").html(album_html.join(''));
                            } else {
                            	clearAlbumList();
                            }
                            $.unblockUI();
                        },
                        "json"
                );
        	};
			
			$('#searchbtn').on('click', function(){
        		if($('#searchVal').val() == ''){
        			alert('검색어를 입력해주세요.');
        			return;
        		}else{
   					$.blockUI({
   						blockMsgClass: "ajax-loading",
   						showOverlay: true,
   						overlayCSS: { backgroundColor: '#CECDAD' } ,
   						css: { border: 'none' } ,
   						 message: "<b>로딩중..</b>"
   					});
        			searchBtnEvnt();
        		}
        	});
        	
        	$('#searchVal').on('keypress', function(e){
        		if(e.keyCode == '13'){
        			if($('#searchVal').val() == ''){
            			alert('검색어를 입력해주세요.');
            			return;
            		}else{
       					$.blockUI({
       						blockMsgClass: "ajax-loading",
       						showOverlay: true,
       						overlayCSS: { backgroundColor: '#CECDAD' } ,
       						css: { border: 'none' } ,
       						 message: "<b>로딩중..</b>"
       					});
            			searchBtnEvnt();
            		}
        		}
        	});
            
        	$("#choicebtn").click(function () {
                var choiceCts = "";
                var textHtml = "";
                
            	if($("#contentList") && $("#contentList").length) {
            		$.each($("#contentList option"), function(key) {
            			
            			console.log(key);
            			
            			var ch_value = $("#contentList option:eq("+key+")").attr("value");
            			var ch_text = $("#contentList option:eq("+key+")").text();
            			if(this.selected){
                			choiceCts = ch_value;
                			textHtml = ch_text;
            			}
            		});
            	}
            	var categoryYn = '${categoryYn}';
            	var categoryListYn = '${categoryListYn}';
            	if(categoryYn == 'Y' || categoryListYn == 'Y'){
            		if(choiceCts == ''){
            			alert('카테고리를 선택해주세요');
            			return;
            		}
            		
            		var category_select = $('select[name=category_select]');
            		var category_length = category_select.length;
            		var categoryFullPath = '';
            		
            		
            		
            		for(var i=0;i<category_length;i++){
            			var optionVal = $(category_select[i]).val();
            			if(optionVal != '---'){
            				if(i == 0){
            					categoryFullPath = optionVal;
            				}else{
            					categoryFullPath += ':'+optionVal;
            				}
            				
            	
                        	
            				
            			}
            		}
            		if(categoryFullPath == ''){
            			choiceCts = choiceCts.split("\|\|")[0];
            		}else if(categoryListYn == 'Y'){
            			choiceCts = categoryFullPath + '||' + choiceCts;
            		}else{
            			choiceCts = categoryFullPath + ':' + choiceCts.split("\|\|")[0];
            		}
            	}
            	console.log(textHtml);
            	console.log(choiceCts);
            	if(categoryListYn == 'Y'){
            		alert("popup result test : "+choiceCts);
            		alert("name : "+textHtml);
            	}
               	if("${hiddenName}"!="") $(opener.document).find("#${hiddenName}").val(choiceCts);
               	if("${textName}"!="") $(opener.document).find("#${textName}").val(textHtml);
               	if("${textHtml}"!="") $(opener.document).find("#${textHtml}").html(textHtml);
            	self.close();
            });

            $("#closebtn").click(function () {
                self.close();
            });
            
        })
        
		function clearAlbumList() {
        	$("#contentList").html([].join(''));
        }

        function type_select_bind() {
            $("select[name='category_type_select']").unbind("change");
            $("select[name='category_type_select']").bind("change", function () {
            	var categoryId = "${categoryId}";
                var category_type = $(this).val();
                var thissel = $("select[name='category_select']");
                
             	// 현재 select 태그의 부모인 td의 이후에 나오는 모든 td를 구한다
                var nexttd = $(thissel).parent().nextAll("td");
                var length = nexttd.length;
                for (var i = length - 1; i >= 0; i--) {
                    $(nexttd[i]).remove();
                }
                clearAlbumList();
                
                $.post("<%=webRoot%>/admin/commonMng/getCategoryList.do",
                        {categoryId: categoryId, type: category_type},
                        function (data) {
                        	$("select[name='category_type_select']").val(category_type);

                            // 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
                            if (data.length == 0) {
                            	$("select[name='category_select']").html('');
                            } else {
                                var category_select_html = '<option value="---">선택해주세요</option>';
                                for (var i = 0; i < data.length; i++) {
                                	category_select_html=category_select_html+"<option value="+data[i].category_id+">"+data[i].category_name+"</option>";
                                }
                              	$("select[name='category_select']").html(category_select_html);
                            }
                            type_select_bind();
                        },
                        "json"
                );
                		
            });
        }
        
        function select_bind() {
            $("select[name='category_select']").unbind("change");
            $("select[name='category_select']").bind("change", function () {
                var categoryId = $(this).val();
                var thissel = this;
                var series = "${series}";
                var type = "";
                var isTypeChange = "${isTypeChange}";
                var isAds = "${isAds}";
                
                if(isTypeChange == "Y"){
                	type = $("select[name='category_type_select']").val();
                }else{
                	type = "${type}";
                }

                // 현재 select 태그의 부모인 td의 이후에 나오는 모든 td를 구한다
                var nexttd = $(thissel).parent().nextAll("td");
                var length = nexttd.length;
                for (var i = length - 1; i >= 0; i--) {
                    $(nexttd[i]).remove();
                }

                if (categoryId == "---") {		// 선택해주세요를 선택한 경우
                	
                } else {
                    $.post("<%=webRoot%>/admin/commonMng/getCategoryAlbumList.do",
                            {categoryId: categoryId, type: type},
                            function (data) {
                                // 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
                                if (data.length == 0) {
                                    clearAlbumList();
                                } else {
                                    var html = [], category_html = [], album_html = [], h = -1, c = -1, a = -1;

                                    for (var i = 0; i < data.length; i++) {
                                    	var categoryYn = '${categoryYn}';
                                    	if(categoryYn == 'Y'){
                                    		if (data[i].mytype == "1" && data[i].sub_cnt > 0) {
                                                category_html[++c] = "<option value=\"" + data[i].category_id + "\">" + data[i].category_name + "</option>\n";
                                            	var optionval = "";
                                            	optionval = data[i].category_id + "||" + data[i].category_name;
                                                album_html[++a] = "<option value=\"" + optionval + "\">" + data[i].category_name + "</option>\n";
                                            }
                                    	}else{
                                    		if (data[i].mytype == "1") {
                                                category_html[++c] = "<option value=\"" + data[i].category_id + "\">" + data[i].category_name + "</option>\n";
                                            } else {
                                            	var optionval = "";
                                            	if(series=="Y"){
                                            		var seriesNo = "";
                                            		var categoryNo = "";
                                            		if("Y"==data[i].series_yn){
                                                		seriesNo = data[i].series_no;
                                                		categoryNo = data[i].category_no;
                                            		}
                                            		optionval = categoryId + "||" + data[i].album_id + "||" + seriesNo + "||" + categoryNo;
                                            	}else{
                                            		optionval = categoryId + "||" + data[i].album_id;
                                            	}
                                            	
                                            	if(isTypeChange == "Y"){
                                        			optionval += "||" + type;
                                        		}
                                            	
                                            	if(isAds == "Y"){
                                        			optionval += "||" + data[i].series_yn + "||" + data[i].category_no;
                                        		}
                                            		
                                                album_html[++a] = "<option value=\"" + optionval + "\">" + data[i].album_name + ' '+ data[i].series_no + "</option>\n";
                                            }
                                    	}
                                    }
                                    if (category_html.length != 0) {
                                        html[++h] = "<td name=\"category_td\" valign=\"top\">\n";
                                        html[++h] = "<select name=\"category_select\">\n";
                                        html[++h] = "<option value=\"---\" selected>선택해주세요</option>\n";
                                        html[++h] = category_html.join('');
                                        html[++h] = "</select>\n";
                                        html[++h] = "</td>\n";
                                    }

                                    if (album_html.length != 0) {
                                    	$("#contentList").html(album_html.join(''));
                                    } else {
                                    	clearAlbumList();
                                    }

                                    $("#category_tr").append(html.join(''));
                                }
                                select_bind();

                            },
                            "json"
                    );
                }
            });
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
                    <td class="bold">카테고리 선택</td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    </tbody>
</table>
<table class="sidebarMain">
	<tr class="sidebarMain">
		<td></td>
		<td>
			<select id="selectType" name="selectType">
				<option value="0">선택형</option>
				<option value="1" selected="selected">검색형</option>
			</select>
		</td>
	</tr>
</table>
<table class="sidebarMain typeA" style="display:none">
    <tr id="category_tr">
    	<td name="category_type" valign="top">
    		<select name="category_type_select" <c:if test="${isTypeChange eq 'N' }">disabled</c:if>>
				<option value="I20" <c:if test="${type eq 'I20'}">selected</c:if>>I20</option>
				<option value="I30" <c:if test="${type eq 'I30'}">selected</c:if>>I30</option>
			</select>
    	</td>
        <td name="category_td" valign="top">
            <c:choose>
                <c:when test="${categoryResult==null || fn:length(categoryResult) == 0}">
                    <div>검색된 카테고리가 없습니다</div>
                </c:when>
                <c:otherwise>
                    <select name="category_select">
                        <option value="---">선택해주세요</option>
                        <c:forEach var="item" items="${categoryResult}" varStatus="status">
                        	<c:choose>
                        		<c:when test="${not empty categoryList}">
                        			<c:forEach var="categoryList" items="${categoryList }">
	                        			<c:if test="${item.category_id eq categoryList }">
	                        				<option value="${item.category_id}">${item.category_name}</option>
	                        			</c:if>
	                        		</c:forEach>
                        		</c:when>
                        		<c:otherwise>
                        			<option value="${item.category_id}">${item.category_name}</option>
                        		</c:otherwise>
                        	</c:choose>
                        </c:forEach>
                    </select>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
</table>
<table class="sidebarMain typeB">
	<tr>
		<td name="category_type" valign="top">
    		<select name="category_type_select" <c:if test="${isTypeChange eq 'N' }">disabled</c:if>>
				<option value="I20" <c:if test="${type eq 'I20'}">selected</c:if>>I20</option>
				<option value="I30" <c:if test="${type eq 'I30'}">selected</c:if>>I30</option>
			</select>
    	</td>
		<td valign="top" width="430">
			<select id="searchType" name="searchType">
				<option value="A">앨범명</option>
				<option value="I">앨범ID</option>
				<option value="C">카테고리명</option>
			</select>
			<input type="text" id="searchVal" name="searchVal" style="width:250px">
			<span class="button small blue" id="searchbtn">검색</span>
		</td>
	</tr>
</table>
<table>
    <tr>
        <td valign="top" width="304">
			<select id="contentList" name="contentList" size="10" style="width:500px;">
				<c:if test="${categoryYn eq 'Y' }">
					<c:forEach var="item" items="${categoryResult}" varStatus="status">
						<c:if test="${item.sub_cnt > 0 }">
                    		<option value="${item.category_id}">${item.category_name}</option>
                    	</c:if>
                    </c:forEach>
                </c:if>
			</select>
        </td>
    </tr>
</table>
</select>
<table border="0" cellpadding="0" cellspacing="0" width="100%" align="left">
    <tbody>
    <tr>
        <td height="40" align="left">
            <span class="button small blue" id="choicebtn">선택</span>
            <span class="button small blue" id="closebtn">닫기</span>
        </td>
    </tr>
    </tbody>
</table>
</body>
</html>