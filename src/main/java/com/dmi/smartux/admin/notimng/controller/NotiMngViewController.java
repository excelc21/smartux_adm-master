package com.dmi.smartux.admin.notimng.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.dmi.smartux.admin.notimng.service.NotiMngViewService;
import com.dmi.smartux.admin.notimng.vo.NotiMngViewVO;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.GlobalCom;

@Controller
public class NotiMngViewController {

	@Autowired
	NotiMngViewService service;

	private final Log logger = LogFactory.getLog ( this.getClass ( ) );

	/**
	 * 관리자 초기 화면 ,DB 에 저장되어있는 게시판 목록 을 가져온다.
	 * 
	 * @return 관리자 초기 화면 페이지 URL
	 * @param bbs_Gbn 게시판 구분
	 * @param result 수행 결과 값
	 * @param scr_tp HDTV 와 SMARTUX 구분
	 * @param del_Yn_Check 활성화만 보여줄지 비활성화만 보여줄지에 대한 값(0 = 활성화 , 1 = 비활성화)
	 * @throws Exception
	 */

	@RequestMapping( value = "/admin/notimng/getNotiList", method = RequestMethod.GET )
	public String getNotiMngViewList ( @RequestParam( value = "bbs_Gbn", required = false, defaultValue = "" )
	String bbs_Gbn, @RequestParam( value = "result", required = false, defaultValue = "" )
	String result, @RequestParam( value = "scr_tp", required = false, defaultValue = "" )
	String scr_tp, @RequestParam( value = "del_Yn_Check", required = false, defaultValue = "" )
	String del_Yn_Check, @RequestParam( value = "is_none", required = false, defaultValue = "" )
	String is_none, Model model ) throws Exception {

		logger.debug ( "bbs_Gbn =" + bbs_Gbn );
		logger.debug ( "result =" + result );
		logger.debug ( "scr_tp =" + scr_tp );
		logger.debug ( "del_Yn_check = " + del_Yn_Check );

		// 게시판 구분(bbs_Gbn) 에 값이 없다면 EV 을 초기값으로 설정 한다.
		if ( bbs_Gbn.equals ( "" ) ) bbs_Gbn = "EV";
		// 지금은 H로 강제 하지만 Smart 가 생기게 되면 이거는 없애도 된다. 왼쪽의 메뉴를 클릭하는 순간 파라메터 값은 항상 넘어 오기에.
		if ( scr_tp.equals ( "" ) ) scr_tp = "T";
		// del_Yn_check 에 값이 없다면 활성화된 게시판을 보여주는 0으로 del_Yn_Check 를 세팅한다.
		if ( del_Yn_Check.equals ( "" ) ) del_Yn_Check = "0";

		List<Map<String, String>> data = null;

		// 게시판 구분 값 을 가지고 그에 맞는 게시판 목록을 list<map<String,String>> 가지고 온다.
		try {
			logger.debug ( "getNotiMngViewList Start" );
			data = service.getNotiMngViewList ( bbs_Gbn, scr_tp );
			logger.debug ( "getNotiMngViewList implemeted" );
		} catch ( Exception e ) {
			logger.error ( "getNotiMngViewList " + e.getMessage ( ) );
		}
		// data의 listNum 이 가지는 의미는 0일경우 데이타가 없다는 뜻이다.. jsp 에서 data안의 size를 보고 for문을 돌려 lsit 를 나열하기 때문에
		// data 의 listNum 이 0일 경우 data 안에는 아예 값이 없어야 한다. 그래서 if 문을 통해 리스트가 0일경우 List인 data 배열을 초기화 시킨다.

		// listnum 은 뷰로 올라가는 딱 그갯수로 지칭 해야 된다.
		String listNum = String.valueOf ( data.size ( ) );
		logger.debug ( "listNum is " + listNum );

		if ( listNum.equals ( "0" ) ) {
			data.clear ( );
		}

		logger.debug ( bbs_Gbn );
		logger.debug ( scr_tp );
		logger.debug ( result );

		if ( result.equals ( "SUCCESS" ) ) {
			result = "1";
		} else if ( result.equals ( "DUPLECATE" ) ) {
			result = "2";
		} else if ( result.equals ( "ERROR" ) ) {
			result = "0";
		}

		model.addAttribute ( "listSize", listNum );
		model.addAttribute ( "notiMngList", data );
		model.addAttribute ( "bbs_Gbn", bbs_Gbn );
		model.addAttribute ( "result", result );
		model.addAttribute ( "scr_Type", scr_tp );
		model.addAttribute ( "del_Yn_Check", del_Yn_Check );
		model.addAttribute ( "is_none", is_none );

		return "admin/noti/notimng";
	}

	/**
	 * 관리자 로 부터 받은 값을 Insert 시키는 메소드
	 * 
	 * @return 관리자 초기 화면 페이지 URL
	 * @param bbs_Gbn 게시판 구분
	 * @param bbs_Nm 게시판 명
	 * @param listSize 해당게시판의 게시글 갯수
	 * @throws Exception
	 */
	@RequestMapping( value = "/admin/notimng/setInsertList", method = RequestMethod.GET )
	public String setNotiMngInsert ( HttpServletRequest request, @RequestParam( value = "bbs_Gbn", required = false, defaultValue = "" )
	String bbs_Gbn, @RequestParam( value = "bbs_Nm", required = false, defaultValue = "" )
	String bbs_Nm, @RequestParam( value = "scr_tp", required = false, defaultValue = "" )
	String scr_Type, @RequestParam( value = "bbs_Id", required = false, defaultValue = "" )
	String bbs_Id, Model model ) throws Exception {

		logger.debug ( "bbs_Gbn = " + bbs_Gbn );
		logger.debug ( "bbs_Nm = " + bbs_Nm );

		// 로그 기록을 남기기 위해 관리자의 IP와 ID 를 받는다. 아랫부분에서 vo 객체에 담아 Dao 까지 보낸뒤 로그DB 에 저장한다.
		String ip = request.getRemoteAddr ( );
		String id = CookieUtil.getCookieUserID ( request );

		// 작업(insert , delete , update) 의 성공유무에 따라 메시지 저장
		String result = null;

		// 현재시간(yyyyMMdd)에 001을 붙혀서 버전정보를 만든다.
		String nowDate = GlobalCom.getTodayFormat ( );
		nowDate = nowDate + "001";

		// vo 객체에 insert 에 필요한 정보들을 담는다.
		NotiMngViewVO vo = new NotiMngViewVO ( );
		vo.setBbsGbn ( bbs_Gbn );
		vo.setBbsNm ( bbs_Nm );
		vo.setVerSion ( nowDate );
		vo.setRegId ( CookieUtil.getCookieUserID ( request ) );
		vo.setScr_Type ( scr_Type );
		vo.setBbsId ( bbs_Id );
		vo.setAct_id ( id );
		vo.setAct_ip ( ip );

		// result 값이 1이면 성공 0이면 오류
		try {
			logger.debug ( "setNotiMngInsert  Start" );
			result = service.setNotiMngInsert ( vo );
			logger.debug ( "setNotiMngInsert implemeted" );

		} catch ( Exception e ) {
			logger.error ( "setNotiMngInsert" + e.getMessage ( ) );
			result = "ERROR";
		}

		return "redirect:/admin/notimng/getNotiList.do?bbs_Gbn=" + bbs_Gbn + "&result=" + result + "&scr_tp=" + scr_Type;
	}

	/**
	 * 관리자 로 부터 받은 값을 Update 시키는 메소드
	 * 
	 * @return 관리자 초기 화면 페이지 URL
	 * @param bbs_Gbn 게시판 구분
	 * @param bbs_Nm 게시판 명
	 * @param bbs_Id 게시판 아이디
	 * @throws Exception
	 */
	@RequestMapping( value = "/admin/notimng/setUpdateList", method = RequestMethod.GET )
	public String setNotiMngUpdate ( @RequestParam( value = "bbs_Id", required = false, defaultValue = "" )
	String bbs_Id, @RequestParam( value = "bbs_Nm", required = false, defaultValue = "" )
	String bbs_Nm, @RequestParam( value = "bbs_Gbn", required = false, defaultValue = "" )
	String bbs_Gbn, @RequestParam( value = "scr_tp", required = false, defaultValue = "" )
	String scr_Type, HttpServletRequest request ) throws Exception {

		logger.debug ( "bbs_Id = " + bbs_Id );
		logger.debug ( "bbs_Nm = " + bbs_Nm );
		logger.debug ( "bbs_Gbn = " + bbs_Gbn );

		// 작업(insert , delete , update) 의 성공유무에 따라 메시지 저장
		String result = null;

		NotiMngViewVO vo = new NotiMngViewVO ( );
		vo.setBbsId ( bbs_Id );
		vo.setBbsNm ( bbs_Nm );
		vo.setModId ( CookieUtil.getCookieUserID ( request ) );
		vo.setAct_id ( CookieUtil.getCookieUserID ( request ) );
		vo.setAct_ip ( request.getRemoteAddr ( ) );

		// result 값이 1이면 성공 0이면 오류
		try {
			logger.debug ( "setNotiMngUpdate  Start" );
			service.setNotiMngUpdate ( vo );
			result = "SUCCESS";
			logger.debug ( "setNotiMngUpdate implemeted" );
		} catch ( Exception e ) {
			logger.error ( "setNotiMngUpdate" + e.getMessage ( ) );
			result = "ERROR";
		}

		return "redirect:/admin/notimng/getNotiList.do?bbs_Gbn=" + bbs_Gbn + "&result=" + result + "&scr_tp=" + scr_Type;

	}

	/**
	 * 관리자 로 부터 받은 bbs_Id 값을 이용하여 Delete 시키는 메소드
	 * 
	 * @return 관리자 초기 화면 페이지 URL
	 * @param bbs_Gbn 게시판 구분
	 * @param bbs_Nm 게시판 명
	 * @param bbs_Id 게시판 아이디
	 */
	@RequestMapping( value = "/admin/notimng/setDeleteList", method = RequestMethod.GET )
	public String setNotiMngDelete ( @RequestParam( value = "bbs_Id", required = false, defaultValue = "" )
	String bbs_Id, @RequestParam( value = "bbs_Nm", required = false, defaultValue = "" )
	String bbs_Nm, @RequestParam( value = "bbs_Gbn", required = false, defaultValue = "" )
	String bbs_Gbn, @RequestParam( value = "scr_tp", required = false, defaultValue = "" )
	String scr_Type, HttpServletRequest request ) {

		logger.debug ( "bbs_Id = " + bbs_Id );
		logger.debug ( "bbs_Nm = " + bbs_Nm );
		logger.debug ( "bbs_Gbn = " + bbs_Gbn );

		// 작업(insert , delete , update) 의 성공유무에 따라 메시지 저장
		String result = null;
		try {
			NotiMngViewVO vo = new NotiMngViewVO ( );
			vo.setBbsId ( bbs_Id );
			vo.setBbsNm ( bbs_Nm );
			// 1 = Y(삭제됨) 0 = N(존재)
			vo.setDelYn ( "1" );

			// log 에 남기기 위해 관리자의 아이피와 아이디를 받는다.
			vo.setAct_id ( CookieUtil.getCookieUserID ( request ) );
			vo.setAct_ip ( request.getRemoteAddr ( ) );
			// result 값이 1이면 성공 0이면 오류

			logger.debug ( "setNotiMngDelete  Start" );
			service.NotiMngDelete ( vo );
			result = "SUCCESS";
			logger.debug ( "setNotiMngDelete implemeted" );
		} catch ( Exception e ) {
			logger.error ( "setNotiMngDelete" + e.getMessage ( ) );
			result = "ERROR";
		}

		return "redirect:/admin/notimng/getNotiList.do?bbs_Gbn=" + bbs_Gbn + "&result=" + result + "&scr_tp=" + scr_Type;
	}

}