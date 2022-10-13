package com.dmi.smartux.common.aop;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.util.StopWatch;

@Aspect
public class ExcuteLogAspect {
//	private final Log logger = LogFactory.getLog(this.getClass());
	private Log logger;
	
	private void setLogger(String _url){
		//logger.info("["+_url+"] =========================");
		
		if(_url.equalsIgnoreCase("/smartux_adm/addRegistrationID")){						this.logger = LogFactory.getLog("addRegistrationID");
		}else if(_url.equalsIgnoreCase("/smartux_adm/addReservedProgram")){					this.logger = LogFactory.getLog("addReservedProgram");
		}else if(_url.equalsIgnoreCase("/smartux_adm/getConfiguration")){					this.logger = LogFactory.getLog("getConfiguration");
		}else if(_url.equalsIgnoreCase("/smartux_adm/getGenreVodBestList")){				this.logger = LogFactory.getLog("getGenreVodBestList");
		}else if(_url.equalsIgnoreCase("/smartux_adm/getI20AlbumList")){					this.logger = LogFactory.getLog("getI20AlbumList");
		}else if(_url.equalsIgnoreCase("/smartux_adm/getMainPanelInterlockingInfo")){		this.logger = LogFactory.getLog("getMainPanelInterlockingInfo");
		}else if(_url.equalsIgnoreCase("/smartux_adm/getMainPanelVersionInfo")){			this.logger = LogFactory.getLog("getMainPanelVersionInfo");
		}else if(_url.equalsIgnoreCase("/smartux_adm/getRealRating")){						this.logger = LogFactory.getLog("getRealRating");
		}else if(_url.equalsIgnoreCase("/smartux_adm/getReservedProgramList")){				this.logger = LogFactory.getLog("getReservedProgramList");
		}else if(_url.equalsIgnoreCase("/smartux_adm/getSmartStartItemList")){				this.logger = LogFactory.getLog("getSmartStartItemList");
		}else if(_url.equalsIgnoreCase("/smartux_adm/getSUXMAlbumList")){					this.logger = LogFactory.getLog("getSUXMAlbumList");
		}else if(_url.equalsIgnoreCase("/smartux_adm/getThemeInfo")){						this.logger = LogFactory.getLog("getThemeInfo");
		}else if(_url.equalsIgnoreCase("/smartux_adm/getWishList")){						this.logger = LogFactory.getLog("getWishList");
		}else if(_url.equalsIgnoreCase("/smartux_adm/getYoutubeSearchKey")){				this.logger = LogFactory.getLog("getYoutubeSearchKey");
		}else if(_url.equalsIgnoreCase("/smartux_adm/removeAllReservedProgram")){			this.logger = LogFactory.getLog("removeAllReservedProgram");
		}else if(_url.equalsIgnoreCase("/smartux_adm/removeRegistrationID")){				this.logger = LogFactory.getLog("removeRegistrationID");
		}else if(_url.equalsIgnoreCase("/smartux_adm/removeReservedProgram")){				this.logger = LogFactory.getLog("removeReservedProgram");
		}else if(_url.equalsIgnoreCase("/smartux_adm/getAhomeInfoAgreeText")){				this.logger = LogFactory.getLog("getAhomeInfoAgreeText");
		}else{																			this.logger = LogFactory.getLog(this.getClass());
		}
	}
	
	

// @Before("within(@org.springframework.stereotype.Controller *)")
// public void logBeforeController(JoinPoint.StaticPart jpsp){
//      //if (logger.isInfoEnabled()) {
//    	  logger.info("[SmartUX Log] RequestMapping Controller.. ClassName = " + jpsp.getSignature().getDeclaringType().getName() + ", MethodName = " + jpsp.getSignature().getName());
//     // }
// }

	//@Around("within(@org.springframework.stereotype.Controller *)")
	@Around("execution(* API_*(..))")
    public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable {
        
    	HttpServletRequest request = null;
        HttpServletResponse response = null;
        for ( Object o : joinPoint.getArgs() ){ 
            if ( o instanceof HttpServletRequest ) {
                request = (HttpServletRequest)o;
            } 
            if ( o instanceof HttpServletResponse ) {
                response = (HttpServletResponse)o;
            } 
        }
        
        String ip = request.getRemoteAddr();
		String url = request.getRequestURI();
		
		setLogger(url);
		
		//if (logger.isInfoEnabled()) {
			//logger.info("[SmartUX Log] Excute Controller.. ClassName = " + joinPoint.getTarget().getClass().getName() + ", MethodName = " + joinPoint.getSignature().getName());
		//}
		
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
		
		logger.info("[AOP]["+ip+"] ["+url+"][START] - ["+info_mac+"]["+info_id+"]["+info_app_type+"]");
		logger.debug("[AOP]["+ip+"] ["+url+"][START] -"+debug);
        
        
        
        Object obj = joinPoint.getArgs();
        StopWatch sw = new StopWatch(joinPoint.toShortString());
        sw.start();
        try {
            return joinPoint.proceed();
        } finally {
        	
            sw.stop();
//            if (logger.isInfoEnabled()) {
//            	logger.info("Controller proceeding spending time is " + sw.getTotalTimeSeconds());
//            }
            logger.info("[AOP]["+ip+"] ["+url+"][END] ["+sw.getTotalTimeSeconds()+" sec] - ["+info_mac+"]["+info_id+"]["+info_app_type+"]");
    		logger.debug("[AOP]["+ip+"] ["+url+"][END] ["+sw.getTotalTimeSeconds()+" sec] -"+debug);
        }
    }
}