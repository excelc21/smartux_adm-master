var draggedEventIsAllDay;
var activeInactiveWeekends = true;

var calendar = $('#calendar').fullCalendar({

	/** ******************
	 *  OPTIONS
	 * *******************/
    locale                    : 'ko',    
    timezone                  : "local", 
    nextDayThreshold          : "09:00:00",
    allDaySlot                : false,
    displayEventTime          : true,
    displayEventEnd           : true,
    firstDay                  : 0, //월요일이 먼저 오게 하려면 1
    weekNumbers               : false,
    selectable                : true,
    weekNumberCalculation     : "ISO",
    defaultView: view,
    eventLimit                : true,
    views                     : { 
                                month : { eventLimit : 12 } // 한 날짜에 최대 이벤트 12개, 나머지는 + 처리됨
                              },
    eventLimitClick           : 'week', //popover
    navLinks                  : true,
  /*defaultDate               : moment('2019-05'), //실제 사용시 현재 날짜로 수정*/  
    timeFormat                : 'HH:mm',
    defaultTimedEventDuration : '01:00:00',
    editable                  : true,
    minTime                   : '00:00:00',
    maxTime                   : '24:00:00',
    slotLabelFormat           : 'HH:mm',
    weekends                  : true,
    nowIndicator              : true,
    dayPopoverFormat          : 'MM/DD dddd',
    longPressDelay            : 0,
    eventLongPressDelay       : 0,
    selectLongPressDelay      : 0,  
    header                    : {
                                left   : 'today, prevYear, nextYear, viewWeekends',
                                center : 'prev, title, next',
                                right  : 'month, agendaWeek, agendaDay, listWeek, addGuide, apply'
                              },
    views                     : {
                                month : {
                                  columnFormat : 'dddd'
                                },
                                agendaWeek : {
                                  columnFormat : 'M/D ddd',
                                  titleFormat  : 'YYYY년 M월 D일',
                                  eventLimit   : false
                                },
                                agendaDay : {
                                  columnFormat : 'dddd',
                                  eventLimit   : false
                                },
                                listWeek : {
                                  columnFormat : ''
                                }
                              },
    customButtons             : { //주말 숨기기 & 보이기 버튼
                                viewWeekends : {
                                  text  : '주말',
                                  click : function () {
                                    activeInactiveWeekends ? activeInactiveWeekends = false : activeInactiveWeekends = true;
                                    $('#calendar').fullCalendar('option', { 
                                      weekends: activeInactiveWeekends
                                    });
                                  }
                                },
                                addGuide : {
                                    text  : '일정등록',
                                    click : function () {
                                      location.href = "./insertGuideLink.do";
                                    }
                                  },
                                  apply : {
                                      text  : '즉시적용',
                                      click : function () {
                                   	   apply();
                                      }
                                    },
                               },
                               


    eventRender: function (event, element, view) {

    //일정에 hover시 요약
    /*element.popover({
      title: $('<div />', {
        class: 'popoverTitleCalendar',
        text: event.title
      }).css({
        'background': event.backgroundColor,
        'color': event.textColor
      }),
      content: $('<div />', {
          class: 'popoverInfoCalendar'
        }).append('<p><strong>등록자:</strong> ' + event.username + '</p>')
        .append('<p><strong>구분:</strong> ' + event.type + '</p>')
        .append('<p><strong>시간:</strong> ' + getDisplayEventDate(event) + '</p>')
        .append('<div class="popoverDescCalendar"><strong>설명:</strong> ' + event.description + '</div>'),
      delay: {
        show: "800",
        hide: "50"
      },
      trigger: 'hover',
      placement: 'top',
      html: true,
      container: 'body'
    });

    return filtering(event);*/

    },

    /* ****************
     *  일정 받아옴 
     * ************** */
	events: function (start, end, timezone, callback) {
		var serviceIdArr = $('#type_filter').val();
		var check_arr = Array.isArray(serviceIdArr);
		var serviceId_str = "";
		if(!check_arr){
			serviceId_str = serviceIdArr;
		}else{
			serviceId_str = serviceIdArr.join();
		}
		
	    $.ajax({
	    	type: "post",
	    	url: "getGuideLinkList",
	    	data: {
	    		serviceId : serviceId_str,
	    		sDate : moment(start).format('YYYY-MM-DD'),
	    		eDate   : moment(end).format('YYYY-MM-DD')
	    	},
	    	success: function (response) {
	    		response = JSON.parse(response);
	    		for(var i = 0 ; i < response.length; i++){
	    			var startdate = new Date(response[i].sDate); 
	    			var startiso = startdate.toISOString();
	    			response[i].start = startiso;
	    		  
	    			var enddate = new Date(response[i].eDate); 
	    			var endiso = enddate.toISOString();
	    			response[i].end = endiso;
	    		  
	    			response[i].textColor = "#ffffff";
	    		  
	    			if(response[i].useYn == "N"){
	    				response[i].backgroundColor = "#495057";
	    			}else if(response[i].linkType == "3"){
	    				response[i].backgroundColor = "#f06595";
	    			}else if(response[i].linkType == "4"){
	    				response[i].backgroundColor = "#9775fa";
	    			}
	    		}
	    		callback(response);
	    	}    
        });
	},

	eventAfterAllRender: function (view) {
		if (view.name == "month") $(".fc-content").css('height', 'auto');
	},

	//일정 리사이즈
	eventResize: function (event, delta, revertFunc, jsEvent, ui, view) {
		var newDates = calDateWhenResize(event);

		$.ajax({
			type: "post",
			url: "updateGuideTime",
			data: {
				seq : event.seq,
				serviceId : event.serviceId,
				sDate : newDates.startDate,
				eDate : newDates.endDate,
			},
			success: function (response) {
				response = JSON.parse(response);
				console.log(response);
				if(response.flag == "0000"){
					alert("가이드채널정보가 수정되었습니다.\n"+newDates.startDate+" ~ "+newDates.endDate);
				}else if(response.flag == "5555"){
					alert("이미 동일시간에 가이드채널이 있습니다.");
					revertFunc();
				}
			}
		});
	},

	eventDragStart: function (event, jsEvent, ui, view) {
		draggedEventIsAllDay = event.allDay;
	},

	//일정 드래그앤드롭
	eventDrop: function (event, delta, revertFunc, jsEvent, ui, view) {
		var newDates = calDateWhenResize(event);

	    $.ajax({
	    	type: "post",
	    	url: "updateGuideTime",
	    	data: {
	    		seq : event.seq,
	    		serviceId : event.serviceId,
	    		sDate : newDates.startDate,
	    		eDate : newDates.endDate,
	    	},
	    	success: function (response) {
	    		response = JSON.parse(response);
	    		console.log(response);
	    		if(response.flag == "0000"){
	    			alert("가이드채널정보가 수정되었습니다.\n"+newDates.startDate+" ~ "+newDates.endDate);
	    		}else if(response.flag == "5555"){
	    			alert("이미 동일시간에 가이드채널이 있습니다.");
	    			revertFunc();
	    		}
	    	}
	    });
	},

  select: function (startDate, endDate, jsEvent, view) {

    $(".fc-body").unbind('click');
    $(".fc-body").on('click', 'td', function (e) {

      $("#contextMenu")
        .addClass("contextOpened")
        .css({
          display: "block",
          left: e.pageX,
          top: e.pageY
        });
      return false;
    });

    var today = moment();

    if (view.name == "month") {
      startDate.set({
        hours: today.hours(),
        minute: today.minutes()
      });
      startDate = moment(startDate).format('YYYY-MM-DD HH:mm');
      endDate = moment(endDate).subtract(1, 'days');

      endDate.set({
        hours: today.hours() + 1,
        minute: today.minutes()
      });
      endDate = moment(endDate).format('YYYY-MM-DD HH:mm');
    } else {
      startDate = moment(startDate).format('YYYY-MM-DD HH:mm');
      endDate = moment(endDate).format('YYYY-MM-DD HH:mm');
    }

    //날짜 클릭시 카테고리 선택메뉴
    var $contextMenu = $("#contextMenu");
    $contextMenu.on("click", "a", function (e) {
      e.preventDefault();

      //닫기 버튼이 아닐때
      if ($(this).data().role !== 'close') {
        newEvent(startDate, endDate, $(this).html());
      }

      $contextMenu.removeClass("contextOpened");
      $contextMenu.hide();
    });

    $('body').on('click', function () {
      $contextMenu.removeClass("contextOpened");
      $contextMenu.hide();
    });

  },

  	//이벤트 클릭시 수정이벤트
  	eventClick: function (event, jsEvent, view) {
  		editEvent(event);
  	}

});


function getDisplayEventDate(event) {

	var displayEventDate;

	if (event.allDay == false) {
		var startTimeEventInfo = moment(event.start).format('HH:mm');
		var endTimeEventInfo = moment(event.end).format('HH:mm');
		displayEventDate = startTimeEventInfo + " - " + endTimeEventInfo;
	} else {
		displayEventDate = "하루종일";
	}

	return displayEventDate;
}


function calDateWhenResize(event) {

	var newDates = {
			startDate: '',
			endDate: ''
	};

	if (event.allDay) {
		newDates.startDate = moment(event.start._d).format('YYYY-MM-DD');
		newDates.endDate = moment(event.end._d).subtract(1, 'days').format('YYYY-MM-DD');
	} else {
		newDates.startDate = moment(event.start._d).format('YYYY-MM-DD HH:mm:ss');
		newDates.endDate = moment(event.end._d).format('YYYY-MM-DD HH:mm:ss');
	}

	console.log(newDates)
	return newDates;
}

function calDateWhenDragnDrop(event) {
	// 드랍시 수정된 날짜반영
	var newDates = {
			startDate: '',
			endDate: ''
	}

	// 날짜 & 시간이 모두 같은 경우
	if(!event.end) {
		event.end = event.start;
	}

	//하루짜리 all day
	if (event.allDay && event.end === event.start) {
		console.log('1111')
		newDates.startDate = moment(event.start._d).format('YYYY-MM-DD');
		newDates.endDate = newDates.startDate;
	}

	//2일이상 all day
	else if (event.allDay && event.end !== null) {
		newDates.startDate = moment(event.start._d).format('YYYY-MM-DD');
		newDates.endDate = moment(event.end._d).subtract(1, 'days').format('YYYY-MM-DD');
	}

	//all day가 아님
	else if (!event.allDay) {
		newDates.startDate = moment(event.start._d).format('YYYY-MM-DD HH:mm:ss');
		newDates.endDate = moment(event.end._d).format('YYYY-MM-DD HH:mm:ss');
	}

	return newDates;
}