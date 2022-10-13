<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>MIMS 카테고리 선택</title>
    <link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
    <jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include>
    <script type="text/javascript">
        var td_idx = 2;

        var callbak = "";
        $(document).ready(function () {
        	callbak = '${param.callbak}';
            select_bind();

            $("#closebtn").click(function () {
                self.close();
            });
            
            $("#sltBtn").click(function(){
        		var setVal = "";     // 선택된 카테고리 aaaa:dddd:....
        		var setCateId =""; // 마지막 선택한 카테고리  
        		var setCateNm ="";// 마지막 선택한 카테고리명
        		var setCateExSub=false;
        		        		
        		var categorysels = $("select[name='category_select']");
        		var category_length = categorysels.length;
        		
        		//수정 start 2016.01.27 카테고리count, 앨범count추가
        		var subCate="";        			   
        		var subContents="";
        		//수정 end 2016.01.27 카테고리count, 앨범count추가
        		
        		for(var i = 0; i < category_length; i++){
        			var seltext = $(categorysels[i]).children("option:selected").text();
        			var optionvalay = $(categorysels[i]).val();
        			
        			//수정 start 2016.01.27 카테고리count, 앨범count추가
        			var ay = optionvalay.split("|");
        			var optionval = ay[0];
        			//수정 end 2016.01.27 카테고리count, 앨범count추가
        			
        			//var optionval = optionvalay.split('|')[0];
        			if(i == 0){
        				setVal = optionval;	
        			}else{
        				setVal += ":"+optionval;
        			} 
        			if(optionval != "---"){
        				setCateId=optionval;
        				setCateNm = seltext;
        				
        				//수정 start 2016.01.27 카테고리count, 앨범count추가
        				subCate=ay[2];          			   
            			subContents=ay[3]; 
            			//수정 start 2016.01.27 카테고리count, 앨범count추가
        			
        			}
        		}
        		if(setVal == "---"){
        			alert("카테고리를 선택해 주세요");
        			return;
        		}else{
        			if(setVal.indexOf(":---") != -1){
        				if('${param.catetype}'=='1'){ 
        					alert('카테고리를 선택해 주세요');       
        					return;
        				}       
        				setVal = setVal.replace(":---","");
        			}
        		}
        		
        		if(setVal==''){        			
        			alert('카테고리를 선택해 주세요');
        			return false;
        		}
        		
        		var dataobj = new Object(); 
        		dataobj.cate =setCateId;
        		dataobj.catenm =setCateNm;
        		
        		//수정 start 2016.01.27 카테고리count, 앨범count추가
        		dataobj.cateCnt =subCate;   
        		dataobj.contentsCnt =subContents;   
        		//수정 end 2016.01.27 카테고리count, 앨범count추가
        		
        		var callbak_m = eval("opener."+callbak);    
        		if('${param.catetype}'=='2'){ // 카테고리 data 선택일시 선택된 카테고리의 하위에 카테고리가 존재하는지 검사한다.        	       			
	           		 $.get('<%=webRoot%>/admin/commonMng/getMimsIptvCategorySubYn.do?categoryId='+setCateId
	   				 ,''
	   			     ,function(data){
	           			 if(data=='Y'){
	           				callbak_m(dataobj);   	
	           				self.close();
	           			 }else{
	           				 alert('하위에 카테고리로 구성된 카테고리가 아닙니다.');   
	           			 }
	   			     });
        		}else{
        			callbak_m(dataobj);   
        			self.close();
        		} 
        	});
            
        })

        function clearCategoryAlbumList() {
           // var albumElementId = $("#albumElementId").val();
           // var openerelement = window.opener.jQuery("#" + albumElementId);
            //openerelement.html([].join(''));
        }
        
        function select_bind() {
            $("select[name='category_select']").unbind("change");

            $("select[name='category_select']").bind("change", function () {
                var cateinfo = $(this).val();
                                
                var categoryId = cateinfo.split('|')[0];
                var thissel = this;

                // 현재 select 태그의 부모인 td의 이후에 나오는 모든 td를 구한다
                var nexttd = $(thissel).parent().nextAll("td");
                var length = nexttd.length;
                for (var i = length - 1; i >= 0; i--) {
                    $(nexttd[i]).remove();
                }

                if (categoryId == "---") {		// 선택해주세요를 선택한 경우

                } else {
                	
                    $.post("<%=webRoot%>/admin/commonMng/getMimsIptvCategoryListPop.do",
                            {categoryId: categoryId,
                    	contentType:'${param.contentType}'},
                            function (data) {
                                // 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
                                if (data.length == 0) {
                                    clearCategoryAlbumList();
                                } else {
                                    var html = [], category_html = [], album_html = [], h = -1, c = -1, a = -1;

                                    for (var i = 0; i < data.length; i++) {
                                        if (data[i].MYTYPE == "1") {
                                            category_html[++c] = "<option value=\"" + data[i].CATE_INFO + "|"+data[i].SUB_CATE+"|"+data[i].SUB_CONTENTS+ "\">" + data[i].CATEGORY_NAME + "</option>\n";
                                        } else {
                                            var optionval = categoryId + "|" + data[i].ALBUM_ID;
                                            //album_html[++a] = "<option value=\"" + optionval + "\">" + data[i].albumName + "</option>\n";
                                        }
                                    }

                                    if (category_html.length != 0) {
                                        html[++h] = "<td name=\"category_td\" valign=\"top\">\n";
                                        html[++h] = "<select name=\"category_select\" style=\"font-size:9pt\">\n";
                                        html[++h] = "<option value=\"---\" selected>선택해주세요</option>\n";
                                        html[++h] = category_html.join('');
                                        html[++h] = "</select>\n";
                                        html[++h] = "</td>\n";
                                    }

                                    if (album_html.length != 0) {
                                        var albumElementId = $("#albumElementId").val();
                                    } else {
                                        clearCategoryAlbumList();
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
                    <td class="bold">MIMS 카테고리 선택</td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    </tbody>
</table>
<table class="sidebarMain">
    <tr id="category_tr">
        <td name="category_td" valign="top">
            <c:choose>
                <c:when test="${categoryResult==null || fn:length(categoryResult) == 0}">
                    <div>검색된 카테고리가 없습니다</div>
                </c:when>
                <c:otherwise>
                    <select name="category_select" style="font-size:9pt">
                        <option value="---">선택해주세요</option>
                        <c:forEach var="item" items="${categoryResult}" varStatus="status">
                            <option value="${item.CATE_INFO}|${item.SUB_CATE}|${item.SUB_CONTENTS}">${item.CATEGORY_NAME}</option>
                        </c:forEach>
                    </select>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
</table>
<input type="hidden" id="albumElementId" name="albumElementId" value="${albumElementId}"/>
<table border="0" cellpadding="0" cellspacing="0" width="100%" align="left">
    <tbody>
    <tr>
        <td height="40" align="left">
        	&nbsp;&nbsp;&nbsp;<span class="button small blue" id="sltBtn">설정</span>
			<span class="button small blue" id="closebtn">닫기</span>
        </td>
    </tr>
    </tbody>
</table>
</body>
</html>