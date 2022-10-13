package com.dmi.smartux.common.interceptor;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.dmi.smartux.common.vo.Result;
import com.dmi.smartux.configuration.vo.YoutubeResultVO;
import com.dmi.smartux.mainpanel.vo.MainPanelResult;
import com.dmi.smartux.mainpanel.vo.MainPanelVersionInfoVO;

public class SmartUXInterceptor implements HandlerInterceptor{

	private final Log logger = LogFactory.getLog(this.getClass());
	//private final Log logger = LogFactory.getLog("dailyout2");
	//private Log logger;
	
	
	long startTime;
	long endTime;
	
	String timeStr;
	
	private void setLogger(String _url){
		//logger.info("["+_url+"] =========================");
		
//		if(_url.equalsIgnoreCase("/smartux_adm/addRegistrationID")){						this.logger = LogFactory.getLog("addRegistrationID");
//		}else if(_url.equalsIgnoreCase("/smartux_adm/addReservedProgram")){					this.logger = LogFactory.getLog("addReservedProgram");
//		}else if(_url.equalsIgnoreCase("/smartux_adm/getConfiguration")){					this.logger = LogFactory.getLog("getConfiguration");
//		}else if(_url.equalsIgnoreCase("/smartux_adm/getGenreVodBestList")){				this.logger = LogFactory.getLog("getGenreVodBestList");
//		}else if(_url.equalsIgnoreCase("/smartux_adm/getI20AlbumList")){					this.logger = LogFactory.getLog("getI20AlbumList");
//		}else if(_url.equalsIgnoreCase("/smartux_adm/getMainPanelInterlockingInfo")){		this.logger = LogFactory.getLog("getMainPanelInterlockingInfo");
//		}else if(_url.equalsIgnoreCase("/smartux_adm/getMainPanelVersionInfo")){			this.logger = LogFactory.getLog("getMainPanelVersionInfo");
//		}else if(_url.equalsIgnoreCase("/smartux_adm/getRealRating")){						this.logger = LogFactory.getLog("getRealRating");
//		}else if(_url.equalsIgnoreCase("/smartux_adm/getReservedProgramList")){				this.logger = LogFactory.getLog("getReservedProgramList");
//		}else if(_url.equalsIgnoreCase("/smartux_adm/getSmartStartItemList")){				this.logger = LogFactory.getLog("getSmartStartItemList");
//		}else if(_url.equalsIgnoreCase("/smartux_adm/getSUXMAlbumList")){					this.logger = LogFactory.getLog("getSUXMAlbumList");
//		}else if(_url.equalsIgnoreCase("/smartux_adm/getThemeInfo")){						this.logger = LogFactory.getLog("getThemeInfo");
//		}else if(_url.equalsIgnoreCase("/smartux_adm/getWishList")){						this.logger = LogFactory.getLog("getWishList");
//		}else if(_url.equalsIgnoreCase("/smartux_adm/getYoutubeSearchKey")){				this.logger = LogFactory.getLog("getYoutubeSearchKey");
//		}else if(_url.equalsIgnoreCase("/smartux_adm/removeAllReservedProgram")){			this.logger = LogFactory.getLog("removeAllReservedProgram");
//		}else if(_url.equalsIgnoreCase("/smartux_adm/removeRegistrationID")){				this.logger = LogFactory.getLog("removeRegistrationID");
//		}else if(_url.equalsIgnoreCase("/smartux_adm/removeReservedProgram")){				this.logger = LogFactory.getLog("removeReservedProgram");
//		}else{																			this.logger = LogFactory.getLog(this.getClass());
//		}
	}
	
	/**
	 * 캐쉬를 사용하는 API 인지 구분하는 메서드(로그출력 구분 위함)
	 * @param _url
	 * @return true : 
	 */
	private boolean getCacheAPIYN(String _url){
		
		//캐쉬사용하지 않는 API 목록
		if(_url.equalsIgnoreCase("/smartux_adm/addRegistrationID") ||
		   _url.equalsIgnoreCase("/smartux_adm/removeRegistrationID") ||
		   _url.equalsIgnoreCase("/smartux_adm/addReservedProgram") ||
		   _url.equalsIgnoreCase("/smartux_adm/removeReservedProgram") ||
		   _url.equalsIgnoreCase("/smartux_adm/removeAllReservedProgram") ||
		   _url.equalsIgnoreCase("/smartux_adm/getReservedProgramList") ||
		   _url.equalsIgnoreCase("/smartux_adm/reqPushMessage") ||
		   _url.equalsIgnoreCase("/smartux_adm/getWishList")
		){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		
		String ip = request.getRemoteAddr();
		String url = request.getRequestURI();
		setLogger(url);
		Enumeration penum = request.getParameterNames();
		
		String key = null;
		String value = null;
		
		String info_mac = "";
		String info_id = "";
		String info_app_type = "";
		String debug = "";
				
		while(penum.hasMoreElements()){
			key = (String)penum.nextElement();
			value = (new String(request.getParameter(key)) == null) ? "" : new String(request.getParameter(key));
			
			if(key.equalsIgnoreCase("stb_mac")){
				info_mac = value;
			}
			if(key.equalsIgnoreCase("sa_id")){
				info_id = value;
			}
			if(key.equalsIgnoreCase("app_type")){
				info_app_type = value;
			}
			debug += " ["+key+"="+value+"]";
		}
		
		logger.info("[Interceptor]["+ip+"] ["+url+"][START] - ["+info_mac+"]["+info_id+"]["+info_app_type+"]");
		logger.debug("[Interceptor]["+ip+"] ["+url+"][START] -"+debug);
		
		startTime = System.currentTimeMillis();
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		String ip = request.getRemoteAddr();
		String url = request.getRequestURI();
		
		try{
			Map map = modelAndView.getModel();
			
			Iterator<String> iterator = map.keySet().iterator();
			
			String mapKey = (String) iterator.next();
			String key = "";
			String value = "";
			String info_mac = "";
			String info_id = "";
			String info_app_type = "";
			
			String callByScheduler = "";
			
			Enumeration penum = request.getParameterNames();
			while(penum.hasMoreElements()){
				key = (String)penum.nextElement();
				value = (new String(request.getParameter(key)) == null) ? "" : new String(request.getParameter(key));
				
				if(key.equalsIgnoreCase("stb_mac")){
					info_mac = value;
				}
				if(key.equalsIgnoreCase("sa_id")){
					info_id = value;
				}
				if(key.equalsIgnoreCase("app_type")){
					info_app_type = value;
				}
				
				if(key.equalsIgnoreCase("callByScheduler")){
					callByScheduler = value;
				}
			}
			
			if(getCacheAPIYN(url)){
				//CASTING 구분을 위하여 필요
				if(url.equalsIgnoreCase("/smartux_adm/getMainPanelInterlockingInfo")){
					MainPanelResult temp = (MainPanelResult)map.get(mapKey);
					logger.info("[Interceptor]["+ip+"] ["+url+"][RETURN] - ["+info_mac+"]["+info_id+"]["+info_app_type+"] ["+temp.getFlag()+"]");
				}else if(url.equalsIgnoreCase("/smartux_adm/getMainPanelVersionInfo")){
					MainPanelVersionInfoVO temp = (MainPanelVersionInfoVO)map.get(mapKey);
					logger.info("[Interceptor]["+ip+"] ["+url+"][RETURN] - ["+info_mac+"]["+info_id+"]["+info_app_type+"] ["+temp.getFlag()+"]");
				}else if(url.equalsIgnoreCase("/smartux_adm/getYoutubeSearchKey")){
					YoutubeResultVO temp = (YoutubeResultVO)map.get(mapKey);
					logger.info("[Interceptor]["+ip+"] ["+url+"][RETURN] - ["+info_mac+"]["+info_id+"]["+info_app_type+"] ["+temp.getFlag()+"]");
				}else{
					Result temp = (Result)map.get(mapKey);
					logger.info("[Interceptor]["+ip+"] ["+url+"][RETURN] - ["+info_mac+"]["+info_id+"]["+info_app_type+"] ["+temp.getFlag()+"]");
				}
			}else{
				logger.info("[Interceptor]["+ip+"] ["+url+"][RETURN] - ["+info_mac+"]["+info_id+"]["+info_app_type+"] ["+map.get(mapKey)+"]");
			}
			
//		while (iterator.hasNext()) {
//		     String key = (String) iterator.next();
//		     System.out.print("key="+key);
//		     System.out.println(" value="+map.get(key));
//		}
		}catch(Exception e){}
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		String ip = request.getRemoteAddr();
		String url = request.getRequestURI();
		String key = "";
		String value = "";
		String info_mac = "";
		String info_id = "";
		String info_app_type = "";


		Enumeration penum = request.getParameterNames();
		while(penum.hasMoreElements()){
			key = (String)penum.nextElement();
			value = (new String(request.getParameter(key)) == null) ? "" : new String(request.getParameter(key));
			
			if(key.equalsIgnoreCase("stb_mac")){
				info_mac = value;
			}
			if(key.equalsIgnoreCase("sa_id")){
				info_id = value;
			}
			if(key.equalsIgnoreCase("app_type")){
				info_app_type = value;
			}
		}
		endTime = System.currentTimeMillis();
		timeStr = Long.toString((endTime-startTime));
//		logger.info(timeStr);
		if(timeStr.length() > 3){
			timeStr = timeStr.substring(0,timeStr.length()-3)+"."+timeStr.substring(timeStr.length()-3,timeStr.length());
		}else{
			for(int i=timeStr.length();i<3;i++){
				timeStr = "0"+timeStr;	
			}
			timeStr = "0."+timeStr;
		}
		//logger.info("["+ip+"] ["+url+"][END] ["+(timeStr)+" sec] - ["+info_mac+"]["+info_id+"]["+info_app_type+"]");
		logger.info("[Interceptor]["+ip+"] ["+url+"][END] - ["+info_mac+"]["+info_id+"]["+info_app_type+"]");
	}
	
}
