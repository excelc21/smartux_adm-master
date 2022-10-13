<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>LG U+ IPTV SmartUX </title>
<link href="/smartux_adm/css/basic_style.css" rel="stylesheet" type="text/css">
<jsp:include page="/WEB-INF/views/include/js.jsp"></jsp:include> 
<script type="text/javascript">
var td_idx = 1;
var panel_id ='';
var result2 ='';

var depthIndex ='0';

$(document).ready(function(){
	type_select_bind();
	
	
	$("#category_select").change(function(){
	    panel_id =  $(this).val();
	    var p_title_id = 'none';
	    var Ca = /\+/g;
	    td_idx =1;
	    
	    $("td[name^='category_td']").remove();
	
	    	
	    $.post("<%=webRoot%>/admin/mainpanel/getPanelTitleTempListPopup.do",
	            {panel_id: panel_id , p_title_id: p_title_id},
                function (data) {
             
                    // 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
                    if (data.length == 0) {
                  //  	$("select[name='select_area']").html('');
                   // 	$("select[name='select_area']").css("display","none");
                    } else {
                    	 var category_select_html ='<td name="category_td'+td_idx+'"><select id="category_td'+td_idx+'" name="category_type_select">';
		            	category_select_html = category_select_html +'<option value="">선택해주세요</option>';           
                        for (var i = 0; i < data.length; i++) {
                        	category_select_html=category_select_html+"<option value="+data[i].title_id+">"+data[i].title_nm+"</option>";
                        }
                        category_select_html = category_select_html +'</select>';
                        
                        $("#category_tr").append(category_select_html);
                      	
                      	td_idx = td_idx+1;
                    }type_select_bind();       
                    
                },
                "json"
        );	    
	    	    
	})
	

	
})


function type_select_bind() {
            $("select[name='category_type_select']").unbind("change");
            $("select[name='category_type_select']").bind("change", function () {
            	
        	  
        	   var p_title_id = $(this).val();	
               var id = $(this).attr('id');
        	   var d_position = id.indexOf("d");
        	   var index = id.substring(d_position+1);
        	   var temp = Number(index)+1;
        	 
        	  
        	  for(i=temp; i<td_idx; i++){
        		   $("td[name='category_td"+i+"']").remove();  		   
        	   }
        	   
        	  
        	   
        	   
        	  $.post("<%=webRoot%>/admin/mainpanel/getPanelTitleTempListPopup.do",
                        {panel_id: panel_id , p_title_id: p_title_id},
                 
                        function (data) {
                     
                            // 결과 코드를 받아 결과 코드에 따라 메시지를 보여준다
                            if (data.length == 0) {
                          //  	$("select[name='select_area']").html('');
                           // 	$("select[name='select_area']").css("display","none");
                            } else {
                            	 var category_select_html ='<td name="category_td'+temp+'"><select id="category_td'+temp+'" name="category_type_select">';
        		            	category_select_html = category_select_html +'<option value="">선택해주세요</option>';           
                                for (var i = 0; i < data.length; i++) {
                                	category_select_html=category_select_html+"<option value="+data[i].title_id+">"+data[i].title_nm+"</option>";
                                }
                                category_select_html = category_select_html +'</select></td>';
                                
                                $("#category_tr").append(category_select_html);
                              	
                              	td_idx = td_idx+1;
                            }type_select_bind();     
                            
                        },
                        "json"
              );		
            	

    });
}

function send_msg(){
	
	var panel = $("select[name='category_select']").val();
	var lenght =  $("select[name='category_type_select']").length;
	var temp ='';
	
	temp = panel +'|';
	
	for(i=1; i<=lenght; i++){
		
		if(i == lenght){
			temp = temp + $("#category_td"+i).val();
		} else {
			temp = temp + $("#category_td"+i).val() + ':';
		}
	}
	
	alert('output : ' + temp);
	
	opener.document.getElementById("linkUrl").value = temp;
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
                   
                    <td class="bold" style="text-align: center;">패널 지면 선택</td>
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
 
    	</td>
		<td name="main_category_td" valign="top">
			<c:choose>
				<c:when test="${result==null || fn:length(result) == 0}">
					<div>검색된 카테고리가 없습니다</div>
				</c:when>
				<c:otherwise>
					<select id="category_select" name="category_select">
						<option value="00">선택해주세요</option>
					<c:forEach var="item" items="${result}" varStatus="status">
						<option value="${item.pannel_id}">${item.pannel_nm}</option>
					</c:forEach>
					</select>
				</c:otherwise>
			</c:choose>
		</td>

	</tr>
</table>
<input type="hidden" id="albumElementid" name="albumElementid" value="" />
<input type="hidden" id="category_level" name="category_level" value="${category_level}" />
<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
<tbody>
    <tr>
        <td height="40" align="center">
        	<a href="#" onclick="send_msg()"><span class="button small blue" id="setPageCategoryBtn">설정</span></a>
			
    	</td>
    </tr>
	</tbody>
</table>
</body>
</html>