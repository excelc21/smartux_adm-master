/* ****************
 *  일정 편집
 * ************** */
var editEvent = function (event, element, view) {
	var seq = event.seq;
	var type_filter = $('#type_filter').val();
	var view = $('#calendar').fullCalendar('getView');
	view = view.name;
	console.log(view)
	
	var type_filter_str = "";
	var check_arr = Array.isArray(type_filter);
	if(!check_arr){
		type_filter_str = "";
	}else{
		type_filter_str = type_filter.join();
	}
	
	
	location.href = "./updateGuideLink.do?seq="+seq+"&type_filter_str="+type_filter_str+"&view="+view;
};

// 삭제버튼
$('#deleteEvent').on('click', function () {
    
    $('#deleteEvent').unbind();
    $("#calendar").fullCalendar('removeEvents', $(this).data('id'));
    eventModal.modal('hide');

    //삭제시
    $.ajax({
        type: "get",
        url: "",
        data: {
            //...
        },
        success: function (response) {
            alert('삭제되었습니다.');
        }
    });

});