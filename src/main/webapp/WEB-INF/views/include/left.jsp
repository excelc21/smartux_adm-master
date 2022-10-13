<table border="0" cellpadding="0" cellspacing="0" width="180">
	<tbody>
	<tr>
		<td><img src="/smartux_adm/images/admin/m_title_04.gif" height="45" width="180"></td>
	</tr>
	<tr>
		<td height="5"></td>
	</tr>
	<tr>
		<td valign="top">
			<script type="text/javascript">
$(function(){
				
				var auth_decrypt = "${auth_decrypt}";
				
				//if(location.pathname.indexOf("admin/login") < 0){
					$.ajax({
					    url: '/smartux_adm/admin/login/menuchk.do',
					    type: 'POST',
					    dataType: 'json',
					    data: {
					        "strArr": location.pathname  
					    },
					    success: function(rs){
					    	var length = rs.length;
						    
					    	if(length > 0){
						    	$.ajax({
								    url: '/smartux_adm/admin/login/menuchk.do',
								    type: 'POST',
								    dataType: 'json',
								    data: {
								        "auth_decrypt": auth_decrypt , "strArr": location.pathname  
								    },
								    success: function(rs){
								    	var length = rs.length;
								    
								    	if(length < 1){
								    		alert("메뉴 권한이 없습니다.");
								    		if(auth_decrypt == "02"){
								    			location.href = "/smartux_adm/admin/startup/getEmergency.do?scr_tp=T&display_type=S";
								    		}else if(auth_decrypt == "00" || auth_decrypt == "01"){
								    			location.href = "/smartux_adm/admin/login/list.do";
								    		}else if(auth_decrypt == "03"){
								    			location.href = "/smartux_adm/admin/gpack/pack/getTvReplayPackView.do";
								    		}else if(auth_decrypt == "04"){
								    			location.href = "/smartux_adm/admin/starrating/getStarRatingList.do?system_gb=2";
								    		}
								    		return false;
								    	}
								    	
								    }, 
								    error: function(){
								    	alert("작업 중 오류가 발생하였습니다.");
								 		$.unblockUI();
								    }
								});
					    	}
					    }, 
					    error: function(){
					    	alert("작업 중 오류가 발생하였습니다.");
					 		$.unblockUI();
					    }
					});
			//	}
				
				$('.file a').click(function(){
					var clickloc = $(this).attr("href");
					var strArr = clickloc.split('?');
					
					$.ajax({
					    url: '/smartux_adm/admin/login/menuchk.do',
					    type: 'POST',
					    dataType: 'json',
					    data: {
					        "auth_decrypt": auth_decrypt , "strArr": strArr[0]   
					    },
					    success: function(rs){
					    	var length = rs.length;
					    	
					    	if(length < 1){
					    		alert("메뉴 권한이 없습니다.");
					    		if(auth_decrypt == "02"){
					    			location.href = "/smartux_adm/admin/startup/getEmergency.do?scr_tp=T&display_type=S";
					    		}else if(auth_decrypt == "00" || auth_decrypt == "01"){
					    			location.href = "/smartux_adm/admin/login/list.do";
					    		}else if(auth_decrypt == "03"){
					    			location.href = "/smartux_adm/admin/gpack/pack/getTvReplayPackView.do";
					    		}else if(auth_decrypt == "04"){
					    			location.href = "/smartux_adm/admin/starrating/getStarRatingList.do?system_gb=2";
					    		}
					    		return false;
					    	}
					    	
					    }, 
					    error: function(){
					    	alert("작업 중 오류가 발생하였습니다.");
					 		$.unblockUI();
					    }
					});
				});
				
		       // document.getElementById("A1").style.display = "none";
				$.ajax({
				    url: '/smartux_adm/admin/login/menu/displayChk.do',
				    type: 'GET',
				    dataType: 'json',
				    data: {
				    	// "code": 'C0002',  "item_code_1": 'AMIMS_OPEN_P1',  "item_code_2": 'AMIMS_CLOSE_MENU'
				    },
				    success: function(rs){
				    	//var code_id = 'A1|B1|B2|B2|A1';
						var code_id = rs.code_id;
				    	var code = code_id.split("|")
						
				    	for(var value of code){
							var elementById =document.getElementById(value);
							elementById.style.pointerEvents = "none"
							elementById.style.color = "gray";
							elementById.style.textDecoration = "line-through";
						}
				    }, 
				    error: function(){
				    	alert("작업 중 오류가 발생하였습니다.");
				 		$.unblockUI();
				    }
				});
			});
				<!--
						/*
						d = new dTree('d');
						d.config.useCookies = false;
						
						//d.add(자신의 SEQ,부모의 SEQ,'출력텍스트','링크','a 링크 title'); 
					
						d.add(0,-1,'SmartUX 관리','/smartux_adm/admin/login/list.do','SmartUX 관리');
							
						d.add(10,0,'관리자','','관리자');
						d.add(1000,10,'관리자 리스트','/smartux_adm/admin/login/list.do','관리자 리스트');
						
						d.add(11,0,'코드','','코드');
						d.add(1100,11,'코드목록 조회','/smartux_adm/admin/code/getCodeList.do','코드목록 조회');
						d.add(1101,11,'코드아이템 목록 조회','/smartux_adm/admin/code/getCodeItemList.do','코드아이템 목록 조회');
						
						d.add(12,0,'패널/지면','','패널/지면');
						d.add(1200,12,'패널목록 조회','/smartux_adm/admin/mainpanel/getPanelList.do','패널목록 조회');
						d.add(1201,12,'지면목록 조회','/smartux_adm/admin/mainpanel/getPanelTitleTempList.do','지면목록 조회');
						
						// d.add(13,0,'스마트스타트','/smartux_adm/admin/smartstart/getItemList.do','스마트스타트');
						// d.add(105,13,'스마트스타트 항목 조회','/smartux_adm/admin/smartstart/getItemList.do','스마트스타트 항목 조회');
						
						d.add(14,0,'Cache','','Cache');
						d.add(1400,14,'Cache Size 조회','/smartux_adm/admin/cache/getCacheSize.do','Cache Size 조회');
						
						d.add(15,0,'자체편성관리','','자체편성관리');
						d.add(1500,15,'자체편성 목록조회','/smartux_adm/admin/schedule/getScheduleList.do','자체편성목록조회');
						
						d.add(16,0,'BestVOD 관리','','BestVOD 관리');
						d.add(1600,16,'랭킹 데이터 관리','/smartux_adm/admin/rank/getRankList.do','랭킹 데이터 관리');
						d.add(1601,16,'랭킹 룰 관리','/smartux_adm/admin/rule/getRuleList.do','랭킹 룰 관리');
						
						document.write(d);
						
						var dtree_open_id = getCookieNew("dtree_left_id");
						//setCookieNew("dtree_left_id",'0',365);
						if(typeof(dtree_open_id) == "undefined"){
							dtree_open_id = '0';
						}
						d.openTo('', true);
						document.write(dtree_open_id);
						*/
						
						$(document).ready(function(){
							
							// second example
							$("#browser").treeview({
								collapsed: true,
								unique: true,
								persist: "location"
							});

						});
				//-->
			</script>
			
			<ul id="browser" class="filetree">
			
			<c:if test="${auth_decrypt=='00' || auth_decrypt=='01'}">
				<li><span class="folder">관리자</span>
					<ul>
						<li><span class="file"><a id ="A1" href="/smartux_adm/admin/login/list.do">관리자 리스트</a></span></li>
					</ul>
				</li>
			</c:if>

			<!--
			//==================세컨드 TV 관리자 전용 메뉴===========================// 
			관리자에 따라 보여주는 메뉴를 다르게 한다. 00:슈퍼관리자 , 01:일반관리자 , 02:세컨드TV관리자 , 03:VOD프로모션관리자
			-->
			<c:if test="${auth_decrypt=='00' || auth_decrypt=='02'}">
				<li><span class="folder">세컨드TV 관리</span>
					<ul>
						<li><span class="file"><a id ="B1" href="/smartux_adm/admin/startup/getEmergency.do?scr_tp=T&display_type=S">긴급(비상)공지</a></span></li>
						<li><span class="file"><a id ="B2" href="/smartux_adm/admin/startup/getEmergency.do?scr_tp=K&display_type=S">긴급(비상)공지<br/>(아이들나라)</a></span></li>
						<li><span class="file"><a id ="B3" href="/smartux_adm/admin/quality/qualityMemberList.do?serviceType=">품질대상단말정보</a></span></li>
						<li><span class="file"><a id ="B4" href="/smartux_adm/admin/notimng/getNotiList.do">공지 게시판 마스터</a></span></li>
						<li><span class="file"><a id ="B5" href="/smartux_adm/admin/noti/getNotiList.do?bbs_gbn=PU&scr_tp=T">팝업공지/이벤트</a></span></li>
						<li><span class="file"><a id ="B6" href="/smartux_adm/admin/noti/getNotiList.do?bbs_gbn=EV&scr_tp=T">공지/이벤트 게시판</a></span></li>
						<li><span class="file"><a id ="B7" href="/smartux_adm/admin/noti/getManageTerm.do?scr_tp=T">단말정보</a></span></li>
						<li><span class="file"><a id ="B8" href="/smartux_adm/admin/statbbs/statbbsList.do">참여통계 리스트</a></span></li>
						<li><span class="file"><a id ="B9" href="/smartux_adm/admin/statbbs/statPaticipantList.do">참여이력 리스트</a></span></li>
					</ul>
				</li>		
			</c:if>		
			<c:if test="${auth_decrypt == '00'}">
				<li><span class="folder">코드</span>
					<ul>
						<li><span class="file"><a id ="C1" href="/smartux_adm/admin/code/getCodeList.do">코드목록 조회</a></span></li>
						<li><span class="file"><a id ="C2" href="/smartux_adm/admin/code/getCodeItemList.do">코드아이템 목록 조회</a></span></li>
			Ï		</ul>
				</li>
			</c:if>
			<c:if test="${auth_decrypt == '00'}">
				<li><span class="folder">트리거</span>
					<ul>
						<li><span class="file"><a id ="D1" href="/smartux_adm/admin/trigger/getTriggerInfo.do?itemCode=TVapp_tr">트리거 정보 조회</a></span></li>
						<li><span class="file"><a id ="D2" href="/smartux_adm/admin/trigger/getStartChannel.do">시작채널 설정</a></span></li>
						<li><span class="file"><a id ="D3" href="/smartux_adm/admin/trigger/getStartDiscount.do">가격버튼하단문구설정</a></span></li>
					</ul>
				</li>
			</c:if>					
			<c:if test="${auth_decrypt=='00' || auth_decrypt=='01'}">
				<li><span class="folder">패널/지면</span>
					<ul>
						<li><span class="file"><a id ="E1" href="/smartux_adm/admin/mainpanel/getPanelList.do">패널목록 조회</a></span></li>
						<li><span class="file"><a id ="E2" href="/smartux_adm/admin/mainpanel/getPanelTitleTempList.do">지면목록 조회</a></span></li>
						<c:choose>
						  	<c:when test="${auth_decrypt == '00'}">
								<li><span class="file"><a id ="E3" href="/smartux_adm/admin/mainpanel/getContingency.do">Contingency Mode</a></span></li>
						  	</c:when>
						</c:choose>
<!-- 2019.11.04 : 패널UI타입 관리 -> UI타입 관리 으로 수정 - 이태광 Start -->						
						<li><span class="file"><a id ="E4" href="/smartux_adm/admin/mainpanel/getPanelUiTypeList.do">UI타입 관리</a></span></li>
<!-- 2019.11.04 : 패널UI타입 관리 -> UI타입 관리 으로 수정 - 이태광 End -->		
                        <li><span class="file"><a id ="E5" href="/smartux_adm/admin/mainpanel/getBubbleList.do">말풍선 관리</a></span></li>                      				
                        <li><span class="file"><a id ="E6" href="/smartux_adm/admin/ads/getAdsList.do?scr_tp=L&findName=title&findValue=&adsListMode=total&masterID=topmenu01&t=2">상단메뉴 변경</a></span></li>
					</ul>
				</li>
			</c:if>
			<c:if test="${auth_decrypt == '00'}">
				<li><span class="folder">Cache</span>
					<ul>
						<li><span class="file"><a id ="F1" href="/smartux_adm/admin/cache/getCacheSize.do">Cache Size 조회</a></span></li>
					</ul>
				</li>	
			</c:if>
			<c:if test="${auth_decrypt=='00' || auth_decrypt=='01'}">
				<li><span class="folder">자체편성관리</span>
					<ul>
						<li><span class="file"><a id ="G1" href="/smartux_adm/admin/schedule/getScheduleList.do">자체편성 목록조회</a></span></li>
					</ul>
				</li>
				<li><span class="folder">BestVOD 관리</span>
					<ul>
						<li><span class="file"><a id ="H1"  href="/smartux_adm/admin/rank/getRankList.do">랭킹 데이터 관리</a></span></li>
						<li><span class="file"><a id ="H2"  href="/smartux_adm/admin/rule/getRuleList.do">랭킹 룰 관리</a></span></li>
					</ul>
				</li>
				<li><span class="folder">Youtube</span>
					<ul>
						<li><span class="file"><a id ="I1" href="/smartux_adm/admin/youtube/list.do">Youtube 검색어 관리</a></span></li>
					</ul>
				</li>
				<li><span class="folder">약관 관리</span>
					<ul>
						<li><span class="file"><a id ="J1" href="/smartux_adm/admin/terms/getTermsList.do">(추천이용동의)약관 관리</a></span></li>
						<li><span class="file"><a id ="J2" href="/smartux_adm/admin/terms/getAccessList.do">약관 마스터 관리</a></span></li>
						<li><span class="file"><a id ="J3" href="/smartux_adm/admin/terms/getAccessInfoList.do">약관 항목 관리</a></span></li>
					</ul>
				</li>
				<li><span class="folder">공지 관리</span>
					<ul>
						<li><span class="file"><a id ="K1" href="/smartux_adm/admin/noticeinfo/getNoticeInfoList.do">공지/이미지 정보 관리</a></span></li>
					</ul>
				</li>
				<li><span class="folder">Push 관리</span>
					<ul>
						<li><span class="file"><a id ="L1" href="${pageContext.request.contextPath}/admin/news/newsList.do">새소식 리스트</a></span></li>
					</ul>
				</li>
			</c:if>
			<c:if test="${auth_decrypt=='00'}">
				<li><span class="folder">HDTV</span>
					<ul>
						<li><span class="file"><a id ="M1" href="/smartux_adm/admin/hdtv/view.do">HDTV STARTUP 설정</a></span></li>
					</ul>
				</li>
			</c:if>
			<c:if test="${auth_decrypt == '00' || auth_decrypt == '01' || auth_decrypt == '03'}">
				<li><span class="folder">VOD 프로모션</span>
					<ul>
						<li>
							<span class="folder">TV다시보기</span>
							<ul>
								<li><span class="file"><a id ="N1" href="/smartux_adm/admin/gpack/pack/getTvReplayPackView.do">템플릿 관리</a></span></li>
								<li><span class="file"><a id ="N2" href="/smartux_adm/admin/gpack/event/getTvReplayPromotionList.do">프로모션 관리</a></span></li>
								<li><span class="file"><a id ="N3" href="/smartux_adm/admin/gpack/category/getTvReplayGpackCategoryList.do">카테고리 관리</a></span></li>
							</ul>
						</li>
						<li>
							<span class="folder">영화/애니</span>
							<ul>
								<li><span class="file"><a id ="N4" href="/smartux_adm/admin/gpack/pack/getMovieAniPackView.do">템플릿 관리</a></span></li>
								<li><span class="file"><a id ="N5" href="/smartux_adm/admin/gpack/event/getMovieAniPromotionList.do">프로모션 관리</a></span></li>
								<li><span class="file"><a id ="N6" href="/smartux_adm/admin/gpack/category/getMovieAniGpackCategoryList.do">카테고리 관리</a></span></li>
							</ul>
						</li>
						<li>
							<span class="folder">키즈교육</span>
							<ul>
								<li><span class="file"><a id ="N7" href="/smartux_adm/admin/gpack/pack/getKidsEduPackView.do">템플릿 관리</a></span></li>
								<li><span class="file"><a id ="N8" href="/smartux_adm/admin/gpack/event/getKidsEduPromotionList.do">프로모션 관리</a></span></li>
								<li><span class="file"><a id ="N9" href="/smartux_adm/admin/gpack/category/getKidsEduGpackCategoryList.do">카테고리 관리</a></span></li>
							</ul>
						</li>
						<li>
							<span class="folder">특집이벤트</span>
							<ul>
								<li><span class="file"><a id ="N10" href="/smartux_adm/admin/gpack/pack/getSpecEventPackView.do">템플릿 관리</a></span></li>
								<li><span class="file"><a id ="N11" href="/smartux_adm/admin/gpack/event/getSpecEventPromotionList.do">프로모션 관리</a></span></li>
								<li><span class="file"><a id ="N12" href="/smartux_adm/admin/gpack/category/getSpecEventGpackCategoryList.do">카테고리 관리</a></span></li>
							</ul>
						</li>
					</ul>
				</li>
			</c:if>
			<c:if test="${auth_decrypt=='00'}">
				<li><span class="folder">Paynow</span>
					<ul>
						<li><span class="file"><a id ="O1" href="/smartux_adm/admin/paynow/list.do">Paynow 결제 정보</a></span></li>
						<li><span class="file"><a id ="O2" href="/smartux_adm/admin/paynow/bannerList.do">Paynow 배너 조회</a></span></li>
					</ul>
				</li>
			</c:if>
			<c:if test="${auth_decrypt=='00' || auth_decrypt=='04'}">
			<!-- <li><span class="folder">시즌 관리</span>
				<ul>
					<li><span class="file"><a href="/smartux_adm/admin/season/seasonList.do">시즌 관리</a></span></li>
				</ul>
			</li> -->
			<li><span class="folder">별점 관리</span>
				<ul>
					<li><span class="file"><a id ="P1" href="/smartux_adm/admin/starrating/getStarRatingList.do?system_gb=2">별점 목록 조회 </a></span></li>
				</ul>
			</li>
			</c:if>
			<c:if test="${auth_decrypt=='00'}">
				<li><span class="folder">구매내역</span>
					<ul>
						<li><span class="file"><a id ="Q1" href="/smartux_adm/admin/multipayment/list.do">구매내역 리스트</a></span></li>
						<li><span class="file"><a id ="Q2" href="/smartux_adm/admin/multipayment/ppmlist.do">다회선 구매내역 리스트</a></span></li>
						<li><span class="file"><a id ="Q3" href="/smartux_adm/admin/multipayment/list.do?mtype=9">구매내역 리스트(아이들나라)</a></span></li>
						<li><span class="file"><a id ="Q4" href="/smartux_adm/admin/multipayment/livelist.do?find_pa_key=LT">구매내역 리스트(라이브)</a></span></li>
					</ul>

						

				</li>
			</c:if>
			<c:if test="${auth_decrypt=='00' || auth_decrypt=='01'}">
				<li><span class="folder">배너 관리</span>
					<ul>
						<li><span class="file"><a id ="R1" href="/smartux_adm/admin/ads/adsMaster.do">배너 마스터</a></span></li>
						<li><span class="file"><a id ="R2" href="/smartux_adm/admin/ads/getAdsList.do?scr_tp=L&t=1">배너 리스트</a></span></li>
					</ul>
				</li>
			</c:if>
			<c:if test="${auth_decrypt=='00' || auth_decrypt=='01'}">
			<li><span class="folder">전용포스터 관리</span>
				<ul>
					<li><span class="file"><a id ="S1" href="/smartux_adm/admin/poster/getPosterList.do">전용포스터 목록</a></span></li>
				</ul>	
			</li>
			</c:if>
			<c:if test="${auth_decrypt=='00' || auth_decrypt=='01'}">
			<li><span class="folder">Senior</span>
				<ul>
					<li><span class="file"><a id ="T1" href="/smartux_adm/admin/teleport/list.do">순간이동 관리</a></span></li>
					<li><span class="file"><a id ="T2" href="/smartux_adm/admin/gallery/galleryCateList.do">갤러리 관리</a></span></li>
					<li><span class="file"><a id ="T3" href="/smartux_adm/admin/dayinfo/dayInfoList.do">일별정보 관리</a></span></li>
					<li><span class="file"><a id ="T4" href="/smartux_adm/admin/greeting/list.do">인사말 관리</a></span></li>
					<li><span class="file"><a id ="T5" href="/smartux_adm/admin/lifemessage/getLifeMessageList.do">생활지수 문구 관리</a></span></li>
     				<li><span class="file"><a id ="T6" href="/smartux_adm/admin/backgroundimage/getBackgroundImageList.do">배경영상 관리</a></span></li>
				</ul>
			</li>
			</c:if>
			<c:if test="${auth_decrypt=='00' || auth_decrypt=='01'}">
			<li><span class="folder">화제동영상 관리</span>
				<ul>
					<li><span class="file"><a id ="U1" href="/smartux_adm/admin/hotvod/siteList.do">사이트 리스트</a></span></li>
					<li><span class="file"><a id ="U2" href="/smartux_adm/admin/hotvod/contentList.do">컨텐츠 리스트</a></span></li>
					<li><span class="file"><a id ="U3" href="/smartux_adm/admin/hotvod/hitcntStats.do">조회수 통계</a></span></li>
					<li><span class="file"><a id ="U4" href="/smartux_adm/admin/hotvod/filteringSiteList.do">필터링 사이트</a></span></li>
				</ul>
			</li>
			</c:if>	
			<c:if test="${auth_decrypt=='00' || auth_decrypt=='01'}">
				<li><span class="folder">포터블 TV관리</span>
				<ul>
					<li><span class="file"><a id ="V1" href="/smartux_adm/admin/quality/qualityMemberList.do?serviceType=P">품질대상단말정보</a></span></li>
				</ul>
			</c:if>			
			<c:if test="${auth_decrypt=='00' || auth_decrypt=='01'}">
				<li><span class="folder">고객 관리</span>
				<ul>
					<li><span class="file"><a id ="W1" href="/smartux_adm/admin/memberlist/memberList.do">가입자 그룹 관리</a></span></li>
				</ul>
			</c:if>	
			<c:if test="${auth_decrypt=='00' || auth_decrypt=='01'}">
				<li><span class="folder">IPTV 관리</span>
				<ul>
					<li><span class="file"><a id ="X1" href="/smartux_adm/admin/keyword/keywordCateList.do">발화어 관리</a></span></li>
				</ul>
			</c:if>	
			<c:if test="${auth_decrypt=='00' || auth_decrypt=='01'}">
                <li><span class="folder">가이드채널</span>
                <ul>
                    <li><span class="file"><a id ="Y1" href="/smartux_adm/admin/guidelink/guideLinkList.do">가이드채널 링크관리</a></span></li>
                </ul>
            </c:if> 
            <c:if test="${auth_decrypt=='00' || auth_decrypt=='01'}">
                <li><span class="folder">AB테스트 관리</span>
                <ul>
                    <li><span class="file"><a id ="Z1" href="/smartux_adm/admin/abtest/getABTestList.do">AB테스트 목록 관리</a></span></li>
                    <li><span class="file"><a id ="Z2" href="/smartux_adm/admin/abtest/getBPASList.do">BPAS 편성 관리</a></span></li>
                </ul>
            </c:if> 
            <c:if test="${auth_decrypt=='00' || auth_decrypt=='01'}">
                <li><span class="folder">프로필이미지 관리</span>
                <ul>
                    <li><span class="file"><a id ="AA1" href="/smartux_adm/admin/profile/profileMaster.do?service_type=K">프로필이미지 마스터</a></span></li>
                    <li><span class="file"><a id ="AA2" href="/smartux_adm/admin/profile/getProfileList.do?service_type=K">프로필이미지 관리</a></span></li>
                </ul>
            </c:if> 
			</ul>
		</td>
	</tr>
</tbody>
</table>