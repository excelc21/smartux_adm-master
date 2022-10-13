<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>LG U+ IPTV SmartUX</title>
    <link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
    <jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
    <script type="text/javascript">
        var rowGbn = "\f^";
        var colsGbn = "\b^";

        $(document).ready(function () {
        	type_select_bind();
            select_bind();
            already_content();

            $("#choicebtn").click(function () {
                var choiceCts = "";
                var textHtml = "";
            	if($("#choiceContentList") && $("#choiceContentList").length) {
            		var cc = 0;
            		$.each($("#choiceContentList option"), function(key) {
            			var ch_value = $("#choiceContentList option:eq("+key+")").attr("value");
            			var ch_text = $("#choiceContentList option:eq("+key+")").text();
            			var ch_valueArr = ch_value.split(colsGbn);
            			
            			if(ch_valueArr.length==2) {
            				if(cc==0){
                    			choiceCts = ch_valueArr[1] + colsGbn + ch_text+ colsGbn +ch_valueArr[0];
                    			textHtml = ch_text
                			}else{
                    			choiceCts = choiceCts + rowGbn + ch_valueArr[1] + colsGbn + ch_text+ colsGbn +ch_valueArr[0];
                    			textHtml = textHtml+"<br>"+ch_text;
                			}
                			cc++;
            			}
            		});
            	}
            	$(opener.document).find("#ev_detail_cont").val(choiceCts);
            	$(opener.document).find("#div_choiceContents").html(textHtml);
            	self.close();
            });

            $("#closebtn").click(function () {
                self.close();
            });
            
            $("#addbtn").click(function(){
            	var choiceCnt = $("#contentList option:selected").size();
        		var choiceSize = $("#choiceContentList option").size();
        		
        		if(choiceCnt <= 0){			// 선택한 항목이 없으면
        			alert("추가하고자 하는 앨범을 선택해주세요");
        		//}else if((choiceSize+choiceCnt) >= 20){
        		//	alert("20개 이상 추가할 수 없습니다.");
        		}else{
                	for( var i=0; i<$('#contentList option').size(); i++){
						if( $("#contentList option:eq("+i+")").attr("selected")){
	                  		// 선택한 항목이 있으면
	              			var seloption = $("#contentList option").eq(i);
	              			var seltext = $(seloption).text();
	              			var selval = $("select[name='category_type_select']").val()+colsGbn+$(seloption).val();
	              			
	              			// 추가하고자 하는 것이 기존에 있는지를 확인한다
	              			var size = $("#choiceContentList option").size();
	              			var isexist = false;
	              			if(size != 0){
	              				$("#choiceContentList option").each(function(){
	              					if($(this).val() == selval){
	              						isexist = true;
	              					}
	              				});
	              			}
	              			
	              			if(isexist == true){
	              				//alert("추가하고자 하는 앨범이 이미 설정되어 있습니다");
	              			}else{
	              				var html = "<option value=\"" + selval + "\">" + seltext + "</option>\n";
	              				$("#choiceContentList").append(html);
	              			}
						}
                  	}
        		}
        	});
        	
        	$("#delbtn").click(function(){
        		//var idx = $("#choiceContentList option").index($("#choiceContentList option:selected"));
            	var choiceCnt = $("#choiceContentList option:selected").size();
        		
        		if(choiceCnt <= 0){			// 선택한 항목이 없으면
        			alert("삭제하고자 하는 앨범을 선택해주세요");
        		}else{					// 선택한 항목이 있으면
        			$("#choiceContentList option:selected").remove();
                	//for( var i=0; i<$('#choiceContentList option').size(); i++){
					//	if( $("#choiceContentList option:eq("+i+")").attr("selected")){
		        	//		$("#choiceContentList option").eq(i).remove();
					//	}
                	//}
        		}
        	});
        	$("#upbtn").click(function(){
            	var choiceCnt = $("#choiceContentList option:selected").size();
            	if(choiceCnt > 1){
        			alert("순서변경은 하나씩 선택해서 변경해 주세요.");
        		}else{
	        		var idx = $("#choiceContentList option").index($("#choiceContentList option:selected"));
	        		var size = $("#choiceContentList option").size();
	        		if(idx == -1){			// 선택한 항목이 없으면
	        			alert("순서를 바꾸고자 하는 항목을 선택해주세요");
	        		}else{					// 선택한 항목이 있으면
	        			fnSortCateogry("choiceContentList", "U");
	        		}
        		}
        	});
        	
        	$("#downbtn").click(function(){
            	var choiceCnt = $("#choiceContentList option:selected").size();
            	if(choiceCnt > 1){
        			alert("순서변경은 하나씩 선택해서 변경해 주세요.");
        		}else{
	        		var idx = $("#choiceContentList option").index($("#choiceContentList option:selected"));
	        		var size = $("#choiceContentList option").size();
	        		if(idx == -1){			// 선택한 항목이 없으면
	        			alert("순서를 바꾸고자 하는 항목을 선택해주세요");
	        		}else{					// 선택한 항목이 있으면
	        			fnSortCateogry("choiceContentList", "D");
	        		}
        		}
        	});
            
        });
        
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
				var cate_type = $("select[name='category_type_select']").val(); 
                
                // 현재 select 태그의 부모인 td의 이후에 나오는 모든 td를 구한다
                var nexttd = $(thissel).parent().nextAll("td");
                var length = nexttd.length;
                for (var i = length - 1; i >= 0; i--) {
                    $(nexttd[i]).remove();
                }

                if (categoryId == "---") {		// 선택해주세요를 선택한 경우
                	
                } else {
                    $.post("<%=webRoot%>/admin/commonMng/getCategoryAlbumList.do",
                            {categoryId: categoryId, type: cate_type},
                            function (data) {
                                // 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
                                if (data.length == 0) {
                                    clearAlbumList();
                                } else {
                                    var html = [], category_html = [], album_html = [], h = -1, c = -1, a = -1;

                                    for (var i = 0; i < data.length; i++) {
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
                                        	
                                            album_html[++a] = "<option value=\"" + optionval + "\">" + data[i].album_name + "</option>\n";
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
		
        function already_content(){
            //var rowGbn_Sp = "\f^";
            //var colsGbn_Sp = "\c^";
        	alreadyCnt = $(opener.document).find("#ev_detail_cont").val();
        	var choiceCnt_html = [];
        	var cntArr = alreadyCnt.split(rowGbn);
        	for(var i=0;cntArr.length > i;i++){
            	if(cntArr[i]!=null && cntArr[i]!=""){
            		var subArr = cntArr[i].split(colsGbn);
            		
            		if(subArr.length==2){
            			choiceCnt_html[i] = "<option value=\"" + "I20"+colsGbn+subArr[0]+ "\">" + subArr[1] + "</option>\n";
            		}else if(subArr.length==3){
            			choiceCnt_html[i] = "<option value=\"" + subArr[2]+colsGbn+subArr[0]+ "\">" + subArr[1] + "</option>\n";
            		}
            	}
        	}
        	$("#choiceContentList").html(choiceCnt_html.join(''));
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
                            <option value="${item.category_id}">${item.category_name}</option>
                        </c:forEach>
                    </select>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
</table>
<table>
    <tr>
        <td valign="top" width="304">
			<select id="contentList" name="contentList" size="10" style="width:300px;" multiple="multiple"></select>
        </td>
        <td width="70" align="center">
			<span class="button small blue" id="addbtn">추가</span><br/><br/>
			<span class="button small blue" id="delbtn">삭제</span>	
        </td>
        <td valign="top" width="304">
			<select id="choiceContentList" name="choiceContentList" size="10" style="width:300px;" max="20" multiple="multiple"></select>
        </td>
        <td width="70" align="center">
			<span class="button small blue" id="upbtn">위로</span><br/><br/>
			<span class="button small blue" id="downbtn">아래로</span>	
        </td>
    </tr>
</table>
</select>
<input type="hidden" id="albumElementId" name="albumElementId" value="${albumElementId}"/>
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